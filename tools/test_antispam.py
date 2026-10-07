#!/usr/bin/env python3
"""Execute real anti-spam filters, journals and action queues on all three RU platforms."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from test_roster_keys import configuration as keys_configuration
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def fixture_proof(artifact, fixture, spec, clocked, data, cp):
    with zipfile.ZipFile(artifact) as a, zipfile.ZipFile(fixture) as b:
        assert len(a.namelist()) == len(set(a.namelist()))
        assert len(b.namelist()) == len(set(b.namelist()))
        assert not set(a.namelist()) - set(b.namelist())
        added = sorted(set(b.namelist()) - set(a.namelist()))
        assert added == ['AntispamIO$Clock.class', 'AntispamIO$Queue.class', 'AntispamIO.class', 'GraphicsMIDlet.class'], added
        changed = sorted(n for n in a.namelist() if n.lower() != 'meta-inf/manifest.mf' and a.read(n) != b.read(n))
        assert changed == (sorted([spec['util'] + '.class', spec['send'] + '.class']) if clocked else []), changed
    records = []
    if clocked:
        right = {c['name']: c for c in classes(fixture, cp)}
        left = {c['name']: c for c in data}
        captures = 0
        for owner in [spec['util'], spec['send']]:
            before, after = left[owner], right[owner]
            assert {k: v for k, v in before.items() if k != 'methods'} == {k: v for k, v in after.items() if k != 'methods'}
            assert len(before['methods']) == len(after['methods'])
            for m, n in zip(before['methods'], after['methods']):
                assert {k: v for k, v in m.items() if k not in ['code', 'refs']} == {k: v for k, v in n.items() if k not in ['code', 'refs']}
                expected = []
                for instruction in m['code']:
                    if owner == spec['util'] and m['name'] == spec['dateMethod'] and m['desc'] == spec['dateDesc']:
                        if instruction == '187 java/util/Date':
                            instruction = '187 AntispamIO$Clock'; captures += 1
                        elif instruction == '183 java/util/Date.<init>()V':
                            instruction = '183 AntispamIO$Clock.<init>()V'; captures += 1
                    elif owner == spec['send'] and m['name'] == '<init>' and m['desc'] == spec['sendCtorDesc']:
                        if instruction == '184 java/lang/System.currentTimeMillis()J':
                            instruction = '184 AntispamIO.time()J'; captures += 1
                    expected.append(instruction)
                assert expected == n['code'], (owner, m['name'], m['desc'])
                assert n['refs'] == [s for s in expected if 178 <= int(s.partition(' ')[0]) <= 185]
                if m['code'] != n['code']:
                    records.append({'owner': owner, 'before': m, 'after': n})
        assert captures == 3 and len(records) == 2, (captures, len(records))
    return {'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
            'changed_application_entries': changed, 'added_entries': added,
            'external_clock_instructions_changed': 3 if clocked else 0,
            'whole_captured_methods': records, 'all_other_application_and_resource_bytes_unchanged': True}


def configuration(target, reference, data):
    spec, _, _ = keys_configuration(target, reference, data)
    owners = {c['name']: c for c in data}
    def member(owner, desc, predicate=lambda m: True):
        found = [m for m in owner['methods'] if m['desc'] == desc and predicate(m)]
        assert len(found) == 1, (owner['name'], desc, [m['name'] for m in found])
        return found[0]
    found = [(c, m) for c in data for m in c['methods'] if m['desc'].startswith('(L') and m['desc'].endswith(';)Z')
             and m['code'][:1] == ['17 162'] and m['access'] & 8]
    assert len(found) == 1
    listener, spam = found[0]
    message = owners[spam['desc'][2:-3]]
    plain_name = next(s.split(' ')[1] for s in spam['code'] if s.startswith('193 '))
    plain = owners[plain_name]
    fields = [f['name'] for f in listener['fields'] if f['desc'] == 'Ljava/util/Vector;']
    assert len(fields) == 3
    checked = member(listener, '(Ljava/lang/String;)Z', lambda m: reference or m['name'] == 'isChecked')
    checked_vector = next(s.split(' ')[1].split('.')[1] for s in checked['code'] if s.startswith('178 ') and s.endswith(' Ljava/util/Vector;'))
    spec.update(listener=listener['name'], spam=spam['name'], message=message['name'], plain=plain_name,
                checked=checked['name'], passed=checked_vector)
    if reference:
        assert fields[0] == checked_vector
        spec.update(seen=fields[1], counts=fields[2])
    else:
        assert checked_vector == 'uins';spec.update(seen='uin1', counts='uin2')
    icqs = [c for c in data if any(m['desc'] == '(Ljava/lang/String;Z)V' and m['code'][:1] == ['17 146'] for m in c['methods'])]
    assert len(icqs) == 1
    icq = icqs[0]
    connected_getters = [m for m in icq['methods'] if m['desc'] == '()Z' and m['access'] & 8 and len(m['code']) == 2
                         and m['code'][0].startswith('178 ') and m['code'][-1] == '172']
    assert len(connected_getters) == 1
    connected = connected_getters[0]['code'][0].split(' ')[1].split('.')[1]
    requests = [m for m in icq['methods'] if m['desc'].startswith('(L') and m['desc'].endswith(';)V')
                and '182 java/util/Vector.addElement(Ljava/lang/Object;)V' in m['code'] and '194' in m['code']]
    assert len(requests) == 1
    request = requests[0];action_name=request['desc'][2:-3]
    queue = next(s.split(' ')[1].split('.')[1] for s in request['code'] if s.startswith('178 ') and s.endswith(' Ljava/util/Vector;'))
    own = next(s.split(' ')[1].split('.')[1] for s in request['code'] if s.startswith('178 ') and s.endswith(' L' + icq['name'] + ';'))
    sends = [c for c in data if c['super'] == action_name and any(m['name'] == '<init>' and m['desc'] == '(L' + message['name'] + ';)V' for m in c['methods'])]
    assert len(sends) == 1
    send = sends[0]
    spec.update(icq=icq['name'], connected=connected, request=request['name'], queue=queue, own=own,
                action=action_name, send=send['name'], sendCtorDesc='(L' + message['name'] + ';)V')
    spec['wait'] = next(s.split(' ')[1].split('.')[1] for s in request['code'] if s.startswith('178 ') and s.endswith(' Ljava/lang/Object;'))
    files = [c for c in data if c['super'] == message['name'] and any(f['desc'] == 'Ljava/io/InputStream;' for f in c['fields'])]
    assert len(files) == 1
    spec['fileMessage'] = files[0]['name']
    ctor = member(files[0], next(m['desc'] for m in files[0]['methods'] if m['name'] == '<init>'))
    spec['fileHasType'] = str(ctor['desc'] == '(Ljava/lang/String;L' + spec['contact'] + ';ILjava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V').lower()
    assert spec['fileHasType'] == 'true' or ctor['desc'] == '(Ljava/lang/String;L' + spec['contact'] + ';Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V'
    text = owners[spec['text']]
    spec['clearText'] = member(text, '()V', lambda m: m['name'] == ('a' if reference else 'clear'))['name']
    spec['getText'] = member(text, '(IZI)Ljava/lang/String;')['name']
    contacts = owners[spec['list']]
    spec['contacts'] = ('a' if reference else 'cItems')
    assert any(f['name'] == spec['contacts'] and f['desc'] == 'Ljava/util/Vector;' for f in contacts['fields'])
    spec['plainField'] = next(f['name'] for f in send['fields'] if f['desc'] == 'L' + plain_name + ';')
    spec['actionOwner'] = next(f['name'] for f in owners[action_name]['fields'] if f['desc'] == 'L' + icq['name'] + ';')
    getters = [(c, m) for c in data for m in c['methods'] if m['desc'].endswith(')J')
               and '187 java/util/Date' in m['code'] and '183 java/util/Date.<init>()V' in m['code']]
    assert len(getters) == 1
    util, date = getters[0]
    spec.update(util=util['name'], dateMethod=date['name'], dateDesc=date['desc'])
    spec['sender'] = ('sndrUin' if not reference else next(s.split(' ')[1].split('.')[1] for s in spam['code'] if s.startswith('180 ') and s.endswith(' Ljava/lang/String;')))
    spec['messageText'] = next(f['name'] for f in plain['fields'] if f['desc'] == 'Ljava/lang/String;')
    spec['offline'] = next(f['name'] for f in message['fields'] if f['desc'] == 'Z')
    spec['recipient'] = next(f['name'] for f in message['fields'] if f['desc'] == 'L' + spec['contact'] + ';')
    spec['messageDate'] = next(f['name'] for f in message['fields'] if f['desc'] == 'J')
    spec['messageType'] = next(f['name'] for f in message['fields'] if f['desc'] == 'I' and not f['access'] & 8)
    if reference:
        getter = member(send, '()J')
        ids = [s.split(' ')[1].split('.')[1] for s in getter['code'] if s.startswith('180 ') and s.endswith(' I')]
        assert len(ids) == 2
        spec.update(id1=ids[0], id2=ids[1])
    else:
        spec.update(id1='msgId1', id2='msgId2')
    counter = member(send, '()I', lambda m: m['access'] & 40 == 40)
    spec['counterField'] = next(s.split(' ')[1].split('.')[1] for s in counter['code'] if s.startswith('178 ') and s.endswith(' I'))
    eyes = [c for c in data if any(m['desc'] == '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V' and m['access'] & 8 for m in c['methods'])
            and sum(f['desc'] == 'L' + spec['text'] + ';' for f in c['fields']) == 1 and sum(f['desc'] == 'Ljava/util/Vector;' for f in c['fields']) == 1]
    assert len(eyes) == 1
    eye = eyes[0];spec['eye'] = eye['name']
    add = member(eye, '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V')
    spec['eyeInstance'] = next(s.split(' ')[1].split('.')[1] for s in add['code'] if s.startswith('178 ') and s.endswith(' L' + eye['name'] + ';'))
    spec['eyeList'] = next(f['name'] for f in eye['fields'] if f['desc'] == 'L' + spec['text'] + ';')
    spec['eyeCounter'] = next(f['name'] for f in eye['fields'] if f['desc'] == 'I')
    spec['eyeUins'] = next(f['name'] for f in eye['fields'] if f['desc'] == 'Ljava/util/Vector;')
    return spec


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / name for name in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-antispam-dump')
    runtime = [CACHE / name for name in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / f'tools/source/{name}.java' for name in ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe', 'AntispamFixture', 'AntispamProbe']]], 'compile-antispam')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST, ROOT / 'tools/source/AntispamIO.java'], 'compile-antispam-io')
    builds = []
    for target, historical in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        native = ROOT / f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar'
        raw = ROOT / f'build/source/{target}-RU/classes.jar'
        optimized = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        with zipfile.ZipFile(optimized) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        raw_host = TEST / f'antispam-{target}-raw-input.jar'; write_jar(raw_host, entries)
        outputs, proofs = {}, []
        for mode, artifact in [('reference', native), ('raw', raw_host), ('optimized', optimized)]:
            data = classes(artifact, cp)
            spec = configuration(target, mode == 'reference', data)
            path = TEST / f'antispam-{target}-{mode}.properties'
            # Class/descriptor paths remain JVM-style; the Java probe resolves them explicitly.
            path.write_text(''.join(k + '=' + v + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            for clocked in [False, True]:
                tag = f'antispam-{target}-{mode}-' + ('clock' if clocked else 'device')
                host, fixture, output = [TEST / (tag + ext) for ext in ['-host.jar', '.jar', '.txt']]
                run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture', artifact, host, TEST, 'unused', 'false', 'unused'], tag + '-host')
                run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'AntispamFixture', host, fixture, TEST, path, str(clocked).lower()], tag + '-fixture')
                proof = fixture_proof(artifact, fixture, spec, clocked, data, cp)
                result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                              '-cp', recover.cp([TEST, *runtime]), 'AntispamProbe', fixture, output, path, str(clocked).lower()], tag)
                content = output.read_bytes(); rows = content.splitlines()
                assert len(rows) == (1170 if clocked else 1046), (tag, len(rows))
                assert rows[-1] == (b'coverage:774:360:35:230:638' if clocked else b'coverage:774:236:35:154:0')
                outputs[mode, clocked] = content
                proof.update(mode=mode, clocked=clocked, configuration=spec, observations=len(rows),
                             output_sha256=recover.sha(output), filter_cases=774, helper_cases=35,
                             sequence_cases=360 if clocked else 236,
                             queued_action_snapshot_observations=230 if clocked else 154,
                             journal_entry_snapshot_observations=638 if clocked else 0)
                proofs.append(proof)
                print(target, mode, clocked, result, flush=True)
        differences = []
        for clocked in [False, True]:
            for mode in ['raw', 'optimized']:
                reference, other = outputs['reference', clocked], outputs[mode, clocked]
                changed = sum(a != b for a, b in zip(reference.splitlines(), other.splitlines()))
                assert reference == other, (target, mode, clocked, changed)
                differences.append({'mode': mode, 'clocked': clocked, 'different_observations': changed})
        builds.append({'target': target, 'reference_sha256': recover.sha(native), 'raw_sha256': recover.sha(raw),
                       'delivered_sha256': recover.sha(optimized), 'proofs': proofs, 'comparisons': differences})
    report = {'scope': 'Complete anti-spam execution on three May RU platforms, native/raw/optimized. '
                       'Each artifact executes 1046 device and 1170 fixed-clock observations, including 774 early-filter cases, '
                       'stateful repeated/multiple-UIN sequences, overflow/malformed counters, multilingual control phrases, '
                       'actual file-transfer constructors, 35 direct isChecked cases, and queue/options/clock failure states. '
                       'Real ContactList, ContactItem, MagicEye/TextList, message and action constructors and Icq.requestAction '
                       'execute unchanged. The input queue is an explicit observing/faulting Vector supplied to the real Icq; '
                       'it records actual receiver-monitor ownership. Device preserves all application/resource bytes and '
                       'does not enable the time-dependent journal or compare wall-clock message IDs/dates. Fixed-clock mode '
                       'replaces exactly NEW Date, Date.<init> and System.currentTimeMillis, retaining all other dumped '
                       'metadata/instructions/handlers and all other class/resource bytes. Dates, IDs, queue actions, '
                       'private vectors, partial failures and actual journal text are compared. Queue/journal counts are '
                       'snapshot observations, including repeated reads, not counts of new writes. The MIDlet host/manifest '
                       'and explicit input states are supplied. Network action execution, UI rendering/scheduling, concurrent '
                       'anti-spam writers, other locales and physical devices or whole-program equivalence are outside scope.',
              'builds': builds}
    (ROOT / 'preservation/reports/source-antispam.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS antispam: 6648 observations on native/raw/optimized; zero differences across three platforms')
    return report


if __name__ == '__main__': main()
