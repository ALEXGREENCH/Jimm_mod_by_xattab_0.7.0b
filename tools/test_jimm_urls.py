#!/usr/bin/env python3
"""Compare the real May URL controller in the prepared login/date fixtures."""
import json
import os
import argparse
import audit_source as audit
import recover
from test_source import ROOT, TEST, CACHE, run


def prepare():
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in
           ['JimmUrlIO', 'JimmUrlFixture', 'JimmUrlProbe']]], 'compile-jimm-urls')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7',
         '-encoding', 'UTF-8', '-d', TEST, ROOT / 'tools/source/JimmUrlIO.java'],
        'compile-jimm-url-io')
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-jimm-url-dump')
    return runtime, cp


def dump(path, owner, cp):
    data = json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]),
                                  'BytecodeDump', path], capture=True))
    return next(c for c in data if c['name'] == owner)


def method(owner, name, desc):
    found = [m for m in owner['methods'] if (m['name'], m['desc']) == (name, desc)]
    assert len(found) == 1, (owner['name'], name, desc)
    return found[0]


def execute(runtime, cp, bases=None):
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    classes = ROOT / 'build/source/MIDP2-RU/classes.jar'
    outputs, proofs = [], []
    for mode in ['reference', 'source']:
        owner, name = ('cf', 'a') if mode == 'reference' else ('jimm/JimmUI', 'gotoURL')
        base = bases[mode] if bases else TEST / ('clipboard-' + mode + '.jar')
        untouched = dump(original if mode == 'reference' else classes, owner, cp)
        prepared = dump(base, owner, cp)
        # The existing fixtures must retain the entire controller class.
        assert untouched == prepared, ('Prepared fixture changed JimmUI', mode)
        fixture, output = TEST / ('jimm-url-' + mode + '.jar'), TEST / ('jimm-url-' + mode + '.txt')
        run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']),
             'JimmUrlFixture', base, fixture, mode, TEST], 'jimm-url-fixture-' + mode)
        instrumented = dump(fixture, owner, cp)
        assert {k: v for k, v in untouched.items() if k != 'methods'} == {
            k: v for k, v in instrumented.items() if k != 'methods'}, ('Changed controller declarations', mode)
        captured = 0
        for before, after in zip(untouched['methods'], instrumented['methods']):
            assert (before['name'], before['desc'], before['access'], before['handlers']) == (
                after['name'], after['desc'], after['access'], after['handlers'])
            expected = []
            for instruction in before['code']:
                if instruction in ['182 jimm/Jimm.platformRequest(Ljava/lang/String;)Z',
                                   '182 javax/microedition/midlet/MIDlet.platformRequest(Ljava/lang/String;)Z']:
                    instruction = '184 JimmUrlIO.request(Ljava/lang/Object;Ljava/lang/String;)Z'
                    captured += 1
                expected.append(instruction)
            assert expected == after['code'], ('Unexpected controller rewrite', mode, before['name'], before['desc'])
        assert len(untouched['methods']) == len(instrumented['methods']) and captured >= 2
        goto = method(untouched, name, '(Ljava/lang/String;Ljava/lang/Object;)V')
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'JimmUrlProbe', fixture, mode, output], 'jimm-url-' + mode)
        outputs.append(output.read_bytes())
        proofs.append({'mode': mode, 'base_fixture_sha256': recover.sha(base),
                       'fixture_sha256': recover.sha(fixture), 'terminal_call_sites': captured,
                       'unchanged_controller_before_capture': True,
                       'only_terminal_invocations_changed': True,
                       'goto_url_instructions': len(goto['code']),
                       'goto_url_sha256': audit.digest(goto['code'])})
    return outputs, proofs


def main(prepared=False):
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    if not prepared:
        current = json.loads((ROOT / 'preservation/reports/source-tests.json').read_text(encoding='utf-8'))
        assert current['source_jar_sha256'] == recover.sha(artifact), 'Run test_source.py for the current build first'
    runtime, cp = prepare()
    outputs, proofs = execute(runtime, cp)
    if outputs[0] != outputs[1]:
        raise AssertionError('URL controller mismatch: compare build/source-tests/jimm-url-{reference,source}.txt')
    lines = outputs[0].decode('utf-8').splitlines()
    cases, lists, requests, fragments, commands, rasters = map(int, lines[-1].split(':')[1:])
    assert (cases, lists, requests, commands, rasters) == (1056, 648, 480, 576, 648)
    exception_rows = [line for line in lines if ':result=exception:java.lang.AssertionError:' in line]
    assert len(exception_rows) == 120
    report = {'scope': 'Actual MIDP2-RU JimmUI.gotoURL and the URL-list commandAction route, real Util URL parsing, '
                       'TextList wrapping, tagged selection, text/font metrics, command labels and screen transitions. '
                       'All eight font bit masks, three contact-font option values and four terminal results are exercised. '
                       'The URL list retains its native fixed small font; 176x220 MicroEmulator pixels are compared. '
                       'Only JimmUI platformRequest calls are captured; an Exception is swallowed and an Error escapes '
                       'as in the original. Existing login/date fixtures provide inert surrounding initialization. '
                       'Whole controller bytecode is verified unchanged before capture; only terminal call instructions '
                       'change afterward. No physical browser launch, device rendering or complete controller equivalence claim.',
              'reference_sha256': recover.sha(ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'),
              'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(ROOT / 'build/source/MIDP2-RU/classes.jar'),
              'observations': len(lines), 'goto_url_calls': cases, 'list_snapshots': lists,
              'terminal_requests': requests, 'text_fragments': fragments, 'command_calls': commands, 'raster_frames': rasters,
              'escaping_terminal_errors': len(exception_rows),
              'differences': 0, 'fixture_proofs': proofs}
    (ROOT / 'preservation/reports/source-jimm-urls.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS URL controller:', len(lines), 'observations,', cases, 'openings,', commands, 'commands')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--prepared', action='store_true', help='Use freshly prepared fixtures during test_source.py')
    main(parser.parse_args().prepared)
