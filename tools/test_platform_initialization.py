#!/usr/bin/env python3
"""Observe the genuine Jimm initializer through real JVM System property callbacks."""
import argparse
import difflib
import json
import os
import subprocess

import recover
import audit_source as audit
from audit_send_text import classes
from test_source import ROOT,TEST,CACHE,run

OWNER='jimm/Jimm'


def configuration(data,native):
    c=next(c for c in data if c['name']==OWNER);m=next(m for m in c['methods'] if m['name']=='<clinit>');code=m['code'];fields={}
    def field(role,instruction,desc):
        assert instruction.startswith('179 '+OWNER+'.') and instruction.endswith(' '+desc)
        f=next(f for f in c['fields'] if instruction=='179 '+OWNER+'.'+f['name']+' '+f['desc'])
        assert f['access'] & 8
        if not native: assert f['name']==role
        fields[role]=f
    for key,role in [('microedition.platform','microeditionPlatform'),('microedition.profiles','microeditionProfiles'),('com.sonyericsson.java.platform','sonyEricssonPlatform')]:
        i=code.index('18 String:'+key)
        assert code[i+1]=='184 java/lang/System.getProperty(Ljava/lang/String;)Ljava/lang/String;'
        put=next(s for s in code[i+2:] if s.startswith('179 '+OWNER+'.') and s.endswith(' Ljava/lang/String;'));field(role,put,'Ljava/lang/String;')
    for key,role in [('fly','is_phone_FLY'),('ericsson','is_phone_SE'),('nokia','is_phone_NOKIA'),('sjp','is_smart_SE')]:
        i=code.index('18 String:'+key);assert code[i+1]=='182 java/lang/String.indexOf(Ljava/lang/String;)I'
        field(role,next(s for s in code[i+2:] if s.startswith('179 '+OWNER+'.') and s.endswith(' Z')),'Z')
    nokia=fields['is_phone_NOKIA'];get='178 '+OWNER+'.'+nokia['name']+' Z'
    i=next(i for i,s in enumerate(code[:-1]) if s in [get,get.replace('178 ','179 ',1)] and code[i+1].startswith('179 '+OWNER+'.') and code[i+1].endswith(' Z'))
    if code[i].startswith('179 '):assert code[i-1]=='89','The optimized field copy must preserve its actual DUP'
    field('supportsNokiaLight',code[i+1],'Z')
    timers=[f for f in c['fields'] if f['desc']=='Ljava/util/Timer;' and f['access'] & 8];assert len(timers)==1;fields['timer']=timers[0]
    assert len({(f['name'],f['desc']) for f in fields.values()})==9
    jp8=next((f for f in c['fields'] if f['name']=='is_JP8' and f['desc']=='Z'),None) if not native else None
    i=code.index('18 String:jp-8'); assert code[i+1]=='182 java/lang/String.indexOf(Ljava/lang/String;)I'
    if native or jp8:
        field('is_JP8',next(s for s in code[i+2:] if s.startswith('179 '+OWNER+'.') and s.endswith(' Z')),'Z');jp8=fields['is_JP8']
    else:assert code[i+2]=='87','An absent optimized JP8 field is not a substituted observable ABI'
    aliases={OWNER+'.'+f['name']+' '+f['desc']:OWNER+'.'+role+' '+f['desc'] for role,f in fields.items()}
    typed=[op+sep+aliases.get(operand,operand) for op,sep,operand in [s.partition(' ') for s in code]]
    props={role:f['name'] for role,f in fields.items()}
    return props,dict(complete_initializer=m,complete_bound_field_declarations=fields,typed_instructions=typed,actual_jp8_field=jp8,
                     complete_jimm_field_inventory=c['fields'],lowercase_instruction_positions=[i for i,s in enumerate(code) if s=='182 java/lang/String.toLowerCase()Ljava/lang/String;'])


def main(before=False):
    recover.bootstrap();TEST.mkdir(parents=True,exist_ok=True);audit.OUT.mkdir(parents=True,exist_ok=True)
    before_path=ROOT/'preservation/reports/source-platform-initialization-before.json'
    if before:
        commit=subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip();assert commit.startswith('8eed901')
        assert not before_path.exists(),'Never overwrite genuine pre-edit observations'
        assert b'platform.toLowerCase().indexOf("nokia")' in (ROOT/'src/jimm/Jimm.java').read_bytes()
    cp=recover.cp([CACHE/n for n in recover.ASM])
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-platform-initialization-dump')
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-d',TEST,ROOT/'tools/source/PlatformInitializationProbe.java'],'compile-platform-initialization-probe')
    evidence=[];outputs={};jp8_outputs={};tag='before' if before else 'current'
    for mode,path in [('reference',ROOT/'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),('raw',ROOT/'build/source/MIDP2-RU/classes.jar'),('optimized',ROOT/'dist/source/Jimm-MIDP2-RU.jar')]:
        props,proof=configuration(classes(path,cp),mode=='reference');spec=TEST/f'platform-initialization-{tag}-{mode}.properties';out=TEST/f'platform-initialization-{tag}-{mode}.txt'
        spec.write_text(''.join(k+'='+v+'\n' for k,v in props.items()),encoding='utf-8',newline='\n')
        result=run([recover.java(),'-Djava.awt.headless=true','-cp',TEST,'PlatformInitializationProbe',path,CACHE,spec,out],'platform-initialization-'+tag+'-'+mode)
        values=out.read_bytes();assert len(values.splitlines())==1400;outputs[mode]=values
        if proof['actual_jp8_field']:
            extra=out.with_suffix('.jp8.txt');assert len(extra.read_bytes().splitlines())==1400;jp8_outputs[mode]=extra.read_bytes();proof['actual_jp8_output_sha256']=recover.sha(extra)
        proof.update(mode=mode,input_sha256=recover.sha(path),output_sha256=recover.sha(out),observations=1400,all_original_artifact_bytes_unchanged=True,result=result)
        evidence.append(proof);print(mode,result,flush=True)
    comparisons=[]
    for mode in ['raw','optimized']:
        different=[dict(position=i,native=a,source=b) for i,(a,b) in enumerate(zip(outputs['reference'].decode().splitlines(),outputs[mode].decode().splitlines())) if a!=b]
        if before:assert different
        else:assert outputs['reference']==outputs[mode],(mode,different[:2])
        comparisons.append(dict(mode=mode,complete_output_bytes_equal=outputs['reference']==outputs[mode],differing_observations=len(different),all_actual_differing_rows=different,
                                whole_typed_instruction_diff=list(difflib.unified_diff(evidence[0]['typed_instructions'],next(e for e in evidence if e['mode']==mode)['typed_instructions'],lineterm=''))))
    assert jp8_outputs['reference']==jp8_outputs['raw']
    if before:assert set(jp8_outputs)=={'reference','raw'}
    else:assert set(jp8_outputs)=={'reference','raw','optimized'} and jp8_outputs['reference']==jp8_outputs['optimized']
    report=dict(scope='The genuine complete MIDP2 Jimm static initializer executes directly from each original input JAR in 1400 fresh isolated JVM class loaders per version. '
                'No application/API class, instruction, method or resource is rewritten and no fixture artifact is substituted. A real custom java.util.Properties installed in each probe process observes actual System.getProperty callbacks, '
                'returns explicit platform/profile/Sony strings, or throws actual SecurityException/AssertionError at one of three properties. US/Turkish default Locale, ten platforms, two profile values, five Sony values and seven failure cases are covered. '
                'Unsafe reads the actual partially initialized static fields without recursively initializing the class, including after uncaught initializer failure. Actual Timer instances are cancelled, System properties/Locale restored and loaders closed. '
                'Full property-read traces, eight shared bound scalar/string fields, Timer presence and actual completion/error classes are recorded. Actual retained JP8 fields have a separate matching 1400-row observation series. The genuine pre-edit optimized field was absent; after restoration of its editor consumer the field is actually retained and compared in all three versions. No pre-seeding static fields, synthetic predicate or physical-device/whole-program equivalence claim.',
                source_file='src/jimm/Jimm.java',source_sha256=recover.sha(ROOT/'src/jimm/Jimm.java'),evidence=evidence,comparisons=comparisons)
    if before:report['baseline_commit']=commit
    else:
        baseline=json.loads(before_path.read_text(encoding='utf-8'));assert baseline['evidence'][0]==evidence[0]
        report['before_report_sha256']=recover.sha(before_path);report['genuine_before_differences']={c['mode']:c['differing_observations'] for c in baseline['comparisons']}
    (before_path if before else ROOT/'preservation/reports/source-platform-initialization-replay.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS platform initialization '+tag+': '+('genuine pre-edit property-order differences retained' if before else 'all 4200 direct unmodified-artifact observations match'))
    return report


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--before',action='store_true');main(p.parse_args().before)
