#!/usr/bin/env python3
"""Compare actual May sound methods against raw and delivered source on three targets."""
import argparse
import json
import os
import sys
import zipfile
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def member(owner, descriptor, predicate=lambda m: True):
    matches = [m for m in owner['methods'] if m['desc'] == descriptor and predicate(m)]
    assert len(matches) == 1, (owner['name'], descriptor, [(m['name'], m['desc']) for m in matches])
    return matches[0]


def configuration(target, reference, data):
    owners = {c['name']: c for c in data}
    listeners = [c for c in data if 'javax/microedition/media/PlayerListener' in c['interfaces']]
    assert len(listeners) == 1
    cl = listeners[0]
    create = member(cl, '(Ljava/lang/String;)Ljavax/microedition/media/Player;')
    assert {'audio/mpeg', 'audio/midi', 'audio/amr', 'audio/X-wav'} <= set(create['strings'])
    assert create['code'][0].startswith('184 ' + cl['name'] + '.') and create['code'][0].endswith('()V')
    close_name = create['code'][0].split('.')[1][:-3]
    close = member(cl, '()V', lambda m: m['name'] == close_name)
    test = member(cl, '(Ljava/lang/String;)Z', lambda m: '184 ' + cl['name'] + '.' + create['name'] + create['desc'] in m['code'])
    volume = member(cl, '(I)V', lambda m: '185 javax/microedition/media/control/VolumeControl.setLevel(I)I' in m['code'])
    notification = member(cl, '(I)V', lambda m: m['code'].count('184 javax/microedition/media/Manager.playTone(III)V') == 4)
    event = member(cl, '(Ljavax/microedition/media/Player;Ljava/lang/String;Ljava/lang/Object;)V')
    options = [c for c in data if any('wav|mp3' in m['strings'] for m in c['methods'])]
    assert len(options) == 1
    options = options[0]
    select = member(options, '(Ljava/lang/String;I)V', lambda m: 'wav|mp3' in m['strings'])
    assert '184 ' + cl['name'] + '.' + test['name'] + test['desc'] in select['code']
    self_field = next(f for f in cl['fields'] if f['desc'] == 'L' + cl['name'] + ';')
    player = next(f for f in cl['fields'] if f['desc'] == 'Ljavax/microedition/media/Player;')
    assert notification['code'][0] == '178 ' + cl['name'] + '.' + self_field['name'] + ' ' + self_field['desc']
    assert notification['code'][3] == '194'
    built_read = notification['code'][4]
    assert built_read.startswith('178 ' + cl['name'] + '.') and built_read.endswith(' Z')
    locked_calls = [s for s in notification['code'] if s.startswith('184 ') and s.endswith('()Z')]
    assert len(locked_calls) == 1
    call = locked_calls[0][4:]
    splash_name, locked_name = call.split('.', 1)
    locked = member(owners[splash_name], '()Z', lambda m: m['name'] == locked_name[:-3])
    assert len(locked['code']) == 2 and locked['code'][0].startswith('178 ' + splash_name + '.') and locked['code'][0].endswith(' Z') and locked['code'][1] == '172'
    table = next(f for f in options['fields'] if f['desc'] == '[Ljava/lang/Object;')
    jimm = owners['jimm/Jimm']
    display = next(f for f in jimm['fields'] if f['desc'] == 'Ljavax/microedition/lcdui/Display;')
    constructor = member(cl, '()V', lambda m: m['name'] == '<init>')
    load_call = constructor['code'][4]
    assert load_call.startswith('184 ' + cl['name'] + '.') and load_call.endswith('()V')
    load = member(cl, '()V', lambda m: load_call == '184 ' + cl['name'] + '.' + m['name'] + m['desc'])
    versions = [s for s in load['code'] if s.startswith('178 jimm/Jimm.') and s.endswith(' Ljava/lang/String;')]
    assert len(versions) == 1
    version = versions[0].split(' ')[1].split('.')[-1]
    assert any(f['name'] == version and f['desc'] == 'Ljava/lang/String;' and f['access'] == 9 for f in jimm['fields'])
    spec = {'list': cl['name'], 'options': options['name'], 'table': table['name'], 'display': display['name'],
            'version': version, 'self': self_field['name'], 'player': player['name'],
            'built': built_read.split(' ')[1].split('.')[-1], 'splash': splash_name,
            'locked': locked['code'][0].split(' ')[1].split('.')[-1]}
    methods = {'create': create, 'test': test, 'close': close, 'volume': volume, 'notify': notification, 'event': event, 'select': select}
    spec.update({key: m['name'] for key, m in methods.items()})
    if target == 'MOTOROLA':
        spec.update(bitmap='cg' if reference else 'DrawControls/TPropFont', bitmapField='a' if reference else 'font')
        bitmap = owners[spec['bitmap']]
        assert any(f['name'] == spec['bitmapField'] and f['desc'] == 'L' + bitmap['name'] + ';' and f['access'] & 8 for f in bitmap['fields'])
        member(bitmap, '(Ljava/lang/String;)V', lambda m: m['name'] == '<init>')
    return spec, cl, options, methods


def transformed(owner, spec):
    mapping = {
        '182 java/lang/Class.getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;':
            '184 SoundIO.resource(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;',
        '184 javax/microedition/media/Manager.createPlayer(Ljava/io/InputStream;Ljava/lang/String;)Ljavax/microedition/media/Player;':
            '184 SoundIO.create(Ljava/io/InputStream;Ljava/lang/String;)Ljavax/microedition/media/Player;',
        '184 javax/microedition/media/Manager.playTone(III)V': '184 SoundIO.tone(III)V',
        '182 javax/microedition/lcdui/Display.vibrate(I)Z': '184 SoundIO.vibrate(Ljavax/microedition/lcdui/Display;I)Z'}
    result = json.loads(json.dumps(owner))
    count = 0
    for m in result['methods']:
        if (m['name'], m['desc']) not in [(spec['create'], '(Ljava/lang/String;)Ljavax/microedition/media/Player;'), (spec['notify'], '(I)V')]:
            continue
        count += sum(s in mapping for s in m['code'])
        m['code'] = [mapping.get(s, s) for s in m['code']]
        m['refs'] = [s for s in m['code'] if 178 <= int(s.split(' ', 1)[0]) <= 185]
    assert count == 8, count
    return result, count


def execute(target, mode, artifact, runtime, cp):
    data = classes(artifact, cp)
    spec, owner, options, methods = configuration(target, mode == 'reference', data)
    tag = 'sound-' + target + '-' + mode
    settings = TEST / (tag + '.properties')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    fixture, output = [TEST / (tag + ext) for ext in ['.jar', '.txt']]
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SoundFixture', artifact, fixture,
         TEST, owner['name'], spec['create'], spec['notify']], tag + '-fixture')
    with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
        changes = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf' and before.read(n) != after.read(n)]
        assert changes == [owner['name'] + '.class']
        assert all(n == 'GraphicsMIDlet.class' or n.startswith('SoundIO') and n.endswith('.class')
                   for n in set(after.namelist()) - set(before.namelist()))
    changed = next(c for c in classes(fixture, cp) if c['name'] == owner['name'])
    expected, captures = transformed(owner, spec)
    assert expected == changed, 'Non-enumerated method metadata/instructions/handlers were changed'
    run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
         '-cp', recover.cp([TEST, *runtime]), 'SoundProbe', fixture, output, settings], tag)
    result = output.read_bytes()
    coverage = result.splitlines()[-1].decode()
    assert coverage.startswith('coverage:')
    counts = [int(s) for s in coverage.split(':')[1:]]
    assert len(counts) == 7 and all(n > 0 for n in counts) and len(result.splitlines()) == sum(counts) + 1
    return result, {'mode': mode, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                    'output_sha256': recover.sha(output), 'observations': sum(counts),
                    'coverage': dict(zip(['create', 'test_file', 'close', 'volume', 'callback', 'select', 'notify'], counts)),
                    'enumerated_external_call_sites': captures, 'other_application_bytes_unchanged': True,
                    'subject_other_instructions_metadata_and_handlers_unchanged': True,
                    'methods': {key: m['name'] + m['desc'] for key, m in methods.items()}}


def main(skip_build=False):
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / ('tools/source/' + n + '.java') for n in ['GraphicsFixture', 'PopupProbe', 'SoundFixture', 'SoundProbe']]], 'compile-sound')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8',
         '-cp', recover.cp(runtime), '-d', TEST, ROOT / 'tools/source/SoundIO.java'], 'compile-sound-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-sound-dump')
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        if not skip_build:
            run([sys.executable, ROOT / 'tools/build_source.py', '--target', target], 'sound-build-' + target)
        original = ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'
        optimized = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        raw = TEST / ('sound-' + target + '-raw-input.jar')
        with zipfile.ZipFile(optimized) as built, zipfile.ZipFile(ROOT / f'build/source/{target}-RU/classes.jar') as authoring:
            entries = {n: built.read(n) for n in built.namelist() if not n.endswith('.class')}
            entries.update({n: authoring.read(n) for n in authoring.namelist()})
            write_jar(raw, entries)
        results, proofs = [], []
        for mode, artifact in [('reference', original), ('raw', raw), ('optimized', optimized)]:
            result, proof = execute(target, mode, artifact, runtime, cp)
            results.append(result);proofs.append(proof)
        if results[0] != results[1] or results[0] != results[2]:
            differences = [[i for i, (a, b) in enumerate(zip(results[0].splitlines(), result.splitlines())) if a != b] for result in results[1:]]
            raise AssertionError('Sound mismatch: ' + target + ' first raw/optimized rows ' + repr([d[:20] for d in differences]))
        builds.append({'target': target, 'observations': proofs[0]['observations'], 'coverage': proofs[0]['coverage'], 'differences': 0, 'proofs': proofs})
        print('PASS sound:', target, proofs[0]['observations'], 'genuine observations', flush=True)
    report = {'scope': 'Whole genuine ContactList create/test/close/volume/playerUpdate/playSoundNotification and Options.selectSoundType '
                       'execute in original, raw and delivered RU classes on three targets. Only eight enumerated Class resource, '
                       'Manager create/tone and Display vibrate call sites are substituted. Player/Control objects are external device '
                       'inputs; MIME parsing, fallback, field mutations, handlers, monitor and option selection are actual application code. '
                       'The real ContactList constructor and real Splash locked getter execute. This is not physical audio, codec '
                       'compatibility, the full notification scheduling/typing pipeline or real device concurrency.',
              'observations': sum(b['observations'] for b in builds), 'builds': builds}
    (ROOT / 'preservation/reports/source-sound.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS sound total:', report['observations'])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true')
    main(parser.parse_args().skip_build)
