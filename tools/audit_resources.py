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


def attributes(data):
    """Read the main Java ME manifest/JAD section, including folded values."""
    lines = []
    for line in data.decode('utf-8').splitlines():
        if not line:
            break
        if line.startswith(' '):
            if not lines:
                raise AssertionError('Manifest continuation without an attribute')
            lines[-1] += line[1:]
        else:
            lines.append(line)
    result = {}
    for line in lines:
        name, separator, value = line.partition(': ')
        if not separator or name in result:
            raise AssertionError('Invalid or duplicate manifest attribute: ' + line)
        result[name] = value
    return result


def metadata(old, new, reference, source):
    expected = attributes(old.read('META-INF/MANIFEST.MF'))
    actual = attributes(new.read('META-INF/MANIFEST.MF'))
    old_jad = attributes(reference.with_suffix('.jad').read_bytes())
    jad = attributes(source.with_suffix('.jad').read_bytes())
    # Ant's version identifies the original tool, not the restored application.
    # The URL and byte count belong to the rebuilt file and its accompanying JAD.
    regenerated = {'Ant-Version', 'MIDlet-Jar-URL', 'MIDlet-Jar-Size'}
    historic = {k: v for k, v in expected.items() if k not in regenerated}
    differences = []
    if {k: v for k, v in actual.items() if k not in regenerated} != historic:
        differences.append('manifest historical attributes')
    if {k: v for k, v in old_jad.items() if k not in regenerated} != historic:
        differences.append('reference JAD historical attributes')
    if {k: v for k, v in jad.items() if k not in regenerated} != historic:
        differences.append('JAD historical attributes')
    if any('###' in value for value in [*actual.values(), *jad.values()]):
        differences.append('unexpanded build token')
    if jad.get('MIDlet-Jar-URL') != source.name:
        differences.append('JAD URL')
    if jad.get('MIDlet-Jar-Size') != str(source.stat().st_size):
        differences.append('JAD size')
    entry = actual.get('MIDlet-1', '').split(',')
    if len(entry) != 3 or entry[2].strip().replace('.', '/') + '.class' not in new.namelist():
        differences.append('MIDlet entry class')
    if len(entry) != 3 or entry[1].strip().lstrip('/') not in new.namelist():
        differences.append('MIDlet entry icon')
    if actual.get('MIDlet-Icon', '').lstrip('/') not in new.namelist():
        differences.append('MIDlet icon')
    return {'historical_attributes_checked': len(historic),
            'attributes': historic, 'jad_url': jad.get('MIDlet-Jar-URL'),
            'jad_size': jad.get('MIDlet-Jar-Size'), 'differences': differences}


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
    report = {'scope': 'All non-class files are checked. Manifest and JAD application attributes must match '
                      'the original release; regenerated JAR URL/size and the original Ant version are separate. '
                      'Language dictionaries '
                      'must match literal keys and decoded values without key normalization. Recovered semantic '
                      'names are checked separately; equal-value name candidates remain explicit. Serialized '
                      'record order/bytes are reported independently of dictionary equality.',
              'builds': []}
    failures = []
    for target, original_target in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            reference = ROOT / ('preservation/wayback-originals/Jimm_' + original_target + '_' + language + '/Jimm.jar')
            source = ROOT / ('dist/source/Jimm-' + target + '-' + language + '.jar')
            with zipfile.ZipFile(reference) as old, zipfile.ZipFile(source) as new:
                descriptor = metadata(old, new, reference, source)
                left, right = assets(old, language), assets(new, language)
                differences = [name for name in sorted(set(left) | set(right)) if left.get(name) != right.get(name)]
                old_language = old.read(language + '.lng')
                new_language = new.read(language + '.lng')
                old_records, new_records = records(old_language), records(new_language)
                old_pairs, new_pairs = dict(old_records), dict(new_records)
                if len(old_records) != len(old_pairs) or len(new_records) != len(new_pairs):
                    raise AssertionError('Duplicate literal language keys: ' + target + ' ' + language)
                literal_differences = [key for key in sorted(set(old_pairs) | set(new_pairs))
                                       if old_pairs.get(key) != new_pairs.get(key)]
                checked = []
                language_differences = []
                if literal_differences:
                    language_differences.append('<literal key/value table>')
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
                    if target == 'MIDP2' and new_key != old_key:
                        language_differences.append(name + ': short key')
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
                                        'metadata': descriptor,
                                        'resource_files_matched': len(left) - len([n for n in differences if n in left]),
                                        'resource_differences': differences,
                                        'language_entries': len(old_pairs), 'named_language_entries_checked': len(checked),
                                        'language_differences': language_differences,
                                        'literal_language_key_value_differences': literal_differences,
                                        'same_literal_language_table': not literal_differences,
                                        'same_serialized_language_bytes': old_language == new_language,
                                        'reference_language_sha256': sha(old_language),
                                        'source_language_sha256': sha(new_language),
                                        'files': [{'path': name, 'bytes': len(data), 'sha256': sha(data)} for name, data in sorted(left.items())],
                                        'language_keys': checked})
                if differences or language_differences or descriptor['differences']:
                    failures.append((target, language, differences, language_differences, descriptor['differences']))
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    if failures:
        raise AssertionError('May resource mismatch: ' + repr(failures))
    print('PASS resources: 15 builds, ' + str(sum(b['resource_files_matched'] for b in report['builds'])) +
          ' exact asset files, ' + str(sum(b['language_entries'] for b in report['builds'])) + ' decoded language entries')
    print('PASS metadata: 15 JAR/JAD pairs, historical application attributes, entry class/icon and rebuilt URL/size')


if __name__ == '__main__':
    main()
