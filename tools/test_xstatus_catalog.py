#!/usr/bin/env python3
"""Compare native XStatus/GUID tables, real parser, aliases, locale labels and raster icons."""
import argparse
import json
import os
import subprocess
import sys
import recover


def main(all_languages=False, skip_build=False):
    recover.bootstrap()
    out = recover.ROOT / 'build/xstatus-catalog-tests'
    out.mkdir(parents=True, exist_ok=True)
    runtime = [recover.CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    recover.run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp(runtime),
                 '-d', out, recover.ROOT / 'tools/source/XStatusCatalogProbe.java'])
    report = {'scope': 'Actual optimized MIDP2 XStatus and GUID constructor, native GUID/label tables, '
                       'real ContactItem.setXStatus caller, all single-byte replacements, partial/multiple capabilities, state reset, boundaries '
                       'and first-match behavior. Mutated real GUID arrays/aliases, caption slots and ImageList '
                       'icon arrays exercise historical failures. Actual ResourceBundle and Icon.drawImage '
                       'execute; returned GUID/icon identities and icon raster hashes are compared. '
                       'Reference native GUID bytes supply common wire fixtures. No method/resource substitutions '
                       'or network/server/physical-display equivalence claim.', 'builds': []}
    for language in ['RU', 'UA', 'RO', 'EN', 'CZ'] if all_languages else ['RU']:
        if not skip_build:
            recover.run([sys.executable, recover.ROOT / 'tools/build_source.py', '--language', language])
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_MIDP2_' + language) / 'Jimm.jar'
        source = recover.ROOT / 'dist/source' / ('Jimm-MIDP2-' + language + '.jar')
        outputs = []
        for mode, jar in [('reference', original), ('source', source)]:
            result_path = out / (language + '-' + mode + '.txt')
            result = subprocess.run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                                     '-cp', recover.cp([out, *runtime]), 'XStatusCatalogProbe', str(jar), mode,
                                     str(out / (language + '-guids.bin')), str(result_path)],
                                    stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=180)
            (out / (language + '-' + mode + '.log')).write_bytes(result.stdout)
            if result.returncode:
                raise RuntimeError('XStatus catalog probe failed: ' + language + ' ' + mode)
            outputs.append(result_path.read_bytes())
        if outputs[0] != outputs[1]:
            raise AssertionError('XStatus catalog mismatch: ' + language + '; compare build/xstatus-catalog-tests/' + language + '-{reference,source}.txt')
        report['builds'].append({'language': language, 'reference_sha256': recover.sha(original),
                                'source_jar_sha256': recover.sha(source), 'observations': len(outputs[0].splitlines()),
                                'parser_calls': int(outputs[0].splitlines()[-1].split(b':')[1]),
                                'guid_entries': 37, 'selectable_statuses': 36, 'caption_entries': 36, 'differences': 0})
    if all_languages:
        report['bytecode'] = recover.run([sys.executable, recover.ROOT / 'tools/audit_xstatus.py'], capture=True).decode('utf-8').strip()
        print(report['bytecode'])
    path = recover.ROOT / 'preservation/reports/source-xstatus-catalog.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS XStatus catalog: ' + str(len(report['builds'])) + ' locales, ' + str(sum(b['observations'] for b in report['builds'])) + ' observations')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--all-languages', action='store_true')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.all_languages, args.skip_build)
