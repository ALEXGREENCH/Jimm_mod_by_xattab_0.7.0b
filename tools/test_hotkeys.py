#!/usr/bin/env python3
"""Verify actual action dispatch, simple/double hotkeys and backlight state on all RU platforms."""
import argparse
import json
import os
import sys
import zipfile
import audit_source as audit
import recover
from test_source import ROOT, TEST, CACHE, run
from audit_send_text import classes


def prepare():
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp',
         recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in
           ['HotKeyIO', 'HotKeyFixture', 'HotKeyProbe']]], 'compile-hotkeys')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7',
         '-cp', recover.cp(runtime), '-d', TEST, ROOT / 'tools/source/HotKeyIO.java'], 'compile-hotkey-io')
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-hotkey-dump')
    return runtime, cp


def configuration(target, mode, data):
    ref = mode == 'reference'
    native = {'MIDP2': ('cf', 'z', 'cj', 'cd', 'bj', 'ag', 'ah', 'bt', 'ba', 'cv', 'b'),
              'MOTOROLA': ('cd', 'z', 'ci', 'cb', 'bi', 'ag', 'ah', 'bs', 'ba', 'ct', 'b'),
              'SIEMENS2': ('cd', 'y', 'ch', 'cb', 'bh', 'af', 'ag', 'br', 'ay', 'ct', 'c')}[target]
    if ref:
        ui, contact, options, vl, xstatus, menu, magic, chat, xtraz, splash, selected = native
        spec = {'ui': ui, 'contact': contact, 'options': options, 'vl': vl, 'xstatus': xstatus,
                'light': '' if target == 'SIEMENS2' else 'aj', 'jimm': 'jimm.Jimm', 'io': 'HotKeyIO',
                'action': 'a', 'single': 'a', 'double': 'a', 'lock': 'a', 'contactMenu': selected,
                'zero': 'a', 'lightOn': 'a', 'readXtraz': 'a', 'openChat': 'c', 'xstatusField': 'a',
                'setStatus': 'a', 'optionTable': 'a', 'setBoolean': 'a', 'getBoolean': 'a', 'setInt': 'a',
                'setString': 'a', 'setContactString': 'a', 'midletField': 'a', 'historyMethod': 'b'}
    else:
        ui, contact, options, vl, xstatus = 'jimm/JimmUI', 'jimm/ContactItem', 'jimm/Options', 'DrawControls/VirtualList', 'jimm/comm/XStatus'
        menu, magic, chat, xtraz, splash = 'jimm/MainMenu', 'jimm/util/MagicEye', 'jimm/ChatHistory', 'jimm/comm/XtrazSM', 'jimm/SplashCanvas'
        spec = {'ui': ui, 'contact': contact, 'options': options, 'vl': vl, 'xstatus': xstatus,
                'light': '' if target == 'SIEMENS2' else 'DrawControls/LightControl', 'jimm': 'jimm.Jimm', 'io': 'HotKeyIO',
                'action': 'execHotKeyAction', 'single': 'execHotKey', 'double': 'execDoubleHotKey',
                'lock': 'lockPressedTime', 'contactMenu': 'clciContactMenu', 'zero': 'zeroWasPressed',
                'lightOn': 'lightOn', 'readXtraz': 'readXtraz', 'openChat': 'openChat', 'xstatusField': 'xstatus',
                'setStatus': 'setStatusIndex', 'optionTable': 'options', 'setBoolean': 'setBoolean', 'getBoolean': 'getBoolean',
                'setInt': 'setInt', 'setString': 'setString', 'setContactString': 'setStringValue', 'midletField': 'jimm', 'historyMethod': 'showHistory'}
    if target == 'SIEMENS2':
        spec.update(menuURL='b' if ref else 'strMenuCall', lightURL='c' if ref else 'strLightCall')
    spec['press'] = '3' if target == 'MOTOROLA' else '1'
    lookup = {c['name']: c for c in data}
    dispatcher = next(m for m in lookup[ui]['methods'] if (m['name'], m['desc']) == (spec['action'], '(IL' + contact + ';I)V'))
    mappings = {}
    def add(call, name, desc, opcode=184):
        assert str(opcode) + ' ' + call not in mappings
        mappings[str(opcode) + ' ' + call] = ('HotKeyIO.' + name + desc)
    def native_or_source(short, long): return short if ref else long
    add('java/lang/System.currentTimeMillis()J', 'time', '()J')
    add(ui + '.' + native_or_source('a', 'requiestUserInfo') + '(Ljava/lang/String;Ljava/lang/String;)V', 'info', '(Ljava/lang/String;Ljava/lang/String;)V')
    add(ui + '.' + native_or_source('a', 'writeMessage') + '(L' + contact + ';Ljava/lang/String;)V', 'write', '(Ljava/lang/Object;Ljava/lang/String;)V')
    add(ui + '.' + native_or_source('c', 'showClientInfo') + '(L' + contact + ';)V', 'client', '(Ljava/lang/Object;)V')
    if ref:
        wrapper = next(m for m in lookup[contact]['methods'] if (m['name'], m['desc']) == ('b', '()V'))
        assert wrapper['code'][:5] == ['25 0', '3', '182 ' + contact + '.a(I)Ljava/lang/String;', '25 0', '180 ' + contact + '.a Ljava/lang/String;']
        assert len(wrapper['code']) == 7 and wrapper['code'][5].startswith('184 ') and wrapper['code'][-1] == '177'
        add(wrapper['code'][5][4:], 'history', '(Ljava/lang/String;Ljava/lang/String;)V')
    else:
        add('jimm/HistoryStorage.showHistoryList(Ljava/lang/String;Ljava/lang/String;)V', 'history', '(Ljava/lang/String;Ljava/lang/String;)V')
    add(options + '.' + native_or_source('d', 'safe_save') + '()V', 'save', '()V')
    add(options + '.' + native_or_source('e', 'editOptions') + '()V', 'options', '()V')
    add(menu + '.' + native_or_source('b', 'activate') + '()V', 'menu', '()V')
    add('jimm/Jimm.' + native_or_source('a', 'setMinimized') + '(Z)V', 'minimize', '(Z)V')
    add('m.a(Z)V' if ref else 'jimm/ContactList.optionsChanged(ZZ)V', 'changed' if ref else 'changedTwo', '(Z)V' if ref else '(ZZ)V')
    add('m.a()V' if ref else 'jimm/ContactList.activate()V', 'activate', '()V')
    add('m.a(Z)Z' if ref else 'jimm/ContactList.changeSoundMode(Z)Z', 'sound', '(Z)Z')
    add(magic + '.' + native_or_source('a', 'activate') + '()V', 'magic', '()V')
    add(chat + '.' + native_or_source('a', 'chatHistoryDelete') + '(Ljava/lang/String;I)V', 'delete', '(Ljava/lang/String;I)V')
    add(xtraz + '.a(Ljava/lang/String;I)V', 'xtraz', '(Ljava/lang/String;I)V')
    add('bd.a()V' if ref else 'jimm/util/PhoneBook.activate()V', 'phone', '()V')
    add(splash + '.' + native_or_source('n', 'lockScreen') + '()V', 'lock', '()V')
    add('javax/microedition/midlet/MIDlet.platformRequest(Ljava/lang/String;)Z', 'platform', '(Ljava/lang/Object;Ljava/lang/String;)Z', 182)
    add('jimm/Jimm.platformRequest(Ljava/lang/String;)Z', 'platform', '(Ljava/lang/Object;Ljava/lang/String;)Z', 182)
    add('com/nokia/mid/ui/DeviceControl.setLights(II)V', 'lights', '(II)V')
    add('javax/microedition/lcdui/Display.flashBacklight(I)Z', 'flash', '(Ljavax/microedition/lcdui/Display;I)Z', 182)
    if ref:
        add(ui + '.a$1385ff()V', 'colors', '()V')
    else:
        add('jimm/JimmUI.setColorScheme(Z)V', 'colorsArg', '(Z)V')
        add('DrawControls/VirtualList.setFullScreenForCurrent(Z)V', 'oldFullscreen', '(Z)V')
        for m in lookup[ui]['methods']:
            if m['name'].startswith('setColorScheme') and m['desc'] == '()V':
                add(ui + '.' + m['name'] + m['desc'], 'colors', '()V')
        for m in lookup['jimm/ContactList']['methods']:
            if m['name'].startswith('optionsChanged') and m['desc'] == '(Z)V':
                add('jimm/ContactList.' + m['name'] + m['desc'], 'changed', '(Z)V')
        for c in data:
            for m in c['methods']:
                call = '184 ' + c['name'] + '.' + m['name'] + m['desc']
                if c['name'] == 'DrawControls/Icon' and m['name'].startswith('a$') and m['desc'] == '(Ljava/lang/String;)V' and call in dispatcher['code']:
                    marker = '18 String:<srv><id>cAwaySrv</id><req><id>AwayStat</id><trans>'
                    at = m['code'].index(marker)
                    assert m['code'][at + 1:at + 5] == ['184 jimm/comm/Util.MangleXml(Ljava/lang/String;)Ljava/lang/String;',
                                                        '182 java/lang/StringBuffer.append(Ljava/lang/String;)Ljava/lang/StringBuffer;',
                                                        '3', '182 java/lang/StringBuffer.append(I)Ljava/lang/StringBuffer;'], 'Unproven Xtraz constant specialization'
                    add(call[4:], 'xtrazOnly', '(Ljava/lang/String;)V')
    return spec, mappings, dispatcher


def execute(target, mode, artifact, runtime, cp):
    data = classes(artifact, cp)
    spec, mapping, dispatcher = configuration(target, mode, data)
    tag = 'hotkeys-' + target + '-' + mode
    settings, calls_file = TEST / (tag + '.properties'), TEST / (tag + '.tsv')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    calls_file.write_text(''.join(call.split(' ', 1)[0] + '\t' + call.split(' ', 1)[1] + '\t' + dest.split('.', 1)[1].split('(', 1)[0] + '\t(' + dest.split('(', 1)[1] + '\n'
                                 for call, dest in mapping.items()), encoding='utf-8', newline='\n')
    fixture, output = TEST / (tag + '.jar'), TEST / (tag + '.txt')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'HotKeyFixture', artifact,
         fixture, TEST, settings, calls_file], tag + '-fixture')
    after = {c['name']: c for c in classes(fixture, cp)}
    capture_counts = {}
    for owner in data:
        before = owner
        changed = after[owner['name']]
        assert {k: v for k, v in before.items() if k != 'methods'} == {k: v for k, v in changed.items() if k != 'methods'}
        assert len(before['methods']) == len(changed['methods'])
        for left, right in zip(before['methods'], changed['methods']):
            assert {k: v for k, v in left.items() if k not in ('code', 'refs')} == {k: v for k, v in right.items() if k not in ('code', 'refs')}
            allow = (owner['name'] == spec['ui'] and left['name'] == spec['action'] and left['desc'].startswith('(IL')) or (owner['name'] == spec['contact'] and left['name'] == spec['historyMethod'] and left['desc'] == '()V') or owner['name'] == spec['light']
            expected = []
            for instruction in left['code']:
                if allow and instruction in mapping:
                    capture_counts[instruction] = capture_counts.get(instruction, 0) + 1
                    instruction = '184 ' + mapping[instruction]
                expected.append(instruction)
            assert expected == right['code'], ('Unexpected host rewrite', owner['name'], left['name'], left['desc'])
            assert right['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
    assert capture_counts.get('184 java/lang/System.currentTimeMillis()J') == 2
    if mode != 'reference':
        raw = {c['name']: c for c in classes(ROOT / 'build/source' / (target + '-RU') / 'classes.jar', cp)}
        colors = next(m for m in raw['jimm/JimmUI']['methods'] if m['name'] == 'setColorScheme' and m['desc'] == '(Z)V')
        assert '21 0' not in colors['code'], 'Color flag must be unused before capture'
        changed = next(m for m in raw['jimm/ContactList']['methods'] if m['name'] == 'optionsChanged' and m['desc'] == '(ZZ)V')
        assert '21 1' not in changed['code'], 'Second changed flag must be unused before capture'
        raw_action = next(m for m in raw['jimm/JimmUI']['methods'] if m['name'] == 'execHotKeyAction')
        xtraz_call = '184 jimm/comm/XtrazSM.a(Ljava/lang/String;I)V'
        at = raw_action['code'].index(xtraz_call)
        assert raw_action['code'][at - 1] == '3', 'Only constant-zero Xtraz calls are represented by the optimized capture'
    run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
         '-cp', recover.cp([TEST, *runtime]), 'HotKeyProbe', fixture, output, settings], tag)
    return output.read_bytes(), {'mode': mode, 'input_sha256': recover.sha(artifact),
                                'fixture_sha256': recover.sha(fixture), 'captures': capture_counts,
                                'all_original_class_declarations_and_method_metadata_retained': True,
                                'only_enumerated_call_sites_changed': True,
                                'ui_static_initializer_and_both_facades_unchanged': True}


def main(targets=None, negative=False):
    runtime, cp = prepare()
    builds = []
    for target in targets or ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
        platform = {'MIDP2': 'MIDP2', 'MOTOROLA': 'Moto', 'SIEMENS2': 'Siemens2'}[target]
        original = ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        artifact = ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        # Restore only the resources stripped from the authored class archive, without touching a class.
        source_host = TEST / ('hotkeys-' + target + '-authored-resources.jar')
        with zipfile.ZipFile(artifact) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        from build_source import write_jar
        write_jar(source_host, entries)
        outputs, proofs = [], []
        modes = [('reference', original), ('source', source_host)]
        if not negative: modes.append(('optimized-source', artifact))
        for mode, input_path in modes:
            output, proof = execute(target, mode, input_path, runtime, cp)
            outputs.append(output); proofs.append(proof)
        assert all(s == outputs[0] for s in outputs), 'Hotkey behavior differs on ' + target
        counts = list(map(int, outputs[0].decode('utf-8').splitlines()[-1].split(':')[1:]))
        assert counts[:4] == [10164, 4999, 2280, 2885], counts
        builds.append({'target': target, 'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
                       'source_unoptimized_class_jar_sha256': recover.sha(raw), 'observations': len(outputs[0].splitlines()),
                       'calls': counts[0], 'action_calls': counts[1], 'single_calls': counts[2], 'double_calls': counts[3],
                       'terminal_events': counts[4], 'device_light_calls': counts[5], 'screen_locks': counts[6],
                       'differences': 0, 'fixture_proofs': proofs})
    report = {'scope': 'Actual JimmUI.execHotKeyAction, execHotKey and execDoubleHotKey on each RU platform, '
                       'real Options reads/writes, constructors/property access/XStatus and LightControl state/hardware dispatch. '
                       'Clock and enumerated downstream entry/hardware calls are captured; history uses its real contact wrapper. '
                       'All input classes and subject initializers/facades remain unchanged except enumerated call sites. '
                       'A minimal GraphicsMIDlet isolates startup; Options are explicitly seeded. An allocated inert Jimm '
                       'instance supplies only the Siemens platformRequest receiver. No complete startup, downstream forms, '
                       'network/RMS persistence, physical display/minimization/locking or actual device-light claim.', 'builds': builds}
    (ROOT / 'preservation/reports/source-hotkeys.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS hotkeys:', len(builds), 'platforms,', sum(b['observations'] for b in builds), 'observations; actual raw and optimized application behavior')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--target', choices=['MIDP2', 'MOTOROLA', 'SIEMENS2'])
    parser.add_argument('--negative', action='store_true', help='Run original/authored only, retaining genuine pre-fix evidence before the expected mismatch')
    args = parser.parse_args()
    main([args.target] if args.target else None, args.negative)
