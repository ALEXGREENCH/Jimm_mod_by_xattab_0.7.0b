#!/usr/bin/env python3
"""Retain whole splash methods and chat/helper ABIs before and after optimization."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


SPLASH = [('a', '()Ljavax/microedition/lcdui/Image;', 'getSplashImage'),
          ('a', '()V', 'show'), ('f', '(I)V', 'keyPressed'), ('h', '(I)V', 'keyReleased'),
          ('g', '(I)V', 'keyRepeated'), ('b', '(II)V', 'pointerPressed'),
          ('a', '(Ljavax/microedition/lcdui/Graphics;)V', 'paint')]
CHAT = [('a', '()Ljava/util/Vector;', 'getMessData'), ('a', '(Le;)V', 'setImage'),
        ('b', '(Le;)V', 'setXstImage'), ('c', '(Le;)V', 'setHappyImage'),
        ('d', '()V', 'BeginTyping'), ('a', '(JLe;Z)V', 'AckMessage'),
        ('a', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZZLe;J)V', 'addTextToForm')]


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / name for name in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    inputs = {'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
              'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar'}
    data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in inputs.items()}
    def body(owner, method, mode):
        return {'method': owner + '.' + method['name'] + method['desc'], 'access': method['access'],
                'instructions': audit.normalized(method['code']) if mode == 'reference' else method['code'],
                'handlers': audit.normalized_handlers(method['handlers']) if mode == 'reference' else method['handlers']}
    records = []
    for owner, entries in [('cv', SPLASH), ('y', CHAT)]:
        for native_name, desc, source_name in entries:
            bodies = {}
            for mode in inputs:
                cls = owner if mode == 'reference' else audit.CLASSES[owner]
                target_desc = desc
                if mode != 'reference':
                    for before, after in audit.CLASSES.items():
                        target_desc = target_desc.replace('L' + before + ';', 'L' + after + ';')
                    if source_name == 'BeginTyping':
                        target_desc = '(Z)V'
                candidates = [m for m in data[mode][cls]['methods'] if (m['name'], m['desc']) ==
                              (native_name if mode == 'reference' else source_name, target_desc)]
                if owner == 'y' and mode == 'optimized':
                    assert not candidates, source_name
                    bodies[mode] = {'absent_after_optimization': True, 'requested_source_descriptor': target_desc}
                    continue
                assert len(candidates) == 1, (mode, source_name)
                m = candidates[0]
                if source_name == 'BeginTyping' and mode == 'raw':
                    assert not any(s in ['21 1', '54 1'] for s in m['code']), 'Unused boolean specialization'
                if mode != 'reference':
                    assert m['access'] & 40 == bodies['reference']['access'] & 40
                bodies[mode] = body(cls, m, mode)
            left = bodies['reference'];comparisons = []
            for mode in ['raw', 'optimized']:
                right = bodies[mode]
                if right.get('absent_after_optimization'):
                    comparisons.append({'mode': mode, 'whole_body_comparison_available': False})
                    continue
                exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
                if source_name in ['getMessData', 'setImage', 'setXstImage', 'setHappyImage', 'BeginTyping',
                                   'show', 'getSplashImage', 'keyReleased', 'keyRepeated']:
                    assert exact, (mode, source_name)
                comparisons.append({'mode': mode, 'whole_body_comparison_available': True,
                                    'same_complete_instructions_and_handlers': exact,
                                    'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                                    'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
            records.append({'role': source_name, 'owner': audit.CLASSES[owner], 'bodies': bodies, 'comparisons': comparisons})
    dimensions = []
    for native_name, source_name in [('k', 'getWidth'), ('l', 'getHeight')]:
        native = next(m for m in data['reference']['cd']['methods'] if (m['name'], m['desc']) == (native_name, '()I'))
        raw = next(m for m in data['raw']['DrawControls/VirtualList']['methods'] if (m['name'], m['desc']) == (source_name, '()I'))
        assert native['access'] & 8 and raw['access'] & 8 == 0
        assert '25 0' not in raw['code'] and not native['handlers'] and not raw['handlers']
        left, right = body('cd', native, 'reference'), body('DrawControls/VirtualList', raw, 'raw')
        assert left['instructions'] == right['instructions']
        assert not any(m['name'] == source_name for m in data['optimized']['DrawControls/VirtualList']['methods'])
        dimensions.append({'role': source_name, 'reference': left, 'raw': right,
                           'raw_receiver_unused': True, 'native_method_static': True,
                           'same_complete_instructions_and_handlers': True, 'modern_optimized_method_absent': True})
    witnesses = []
    helpers = {'jimm/ChatTextList.' + name for _, _, name in CHAT}
    for cls in data['raw'].values():
        for m in cls['methods']:
            called = sorted({s.partition(' ')[2].split('(', 1)[0] for s in m['code'] if s.startswith(('182 ', '184 '))
                             and s.partition(' ')[2].split('(', 1)[0] in helpers})
            if not called:
                continue
            optimized = [n for n in data['optimized'].get(cls['name'], {}).get('methods', [])
                         if n['name'].split('$')[0] == m['name'].split('$')[0] and n['desc'] == m['desc']]
            if len(optimized) != 1:
                continue
            assert not any(s.partition(' ')[2].split('(', 1)[0] in helpers for s in optimized[0]['code'])
            witnesses.append({'raw_helper_calls': called, 'raw_caller': body(cls['name'], m, 'raw'),
                              'optimized_caller': body(cls['name'], optimized[0], 'optimized'),
                              'same_caller_signature': True, 'helper_invocations_absent_after_optimization': True})
    assert {'jimm/ChatTextList.' + name for _, _, name in CHAT} <= {n for w in witnesses for n in w['raw_helper_calls']}
    report = {'scope': 'Fourteen complete May MIDP2 RU splash/chat native and raw bodies; seven splash optimized bodies and seven '
                       'explicitly absent optimized chat helpers. Only typed names are normalized; every instruction, local, jump, '
                       'handler, descriptor and access flag is retained. Five native/raw chat helpers match completely; BeginTyping '
                       'retains its unused raw boolean explicitly. Two width/height helpers have equal native/raw code, unused raw '
                       'receivers, native static methods and absent/inlined modern methods. Whole raw/optimized callers with direct '
                       'helper calls before optimization are recorded as witnesses; their differing bodies are not declared equal. '
                       'This inventory alone does not prove behavior equivalence or complete rendering; legacy replay scope is '
                       'source-ui-helper-replay.json.',
              'inputs': {mode: {'path': str(path.relative_to(recover.ROOT)).replace('\\', '/'), 'sha256': recover.sha(path)}
                         for mode, path in inputs.items()}, 'methods': records, 'dimension_helpers': dimensions,
              'whole_caller_witnesses': witnesses}
    (recover.ROOT / 'preservation/reports/source-ui-helper-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS UI helpers: 14 native/raw bodies, 7 optimized splash bodies, 7 absent chat helpers, 2 complete dimension helpers')


if __name__ == '__main__':
    main()
