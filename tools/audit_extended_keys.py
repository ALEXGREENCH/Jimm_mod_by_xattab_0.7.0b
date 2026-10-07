#!/usr/bin/env python3
"""Verify original platform key switches, device calls and fall-through return arguments."""
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes


def witness(data, target, mode):
    authored = mode == 'authoring'
    subject = [(c, m) for c in data for m in c['methods'] if m['desc'] == '(I)I' and 'soft1' in m['strings']]
    assert len(subject) == 1
    owner, method = subject[0]
    code = method['code']
    key_slot = 1 if authored else 0
    assert method['access'] & 1064 == (0 if authored else 8)
    switches = [s for s in code if s.startswith('171 ')]
    assert len(switches) == 1
    keys = json.loads(switches[0][4:switches[0].index(']') + 1])
    expected = [-203, -202, 21, 22, 105, 106, 112, 113, 57345, 57346]
    if target == 'MIDP2': expected += [-6, -7]
    if target == 'MOTOROLA': expected += [-21, -22]
    if target != 'SIEMENS2': expected += [-11]
    assert keys == sorted(expected), (target, authored, keys)
    assert code[-2:] == ['21 ' + str(key_slot), '172'], 'Fallback must return the original input argument'
    calls = [s for s in code if s.startswith('182 ') and
             (s.endswith('.getKeyName(I)Ljava/lang/String;') or s.endswith('.getGameAction(I)I'))]
    assert len(calls) == 2 and calls[0].endswith('.getKeyName(I)Ljava/lang/String;')
    game = code.index(calls[1])
    assert code[game - 1] == '21 ' + str(key_slot)
    # The result is stored, tested with IFLE, and returned only when strictly positive.
    end = code[game + 1:]
    if end[0] == '89': end = end[1:]
    result_slot = end[0].split(' ')[1]
    assert end[0].startswith('54 ')
    if authored:
        assert end[1] == '21 ' + result_slot
        end = [end[0], *end[2:]]
    assert end[1].startswith('158 ') and end[2:4] == ['21 ' + result_slot, '172']
    target_index = int(end[1].split(' ')[1])
    assert code[target_index] == '21 ' + str(key_slot) or code[target_index] == '167 ' + str(len(code) - 2)
    assert [h.split(' ')[-1] for h in method['handlers']] == ['java/lang/IllegalArgumentException', 'java/lang/Exception']
    catch = int(method['handlers'][1].split(' ')[2])
    assert code[catch:] == ['87', '21 ' + str(key_slot), '172']
    ctor = next(m for m in owner['methods'] if (m['name'], m['desc']) == ('<init>', '(Ljava/lang/String;)V'))
    font = 'fontSize' if mode != 'reference' else 'u' if target == 'MIDP2' else 's'
    write = '181 ' + owner['name'] + '.' + font + ' I'
    at = ctor['code'].index(write)
    assert ctor['code'][at - 2:at + 1] == ['25 0', '16 8', write], 'Default constructor must use native SMALL_FONT'
    return {'owner': owner['name'], 'name': method['name'], 'desc': method['desc'],
            'instructions': len(code), 'platform_switch_keys': keys, 'input_argument_slot': key_slot,
            'fallback_returns_input_argument': True, 'strictly_positive_game_action_return_verified': True,
            'game_api_exception_fallback_verified': True, 'default_font_field': owner['name'] + '.' + font + ' I',
            'default_font_size': 8, 'default_constructor_font_store_verified': True,
            'device_calls': calls, 'handlers': method['handlers']}


def main():
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = recover.ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        optimized = raw.with_name('preverified.jar')
        proofs = [dict(mode=mode, **witness(classes(p, cp), target, mode)) for mode, p in
                  [('reference', original), ('authoring', raw), ('optimized', optimized)]]
        builds.append({'target': target, 'reference_sha256': recover.sha(original),
                       'source_unoptimized_class_jar_sha256': recover.sha(raw),
                       'source_optimized_class_jar_sha256': recover.sha(optimized), 'proofs': proofs})
    report = {'scope': 'Direct three-platform switch identities, exact allowed code sets, original argument fallback, '
                       'strictly-positive real getGameAction return, exception fall-through and default constructor '
                       'SMALL_FONT store in native/raw/optimized '
                       'subjects. Authoring instance argument slot and optimizer static conversion remain explicit. '
                       'Entire instruction/handler sequences are not rewritten or claimed identical; no public pipeline '
                       'or physical device keymap claim.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-extended-keys-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS extended key bytecode: 3 platform switches, original argument fallback, positive game action, exception routes and default SMALL_FONT')


if __name__ == '__main__':
    main()
