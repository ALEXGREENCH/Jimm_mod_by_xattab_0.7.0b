#!/usr/bin/env python3
"""Audit the retained PhoneBook controller and its SiJaPP platform presence."""
import json
import os
import zipfile
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

    def native_phone(classes):
        found = [c for c in classes.values() if any(
            m['name'] == 'commandAction' and 'tel:' in m['strings'] and 'sms://' in m['strings']
            for m in c['methods'])]
        assert len(found) == 1, 'Ambiguous native phone controller'
        return found[0]

    source = 'jimm/util/PhoneBook'
    report = {'scope': 'Four retained optimized PhoneBook signatures, static/synchronized modifiers, '
              'complete instruction/handler comparisons in MIDP2 and Siemens2 RU. Instruction '
              'differences remain visible; no body equality inferred from signatures. Native '
              'controller presence is also checked in all 15 May and delivered JARs.',
              'builds': [], 'packaging': []}
    for target, old_target in [('MIDP2', 'MIDP2'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_RU') / 'Jimm.jar'
        rebuilt = recover.ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        old, new = dump(original), dump(rebuilt)
        before = native_phone(old)
        owner = before['name']
        assert len(before['methods']) == 4 and len(new[source]['methods']) == 4
        activate = next(m for m in before['methods'] if (m['name'], m['desc']) == ('a', '()V'))
        action = next(m for m in before['methods'] if m['name'] == 'commandAction')
        clinit = next(m for m in before['methods'] if m['name'] == '<clinit>')
        # Discover each alias from actual fields/calls, rather than assume Siemens obfuscation names.
        getter = next(x.split(' ', 1)[1] for x in activate['code'] if x.startswith('184 ') and x.endswith('(Ljava/lang/String;)Ljava/lang/String;'))
        menu = [x.split(' ', 1)[1] for x in action['code'] if x.startswith('184 ') and x.endswith('()V')]
        assert len(menu) == 2 and menu[0] == menu[1]
        commands = [x.split(' ', 1)[1] for x in clinit['code'] if x.startswith('179 ' + owner + '.')]
        assert len(commands) == 4 and all(x.endswith(' Ljavax/microedition/lcdui/Command;') for x in commands)
        input_field = next(x.split(' ', 1)[1] for x in activate['code'] if x.startswith('181 ') and x.endswith(' Ljavax/microedition/lcdui/TextBox;'))
        sms_field = next(x.split(' ', 1)[1] for x in action['code'] if x.startswith('181 ') and x.endswith(' Ljavax/microedition/lcdui/TextBox;'))
        assert input_field != sms_field
        audit.CLASSES = {owner: source, getter.split('.')[0]: 'jimm/util/ResourceBundle', menu[0].split('.')[0]: 'jimm/MainMenu'}
        audit.SYMBOLS = {getter: 'jimm/util/ResourceBundle.getString(Ljava/lang/String;)Ljava/lang/String;',
                         menu[0]: 'jimm/MainMenu.activate()V',
                         input_field: source + '.inputNumber Ljavax/microedition/lcdui/TextBox;',
                         sms_field: source + '.SmsTextBox Ljavax/microedition/lcdui/TextBox;'}
        for raw, named in zip(commands, ['cmdBack', 'cmdSms', 'cmdCall', 'cmdSend']):
            audit.SYMBOLS[raw] = source + '.' + named + ' Ljavax/microedition/lcdui/Command;'
        singleton = next(f for f in before['fields'] if f['desc'] == 'L' + owner + ';')
        audit.SYMBOLS[owner + '.' + singleton['name'] + ' L' + owner + ';'] = source + '.instance L' + source + ';'
        for method in [activate, action]:
            for instruction in method['code']:
                if instruction.startswith('178 jimm/Jimm.'):
                    field = instruction.split(' ', 1)[1]
                    if field.endswith(' Ljavax/microedition/lcdui/Display;'):
                        audit.SYMBOLS[field] = 'jimm/Jimm.display Ljavax/microedition/lcdui/Display;'
                    elif field.endswith(' Ljimm/Jimm;'):
                        audit.SYMBOLS[field] = 'jimm/Jimm.jimm Ljimm/Jimm;'
                    else:
                        raise AssertionError('Unexpected Jimm field in phone controller')
        methods = []
        for name, named in [('<init>', '<init>'), ('<clinit>', '<clinit>'), ('a', 'activate'), ('commandAction', 'commandAction')]:
            left = next(m for m in before['methods'] if m['name'] == name)
            right = next(m for m in new[source]['methods'] if m['name'] == named)
            assert left['desc'] == right['desc'] and (left['access'] & 40) == (right['access'] & 40)
            instructions = audit.normalized(left['code'])
            handlers = audit.normalized_handlers(left['handlers'])
            if name == '<init>':
                assert instructions == right['code'] and handlers == right['handlers']
            if name == 'commandAction':
                assert left['strings'] == right['strings'] == ['tel:', 'SMS ', '', 'sms://', 'text']
                assert [h.split(' ')[-1] for h in handlers] == [h.split(' ')[-1] for h in right['handlers']] == ['java/lang/Exception', 'java/lang/Exception']
            methods.append({'reference': owner + '.' + name + left['desc'], 'source': source + '.' + named + right['desc'],
                            'signature_verified': True, 'static_and_synchronized_modifiers_verified': True,
                            'reference_instructions': len(instructions), 'source_instructions': len(right['code']),
                            'same_normalized_instructions': instructions == right['code'],
                            'same_normalized_handlers': handlers == right['handlers'],
                            'same_normalized_bytecode': instructions == right['code'] and handlers == right['handlers'],
                            'reference_normalized_sha256': audit.digest(instructions), 'source_normalized_sha256': audit.digest(right['code'])})
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(original),
                                'source_class_jar_sha256': recover.sha(rebuilt), 'methods': methods})
    for target, old_target in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        for language in ['RU', 'UA', 'RO', 'EN', 'CZ']:
            original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_' + language) / 'Jimm.jar'
            rebuilt = recover.ROOT / 'dist/source' / ('Jimm-' + target + '-' + language + '.jar')
            with zipfile.ZipFile(original) as jar:
                candidates = [name for name in jar.namelist() if name.endswith('.class') and
                              b'sms://' in jar.read(name) and b'tel:' in jar.read(name)]
            with zipfile.ZipFile(rebuilt) as jar:
                present = source + '.class' in jar.namelist()
            assert len(candidates) == int(target != 'MOTOROLA') and present == (target != 'MOTOROLA')
            report['packaging'].append({'target': target, 'language': language,
                                        'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(rebuilt),
                                        'reference_controller': candidates[0] if candidates else None,
                                        'phone_book_present': present, 'same_platform_presence': True})
    path = recover.ROOT / 'preservation/reports/source-phone-book-bytecode.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS PhoneBook: 8 optimized signatures in MIDP2/Siemens2, 15 platform/language presence checks')


if __name__ == '__main__':
    main()
