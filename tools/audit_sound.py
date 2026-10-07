#!/usr/bin/env python3
"""Compare complete typed sound bodies/handlers and platform silence paths; no instruction elision."""
import difflib
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from test_sound import configuration


def symbols(spec, methods):
    cl, options = spec['list'], spec['options']
    result = {cl + '.' + m['name'] + m['desc']: 'jimm/ContactList.' + name + m['desc']
              for key, name in [('create', 'createPlayer'), ('test', 'testSoundFile'), ('close', 'closePlayer'),
                                ('volume', 'setVolume'), ('notify', 'playSoundNotification'), ('event', 'playerUpdate')]
              for m in [methods[key]]}
    result[options + '.' + methods['select']['name'] + methods['select']['desc']] = 'jimm/Options.selectSoundType(Ljava/lang/String;I)V'
    for key, descriptor, name in [('self', 'L' + cl + ';', '_this'), ('player', 'Ljavax/microedition/media/Player;', 'player'), ('built', 'Z', 'treeBuilt')]:
        result[cl + '.' + spec[key] + ' ' + descriptor] = 'jimm/ContactList.' + name + ' ' + ('Ljimm/ContactList;' if key == 'self' else descriptor)
    result['jimm/Jimm.' + spec['display'] + ' Ljavax/microedition/lcdui/Display;'] = 'jimm/Jimm.display Ljavax/microedition/lcdui/Display;'
    for desc, name in [('(I)I', 'getInt'), ('(I)Z', 'getBoolean'), ('(I)Ljava/lang/String;', 'getString'), ('(ILjava/lang/String;)V', 'setString')]:
        calls = {s[4:] for m in methods.values() for s in m['code'] if s.startswith('184 ' + options + '.') and s.endswith(desc)}
        assert len(calls) == 1, (desc, calls)
        result[next(iter(calls))] = 'jimm/Options.' + name + desc
    calls = [s[4:] for s in methods['select']['code'] if s.startswith('184 ') and s.endswith('(Ljava/lang/String;C)[Ljava/lang/String;')]
    assert len(calls) == 1
    result[calls[0]] = 'jimm/comm/Util.explode(Ljava/lang/String;C)[Ljava/lang/String;'
    calls = [s[4:] for s in methods['notify']['code'] if s.startswith('184 ' + spec['splash'] + '.') and s.endswith('()Z')]
    assert len(calls) == 1
    result[calls[0]] = 'jimm/SplashCanvas.locked()Z'
    return result


def normalized(code, mapping):
    result = []
    for s in code:
        opcode, sep, value = s.partition(' ')
        result.append(opcode + sep + mapping.get(value, value))
    return result


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        inputs = [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'),
                  ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                  ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]
        evidence = []
        for mode, path in inputs:
            data = classes(path, cp)
            spec, owner, options, methods = configuration(target, mode == 'reference', data)
            mapping = symbols(spec, methods)
            bodies = {}
            for key, m in methods.items():
                if key == 'event':
                    assert m['access'] & 40 == 0 and m['handlers'] == []
                else:
                    assert m['access'] & 40 == 8
                if key in ['create', 'close', 'volume', 'select']:
                    assert m['access'] == 10
                if key == 'test':
                    assert m['access'] == 9 and m['handlers'] == []
                if key in ['create', 'close', 'volume']:
                    assert len(m['handlers']) == 1 and m['handlers'][0].endswith(' java/lang/Exception')
                if key == 'select':
                    assert m['handlers'] == [] and m['strings'] == ['wav|mp3']
                if key in ['create', 'select']:
                    assert m['code'].count('183 java/lang/StringBuffer.<init>()V') == 1
                    assert m['code'].count('182 java/lang/StringBuffer.append(Ljava/lang/String;)Ljava/lang/StringBuffer;') == 2
                    assert not any(s.startswith('183 java/lang/StringBuffer.<init>(') and s != '183 java/lang/StringBuffer.<init>()V' for s in m['code'])
                    assert '184 java/lang/String.valueOf(Ljava/lang/Object;)Ljava/lang/String;' not in m['code']
                bodies[key] = {'method': (options['name'] if key == 'select' else owner['name']) + '.' + m['name'] + m['desc'],
                               'access': m['access'], 'instructions': normalized(m['code'], mapping), 'handlers': m['handlers']}
            notification = methods['notify']
            assert notification['strings'].count('silence.wav') == (1 if target == 'SIEMENS2' else 0)
            assert notification['code'].count('194') == 1 and any(s == '195' for s in notification['code'])
            assert notification['code'].count('184 javax/microedition/media/Manager.playTone(III)V') == 4
            assert notification['code'].count('182 javax/microedition/lcdui/Display.vibrate(I)Z') == 1
            silence = []
            if target == 'SIEMENS2':
                start = notification['code'].index('18 String:silence.wav')
                # The actual warm-up sequence includes its create, volume, start and close.
                silence = normalized(notification['code'][start:start + 9], mapping)
                assert silence[2].startswith('58 ')
                slot = silence[2].split(' ')[1]
                assert silence[0:4] == ['18 String:silence.wav', '184 jimm/ContactList.createPlayer(Ljava/lang/String;)Ljavax/microedition/media/Player;', '58 ' + slot, '16 100']
                assert silence[4:] == ['184 jimm/ContactList.setVolume(I)V', '25 ' + slot, '185 javax/microedition/media/Player.start()V', '25 ' + slot, '185 javax/microedition/media/Player.close()V']
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'aliases': mapping,
                             'silence_warmup': silence, 'bodies': bodies})
        comparisons = []
        for source in evidence[1:]:
            for key, left in evidence[0]['bodies'].items():
                right = source['bodies'][key]
                exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
                if key in ['close', 'event'] or key in ['test', 'volume'] and source['mode'] == 'optimized':
                    assert exact, (target, source['mode'], key)
                comparisons.append({'mode': source['mode'], 'role': key, 'same_whole_body_and_handlers': exact,
                                    'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                                    'same_handlers': left['handlers'] == right['handlers']})
        builds.append({'target': target, 'evidence': evidence, 'comparisons': comparisons})
    report = {'scope': 'Seven complete typed methods per flavor on all three RU targets. No instruction, jump, local slot or handler '
                       'is removed from equality comparisons. Raw/public visibility and optimizer final/private changes remain recorded. '
                       'Raw DUP/local-load and return lowering, loop rotation and '
                       'notification handler layout remain explicit differences. The native, raw and optimized Siemens '
                       'silence warm-up is checked as a nine-instruction path with its actual retained local slot; '
                       'it must be absent on MIDP2 and Motorola. Both path builders retain one empty StringBuffer constructor '
                       'and two String append calls, without String-value/prefix constructor substitutions. '
                       'Runtime scope is separate in source-sound.json.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-sound-bytecode.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS sound bytecode: 3 platforms, 7 whole typed methods per flavor, complete diffs/handlers and Siemens warm-up')


if __name__ == '__main__':
    main()
