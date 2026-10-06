#!/usr/bin/env python3
"""Compare real packet parsers/serializers, including malformed lengths and historical failures."""
import json
import os
import recover
from test_source import TEST, CACHE, run

ROOT = recover.ROOT


def main():
    recover.bootstrap()
    TEST.mkdir(parents=True, exist_ok=True)
    helpers = TEST / 'packets-helpers'
    helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', CACHE / 'asm.jar', '-d', helpers,
         *[ROOT / 'tools/source' / n for n in ['ResourceIO.java', 'ResourceFixture.java', 'PacketProbe.java']]], 'compile-packets')
    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    source = ROOT / 'build/source/MIDP2-RU/classes.jar'
    results = []
    for mode, artifact in [('reference', reference), ('source', source)]:
        fixture = TEST / ('packets-resource-' + mode + '.jar')
        run([recover.java(), '-cp', recover.cp([helpers, CACHE / 'asm.jar']), 'ResourceFixture', artifact, fixture, mode, helpers], fixture.stem)
        results.append(run([recover.java(), '-cp', helpers, 'PacketProbe', fixture, artifact, mode, CACHE,
                            TEST / ('packets-' + mode + '.txt')], 'packets-' + mode))
    left, right = [TEST / ('packets-' + mode + '.txt') for mode in ['reference', 'source']]
    if left.read_bytes() != right.read_bytes():
        raise AssertionError('Packet mismatch: compare build/source-tests/packets-{reference,source}.txt')
    assert results[0] == results[1]
    report = {'scope': 'Real MIDP2 Packet/SnacPacket/ConnectPacket/DisconnectPacket/ToIcqSrvPacket/FromIcqSrvPacket '
                       'parsers, constructors, stored fields, copy/alias behavior and serializers. '
                       'Only Class.getResourceAsStream in ResourceBundle is captured; real JimmException formats error and extended error codes. '
                       'Synthetic error dictionary; option 133 exercises both actual CP1251 and default encoding paths. Source classes are unoptimized to retain parser entry points. '
                       'Two original specialized ToIcqSrvPacket constructors are called with equivalent constant arguments restored at the source call boundary. '
                       'No network connection or server compatibility claim.',
              'reference_sha256': recover.sha(reference), 'source_unoptimized_class_jar_sha256': recover.sha(source),
              'observations': len(left.read_text(encoding='utf-8').splitlines()),
              'successful_parses': int(left.read_text(encoding='utf-8').splitlines()[-1].split(':')[1]), 'differences': 0}
    (ROOT / 'preservation/reports/source-packets.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print(results[1])


if __name__ == '__main__':
    main()
