#!/usr/bin/env python3
"""Compare actual editor command handling, including clipboard and otherwise-unused history commands."""
import argparse
import json
import os
import recover
from test_source import ROOT, TEST, CACHE, run
from test_editor import prepare, execute
from test_jimm_urls import dump


def command_labels(owner):
    init = next(m for m in owner['methods'] if m['name'] == '<clinit>')['code']
    labels = {}
    for at, insn in enumerate(init):
        if insn.startswith('179 ' + owner['name'] + '.') and insn.endswith(' Ljavax/microedition/lcdui/Command;'):
            start = max(i for i in range(at) if init[i] == '187 javax/microedition/lcdui/Command')
            strings = [s[10:] for s in init[start:at] if s.startswith('18 String:')]
            assert len(strings) == 1
            labels[insn[4:]] = strings[0]
    return labels


def main(negative=False):
    runtime, cp = prepare()
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp',
         recover.cp([TEST, *runtime]), '-d', TEST, ROOT / 'tools/source/EditorCommandProbe.java'], 'compile-editor-commands')
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    raw = ROOT / 'build/source/MIDP2-RU/classes.jar'
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    before = dump(original, 'cf', cp)
    assert {name: command_labels(before)['cf.' + short + ' Ljavax/microedition/lcdui/Command;'] for short, name in
            [('l', 'quote'), ('m', 'paste'), ('p', 'next'), ('q', 'prev'), ('r', 'clear'), ('w', 'trans'), ('x', 'detrans')]} == {
                'quote': 'O4', 'paste': 'y4', 'next': 'c', 'prev': 'F4', 'clear': 'f1', 'trans': 'd0', 'detrans': 'c0'}
    code = next(m for m in before['methods'] if m['name'] == 'commandAction')['code']
    assert '184 cf.a()Z' in code, 'Original handler must read the real clipboard-empty predicate'
    assert not any('cf.' + name + ' Ljavax/microedition/lcdui/Command;' in insn for insn in code for name in ['p', 'q'])
    outputs, proofs = [], []
    for mode, untouched in [('reference', original), ('source', raw)]:
        data, proof = execute(runtime, cp, mode, TEST / ('clipboard-' + mode + '.jar'), untouched,
                              'EditorCommandProbe', 'editor-commands')
        outputs.append(data); proofs.append(proof)
    if negative:
        assert outputs[0] != outputs[1], 'Expected genuine pre-fix mismatch'
        print('EXPECTED pre-fix editor-command mismatch; original and authored logs retained')
        return
    assert outputs[0] == outputs[1], 'Raw editor command behavior differs'
    login, host = TEST / 'editor-commands-optimized-login.jar', TEST / 'editor-commands-optimized-host.jar'
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'LoginFixture', artifact,
         login, 'source', TEST], 'editor-commands-optimized-login')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SplashFixture', login,
         host, 'source'], 'editor-commands-optimized-host')
    data, proof = execute(runtime, cp, 'optimized-source', host, artifact, 'EditorCommandProbe', 'editor-commands')
    assert data == outputs[0], 'Optimized editor command behavior differs'
    proofs.append(proof)
    lines = data.decode('utf-8').splitlines()
    assert lines[-1] == 'coverage:4320:960:960'
    report = {'scope': 'Actual MIDP2 editor commandAction, real TextBox, clipboard, page vector, insertion and '
                       'transliteration. Quote/paste/next/previous/clear/transliterate/detransliterate/unknown/null '
                       'commands are compared. The real command objects are identified by their original '
                       'constructor keys. Existing login/date startup boundaries apply. Complete controller '
                       'declarations, instructions and handlers are unchanged except enumerated EditorIO '
                       'terminal/API captures, which delegate command/listener/constraint operations. '
                       'No send/cancel/network, complete controller, full startup or physical device claim.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(raw), 'observations': len(lines),
              'command_calls': 4320, 'quote_paste_calls': 960, 'history_paging_command_calls': 960,
              'optimized_observations': len(lines), 'differences': 0, 'fixture_proofs': proofs}
    (ROOT / 'preservation/reports/source-editor-commands.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS editor commands:', len(lines), 'observations, 4320 calls; raw and optimized actual handlers match')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--negative', action='store_true')
    main(parser.parse_args().negative)
