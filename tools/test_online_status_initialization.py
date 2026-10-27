#!/usr/bin/env python3
"""Check May's lazy OtherAction initialization during a disconnected status update."""
import argparse
import json
import os
import zipfile

import audit_source as audit
import recover
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def prefix(method):
    code = method['code']
    return (code[:3] == ['17 192', '21 0', '133']
            and code[3].startswith('184 ') and code[3].endswith('(IJ)V')
            and code[4].startswith('184 ') and code[4].endswith('()V'))


def main(record_before=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    audit.OUT.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / name for name in ['microemu.jar', 'microemu-nokiaui.jar',
                                       'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    cp = recover.cp([CACHE / name for name in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + name + '.java') for name in
           ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe',
            'OnlineStatusInitializationFixture', 'OnlineStatusInitializationProbe']]],
        'compile-online-status-initialization')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST,
         ROOT / 'tools/source/OnlineStatusInitializationEvents.java'], 'compile-online-status-events')
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-online-status-dump')

    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    raw = ROOT / 'build/source/MIDP2-RU/classes.jar'
    delivered = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    raw_host = TEST / 'online-status-raw-input.jar'
    with zipfile.ZipFile(delivered) as z:
        entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(raw) as z:
        entries.update({n: z.read(n) for n in z.namelist()})
    write_jar(raw_host, entries)
    results, proofs = [], []
    for mode, path in [('reference', original), ('raw', raw_host), ('optimized', delivered)]:
        reference = mode == 'reference'
        variant = 'reference' if reference else 'source'
        tag = 'online-status-initialization-' + mode
        host, fixture, undone = [TEST / (tag + suffix + '.jar') for suffix in ['-host', '-fixture', '-undone']]
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture',
             path, host, TEST, 'unused', 'false', 'unused'], tag + '-host')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'OnlineStatusInitializationFixture', host, fixture, TEST, variant], tag + '-fixture')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'OnlineStatusInitializationFixture', fixture, undone, TEST, variant, 'undo'], tag + '-undo')
        owner = 'bq' if reference else 'jimm/comm/OtherAction'
        with zipfile.ZipFile(host) as before, zipfile.ZipFile(fixture) as after:
            assert [n for n in before.namelist() if before.read(n) != after.read(n)] == [owner + '.class']
            assert set(after.namelist()) - set(before.namelist()) == {'OnlineStatusInitializationEvents.class'}
        before_dump, after_dump, undone_dump = [classes(p, cp) for p in [host, fixture, undone]]
        assert before_dump == undone_dump, ('Inverse observer changed an application body/declaration', mode)
        icq_owner = 'r' if reference else 'jimm/comm/Icq'
        whole_icq = next(c for c in before_dump if c['name'] == icq_owner)
        assert whole_icq == next(c for c in after_dump if c['name'] == icq_owner)
        proofs.append(dict(mode=mode, input_sha256=recover.sha(path), fixture_sha256=recover.sha(fixture),
                           inverse_restores_all_dumped_application_members=True,
                           all_other_application_and_resource_entries_exact=True,
                           whole_icq_class=whole_icq))
        for fault in ['false', 'true']:
            output = TEST / (tag + '-' + fault + '.txt')
            run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                 '-cp', recover.cp([TEST, *runtime]), 'OnlineStatusInitializationProbe',
                 fixture, output, variant, fault], tag + '-' + fault)
            line = output.read_text().strip()
            if not record_before:
                assert line == 'before=0:after=0:result=ok:status=32', (mode, fault, line)
            results.append(dict(mode=mode, injected_initializer_failure=fault == 'true', observation=line))

    methods = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            native_path = ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_' + language) / 'Jimm.jar'
            built_path = ROOT / 'dist/source' / ('Jimm-' + target + '-' + language + '.jar')
            native, built = classes(native_path, cp), classes(built_path, cp)
            candidates = [(c, m) for c in native for m in c['methods']
                          if m['desc'] == '(I)V' and m['access'] & 8 and prefix(m)
                          and m['code'][5].startswith('184 ') and m['code'][5].endswith('()Z')]
            assert len(candidates) == 1, (target, language, 'May status prefix')
            c, native_method = candidates[0]
            source_class = next(c for c in built if c['name'] == 'jimm/comm/Icq')
            source_method = next(m for m in source_class['methods']
                                 if (m['name'], m['desc']) == ('setOnlineStatus', '(I)V'))
            status_calls = [s for s in native_method['code'] if s.startswith('184 ') and s.endswith('(I)V')]
            assert len(status_calls) == 1, (target, language, 'OtherAction status call')
            other_owner = status_calls[0].split(' ', 1)[1].split('.', 1)[0]
            native_other = next(c for c in native if c['name'] == other_owner)
            native_fields = [f for f in native_other['fields'] if f['desc'] == '[B']
            assert len(native_fields) == 1, (target, language, 'Native status data field')
            source_other = next(c for c in built if c['name'] == 'jimm/comm/OtherAction')
            source_field = next(f for f in source_other['fields'] if f['name'] == 'CLI_SETSTATUS_DATA' and f['desc'] == '[B')
            if not record_before:
                assert prefix(source_method), (target, language, 'Eager access before saving status')
                assert native_fields[0]['access'] == source_field['access'] == 10
            methods.append(dict(target=target, language=language, native_owner=c['name'],
                                native_sha256=recover.sha(native_path), built_sha256=recover.sha(built_path),
                                native_method=native_method, built_method=source_method,
                                native_status_data_field=native_fields[0], built_status_data_field=source_field,
                                same_option_save_prefix=prefix(source_method)))
    report = dict(
        scope='Disconnected Icq.setOnlineStatus(32) in original, raw and delivered MIDP2-RU, each in a fresh '
              'MicroEmulator process with typed options, memory RMS, real date/persistence/contact-list code. '
              'Only OtherAction.<clinit> receives an entry observer, optionally throwing a scripted exception. '
              'Its inverse restores all dumped classes/methods/handlers; Icq and all other application/resource '
              'bytes are unchanged by the observer. An injected failure demonstrates ordering, not a claim '
              'that the original initializer naturally fails. Connected/network paths and full program '
              'equivalence are outside this test. Full status methods and exact initial option-save sequence '
              'are separately retained for all 15 original/delivered variants.',
        observations=results, fixture_proofs=proofs, platform_methods=methods,
        input_sha256=dict(original=recover.sha(original), raw=recover.sha(raw), delivered=recover.sha(delivered)),
        recording_before=record_before)
    suffix = '-before' if record_before else ''
    (ROOT / ('preservation/reports/source-online-status-initialization' + suffix + '.json')).write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(('RECORDED before' if record_before else 'PASS') +
          ' online status initialization: 6 runtime observations; 15 complete status method pairs')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--record-before', action='store_true')
    main(parser.parse_args().record_before)
