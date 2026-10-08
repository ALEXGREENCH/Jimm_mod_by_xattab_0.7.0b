#!/usr/bin/env python3
"""Recover the May command-reset write order with complete typed field evidence."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from audit_auxiliary_helpers import configuration


OWNER = 'DrawControls/VirtualList'
COMMAND = 'Ljavax/microedition/lcdui/Command;'
VECTOR = 'Ljava/util/Vector;'
ROLES = {1: ('leftMenu', COMMAND), 2: ('rightMenu', COMMAND), 5: ('defaultCommand', COMMAND),
         3: ('leftMenuItems', VECTOR), 4: ('rightMenuItems', VECTOR)}


def field_bindings(data, native, check_constants=False):
    owner = configuration(data)['cd'] if native else OWNER
    c = next(c for c in data if c['name'] == owner)
    candidates = [m for m in c['methods'] if m['desc'] == '(' + COMMAND + 'I)V']
    assert len(candidates) == 1
    add = candidates[0]; code = add['code']
    assert code[0] == '21 2'
    op, low, high, default, *labels = map(int, code[1].split())
    assert op == 170 and low == 1 and high == 5 and len(labels) == 5
    fields = {}; witnesses = []
    for value, (role, desc) in ROLES.items():
        position = labels[value - low]
        expected_prefix = ['25 0', '25 1'] if desc == COMMAND else ['25 0']
        assert code[position:position + len(expected_prefix)] == expected_prefix
        field_position = position + len(expected_prefix)
        instruction = code[field_position]
        assert instruction.startswith(('181 ' if desc == COMMAND else '180 ') + owner + '.') and instruction.endswith(' ' + desc)
        name = instruction.split()[1].split('.')[1]
        declaration = next(f for f in c['fields'] if (f['name'], f['desc']) == (name, desc))
        assert declaration['access'] & (8 | 16 | 64) == 0
        enum = None
        if not native:
            assert name == role
            if check_constants:
                constant = {1: 'MENU_TYPE_LEFT_BAR', 2: 'MENU_TYPE_RIGHT_BAR', 3: 'MENU_TYPE_LEFT', 4: 'MENU_TYPE_RIGHT', 5: 'MENU_DEFAULT'}[value]
                enum = next(f for f in c['fields'] if f['name'] == constant and f['desc'] == 'I')
                assert enum['value'] == str(value) and enum['access'] & 24 == 24
        fields[role] = declaration
        witnesses.append(dict(menu_type=value, role=role, branch_target=position, field_instruction_position=field_position,
                              whole_field_declaration=declaration, actual_raw_enum_declaration=enum, instruction=instruction))
    assert len({(f['name'], f['desc']) for f in fields.values()}) == 5
    return owner, fields, dict(owner=owner, complete_method=add, switch_default_target=default, typed_case_field_witnesses=witnesses)


def inspect_build(target, historical, cp):
    paths = dict(reference=recover.ROOT / f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar',
                 raw=recover.ROOT / f'build/source/{target}-RU/classes.jar', optimized=recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')
    evidence = []
    for mode, path in paths.items():
        data = classes(path, cp); owner, fields, witness = field_bindings(data, mode == 'reference', mode == 'raw')
        c = next(c for c in data if c['name'] == owner)
        expected_tail = ['25 0', '180 ' + owner + '.' + fields['leftMenuItems']['name'] + ' ' + VECTOR,
                         '182 java/util/Vector.removeAllElements()V', '25 0',
                         '180 ' + owner + '.' + fields['rightMenuItems']['name'] + ' ' + VECTOR,
                         '182 java/util/Vector.removeAllElements()V', '177']
        methods = [m for m in c['methods'] if m['desc'] == '()V' and len(m['code']) == 16 and m['code'][9:] == expected_tail]
        assert len(methods) == 1
        reset = methods[0]
        if mode != 'reference': assert reset['name'] == 'removeAllCommands'
        writes = []
        for i in [0, 3, 6]:
            assert reset['code'][i:i + 2] == ['25 0', '1']
            instruction = reset['code'][i + 2]
            assert instruction.startswith('181 ' + owner + '.') and instruction.endswith(' ' + COMMAND)
            role = [role for role, f in fields.items() if f['desc'] == COMMAND and instruction == '181 ' + owner + '.' + f['name'] + ' ' + COMMAND]
            assert len(role) == 1
            writes.append(role[0])
        assert set(writes) == {'leftMenu', 'rightMenu', 'defaultCommand'} and not reset['handlers']
        aliases = {owner + '.' + f['name'] + ' ' + f['desc']: OWNER + '.' + role + ' ' + f['desc'] for role, f in fields.items()}
        typed = [op + sep + aliases.get(value, value) for op, sep, value in [s.partition(' ') for s in reset['code']]]
        actual = owner + '.' + reset['name'] + reset['desc']
        callers = [dict(owner=c['name'], complete_method=m, positions=[i for i, s in enumerate(m['code']) if s.partition(' ')[2] == actual])
                   for c in data for m in c['methods'] if any(s.startswith(('182 ', '183 ', '184 ')) and s.partition(' ')[2] == actual for s in m['code'])]
        assert callers
        evidence.append(dict(mode=mode, input_sha256=recover.sha(path), owner=owner, complete_method=reset,
                             typed_instructions=typed, typed_handlers=reset['handlers'], command_null_write_order=writes,
                             whole_field_declarations=fields, whole_add_command_case_binding=witness,
                             aliases=aliases, whole_actual_reset_callers=callers))
    comparisons = []
    before = evidence[0]
    assert before['command_null_write_order'] == ['leftMenu', 'rightMenu', 'defaultCommand']
    for after in evidence[1:]:
        comparisons.append(dict(mode=after['mode'], same_complete_instructions_and_handlers=before['typed_instructions'] == after['typed_instructions'] and before['typed_handlers'] == after['typed_handlers'],
                                whole_instruction_diff=list(difflib.unified_diff(before['typed_instructions'], after['typed_instructions'], lineterm='')),
                                whole_handler_diff=list(difflib.unified_diff(before['typed_handlers'], after['typed_handlers'], lineterm=''))))
    return dict(target=target, evidence=evidence, complete_comparisons=comparisons)


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = [inspect_build(target, old, cp) for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]]
    assert all(c['same_complete_instructions_and_handlers'] for b in builds for c in b['complete_comparisons'])
    native_midp = builds[0]['evidence'][0]
    for raw, named in native_midp['aliases'].items():
        assert audit.SYMBOLS.get(raw) == named, ('The main alias must have an actual typed case/field binding', raw, named)
    source = recover.ROOT / 'src/DrawControls/VirtualList.java'
    report = dict(scope='Three complete May native/raw/optimized removeAllCommands bodies, every instruction and empty handler list. '
                  'Five field identities are bound through actual addCommandEx switch cases 1/2/5 (left/right/default command) and 3/4 (left/right vector). '
                  'Actual raw enum constants, complete field declarations, whole addCommand bodies and all direct reset callers are retained. '
                  'Native command fields are widened and methods finalized by optimization; full access values are retained, not claimed equal to raw private fields/nonfinal methods. '
                  'The recovered write order is leftMenu, rightMenu, defaultCommand, followed by left/right Vector.removeAllElements. '
                  'Instruction/handler equality does not claim general concurrency, reflective access or whole-program equivalence.',
                  source_file='src/DrawControls/VirtualList.java', source_sha256=recover.sha(source), builds=builds)
    (recover.ROOT / 'preservation/reports/source-menu-reset-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS menu reset: complete native/raw/optimized instruction and handler equality on all three May RU platforms; five fields bound through actual menu-type cases')
    return report


if __name__ == '__main__': main()
