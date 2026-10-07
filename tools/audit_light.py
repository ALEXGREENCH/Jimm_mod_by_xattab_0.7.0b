#!/usr/bin/env python3
"""Verify backlight signatures, modifiers, instructions and handlers after optimization."""
import json
import os
import recover
import audit_source as audit

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-light-bytecode.json'


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    report = {'scope': 'Executable optimized backlight signatures, static/synchronized modifiers and exact normalized instructions/handlers. '
                       'The constructor may be removed by optimization. MIDP2 changeState/On and lightOn are now '
                       'retained through the restored hotkey route and are audited as optimized declarations too.', 'builds': []}
    for target, old, opt in [('MIDP2', 'MIDP2', 'cj'), ('MOTOROLA', 'Moto', 'ci')]:
        audit.CLASSES = {'aj': 'DrawControls/LightControl', opt: 'jimm/Options'}
        audit.SYMBOLS = {'aj.a Z': 'DrawControls/LightControl.lightOn Z', 'aj.a I': 'DrawControls/LightControl.TIMEOUT I',
                         'aj.a Ljava/util/TimerTask;': 'DrawControls/LightControl.lightTask Ljava/util/TimerTask;',
                         opt + '.a(I)I': 'jimm/Options.getInt(I)I', opt + '.a(I)Z': 'jimm/Options.getBoolean(I)Z',
                         'jimm/Jimm.a Ljavax/microedition/lcdui/Display;': 'jimm/Jimm.display Ljavax/microedition/lcdui/Display;',
                         'jimm/Jimm.f Z': 'jimm/Jimm.supportsNokiaLight Z', 'jimm/Jimm.a()Ljava/util/Timer;': 'jimm/Jimm.getTimerRef()Ljava/util/Timer;'}
        methods = [('aj', '<clinit>', '()V', '<clinit>'), ('aj', 'a', '(Z)V', 'flash')]
        if target == 'MIDP2':
            audit.CLASSES['t'] = 'DrawControls/LightControl$1'
            methods += [('aj', 'd', '()V', 'cancelTimeout'), ('aj', 'a', '()V', 'reset'), ('aj', 'c', '()V', 'Off'),
                        ('aj', 'b', '()V', 'changeState'), ('aj', 'e', '()V', 'On'),
                        ('t', '<init>', '()V', '<init>'), ('t', 'run', '()V', 'run')]
            audit.SYMBOLS.update({'aj.e()V': 'DrawControls/LightControl.On()V', 'aj.b()V': 'DrawControls/LightControl.changeState()V'})
        else:
            methods += [('aj', 'a', '()V', 'changeState'), ('aj', 'b', '()V', 'Off'), ('aj', 'c', '()V', 'On')]
        for owner, old_name, signature, new_name in methods:
            audit.SYMBOLS[owner + '.' + old_name + signature] = audit.CLASSES[owner] + '.' + new_name + signature
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old + '_RU') / 'Jimm.jar'
        rebuilt = ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        before, after = dump(reference), dump(rebuilt)
        records = []
        for owner, old_name, signature, new_name in methods:
            a = next(m for m in before[owner]['methods'] if (m['name'], m['desc']) == (old_name, signature))
            b = next(m for m in after[audit.CLASSES[owner]]['methods'] if (m['name'], m['desc']) == (new_name, signature))
            assert (a['access'] & 40) == (b['access'] & 40)
            left, right = audit.normalized(a['code']), audit.normalized(b['code'])
            handlers = audit.normalized_handlers(a['handlers']) == audit.normalized_handlers(b['handlers'])
            records.append({'reference': owner + '.' + old_name + signature, 'source': audit.CLASSES[owner] + '.' + new_name + signature,
                            'signature_verified': True, 'static_and_synchronized_modifiers_verified': True,
                            'reference_instructions': len(left), 'source_instructions': len(right),
                            'same_normalized_instructions': left == right, 'same_normalized_handlers': handlers,
                            'same_normalized_bytecode': left == right and handlers,
                            'reference_normalized_sha256': audit.digest(left), 'source_normalized_sha256': audit.digest(right)})
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference), 'source_class_jar_sha256': recover.sha(rebuilt), 'methods': records})
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    methods = [m for b in report['builds'] for m in b['methods']]
    print('PASS light bytecode: ' + str(len(methods)) + ' signatures, ' + str(sum(m['same_normalized_bytecode'] for m in methods)) + ' identical normalized instructions/handlers')


if __name__ == '__main__':
    main()
