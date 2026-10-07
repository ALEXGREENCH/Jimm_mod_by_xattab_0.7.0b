#!/usr/bin/env python3
"""Compare the real converter, initialization, rule parser and Character fallbacks."""
import hashlib
import json
import os
import zipfile
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT


def main():
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    helpers = TEST / 'convertor-helpers'
    helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', CACHE / 'asm.jar', '-d', helpers,
         *[ROOT / 'tools/source' / n for n in ['ConvertorIO.java', 'ConvertorFixture.java', 'ConvertorProbe.java']]], 'compile-convertor')
    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    source = ROOT / 'build/source/MIDP2-RU/classes.jar'
    built = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    resources = []
    results = []
    for mode, artifact, resource_jar in [('reference', reference, reference), ('source', source, built)]:
        with zipfile.ZipFile(resource_jar) as jar:
            resources.append(hashlib.sha256(jar.read('replaces.txt')).hexdigest())
        fixture = TEST / ('convertor-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([helpers, CACHE / 'asm.jar']), 'ConvertorFixture', artifact, fixture, mode, helpers], fixture.stem + '-fixture')
        results.append(run([recover.java(), '-cp', helpers, 'ConvertorProbe', fixture, artifact, mode, CACHE,
                            TEST / ('convertor-' + mode + '.txt'), resource_jar], 'convertor-' + mode))
    left, right = [TEST / ('convertor-' + mode + '.txt') for mode in ['reference', 'source']]
    if left.read_bytes() != right.read_bytes():
        aa, bb = [p.read_text(encoding='utf-8').splitlines() for p in [left, right]]
        for i, (a, b) in enumerate(zip(aa, bb)):
            if a != b:
                print('First converter mismatch:', i, a[:500], b[:500])
                break
        raise AssertionError('Converter mismatch: compare build/source-tests/convertor-{reference,source}.txt')
    assert results[0] == results[1] and resources[0] == resources[1]
    guard = left.read_text(encoding='utf-8').splitlines()[-1]
    conversions, tables = [int(part.split(':')[1]) for part in guard.split('/')]
    report = {'scope': 'Real MIDP2 StringConvertor initialization, resource decoding, rule parsing, constructors, '
                       'table copies/aliases, longest matches, duplicate precedence, case preservation and public conversions. '
                       'Only resource opening and Character results are captured. Character modes delegate to the host, '
                       'model ASCII-only conversion, or return the input; the actual converter fallback executes in all cases. '
                       'Streams script available/read/close and partial reads. Results retain raw UTF-16 code units. '
                       'Named zero-width tables are inspected but their nonempty conversion path is excluded: '
                       'the original and source loop cannot advance. No real-device Character contract or full editor integration claim.',
              'reference_sha256': recover.sha(reference), 'source_unoptimized_class_jar_sha256': recover.sha(source),
              'source_resource_jar_sha256': recover.sha(built), 'reference_table_sha256': resources[0], 'source_table_sha256': resources[1],
              'observations': len(left.read_text(encoding='utf-8').splitlines()),
              'conversion_calls': conversions, 'parsed_tables': tables, 'differences': 0}
    (ROOT / 'preservation/reports/source-convertor.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(results[1])


if __name__ == '__main__':
    main()
