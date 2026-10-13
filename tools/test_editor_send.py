#!/usr/bin/env python3
"""Compare the complete editor send handler, including the genuine JP8 exception."""
import argparse
import json
import os
from pathlib import Path

import recover
import audit_source as audit
from audit_send_text import classes
from test_source import ROOT, TEST, CACHE, run
from test_softkeys import entries, configuration as softkeys_configuration
from test_platform_initialization import configuration as platform_configuration
from build_source import write_jar


def configuration(data, native):
    owner='cf' if native else 'jimm/JimmUI'
    ui=next(c for c in data if c['name']==owner)
    method=next(m for m in ui['methods'] if m['name']=='commandAction')
    props,_=softkeys_configuration(data,native,'MIDP2')
    flags,flag_proof=platform_configuration(data,native)
    props.update(ui=owner,se=flags['is_phone_SE'])
    if 'is_JP8' in flags:props['jp8']=flags['is_JP8']
    for role,short,long,desc in [('box','a','messageTextbox','Ljavax/microedition/lcdui/TextBox;'),('mode','f','textMessCurMode','I'),
                                ('current','d','current','I'),('pages','a','strings','Ljava/util/Vector;'),('send','k','cmdSend','Ljavax/microedition/lcdui/Command;')]:
        name=short if native else long
        field=next(f for f in ui['fields'] if f['name']==name and f['desc']==desc);props[role]=field['name']
    contact='Lz;' if native else 'Ljimm/ContactItem;'
    mapping={f'184 {owner}.{"a" if native else "sendMessage"}(Ljava/lang/String;{contact})V':'184 EditorSendIO.send(Ljava/lang/String;Ljava/lang/Object;)V',
             f'184 {owner}.{"a" if native else "backToLastScreen"}()V':'184 EditorSendIO.back()V',
             f'184 {"bt.a" if native else "jimm/ChatHistory.activateIfExists"}({contact})Z':'184 EditorSendIO.chat(Ljava/lang/Object;)Z',
             f'183 {owner}.chatExists({contact})Z':'184 EditorSendIO.chatWithReceiver(Ljava/lang/Object;Ljava/lang/Object;)Z'}
    assert all(any(s in method['code'] for s,d in mapping.items() if d==value) for value in [mapping[next(iter(mapping))],'184 EditorSendIO.back()V'])
    return props,dict(whole_original_handler=method,actual_platform_fields=flag_proof['complete_bound_field_declarations'],
                      actual_jp8_field_present='jp8' in props,whole_controller=ui),mapping


def main(baseline=None):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True)
    before=ROOT/'preservation/reports/source-editor-send-before.json'
    if baseline:assert not before.exists(),'Never overwrite genuine pre-edit observations'
    base=Path(baseline) if baseline else ROOT
    cp=recover.cp([CACHE/n for n in recover.ASM]);runtime=[CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-editor-send-dump')
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,CACHE/'asm.jar',*runtime]),'-d',TEST,
         *[ROOT/f'tools/source/{n}.java' for n in ['PopupProbe','SoftKeysProbe','GraphicsFixture','OptionsSettingsFixture','EditorSendIO','EditorSendFixture','EditorSendProbe']]],'compile-editor-send')
    run([os.environ.get('JAVAC','javac'),'-source','7','-target','7','-d',TEST,ROOT/'tools/source/EditorSendIO.java'],'compile-editor-send-io')
    paths=dict(reference=ROOT/'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',raw=base/'build/source/MIDP2-RU/classes.jar',optimized=base/'dist/source/Jimm-MIDP2-RU.jar')
    evidence=[];outputs={};tag='before' if baseline else 'current'
    for mode,path in paths.items():
        data=classes(path,cp);props,proof,mapping=configuration(data,mode=='reference');actual=path
        if mode=='raw':
            actual=TEST/f'editor-send-{tag}-raw-input.jar';values={n:b for n,b in entries(paths['optimized']).items() if not n.endswith('.class')};values.update(entries(path));write_jar(actual,values)
        name=f'editor-send-{tag}-{mode}';host=TEST/(name+'-host.jar');fixture=TEST/(name+'.jar');out=TEST/(name+'.txt');spec=TEST/(name+'.properties')
        spec.write_text(''.join(k+'='+v.replace('/','.')+'\n' for k,v in props.items()),encoding='utf8',newline='\n')
        run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'OptionsSettingsFixture',actual,host,TEST,props['options'],'false','unused'],name+'-host')
        original_entries,host_entries=entries(actual),entries(host)
        assert set(host_entries)-set(original_entries)=={'GraphicsMIDlet.class'} and not set(original_entries)-set(host_entries)
        assert all(b==host_entries[n] for n,b in original_entries.items() if n.lower()!='meta-inf/manifest.mf')
        run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'EditorSendFixture',host,fixture,'reference' if mode=='reference' else 'source',TEST],name+'-fixture')
        captured=entries(fixture);owner=props['ui'];assert set(captured)-set(host_entries)=={'EditorSendIO.class'}
        assert all(b==captured[n] for n,b in host_entries.items() if n!=owner+'.class')
        modified=next(c for c in classes(fixture,cp) if c['name']==owner)
        original=proof['whole_controller'];assert {k:v for k,v in original.items() if k!='methods'}=={k:v for k,v in modified.items() if k!='methods'}
        assert len(original['methods'])==len(modified['methods']);captures={}
        for left,right in zip(original['methods'],modified['methods']):
            if left['name']!='commandAction':assert left==right;continue
            expected=[mapping.get(s,s) for s in left['code']]
            assert right['code']==expected and {k:v for k,v in left.items() if k not in ['code','refs']}=={k:v for k,v in right.items() if k not in ['code','refs']}
            assert right['refs']==[s for s in expected if 178<=int(s.split()[0])<=185]
            captures={s:left['code'].count(s) for s in mapping if s in left['code']}
        result=run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'EditorSendProbe',fixture,out,spec],name+'-probe')
        outputs[mode]=out.read_bytes();assert len(outputs[mode].splitlines())==336
        normal_sends=[row for row in outputs[mode].decode().splitlines() if ':200001:' in row and ':0:ok:' in row and 'send=' in row]
        assert len(normal_sends)==16 and all('chat=true;' in row for row in normal_sends),'Successful routes must reach the actual cache condition and chat destination'
        proof.update(mode=mode,input_sha256=recover.sha(path),fixture_sha256=recover.sha(fixture),output_sha256=recover.sha(out),observations=336,
                     captured_calls=captures,only_handler_enumerated_invocations_changed=True,all_other_application_and_resource_bytes_unchanged=True,result=result)
        evidence.append(proof);print(mode,result,flush=True)
    comparisons=[]
    for mode in ['raw','optimized']:
        differences=[dict(position=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs[mode].decode().splitlines())) if a!=b]
        if baseline:assert differences
        else:assert not differences,(mode,differences[:2])
        comparisons.append(dict(mode=mode,differing_observations=len(differences),all_actual_differences=differences))
    report=dict(scope='Complete actual MIDP2 commandAction executes with real TextBox API with a null-to-empty compatibility subclass (MicroEmulator 2.0.4 incorrectly rejects null), page Vector, getString and setString/optimized specialized clearing bodies. '
                'Four SE/JP8 combinations, three text values, message/other mode, both chat-existence outcomes, successful destinations and real IllegalStateException/AssertionError at send/chat/back: 336 calls/version. '
                'Only enumerated sendMessage/chatExists or inlined ChatHistory/backToLastScreen call destinations in commandAction are captured; complete class metadata and all other methods/instructions, every other original application/resource entry remain exact. '
                'Headless host changes manifest and adds GraphicsMIDlet/EditorSendIO. Actual flags are bound to genuine initializer writes. The genuine old optimized JP8 ABI is absent; no field is invented. '
                'Recipient is null at captured boundaries, typing option 146 enabled; no network, chat activation, back navigation, other commands or physical-device equivalence claim.',
                source_file='src/jimm/JimmUI.java',source_sha256=recover.sha(ROOT/'src/jimm/JimmUI.java'),evidence=evidence,comparisons=comparisons)
    if baseline:report['baseline_commit']=json.loads((base/'manifest.json').read_text())['commit']
    else:
        old=json.loads(before.read_text());assert old['evidence'][0]['whole_controller']==evidence[0]['whole_controller'] and old['evidence'][0]['output_sha256']==evidence[0]['output_sha256']
        report['before_report_sha256']=recover.sha(before);report['genuine_before_differences']={c['mode']:c['differing_observations'] for c in old['comparisons']}
    (before if baseline else ROOT/'preservation/reports/source-editor-send-replay.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS editor send '+tag+': 1008 actual-handler observations; '+('genuine JP8 differences retained' if baseline else 'all versions match'))
    return report


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--baseline',type=Path);main(p.parse_args().baseline)
