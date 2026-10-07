#!/usr/bin/env python3
"""Compare actual offset-zero FLAP dispatch, including the optimized combined parser."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from test_packet_parsers import configuration
from test_source import ROOT, TEST, CACHE, run


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    helpers = TEST / 'packet-root-helpers'; helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', CACHE / 'asm.jar', '-d', helpers,
         *[ROOT / f'tools/source/{n}.java' for n in ['ResourceIO', 'ResourceFixture', 'PacketProbe', 'PacketRootProbe']]], 'compile-packet-root')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-packet-root-dump')
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        raw = ROOT / f'build/source/{target}-RU/classes.jar'
        source_spec, _ = configuration(target, False, classes(raw, cp))
        outputs, proofs = [], []
        for mode, path in [('reference', ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'),
                           ('raw', raw), ('optimized', ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp); owners = {c['name']: c for c in data}
            if mode == 'reference': spec, _ = configuration(target, True, data)
            else: spec = dict(source_spec)
            packet = owners[spec['Packet']]
            desc = ('([BI)L' if mode == 'optimized' else '([BII)L') + packet['name'] + ';'
            candidates = [m for m in packet['methods'] if m['desc'] == desc and m['access'] & 8]
            assert len(candidates) == 1, (target, mode, candidates)
            root = candidates[0]; spec.update(rootName=root['name'], rootDesc=root['desc'])
            call = '184 ' + packet['name'] + '.' + root['name'] + root['desc']
            callers = [{'owner': c['name'], 'complete_method': m, 'positions': [i for i, s in enumerate(m['code']) if s == call]}
                       for c in data for m in c['methods'] if call in m['code']]
            assert callers
            if mode != 'optimized':
                assert all(c['complete_method']['code'][i - 3] == '3' for c in callers for i in c['positions']), 'All actual offset arguments must be zero'
            tag = f'packet-root-{target}-{mode}'; settings = TEST / (tag + '.properties')
            settings.write_text(''.join(k + '=' + (v if k == 'rootDesc' else v.replace('/', '.')) + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            fixture, output = TEST / (tag + '.jar'), TEST / (tag + '.txt')
            run([recover.java(), '-cp', recover.cp([helpers, CACHE / 'asm.jar']), 'ResourceFixture', path, fixture, 'reference' if mode == 'reference' else 'source', helpers, spec['resource']], tag + '-fixture')
            with zipfile.ZipFile(fixture) as z:
                assert set(z.namelist()) == {spec['resource'] + '.class', 'ResourceIO.class', 'ResourceIO$Bytes.class'}
                assert len(z.namelist()) == len(set(z.namelist()))
            before = owners[spec['resource']]; expected = json.loads(json.dumps(before)); captures = 0
            for m in expected['methods']:
                resource_call = '182 java/lang/Class.getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;'; captures += m['code'].count(resource_call)
                m['code'] = ['184 ResourceIO.open(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;' if s == resource_call else s for s in m['code']]
                m['refs'] = [s for s in m['code'] if 178 <= int(s.partition(' ')[0]) <= 185]
            after = next(c for c in classes(fixture, cp) if c['name'] == spec['resource'])
            assert captures > 0 and expected == after
            result = run([recover.java(), '-cp', helpers, 'PacketRootProbe', fixture, path, 'reference' if mode == 'reference' else 'source', CACHE, output, settings], tag)
            content = output.read_bytes(); outputs.append(content)
            coverage = list(map(int, content.splitlines()[-1].decode().split(':')[1:]))
            assert coverage == [31710, 16176] and len(content.splitlines()) == 34929
            proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'configuration': spec, 'observations': len(content.splitlines()),
                           'parser_cases': coverage[0], 'successful_parses': coverage[1], 'complete_parser': root,
                           'whole_actual_callers': callers, 'captured_external_resource_calls': captures,
                           'resource_loader_before': before, 'resource_loader_after': after,
                           'subject_class_bytes_loaded_directly_from_unchanged_input_jar': True})
            print(target, mode, result, flush=True)
        assert outputs[0] == outputs[1] == outputs[2], ('Actual complete FLAP dispatch differs', target)
        builds.append({'target': target, 'proofs': proofs, 'different_observations': 0})
    report = {'scope': 'Actual complete FLAP Packet.parse executes on three May RU native/raw/optimized versions. Every '
                       'native/raw actual caller passes offset zero; full caller bodies and positions remain explicit. '
                       'Modern optimized Packet.parse has a two-argument offset-zero ABI and inlines all six subclass '
                       'parsers. The real delivered parser and field/serializer bodies are loaded unchanged, rather '
                       'than inventing matching optimized helper entry points. Synthetic error dictionaries capture '
                       'only Class.getResourceAsStream in the real ResourceBundle; every other dumped loader '
                       'instruction/handler/declaration remains, and subject/dependency bytes come directly from '
                       'the unchanged second classpath JAR. FLAP/TLV/ICQ seed truncations, declared/supplied lengths, '
                       'all 256 channel/prefix bytes, null and overflow lengths, extended-data paths and 8000 seeded '
                       'mutations per CP1251 mode compare actual fields, wire bytes, exceptions and input-buffer '
                       'mutation effects. Generic raw nonzero offsets remain covered by source-packet-parsers.json; '
                       'no equivalent optimized nonzero-offset ABI is invented. Physical networking/device resources '
                       'and whole-program equivalence are outside scope.', 'builds': builds}
    (ROOT / 'preservation/reports/source-packet-root.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS packet root: zero native/raw/optimized differences on three platforms')
    return report


if __name__ == '__main__': main()
