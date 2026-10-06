#!/usr/bin/env python3
"""Test maintained src against the original May 12 release; no network login."""
import argparse
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
         '-cp', recover.cp([TEST, *runtime]), '-d', TEST, ROOT / 'tools/source/TransportIO.java', ROOT / 'tools/source/LoginIO.java', ROOT / 'tools/source/MessageIO.java', ROOT / 'tools/source/AboutIO.java', ROOT / 'tools/source/FileTransferIO.java', ROOT / 'tools/source/CameraIO.java'], 'compile-transport-io')
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
    for prefix, fixture_class, probe in [('camera', 'CameraFixture', 'CameraProbe'), ('direct', 'DirectFixture', 'DirectProbe'), ('outgoing', 'OutgoingFixture', 'OutgoingProbe'), ('traffic', 'TrafficFixture', 'TrafficProbe'), ('xstatus', 'XStatusFixture', 'XStatusProbe'), ('chat', 'ChatFixture', 'ChatProbe'), ('history', 'HistoryFixture', 'HistoryProbe')]:
        reference_output, source_output = TEST / (prefix + '-reference.txt'), TEST / (prefix + '-source.txt')
        for mode, output in [('reference', reference_output), ('source', source_output)]:
            fixture = TEST / (prefix + '-' + mode + '.jar')
            extra = [original if mode == 'reference' else test_jar] if prefix == 'chat' else []
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), fixture_class,
                 TEST / ('file-transfer-' + mode + '.jar'), fixture, mode, TEST, *extra], prefix + '-fixture-' + mode)
            report[prefix + '_' + mode] = run([*java, probe, fixture, mode, output], prefix + '-' + mode)
        if reference_output.read_bytes() != source_output.read_bytes():
            raise AssertionError(prefix + ' mismatch: compare build/source-tests/' + prefix + '-{reference,source}.txt')
        report[prefix + '_observations'] = len(reference_output.read_text().splitlines())
        report[prefix + '_differences'] = 0
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
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--matrix', action='store_true', help='Build all 15 combinations plus three minimal configurations')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.matrix, args.skip_build)
