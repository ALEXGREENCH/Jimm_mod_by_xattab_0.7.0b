#!/usr/bin/env python3
"""Exercise actual JimmUI.sendMessage and Icq.beginTyping in the login/date host."""
import argparse
import json
import os
import zipfile
import audit_source as audit
import recover
from test_source import ROOT, TEST, CACHE, run
from test_jimm_urls import dump, method


def prepare():
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                  'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / ('tools/source/' + n + '.java') for n in
           ['SendTextIO', 'SendTextFixture', 'SendTextProbe']]], 'compile-send-text')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST,
         ROOT / 'tools/source/SendTextIO.java'], 'compile-send-text-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-send-text-dump')
    return runtime, cp


def capture_map(ref):
    return {
        ('r.a(Laa;)V' if ref else 'jimm/comm/Icq.requestAction(Ljimm/comm/Action;)V'):
            'SendTextIO.request(Ljava/lang/Object;)V',
        ('bv.a(Lbv;)V' if ref else 'jimm/JimmException.handleException(Ljimm/JimmException;)V'):
            'SendTextIO.error(Ljava/lang/Object;)V',
        ('bt.a(Lz;Ljava/lang/String;JLjava/lang/String;J)V' if ref else
         'jimm/ChatHistory.addMyMessage(Ljimm/ContactItem;Ljava/lang/String;JLjava/lang/String;J)V'):
            'SendTextIO.chat(Ljava/lang/Object;Ljava/lang/String;JLjava/lang/String;J)V',
        ('q.a(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V' if ref else
         'jimm/HistoryStorage.addText(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V'):
            'SendTextIO.history(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V',
        'java/lang/Thread.sleep(J)V': 'SendTextIO.sleep(J)V',
        ('aj.a(Z)V' if ref else 'DrawControls/LightControl.flash(Z)V'): 'SendTextIO.light(Z)V',
    }


def execute(runtime, cp, mode, base, untouched):
    ref = mode == 'reference'
    ui, sender, icq = ('cf', 'cl', 'r') if ref else ('jimm/JimmUI', 'jimm/comm/SendMessageAction', 'jimm/comm/Icq')
    originals = {owner: dump(untouched, owner, cp) for owner in [ui, sender, icq]}
    assert dump(base, ui, cp) == originals[ui], ('Host changed JimmUI', mode)
    typing = method(originals[icq], 'a' if ref else 'beginTyping', '(Ljava/lang/String;Z)V')
    assert method(dump(base, icq, cp), typing['name'], typing['desc']) == typing, ('Host changed typing', mode)
    assert dump(base, sender, cp) == originals[sender], ('Host changed SendMessageAction', mode)
    util = 'co' if ref else 'jimm/comm/Util'
    base_util = dump(base, util, cp)
    date = method(base_util, 'a' if ref else 'createCurrentDate', '(ZZ)J')
    assert date == method(dump(untouched, util, cp), date['name'], date['desc']), ('Host changed current-date conversion', mode)
    fixture, output = TEST / ('send-text-' + mode + '.jar'), TEST / ('send-text-' + mode + '.txt')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SendTextFixture',
         base, fixture, 'reference' if ref else 'source', TEST], 'send-text-fixture-' + mode)
    mapping, captures = capture_map(ref), {}
    for owner in [ui, sender]:
        before, after = originals[owner], dump(fixture, owner, cp)
        assert {k: v for k, v in before.items() if k != 'methods'} == {k: v for k, v in after.items() if k != 'methods'}
        assert len(before['methods']) == len(after['methods'])
        for left, right in zip(before['methods'], after['methods']):
            # refs is derived from field and call instructions, which are checked below.
            assert {k: v for k, v in left.items() if k not in ('code', 'refs')} == {
                k: v for k, v in right.items() if k not in ('code', 'refs')}
            expected = []
            for instruction in left['code']:
                opcode, _, operand = instruction.partition(' ')
                dest = None
                if owner == ui and left['name'] == ('a' if ref else 'sendMessage') and left['desc'] == ('(Ljava/lang/String;Lz;)V' if ref else '(Ljava/lang/String;Ljimm/ContactItem;)V'):
                    dest = mapping.get(operand) if opcode == '184' else None
                if owner == sender and instruction == '184 java/lang/System.currentTimeMillis()J':
                    dest = 'LoginIO.time()J'
                if dest:
                    captures[dest] = captures.get(dest, 0) + 1
                    instruction = '184 ' + dest
                expected.append(instruction)
            assert expected == right['code'], ('Unexpected rewrite', owner, left['name'], left['desc'])
            assert right['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
    assert method(dump(fixture, icq, cp), typing['name'], typing['desc']) == typing
    captured_util = dump(fixture, util, cp)
    dated = method(captured_util, date['name'], date['desc'])
    assert {k: v for k, v in base_util.items() if k != 'methods'} == {k: v for k, v in captured_util.items() if k != 'methods'}
    assert [m for m in base_util['methods'] if (m['name'], m['desc']) != (date['name'], date['desc'])] == [
        m for m in captured_util['methods'] if (m['name'], m['desc']) != (date['name'], date['desc'])]
    cuts = [i for i, s in enumerate(date['code']) if s == '183 java/util/Date.<init>()V']
    assert len(cuts) == 1
    cut = cuts[0]
    def shifted(position):
        assert position != cut, 'No branch/exception boundary may skip the inserted Date argument'
        return position + (position > cut)
    expected = []
    for i, instruction in enumerate(date['code']):
        opcode, _, operand = instruction.partition(' ')
        if i == cut:
            expected.extend(['184 LoginIO.time()J', '183 java/util/Date.<init>(J)V'])
            continue
        if int(opcode) in [*range(153, 169), 198, 199]:
            instruction = opcode + ' ' + str(shifted(int(operand)))
        assert int(opcode) not in (170, 171), 'Unexpected switch in date conversion'
        expected.append(instruction)
    assert dated['code'] == expected
    assert dated['refs'] == [s for s in expected if 178 <= int(s.split(' ', 1)[0]) <= 185]
    assert dated['handlers'] == [' '.join([*(str(shifted(int(x))) for x in h.split()[:3]), h.split()[3]]) for h in date['handlers']]
    assert (date['access'], date['name'], date['desc'], date['strings']) == (dated['access'], dated['name'], dated['desc'], dated['strings'])
    run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
         '-cp', recover.cp([TEST, *runtime]), 'SendTextProbe', fixture,
         'reference' if ref else 'source', output], 'send-text-' + mode)
    return output.read_bytes(), {'mode': mode, 'base_sha256': recover.sha(base), 'fixture_sha256': recover.sha(fixture),
                                'unchanged_controller_before_capture': True, 'only_checked_call_instructions_changed': True,
                                'typing_body_unchanged': True, 'only_default_date_clock_changed': True, 'captures': captures}


def main(prepared=False):
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    classes = ROOT / 'build/source/MIDP2-RU/classes.jar'
    if not prepared:
        report = json.loads((ROOT / 'preservation/reports/source-tests.json').read_text(encoding='utf-8'))
        assert report['source_jar_sha256'] == recover.sha(artifact), 'Run test_source.py for this build first'
    runtime, cp = prepare()
    outputs, proofs = [], []
    for mode, unmodified in [('reference', original), ('source', classes)]:
        output, proof = execute(runtime, cp, mode, TEST / ('clipboard-' + mode + '.jar'), unmodified)
        outputs.append(output); proofs.append(proof)
    if outputs[0] != outputs[1]:
        raise AssertionError('Sender/typing mismatch: compare build/source-tests/send-text-{reference,source}.txt')
    # Exercise the delivered optimized application too, with the same host boundaries.
    login, host = TEST / 'send-text-optimized-login.jar', TEST / 'send-text-optimized-host.jar'
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'LoginFixture', artifact,
         login, 'source', TEST], 'send-text-optimized-login')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SplashFixture', login,
         host, 'source'], 'send-text-optimized-host')
    output, proof = execute(runtime, cp, 'optimized-source', host, artifact)
    assert output == outputs[0], 'Delivered optimized sender/typing differs'
    proofs.append(proof)
    lines = outputs[0].decode('utf-8').splitlines()
    counts = list(map(int, lines[-1].split(':')[1:]))
    assert counts[0] == 316 and counts[-3:] == [300, 96, 2], counts
    report = {'scope': 'Actual MIDP2-RU JimmUI.sendMessage orchestration, real PlainMessage/SendMessageAction constructors, '
                       'ID counter and date conversion, and untouched Icq.beginTyping with real SNAC serialization. '
                       'Queue, chat/history, error delivery, sleep and light call sites are captured with scripted exceptions. '
                       'Typing uses the actual LoginConnection boundary and two real class-monitor contention cases. '
                       'Unoptimized authored and delivered optimized applications must both match May; controller instructions '
                       'and handlers are verified unchanged except the enumerated calls, action ID clock and the single '
                       'default Date clock in real Calendar conversion. Helpful-NPE strings containing JVM/compiler names '
                       'are excluded; NPE classes and partial state are compared. '
                       'No live queue execution, physical RMS/history, backlight, waiting or network claim.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(classes), 'observations': len(lines),
              'optimized_observations': len(output.splitlines()), 'send_calls': counts[0], 'queue_attempts': counts[1],
              'chat_appends': counts[2], 'history_appends': counts[3], 'sleep_calls': counts[4], 'light_calls': counts[5],
              'typing_calls': counts[6], 'typing_packets': counts[7], 'class_monitor_cases': counts[8],
              'differences': 0, 'fixture_proofs': proofs}
    (ROOT / 'preservation/reports/source-send-text.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS sender/typing:', len(lines), 'observations, 316 sends, 300 typing calls, 2 class monitors; delivered optimized application matches')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--prepared', action='store_true')
    main(parser.parse_args().prepared)
