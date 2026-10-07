#!/usr/bin/env python3
"""Retain remaining whole contact helpers and explicit compiler-generated expressions."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes
from audit_vlist_helpers import translated


OWNER = 'jimm/ContactItem'
DIRECT = [
    ('a', '(Ljava/io/DataOutputStream;)V', 'saveToStream', None),
    ('a', '(Ljava/io/DataInputStream;)V', 'loadFromStream', None),
    ('a$13462e', '()V', 'addCapability', '(I)V'),
    ('c', '()I', 'getUIN', None),
    ('b', '()Ljava/lang/String;', 'getUinString', None),
    ('a', '(Lab;)V', 'setFTM', None),
    ('e', '()I', 'getUnreadMessCount', None),
    ('b', '()V', 'showHistory', None),
    ('d', '()V', 'setOfflineStatus', None),
    ('l', '()I', 'getIgnoreId', None),
    ('d', '(I)V', 'setIgnoreId', None),
    ('n', '()I', 'getInvisibleId', None),
]
EXPRESSIONS = [
    ('a', '(Lz;)I', 'post_increment_blinkingNumber', [('access$0', '(Ljimm/ContactItem;)I'), ('access$1', '(Ljimm/ContactItem;I)V')]),
    ('a', '(Lz;Z)Z', 'assign_blinkingOnline', [('access$4', '(Ljimm/ContactItem;Z)V')]),
    ('b', '(Lz;Z)Z', 'assign_blinkingOffline', [('access$6', '(Ljimm/ContactItem;Z)V')]),
]


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    inputs = {'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
              'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar'}
    data = {mode: {c['name']: c for c in classes(p, cp)} for mode, p in inputs.items()}
    def body(owner, m, native=False):
        return {'method': owner + '.' + m['name'] + m['desc'], 'access': m['access'],
                'instructions': audit.normalized(m['code']) if native else m['code'],
                'handlers': audit.normalized_handlers(m['handlers']) if native else m['handlers']}
    records, absent = [], set()
    for name, desc, role, override in DIRECT:
        native = audit.resolve_method(data['reference']['z']['methods'], name, desc)
        raw = audit.resolve_method(data['raw'][OWNER]['methods'], role, override or translated(desc))
        left, right = body('z', native, True), body(OWNER, raw)
        assert native['access'] & 40 == raw['access'] & 40
        assert not any(m['name'].split('$')[0] == role for m in data['optimized'][OWNER]['methods']), role
        specialization = None
        if role == 'addCapability':
            assert right['instructions'].count('21 1') == 1
            assert [s if s != '21 1' else '17 256' for s in right['instructions']] == left['instructions']
            call = '182 ' + OWNER + '.addCapability(I)V'
            sites = [{'caller': c['name'] + '.' + m['name'] + m['desc'], 'instruction': i}
                     for c in data['raw'].values() for m in c['methods'] for i, s in enumerate(m['code']) if s == call]
            assert sites
            for site in sites:
                caller_owner, rest = site['caller'].split('.', 1); caller_name = rest.split('(', 1)[0]; caller_desc = '(' + rest.split('(', 1)[1]
                caller = audit.resolve_method(data['raw'][caller_owner]['methods'], caller_name, caller_desc)
                assert caller['code'][site['instruction'] - 1] == '17 256'
            specialization = {'kind': 'constant_capability_argument', 'raw_slot': 1, 'native_value': 256,
                              'complete_raw_code_with_one_argument_load_replaced_equals_native': True,
                              'all_actual_raw_call_sites_pass_256': sites}
        exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
        if role not in ['saveToStream', 'loadFromStream', 'addCapability']: assert exact, role
        records.append({'role': role, 'bodies': {'reference': left, 'raw': right, 'optimized': {'method_absent': True}},
                        'same_complete_raw_instructions_and_handlers': exact, 'specialization': specialization,
                        'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                        'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
        absent.add(OWNER + '.' + raw['name'] + raw['desc'])
    expressions = []
    for name, desc, role, source_members in EXPRESSIONS:
        native = audit.resolve_method(data['reference']['z']['methods'], name, desc)
        left = body('z', native, True)
        raw_bodies = [body(OWNER, audit.resolve_method(data['raw'][OWNER]['methods'], n, d)) for n, d in source_members]
        opt_bodies = [body(OWNER, audit.resolve_method(data['optimized'][OWNER]['methods'], n, d)) for n, d in source_members]
        assert raw_bodies == opt_bodies
        if role.startswith('post_increment'):
            assert left['instructions'] == ['25 0', '89', '180 jimm/ContactItem.blinkingNumber I', '90', '4', '96', '181 jimm/ContactItem.blinkingNumber I', '172']
            assert raw_bodies[0]['instructions'] == ['25 0', '180 jimm/ContactItem.blinkingNumber I', '172']
            assert raw_bodies[1]['instructions'] == ['25 0', '21 1', '181 jimm/ContactItem.blinkingNumber I', '177']
        else:
            field = 'blinkingOnline' if role.endswith('Online') else 'blinkingOffline'
            assert left['instructions'] == ['25 0', '21 1', '90', '181 ' + OWNER + '.' + field + ' Z', '172']
            assert raw_bodies[0]['instructions'] == ['25 0', '21 1', '181 ' + OWNER + '.' + field + ' Z', '177']
        call = '184 z.' + name + desc
        callers = []
        for c in data['reference'].values():
            for m in c['methods']:
                positions = [i for i, s in enumerate(m['code']) if s == call]
                if not positions: continue
                assert all(m['code'][i + 1] == '87' for i in positions), ('Native expression return must be unused', role)
                source_owner = audit.CLASSES[c['name']]
                raw_caller = audit.resolve_method(data['raw'][source_owner]['methods'], m['name'], translated(m['desc']))
                opt_caller = audit.resolve_method(data['optimized'][source_owner]['methods'], m['name'], translated(m['desc']))
                assert any(n + d in s for n, d in source_members for s in raw_caller['code'])
                callers.append({'reference': body(c['name'], m, True), 'raw': body(source_owner, raw_caller),
                                'optimized': body(source_owner, opt_caller), 'native_return_discarded_positions': positions})
        assert callers
        expressions.append({'role': role, 'reference': left, 'raw_accessors': raw_bodies, 'optimized_accessors': opt_bodies,
                            'accessor_bodies_equal_before_after_modern_optimization': True,
                            'whole_callers': callers, 'native_return_unused_in_all_callers': True,
                            'same_native_and_source_abi': False})
    witnesses = []
    for c in data['raw'].values():
        for m in c['methods']:
            calls = sorted({s.partition(' ')[2] for s in m['code'] if s.startswith(('182 ', '183 ', '184 ')) and s.partition(' ')[2] in absent})
            if not calls: continue
            opt = [n for n in data['optimized'].get(c['name'], {}).get('methods', []) if n['name'].split('$')[0] == m['name'].split('$')[0]]
            assert not any(s.partition(' ')[2] in absent for n in opt for s in n['code'] if s.startswith(('182 ', '183 ', '184 ')))
            witnesses.append({'raw_helper_calls': calls, 'complete_raw_caller': body(c['name'], m),
                              'same_name_optimized_callers': [body(c['name'], n) for n in opt],
                              'same_descriptor_caller_present': any(n['desc'] == m['desc'] for n in opt)})
    assert absent <= {s for w in witnesses for s in w['raw_helper_calls']}
    main_members = {(n, d) for o, n, d, s in audit.METHODS if o == 'z'}
    extra = {(n, d) for n, d, role, override in DIRECT} | {(n, d) for n, d, role, source_members in EXPRESSIONS}
    all_native = {(m['name'], m['desc']) for m in data['reference']['z']['methods']}
    assert all_native == main_members | extra
    report = {'scope': 'Fifteen complete remaining May MIDP2 RU ContactItem native methods: twelve raw authoring helpers and three '
                       'compiler expressions represented by four actual raw/optimized accessors. Nine native/raw complete bodies match; '
                       'addCapability is separately proven at constant 256 in every actual raw caller. Serialization/compiler layout '
                       'differences stay in full. All twelve helper methods are absent in modern optimization, with whole callers '
                       'retained without declaring their bodies equal. Native post-increment and value-returning setters are not '
                       'invented as modern methods: full native instructions, modern read/write accessors and complete timer callers '
                       'stay explicit; native expression return values are discarded in every actual caller. This inventory alone '
                       'does not claim runtime, physical timing, concurrency or whole-program equivalence.',
              'inputs': {mode: {'path': str(p.relative_to(recover.ROOT)).replace('\\', '/'), 'sha256': recover.sha(p)} for mode, p in inputs.items()},
              'methods': records, 'compiler_expressions': expressions, 'complete_caller_witnesses': witnesses,
              'native_class_accounting': {'all_native_methods_accounted': True, 'native_methods': len(all_native),
                                          'main_audit_methods': len(main_members), 'additional_helpers_and_expressions': len(extra - main_members)}}
    (recover.ROOT / 'preservation/reports/source-contact-helper-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS ContactItem helpers: 15 native methods; 12 removed helpers / 9 exact raw bodies; capability 256 and 3 compiler expressions explicit; all 66 methods accounted')


if __name__ == '__main__': main()
