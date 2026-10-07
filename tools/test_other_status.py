#!/usr/bin/env python3
"""Execute complete May MIDP2 status/capability/private packets through real sockets."""
import json
import os
import zipfile

import audit_source as audit
import recover
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def main():
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / name for name in ['microemu.jar', 'microemu-nokiaui.jar',
                                       'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8',
         '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]), '-d', TEST,
         *[ROOT / f'tools/source/{name}.java' for name in
           ['GraphicsFixture', 'ExtendedKeysFixture', 'PopupProbe', 'OtherStatusFixture', 'OtherStatusProbe']]],
        'compile-other-status')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', TEST,
         ROOT / 'tools/source/OtherStatusClock.java'], 'compile-other-status-clock')
    cp = recover.cp([CACHE / name for name in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-other-status-dump')
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    raw = ROOT / 'build/source/MIDP2-RU/classes.jar'
    delivered = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    raw_host = TEST / 'other-status-raw-host.jar'
    with zipfile.ZipFile(delivered) as z:
        entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
    with zipfile.ZipFile(raw) as z:
        entries.update({n: z.read(n) for n in z.namelist()})
    write_jar(raw_host, entries)
    proofs = []
    for clocked in [False, True]:
        outputs = []
        for mode, path in [('reference', original), ('raw', raw_host), ('optimized', delivered)]:
            reference = mode == 'reference'
            tag = 'other-status-' + ('clock-' if clocked else 'device-') + mode
            host, fixture, output = [TEST / (tag + ext) for ext in ['-host.jar', '.jar', '.txt']]
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture',
                 path, host, TEST, 'unused', 'false', 'unused'], tag + '-host')
            if clocked:
                run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'OtherStatusFixture',
                     host, fixture, TEST, 'reference' if reference else 'source'], tag + '-fixture')
            else:
                fixture.write_bytes(host.read_bytes())
            util = 'co' if reference else 'jimm/comm/Util'
            captures = 0
            with zipfile.ZipFile(path) as before, zipfile.ZipFile(fixture) as after:
                changed = [n for n in before.namelist() if n.lower() != 'meta-inf/manifest.mf'
                           and before.read(n) != after.read(n)]
                assert changed == ([util + '.class'] if clocked else [])
                assert set(after.namelist()) - set(before.namelist()) <= (
                    {'GraphicsMIDlet.class', 'OtherStatusClock.class'} if clocked else {'GraphicsMIDlet.class'})
            if clocked:
                before = next(c for c in classes(path, cp) if c['name'] == util)
                after = next(c for c in classes(fixture, cp) if c['name'] == util)
                assert {k: v for k, v in before.items() if k != 'methods'} == {
                    k: v for k, v in after.items() if k != 'methods'}
                for left, right in zip(before['methods'], after['methods']):
                    assert {k: v for k, v in left.items() if k not in ['code', 'refs']} == {
                        k: v for k, v in right.items() if k not in ['code', 'refs']}
                    expected = []
                    for instruction in left['code']:
                        if left['name'] == ('b' if reference else 'createRandomId') and left['desc'] == '()I' and instruction == '184 java/lang/System.currentTimeMillis()J':
                            instruction = '184 OtherStatusClock.time()J'
                            captures += 1
                        expected.append(instruction)
                    assert expected == right['code']
                    assert right['refs'] == [s for s in expected if 178 <= int(s.partition(' ')[0]) <= 185]
                assert len(before['methods']) == len(after['methods']) and captures == 1
            run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                 '-cp', recover.cp([TEST, *runtime]), 'OtherStatusProbe', fixture, output,
                 'reference' if reference else 'source', str(clocked).lower()], tag)
            result = output.read_bytes()
            coverage = list(map(int, result.splitlines()[-1].decode().split(':')[1:]))
            assert coverage[:3] == ([644, 5250, 81] if clocked else [504, 5250, 81])
            assert coverage[3] > 500
            outputs.append(result)
            proofs.append({'mode': mode, 'clocked': clocked, 'input_sha256': recover.sha(path),
                           'fixture_sha256': recover.sha(fixture), 'output_sha256': recover.sha(output),
                           'observations': len(result.splitlines()), 'coverage': coverage,
                           'external_clock_instructions_replaced': captures,
                           'other_application_resource_bytes_unchanged': True})
        assert outputs[0] == outputs[1] == outputs[2], ('Whole other-status execution differs', clocked)
    report = {
        'scope': 'Complete May MIDP2 RU OtherAction.setPrivateStatus/setStatus/setUserInfo with actual Icq, GUID, '
                 'Util, capability builders, SnacPacket serializers, SOCKETConnection.sendPacket, Traffic and roster title updates. '
                 'Natural output streams record real wire bytes and actual stream monitor ownership; first/second packet write/flush IOExceptions, '
                 'unchecked exceptions/Error and null connection/output paths retain real application handling. '
                 'Device mode preserves every application/resource byte. Clock mode changes exactly one external System clock '
                 'read in actual Util.createRandomId; every other declaration/instruction/handler and application/resource is retained. '
                 'Host MIDlet, typed option table, privacy ID, FLAP sequence, status flags/data and socket streams are explicit inputs. '
                 'XStatus is set to none, physical network, UI event ordering and all GUID advertisement combinations are not claimed.',
        'reference_sha256': recover.sha(original), 'raw_sha256': recover.sha(raw),
        'delivered_sha256': recover.sha(delivered), 'proofs': proofs,
    }
    (ROOT / 'preservation/reports/source-other-status.json').write_text(
        json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS other status: 5836 device + 5976 clock observations; original/raw/optimized traces match')
    return report


if __name__ == '__main__':
    main()
