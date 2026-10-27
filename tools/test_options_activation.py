#!/usr/bin/env python3
"""Execute complete untouched OptionsForm.activate and retain the May read frontier."""
import argparse
import json
import os
import subprocess
import zipfile

import audit_source as audit
import recover
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def inspect(path, native, required):
    cp = recover.cp([CACHE / n for n in recover.ASM])
    data = classes(path, cp)
    if native:
        subjects = [(c, m) for c in data for m in c['methods']
                    if m['desc'] == '()V' and m['code'][:2] == ['25 0', '6']
                    and len(m['code']) > 16 and m['code'][5] == '17 130']
        assert len(subjects) == 1, (path, 'Options activation')
        controller, subject = subjects[0]
    else:
        controller = next(c for c in data if c['name'] == 'jimm/OptionsForm')
        subject = next(m for m in controller['methods'] if (m['name'], m['desc']) == ('activate', '()V'))
    code = subject['code']
    option_owner = code[2].split(' ', 1)[1].split('.', 1)[0]
    assert code[:2] == ['25 0', '6'] and code[2].endswith('(I)Ljava/lang/String;')
    assert code[3].startswith('181 ' + controller['name'] + '.') and code[3].endswith(' Ljava/lang/String;')
    bool_reads = [(i, int(code[i - 1].split()[1])) for i, s in enumerate(code)
                  if s.startswith('184 ' + option_owner + '.') and s.endswith('(I)Z')
                  and i and code[i - 1].startswith('17 ')]
    bool_prefix = [value for _, value in bool_reads[:3]]
    sort_reads = [i for i, s in enumerate(code) if i and code[i - 1] == '16 65'
                  and s.startswith('184 ' + option_owner + '.') and s.endswith('(I)I')]
    sort_after_flags = len(sort_reads) == 1 and sort_reads[0] > bool_reads[2][0]
    state = dict(flags_read_in_may_order=bool_prefix == [130, 129, 136],
                 sort_read_after_flag_caches=sort_after_flags)
    if native or required:
        assert all(state.values()), (path, state)
    return dict(input_sha256=recover.sha(path), controller=controller['name'], whole_subject=subject,
                actual_boolean_reads=bool_reads, actual_sort_reads=sort_reads, invariants=state,
                controller_fields=controller['fields'])


def main(before=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    audit.OUT.mkdir(parents=True, exist_ok=True)
    if before:
        assert subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT).decode().strip().startswith('baa59b7')
        assert not subprocess.check_output(['git', 'diff', 'HEAD', '--', 'src'], cwd=ROOT)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / ('tools/source/' + n + '.java') for n in
                      ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe', 'OptionsActivationProbe']]], 'compile-options-activation')
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-options-activation-dump')
    builds = []
    for target, historical in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        raw = inspect(ROOT / ('build/source/' + target + '-RU/classes.jar'), False, not before)
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            original = ROOT / ('preservation/wayback-originals/Jimm_' + historical + '_' + language + '/Jimm.jar')
            delivered = ROOT / ('dist/source/Jimm-' + target + '-' + language + '.jar')
            builds.append(dict(target=target, language=language, native=inspect(original, True, True),
                               delivered=inspect(delivered, False, not before), raw_RU=raw))
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    raw = ROOT / 'build/source/MIDP2-RU/classes.jar'
    delivered = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    with zipfile.ZipFile(delivered) as z:
        entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(raw) as z:
        entries.update({n: z.read(n) for n in z.namelist()})
    raw_host = TEST / 'options-activation-raw-input.jar'
    write_jar(raw_host, entries)
    results, outputs = [], {}
    tag = 'before' if before else 'current'
    for mode, path in [('reference', original), ('raw', raw_host), ('optimized', delivered)]:
        host = TEST / ('options-activation-' + tag + '-' + mode + '-host.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture',
             path, host, TEST, 'unused', 'false', 'unused'], 'options-activation-' + mode + '-host')
        with zipfile.ZipFile(path) as a, zipfile.ZipFile(host) as b:
            assert all(a.read(n) == b.read(n) for n in a.namelist() if n.lower() != 'meta-inf/manifest.mf')
            assert set(b.namelist()) - set(a.namelist()) == {'GraphicsMIDlet.class'}
        output = TEST / ('options-activation-' + tag + '-' + mode + '.txt')
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'OptionsActivationProbe', host, output,
             'reference' if mode == 'reference' else 'source'], 'options-activation-' + mode)
        rows = output.read_text().splitlines()
        assert len(rows) == 82
        outputs[mode] = rows
        results.append(dict(mode=mode, input_sha256=recover.sha(path), host_sha256=recover.sha(host),
                            output_sha256=recover.sha(output), all_application_and_resource_bytes_unchanged=True,
                            observations=rows))
    differences = {mode: [dict(index=i, native=a, source=b) for i, (a, b) in enumerate(zip(outputs['reference'], outputs[mode])) if a != b]
                   for mode in ['raw', 'optimized']}
    assert all(len(v) == (20 if before else 0) for v in differences.values()), differences
    report = dict(scope='Whole OptionsForm.activate in untouched original/raw/delivered MIDP2-RU application classes. '
                  'Only the manifest changes and an inert host MIDlet is added; no application method, callsite or resource is instrumented. '
                  '82 explicit model states per version cover valid flag combinations, cached-state seeds, sort boundaries, null/wrong typed '
                  'options and two competing errors. Observe exception classes, four previous-state caches, real menu line counts and the full option table. '
                  'Corrupt states are supplied inputs, not a claim they all arise through ordinary physical UI/RMS operation. '
                  'All fifteen native/delivered and three raw read frontiers retain full bodies, fields and compiler differences; full-program equivalence is not claimed.',
                  source_sha256=recover.sha(ROOT / 'src/jimm/Options.java'), builds=builds, runtime=results,
                  all_differences=differences, baseline_commit='baa59b7569d905eba545023c6e08525ca6d8ed8b' if before else None)
    name = 'source-options-activation' + ('-before' if before else '') + '.json'
    (ROOT / 'preservation/reports' / name).write_text(json.dumps(report, indent=2) + '\n', encoding='utf8', newline='\n')
    print(('RECORDED before' if before else 'PASS') + ' options activation: 246 observations, 15 method pairs, three raw frontiers; ' + str(sum(map(len, differences.values()))) + ' differences')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--before', action='store_true')
    main(parser.parse_args().before)
