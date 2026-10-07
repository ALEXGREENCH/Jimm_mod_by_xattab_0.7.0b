#!/usr/bin/env python3
"""Retain remaining whole MIDP2 packet/roster/MIDlet helper bodies and all differences."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    inputs = {'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
              'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar'}
    data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in inputs.items()}
    records = []
    for native, name, desc, source in audit.NETWORK_HELPERS:
        translated = desc
        for before, after in audit.CLASSES.items(): translated = translated.replace('L' + before + ';', 'L' + after + ';')
        bodies = {}
        for mode in inputs:
            owner = native if mode == 'reference' else audit.CLASSES[native]
            m = audit.resolve_method(data[mode][owner]['methods'], name if mode == 'reference' else source,
                                     desc if mode == 'reference' else translated)
            bodies[mode] = {'method': owner + '.' + m['name'] + m['desc'], 'access': m['access'],
                            'instructions': audit.normalized(m['code']) if mode == 'reference' else m['code'],
                            'handlers': audit.normalized_handlers(m['handlers']) if mode == 'reference' else m['handlers']}
        assert bodies['reference']['access'] & 40 == bodies['optimized']['access'] & 40
        staticized = bool(bodies['raw']['access'] & 8) != bool(bodies['reference']['access'] & 8)
        if staticized:
            assert bodies['reference']['access'] & 8 and not bodies['raw']['access'] & 8
        assert bodies['raw']['access'] & 32 == bodies['reference']['access'] & 32
        comparisons = []
        for mode in ['raw', 'optimized']:
            left, right = bodies['reference'], bodies[mode]
            comparisons.append({'mode': mode,
                                'same_complete_instructions_and_handlers': left['instructions'] == right['instructions'] and left['handlers'] == right['handlers'],
                                'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                                'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))})
        records.append({'role': source, 'bodies': bodies, 'raw_instance_becomes_static': staticized,
                        'raw_receiver_load_positions': [i for i, s in enumerate(bodies['raw']['instructions']) if s == '25 0'] if staticized else [],
                        'comparisons': comparisons})
    report = {'scope': 'Fourteen complete May MIDP2 RU native/raw/optimized packet-queue, SOCKS-send, roster-packing/event, '
                       'connect/retry/event, MIDlet timer/splash/workscreen and error-description methods. Only typed names are '
                       'normalized; every instruction, local, branch, handler, descriptor and access flag stays explicit. '
                       'Raw instance methods that become static stay documented, including receiver loads passed to raw helpers. '
                       'Differing bodies are not declared equal. '
                       'This inventory makes no additional network/thread/runtime or whole-program equivalence claim; '
                       'the changed resolver has separate three-platform external-capture runtime evidence in source-resolver.json.',
              'inputs': {mode: {'path': str(path.relative_to(recover.ROOT)).replace('\\', '/'), 'sha256': recover.sha(path)} for mode, path in inputs.items()},
              'methods': records}
    (recover.ROOT / 'preservation/reports/source-network-helper-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS network helpers: fourteen complete native/raw/optimized methods; raw instance staticization and full differences retained')


if __name__ == '__main__': main()
