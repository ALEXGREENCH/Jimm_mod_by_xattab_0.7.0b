#!/usr/bin/env python3
"""Replay complete actual account item callbacks without rewriting application bytes."""
import argparse
import json
import os
import subprocess

import recover
import audit_source as audit
from audit_send_text import classes
from audit_auxiliary_helpers import configuration as native_configuration
from test_softkeys import configuration as softkeys_configuration, entries
from test_source import ROOT, TEST, CACHE, run
from build_source import write_jar


def configuration(data,native,target):
    owners={c['name']:c for c in data};base=native_configuration(data) if native else {'d':'DrawControls/VirtualForm','cf':'jimm/JimmUI'}
    props,_=softkeys_configuration(data,native,target);props.update(form=base['d'],ui=base['cf'])
    candidates=[c for c in data if any(m['name']=='itemStateChanged' and m['desc']=='(Ljavax/microedition/lcdui/Item;)V' for m in c['methods']) and sum(f['desc']=='[Ljavax/microedition/lcdui/TextField;' for f in c['fields'])==2]
    assert len(candidates)==1;c=candidates[0];props['controller']=c['name']
    subject=next(m for m in c['methods'] if m['name']=='itemStateChanged');assert not subject['handlers']
    def read_fields(desc):
        used=[s for s in subject['code'] if s.startswith('180 '+c['name']+'.') and s.endswith(' '+desc)]
        result=[]
        for s in dict.fromkeys(used):
            candidates=[f for f in c['fields'] if f['desc']==desc and s=='180 '+c['name']+'.'+f['name']+' '+desc]
            assert len(candidates)==1,(c['name'],s,candidates)
            result.append(candidates[0])
        return result
    arrays=read_fields('[Ljavax/microedition/lcdui/TextField;');choices=read_fields('Ljavax/microedition/lcdui/ChoiceGroup;')
    assert len(arrays)==1 and len(choices)==2
    panels=[f for f in c['fields'] if f['desc']=='L'+base['d']+';'];assert len(panels)==1
    setter=next(m for m in owners[base['cf']]['methods'] if m['desc']=='(Ljava/lang/Object;)V' and len(m['code'])==3 and m['code'][0]=='25 0' and m['code'][1].startswith('179 ') and m['code'][2]=='177')
    last=next(f for f in owners[base['cf']]['fields'] if setter['code'][1]=='179 '+base['cf']+'.'+f['name']+' '+f['desc']);assert last['desc']=='Ljava/lang/Object;'
    bindings=dict(accounts=arrays[0],labels=choices[0],background=choices[1],panel=panels[0],last=last)
    props.update({k:v['name'] for k,v in bindings.items()})
    constructor=next(m for m in c['methods'] if m['name']=='<init>' and m['desc']=='()V')
    helper_calls=[s for s in subject['code'] if s.startswith('184 '+c['name']+'.')];assert len(helper_calls)==1
    helper=next(m for m in c['methods'] if helper_calls[0]=='184 '+c['name']+'.'+m['name']+m['desc'])
    call=c['name']+'.itemStateChanged(Ljavax/microedition/lcdui/Item;)V'
    callers=[dict(owner=cc['name'],whole_method=m) for cc in data for m in cc['methods'] if any(s.partition(' ')[2]==call for s in m['code'])]
    proof=dict(owner=c['name'],whole_subject=subject,whole_controller_constructor=constructor,whole_last_screen_setter=setter,whole_check_uin_helper=helper,complete_bound_fields=bindings,whole_direct_callers=callers)
    return props,proof


def main(before=False):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True)
    before_path=ROOT/'preservation/reports/source-options-item-state-before.json'
    if before:
        commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip();assert commit.startswith('f8e058e') and not before_path.exists()
        assert not subprocess.check_output(['git','diff','HEAD','--','src'],cwd=ROOT)
    cp=recover.cp([CACHE/n for n in recover.ASM]);runtime=[CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-options-item-state-dump')
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,CACHE/'asm.jar',*runtime]),'-d',TEST,*[ROOT/f'tools/source/{n}.java' for n in ['PopupProbe','SoftKeysProbe','GraphicsFixture','OptionsSettingsFixture','OptionsItemStateProbe']]],'compile-options-item-state')
    builds=[];tag='before' if before else 'current'
    for target,historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        paths=dict(reference=ROOT/f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar',raw=ROOT/f'build/source/{target}-RU/classes.jar',optimized=ROOT/f'dist/source/Jimm-{target}-RU.jar');outputs={};evidence=[]
        for mode,path in paths.items():
            props,proof=configuration(classes(path,cp),mode=='reference',target);actual=path
            if mode=='raw':
                actual=TEST/f'options-item-state-{tag}-{target}-raw-input.jar';values={n:b for n,b in entries(paths['optimized']).items() if not n.endswith('.class')};values.update(entries(path));write_jar(actual,values)
            name=f'options-item-state-{tag}-{target}-{mode}';host=TEST/(name+'-host.jar');out=TEST/(name+'.txt');spec=TEST/(name+'.properties')
            spec.write_text(''.join(k+'='+v.replace('/','.')+'\n' for k,v in props.items()),encoding='utf8',newline='\n')
            run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'OptionsSettingsFixture',actual,host,TEST,props['options'],'false','unused'],name+'-host')
            original,hosted=entries(actual),entries(host);assert set(hosted)-set(original)=={'GraphicsMIDlet.class'} and not set(original)-set(hosted)
            assert all(b==hosted[n] for n,b in original.items() if n.lower()!='meta-inf/manifest.mf')
            result=run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'OptionsItemStateProbe',host,out,spec],name+'-probe')
            outputs[mode]=out.read_bytes();assert len(outputs[mode].splitlines())==300
            proof.update(mode=mode,input_sha256=recover.sha(path),host_sha256=recover.sha(host),output_sha256=recover.sha(out),observations=300,all_application_and_resource_bytes_unchanged=True,result=result)
            evidence.append(proof);print(target,mode,result,flush=True)
        comparisons=[]
        for mode in ['raw','optimized']:
            differences=[dict(position=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs[mode].decode().splitlines())) if a!=b]
            if before:assert differences
            else:assert not differences,(target,mode,differences[:2])
            comparisons.append(dict(mode=mode,differing_observations=len(differences),all_actual_differences=differences))
        builds.append(dict(target=target,evidence=evidence,comparisons=comparisons))
    report=dict(scope='Complete actual OptionsForm.itemStateChanged, real constructors, checkUin, ChoiceGroup and actual JimmUI last-screen setter. Three RU platforms with native/raw/optimized artifacts, 300 observations each. '
                'Null/empty/one/two/three account arrays, null or actual form, actual matching/unrelated/null Item and null account elements expose full label/exception/last-screen state. '
                'Application methods/classes/resources are byte-for-byte unmodified; the headless host changes manifest and adds GraphicsMIDlet only. No subject/API/app-call capture or replaced helper. '
                'Reflection seeds fields explicitly; background chooser/FileTransfer execution and all-platform physical UI are outside this replay.',
                source_file='src/jimm/Options.java',source_sha256=recover.sha(ROOT/'src/jimm/Options.java'),builds=builds)
    if before:report['baseline_commit']=commit
    else:
        old=json.loads(before_path.read_text(encoding='utf8'));report['before_report_sha256']=recover.sha(before_path)
        corrections=[]
        for b,a in zip(old['builds'],builds):
            assert b['target']==a['target']
            left,right=b['evidence'][0],a['evidence'][0]
            assert {k:v for k,v in left.items() if k not in ['host_sha256','complete_bound_fields']}=={k:v for k,v in right.items() if k not in ['host_sha256','complete_bound_fields']}
            old_fields,new_fields=left['complete_bound_fields'],right['complete_bound_fields']
            assert {k:v for k,v in old_fields.items() if k!='accounts'}=={k:v for k,v in new_fields.items() if k!='accounts'}
            assert old_fields['accounts']['name']==new_fields['accounts']['name'] and old_fields['accounts']['desc']=='Z' and new_fields['accounts']['desc']=='[Ljavax/microedition/lcdui/TextField;'
            actual=next(c for c in classes(ROOT/f"preservation/wayback-originals/Jimm_{dict(MIDP2='MIDP2',MOTOROLA='Moto',SIEMENS2='Siemens2')[a['target']]}_RU/Jimm.jar",cp) if c['name']==right['owner'])
            assert old_fields['accounts'] in actual['fields'] and new_fields['accounts'] in actual['fields']
            read='180 '+right['owner']+'.'+new_fields['accounts']['name']+' '+new_fields['accounts']['desc'];assert right['whole_subject']['code'][1]==read
            corrections.append(dict(target=a['target'],owner=right['owner'],input_sha256=right['input_sha256'],frozen_before_declaration=old_fields['accounts'],verified_actual_declaration=new_fields['accounts'],actual_subject_field_read=read))
        report['before_accounts_declaration_erratum']=dict(reason='The original declaration lookup matched the field name without requiring its descriptor and recorded the same-named boolean. '
                'The frozen baseline is preserved unmodified. Actual execution always resolved TextField[].class explicitly; original subject, fixtures and outputs are unchanged. '
                'Current bindings require both actual name and descriptor. Both declarations and the actual array GETFIELD are verified against the same immutable native JAR.',corrections=corrections)
    path=before_path if before else ROOT/'preservation/reports/source-options-item-state-replay.json';path.write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS options item state '+tag+': 2700 unchanged-application observations; '+('genuine negative differences retained' if before else 'all outputs match'))
    return report


if __name__=='__main__':
    p=argparse.ArgumentParser();p.add_argument('--before',action='store_true');main(p.parse_args().before)
