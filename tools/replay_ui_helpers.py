#!/usr/bin/env python3
"""Bind existing splash/chat replay to current unchanged subject classes and rerun it."""
import json
import zipfile

import audit_source as audit
import recover
from audit_send_text import classes
from test_source import ROOT, TEST, CACHE, run


def main():
    runtime = [CACHE / name for name in ['microemu.jar', 'microemu-nokiaui.jar',
                                       'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    inputs = {'reference': ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
              'raw': ROOT / 'build/source/MIDP2-RU/classes.jar',
              'optimized': ROOT / 'dist/source/Jimm-MIDP2-RU.jar'}
    reports = []
    cp = recover.cp([CACHE / name for name in recover.ASM])
    for prefix, probe, required in [
        ('splash', 'SplashProbe', [('cv', 'jimm/SplashCanvas')]),
        ('chat', 'ChatProbe', [('y', 'jimm/ChatTextList'), ('bt', 'jimm/ChatHistory'),
                             ('bi', 'DrawControls/TextList'), ('bm', 'DrawControls/TextLine'),
                             ('bc', 'DrawControls/TextItem')]),
    ]:
        proofs, outputs = [], []
        for mode in (['reference', 'raw', 'optimized'] if prefix == 'splash' else ['reference', 'raw']):
            reference = mode == 'reference'
            legacy_mode = 'reference' if reference else 'source'
            fixture = TEST / (prefix + '-' + legacy_mode + '.jar')
            output = TEST / ('ui-helper-' + prefix + '-' + mode + '.txt')
            if mode == 'optimized':
                fixture = TEST / 'ui-helper-splash-optimized.jar'
                run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SplashFixture',
                     inputs[mode], fixture, 'source'], 'ui-helper-splash-optimized-fixture')
            subject_proofs = []
            with zipfile.ZipFile(inputs[mode]) as before, zipfile.ZipFile(fixture) as after:
                for native, source in required:
                    name = (native if reference else source) + '.class'
                    assert before.read(name) == after.read(name), ('Legacy fixture subject is stale/modified', prefix, mode, name)
                    subject_proofs.append({'class': name, 'current_input_bytes_equal_fixture': True})
                changed = [n for n in before.namelist() if n.endswith('.class') and n in after.namelist()
                           and before.read(n) != after.read(n)]
                added = sorted(set(after.namelist()) - set(before.namelist()))
            # Chat composition must keep the actual shared message-text builder too.
            if prefix == 'chat':
                owner = 'cf' if reference else 'jimm/JimmUI'
                name = 'a' if reference else 'addMessageText'
                desc = '(Lbi;Ljava/lang/String;II)V' if reference else '(LDrawControls/TextList;Ljava/lang/String;II)V'
                original = next(c for c in classes(inputs[mode], cp) if c['name'] == owner)
                captured = next(c for c in classes(fixture, cp) if c['name'] == owner)
                left = next(m for m in original['methods'] if (m['name'], m['desc']) == (name, desc))
                right = next(m for m in captured['methods'] if (m['name'], m['desc']) == (name, desc))
                assert left == right
                subject_proofs.append({'method': owner + '.' + name + desc,
                                       'current_complete_body_equal_fixture': True})
            run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                 '-cp', recover.cp([TEST, *runtime]), probe, fixture, legacy_mode, output],
                'ui-helper-' + prefix + '-' + mode)
            result = output.read_bytes()
            outputs.append(result)
            proofs.append({'mode': mode, 'input_sha256': recover.sha(inputs[mode]),
                           'fixture_sha256': recover.sha(fixture), 'output_sha256': recover.sha(output),
                           'observations': len(result.splitlines()), 'unchanged_subjects': subject_proofs,
                           'other_changed_class_bytes': changed, 'added_entries': added})
        assert all(output == outputs[0] for output in outputs[1:]), ('Legacy whole subject replay differs', prefix)
        assert len(outputs[0].splitlines()) == (122 if prefix == 'splash' else 1341)
        reports.append({'series': prefix, 'proofs': proofs})
    report = {
        'scope': 'Replayed existing MIDP2 RU legacy SplashProbe (122 observations, native/raw/delivered optimized) '
                 'and ChatProbe (1341 observations, native/raw only). '
                 'Current original/raw complete SplashCanvas bytes and ChatTextList/ChatHistory/TextList/TextLine/TextItem '
                 'bytes must equal the actual legacy fixture; actual JimmUI.addMessageText complete body is retained too. '
                 'Splash transition/password/action/key and raster samples, chat composition/styles/scrolling/ack/RMS/deletion '
                 'and smile samples remain their existing scopes, not new exhaustive tests. '
                 'Existing SplashFixture supplies fixed date/day text by replacing downstream Util methods and adds an inert Action. '
                 'Existing chat fixture chain replaces downstream application UI/network/file callbacks and clocks; it is not an '
                 'external-only capture. Every changed class byte entry and added entry is listed. '
                 'This replay does not claim all application bytes unchanged, optimized chat runtime execution, three-platform '
                 'rendering, physical timing or whole-program equivalence. Raw/optimized helper ABI/body evidence is separate.',
        'series': reports,
    }
    (ROOT / 'preservation/reports/source-ui-helper-replay.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS UI helper replay: 122 splash + 1341 chat observations; current subject bytes unchanged in legacy fixtures')
    return report


if __name__ == '__main__':
    main()
