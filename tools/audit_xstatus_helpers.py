#!/usr/bin/env python3
"""Retain complete removed XStatus helpers and actual optimized capability callers."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_xstatus_platforms import configuration


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
            spec = configuration(data) if mode == 'reference' else dict(status='jimm/comm/XStatus', guid='jimm/comm/GUID', contact='jimm/ContactItem', icon='DrawControls/Icon', images='DrawControls/ImageList')
            canonical = dict(status='jimm/comm/XStatus', guid='jimm/comm/GUID', contact='jimm/ContactItem', icon='DrawControls/Icon', images='DrawControls/ImageList')
            class_map = {spec[k]: canonical[k] for k in spec}; aliases, bindings = {}, []
            def descriptor(value):
                for a, b in class_map.items(): value = value.replace('L' + a + ';', 'L' + b + ';')
                return value
            def bind(owner, name, desc, field, source_name):
                declared = owners[owner]['fields' if field else 'methods']
                found = [m for m in declared if m['name'] == name and m['desc'] == desc]
                assert len(found) == 1
                actual = owner + '.' + name + (' ' if field else '') + desc
                normalized = class_map[owner] + '.' + source_name + (' ' if field else '') + descriptor(desc)
                aliases[actual] = normalized; bindings.append({'actual_member': actual, 'typed_alias': normalized, 'whole_declaration': found[0]})
                return found[0]
            if mode == 'reference':
                bind(spec['status'], 'a', 'I', True, 'index')
                bind(spec['status'], 'a', '[L' + spec['guid'] + ';', True, 'xguids')
                bind(spec['guid'], 'a', '[B', True, 'guid')
                bind(spec['status'], 'a', '(I)L' + spec['icon'] + ';', False, 'getStatusImage')
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
                return [{'owner': c['name'], 'complete_method': candidate,
                         'positions': [i for i, s in enumerate(candidate['code']) if s.partition(' ')[2] == call]}
                        for c in data for candidate in c['methods'] if any(s.startswith(('182 ', '183 ', '184 ')) and s.partition(' ')[2] == call for s in candidate['code'])]
            bodies, actual_callers, helpers = {}, {}, []
            for role, name, desc in [('setXStatus', 'a' if mode == 'reference' else 'setXStatus', '([B)V'),
                                     ('getStatusImage', 'a' if mode == 'reference' else 'getStatusImage', '()L' + spec['icon'] + ';')]:
                matches = [m for m in owners[spec['status']]['methods'] if m['name'] == name and m['desc'] == desc]
                if mode == 'optimized':
                    assert not matches
                    bodies[role] = {'matching_raw_helper_ABI_absent': True, 'requested_owner': spec['status'], 'name': name, 'descriptor': desc}
                    continue
                assert len(matches) == 1
                bodies[role] = body(spec['status'], matches[0]); actual_callers[role] = callers(spec['status'], matches[0]); assert actual_callers[role]
            constant_domain = None
            if mode != 'reference':
                helper = next(m for m in owners[spec['status']]['methods'] if m['name'] == 'getXStatus' and m['desc'] == '([B)I')
                helpers.append(body(spec['status'], helper)); actual_callers['getXStatus'] = callers(spec['status'], helper); assert actual_callers['getXStatus']
                if mode == 'raw':
                    equals = next(m for m in owners[spec['guid']]['methods'] if m['name'] == 'equals' and m['desc'] == '([B)Z')
                    helpers.append(body(spec['guid'], equals))
                else:
                    sites = []
                    for caller in actual_callers['getXStatus']:
                        code = caller['complete_method']['code']
                        for position in caller['positions']:
                            argument = code[position - 1]; assert argument.startswith('25 ')
                            slot = argument.split(' ')[1]
                            allocations = [i for i in range(position - 2) if code[i:i + 3] == ['16 16', '188 8', '58 ' + slot]]
                            assert len(allocations) == 1
                            allocation = allocations[0]; assert '58 ' + slot not in code[allocation + 3:position]
                            sites.append({'caller_owner': caller['owner'], 'name': caller['complete_method']['name'], 'call_position': position,
                                          'payload_local_slot': int(slot), 'allocated_length': 16, 'allocation_position': allocation,
                                          'payload_slot_not_reassigned_before_call': True})
                    assert len(sites) == 1
                    assert '16 16' in helper['code'] and '16 37' in helper['code']
                    constant_domain = {'actual_fixed_16_byte_argument_sites': sites, 'modern_helper_embeds_lengths_16_and_37': True}
            array_field = next(f for f in owners[spec['status']]['fields'] if f['desc'] == '[L' + spec['guid'] + ';')
            assert array_field['access'] & 24 == 24
            store = '179 ' + spec['status'] + '.' + array_field['name'] + ' ' + array_field['desc']
            writers = [{'owner': c['name'], 'complete_method': m} for c in data for m in c['methods'] if store in m['code']]
            assert len(writers) == 1 and writers[0]['owner'] == spec['status'] and writers[0]['complete_method']['name'] == '<clinit>'
            initializer = writers[0]['complete_method']; position = initializer['code'].index('189 ' + spec['guid'])
            assert initializer['code'][position - 1] == '16 37'
            if constant_domain is not None: constant_domain['whole_37_entry_table_writer'] = writers[0]
            contact_parser = next(m for m in owners[spec['contact']]['methods'] if m['name'] == ('a' if mode == 'reference' else 'setXStatus') and m['desc'] == '([B)V')
            optimizer_witnesses = []
            if mode == 'raw':
                optimized = {c['name']: c for c in classes(recover.ROOT / f'dist/source/Jimm-{target}-RU.jar', cp)}
                for role in ['setXStatus', 'getStatusImage']:
                    invocation = spec['status'] + '.' + bodies[role]['complete_method']['name'] + bodies[role]['complete_method']['desc']
                    for caller in actual_callers[role]:
                        candidates = [m for m in optimized[caller['owner']]['methods'] if m['name'].split('$')[0] == caller['complete_method']['name'].split('$')[0]]
                        assert candidates and all(not any(s.partition(' ')[2] == invocation for s in m['code']) for m in candidates)
                        optimizer_witnesses.append({'role': role, 'whole_raw_caller': caller, 'whole_same_name_optimized_callers': candidates,
                                                    'removed_helper_invocations_absent': True})
            evidence.append({'mode': mode, 'input_sha256': recover.sha(path), 'configuration': spec, 'classes': class_map,
                             'whole_alias_bindings': bindings, 'bodies': bodies, 'whole_actual_callers': actual_callers,
                             'whole_parser_helpers': helpers, 'complete_contact_parser': body(spec['contact'], contact_parser),
                             'modern_helper_constant_argument_domain': constant_domain, 'whole_table_writers': writers,
                             'whole_optimized_caller_witnesses': optimizer_witnesses})
        comparisons = []
        for role in ['setXStatus', 'getStatusImage']:
            a, b = evidence[0]['bodies'][role], evidence[1]['bodies'][role]
            exact = a['typed_instructions'] == b['typed_instructions'] and a['typed_handlers'] == b['typed_handlers']
            assert a['complete_method']['access'] & 1064 == b['complete_method']['access'] & 1064
            if role == 'getStatusImage': assert exact and len(a['typed_instructions']) == 4
            comparisons.append({'role': role, 'same_complete_instructions_and_handlers': exact,
                                'full_instruction_diff': list(difflib.unified_diff(a['typed_instructions'], b['typed_instructions'], lineterm='')),
                                'full_handler_diff': list(difflib.unified_diff(a['typed_handlers'], b['typed_handlers'], lineterm=''))})
        accounting = None
        if target == 'MIDP2':
            native_class = next(c for c in classes(recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar', cp) if c['name'] == 'bj')
            inventory = {(m['name'], m['desc']) for m in native_class['methods']}
            main_members = {(name, desc) for owner, name, desc, source in audit.METHODS if owner == 'bj'}
            extra = {('a', '([B)V'), ('a', '()Le;')}; assert inventory == main_members | extra
            accounting = {'all_native_methods_accounted': True, 'native_methods': len(inventory), 'main_audit_methods': len(main_members), 'additional_methods': len(extra - main_members)}
        builds.append({'target': target, 'evidence': evidence, 'native_raw_comparisons': comparisons, 'native_class_accounting': accounting})
    report = {'scope': 'Two remaining complete XStatus helpers on native/raw May RU classes of three platforms. '
                       'All three four-instruction image wrappers and handlers match exactly. Native capability '
                       'parser contains inlined GUID comparison/lookup; raw retains complete getXStatus and GUID.equals '
                       'helpers. Full literal branch/local/handler/ABI differences remain. Modern optimized XStatus '
                       'has neither matching helper ABI; its real ContactItem parser and surviving getXStatus helper '
                       'are retained. The modern helper embeds lengths 16 and 37: its one actual caller allocates '
                       'a fresh 16-byte buffer in a proven unreassigned local, and the sole actual table writer '
                       'creates 37 entries in clinit. No arbitrary-length private-helper reflection or replacement '
                       'of the final GUID table is declared equivalent to that specialized domain. Whole actual '
                       'callers and field writer remain explicit. Native final/raw nonfinal access differences are '
                       'retained. Runtime and host/provider boundaries are in source-xstatus-platforms.json; '
                       'whole-program equivalence is not claimed.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-xstatus-helper-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS XStatus helpers: six native/raw pairs, three exact image wrappers; complete actual optimized parser/helper domains retained')
    return report


if __name__ == '__main__': main()
