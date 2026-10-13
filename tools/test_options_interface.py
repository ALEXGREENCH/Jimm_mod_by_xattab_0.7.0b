#!/usr/bin/env python3
"""Retain complete May interface controllers and replay construction/save state."""
import argparse
import json
import os
import subprocess
import recover
import audit_source as audit
from audit_send_text import classes
from test_options_item_state import configuration
from test_import_cleanup import verified_controller_host
from test_source import ROOT,TEST,CACHE,run

BASELINE_COMMIT='af0ebf26b43df5c4d1b8f73005063caa7be9ab85'
SOURCE='src/jimm/Options.java'


def inspected(path,native,target,require_restored):
    cp=recover.cp([CACHE/n for n in recover.ASM]);data=classes(path,cp);owners={c['name']:c for c in data};props,_=configuration(data,native,target)
    c=owners[props['controller']];options=owners[props['options']];m=next(m for m in c['methods'] if m['name']=='commandAction');code=m['code']
    def positions(value,desc):
        constant=('16 ' if value<128 else '17 ')+str(value)
        return [i for i,s in enumerate(code) if s==constant and any(t.startswith('184 '+options['name']+'.') and t.endswith(desc) for t in code[i+1:i+7])]
    def one(value,desc):
        found=positions(value,desc);assert len(found)==1,(path,value,desc,found);return found[0]
    reads={str(value):one(value,'(I)I') for value in [118,65,100,116,117]}
    writes={str(value):one(value,'(II)V') for value in [118,65,100,116,117]}
    groups=one(136,'(IZ)V');hidden=one(129,'(IZ)V')
    alpha_methods=[mm for mm in options['methods'] if '182 javax/microedition/lcdui/Display.numAlphaLevels()I' in mm['code']];assert len(alpha_methods)==1
    alpha_call='184 '+options['name']+'.'+alpha_methods[0]['name']+alpha_methods[0]['desc'];alpha_positions=[i for i,s in enumerate(code) if s==alpha_call];assert alpha_positions;alpha=alpha_positions[-1]
    callbacks=[(i,s) for i,s in enumerate(code) if s.startswith('184 ') and not s.startswith('184 '+options['name']+'.') and s.endswith(('(Z)V','(ZZ)V')) and groups<i<groups+40]
    assert len(callbacks)==1;notify,notify_call=callbacks[0]
    field_writers=[]
    for mm in c['methods']:
        for i,s in enumerate(mm['code']):
            if s=='17 129' and i+2<len(mm['code']) and mm['code'][i+1].startswith('184 '+options['name']+'.') and mm['code'][i+1].endswith('(I)Z') and mm['code'][i+2].startswith('181 '+c['name']+'.') and mm['code'][i+2].endswith(' Z'):field_writers.append((mm,mm['code'][i+2].replace('181 ','180 ',1)))
    assert len(field_writers)==1;last=field_writers[0][1]
    cached=False;cache_details={}
    if code[hidden+1].startswith('21 '):
        slot=code[hidden+1].partition(' ')[2];stores=[i for i in range(hidden) if code[i]=='54 '+slot and i>0 and code[i-1]=='182 javax/microedition/lcdui/ChoiceGroup.isSelected(I)Z']
        assert stores;store=stores[-1]
        comparisons=[i for i in range(hidden,notify) if code[i]=='21 '+slot and code[i+1:i+3]==['25 0',last]]
        cached=len(comparisons)==1;cache_details=dict(slot=slot,actual_getter_store=store,actual_comparisons=comparisons,last_field_read=last)
    item_calls=[i for i,s in enumerate(code) if i>reads['118'] and s.startswith('182 '+props['form']+'.') and s.endswith('(Ljavax/microedition/lcdui/Item;)V')];assert item_calls;first_append=item_calls[0]
    light={};constructor_order=True;light_save_order=True
    if target in ['MIDP2','MOTOROLA']:
        timeout=one(74,'(I)I');timeout_write=one(74,'(II)V')
        selected_helpers=[i for i,s in enumerate(code) if s=='17 163' and any(t.startswith('184 '+c['name']+'.') and t.endswith('(Ljavax/microedition/lcdui/ChoiceGroup;Ljava/lang/String;I)V') for t in code[i+1:i+3])];assert len(selected_helpers)==1
        last_chat=selected_helpers[0]+1
        constructor_order=last_chat<timeout<reads['100']<first_append
        between=code[last_chat+1:timeout]
        constructor_order=constructor_order and not any(s.startswith('178 jimm/Jimm.') and s.endswith(' Z') or s.startswith('184 jimm/Jimm.') and s.endswith('()Z') for s in between)
        light=dict(timeout_read=timeout,timeout_write=timeout_write,last_chat_choice_helper=last_chat,whole_construction_interval=between)
        if target=='MIDP2':
            level=one(101,'(I)I');level_write=one(101,'(II)V');constructor_order=constructor_order and timeout<level<reads['100']<first_append;light_save_order=writes['117']<timeout_write<level_write<alpha
            light.update(level_read=level,level_write=level_write)
    state=dict(font_view_constructed_before_sort=reads['118']<reads['65'],font_view_saved_before_sort=writes['118']<writes['65'],
               hide_empty_uses_stored_choice_in_comparison=cached,roster_notification_precedes_alpha_fields=groups<notify<writes['100']<writes['116']<writes['117']<alpha,
               light_controls_constructed_in_may_order_without_device_guard=constructor_order,light_level_saved_after_timeout=light_save_order,only_common_post_save_alpha_update=len(alpha_positions)==1)
    if native or require_restored:assert all(state.values()),(path,state)
    return dict(input_sha256=recover.sha(path),owner=c['name'],whole_subject=m,whole_previous_flags_writer=field_writers[0][0],whole_alpha_helper=alpha_methods[0],
                actual_option_read_positions=reads,actual_option_write_positions=writes,actual_groups_write=groups,actual_roster_notification=[notify,notify_call],actual_alpha_call_positions=alpha_positions,
                actual_first_form_append=first_append,cache_binding=cache_details,light_control_binding=light,restoration_invariants=state)


def replay(before):
    runtime=[CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']];cp=recover.cp([CACHE/n for n in recover.ASM]);tag='before' if before else 'current'
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,*runtime]),'-d',TEST,ROOT/'tools/source/OptionsInterfaceProbe.java'],'compile-options-interface-probe')
    evidence=[];outputs={}
    for mode,path in [('reference',ROOT/'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),('source',ROOT/'build/source/MIDP2-RU/classes.jar')]:
        data=classes(path,cp);props,_=configuration(data,mode=='reference','MIDP2');owner=props['controller'];subject=next(m for c in data if c['name']==owner for m in c['methods'] if m['name']=='commandAction')
        fixture=TEST/f'options-{mode}.jar';hosted=next(m for c in classes(fixture,cp) if c['name']==owner for m in c['methods'] if m['name']=='commandAction');proof=verified_controller_host(subject,hosted)
        out=TEST/f'options-interface-{tag}-{mode}.txt';result=run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'OptionsInterfaceProbe',fixture,mode,out],f'options-interface-{tag}-{mode}')
        outputs[mode]=out.read_bytes();assert len(outputs[mode].splitlines())==145
        evidence.append(dict(mode=mode,input_sha256=recover.sha(path),fixture_sha256=recover.sha(fixture),output_sha256=recover.sha(out),observations=145,result=result,controller_API_capture_proof=proof))
    diff=[dict(index=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs['source'].decode().splitlines())) if a!=b]
    assert len(diff)==(132 if before else 0),diff[:1]
    return dict(scope='Whole actual MIDP2-RU interface select/save commandAction with enumerated Date/Display/Thread API captures and unchanged other instructions/metadata/handlers. '
                '145 observations per version cover five explicit device profiles, seven normal/null-control save states, six explicit valid/invalid/null/wrong-type values for each stored option 118/101/74 at form construction, and twenty stable/changing ChoiceGroup subclass models. '
                'The changing model delegates isSelected to the actual MIDP API and changes real selection 3 after reading selection 4; no application method/callsite is replaced for this mutation. '
                'Observe the complete typed option table, four real caches, presence of ten controls, save count, destination events and final selection. These are explicitly seeded states; reachability through a physical UI is not asserted for all null/corrupt/provider inputs. '
                'The surrounding existing Options fixture captures history/chat/menu/device/filesystem/persistence effects. This is not an all-platform/optimized runtime or physical display/storage test.',evidence=evidence,differing_observations=len(diff),all_actual_differences=diff)


def main(before=False):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True);before_path=ROOT/'preservation/reports/source-options-interface-before.json'
    if before:
        assert not before_path.exists() and not subprocess.check_output(['git','diff','HEAD','--','src'],cwd=ROOT)
        commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip();assert commit==BASELINE_COMMIT
    cp=recover.cp([CACHE/n for n in recover.ASM]);run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-options-interface-dump')
    targets=[];builds=[]
    for target,historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        raw=inspected(ROOT/f'build/source/{target}-RU/classes.jar',False,target,not before)
        if before:assert not all(raw['restoration_invariants'].values())
        targets.append(dict(target=target,raw_RU=raw))
        for language in ['RU','UA','RO','EN','CZ']:
            evidence=[dict(mode=mode,**inspected(path,mode=='reference',target,not before)) for mode,path in [('reference',ROOT/f'preservation/wayback-originals/Jimm_{historical}_{language}/Jimm.jar'),('optimized',ROOT/f'dist/source/Jimm-{target}-{language}.jar')]]
            builds.append(dict(target=target,language=language,evidence=evidence))
    runtime=replay(before);report=dict(scope='Whole interface select/save controller and bound cache/previous-state helpers of all fifteen native/delivered pairs and three raw RU configurations. '
                'Explicit construction, option-write, cached-choice, notification, alpha and light-control ordering invariants retain full instructions, handlers and compiler differences. No full instruction or whole-program equality claim.',source_file=SOURCE,source_sha256=recover.sha(ROOT/SOURCE),targets=targets,builds=builds,runtime=runtime)
    if before:report['baseline_commit']=commit
    else:
        old=json.loads(before_path.read_text(encoding='utf8'));report['before_report_sha256']=recover.sha(before_path)
        for b,a in zip(old['builds'],builds):assert b['target']==a['target'] and b['language']==a['language'] and b['evidence'][0]==a['evidence'][0]
        assert old['runtime']['evidence'][0]['output_sha256']==runtime['evidence'][0]['output_sha256']
    dest=before_path if before else ROOT/'preservation/reports/source-options-interface.json';dest.write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS options interface '+('before' if before else 'current')+': fifteen platform/locale pairs, three raw configurations, 290 construction/save/model observations; '+str(runtime['differing_observations'])+' differing rows')
    return report


if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--before',action='store_true');main(parser.parse_args().before)
