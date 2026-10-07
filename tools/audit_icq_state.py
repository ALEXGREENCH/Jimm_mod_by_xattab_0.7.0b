#!/usr/bin/env python3
"""Retain whole May MIDP2 Icq state/queue bodies, including optimizer differences."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


MEMBERS = [
    ('a', '(Laa;)V', 'requestAction'),
    ('a', '(Lz;)V', 'addToContactList'),
    ('a', '(Lz;)Z', 'delFromContactList'),
    ('c', '()V', 'setNotConnected'),
    ('d', '()V', 'setConnected'),
    ('d', '()I', 'setWebAware'),
    ('g', '()V', 'setPoint'),
    ('a', '()Ljava/lang/String;', 'getLastStatusChangeTime'),
    ('a', '(B)V', 'setPrivateStatus'),
    ('c', '(I)V', 'setPrivateStatusId'),
    ('e', '()I', 'getPrivateStatusId'),
]
EXACT_RAW = {'setNotConnected', 'setConnected', 'getLastStatusChangeTime',
             'setPrivateStatusId', 'getPrivateStatusId'}
EXACT_OPTIMIZED = (EXACT_RAW - {'getPrivateStatusId'}) | {'addToContactList'}


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / name for name in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    inputs = {
        'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
        'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
        'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar',
    }
    data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in inputs.items()}
    records = []
    for obfuscated, desc, name in MEMBERS:
        source_desc = desc
        for before, after in audit.CLASSES.items():
            source_desc = source_desc.replace('L' + before + ';', 'L' + after + ';')
        bodies = {}
        for mode in inputs:
            owner = 'r' if mode == 'reference' else 'jimm/comm/Icq'
            candidates = [m for m in data[mode][owner]['methods']
                          if m['name'].split('$')[0] == (obfuscated if mode == 'reference' else name)
                          and m['desc'] == (desc if mode == 'reference' else source_desc)]
            if mode == 'optimized' and name == 'getPrivateStatusId':
                assert not candidates, 'Unused getter removal must stay explicit'
                bodies[mode] = {'absent_after_optimization': True}
                continue
            assert len(candidates) == 1, (mode, name, candidates)
            m = candidates[0]
            bodies[mode] = {
                'method': owner + '.' + m['name'] + m['desc'],
                'access': m['access'],
                'instructions': audit.normalized(m['code']) if mode == 'reference' else m['code'],
                'handlers': audit.normalized_handlers(m['handlers']) if mode == 'reference' else m['handlers'],
                'absent_after_optimization': False,
            }
        if name == 'setPrivateStatus':
            assert bodies['raw']['access'] & 8 == 0
            assert bodies['reference']['access'] & 8 and bodies['optimized']['access'] & 8
            assert '25 0' not in bodies['raw']['instructions'], 'Raw instance receiver is unused'
        else:
            for mode in ['raw', 'optimized']:
                if not bodies[mode]['absent_after_optimization']:
                    assert bodies[mode]['access'] & 40 == bodies['reference']['access'] & 40
        comparisons = []
        left = bodies['reference']
        for mode, expected in [('raw', EXACT_RAW), ('optimized', EXACT_OPTIMIZED)]:
            right = bodies[mode]
            if right['absent_after_optimization']:
                comparisons.append({'mode': mode, 'whole_body_comparison_available': False})
                continue
            exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
            if name in expected:
                assert exact, (mode, name)
            comparisons.append({
                'mode': mode, 'whole_body_comparison_available': True,
                'same_whole_instructions_and_handlers': exact,
                'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm='')),
            })
        records.append({'role': name, 'bodies': bodies, 'comparisons': comparisons})
    report = {
        'scope': 'Eleven complete May MIDP2 RU Icq methods and raw source bodies; ten optimized bodies and one explicitly removed getter. '
                 'Only typed name aliases are normalized. Instructions, jumps, locals, exception regions and access flags are retained. '
                 'No product source changes, network execution, threading equivalence or whole-program equivalence claim. '
                 'The raw instance privacy setter has an unused receiver and becomes static in both optimized artifacts. '
                 'The packet constructor in setPoint is specialized differently by the two optimizers; descriptors remain explicit.',
        'inputs': {mode: {'path': str(path.relative_to(recover.ROOT)).replace('\\', '/'),
                          'sha256': recover.sha(path)} for mode, path in inputs.items()},
        'methods': records,
    }
    path = recover.ROOT / 'preservation/reports/source-icq-state-bytecode.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS Icq state bytecode: 11 native/raw bodies, 10 optimized bodies, explicit removed getter/staticization, 5 exact raw and 5 exact optimized')


if __name__ == '__main__':
    main()
