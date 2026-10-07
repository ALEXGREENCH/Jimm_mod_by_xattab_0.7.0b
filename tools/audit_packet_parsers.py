#!/usr/bin/env python3
"""Retain whole packet parser bodies, callers and explicit constructor specialization."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_packet_parsers import configuration, NAMES


def diff(left, right):
    return list(difflib.unified_diff(left, right, lineterm=''))


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        evidence = []
        for mode, path in [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'),
                           ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                           ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp); owners = {c['name']: c for c in data}
            if mode != 'optimized': spec, parsers = configuration(target, mode == 'reference', data)
            else:
                spec = {n: 'jimm/comm/' + n for n in NAMES}; parsers = []
                spec.update(options='jimm/Options', error='jimm/JimmException', resource='jimm/util/ResourceBundle')
            root_desc = ('([BI)L' if mode == 'optimized' else '([BII)L') + spec['Packet'] + ';'
            root = next(m for m in owners[spec['Packet']]['methods'] if m['desc'] == root_desc and m['access'] & 8)
            util_calls = {s.split(' ')[1].split('.')[0] for s in root['code'] if s.startswith('184 ') and '([BI)I' in s}
            if mode == 'reference':
                assert len(util_calls) == 1; spec['util'] = next(iter(util_calls))
            else: spec['util'] = 'jimm/comm/Util'
            class_map = {spec[n]: 'jimm/comm/' + n for n in NAMES}
            class_map.update({spec['util']: 'jimm/comm/Util', spec['options']: 'jimm/Options',
                              spec['error']: 'jimm/JimmException', spec['resource']: 'jimm/util/ResourceBundle'})
            def descriptor(value):
                for a, b in class_map.items(): value = value.replace('L' + a + ';', 'L' + b + ';')
                return value
            aliases, bindings = {}, []
            if mode == 'reference':
                original_spec = {'Packet': 'an', 'SnacPacket': 'ak', 'ConnectPacket': 'h', 'DisconnectPacket': 'bn',
                                 'ToIcqSrvPacket': 'bu', 'FromIcqSrvPacket': 'ck', 'DCPacket': 'au',
                                 'util': 'co', 'options': 'cj', 'error': 'bv', 'resource': 'ai'}
                rebasing = {original_spec[k]: spec[k] for k in original_spec}
                def rebased(key):
                    owner, sep, rest = key.partition('.')
                    if not sep: return rebasing.get(key, key)
                    for a, b in rebasing.items(): rest = rest.replace('L' + a + ';', 'L' + b + ';')
                    return rebasing.get(owner, owner) + '.' + rest
                for key, canonical in audit.SYMBOLS.items():
                    if key.partition('.')[0] not in rebasing or '.' not in key: continue
                    actual = rebased(key); owner, member = actual.split('.', 1)
                    if '(' in member:
                        name, desc = member.split('(', 1); desc = '(' + desc
                        found = [m for m in owners[owner]['methods'] if m['name'] == name and m['desc'] == desc]
                    else:
                        name, desc = member.split(' ', 1)
                        found = [f for f in owners[owner]['fields'] if f['name'] == name and f['desc'] == desc]
                    if found:
                        assert len(found) == 1
                        aliases[actual] = canonical
                        bindings.append({'actual_member': actual, 'canonical': canonical, 'whole_declaration': found[0]})
                for p in parsers:
                    m = p['complete_parser']; aliases[p['owner'] + '.' + m['name'] + m['desc']] = class_map[p['owner']] + '.parse' + descriptor(m['desc'])
            def normalized(code):
                result = []
                for instruction in code:
                    op, sep, value = instruction.partition(' '); value = aliases.get(value, value)
                    if op in ['187', '189', '192', '193']: value = class_map.get(value, value)
                    elif op == '183' and '.<init>(' in value:
                        owner, ctor = value.split('.', 1); value = class_map.get(owner, owner) + '.' + ctor
                    result.append(op + sep + descriptor(value))
                return result
            def handlers(m):
                return [region + ' ' + class_map.get(exception, exception)
                        for region, _, exception in [h.rpartition(' ') for h in m['handlers']]]
            def body(owner, m):
                return {'owner': owner, 'complete_method': m, 'typed_instructions': normalized(m['code']),
                        'typed_handlers': handlers(m)}
            def callers(owner, m):
                prefix = '183 ' if m['name'] == '<init>' else '184 '
                call = prefix + owner + '.' + m['name'] + m['desc']
                return [{'owner': c['name'], 'complete_method': candidate,
                         'positions': [i for i, s in enumerate(candidate['code']) if s == call]}
                        for c in data for candidate in c['methods'] if call in candidate['code']]
            bodies = {p['role']: body(p['owner'], p['complete_parser']) for p in parsers}
            bodies['Packet'] = body(spec['Packet'], root)
            actual_callers = {n: callers(spec[n], b['complete_method']) for n, b in bodies.items()}
            assert actual_callers['Packet']
            absent = []
            if mode == 'optimized':
                for n in NAMES[1:]:
                    assert not any(m['desc'] == '([BII)L' + spec['Packet'] + ';' for m in owners[spec[n]]['methods'])
                    absent.append(n)
                assert len(root['code']) == 790
            constructors = {}
            for role, owner, desc in [
                    ('channel5', spec['Packet'], '([B)V' if mode == 'reference' else '(I[B)V'),
                    ('meta2000', spec['ToIcqSrvPacket'], '(JILjava/lang/String;[B[B)V' if mode == 'reference' else '(JILjava/lang/String;I[B[B)V'),
                    ('reference0', spec['ToIcqSrvPacket'], '(Ljava/lang/String;I[B[B)V' if mode != 'raw' else '(JLjava/lang/String;I[B[B)V')]:
                if mode == 'optimized':
                    desc = '(ILjava/lang/String;[B[B)V' if role == 'meta2000' else desc
                    if role == 'channel5': desc = '([B)V'
                matches = [m for m in owners[owner]['methods'] if m['name'] == '<init>' and m['desc'] == desc]
                assert len(matches) == 1, (target, mode, role, matches)
                constructors[role] = body(owner, matches[0]); constructors[role]['whole_actual_callers'] = callers(owner, matches[0])
                assert constructors[role]['whole_actual_callers']
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'classes': class_map,
                             'aliases': aliases, 'whole_binding_declarations': bindings, 'bodies': bodies,
                             'whole_actual_parser_callers': actual_callers, 'absent_subclass_parser_ABIs': absent,
                             'constructors': constructors})
        native, raw, optimized = evidence
        comparisons = []
        for n in NAMES:
            a, b = native['bodies'][n], raw['bodies'][n]
            comparisons.append({'role': n, 'same_complete_instructions_and_handlers': a['typed_instructions'] == b['typed_instructions'] and a['typed_handlers'] == b['typed_handlers'],
                                'full_instruction_diff': diff(a['typed_instructions'], b['typed_instructions']),
                                'full_handler_diff': diff(a['typed_handlers'], b['typed_handlers'])})
        assert comparisons[-1]['same_complete_instructions_and_handlers'], 'Complete DC parser must match'
        adapters = {
            'channel5': {'21 1': '8', '25 2': '25 1'},
            'meta2000': {'21 5': '17 2000', '25 6': '25 5', '25 7': '25 6'},
            'reference0': {'22 1': '9', '25 3': '25 1', '21 4': '21 2', '25 5': '25 3', '25 6': '25 4'}}
        specializations = []
        for role, adapter in adapters.items():
            a, b = native['constructors'][role], raw['constructors'][role]
            adapted = [adapter.get(s, s) for s in b['typed_instructions']]
            assert adapted == a['typed_instructions'] and a['typed_handlers'] == b['typed_handlers'], (target, role)
            assert (a['complete_method']['access'] & 1064) == (b['complete_method']['access'] & 1064)
            calls = b['whole_actual_callers']; count = sum(len(c['positions']) for c in calls)
            assert count == {'channel5': 1, 'meta2000': 2, 'reference0': 4}[role]
            constant_proofs = []
            for c in calls:
                code = c['complete_method']['code']
                for position in c['positions']:
                    if role == 'channel5':
                        assert code[position - 3:position] == ['8', '3', '188 8']
                        proof = code[position - 3:position]
                    else:
                        owner = b['owner']; starts = [i for i, s in enumerate(code[:position]) if s == '187 ' + owner]
                        start = starts[-1]; proof = code[start:position]
                        assert proof[1] == '89'
                        if role == 'reference0': assert proof[2] == '9'
                        else: assert proof[2] == '18 Long:2' and proof.count('17 2000') == 1
                    constant_proofs.append({'caller_owner': c['owner'], 'name': c['complete_method']['name'],
                                            'call_position': position, 'actual_argument_instructions': proof})
            specializations.append({'role': role, 'explicit_constant_and_local_slot_adapter': adapter,
                                    'adapted_complete_raw_instructions': adapted,
                                    'complete_adapter_equality': True, 'whole_actual_raw_call_sites': count,
                                    'constant_argument_proofs': constant_proofs,
                                    'unadapted_instruction_diff': diff(a['typed_instructions'], b['typed_instructions']),
                                    'modern_constructor_instruction_diff': diff(a['typed_instructions'], optimized['constructors'][role]['typed_instructions'])})
        a, b = native['bodies']['Packet'], optimized['bodies']['Packet']
        builds.append({'target': target, 'evidence': evidence, 'native_raw_parser_comparisons': comparisons,
                       'explicit_constructor_specializations': specializations,
                       'native_optimized_root_complete_instruction_diff': diff(a['typed_instructions'], b['typed_instructions']),
                       'native_optimized_root_complete_handler_diff': diff(a['typed_handlers'], b['typed_handlers'])})
    report = {'scope': 'Seven complete native/raw parsers on each of three May RU platforms, plus each actual '
                       'modern two-argument combined root parser (790 instructions) and complete actual callers. '
                       'Six subclass parsers are absent from modern optimized classes; no matching helpers are '
                       'invented. Whole literal instructions, branches, locals, handlers, declarations and full '
                       'differences remain. Typed aliases bind actual members with full declarations. Three '
                       'constructor pairs per platform preserve generic raw ABIs and explicit constant/slot '
                       'adapters; all seven raw call sites per platform prove the specialized argument domain. '
                       'Modern meta constructor also removes constant long reference 2; its full body/ABI is '
                       'retained rather than asserted equal for arbitrary reference values. These adapters are '
                       'not normalization of generic parser behavior. Complete body equality is asserted only '
                       'where explicitly recorded. Runtime evidence and external capture boundaries are in '
                       'source-packet-parsers.json and source-packet-root.json. No product source changes or '
                       'whole-program equivalence are claimed.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-packet-parsers-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS packet parser bytecode: 21 native/raw pairs, three actual optimized roots, nine explicit constructor adapters')
    return report


if __name__ == '__main__': main()
