#!/usr/bin/env python3
"""Bind the complete MIDP2 May method inventory to real current authoring members.

This is an accounting/binding check, not a whole-program equivalence claim.
Named helper reports keep their own narrower transformation/runtime boundaries.
"""
import argparse
import json
import re
import sys

import recover
import audit_source as audit
from audit_send_text import classes
from audit_vlist_helpers import MEMBERS as VLIST, translated
from audit_contact_helpers import DIRECT, EXPRESSIONS
from audit_ui_helpers import CHAT
from audit_auxiliary_helpers import MEMBERS as AUX


REPORTS = recover.ROOT / 'preservation/reports'
# These are explicit authoring ABIs, not guesses made by dropping descriptor args.
# The native/modern ABIs remain separately recorded; this map claims correspondence.
RAW_OVERRIDES = {
    'ci.i(I)V': ('DrawControls/VirtualAlert', 'pointerDragged', '(II)V'),
    'bi.a(Ljava/lang/String;IIIZC)V': ('DrawControls/TextList', 'internAdd', '(Ljava/lang/String;IIIIZC)V'),
    'm.a(II[Lbs;)V': ('jimm/ContactList', 'update', '(III[Ljimm/ContactListItem;)V'),
    'cd.i(I)V': ('DrawControls/VirtualList', 'pointerDragged', '(II)V'),
    'cf.a$1385ff()V': ('jimm/JimmUI', 'setColorScheme', '(Z)V'),
    'bg.a$16da05f7(Ljava/lang/String;)V': ('jimm/HistoryStorageList', 'setCurrUin', '(Ljava/lang/String;Ljava/lang/String;)V'),
    'y.e()V': ('jimm/ChatTextList', 'activate', '(ZZ)V'),
    'a.<init>()V': ('jimm/Traffic$TrafficScreen', '<init>', '(Ljimm/Traffic;)V'),
    'ao.<init>()V': ('jimm/comm/Icq$PeerConnection', '<init>', '(Ljimm/comm/Icq;)V'),
    'ab.<init>(Ljava/lang/String;Lz;Ljava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V':
        ('jimm/comm/FileTransferMessage', '<init>', '(Ljava/lang/String;Ljimm/ContactItem;ILjava/lang/String;Ljava/lang/String;Ljava/io/InputStream;I)V'),
    'cf.b()V': ('jimm/JimmUI', 'about', '(Ljavax/microedition/lcdui/Displayable;)V'),
    'an.<init>([B)V': ('jimm/comm/Packet', '<init>', '(I[B)V'),
    'bu.<init>(Ljava/lang/String;I[B[B)V': ('jimm/comm/ToIcqSrvPacket', '<init>', '(JLjava/lang/String;I[B[B)V'),
    'co.a$1385f3()J': ('jimm/comm/Util', 'createCurrentDate', '(Z)J'),
    'l.a$4e9ee315([Ljava/lang/Object;)Z': ('jimm/RunnableImpl', 'getBoolean', '([Ljava/lang/Object;I)Z'),
    'ba.a(Ljava/lang/String;IJJLjava/lang/String;)[B': ('jimm/comm/XtrazSM', 'a', '(Ljava/lang/String;IJJLjava/lang/String;)[B'),
    'ba.a([BI)I': ('jimm/comm/XtrazSM', 'a', '([BI)I'),
    'ba.b([BI)I': ('jimm/comm/XtrazSM', 'b', '([BI)I'),
    'co.a$6f0c2d54([B)Ljava/io/DataInputStream;': ('jimm/comm/Util', 'getDataInputStream', '([BI)Ljava/io/DataInputStream;'),
    'co.a$175c50c1(Ljava/io/DataInputStream;)I': ('jimm/comm/Util', 'getWord', '(Ljava/io/DataInputStream;Z)I'),
    'co.b$559c4327(Ljava/io/ByteArrayOutputStream;I)V': ('jimm/comm/Util', 'writeDWord', '(Ljava/io/ByteArrayOutputStream;IZ)V'),
    'ap.<init>()V': ('jimm/comm/Icq$Connection', '<init>', '(Ljimm/comm/Icq;)V'),
    'cd.c$13462e()V': ('DrawControls/VirtualList', 'setMode', '(I)V'),
    'm.a$505cff1c(Ljava/lang/String;)V': ('jimm/ContactList', 'update', '(Ljava/lang/String;I)V'),
    'cj.a$134622()J': ('jimm/Options', 'getLong', '(I)J'),
    'm.a(Z)V': ('jimm/ContactList', 'optionsChanged', '(ZZ)V'),
    'cf.b$552c4e01()V': ('jimm/JimmUI', 'setString', '(Ljava/lang/String;)V'),
}


def key(owner, m):
    return owner + '.' + m['name'] + m['desc']


def split(member):
    owner, rest = member.split('.', 1)
    name, desc = rest.split('(', 1)
    return owner, name, '(' + desc


def exact(data, owner, name, desc):
    matches = [m for m in data.get(owner, {}).get('methods', []) if (m['name'], m['desc']) == (name, desc)]
    assert len(matches) == 1, ('Missing/ambiguous actual owner and ABI', owner, name, desc)
    return matches[0]


def member_record(owner, method):
    return dict(member=key(owner, method), access=method['access'],
                instructions=len(method['code']), handlers=method['handlers'],
                literal_instruction_sha256=audit.digest(method['code']),
                complete_dumped_method_sha256=audit.digest([json.dumps(method, sort_keys=True)]))


def source_binding(owner):
    outer = owner.split('$')[0]
    filename = recover.ROOT / 'src' / (outer + '.java')
    if not filename.exists():
        package, name = outer.rsplit('/', 1)
        candidates = []
        for path in (recover.ROOT / 'src' / package).glob('*.java'):
            if re.search(r'\b(?:class|interface)\s+' + re.escape(name) + r'\b', path.read_bytes().decode('cp1251')):
                candidates.append(path)
        assert len(candidates) == 1, ('Missing/ambiguous authoring type declaration', owner, candidates)
        filename = candidates[0]
    return dict(file=filename.relative_to(recover.ROOT).as_posix(), sha256=recover.sha(filename),
                scope='Declaring source file/type, including compiler-generated accessors; original method spelling is not inferred from this binding.')


def helper_groups():
    # native owner/name/descriptor -> one or several actual authoring members.
    groups = []
    def add(group, report, entries):
        groups.append((group, report + '.json', entries))
    def direct(owner, canonical, entries):
        return [(owner + '.' + n + d, [(canonical, role, override or translated(d))])
                for n, d, role, override in entries]
    add('VirtualList', 'source-vlist-helper-bytecode', direct('cd', 'DrawControls/VirtualList', VLIST))
    add('ContactItem', 'source-contact-helper-bytecode', direct('z', 'jimm/ContactItem', DIRECT) +
        [('z.' + n + d, [('jimm/ContactItem', name, desc) for name, desc in members]) for n, d, role, members in EXPRESSIONS])
    add('Chat', 'source-ui-helper-bytecode', [('y.' + n + d, [('jimm/ChatTextList', role, '(Z)V' if role == 'BeginTyping' else translated(d))]) for n, d, role in CHAT])
    add('staticText', 'source-static-text-bytecode', [
        ('bi.a(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIIIII)V', [('DrawControls/TextList', 'showText', '(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;IIIIII)V')]),
        ('bi.a$ba2c332(Ljava/lang/String;III)I', [('DrawControls/TextList', 'getLineNumbers', '(Ljava/lang/String;IIII)I')])])
    add('emptyConstructors', 'source-unused-constructor-bytecode', [(o + '.<init>()V', [(canonical, '<init>', '()V')])
        for o, canonical in [('ai', 'jimm/util/ResourceBundle'), ('aj', 'DrawControls/LightControl'), ('ba', 'jimm/comm/XtrazSM'), ('bq', 'jimm/comm/OtherAction'), ('co', 'jimm/comm/Util')]])
    add('packets', 'source-packet-parsers-bytecode', [(o + '.a([BII)Lan;', [('jimm/comm/' + canonical, 'parse', '([BII)Ljimm/comm/Packet;')])
        for o, canonical in [('an', 'Packet'), ('ak', 'SnacPacket'), ('h', 'ConnectPacket'), ('bn', 'DisconnectPacket'), ('bu', 'ToIcqSrvPacket'), ('ck', 'FromIcqSrvPacket'), ('au', 'DCPacket')]] +
        [('bu.<init>(JILjava/lang/String;[B[B)V', [('jimm/comm/ToIcqSrvPacket', '<init>', '(JILjava/lang/String;I[B[B)V')])])
    add('xtrazQuery', 'source-xtraz-queries', [('ba.a(Ljava/lang/String;I)V', [('jimm/comm/XtrazSM', 'a', '(Ljava/lang/String;I)V')])])
    add('xstatus', 'source-xstatus-helper-bytecode', [('bj.a([B)V', [('jimm/comm/XStatus', 'setXStatus', '([B)V')]), ('bj.a()Le;', [('jimm/comm/XStatus', 'getStatusImage', '()LDrawControls/Icon;')])])
    add('util', 'source-specialized-util-version-bytecode', [('co.' + m['reference_name'] + m['reference_desc'], [('jimm/comm/Util', m['source_name'], m['source_declaration_desc'])])
        for m in audit.UTIL_SYMBOLS['methods'] if m['source_name'] in ['writeAsciizTLVInterest', 'byteArrayEquals', 'strToIntDef']])
    add('icq', 'source-icq-state-bytecode', [('r.e()I', [('jimm/comm/Icq', 'getPrivateStatusId', '()I')])])
    add('resolver', 'source-resolver', [('cb.a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;', [('jimm/comm/Icq$SOCKSConnection', 'ResolveIP', '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;')])])
    add('versionSetters', 'source-specialized-util-version-bytecode', [('cf.' + name + '(Ljava/lang/String;)Ljava/lang/String;', [('jimm/JimmUI', accessor, '(Ljava/lang/String;)V')]) for name, accessor in [('a', 'access$0'), ('b', 'access$1')]])
    add('aux', 'source-auxiliary-helper-bytecode', [(o + '.' + n + d, [(audit.CLASSES[o], role, translated(d))]) for o, n, d, role in AUX])
    add('exception', 'source-exception-constructor-bytecode', [('bv.<init>(IIZZ)V', [('jimm/JimmException', '<init>', '(IIZZ)V')])])
    add('remaining', 'source-remaining-helper-bytecode', [
        ('a.a(Z)V', [('jimm/Traffic$TrafficScreen', 'update', '(Z)V')]),
        ('w.a(Ljava/lang/String;Z)[Ljava/lang/String;', [('jimm/FileSystem', 'getDirectoryContents', '(Ljava/lang/String;Z)[Ljava/lang/String;')]),
        ('cl.a()J', [('jimm/comm/SendMessageAction', 'getMsgId', '()J')]),
        ('bs.equals(Ljava/lang/Object;)Z', [('jimm/ContactListItem', 'equals', '(Ljava/lang/Object;)Z')])])
    return groups


def saved_bodies(document):
    """Extract owner-qualified whole bodies only, keeping their actual evidence mode."""
    result = []
    def walk(value, pointer='', mode=None):
        if isinstance(value, dict):
            mode = value.get('mode', mode)
            if all(k in value for k in ['method', 'access', 'instructions', 'handlers']):
                result.append((mode, pointer, 'compact', value['method'], value))
            if 'owner' in value:
                for field in ['complete_method', 'whole_constructor']:
                    method = value.get(field)
                    if isinstance(method, dict) and 'code' in method:
                        result.append((mode, pointer + '/' + field, 'literal', key(value['owner'], method), method))
            if 'query_owner' in value and 'complete_query' in value:
                result.append((mode, pointer + '/complete_query', 'literal', key(value['query_owner'], value['complete_query']), value['complete_query']))
            for field, child in value.items():
                child_mode = {'reference': 'reference', 'raw': 'raw', 'optimized': 'optimized',
                              'raw_accessors': 'raw', 'optimized_accessors': 'optimized'}.get(field, mode)
                walk(child, pointer + '/' + field.replace('~', '~0').replace('/', '~1'), child_mode)
        elif isinstance(value, list):
            for i, child in enumerate(value): walk(child, pointer + '/' + str(i), mode)
    walk(document)
    return result


def same_saved_body(method, body, format_name, mode):
    if format_name == 'literal': return body == method
    code = audit.normalized(method['code']) if mode == 'reference' else method['code']
    handlers = audit.normalized_handlers(method['handlers']) if mode == 'reference' else method['handlers']
    return body['access'] == method['access'] and body['instructions'] == code and body['handlers'] == handlers


def bind_saved(bodies, mode, owner, method):
    candidates = [b for b in bodies if b[0] == mode and b[3] == key(owner, method)]
    matched = []
    for _, pointer, format_name, _, body in candidates:
        if same_saved_body(method, body, format_name, mode): matched.append(dict(pointer=pointer, format=format_name))
    assert matched, ('No whole current method bound in designated helper report', mode, key(owner, method), [c[1] for c in candidates])
    return matched


def validate_source_context(bodies, data):
    records = []
    for mode, pointer, fmt, member, body in bodies:
        if mode not in ['raw', 'optimized']: continue
        owner, name, desc = split(member)
        method = exact(data[mode], owner, name, desc)
        assert same_saved_body(method, body, fmt, mode), ('Stale whole source context', mode, pointer, member)
        records.append(dict(mode=mode, pointer=pointer, format=fmt, **member_record(owner, method)))
    return dict(scope='Every extracted owner-qualified raw/optimized whole body occurrence in this MIDP2 report, including caller witnesses, '
                      'is bound to current classes. Duplicate occurrences are counted separately. Unqualified body lists, other report metadata, '
                      'absence/reachability statements and runtime fixtures are outside this generic context check.',
                whole_body_occurrences=len(records), distinct_mode_members=len({(r['mode'], r['member']) for r in records}), bindings=records)


def main(skip_build=False):
    if not skip_build:
        recover.run([sys.executable, recover.ROOT / 'tools/build_source.py', '--target', 'MIDP2', '--language', 'RU'])
    # Refresh the named subset against actual current input classes before binding it.
    audit.main()
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    paths = dict(reference=recover.ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar',
                 raw=recover.ROOT / 'build/source/MIDP2-RU/classes.jar',
                 optimized=recover.ROOT / 'build/source/MIDP2-RU/preverified.jar')
    data = {mode: {c['name']: c for c in classes(path, cp)} for mode, path in paths.items()}
    native = {key(o, m): (o, m) for o, c in data['reference'].items() for m in c['methods']}
    inventory = [key(o, {'name': n, 'desc': d}) for o, n, d, _ in audit.METHODS]
    assert len(inventory) == len(set(inventory)) == 1129
    main_report = json.loads((REPORTS / 'source-bytecode-comparison.json').read_text())
    assert main_report['reference_sha256'] == recover.sha(paths['reference'])
    assert main_report['rebuilt_sha256'] == recover.sha(paths['optimized'])
    assert {m['reference'] for m in main_report['methods']} == set(inventory)
    records = []
    used_overrides = set()
    for i, row in enumerate(main_report['methods']):
        owner, before = native[row['reference']]
        opt_owner, opt_name, opt_desc = split(row['source'])
        optimized = exact(data['optimized'], opt_owner, opt_name, opt_desc)
        assert row['reference_normalized_sha256'] == audit.digest(audit.normalized(before['code']))
        assert row['source_normalized_sha256'] == audit.digest(optimized['code'])
        if row['reference'] in RAW_OVERRIDES:
            raw_owner, name, desc = RAW_OVERRIDES[row['reference']]; used_overrides.add(row['reference'])
            raw = exact(data['raw'], raw_owner, name, desc)
            boundary = 'explicit authoring ABI/owner; correspondence only, no generic ABI equivalence claim'
        else:
            raw_owner = opt_owner
            exact_name = [m for m in data['raw'][raw_owner]['methods'] if (m['name'], m['desc']) == (opt_name, opt_desc)]
            raw = exact_name[0] if exact_name else exact(data['raw'], raw_owner, opt_name.split('$')[0], opt_desc)
            boundary = 'same owner and descriptor; optimizer name suffix removed where present'
        for flag in [8, 32, 1024]:
            assert bool(before['access'] & flag) == bool(optimized['access'] & flag)
        records.append(dict(native=member_record(owner, before), raw=[dict(**member_record(raw_owner, raw), declaring_source=source_binding(raw_owner))],
                            optimized=member_record(opt_owner, optimized), group='main', raw_boundary=boundary,
                            raw_optimized_modifier_differences={name: dict(raw=bool(raw['access'] & flag), optimized=bool(optimized['access'] & flag))
                                for name, flag in [('static', 8), ('synchronized', 32), ('abstract', 1024)] if bool(raw['access'] & flag) != bool(optimized['access'] & flag)},
                            main_evidence_pointer='/methods/' + str(i),
                            whole_native_optimized_normalized_equal=row['same_normalized_bytecode']))
    assert used_overrides == set(RAW_OVERRIDES)
    extra = set(native) - set(inventory)
    covered = set(); sources = {}; groups = []
    for group, filename, entries in helper_groups():
        selected = [(member, raws) for member, raws in entries if member in extra]
        assert selected, group
        document = json.loads((REPORTS / filename).read_text())
        # Never allow another platform's coincident obfuscated name to count as MIDP2.
        if 'builds' in document:
            matches = [b for b in document['builds'] if b['target'] == 'MIDP2']; assert len(matches) == 1
            index = document['builds'].index(matches[0])
            bodies = [(mode, '/builds/' + str(index) + p, fmt, name, body)
                      for mode, p, fmt, name, body in saved_bodies(matches[0])]
        else: bodies = saved_bodies(document)
        if filename not in sources:
            sources[filename] = dict(sha256=recover.sha(REPORTS / filename), scope=document['scope'],
                                     current_source_context=validate_source_context(bodies, data))
        for member, raws in selected:
            assert member not in covered; covered.add(member)
            owner, before = native[member]
            native_bindings = bind_saved(bodies, 'reference', owner, before)
            raw_records = []
            for raw_owner, name, desc in raws:
                method = exact(data['raw'], raw_owner, name, desc)
                raw_records.append(dict(**member_record(raw_owner, method),
                                        declaring_source=source_binding(raw_owner),
                                        whole_body_bindings=bind_saved(bodies, 'raw', raw_owner, method)))
            records.append(dict(native=member_record(owner, before), raw=raw_records,
                                group=group, evidence_report=filename, whole_native_body_bindings=native_bindings,
                                raw_boundary='See the retained whole-method/compiler-expression proof and its explicit scope; no equality inferred from membership.'))
        groups.append(dict(group=group, methods=len(selected), evidence_report=filename))
    assert covered == extra, ('Unaccounted/extraneous native helpers', extra - covered, covered - extra)
    assert len(records) == len(native) == 1201
    assert {r['native']['member'] for r in records} == set(native)
    assert len(covered) == 72
    # Preserve a repository-relative snapshot to make --skip-build's supplied-artifact
    # boundary visible. The default performs a fresh authoring build from these inputs.
    source_files = sorted((recover.ROOT / 'src').rglob('*.java'))
    snapshot = {p.relative_to(recover.ROOT).as_posix(): recover.sha(p) for p in source_files}
    report = dict(scope='Complete declared-method accounting for the preserved May MIDP2-RU JAR, not whole-program equivalence or original source spelling recovery. '
                  'Every native owner/name/descriptor occurs exactly once. Main entries bind actual raw and optimized declarations; '
                  '27 explicit raw ABI/owner correspondences remain visible and are not considered behavior proofs. '
                  'Raw/optimized static, synchronized and abstract modifier differences are recorded, not silently treated as equal. '
                  'All 72 additional methods bind their whole current native and actual raw/compiler-accessor bodies to designated retained evidence reports. '
                  'Saved normalized bodies are compared with the same explicit audit symbol map; literal bodies include all dumped metadata, code and handlers. '
                  'Every extracted owner-qualified raw/optimized whole context body occurrence in the selected MIDP2 evidence is also checked against current classes. '
                  'Report SHA and JSON pointers identify the exact evidence. Individual evidence scopes and runtime captures remain in force. '
                  'This does not extend MIDP2 subset equality to other platforms/locales, arbitrary argument domains, reflection or physical devices.',
                  build_boundary='Existing supplied artifacts (--skip-build); source snapshot recorded, no fresh compilation claimed.' if skip_build else 'Fresh full MIDP2-RU authoring build from current src, then current-class audit.',
                  input_sha256={mode: recover.sha(path) for mode, path in paths.items()},
                  source_snapshot=snapshot, main_evidence=dict(file='source-bytecode-comparison.json', sha256=recover.sha(REPORTS / 'source-bytecode-comparison.json')),
                  evidence_reports=sources, totals=dict(native_methods=len(native), main_methods=len(inventory), additional_methods=len(covered),
                  raw_explicit_ABI_or_owner_correspondences=len(used_overrides), compiler_expression_groups=len(EXPRESSIONS),
                  main_raw_optimized_modifier_difference_methods=sum(bool(r.get('raw_optimized_modifier_differences')) for r in records),
                  rebound_source_context_body_occurrences=sum(s['current_source_context']['whole_body_occurrences'] for s in sources.values()),
                  main_normalized_equal=sum(r.get('whole_native_optimized_normalized_equal', False) for r in records)),
                  groups=groups, methods=sorted(records, key=lambda r: r['native']['member']))
    (REPORTS / 'source-method-accounting.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS method accounting: 1201 actual native declarations = 1129 main + 72 whole-body-bound helpers; 27 explicit raw ABI/owner correspondences; no whole-program equivalence claim')
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--skip-build', action='store_true', help='Use existing artifacts; record that compilation was not performed')
    main(parser.parse_args().skip_build)
