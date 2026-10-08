#!/usr/bin/env python3
"""Replay real XStatus catalog/capability/icon behavior on all three RU artifacts."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def configuration(data):
    candidates = [c for c in data if any(f['desc'].startswith('[L') and not f['desc'].startswith('[Ljava') for f in c['fields'])
                  and any(m['desc'] == '([B)V' for m in c['methods']) and any(f['desc'] == '[Ljava/lang/String;' for f in c['fields'])]
    assert len(candidates) == 1; status = candidates[0]
    guid = next(f['desc'][2:-1] for f in status['fields'] if f['desc'].startswith('[L') and not f['desc'].startswith('[Ljava'))
    contacts = [c for c in data if any(m['name'] == '<init>' and m['desc'] == '(IILjava/lang/String;Ljava/lang/String;ZZ)V' for m in c['methods'])]
    assert len(contacts) == 1; contact = contacts[0]
    image = next(m for m in status['methods'] if m['name'] == 'a' and m['desc'].startswith('(I)L') and m['desc'] != '(I)L' + guid + ';')
    icon = image['desc'][4:-1]
    image_list = next(f['desc'][1:-1] for f in status['fields'] if f['desc'].startswith('L') and f['desc'] != 'L' + guid + ';')
    return dict(status=status['name'], guid=guid, contact=contact['name'], icon=icon, images=image_list)


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-xstatus-platform-dump')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    helpers = TEST / 'xstatus-platform-helpers'; helpers.mkdir(exist_ok=True)
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp(runtime), '-d', helpers,
         ROOT / 'tools/source/XStatusCatalogProbe.java'], 'compile-xstatus-platforms')
    vendor = TEST / 'xstatus-platform-vendor'; vendor.mkdir(exist_ok=True)
    vendor_source = ROOT / 'tools/source/platform/com/siemens/mp/game/Light.java'
    run([os.environ.get('JAVAC', 'javac'), '-source', '7', '-target', '7', '-d', vendor, vendor_source], 'compile-xstatus-platform-vendor')
    light_class = vendor / 'com/siemens/mp/game/Light.class'
    light_bytes = light_class.read_bytes(); assert light_bytes[:4] == b'\xca\xfe\xba\xbe'
    # This provider uses only JVM 1.1 instructions; retain compatibility with the legacy host loader.
    light_class.write_bytes(light_bytes[:6] + b'\x00\x31' + light_bytes[8:])
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        native = ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar'
        raw = ROOT / f'build/source/{target}-RU/classes.jar'; delivered = ROOT / f'dist/source/Jimm-{target}-RU.jar'
        assembled = TEST / f'xstatus-platform-{target}-raw.jar'
        with zipfile.ZipFile(delivered) as z: entries = {n: z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z: entries.update({n: z.read(n) for n in z.namelist()})
        write_jar(assembled, entries)
        with zipfile.ZipFile(assembled) as z: assert {n: z.read(n) for n in z.namelist()} == entries
        outputs, proofs = [], []; fixtures = TEST / f'xstatus-platform-{target}-guids.bin'
        for mode, path in [('reference', native), ('raw', assembled), ('optimized', delivered)]:
            data = classes(path, cp); owners = {c['name']: c for c in data}
            spec = configuration(data) if mode == 'reference' else dict(status='jimm/comm/XStatus', guid='jimm/comm/GUID', contact='jimm/ContactItem', icon='DrawControls/Icon', images='DrawControls/ImageList')
            if target == 'SIEMENS2': spec['vendorLight'] = 'com/siemens/mp/game/Light'
            settings = TEST / f'xstatus-platform-{target}-{mode}.properties'
            settings.write_text(''.join(k + '=' + v.replace('/', '.') + '\n' for k, v in spec.items()), encoding='utf-8', newline='\n')
            output = TEST / f'xstatus-platform-{target}-{mode}.txt'
            result = run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
                          '-cp', recover.cp([helpers, *runtime, *([vendor] if target == 'SIEMENS2' else [])]), 'XStatusCatalogProbe', path,
                          'reference' if mode == 'reference' else 'source', fixtures, output, settings], f'xstatus-platform-{target}-{mode}')
            content = output.read_bytes(); outputs.append(content)
            assert len(content.splitlines()) == 156308 and content.splitlines()[-1] == b'parse-calls:156047'
            vendor_calls = None
            if target == 'SIEMENS2':
                log = (TEST / f'xstatus-platform-{target}-{mode}.log').read_text(encoding='utf-8')
                rows = [s for s in log.splitlines() if s.startswith('VENDOR-LIGHT:')]; assert len(rows) == 1
                vendor_calls = list(map(int, rows[0].split(':')[1:])); assert len(vendor_calls) == 2 and sum(vendor_calls) > 0
            with zipfile.ZipFile(path) as z: assert not any(n.startswith('com/siemens/') for n in z.namelist()), 'Test vendor provider must stay outside every application JAR'
            if target == 'MIDP2' and mode != 'raw':
                legacy = ROOT / 'build/xstatus-catalog-tests' / ('RU-' + ('reference' if mode == 'reference' else 'source') + '.txt')
                assert content == legacy.read_bytes(), 'All existing MIDP2 RU observations must remain'
            methods = []
            for role, owner, name, desc in [
                    ('contact_capability_parser', spec['contact'], 'a' if mode == 'reference' else 'setXStatus', '([B)V'),
                    ('contact_status_getter', spec['contact'], 'a' if mode == 'reference' else 'getXStatus', '()L' + spec['status'] + ';'),
                    ('static_image_getter', spec['status'], 'a' if mode == 'reference' else 'getStatusImage', '(I)L' + spec['icon'] + ';')]:
                method = next(m for m in owners[owner]['methods'] if m['name'] == name and m['desc'] == desc)
                methods.append({'role': role, 'owner': owner, 'complete_method': method})
            proofs.append({'mode': mode, 'input_sha256': recover.sha(path), 'source_raw_class_jar_sha256': recover.sha(raw) if mode == 'raw' else None,
                           'source_resource_jar_sha256': recover.sha(delivered) if mode == 'raw' else None,
                           'raw_class_and_delivered_resource_bytes_preserved_exactly': mode == 'raw',
                           'output_sha256': recover.sha(output), 'guid_fixture_sha256': recover.sha(fixtures), 'observations': 156308,
                           'parser_calls': 156047, 'configuration': spec, 'whole_actual_parser_and_getters': methods,
                           'external_siemens_light_provider': {'source_sha256': recover.sha(vendor_source), 'class_sha256': recover.sha(light_class), 'actual_on_off_calls': vendor_calls} if target == 'SIEMENS2' else None,
                           'application_methods_and_resource_bytes_not_substituted': True})
            print(target, mode, result, flush=True)
        assert outputs[0] == outputs[1] == outputs[2], ('Platform catalog/parser/image mismatch', target)
        builds.append({'target': target, 'proofs': proofs, 'different_observations': 0})
    report = {'scope': 'Existing complete XStatusCatalogProbe dataset executes on untouched May native/raw/optimized '
                       'RU application classes of all three platforms. Raw packaging combines each unchanged raw class '
                       'byte entry with its unchanged delivered resource/manifest entries, checked as an exact entry '
                       'dictionary; no additional application fixture transformation, resource, clock, network callback '
                       'or device predicate substitution is made. The MicroEmulator host, its normal external-API '
                       'classloader rewrites and memory RMS are explicit; unchanged input bytes do not imply '
                       'unchanged loaded JVM code. Actual ContactItem parser/getter executes, '
                       'Siemens startup additionally needs a test-host com.siemens.mp.game.Light provider that records '
                       'setLightOn/off calls without physical control; it is on the external host classpath only, '
                       'never inserted in any application JAR, and its source/class hashes remain explicit. '
                       'including the two XStatus helpers removed/inlined by modern optimization. All single-byte '
                       'GUID mutations, null/reset/partial/multiple caps, first-match/compatibility GUID, mutated real '
                       'GUID arrays/aliases/captions/image arrays and returned identities/raster hashes retain the '
                       'original dataset. Actual language loading and icon drawing remain real. The existing MIDP2 '
                       'RU output is preserved exactly. Three RU artifacts do not establish all five locales on '
                       'three platforms, live server/device/scheduler behavior or whole-program equivalence.', 'builds': builds}
    (ROOT / 'preservation/reports/source-xstatus-platforms.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS XStatus platforms: 468924 matched observations per native/raw/optimized version; no application/resource substitutions')
    return report


if __name__ == '__main__': main()
