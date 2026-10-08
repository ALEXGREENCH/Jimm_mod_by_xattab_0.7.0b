#!/usr/bin/env python3
"""Verify the complete editor-preserving minimize body and its SiJaPP platform boundary."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_minimize import subject, OWNER


def inspect(path, mode, cp):
    data = classes(path, cp); m, field, destination = subject(data, mode)
    assert m['access'] & 8 and not m['handlers'] and m['code'].count('193 javax/microedition/lcdui/TextBox') == 1
    aliases = {OWNER + '.' + field['name'] + ' ' + field['desc']: OWNER + '.display ' + field['desc'],
               OWNER + '.' + destination['name'] + destination['desc']: OWNER + '.showWorkScreen()V'}
    typed = [op + sep + aliases.get(value, value) for op, sep, value in [s.partition(' ') for s in m['code']]]
    assert len(m['code']) == 19
    assert typed[14:18] == ['25 ' + ('1' if mode == 'raw' else '0'), '193 javax/microedition/lcdui/TextBox', '154 18', '184 jimm/Jimm.showWorkScreen()V']
    call = '184 ' + OWNER + '.' + m['name'] + m['desc']
    callers = [dict(owner=c['name'], complete_method=n, positions=[i for i, s in enumerate(n['code']) if s == call])
               for c in data for n in c['methods'] if call in n['code']]
    assert callers
    return dict(input_sha256=recover.sha(path), complete_method=m, complete_display_field=field,
                complete_work_screen_destination=destination, typed_instructions=typed, whole_direct_callers=callers)


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []; raw = inspect(recover.ROOT / 'build/source/MIDP2-RU/classes.jar', 'raw', cp)
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            native_path = recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_{language}/Jimm.jar'
            modern_path = recover.ROOT / f'dist/source/Jimm-{target}-{language}.jar'
            if target == 'MIDP2':
                native = inspect(native_path, 'reference', cp); modern = inspect(modern_path, 'optimized', cp)
                assert native['typed_instructions'] == modern['typed_instructions']
                assert native['complete_method']['handlers'] == modern['complete_method']['handlers'] == raw['complete_method']['handlers']
                builds.append(dict(target=target, language=language, reference=native, optimized=modern,
                                   complete_native_optimized_instructions_and_handlers_equal=True,
                                   complete_native_raw_diff=list(difflib.unified_diff(native['typed_instructions'], raw['typed_instructions'], lineterm=''))))
            else:
                declarations = []
                for mode, path in [('reference', native_path), ('optimized', modern_path)]:
                    c = next(c for c in classes(path, cp) if c['name'] == OWNER)
                    methods = [m for m in c['methods'] if m['desc'] == '(Z)V' and m['access'] & 8]
                    assert not methods, (target, language, mode, methods)
                    declarations.append(dict(mode=mode, input_sha256=recover.sha(path), actual_static_boolean_declarations=methods,
                                             all_jimm_method_declarations=[dict(name=m['name'], descriptor=m['desc'], access=m['access']) for m in c['methods']]))
                builds.append(dict(target=target, language=language, minimize_method_absent=True, evidence=declarations))
    for target in ['MOTOROLA', 'SIEMENS2']:
        c = next(c for c in classes(recover.ROOT / f'build/source/{target}-RU/classes.jar', cp) if c['name'] == OWNER)
        assert not any(m['name'] == 'setMinimized' for m in c['methods'])
    source = recover.ROOT / 'src/jimm/Jimm.java'; text = source.read_bytes()
    guard = b'if (((disp == null) || !disp.isShown()) && !(disp instanceof TextBox))'
    assert text.count(guard) == 1 and b'import javax.microedition.lcdui.TextBox;' in text
    begin = text.rfind(b'//#sijapp cond.if target is "MIDP2" #', 0, text.index(guard))
    end = text.index(b'//#sijapp cond.end#', text.index(guard)); assert begin >= 0 and end > begin
    report = dict(scope='All five May/optimized MIDP2 localization pairs match complete 19-instruction editor-preserving minimize bodies and empty handlers. '
                  'Actual display-field and work-screen-method declarations and whole direct callers are retained. Raw keeps its full local/branch compiler difference. '
                  'All ten Motorola/Siemens native/delivered Jimm inventories lack static boolean methods; their actual raw sources also lack setMinimized. '
                  'The guard remains inside the original MIDP2 SiJaPP block, with only the necessary TextBox import added. No arbitrary all-platform minimize API or whole-program equivalence claim.',
                  source_file='src/jimm/Jimm.java', source_sha256=recover.sha(source), raw_midp2=raw, builds=builds)
    (recover.ROOT / 'preservation/reports/source-minimize-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS minimize bytecode: all five MIDP2 complete 19-instruction bodies/handlers; original platform boundary retained in ten other builds')
    return report


if __name__ == '__main__': main()
