#!/usr/bin/env python3
"""Verify the complete settings-entry method and typed cache on native/raw/optimized targets."""
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from test_options_cache import configuration


def main():
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        raw = recover.ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        delivered = recover.ROOT / 'dist/source' / ('Jimm-' + target + '-RU.jar')
        proofs = []
        for mode, path in [('reference', original), ('authoring', raw), ('optimized', delivered)]:
            spec, entry, field, activate = configuration(target, mode == 'reference', classes(path, cp))
            operand = spec['options'] + '.' + spec['cache'] + ' L' + spec['form'] + ';'
            expected = ['178 ' + operand, '199 6', '187 ' + spec['form'], '89',
                        '183 ' + spec['form'] + '.<init>()V', '179 ' + operand,
                        '178 ' + operand, '182 ' + spec['form'] + '.' + activate['name'] + '()V', '177']
            assert entry['code'] == expected, (target, mode, entry['code'])
            assert entry['access'] == 9 and entry['handlers'] == []
            assert field['access'] == 9 and field['desc'] == 'L' + spec['form'] + ';'
            proofs.append({'mode': mode, 'jar_sha256': recover.sha(path), 'owner': spec['options'],
                           'entry': entry['name'] + entry['desc'], 'entry_access': entry['access'],
                           'cache_field': field, 'complete_instructions': entry['code'], 'handlers': entry['handlers'],
                           'complete_typed_entry_matches_original': True})
        builds.append({'target': target, 'proofs': proofs})
    report = {'scope': 'Entire nine-instruction public static settings entry and public static controller cache, '
              'including the precise non-null branch target, genuine construction, single conditional store and '
              'unconditional activation. Three-platform native, unoptimized authoring and delivered optimized '
              'artifacts are read directly. Typed names are established by construction/cache/activation and '
              'actual activation state stores; no instruction deletion or compiler normalization. '
              'The entire OptionsForm controller is not claimed identical.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-options-cache-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS settings entry bytecode: complete 9 instructions and cache declarations on all 3 native/raw/optimized targets')


if __name__ == '__main__':
    main()
