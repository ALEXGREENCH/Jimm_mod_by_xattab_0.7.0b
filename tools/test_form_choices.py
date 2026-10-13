#!/usr/bin/env python3
"""Replay the actual form-choice loop through real mutable ChoiceGroup models."""
import argparse
import json
import os
import subprocess

import recover
import audit_source as audit
from audit_send_text import classes
from audit_auxiliary_helpers import configuration as native_configuration
from test_softkeys import entries, configuration as softkeys_configuration
from test_source import ROOT, TEST, CACHE, run
from build_source import write_jar

GET='182 javax/microedition/lcdui/ChoiceGroup.getString(I)Ljava/lang/String;'
CAPTURE='184 FormChoicesIO.getString(Ljavax/microedition/lcdui/ChoiceGroup;I)Ljava/lang/String;'
SIZE='182 javax/microedition/lcdui/ChoiceGroup.size()I'


def configuration(data,native,target):
    owner=native_configuration(data)['d'] if native else 'DrawControls/VirtualForm'
    form=next(c for c in data if c['name']==owner)
    subjects=[m for m in form['methods'] if SIZE in m['code'] and GET in m['code'] and m['desc'].endswith(';Ljavax/microedition/lcdui/Item;)V')]
    assert len(subjects)==1;subject=subjects[0];assert subject['access'] & 2 and not subject['handlers']
    if not native:assert subject['name']=='appendChoices'
    node=subject['desc'].split(';')[0][2:];tree=next(c for c in data if c['name']==form['super'])
    nodes=next(c for c in data if c['name']==node)
    choice=next(s[4:] for s in subject['code'] if s.startswith('193 '))
    item=next(s[4:] for s in subject['code'] if s.startswith('187 '));items=next(c for c in data if c['name']==item)
    props,_=softkeys_configuration(data,native,target);props.update(form=owner,node=node,item=item,choice=choice,subject=subject['name'])
    fields={}
    def bind(role,c,desc):
        candidates=[f for f in c['fields'] if f['desc']==desc]
        if role=='root':
            writes={s for m in c['methods'] if m['name']=='<init>' for i,s in enumerate(m['code']) if i>0 and m['code'][i-1]=='183 '+node+'.<init>(Ljava/lang/Object;)V' and s.startswith('181 '+c['name']+'.')}
            assert len(writes)==1;write=next(iter(writes));candidates=[f for f in candidates if write=='181 '+c['name']+'.'+f['name']+' '+f['desc']]
        assert len(candidates)==1,(role,c['name'],candidates)
        fields[role]=dict(owner=c['name'],declaration=candidates[0]);props[role]=candidates[0]['name'];return candidates[0]
    bind('root',tree,'L'+node+';');bind('children',nodes,'Ljava/util/Vector;');bind('data',nodes,'Ljava/lang/Object;');bind('expanded',nodes,'Z');bind('level',nodes,'I')
    bind('text',items,'Ljava/lang/String;');bind('index',items,'I');bind('original',items,'Ljavax/microedition/lcdui/Item;')
    pictures=[f for f in items['fields'] if f['desc'].startswith('L') and f['desc'] not in ['Ljava/lang/String;','Ljavax/microedition/lcdui/Item;']];assert len(pictures)==1
    icon=pictures[0]['desc'][1:-1];bind('image',items,pictures[0]['desc']);props['icon']=icon
    image_lists=[c for c in data if any(m['desc']=='(I)L'+icon+';' for m in c['methods']) and any(f['desc']=='L'+c['name']+';' for f in form['fields'])];assert len(image_lists)==1
    images=image_lists[0];props['images']=images['name'];bind('pictures',form,'L'+images['name']+';')
    getters=[m for m in images['methods'] if m['desc']=='(I)L'+icon+';'];assert len(getters)==1;props['pictureAt']=getters[0]['name']
    call=owner+'.'+subject['name']+subject['desc'];callers=[dict(owner=c['name'],complete_method=m) for c in data for m in c['methods'] if any(s.partition(' ')[2]==call for s in m['code'])]
    assert callers
    invoked=[]
    for instruction in subject['code']:
        if not instruction.startswith(('182 ','183 ')):continue
        operand=instruction.split()[1];called_owner,member=operand.split('.',1)
        if called_owner not in [owner,tree['name'],item]:continue
        name,desc=member.split('(',1);desc='('+desc
        declaration=next(m for c in data if c['name']==called_owner for m in c['methods'] if m['name']==name and m['desc']==desc)
        invoked.append(dict(invocation=instruction,owner=called_owner,whole_declaration=declaration))
    assert len(invoked)==4
    proof=dict(owner=owner,whole_subject=subject,whole_direct_callers=callers,complete_invoked_application_methods=invoked,complete_form_declarations=form['fields'],complete_bound_fields=fields,
               actual_node_owner=node,actual_tree_owner=tree['name'],actual_form_item_owner=item,actual_choice_owner=choice,actual_icon_owner=icon,
               complete_choice_class=next(c for c in data if c['name']==choice),complete_form_constructor=next(m for m in form['methods'] if m['name']=='<init>'),complete_tree_constructors=[m for m in tree['methods'] if m['name']=='<init>'],complete_picture_getter=getters[0])
    return props,proof


def main(before=False):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True)
    before_path=ROOT/'preservation/reports/source-form-choices-before.json'
    if before:
        commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip();assert commit.startswith('c027638')
        assert not before_path.exists(),'Never replace genuine pre-edit evidence'
        assert b'for (int i = 0; i < choices.size(); i++)' in (ROOT/'src/DrawControls/VirtualForm.java').read_bytes()
    cp=recover.cp([CACHE/n for n in recover.ASM]);runtime=[CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-form-choices-dump')
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,CACHE/'asm.jar',*runtime]),'-d',TEST,
         *[ROOT/f'tools/source/{n}.java' for n in ['PopupProbe','SoftKeysProbe','GraphicsFixture','OptionsSettingsFixture','FormChoicesIO','FormChoicesFixture','FormChoicesProbe']]],'compile-form-choices')
    run([os.environ.get('JAVAC','javac'),'-source','7','-target','7','-cp',recover.cp(runtime),'-d',TEST,ROOT/'tools/source/FormChoicesIO.java'],'compile-form-choices-io')
    builds=[];tag='before' if before else 'current'
    for target,historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        paths=dict(reference=ROOT/f'preservation/wayback-originals/Jimm_{historical}_RU/Jimm.jar',raw=ROOT/f'build/source/{target}-RU/classes.jar',optimized=ROOT/f'dist/source/Jimm-{target}-RU.jar')
        evidence=[];outputs={}
        for mode,path in paths.items():
            data=classes(path,cp);props,proof=configuration(data,mode=='reference',target);actual=path
            if mode=='raw':
                actual=TEST/f'form-choices-{tag}-{target}-raw-input.jar';values={n:b for n,b in entries(paths['optimized']).items() if not n.endswith('.class')};values.update(entries(path));write_jar(actual,values)
            name=f'form-choices-{tag}-{target}-{mode}';host=TEST/(name+'-host.jar');fixture=TEST/(name+'.jar');out=TEST/(name+'.txt');spec=TEST/(name+'.properties')
            spec.write_text(''.join(k+'='+v.replace('/','.')+'\n' for k,v in props.items()),encoding='utf8',newline='\n')
            run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'OptionsSettingsFixture',actual,host,TEST,props['options'],'false','unused'],name+'-host')
            original_entries,host_entries=entries(actual),entries(host)
            assert set(host_entries)-set(original_entries)=={'GraphicsMIDlet.class'} and not set(original_entries)-set(host_entries)
            assert all(b==host_entries[n] for n,b in original_entries.items() if n.lower()!='meta-inf/manifest.mf')
            owner=proof['owner'];subject=proof['whole_subject']
            run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'FormChoicesFixture',host,fixture,TEST,owner,subject['name'],subject['desc']],name+'-fixture')
            captured=entries(fixture);assert set(captured)-set(host_entries)=={'FormChoicesIO.class'} and not set(host_entries)-set(captured)
            assert all(b==captured[n] for n,b in host_entries.items() if n!=owner+'.class')
            original=next(c for c in data if c['name']==owner);modified=next(c for c in classes(fixture,cp) if c['name']==owner)
            assert {k:v for k,v in original.items() if k!='methods'}=={k:v for k,v in modified.items() if k!='methods'} and len(original['methods'])==len(modified['methods'])
            for left,right in zip(original['methods'],modified['methods']):
                if (left['name'],left['desc'])!=(subject['name'],subject['desc']):assert left==right;continue
                assert left['code'].count(GET)==1
                expected=[CAPTURE if s==GET else s for s in left['code']]
                assert right['code']==expected and {k:v for k,v in left.items() if k not in ['code','refs']}=={k:v for k,v in right.items() if k not in ['code','refs']}
                assert right['refs']==[s for s in expected if 178<=int(s.split()[0])<=185]
            result=run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'FormChoicesProbe',fixture,out,spec],name+'-probe')
            outputs[mode]=out.read_bytes();assert len(outputs[mode].splitlines())==1086
            normal=[row for row in outputs[mode].decode().splitlines() if row.split(':')[3:5]==['0','9']]
            assert len(normal)==36 and all(row.split(':')[5]=='ok' for row in normal),'Real normal rows must be constructed successfully'
            proof.update(mode=mode,input_sha256=recover.sha(path),fixture_sha256=recover.sha(fixture),output_sha256=recover.sha(out),observations=1086,
                         only_one_subject_API_getter_call_captured=True,all_other_application_and_resource_bytes_unchanged=True,result=result)
            evidence.append(proof);print(target,mode,result,flush=True)
        comparisons=[]
        for mode in ['raw','optimized']:
            different=[dict(position=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs[mode].decode().splitlines())) if a!=b]
            if before:assert different
            else:assert not different,(target,mode,different[:2])
            comparisons.append(dict(mode=mode,differing_observations=len(different),all_actual_differences=different))
        builds.append(dict(target=target,evidence=evidence,comparisons=comparisons))
    report=dict(scope='Whole actual private appendChoices, real form/ChoiceGroup/FormItem/TreeNode/ImageList constructors, actual size(), selection, node addition and expansion execute on three RU platforms in native/raw/optimized artifacts. '
                'Each version has 1086 observations: 0/1/3/5 initial options, exclusive/multiple/popup, null/root/detached parents, ten no-op/grow/shrink/clear/rename/insert/real exception cases at indices 0/1/9, plus six null/nonchoice guards. '
                'Only one ChoiceGroup.getString call in the subject delegates through a provider to the actual API, then mutates the real model or throws at an explicit getter boundary. size() and every loop instruction/branch remain real and unmodified. '
                'Class metadata, all other methods, every other original class/resource entry are preserved; minimal headless host changes manifest and adds GraphicsMIDlet/FormChoicesIO. '
                'Full partial rows, original Item identity, icon identity, node levels/expansion, actual model and getter trace/error classes are recorded. No fake size value, substituted loop, fabricated optimized ABI, concurrency, physical rendering or whole-program equivalence claim.',
                source_file='src/DrawControls/VirtualForm.java',source_sha256=recover.sha(ROOT/'src/DrawControls/VirtualForm.java'),builds=builds)
    if before:report['baseline_commit']=commit
    else:
        old=json.loads(before_path.read_text(encoding='utf8'));report['before_report_sha256']=recover.sha(before_path)
        for b,a in zip(old['builds'],builds):
            assert b['target']==a['target']
            assert {k:v for k,v in b['evidence'][0].items() if k not in ['fixture_sha256','complete_invoked_application_methods']}=={k:v for k,v in a['evidence'][0].items() if k not in ['fixture_sha256','complete_invoked_application_methods']}
        report['genuine_before_differences']={b['target']:{c['mode']:c['differing_observations'] for c in b['comparisons']} for b in old['builds']}
    (before_path if before else ROOT/'preservation/reports/source-form-choices-replay.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS form choices '+tag+': 9774 actual-loop observations; '+('genuine changing-model differences retained' if before else 'all nine versions match'))
    return report


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--before',action='store_true');main(p.parse_args().before)
