#!/usr/bin/env python3
"""Retain whole callbacks and prove the original account-screen and selection reads."""
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_options_item_state import configuration


def inspected(path,native,target):
    cp=recover.cp([recover.CACHE/n for n in recover.ASM]);props,proof=configuration(classes(path,cp),native,target)
    code=proof['whole_subject']['code'];setter=proof['whole_last_screen_setter'];owner=proof['owner'];fields=proof['complete_bound_fields']
    call='184 '+props['ui']+'.'+setter['name']+setter['desc'];panel='180 '+owner+'.'+fields['panel']['name']+' '+fields['panel']['desc']
    assert code.count(call)==1 and code[3:6]==['25 0',panel,call]
    assert code[6:9]==['25 0','180 '+owner+'.'+fields['accounts']['name']+' '+fields['accounts']['desc'],'190']
    assert code[0:2]==['25 0','180 '+owner+'.'+fields['accounts']['name']+' '+fields['accounts']['desc']] and code[2].startswith('198 ')
    skip=int(code[2].split()[1]);assert skip>code.index(call)
    getter='182 javax/microedition/lcdui/ChoiceGroup.getSelectedIndex()I';assert code.count(getter)==1;at=code.index(getter)
    assert code[at-3:at]==['16 69','25 0','180 '+owner+'.'+fields['background']['name']+' '+fields['background']['desc']]
    assert code[at+1].startswith('184 '+props['options']+'.') and code[at+1].endswith('(II)V')
    proof.update(input_sha256=recover.sha(path),actual_screen_setter_position=5,actual_array_length_position=8,actual_account_null_skip_target=skip,
                 actual_selected_index_position=at,actual_selection_and_option_assignment=code[at-3:at+2],setter_precedes_array_length_and_account_label_processing=True)
    return proof


def main():
    recover.bootstrap();audit.OUT.mkdir(parents=True,exist_ok=True);cp=recover.cp([recover.CACHE/n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,recover.ROOT/'tools/recovery/BytecodeDump.java'])
    builds=[]
    for target,historical in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        raw=inspected(recover.ROOT/f'build/source/{target}-RU/classes.jar',False,target)
        for language in ['RU','UA','RO','EN','CZ']:
            evidence=[]
            for mode,path in [('reference',recover.ROOT/f'preservation/wayback-originals/Jimm_{historical}_{language}/Jimm.jar'),('optimized',recover.ROOT/f'dist/source/Jimm-{target}-{language}.jar')]:
                p=inspected(path,mode=='reference',target);p['mode']=mode;evidence.append(p)
            builds.append(dict(target=target,language=language,evidence=evidence,raw_RU_evidence=raw))
    report=dict(scope='All fifteen May/delivered callback pairs and three RU raw bodies. Whole code, handlers, controller constructor, checkUin, actual last-screen setter, bound declarations and direct callers are retained. '
                'When the account array is nonnull, exactly one real form-to-last-screen setter executes before array length and account label processing; the null-array edge skips it. '
                'The background branch reads the real ChoiceGroup selected index into option 69. Compiler local/branch/return layouts and whole original instructions are not removed or claimed identical. '
                'Actual account callback replay and byte-isolated source/JAR delta are separate evidence; background FileTransfer execution is not proven by this ordering audit.',
                source_file='src/jimm/Options.java',source_sha256=recover.sha(recover.ROOT/'src/jimm/Options.java'),builds=builds)
    (recover.ROOT/'preservation/reports/source-options-item-state-bytecode.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf8',newline='\n')
    print('PASS options item-state bytecode: fifteen native/delivered pairs and three raw callbacks restore screen-before-array and actual selected-index reads')
    return report


if __name__=='__main__':main()
