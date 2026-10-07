#!/usr/bin/env python3
"""Exercise real contact constructors, properties and serialization against May bytecode."""
import argparse
import json
import os
import re
import sys
import zipfile
import recover
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    if not skip_build:
        run([sys.executable, ROOT / 'tools/build_source.py'], 'contact-core-build')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-d', TEST,
         ROOT / 'tools/source/UtilCoreDifferentialTest.java',
         ROOT / 'tools/source/ContactCoreDifferentialTest.java'], 'compile-contact-core')
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    classes = ROOT / 'build/source/MIDP2-RU/classes.jar'
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    with zipfile.ZipFile(artifact) as jar:
        data = {n: jar.read(n) for n in jar.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(classes) as jar:
        data.update({n: jar.read(n) for n in jar.namelist()})
    source = TEST / 'contact-core-unoptimized.jar'
    write_jar(source, data)
    inventory = TEST / 'contact-core-coverage.txt'
    result = run([recover.java(), '-Xmx512m', '-cp', TEST, 'ContactCoreDifferentialTest',
                  original, source, CACHE, inventory], 'contact-core')
    match = re.fullmatch(r'PASS contact core: (\d+) observations, (\d+) real method pairs, (\d+) constructor pairs', result)
    assert match, result
    guard = (TEST / 'contact-core.log').read_text(encoding='utf-8').splitlines()[-1]
    counts = re.fullmatch(r'constructed:(\d+)/constructorFailures:(\d+)/saves:(\d+)/loads:(\d+)', guard)
    assert counts, guard
    methods = []
    for line in inventory.read_text(encoding='utf-8').splitlines():
        count, _, name = line.partition(' ')
        methods.append({'source_method_and_parameters': name, 'calls': int(count)})
    assert len(methods) == int(match[2])
    report = {'scope': 'Genuine May optimized ContactItem and source unoptimized methods in isolated loaders, '
                       'without bytecode rewriting. Constructor results, exception classes, packed properties, '
                       'capabilities, privacy IDs, counters, cached names, IP/roster arrays and input aliases are compared. '
                       'addCapability is observed as the original specialized constant 256 only. '
                       'Static/synchronized modifiers are verified for every method pair; hasCapability is also '
                       'invoked while another thread holds the actual receiver monitor, once for each JAR. '
                       'Options use explicit real-table values; a sentinel birthday entry prevents external RMS loading. '
                       'Save/load calls compare output prefixes, read/write calls, input positions and partial contact '
                       'states, including signed roster lengths and short reads. No whole contact/UI equivalence, '
                       'physical persistence, concurrency or J2ME charset implementation claim.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(classes), 'source_probe_jar_sha256': recover.sha(source),
              'observations': int(match[1]), 'method_pairs': len(methods), 'constructor_pairs': int(match[3]),
              'constructed_pairs': int(counts[1]), 'constructor_exception_pairs': int(counts[2]),
              'save_calls': int(counts[3]), 'load_calls': int(counts[4]), 'receiver_monitor_cases': 2,
              'method_modifiers_verified': True, 'differences': 0, 'methods': methods}
    (ROOT / 'preservation/reports/source-contact-core.json').write_text(json.dumps(report, indent=2) + '\n',
                                                                    encoding='utf-8', newline='\n')
    print(result)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    main(parser.parse_args().skip_build)
