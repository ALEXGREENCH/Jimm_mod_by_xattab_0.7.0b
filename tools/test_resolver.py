#!/usr/bin/env python3
"""Execute complete SOCKS ResolveIP with one external call captured on all platforms."""
import argparse
import json
import os
from pathlib import Path
import zipfile

import audit_source as audit
import recover
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


DESC = '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;'


def main(before=None):
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, CACHE / 'asm.jar', *runtime]),
         '-d', TEST, *[ROOT / f'tools/source/{n}.java' for n in
                      ['GraphicsFixture', 'ExtendedKeysFixture', 'ResolverFixture', 'ResolverProbe']]], 'compile-resolver')
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-cp', recover.cp(runtime), '-d', TEST,
         ROOT / 'tools/source/ResolverIO.java'], 'compile-resolver-io')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
         ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-resolver-dump')
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'
        raw = (before / target / 'classes.jar') if before else ROOT / f'build/source/{target}-RU/classes.jar'
        delivered = (before / target / 'delivered.jar') if before else ROOT / f'dist/source/Jimm-{target}-RU.jar'
        tag = 'resolver-' + ('before-' if before else '') + target
        raw_host = TEST / (tag + '-raw-resource.jar')
        with zipfile.ZipFile(delivered) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(raw_host, entries)
        outputs, proofs, bodies = [], [], []
        for mode, path in [('reference', original), ('raw', raw_host), ('optimized', delivered)]:
            data = {c['name']: c for c in classes(path, cp)}
            if mode == 'reference':
                candidates = [(c['name'], m['name']) for c in data.values() for m in c['methods']
                              if m['desc'] == DESC and m['access'] & 40 == 40 and '0.0.0.0' in m['strings']]
                assert len(candidates) == 1, candidates
                owner, name = candidates[0]
            else: owner, name = 'jimm/comm/Icq$SOCKSConnection', 'ResolveIP'
            host, fixture, output = [TEST / (tag + '-' + mode + ext) for ext in ['-host.jar', '.jar', '.txt']]
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ExtendedKeysFixture',
                 path, host, TEST, 'unused', 'false', 'unused'], tag + '-' + mode + '-host')
            run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'ResolverFixture',
                 host, fixture, TEST, owner, name], tag + '-' + mode + '-fixture')
            with zipfile.ZipFile(path) as a, zipfile.ZipFile(fixture) as b:
                changed = [n for n in a.namelist() if n.lower() != 'meta-inf/manifest.mf' and a.read(n) != b.read(n)]
                assert changed == [owner + '.class'], changed
                assert set(b.namelist()) - set(a.namelist()) == {
                    'GraphicsMIDlet.class', 'ResolverIO.class', 'ResolverIO$Endpoint.class', 'ResolverIO$1.class'}
            left = data[owner]; right = next(c for c in classes(fixture, cp) if c['name'] == owner)
            assert {k: v for k, v in left.items() if k != 'methods'} == {k: v for k, v in right.items() if k != 'methods'}
            captures = 0
            for a, b in zip(left['methods'], right['methods']):
                assert {k: v for k, v in a.items() if k not in ['code', 'refs']} == {
                    k: v for k, v in b.items() if k not in ['code', 'refs']}
                expected = []
                for instruction in a['code']:
                    if a['name'] == name and a['desc'] == DESC and instruction == '184 javax/microedition/io/Connector.open(Ljava/lang/String;I)Ljavax/microedition/io/Connection;':
                        instruction = '184 ResolverIO.open(Ljava/lang/String;I)Ljavax/microedition/io/Connection;'; captures += 1
                    expected.append(instruction)
                assert expected == b['code']
                assert b['refs'] == [s for s in expected if 178 <= int(s.partition(' ')[0]) <= 185]
            assert len(left['methods']) == len(right['methods']) and captures == 1
            method = next(m for m in left['methods'] if (m['name'], m['desc']) == (name, DESC))
            bodies.append({'mode': mode, 'owner': owner, 'complete_method': method})
            run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                 '-cp', recover.cp([TEST, *runtime]), 'ResolverProbe', fixture, output, owner, name], tag + '-' + mode)
            result = output.read_bytes(); outputs.append(result)
            assert len(result.splitlines()) == 1442
            proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'observations': 1442,
                           'external_connector_instructions_replaced': 1,
                           'other_application_resource_bytes_unchanged': True})
        differences = [sum(a != b for a, b in zip(outputs[0].splitlines(), other.splitlines())) for other in outputs[1:]]
        if not before: assert differences == [0, 0], (target, differences)
        builds.append({'target': target, 'reference_sha256': recover.sha(original), 'raw_sha256': recover.sha(raw),
                       'delivered_sha256': recover.sha(delivered), 'proofs': proofs, 'whole_resolver_bodies': bodies,
                       'raw_optimized_differences': differences})
    report = {'scope': 'Complete three-platform May RU SOCKS ResolveIP with actual Util.isIP. Exactly one external Connector.open '
                       'instruction per artifact is captured; every other declaration, instruction, handler, class and resource stays unchanged. '
                       'Natural SocketConnection callbacks observe actual class and receiver monitors during open/getAddress/close. '
                       'Twelve hosts, five ports, twelve faults and null/non-null addresses cover early returns, exceptions/Error and cleanup; '
                       'two real threads test class-lock contention on literal-IP and resolving branches. The historical static synchronized '
                       'method and the source explicit class monitor retain their distinct declarations. Physical DNS/network, full SOCKS '
                       'handshake and whole-program equivalence are outside this test.', 'builds': builds}
    report_path = (before / 'negative-evidence.json') if before else ROOT / 'preservation/reports/source-resolver.json'
    report_path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(('BEFORE' if before else 'PASS') + ' resolver: 1442 observations per platform; differences=' + str([b['raw_optimized_differences'] for b in builds]))
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(); parser.add_argument('--before', type=Path)
    args = parser.parse_args(); main(args.before)
