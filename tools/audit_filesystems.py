#!/usr/bin/env python3
"""Audit verified filesystem signatures/instructions in all three May platform variants."""
import json
import os
import recover
import audit_source as audit

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-filesystems-bytecode.json'
METHODS = {
    'JSR75FileSystem': [('<init>', '()V', '<init>'), ('a', '(Ljava/lang/String;Z)[Ljava/lang/String;', 'getDirectoryContents'),
                       ('a', '(Ljava/lang/String;)V', 'openFile'), ('a', '()Ljava/io/OutputStream;', 'openOutputStream'),
                       ('a', '()Ljava/io/InputStream;', 'openInputStream'), ('a', '()V', 'close'), ('a', '()J', 'fileSize')],
    'MotorolaFileSystem': [('<init>', '()V', '<init>'), ('a', '(Ljava/lang/String;Z)[Ljava/lang/String;', 'getDirectoryContents'),
                          ('a', '(Ljava/lang/String;)V', 'openFile'), ('a', '()Ljava/io/OutputStream;', 'openOutputStream'),
                          ('a', '()Ljava/io/InputStream;', 'openInputStream'), ('a', '()V', 'close'), ('a', '()J', 'fileSize')],
    'FileSystem': [('a', '()Lw;', 'getInstance'), ('a', '(Ljava/lang/String;Z)[Ljava/lang/String;', 'getDirectoryContents')],
}


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    report = {'scope': 'Verified executable filesystem signatures, modifiers, normalized instructions and handlers on all three platforms. '
                       'Optimized-away FileSystem wrappers and totalSize helpers on all platforms are not claimed as matching signatures.', 'builds': []}
    for target, old_target, error in [('MIDP2', 'MIDP2', 'bv'), ('MOTOROLA', 'Moto', 'bu'), ('SIEMENS2', 'Siemens2', 'bt')]:
        aliases = {'JSR75FileSystem': 'b'}
        if target == 'MOTOROLA':
            aliases.update({'MotorolaFileSystem': 'n', 'FileSystem': 'w'})
        audit.CLASSES = {raw: 'jimm/' + name for name, raw in aliases.items()}
        audit.CLASSES.update({'w': 'jimm/FileSystem', error: 'jimm/JimmException'})
        audit.SYMBOLS = {'jimm/Jimm.a Z': 'jimm/Jimm.supports_JSR75 Z'} if target == 'MOTOROLA' else {}
        methods_by_class = {name: list(METHODS[name]) for name in aliases}
        if target != 'MOTOROLA':
            methods_by_class['JSR75FileSystem'].append(('a', '()Ljava/lang/String;', 'getName'))

        def desc(value):
            for before, after in audit.CLASSES.items():
                value = value.replace('L' + before + ';', 'L' + after + ';')
            return value

        for name, raw in aliases.items():
            for old_name, signature, new_name in methods_by_class[name]:
                audit.SYMBOLS[raw + '.' + old_name + signature] = 'jimm/' + name + '.' + new_name + desc(signature)
            if name != 'FileSystem':
                api = 'com/motorola/io/FileConnection' if name == 'MotorolaFileSystem' else 'com/siemens/mp/io/file/FileConnection' if target == 'SIEMENS2' else 'javax/microedition/io/file/FileConnection'
                audit.SYMBOLS[raw + '.a L' + api + ';'] = 'jimm/' + name + '.fileConnection L' + api + ';'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_RU') / 'Jimm.jar'
        rebuilt = ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        old, new = dump(reference), dump(rebuilt)
        methods = []
        for name, raw in aliases.items():
            for old_name, signature, new_name in methods_by_class[name]:
                before = next(m for m in old[raw]['methods'] if (m['name'], m['desc']) == (old_name, signature))
                after = next(m for m in new['jimm/' + name]['methods'] if m['name'].split('$')[0] == new_name and m['desc'] == desc(signature))
                assert (before['access'] & 40) == (after['access'] & 40), (target, name, signature)
                left, right = audit.normalized(before['code']), audit.normalized(after['code'])
                handlers = audit.normalized_handlers(before['handlers']) == audit.normalized_handlers(after['handlers'])
                methods.append({'reference': raw + '.' + old_name + signature, 'source': 'jimm/' + name + '.' + after['name'] + after['desc'],
                                'signature_verified': True, 'static_and_synchronized_modifiers_verified': True,
                                'reference_instructions': len(left), 'source_instructions': len(right),
                                'same_normalized_instructions': left == right, 'same_normalized_handlers': handlers,
                                'same_normalized_bytecode': left == right and handlers,
                                'reference_normalized_sha256': audit.digest(left), 'source_normalized_sha256': audit.digest(right)})
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference), 'source_class_jar_sha256': recover.sha(rebuilt), 'methods': methods})
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    methods = [m for b in report['builds'] for m in b['methods']]
    print('PASS filesystems bytecode: ' + str(len(methods)) + ' signatures, ' + str(sum(m['same_normalized_bytecode'] for m in methods)) + ' identical normalized instructions/handlers')


if __name__ == '__main__':
    main()
