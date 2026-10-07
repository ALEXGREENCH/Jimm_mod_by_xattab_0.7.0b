#!/usr/bin/env python3
"""Compare genuine settings-controller reuse and selection on all three release targets."""
import argparse
import json
import os
import zipfile
from pathlib import Path
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def configuration(target, reference, data):
    name = {'MIDP2': 'cj', 'MOTOROLA': 'ci', 'SIEMENS2': 'ch'}[target] if reference else 'jimm/Options'
    options = next(c for c in data if c['name'] == name)
    entry = next(m for m in options['methods'] if (m['name'], m['desc']) == ('e' if reference else 'editOptions', '()V'))
    assert entry['access'] == 9
    created = [s[4:] for s in entry['code'] if s.startswith('187 ')]
    assert len(created) == 1
    form = next(c for c in data if c['name'] == created[0])
    field = next(f for f in options['fields'] if f['desc'] == 'L' + form['name'] + ';')
    assert field['access'] & 8
    activated = next(s for s in entry['code'] if s.startswith('182 '))
    assert activated.startswith('182 ' + form['name'] + '.') and activated.endswith('()V')
    activate = next(m for m in form['methods'] if activated == '182 ' + form['name'] + '.' + m['name'] + m['desc'])
    menu_read = next(s for s in activate['code'] if s.startswith('180 ') and s.endswith(';') and not s.endswith('Ljava/lang/String;'))
    _, qualified, desc = menu_read.split(' ')
    text = next(c for c in data if c['name'] == desc[1:-1])
    spec = {'options': name, 'form': form['name'], 'text': text['name'], 'open': entry['name'], 'cache': field['name'],
            'menu': qualified.split('.')[-1], 'table': 'a' if reference else 'options', 'display': 'a' if reference else 'display',
            'select': 'a' if reference else 'selectTextByIndex', 'index': 'b' if reference else 'getCurrTextIndex',
            'contents': 'a' if reference else 'getTextByIndex'}
    vl = next(c for c in data if c['name'] == text['super'])
    canvases = [c for c in data if c['super'] == 'javax/microedition/lcdui/Canvas' and
                any(f['desc'] == 'L' + vl['name'] + ';' for f in c['fields'])]
    assert len(canvases) == 1
    canvas = canvases[0]
    spec.update(canvasType=canvas['name'], canvas=next(f['name'] for f in vl['fields'] if f['desc'] == 'L' + canvas['name'] + ';'),
                control=next(f['name'] for f in canvas['fields'] if f['desc'] == 'L' + vl['name'] + ';'))
    if target == 'MOTOROLA':
        spec.update(bitmap='cg' if reference else 'DrawControls/TPropFont', bitmapField='a' if reference else 'font')
        bitmap = next(c for c in data if c['name'] == spec['bitmap'])
        assert any(f['name'] == spec['bitmapField'] and f['desc'] == 'L' + bitmap['name'] + ';' and f['access'] & 8 for f in bitmap['fields'])
    for key, descriptor in [('select', '(I)V'), ('index', '()I'), ('contents', '(IZI)Ljava/lang/String;')]:
        assert any((m['name'], m['desc']) == (spec[key], descriptor) for m in text['methods'])
    # These state fields are identified by the actual activation stores from typed option reads.
    roles = [('language', '6', 'Ljava/lang/String;'), ('offline', '17 130', 'Z'), ('empty', '17 129', 'Z'), ('groups', '17 136', 'Z')]
    for role, literal, descriptor in roles:
        reads = [i for i, s in enumerate(activate['code']) if s == literal and i + 2 < len(activate['code']) and
                 activate['code'][i+1].startswith('184 ' + name + '.') and activate['code'][i+2].startswith('181 ' + form['name'] + '.')]
        assert len(reads) == 1, (role, reads)
        store = activate['code'][reads[0]+2]
        assert store.endswith(' ' + descriptor)
        spec[role] = store.split(' ')[1].split('.')[-1]
    return spec, entry, field, activate


def main(negative=False, baseline=None):
    assert baseline is None or negative
    TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / ('tools/source/' + n + '.java') for n in ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe', 'OptionsCacheProbe']]], 'compile-options-cache')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-options-cache-dump')
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = (baseline / (target + '-classes.jar')) if baseline else ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        delivered = (baseline / (target + '-source.jar')) if baseline else ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        host = TEST / ('options-cache-' + target + '-authored-resources.jar')
        with zipfile.ZipFile(delivered) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(host, entries)
        proofs, outputs = [], []
        modes = [('reference', original), ('source', host)]
        if not negative: modes.append(('optimized-source', delivered))
        for mode, artifact in modes:
            spec, entry, field, activate = configuration(target, mode == 'reference', classes(artifact, cp))
            tag = 'options-cache-' + target + '-' + mode
            settings, fixture, output = [TEST / (tag + ext) for ext in ['.properties', '.jar', '.txt']]
            settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture', artifact, fixture,
                 TEST, 'unused', 'false', 'unused'], tag + '-fixture')
            with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
                assert all(before.read(n) == after.read(n) for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf')
                assert set(after.namelist()) - set(before.namelist()) <= {'GraphicsMIDlet.class'}
            run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647', '-cp',
                 recover.cp([TEST, *runtime]), 'OptionsCacheProbe', fixture, output, settings], tag)
            result = output.read_bytes();assert result.splitlines()[-1] == b'coverage:1536';outputs.append(result)
            proofs.append({'mode': mode, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'all_input_application_bytes_unchanged': True,
                           'entry_instructions': entry['code'], 'cache_field': field, 'activation': activate['name'] + activate['desc']})
        left, right = outputs[0].splitlines(), outputs[1].splitlines();assert len(left) == len(right)
        differences = sum(a != b for a, b in zip(left, right))
        if negative: assert differences > 0, 'Pre-fix source must really differ'
        else: assert all(o == outputs[0] for o in outputs), ('Actual settings cache differs', target)
        builds.append({'target': target, 'reference_sha256': recover.sha(original), 'raw_sha256': recover.sha(raw),
                       'delivered_sha256': recover.sha(delivered), 'opens': 1536, 'observations': len(left),
                       'different_observations': differences, 'fixture_proofs': proofs})
    report = {'scope': 'Actual settings entry, constructors, activation and TextList on three targets. '
              'All input application classes/resources remain byte-for-byte intact. Only a minimal MIDlet and '
              'host manifest isolate startup; typed Options and Jimm.display are explicitly seeded. '
              'Motorola bitmap font is initialized by its genuine resource-reading constructor. '
              'Null/existing controller, repeated entry, selected tags, connection type, font, four refreshed '
              'snapshot values, canvas current-control binding, entire menu text and explicit cache clearing are observed. '
              'No replacement application implementations or derived expected outputs. Explicit cache clearing '
              'is input, not a claim about the Back command, all settings panels, complete startup or handset.',
              'negative_before_fix': negative, 'builds': builds}
    if baseline:
        report.update(source_options_sha256=recover.sha(baseline / 'Options.java'), source_ref=(baseline / 'source_ref.txt').read_text().strip())
    destination = 'source-options-cache-before.json' if negative else 'source-options-cache.json'
    (ROOT / 'preservation/reports' / destination).write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS settings cache:', sum(b['opens'] for b in builds), 'actual opens;', sum(b['observations'] for b in builds), 'observations;' + (' genuine pre-fix differences retained' if negative else ' original/raw/optimized match'))
    return report


if __name__ == '__main__':
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--negative', action='store_true')
    p.add_argument('--baseline', type=Path)
    args = p.parse_args();main(args.negative, args.baseline)
