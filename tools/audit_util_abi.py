#!/usr/bin/env python3
"""Bind four changed Util ABIs to whole May methods and actual argument domains."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from audit_specialized_util_version import configuration


OWNER = 'jimm/comm/Util'
MEMBERS = [
    ('createCurrentDate', 'a$1385f3', '()J', '(Z)J', 13),
    ('getDataInputStream', 'a$6f0c2d54', '([B)Ljava/io/DataInputStream;', '([BI)Ljava/io/DataInputStream;', 2),
    ('getWord', 'a$175c50c1', '(Ljava/io/DataInputStream;)I', '(Ljava/io/DataInputStream;Z)I', 8),
    ('writeDWord', 'b$559c4327', '(Ljava/io/ByteArrayOutputStream;I)V', '(Ljava/io/ByteArrayOutputStream;IZ)V', 1),
]


def method(data, owner, name, desc):
    c = next(c for c in data if c['name'] == owner)
    return next(m for m in c['methods'] if m['name'] == name and m['desc'] == desc)


def targets(m):
    result = []
    for instruction in m['code']:
        parts = instruction.split(); op = int(parts[0])
        if op in [*range(153, 169), 198, 199]: result.append(int(parts[1]))
        elif op == 170: result += list(map(int, parts[3:]))
        elif op == 171: result += list(map(int, instruction.split('] ', 1)[1].split()))
    result += [int(h.split()[2]) for h in m['handlers']]
    return result


def callers(data, owner, m):
    call = '184 ' + owner + '.' + m['name'] + m['desc']
    return [dict(owner=c['name'], complete_method=n, positions=[i for i, s in enumerate(n['code']) if s == call])
            for c in data for n in c['methods'] if call in n['code']]


def constant(n):
    return [(n >> i) & 1 for i in range(32)]


def dword_writes(code):
    """Exact bit-vector execution of the straight-line four-write path; reject other opcodes."""
    stack = []; writes = []; returned = False
    for pos, instruction in enumerate(code):
        op, _, operand = instruction.partition(' '); op = int(op)
        if op == 25:
            assert operand == '0'; stack.append('stream-local-0')
        elif op == 21:
            assert operand == '1'; stack.append(['value-bit-%02d' % i for i in range(32)])
        elif op in [16, 17]: stack.append(constant(int(operand)))
        elif op == 18:
            assert operand.startswith('Integer:'); stack.append(constant(int(operand.split(':')[1])))
        elif op == 126:
            right, left = stack.pop(), stack.pop(); out = []
            for a, b in zip(left, right):
                assert a in [0, 1] or b in [0, 1] or a == b
                out.append(0 if a == 0 or b == 0 else b if a == 1 else a)
            stack.append(out)
        elif op in [122, 124]:
            count = stack.pop(); assert all(bit in [0, 1] for bit in count)
            shift = sum(bit << i for i, bit in enumerate(count)) & 31
            value = stack.pop(); fill = value[31] if op == 122 else 0
            stack.append(value[shift:] + [fill] * shift)
        elif op == 182:
            assert operand == 'java/io/ByteArrayOutputStream.write(I)V'
            value, receiver = stack.pop(), stack.pop(); assert receiver == 'stream-local-0'
            writes.append(dict(position=pos, receiver=receiver, all_32_argument_bits=value))
        elif op == 177:
            assert pos == len(code) - 1 and not stack; returned = True
        else: raise AssertionError(('Unsupported symbolic instruction', instruction))
    assert returned and len(writes) == 4
    return writes


def inspect_build(target, historical, cp):
    paths = dict(reference=recover.ROOT / f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar',
                 raw=recover.ROOT / f'build/source/{target}-RU/classes.jar', optimized=recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')
    data = {mode: classes(path, cp) for mode, path in paths.items()}
    spec = configuration(data['reference']); native_owner = spec['util']
    # Bind the date wrapper's actual callee to its real authoring declaration.
    core = method(data['reference'], native_owner, 'a', '(ZZ)J')
    raw_core = method(data['raw'], OWNER, 'createCurrentDate', '(ZZ)J')
    assert not core['handlers'] and '21 0' not in core['code'] and '21 1' in core['code']
    aliases = {native_owner + '.a(ZZ)J': OWNER + '.createCurrentDate(ZZ)J'}
    core_evidence = []
    for mode in paths:
        owner = native_owner if mode == 'reference' else OWNER
        found = [m for c in data[mode] if c['name'] == owner for m in c['methods'] if m['desc'] == '(ZZ)J']
        assert len(found) == (0 if target == 'MOTOROLA' and mode == 'optimized' else 1)
        if found:
            m = found[0]; sites = callers(data[mode], owner, m)
            # Direct raw core calls use GMT false; the wrapper forwards a flag already proven false below.
            domains = []
            if mode == 'raw':
                for caller in sites:
                    n = caller['complete_method']
                    for i in caller['positions']:
                        prefix = n['code'][i - 2:i]
                        forwarded = n['name'] == 'createCurrentDate' and n['desc'] == '(Z)J'
                        assert prefix == (['21 0', '3'] if forwarded else ['3', '4'])
                        edges = targets(n); assert not any(i - 2 < edge <= i for edge in edges)
                        domains.append(dict(owner=caller['owner'], name=n['name'], descriptor=n['desc'], position=i,
                                            gmt_false_via_checked_wrapper=forwarded, literal_prefix=prefix, all_ingress_targets=edges))
            core_evidence.append(dict(mode=mode, owner=owner, complete_method=m, whole_actual_callers=sites, actual_raw_gmt_domains=domains))
        else: core_evidence.append(dict(mode=mode, owner=owner, core_absent=True))
    pairs = []
    for source_name, native_name, desc, raw_desc, site_count in MEMBERS:
        evidence = []
        for mode, path in paths.items():
            owner = native_owner if mode == 'reference' else OWNER
            if mode == 'reference': m = method(data[mode], owner, native_name, desc)
            elif mode == 'raw': m = method(data[mode], owner, source_name, raw_desc)
            else:
                c = next(c for c in data[mode] if c['name'] == owner)
                found = [m for m in c['methods'] if m['name'].split('$')[0] == source_name and m['desc'] == desc]
                assert len(found) == 1; m = found[0]
            assert not m['handlers']
            sites = callers(data[mode], owner, m); domains = []
            if mode == 'raw':
                for caller in sites:
                    n = caller['complete_method']
                    for i in caller['positions']:
                        assert n['code'][i - 1] == '3'
                        edges = targets(n); assert not any(i - 1 < edge <= i for edge in edges)
                        domains.append(dict(owner=caller['owner'], name=n['name'], descriptor=n['desc'], position=i,
                                            constant_position=i - 1, value=0, all_ingress_targets=edges))
                assert len(domains) == site_count
            typed = [op + sep + aliases.get(value, value) for op, sep, value in [s.partition(' ') for s in m['code']]]
            evidence.append(dict(mode=mode, input_sha256=recover.sha(path), owner=owner, complete_method=m,
                                 typed_instructions=typed, whole_actual_callers=sites, all_actual_raw_zero_or_false_sites=domains))
        a, b, c = [e['typed_instructions'] for e in evidence]; proof = None
        if source_name == 'createCurrentDate':
            assert a == ['3', '3', '184 ' + OWNER + '.createCurrentDate(ZZ)J', '173']
            adjusted = list(b); assert adjusted[0] == '21 0'; adjusted[0] = '3'; assert adjusted == a
            if target != 'MOTOROLA': assert c == a
            else: assert len(c) == 41 and c.count('187 java/util/Date') == 1
            proof = dict(wrapper_gmt_false_substitution_position=0, complete_adjusted_raw=adjusted,
                         modern_core_inlined_into_wrapper=target == 'MOTOROLA')
        elif source_name == 'getDataInputStream':
            assert len(b) == 13 and b[5] == b[8] == '21 1' and b[9] == '100'
            adjusted = list(b); adjusted[5] = '3'; del adjusted[8:10]; assert adjusted == a == c
            proof = dict(offset_load_position=5, length_minus_zero_positions=[8, 9], complete_adjusted_raw=adjusted)
        elif source_name == 'getWord':
            assert b[:5] == ['21 1', '153 5', '25 0', '182 java/io/DataInputStream.readUnsignedShort()I', '167 16']
            assert b[5:] == a == c
            proof = dict(false_branch_entry=5, complete_false_path=b[5:], full_true_branch_retained_in_raw=evidence[1]['complete_method'])
        else:
            assert b[:2] == ['21 2', '153 35'] and b[34] == '167 67' and len(b) == 68
            assert a == c
            left, right = dword_writes(a), dword_writes(b[35:])
            assert [(w['receiver'], w['all_32_argument_bits']) for w in left] == [(w['receiver'], w['all_32_argument_bits']) for w in right]
            proof = dict(false_branch_entry=35, complete_false_path=b[35:], native_four_ordered_symbolic_writes=left,
                         raw_four_ordered_symbolic_writes=right, equal_receivers_and_all_32_argument_bits_for_every_int=True,
                         instruction_equality_claimed=False)
        pairs.append(dict(source_name=source_name, evidence=evidence, explicit_domain_proof=proof,
                          full_native_raw_diff=list(difflib.unified_diff(a, b, lineterm='')),
                          full_native_optimized_diff=list(difflib.unified_diff(a, c, lineterm=''))))
    return dict(target=target, configuration=spec, whole_date_core_bindings=core_evidence,
                native_raw_date_core_declarations=[core, raw_core], pairs=pairs)


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = [inspect_build(target, old, cp) for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]]
    report = dict(scope='Twelve complete native/raw pairs and actual optimized bodies for four changed Util ABIs across three May RU platforms. '
                  'Every raw wrapper call passes GMT false (13 sites), stream offset zero (2), little-endian word false (8), or dword false (1). '
                  'All whole callers, ingress targets, complete methods/handlers and literal diffs are retained. '
                  'Date core GMT false also follows through the checked wrapper; the May core ignores its first flag. '
                  'Motorola optimized date core is absent and the surviving 41-instruction wrapper inlines it; no absent optimized ABI is invented. '
                  'Stream and word false paths match after explicit domain adapters. Dword masks/shifts differ: exact 32-bit symbolic execution proves '
                  'the same four receiver/write arguments for every int, in order; no instruction equality is claimed for that path. '
                  'Generic source APIs, other boolean/offset domains, arbitrary host calendars and whole-program equivalence are not inferred.',
                  source_file='src/jimm/comm/Util.java', source_sha256=recover.sha(recover.ROOT / 'src/jimm/comm/Util.java'), builds=builds)
    (recover.ROOT / 'preservation/reports/source-util-abi-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS Util ABI: 12 whole native/raw pairs, all 72 raw false/zero sites, complete optimized bodies and exact 32-bit dword write proof')
    return report


if __name__ == '__main__': main()
