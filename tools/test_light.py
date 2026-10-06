#!/usr/bin/env python3
"""Compare real May Nokia/Motorola backlight controllers and scheduled tasks."""
import argparse
import json
import os
import sys
import zipfile
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-light.json'


def main(skip_build=False, seeds=None):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    targets = [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto')]
    if not skip_build:
        for target, old in targets:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target], 'light-build-' + target)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / 'tools/source' / n for n in ['GraphicsProbe.java', 'GraphicsFixture.java', 'LightProbe.java', 'LightFixture.java']]], 'compile-light')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8', '-cp', recover.cp([TEST, *runtime]),
         '-d', TEST, ROOT / 'tools/source/LightIO.java'], 'compile-light-io')
    report = {'scope': 'Real backlight controllers and TimerTask bodies. Scripted device predicate, hardware result/errors, clock and task delivery; '
                       'real TimerTask cancellation state. A read-only observer exposes the light flag before option 74 is read in class initialization. '
                       'Unoptimized source is used for behavioral comparison; optimized signatures are audited separately. '
                       'No physical brightness, real-time delivery or Siemens LightControl claim.', 'builds': []}
    for target, old in targets:
        artifact = ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        classes = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old + '_RU') / 'Jimm.jar'
        with zipfile.ZipFile(artifact) as jar:
            data = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(classes) as jar:
            data.update({n: jar.read(n) for n in jar.namelist() if n.endswith('.class')})
        rebuilt = TEST / ('light-' + target + '-unoptimized.jar')
        recover.write_jar(rebuilt, data)
        for mode, original in [('reference', reference), ('source', rebuilt)]:
            fixture = TEST / ('light-' + target + '-' + mode + '.jar')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'LightFixture', original, fixture, mode, target, TEST], fixture.stem + '-fixture')
        observations = 0
        for seed in seeds if seeds is not None else [-1, 0, 1, 10, 2147483, 2147484, 2147483647, -2147483648]:
            for mode in ['reference', 'source']:
                name = 'light-' + target + '-' + str(seed) + '-' + mode
                fixture = TEST / ('light-' + target + '-' + mode + '.jar')
                result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                              '-cp', recover.cp([TEST, *runtime]), 'LightProbe', fixture, mode, TEST / (name + '.txt'), target, str(seed)], name)
                rows = (TEST / (name + '.txt')).read_text(encoding='utf-8').splitlines()
                assert result == 'PASS light: ' + str(len(rows)) + ' observations'
            left = TEST / ('light-' + target + '-' + str(seed) + '-reference.txt')
            right = TEST / ('light-' + target + '-' + str(seed) + '-source.txt')
            if left.read_bytes() != right.read_bytes():
                raise AssertionError('Backlight mismatch: compare ' + left.name + ' and ' + right.name)
            observations += len(left.read_text(encoding='utf-8').splitlines())
        report['builds'].append({'target': target, 'timeout_seeds': seeds if seeds is not None else [-1, 0, 1, 10, 2147483, 2147484, 2147483647, -2147483648],
                                'reference_sha256': recover.sha(reference), 'source_jar_sha256': recover.sha(artifact),
                                'source_unoptimized_class_jar_sha256': recover.sha(classes), 'observations': observations, 'differences': 0})
    report['bytecode'] = run([sys.executable, ROOT / 'tools/audit_light.py'], 'light-bytecode-audit')
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS light: 2 platforms, ' + str(sum(b['observations'] for b in report['builds'])) + ' observations')
    print(report['bytecode'])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    parser.add_argument('--seed', type=int, help='Focused run for one initial timeout')
    args = parser.parse_args()
    main(args.skip_build, None if args.seed is None else [args.seed])
