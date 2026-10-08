#!/usr/bin/env python3
"""Retain complete error constructors and prove their actual constant argument domain."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes


def configuration(data):
    candidates = [c for c in data if c['super'] == 'java/lang/Exception'
                  and any(m['name'] == '<init>' and m['desc'] == '(II)V' for m in c['methods'])]
    assert len(candidates) == 1
    error = candidates[0]
    ctor = next(m for m in error['methods'] if m['name'] == '<init>' and m['desc'] == '(II)V')
    assert len(ctor['code']) == 18
    fields = {}
    for role, position, desc in [('code', 7, 'I'), ('critical', 10, 'Z'), ('display', 13, 'Z'), ('peer', 16, 'Z')]:
        instruction = ctor['code'][position]
        assert instruction.startswith('181 ' + error['name'] + '.') and instruction.endswith(' ' + desc)
        name = instruction.split('.')[1].split(' ')[0]
        declarations = [f for f in error['fields'] if f['name'] == name and f['desc'] == desc]
        assert len(declarations) == 1
        fields[role] = name
    call = ctor['code'][3]
    assert call.startswith('184 ' + error['name'] + '.') and call.endswith('(II)Ljava/lang/String;')
    description = call.split('.')[1].split('(')[0]
    method = next(m for m in error['methods'] if m['name'] == description and m['desc'] == '(II)Ljava/lang/String;')
    resources = [s for s in method['code'] if s.startswith('184 ') and s.endswith('(Ljava/lang/String;)Ljava/lang/String;')]
    assert len(resources) == 1
    resource, getter = resources[0][4:].split('.', 1)
    resource_class = next(c for c in data if c['name'] == resource)
    assert any(m['name'] + m['desc'] == getter for m in resource_class['methods'])
    return dict(error=error['name'], description=description, resource=resource, resourceGetter=getter.split('(')[0], **fields)


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
            data = classes(path, cp); owners = {c['name']: c for c in data}; spec = configuration(data)
            aliases = {spec['error'] + '.' + spec['description'] + '(II)Ljava/lang/String;': 'jimm/JimmException.getErrDesc(II)Ljava/lang/String;',
                       spec['resource'] + '.' + spec['resourceGetter'] + '(Ljava/lang/String;)Ljava/lang/String;': 'jimm/util/ResourceBundle.getString(Ljava/lang/String;)Ljava/lang/String;'}
            for role, name, desc in [('code', '_ErrCode', 'I'), ('critical', 'critical', 'Z'), ('display', 'displayMsg', 'Z'), ('peer', 'peer', 'Z')]:
                aliases[spec['error'] + '.' + spec[role] + ' ' + desc] = 'jimm/JimmException.' + name + ' ' + desc
            def normalize(code):
                return [op + sep + aliases.get(value, value) for op, sep, value in [s.partition(' ') for s in code]]
            constructors = {}; callers = {}; domains = {}
            for desc in ['(II)V', '(IIZ)V', '(IIB)V' if mode == 'optimized' else '(IIZZ)V']:
                method = next(m for m in owners[spec['error']]['methods'] if m['name'] == '<init>' and m['desc'] == desc)
                assert len(method['code']) == 18 and method['handlers'] == []
                constructors[desc] = dict(owner=spec['error'], complete_method=method, typed_instructions=normalize(method['code']), typed_handlers=method['handlers'])
                invocation = '183 ' + spec['error'] + '.<init>' + desc
                sites = [dict(owner=c['name'], complete_method=m, positions=[i for i, s in enumerate(m['code']) if s == invocation])
                         for c in data for m in c['methods'] if invocation in m['code']]
                assert sites; callers[desc] = sites
                if desc in ['(IIZZ)V', '(IIB)V']:
                    domain = []
                    for caller in sites:
                        for position in caller['positions']:
                            code = caller['complete_method']['code']
                            if desc == '(IIZZ)V': assert code[position - 2:position] == ['4', '4']
                            else: assert code[position - 1] == '3'
                            domain.append(dict(caller_owner=caller['owner'], caller_name=caller['complete_method']['name'],
                                               caller_descriptor=caller['complete_method']['desc'], position=position,
                                               final_argument_instructions=code[position - (1 if desc == '(IIB)V' else 2):position]))
                    assert len(domain) == 6
                    domains[desc] = domain
            if mode == 'optimized':
                assert not any(m['name'] == '<init>' and m['desc'] == '(IIZZ)V' for m in owners[spec['error']]['methods'])
                assert not any(s == '21 3' for s in constructors['(IIB)V']['complete_method']['code'])
            description = next(m for m in owners[spec['error']]['methods'] if m['name'] == spec['description'] and m['desc'] == '(II)Ljava/lang/String;')
            fields = owners[spec['error']]['fields']
            evidence.append(dict(mode=mode, input_sha256=recover.sha(path), configuration=spec, whole_fields=fields,
                                 typed_member_aliases=aliases, constructors=constructors, whole_actual_callers=callers,
                                 actual_constant_argument_sites=domains,
                                 whole_error_description=dict(owner=spec['error'], complete_method=description, typed_instructions=normalize(description['code']))))
        comparisons = []
        for desc in ['(II)V', '(IIZ)V', '(IIZZ)V']:
            native = evidence[0]['constructors'][desc]; raw = evidence[1]['constructors'][desc]
            adjusted = list(raw['typed_instructions'])
            if desc == '(IIZZ)V':
                assert adjusted[12] == '21 3' and adjusted[15] == '21 4'
                adjusted[12] = adjusted[15] = '4'
            exact = native['typed_instructions'] == raw['typed_instructions'] and native['typed_handlers'] == raw['typed_handlers']
            assert native['typed_instructions'] == adjusted and native['typed_handlers'] == raw['typed_handlers']
            optimized_desc = '(IIB)V' if desc == '(IIZZ)V' else desc
            optimized = evidence[2]['constructors'][optimized_desc]
            assert native['typed_instructions'] == optimized['typed_instructions'] and native['typed_handlers'] == optimized['typed_handlers']
            assert all(x['complete_method']['access'] == 1 for x in [native, raw, optimized])
            comparisons.append(dict(native_descriptor=desc, raw_descriptor=desc, optimized_descriptor=optimized_desc,
                                    same_complete_native_raw_instructions_and_handlers=exact,
                                    full_native_raw_diff=list(difflib.unified_diff(native['typed_instructions'], raw['typed_instructions'], lineterm='')),
                                    explicit_raw_adapter=None if exact else dict(replacements=[dict(position=12, before='21 3', after='4'), dict(position=15, before='21 4', after='4')],
                                        domain='All six actual native and raw call sites per platform pass displayMsg=true and peer=true.', complete_adjusted_instructions=adjusted),
                                    complete_body_and_handlers_equal_after_adapter=True,
                                    same_complete_native_optimized_body_and_handlers=True))
        accounting = None
        if target == 'MIDP2':
            owner = evidence[0]['configuration']['error']
            native_class = next(c for c in classes(recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar', cp) if c['name'] == owner)
            inventory = {(m['name'], m['desc']) for m in native_class['methods']}
            main_members = {(name, desc) for native_owner, name, desc, source_name in audit.METHODS if native_owner == owner}
            extra = {('<init>', '(IIZZ)V')}; assert inventory == main_members | extra
            accounting = dict(native_methods=len(inventory), main_audit_methods=len(main_members), additional_methods=len(extra - main_members), all_native_methods_accounted=True)
        builds.append(dict(target=target, evidence=evidence, complete_constructor_comparisons=comparisons, native_class_accounting=accounting))
    report = dict(scope='Nine complete native/raw constructor pairs on three May RU platforms, actual whole callers and error description helpers. '
                  'The two ordinary 18-instruction constructors match exactly. The native four-argument peer constructor ignores its two bool inputs; '
                  'the raw generic constructor retains them. Exactly replacing its two ILOAD instructions with ICONST_1 gives the complete native body/handlers. '
                  'All six actual native and raw call sites per platform pass true,true, and remain explicit. Modern optimized code has an (IIB)V '
                  'constructor with the same complete specialized body and an unused dummy byte; all six actual callers pass byte zero. '
                  'No surviving four-argument optimized ABI is invented. Full description-helper/compiler and access differences are retained. '
                  'This proves that specialization domain, not equivalence for direct reflection calls with false flags or exception dispatch/network behavior. '
                  'The generic source is retained; no application source or delivered JAR changes are made.', builds=builds)
    (recover.ROOT / 'preservation/reports/source-exception-constructor-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS exception constructor bytecode: nine full pairs, six exact; all actual true/true and unused-byte domains retained')
    return report


if __name__ == '__main__': main()
