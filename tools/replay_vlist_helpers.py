#!/usr/bin/env python3
"""Bind the existing VirtualList replay to current complete helper bodies and rerun it."""
import json
import zipfile

import recover
from audit_send_text import classes
from audit_vlist_helpers import MEMBERS, OWNER, translated
from test_source import ROOT, TEST, CACHE, run


def main():
    cp = recover.cp([CACHE / n for n in recover.ASM])
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    inputs = {'reference': ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': ROOT / 'build/source/MIDP2-RU/classes.jar'}
    outputs, proofs = [], []
    for mode, path in inputs.items():
        reference = mode == 'reference'; legacy = 'reference' if reference else 'source'
        fixture = TEST / ('vlist-' + legacy + '.jar'); output = TEST / ('vlist-helper-' + mode + '.txt')
        before = {c['name']: c for c in classes(path, cp)}
        after = {c['name']: c for c in classes(fixture, cp)}
        owner = 'cd' if reference else OWNER
        unchanged = []
        for name, desc, role, raw_override in MEMBERS:
            identity = (name, desc) if reference else (role, raw_override or translated(desc))
            left = next(m for m in before[owner]['methods'] if (m['name'], m['desc']) == identity)
            right = next(m for m in after[owner]['methods'] if (m['name'], m['desc']) == identity)
            assert left == right, ('Legacy helper is stale/altered', mode, identity)
            unchanged.append(owner + '.' + left['name'] + left['desc'])
        assert {k: v for k, v in before[owner].items() if k != 'methods'} == {
            k: v for k, v in after[owner].items() if k != 'methods'}
        changed_methods = []
        for left, right in zip(before[owner]['methods'], after[owner]['methods']):
            assert (left['name'], left['desc']) == (right['name'], right['desc'])
            if left != right:
                changed_methods.append({'complete_current_method': left, 'complete_fixture_method': right})
        assert len(before[owner]['methods']) == len(after[owner]['methods'])
        with zipfile.ZipFile(path) as a, zipfile.ZipFile(fixture) as b:
            changed = [n for n in a.namelist() if n in b.namelist() and a.read(n) != b.read(n)]
            absent = sorted(set(a.namelist()) - set(b.namelist())); added = sorted(set(b.namelist()) - set(a.namelist()))
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'VirtualListProbe', fixture, legacy, output], 'vlist-helper-' + mode)
        result = output.read_bytes(); outputs.append(result)
        assert len(result.splitlines()) == 1992 and result.splitlines()[-1] == b'rasters:1238'
        proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'observations': 1992, 'raster_frames': 1238,
                       'eighteen_current_complete_helpers_equal_fixture': unchanged,
                       'dumped_class_header_and_field_metadata_unchanged': True, 'other_changed_subject_methods': changed_methods,
                       'changed_entry_bytes': changed, 'missing_entries': absent, 'added_entries': added})
    assert outputs[0] == outputs[1]
    report = {'scope': 'Replayed existing MIDP2 RU VirtualListProbe: 1992 observations including 1238 whole-frame rasters '
                       'on native/raw only. Eighteen complete current helper bodies must equal their actual legacy fixture '
                       'methods; dumped class name, superclass, interfaces, version and field metadata remain unchanged. '
                       'Class access, declared throws and other attributes are outside this dump comparison. Every other modified VirtualList method is retained '
                       'in full before/after, and changed/missing/added entries are listed. Fixtures capture clocks and downstream '
                       'application display/file/network/UI callbacks, including background-image installation outside the eighteen '
                       'helpers; this is not an external-only capture or a claim that all application bytes are unchanged. '
                       'RecoveryList supplies row data and observes callbacks; real base rendering/input remains executed. '
                       'Existing frame/font/icon/caption-pointer/row-hook/scrollbar scenarios are reused, not asserted as new '
                       'exhaustive coverage of each helper. Optimized runtime, all three platforms and physical device rendering '
                       'or whole-program equivalence are outside this replay.', 'proofs': proofs}
    (ROOT / 'preservation/reports/source-vlist-helper-replay.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS VirtualList helper replay: 1992 observations / 1238 rasters; eighteen complete current helpers unchanged in legacy fixtures')
    return report


if __name__ == '__main__': main()
