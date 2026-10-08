#!/usr/bin/env python3
"""Retain whole traffic, filesystem, message-ID and interface helpers on three platforms."""
import difflib
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from audit_exception_constructors import configuration as errors
from test_xstatus_platforms import configuration as statuses


DIRECTORY = '(Ljava/lang/String;Z)[Ljava/lang/String;'
EQUALS = '(Ljava/lang/Object;)Z'
CANONICAL = dict(screen='jimm/Traffic$TrafficScreen', traffic='jimm/Traffic', text='DrawControls/TextList',
                 vl='DrawControls/VirtualList', options='jimm/Options', resource='jimm/util/ResourceBundle',
                 send='jimm/comm/SendMessageAction', item='jimm/ContactListItem', filesystem='jimm/FileSystem',
                 jsr='jimm/JSR75FileSystem', moto='jimm/MotorolaFileSystem', contact='jimm/ContactItem')


def configuration(data, target):
    owners = {c['name']: c for c in data}
    def pick(predicate):
        candidates = [c for c in data if predicate(c)]
        assert len(candidates) == 1, [c['name'] for c in candidates]
        return candidates[0]
    screen = pick(lambda c: len(c['fields']) == 3 and sum(f['desc'] == 'Ljavax/microedition/lcdui/Command;' for f in c['fields']) == 2
                  and any(m['desc'] == '(Z)V' for m in c['methods']) and any(m['name'] == 'commandAction' for m in c['methods']))
    update = next(m for m in screen['methods'] if m['desc'] == '(Z)V')
    assert update['code'][0].startswith('184 ') and update['code'][0].endswith('()I') and update['code'][1] == '87'
    traffic = update['code'][0][4:].split('.')[0]
    text = next(f['desc'][1:-1] for f in screen['fields'] if f['desc'].startswith('L') and not f['desc'].startswith('Ljava'))
    vl = update['code'][4][4:].split('.')[0]; assert owners[text]['super'] == vl
    options = {s[4:].split('.')[0] for s in update['code'] if s.startswith('178 ') and s.endswith(' I')}; assert len(options) == 1
    send = pick(lambda c: any(m['desc'] == '()J' and len(m['code']) == 10 and m['code'][0] == '25 0'
                             and m['code'][2:5] == ['133', '16 32', '121'] for m in c['methods']))
    contact = statuses(data)['contact']; assert len(owners[contact]['interfaces']) == 1
    item = owners[contact]['interfaces'][0]; assert any(m['name'] == 'equals' and m['desc'] == EQUALS for m in owners[item]['methods'])
    jsr = pick(lambda c: any(f['desc'] in ['Ljavax/microedition/io/file/FileConnection;', 'Lcom/siemens/mp/io/file/FileConnection;'] for f in c['fields']))
    result = dict(screen=screen['name'], traffic=traffic, text=text, vl=vl, options=next(iter(options)), resource=errors(data)['resource'],
                  send=send['name'], item=item, filesystem=jsr['super'], jsr=jsr['name'], contact=contact)
    if target == 'MOTOROLA':
        moto = pick(lambda c: any(f['desc'] == 'Lcom/motorola/io/FileConnection;' for f in c['fields']))
        assert moto['super'] == jsr['super']; result['moto'] = moto['name']
    return result


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        paths = {'reference': recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar',
                 'raw': recover.ROOT / f'build/source/{target}-RU/classes.jar', 'optimized': recover.ROOT / f'dist/source/Jimm-{target}-RU.jar'}
        data = {mode: classes(path, cp) for mode, path in paths.items()}
        declarations = {mode: {c['name']: c for c in values} for mode, values in data.items()}
        native = configuration(data['reference'], target); class_map = {owner: CANONICAL[role] for role, owner in native.items()}
        aliases, bindings = {}, []
        def descriptor(value):
            for a, b in class_map.items(): value = value.replace('L' + a + ';', 'L' + b + ';')
            return value
        def bind(role, name, desc, source_name, field=False):
            owner = native[role]; kind = 'fields' if field else 'methods'
            a = [m for m in declarations['reference'][owner][kind] if m['name'] == name and m['desc'] == desc]; assert len(a) == 1
            b = [m for m in declarations['raw'][CANONICAL[role]][kind] if m['name'] == source_name and m['desc'] == descriptor(desc)]; assert len(b) == 1
            actual = owner + '.' + name + (' ' if field else '') + desc
            canonical = CANONICAL[role] + '.' + source_name + (' ' if field else '') + descriptor(desc)
            aliases[actual] = canonical; bindings.append(dict(actual_member=actual, typed_alias=canonical, complete_native_declaration=a[0], complete_raw_declaration=b[0]))
        bind('screen', 'a', 'L' + native['text'] + ';', 'trafficTextList', True)
        bind('traffic', 'a', '()I', 'getSessionTraffic'); bind('traffic', 'a', '(I)Ljava/lang/String;', 'getTrafficString')
        bind('resource', 'a', '(Ljava/lang/String;)Ljava/lang/String;', 'getString')
        bind('options', 'g', 'I', 'fontStyle', True)
        bind('text', 'a', '()V', 'clear'); bind('text', 'a', '(Ljava/lang/String;III)L' + native['text'] + ';', 'addBigText')
        bind('vl', 'd', '()I', 'getTextColor'); bind('vl', 'e', '()V', 'repaint')
        bind('send', 'b', 'I', 'msgId1', True); bind('send', 'c', 'I', 'msgId2', True)
        bind('jsr', 'a', DIRECTORY, 'getDirectoryContents')
        if target == 'MOTOROLA':
            bind('moto', 'a', DIRECTORY, 'getDirectoryContents')
            a = next(f for f in declarations['reference']['jimm/Jimm']['fields'] if f['name'] == 'a' and f['desc'] == 'Z')
            b = next(f for f in declarations['raw']['jimm/Jimm']['fields'] if f['name'] == 'supports_JSR75' and f['desc'] == 'Z')
            aliases['jimm/Jimm.a Z'] = 'jimm/Jimm.supports_JSR75 Z'
            bindings.append(dict(actual_member='jimm/Jimm.a Z', typed_alias=aliases['jimm/Jimm.a Z'], complete_native_declaration=a, complete_raw_declaration=b))
        def normalize(code):
            return [op + sep + descriptor(aliases.get(value, value)) for op, sep, value in [s.partition(' ') for s in code]]
        def body(owner, m, mode):
            return dict(owner=owner, complete_method=m, typed_instructions=normalize(m['code']) if mode == 'reference' else m['code'],
                        typed_handlers=m['handlers'])
        def callers(mode, owner, name, desc):
            invocation = owner + '.' + name + desc
            return [dict(owner=c['name'], complete_method=m, positions=[i for i, s in enumerate(m['code']) if s.partition(' ')[2] == invocation])
                    for c in data[mode] for m in c['methods'] if any(s.startswith(('182 ', '183 ', '184 ', '185 ')) and s.partition(' ')[2] == invocation for s in m['code'])]
        records = []; evidence = []
        for mode in paths:
            spec = native if mode == 'reference' else CANONICAL
            methods = {}; sites = {}; absence = {}; extras = {}
            for role, owner_role, desc, source_name in [('traffic_update', 'screen', '(Z)V', 'update'), ('directory', 'filesystem', DIRECTORY, 'getDirectoryContents'),
                                                       ('message_id', 'send', '()J', 'getMsgId'), ('equals_declaration', 'item', EQUALS, 'equals')]:
                owner = spec[owner_role]; name = 'equals' if role == 'equals_declaration' else 'a' if mode == 'reference' else source_name
                found = [m for m in declarations[mode][owner]['methods'] if m['name'] == name and m['desc'] == desc]
                if mode == 'optimized' and role == 'traffic_update':
                    assert not found
                    found = [m for m in declarations[mode][owner]['methods'] if m['name'].split('$')[0] == source_name and m['desc'] == '()V']
                if mode == 'optimized' and (role in ['message_id', 'equals_declaration'] or (role == 'directory' and target != 'MOTOROLA')):
                    assert not found; methods[role] = None; absence[role] = dict(owner=owner, name=name, descriptor=desc, matching_ABI_absent=True)
                    continue
                assert len(found) == 1; m = found[0]; methods[role] = body(owner, m, mode); sites[role] = callers(mode, owner, m['name'], m['desc'])
                if role == 'traffic_update':
                    assert sum(len(c['positions']) for c in sites[role]) == 2
                    if mode != 'optimized': assert all(c['complete_method']['code'][i - 1] == '4' for c in sites[role] for i in c['positions'])
                elif role == 'directory': assert sum(len(c['positions']) for c in sites[role]) == 3
                elif role == 'message_id': assert sum(len(c['positions']) for c in sites[role]) == 1
                else: assert m['access'] == 1025 and not m['code'] and not m['handlers']
            # Preserve the side effects of getSessionTraffic even when the screen test/field store disappears.
            owner = spec['traffic']; m = next(m for m in declarations[mode][owner]['methods'] if m['name'] == ('a' if mode == 'reference' else 'getSessionTraffic') and m['desc'] == '()I')
            extras['session_helper'] = body(owner, m, mode)
            owner = spec['screen']; fields = declarations[mode][owner]['fields']; extras['whole_screen_fields'] = fields
            if mode == 'raw':
                byte_fields = [f for f in fields if f['desc'] == 'B']; assert {f['name'] for f in byte_fields} == {'compareTraffic', 'updateThreshold'}
                accesses = []
                for c in data[mode]:
                    for m in c['methods']:
                        positions = [i for i, s in enumerate(m['code']) if any(s.partition(' ')[2] == owner + '.' + f['name'] + ' B' for f in byte_fields)]
                        if positions: accesses.append(dict(owner=c['name'], complete_method=m, positions=positions))
                assert {c['owner'] for c in accesses} == {owner}
                assert {c['complete_method']['name'] for c in accesses} == {'<init>', 'update'}
                extras['whole_raw_threshold_field_accessors'] = accesses
            else: assert not any(f['desc'] == 'B' for f in fields)
            # Actual interface implementors and real equals overrides remain distinct from the removed declaration.
            implementors = [c for c in data[mode] if spec['item'] in c['interfaces']]; assert len(implementors) == 2
            overrides = []
            for c in implementors:
                matches = [m for m in c['methods'] if m['name'] == 'equals' and m['desc'] == EQUALS]; assert len(matches) == 1
                assert not matches[0]['access'] & (8 | 1024)
                overrides.append(dict(owner=c['name'], super=c['super'], interfaces=c['interfaces'], complete_equals_override=matches[0]))
            extras['whole_actual_equals_overrides'] = overrides
            evidence.append(dict(mode=mode, input_sha256=recover.sha(paths[mode]), whole_helpers=methods, whole_actual_callers=sites, absent_matching_ABIs=absence, extras=extras))
        for role in ['traffic_update', 'directory', 'message_id', 'equals_declaration']:
            a = evidence[0]['whole_helpers'][role]; b = evidence[1]['whole_helpers'][role]
            exact = a['typed_instructions'] == b['typed_instructions'] and a['typed_handlers'] == b['typed_handlers']
            if role != 'traffic_update': assert exact
            records.append(dict(role=role, complete_native_raw_instructions_and_handlers_equal=exact,
                                native_access=a['complete_method']['access'], raw_access=b['complete_method']['access'],
                                complete_instruction_diff=list(difflib.unified_diff(a['typed_instructions'], b['typed_instructions'], lineterm='')),
                                complete_handler_diff=list(difflib.unified_diff(a['typed_handlers'], b['typed_handlers'], lineterm=''))))
        raw_update = evidence[1]['whole_helpers']['traffic_update']['complete_method']['code']
        assert raw_update[:9] == ['184 jimm/Traffic.getSessionTraffic()I', '25 0', '180 jimm/Traffic$TrafficScreen.compareTraffic B', '100',
                                  '25 0', '180 jimm/Traffic$TrafficScreen.updateThreshold B', '162 9', '21 1', '153 159']
        assert raw_update[-8:-4] == ['25 0', '184 jimm/Traffic.getSessionTraffic()I', '145', '181 jimm/Traffic$TrafficScreen.compareTraffic B']
        optimized_update = evidence[2]['whole_helpers']['traffic_update']['complete_method']['code']
        assert optimized_update[:2] == ['184 jimm/Traffic.getSessionTraffic()I', '87'] and optimized_update[-6:-4] == ['184 jimm/Traffic.getSessionTraffic()I', '87']
        assert not any(s.startswith(('153 ', '154 ', '155 ', '156 ', '157 ', '158 ', '159 ', '160 ', '161 ', '162 ', '163 ', '164 ', '167 ')) for s in optimized_update)
        optimized_witnesses = []
        if target == 'MOTOROLA':
            assert evidence[1]['whole_helpers']['directory']['typed_instructions'] == evidence[2]['whole_helpers']['directory']['typed_instructions']
            assert evidence[1]['whole_helpers']['directory']['typed_handlers'] == evidence[2]['whole_helpers']['directory']['typed_handlers']
        for role in ['directory', 'message_id']:
            raw_body = evidence[1]['whole_helpers'][role]; invocation = raw_body['owner'] + '.' + raw_body['complete_method']['name'] + raw_body['complete_method']['desc']
            for caller in evidence[1]['whole_actual_callers'][role]:
                actual = [m for m in declarations['optimized'][caller['owner']]['methods'] if m['name'].split('$')[0] == caller['complete_method']['name'].split('$')[0]]
                parents = []
                if not actual:
                    assert role == 'directory' and caller['owner'] == 'jimm/FileBrowser' and caller['complete_method']['name'] == 'VTnodeClicked'
                    parents = callers('raw', caller['owner'], caller['complete_method']['name'], caller['complete_method']['desc'])
                    assert parents
                    actual = [m for parent in parents for m in declarations['optimized'][parent['owner']]['methods']
                              if m['name'].split('$')[0] == parent['complete_method']['name'].split('$')[0]]
                    assert actual
                    removed_call = caller['owner'] + '.' + caller['complete_method']['name'] + caller['complete_method']['desc']
                    assert all(not any(s.partition(' ')[2] == removed_call for s in m['code']) for m in actual)
                if role == 'directory' and target == 'MOTOROLA':
                    assert any(any(s.partition(' ')[2] == invocation for s in m['code']) for m in actual)
                else: assert all(not any(s.partition(' ')[2] == invocation for s in m['code']) for m in actual)
                direct_calls = None
                if role == 'directory':
                    delegate = ('jimm/FileSystem' if target == 'MOTOROLA' else 'jimm/JSR75FileSystem') + '.getDirectoryContents' + DIRECTORY
                    direct_calls = [dict(name=m['name'], descriptor=m['desc'], positions=[i for i, s in enumerate(m['code']) if s == '184 ' + delegate]) for m in actual]
                    assert any(site['positions'] for site in direct_calls)
                fold = None
                if role == 'message_id':
                    assert len(actual) == 1; code = actual[0]['code']; positions = [i for i, s in enumerate(code) if s == '180 jimm/comm/SendMessageAction.msgId1 I']; assert len(positions) == 1
                    i = positions[0]
                    assert code[i - 8:i] == ['187 jimm/comm/SendMessageAction', '89', '25 2', '183 jimm/comm/SendMessageAction.<init>(Ljimm/comm/Message;)V', '89', '58 5', '89', '58 6']
                    assert code[i:i + 8] == ['180 jimm/comm/SendMessageAction.msgId1 I', '133', '16 32', '121', '25 6', '180 jimm/comm/SendMessageAction.msgId2 I', '133', '97']
                    assert code[i + 8] == '55 6'
                    fold = dict(position=i, actual_receiver_construction_duplicates_and_locals=code[i - 8:i], exact_signed_long_shift_and_add=code[i:i + 8], following_long_store=code[i + 8])
                optimized_witnesses.append(dict(role=role, whole_raw_caller=caller, whole_raw_callers_of_eliminated_caller=parents,
                                               original_caller_entrypoint_inlined_away=bool(parents), whole_actual_optimized_callers=actual,
                                               actual_optimized_directory_call_sites=direct_calls, actual_message_id_fold=fold))
        accounting = None
        if target == 'MIDP2':
            accounting = []
            for role, native_name, desc in [('screen', 'a', '(Z)V'), ('filesystem', 'a', DIRECTORY), ('send', 'a', '()J'), ('item', 'equals', EQUALS)]:
                owner = native[role]; inventory = {(m['name'], m['desc']) for m in declarations['reference'][owner]['methods']}
                main_members = {(name, descriptor) for native_owner, name, descriptor, source_name in audit.METHODS if native_owner == owner}
                extra = {(native_name, desc)}; assert inventory == main_members | extra
                accounting.append(dict(owner=owner, source_owner=CANONICAL[role], native_methods=len(inventory), main_audit_methods=len(main_members),
                                       additional_methods=len(extra - main_members), all_native_methods_accounted=True))
        builds.append(dict(target=target, native_configuration=native, whole_typed_bindings=bindings, evidence=evidence, complete_native_raw_comparisons=records,
                           whole_optimized_caller_witnesses=optimized_witnesses,
                           native_class_accounting=accounting,
                           traffic_specialization_domain='Both actual native/raw call sites pass true; native/optimized preserve both getSessionTraffic calls with POP. Raw threshold bytes are accessed only by constructor/update and remain generic in src. No update(false) equivalence is claimed.'))
    report = dict(scope='Twelve complete native/raw helper pairs across three May RU platforms. Complete FileSystem directory wrappers, SendMessageAction message-ID getters and ContactListItem abstract equals declarations match exactly; native final/raw nonfinal access is retained. '
                  'Traffic update has two actual true call sites per version/platform; its full generic raw predicate/byte-field accesses and whole session helper are retained, together with the actual optimized no-arg method and full differences. '
                  'Both session calls remain with discarded results; they are not treated as pure or removed. Direct update(false) is outside the proven specialized domain. '
                  'MIDP2/Siemens directory wrappers are inlined away, whereas Motorola keeps its full ten-instruction JSR75/vendor dispatch wrapper. All whole real raw/optimized callers remain. '
                  'Modern message getter is removed; its actual sendMessage caller contains the exact signed I2L, left-shift-32 and long addition with a proven duplicated receiver/local. '
                  'The optimized interface equals declaration is removed, while actual ContactItem/GroupItem equals overrides remain. Absence of a direct interface call does not imply absence of Object.equals virtual dispatch. '
                  'The body/access/hierarchy inventory does not claim full behavioral equivalence of override implementations, arbitrary reflection inputs or whole-program behavior. No product source/JAR change is made.', builds=builds)
    (recover.ROOT / 'preservation/reports/source-remaining-helper-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS remaining helpers: 12 full native/raw pairs, nine exact; actual traffic specialization, Motorola dispatch, signed ID folds and equals overrides retained')
    return report


if __name__ == '__main__': main()
