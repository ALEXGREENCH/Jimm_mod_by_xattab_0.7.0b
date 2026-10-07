#!/usr/bin/env python3
"""Test maintained src against the original May 12 release; no network login."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys
import zipfile
import recover
from build_source import write_jar

ROOT = recover.ROOT
CACHE = recover.CACHE
TEST = ROOT / 'build/source-tests'
REPORT = ROOT / 'preservation/reports/source-tests.json'


def run(args, name):
    result = subprocess.run([str(a) for a in args], cwd=ROOT, stdout=subprocess.PIPE,
                            stderr=subprocess.STDOUT, timeout=180)
    (TEST / (name + '.log')).write_bytes(result.stdout)
    if result.returncode:
        raise RuntimeError(name + ' failed; see ' + str(TEST / (name + '.log')))
    text = result.stdout.decode('utf-8', errors='replace')
    return '\n'.join(line for line in text.splitlines() if line.startswith('PASS'))


def main(matrix=False, skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    if not skip_build:
        run([sys.executable, ROOT / 'tools/build_source.py'], 'build-MIDP2-RU')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    helpers = list((ROOT / 'tools/source').glob('*.java'))
    helpers = [p for p in helpers if p.name != 'Preprocess.java']
    # MicroEmulator lacks this MMAPI interface; camera tests provide its implementation.
    helpers.append(ROOT / 'tools/api-stubs/javax/microedition/media/control/VideoControl.java')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *helpers], 'compile-tests')
    # No invokedynamic in the fixture classes loaded by MicroEmulator's legacy ASM.
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, *runtime]), '-d', TEST, ROOT / 'tools/source/TransportIO.java', ROOT / 'tools/source/LoginIO.java', ROOT / 'tools/source/MessageIO.java', ROOT / 'tools/source/AboutIO.java', ROOT / 'tools/source/FileTransferIO.java', ROOT / 'tools/source/FileSystemIO.java', ROOT / 'tools/source/CameraIO.java', ROOT / 'tools/source/BlinkIO.java', ROOT / 'tools/source/OptionsIO.java', ROOT / 'tools/source/TimerIO.java', ROOT / 'tools/source/MenuIO.java', ROOT / 'tools/source/ResourceIO.java', ROOT / 'tools/source/EmotionsIO.java', ROOT / 'tools/source/ServerActionIO.java', ROOT / 'tools/source/RunnableIO.java', ROOT / 'tools/source/BirthdayIO.java', ROOT / 'tools/source/SocketIO.java', ROOT / 'tools/source/RequestInfoIO.java', ROOT / 'tools/source/PasswordIO.java', ROOT / 'tools/source/PhoneBookIO.java', ROOT / 'tools/source/SaveInfoIO.java', ROOT / 'tools/source/MagicEyeIO.java'], 'compile-transport-io')
    java = [recover.java(), '-Djava.awt.headless=true',
            '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST, *runtime])]
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    built = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    source = ROOT / 'build/source/MIDP2-RU'
    # Retain methods that ProGuard specializes/removes so reflection can exercise them.
    with zipfile.ZipFile(built) as jar:
        entries = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(source / 'classes.jar') as jar:
        entries.update({n: jar.read(n) for n in jar.namelist()})
    test_jar = TEST / 'unoptimized-with-resources.jar'
    write_jar(test_jar, entries)
    report = {'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built)}
    report['pure_logic'] = run([*java, 'SourceDifferentialTest', original, source / 'classes', CACHE], 'differential')
    report['packets'] = run([sys.executable, ROOT / 'tools/test_packets.py'], 'packets-audit')
    report['convertor'] = run([sys.executable, ROOT / 'tools/test_convertor.py'], 'convertor-audit')
    report['util_core'] = run([sys.executable, ROOT / 'tools/test_util_core.py', '--skip-build'], 'util-core-audit')
    fixtures = TEST / 'capabilities.bin'
    ref_output, src_output = TEST / 'reference.txt', TEST / 'source.txt'
    run([*java, 'DetectorProbe', original, 'reference', fixtures, ref_output], 'detector-reference')
    report['detector'] = run([*java, 'DetectorProbe', test_jar, 'source', fixtures, src_output], 'detector-source')
    if ref_output.read_bytes() != src_output.read_bytes():
        raise AssertionError('Client detection/status mismatch: compare build/source-tests/reference.txt and source.txt')
    report['detector_cases'] = len(ref_output.read_text().splitlines())
    report['detector_differences'] = 0
    popup_ref, popup_src = TEST / 'popup-reference.txt', TEST / 'popup-source.txt'
    run([*java, 'PopupProbe', original, 'reference', popup_ref], 'popup-reference')
    report['popups'] = run([*java, 'PopupProbe', test_jar, 'source', popup_src], 'popup-source')
    if popup_ref.read_bytes() != popup_src.read_bytes():
        raise AssertionError('Popup mismatch: compare build/source-tests/popup-reference.txt and popup-source.txt')
    report['popup_observations'] = len(popup_ref.read_text().splitlines())
    report['popup_differences'] = 0
    network_ref, network_src = TEST / 'network-reference.txt', TEST / 'network-source.txt'
    for path, mode, output in [(original, 'reference', network_ref), (test_jar, 'source', network_src)]:
        fixture = TEST / ('network-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'NetworkFixture', path, fixture, mode], 'network-fixture-' + mode)
        report['network_' + mode] = run([*java, 'NetworkStateProbe', fixture, mode, output], 'network-' + mode)
    if network_ref.read_bytes() != network_src.read_bytes():
        raise AssertionError('Lifecycle mismatch: compare build/source-tests/network-reference.txt and network-source.txt')
    report['network_observations'] = len(network_ref.read_text().splitlines())
    report['network_differences'] = 0
    splash_ref, splash_src = TEST / 'splash-reference.txt', TEST / 'splash-source.txt'
    for path, mode, output in [(original, 'reference', splash_ref), (test_jar, 'source', splash_src)]:
        fixture = TEST / ('splash-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'SplashFixture', path, fixture, mode], 'splash-fixture-' + mode)
        report['splash_' + mode] = run([*java, 'SplashProbe', fixture, mode, output], 'splash-' + mode)
    if splash_ref.read_bytes() != splash_src.read_bytes():
        raise AssertionError('Splash mismatch: compare build/source-tests/splash-reference.txt and splash-source.txt')
    report['splash_observations'] = len(splash_ref.read_text().splitlines())
    report['splash_differences'] = 0
    transport_ref, transport_src = TEST / 'transport-reference.txt', TEST / 'transport-source.txt'
    for path, mode, output in [(original, 'reference', transport_ref), (test_jar, 'source', transport_src)]:
        fixture = TEST / ('transport-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'TransportFixture', path, fixture, mode, TEST], 'transport-fixture-' + mode)
        report['transport_' + mode] = run([*java, 'TransportProbe', fixture, mode, output], 'transport-' + mode)
    if transport_ref.read_bytes() != transport_src.read_bytes():
        raise AssertionError('Transport mismatch: compare build/source-tests/transport-reference.txt and transport-source.txt')
    report['transport_observations'] = len(transport_ref.read_text().splitlines())
    report['transport_differences'] = 0
    socket_outputs = []
    for mode in ['reference', 'source']:
        fixture, output = TEST / ('socket-' + mode + '.jar'), TEST / ('socket-' + mode + '.txt')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SocketFixture',
             TEST / ('network-' + mode + '.jar'), fixture, mode, TEST], 'socket-fixture-' + mode)
        report['socket_' + mode] = run([*java, 'SocketProbe', fixture, mode, output], 'socket-' + mode)
        socket_outputs.append(output.read_bytes())
    if socket_outputs[0] != socket_outputs[1]:
        raise AssertionError('Socket mismatch: compare build/source-tests/socket-{reference,source}.txt')
    report['socket_observations'] = len(socket_outputs[0].splitlines())
    report['socket_differences'] = 0
    guard = socket_outputs[0].decode('utf-8').splitlines()[-1]
    connects, closes, sends, receives = [int(part.split(':')[1]) for part in guard.split('/')]
    socket_report = {'scope': 'Actual MIDP2 SOCKETConnection connect/close/send/run/local-address methods. '
                     'Scripted Connector and streams, fixed initial-sequence clock, captured Thread.start/sleep/yield '
                     'and terminal JimmException delivery. Actual locks, Object.notify, receiver queue, packet '
                     'serialization, sequence assignment and Traffic counters execute. The start boundary does not '
                     'launch a thread; run is invoked directly. Physical scheduling, a live ICQ server, and device '
                     'network APIs are not claimed. Existing NetworkFixture replaces the separate Icq.connect entry point.',
                     'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built),
                     'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                     'observations': report['socket_observations'], 'connect_calls': connects,
                     'close_calls': closes, 'send_calls': sends, 'receiver_calls': receives, 'differences': 0}
    (ROOT / 'preservation/reports/source-socket.json').write_text(
        json.dumps(socket_report, indent=2) + '\n', encoding='utf-8', newline='\n')
    login_ref, login_src = TEST / 'login-reference.txt', TEST / 'login-source.txt'
    for path, mode, output in [(original, 'reference', login_ref), (test_jar, 'source', login_src)]:
        fixture = TEST / ('login-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'LoginFixture', path, fixture, mode, TEST], 'login-fixture-' + mode)
        report['login_' + mode] = run([*java, 'LoginProbe', fixture, mode, output], 'login-' + mode)
    if login_ref.read_bytes() != login_src.read_bytes():
        raise AssertionError('Login/roster mismatch: compare build/source-tests/login-reference.txt and login-source.txt')
    report['login_observations'] = len(login_ref.read_text().splitlines())
    report['login_differences'] = 0
    roster_ref, roster_src = TEST / 'roster-reference.txt', TEST / 'roster-source.txt'
    for mode, output in [('reference', roster_ref), ('source', roster_src)]:
        report['roster_' + mode] = run([*java, 'RosterProbe', TEST / ('login-' + mode + '.jar'), mode, output], 'roster-' + mode)
    if roster_ref.read_bytes() != roster_src.read_bytes():
        raise AssertionError('Roster transaction mismatch: compare build/source-tests/roster-reference.txt and roster-source.txt')
    report['roster_observations'] = len(roster_ref.read_text().splitlines())
    report['roster_differences'] = 0
    message_ref, message_src = TEST / 'message-reference.txt', TEST / 'message-source.txt'
    for mode, output in [('reference', message_ref), ('source', message_src)]:
        fixture = TEST / ('message-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'MessageFixture', TEST / ('login-' + mode + '.jar'), fixture, mode, TEST], 'message-fixture-' + mode)
        report['message_' + mode] = run([*java, 'MessageProbe', fixture, mode, output], 'message-' + mode)
    if message_ref.read_bytes() != message_src.read_bytes():
        raise AssertionError('Incoming message mismatch: compare build/source-tests/message-reference.txt and message-source.txt')
    report['message_observations'] = len(message_ref.read_text().splitlines())
    report['message_differences'] = 0
    clipboard_ref, clipboard_src = TEST / 'clipboard-reference.txt', TEST / 'clipboard-source.txt'
    for mode, output in [('reference', clipboard_ref), ('source', clipboard_src)]:
        fixture = TEST / ('clipboard-' + mode + '.jar')
        # Reuse the fixed date fixture; clipboard and list controllers are unchanged.
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'SplashFixture', TEST / ('login-' + mode + '.jar'), fixture, mode], 'clipboard-fixture-' + mode)
        report['clipboard_' + mode] = run([*java, 'ClipboardProbe', fixture, mode, output], 'clipboard-' + mode)
    if clipboard_ref.read_bytes() != clipboard_src.read_bytes():
        raise AssertionError('Clipboard/MagicEye mismatch: compare build/source-tests/clipboard-reference.txt and clipboard-source.txt')
    report['clipboard_observations'] = len(clipboard_ref.read_text().splitlines())
    report['clipboard_differences'] = 0
    report['jimm_urls'] = run([sys.executable, ROOT / 'tools/test_jimm_urls.py', '--prepared'], 'jimm-urls-audit')
    profile_ref, profile_src = TEST / 'profile-reference.txt', TEST / 'profile-source.txt'
    for mode, output in [('reference', profile_ref), ('source', profile_src)]:
        fixture = TEST / ('profile-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'ProfileFixture', TEST / ('message-' + mode + '.jar'), fixture, mode], 'profile-fixture-' + mode)
        report['profile_' + mode] = run([*java, 'ProfileProbe', fixture, mode, output], 'profile-' + mode)
    if profile_ref.read_bytes() != profile_src.read_bytes():
        raise AssertionError('Profile mismatch: compare build/source-tests/profile-reference.txt and profile-source.txt')
    report['profile_observations'] = len(profile_ref.read_text().splitlines())
    report['profile_differences'] = 0
    about_ref, about_src = TEST / 'about-reference.txt', TEST / 'about-source.txt'
    for mode, output in [('reference', about_ref), ('source', about_src)]:
        fixture = TEST / ('about-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'AboutFixture', TEST / ('login-' + mode + '.jar'), fixture, mode, TEST], 'about-fixture-' + mode)
        report['about_' + mode] = run([*java, 'AboutProbe', fixture, mode, output], 'about-' + mode)
    if about_ref.read_bytes() != about_src.read_bytes():
        raise AssertionError('About/version mismatch: compare build/source-tests/about-reference.txt and about-source.txt')
    report['about_observations'] = len(about_ref.read_text().splitlines())
    report['about_differences'] = 0
    contact_ref, contact_src = TEST / 'contact-menu-reference.txt', TEST / 'contact-menu-source.txt'
    for mode, output in [('reference', contact_ref), ('source', contact_src)]:
        fixture = TEST / ('contact-menu-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'ContactMenuFixture', TEST / ('message-' + mode + '.jar'), fixture, mode], 'contact-menu-fixture-' + mode)
        report['contact_menu_' + mode] = run([*java, 'ContactMenuProbe', fixture, mode, output], 'contact-menu-' + mode)
    if contact_ref.read_bytes() != contact_src.read_bytes():
        raise AssertionError('Contact menu mismatch: compare build/source-tests/contact-menu-reference.txt and contact-menu-source.txt')
    report['contact_menu_observations'] = len(contact_ref.read_text().splitlines())
    report['contact_menu_differences'] = 0
    search_ref, search_src = TEST / 'search-reference.txt', TEST / 'search-source.txt'
    for mode, output in [('reference', search_ref), ('source', search_src)]:
        fixture = TEST / ('search-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'SearchFixture', TEST / ('message-' + mode + '.jar'), fixture, mode], 'search-fixture-' + mode)
        report['search_' + mode] = run([*java, 'SearchProbe', fixture, mode, output], 'search-' + mode)
    if search_ref.read_bytes() != search_src.read_bytes():
        raise AssertionError('Search mismatch: compare build/source-tests/search-reference.txt and search-source.txt')
    report['search_observations'] = len(search_ref.read_text().splitlines())
    report['search_differences'] = 0
    file_ref, file_src = TEST / 'file-transfer-reference.txt', TEST / 'file-transfer-source.txt'
    for mode, output in [('reference', file_ref), ('source', file_src)]:
        fixture = TEST / ('file-transfer-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'FileTransferFixture', TEST / ('message-' + mode + '.jar'), fixture, mode, TEST], 'file-transfer-fixture-' + mode)
        report['file_transfer_' + mode] = run([*java, 'FileTransferProbe', fixture, mode, output], 'file-transfer-' + mode)
    if file_ref.read_bytes() != file_src.read_bytes():
        raise AssertionError('File transfer mismatch: compare build/source-tests/file-transfer-reference.txt and file-transfer-source.txt')
    report['file_transfer_observations'] = len(file_ref.read_text().splitlines())
    report['file_transfer_differences'] = 0
    for prefix, fixture_class, probe in [('magic_eye', 'MagicEyeFixture', 'MagicEyeProbe'), ('save_info', 'SaveInfoFixture', 'SaveInfoProbe'), ('phone_book', 'PhoneBookFixture', 'PhoneBookProbe'), ('password', 'PasswordFixture', 'PasswordProbe'), ('birthday', 'BirthdayFixture', 'BirthdayProbe'), ('request_info', 'RequestInfoFixture', 'RequestInfoProbe'), ('xtraz', 'XtrazFixture', 'XtrazProbe'), ('runnable', 'RunnableFixture', 'RunnableProbe'), ('server_actions', 'ServerActionFixture', 'ServerActionProbe'), ('camera', 'CameraFixture', 'CameraProbe'), ('direct', 'DirectFixture', 'DirectProbe'), ('outgoing', 'OutgoingFixture', 'OutgoingProbe'), ('traffic', 'TrafficFixture', 'TrafficProbe'), ('xstatus', 'XStatusFixture', 'XStatusProbe'), ('chat', 'ChatFixture', 'ChatProbe'), ('history', 'HistoryFixture', 'HistoryProbe'), ('blink', 'BlinkFixture', 'BlinkProbe'), ('options', 'OptionsFixture', 'OptionsProbe'), ('timer', 'TimerFixture', 'TimerProbe'), ('menu', 'MenuFixture', 'MenuProbe'), ('vlist', 'VirtualListFixture', 'VirtualListProbe'), ('list_menu', 'ListMenuFixture', 'ListMenuProbe'), ('list_menu_italic', 'ListMenuFixture', 'ListMenuProbe'), ('contact_tree', 'ContactTreeFixture', 'ContactTreeProbe'), ('tree', 'VirtualListFixture', 'TreeProbe'), ('form', 'FormFixture', 'FormProbe'), ('text_list', 'VirtualListFixture', 'TextListProbe'), ('alert', 'VirtualListFixture', 'AlertProbe'), ('templates', 'TemplatesFixture', 'TemplatesProbe'), ('templates_smart', 'TemplatesFixture', 'TemplatesProbe'), ('selector', 'SelectorFixture', 'SelectorProbe'), ('file_browser', 'VirtualListFixture', 'FileBrowserProbe'), ('emotions', 'EmotionsFixture', 'EmotionsProbe')]:
        reference_output, source_output = TEST / (prefix + '-reference.txt'), TEST / (prefix + '-source.txt')
        for mode, output in [('reference', reference_output), ('source', source_output)]:
            fixture = TEST / (prefix + '-' + mode + '.jar')
            extra = [original if mode == 'reference' else test_jar] if prefix in ['runnable', 'chat', 'options', 'list_menu', 'list_menu_italic', 'contact_tree'] else []
            if prefix.startswith('list_menu'): extra.append('2' if prefix.endswith('italic') else '0')
            parent = 'birthday' if prefix == 'request_info' else 'file-transfer'
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), fixture_class,
                 TEST / (parent + '-' + mode + '.jar'), fixture, mode, TEST, *extra], prefix + '-fixture-' + mode)
            report[prefix + '_' + mode] = run([*java, *(['-Djimm.templates.smart=true'] if prefix == 'templates_smart' else []), probe, fixture, mode, output], prefix + '-' + mode)
        if reference_output.read_bytes() != source_output.read_bytes():
            raise AssertionError(prefix + ' mismatch: compare build/source-tests/' + prefix + '-{reference,source}.txt')
        report[prefix + '_observations'] = len(reference_output.read_text().splitlines())
        report[prefix + '_differences'] = 0

        if prefix == 'magic_eye':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            additions, activations, commands, keys, destinations, rasters = [int(part.split(':')[1]) for part in guard.split('/')]
            magic_report = {'scope': 'Actual MIDP2 MagicEye constructor, singleton/listener split, synchronized journal, '
                            'TextList wrapping/tags/format and full-frame raster, contact lookup, command identity, '
                            'menus, clipboard, counter overflow, partial failures and ignored key-event types. '
                            'The inherited file-transfer and virtual-list host fixtures remain. Only three terminal '
                            'journal routing calls and default Date NEW/constructor in Util.createCurrentDate '
                            'are additionally captured, with guarded site counts. Date formatting, ResourceBundle, '
                            'contact creation, clipboard and TextList algorithms remain real. The user_menu action '
                            'uses literal Y1 in both restored native RU dictionaries; unknown keys are literal. '
                            'Original/unoptimized authoring JARs only; no whole optimized JAR, physical scheduler '
                            'or complete terminal destination side-effect claim.',
                            'reference_sha256': recover.sha(original),
                            'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                            'observations': report[prefix + '_observations'], 'journal_calls': additions,
                            'activation_calls': activations, 'command_calls': commands, 'key_calls': keys,
                            'destination_attempts': destinations, 'raster_frames': rasters, 'differences': 0}
            (ROOT / 'preservation/reports/source-magic-eye.json').write_text(
                json.dumps(magic_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'save_info':
            optimized_fixture = TEST / 'save-info-optimized-source.jar'
            optimized_output = TEST / 'save-info-optimized-source.txt'
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SaveInfoFixture',
                 TEST / 'file-transfer-source.jar', optimized_fixture, 'source', TEST, built], 'save-info-optimized-fixture')
            report['save_info_optimized'] = run([*java, 'SaveInfoProbe', optimized_fixture, 'source', optimized_output], 'save-info-optimized')
            if optimized_output.read_bytes() != reference_output.read_bytes():
                raise AssertionError('Whole delivered optimized SaveInfo differs from reference/unoptimized source')
            report['save_info_optimized_observations'] = len(optimized_output.read_text().splitlines())
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            initializations, forwards, accepted, sends, send_returns, callbacks = [int(part.split(':')[1]) for part in guard.split('/')]
            save_report = {'scope': 'Actual MIDP2 SaveInfoAction constructor, referenced profile array/UTF16 content, '
                           'TLV payload construction, real selected-account getters and packet serializers, '
                           'native FromIcqSrvPacket replies, repeated init/forward, partial state, counters/progress, '
                           'timeout overflow and completion callbacks. Original and unoptimized controllers use '
                           'the inherited file-transfer host fixture. The third run starts from the whole delivered '
                           'optimized source JAR and its actual optimized helpers/constructors; it adds only the same '
                           'scripted connection boundary and blocks live login. Four action sites are captured: '
                           'default Date NEW/constructor, currentTimeMillis and ContactList.activate. '
                           'Connection records the actual packet and may throw JimmException or be a null receiver. '
                           'Packet serialization then executes separately on that recorded object and serialization '
                           'exceptions are compared literally; successful send returns count acceptance by this '
                           'scripted connection, not successful physical transport. Profile arrays use length-prefixed '
                           'UTF16-unit SHA256 including null distinctions; packet bytes are compared directly. '
                           'No physical network, scheduler or complete ContactList.activate side-effect claim.',
                           'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built),
                           'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                           'observations': report[prefix + '_observations'], 'optimized_observations': report['save_info_optimized_observations'],
                           'initializations_per_run': initializations, 'forward_calls_per_run': forwards,
                           'accepted_responses_per_run': accepted, 'send_attempts_per_run': sends,
                           'successful_scripted_send_returns_per_run': send_returns,
                           'completion_notification_attempts_per_run': callbacks, 'differences': 0}
            (ROOT / 'preservation/reports/source-save-info.json').write_text(
                json.dumps(save_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'phone_book':
            optimized_fixture = TEST / 'phone-book-optimized-source.jar'
            optimized_output = TEST / 'phone-book-optimized-source.txt'
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'PhoneBookFixture',
                 TEST / 'file-transfer-source.jar', optimized_fixture, 'source', TEST, built], 'phone-book-optimized-fixture')
            report['phone_book_optimized'] = run([*java, 'PhoneBookProbe', optimized_fixture, 'source', optimized_output], 'phone-book-optimized')
            if optimized_output.read_bytes() != reference_output.read_bytes():
                raise AssertionError('Delivered optimized PhoneBook differs from reference/unoptimized source')
            report['phone_book_optimized_observations'] = len(optimized_output.read_text().splitlines())
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            activations, commands, destinations = [int(part.split(':')[1]) for part in guard.split('/')]
            def controller_sha(path, name):
                with zipfile.ZipFile(path) as jar:
                    return hashlib.sha256(jar.read(name + '.class')).hexdigest()
            phone_report = {'scope': 'Actual MIDP2 PhoneBook activation/singleton, native TextBox fields, title/content/size/constraints, '
                            'commands and listener, exact command identity, repeated forms/old SMS state and send/call exception paths. '
                            'Reference and unoptimized authored controller execute against the inherited file-transfer host fixture. '
                            'A second run substitutes only the delivered optimized source PhoneBook class in that source host fixture; '
                            'its observations must also match. Only six PhoneBook call sites to Display.setCurrent, '
                            'MIDlet.platformRequest, Connector.open and MainMenu.activate are captured (2/1/1/2). '
                            'SMS connection/messages are scripted implementations of the real JSR120 interfaces, with actual '
                            'casts, method dispatch/order and controller catches. IOException/runtime/Error failures, null/wrong '
                            'types, null native fields/receivers, command aliases/null and native text validation are covered. '
                            'Existing file-transfer boundaries remain. No physical call/SMS, display or complete main-menu effect claim.',
                            'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built),
                            'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                            'reference_controller_sha256': controller_sha(original, 'bd'),
                            'source_unoptimized_controller_sha256': controller_sha(source / 'classes.jar', 'jimm/util/PhoneBook'),
                            'source_optimized_controller_sha256': controller_sha(built, 'jimm/util/PhoneBook'),
                            'observations': report[prefix + '_observations'], 'optimized_observations': report['phone_book_optimized_observations'],
                            'activation_calls_per_run': activations, 'command_calls_per_run': commands, 'destination_attempts_per_run': destinations,
                            'differences': 0}
            (ROOT / 'preservation/reports/source-phone-book.json').write_text(
                json.dumps(phone_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'password':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            activations, commands, destinations = [int(part.split(':')[1]) for part in guard.split('/')]
            password_report = {'scope': 'Actual MIDP2 EnterPassword constructor, activation, singleton previous screen, '
                               'exact command identity, native TextBox title/content/max length/PASSWORD constraints, '
                               'command list and listener, selected settings, password-protection and retry/pound state. '
                               'Real Option getters/setters and Icq connection predicates execute; corrupted null setting '
                               'is assigned to the real private table after separately checking setString(null) rejection. '
                               'Only EnterPassword call sites to Display.setCurrent, light flash, ContactList activate/beforeConnect, '
                               'Icq.connect, SplashCanvas.unlock and Jimm.destroyApp are captured, including scripted failures '
                               'and native null-receiver failure. Existing file-transfer fixture boundaries remain. '
                               'No full downstream callback, physical display/lighting, destruction or network login claim.',
                               'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built),
                               'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                               'observations': report[prefix + '_observations'], 'activation_calls': activations,
                               'command_calls': commands, 'destination_attempts': destinations, 'differences': 0}
            (ROOT / 'preservation/reports/source-password.json').write_text(
                json.dumps(password_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'request_info':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            initializations, forwards, accepted, deliveries = [int(part.split(':')[1]) for part in guard.split('/')]
            request_report = {'scope': 'Actual MIDP2 RequestInfoAction initialization, response parsing, '
                              'partial fields, completion/timeout state and repeated delivery. Real contact '
                              'rename/save, UpdateContactListAction construction, birthday tables/RMS and '
                              'request serialization execute. Clock and UI/contact-list/chat notification '
                              'destinations are captured; inherited birthday/file-transfer fixture boundaries '
                              'remain. Only category fields 40-43 are translated through each real ResourceBundle; '
                              'all other fields retain literal UTF-16 data, including overflow into field 44. '
                              'No live server, physical event queue or complete downstream notification claim.',
                              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built),
                              'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                              'observations': report[prefix + '_observations'], 'initializations': initializations,
                              'forward_calls': forwards, 'accepted_responses': accepted,
                              'delivery_attempts': deliveries, 'differences': 0}
            (ROOT / 'preservation/reports/source-request-info.json').write_text(
                json.dumps(request_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'birthday':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            dates, loads, saves, adds = [int(part.split(':')[1]) for part in guard.split('/')]
            birthday_report = {'scope': 'Actual MIDP2 NoticeOnBirthDay and Util calendar algorithms. '
                               'Real contact fields, Vector state, in-memory MicroEmulator RMS records, '
                               'partial/corrupt reads, writes and historical errors. Only default Date '
                               'construction in createCurrentDate and birthday Thread.start/sleep are captured. '
                               'The actual worker runs synchronously at the launch boundary; physical scheduling '
                               'and waiting are not claimed. Existing file-transfer fixture boundaries remain. '
                               'Calendar behavior is compared with the original, including historical quirks; '
                               'no Gregorian-calendar correction or physical-device RMS guarantee.',
                               'reference_sha256': recover.sha(original),
                               'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                               'observations': report[prefix + '_observations'], 'date_checks': dates,
                               'load_calls': loads, 'save_calls': saves, 'contact_add_calls': adds, 'differences': 0}
            (ROOT / 'preservation/reports/source-birthday.json').write_text(
                json.dumps(birthday_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'xtraz':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            sends, texts, chats, logs = [int(part.split(':')[1]) for part in guard.split('/')]
            xtraz_report = {'scope': 'Actual MIDP2 XtrazSM serializers and Util XML helpers. '
                            'Queries use transaction ID zero, as all actual callers do and the optimized '
                            'reference specializes. Real ActionListener parses incoming requests/responses '
                            'and retains privacy gates, response construction, open-chat state and failures. '
                            'Only the Xtraz clock, new-chat destination and MagicEye log destination are '
                            'additionally captured; existing file-transfer fixture I/O/chat/UI boundaries remain. '
                            'Log resource keys are normalized through each real ResourceBundle dictionary. '
                            'No live server, native queue or complete downstream chat/log side-effect claim.',
                            'reference_sha256': recover.sha(original),
                            'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                            'observations': report[prefix + '_observations'], 'packet_send_attempts': sends,
                            'chat_appends': texts, 'new_chat_calls': chats, 'log_calls': logs, 'differences': 0}
            (ROOT / 'preservation/reports/source-xtraz.json').write_text(
                json.dumps(xtraz_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'runnable':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            callbacks, queued = [int(part.split(':')[1]) for part in guard.split('/')]
            runnable_report = {'scope': 'Actual MIDP2 RunnableImpl restored from the untouched class JAR '
                               'before instrumentation. Real dispatch, argument boxing, array aliases, '
                               'queue wrappers, message filtering and reconnect decisions. '
                               'Only Display.callSerially, System.gc, Thread.sleep and 15 downstream '
                               'UI/network callbacks are captured here; actual Display.getDisplay is retained. '
                               'Spam cases include both scripted predicate outcomes and the real ActionListener '
                               'predicate. Presence packets reach the real ActionListener and real queued task. '
                               'No native event-queue implementation, physical waiting or complete callback '
                               'side-effect equivalence claim.',
                               'reference_sha256': recover.sha(original),
                               'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                               'observations': report[prefix + '_observations'],
                               'downstream_callbacks': callbacks, 'queue_attempts': queued, 'differences': 0}
            (ROOT / 'preservation/reports/source-runnable.json').write_text(
                json.dumps(runnable_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix == 'server_actions':
            guard = reference_output.read_text(encoding='utf-8').splitlines()[-1]
            sends, acks = [int(part.split(':')[1]) for part in guard.split('/')]
            action_report = {'scope': 'Actual MIDP2 ServerListsAction/RemoveMeAction/SysNoticeAction/SystemNotice. '
                            'Real constructors, action state, contact IDs, selected account, serializers and error paths. '
                            'Existing file-transfer fixture connection/UI boundaries are retained. '
                            'Only action clock reads/default Date construction and ContactList.contactChanged are captured here; '
                            'Util date conversion and roster-ID generation algorithms remain real with a fixed clock. '
                            'No network login, physical timing or complete protocol-equivalence claim.',
                             'reference_sha256': recover.sha(original),
                             'source_unoptimized_class_jar_sha256': recover.sha(source / 'classes.jar'),
                             'observations': report[prefix + '_observations'],
                             'successful_sends': sends, 'accepted_acknowledgments': acks, 'differences': 0}
            (ROOT / 'preservation/reports/source-server-actions.json').write_text(
                json.dumps(action_report, indent=2) + '\n', encoding='utf-8', newline='\n')
        if prefix in ['vlist', 'list_menu', 'list_menu_italic', 'tree', 'form', 'text_list', 'alert', 'templates', 'templates_smart', 'selector', 'file_browser', 'emotions']:
            report[prefix + '_raster_frames'] = int(reference_output.read_text().splitlines()[-1].split(':')[1])
    report['ui'] = run([*java, 'SourceSmokeTest', built], 'ui')
    report['limitations'] = ['MicroEmulator does not play all original sound formats.',
                            'No live ICQ login, real-device or complete bytecode-equivalence claim.']
    if matrix:
        report['builds'] = []
        for target in ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
            for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
                if (target, language) != ('MIDP2', 'RU'):
                    run([sys.executable, ROOT / 'tools/build_source.py', '--target', target,
                         '--language', language], 'build-' + target + '-' + language)
                jar = ROOT / 'dist/source' / ('Jimm-' + target + '-' + language + '.jar')
                with zipfile.ZipFile(jar) as z:
                    assert language + '.lng' in z.namelist()
                    assert all(n in z.namelist() for n in ['forms.png', 'groups.png', 'fs.png', 'smiles.txt'])
                    assert not any(n.startswith(('javax/', 'com/')) and n.endswith('.class') for n in z.namelist())
                    # The May releases ship the static 22x22 pack with animation support still compiled in.
                    assert 'smiles/animate.bin' not in z.namelist()
                    with zipfile.ZipFile(ROOT / 'preservation/wayback-originals' / ('Jimm_' + {'MIDP2': 'MIDP2', 'MOTOROLA': 'Moto', 'SIEMENS2': 'Siemens2'}[target] + '_' + language) / 'Jimm.jar') as reference:
                        assert all(z.read(n) == reference.read(n) for n in ['smiles.png', 'smiles.txt'])
                report['builds'].append({'target': target, 'language': language, 'sha256': recover.sha(jar)})
                print('PASS build:', target, language, flush=True)
        for target in ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target, '--language', 'EN',
                 '--modules', '', '--compile-only'], 'minimal-' + target)
        report['minimal_modules'] = ['MIDP2', 'MOTOROLA', 'SIEMENS2']
        report['resources'] = run([sys.executable, ROOT / 'tools/audit_resources.py'], 'resource-audit')
        report['language_loader'] = run([sys.executable, ROOT / 'tools/test_languages.py'], 'language-loader-audit')
        report['graphics'] = run([sys.executable, ROOT / 'tools/test_graphics.py', '--skip-build'], 'graphics-audit')
        report['filesystems'] = run([sys.executable, ROOT / 'tools/test_filesystems.py', '--skip-build'], 'filesystems-audit')
        report['light'] = run([sys.executable, ROOT / 'tools/test_light.py', '--skip-build'], 'light-audit')
        report['key_routing'] = run([sys.executable, ROOT / 'tools/test_key_routing.py', '--skip-build'], 'key-routing-audit')
        report['socket_bytecode'] = run([sys.executable, ROOT / 'tools/audit_socket.py'], 'socket-bytecode-audit')
        report['phone_book_bytecode'] = run([sys.executable, ROOT / 'tools/audit_phone_book.py'], 'phone-book-bytecode')
    report['request_info_categories'] = run([sys.executable, ROOT / 'tools/test_request_info_categories.py',
                                            *(['--all-languages'] if matrix else []), '--skip-build'],
                                           'request-info-category-audit')
    report['xstatus_catalog'] = run([sys.executable, ROOT / 'tools/test_xstatus_catalog.py',
                                   *(['--all-languages'] if matrix else []), '--skip-build'],
                                  'xstatus-catalog-audit')
    report['save_info_locales'] = run([sys.executable, ROOT / 'tools/test_save_info_locales.py',
                                      *(['--all-languages'] if matrix else []), '--skip-build'],
                                     'save-info-locales-audit')
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--matrix', action='store_true', help='Build all 15 combinations plus three minimal configurations')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.matrix, args.skip_build)
