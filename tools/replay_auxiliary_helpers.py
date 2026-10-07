#!/usr/bin/env python3
"""Bind existing template/timer/form replays to the current complete helper bodies."""
import json
import zipfile

import recover
from audit_send_text import classes
from test_source import ROOT, TEST, CACHE, run


def main():
    cp = recover.cp([CACHE / n for n in recover.ASM])
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    series = []
    for prefix, probe, members, observations, rasters in [
        ('templates', 'TemplatesProbe', [('aq', 'jimm/Templates', 'b', 'select', '()V'),
                                        ('aq', 'jimm/Templates', 'a', 'getTemlate', '()Ljava/lang/String;')], 1211, 76),
        ('templates_smart', 'TemplatesProbe', [('aq', 'jimm/Templates', 'b', 'select', '()V'),
                                              ('aq', 'jimm/Templates', 'a', 'getTemlate', '()Ljava/lang/String;')], 1211, 76),
        ('timer', 'TimerProbe', [('at', 'jimm/TimerTasks', 'a', 'isCanceled', '()Z'),
                               ('at', 'jimm/TimerTasks', 'a', 'getType', '()I'),
                               ('at', 'jimm/TimerTasks', 'b', 'flashRestoreOldCaption', '()V')], 8941, None),
        ('form', 'FormProbe', [('d', 'DrawControls/VirtualForm', 'a', 'fitField', '(Ljavax/microedition/lcdui/TextField;Ljavax/microedition/lcdui/Font;)Ljava/lang/String;'),
                             ('d', 'DrawControls/VirtualForm', 'a', 'fieldText', '(Ljavax/microedition/lcdui/TextField;)Ljava/lang/String;')], 6149, 2562)]:
        proofs, outputs = [], []
        for mode, legacy in [('reference', 'reference'), ('raw', 'source')]:
            path = ROOT / ('preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar' if mode == 'reference' else 'build/source/MIDP2-RU/classes.jar')
            fixture = TEST / (prefix + '-' + legacy + '.jar')
            output = TEST / ('auxiliary-' + prefix + '-' + mode + '.txt')
            before = {c['name']: c for c in classes(path, cp)}; after = {c['name']: c for c in classes(fixture, cp)}
            subjects = []
            for native_owner, source_owner, native_name, source_name, desc in members:
                owner, name = (native_owner, native_name) if mode == 'reference' else (source_owner, source_name)
                a = next(m for m in before[owner]['methods'] if m['name'] == name and m['desc'] == desc)
                b = next(m for m in after[owner]['methods'] if m['name'] == name and m['desc'] == desc)
                assert a == b, ('Legacy helper body is stale/modified', prefix, mode, owner, name)
                subjects.append({'owner': owner, 'complete_method': a, 'fixture_complete_method': b, 'complete_body_and_metadata_equal': True})
            if prefix.startswith('templates') and mode == 'raw':
                owner = 'jimm/Templates'; a = next(m for m in before[owner]['methods'] if m['name'] == 'sort' and m['desc'] == '()V')
                b = next(m for m in after[owner]['methods'] if m['name'] == 'sort' and m['desc'] == '()V'); assert a == b
                subjects.append({'owner': owner, 'complete_method': a, 'fixture_complete_method': b, 'complete_body_and_metadata_equal': True})
            with zipfile.ZipFile(path) as original, zipfile.ZipFile(fixture) as captured:
                changed = sorted(n for n in original.namelist() if n in captured.namelist() and original.read(n) != captured.read(n))
                added = sorted(set(captured.namelist()) - set(original.namelist()))
                removed = sorted(set(original.namelist()) - set(captured.namelist()))
            args = [recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647']
            if prefix.endswith('smart'): args.append('-Djimm.templates.smart=true')
            result = run([*args, '-cp', recover.cp([TEST, *runtime]), probe, fixture, legacy, output], 'auxiliary-' + prefix + '-' + mode)
            content = output.read_bytes(); outputs.append(content); assert len(content.splitlines()) == observations
            if rasters is not None: assert content.splitlines()[-1] == ('rasters:' + str(rasters)).encode()
            assert content == (TEST / (prefix + '-' + legacy + '.txt')).read_bytes(), 'Existing complete replay output changed'
            proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'observations': observations, 'raster_frames': rasters,
                           'current_complete_helpers_equal_legacy_fixture': subjects,
                           'changed_entries': changed, 'added_entries': added, 'removed_entries': removed})
            print(prefix, mode, result, flush=True)
        assert outputs[0] == outputs[1]
        series.append({'series': prefix, 'proofs': proofs, 'different_observations': 0})
    report = {'scope': 'Existing native/raw MIDP2 RU TemplatesProbe in normal and smart-SE modes, TimerProbe and '
                       'FormProbe execute after binding every selected complete current helper declaration/body '
                       'to its actual legacy fixture. Template promotion/editor/RMS/selection and insert failures, '
                       'timer getters/caption restoration/ticks/keepalive/status and form fitting/choice/touch/editor '
                       'and rasters retain their existing scopes. The full raw sort helper is retained too. '
                       'Every changed/added/removed fixture entry is enumerated; inherited host/clock/application '
                       'UI/file/network callback captures remain. This is not an external-only fixture or proof '
                       'that all application bytes are unchanged. No optimized runtime, three-platform rendering, '
                       'physical scheduler/device or whole-program equivalence is claimed.', 'series': series}
    (ROOT / 'preservation/reports/source-auxiliary-helper-replay.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS auxiliary replay: 17512 observations per native/raw version; complete current helpers retained in legacy fixtures')
    return report


if __name__ == '__main__': main()
