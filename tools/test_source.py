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
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *helpers], 'compile-tests')
    # No invokedynamic in the fixture classes loaded by MicroEmulator's legacy ASM.
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8',
         '-cp', recover.cp(runtime), '-d', TEST, ROOT / 'tools/source/TransportIO.java', ROOT / 'tools/source/LoginIO.java'], 'compile-transport-io')
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
    popup_ref, popup_src = TEST / 'popup-reference.txt', TEST / 'popup-source.txt'
    run([*java, 'PopupProbe', original, 'reference', popup_ref], 'popup-reference')
    report['popups'] = run([*java, 'PopupProbe', test_jar, 'source', popup_src], 'popup-source')
    if popup_ref.read_bytes() != popup_src.read_bytes():
        raise AssertionError('Popup mismatch: compare build/source-tests/popup-reference.txt and popup-source.txt')
    report['popup_observations'] = len(popup_ref.read_text().splitlines())
    report['popup_differences'] = 0
    network_ref, network_src = TEST / 'network-reference.txt', TEST / 'network-source.txt'
    for path, mode, output in [(original, 'reference', network_ref), (test_jar, 'source', network_src)]:
        fixture = TEST / ('network-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'NetworkFixture', path, fixture, mode], 'network-fixture-' + mode)
        report['network_' + mode] = run([*java, 'NetworkStateProbe', fixture, mode, output], 'network-' + mode)
    if network_ref.read_bytes() != network_src.read_bytes():
        raise AssertionError('Lifecycle mismatch: compare build/source-tests/network-reference.txt and network-source.txt')
    report['network_observations'] = len(network_ref.read_text().splitlines())
    report['network_differences'] = 0
    splash_ref, splash_src = TEST / 'splash-reference.txt', TEST / 'splash-source.txt'
    for path, mode, output in [(original, 'reference', splash_ref), (test_jar, 'source', splash_src)]:
        fixture = TEST / ('splash-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'SplashFixture', path, fixture, mode], 'splash-fixture-' + mode)
        report['splash_' + mode] = run([*java, 'SplashProbe', fixture, mode, output], 'splash-' + mode)
    if splash_ref.read_bytes() != splash_src.read_bytes():
        raise AssertionError('Splash mismatch: compare build/source-tests/splash-reference.txt and splash-source.txt')
    report['splash_observations'] = len(splash_ref.read_text().splitlines())
    report['splash_differences'] = 0
    transport_ref, transport_src = TEST / 'transport-reference.txt', TEST / 'transport-source.txt'
    for path, mode, output in [(original, 'reference', transport_ref), (test_jar, 'source', transport_src)]:
        fixture = TEST / ('transport-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'TransportFixture', path, fixture, mode, TEST], 'transport-fixture-' + mode)
        report['transport_' + mode] = run([*java, 'TransportProbe', fixture, mode, output], 'transport-' + mode)
    if transport_ref.read_bytes() != transport_src.read_bytes():
        raise AssertionError('Transport mismatch: compare build/source-tests/transport-reference.txt and transport-source.txt')
    report['transport_observations'] = len(transport_ref.read_text().splitlines())
    report['transport_differences'] = 0
    login_ref, login_src = TEST / 'login-reference.txt', TEST / 'login-source.txt'
    for path, mode, output in [(original, 'reference', login_ref), (test_jar, 'source', login_src)]:
        fixture = TEST / ('login-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'LoginFixture', path, fixture, mode, TEST], 'login-fixture-' + mode)
        report['login_' + mode] = run([*java, 'LoginProbe', fixture, mode, output], 'login-' + mode)
    if login_ref.read_bytes() != login_src.read_bytes():
        raise AssertionError('Login/roster mismatch: compare build/source-tests/login-reference.txt and login-source.txt')
    report['login_observations'] = len(login_ref.read_text().splitlines())
    report['login_differences'] = 0
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
