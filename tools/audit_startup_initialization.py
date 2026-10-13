#!/usr/bin/env python3
"""Retain whole platform and canvas initializers, including explicit compiler differences."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from audit_auxiliary_helpers import configuration as native_configuration
from test_platform_initialization import configuration,OWNER


def platform(data,native):
    _,p=configuration(data,native);code=p['complete_initializer']['code'];fields=p['complete_bound_field_declarations']
    c=next(c for c in data if c['name']==OWNER)
    first_read=next(i for i,s in enumerate(code) if s=='178 '+OWNER+'.'+fields['microeditionPlatform']['name']+' Ljava/lang/String;')
    prefix=code[:first_read]
    booleans=[(i,s) for i,s in enumerate(prefix) if s.startswith('179 '+OWNER+'.') and s.endswith(' Z')]
    assert len(booleans)==2
    aliases={};declarations={}
    for role,(i,s),value in zip(['supportsCamCapture','isPasswordProtected'],booleans,['3','4']):
        assert prefix[i-1]==value
        f=next(f for f in c['fields'] if s=='179 '+OWNER+'.'+f['name']+' '+f['desc'])
        if not native:assert f['name']==role
        aliases[OWNER+'.'+f['name']+' Z']=OWNER+'.'+role+' Z';declarations[role]=f
    typed=[op+sep+aliases.get(value,value) for op,sep,value in [s.partition(' ') for s in p['typed_instructions']]]
    sony_null=['1','179 '+OWNER+'.sonyEricssonPlatform Ljava/lang/String;'];assert typed[10:12]==sony_null
    assert len(p['lowercase_instruction_positions'])==2
    assert [s for s in code if s.startswith('18 String:')]==['18 String:microedition.platform','18 String:microedition.profiles','18 String:fly','18 String:ericsson','18 String:nokia','18 String:com.sonyericsson.java.platform','18 String:jp-8','18 String:sjp']
    assert typed[-3:]==['178 jimm/Jimm.is_phone_NOKIA Z','179 jimm/Jimm.supportsNokiaLight Z','177']
    assert len(p['complete_initializer']['handlers'])==1 and p['complete_initializer']['handlers'][0].endswith(' java/lang/Exception')
    p.update(typed_instructions=typed,complete_camera_password_declarations=declarations,additional_typed_aliases=aliases)
    return p


def editor_consumer(data,native):
    owner='cf' if native else 'jimm/JimmUI'
    ui=next(c for c in data if c['name']==owner)
    method=next(m for m in ui['methods'] if m['name']=='commandAction')
    _,flags=configuration(data,native);field=flags['complete_bound_field_declarations']['is_JP8']
    read='178 '+OWNER+'.'+field['name']+' Z';code=method['code'];at=code.index(read)
    assert code.count(read)==1 and code[at+1].startswith('154 ')
    join=int(code[at+1].split()[1]);assert code[at-1]=='153 '+str(join)
    assert code[at+2:at+4]==['1','179 '+owner+'.'+('a' if native else 'messageTextbox')+' Ljavax/microedition/lcdui/TextBox;']
    assert join==at+4
    se=flags['complete_bound_field_declarations']['is_phone_SE'];se_read='178 '+OWNER+'.'+se['name']+' Z'
    if code[at-2]!=se_read:
        j=next(c for c in data if c['name']==OWNER)
        getter=next(m for m in j['methods'] if code[at-2]=='184 '+OWNER+'.'+m['name']+m['desc'])
        assert getter['code']==[se_read,'172']
    return dict(complete_handler=method,actual_jp8_field=field,jp8_read_position=at,shared_skip_null_target=join,complete_source_declarations=ui['fields'])


def canvas(data,native,target):
    owner=native_configuration(data)['cd'] if native else 'DrawControls/VirtualList';c=next(c for c in data if c['name']==owner)
    init=next(m for m in c['methods'] if m['name']=='<clinit>');assign=[m for m in c['methods'] if m['desc']=='()V' and all(s in m['code'] for s in ['17 143','17 185','16 118'])];assert len(assign)==1
    arrays=[f for f in c['fields'] if f['desc']=='[I' and f['access'] & 8];assert len(arrays)==1;buffer=arrays[0]
    put='179 '+owner+'.'+buffer['name']+' [I';at=init['code'].index(put);assert init['code'][at-1]=='1'
    consumers=[m for m in c['methods'] if put in m['code'] and any(s=='182 javax/microedition/lcdui/Graphics.drawRGB([IIIIIIIZ)V' for s in m['code'])]
    assert len(consumers)==1 and '188 10' in consumers[0]['code']
    own=[s for s in init['code'] if s.startswith('184 '+owner+'.') and s.endswith('()V')]
    assert own==['184 '+owner+'.'+assign[0]['name']+'()V'] and not init['handlers']
    helpers=[m for m in c['methods'] if m['name']=='setCaptionOffsets' and m['desc']=='()V']
    if helpers:
        assert not native and len(helpers)==1 and assign[0]['code'][0]=='184 '+owner+'.setCaptionOffsets()V'
    else:
        assert not helpers
        if target=='MIDP2':
            j=next(c for c in data if c['name']==OWNER);jm=next(m for m in j['methods'] if m['name']=='<clinit>');nokia=jm['code'].index('18 String:nokia')
            write=next(s for s in jm['code'][nokia:] if s.startswith('179 '+OWNER+'.') and s.endswith(' Z'))
            first=assign[0]['code'][0]
            if first.startswith('184 '+OWNER+'.'):
                getter=next(m for m in j['methods'] if first=='184 '+OWNER+'.'+m['name']+m['desc'])
                assert getter['code']==[write.replace('179 ','178 ',1),'172']
            else:assert first==write.replace('179 ','178 ',1)
            assert assign[0]['code'][1]=='153 7'
        else:assert assign[0]['code'][:2]==['5','89']
    return dict(owner=owner,complete_initializer=init,complete_assign_softkeys=assign[0],complete_source_caption_helpers=helpers,
                only_direct_same_owner_void_call=own[0],caption_setup_reached_once_through_assign_softkeys=True,
                complete_gradient_buffer_declaration=buffer,explicit_null_write_position=at,whole_unique_gradient_writer=consumers[0])


def main():
    recover.bootstrap();audit.OUT.mkdir(parents=True,exist_ok=True);cp=recover.cp([recover.CACHE/n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,recover.ROOT/'tools/recovery/BytecodeDump.java'])
    raw_data={target:classes(recover.ROOT/f'build/source/{target}-RU/classes.jar',cp) for target in ['MIDP2','MOTOROLA','SIEMENS2']}
    raw=platform(raw_data['MIDP2'],False);raw_editor=editor_consumer(raw_data['MIDP2'],False);builds=[]
    for target,old in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        source_canvas=canvas(raw_data[target],False,target)
        for language in ['RU','UA','RO','EN','CZ']:
            paths=dict(reference=recover.ROOT/f'preservation/wayback-originals/Jimm_{old}_{language}/Jimm.jar',optimized=recover.ROOT/f'dist/source/Jimm-{target}-{language}.jar')
            evidence=[]
            for mode,path in paths.items():
                data=classes(path,cp);item=dict(mode=mode,input_sha256=recover.sha(path),canvas=canvas(data,mode=='reference',target))
                if target=='MIDP2':
                    item['platform']=platform(data,mode=='reference');item['editor_jp8_consumer']=editor_consumer(data,mode=='reference')
                else:
                    c=next(c for c in data if c['name']==OWNER);item['whole_unchanged_platform_initializer']=next(m for m in c['methods'] if m['name']=='<clinit>')
                    assert not any(s=='18 String:com.sonyericsson.java.platform' for s in item['whole_unchanged_platform_initializer']['code'])
                evidence.append(item)
            item=dict(target=target,language=language,evidence=evidence,raw_RU_canvas=source_canvas)
            if target=='MIDP2':
                native=evidence[0]['platform'];modern=evidence[1]['platform']
                assert native['complete_initializer']['handlers']==raw['complete_initializer']['handlers']
                assert len(native['complete_initializer']['code'])==len(raw['complete_initializer']['code'])==77
                item['whole_native_raw_diff']=list(difflib.unified_diff(native['typed_instructions'],raw['typed_instructions'],lineterm=''))
                assert len(modern['complete_initializer']['code'])==77 and modern['actual_jp8_field']
                item['whole_native_optimized_diff']=list(difflib.unified_diff(native['typed_instructions'],modern['typed_instructions'],lineterm=''))
            builds.append(item)
    report=dict(scope='Whole platform and canvas initializer evidence in all 15 May/delivered localization pairs and actual three full RU authoring artifacts. '
                'Every MIDP2 initializer retains explicit Sony null after platform/profile reads and before camera/password assignments, one cached platform lowercase plus one Sony lowercase, '
                'FLY/SE/Nokia detection before Sony property access and the final unconditional Nokia-light copy. The actual Exception-only handler, field declarations, native/raw complete 77-instruction bodies '
                'and full compiler differences are retained. Raw local/DUP forms remain explicit compiler differences; the optimized JP8 field is actually retained by the restored editor consumer. '
                'All canvas initializers call assignSoftKeys exactly once with no preceding redundant caption helper; real source caption helpers/native inlined prefixes remain in the full evidence. '
                'The explicit gradient-array null initializer is bound to the unique static int array and its whole real newarray/drawRGB writer. '
                'The whole native/raw/optimized editor handlers bind the actual SE and JP8 reads to two branches sharing the skip-null target. Other-platform Jimm initializers retain their actual full bodies without Sony MIDP2 probes. Source/JAR isolation and meaningful property callback replay are separate evidence, not a whole-program equivalence claim.',
                source_sha256={p:recover.sha(recover.ROOT/p) for p in ['src/jimm/Jimm.java','src/DrawControls/VirtualList.java','src/jimm/JimmUI.java']},raw_midp2_platform=raw,raw_midp2_editor_jp8_consumer=raw_editor,builds=builds)
    (recover.ROOT/'preservation/reports/source-startup-initialization-bytecode.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS startup initializers: all 15 native/delivered platform boundaries and single canvas setup; five whole May 77-instruction platform bodies with explicit raw/optimized differences')
    return report


if __name__=='__main__':main()
