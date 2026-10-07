#!/usr/bin/env python3
"""Exercise whole default tables, font updates and alpha/cache paths on genuine May classes."""
import argparse
import json
import os
import zipfile
from pathlib import Path
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def configuration(target, reference, data):
    name = {'MIDP2': 'cj', 'MOTOROLA': 'ci', 'SIEMENS2': 'ch'}[target] if reference else 'jimm/Options'
    owner = next(c for c in data if c['name'] == name)
    spec = {'options': name, 'table': 'a' if reference else 'options', 'display': 'a' if reference else 'display',
            'defaults': 'f' if reference else 'setDefaults', 'alpha': 'a' if reference else 'updateAlpha',
            'style': 'b' if reference else 'updateFontStyle', 'cursor': 'd' if reference else 'cursorAlpha',
            'caption': 'e' if reference else 'captionAlpha', 'softbar': 'f' if reference else 'softbarAlpha',
            'font': 'g' if reference else 'fontStyle', 'gradient': ('j' if target == 'MIDP2' else 'h') if reference else 'gradientHeight',
            'vl': ('cd' if target == 'MIDP2' else 'cb') if reference else 'DrawControls/VirtualList'}
    vl = next(c for c in data if c['name'] == spec['vl'])
    alpha = next(m for m in owner['methods'] if (m['name'], m['desc']) == (spec['alpha'], '()V'))
    assert alpha['access'] == 9 and alpha['handlers'] == []
    assert alpha['code'].count('182 javax/microedition/lcdui/Display.numAlphaLevels()I') == 1
    for key in ['cursor', 'caption', 'softbar']:
        assert '179 ' + name + '.' + spec[key] + ' I' in alpha['code']
    assert any(f['name'] == spec['gradient'] and f['desc'] == 'I' and f['access'] & 8 for f in vl['fields'])
    if reference:
        assert '179 ' + spec['vl'] + '.' + spec['gradient'] + ' I' in alpha['code']
    else:
        write = '179 ' + spec['vl'] + '.gradientHeight I'
        if write not in alpha['code']:
            assert '184 ' + spec['vl'] + '.resetGradient()V' in alpha['code']
            assert next(m for m in vl['methods'] if (m['name'], m['desc']) == ('resetGradient', '()V'))['code'] == ['3', write, '177']
    style = next(m for m in owner['methods'] if (m['name'], m['desc']) == (spec['style'], '()V'))
    assert style['access'] == 9 and any(s.startswith('16 112') for s in style['code'])
    assert '179 ' + name + '.' + spec['font'] + ' I' in style['code']
    defaults = next(m for m in owner['methods'] if (m['name'], m['desc']) == (spec['defaults'], '()V'))
    assert defaults['access'] == 10 and defaults['handlers'] == []
    jimm = next(c for c in data if c['name'] == 'jimm/Jimm')
    flags = []
    for instruction in defaults['code']:
        if instruction.startswith('178 jimm/Jimm.') and instruction.endswith(' Z'):
            field = instruction.split(' ')[1].split('.')[-1]
        elif instruction.startswith('184 jimm/Jimm.') and instruction.endswith('()Z'):
            getter = next(m for m in jimm['methods'] if instruction == '184 jimm/Jimm.' + m['name'] + m['desc'])
            assert len(getter['code']) == 2 and getter['code'][0].startswith('178 jimm/Jimm.') and getter['code'][1] == '172'
            field = getter['code'][0].split(' ')[1].split('.')[-1]
        else: continue
        if field not in flags: flags.append(field)
    assert len(flags) == {'MIDP2':2,'MOTOROLA':0,'SIEMENS2':1}[target]
    spec['devices'] = ','.join(flags)
    return spec, owner, alpha


def execute(target, language, mode, artifact, runtime, cp):
    data = classes(artifact, cp);spec, owner, alpha = configuration(target, mode == 'reference', data)
    tag = 'options-settings-' + target + '-' + language + '-' + mode
    settings = TEST / (tag + '.properties')
    settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
    outputs, proofs = [], []
    for scripted in ([False, True] if language == 'RU' else [False]):
        flavor = 'scripted' if scripted else 'device'
        fixture, output = [TEST / (tag + '-' + flavor + ext) for ext in ['.jar', '.txt']]
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'OptionsSettingsFixture', artifact, fixture,
             TEST, spec['options'], str(scripted).lower(), spec['alpha']], tag + '-' + flavor + '-fixture')
        with zipfile.ZipFile(artifact) as before, zipfile.ZipFile(fixture) as after:
            changes = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf' and before.read(n) != after.read(n)]
            assert changes == ([spec['options'] + '.class'] if scripted else [])
            assert set(after.namelist()) - set(before.namelist()) <= {'GraphicsMIDlet.class', 'OptionsSettingsIO.class'}
        if scripted:
            changed = next(c for c in classes(fixture, cp) if c['name'] == owner['name'])
            assert {k:v for k,v in owner.items() if k != 'methods'} == {k:v for k,v in changed.items() if k != 'methods'}
            assert len(owner['methods']) == len(changed['methods']);captures=0
            for left, right in zip(owner['methods'], changed['methods']):
                assert {k:v for k,v in left.items() if k not in ['code','refs']} == {k:v for k,v in right.items() if k not in ['code','refs']}
                expected = []
                for instruction in left['code']:
                    if left['name'] == spec['alpha'] and instruction == '182 javax/microedition/lcdui/Display.numAlphaLevels()I':
                        instruction = '184 OptionsSettingsIO.alphaLevels(Ljavax/microedition/lcdui/Display;)I';captures += 1
                    expected.append(instruction)
                assert expected == right['code']
                assert right['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
            assert captures == 1
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647', '-cp', recover.cp([TEST,*runtime]),
             'OptionsSettingsProbe', fixture, output, settings, str(scripted).lower()], tag + '-' + flavor)
        result = output.read_bytes();default_calls = 2 << {'MIDP2':2,'MOTOROLA':0,'SIEMENS2':1}[target]
        expected = ('coverage:' + str(default_calls) + ':40:' + ('9216' if scripted else '384')).encode()
        assert result.splitlines()[-1] == expected;outputs.append(result)
        proofs.append({'flavor': flavor, 'input_sha256': recover.sha(artifact), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'all_input_application_bytes_unchanged': not scripted,
                       'only_one_enumerated_display_call_changed': scripted, 'coverage': expected.decode()})
    return outputs, proofs


def main(negative=False, baseline=None):
    assert baseline is None or negative
    runtime = [CACHE / n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'), '-encoding','UTF-8','-cp',recover.cp([TEST,CACHE/'asm.jar',*runtime]),'-d',TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in ['GraphicsFixture','PopupProbe','OptionsSettingsFixture','OptionsSettingsIO','OptionsSettingsProbe']]],'compile-options-settings')
    run([os.environ.get('JAVAC','javac'),'-source','7','-target','7','-cp',recover.cp(runtime),'-d',TEST,ROOT/'tools/source/OptionsSettingsIO.java'],'compile-options-settings-io')
    cp = recover.cp([CACHE/n for n in recover.ASM])
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-options-settings-dump')
    builds=[]
    for target,platform in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        for language in ['RU','UA','RO','EN','CZ']:
            original=ROOT/'preservation/wayback-originals'/('Jimm_'+platform+'_'+language)/'Jimm.jar'
            raw=baseline/(target+'-'+language+'-classes.jar') if baseline else ROOT/'build/source'/(target+'-'+language)/'classes.jar'
            delivered=baseline/(target+'-'+language+'-source.jar') if baseline else ROOT/'dist/source'/('Jimm-'+target+'-'+language+'.jar')
            # EN authoring classes may be the intentionally minimal matrix check: recover full classes from a localized peer.
            if not baseline and language == 'EN': raw=ROOT/'build/source'/(target+'-RU')/'classes.jar'
            host=TEST/('options-settings-'+target+'-'+language+'-authoring.jar')
            with zipfile.ZipFile(delivered) as z:entries={n:z.read(n) for n in z.namelist() if not n.endswith('.class')}
            with zipfile.ZipFile(raw) as z:entries.update({n:z.read(n) for n in z.namelist()})
            write_jar(host,entries);outputs=[];proofs=[]
            modes=[('reference',original),('source',host)]
            if not negative:modes.append(('optimized-source',delivered))
            for mode,path in modes:
                results,proof=execute(target,language,mode,path,runtime,cp);outputs.append(results);proofs.append({'mode':mode,'flavors':proof})
            differences=[]
            for i,flavor in enumerate(['device','scripted'] if language == 'RU' else ['device']):
                a,b=outputs[0][i].splitlines(),outputs[1][i].splitlines();assert len(a)==len(b)
                different=[(l,h) for l,h in zip(a,b) if l!=h]
                if negative:assert different,'Negative evidence must actually differ'
                else:assert all(o[i]==outputs[0][i] for o in outputs),(target,language,flavor,different[:2])
                differences.append({'flavor':flavor,'observations':len(a),'different_observations':len(different),
                                    'default_differences':sum(l.startswith((b'defaults:',b'value:')) for l,h in different),
                                    'font_differences':sum(l.startswith(b'font:') for l,h in different),
                                    'alpha_differences':sum(l.startswith(b'alpha:') for l,h in different)})
            builds.append({'target':target,'language':language,'source_raw_build_language':'RU' if language == 'EN' else language,
                           'reference_sha256':recover.sha(original),'raw_sha256':recover.sha(raw),
                           'delivered_sha256':recover.sha(delivered),'comparisons':differences,'fixture_proofs':proofs})
    report={'scope':'Full 256-slot defaults from null and typed sentinel tables in all 15 localized artifacts, '
            'including all combinations of the genuine Nokia/SE and Siemens SGold static flags read by defaults; '
            '40 genuine font-style updates per flavor, and alpha/cache transitions with absent/real Display, integer '
            'boundaries, malformed option types and gradient sentinels. Device archives preserve every application '
            'byte. Scripted RU archives change only one Display.numAlphaLevels instruction and add a device-input '
            'helper, retaining every declaration, instruction, branch, local slot, exception handler and all other '
            'class/resource bytes. Null invocation, device levels/errors, complete alpha fields, gradient and call '
            'traces are actual observations, not expected application calculations. Minimal host/explicit Options '
            'seed isolate startup. No whole Options UI, physical display or complete program equivalence claim.',
            'negative_before_fix':negative,'builds':builds}
    if baseline:report.update(source_ref=(baseline/'source_ref.txt').read_text().strip(),source_options_sha256=recover.sha(baseline/'Options.java'))
    name='source-options-settings-before.json' if negative else 'source-options-settings.json'
    (ROOT/'preservation/reports'/name).write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS settings defaults/font/alpha:',len(builds),'localized targets;',sum(c['observations'] for b in builds for c in b['comparisons']),'observations;'+(' genuine differences retained' if negative else ' original/raw/optimized match'))
    return report


if __name__ == '__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--negative',action='store_true');p.add_argument('--baseline',type=Path)
    args=p.parse_args();main(args.negative,args.baseline)
