#!/usr/bin/env python3
"""Execute actual device-key classification/reaction, retaining genuine pre-fix logs."""
import argparse
import json
import os
import zipfile
from pathlib import Path
import recover
import audit_source as audit
from test_source import ROOT, TEST, CACHE, run
from audit_send_text import classes
from build_source import write_jar


def configuration(target, reference, data):
    owners = [c for c in data if any('soft1' in m['strings'] and m['desc'] == '(I)I' for m in c['methods'])]
    assert len(owners) == 1
    vl = owners[0]
    action = next(m for m in vl['methods'] if 'soft1' in m['strings'] and m['desc'] == '(I)I')
    reaction = [m for m in vl['methods'] if m['desc'] == '(II)V' and
                any(s.endswith(vl['name'] + '.' + action['name'] + action['desc']) for s in m['refs'])]
    assert len(reaction) == 1
    texts = [c for c in data if c['super'] == vl['name'] and any(m['desc'] ==
             '(Ljava/lang/String;III)L' + c['name'] + ';' for m in c['methods'])]
    assert len(texts) == 1
    text = texts[0]
    if reference:
        spec = {'options': {'MIDP2': 'cj', 'MOTOROLA': 'ci', 'SIEMENS2': 'ch'}[target],
                'table': 'a', 'addText': 'a', 'newline': 'a', 'addCommand': 'a', 'listener': 'a',
                'assign': 'd', 'current': 'b', 'top': 'd', 'state': 'n', 'height': 'I', 'width': 'H',
                'menuIndex': 's', 'topMenu': 'y', 'visibleMenu': 'x', 'zero': 'a', 'fontSize': 'u', 'capHeightMethod': 'm', 'menuHeightMethod': 'o', 'visMethod': 'e', 'itemHeightMethod': 'a'}
        if target != 'MIDP2':
            spec.update(state='l', height='C', width='B', menuIndex='q', topMenu='w', visibleMenu='v', fontSize='s')
    else:
        spec = {'options': 'jimm/Options', 'table': 'options', 'addText': 'addBigText', 'newline': 'doCRLF',
                'addCommand': 'addCommandEx', 'listener': 'setCommandListener', 'assign': 'assignSoftKeys',
                'current': 'currItem', 'top': 'topItem', 'state': 'uiState', 'height': 'forcedHeight', 'width': 'forcedWidth',
                'menuIndex': 'curMenuItemIndex', 'topMenu': 'topMenuItem', 'visibleMenu': 'visibleItemsMenuCount', 'zero': 'zeroWasPressed',
                'fontSize': 'fontSize', 'capHeightMethod': 'getCapHeight', 'menuHeightMethod': 'getMenuBarHeight', 'visMethod': 'getVisCount', 'itemHeightMethod': 'getItemHeight'}
    spec.update(vl=vl['name'], text=text['name'], action=action['name'], reaction=reaction[0]['name'])
    # State aliases are the fields independently exercised by the prior list/menu/graphics probes.
    fields = {(f['name'], f['desc']) for f in vl['fields']}
    assert all((spec[k], 'I') in fields for k in ['current', 'top', 'state', 'height', 'width', 'menuIndex', 'topMenu', 'visibleMenu'])
    assert (spec['zero'], 'Z') in fields
    first_instance_read = next(s for s in reaction[0]['code'] if s.startswith('180 ' + vl['name'] + '.'))
    assert first_instance_read == '180 ' + vl['name'] + '.' + spec['state'] + ' I'
    for role, device in [('height', 'getHeight'), ('width', 'getWidth')]:
        read = '180 ' + vl['name'] + '.' + spec[role] + ' I'
        matching = [m for m in vl['methods'] if m['desc'] == '()I' and m['code'].count(read) == 2 and
                    any(s.endswith('.' + device + '()I') for s in m['code'])]
        assert len(matching) == 1, (target, role, matching)
    return spec


def execute(target, mode, artifact, runtime, cp):
    data = classes(artifact, cp)
    spec = configuration(target, mode == 'reference', data)
    tag = 'extended-keys-' + target + '-' + mode
    settings = TEST / (tag + '.properties')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    outputs, proofs = [], []
    for scripted in [False, True]:
        flavor = 'scripted' if scripted else 'device'
        fixture, output = TEST / (tag + '-' + flavor + '.jar'), TEST / (tag + '-' + flavor + '.txt')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture', artifact,
             fixture, TEST, spec['vl'], str(scripted).lower(), spec['action']], tag + '-' + flavor + '-fixture')
        # Every original entry except the host manifest remains byte-for-byte intact in device mode.
        with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
            changes = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf' and before.read(n) != after.read(n)]
        assert changes == ([spec['vl'] + '.class'] if scripted else []), (mode, changes)
        if scripted:
            changed = next(c for c in classes(fixture, cp) if c['name'] == spec['vl'])
            native = next(c for c in data if c['name'] == spec['vl'])
            assert {k: v for k, v in native.items() if k != 'methods'} == {k: v for k, v in changed.items() if k != 'methods'}
            assert len(native['methods']) == len(changed['methods'])
            captures = {}
            for left, right in zip(native['methods'], changed['methods']):
                assert {k: v for k, v in left.items() if k not in ['code', 'refs']} == {k: v for k, v in right.items() if k not in ['code', 'refs']}
                expected = []
                for instruction in left['code']:
                    dest = None
                    if left['name'] == spec['action'] and left['desc'] == '(I)I' and instruction.startswith('182 ') and instruction.endswith('.getKeyName(I)Ljava/lang/String;'):
                        dest = '184 ExtendedKeysIO.keyName(Ljavax/microedition/lcdui/Canvas;I)Ljava/lang/String;'
                    elif left['name'] == spec['action'] and left['desc'] == '(I)I' and instruction.startswith('182 ') and instruction.endswith('.getGameAction(I)I'):
                        dest = '184 ExtendedKeysIO.gameAction(Ljavax/microedition/lcdui/Canvas;I)I'
                    if dest:
                        assert left['name'] == spec['action'], 'Only subject device calls may be scripted'
                        captures[instruction] = captures.get(instruction, 0) + 1
                        instruction = dest
                    expected.append(instruction)
                assert expected == right['code'], (mode, left['name'], left['desc'])
                assert right['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
            assert sum(captures.values()) == 2
        else:
            captures = {}
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'ExtendedKeysProbe', fixture, output, settings,
             str(scripted).lower()], tag + '-' + flavor)
        result = output.read_bytes()
        counts = list(map(int, result.decode('utf-8').splitlines()[-1].split(':')[1:]))
        assert counts == [2508] if scripted else counts[:2] == [523, 570]
        outputs.append(result)
        proofs.append({'flavor': flavor, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                       'all_input_class_bytes_unchanged': not scripted, 'only_two_enumerated_device_calls_changed': scripted,
                       'captures': captures, 'coverage': counts})
    return outputs, proofs


def main(negative=False, baseline=None):
    assert baseline is None or negative, 'Baseline archives are used only to retain pre-fix evidence'
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / ('tools/source/' + n + '.java') for n in ['ExtendedKeysFixture', 'ExtendedKeysProbe', 'ExtendedKeysIO']]], 'compile-extended-keys')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-cp', recover.cp(runtime),
         '-d', TEST, ROOT / 'tools/source/ExtendedKeysIO.java'], 'compile-extended-keys-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-extended-keys-dump')
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        artifact = ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        if baseline is not None:
            raw = baseline / (target + '-classes.jar')
            artifact = baseline / (target + '-source.jar')
        resource_host = TEST / ('extended-keys-' + target + '-authored-resources.jar')
        with zipfile.ZipFile(artifact) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(resource_host, entries)
        outputs, proofs = [], []
        modes = [('reference', original), ('source', resource_host)]
        if not negative: modes.append(('optimized-source', artifact))
        for mode, input_path in modes:
            result, proof = execute(target, mode, input_path, runtime, cp)
            outputs.append(result);proofs.append({'mode': mode, 'flavors': proof})
        differences = {}
        for i, flavor in enumerate(['device', 'scripted']):
            left, right = outputs[0][i].splitlines(), outputs[1][i].splitlines()
            assert len(left) == len(right)
            different = [(a, b) for a, b in zip(left, right) if a != b]
            differences[flavor] = {'different_observations': len(different),
                                   'lookup_differences': sum(a.startswith(b'lookup:') for a, b in different),
                                   'reaction_differences': sum(a.startswith(b'reaction:') for a, b in different),
                                   'reference_output_sha256': recover.sha(TEST / ('extended-keys-' + target + '-reference-' + flavor + '.txt')),
                                   'source_output_sha256': recover.sha(TEST / ('extended-keys-' + target + '-source-' + flavor + '.txt'))}
            if not negative: assert all(s[i] == outputs[0][i] for s in outputs), ('Extended keys differ', target, flavor)
        if negative: assert differences['device']['lookup_differences'] > 0, 'Negative evidence must actually differ'
        builds.append({'target': target, 'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
                       'source_unoptimized_class_jar_sha256': recover.sha(raw), 'device_observations': len(outputs[0][0].splitlines()),
                       'scripted_observations': len(outputs[0][1].splitlines()), 'comparisons': differences, 'fixture_proofs': proofs})
    report = {'scope': 'Actual three-platform extended-key classification and private menu/cursor reaction in '
                       'original and unoptimized classes; delivered optimized classes are additionally executed after '
                       'restoration. A minimal MIDlet and explicit Options seed '
                       'isolate startup. Device flavor uses genuine MicroEmulator APIs and preserves every input class '
                       'byte; scripted flavor changes exactly two API invocation instructions, retaining all declarations, '
                       'subject control flow, exception regions and other application class bytes. API names, game '
                       'results, errors and call order are inputs/observations, never derived expected application code. '
                       'No complete startup, public key pipeline or physical handset keymap claim.',
              'negative_before_fix': negative, 'builds': builds}
    if baseline is not None:
        report['source_virtual_list_sha256'] = recover.sha(baseline / 'VirtualList.java')
        report['source_ref'] = (baseline / 'source_ref.txt').read_text(encoding='utf-8').strip()
    name = 'source-extended-keys-before.json' if negative else 'source-extended-keys.json'
    (ROOT / 'preservation/reports' / name).write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS extended keys:', len(builds), 'platforms;', sum(b['device_observations'] + b['scripted_observations'] for b in builds), 'observations' + ('; genuine pre-fix differences recorded' if negative else '; actual raw and optimized results match'))
    return report


if __name__ == '__main__':
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--negative', action='store_true')
    p.add_argument('--baseline', type=Path, help='Immutable work directory with TARGET-classes.jar/TARGET-source.jar, VirtualList.java and source_ref.txt')
    args = p.parse_args()
    main(args.negative, args.baseline)
