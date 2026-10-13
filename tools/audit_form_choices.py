#!/usr/bin/env python3
"""Retain whole form-choice loops and prove that the original bound is cached."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_form_choices import configuration, SIZE


def cached_bound(proof):
    method=proof['whole_subject'];code=method['code'];at=code.index(SIZE)
    assert code.count(SIZE)==1 and code[at+1].startswith('54 ')
    slot=code[at+1].split()[1]
    backedges=[(i,int(s.split()[1])) for i,s in enumerate(code) if 153<=int(s.split()[0])<=167 and len(s.split())==2 and int(s.split()[1])<i]
    assert len(backedges)==1;end,start=backedges[0]
    assert at<start and SIZE not in code[start:end+1]
    assert '21 '+slot in code[start:end+1] and '54 '+slot not in code[start:end+1]
    assert not any(s.startswith('132 '+slot+' ') for s in code[start:end+1])
    return dict(actual_size_call_position=at,actual_cached_integer_slot=int(slot),whole_loop_range=[start,end],backedge_instruction=code[end],
                actual_cached_bound_reads=[i for i,s in enumerate(code) if s=='21 '+slot],size_read_once_before_loop=True)


def typed(proof):
    aliases={proof['owner']:'DrawControls/VirtualForm',proof['actual_node_owner']:'DrawControls/TreeNode',proof['actual_tree_owner']:'DrawControls/VirtualTree',
             proof['actual_choice_owner']:'DrawControls/FormChoiceGroup',proof['actual_icon_owner']:'DrawControls/Icon',proof['actual_form_item_owner']:'DrawControls/FormItem'}
    members={proof['owner']+'.a(L'+proof['actual_choice_owner']+';I)L'+proof['actual_icon_owner']+';':'DrawControls/VirtualForm.choiceImage(LDrawControls/FormChoiceGroup;I)LDrawControls/Icon;',
             proof['actual_tree_owner']+'.a(L'+proof['actual_node_owner']+';Ljava/lang/Object;)L'+proof['actual_node_owner']+';':'DrawControls/VirtualTree.addNode(LDrawControls/TreeNode;Ljava/lang/Object;)LDrawControls/TreeNode;',
             proof['actual_tree_owner']+'.a(L'+proof['actual_node_owner']+';Z)V':'DrawControls/VirtualTree.setExpandFlag(LDrawControls/TreeNode;Z)V'}
    rows=[]
    for s in proof['whole_subject']['code']:
        op,sep,value=s.partition(' ')
        if value in members:value=members[value]
        elif op in ['187','192','193']:value=aliases.get(value,value)
        elif op=='183' and '.<init>(' in value:
            owner,member=value.split('.',1);value=aliases.get(owner,owner)+'.'+member
        for native,source in aliases.items():value=value.replace('L'+native+';','L'+source+';')
        rows.append(op+sep+value)
    return rows


def main():
    recover.bootstrap();audit.OUT.mkdir(parents=True,exist_ok=True);cp=recover.cp([recover.CACHE/n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,recover.ROOT/'tools/recovery/BytecodeDump.java'])
    raw={}
    for target in ['MIDP2','MOTOROLA','SIEMENS2']:
        path=recover.ROOT/f'build/source/{target}-RU/classes.jar';_,p=configuration(classes(path,cp),False,target)
        p.update(input_sha256=recover.sha(path),cached_bound=cached_bound(p),typed_instructions=typed(p));raw[target]=p
    builds=[]
    for target,old in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        for language in ['RU','UA','RO','EN','CZ']:
            evidence=[]
            for mode,path in [('reference',recover.ROOT/f'preservation/wayback-originals/Jimm_{old}_{language}/Jimm.jar'),('optimized',recover.ROOT/f'dist/source/Jimm-{target}-{language}.jar')]:
                data=classes(path,cp);_,p=configuration(data,mode=='reference',target)
                p.update(mode=mode,input_sha256=recover.sha(path),cached_bound=cached_bound(p),typed_instructions=typed(p));evidence.append(p)
            builds.append(dict(target=target,language=language,evidence=evidence,raw_RU_evidence=raw[target],
                               whole_native_raw_diff=list(difflib.unified_diff(evidence[0]['typed_instructions'],raw[target]['typed_instructions'],lineterm='')),
                               whole_native_optimized_diff=list(difflib.unified_diff(evidence[0]['typed_instructions'],evidence[1]['typed_instructions'],lineterm=''))))
    report=dict(scope='Complete appendChoices in all fifteen May/delivered localization pairs and three actual RU authoring artifacts. '
                'Each unique real ChoiceGroup.size invocation is immediately stored in an integer local before the unique backward loop edge; the loop reads that slot without rewriting it or calling size. '
                'Whole original bodies/handlers/direct callers, form/choice declarations, real node/item/image bindings and complete constructors remain explicit. '
                'Typed aliases only name actual form/tree/node/item/choice/icon references; original branch indices, local/DUP forms, early returns and full compiler differences remain. '
                'No deletion of differing instructions or whole unchanged-body equality is claimed. The changing-real-model replay and source/JAR isolation are separate evidence.',
                source_file='src/DrawControls/VirtualForm.java',source_sha256=recover.sha(recover.ROOT/'src/DrawControls/VirtualForm.java'),builds=builds)
    (recover.ROOT/'preservation/reports/source-form-choices-bytecode.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS form-choice bytecode: all fifteen native/delivered pairs and three raw loops cache the actual bound before iteration')
    return report


if __name__=='__main__':main()
