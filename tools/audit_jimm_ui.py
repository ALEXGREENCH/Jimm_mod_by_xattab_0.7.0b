#!/usr/bin/env python3
"""Complete typed JimmUI inventory with command, state-access and compiler witnesses."""
import json
import os
import recover
import audit_source as audit
from audit_editor import command_keys
from audit_util import member, translated
from audit_send_text import classes

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-jimm-ui-bytecode.json'


def main():
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'])
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    authoring = ROOT / 'build/source/MIDP2-RU/classes.jar'
    optimized = authoring.with_name('preverified.jar')
    old = {c['name']: c for c in classes(original, cp)}
    raw = {c['name']: c for c in classes(authoring, cp)}
    built = {c['name']: c for c in classes(optimized, cp)}
    native, source, delivery = old['cf'], raw['jimm/JimmUI'], built['jimm/JimmUI']
    schema = audit.UI_SYMBOLS
    assert set(schema['fields']) == {'cf.' + f['name'] + ' ' + f['desc'] for f in native['fields']}
    assert {(m['reference_name'], m['reference_desc']) for m in schema['methods']} == {
        (m['name'], m['desc']) for m in native['methods']}
    assert len(schema['fields']) == 67 and len(schema['methods']) == 71
    command_native, command_raw, command_built = command_keys(native), command_keys(source), command_keys(delivery)
    fields = []
    for key, value in schema['fields'].items():
        name, desc = key.split('.', 1)[1].split(' ', 1)
        named, source_desc = value.split('.', 1)[1].split(' ', 1)
        assert translated(desc) == source_desc
        a = member(native['fields'], name, desc)
        b = member(source['fields'], named, source_desc)
        c = member(delivery['fields'], named, source_desc)
        assert a['access'] & 72 == b['access'] & 72 == c['access'] & 72
        assert a['access'] == c['access'], (key, a['access'], c['access'])
        accesses = {}
        for mode, controller, field in [('reference', native, key), ('authoring', source, value), ('optimized', delivery, value)]:
            accesses[mode] = [{'method': m['name'] + m['desc'], 'instructions': [i for i, s in enumerate(m['code'])
                                if s in [str(op) + ' ' + field for op in range(178, 182)]]}
                              for m in controller['methods'] if any(s[4:] == field for s in m['refs'])]
            assert accesses[mode], ('Unwitnessed controller field', mode, field)
        record = {'reference': key, 'source': value, 'typed_declaration_verified': True,
                  'static_and_volatile_modifiers_verified': True, 'optimized_access_flags_equal': True,
                  'authoring_access_flags': b['access'], 'reference_access_flags': a['access'], 'access_sites': accesses}
        if desc == 'Ljavax/microedition/lcdui/Command;':
            assert command_native[name] == command_raw[named] == command_built[named]
            record['constructor_label_key'] = command_native[name]
        fields.append(record)
    methods = []
    for entry in schema['methods']:
        before = member(native['methods'], entry['reference_name'], entry['reference_desc'])
        authored = member(source['methods'], entry['authoring_name'], entry['authoring_desc'])
        record = dict(entry, authoring_method_present=True)
        if not entry['same_optimized_signature']:
            field = 'version' if entry['reference_name'] == 'a' else 'aboutNotice'
            ref_field = 'cf.' + ('a' if field == 'version' else 'b') + ' Ljava/lang/String;'
            source_field = 'jimm/JimmUI.' + field + ' Ljava/lang/String;'
            assert before['code'] == ['25 0', '89', '179 ' + ref_field, '176']
            assert authored['code'] == ['25 0', '179 ' + source_field, '177']
            assert before['access'] & 1064 == authored['access'] & 1064 == 8
            native_call = '184 cf.' + before['name'] + before['desc']
            source_call = '184 jimm/JimmUI.' + authored['name'] + authored['desc']
            call_sites = []
            for owner in old.values():
                for m in owner['methods']:
                    for i, s in enumerate(m['code']):
                        if s == native_call:
                            assert m['code'][i + 1] == '87', 'Native assignment result must be discarded'
                            call_sites.append(owner['name'] + '.' + m['name'] + m['desc'])
            assert call_sites
            assert any(source_call in m['code'] for owner in raw.values() for m in owner['methods'])
            record.update(compiler_writer_field_verified=source_field, native_result_discarded_at_every_call=True,
                          native_call_sites=call_sites, aliased_to_incompatible_signature=False)
            methods.append(record)
            continue
        after = member(delivery['methods'], entry['source_optimized_name'], entry['source_optimized_desc'])
        assert entry['source_optimized_desc'] == translated(entry['reference_desc'])
        assert before['access'] & 1064 == authored['access'] & 1064 == after['access'] & 1064
        specialization = entry['authoring_specialization']
        if specialization == 'unused_argument':
            opcode = '21 0' if entry['authoring_desc'] == '(Z)V' else '25 0'
            assert opcode not in authored['code'], ('Unused argument proof failed', entry)
        elif specialization == 'constant_null_argument':
            call = '183 jimm/JimmUI.setString(Ljava/lang/String;)V'
            callers = [(m, i) for m in source['methods'] for i, s in enumerate(m['code']) if s == call]
            assert callers and all(m['code'][i - 1] == '1' for m, i in callers)
            assert after['code'] == ['25 0', '3', '181 jimm/JimmUI.current I', '25 0',
                                     '180 jimm/JimmUI.strings Ljava/util/Vector;', '182 java/util/Vector.removeAllElements()V',
                                     '178 jimm/JimmUI.messageTextbox Ljavax/microedition/lcdui/TextBox;', '1',
                                     '182 javax/microedition/lcdui/TextBox.setString(Ljava/lang/String;)V', '177']
        else:
            assert entry['authoring_desc'] == entry['source_optimized_desc']
        left, right = audit.normalized(before['code']), after['code']
        same_handlers = audit.normalized_handlers(before['handlers']) == after['handlers']
        record.update(signature_and_modifiers_verified=True, reference_instructions=len(left), source_instructions=len(right),
                      same_normalized_instructions=left == right, same_normalized_handlers=same_handlers,
                      same_normalized_bytecode=left == right and same_handlers,
                      reference_normalized_sha256=audit.digest(left), source_normalized_sha256=audit.digest(right))
        methods.append(record)
    # Native retained two null-control branches from removed old message boxes.
    # The real called helper returns false before any field read or external call for null.
    active = member(native['methods'], 'a', '(Lcd;)Z')
    assert active['code'] == ['25 0', '199 4', '3', '172', '25 0', '182 cd.b()Z', '172']
    tag = member(native['methods'], 'a', '()I')
    assert tag['code'][:10] == ['1', '184 cf.a(Lcd;)Z', '153 5', '178 cf.a I', '172',
                              '1', '184 cf.a(Lcd;)Z', '153 10', '178 cf.a I', '172']
    assert audit.normalized(tag['code'][10:]) == ['178 jimm/JimmUI.lstSelector LDrawControls/TextList;',
                                                 '184 jimm/JimmUI.isControlActive(LDrawControls/VirtualList;)Z',
                                                 '153 15', '178 jimm/JimmUI.curScreenTag I', '172', '2', '172']
    # Self and four compiler getters are exact reads of their anchored state fields.
    for name, desc, field in [('access$2', '()Ljimm/JimmUI;', '_this Ljimm/JimmUI;'),
                              ('access$3', '()LDrawControls/TextList;', 'aboutTextList LDrawControls/TextList;'),
                              ('access$4', '()Ljava/lang/String;', 'version Ljava/lang/String;'),
                              ('access$5', '()Ljava/lang/String;', 'aboutNotice Ljava/lang/String;')]:
        assert member(source['methods'], name, desc)['code'] == member(delivery['methods'], name, desc)['code'] == ['178 jimm/JimmUI.' + field, '176']
    ctor = member(source['methods'], '<init>', '()V')
    assert ctor['code'] == audit.normalized(member(native['methods'], '<init>', '()V')['code'])
    auth = member(source['methods'], 'authMessage', '(ILjimm/ContactItem;Ljava/lang/String;Ljava/lang/String;)V')
    assert auth['code'][:4] == ['21 0', '179 jimm/JimmUI.authType I', '25 1', '179 jimm/JimmUI.authContactItem Ljimm/ContactItem;']
    assert '17 500' in auth['code'] and '179 jimm/JimmUI.authTextbox Ljavax/microedition/lcdui/TextBox;' in auth['code']
    report = {'scope': schema['scope'] + ' Complete original MIDP2 declarations and all their actual controller access '
                       'sites are inventoried; constructor label keys, static/volatile and optimized visibility flags, '
                       'authoring parameter specializations, exact compiler getter/writer operations, discarded native '
                       'writer results, controller constructor, auth state inputs and dead null-control tag branches '
                       'are independently checked. Field access enumeration alone is not semantic identity proof. '
                       'Functional evidence is recorded in the focused runtime reports; whole-controller behavior is not claimed.',
              'reference_sha256': recover.sha(original), 'source_unoptimized_class_jar_sha256': recover.sha(authoring),
              'source_optimized_class_jar_sha256': recover.sha(optimized), 'field_declarations': len(fields),
              'native_methods': len(methods), 'same_optimized_signatures': sum(m['same_optimized_signature'] for m in methods),
              'exact_method_bodies': sum(m.get('same_normalized_bytecode', False) for m in methods),
              'incompatible_compiler_writers': 2, 'dead_null_control_branches_verified': 2,
              'fields': fields, 'methods': methods}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS JimmUI inventory: 67 fields, 71 native methods, 69 optimized signatures,',
          report['exact_method_bodies'], 'exact bodies; two incompatible compiler writers explicitly witnessed')


if __name__ == '__main__':
    main()
