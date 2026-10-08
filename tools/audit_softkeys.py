#!/usr/bin/env python3
"""Bind full soft-key assignment bodies and retain every explicit compiler expansion."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from test_softkeys import configuration, OWNER


def expanded(proof, native):
    owner = proof['owner']; code = proof['complete_subject']['code']
    aliases = {owner + '.' + f['name'] + ' ' + f['desc']: OWNER + '.' + role + ' ' + f['desc']
               for role,f in proof['complete_fields'].items()}
    for m in proof['complete_getters']:
        actual = next(s for s in code if s.startswith('184 ') and s.endswith(m['name'] + m['desc']))[4:]
        role = 'getBoolean' if m['desc'] == '(I)Z' else 'getInt'
        aliases[actual] = 'jimm/Options.' + role + m['desc']
    flag_getters = {g['name'] + g['desc']: g['code'][0] for g in proof['complete_raw_device_getters']}
    for role,f in zip(['is_phone_NOKIA', 'is_phone_SE'], proof['complete_device_fields']):
        aliases['jimm/Jimm.' + f['name'] + ' ' + f['desc']] = 'jimm/Jimm.' + role + ' ' + f['desc']
    def typed(s):
        if s.startswith('184 jimm/Jimm.'):
            s = flag_getters[s.partition('jimm/Jimm.')[2]]
        op,sep,operand = s.partition(' '); return op + sep + aliases.get(operand,operand)
    removed = set()
    for arm in proof['complete_menu_arms']:
        if arm['source_only_unused_right_menu_write']: removed.update([arm['start'] + 6, arm['start'] + 7])
    records = []; labels = {}; ordinal = 0
    # Both helpers are static, have no locals/handlers and end in return. Their real body and call are retained in proof.
    for i,s in enumerate(code):
        labels[('subject',i)] = ordinal
        if i in removed: continue
        if not native and i == 0:
            for j,t in enumerate(proof['whole_caption_offset_prefix']):
                labels[('caption',j)] = ordinal; records.append(('caption',t)); ordinal += 1
        else: records.append(('subject',s)); ordinal += 1
    result = []
    for region,s in records:
        op,sep,operand = typed(s).partition(' ')
        if op in ['153','167']:
            operand = str(labels[(region,int(operand))])
        result.append(op + sep + operand)
    assert len(removed) in [0,4]
    return result, dict(expanded_real_static_caption_helper=not native,
                       expanded_actual_two_instruction_device_getters=len(flag_getters),
                       removed_only_raw_dead_right_menu_instruction_positions=sorted(removed), actual_typed_aliases=aliases)


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True,exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,recover.ROOT/'tools/recovery/BytecodeDump.java'])
    builds = []
    for target,old in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        evidence = []; raw = None
        for mode,path in [('reference',recover.ROOT/f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'),
                          ('raw',recover.ROOT/f'build/source/{target}-RU/classes.jar'),
                          ('optimized',recover.ROOT/f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path,cp); props,proof = configuration(data,mode=='reference',target)
            fields = next(c for c in data if c['name'] == proof['owner'])['fields']
            proof['actual_complete_virtual_list_field_inventory'] = fields
            if mode == 'optimized': assert not any(f['name'] == 'MENU_RIGHT' for f in fields)
            if mode == 'raw':
                declarations = []
                for name,value in [('MENU_TYPE_LEFT_BAR',1),('MENU_TYPE_RIGHT_BAR',2),('MENU_TYPE_LEFT',3),('MENU_TYPE_RIGHT',4)]:
                    f = next(f for f in fields if f['name'] == name and f['desc'] == 'I')
                    assert f['value'] == str(value) and f['access'] & 24 == 24; declarations.append(f)
                option_fields = next(c for c in data if c['name'] == props['options'])['fields']
                for name,value in [('OPTION_SWAP_SOFT_KEY',143),('OPTION_XSTATUS_RIGHT',185),('OPTION_FONT_VIEW',118)]:
                    f = next(f for f in option_fields if f['name'] == name and f['desc'] == 'I')
                    assert f['value'] == str(value) and f['access'] & 24 == 24; declarations.append(f)
                proof['actual_raw_enum_and_option_constant_declarations'] = declarations
            assert proof['actual_option_read_instruction_order'] == ['17 143','17 185','16 118']
            typed,operations = expanded(proof,mode=='reference')
            proof.update(mode=mode,input_sha256=recover.sha(path), complete_expanded_typed_instructions=typed,explicit_expansion_operations=operations)
            if mode == 'raw': raw=proof
            evidence.append(proof)
        comparisons = []
        for p in evidence[1:]:
            left,right=evidence[0]['complete_expanded_typed_instructions'],p['complete_expanded_typed_instructions']
            assert left == right, (target,p['mode'],list(difflib.unified_diff(left,right,lineterm='')))
            comparisons.append(dict(mode=p['mode'],complete_instructions_after_explicit_expansions_equal=True,
                                    whole_instruction_diff=list(difflib.unified_diff(left,right,lineterm=''))))
        assert len(evidence[0]['complete_expanded_typed_instructions']) == (41 if target=='MIDP2' else 27)
        builds.append(dict(target=target,evidence=evidence,comparisons=comparisons))
    source = recover.ROOT/'src/DrawControls/VirtualList.java'
    report = dict(scope='Whole three-platform native/raw/optimized bodies and empty handlers, all actual shared field declarations, '
                  'getter bodies/table and every direct caller retained. All bodies read swap key 143 before XStatus key 185 before font key 118. '
                  'The full raw/optimized caption-offset helper is expanded at its sole leading call, as are actual two-instruction device getters; '
                  'branch coordinates are explicitly rebased. Only the two raw dead MENU_RIGHT assignment pairs are excluded, matching its actual absence '
                  'from all native/optimized declarations; source ABI and all raw writes remain in the whole evidence. '
                  'After these enumerated compiler operations all six source bodies equal their complete native 41/27-instruction counterparts. '
                  'This is not unmodified instruction equality, an arbitrary inliner, reflection or whole-program equivalence.',
                  source_file='src/DrawControls/VirtualList.java',source_sha256=recover.sha(source),builds=builds)
    (recover.ROOT/'preservation/reports/source-softkeys-bytecode.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS soft-key bytecode: all three whole May 41/27-instruction bodies match after explicit caption/device helper expansion and raw dead-field removal')
    return report


if __name__ == '__main__': main()
