#!/usr/bin/env python3
"""Compare verified method signatures and normalized instructions with May bytecode.

This deliberately audits a named subset, not speculative whole-program symbol matches.
Different instruction sequences do not imply different behavior (see test_source.py).
"""
import hashlib
import json
import os
import re
import recover

ROOT = recover.ROOT
OUT = ROOT / 'build/source-audit'
CLASSES = {'co': 'jimm/comm/Util', 'cj': 'jimm/Options', 'z': 'jimm/ContactItem',
           'ci': 'DrawControls/VirtualAlert', 'cf': 'jimm/JimmUI',
           'ag': 'jimm/MainMenu', 'aq': 'jimm/Templates'}
SYMBOLS = {
    'co.a([BIIZ)Ljava/lang/String;': 'jimm/comm/Util.byteArrayToString([BIIZ)Ljava/lang/String;',
    'co.a(Ljava/lang/String;Z)[B': 'jimm/comm/Util.stringToByteArray(Ljava/lang/String;Z)[B',
    'co.a([B)[B': 'jimm/comm/Util.decipherPassword([B)[B',
    'cj.a(II)V': 'jimm/Options.setInt(II)V',
    'cj.a(IJ)V': 'jimm/Options.setLong(IJ)V',
    'cj.a(IZ)V': 'jimm/Options.setBoolean(IZ)V',
    'cj.a(ILjava/lang/String;)V': 'jimm/Options.setString(ILjava/lang/String;)V',
    'cj.a [Ljava/lang/Object;': 'jimm/Options.options [Ljava/lang/Object;',
    'z.a(I)Z': 'jimm/ContactItem.getBooleanValue(I)Z',
    'z.b(I)Z': 'jimm/ContactItem.hasCapability(I)Z',
    'z.b(I)I': 'jimm/ContactItem.getIntValue(I)I',
    'z.c(I)Z': 'jimm/ContactItem.isMessageAvailable(I)Z',
    'cf.a(Ljavax/microedition/lcdui/Command;I)I': 'jimm/JimmUI.getCommandType(Ljavax/microedition/lcdui/Command;I)I',
    'cf.b(Ljava/lang/Object;)V': 'jimm/JimmUI.selectScreen(Ljava/lang/Object;)V',
    'cf.c Ljavax/microedition/lcdui/Command;': 'jimm/JimmUI.cmdBack Ljavax/microedition/lcdui/Command;',
    'cf.a I': 'jimm/JimmUI.curScreenTag I',
    'ci.a Ljava/lang/Object;': 'DrawControls/VirtualAlert.previousScreen Ljava/lang/Object;',
    'ag.a(ZZ)V': 'jimm/MainMenu.doExit(ZZ)V',
    'aq.a()V': 'jimm/Templates.clearTemplates()V',
    'cf.e()V': 'jimm/JimmUI.menuRemoveContactSelected()V',
    'cf.f()V': 'jimm/JimmUI.menuRemoveMeSelected()V',
}
METHODS = [
    ('co', 'c', '([BII)Ljava/lang/String;', 'detectClientVersion'),
    ('co', 'a', '(I)I', 'translateStatusSend'),
    ('co', 'a', '(ILz;)I', 'translateStatusReceived'),
    ('cj', 'a', '(Ljava/io/DataInputStream;)V', 'readOptions'),
    ('cj', 'a', '(Ljava/io/DataOutputStream;)V', 'writeOptions'),
    ('z', 'a', '(I)I', 'getSortWeight'),
    ('ci', 'commandAction', '(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V', 'commandAction'),
    ('ag', 'a', '(ZZ)V', 'doExit'),
    ('aq', 'a', '()V', 'clearTemplates'),
]


def normalized(code):
    result = []
    for instruction in code:
        for before, after in SYMBOLS.items():
            instruction = instruction.replace(before, after)
        for before, after in CLASSES.items():
            instruction = instruction.replace('L' + before + ';', 'L' + after + ';')
        result.append(instruction)
    return result


def digest(code):
    return hashlib.sha256('\n'.join(code).encode()).hexdigest()


def main():
    recover.bootstrap()
    OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', OUT,
                 ROOT / 'tools/recovery/BytecodeDump.java'])
    def dump(path):
        data = json.loads(recover.run([recover.java(), '-cp', recover.cp([OUT, cp]),
                                      'BytecodeDump', path], capture=True))
        return {c['name']: c for c in data}
    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    rebuilt = ROOT / 'build/source/MIDP2-RU/preverified.jar'
    old, new = dump(reference), dump(rebuilt)
    methods = []
    for owner, name, desc, source_name in METHODS:
        before = next(m for m in old[owner]['methods'] if (m['name'], m['desc']) == (name, desc))
        source_desc = desc
        for short, long in CLASSES.items():
            source_desc = source_desc.replace('L' + short + ';', 'L' + long + ';')
        after = next(m for m in new[CLASSES[owner]]['methods']
                     if m['name'].split('$')[0] == source_name and m['desc'] == source_desc)
        left, right = normalized(before['code']), after['code']
        methods.append({'reference': owner + '.' + name + desc,
                        'source': CLASSES[owner] + '.' + after['name'] + source_desc,
                        'signature_verified': True, 'reference_instructions': len(left),
                        'source_instructions': len(right), 'same_normalized_instructions': left == right,
                        'reference_normalized_sha256': digest(left), 'source_normalized_sha256': digest(right)})
    report = {'reference_sha256': recover.sha(reference), 'rebuilt_sha256': recover.sha(rebuilt),
              'reference_classes': len(old), 'rebuilt_classes': len(new), 'methods': methods,
              'scope': 'Verified subset only. Instruction equality includes local slots and branch layout; '
                       'compiler and optimizer differences remain. Functional checks are recorded separately.'}
    path = ROOT / 'preservation/reports/source-bytecode-comparison.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8')
    print('Audited', len(methods), 'method signatures;', sum(m['same_normalized_instructions'] for m in methods),
          'identical normalized instruction sequences')


if __name__ == '__main__':
    main()
