#!/usr/bin/env python3
"""Execute real Xtraz request serialization and socket sends on three May RU platforms."""
import difflib
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from test_roster_keys import configuration as keys_configuration
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def configuration(target, reference, data):
    spec, _, _ = keys_configuration(target, reference, data)
    owners = {c['name']: c for c in data}
    def member(owner, name, desc):
        found = [m for m in owners[owner]['methods'] if m['name'] == name and m['desc'] == desc]
        assert len(found) == 1, (owner, name, desc)
        return found[0]
    candidates = [c for c in data if any(m['desc'] == '(Ljava/lang/String;IJJLjava/lang/String;)[B' for m in c['methods'])]
    assert len(candidates) == 1
    xtraz = candidates[0]
    query = [m for m in xtraz['methods'] if m['desc'] in ['(Ljava/lang/String;I)V', '(Ljava/lang/String;)V']
             and '184 java/lang/System.currentTimeMillis()J' in m['code']]
    assert len(query) == 1
    query = query[0]
    calls = [s[4:] for s in query['code'] if s.startswith('184 ') and s.endswith('()I')]
    assert len(calls) == 1
    util, method_name = calls[0].split('.', 1); method_name = method_name[:-3]
    get_counter = member(util, method_name, '()I')
    counter = next(s.split(' ')[1].split('.')[1] for s in get_counter['code'] if s.startswith('178 ') and s.endswith(' I'))
    channel = [s.split(' ') for s in query['code'] if s.startswith('178 ') and s.endswith(';')]
    assert len(channel) == 1
    icq, channel_name = channel[0][1].split('.'); connection = channel[0][2][1:-1]
    sockets = [c for c in data if c['super'] == connection and any(f['desc'] == 'Ljavax/microedition/io/SocketConnection;' for f in c['fields'])
               and any(f['desc'] == 'Ljava/io/OutputStream;' for f in c['fields'])
               and not any(m['desc'] == '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;' for m in c['methods'])]
    assert len(sockets) == 1
    socket = sockets[0]
    sends = [m for m in socket['methods'] if '182 java/io/OutputStream.write([B)V' in m['code']]
    assert len(sends) == 1
    send = sends[0]
    seq_calls = [s[4:] for s in send['code'] if s.startswith('184 ' + icq + '.') and s.endswith('()I')]
    assert len(seq_calls) == 1
    seq_method = member(icq, seq_calls[0].split('.', 1)[1][:-3], '()I')
    sequence = next(s.split(' ')[1].split('.')[1] for s in seq_method['code'] if s.startswith('178 ') and s.endswith(' I'))
    traffic_calls = [s[4:] for s in send['code'] if s.startswith('184 ') and s.endswith('(I)V')]
    assert len(traffic_calls) == 1
    traffic, traffic_method = traffic_calls[0].split('.', 1)
    add_out = member(traffic, traffic_method[:-4], '(I)V')
    traffic_out = next(s.split(' ')[1].split('.')[1] for s in add_out['code'] if s.startswith('178 ') and s.endswith(' I'))
    exceptions = {s.split(' ')[1] for s in send['code'] if s.startswith('187 ') and owners.get(s.split(' ')[1], {}).get('super') == 'java/lang/Exception'}
    assert len(exceptions) == 1
    error = owners[next(iter(exceptions))]
    booleans = [f['name'] for f in error['fields'] if f['desc'] == 'Z']
    assert len(booleans) == 3
    close_getters = [m for m in owners[connection]['methods'] if m['desc'] == '()Z' and '194' in m['code']]
    assert len(close_getters) == 1
    spec.update(xtraz=xtraz['name'], query=query['name'], queryDesc=query['desc'], util=util, counter=counter,
                icq=icq, channel=channel_name, connection=connection, socket=socket['name'], sequence=sequence,
                output=next(f['name'] for f in socket['fields'] if f['desc'] == 'Ljava/io/OutputStream;'),
                traffic=traffic, trafficOut=traffic_out, closed=close_getters[0]['name'], clock='XtrazQueryClock',
                error=error['name'], errorCode=next(f['name'] for f in error['fields'] if f['desc'] == 'I' and not f['access'] & 8),
                critical=booleans[0], displayError=booleans[1], peer=booleans[2])
    call = '184 ' + xtraz['name'] + '.' + query['name'] + query['desc']
    callers = [{'owner': c['name'], 'complete_method': m, 'positions': [i for i, s in enumerate(m['code']) if s == call]}
               for c in data for m in c['methods'] if call in m['code']]
    assert len(callers) == 3 and sum(len(c['positions']) for c in callers) == 3
    if query['desc'] == '(Ljava/lang/String;I)V':
        assert all(c['complete_method']['code'][i - 1] == '3' for c in callers for i in c['positions'])
    append = query['code'].index('182 java/lang/StringBuffer.append(I)Ljava/lang/StringBuffer;')
    assert query['code'][append - 1] == ('3' if reference or query['desc'] == '(Ljava/lang/String;)V' else '21 1')
    if reference:
        # Slot 1 is reused for String and counter locals later; those loads are not reads of the original argument.
        first_store = next(i for i, s in enumerate(query['code']) if s in ['54 1', '55 1', '56 1', '57 1', '58 1'])
        assert first_store > append and '21 1' not in query['code'][:first_store]
        assert not any(153 <= int(s.split()[0]) <= 171 for s in query['code'][:first_store])
    raw_helpers = [m for m in xtraz['methods'] if m['desc'] == '(Ljava/lang/String;IJJLjava/lang/String;)[B' and m['name'] == 'b']
    if xtraz['name'] == 'jimm/comm/XtrazSM':
        assert len(raw_helpers) == 1 and '184 ' + xtraz['name'] + '.b(Ljava/lang/String;IJJLjava/lang/String;)[B' in query['code']
    else: assert not raw_helpers
    source_ctor = [m for m in xtraz['methods'] if m['name'] == '<init>' and m['desc'] == '()V']
    if reference or xtraz['name'] == 'jimm/comm/XtrazSM':
        assert len(source_ctor) == 1 and source_ctor[0]['code'] == ['25 0', '183 java/lang/Object.<init>()V', '177']
    else:
        assert 'jimm/comm/XtrazSM' not in owners
        source_ctor = []  # Icon constructors belong to Icon; they are not invented Xtraz constructors.
    return spec, {'query_owner': xtraz['name'], 'complete_query': query, 'whole_actual_callers': callers,
                  'whole_raw_inlined_packet_builder': raw_helpers,
                  'whole_xtraz_constructor': source_ctor, 'authoring_class_removed_and_helpers_relocated': xtraz['name'] == 'DrawControls/Icon',
                  'whole_bindings': [{'owner': util, 'method': get_counter}, {'owner': icq, 'method': seq_method},
                                     {'owner': socket['name'], 'method': send}, {'owner': traffic, 'method': add_out},
                                     {'owner': connection, 'method': close_getters[0]}]}


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / f'tools/source/{n}.java' for n in ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe', 'XtrazQueryFixture', 'XtrazQueryProbe']]], 'compile-xtraz-query')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST, ROOT / 'tools/source/XtrazQueryClock.java'], 'compile-xtraz-query-clock')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-xtraz-query-dump')
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        native = ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'
        raw = ROOT / f'build/source/{target}-RU/classes.jar'; optimized = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        with zipfile.ZipFile(optimized) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        raw_host = TEST / f'xtraz-query-{target}-raw-input.jar'; write_jar(raw_host, entries)
        outputs, proofs = [], []
        for mode, path in [('reference', native), ('raw', raw_host), ('optimized', optimized)]:
            data = classes(path, cp); spec, evidence = configuration(target, mode == 'reference', data)
            tag = f'xtraz-query-{target}-{mode}'
            settings = TEST / (tag + '.properties'); settings.write_text(''.join(k + '=' + v + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            host, fixture, output = [TEST / (tag + ext) for ext in ['-host.jar', '.jar', '.txt']]
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture', path, host, TEST, 'unused', 'false', 'unused'], tag + '-host')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'XtrazQueryFixture', host, fixture, TEST, settings], tag + '-fixture')
            with zipfile.ZipFile(path) as a, zipfile.ZipFile(fixture) as b:
                assert len(b.namelist()) == len(set(b.namelist()))
                assert not set(a.namelist()) - set(b.namelist())
                changed = [n for n in a.namelist() if n.lower() != 'meta-inf/manifest.mf' and a.read(n) != b.read(n)]
                assert changed == [spec['xtraz'] + '.class']
                assert set(b.namelist()) - set(a.namelist()) == {'GraphicsMIDlet.class', 'XtrazQueryClock.class'}
            before = next(c for c in data if c['name'] == spec['xtraz'])
            after = next(c for c in classes(fixture, cp) if c['name'] == spec['xtraz'])
            expected = json.loads(json.dumps(before)); captures = 0
            for m in expected['methods']:
                if m['name'] == spec['query'] and m['desc'] == spec['queryDesc']:
                    clock = '184 java/lang/System.currentTimeMillis()J'; captures += m['code'].count(clock)
                    m['code'] = ['184 XtrazQueryClock.time()J' if s == clock else s for s in m['code']]
                    m['refs'] = [s for s in m['code'] if 178 <= int(s.partition(' ')[0]) <= 185]
            assert captures == 1 and expected == after, 'Non-enumerated instruction/metadata changes'
            result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                          '-cp', recover.cp([TEST, *runtime]), 'XtrazQueryProbe', fixture, output, settings], tag)
            content = output.read_bytes(); outputs.append(content)
            assert len(content.splitlines()) == 5889, (tag, len(content.splitlines()))
            coverage = list(map(int, content.splitlines()[-1].decode().split(':')[1:])); assert coverage == [5888, 1988]
            proofs.append({'mode': mode, 'configuration': spec, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'observations': len(content.splitlines()), 'successful_stream_writes': coverage[1],
                           'external_clock_instructions_changed': 1, 'all_other_application_resource_bytes_unchanged': True,
                           'whole_subject_and_bindings': evidence})
            print(target, mode, result, flush=True)
        assert outputs[0] == outputs[1] == outputs[2], ('Whole request/stream state differs', target)
        native_method = proofs[0]['whole_subject_and_bindings']['complete_query']
        comparisons = []
        for p in proofs[1:]:
            other = p['whole_subject_and_bindings']['complete_query']
            comparisons.append({'mode': p['mode'], 'same_complete_instructions_and_handlers': native_method['code'] == other['code'] and native_method['handlers'] == other['handlers'],
                                'full_literal_instruction_diff': list(difflib.unified_diff(native_method['code'], other['code'], lineterm='')),
                                'full_literal_handler_diff': list(difflib.unified_diff(native_method['handlers'], other['handlers'], lineterm=''))})
        builds.append({'target': target, 'native_sha256': recover.sha(native), 'raw_sha256': recover.sha(raw),
                       'delivered_sha256': recover.sha(optimized), 'proofs': proofs, 'different_observations': 0,
                       'complete_query_comparisons': comparisons})
    report = {'scope': 'Complete Xtraz outgoing queries on three May RU platforms, native/raw/optimized. Actual XML escape, '
                       'Options account lookup, counter, request builder, SnacPacket, SOCKETConnection.sendPacket, Traffic '
                       'and close/error handling execute. Only one external System clock instruction changes; every other '
                       'dumped declaration/instruction/handler in its class and every other class/resource byte stay '
                       'unchanged. MIDlet host/manifest, typed option table, counters and natural observing/faulting output '
                       'streams are explicit inputs. All three actual native/raw callers pass ID zero; the native XML '
                       'append uses constant zero before slot 1 is overwritten, and modern optimization removes that '
                       'argument and relocates the helper to Icon. The identical three-instruction native/raw Xtraz '
                       'constructor and removal of the authoring class stay explicit; Icon constructors are not treated '
                       'as Xtraz constructors. These descriptors/owners and complete callers remain '
                       'explicit, without claiming an unused local after optimizer slot reuse. Seven targets, five clock '
                       'values, four counters, three FLAP sequences, both CP1251 modes and seven stream/channel states '
                       'cover writes/flush IOExceptions, unchecked/Error failures, null stream/channel and malformed UIN '
                       'with partial state/bytes and stream-monitor ownership. Four account strings add XML/Unicode/null '
                       'coverage. Arbitrary nonzero transaction IDs, incoming Xtraz, UI/scheduling, live network/server, '
                       'physical devices and whole-program equivalence are outside scope.', 'builds': builds}
    (ROOT / 'preservation/reports/source-xtraz-queries.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS Xtraz queries: 17667 observations on each native/raw/optimized version; zero differences')
    return report


if __name__ == '__main__': main()
