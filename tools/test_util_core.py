#!/usr/bin/env python3
"""Exercise real May and authoring bytecode for protocol encodings, byte helpers, text and MD5."""
import argparse
import json
import os
import re
import sys
import zipfile
import recover
from build_source import write_jar
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-util-core.json'


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    if not skip_build:
        run([sys.executable, ROOT / 'tools/build_source.py'], 'util-core-build')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-d', TEST,
         ROOT / 'tools/source/UtilCoreDifferentialTest.java'], 'compile-util-core')
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    classes = ROOT / 'build/source/MIDP2-RU/classes.jar'
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    with zipfile.ZipFile(artifact) as jar:
        data = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(classes) as jar:
        data.update({n: jar.read(n) for n in jar.namelist()})
    source = TEST / 'util-core-unoptimized.jar'
    write_jar(source, data)
    inventory = TEST / 'util-core-coverage.txt'
    result = run([recover.java(), '-Xmx512m', '-cp', TEST, 'UtilCoreDifferentialTest', original, source, CACHE, inventory], 'util-core')
    match = re.fullmatch(r'PASS utility core: (\d+) observations, (\d+) real method pairs', result)
    if not match:
        raise AssertionError('Missing utility core result: ' + result)
    methods = []
    for line in inventory.read_text(encoding='utf-8').splitlines():
        count, _, name = line.partition(' ')
        methods.append({'source_method_and_parameters': name, 'calls': int(count)})
    assert len(methods) == int(match[2])
    report = {'scope': 'Genuine May optimized and source unoptimized Util methods in isolated loaders, without body rewriting. '
                       'Explicit option-table inputs control CP1251. Returns, exception classes, input array mutations and '
                       'MD5 state/return aliasing are compared. The original optimizer fixes byteArrayEquals second offset '
                       'and strToIntDef default to zero and the interest TLV tag to 490; stream word/dword endian '
                       'and stream-factory offset are observed as false/zero. Only those observed values are tested. Large negative tariff '
                       'remainders are excluded because the historical padding loop does not terminate. '
                       'No whole Util equivalence, concurrency, physical J2ME charset or display claim.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(classes), 'source_probe_jar_sha256': recover.sha(source),
              'observations': int(match[1]), 'method_pairs': len(methods), 'differences': 0, 'methods': methods}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(result)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    main(parser.parse_args().skip_build)
