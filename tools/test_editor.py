#!/usr/bin/env python3
"""Compare actual May editor logic in source and delivered optimized classes."""
import argparse
import json
import os
import audit_source as audit
import recover
from test_source import ROOT, TEST, CACHE, run
from test_jimm_urls import dump


def prepare():
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp',
         recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in
           ['EditorIO', 'EditorFixture', 'EditorProbe']]], 'compile-editor')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7',
         '-cp', recover.cp(runtime), '-d', TEST, ROOT / 'tools/source/EditorIO.java'], 'compile-editor-io')
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-editor-dump')
    return runtime, cp


def call_map(ref):
    result = {}
    for owner in ['javax/microedition/lcdui/TextBox', 'javax/microedition/lcdui/Displayable']:
        result['182 ' + owner + '.addCommand(Ljavax/microedition/lcdui/Command;)V'] = '184 EditorIO.add(Ljavax/microedition/lcdui/Displayable;Ljavax/microedition/lcdui/Command;)V'
        result['182 ' + owner + '.setCommandListener(Ljavax/microedition/lcdui/CommandListener;)V'] = '184 EditorIO.listener(Ljavax/microedition/lcdui/Displayable;Ljavax/microedition/lcdui/CommandListener;)V'
        result['182 ' + owner + '.setConstraints(I)V'] = '184 EditorIO.constraints(Ljavax/microedition/lcdui/TextBox;I)V'
    result['182 javax/microedition/lcdui/Display.setCurrent(Ljavax/microedition/lcdui/Displayable;)V'] = '184 EditorIO.show(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V'
    result['184 java/lang/System.gc()V'] = '184 EditorIO.gc()V'
    result['184 ' + ('aj.a' if ref else 'DrawControls/LightControl.flash') + '(Z)V'] = '184 EditorIO.light(Z)V'
    result['184 ' + ('r.a' if ref else 'jimm/comm/Icq.beginTyping') + '(Ljava/lang/String;Z)V'] = '184 EditorIO.typing(Ljava/lang/String;Z)V'
    return result


def execute(runtime, cp, mode, base, untouched, probe='EditorProbe', prefix='editor'):
    ref = mode == 'reference'
    owner = 'cf' if ref else 'jimm/JimmUI'
    before = dump(untouched, owner, cp)
    assert dump(base, owner, cp) == before, ('Prepared host changed controller', mode)
    fixture, output = TEST / (prefix + '-' + mode + '.jar'), TEST / (prefix + '-' + mode + '.txt')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'EditorFixture',
         base, fixture, 'reference' if ref else 'source', TEST], prefix + '-fixture-' + mode)
    after = dump(fixture, owner, cp)
    assert {k: v for k, v in before.items() if k != 'methods'} == {k: v for k, v in after.items() if k != 'methods'}
    assert len(before['methods']) == len(after['methods'])
    captures, mapping = {}, call_map(ref)
    for left, right in zip(before['methods'], after['methods']):
        assert {k: v for k, v in left.items() if k not in ('code', 'refs')} == {
            k: v for k, v in right.items() if k not in ('code', 'refs')}
        expected = []
        for instruction in left['code']:
            if instruction in mapping:
                captures[instruction] = captures.get(instruction, 0) + 1
                instruction = mapping[instruction]
            expected.append(instruction)
        assert expected == right['code'], ('Unexpected editor rewrite', mode, left['name'], left['desc'])
        assert right['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
    run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
         '-cp', recover.cp([TEST, *runtime]), probe, fixture,
         'reference' if ref else 'source', output], prefix + '-' + mode)
    return output.read_bytes(), {'mode': mode, 'base_sha256': recover.sha(base),
                               'fixture_sha256': recover.sha(fixture), 'captures': captures,
                               'whole_controller_unchanged_before_capture': True,
                               'only_enumerated_invocations_changed': True}


def main(prepared=False):
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    classes = ROOT / 'build/source/MIDP2-RU/classes.jar'
    if not prepared:
        current = json.loads((ROOT / 'preservation/reports/source-tests.json').read_text(encoding='utf-8'))
        assert current['source_jar_sha256'] == recover.sha(artifact), 'Prepare current build using test_source.py'
    runtime, cp = prepare()
    outputs, proofs = [], []
    for mode, untouched in [('reference', original), ('source', classes)]:
        output, proof = execute(runtime, cp, mode, TEST / ('clipboard-' + mode + '.jar'), untouched)
        outputs.append(output); proofs.append(proof)
    assert outputs[0] == outputs[1], 'Editor mismatch: compare build/source-tests/editor-{reference,source}.txt'
    login, host = TEST / 'editor-optimized-login.jar', TEST / 'editor-optimized-host.jar'
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'LoginFixture', artifact,
         login, 'source', TEST], 'editor-optimized-login')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SplashFixture', login,
         host, 'source'], 'editor-optimized-host')
    output, proof = execute(runtime, cp, 'optimized-source', host, artifact)
    assert output == outputs[0], 'Delivered optimized editor differs'
    proofs.append(proof)
    lines = outputs[0].decode('utf-8').splitlines()
    counts = list(map(int, lines[-1].split(':')[1:]))
    assert counts[:3] == [320, 180, 1578], counts
    report = {'scope': 'Actual MIDP2-RU JimmUI private caption, pagination, insertion, save/get/current-page '
                       'and public writeMessage creation/reuse. Actual TextBox state, constraints, commands and '
                       'listener are used. Add/listener/constraints call captures delegate to the real API; '
                       'display switch, gc, light and typing terminal calls are recorded with scripted failures. '
                       'Complete controller declarations, flags, instructions and handlers are verified before '
                       'and after interception. No physical display, gc scheduling, backlight or typing packet claim; '
                       'real typing/SNAC is independently tested by test_send_text.py. Existing login/date host '
                       'initialization boundaries apply. Unoptimized and delivered optimized classes both run.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(classes), 'observations': len(lines),
              'title_calls': counts[0], 'pagination_scenarios': counts[1], 'write_message_calls': counts[2],
              'command_add_calls': counts[3], 'typing_calls': counts[4], 'gc_calls': counts[5],
              'light_calls': counts[6], 'optimized_observations': len(output.splitlines()),
              'differences': 0, 'fixture_proofs': proofs}
    (ROOT / 'preservation/reports/source-editor.json').write_text(json.dumps(report, indent=2) + '\n',
                                                              encoding='utf-8', newline='\n')
    print('PASS editor:', len(lines), 'observations,', counts[:3], 'title/page/open calls; optimized application matches')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--prepared', action='store_true')
    main(parser.parse_args().prepared)
