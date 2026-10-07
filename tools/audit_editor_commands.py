#!/usr/bin/env python3
"""Verify platform command identities, exact empty predicate and guarded insertion in the real handler."""
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from audit_editor import command_keys, KEYS
from test_jimm_urls import dump, method


def inspect(controller, roles, predicate_name, target):
    owner = controller['name']
    handler = method(controller, 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V')
    assert handler['access'] & 1064 == 0
    predicate = method(controller, predicate_name, '()Z')
    assert predicate['access'] & 1064 == 8 and not predicate['handlers']
    assert len(predicate['code']) == 6
    assert predicate['code'][0].startswith('178 ' + owner + '.') and predicate['code'][0].endswith(' Ljava/lang/String;')
    assert predicate['code'][1:] == ['199 4', '4', '172', '3', '172'], 'Real predicate is exactly null clipboard text'
    code = handler['code']
    field = lambda name: '178 ' + owner + '.' + roles[name] + ' Ljavax/microedition/lcdui/Command;'
    assert not any(field(name) in code for name in ['nextCmd', 'prevCmd']), 'History paging commands have no editor branches'
    quote, paste = field('cmdQuote'), field('cmdPaste')
    at = code.index(paste)
    assert code[at - 3] == quote
    assert code[at - 2].startswith('165 ')
    start = int(code[at - 2].split()[1])
    assert start == at + 2 and code[start] == '184 ' + owner + '.' + predicate_name + '()Z'
    assert code[start + 1].startswith('154 '), 'Empty clipboard must skip insertion'
    exit_at = int(code[start + 1].split()[1])
    caret = '182 javax/microedition/lcdui/TextBox.' + ('getString()Ljava/lang/String;' if target == 'MOTOROLA' else 'getCaretPosition()I')
    caret_at = code.index(caret, start)
    assert start + 1 < caret_at < exit_at
    insertion = [i for i in range(caret_at + 1, min(exit_at, len(code))) if
                 code[i].startswith('183 ' + owner + '.') and code[i].endswith('(Ljava/lang/String;I)V')]
    assert len(insertion) == 1
    insert_at = insertion[0]
    # The guard leaves the entire quote/paste branch, with no reads, inserts or mutations on its taken path.
    assert code[exit_at] == '177' or exit_at > insert_at and code[exit_at:exit_at + 2] == ['25 1', field('detransCmd')]
    if target == 'MOTOROLA':
        assert code[caret_at + 1] == '182 java/lang/String.length()I'
    return {'handler': owner + '.' + handler['name'] + handler['desc'],
            'empty_predicate': owner + '.' + predicate_name + '()Z', 'predicate_instructions': 6,
            'guarded_quote_paste_insert': True, 'history_paging_commands_absent_from_editor_handler': True,
            'caret_uses_end_of_string': target == 'MOTOROLA'}


def main():
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    audit.OUT.mkdir(parents=True, exist_ok=True)
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = recover.ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        optimized = raw.with_name('preverified.jar')
        candidates = [c for c in classes(original, cp) if any('18 Integer:200001' in m['code'] and
                      any('TextBox.<init>' in s for s in m['code']) for m in c['methods'])]
        assert len(candidates) == 1
        native = candidates[0]
        source = dump(raw, 'jimm/JimmUI', cp); built = dump(optimized, 'jimm/JimmUI', cp)
        source_keys, native_keys = command_keys(source), command_keys(native)
        names = ['cmdQuote', 'cmdPaste', 'nextCmd', 'prevCmd', 'cmdClearText', 'transCmd', 'detransCmd']
        assert [source_keys[name] for name in ['cmdQuote', 'cmdPaste', 'cmdClearText', 'transCmd', 'detransCmd']] == [KEYS[target][i] for i in [4, 5, 8, 6, 7]]
        roles = {}
        for name in names:
            matches = [short for short, key in native_keys.items() if key == source_keys[name]]
            assert len(matches) == 1, ('Ambiguous command identity', target, name, matches)
            roles[name] = matches[0]
        predicates = [m for m in native['methods'] if m['desc'] == '()Z' and m['code'] and
                      m['code'][0].startswith('178 ' + native['name'] + '.') and m['code'][0].endswith(' Ljava/lang/String;') and
                      m['code'][1:] == ['199 4', '4', '172', '3', '172']]
        assert len(predicates) == 1
        records = [inspect(native, roles, predicates[0]['name'], target)]
        for controller in [source, built]:
            assert {name: command_keys(controller)[name] for name in names} == {name: source_keys[name] for name in names}
            records.append(inspect(controller, {name: name for name in names}, 'clipBoardIsEmpty', target))
        builds.append({'target': target, 'reference_sha256': recover.sha(original),
                       'source_unoptimized_class_jar_sha256': recover.sha(raw), 'source_optimized_class_jar_sha256': recover.sha(optimized),
                       'command_roles': roles, 'command_label_keys': {name: source_keys[name] for name in names}, 'checks': records})
    report = {'scope': 'Enumerated editor command properties in actual native/raw/optimized classes: unique '
                       'command constructor keys, exact six-instruction null-clipboard predicate, its branch '
                       'skipping caret read/insertion, absent history paging commands and platform caret API. '
                       'This is not whole-handler equivalence or runtime evidence for Motorola/Siemens.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-editor-commands-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS editor command audit: 3 original identities, null clipboard guard, no history paging branches and platform caret')


if __name__ == '__main__':
    main()
