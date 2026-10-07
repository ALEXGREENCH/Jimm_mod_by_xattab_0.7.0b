#!/usr/bin/env python3
"""Retain full template selection, timer accessors and form text fitting across platforms."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes


def configuration(data):
    def pick(predicate):
        candidates = [c for c in data if predicate(c)]
        assert len(candidates) == 1, [c['name'] for c in candidates]
        return candidates[0]
    templates = pick(lambda c: any(m['desc'] == '(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V' for m in c['methods'])
                     and sum(f['desc'] == 'Ljavax/microedition/lcdui/Command;' for f in c['fields']) == 9)
    timer = pick(lambda c: c['super'] == 'java/util/TimerTask' and any(m['name'] == '<init>' and m['desc'] == '(I)V' for m in c['methods'])
                 and any(m['desc'] == '()I' for m in c['methods']))
    form = pick(lambda c: any(m['desc'] == '(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;' for m in c['methods']))
    fit = next(m for m in form['methods'] if m['desc'] == '(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;')
    area = fit['code'][4]; assert area.startswith('182 ') and area.endswith('()I')
    vl = area[4:].split('.')[0]
    text_fields = [f for f in templates['fields'] if f['desc'].startswith('L') and not f['desc'].startswith('Ljava') and f['desc'] != 'L' + templates['name'] + ';']
    assert len(text_fields) == 1; text = text_fields[0]['desc'][1:-1]
    caption = next(m for m in timer['methods'] if m['name'] == 'b' and m['desc'] == '()V')
    ui_call = caption['code'][4]; assert ui_call.startswith('184 ') and ui_call.endswith('(Ljava/lang/Object;Ljava/lang/String;)V')
    return {'aq': templates['name'], 'at': timer['name'], 'd': form['name'], 'cd': vl, 'bi': text, 'cf': ui_call[4:].split('.')[0]}


MEMBERS = [('at', 'a', '()Z', 'isCanceled'), ('at', 'a', '()I', 'getType'),
           ('at', 'b', '()V', 'flashRestoreOldCaption'), ('aq', 'b', '()V', 'select'),
           ('d', 'a', '(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;', 'fitField')]


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        evidence = []
        for mode, path in [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'),
                           ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                           ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp); owners = {c['name']: c for c in data}
            rebasing = configuration(data) if mode == 'reference' else {key: audit.CLASSES[key] for key in ['at', 'aq', 'd', 'cd', 'bi', 'cf']}
            class_map = {owner: audit.CLASSES[key] for key, owner in rebasing.items()}
            def descriptor(value):
                for a, b in class_map.items(): value = value.replace('L' + a + ';', 'L' + b + ';')
                return value
            aliases, bindings = {}, []
            if mode == 'reference':
                for key, canonical in audit.SYMBOLS.items():
                    owner, sep, member = key.partition('.')
                    if owner not in rebasing or not sep: continue
                    for a, b in rebasing.items(): member = member.replace('L' + a + ';', 'L' + b + ';')
                    actual_owner = rebasing[owner]
                    if '(' in member:
                        name, desc = member.split('(', 1); desc = '(' + desc
                        matches = [m for m in owners[actual_owner]['methods'] if m['name'] == name and m['desc'] == desc]
                    else:
                        name, desc = member.split(' ', 1)
                        matches = [f for f in owners[actual_owner]['fields'] if f['name'] == name and f['desc'] == desc]
                    if matches:
                        assert len(matches) == 1
                        actual = actual_owner + '.' + member; aliases[actual] = canonical
                        bindings.append({'actual_member': actual, 'canonical': canonical, 'whole_declaration': matches[0]})
            def normalized(code):
                result = []
                for s in code:
                    op, sep, value = s.partition(' '); value = aliases.get(value, value)
                    if op in ['187', '189', '192', '193']: value = class_map.get(value, value)
                    elif op == '183' and '.<init>(' in value:
                        owner, ctor = value.split('.', 1); value = class_map.get(owner, owner) + '.' + ctor
                    result.append(op + sep + descriptor(value))
                return result
            def body(owner, m):
                return {'owner': owner, 'complete_method': m, 'typed_instructions': normalized(m['code']),
                        'typed_handlers': [region + ' ' + class_map.get(exception, exception) for region, _, exception in [h.rpartition(' ') for h in m['handlers']]]}
            def callers(owner, m):
                call = owner + '.' + m['name'] + m['desc']
                return [{'owner': c['name'], 'complete_method': candidate, 'positions': [i for i, s in enumerate(candidate['code']) if s.partition(' ')[2] == call]}
                        for c in data for candidate in c['methods'] if any(s.startswith(('182 ', '183 ', '184 ')) and s.partition(' ')[2] == call for s in candidate['code'])]
            bodies, actual_callers = {}, {}
            for key, native_name, desc, role in MEMBERS:
                owner = rebasing[key]; name = native_name if mode == 'reference' else role
                matches = [m for m in owners[owner]['methods'] if m['name'] == name and m['desc'] == desc]
                if mode == 'optimized' and role != 'select':
                    assert not matches
                    bodies[role] = {'matching_helper_ABI_absent': True, 'requested_owner': owner, 'requested_name': name, 'requested_descriptor': desc}
                    continue
                assert len(matches) == 1, (target, mode, role)
                m = matches[0]; bodies[role] = body(owner, m); actual_callers[role] = callers(owner, m)
                assert actual_callers[role]
            if mode == 'raw':
                helpers = [m for m in owners[rebasing['aq']]['methods'] if m['name'] in ['sort', 'getTemlate']]
                assert len(helpers) == 2
                assert bodies['select']['complete_method']['code'].count('25 0') == 2, 'Raw selection uses its receiver for two helper calls'
            elif mode == 'optimized':
                helpers = [m for m in owners[rebasing['aq']]['methods'] if m['name'] == 'getTemlate']
                assert len(helpers) == 1 and not any(m['name'] == 'sort' for m in owners[rebasing['aq']]['methods'])
                assert bodies['select']['complete_method']['access'] & 8 and helpers[0]['access'] & 8
            else: helpers = [m for m in owners[rebasing['aq']]['methods'] if m['name'] == 'a' and m['desc'] == '()Ljava/lang/String;']
            witnesses = []
            if mode == 'raw':
                optimized_data = {c['name']: c for c in classes(recover.ROOT / f'dist/source/Jimm-{target}-RU.jar', cp)}
                for role in ['isCanceled', 'getType', 'flashRestoreOldCaption', 'fitField']:
                    for caller in actual_callers[role]:
                        candidates = [m for m in optimized_data.get(caller['owner'], {}).get('methods', []) if m['name'].split('$')[0] == caller['complete_method']['name'].split('$')[0]]
                        invocation = bodies[role]['owner'] + '.' + bodies[role]['complete_method']['name'] + bodies[role]['complete_method']['desc']
                        assert candidates and all(not any(s.partition(' ')[2] == invocation for s in m['code']) for m in candidates)
                        witnesses.append({'role': role, 'whole_raw_caller': caller, 'whole_same_name_optimized_callers': candidates,
                                          'helper_invocations_absent': True})
            fit_method = (bodies['fitField']['complete_method'] if mode != 'optimized' else
                          next(m for m in owners[rebasing['d']]['methods'] if m['name'] == 'getItemText' and m['desc'] == '(Ljava/lang/Object;)Ljava/lang/String;'))
            fit_code = fit_method['code']; length_at = fit_code.index('182 java/lang/String.length()I')
            buffer_at = fit_code.index('187 java/lang/StringBuffer')
            assert length_at < buffer_at and fit_code[length_at + 1].startswith('54 '), 'Cache the May character-loop bound before the buffer'
            slot = fit_code[length_at + 1].split(' ')[1]
            loops = [(int(s.split(' ')[1]), i) for i, s in enumerate(fit_code)
                     if 153 <= int(s.partition(' ')[0]) <= 167 and buffer_at <= int(s.split(' ')[1]) < i]
            assert len(loops) == 1
            start, end = loops[0]; region = fit_code[start:end + 1]
            assert '21 ' + slot in region and '182 java/lang/String.length()I' not in region
            cached_fit_length = {'whole_method': body(rebasing['d'], fit_method), 'length_call_position': length_at,
                                 'buffer_position': buffer_at, 'cached_local_slot': int(slot),
                                 'character_loop_bounds': [start, end], 'cached_bound_read_in_loop': True,
                                 'no_string_length_call_inside_character_loop': True}
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'classes': class_map, 'aliases': aliases,
                             'whole_alias_binding_declarations': bindings, 'bodies': bodies, 'whole_actual_callers': actual_callers,
                             'whole_template_helpers': [body(rebasing['aq'], m) for m in helpers],
                             'whole_optimized_caller_witnesses': witnesses, 'cached_fit_length': cached_fit_length})
        comparisons = []
        for _, _, _, role in MEMBERS:
            a, b = evidence[0]['bodies'][role], evidence[1]['bodies'][role]
            exact = a['typed_instructions'] == b['typed_instructions'] and a['typed_handlers'] == b['typed_handlers']
            assert (a['complete_method']['access'] & 1064) == (b['complete_method']['access'] & 1064)
            if role in ['isCanceled', 'getType', 'flashRestoreOldCaption']: assert exact, (target, role)
            comparisons.append({'role': role, 'same_complete_instructions_and_handlers': exact,
                                'full_instruction_diff': list(difflib.unified_diff(a['typed_instructions'], b['typed_instructions'], lineterm='')),
                                'full_handler_diff': list(difflib.unified_diff(a['typed_handlers'], b['typed_handlers'], lineterm=''))})
        a, b = evidence[0]['bodies']['select'], evidence[2]['bodies']['select']
        accounting = []
        if target == 'MIDP2':
            native = classes(recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar', cp)
            for owner in ['at', 'aq', 'd']:
                inventory = {(m['name'], m['desc']) for c in native if c['name'] == owner for m in c['methods']}
                main_members = {(name, desc) for cls, name, desc, role in audit.METHODS if cls == owner}
                extra = {(name, desc) for cls, name, desc, role in MEMBERS if cls == owner}
                assert inventory == main_members | extra, (owner, inventory - main_members - extra)
                accounting.append({'owner': owner, 'native_methods': len(inventory), 'main_audit_methods': len(main_members),
                                   'additional_methods': len(extra - main_members), 'all_native_methods_accounted': True})
        builds.append({'target': target, 'evidence': evidence, 'native_raw_comparisons': comparisons,
                       'complete_native_optimized_selection_diff': list(difflib.unified_diff(a['typed_instructions'], b['typed_instructions'], lineterm='')),
                       'native_class_accounting': accounting})
    report = {'scope': 'Five complete May RU helpers on each native/raw platform; three timer bodies match exactly '
                       'including handlers. Full template selection and text fitting compiler/optimizer differences '
                       'remain, with typed aliases bound to whole actual declarations. Raw selection reads its '
                       'receiver for getTemlate and sort; receiver removal follows inlining/staticization rather '
                       'than an assertion that the raw receiver is unused. The real modern static selection body '
                       'and remaining getTemlate body are retained. Three timer helpers and fitField are absent '
                       'after optimization; all actual raw callers and whole same-name optimized callers witness '
                       'their eliminated invocations. No invented optimized helper, three-platform runtime proof '
                       'or whole-program equivalence is claimed. The restored cached character-loop bound is checked '
                       'in native/raw fitField and actual optimized getItemText on all platforms; no string length '
                       'call remains inside that loop. Existing MIDP2 replay is recorded separately.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-auxiliary-helper-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS auxiliary helpers: 15 native/raw pairs, nine timer bodies exact; real static selections and full optimizer witnesses retained')
    return report


if __name__ == '__main__': main()
