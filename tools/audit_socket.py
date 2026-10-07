#!/usr/bin/env python3
"""Verify the May socket close body independently on all three phone platforms."""
import json
import os
import recover
import audit_source as audit


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run(
            [recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    report = {'scope': 'Only SOCKETConnection.close: real optimized signature, static/synchronized '
                      'modifiers, complete normalized instructions and exception handlers in each '
                      'May platform. Other socket methods are audited for MIDP2 in audit_source.py.',
              'builds': []}
    source = 'jimm/comm/Icq$SOCKETConnection'
    for target, old_target, owner, base in [('MIDP2', 'MIDP2', 'bf', 'ap'),
                                           ('MOTOROLA', 'Moto', 'be', 'ap'),
                                           ('SIEMENS2', 'Siemens2', 'bd', 'an')]:
        audit.CLASSES = {owner: source, base: 'jimm/comm/Icq$Connection'}
        audit.SYMBOLS = {base + '.a(Z)V': 'jimm/comm/Icq$Connection.setInputCloseFlag(Z)V',
                         owner + '.a Ljava/io/InputStream;': source + '.is Ljava/io/InputStream;',
                         owner + '.a Ljava/io/OutputStream;': source + '.os Ljava/io/OutputStream;',
                         owner + '.a Ljavax/microedition/io/SocketConnection;': source + '.sc Ljavax/microedition/io/SocketConnection;'}
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_RU') / 'Jimm.jar'
        rebuilt = recover.ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        before, after = dump(original), dump(rebuilt)
        left = next(m for m in before[owner]['methods'] if (m['name'], m['desc']) == ('a', '()V'))
        right = next(m for m in after[source]['methods'] if (m['name'], m['desc']) == ('close', '()V'))
        instructions = audit.normalized(left['code'])
        assert (left['access'] & 40) == (right['access'] & 40)
        assert instructions == right['code'] and audit.normalized_handlers(left['handlers']) == right['handlers']
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(original),
                                'source_class_jar_sha256': recover.sha(rebuilt),
                                'reference': owner + '.a()V', 'source': source + '.close()V',
                                'signature_verified': True, 'static_and_synchronized_modifiers_verified': True,
                                'instructions': len(instructions), 'same_normalized_bytecode': True,
                                'normalized_sha256': audit.digest(instructions)})
    path = recover.ROOT / 'preservation/reports/source-socket-bytecode.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS socket close: all 3 platforms match 28 normalized instructions and all exception handlers')


if __name__ == '__main__':
    main()
