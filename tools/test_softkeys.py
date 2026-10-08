#!/usr/bin/env python3
"""Compare genuine soft-key assignment, including real getter failures and partial writes."""
import argparse
import hashlib
import json
import os
import subprocess
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from audit_auxiliary_helpers import configuration as native_configuration
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run

OWNER = 'DrawControls/VirtualList'
ROLES = ['leftOffset', 'rightOffset', 'MENU_LEFT_BAR', 'MENU_RIGHT_BAR', 'MENU_LEFT', 'xStatusOnRight', 'fontView']


def entries(path):
    with zipfile.ZipFile(path) as z: return {n: z.read(n) for n in z.namelist()}


def configuration(data, native, target):
    owner = native_configuration(data)['cd'] if native else OWNER
    c = next(c for c in data if c['name'] == owner)
    methods = [m for m in c['methods'] if m['desc'] == '()V' and m['access'] & 8
               and all(s in m['code'] for s in ['17 143', '17 185', '16 118'])]
    assert len(methods) == 1
    m = methods[0]; code = m['code']; assert not m['handlers']
    if not native: assert m['name'] == 'assignSoftKeys'
    declarations = {}; witnesses = []
    def bind(role, instruction):
        assert instruction.startswith('179 ' + owner + '.')
        operand, desc = instruction.split()[1:]; name = operand.split('.')[1]
        f = next(f for f in c['fields'] if f['name'] == name and f['desc'] == desc)
        assert f['access'] & 8 and not f['access'] & (16 | 64)
        if not native: assert name == role
        if role in declarations: assert declarations[role] == f
        declarations[role] = f
        witnesses.append(dict(role=role, instruction=instruction, complete_declaration=f))
    swap = code.index('17 143'); branch = swap + 2
    assert code[swap + 1].startswith('184 ') and code[swap + 1].endswith('(I)Z') and code[branch].startswith('153 ')
    arms = []
    for start, values in [(branch + 1, ['5', '4', '7']), (int(code[branch].split()[1]), ['4', '5', '6'])]:
        rows = []
        for i, role in enumerate(['MENU_LEFT_BAR', 'MENU_RIGHT_BAR', 'MENU_LEFT']):
            at = start + i * 2; assert code[at] == values[i]; bind(role, code[at + 1]); rows.append([code[at], code[at + 1]])
        at = start + 6
        extra = []
        if code[at] in ['6', '7']:
            assert not native and code[at + 1] == '179 ' + owner + '.MENU_RIGHT I'
            assert code[at] == ('6' if values[0] == '5' else '7')
            extra = code[at:at + 2]; at += 2
        assert code[at].startswith('167 ') or code[at] in ['177', '17 185']
        arms.append(dict(start=start, complete_assignment_pairs=rows, source_only_unused_right_menu_write=extra, terminator=code[at]))
    options = code[swap + 1].split()[1].split('.')[0]
    getters = {}
    for value, role, desc in [('17 185', 'xStatusOnRight', '(I)Z'), ('16 118', 'fontView', '(I)I')]:
        pos = code.index(value); assert code[pos + 1].startswith('184 ' + options + '.') and code[pos + 1].endswith(desc)
        bind(role, code[pos + 2]); getters[desc] = code[pos + 1]
    assert getters['(I)Z'] == code[swap + 1]
    offsets = None; devices = []; device_getters = []
    if native:
        end = 18 if target == 'MIDP2' else 4; prefix = code[:end]
    else:
        assert code[0] == '184 ' + owner + '.setCaptionOffsets()V'
        offsets = next(n for n in c['methods'] if n['name'] == 'setCaptionOffsets' and n['desc'] == '()V')
        assert not offsets['handlers'] and offsets['code'][-1] == '177'; prefix = offsets['code'][:-1]
    if target == 'MIDP2':
        assert len(prefix) == 18
        for start, key, role in [(0, 95, 'leftOffset'), (9, 96, 'rightOffset')]:
            assert prefix[start + 1] == '153 ' + str(start + 7)
            assert prefix[start + 2] == '16 ' + str(key) and prefix[start + 3] == getters['(I)I']
            assert prefix[start + 4:start + 8] == ['5', '96', '167 ' + str(start + 8), '5']
            bind(role, prefix[start + 8]); instruction = prefix[start]
            jimm = next(n for n in data if n['name'] == 'jimm/Jimm')
            if instruction.startswith('184 '):
                getter = next(n for n in jimm['methods'] if instruction == '184 jimm/Jimm.' + n['name'] + n['desc'])
                assert getter['code'][-1] == '172' and len(getter['code']) == 2
                device_getters.append(getter); instruction = getter['code'][0]
            assert instruction.startswith('178 jimm/Jimm.') and instruction.endswith(' Z')
            f = next(f for f in jimm['fields'] if instruction == '178 jimm/Jimm.' + f['name'] + ' ' + f['desc'])
            assert f['access'] & 8 and f['desc'] == 'Z'; devices.append(f)
        assert len({f['name'] for f in devices}) == 2
    else:
        assert prefix[:2] == ['5', '89'] and len(prefix) == 4
        bind('rightOffset', prefix[2]); bind('leftOffset', prefix[3])
    assert len({(f['name'], f['desc']) for f in declarations.values()}) == 7
    option_class = next(n for n in data if n['name'] == options); getter_bodies = []
    tables = []
    for desc, instruction in getters.items():
        n = next(n for n in option_class['methods'] if instruction == '184 ' + options + '.' + n['name'] + n['desc'])
        assert n['access'] & 8 and not n['handlers']
        read = [s for s in n['code'] if s.startswith('178 ' + options + '.') and s.endswith(' [Ljava/lang/Object;')]
        assert len(read) == 1
        f = next(f for f in option_class['fields'] if read[0] == '178 ' + options + '.' + f['name'] + ' ' + f['desc'])
        assert f['access'] & 8; tables.append(f); getter_bodies.append(n)
    assert tables[0] == tables[1]
    call = '184 ' + owner + '.' + m['name'] + m['desc']
    callers = [dict(owner=n['name'], complete_method=p, positions=[i for i, s in enumerate(p['code']) if s == call])
               for n in data for p in n['methods'] if call in p['code']]
    assert callers
    props = dict(vl=owner, options=options, table=tables[0]['name'], assign=m['name'], devices=','.join(f['name'] for f in devices))
    props.update({role: declarations[role]['name'] for role in ROLES})
    evidence = dict(owner=owner, complete_subject=m, complete_fields=declarations, typed_option_write_witnesses=witnesses,
                    complete_caption_offset_helper=offsets, whole_caption_offset_prefix=prefix,
                    complete_device_fields=devices, complete_raw_device_getters=device_getters,
                    complete_getters=getter_bodies, complete_option_table=tables[0], complete_menu_arms=arms,
                    whole_direct_callers=callers, actual_option_read_instruction_order=[s for s in code if s in ['17 143', '17 185', '16 118']])
    return props, evidence


def main(before=False):
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    before_path = ROOT / 'preservation/reports/source-softkeys-before.json'
    if before:
        commit = subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT).decode().strip(); assert commit.startswith('c8bddc0')
        assert not before_path.exists(), 'Never replace genuine pre-edit observations'
        src = (ROOT / 'src/DrawControls/VirtualList.java').read_bytes()
        start = src.index(b'public static void assignSoftKeys()'); body = src[start:src.index(b'public void addCommandEx', start)]
        assert body.index(b'xStatusOnRight =') < body.index(b'OPTION_SWAP_SOFT_KEY')
    cp = recover.cp([CACHE / n for n in recover.ASM]); runtime = [CACHE / n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-softkeys-dump')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in ['PopupProbe','GraphicsFixture','OptionsSettingsFixture','SoftKeysProbe']]], 'compile-softkeys-probe')
    builds = []; tag = 'before' if before else 'current'
    for target, historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        paths = dict(reference=ROOT / f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar', raw=ROOT / f'build/source/{target}-RU/classes.jar', optimized=ROOT / f'dist/source/Jimm-{target}-RU.jar')
        evidence = []; outputs = {}
        for mode, path in paths.items():
            data = classes(path, cp); props, proof = configuration(data, mode == 'reference', target); actual = path
            if mode == 'raw':
                actual = TEST / f'softkeys-{tag}-{target}-raw-input.jar'
                values = {n:b for n,b in entries(paths['optimized']).items() if not n.endswith('.class')}; values.update(entries(path)); write_jar(actual, values)
                assert entries(actual) == values
            base = f'softkeys-{tag}-{target}-{mode}'; fixture = TEST / (base + '.jar'); out = TEST / (base + '.txt'); spec = TEST / (base + '.properties')
            spec.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k,v in props.items()), encoding='utf-8', newline='\n')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'OptionsSettingsFixture', actual, fixture, TEST, props['options'], 'false', 'unused'], base + '-fixture')
            a,b = entries(actual),entries(fixture); assert set(b) - set(a) == {'GraphicsMIDlet.class'} and not set(a) - set(b)
            assert all(a[n] == b[n] for n in a if n.lower() != 'meta-inf/manifest.mf')
            assert b['GraphicsMIDlet.class'][:4] == b'\xca\xfe\xba\xbe' and int.from_bytes(b['GraphicsMIDlet.class'][6:8], 'big') == 49
            fixture_data = classes(fixture, cp); assert [n for n in fixture_data if n['name'] != 'GraphicsMIDlet'] == data
            result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST,*runtime]),
                          'SoftKeysProbe', fixture, out, spec], base + '-probe')
            values = out.read_bytes(); count = 5472 if target == 'MIDP2' else 1368; assert len(values.splitlines()) == count; outputs[mode] = values
            proof.update(mode=mode, input_sha256=recover.sha(path), assembled_input_sha256=recover.sha(actual), fixture_sha256=recover.sha(fixture),
                         output_sha256=recover.sha(out), observations=count, all_original_application_and_resource_bytes_unchanged=True,
                         only_original_entry_change='META-INF/MANIFEST.MF', only_added_entry='GraphicsMIDlet.class', provider_sha256=hashlib.sha256(b['GraphicsMIDlet.class']).hexdigest(), result=result)
            evidence.append(proof); print(target, mode, result, flush=True)
        comparisons = []
        for mode in ['raw', 'optimized']:
            different = [dict(position=i, native=a, source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs[mode].decode().splitlines())) if a != b]
            if before: assert different
            else: assert outputs['reference'] == outputs[mode], (target,mode,different[:2])
            comparisons.append(dict(mode=mode, complete_output_bytes_equal=outputs['reference'] == outputs[mode], differing_observations=len(different), first_24_actual_differing_rows=different[:24]))
        builds.append(dict(target=target,evidence=evidence,comparisons=comparisons))
    report = dict(scope='The whole genuine assignSoftKeys methods, actual raw caption helper and actual Options getInt/getBoolean bodies execute without any instruction or declaration edits. '
                  'A headless MicroEmulator device and minimal MIDlet initialize host APIs; each fixture changes only the manifest and adds that host class. Every original application/resource byte and whole dumped class remains unchanged. '
                  'Seven shared mutable fields are bound to actual typed writes: left/right caption offsets, three surviving menu directions, XStatus placement and font view. The raw fourth MENU_RIGHT field/write is preserved and explicitly excluded from native/optimized observations because optimization removes it. '
                  'All four real Nokia/SE flag combinations on MIDP2 and no invented device flags on Motorola/Siemens, both swap and XStatus values, three font integers and two offset pairs include signed-overflow edges. '
                  'Nineteen actual option-table states include valid/null tables, seven truncated lengths and null/wrong-type values at five consumed keys. Three consecutive broken/repaired/broken calls observe real exception class, every shared field and unchanged table contents. '
                  '5472 observations/version on MIDP2 and 1368/version on each other RU platform; no scripted getter, fake subject or whole-program/concurrency/physical-key claim.',
                  source_file='src/DrawControls/VirtualList.java', source_sha256=recover.sha(ROOT / 'src/DrawControls/VirtualList.java'), builds=builds)
    if before: report['baseline_commit'] = commit
    else:
        baseline = json.loads(before_path.read_text(encoding='utf-8')); report['before_report_sha256'] = recover.sha(before_path)
        for left,right in zip(baseline['builds'],builds):
            assert left['target'] == right['target']
            # JarOutputStream fixture timestamps are not application data; all class/resource bytes are checked above.
            assert {k:v for k,v in left['evidence'][0].items() if k != 'fixture_sha256'} == {k:v for k,v in right['evidence'][0].items() if k != 'fixture_sha256'}
        report['genuine_before_differences'] = {b['target']:{c['mode']:c['differing_observations'] for c in b['comparisons']} for b in baseline['builds']}
    (before_path if before else ROOT / 'preservation/reports/source-softkeys-replay.json').write_text(json.dumps(report, indent=2) + '\n',encoding='utf-8',newline='\n')
    print('PASS soft keys ' + tag + ': whole subjects and real getters; ' + ('genuine partial-write differences retained' if before else 'all 24624 native/raw/optimized observations match'))
    return report


if __name__ == '__main__':
    p=argparse.ArgumentParser(description=__doc__); p.add_argument('--before', action='store_true'); main(p.parse_args().before)
