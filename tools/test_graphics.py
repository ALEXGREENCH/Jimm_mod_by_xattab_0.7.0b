#!/usr/bin/env python3
"""Compare actual sprite/animation/bitmap-font code with all three May platform JARs."""
import argparse
import json
import os
import sys
import zipfile
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-graphics.json'


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    targets = [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]
    if not skip_build:
        for target, old in targets:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target], 'graphics-build-' + target)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / 'tools/source' / n for n in ['GraphicsProbe.java', 'GraphicsFixture.java', 'GraphicsIO.java']]], 'compile-graphics')
    report = {'scope': 'Real Icon, ImageList, AniIcon and AniImageList on all three platforms; '
                       'all UTF-16 character widths, colored bitmap glyphs and TextList integration on Motorola. '
                       'Pixels are measured in MicroEmulator. Animation clock, sleep, current-screen and refresh boundaries are captured. '
                       'The test MIDlet isolates application startup; this is not a physical-device or scheduler test.', 'builds': []}
    for target, old in targets:
        artifact = ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        classes = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old + '_RU') / 'Jimm.jar'
        with zipfile.ZipFile(artifact) as jar:
            data = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(classes) as jar:
            data.update({n: jar.read(n) for n in jar.namelist() if n.endswith('.class')})
        rebuilt = TEST / ('graphics-' + target + '-unoptimized.jar')
        recover.write_jar(rebuilt, data)
        results = {}
        for mode, original in [('reference', reference), ('source', rebuilt)]:
            fixture = TEST / ('graphics-' + target + '-' + mode + '.jar')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'GraphicsFixture', original, fixture, mode, target, TEST], 'graphics-fixture-' + target + '-' + mode)
            results[mode] = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                                 '-cp', recover.cp([TEST, *runtime]), 'GraphicsProbe', fixture, mode,
                                 TEST / ('graphics-' + target + '-' + mode + '.txt'), target], 'graphics-' + target + '-' + mode)
        left = (TEST / ('graphics-' + target + '-reference.txt')).read_bytes()
        right = (TEST / ('graphics-' + target + '-source.txt')).read_bytes()
        if left != right:
            raise AssertionError('Graphics mismatch: compare build/source-tests/graphics-' + target + '-{reference,source}.txt')
        rows = left.decode('utf-8').splitlines()
        frames = int(rows[-1].split(':')[1])
        assert results['reference'] == results['source'] == 'PASS graphics: ' + str(len(rows)) + ' observations, ' + str(frames) + ' frames'
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference),
                                'source_jar_sha256': recover.sha(artifact), 'source_unoptimized_class_jar_sha256': recover.sha(classes),
                                'source_unoptimized_with_resources_sha256': recover.sha(rebuilt),
                                'observations': len(rows), 'raster_frames': frames, 'differences': 0})
    report['bytecode'] = run([sys.executable, ROOT / 'tools/audit_graphics.py'], 'graphics-bytecode-audit')
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS graphics: 3 platforms, ' + str(sum(b['observations'] for b in report['builds'])) +
          ' observations, ' + str(sum(b['raster_frames'] for b in report['builds'])) + ' frames')
    print(report['bytecode'])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true', help='Use the existing full RU builds for all three platforms')
    main(parser.parse_args().skip_build)
