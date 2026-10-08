#!/usr/bin/env python3
"""Retain full constant-domain Util helpers and compiler-generated version setters."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes


MEMBERS = [('interest', '(ILjava/io/ByteArrayOutputStream;ILjava/lang/String;)V', 'writeAsciizTLVInterest'),
           ('array', '([BI[BII)Z', 'byteArrayEquals'), ('integer', '(Ljava/lang/String;I)I', 'strToIntDef')]


def configuration(data):
    util = [c for c in data if any(m['desc'] == '(Ljava/lang/String;III[BIZ)V' for m in c['methods'])]; assert len(util) == 1
    ui = [c for c in data if sum(m['desc'] == '(Ljava/lang/String;)Ljava/lang/String;' and len(m['code']) == 4
          and m['code'][:2] == ['25 0', '89'] and m['code'][2].startswith('179 ' + c['name'] + '.') for m in c['methods']) == 2]; assert len(ui) == 1
    conversion = next(m for m in util[0]['methods'] if m['name'] == 'a' and m['desc'] == '(Ljava/lang/String;Z)[B')
    options = {s[4:].split('.')[0] for s in conversion['code'] if s.startswith('184 ') and s.endswith('(I)Z')}; assert len(options) == 1
    return dict(util=util[0]['name'], ui=ui[0]['name'], options=next(iter(options)))


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        paths = dict(reference=recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar', raw=recover.ROOT / f'build/source/{target}-RU/classes.jar', optimized=recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')
        data = {mode: classes(path, cp) for mode, path in paths.items()}; owners = {mode: {c['name']: c for c in values} for mode, values in data.items()}
        native = configuration(data['reference']); evidence = []
        aliases = {native['util'] + '.a(Ljava/io/ByteArrayOutputStream;IZ)V': 'jimm/comm/Util.writeWord(Ljava/io/ByteArrayOutputStream;IZ)V',
                   native['util'] + '.a(Ljava/lang/String;Z)[B': 'jimm/comm/Util.stringToByteArray(Ljava/lang/String;Z)[B'}
        bindings = []
        for actual, canonical in aliases.items():
            name, desc = actual.split('.', 1)[1].split('(', 1); desc = '(' + desc
            a = next(m for m in owners['reference'][native['util']]['methods'] if m['name'] == name and m['desc'] == desc)
            name, desc = canonical.split('.', 1)[1].split('(', 1); desc = '(' + desc
            b = next(m for m in owners['raw']['jimm/comm/Util']['methods'] if m['name'] == name and m['desc'] == desc)
            bindings.append(dict(actual=actual, canonical=canonical, complete_native_declaration=a, complete_raw_declaration=b))
        def body(owner, m, mode):
            return dict(owner=owner, complete_method=m, typed_instructions=[op + sep + aliases.get(value, value) for op, sep, value in [s.partition(' ') for s in m['code']]] if mode == 'reference' else m['code'], typed_handlers=m['handlers'])
        def callers(mode, owner, method):
            call = '184 ' + owner + '.' + method['name'] + method['desc']
            return [dict(owner=c['name'], complete_method=m, positions=[i for i, s in enumerate(m['code']) if s == call]) for c in data[mode] for m in c['methods'] if call in m['code']]
        for mode in paths:
            util_owner = native['util'] if mode == 'reference' else 'jimm/comm/Util'; ui_owner = native['ui'] if mode == 'reference' else 'jimm/JimmUI'
            helpers = {}; helper_callers = {}; domains = {}; setters = {}; setter_callers = {}
            for role, desc, source_name in MEMBERS:
                found = [m for m in owners[mode][util_owner]['methods'] if m['name'] == 'a' and m['desc'] == desc] if mode == 'reference' else [m for m in owners[mode][util_owner]['methods'] if m['name'].split('$')[0] == source_name]
                assert len(found) == 1; m = found[0]; helpers[role] = body(util_owner, m, mode); sites = callers(mode, util_owner, m); assert sites
                if mode == 'reference' and role == 'interest':
                    assert m['code'][:3] == ['25 1', '17 490', '3'] and '21 0' not in m['code']
                if mode == 'reference' and role == 'array':
                    assert m['code'][:14] == ['21 1', '21 4', '96', '25 0', '190', '163 10', '21 4', '25 2', '190', '164 12', '3', '172', '3', '54 3']
                    assert '21 3' not in m['code'][:13] and '132 3 1' in m['code']
                helper_callers[role] = sites
                if mode == 'raw':
                    constants = []
                    for caller in sites:
                        code = caller['complete_method']['code']
                        for i in caller['positions']:
                            if role == 'integer': assert code[i - 1] == '3'; position = i - 1; value = 0
                            elif role == 'array':
                                assert code[i - 2] == '3' and (code[i - 1].startswith('16 ') or code[i - 1] in [str(k) for k in range(2, 9)])
                                position = i - 2; value = 0
                            else:
                                prefix = code[i - 9:i]
                                assert prefix[0] == '17 490' and prefix[1].startswith('25 ') and prefix[2].startswith('178 ') and prefix[2].endswith(' [I')
                                assert prefix[3] in ['3', '4', '5', '6'] and prefix[4:6] == ['46', '25 0']
                                assert prefix[6].startswith('180 ') and prefix[6].endswith(' [Ljava/lang/String;') and prefix[7].startswith('16 ') and prefix[8] == '50'
                                position = i - 9; value = 490
                            targets = []
                            for instruction in code:
                                parts = instruction.split(); opcode = int(parts[0])
                                if opcode in [*range(153, 169), 198, 199]: targets.append(int(parts[1]))
                                elif opcode == 170: targets += list(map(int, parts[3:]))
                                elif opcode == 171: targets += list(map(int, instruction.split('] ', 1)[1].split()))
                            targets += [int(handler.split()[2]) for handler in caller['complete_method']['handlers']]
                            assert not any(position < target <= i for target in targets), 'No control-flow edge may bypass the proven constant expression'
                            constants.append(dict(caller_owner=caller['owner'], caller_name=caller['complete_method']['name'], caller_descriptor=caller['complete_method']['desc'], call_position=i, constant_position=position, constant_value=value,
                                                  all_branch_switch_and_exception_handler_targets=targets, no_control_flow_edge_enters_after_constant_before_call=True))
                    domains[role] = constants
            for native_name, field, accessor in [('a', 'version', 'access$0'), ('b', 'aboutNotice', 'access$1')]:
                desc = '(Ljava/lang/String;)Ljava/lang/String;' if mode == 'reference' else '(Ljava/lang/String;)V'
                m = next(m for m in owners[mode][ui_owner]['methods'] if m['name'] == (native_name if mode == 'reference' else accessor) and m['desc'] == desc)
                native_field = native_name if mode == 'reference' else field
                field_decl = next(f for f in owners[mode][ui_owner]['fields'] if f['name'] == native_field and f['desc'] == 'Ljava/lang/String;')
                assert field_decl['access'] & 8 and m['handlers'] == []
                expected = ['25 0', '89', '179 ' + ui_owner + '.' + native_field + ' Ljava/lang/String;', '176'] if mode == 'reference' else ['25 0', '179 ' + ui_owner + '.' + native_field + ' Ljava/lang/String;', '177']
                assert m['code'] == expected
                sites = callers(mode, ui_owner, m); assert sum(len(s['positions']) for s in sites) == (2 if field == 'version' else 1)
                assert all(s['complete_method']['name'] == 'run' and s['complete_method']['desc'] == '()V' for s in sites)
                if mode == 'reference': assert all(s['complete_method']['code'][i + 1] == '87' for s in sites for i in s['positions'])
                setters[field] = dict(owner=ui_owner, complete_method=m, complete_field_declaration=field_decl)
                setter_callers[field] = sites
            evidence.append(dict(mode=mode, input_sha256=recover.sha(paths[mode]), complete_helpers=helpers, whole_actual_helper_callers=helper_callers,
                                 all_actual_raw_constant_argument_sites=domains, complete_version_setters=setters, whole_actual_setter_callers=setter_callers))
        comparisons = []
        for role, desc, source_name in MEMBERS:
            a = evidence[0]['complete_helpers'][role]; b = evidence[1]['complete_helpers'][role]; c = evidence[2]['complete_helpers'][role]
            adapter = None
            if role == 'integer':
                adjusted = list(b['typed_instructions']); assert adjusted[2] == adjusted[4] == '21 1'
                adjusted[2] = adjusted[4] = '3'
                adjusted = ['54 1' if s == '54 2' else '21 1' if s == '21 2' else s for s in adjusted]
                assert a['typed_instructions'] == adjusted == c['typed_instructions'] and a['typed_handlers'] == b['typed_handlers'] == c['typed_handlers']
                adapter = dict(default_arg_positions=[2, 4], value=0, result_local_slot_before=2, result_local_slot_after=1, complete_adjusted_instructions=adjusted, complete_instructions_and_handlers_equal=True)
            assert (b['complete_method']['desc'] == desc) and c['complete_method']['desc'] != desc
            comparisons.append(dict(role=role, native_descriptor=a['complete_method']['desc'], raw_descriptor=b['complete_method']['desc'], optimized_descriptor=c['complete_method']['desc'],
                                    complete_native_raw_instruction_diff=list(difflib.unified_diff(a['typed_instructions'], b['typed_instructions'], lineterm='')),
                                    complete_native_optimized_instruction_diff=list(difflib.unified_diff(a['typed_instructions'], c['typed_instructions'], lineterm='')),
                                    complete_native_raw_handler_diff=list(difflib.unified_diff(a['typed_handlers'], b['typed_handlers'], lineterm='')), explicit_integer_default_adapter=adapter))
        builds.append(dict(target=target, native_configuration=native, whole_typed_helper_bindings=bindings, evidence=evidence, full_helper_comparisons=comparisons))
    report = dict(scope='Fifteen full native/raw helper pairs across three May RU platforms: specialized Util interest TLV, array comparison, integer default, and compiler-generated JimmUI version/aboutNotice setters. '
                  'Actual raw callers all pass TLV tag 490, second-array offset zero and integer default zero; complete callers and constant expression positions are retained. '
                  'The full integer helper matches native/optimized after the explicit two zero substitutions and result-local rebase. Array loop rotation/offset folding and TLV wrapper/string-buffer/local differences remain in full literal diffs. '
                  'All three actual optimized helpers survive under changed ABI, and their whole callers remain. No arbitrary raw defaults/offsets/tags are declared equivalent to specialization. '
                  'Native four-instruction setters assign and return the same String; ECJ three-instruction synthetic setters assign and return void. All actual native task callers discard the result with POP; real raw/optimized task callers and typed field declarations are retained. '
                  'No absent String-returning raw accessor is invented. No product source/JAR change, live version HTTP/device or whole-program equivalence claim is made.', builds=builds)
    (recover.ROOT / 'preservation/reports/source-specialized-util-version-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS specialized Util/version bytecode: 15 full pairs; all raw 490/zero domains, complete integer adapters and discarded-return version setters')
    return report


if __name__ == '__main__': main()
