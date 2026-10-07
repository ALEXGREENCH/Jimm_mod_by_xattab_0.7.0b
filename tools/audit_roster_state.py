#!/usr/bin/env python3
"""Compare whole roster bodies and retain explicit inlined/parameter-specialized helpers."""
import difflib
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from test_roster_state import configuration


def main():
    recover.bootstrap();audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        evidence = []
        for mode, path in [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'),
                           ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                           ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp);owners = {c['name']: c for c in data}
            spec, vl, methods = configuration(target, mode == 'reference', data)
            options = owners[spec['options']]
            saved = next(s[4:] for s in methods['sound']['code'] if s.startswith('184 ' + options['name'] + '.') and s.endswith('()V'))
            methods['save'] = next(m for m in options['methods'] if saved == options['name'] + '.' + m['name'] + m['desc'])
            names = {'next': 'showNextPrevChat', 'unread': 'getUnreadMessCount', 'items': 'getItems',
                     'typing': 'TypingHelper', 'sound': 'changeSoundMode', 'save': 'safe_save'}
            classes_map = {spec[k]: name for k, name in [('list', 'jimm/ContactList'), ('contact', 'jimm/ContactItem'),
                           ('group', 'jimm/GroupItem'), ('ui', 'jimm/JimmUI'), ('history', 'jimm/ChatHistory'),
                           ('chat', 'jimm/ChatTextList'), ('text', 'DrawControls/TextList'), ('tree', 'DrawControls/VirtualTree'),
                           ('vl', 'DrawControls/VirtualList'), ('options', 'jimm/Options')]}
            def descriptor(desc):
                for a, b in classes_map.items():desc = desc.replace('L' + a + ';', 'L' + b + ';')
                return desc
            mapping = {(spec['options'] if key == 'save' else spec['list']) + '.' + m['name'] + m['desc']:
                       ('jimm/Options' if key == 'save' else 'jimm/ContactList') + '.' + names[key] + descriptor(m['desc'])
                       for key, m in methods.items()}
            for key, owner, desc, name in [('last', 'list', 'L' + spec['contact'] + ';', 'lastChatItem'),
                                         ('contacts', 'list', 'Ljava/util/Vector;', 'cItems'), ('enter', 'list', 'Z', 'enterContactMenu'),
                                         ('receiver', 'ui', 'L' + spec['contact'] + ';', 'textMessReceiver'),
                                         ('treeField', 'list', 'L' + spec['tree'] + ';', 'tree'),
                                         ('chatText', 'chat', 'L' + spec['text'] + ';', 'textList')]:
                mapping[spec[owner] + '.' + spec[key] + ' ' + desc] = classes_map[spec[owner]] + '.' + name + ' ' + descriptor(desc)
            contact = owners[spec['contact']]
            for key, source, desc in [('getInt', 'getIntValue', '(I)I'), ('setInt', 'setIntValue', '(II)V'), ('setBoolean', 'setBooleanValue', '(IZ)V')]:
                mapping[contact['name'] + '.' + spec[key] + desc] = 'jimm/ContactItem.' + source + desc
            for owner_key, target_name, name in [('list', 'jimm/ContactList', 'getCItem'), ('contact', 'jimm/ContactItem', 'activate')]:
                desc = '(I)L' + spec['contact'] + ';' if name == 'getCItem' else '()V'
                calls = {s[4:] for m in methods.values() for s in m['code'] if s.startswith(('182 ' + spec[owner_key] + '.', '184 ' + spec[owner_key] + '.')) and s.endswith(desc)}
                assert len(calls) == 1
                mapping[next(iter(calls))] = target_name + '.' + name + descriptor(desc)
            calls = [s[4:] for s in methods['next']['code'] if s.startswith('182 ' + contact['name'] + '.') and s.endswith('(I)Z')]
            assert len(calls) == 1
            mapping[calls[0]] = 'jimm/ContactItem.getBooleanValue(I)Z'
            calls = [s[4:] for s in methods['next']['code'] if s.startswith('182 ' + contact['name'] + '.') and s.endswith('(I)Ljava/lang/String;')]
            assert len(calls) == 1
            mapping[calls[0]] = 'jimm/ContactItem.getStringValue(I)Ljava/lang/String;'
            for desc, name in [('(Ljava/lang/String;)Z', 'chatHistoryShown'), ('(Ljava/lang/String;)L' + spec['chat'] + ';', 'getChatHistoryAt')]:
                calls = [s[4:] for s in methods['typing']['code'] if s.startswith('184 ' + spec['history'] + '.') and s.endswith(desc)]
                assert len(calls) == 1
                mapping[calls[0]] = 'jimm/ChatHistory.' + name + descriptor(desc)
            repaint_calls = {s[4:] for s in methods['typing']['code'] if s.startswith('182 ' + vl['name'] + '.') and s.endswith('()V')}
            assert len(repaint_calls) == 1
            mapping[next(iter(repaint_calls))] = 'DrawControls/VirtualList.repaint()V'
            if mode == 'reference' and target == 'MIDP2':
                used = {s.partition(' ')[2] for m in methods.values() for s in m['code']}
                mapping.update({k: v for k, v in audit.SYMBOLS.items() if k in used})
            def normalize(code):
                result = []
                for s in code:
                    op, sep, value = s.partition(' ');s = op + sep + mapping.get(value, value)
                    if mode == 'reference' and target == 'MIDP2':s = audit.normalized([s])[0]
                    op, sep, value = s.partition(' ');value = mapping.get(value, value)
                    if op in ['187', '189', '192', '193']:value = classes_map.get(value, value)
                    if op == '183' and '.<init>(' in value:
                        a, b = value.split('.', 1);value = classes_map.get(a, a) + '.' + b
                    result.append(op + sep + descriptor(value))
                return result
            for key in ['next', 'typing']:assert methods[key]['access'] & 40 == 40 and not methods[key]['handlers']
            for key in ['unread', 'items', 'sound', 'save']:assert methods[key]['access'] & 40 == 8
            assert methods['unread']['handlers'] == methods['items']['handlers'] == methods['sound']['handlers'] == []
            assert len(methods['save']['handlers']) == 1 and methods['save']['handlers'][0].endswith(' java/lang/Exception')
            bodies = {key: {'method': (spec['options'] if key == 'save' else spec['list']) + '.' + m['name'] + m['desc'],
                            'access': m['access'], 'instructions': normalize(m['code']), 'handlers': m['handlers']} for key, m in methods.items()}
            helper_name = 'e' if mode == 'reference' else 'getUnreadMessCount'
            helper = [m for m in contact['methods'] if (m['name'], m['desc']) == (helper_name, '()I')]
            if mode != 'optimized':assert len(helper) == 1 and len(helper[0]['code']) == 16 and not helper[0]['handlers']
            else:assert not helper, 'Keep optimizer inlining explicit, rather than inventing a method body'
            unread_helper = {'method': contact['name'] + '.' + helper_name + '()I', 'removed_after_inlining': not helper,
                             'instructions': normalize(helper[0]['code']) if helper else None, 'handlers': helper[0]['handlers'] if helper else None}
            chat = owners[spec['chat']]
            chat_typing = [m for m in chat['methods'] if (m['name'], m['desc']) == (('d', '()V') if mode == 'reference' else ('BeginTyping', '(Z)V'))]
            if mode != 'optimized':
                assert len(chat_typing) == 1 and len(chat_typing[0]['code']) == 4 and chat_typing[0]['handlers'] == []
                if mode == 'raw':assert not any(s in ['21 1', '54 1'] for s in chat_typing[0]['code'])
            else:assert not chat_typing
            chat_helper = {'method': chat['name'] + '.' + ('d()V' if mode == 'reference' else 'BeginTyping(Z)V'),
                           'unused_boolean_parameter_in_raw': mode == 'raw', 'removed_after_inlining': not chat_typing,
                           'instructions': normalize(chat_typing[0]['code']) if chat_typing else None,
                           'handlers': chat_typing[0]['handlers'] if chat_typing else None}
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'aliases': mapping, 'classes': classes_map,
                             'bodies': bodies, 'contact_unread_helper': unread_helper, 'chat_typing_helper': chat_helper})
        comparisons = []
        for source in evidence[1:]:
            for key, left in evidence[0]['bodies'].items():
                right = source['bodies'][key];exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
                if key == 'next' and source['mode'] == 'optimized':assert exact, (target, key)
                comparisons.append({'mode': source['mode'], 'role': key, 'same_whole_body_and_handlers': exact,
                                    'same_handlers': left['handlers'] == right['handlers'],
                                    'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm=''))})
        helpers = []
        for key in ['contact_unread_helper', 'chat_typing_helper']:
            left, right = evidence[0][key], evidence[1][key]
            exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
            assert exact, (target, key)
            assert evidence[2][key]['removed_after_inlining']
            helpers.append({'role': key, 'native_raw_same_whole_instructions_and_handlers': exact,
                            'descriptors_recorded_separately': True, 'modern_optimized_helper_absent': True})
        builds.append({'target': target, 'evidence': evidence, 'comparisons': comparisons, 'helper_comparisons': helpers})
    report = {'scope': 'Six complete method bodies and actual exception bounds in native/raw/optimized classes on three platforms. '
                       'No instruction, jump, local slot, call or handler is elided from body comparisons. Optimized chat cycling '
                       'must match the full native body. Native/raw contact-unread and chat-typing helper bodies are retained separately; '
                       'their modern optimizer removal/inlining and the native unused-boolean specialization remain explicit. '
                       'This is not a fabricated whole-body equivalence for the other methods; runtime scope is source-roster-state.json.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-roster-state-bytecode.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS roster state bytecode: 6 whole methods per flavor, complete diffs/handlers and explicit unread/typing helper specialization')


if __name__ == '__main__':
    main()
