#!/usr/bin/env python3
"""Retain whole anti-spam bodies and the actual raw helpers inlined by both optimizers."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_antispam import configuration


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        evidence = []
        for mode, path in [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'),
                           ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                           ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp); owners = {c['name']: c for c in data}
            spec = configuration(target, mode == 'reference', data)
            listener = owners[spec['listener']]
            spam = next(m for m in listener['methods'] if m['name'] == spec['spam'] and m['desc'] == '(L' + spec['message'] + ';)Z')
            checked = next(m for m in listener['methods'] if m['name'] == spec['checked'] and m['desc'] == '(Ljava/lang/String;)Z')
            class_map = {spec[key]: owner for key, owner in [
                ('listener', 'jimm/comm/ActionListener'), ('message', 'jimm/comm/Message'), ('plain', 'jimm/comm/PlainMessage'),
                ('list', 'jimm/ContactList'), ('contact', 'jimm/ContactItem'), ('eye', 'jimm/util/MagicEye'),
                ('options', 'jimm/Options'), ('util', 'jimm/comm/Util'), ('send', 'jimm/comm/SendMessageAction'),
                ('icq', 'jimm/comm/Icq'), ('action', 'jimm/comm/Action')]}
            def descriptor(desc):
                for a, b in class_map.items(): desc = desc.replace('L' + a + ';', 'L' + b + ';')
                return desc
            aliases, bindings = {}, []
            def bind(owner, name, desc, source):
                member = next(m for m in owners[owner]['methods'] if m['name'] == name and m['desc'] == desc)
                key = owner + '.' + name + desc
                aliases[key] = class_map[owner] + '.' + source + descriptor(desc)
                bindings.append({'actual_member': key, 'alias': aliases[key], 'complete_method': member})
                return member
            bind(spec['listener'], spec['spam'], spam['desc'], 'isSpam')
            bind(spec['listener'], spec['checked'], checked['desc'], 'isChecked')
            bind(spec['icq'], spec['request'], '(L' + spec['action'] + ';)V', 'requestAction')
            for key, field_name in [('passed', 'uins'), ('seen', 'uin1'), ('counts', 'uin2')]:
                aliases[spec['listener'] + '.' + spec[key] + ' Ljava/util/Vector;'] = 'jimm/comm/ActionListener.' + field_name + ' Ljava/util/Vector;'
            aliases[spec['message'] + '.' + spec['sender'] + ' Ljava/lang/String;'] = 'jimm/comm/Message.sndrUin Ljava/lang/String;'
            aliases[spec['plain'] + '.' + spec['messageText'] + ' Ljava/lang/String;'] = 'jimm/comm/PlainMessage.text Ljava/lang/String;'
            raw_helpers = []
            if mode == 'reference':
                for owner_key, desc, source in [
                    ('options', '(I)Z', 'getBoolean'), ('options', '(I)Ljava/lang/String;', 'getString'),
                    ('list', '(Ljava/lang/String;)L' + spec['contact'] + ';', 'getItembyUIN'),
                    ('contact', '(I)Ljava/lang/String;', 'getStringValue'),
                    ('message', '()Z', 'getOffline'), ('eye', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', 'addAction')]:
                    calls = {s[4:] for s in spam['code'] if s.startswith(('182 ' + spec[owner_key] + '.', '184 ' + spec[owner_key] + '.')) and s.endswith(desc)}
                    assert len(calls) == 1, (target, source, calls)
                    call = next(iter(calls)); owner, member = call.split('.', 1); name = member[:member.index('(')]
                    bind(owner, name, desc, source)
                bind(spec['util'], spec['dateMethod'], spec['dateDesc'], 'createCurrentDate' + (('$' + spec['dateMethod'].split('$', 1)[1]) if '$' in spec['dateMethod'] else ''))
                long_calls = {s[4:] for s in spam['code'] if s.startswith('184 ' + spec['options'] + '.') and s.endswith('()J')}
                assert len(long_calls) == 1
                member = next(iter(long_calls)).split('.', 1)[1]; name = member[:member.index('(')]
                long_method = bind(spec['options'], name, '()J', 'getLong' + (('$' + name.split('$', 1)[1]) if '$' in name else ''))
                assert len(long_method['code']) == 6 and long_method['code'][1] == '17 192', 'Original getLong is specialized to online status'
                lower_calls = {s[4:] for s in spam['code'] if s.startswith('184 ') and s.endswith('(C)C')}
                assert len(lower_calls) == 1
                call = next(iter(lower_calls)); owner, member = call.split('.', 1); name = member[:member.index('(')]
                class_map[owner] = 'jimm/util/StringConvertor'; bind(owner, name, '(C)C', 'toLowerCase')
                assert len(spam['code']) == 192 and len(checked['code']) == 18
                assert len(spam['handlers']) == 1 and spam['handlers'][0].endswith(' java/lang/Exception')
            else:
                for name in ['isCheckedData', 'stringEquals', 'sendMessage']:
                    matches = [m for m in listener['methods'] if m['name'].split('$')[0] == name]
                    if mode == 'raw':
                        assert len(matches) == 1
                        helper = matches[0]
                        assert '184 ' + spec['listener'] + '.' + helper['name'] + helper['desc'] in spam['code']
                        raw_helpers.append(helper)
                    else: assert not matches, (target, name, 'Keep inlining explicit')
                assert len(spam['code']) == (104 if mode == 'raw' else 182)
                assert len(checked['code']) == 18
            def normalized(code):
                result = []
                for s in code:
                    op, sep, value = s.partition(' '); value = aliases.get(value, value)
                    if op in ['187', '189', '192', '193']: value = class_map.get(value, value)
                    elif op == '183' and '.<init>(' in value:
                        owner, ctor = value.split('.', 1); value = class_map.get(owner, owner) + '.' + ctor
                    result.append(op + sep + descriptor(value))
                return result
            bodies = {name: {'owner': listener['name'], 'complete_method': m,
                             'typed_instructions': normalized(m['code']), 'handlers': m['handlers']}
                      for name, m in [('isSpam', spam), ('isChecked', checked)]}
            calls = {}
            for role, m in [('isSpam', spam), ('isChecked', checked)]:
                invocation = '184 ' + spec['listener'] + '.' + m['name'] + m['desc']
                calls[role] = [{'owner': c['name'], 'complete_method': caller,
                                'call_positions': [i for i, s in enumerate(caller['code']) if s == invocation]}
                               for c in data for caller in c['methods'] if invocation in caller['code']]
                assert calls[role]
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'classes': class_map, 'aliases': aliases,
                             'whole_binding_methods': bindings, 'fields': listener['fields'], 'bodies': bodies,
                             'whole_actual_callers': calls, 'whole_raw_inlined_helpers': raw_helpers})
        comparisons = []
        for mode in ['raw', 'optimized']:
            other = next(e for e in evidence if e['mode'] == mode)
            for role, left in evidence[0]['bodies'].items():
                right = other['bodies'][role]
                exact = left['typed_instructions'] == right['typed_instructions'] and left['handlers'] == right['handlers']
                comparisons.append({'mode': mode, 'role': role, 'same_complete_instructions_and_handlers': exact,
                                    'full_instruction_diff': list(difflib.unified_diff(left['typed_instructions'], right['typed_instructions'], lineterm='')),
                                    'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
        builds.append({'target': target, 'evidence': evidence, 'comparisons': comparisons})
    report = {'scope': 'Complete May RU isSpam/isChecked bodies on three platforms, with complete raw isCheckedData, '
                       'stringEquals and sendMessage bodies that both optimizers inline into isSpam. Typed aliases retain '
                       'their actual descriptors and full binding methods/callers. All instructions, branches, local slots, '
                       'handlers and access flags remain explicit; native 192 and optimized 182 instructions differ in '
                       'loop rotation, null comparisons, local clearing, condition lowering and inlining. The specialized '
                       'online-status getter and date helper ABIs remain explicit. No whole-body equality, invented '
                       'optimized helper, product source change or whole-program equivalence is claimed. Execution '
                       'evidence and capture boundaries are in source-antispam.json.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-antispam-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS anti-spam bytecode: 6 complete native/raw/optimized bodies; 9 raw inlined helpers; full differences retained')
    return report


if __name__ == '__main__': main()
