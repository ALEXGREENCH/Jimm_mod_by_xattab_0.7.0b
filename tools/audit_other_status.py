#!/usr/bin/env python3
"""Compare whole May MIDP2 OtherAction packets without hiding compiler differences."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / name for name in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    paths = {
        'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
        'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
        'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar',
    }
    data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in paths.items()}
    records = []
    for desc, name in [('([Lbh;)V', 'setUserInfo'), ('(I)V', 'setStatus'), ('(B)V', 'setPrivateStatus')]:
        source_desc = desc.replace('Lbh;', 'Ljimm/comm/GUID;')
        bodies = {}
        for mode in paths:
            owner = 'bq' if mode == 'reference' else 'jimm/comm/OtherAction'
            matches = [m for m in data[mode][owner]['methods']
                       if (m['name'], m['desc']) == (('a' if mode == 'reference' else name),
                                                     desc if mode == 'reference' else source_desc)]
            assert len(matches) == 1
            m = matches[0]
            assert m['access'] & 40 == 8 and m['handlers'] == []
            bodies[mode] = {'method': owner + '.' + m['name'] + m['desc'], 'access': m['access'],
                            'instructions': audit.normalized(m['code']) if mode == 'reference' else m['code'],
                            'handlers': m['handlers']}
        left = bodies['reference']
        comparisons = []
        for mode in ['raw', 'optimized']:
            right = bodies[mode]
            exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
            if name == 'setStatus' and mode == 'optimized':
                assert exact and len(left['instructions']) == 209
            comparisons.append({'mode': mode, 'same_whole_body_and_handlers': exact,
                                'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                                'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
        records.append({'role': name, 'bodies': bodies, 'comparisons': comparisons})
    report = {
        'scope': 'Three whole May MIDP2 RU OtherAction methods, native/raw/optimized; typed names only are normalized. '
                 'Every instruction, jump, local, descriptor, handler and access flag is retained. '
                 'Native and optimized setStatus match completely, including the inlined client-ID selection. '
                 'setUserInfo loop rotation and GUID getter inlining, and privacy getter/setter inlining and constant offsets '
                 'remain explicit differences. Execution scope and limitations are in source-other-status.json.',
        'inputs': {mode: {'path': str(path.relative_to(recover.ROOT)).replace('\\', '/'),
                          'sha256': recover.sha(path)} for mode, path in paths.items()},
        'methods': records,
    }
    (recover.ROOT / 'preservation/reports/source-other-status-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS other status bytecode: 3 whole methods; 209 exact optimized status instructions; complete remaining diffs')


if __name__ == '__main__':
    main()
