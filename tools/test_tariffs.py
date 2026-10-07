#!/usr/bin/env python3
"""Compare actual tariff forms after test_source.py has prepared the Options fixtures."""
import json
import os
import recover
from test_source import ROOT, TEST, CACHE, run


def main():
    prepared = json.loads((ROOT / 'preservation/reports/source-tests.json').read_text(encoding='utf-8'))
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    if prepared['source_jar_sha256'] != recover.sha(artifact):
        raise AssertionError('Run test_source.py for the current MIDP2-RU build before testing tariff fixtures')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, *runtime]),
         '-d', TEST, ROOT / 'tools/source/TariffProbe.java'], 'compile-tariffs')
    for mode in ['reference', 'source']:
        fixture = TEST / ('options-' + mode + '.jar')
        run([recover.java(), '-Djava.awt.headless=true',
             '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST, *runtime]),
             'TariffProbe', fixture, mode, TEST / ('tariff-' + mode + '.txt')], 'tariff-' + mode)
    left, right = [TEST / ('tariff-' + mode + '.txt') for mode in ['reference', 'source']]
    if left.read_bytes() != right.read_bytes():
        raise AssertionError('Tariff form mismatch: compare ' + str(left) + ' and ' + str(right))
    lines = left.read_text(encoding='utf-8').splitlines()
    assert lines[-1] == 'openings:260/saves:120'
    report = {'scope': 'Real MIDP2-RU OptionsForm and tariff helpers in the OptionsFixture prepared by test_source.py. '
                       'Native field constraints, initial prices, repeated edits, saved integer options, normalized field '
                       'text, menu transitions, reopened forms and serialized option bytes are compared. '
                       'The existing fixture captures persistence notification, surrounding UI/network calls and clocks; '
                       'no physical RMS write or complete optimized OptionsForm claim.',
              'reference_sha256': recover.sha(ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),
              'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(ROOT / 'build/source/MIDP2-RU/classes.jar'),
              'reference_fixture_sha256': recover.sha(TEST / 'options-reference.jar'),
              'source_fixture_sha256': recover.sha(TEST / 'options-source.jar'),
              'observations': len(lines), 'form_openings': 260, 'save_calls': 120, 'differences': 0}
    (ROOT / 'preservation/reports/source-tariffs.json').write_text(json.dumps(report, indent=2) + '\n',
                                                                encoding='utf-8', newline='\n')
    print('PASS tariff forms:', len(lines), 'observations, 260 openings, 120 saves')


if __name__ == '__main__':
    main()
