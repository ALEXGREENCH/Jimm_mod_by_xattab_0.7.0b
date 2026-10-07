#!/usr/bin/env python3
"""Check editor creation command identities/order and device guards in each RU build."""
import json
import os
import recover
import audit_source as audit
from test_jimm_urls import dump, method
from audit_send_text import classes

COMMANDS = ['cmdSend', 'cmdCancel', 'cmdInsertEmo', 'cmdInsTemplate', 'cmdQuote',
            'cmdPaste', 'transCmd', 'detransCmd', 'cmdClearText']
KEYS = {
    'MIDP2': ['b', '5', 'h3', 'f6', 'O4', 'y4', 'd0', 'c0', 'f1'],
    'MOTOROLA': ['e', '8', 'k3', 'i6', 'R4', 'B4', 'g0', 'f0', 'i1'],
    'SIEMENS2': ['9', '3', 'f3', 'c6', 'M4', 'w4', 'b0', 'a0', 'd1'],
}


def command_keys(controller):
    keys, active = {}, None
    for instruction in method(controller, '<clinit>', '()V')['code']:
        if instruction == '187 javax/microedition/lcdui/Command':
            active = []
        elif active is not None:
            active.append(instruction)
            if instruction.startswith('179 ' + controller['name'] + '.') and instruction.endswith(' Ljavax/microedition/lcdui/Command;'):
                labels = [s[len('18 String:'):] for s in active if s.startswith('18 String:')]
                assert len(labels) == 1
                field = instruction.split('.', 1)[1].split()[0]
                if field in keys:
                    assert keys[field] == labels[0], ('Reinitialized command changed key', field)
                keys[field] = labels[0]; active = None
    return keys


def creation_commands(controller, editor):
    result = []
    for i, instruction in enumerate(editor['code']):
        if instruction in ['182 javax/microedition/lcdui/TextBox.addCommand(Ljavax/microedition/lcdui/Command;)V',
                           '182 javax/microedition/lcdui/Displayable.addCommand(Ljavax/microedition/lcdui/Command;)V']:
            field = editor['code'][i - 1]
            assert field.startswith('178 ' + controller['name'] + '.') and field.endswith(' Ljavax/microedition/lcdui/Command;')
            result.append(field.split('.', 1)[1].split()[0])
    return result


def main():
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        optimized = recover.ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        authoring = recover.ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        candidates = [(c, m) for c in classes(original, cp) for m in c['methods']
                      if '18 Integer:200001' in m['code'] and any('TextBox.<init>' in s for s in m['code'])]
        assert len(candidates) == 1
        native, editor = candidates[0]
        built = dump(optimized, 'jimm/JimmUI', cp); raw = dump(authoring, 'jimm/JimmUI', cp)
        built_editor = method(built, 'writeMessage', '(Ljimm/ContactItem;Ljava/lang/String;)V')
        raw_editor = method(raw, 'writeMessage', built_editor['desc'])
        # Anchor the five field roles independently of identical primitive descriptors.
        native_box = [(f['name'], f['desc']) for f in native['fields'] if f['desc'] == 'Ljavax/microedition/lcdui/TextBox;']
        created_box = [s[4:] for s in editor['code'] if s.startswith('179 ' + native['name'] + '.') and s.endswith(' Ljavax/microedition/lcdui/TextBox;')]
        assert len(created_box) == 1 and created_box[0].split('.', 1)[1].split()[0] in [n for n, d in native_box]
        limit_at = editor['code'].index('182 javax/microedition/lcdui/TextBox.getMaxSize()I')
        native_limit = editor['code'][limit_at + 1]
        assert native_limit == '179 ' + native['name'] + '.e I'
        ctor = method(native, '<init>', '()V')
        vector_at = ctor['code'].index('183 java/util/Vector.<init>()V')
        assert ctor['code'][vector_at + 1] == '181 ' + native['name'] + '.a Ljava/util/Vector;'
        assert '181 ' + native['name'] + '.d I' in ctor['code']
        for native_name, source_name, desc in [('a', 'messageTextbox', 'Ljavax/microedition/lcdui/TextBox;'),
                                               ('a', 'strings', 'Ljava/util/Vector;'), ('d', 'current', 'I'),
                                               ('e', 'textLimit', 'I'), ('h', 'caption', 'Ljava/lang/String;')]:
            declaration = next(f for f in native['fields'] if (f['name'], f['desc']) == (native_name, desc))
            authored = next(f for f in raw['fields'] if (f['name'], f['desc']) == (source_name, desc))
            assert declaration['access'] & 88 == authored['access'] & 88
        commands = creation_commands(native, editor)
        native_keys = command_keys(native); source_keys = command_keys(built)
        assert [native_keys[name] for name in commands] == KEYS[target]
        assert [source_keys[name] for name in COMMANDS] == KEYS[target]
        assert creation_commands(built, built_editor) == creation_commands(raw, raw_editor) == COMMANDS
        for c in [editor, built_editor, raw_editor]:
            assert c['access'] & 1064 == 8
            assert '17 2048' in c['code'], 'Editor size remains independent of the 1024 sender bound'
            assert c['code'].count('184 java/lang/System.gc()V') == (1 if target == 'MIDP2' else 0)
            assert c['code'].count('17 154') == 1
            assert any(s == '182 javax/microedition/lcdui/TextBox.getCaretPosition()I' for s in c['code']) == (target != 'MOTOROLA')
        for controller, candidate, call in [(native, editor, '184 aj.a(Z)V'),
                                             (built, built_editor, '184 DrawControls/LightControl.flash(Z)V'),
                                             (raw, raw_editor, '184 DrawControls/LightControl.flash(Z)V')]:
            assert candidate['code'].count(call) == (0 if target == 'SIEMENS2' else 1)
            if target != 'SIEMENS2':
                assert candidate['code'][-3:] == ['4', call, '177']
        caption_candidates = [m for m in native['methods'] if m['desc'] == '(Ljava/lang/String;)V'
                              and any(s.endswith('.setTitle(Ljava/lang/String;)V') for s in m['code'])]
        assert len(caption_candidates) == 1
        caption = caption_candidates[0]
        for c in [caption, method(built, 'setCaption', caption['desc']), method(raw, 'setCaption', caption['desc'])]:
            assert c['access'] & 1064 == 0
            assert '17 154' not in c['code']
            assert c['code'][:2] == ['25 0', '25 1'], 'Null caption is stored directly'
            assert c['code'][2].startswith('181 ') and c['code'][2].endswith(' Ljava/lang/String;')
            assert c['strings'] == ['[', '/', '] ']
        if target == 'MIDP2':
            # The native field is witnessed at its real GC guard; the source getter is independently checked.
            at = editor['code'].index('184 java/lang/System.gc()V')
            assert editor['code'][at - 2:at] == ['178 jimm/Jimm.b Z', '153 ' + str(at + 1)]
            jimm = dump(authoring, 'jimm/Jimm', cp)
            assert method(jimm, 'is_phone_SE', '()Z')['code'] == ['178 jimm/Jimm.is_phone_SE Z', '172']
            for c in [built_editor, raw_editor]:
                at = c['code'].index('184 java/lang/System.gc()V')
                assert c['code'][at - 2:at] == ['184 jimm/Jimm.is_phone_SE()Z', '153 ' + str(at + 1)]
        builds.append({'target': target, 'reference_sha256': recover.sha(original),
                       'source_optimized_class_jar_sha256': recover.sha(optimized),
                       'source_unoptimized_class_jar_sha256': recover.sha(authoring),
                       'reference_editor': native['name'] + '.' + editor['name'] + editor['desc'],
                       'creation_command_names': COMMANDS, 'creation_command_label_keys': KEYS[target],
                       'editor_field_role_witnesses_verified': 5,
                       'editor_size': 2048, 'caption_option_in_entry_only': True,
                       'gc_phone_SE_guard_verified': target == 'MIDP2',
                       'gc_calls': 1 if target == 'MIDP2' else 0,
                       'light_true_calls': 0 if target == 'SIEMENS2' else 1,
                       'caret_uses_end_of_string': target == 'MOTOROLA'})
    report = {'scope': 'Original platform identities, static/concrete signatures, command label keys and creation '
                       'order, size 2048, option-154 location, direct nullable caption storage and counter tokens, '
                       'SE GC guard and its real source getter, light presence/tail and caret API. '
                       'This is not full body equivalence or runtime evidence for Motorola/Siemens; '
                       'actual MIDP2 editor execution is recorded separately.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-editor-bytecode.json').write_text(json.dumps(report, indent=2) + '\n',
                                                                                encoding='utf-8', newline='\n')
    print('PASS editor platform audit: 3 creation identities, command labels/order, caption location and device guards')


if __name__ == '__main__':
    main()
