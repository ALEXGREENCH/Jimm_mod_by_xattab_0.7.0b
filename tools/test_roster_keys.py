#!/usr/bin/env python3
"""Execute the whole May roster key callback and cursor paging on all three targets."""
import argparse
import json
import os
import zipfile
from pathlib import Path
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run
from test_extended_keys import configuration as list_configuration
from test_sound import configuration as sound_configuration


def configuration(target, reference, data):
    spec = list_configuration(target, reference, data)
    sound, cl, options, _ = sound_configuration(target, reference, data)
    spec.update({key: sound[key] for key in ['list', 'display', 'version']})
    if 'bitmap' in sound:
        spec.update({key: sound[key] for key in ['bitmap', 'bitmapField']})
    tree_field = next(f for f in cl['fields'] if f['desc'] == ('Law;' if reference else 'LDrawControls/VirtualTree;')) if not reference or target == 'MIDP2' else None
    if tree_field is None:
        owners = {c['name']: c for c in data}
        tree_field = next(f for f in cl['fields'] if f['desc'].startswith('L') and f['desc'][1:-1] in owners and owners[f['desc'][1:-1]]['super'] == spec['vl'])
    tree = next(c for c in data if c['name'] == tree_field['desc'][1:-1])
    # Root node's class is identified by its Object constructor, not field ordering.
    nodes = [c for c in data if any(m['name'] == '<init>' and m['desc'] == '(Ljava/lang/Object;)V' for m in c['methods']) and any(f['desc'] == 'Ljava/util/Vector;' for f in c['fields'])]
    assert len(nodes) == 1
    callback = next(m for m in cl['methods'] if m['desc'] == '(L' + spec['vl'] + ';II)V')
    contacts = [c for c in data if any(m['name'] == '<init>' and m['desc'] == '(IILjava/lang/String;Ljava/lang/String;ZZ)V' for m in c['methods'])]
    assert len(contacts) == 1
    vl = next(c for c in data if c['name'] == spec['vl'])
    action = next(m for m in vl['methods'] if m['desc'] == '(I)I' and len(m['code']) == 4 and '182 javax/microedition/lcdui/Canvas.getGameAction(I)I' in m['code'])
    spec.update(tree=tree['name'], treeField=tree_field['name'], node=nodes[0]['name'],
                drawItems=next(f['name'] for f in tree['fields'] if f['desc'] == 'Ljava/util/Vector;'),
                contact=contacts[0]['name'], callback=callback['name'], gameAction=action['name'],
                trigger='3' if target == 'MOTOROLA' else '1')
    return spec, vl, callback


def execute(target, mode, artifact, runtime, cp):
    data = classes(artifact, cp)
    spec, vl, callback = configuration(target, mode == 'reference', data)
    tag = 'roster-keys-' + target + '-' + mode
    settings = TEST / (tag + '.properties')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    results, proofs = [], []
    for scripted in [False, True]:
        flavor = 'scripted' if scripted else 'device'
        fixture, output = [TEST / (tag + '-' + flavor + ext) for ext in ['.jar', '.txt']]
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'RosterKeysFixture', artifact,
             fixture, TEST, spec['vl'], str(scripted).lower(), spec['gameAction']], tag + '-' + flavor + '-fixture')
        with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
            changes = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf' and before.read(n) != after.read(n)]
            assert changes == ([vl['name'] + '.class'] if scripted else [])
            assert set(after.namelist()) - set(before.namelist()) <= {'GraphicsMIDlet.class', 'RosterKeysIO.class'}
        if scripted:
            expected = json.loads(json.dumps(vl))
            count = 0
            for m in expected['methods']:
                if m['name'] == spec['gameAction'] and m['desc'] == '(I)I':
                    call = '182 javax/microedition/lcdui/Canvas.getGameAction(I)I'
                    count += m['code'].count(call)
                    m['code'] = ['184 RosterKeysIO.gameAction(Ljavax/microedition/lcdui/Canvas;I)I' if s == call else s for s in m['code']]
                    m['refs'] = [s for s in m['code'] if 178 <= int(s.split(' ', 1)[0]) <= 185]
            assert count == 1
            changed = next(c for c in classes(fixture, cp) if c['name'] == vl['name'])
            assert expected == changed, 'Non-enumerated instructions or metadata changed'
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'RosterKeysProbe', fixture, output, settings, str(scripted).lower()], tag + '-' + flavor)
        result = output.read_bytes()
        coverage = list(map(int, result.splitlines()[-1].decode().split(':')[1:]))
        assert len(coverage) == 4 and coverage[0] == len(result.splitlines()) - 1
        results.append(result)
        proofs.append({'flavor': flavor, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'observations': coverage[0],
                       'coverage': dict(zip(['callbacks', 'cursor_moves', 'device_lookups', 'zero_key_consumptions'], coverage)),
                       'all_application_bytes_unchanged': not scripted,
                       'only_one_external_call_instruction_changed': scripted,
                       'callback': callback['name'] + callback['desc']})
    return results, proofs


def main(baseline=None):
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / ('tools/source/' + n + '.java') for n in ['GraphicsFixture', 'PopupProbe', 'RosterKeysFixture', 'RosterKeysProbe']]], 'compile-roster-keys')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8', '-cp', recover.cp(runtime),
         '-d', TEST, ROOT / 'tools/source/RosterKeysIO.java'], 'compile-roster-keys-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-roster-keys-dump')
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'
        optimized = baseline / (target + '-source.jar') if baseline else ROOT / f'dist/source/Jimm-{target}-RU.jar'
        raw = baseline / (target + '-classes.jar') if baseline else ROOT / f'build/source/{target}-RU/classes.jar'
        resource_host = TEST / ('roster-keys-' + target + '-raw-input.jar')
        with zipfile.ZipFile(optimized) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(resource_host, entries)
        results, proofs = [], []
        for mode, artifact in [('reference', original), ('raw', resource_host), ('optimized', optimized)]:
            output, proof = execute(target, mode, artifact, runtime, cp);results.append(output);proofs.append({'mode': mode, 'flavors': proof})
        comparisons = []
        for i, flavor in enumerate(['device', 'scripted']):
            for j, mode in [(1, 'raw'), (2, 'optimized')]:
                left, right = results[0][i].splitlines(), results[j][i].splitlines();assert len(left) == len(right)
                differences = [n for n, (a, b) in enumerate(zip(left[:-1], right[:-1])) if a != b]
                comparisons.append({'flavor': flavor, 'mode': mode, 'different_observations': len(differences),
                                    'same_coverage': left[-1] == right[-1], 'first_different_rows': differences[:12]})
                if not baseline: assert not differences, (target, flavor, mode, differences[:12])
                if not baseline: assert left[-1] == right[-1], (target, flavor, mode, 'coverage')
                else: assert differences, 'Pre-fix evidence must genuinely differ'
        builds.append({'target': target, 'proofs': proofs, 'comparisons': comparisons})
        print('PASS roster keys:', target, 'baseline' if baseline else 'restored', flush=True)
    report = {'observations': sum(p['observations'] for b in builds for p in b['proofs'][0]['flavors']),
              'scope': 'Whole genuine ContactList.vlKeyPress, JimmUI hotkeys, tree selection and TextList/VirtualList cursor movement. '
                       'Device mode keeps all application bytes. Scripted mode changes exactly one Canvas.getGameAction invocation; '
                       'all other instructions, method declarations and handlers remain intact. Explicit option and tree-state seeds '
                       'isolate startup. Delete-contact with a selected contact and enabled MagicEye activation are outside this paging scope. '
                       'No physical key-event scheduling or full UI startup claim.', 'builds': builds}
    name = 'source-roster-keys-before.json' if baseline else 'source-roster-keys.json'
    if baseline: report['source_ref'] = '7ed7969'
    (ROOT / 'preservation/reports' / name).write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--baseline', type=Path)
    main(parser.parse_args().baseline)
