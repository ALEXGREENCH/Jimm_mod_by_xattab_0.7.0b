#!/usr/bin/env python3
"""Retain complete static text helpers and their explicit platform/compiler ABIs."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


OWNER = 'DrawControls/TextList'
SHOW_DESC = '(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIIIII)V'
RAW_COUNT_DESC = '(Ljava/lang/String;IIII)I'


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    saved_classes, saved_symbols = audit.CLASSES, audit.SYMBOLS
    builds = []
    for target, historical, text, line, vl in [('MIDP2', 'MIDP2', 'bi', 'bm', 'cd'),
                                             ('MOTOROLA', 'Moto', 'bh', 'bl', 'cb'),
                                             ('SIEMENS2', 'Siemens2', 'bg', 'bk', 'cb')]:
        inputs = {'reference': recover.ROOT / f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar',
                  'raw': recover.ROOT / f'build/source/{target}-RU/classes.jar',
                  'optimized': recover.ROOT / f'dist/source/Jimm-{target}-RU.jar'}
        data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in inputs.items()}
        audit.CLASSES = {text: OWNER, line: 'DrawControls/TextLine', vl: 'DrawControls/VirtualList'}
        audit.SYMBOLS = {
            vl + '.b(I)V': 'DrawControls/VirtualList.setFontSize(I)V',
            text + '.a(Ljava/lang/String;IIII)V': OWNER + '.addBigTextInternal(Ljava/lang/String;IIII)V',
            text + '.a()I': OWNER + '.getSize()I',
            text + '.a(I)L' + line + ';': OWNER + '.getLine(I)LDrawControls/TextLine;',
            line + '.a(I)I': 'DrawControls/TextLine.getHeight(I)I',
            line + '.a(IILjavax/microedition/lcdui/Graphics;IL' + vl + ';)V':
                'DrawControls/TextLine.paint(IILjavax/microedition/lcdui/Graphics;ILDrawControls/VirtualList;)V',
        }
        methods = []
        for name, desc, role, raw_desc in [('a', SHOW_DESC, 'showText', SHOW_DESC),
                                         ('a$ba2c332', '(Ljava/lang/String;III)I', 'getLineNumbers', RAW_COUNT_DESC)]:
            native = audit.resolve_method(data['reference'][text]['methods'], name, desc)
            raw = audit.resolve_method(data['raw'][OWNER]['methods'], role, raw_desc)
            optimized = [m for m in data['optimized'][OWNER]['methods'] if m['name'].split('$')[0] == role]
            assert len(optimized) == 1, (target, role)
            optimized = optimized[0]
            assert native['access'] & 40 == raw['access'] & 40 == optimized['access'] & 40 == 8
            def body(owner, m, reference=False):
                return {'method': owner + '.' + m['name'] + m['desc'], 'access': m['access'],
                        'instructions': audit.normalized(m['code']) if reference else m['code'],
                        'handlers': audit.normalized_handlers(m['handlers']) if reference else m['handlers']}
            bodies = {'reference': body(text, native, True), 'raw': body(OWNER, raw),
                      'optimized': body(OWNER, optimized)}
            if role == 'showText':
                assert bodies['reference']['instructions'].count('182 DrawControls/TextLine.getHeight(I)I') == 2
                assert raw['code'].count('182 DrawControls/TextLine.getHeight(I)I') == 2
                assert optimized['code'].count('182 DrawControls/TextLine.getHeight(I)I') == 2
                # Retain the complete first traversal; it must finish before any painting.
                for mode, record in bodies.items():
                    code = record['instructions']
                    height = code.index('182 DrawControls/TextLine.getHeight(I)I')
                    paint = code.index('182 DrawControls/TextLine.paint(IILjavax/microedition/lcdui/Graphics;ILDrawControls/VirtualList;)V')
                    assert height < paint and code[height + 1] == '87', (target, mode)
                    assert any(153 <= int(s.split()[0]) <= 167 and int(s.split()[1]) < height
                               for s in code[height + 2:paint]), (target, mode, 'Missing complete height-loop back edge')
                assert not any(s == '21 5' for s in native['code']), 'Native font-size argument is unused'
            else:
                assert not any(s == '21 2' for s in native['code']), 'Native font-size argument is unused'
                assert optimized['desc'] == '(Ljava/lang/String;II)I'
            comparisons = []
            for mode in ['raw', 'optimized']:
                left, right = bodies['reference'], bodies[mode]
                comparisons.append({'mode': mode,
                                    'same_complete_instructions_and_handlers': left['instructions'] == right['instructions'] and left['handlers'] == right['handlers'],
                                    'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                                    'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
            callers = {}
            for mode, owner, member in [('reference', text, native), ('raw', OWNER, raw), ('optimized', OWNER, optimized)]:
                invocation = '184 ' + owner + '.' + member['name'] + member['desc']
                callers[mode] = [{'owner': c['name'], 'complete_method': m,
                                  'call_positions': [i for i, s in enumerate(m['code']) if s == invocation]}
                                 for c in data[mode].values() for m in c['methods'] if invocation in m['code']]
                assert callers[mode], (target, mode, role)
            methods.append({'role': role, 'bodies': bodies, 'comparisons': comparisons,
                            'native_font_size_argument_unused': True, 'whole_actual_callers': callers})
        builds.append({'target': target, 'inputs': {mode: {'path': str(path.relative_to(recover.ROOT)).replace('\\', '/'),
                                                         'sha256': recover.sha(path)} for mode, path in inputs.items()}, 'methods': methods})
    audit.CLASSES, audit.SYMBOLS = saved_classes, saved_symbols
    report = {'scope': 'Two complete May static text helpers on three RU platforms: native/raw/optimized showText and getLineNumbers. '
                       'The showText height traversal and its discarded results precede painting in all current artifacts. '
                       'Complete instructions/handlers and all actual callers remain recorded, without deleting local slots, '
                       'branches or ABI differences. Native retains an unused font-size argument; modern optimization removes '
                       'it. The generic raw helpers keep their inherited arguments; no equivalence for arbitrary parameters '
                       'outside actual native callers is claimed. Structural checks alone do not establish runtime or '
                       'whole-program equivalence.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-static-text-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS static text: 6 complete native/raw/optimized helpers; height traversal before paint on all three platforms; full ABI and caller differences retained')


if __name__ == '__main__': main()
