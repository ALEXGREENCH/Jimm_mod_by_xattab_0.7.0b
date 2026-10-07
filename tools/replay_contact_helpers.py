#!/usr/bin/env python3
"""Rebind complete remaining contact helpers and real timer caller to the existing blink replay."""
import json
import zipfile

import audit_source as audit
import recover
from audit_contact_helpers import DIRECT, EXPRESSIONS, OWNER
from audit_send_text import classes
from audit_vlist_helpers import translated
from test_source import ROOT, TEST, CACHE, run


def main():
    cp = recover.cp([CACHE / n for n in recover.ASM])
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    inputs = {'reference': ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': ROOT / 'build/source/MIDP2-RU/classes.jar'}
    outputs, proofs = [], []
    for mode, path in inputs.items():
        reference = mode == 'reference'; legacy = 'reference' if reference else 'source'
        fixture = TEST / ('blink-' + legacy + '.jar'); output = TEST / ('contact-helper-' + mode + '.txt')
        before = {c['name']: c for c in classes(path, cp)}; after = {c['name']: c for c in classes(fixture, cp)}
        owner = 'z' if reference else OWNER
        members = [(n, d) if reference else (role, override or translated(d)) for n, d, role, override in DIRECT]
        for n, d, role, raw in EXPRESSIONS: members.extend([(n, d)] if reference else raw)
        members = sorted(set(members)); unchanged = []
        for n, d in members:
            left = audit.resolve_method(before[owner]['methods'], n, d); right = audit.resolve_method(after[owner]['methods'], n, d)
            assert left == right, ('Legacy contact helper stale/altered', mode, n, d)
            unchanged.append(owner + '.' + n + d)
        timer = 'ch' if reference else 'jimm/ContactItem$1'
        left = audit.resolve_method(before[timer]['methods'], 'run', '()V'); right = audit.resolve_method(after[timer]['methods'], 'run', '()V')
        assert {k: v for k, v in before[timer].items() if k != 'methods'} == {k: v for k, v in after[timer].items() if k != 'methods'}
        assert {k: v for k, v in left.items() if k not in ['code', 'refs']} == {k: v for k, v in right.items() if k not in ['code', 'refs']}
        expected, captures = [], 0
        for instruction in left['code']:
            if instruction == '182 java/util/TimerTask.cancel()Z':
                instruction = '184 BlinkIO.cancel(Ljava/util/TimerTask;)Z'; captures += 1
            expected.append(instruction)
        assert expected == right['code'] and captures == 1
        assert right['refs'] == [s for s in expected if 178 <= int(s.partition(' ')[0]) <= 185]
        changed_subjects = []
        for cls in [owner, timer]:
            assert {k: v for k, v in before[cls].items() if k != 'methods'} == {k: v for k, v in after[cls].items() if k != 'methods'}
            assert len(before[cls]['methods']) == len(after[cls]['methods'])
            for a, b in zip(before[cls]['methods'], after[cls]['methods']):
                assert (a['name'], a['desc']) == (b['name'], b['desc'])
                if a != b: changed_subjects.append({'owner': cls, 'complete_current_method': a, 'complete_fixture_method': b})
        with zipfile.ZipFile(path) as a, zipfile.ZipFile(fixture) as b:
            changed = [n for n in a.namelist() if n in b.namelist() and a.read(n) != b.read(n)]
            missing = sorted(set(a.namelist()) - set(b.namelist())); added = sorted(set(b.namelist()) - set(a.namelist()))
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'BlinkProbe', fixture, legacy, output], 'contact-helper-' + mode)
        result = output.read_bytes(); outputs.append(result); assert len(result.splitlines()) == 1816
        proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'observations': 1816,
                       'current_complete_helpers_equal_fixture': unchanged,
                       'actual_timer_run_only_one_external_cancel_call_changed': True,
                       'external_cancel_call_replacements_in_timer_run': 1,
                       'other_changed_subject_methods': changed_subjects,
                       'changed_entry_bytes': changed, 'missing_entries': missing, 'added_entries': added})
    assert outputs[0] == outputs[1]
    report = {'scope': 'Replayed existing MIDP2 RU BlinkProbe: 1816 observations on native/raw only. Fifteen native helpers/expressions '
                       'and sixteen raw helpers/accessors must equal their current complete fixture bodies. The actual anonymous timer '
                       'run keeps every instruction, branch, handler and method metadata except exactly one external TimerTask.cancel '
                       'call. All other modified ContactItem/timer methods are retained in full and changed/missing/added entries listed. '
                       'Existing blink ticks, restarts, option/duration changes, icons/font/color/visibility and callback scenarios '
                       'are reused; this does not claim each helper or serialization branch is newly exercised. Legacy fixtures capture '
                       'scheduling/clocks and downstream application UI/file/network callbacks; this is not external-only or all '
                       'application bytes unchanged. Optimized runtime, three platforms, real scheduling/races and whole-program '
                       'equivalence are outside this replay.', 'proofs': proofs}
    (ROOT / 'preservation/reports/source-contact-helper-replay.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS ContactItem helper replay: 1816 observations; current complete helpers unchanged; real timer has exactly one external cancel capture')
    return report


if __name__ == '__main__': main()
