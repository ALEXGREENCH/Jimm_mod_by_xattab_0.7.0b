#!/usr/bin/env python3
"""Execute whole roster aggregation, grouping, chat navigation, typing and sound mode."""
import json
import os
import zipfile
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run
from test_roster_keys import configuration as keys_configuration
from test_contact_store import configuration as store_configuration
from test_sound import configuration as sound_configuration

API = {
    '182 javax/microedition/lcdui/Displayable.isShown()Z': '184 RosterStateIO.isShown(Ljavax/microedition/lcdui/Displayable;)Z',
    '182 javax/microedition/lcdui/Canvas.repaint()V': '184 RosterStateIO.repaint(Ljavax/microedition/lcdui/Canvas;)V',
    '182 javax/microedition/lcdui/Display.setCurrent(Ljavax/microedition/lcdui/Displayable;)V': '184 RosterStateIO.show(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V',
    '182 javax/microedition/lcdui/Display.callSerially(Ljava/lang/Runnable;)V': '184 RosterStateIO.enqueue(Ljavax/microedition/lcdui/Display;Ljava/lang/Runnable;)V',
}


def configuration(target, reference, data):
    spec, vl, _ = keys_configuration(target, reference, data)
    store, _ = store_configuration(target, reference, data)
    sound, cl, _, _ = sound_configuration(target, reference, data)
    spec.update(store);spec['built'] = sound['built']
    owners = {c['name']: c for c in data}
    def member(owner, desc, predicate=lambda m: True):
        matches = [m for m in owner['methods'] if m['desc'] == desc and predicate(m)]
        assert len(matches) == 1, (owner['name'], desc, [m['name'] for m in matches])
        return matches[0]
    methods = {
        'next': member(cl, '(Z)Ljava/lang/String;'),
        'unread': member(cl, '()I', lambda m: m['access'] == 12),
        'items': member(cl, '(L' + spec['group'] + ';)[L' + spec['contact'] + ';'),
        'typing': member(cl, '(Ljava/lang/String;Z)V', lambda m: m['access'] == 42),
        'sound': member(cl, '(Z)Z'),
    }
    spec.update({key: m['name'] for key, m in methods.items()})
    spec['last'] = methods['next']['code'][1].split(' ')[1].split('.')[-1]
    receiver = next(s for s in methods['next']['code'] if s.startswith('179 ') and not s.startswith('179 ' + cl['name'] + '.') and s.endswith(' L' + spec['contact'] + ';'))
    spec['ui'], spec['receiver'] = receiver.split(' ')[1].split('.')
    spec['enter'] = next(s for s in methods['next']['code'] if s.startswith('179 ' + cl['name'] + '.') and s.endswith(' Z')).split(' ')[1].split('.')[-1]
    history_call = next(s[4:] for s in methods['typing']['code'] if s.startswith('184 ') and s.endswith('(Ljava/lang/String;)Z'))
    spec['history'] = history_call.split('.')[0]
    history = owners[spec['history']]
    getters = [m for m in history['methods'] if m['desc'].startswith('(Ljava/lang/String;)L') and
               m['desc'].split(')L')[1][:-1] in owners and
               any(f['desc'] == 'L' + spec['text'] + ';' for f in owners[m['desc'].split(')L')[1][:-1]]['fields'])]
    assert len(getters) == 1
    getter = getters[0]
    spec['chat'] = getter['desc'].split(')L')[1][:-1]
    spec['historyTable'] = next(f['name'] for f in history['fields'] if f['desc'] == 'Ljava/util/Hashtable;')
    spec['newChat'] = member(history, '(L' + spec['contact'] + ';Ljava/lang/String;)V')['name']
    chat = owners[spec['chat']]
    spec['chatText'] = next(f['name'] for f in chat['fields'] if f['desc'] == 'L' + spec['text'] + ';')
    contact = owners[spec['contact']]
    for key, desc in [('setInt', '(II)V'), ('getInt', '(I)I'), ('setBoolean', '(IZ)V')]:
        if key == 'getInt':
            spec[key] = ('b' if reference else 'getIntValue')
            member(contact, desc, lambda m: m['name'] == spec[key])
        else:
            spec[key] = member(contact, desc)['name']
    activated = next(s[4:] for s in methods['next']['code'] if s.startswith('182 ' + contact['name'] + '.') and s.endswith('()V'))
    activate = member(contact, '()V', lambda m: activated == contact['name'] + '.' + m['name'] + m['desc'])
    spec['currentUin'] = next(s for s in activate['code'] if s.startswith('179 ' + contact['name'] + '.') and s.endswith(' Ljava/lang/String;')).split(' ')[1].split('.')[-1]
    canvas_read = next(s for m in vl['methods'] for s in m['code'] if s.startswith('178 ' + vl['name'] + '.') and m['code'].count('182 javax/microedition/lcdui/Displayable.isShown()Z') == 1 and s.endswith(';' ) and s.split(' ')[2][1:-1] in owners and owners[s.split(' ')[2][1:-1]]['super'] == 'javax/microedition/lcdui/Canvas')
    spec['canvasField'] = canvas_read.split(' ')[1].split('.')[-1]
    spec['canvas'] = canvas_read.split(' ')[2][1:-1]
    spec['currentControl'] = next(f['name'] for f in owners[spec['canvas']]['fields'] if f['desc'] == 'L' + vl['name'] + ';')
    runnable = [c for c in data if 'java/lang/Runnable' in c['interfaces'] and any(f['desc'] == 'Ljavax/microedition/midlet/MIDlet;' for f in c['fields'])]
    assert len(runnable) == 1
    spec['runnable'] = runnable[0]['name']
    spec['midlet'] = next(f['name'] for f in runnable[0]['fields'] if f['desc'] == 'Ljavax/microedition/midlet/MIDlet;')
    return spec, vl, methods


def execute(target, mode, artifact, runtime, cp):
    data = classes(artifact, cp)
    spec, vl, methods = configuration(target, mode == 'reference', data)
    tag = 'roster-state-' + target + '-' + mode
    settings = TEST / (tag + '.properties')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    outputs, proofs = [], []
    for scripted in [False, True]:
        flavor = 'scripted' if scripted else 'device'
        fixture, output = [TEST / (tag + '-' + flavor + ext) for ext in ['.jar', '.txt']]
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'RosterStateFixture', artifact, fixture, TEST, vl['name'], str(scripted).lower(), spec['runnable']], tag + '-' + flavor + '-fixture')
        with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
            changed = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf' and before.read(n) != after.read(n)]
            assert set(changed) == ({vl['name'] + '.class', spec['runnable'] + '.class'} if scripted else set())
            assert set(after.namelist()) - set(before.namelist()) <= {'GraphicsMIDlet.class', 'RosterStateIO.class'}
        captures = 0
        if scripted:
            actual = {c['name']: c for c in classes(fixture, cp)}
            for owner in [vl, next(c for c in data if c['name'] == spec['runnable'])]:
                expected = json.loads(json.dumps(owner))
                for m in expected['methods']:
                    captures += sum(s in API for s in m['code'])
                    m['code'] = [API.get(s, s) for s in m['code']]
                    m['refs'] = [s for s in m['code'] if 178 <= int(s.split(' ', 1)[0]) <= 185]
                assert expected == actual[owner['name']], 'Only enumerated external call instructions may change'
            assert captures == 8
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'RosterStateProbe', fixture, output, settings, str(scripted).lower()], tag + '-' + flavor)
        value = output.read_bytes()
        counts = list(map(int, value.splitlines()[-1].decode().split(':')[1:]))
        assert len(counts) == 6 and len(value.splitlines()) == sum(counts[:4]) + 1
        assert counts[0] > 1000 and counts[1] > 0 and counts[3] > 0 and counts[5] > 0
        if scripted: assert counts[2] > 0 and counts[4] > 0
        outputs.append(value)
        proofs.append({'flavor': flavor, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'observations': sum(counts[:4]), 'coverage': dict(zip(['data', 'navigation', 'typing', 'sound_mode', 'successful_chat_selections', 'rms_records_observed'], counts)),
                       'all_application_bytes_unchanged': not scripted, 'enumerated_external_call_sites': captures,
                       'subject_declarations_instructions_and_handlers_unchanged': True,
                       'methods': {k: m['name'] + m['desc'] for k, m in methods.items()}})
    return outputs, proofs


def main():
    recover.bootstrap();TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in ['GraphicsFixture', 'PopupProbe', 'RosterStateFixture', 'RosterStateProbe']]], 'compile-roster-state')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-encoding', 'UTF-8', '-cp', recover.cp(runtime), '-d', TEST, ROOT / 'tools/source/RosterStateIO.java'], 'compile-roster-state-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-roster-state-dump')
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'
        optimized = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        raw = ROOT / f'build/source/{target}-RU/classes.jar'
        host = TEST / ('roster-state-' + target + '-raw-input.jar')
        with zipfile.ZipFile(optimized) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(host, entries)
        outputs, proofs = [], []
        for mode, artifact in [('reference', original), ('raw', host), ('optimized', optimized)]:
            output, proof = execute(target, mode, artifact, runtime, cp);outputs.append(output);proofs.append({'mode': mode, 'flavors': proof})
        differences = []
        for i, flavor in enumerate(['device', 'scripted']):
            for j, mode in [(1, 'raw'), (2, 'optimized')]:
                left, right = outputs[0][i].splitlines(), outputs[j][i].splitlines();assert len(left) == len(right)
                rows = [k for k, (a, b) in enumerate(zip(left, right)) if a != b]
                differences.append({'flavor': flavor, 'mode': mode, 'different_rows': len(rows), 'first_different_rows': rows[:12]})
                assert not rows, (target, flavor, mode, rows[:12])
        builds.append({'target': target, 'proofs': proofs, 'comparisons': differences})
        print('PASS roster state:', target, sum(p['observations'] for p in proofs[0]['flavors']), flush=True)
    report = {'observations': sum(p['observations'] for b in builds for p in b['proofs'][0]['flavors']),
              'scope': 'Whole genuine ContactList aggregation, group selection, chat cycling, TypingHelper and changeSoundMode. '
                       'Actual ContactItem counters, activation, ChatHistory/ChatTextList and Options save/memory RMS execute. '
                       'Device flavor preserves all application bytes and avoids positive chat display transitions. '
                       'Scripted flavor substitutes exactly seven external isShown/repaint/setCurrent instructions in VirtualList '
                       'and one Display.callSerially invocation in the actual RunnableImpl. Real error-task construction/type/data '
                       'are observed at enqueue; the queued UI callback is not executed in this probe. '
                       'retaining all other declarations, instructions and handlers and every other application/resource byte. '
                       'Fixed option/vector/chat/canvas seeds isolate startup; the typing notification readiness gate is false. '
                       'Physical repaint/display scheduling, the full notification pipeline and real device RMS are not claimed.', 'builds': builds}
    (ROOT / 'preservation/reports/source-roster-state.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')


if __name__ == '__main__':
    main()
