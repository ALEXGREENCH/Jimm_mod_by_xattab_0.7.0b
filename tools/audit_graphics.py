#!/usr/bin/env python3
"""Audit verified graphics signatures/instructions in all three May platform variants."""
import json
import os
import recover
import audit_source as audit

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-graphics-bytecode.json'
METHODS = {
    'Icon': [('<init>', '(Ljavax/microedition/lcdui/Image;IIII)V', '<init>'),
             ('a', '()Ljavax/microedition/lcdui/Image;', 'getImage'),
             ('a', '()I', 'getWidth'), ('b', '()I', 'getHeight'),
             ('b', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawImage'),
             ('a', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawByLeft'),
             ('c', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawByRight'),
             ('d', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawInCenter')],
    'ImageList': [('<init>', '()V', '<init>'), ('a', '(I)Le;', 'elementAt'),
                  ('a', '()I', 'size'), ('b', '()I', 'getHeight'),
                  ('a', '(Ljava/lang/String;I)V', 'load'), ('a', '(Ljava/lang/String;II)V', 'load'),
                  ('a', '(Ljava/lang/String;)Lf;', 'load')],
    'AniIcon': [('<init>', '(Le;I)V', '<init>'), ('a', '()Ljavax/microedition/lcdui/Image;', 'getImage'),
                ('b', '(Ljavax/microedition/lcdui/Graphics;II)V', 'drawImage')],
    'AniImageList': [('a', '(I)Le;', 'elementAt'), ('a', '()I', 'size'),
                     ('<init>', '()V', '<init>'), ('a', '(Ljava/lang/String;II)V', 'load'), ('run', '()V', 'run')],
    'TPropFont': [('<init>', '(Ljava/lang/String;)V', '<init>'), ('a', '(Ljava/lang/String;)V', 'setImage'),
                  ('a', '(Ljava/lang/String;)I', 'getStringWidth'), ('<clinit>', '()V', '<clinit>')],
}
FIELDS = {
    'Icon': [('a', 'Ljavax/microedition/lcdui/Image;', 'image'), ('a', 'I', 'x'), ('b', 'I', 'y'), ('c', 'I', 'width'), ('d', 'I', 'height')],
    'ImageList': [('a', '[Le;', 'icons'), ('a', 'I', 'width'), ('b', 'I', 'height')],
    'AniIcon': [('a', '[Le;', 'frames'), ('a', '[I', 'delays'), ('a', 'I', 'currentFrame'), ('a', 'Z', 'painted'), ('a', 'J', 'sleepTime')],
    'AniImageList': [('a', 'Ljava/lang/Thread;', 'thread'), ('a', 'J', 'time')],
    'TPropFont': [('a', 'I', 'blue'), ('b', 'I', 'text'), ('a', '[I', 'a'), ('b', '[I', 'b'), ('c', '[I', 'c'), ('a', '[Ljavax/microedition/lcdui/Image;', 'd'), ('c', 'I', 'e'), ('d', 'I', 'f'), ('a', 'Lcg;', 'font')],
}


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    report = {'scope': 'Verified graphics signatures and normalized instructions/handlers in optimized JARs. '
                       'TPropFont.getHeight/drawString are inlined in the rebuilt Motorola JAR and are exercised by GraphicsProbe.', 'builds': []}
    for target, old_target, ai, al, vl, opt, ui in [('MIDP2', 'MIDP2', 'be', 'cm', 'cd', 'cj', 'cf'),
                                                  ('MOTOROLA', 'Moto', 'bd', 'cl', 'cb', 'ci', 'cd'),
                                                  ('SIEMENS2', 'Siemens2', 'bb', 'ck', 'cb', 'ch', 'cd')]:
        aliases = {'Icon': 'e', 'ImageList': 'f', 'AniIcon': ai, 'AniImageList': al}
        if target == 'MOTOROLA':
            aliases['TPropFont'] = 'cg'
        audit.CLASSES = {raw: 'DrawControls/' + name for name, raw in aliases.items()}
        audit.CLASSES.update({vl: 'DrawControls/VirtualList', opt: 'jimm/Options', ui: 'jimm/JimmUI'})
        audit.SYMBOLS = {ui + '.a()Ljava/lang/Object;': 'jimm/JimmUI.getCurrentScreen()Ljava/lang/Object;',
                         vl + '.g()V': 'DrawControls/VirtualList.invalidate()V',
                         opt + '.a(I)I': 'jimm/Options.getInt(I)I'}

        def desc(value):
            for before, after in audit.CLASSES.items():
                value = value.replace('L' + before + ';', 'L' + after + ';')
            return value

        for name, raw in aliases.items():
            for old_name, signature, new_name in METHODS[name]:
                audit.SYMBOLS[raw + '.' + old_name + signature] = 'DrawControls/' + name + '.' + new_name + desc(signature)
            for old_name, signature, new_name in FIELDS[name]:
                audit.SYMBOLS[raw + '.' + old_name + ' ' + signature] = 'DrawControls/' + name + '.' + new_name + ' ' + desc(signature)
        audit.SYMBOLS[al + '.a [L' + ai + ';'] = 'DrawControls/AniImageList.icons [LDrawControls/AniIcon;'
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_RU') / 'Jimm.jar'
        rebuilt = ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        old, new = dump(reference), dump(rebuilt)
        methods = []
        for name, raw in aliases.items():
            for old_name, signature, new_name in METHODS[name]:
                before = next(m for m in old[raw]['methods'] if (m['name'], m['desc']) == (old_name, signature))
                after = next(m for m in new['DrawControls/' + name]['methods'] if m['name'].split('$')[0] == new_name and m['desc'] == desc(signature))
                assert (before['access'] & 40) == (after['access'] & 40), (target, name, signature)
                left, right = audit.normalized(before['code']), audit.normalized(after['code'])
                handlers = audit.normalized_handlers(before['handlers']) == audit.normalized_handlers(after['handlers'])
                methods.append({'reference': raw + '.' + old_name + signature, 'source': 'DrawControls/' + name + '.' + after['name'] + after['desc'],
                                'signature_verified': True, 'static_and_synchronized_modifiers_verified': True,
                                'reference_instructions': len(left), 'source_instructions': len(right),
                                'same_normalized_instructions': left == right, 'same_normalized_handlers': handlers,
                                'same_normalized_bytecode': left == right and handlers,
                                'reference_normalized_sha256': audit.digest(left), 'source_normalized_sha256': audit.digest(right)})
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference), 'source_class_jar_sha256': recover.sha(rebuilt), 'methods': methods})
    REPORT.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    methods = [m for b in report['builds'] for m in b['methods']]
    print('PASS graphics bytecode: ' + str(len(methods)) + ' signatures, ' + str(sum(m['same_normalized_bytecode'] for m in methods)) + ' identical normalized instructions/handlers')


if __name__ == '__main__':
    main()
