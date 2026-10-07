#!/usr/bin/env python3
"""Execute all seven real packet parsers and inherited serializers on three RU platforms."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from test_roster_keys import configuration as keys_configuration
from test_source import ROOT, TEST, CACHE, run


NAMES = ['Packet', 'SnacPacket', 'ConnectPacket', 'DisconnectPacket', 'ToIcqSrvPacket', 'FromIcqSrvPacket', 'DCPacket']


def configuration(target, reference, data):
    owners = {c['name']: c for c in data}
    keys, _, _ = keys_configuration(target, reference, data)
    if reference:
        bases = [c for c in data if c['super'] == 'java/lang/Object' and any(m['desc'] == '([BII)L' + c['name'] + ';' for m in c['methods'])]
        assert len(bases) == 1
        base = bases[0]; desc = '([BII)L' + base['name'] + ';'
        candidates = [c for c in data if any(m['desc'] == desc for m in c['methods'])]
        assert len(candidates) == 7
        def pick(predicate):
            result = [c for c in candidates if predicate(c)]; assert len(result) == 1, result
            return result[0]
        snac = pick(lambda c: c['super'] == base['name'] and any(m['desc'] == '(IIIIJ[B[B)V' for m in c['methods']))
        connect = pick(lambda c: any(m['desc'] == '(ILjava/lang/String;Ljava/lang/String;)V' for m in c['methods']))
        disconnect = pick(lambda c: any(m['desc'] == '(IILjava/lang/String;)V' for m in c['methods']))
        direct = pick(lambda c: c['super'] == base['name'] and len(c['fields']) == 1 and c['fields'][0]['desc'] == '[B')
        to = pick(lambda c: any(m['name'] == '<init>' and m['desc'] == '(IJIILjava/lang/String;I[B[B)V' and m['code'][3] == '5' for m in c['methods']))
        incoming = pick(lambda c: any(m['name'] == '<init>' and m['desc'] == '(IJIILjava/lang/String;I[B[B)V' and m['code'][3] == '6' for m in c['methods']))
        types = [base, snac, connect, disconnect, to, incoming, direct]
        parse = next(m for m in base['methods'] if m['desc'] == desc)
        errors = {s.split(' ')[1] for s in parse['code'] if s.startswith('187 ') and owners[s.split(' ')[1]]['super'] == 'java/lang/Exception'}
        assert len(errors) == 1
        resource = [c for c in data if sorted(f['desc'] for f in c['fields']) == sorted(['[Ljava/lang/String;', 'Ljava/lang/String;', 'Ljava/util/Hashtable;'])
                    and any('182 java/lang/Class.getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;' in m['code'] for m in c['methods'])]
        assert len(resource) == 1
        spec = dict(zip(NAMES, [c['name'] for c in types])); spec.update(options=keys['options'], error=next(iter(errors)), resource=resource[0]['name'])
    else:
        spec = {name: 'jimm/comm/' + name for name in NAMES}; spec.update(options='jimm/Options', error='jimm/JimmException', resource='jimm/util/ResourceBundle')
        types = [owners[spec[name]] for name in NAMES]
    desc = '([BII)L' + spec['Packet'] + ';'
    # Bind every selected name to the complete actual declaration/body before reflection.
    parsers = []
    for role, c in zip(NAMES, types):
        matches = [m for m in c['methods'] if m['desc'] == desc and m['access'] & 8]
        assert len(matches) == 1
        parsers.append({'role': role, 'owner': c['name'], 'complete_parser': matches[0], 'fields': c['fields'],
                        'whole_constructors': [m for m in c['methods'] if m['name'] == '<init>']})
    return spec, parsers


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    helpers = TEST / 'packets-helpers'; helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', CACHE / 'asm.jar', '-d', helpers,
         *[ROOT / f'tools/source/{n}.java' for n in ['ResourceIO', 'ResourceFixture', 'PacketProbe']]], 'compile-packet-parsers')
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-packet-parsers-dump')
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        outputs, proofs = [], []
        for mode, path in [('reference', ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'),
                           ('source', ROOT / f'build/source/{target}-RU/classes.jar')]:
            data = classes(path, cp); spec, parsers = configuration(target, mode == 'reference', data)
            tag = f'packet-parsers-{target}-{mode}'
            settings = TEST / (tag + '.properties'); settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            fixture, output = TEST / (tag + '.jar'), TEST / (tag + '.txt')
            run([recover.java(), '-cp', recover.cp([helpers, CACHE / 'asm.jar']), 'ResourceFixture', path, fixture, mode, helpers, spec['resource']], tag + '-fixture')
            with zipfile.ZipFile(fixture) as z:
                assert set(z.namelist()) == {spec['resource'] + '.class', 'ResourceIO.class', 'ResourceIO$Bytes.class'}
                assert len(z.namelist()) == len(set(z.namelist()))
            # All other classes/resources are loaded directly from the unchanged second classpath JAR.
            before = next(c for c in data if c['name'] == spec['resource'])
            expected = json.loads(json.dumps(before)); captures = 0
            for m in expected['methods']:
                call = '182 java/lang/Class.getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;'
                captures += m['code'].count(call)
                m['code'] = ['184 ResourceIO.open(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;' if s == call else s for s in m['code']]
                m['refs'] = [s for s in m['code'] if 178 <= int(s.partition(' ')[0]) <= 185]
            after = next(c for c in classes(fixture, cp) if c['name'] == spec['resource'])
            assert captures > 0 and expected == after, 'Non-enumerated resource loader changes'
            result = run([recover.java(), '-cp', helpers, 'PacketProbe', fixture, path, mode, CACHE, output, settings], tag)
            content = output.read_bytes(); outputs.append(content)
            assert len(content.splitlines()) == 46875 and content.splitlines()[-1] == b'successful-parses:15642'
            old_rows = [s for s in content.splitlines() if not s.startswith(b'direct')]
            if target == 'MIDP2':
                legacy = TEST / ('packets-' + mode + '.txt')
                assert legacy.read_bytes().splitlines() == old_rows, 'Existing 45331 observations changed'
            proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'fixture_sha256': recover.sha(fixture),
                           'output_sha256': recover.sha(output), 'configuration': spec, 'observations': 46875,
                           'retained_packet_observations': len(old_rows), 'new_direct_observations': 1544,
                           'successful_original_six_parser_observations': 15642, 'direct_parser_cases': 534,
                           'captured_external_resource_calls': captures, 'whole_parsers_and_constructors': parsers,
                           'resource_loader_before': before, 'resource_loader_after': after,
                           'subject_class_bytes_loaded_directly_from_unchanged_input_jar': True})
            print(target, mode, result, flush=True)
        assert outputs[0] == outputs[1], ('Whole packet/parser output differs', target)
        builds.append({'target': target, 'proofs': proofs, 'different_observations': 0})
    report = {'scope': 'Seven complete May RU Packet/Snac/Connect/Disconnect/ToIcqSrv/FromIcqSrv/DC parsers, constructors, '
                       'fields, serializers and aliases execute on native/raw classes of all three platforms. Six-parser '
                       '45331 observations remain, with 1544 additional direct-parser observations: unused offset/length, '
                       'null buffers, actual input-array alias, copied serialized output and 65535/65536/65537 lengths. '
                       'Original ToIcqSrv constructors retain specialized constant arguments supplied at raw reflection '
                       'boundaries; source generic constructors stay intact. Actual declarations and bodies are bound '
                       'before reflection. Only Class.getResourceAsStream instructions in ResourceBundle change; all '
                       'other dumped resource-loader instructions/declarations/handlers remain. The fixture contains '
                       'that loader and two ResourceIO helpers; all subject/dependency bytes are loaded directly from '
                       'the original unchanged second JAR. Error strings use an explicit synthetic dictionary, not '
                       'a physical resource/device. Six modern subclass parser entry points are inlined/removed; '
                       'the combined root parser has a different two-argument ABI. Matching generic optimized '
                       'helper entry points are not invented or executed here. Network/server compatibility, optimized dispatch execution '
                       'and whole-program equivalence are outside scope.', 'builds': builds}
    (ROOT / 'preservation/reports/source-packet-parsers.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS packet parsers: 140625 matched observations per native/raw version across three platforms')
    return report


if __name__ == '__main__': main()
