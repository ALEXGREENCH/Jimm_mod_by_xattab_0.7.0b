#!/usr/bin/env python3
"""Compare all three May file adapters, including both Motorola device paths."""
import argparse
import json
import os
import sys
import zipfile
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-filesystems.json'


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    targets = [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]
    if not skip_build:
        for target, old in targets:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target], 'files-build-' + target)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / 'tools/source' / n for n in ['GraphicsProbe.java', 'GraphicsFixture.java', 'FileSystemProbe.java', 'FileSystemFixture.java']]], 'compile-files')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, *runtime]), '-d', TEST,
         *[ROOT / 'tools/source' / n for n in ['TransportIO.java', 'FileTransferIO.java', 'FileSystemIO.java']]], 'compile-files-io')
    report = {'scope': 'Real filesystem bodies and Motorola factory dispatch; scripted Connector, root listings and JSR75 support predicate. '
                       'The test MIDlet isolates application startup. Vendor FileConnection declarations are included in test fixtures only. '
                       'This is not a physical-device, permissions or storage-durability test.', 'builds': []}
    for target, old in targets:
        artifact = ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        classes = ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old + '_RU') / 'Jimm.jar'
        with zipfile.ZipFile(artifact) as jar:
            data = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(classes) as jar:
            data.update({n: jar.read(n) for n in jar.namelist() if n.endswith('.class')})
        rebuilt = TEST / ('files-' + target + '-unoptimized.jar')
        recover.write_jar(rebuilt, data)
        for jsr in [True, False] if target == 'MOTOROLA' else [True]:
            for mode, original in [('reference', reference), ('source', rebuilt)]:
                name = 'files-' + target + '-' + str(jsr) + '-' + mode
                fixture = TEST / (name + '.jar')
                vendor = [ROOT / 'res' / target / 'lib/fileaccess.jar'] if target != 'MIDP2' else []
                run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'FileSystemFixture',
                     original, fixture, mode, target, TEST, *vendor], name + '-fixture')
                result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                              '-Djimm.files.target=' + target, '-Djimm.files.jsr=' + str(jsr).lower(),
                              '-cp', recover.cp([TEST, *runtime]), 'FileSystemProbe', fixture, mode, TEST / (name + '.txt'), target], name)
                rows = (TEST / (name + '.txt')).read_text(encoding='utf-8').splitlines()
                assert result == 'PASS files: ' + str(len(rows)) + ' observations'
            name = 'files-' + target + '-' + str(jsr)
            left = (TEST / (name + '-reference.txt')).read_bytes()
            right = (TEST / (name + '-source.txt')).read_bytes()
            if left != right:
                raise AssertionError('Filesystem mismatch: compare build/source-tests/' + name + '-{reference,source}.txt')
            report['builds'].append({'target': target, 'jsr75_supported': jsr,
                                    'reference_sha256': recover.sha(reference), 'source_jar_sha256': recover.sha(artifact),
                                    'source_unoptimized_class_jar_sha256': recover.sha(classes),
                                    'source_unoptimized_with_resources_sha256': recover.sha(rebuilt),
                                    'observations': len(left.decode('utf-8').splitlines()), 'differences': 0})
    report['bytecode'] = run([sys.executable, ROOT / 'tools/audit_filesystems.py'], 'files-bytecode-audit')
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS filesystems: 4 device paths, ' + str(sum(b['observations'] for b in report['builds'])) + ' observations')
    print(report['bytecode'])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    main(parser.parse_args().skip_build)
