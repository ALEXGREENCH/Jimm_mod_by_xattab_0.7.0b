#!/usr/bin/env python3
"""Replay real error construction/localization and explicitly separate reflection domains."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from audit_exception_constructors import configuration
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-exception-constructor-dump')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    helpers = TEST / 'exception-constructor-helpers'; helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp(runtime), '-d', helpers,
         ROOT / 'tools/source/ExceptionConstructorProbe.java'], 'compile-exception-constructors')
    vendor = TEST / 'exception-constructor-vendor'; vendor.mkdir(exist_ok=True)
    vendor_source = ROOT / 'tools/source/platform/com/siemens/mp/game/Light.java'
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', vendor, vendor_source], 'compile-exception-constructor-vendor')
    light_class = vendor / 'com/siemens/mp/game/Light.class'
    light_bytes = light_class.read_bytes(); assert light_bytes[:4] == b'\xca\xfe\xba\xbe'
    light_class.write_bytes(light_bytes[:6] + b'\x00\x31' + light_bytes[8:])
    builds = []; cases = 1035 * 15
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        native = ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'
        raw = ROOT / f'build/source/{target}-RU/classes.jar'; delivered = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        assembled = TEST / f'exception-constructor-{target}-raw.jar'
        with zipfile.ZipFile(delivered) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(assembled, entries)
        with zipfile.ZipFile(assembled) as z: assert {n: z.read(n) for n in z.namelist()} == entries
        outputs, proofs = [], []
        for mode, path in [('reference', native), ('raw', assembled), ('optimized', delivered)]:
            data = classes(path, cp); spec = configuration(data)
            if target == 'SIEMENS2': spec['vendorLight'] = 'com.siemens.mp.game.Light'
            settings = TEST / f'exception-constructor-{target}-{mode}.properties'
            settings.write_text(''.join(k + '=' + v + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            output = TEST / f'exception-constructor-{target}-{mode}.txt'; boundary = TEST / f'exception-constructor-{target}-{mode}-boundary.txt'
            result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                          '-cp', recover.cp([helpers, *runtime, *([vendor] if target == 'SIEMENS2' else [])]),
                          'ExceptionConstructorProbe', path, mode, output, boundary, settings], f'exception-constructor-{target}-{mode}')
            content = output.read_bytes(); outputs.append(content)
            rows = cases * 5 + 1; extra_rows = cases * (5 if mode == 'optimized' else 4)
            total_calls = cases * (9 if mode == 'optimized' else 8)
            assert result == f'PASS exception constructors: {rows} common observations, {extra_rows} explicit boundary observations, {total_calls} actual constructor calls'
            assert len(content.splitlines()) == rows and len(boundary.read_bytes().splitlines()) == extra_rows
            assert content.splitlines()[-1] == ('constructor-calls-in-common-domain:' + str(cases * 4)).encode()
            vendor_calls = None
            if target == 'SIEMENS2':
                log = (TEST / f'exception-constructor-{target}-{mode}.log').read_text(encoding='utf-8')
                lines = [s for s in log.splitlines() if s.startswith('VENDOR-LIGHT:')]; assert len(lines) == 1
                vendor_calls = list(map(int, lines[0].split(':')[1:])); assert len(vendor_calls) == 2 and sum(vendor_calls) > 0
            with zipfile.ZipFile(path) as z: assert not any(n.startswith('com/siemens/') for n in z.namelist())
            proofs.append(dict(mode=mode, input_sha256=recover.sha(path), configuration=spec, observations=rows,
                               code_extension_pairs=cases, actual_common_constructor_calls=cases * 4,
                               separately_asserted_boundary_observations=extra_rows, actual_total_constructor_calls=total_calls,
                               common_output_sha256=recover.sha(output), boundary_output_sha256=recover.sha(boundary),
                               raw_class_jar_sha256=recover.sha(raw) if mode == 'raw' else None,
                               raw_resource_jar_sha256=recover.sha(delivered) if mode == 'raw' else None,
                               exact_raw_class_and_resource_entry_dictionary_preserved=mode == 'raw',
                               external_siemens_light_provider=dict(source_sha256=recover.sha(vendor_source), class_sha256=recover.sha(light_class), actual_on_off_calls=vendor_calls) if target == 'SIEMENS2' else None,
                               no_application_class_or_resource_substitution=True))
            print(target, mode, result, flush=True)
        assert outputs[0] == outputs[1] == outputs[2], ('Error construction/message mismatch', target)
        builds.append(dict(target=target, proofs=proofs, common_domain_different_observations=0))
    report = dict(scope='Real native/raw/optimized RU error constructors of three platforms, real localized descriptions and complete code/critical/display/peer/message state. '
                  '1035 error codes (0..1024 plus negative/boundary values) times 15 extension values per platform; ordinary critical and noncritical false/true constructors '
                  'and actual peer true,true domain compare byte-for-byte. Separate output asserts all four native bool combinations are ignored, raw bool flags '
                  'remain generic, and five optimized unused dummy-byte values are ignored. Those out-of-domain outputs are intentionally not compared as equivalent. '
                  'Native/delivered JARs are direct inputs; raw byte entries plus delivered resources/manifest are preserved exactly. No application algorithm, resource, '
                  'clock, network or device predicate fixture is added. Host MicroEmulator/memory RMS and normal external-API load-time rewriting remain; unchanged '
                  'input JAR bytes do not imply unchanged loaded JVM code. Siemens uses the separate test-host Light provider, with actual child-loader call counts/hashes, '
                  'never embedded in the application. No physical phone, full startup, exception handler dispatch, live network, all-five-locales or whole-program equivalence '
                  'is claimed. No product source/JAR change is made.', builds=builds)
    (ROOT / 'preservation/reports/source-exception-constructors.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS exception constructors: 232878 matched common observations per native/raw/optimized version; explicit separate boolean/byte boundaries')
    return report


if __name__ == '__main__': main()
