#!/usr/bin/env python3
"""Bind the full minimize controller to a minimal scripted display/destination fixture."""
import argparse
import difflib
import hashlib
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


OWNER = 'jimm/Jimm'


def digest(value):
    return hashlib.sha256(json.dumps(value, sort_keys=True, separators=(',', ':')).encode()).hexdigest()


def entries(path):
    with zipfile.ZipFile(path) as z: return {n: z.read(n) for n in z.namelist()}


def subject(data, mode):
    c = next(c for c in data if c['name'] == OWNER)
    m = next(m for m in c['methods'] if m['name'] == ('a' if mode == 'reference' else 'setMinimized') and m['desc'] == '(Z)V')
    display = next(f for f in c['fields'] if f['desc'] == 'Ljavax/microedition/lcdui/Display;' and f['access'] & 8)
    destination = next(m for m in c['methods'] if m['name'] == ('b' if mode == 'reference' else 'showWorkScreen') and m['desc'] == '()V')
    return m, display, destination


def main(before=False):
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    if before:
        import subprocess
        commit = subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT).decode().strip()
        assert commit.startswith('25b6922')
        assert b'if ((disp == null) || !disp.isShown())' in (ROOT / 'src/jimm/Jimm.java').read_bytes()
        assert not (ROOT / 'preservation/reports/source-minimize-before.json').exists(), 'Never replace the frozen pre-edit result'
    cp = recover.cp([CACHE / n for n in recover.ASM]); tag = 'before' if before else 'current'
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-minimize-dump')
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', cp, '-d', TEST,
         ROOT / 'tools/source/MinimizeFixture.java', ROOT / 'tools/source/MinimizeProbe.java'], 'compile-minimize-probe')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-cp', CACHE / 'microemu.jar', '-d', TEST,
         ROOT / 'tools/source/MinimizeIO.java'], 'compile-minimize-io')
    paths = dict(reference=ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
                 raw=ROOT / 'build/source/MIDP2-RU/classes.jar', optimized=ROOT / 'dist/source/Jimm-MIDP2-RU.jar')
    evidence = []; outputs = {}
    for mode, path in paths.items():
        original = classes(path, cp); m, field, destination = subject(original, mode); actual_input = path
        if mode == 'raw':
            actual_input = TEST / ('minimize-' + tag + '-raw-input.jar')
            values = {n: b for n, b in entries(paths['optimized']).items() if not n.endswith('.class')}; values.update(entries(path))
            write_jar(actual_input, values); assert entries(actual_input) == values
        fixture = TEST / f'minimize-{tag}-{mode}.jar'; out = TEST / f'minimize-{tag}-{mode}.txt'
        run([recover.java(), '-cp', recover.cp([TEST, *[CACHE / n for n in recover.ASM]]), 'MinimizeFixture', actual_input,
             fixture, mode, TEST], 'minimize-fixture-' + tag + '-' + mode)
        modified = classes(fixture, cp); captured, captured_field, captured_destination = subject(modified, mode)
        assert captured_field == field and captured_destination == destination
        mapping = {'182 javax/microedition/lcdui/Display.setCurrent(Ljavax/microedition/lcdui/Displayable;)V': '184 MinimizeIO.setCurrent(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V',
                   '182 javax/microedition/lcdui/Display.getCurrent()Ljavax/microedition/lcdui/Displayable;': '184 MinimizeIO.getCurrent(Ljavax/microedition/lcdui/Display;)Ljavax/microedition/lcdui/Displayable;',
                   '182 javax/microedition/lcdui/Displayable.isShown()Z': '184 MinimizeIO.isShown(Ljavax/microedition/lcdui/Displayable;)Z',
                   '184 ' + OWNER + '.' + destination['name'] + '()V': '184 MinimizeIO.work()V'}
        expected = dict(m); captures = []; expected['code'] = []
        for i, instruction in enumerate(m['code']):
            replacement = mapping.get(instruction, instruction); expected['code'].append(replacement)
            if replacement != instruction: captures.append(dict(position=i, before=instruction, after=replacement))
        assert len(captures) == 4
        expected['refs'] = [s for s in expected['code'] if 178 <= int(s.split()[0]) <= 185]
        assert expected == captured
        original_classes = {c['name']: c for c in original}; captured_classes = {c['name']: c for c in modified}; bindings = []
        assert set(captured_classes) - set(original_classes) == {'MinimizeIO', 'MinimizeIO$Other', 'MinimizeIO$Editor'}
        for name, c in original_classes.items():
            adjusted = dict(c)
            if name == OWNER: adjusted['methods'] = [expected if n['name'] == m['name'] and n['desc'] == m['desc'] else n for n in c['methods']]
            assert captured_classes[name] == adjusted
            bindings.append(dict(owner=name, original_sha256=digest(c), expected_sha256=digest(adjusted), fixture_sha256=digest(captured_classes[name])))
        a, b = entries(actual_input), entries(fixture); added = sorted(set(b) - set(a)); removed = sorted(set(a) - set(b))
        changed = sorted(n for n in a if n in b and a[n] != b[n])
        assert changed == ['jimm/Jimm.class'] and not removed and added == ['MinimizeIO$Editor.class', 'MinimizeIO$Other.class', 'MinimizeIO.class']
        assert all(b[n] == (TEST / n).read_bytes() and int.from_bytes(b[n][6:8], 'big') == 51 for n in added)
        result = run([recover.java(), '-Djava.awt.headless=true', '-cp', TEST, 'MinimizeProbe', fixture, CACHE, mode, out], 'minimize-probe-' + tag + '-' + mode)
        values = out.read_bytes(); assert len(values.splitlines()) == 6144; outputs[mode] = values
        aliases = {OWNER + '.' + field['name'] + ' ' + field['desc']: OWNER + '.display ' + field['desc'],
                   OWNER + '.' + destination['name'] + destination['desc']: OWNER + '.showWorkScreen()V'}
        typed = [op + sep + aliases.get(operand, operand) for op, sep, operand in [s.partition(' ') for s in m['code']]]
        direct = OWNER + '.' + m['name'] + m['desc']
        callers = [dict(owner=c['name'], complete_method=n, positions=[i for i, s in enumerate(n['code']) if s == '184 ' + direct])
                   for c in original for n in c['methods'] if '184 ' + direct in n['code']]
        evidence.append(dict(mode=mode, input_sha256=recover.sha(path), assembled_input_sha256=recover.sha(actual_input), fixture_sha256=recover.sha(fixture),
                             complete_original_method=m, complete_expected_method=expected, complete_fixture_method=captured, typed_instructions=typed,
                             complete_display_field=field, complete_unchanged_work_screen_destination=destination, whole_direct_callers=callers,
                             explicit_three_display_and_one_application_destination_captures=captures, all_original_whole_class_bindings=bindings,
                             changed_entries=changed, added_entries=added, removed_entries=removed,
                             provider_sha256={n: hashlib.sha256(b[n]).hexdigest() for n in added}, observations=6144, output_sha256=recover.sha(out), result=result))
        print(tag, mode, result, flush=True)
    comparisons = []
    for mode in ['raw', 'optimized']:
        left, right = outputs['reference'].decode().splitlines(), outputs[mode].decode().splitlines()
        differing = [dict(position=i, native=a, source=b) for i, (a, b) in enumerate(zip(left, right)) if a != b]
        comparisons.append(dict(mode=mode, complete_output_bytes_equal=outputs['reference'] == outputs[mode], differing_observations=len(differing), all_differing_rows=differing,
                                complete_typed_instruction_diff=list(difflib.unified_diff(evidence[0]['typed_instructions'], next(e for e in evidence if e['mode'] == mode)['typed_instructions'], lineterm=''))))
    if before:
        assert all(c['differing_observations'] > 0 for c in comparisons)
        witness = '2:false:false:false:work:0:false:1:0:'
        for c in comparisons:
            row = next(r for r in c['all_differing_rows'] if r['native'].startswith(witness))
            assert '/work' not in row['native'] and row['native'].endswith(':editor')
            assert '/work' in row['source'] and row['source'].endswith(':work-screen')
    else:
        assert all(c['complete_output_bytes_equal'] for c in comparisons)
    report = dict(scope='Complete MIDP2 setMinimized bodies execute unchanged except exactly three display call-site captures and one explicit application showWorkScreen destination capture. '
                  'All branch/local/instanceof instructions and handlers remain, the actual javax TextBox class and its real subclass determine the predicate. '
                  'Unsafe allocates actual host API objects without physical/emulator constructors; display/current/shown state and work-screen destination are explicitly scripted. '
                  '6144 observations/version record repeated true/false minimize sequences, null/current/other/TextBox/subclass, shown/not shown, null display receivers, '
                  'exact ordered effects/counters and before/after runtime/Error failures at four destinations and two call ordinals. '
                  'The real Jimm initializer and Timer run unmodified and its Timer is cancelled at test exit. No complete showWorkScreen/password downstream, physical display or all-platform minimize claim.',
                  source_file='src/jimm/Jimm.java', source_sha256=recover.sha(ROOT / 'src/jimm/Jimm.java'), evidence=evidence, comparisons=comparisons)
    if before: report['baseline_commit'] = commit
    else:
        baseline_path = ROOT / 'preservation/reports/source-minimize-before.json'; baseline = json.loads(baseline_path.read_text(encoding='utf-8'))
        assert baseline['evidence'][0]['input_sha256'] == evidence[0]['input_sha256'] and baseline['evidence'][0]['output_sha256'] == evidence[0]['output_sha256']
        report['before_report_sha256'] = recover.sha(baseline_path)
        report['genuine_before_differing_observations'] = {c['mode']: c['differing_observations'] for c in baseline['comparisons']}
    name = 'source-minimize-before.json' if before else 'source-minimize-replay.json'
    (ROOT / 'preservation/reports' / name).write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS minimize ' + tag + ': complete fixture bindings; ' + ('genuine pre-edit editor mismatch retained' if before else 'all 6144 native/raw/optimized observations match'))
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--before', action='store_true'); main(parser.parse_args().before)
