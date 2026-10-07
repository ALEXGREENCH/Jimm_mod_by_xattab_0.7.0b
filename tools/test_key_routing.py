#!/usr/bin/env python3
"""Compare May key-event ordering on all three platforms and Util's write diagnostics."""
import argparse
import json
import os
import sys
import zipfile
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-key-routing.json'


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / 'tools/source' / name for name in ['KeyRoutingFixture.java', 'KeyRoutingProbe.java', 'KeyRoutingIO.java', 'UtilWriteProbe.java']]], 'compile-key-routing')
    report = {'scope': 'Actual VirtualList key entrypoints/doKeyreaction on three platforms. Its static initialization is suppressed; '
                       'an allocated abstract-method shim provides only a receiver. Immediate light, option read, status timer and '
                       'private keyReaction call sites are captured with checked site counts; the real callback interface is a proxy. '
                       'Switches, call ordering and exception propagation remain real. No full keyReaction, class initialization, '
                       'physical brightness or scheduler claim. Separate genuine Util.writeByteArray calls compare stream bytes, '
                       'partial writes, virtual diagnostic dispatch, stderr presence and exceptions.', 'builds': []}
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        if not skip_build:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target], 'key-routing-build-' + target)
        classes = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old + '_RU') / 'Jimm.jar'
        for mode, artifact in [('reference', reference), ('source', classes)]:
            fixture = TEST / ('key-routing-' + target + '-' + mode + '.jar')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'KeyRoutingFixture', artifact, fixture, mode, TEST, target], fixture.stem + '-fixture')
            output = TEST / ('key-routing-' + target + '-' + mode + '.txt')
            result = run([recover.java(), '-cp', recover.cp([TEST, *runtime]), 'KeyRoutingProbe', fixture, mode, output, CACHE, target], output.stem)
            count = len(output.read_text(encoding='utf-8').splitlines())
            assert count == 3872
            assert result == 'PASS key routing: ' + str(count) + ' observations'
        left = TEST / ('key-routing-' + target + '-reference.txt')
        right = TEST / ('key-routing-' + target + '-source.txt')
        if left.read_bytes() != right.read_bytes():
            raise AssertionError('Key routing mismatch: ' + target)
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference),
                                 'source_unoptimized_class_jar_sha256': recover.sha(classes),
                                 'source_jar_sha256': recover.sha(ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')),
                                 'observations': count, 'differences': 0})
    for mode, artifact in [('reference', ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),
                           ('source', ROOT / 'build/source/MIDP2-RU/classes.jar')]:
        output = TEST / ('utility-writes-' + mode + '.txt')
        result = run([recover.java(), '-cp', recover.cp([TEST, *runtime]), 'UtilWriteProbe', artifact, mode, output, CACHE], output.stem)
        assert result == 'PASS utility writes: 96 observations'
    assert (TEST / 'utility-writes-reference.txt').read_bytes() == (TEST / 'utility-writes-source.txt').read_bytes()
    report['utility_write_observations'] = 96
    report['utility_write_differences'] = 0
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS key routing: 3 platforms, ' + str(sum(b['observations'] for b in report['builds'])) + ' observations; 96 utility write observations')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    main(parser.parse_args().skip_build)
