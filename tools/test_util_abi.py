#!/usr/bin/env python3
"""Replay the four real ABI-specialized helpers in all nine RU artifacts."""
import hashlib
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from audit_util_abi import method, MEMBERS, OWNER
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def fingerprint(value):
    return hashlib.sha256(json.dumps(value, sort_keys=True, separators=(',', ':')).encode()).hexdigest()


def entries(path):
    with zipfile.ZipFile(path) as jar: return {n: jar.read(n) for n in jar.namelist()}


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-util-abi-dump')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', cp, '-d', TEST,
         ROOT / 'tools/source/UtilAbiFixture.java', ROOT / 'tools/source/UtilAbiProbe.java'], 'compile-util-abi-test')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST,
         ROOT / 'tools/source/UtilAbiClock.java'], 'compile-util-abi-clock')
    bytecode_path = ROOT / 'preservation/reports/source-util-abi-bytecode.json'
    bytecode = json.loads(bytecode_path.read_text(encoding='utf-8')); builds = []
    for build in bytecode['builds']:
        target = build['target']; outputs = {}; evidence = []
        for mode in ['reference', 'raw', 'optimized']:
            old = {'MIDP2': 'MIDP2', 'MOTOROLA': 'Moto', 'SIEMENS2': 'Siemens2'}[target]
            paths = dict(reference=ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar',
                         raw=ROOT / f'build/source/{target}-RU/classes.jar', optimized=ROOT / f'dist/source/Jimm-{target}-RU.jar')
            path = paths[mode]; owner = build['configuration']['util'] if mode == 'reference' else OWNER
            before = classes(path, cp); subjects = []; settings = dict(mode=mode, target=target, util=owner.replace('/', '.'),
                                                                      options=build['configuration']['options'] if mode == 'reference' else 'jimm.Options')
            for source_name, native_name, desc, raw_desc, count in MEMBERS:
                recorded = next(p for p in build['pairs'] if p['source_name'] == source_name)
                saved = next(e for e in recorded['evidence'] if e['mode'] == mode)
                assert saved['input_sha256'] == recover.sha(path)
                actual = method(before, owner, saved['complete_method']['name'], saved['complete_method']['desc'])
                assert actual == saved['complete_method']
                subjects.append(dict(source_name=source_name, owner=owner, complete_actual_method=actual))
                key = {'createCurrentDate': 'date', 'getDataInputStream': 'factory', 'getWord': 'word', 'writeDWord': 'dword'}[source_name]
                settings[key] = actual['name']
            date_core = next(e for e in build['whole_date_core_bindings'] if e['mode'] == mode)
            settings['core'] = '' if date_core.get('core_absent') else date_core['complete_method']['name']
            captured = subjects[0]['complete_actual_method'] if date_core.get('core_absent') else date_core['complete_method']
            assert captured == method(before, owner, captured['name'], captured['desc'])
            input_path = path
            if mode == 'raw':
                input_path = TEST / f'util-abi-{target}-raw-input.jar'
                values = {n: b for n, b in entries(paths['optimized']).items() if not n.endswith('.class')}; values.update(entries(path))
                write_jar(input_path, values); assert entries(input_path) == values
            fixture = TEST / f'util-abi-{target}-{mode}.jar'
            run([recover.java(), '-cp', recover.cp([TEST, *[CACHE / n for n in recover.ASM]]), 'UtilAbiFixture',
                 input_path, fixture, owner, captured['name'], captured['desc'], TEST], f'util-abi-fixture-{target}-{mode}')
            after = classes(fixture, cp); original = {c['name']: c for c in before}; changed = {c['name']: c for c in after}
            assert set(changed) - set(original) == {'UtilAbiClock', 'UtilAbiClock$Clock'} and set(original) <= set(changed)
            expected = dict(captured); captures = []; expected['code'] = []
            replacements = {'187 java/util/Date': '187 UtilAbiClock$Clock',
                            '183 java/util/Date.<init>()V': '183 UtilAbiClock$Clock.<init>()V'}
            for i, instruction in enumerate(captured['code']):
                replacement = replacements.get(instruction, instruction); expected['code'].append(replacement)
                if replacement != instruction: captures.append(dict(position=i, before=instruction, after=replacement))
            assert len(captures) == 2
            expected['refs'] = [s for s in expected['code'] if 178 <= int(s.split(' ')[0]) <= 185]
            actual = method(after, owner, captured['name'], captured['desc']); assert actual == expected
            unchanged = []
            for name, declaration in original.items():
                expected_class = dict(declaration)
                if name == owner:
                    expected_class['methods'] = [expected if (m['name'], m['desc']) == (captured['name'], captured['desc']) else m for m in declaration['methods']]
                assert changed[name] == expected_class, ('Unexpected fixture class/member/handler change', name)
                unchanged.append(dict(owner=name, original_whole_class_sha256=fingerprint(declaration),
                                      expected_whole_class_sha256=fingerprint(expected_class), actual_whole_class_sha256=fingerprint(changed[name])))
            input_entries, fixture_entries = entries(input_path), entries(fixture)
            removed = sorted(set(input_entries) - set(fixture_entries)); added = sorted(set(fixture_entries) - set(input_entries))
            changed_entries = sorted(n for n in input_entries if n in fixture_entries and input_entries[n] != fixture_entries[n])
            assert not removed and changed_entries == [owner + '.class'] and added == ['UtilAbiClock$Clock.class', 'UtilAbiClock.class']
            # Every stream method is unchanged even in the clock fixture; only the chosen real date method is captured.
            for subject in subjects[1:]: assert method(after, owner, subject['complete_actual_method']['name'], subject['complete_actual_method']['desc']) == subject['complete_actual_method']
            props = TEST / f'util-abi-{target}-{mode}.properties'
            props.write_text(''.join(k + '=' + v + '\n' for k, v in settings.items()), encoding='utf-8', newline='\n')
            prefix = TEST / f'util-abi-{target}-{mode}'
            result = run([recover.java(), '-Xmx512m', '-Djava.awt.headless=true', '-cp', TEST, 'UtilAbiProbe',
                          fixture, CACHE, props, prefix], f'util-abi-probe-{target}-{mode}')
            output = {}; outputs[mode] = {}
            for role, count in [('streams', 19202), ('dates', 4400), *([] if target == 'MOTOROLA' else [('date-only', 4400)])]:
                out = TEST / (prefix.name + '-' + role + '.txt'); values = out.read_bytes(); assert len(values.splitlines()) == count
                outputs[mode][role] = values; output[role] = dict(observations=count, sha256=recover.sha(out))
            evidence.append(dict(mode=mode, input_sha256=recover.sha(path), raw_with_resources_input_sha256=recover.sha(input_path) if mode == 'raw' else None,
                                 fixture_sha256=recover.sha(fixture), configuration=settings, whole_subject_methods=subjects,
                                 date_capture=dict(owner=owner, complete_original_method=captured, complete_expected_method=expected,
                                                   complete_fixture_method=actual, exactly_two_external_constructor_captures=captures),
                                 all_original_classes_bound_to_exact_expected_metadata_and_methods=unchanged,
                                 changed_entries=changed_entries, added_entries=added, removed_entries=removed,
                                 provider_class_sha256={n: hashlib.sha256(fixture_entries[n]).hexdigest() for n in added}, outputs=output, result=result))
            print(target, mode, result, flush=True)
        assert outputs['reference'] == outputs['raw'] == outputs['optimized']
        builds.append(dict(target=target, evidence=evidence, all_complete_output_bytes_equal=True))
    report = dict(scope='The four actual Util helpers execute in all nine native/raw/optimized RU artifacts. '
                  'Stream factory retains zero offset and observes array alias mutations, empty/null arrays and unread bytes. '
                  'Word reads retain the false domain and record exact InputStream read calls, scripted IOException/EOF/null, cursor and remaining bytes. '
                  'Dword writes retain false and observe full integer write arguments/order, before/after unchecked failures at all four writes, prefix/partial bytes and null. '
                  'All stream subjects are completely unchanged. Only two Date allocation/constructor instructions in one real date core/wrapper are captured; '
                  'all remaining classes, methods, metadata/handlers and original entries match exactly, with only two explicit provider classes added. '
                  'Date wrapper false executes in all nine artifacts, core false/true in six MIDP2/Siemens artifacts. '
                  'Motorola optimized core is absent, and no direct core comparison is invented there. Controlled JVM Calendar/Locale/TimeZone inputs, '
                  '11 epochs including extreme longs, four zones, ten GMT offsets and ten local offsets establish this host dataset only. '
                  'Every date call reads the external clock exactly once. No generic true GMT/big-endian/nonzero offset, physical J2ME calendar or whole-program equivalence claim.',
                  bytecode_report_sha256=recover.sha(bytecode_path), builds=builds)
    (ROOT / 'preservation/reports/source-util-abi-replay.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS Util ABI replay: all nine artifacts agree on complete stream/date outputs; unchanged stream bodies and exactly two date captures')
    return report


if __name__ == '__main__': main()
