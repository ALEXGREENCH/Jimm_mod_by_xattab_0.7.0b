#!/usr/bin/env python3
"""Extend the real utility dataset to three platforms and bind About's real setters/task."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from audit_specialized_util_version import configuration, MEMBERS
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-specialized-util-dump')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-d', TEST, ROOT / 'tools/source/UtilCoreDifferentialTest.java'], 'compile-specialized-util-core')
    legacy = (TEST / 'util-core-coverage.txt').read_bytes(); builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        native = ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'; raw = ROOT / f'build/source/{target}-RU/classes.jar'; delivered = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        assembled = TEST / f'specialized-util-{target}-raw.jar'
        with zipfile.ZipFile(delivered) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(assembled, entries)
        with zipfile.ZipFile(assembled) as z: assert {n: z.read(n) for n in z.namelist()} == entries
        native_data = classes(native, cp); spec = configuration(native_data)
        settings = TEST / f'specialized-util-{target}.properties'; settings.write_text(''.join(k + '=' + v + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
        inventory = TEST / f'specialized-util-{target}-coverage.txt'
        result = run([recover.java(), '-Xmx512m', '-cp', TEST, 'UtilCoreDifferentialTest', native, assembled, CACHE, inventory, settings], 'specialized-util-' + target)
        assert result == 'PASS utility core: 1595742 observations, 60 real method pairs'
        assert inventory.read_bytes() == legacy, 'All original per-method invocation counts must remain'
        subjects = []
        for mode, data in [('reference', native_data), ('raw', classes(raw, cp))]:
            owner = spec['util'] if mode == 'reference' else 'jimm/comm/Util'; c = next(c for c in data if c['name'] == owner)
            for role, desc, source_name in MEMBERS:
                m = next(m for m in c['methods'] if m['name'] == ('a' if mode == 'reference' else source_name) and m['desc'] == desc)
                subjects.append(dict(mode=mode, role=role, owner=owner, complete_method=m))
        builds.append(dict(target=target, reference_sha256=recover.sha(native), raw_class_jar_sha256=recover.sha(raw), resource_jar_sha256=recover.sha(delivered),
                           assembled_input_sha256=recover.sha(assembled), exact_raw_class_and_resource_entry_dictionary_preserved=True,
                           configuration=spec, observations=1595742, real_method_pairs=60, coverage_sha256=recover.sha(inventory),
                           all_legacy_per_method_invocation_counts_retained=True, whole_actual_specialized_subject_methods=subjects))
        print(target, result, flush=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    about = []; outputs = []
    for mode, legacy_mode in [('reference', 'reference'), ('raw', 'source')]:
        path = ROOT / ('preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar' if mode == 'reference' else 'build/source/MIDP2-RU/classes.jar')
        fixture = TEST / ('about-' + legacy_mode + '.jar'); output = TEST / ('specialized-version-' + mode + '.txt')
        before = {c['name']: c for c in classes(path, cp)}; after = {c['name']: c for c in classes(fixture, cp)}
        owner = 'cf' if mode == 'reference' else 'jimm/JimmUI'; setters = []
        for name in ['a', 'b'] if mode == 'reference' else ['access$0', 'access$1']:
            desc = '(Ljava/lang/String;)Ljava/lang/String;' if mode == 'reference' else '(Ljava/lang/String;)V'
            a = next(m for m in before[owner]['methods'] if m['name'] == name and m['desc'] == desc)
            b = next(m for m in after[owner]['methods'] if m['name'] == name and m['desc'] == desc); assert a == b
            setters.append(dict(owner=owner, complete_method=a, complete_fixture_method=b, complete_metadata_body_and_handlers_equal=True))
        task_owner = 'g' if mode == 'reference' else 'jimm/JimmUI$GetVersionInfoTimerTask'
        a = next(m for m in before[task_owner]['methods'] if m['name'] == 'run' and m['desc'] == '()V'); b = next(m for m in after[task_owner]['methods'] if m['name'] == 'run' and m['desc'] == '()V')
        expected = dict(a); captures = []
        mapping = {'184 javax/microedition/io/Connector.open(Ljava/lang/String;)Ljavax/microedition/io/Connection;': '184 AboutIO.open(Ljava/lang/String;)Ljavax/microedition/io/Connection;',
                   '184 java/lang/Thread.yield()V': '184 AboutIO.yieldThread()V', '182 java/lang/Thread.setPriority(I)V': '184 AboutIO.setPriority(Ljava/lang/Thread;I)V'}
        expected['code'] = []
        for i, instruction in enumerate(a['code']):
            replacement = mapping.get(instruction, instruction)
            if replacement != instruction: captures.append(dict(position=i, before=instruction, after=replacement))
            expected['code'].append(replacement)
        expected['refs'] = [s for s in expected['code'] if 178 <= int(s.split(' ')[0]) <= 185]
        assert captures and expected == b, 'Only the explicit external task operations may be rewritten'
        with zipfile.ZipFile(path) as original, zipfile.ZipFile(fixture) as captured:
            changed = sorted(n for n in original.namelist() if n in captured.namelist() and original.read(n) != captured.read(n))
            added = sorted(set(captured.namelist()) - set(original.namelist())); removed = sorted(set(original.namelist()) - set(captured.namelist()))
        result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST, *runtime]), 'AboutProbe', fixture, legacy_mode, output], 'specialized-version-' + mode)
        content = output.read_bytes(); outputs.append(content); assert len(content.splitlines()) == 673
        assert content == (TEST / ('about-' + legacy_mode + '.txt')).read_bytes()
        about.append(dict(mode=mode, input_sha256=recover.sha(path), fixture_sha256=recover.sha(fixture), output_sha256=recover.sha(output), observations=673,
                          whole_unchanged_version_setters=setters, task_owner=task_owner, complete_original_task=a, complete_expected_task=expected, complete_fixture_task=b,
                          explicit_external_task_captures=captures, all_remaining_task_metadata_instructions_and_handlers_equal=True,
                          changed_entries=changed, added_entries=added, removed_entries=removed))
        print('About', mode, result, flush=True)
    assert outputs[0] == outputs[1]
    report = dict(scope='Existing complete utility-core dataset now runs native/raw classes on all three RU platforms: 4787226 comparisons, 60 real method pairs/platform, all original per-method invocation counts retained. '
                  'Only optional native Util/Options names were added to the existing Java harness; its original four-argument interface remains. Unmodified raw class byte entries and delivered resources are assembled exactly; no application method rewrite. '
                  'Isolated JVM loaders and explicit Options object-table CP1251 inputs retain the original boundary. Array/default/tag comparisons use the specialized zero/490 domains, not arbitrary raw variants. '
                  'The existing full MIDP2 About dataset repeats 673 observations/version only after complete current version/aboutNotice accessors and task run body are bound to its fixture. '
                  'Setters are unmodified; the task has only enumerated external Connector/Thread rewrites, with all other metadata/code/handlers equal. HTTP lengths/chunks/status/errors/partial close, actual version/notice/UI state retain prior scope. '
                  'Inherited application/Options-save/host captures and every changed/added/removed entry remain explicit; this is not an unchanged whole application fixture. '
                  'No optimized utility runtime, three-platform About runtime, live HTTP/server/device, arbitrary setter-return ABI or whole-program equivalence claim is made. No product source/JAR change.', builds=builds, about_replay=about)
    (ROOT / 'preservation/reports/source-specialized-util-version-replay.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS specialized Util/version replay: 4787226 utility observations and 673 About observations per native/raw version; complete current bindings retained')
    return report


if __name__ == '__main__': main()
