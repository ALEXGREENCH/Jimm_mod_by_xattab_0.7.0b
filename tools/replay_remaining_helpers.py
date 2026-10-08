#!/usr/bin/env python3
"""Bind traffic/message-ID/filesystem replays to complete current subject methods."""
import json
import os
import zipfile

import recover
import audit_source as audit
from audit_send_text import classes
from audit_remaining_helpers import configuration, DIRECTORY
from test_source import ROOT, TEST, CACHE, run


def main():
    recover.bootstrap(); TEST.mkdir(parents=True, exist_ok=True); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([CACHE / n for n in recover.ASM])
    run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, ROOT / 'tools/recovery/BytecodeDump.java'], 'compile-remaining-helper-dump')
    runtime = [CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar', 'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    cases = [('traffic', 'MIDP2', 'MIDP2', None, 'TrafficProbe', 'screen', '(Z)V', 'a', 'update', 273),
             ('outgoing', 'MIDP2', 'MIDP2', None, 'OutgoingProbe', 'send', '()J', 'a', 'getMsgId', 2720),
             ('files-MIDP2-True', 'MIDP2', 'MIDP2', True, 'FileSystemProbe', 'filesystem', DIRECTORY, 'a', 'getDirectoryContents', 319),
             ('files-MOTOROLA-True', 'MOTOROLA', 'Moto', True, 'FileSystemProbe', 'filesystem', DIRECTORY, 'a', 'getDirectoryContents', 293),
             ('files-MOTOROLA-False', 'MOTOROLA', 'Moto', False, 'FileSystemProbe', 'filesystem', DIRECTORY, 'a', 'getDirectoryContents', 293),
             ('files-SIEMENS2-True', 'SIEMENS2', 'Siemens2', True, 'FileSystemProbe', 'filesystem', DIRECTORY, 'a', 'getDirectoryContents', 319)]
    series = []
    for prefix, target, old, jsr, probe, role, desc, native_name, source_name, count in cases:
        proofs, outputs = [], []
        for mode, legacy in [('reference', 'reference'), ('raw', 'source')]:
            path = ROOT / (f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar' if mode == 'reference' else f'build/source/{target}-RU/classes.jar')
            fixture = TEST / (prefix + '-' + legacy + '.jar'); output = TEST / ('remaining-helper-' + prefix + '-' + mode + '.txt')
            data = classes(path, cp); before = {c['name']: c for c in data}; after = {c['name']: c for c in classes(fixture, cp)}
            if mode == 'reference': owner, name = configuration(data, target)[role], native_name
            else: owner, name = {'screen': 'jimm/Traffic$TrafficScreen', 'send': 'jimm/comm/SendMessageAction', 'filesystem': 'jimm/FileSystem'}[role], source_name
            a = next(m for m in before[owner]['methods'] if m['name'] == name and m['desc'] == desc)
            b = next(m for m in after[owner]['methods'] if m['name'] == name and m['desc'] == desc)
            expected = dict(a); expected['code'] = list(a['code']); captures = []
            if role == 'filesystem' and target == 'MOTOROLA':
                instruction = '178 jimm/Jimm.' + ('a' if mode == 'reference' else 'supports_JSR75') + ' Z'
                assert expected['code'].count(instruction) == 1
                expected['code'] = ['184 FileSystemIO.jsr()Z' if s == instruction else s for s in expected['code']]
                captures.append(dict(before=instruction, after='184 FileSystemIO.jsr()Z', sites=1))
                expected['refs'] = [s for s in expected['code'] if 178 <= int(s.split(' ')[0]) <= 185]
            assert expected == b, ('Current complete helper differs from fixture', prefix, mode)
            with zipfile.ZipFile(path) as original, zipfile.ZipFile(fixture) as captured:
                changed = sorted(n for n in original.namelist() if n in captured.namelist() and original.read(n) != captured.read(n))
                added = sorted(set(captured.namelist()) - set(original.namelist())); removed = sorted(set(original.namelist()) - set(captured.namelist()))
            args = [recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647']
            if jsr is not None: args += ['-Djimm.files.target=' + target, '-Djimm.files.jsr=' + str(jsr).lower()]
            result = run([*args, '-cp', recover.cp([TEST, *runtime]), probe, fixture, legacy, output, *([target] if jsr is not None else [])], 'remaining-helper-' + prefix + '-' + mode)
            content = output.read_bytes(); outputs.append(content); assert len(content.splitlines()) == count
            assert content == (TEST / (prefix + '-' + legacy + '.txt')).read_bytes(), 'Existing complete observations changed'
            proofs.append(dict(mode=mode, subject_input_sha256=recover.sha(path), fixture_sha256=recover.sha(fixture), output_sha256=recover.sha(output),
                               observations=count, complete_current_method=a, complete_fixture_method=b, complete_expected_fixture_method=expected,
                               additional_subject_method_captures=captures, complete_subject_metadata_body_and_handlers_verified=True,
                               changed_entries=changed, added_entries=added, removed_entries=removed))
            print(prefix, mode, result, flush=True)
        assert outputs[0] == outputs[1]
        series.append(dict(series=prefix, target=target, jsr75_supported=jsr, proofs=proofs, different_observations=0))
    report = dict(scope='Existing complete native/raw MIDP2 TrafficProbe and OutgoingProbe plus four filesystem device paths on three May platforms replay only after full current subject/helper metadata, body and handlers are bound to the real fixture. '
                  'Traffic update and message-ID getter are unchanged in their legacy fixtures. Motorola directory wrapper has exactly one explicit JSR75 device-predicate capture; all remaining instructions/metadata/handlers match. '
                  'The full previous 273 traffic, 2720 outgoing and 1224 filesystem observations remain byte-for-byte. Every changed/added/removed fixture entry is enumerated. '
                  'Inherited clock, file/network, UI/application callback captures and scripted failures remain, including ordinary application-startup isolation/vendor declarations on filesystem fixtures. '
                  'This is not an external-only fixture, unchanged whole application claim, optimized/three-platform traffic/send runtime, arbitrary Traffic.update(false), physical phone/storage/network or whole-program equivalence proof. '
                  'ContactListItem equals override behavior is not newly exercised by this replay. No product source or delivered JAR change is made.', series=series)
    (ROOT / 'preservation/reports/source-remaining-helper-replay.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS remaining helper replay: 4217 matched observations per native/raw version; complete subject bindings and explicit captures')
    return report


if __name__ == '__main__': main()
