#!/usr/bin/env python3
"""Compare all shipped assets and named language values with the 15 May releases."""
from collections import Counter
import hashlib
import json
import os
from pathlib import Path
import zipfile
import recover

ROOT = recover.ROOT
OUT = ROOT / 'build/source-resource-audit'
REPORT = ROOT / 'preservation/reports/source-resources.json'


def records(data, width=2):
    offset = 2
    result = []
    for _ in range(int.from_bytes(data[:2], 'big')):
        record = []
        for _ in range(width):
            size = int.from_bytes(data[offset:offset + 2], 'big')
            offset += 2
            record.append(data[offset:offset + size].replace(b'\xc0\x80', b'\x00').decode('utf-8'))
            offset += size
        result.append(record)
    if offset != len(data):
        raise AssertionError('Invalid language record length')
    return result


def sha(data):
    return hashlib.sha256(data).hexdigest()


def assets(jar, language):
    return {entry.filename: jar.read(entry) for entry in jar.infolist()
            if not entry.is_dir() and not entry.filename.endswith('.class')
            and entry.filename not in ['META-INF/MANIFEST.MF', language + '.lng']}


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    mapping = json.loads((ROOT / 'tools/source/language-keys.json').read_text('utf-8'))
    recover.run([os.environ.get('JAVAC', 'javac'), '-d', OUT,
                 ROOT / 'tools/source/LanguageInventory.java'])
    inventories = {}
    task_cp = recover.cp([OUT, ROOT / 'build/source/MIDP2-RU/tasks', recover.CACHE / 'ant.jar'])
    for target in ['MIDP2', 'MOTOROLA', 'SIEMENS2']:
        prepared = OUT / ('prepared-' + target)
        # Recreate the default SiJaPP input: minimal-module checks can overwrite build/source.
        recover.run([recover.java(), '-cp', task_cp, 'Preprocess', 'source', ROOT / 'src', prepared,
                     target, 'SMILES,TRAFFIC,HISTORY,FILES,PROXY,ANISMILES'])
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            binary = OUT / (target + '-' + language + '.bin')
            recover.run([recover.java(), '-cp', task_cp, 'LanguageInventory',
                         prepared / ('lng/' + language + '.lang'), binary, prepared / 'lng/EN.lang'])
            inventories[target, language] = {name: (short, value) for name, short, value in records(binary.read_bytes(), 3)}
    report = {'scope': 'All non-class files except the regenerated manifest are checked. Language tables '
                      'are compared by recovered semantic names and decoded values; short keys/order differ.',
              'builds': []}
    failures = []
    for target, original_target in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            reference = ROOT / ('preservation/wayback-originals/Jimm_' + original_target + '_' + language + '/Jimm.jar')
            source = ROOT / ('dist/source/Jimm-' + target + '-' + language + '.jar')
            with zipfile.ZipFile(reference) as old, zipfile.ZipFile(source) as new:
                left, right = assets(old, language), assets(new, language)
                differences = [name for name in sorted(set(left) | set(right)) if left.get(name) != right.get(name)]
                old_pairs = dict(records(old.read(language + '.lng')))
                new_pairs = dict(records(new.read(language + '.lng')))
                checked = []
                language_differences = []
                source_keys = set()
                names = {name: short for name, (short, value) in inventories[target, language].items() if short in new_pairs}
                base_names = {short: name for base, name in mapping['keys'].items()
                              for short in [mapping['localized_keys'][language][base]]}
                with zipfile.ZipFile(ROOT / ('preservation/wayback-originals/Jimm_MIDP2_' + language + '/Jimm.jar')) as base_jar:
                    base_pairs = records(base_jar.read(language + '.lng'))
                expected = {}
                for key, value in base_pairs:
                    name = base_names.get(key)
                    if name is None:
                        # This is the ideal-English fallback absent from the authored Czech table.
                        if language != 'CZ' or value != 'Error':
                            raise AssertionError('Unmapped extra language record: ' + repr((language, key, value)))
                        name = 'no_recent_ver'
                    expected[name] = (key, value)
                for name, new_key in names.items():
                    source_keys.add(new_key)
                    old_key, value = expected.get(name, (None, None))
                    if name == 'about_info':
                        value = value.replace('MIDP2', target)
                    platform_key = None
                    if target == 'MOTOROLA' and name in mapping['motorola_values'][language]:
                        platform_value = mapping['motorola_values'][language][name]
                        platform_key, value = platform_value['key'], platform_value['value']
                        if old_pairs.get(platform_key) != value:
                            raise AssertionError('Invalid Motorola language mapping: ' + name)
                    candidates = [k for k, v in old_pairs.items() if v == new_pairs[new_key]]
                    # Siemens-only light captions are absent from the MIDP2 table; require their original value.
                    platform_only = target == 'SIEMENS2' and name in ['backlight_opt', 'backlight_on', 'backlight_off']
                    if not candidates or (not platform_only and value != new_pairs[new_key]):
                        language_differences.append(name)
                    checked.append({'name': name, 'reference_midp2_key': old_key, 'source_key': new_key,
                                    'reference_platform_key': platform_key,
                                    'reference_platform_value_candidates': candidates})
                # The whole decoded record set must also match, including duplicate labels.
                extras_new = {k: v for k, v in new_pairs.items() if k not in source_keys}
                if extras_new or Counter(old_pairs.values()) != Counter(new_pairs.values()):
                    language_differences.append('<extra records>')
                report['builds'].append({'target': target, 'language': language,
                                        'reference_sha256': recover.sha(reference), 'source_sha256': recover.sha(source),
                                        'resource_files_matched': len(left) - len([n for n in differences if n in left]),
                                        'resource_differences': differences,
                                        'language_entries': len(old_pairs), 'named_language_entries_checked': len(checked),
                                        'language_differences': language_differences,
                                        'files': [{'path': name, 'bytes': len(data), 'sha256': sha(data)} for name, data in sorted(left.items())],
                                        'language_keys': checked})
                if differences or language_differences:
                    failures.append((target, language, differences, language_differences))
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    if failures:
        raise AssertionError('May resource mismatch: ' + repr(failures))
    print('PASS resources: 15 builds, ' + str(sum(b['resource_files_matched'] for b in report['builds'])) +
          ' exact asset files, ' + str(sum(b['language_entries'] for b in report['builds'])) + ' decoded language entries')


if __name__ == '__main__':
    main()
