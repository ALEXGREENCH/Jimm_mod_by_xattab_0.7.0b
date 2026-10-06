#!/usr/bin/env python3
"""Execute real language loading and all five dictionaries, including malformed resource paths."""
import base64
import json
import os
import zipfile
import recover
from audit_resources import records
from test_source import TEST, CACHE, run

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-language-loader.json'


def main():
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', CACHE / 'asm.jar', '-d', TEST,
         *[ROOT / 'tools/source' / n for n in ['ResourceIO.java', 'ResourceFixture.java', 'ResourceProbe.java']]], 'compile-language-loader')
    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    source = ROOT / 'build/source/MIDP2-RU/classes.jar'
    inventory = json.loads((ROOT / 'preservation/reports/source-resources.json').read_text(encoding='utf-8'))
    keys = TEST / 'language-loader-keys'
    keys.mkdir(exist_ok=True)
    report = {'scope': 'Real ResourceBundle initialization, lazy loading, language switching and FLAG_ELLIPSIS specialization. '
                       'Scripted resource bytes/read/close failures; real DataInputStream modified-UTF parser. '
                       'All five actual MIDP2 dictionaries are queried by verified reference/source keys. '
                       'A five-language list is provided for switching tests; historical individual releases contain one language each.',
              'reference_sha256': recover.sha(reference), 'source_unoptimized_class_jar_sha256': recover.sha(source), 'dictionaries': []}
    packs = {}
    for mode in ['reference', 'source']:
        packs[mode] = TEST / ('language-loader-packs-' + mode)
        packs[mode].mkdir(exist_ok=True)
    for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
        old = ROOT / 'preservation/wayback-originals' / ('Jimm_MIDP2_' + language) / 'Jimm.jar'
        new = ROOT / 'dist/source' / ('Jimm-MIDP2-' + language + '.jar')
        tables = {}
        for mode, artifact in [('reference', old), ('source', new)]:
            with zipfile.ZipFile(artifact) as jar:
                data = jar.read(language + '.lng')
                (packs[mode] / (language + '.lng')).write_bytes(data)
                tables[mode] = dict(records(data))
        checked = next(b['language_keys'] for b in inventory['builds'] if (b['target'], b['language']) == ('MIDP2', language))
        rows = []
        for key in checked:
            old_key, new_key = key['reference_midp2_key'], key['source_key']
            value = tables['reference'][old_key]
            assert tables['source'][new_key] == value
            rows.append('\t'.join(base64.b64encode(s.encode('utf-8')).decode('ascii') for s in [old_key, new_key, value, key['name']]))
        (keys / (language + '.tsv')).write_text('\n'.join(rows) + '\n', encoding='utf-8', newline='\n')
        report['dictionaries'].append({'language': language, 'reference_jar_sha256': recover.sha(old), 'source_jar_sha256': recover.sha(new), 'entries': len(rows)})
    for mode, original in [('reference', reference), ('source', source)]:
        fixture = TEST / ('language-loader-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ResourceFixture', original, fixture, mode, TEST], fixture.stem + '-fixture')
        result = run([recover.java(), '-cp', TEST, 'ResourceProbe', fixture, mode, TEST / ('language-loader-' + mode + '.txt'), packs[mode], keys], fixture.stem)
        rows = (TEST / ('language-loader-' + mode + '.txt')).read_text(encoding='utf-8').splitlines()
        assert result == 'PASS language loader: ' + str(len(rows)) + ' observations'
    left = TEST / 'language-loader-reference.txt'
    right = TEST / 'language-loader-source.txt'
    if left.read_bytes() != right.read_bytes():
        raise AssertionError('Language loader mismatch: compare build/source-tests/language-loader-{reference,source}.txt')
    report.update({'observations': len(left.read_text(encoding='utf-8').splitlines()), 'actual_dictionary_entries': sum(d['entries'] for d in report['dictionaries']), 'differences': 0})
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS language loader: ' + str(report['observations']) + ' observations, ' + str(report['actual_dictionary_entries']) + ' actual entries')


if __name__ == '__main__':
    main()
