#!/usr/bin/env python3
"""Bind the existing text-list scenarios to complete current helper classes and rerun."""
import json
import zipfile

import recover
from audit_static_text import OWNER
from test_source import ROOT, TEST, CACHE, run


def main():
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    inputs = {'reference': ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': ROOT / 'build/source/MIDP2-RU/classes.jar'}
    outputs, proofs = [], []
    for mode, path in inputs.items():
        legacy = 'reference' if mode == 'reference' else 'source'
        fixture = TEST / ('text_list-' + legacy + '.jar')
        output = TEST / ('static-text-' + mode + '.txt')
        subject = 'bi.class' if mode == 'reference' else OWNER + '.class'
        with zipfile.ZipFile(path) as a, zipfile.ZipFile(fixture) as b:
            assert a.read(subject) == b.read(subject), ('Current TextList class stale/altered in fixture', mode)
            changed = [n for n in a.namelist() if n in b.namelist() and a.read(n) != b.read(n)]
            missing = sorted(set(a.namelist()) - set(b.namelist())); added = sorted(set(b.namelist()) - set(a.namelist()))
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'TextListProbe', fixture, legacy, output], 'static-text-' + mode)
        result = output.read_bytes(); outputs.append(result)
        assert len(result.splitlines()) == 9775 and result.splitlines()[-1] == b'rasters:930'
        proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                       'output_sha256': recover.sha(output), 'observations': 9775, 'raster_frames': 930,
                       'complete_current_text_list_class_bytes_unchanged': True,
                       'changed_entry_bytes': changed, 'missing_entries': missing, 'added_entries': added})
    assert outputs[0] == outputs[1]
    report = {'scope': 'Existing MIDP2 RU TextListProbe rerun: 9775 observations and 930 rasters on native/raw. '
                       'The entire current TextList class is byte-for-byte unchanged in each fixture, including static '
                       'showText and getLineNumbers. Static scenarios call the helpers with small font, null/empty/multiline '
                       'text, widths, styles, clipping and shadow options. Other scenarios cover actual wrapping, caches, '
                       'selection and navigation; they are not claimed as new exhaustive helper coverage. Legacy fixtures '
                       'capture clocks and downstream application UI/file/network callbacks, and supply startup/list callbacks. '
                       'Changed/missing/added entries remain explicit: this is not external-only capture or all application '
                       'bytes unchanged. These observations already agreed before the height prepass was restored; this '
                       'replay does not invent a previous functional failure. Optimized runtime, three-platform rendering, '
                       'font failure injection, physical devices and whole-program equivalence are outside this replay.',
              'proofs': proofs}
    (ROOT / 'preservation/reports/source-static-text-replay.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS static text replay: 9775 observations / 930 rasters; complete current TextList class bytes unchanged in both fixtures')
    return report


if __name__ == '__main__': main()
