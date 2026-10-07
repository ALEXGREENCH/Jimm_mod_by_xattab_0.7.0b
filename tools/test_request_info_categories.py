#!/usr/bin/env python3
"""Compare native interest keys/codes/labels and real category methods in each MIDP2 locale."""
import argparse
import json
import os
import subprocess
import sys
import recover


def main(all_languages=False, skip_build=False):
    recover.bootstrap()
    out = recover.ROOT / 'build/request-info-category-tests'
    out.mkdir(parents=True, exist_ok=True)
    runtime = [recover.CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    recover.run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp(runtime),
                 '-d', out, recover.ROOT / 'tools/source/RequestInfoCategoryProbe.java'])
    report = {'scope': 'Actual optimized MIDP2 RequestInfoAction category methods, all native key/code/name '
                       'entries, signed/unsigned 16-bit lookups and extreme indices; mutated private arrays '
                       'exercise first-match, bounds and partial setter writes. Literal native keys are compared '
                       'without normalization, as well as translations through each JAR own ResourceBundle. '
                       'MicroEmulator initializes each real JAR; no category/parser/resource substitutions.', 'builds': []}
    for language in ['RU', 'UA', 'RO', 'EN', 'CZ'] if all_languages else ['RU']:
        if not skip_build:
            recover.run([sys.executable, recover.ROOT / 'tools/build_source.py', '--language', language])
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_MIDP2_' + language) / 'Jimm.jar'
        source = recover.ROOT / 'dist/source' / ('Jimm-MIDP2-' + language + '.jar')
        outputs = []
        for mode, jar in [('reference', original), ('source', source)]:
            result_path = out / (language + '-' + mode + '.txt')
            result = subprocess.run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                                     '-cp', recover.cp([out, *runtime]), 'RequestInfoCategoryProbe',
                                     str(jar), mode, str(result_path)], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=180)
            (out / (language + '-' + mode + '.log')).write_bytes(result.stdout)
            if result.returncode:
                raise RuntimeError('Category probe failed: ' + language + ' ' + mode)
            outputs.append(result_path.read_bytes())
        if outputs[0] != outputs[1]:
            raise AssertionError('Category mismatch: ' + language + '; compare build/request-info-category-tests/' + language + '-{reference,source}.txt')
        report['builds'].append({'language': language, 'reference_sha256': recover.sha(original),
                                'source_jar_sha256': recover.sha(source), 'observations': len(outputs[0].splitlines()),
                                'native_entries': 51, 'lookup_codes': 98304, 'differences': 0})
    path = recover.ROOT / 'preservation/reports/source-request-info-categories.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS request-info categories: ' + str(len(report['builds'])) + ' locales, ' + str(sum(b['observations'] for b in report['builds'])) + ' observations')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--all-languages', action='store_true')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.all_languages, args.skip_build)
