#!/usr/bin/env python3
"""Keep complete remaining VirtualList bodies, including raw and removed/specialized ABIs."""
import difflib
import json
import os

import audit_source as audit
import recover
from audit_send_text import classes


OWNER = 'DrawControls/VirtualList'
MEMBERS = [
    ('a', '(I)Ljavax/microedition/lcdui/Font;', 'getQuickFont', None),
    ('b$1385ff', '()V', 'setCyclingCursor', '(Z)V'),
    ('c', '(Le;)V', 'setCapPrivateImage', None),
    ('e', '(Le;)V', 'setCapSoundImage', None),
    ('a', '(Lbx;)V', 'setVLCommands', None),
    ('a', '()V', 'createSetOfFonts', '(I)V'),
    ('c', '()I', 'getFontSize', None),
    ('d', '()I', 'getTextColor', None),
    ('f', '()I', 'getCursorMode', None),
    ('f', '()V', 'showNotify', None),
    ('a', '(Lf;)V', 'setImageList', None),
    ('e', '(I)V', 'moveCursor', None),
    ('a', '(Ljavax/microedition/lcdui/Command;)Z', 'executeCommand', None),
    ('b', '()Ljava/lang/String;', 'getCaption', None),
    ('k', '()V', 'lock', None),
    ('c', '()Z', 'getLocked', None),
    ('k', '()I', 'getWidth', None),
    ('l', '()I', 'getHeight', None),
]
SURVIVORS = {'getQuickFont', 'setImageList', 'executeCommand'}


def translated(desc):
    for before, after in audit.CLASSES.items(): desc = desc.replace('L' + before + ';', 'L' + after + ';')
    return desc


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    inputs = {'reference': recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
              'optimized': recover.ROOT / 'dist/source/Jimm-MIDP2-RU.jar'}
    data = {mode: {c['name']: c for c in classes(p, cp)} for mode, p in inputs.items()}
    def body(owner, m, native=False):
        return {'method': owner + '.' + m['name'] + m['desc'], 'access': m['access'],
                'instructions': audit.normalized(m['code']) if native else m['code'],
                'handlers': audit.normalized_handlers(m['handlers']) if native else m['handlers']}
    records, removed = [], set()
    for name, desc, role, raw_override in MEMBERS:
        raw_desc = raw_override or translated(desc)
        native = audit.resolve_method(data['reference']['cd']['methods'], name, desc)
        raw = audit.resolve_method(data['raw'][OWNER]['methods'], role, raw_desc)
        left, right = body('cd', native, True), body(OWNER, raw)
        source_matches = [m for m in data['optimized'][OWNER]['methods'] if m['name'].split('$')[0] == role and m['desc'] == raw_desc]
        assert bool(source_matches) == (role in SURVIVORS), role
        assert len(source_matches) <= 1
        specialization = None
        if role == 'setCyclingCursor':
            assert right['instructions'] == ['25 0', '21 1', '181 DrawControls/VirtualList.cyclingCursor Z', '177']
            assert [s if s != '21 1' else '4' for s in right['instructions']] == left['instructions']
            specialization = {'kind': 'constant_argument', 'raw_boolean_slot': 1, 'native_value': True,
                              'complete_raw_code_with_one_boolean_load_replaced_equals_native': True}
            # Assert all actual application calls use true; raw false is not a recovered native ABI.
            call = '182 ' + OWNER + '.setCyclingCursor(Z)V'
            sites = [{'caller': c['name'] + '.' + m['name'] + m['desc'], 'instruction': i}
                     for c in data['raw'].values() for m in c['methods'] for i, s in enumerate(m['code']) if s == call]
            assert sites
            for site in sites:
                caller_owner, rest = site['caller'].split('.', 1); caller_name = rest.split('(', 1)[0]; caller_desc = '(' + rest.split('(', 1)[1]
                caller = audit.resolve_method(data['raw'][caller_owner]['methods'], caller_name, caller_desc)
                assert caller['code'][site['instruction'] - 1] == '4'
            specialization['actual_raw_true_call_sites'] = sites
        elif role == 'createSetOfFonts':
            assert not any(s in ['21 1', '54 1', '132 1 1', '132 1 -1'] for s in right['instructions'])
            specialization = {'kind': 'unused_integer_argument', 'raw_slot': 1, 'raw_parameter_not_read': True}
        if role in ['getWidth', 'getHeight']:
            assert native['access'] & 8 and not raw['access'] & 8 and '25 0' not in raw['code']
            specialization = {'kind': 'unused_instance_receiver', 'native_static': True, 'raw_receiver_not_read': True}
        else: assert native['access'] & 40 == raw['access'] & 40
        raw_equal = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
        if role not in ['setCyclingCursor', 'showNotify']: assert raw_equal, role
        if role == 'showNotify':
            # Keep the different Java constant-pool owners; do not suppress them in comparison.
            replaced = [s.replace('javax/microedition/lcdui/Canvas.setCommandListener',
                                  'javax/microedition/lcdui/Displayable.setCommandListener') for s in left['instructions']]
            assert replaced == right['instructions'] and left['handlers'] == right['handlers']
        comparisons = [{'mode': 'raw', 'same_complete_instructions_and_handlers': raw_equal,
                        'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm='')),
                        'full_handler_diff': list(difflib.unified_diff(left['handlers'], right['handlers'], lineterm=''))}]
        bodies = {'reference': left, 'raw': right}
        if source_matches:
            optimized = body(OWNER, source_matches[0]); bodies['optimized'] = optimized
            assert native['access'] & 40 == optimized['access'] & 40
            assert left['instructions'] == optimized['instructions'] and left['handlers'] == optimized['handlers']
            comparisons.append({'mode': 'optimized', 'same_complete_instructions_and_handlers': True})
        else:
            bodies['optimized'] = {'matching_raw_signature_absent': True,
                                   'other_same_name_bodies': [body(OWNER, m) for m in data['optimized'][OWNER]['methods'] if m['name'].split('$')[0] == role]}
            removed.add(OWNER + '.' + role + raw_desc)
        records.append({'role': role, 'bodies': bodies, 'specialization': specialization, 'comparisons': comparisons})
    # Retain whole callers; this is optimizer provenance, not a claim of caller equality.
    witnesses = []
    for c in data['raw'].values():
        for m in c['methods']:
            calls = sorted({s.partition(' ')[2] for s in m['code'] if s.startswith(('182 ', '183 ', '184 ')) and s.partition(' ')[2] in removed})
            if not calls: continue
            candidates = [n for n in data['optimized'].get(c['name'], {}).get('methods', []) if n['name'].split('$')[0] == m['name'].split('$')[0]]
            for n in candidates:
                assert not any(s.partition(' ')[2] in removed for s in n['code'] if s.startswith(('182 ', '183 ', '184 ')))
            witnesses.append({'raw_helper_calls': calls, 'complete_raw_caller': body(c['name'], m),
                              'same_name_optimized_callers': [body(c['name'], n) for n in candidates],
                              'same_descriptor_caller_present': any(n['desc'] == m['desc'] for n in candidates),
                              'removed_helper_invocations_absent_after_optimization': True})
    assert removed <= {s for w in witnesses for s in w['raw_helper_calls']}
    main_members = {(name, desc) for owner, name, desc, role in audit.METHODS if owner == 'cd'}
    helper_members = {(name, desc) for name, desc, role, raw_override in MEMBERS}
    all_native = {(m['name'], m['desc']) for m in data['reference']['cd']['methods']}
    assert all_native == main_members | helper_members
    report = {'scope': 'Eighteen complete May MIDP2 RU native/raw VirtualList helpers, three surviving optimized methods and fifteen '
                       'explicitly absent raw helper signatures. Sixteen native/raw complete instruction/handler sequences match; '
                       'the true cycling argument specialization is separate and all actual raw callers are checked. The font-creation '
                       'integer and dimension instance receivers are unused and kept explicit. showNotify retains its distinct Java '
                       'Canvas/Displayable invocation owners rather than hiding the difference. Whole raw callers and every same-name '
                       'optimized body remain recorded without declaring them equal. The surviving two-argument moveCursor is not '
                       'invented as the removed one-argument wrapper. This inventory alone does not prove runtime/whole-program equivalence.',
              'inputs': {mode: {'path': str(p.relative_to(recover.ROOT)).replace('\\', '/'), 'sha256': recover.sha(p)} for mode, p in inputs.items()},
              'methods': records, 'complete_caller_witnesses': witnesses,
              'native_class_accounting': {'all_native_methods_accounted': True, 'native_methods': len(all_native),
                                          'main_audit_methods': len(main_members),
                                          'additional_helper_methods': len(helper_members - main_members)}}
    (recover.ROOT / 'preservation/reports/source-vlist-helper-bytecode.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS VirtualList helpers: 18 native/raw bodies, 16 equal; 3 optimized bodies equal; 15 absent raw ABIs; all cycling calls true')


if __name__ == '__main__': main()
