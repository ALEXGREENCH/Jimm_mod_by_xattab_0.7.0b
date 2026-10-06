#!/usr/bin/env python3
"""Test maintained src against the original May 12 release; no network login."""
import argparse
import json
import os
from pathlib import Path
import subprocess
import sys
import zipfile
import recover
from build_source import write_jar

ROOT = recover.ROOT
CACHE = recover.CACHE
TEST = ROOT / 'build/source-tests'
REPORT = ROOT / 'preservation/reports/source-tests.json'


def run(args, name):
    result = subprocess.run([str(a) for a in args], cwd=ROOT, stdout=subprocess.PIPE,
                            stderr=subprocess.STDOUT, timeout=180)
    (TEST / (name + '.log')).write_bytes(result.stdout)
    if result.returncode:
        raise RuntimeError(name + ' failed; see ' + str(TEST / (name + '.log')))
    text = result.stdout.decode('utf-8', errors='replace')
    return '\n'.join(line for line in text.splitlines() if line.startswith('PASS'))


def main(matrix=False, skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    if not skip_build:
        run([sys.executable, ROOT / 'tools/build_source.py'], 'build-MIDP2-RU')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    helpers = list((ROOT / 'tools/source').glob('*.java'))
    helpers = [p for p in helpers if p.name != 'Preprocess.java']
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp(runtime),
         '-d', TEST, *helpers], 'compile-tests')
    java = [recover.java(), '-Djava.awt.headless=true',
            '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST, *runtime])]
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    built = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    source = ROOT / 'build/source/MIDP2-RU'
    # Retain methods that ProGuard specializes/removes so reflection can exercise them.
    with zipfile.ZipFile(built) as jar:
        entries = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(source / 'classes.jar') as jar:
        entries.update({n: jar.read(n) for n in jar.namelist()})
    test_jar = TEST / 'unoptimized-with-resources.jar'
    write_jar(test_jar, entries)
    report = {'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(built)}
    report['pure_logic'] = run([*java, 'SourceDifferentialTest', original, source / 'classes', CACHE], 'differential')
    fixtures = TEST / 'capabilities.bin'
    ref_output, src_output = TEST / 'reference.txt', TEST / 'source.txt'
    run([*java, 'DetectorProbe', original, 'reference', fixtures, ref_output], 'detector-reference')
    report['detector'] = run([*java, 'DetectorProbe', test_jar, 'source', fixtures, src_output], 'detector-source')
    if ref_output.read_bytes() != src_output.read_bytes():
        raise AssertionError('Client detection/status mismatch: compare build/source-tests/reference.txt and source.txt')
    report['detector_cases'] = len(ref_output.read_text().splitlines())
    report['detector_differences'] = 0
    report['ui'] = run([*java, 'SourceSmokeTest', built], 'ui')
    report['limitations'] = ['MicroEmulator does not play all original sound formats.',
                            'No live ICQ login, real-device or complete bytecode-equivalence claim.']
    if matrix:
        report['builds'] = []
        for target in ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
            for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
                if (target, language) != ('MIDP2', 'RU'):
                    run([sys.executable, ROOT / 'tools/build_source.py', '--target', target,
                         '--language', language], 'build-' + target + '-' + language)
                jar = ROOT / 'dist/source' / ('Jimm-' + target + '-' + language + '.jar')
                with zipfile.ZipFile(jar) as z:
                    assert language + '.lng' in z.namelist()
                    assert all(n in z.namelist() for n in ['forms.png', 'groups.png', 'smiles.txt'])
                    assert not any(n.startswith(('javax/', 'com/')) and n.endswith('.class') for n in z.namelist())
                report['builds'].append({'target': target, 'language': language, 'sha256': recover.sha(jar)})
                print('PASS build:', target, language, flush=True)
        for target in ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target, '--language', 'EN',
                 '--modules', '', '--compile-only'], 'minimal-' + target)
        report['minimal_modules'] = ['MIDP2', 'MOTOROLA', 'SIEMENS2']
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--matrix', action='store_true', help='Build all 15 combinations plus three minimal configurations')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.matrix, args.skip_build)
