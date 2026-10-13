#!/usr/bin/env python3
"""Retain whole May import/render/file methods and replay the real import controller."""
import argparse
import json
import os
import subprocess
import copy

import recover
import audit_source as audit
from audit_send_text import classes
from test_options_item_state import configuration
from test_source import ROOT, TEST, CACHE, run

SOURCE_FILES=['src/jimm/Options.java','src/jimm/FileBrowser.java','src/DrawControls/VirtualList.java']
INPUT_CLOSE='182 java/io/InputStream.close()V'
OUTPUT_CLOSE='182 java/io/OutputStream.close()V'
ARRAY_CLOSE='182 java/io/ByteArrayInputStream.close()V'
DATA_CLOSE='182 java/io/DataInputStream.close()V'
COLOR='182 javax/microedition/lcdui/Graphics.setColor(I)V'
PRINT='182 java/lang/Throwable.printStackTrace()V'


def background_save(controller,options,vl,props):
    subject=next(m for m in controller['methods'] if m['name']=='commandAction')
    code=subject['code'];choice='180 '+controller['name']+'.'+props['background']+' Ljavax/microedition/lcdui/ChoiceGroup;'
    setters=[i for i,s in enumerate(code) if s=='16 69' and any(t.startswith('184 '+options['name']+'.') and t.endswith('(II)V') for t in code[i+1:i+5])];assert len(setters)==1
    slot=setters[0];ends=[i for i,s in enumerate(code[slot+1:],slot+1) if s=='16 38'];assert ends;end=ends[0]
    region=code[slot-4:end];switches=[i for i in range(slot,end) if code[i].startswith(('171 [0, 1] ','170 0 1 '))]
    valid=False;branches={}
    if len(switches)==1:
        switch=switches[0];tail=code[switch].partition('] ')[2] if code[switch].startswith('171 ') else ' '.join(code[switch].split()[3:]);default,zero,one=map(int,tail.split())
        local=code[slot-1].partition(' ')[2]
        prefix=['25 0',choice,'182 javax/microedition/lcdui/ChoiceGroup.getSelectedIndex()I','54 '+local,'16 69','21 '+local]
        setter=code[slot+2];assert setter.startswith('184 '+options['name']+'.') and setter.endswith('(II)V')
        string_setters=[s for s in code[zero:one] if s.startswith('184 '+options['name']+'.') and s.endswith('(ILjava/lang/String;)V')];assert len(string_setters)==1
        background_calls=[s for s in code[zero:one] if s.startswith('184 '+vl['name']+'.') and s.endswith('(Ljava/io/InputStream;Z)V')];assert len(background_calls)==1
        paths=[s for s in code[one:end] if s.startswith('180 '+controller['name']+'.') and s.endswith(' Ljavax/microedition/lcdui/TextField;')];assert len(paths)==1
        expected_zero=['16 34','18 String:/back.png',string_setters[0],'1','4',background_calls[0],'167 '+str(end)]
        expected_one=['16 34','25 0',paths[0],'182 javax/microedition/lcdui/TextField.getString()Ljava/lang/String;',string_setters[0],'1','3',background_calls[0]]
        valid=default==end and code[slot-4:switch]==prefix+[setter,'21 '+local] and code[zero:one]==expected_zero and code[one:end] in [expected_one,expected_one+['167 '+str(end)]]
        branches=dict(default=default,zero=zero,one=one,end=end,zero_body=code[zero:one],one_body=code[one:end],choice_getfield=choice,path_getfield=paths[0])
    return valid,dict(whole_method=subject,slot_position=slot,region_with_original_offsets=region,verified_branches=branches)


def verified_controller_host(original,hosted):
    result=copy.deepcopy(hosted);code=[];mapping={};captures=[]
    for i,s in enumerate(hosted['code']):
        mapping[i]=len(code)
        if s=='184 LoginIO.time()J':
            assert hosted['code'][i+1]=='183 java/util/Date.<init>(J)V';captures.append(dict(position=i,call=s));continue
        replacements={'183 java/util/Date.<init>(J)V':'183 java/util/Date.<init>()V',
                      '184 OptionsIO.show(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V':'182 javax/microedition/lcdui/Display.setCurrent(Ljavax/microedition/lcdui/Displayable;)V',
                      '184 FileTransferIO.start(Ljava/lang/Thread;)V':'182 java/lang/Thread.start()V'}
        if s in replacements:captures.append(dict(position=i,call=s));s=replacements[s]
        code.append(s)
    mapping[len(hosted['code'])]=len(code)
    for i,s in enumerate(code):
        op=int(s.partition(' ')[0])
        if 153<=op<=168 or op in [198,199]:code[i]=str(op)+' '+str(mapping[int(s.partition(' ')[2])])
        elif op==170:
            tokens=s.split();code[i]=' '.join(tokens[:3]+[str(mapping[int(t)]) for t in tokens[3:]])
        elif op==171:
            prefix,sep,tail=s.partition('] ');assert sep;code[i]=prefix+sep+' '.join(str(mapping[int(t)]) for t in tail.split())
    result['code']=code
    result['refs']=[s for s in code if 178<=int(s.partition(' ')[0])<=185]
    result['handlers']=[' '.join([*[str(mapping[int(t)]) for t in h.split()[:3]],h.split()[3]]) for h in hosted['handlers']]
    assert result==original,{k:([(i,a,b) for i,(a,b) in enumerate(zip(original[k],result[k])) if a!=b][:3] if isinstance(original[k],list) else (original[k],result[k])) for k in original if original[k]!=result[k]}
    return dict(whole_original=original,whole_hosted=hosted,only_enumerated_API_captures_with_all_other_instructions_metadata_and_handlers_equal=True,actual_captures=captures)


def inspected(path,native,target,require_restored):
    cp=recover.cp([CACHE/n for n in recover.ASM]);data=classes(path,cp);owners={c['name']:c for c in data};props,_=configuration(data,native,target)
    controller=owners[props['controller']];options=owners[props['options']];vl=owners[props['vl']]
    subject=next(m for m in controller['methods'] if m['name']=='run' and m['desc']=='()V')
    drawings=[m for m in vl['methods'] if m['desc']=='(Ljavax/microedition/lcdui/Graphics;IIIIII)V' and not m['access'] & 8];assert len(drawings)==1;draw=drawings[0]
    gradients=[m for m in vl['methods'] if m['desc']=='(Ljavax/microedition/lcdui/Graphics;IIIIIII)V' and m['access'] & 8];assert len(gradients)==1;gradient=gradients[0]
    candidates=[(c,m) for c in data for m in c['methods'] if m['desc']=='(Ljava/lang/String;)V' and '182 java/lang/String.lastIndexOf(II)I' in m['code'] and '182 java/lang/String.toUpperCase()Ljava/lang/String;' in m['code'] and '182 java/lang/String.endsWith(Ljava/lang/String;)Z' in m['code']]
    assert len(candidates)==1;browser,caption=candidates[0]
    constructor=next(m for m in options['methods'] if m['name']=='<init>' and m['desc']=='()V')
    loads=[m for m in options['methods'] if m['name']=='load' and m['desc']=='()V'];assert len(loads)<=1
    load=loads[0] if loads else constructor;load_owner=options['name']
    background_valid,background=background_save(controller,options,vl,props)
    bound=[f for f in vl['fields'] if f['desc']=='[I' and any(s=='178 '+vl['name']+'.'+f['name']+' '+f['desc'] for s in gradient['code'])];assert len(bound)==1
    field=bound[0];write='179 '+vl['name']+'.'+field['name']+' '+field['desc'];allocation=gradient['code'].index('188 10')
    null_writes=[i for i,s in enumerate(gradient['code']) if s==write and i>0 and gradient['code'][i-1]=='1']
    ordinary_writes=[i for i,s in enumerate(gradient['code']) if s==write and (i==0 or gradient['code'][i-1]!='1')]
    assert len(ordinary_writes)==1 and ordinary_writes[0]==allocation+1
    option_calls=[[i,s] for i,s in enumerate(subject['code']) if s.startswith('184 '+options['name']+'.') and s.endswith('()V')]
    input_positions=[i for i,s in enumerate(subject['code']) if s==INPUT_CLOSE];output_positions=[i for i,s in enumerate(subject['code']) if s==OUTPUT_CLOSE]
    read_positions=[i for i,s in enumerate(subject['code']) if s.startswith('184 '+options['name']+'.') and s.endswith('(Ljava/io/DataInputStream;)V')]
    assert len(read_positions)==1
    cleanup_positions=[i for i,s in enumerate(load['code']) if s in [ARRAY_CLOSE,DATA_CLOSE]]
    parsers=[i for i,s in enumerate(load['code']) if s.startswith('184 '+options['name']+'.') and s.endswith('(Ljava/io/DataInputStream;)V')]
    assert len(parsers)==1
    close_handlers=[]
    for handler in load['handlers']:
        start,end,dest,kind=handler.split();start,end,dest=map(int,[start,end,dest])
        if len(cleanup_positions)==2 and all(start<=i<end for i in cleanup_positions) and not start<=parsers[0]<end:close_handlers.append(handler)
    state=dict(real_input_and_output_close=bool(len(input_positions)==len(output_positions)==1 and len(option_calls)==1 and read_positions[0]<option_calls[0][0]<input_positions[0]<output_positions[0]),
               no_wrapper_close=DATA_CLOSE not in subject['code'] and '182 java/io/DataOutputStream.close()V' not in subject['code'],
               only_save_void_option_call=len(option_calls)==1,
               stream_cleanup_and_close_only_exception_handler=load['code'].count(ARRAY_CLOSE)==load['code'].count(DATA_CLOSE)==1 and len(close_handlers)==1 and close_handlers[0].endswith('java/lang/Exception'),
               no_direct_item_text_color=COLOR not in draw['code'],
               old_gradient_reference_released_before_allocation=len(null_writes)==1 and null_writes[0]<allocation,
               no_directory_caption_stacktrace=PRINT not in caption['code'],background_save_switch_preserves_picker_path_and_textfield=background_valid)
    if native or require_restored:assert all(state.values()),(path,state)
    bodies=dict(import_options=dict(owner=controller['name'],whole_method=subject),options_constructor=dict(owner=options['name'],whole_method=constructor),
                load_options=dict(owner=load_owner,whole_method=load),save_options=dict(owner=controller['name'],whole_method=background['whole_method']),draw_item=dict(owner=vl['name'],whole_method=draw),gradient=dict(owner=vl['name'],whole_method=gradient),directory_caption=dict(owner=browser['name'],whole_method=caption))
    callers={}
    for key,e in bodies.items():
        m=e['whole_method'];call=e['owner']+'.'+m['name']+m['desc']
        callers[key]=[dict(owner=c['name'],whole_method=mm) for c in data for mm in c['methods'] if any(s.partition(' ')[2]==call for s in mm['code'])]
    helper_calls=[s for s in draw['code'] if s.startswith('184 '+vl['name']+'.') and s.endswith('(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIII)V')];assert len(helper_calls)==1
    string_helper=next(m for m in vl['methods'] if helper_calls[0]=='184 '+vl['name']+'.'+m['name']+m['desc'])
    io_helpers=[]
    for desc in ['(Ljava/io/DataInputStream;)V','(Ljava/io/DataOutputStream;)V']:
        methods=[m for m in options['methods'] if m['desc']==desc];assert len(methods)==1;io_helpers.append(methods[0])
    return dict(input_sha256=recover.sha(path),bodies=bodies,whole_direct_callers=callers,complete_gradient_field=field,
                whole_draw_string_helper=string_helper,whole_option_stream_helpers=io_helpers,observed_restoration_invariants=state,
                actual_import_option_void_calls=option_calls,actual_input_close_positions=input_positions,actual_output_close_positions=output_positions,
                actual_gradient_allocation_position=allocation,actual_gradient_null_write_positions=null_writes,actual_stream_cleanup_exception_regions=close_handlers,background_save_branches=background)


def replay(before):
    cp=recover.cp([CACHE/n for n in recover.ASM]);runtime=[CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,*runtime]),'-d',TEST,ROOT/'tools/source/OptionsImportProbe.java'],'compile-options-import-probe')
    evidence=[];outputs={};tag='before' if before else 'current'
    for mode,path in [('reference',ROOT/'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),('source',ROOT/'build/source/MIDP2-RU/classes.jar')]:
        data=classes(path,cp);props,_=configuration(data,mode=='reference','MIDP2');owner=props['controller'];original=next(m for c in data if c['name']==owner for m in c['methods'] if m['name']=='run' and m['desc']=='()V')
        fixture=TEST/f'options-{mode}.jar';fixture_data=classes(fixture,cp);modified=next(m for c in fixture_data if c['name']==owner for m in c['methods'] if m['name']=='run' and m['desc']=='()V');assert modified==original
        controller=next(c for c in data if c['name']==owner);hosted_controller=next(c for c in fixture_data if c['name']==owner)
        save=verified_controller_host(next(m for m in controller['methods'] if m['name']=='commandAction'),next(m for m in hosted_controller['methods'] if m['name']=='commandAction'))
        # File selection calls a controller instance method, so bind the actual source/native transfer class explicitly.
        transfer_owner='p' if mode=='reference' else 'jimm/FileTransfer';selection_name='a' if mode=='reference' else 'onFileSelect'
        selection=next(m for c in data if c['name']==transfer_owner for m in c['methods'] if m['name']==selection_name and m['desc']=='(Ljava/lang/String;)V')
        hosted_selection=next(m for c in fixture_data if c['name']==transfer_owner for m in c['methods'] if m['name']==selection_name and m['desc']=='(Ljava/lang/String;)V');assert selection==hosted_selection
        out=TEST/f'options-import-{tag}-{mode}.txt';result=run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'OptionsImportProbe',fixture,mode,out],f'options-import-{tag}-{mode}')
        outputs[mode]=out.read_bytes();assert len(outputs[mode].splitlines())==102
        evidence.append(dict(mode=mode,owner=owner,input_sha256=recover.sha(path),fixture_sha256=recover.sha(fixture),output_sha256=recover.sha(out),whole_subject=original,observations=102,result=result,whole_subject_unmodified_in_inherited_fixture=True,save_controller_API_capture_proof=save,whole_unmodified_file_selection=selection))
    differences=[dict(index=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs['source'].decode().splitlines())) if a!=b]
    assert len(differences)==(53 if before else 0),differences[:2]
    return dict(scope='Real whole MIDP2-RU OptionsForm.run from actual native/raw classes, unchanged in the existing Options fixture. '
                '102 observations per version: 84 cover import/export, valid/empty input and a real invalid full-screen option, seven file failures, actual alpha cache fields, loaded options, close/flush counts and persistence notification; 18 cover background save modes, explicit text-field edits and actual optional FileTransfer.onFileSelect. '
                'Save commandAction retains its whole body except enumerated Date clock, Display and Thread API captures, verified with relocated jumps/handlers; file selection itself is unchanged and reaches the real callColorSchemeOptions which rebuilds the misc panel. Image loading is an explicit surrounding capture. '
                'One profile seeds consistent valid option/cache values; a second explicitly seeds distinguishing cache integers including out-of-range values. These are explicit state inputs, not a reachability claim for every physical UI state. '
                'Existing surrounding history/chat/menu/application/device/time/filesystem/persistence captures remain explicit; this is not an unmodified whole application, optimized runtime, all-platform runtime or physical storage test.',
                evidence=evidence,differing_observations=len(differences),all_actual_differences=differences)


def main(before=False):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True);cp=recover.cp([CACHE/n for n in recover.ASM]);before_path=ROOT/'preservation/reports/source-import-cleanup-before.json'
    if before:
        assert not before_path.exists();assert not subprocess.check_output(['git','diff','HEAD','--','src'],cwd=ROOT)
        commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip()
        assert commit.startswith('d6be6e3')
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-import-cleanup-dump')
    builds=[]
    for target,historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        raw=inspected(ROOT/f'build/source/{target}-RU/classes.jar',False,target,not before)
        if before:assert not any(raw['observed_restoration_invariants'].values()),raw['observed_restoration_invariants']
        for language in ['RU','UA','RO','EN','CZ']:
            evidence=[]
            for mode,path in [('reference',ROOT/f'preservation/wayback-originals/Jimm_{historical}_{language}/Jimm.jar'),('optimized',ROOT/f'dist/source/Jimm-{target}-{language}.jar')]:
                p=inspected(path,mode=='reference',target,not before);p['mode']=mode;evidence.append(p)
            builds.append(dict(target=target,language=language,evidence=evidence,raw_RU_evidence=raw))
    runtime=replay(before)
    report=dict(scope='Whole import/save/controller/load/constructor, item rendering, gradient allocation and directory-caption bodies for all fifteen native/delivered pairs and three raw RU artifacts. '
                'Explicit restoration invariants retain all instructions, handlers, original branches/locals, helpers and callers; they are not full instruction/whole-program equality. '
                'Native closes the underlying streams, performs colors/save before input close, has no extra import alpha update, catches exceptions only around loading-stream closes, omits the redundant direct item-text color and directory stacktrace, releases the old pixel array before replacement, and saves background modes with a two-case switch preserving the selected file and text field. '
                'Actual import runtime is limited to the explicitly captured MIDP2 fixture; other cleanup/error/physical allocator runtime is not claimed.',
                source_snapshot={p:recover.sha(ROOT/p) for p in SOURCE_FILES},builds=builds,import_runtime=runtime)
    if before:report['baseline_commit']=commit
    else:
        old=json.loads(before_path.read_text(encoding='utf8'));report['before_report_sha256']=recover.sha(before_path)
        for b,a in zip(old['builds'],builds):assert b['target']==a['target'] and b['language']==a['language'] and b['evidence'][0]==a['evidence'][0]
        assert old['import_runtime']['evidence'][0]['output_sha256']==runtime['evidence'][0]['output_sha256']
    dest=before_path if before else ROOT/'preservation/reports/source-import-cleanup.json';dest.write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS import cleanup '+('before' if before else 'current')+': fifteen platform/locale pairs, three raw configurations, 204 import/export/background observations; '+str(runtime['differing_observations'])+' differing rows')
    return report


if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--before',action='store_true');main(parser.parse_args().before)
