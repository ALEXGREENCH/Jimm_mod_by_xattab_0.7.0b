#!/usr/bin/env python3
"""Verify sender bounds/light presence and typing static monitor on each RU platform."""
import json
import os
import recover
import audit_source as audit
from test_jimm_urls import dump, method


def classes(path, cp):
    return json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]),
                                  'BytecodeDump', path], capture=True))


def main():
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + platform + '_RU') / 'Jimm.jar'
        optimized = recover.ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        authoring = recover.ROOT / 'build/source' / (target + '-RU') / 'classes.jar'
        native = classes(original, cp)
        # Locate the actual editor controller by its public URL entry point and TextBox fields.
        controllers = [c for c in native if any(m['desc'] == '(Ljava/lang/String;Ljava/lang/Object;)V' for m in c['methods'])
                       and any(f['desc'] == 'Ljavax/microedition/lcdui/TextBox;' for f in c['fields'])]
        assert len(controllers) == 1
        controller = controllers[0]
        senders = [m for m in controller['methods'] if m['access'] & 8 and m['desc'].startswith('(Ljava/lang/String;L')
                   and m['code'].count('17 1024') == 2 and any(s.startswith('132 ') and s.endswith(' 1024') for s in m['code'])]
        assert len(senders) == 1
        sender = senders[0]
        typings = [(c, m) for c in native for m in c['methods'] if m['desc'] == '(Ljava/lang/String;Z)V'
                   and m['code'][:1] == ['17 146']]
        assert len(typings) == 1
        icq, typing = typings[0]
        assert typing['access'] & 40 == 40
        built_ui, authored_ui = dump(optimized, 'jimm/JimmUI', cp), dump(authoring, 'jimm/JimmUI', cp)
        built_send = method(built_ui, 'sendMessage', '(Ljava/lang/String;Ljimm/ContactItem;)V')
        authored_send = method(authored_ui, 'sendMessage', built_send['desc'])
        built_typing = method(dump(optimized, 'jimm/comm/Icq', cp), 'beginTyping', typing['desc'])
        authored_typing = method(dump(authoring, 'jimm/comm/Icq', cp), 'beginTyping', typing['desc'])
        for candidate in [built_send, authored_send]:
            assert candidate['access'] & 1064 == sender['access'] & 1064
            assert candidate['code'].count('17 1024') == 2
            increments = [s for s in candidate['code'] if s.startswith('132 ') and s.endswith(' 1024')]
            assert len(increments) == 1
            assert not any(s.endswith(' 2048') for s in candidate['code'])
        assert built_typing['access'] & 1064 == authored_typing['access'] & 1064 == typing['access'] & 1064 == 40
        native_call = '184 ' + icq['name'] + '.' + typing['name'] + typing['desc']
        source_call = '184 jimm/comm/Icq.beginTyping(Ljava/lang/String;Z)V'
        assert sum(m['code'].count(native_call) for m in controller['methods']) == 2
        assert sum(m['code'].count(source_call) for m in built_ui['methods']) == 2
        assert sum(m['code'].count(source_call) for m in authored_ui['methods']) == 2
        native_light = [s for s in sender['code'] if s == '184 aj.a(Z)V']
        source_light = [s for s in built_send['code'] if s == '184 DrawControls/LightControl.flash(Z)V']
        authored_light = [s for s in authored_send['code'] if s == '184 DrawControls/LightControl.flash(Z)V']
        expected_light = 0 if target == 'SIEMENS2' else 1
        assert len(native_light) == len(source_light) == len(authored_light) == expected_light
        if expected_light:
            assert sender['code'][-3:] == ['3', native_light[0], '177']
            assert built_send['code'][-3:] == ['3', source_light[0], '177']
        builds.append({'target': target, 'reference_sha256': recover.sha(original),
                       'source_optimized_class_jar_sha256': recover.sha(optimized),
                       'source_unoptimized_class_jar_sha256': recover.sha(authoring),
                       'reference_sender': controller['name'] + '.' + sender['name'] + sender['desc'],
                       'reference_typing': icq['name'] + '.' + typing['name'] + typing['desc'],
                       'sender_chunk_bound_and_increment': 1024, 'light_false_call_sites': expected_light,
                       'typing_static_and_class_monitor_verified': True,
                       'direct_static_typing_call_sites_in_controller': 2,
                       'sender_and_typing_signatures_verified': True,
                       'reference_sender_instructions': len(sender['code']),
                       'source_sender_instructions': len(built_send['code']),
                       'scope': 'Specific bounds, final light call, method signatures and synchronized static modifier only; '
                                'complete instruction equality or runtime behavior on this device is not claimed.'})
    report = {'scope': 'Each original RU platform has its own class/method identities derived from the actual bytecode. '
                       'Both authored and optimized source signatures, the two chunk bounds, loop increment, light guard '
                       'and synchronized static typing modifier are checked. MIDP2 runtime/packet/monitor tests are separate.',
              'builds': builds}
    (recover.ROOT / 'preservation/reports/source-send-text-bytecode.json').write_text(json.dumps(report, indent=2) + '\n',
                                                                                 encoding='utf-8', newline='\n')
    print('PASS sender/typing platform audit: 3 original identities, 1024 bounds/increment, light guard and static class monitor')


if __name__ == '__main__':
    main()
