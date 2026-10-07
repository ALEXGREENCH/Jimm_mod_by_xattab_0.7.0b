#!/usr/bin/env python3
"""Replay unique full-body method identification without circular name assumptions."""
import json
import os
import recover
import audit_source as audit
from audit_util import member, translated

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-method-graph.json'


def reference_key(entry):
    return entry['reference_owner'] + '.' + entry['reference_name'] + entry['reference_desc']


def source_key(entry):
    return entry['source_owner'] + '.' + entry['source_name'] + entry['source_desc']


def authoring_proof(entry, before, after, authoring):
    owner = entry['authoring_owner']
    assert owner == audit.CLASSES[entry['reference_owner']]
    name, desc = entry['authoring_name'], entry['authoring_desc']
    declaration = member(authoring[owner]['methods'], name, desc)
    optimizer_change = entry['authoring_optimizer_change']
    if optimizer_change is None:
        assert declaration['access'] & 1064 == before['access'] & 1064, entry
    else:
        assert optimizer_change['kind'] == 'staticized_instance'
        assert (owner, name, desc) == ('jimm/comm/UpdateContactListAction', 'sendCLI_ADDEND', '()V')
        assert declaration['access'] == 2 and before['access'] == 10
        assert declaration['code'] == after['code'] and declaration['handlers'] == after['handlers']
        assert not any(i.startswith('25 0') for i in declaration['code'])
    specialization = entry['authoring_specialization']
    if specialization is None:
        assert desc == entry['source_desc'], entry
    elif specialization['kind'] == 'constant_argument':
        assert (owner, name, desc, entry['source_desc']) == ('jimm/Options', 'getLong', '(I)J', '()J')
        assert specialization['slot'] == 0 and specialization['value'] == 192
        assert declaration['code'].count('21 0') == 1 and declaration['code'][1] == '21 0'
        specialized = list(declaration['code'])
        specialized[1] = '17 192'
        assert specialized == after['code'] and declaration['handlers'] == after['handlers']
        constant = member(authoring[owner]['fields'], 'OPTION_ONLINE_STATUS', 'I')
        assert constant['value'] == '192' and constant['access'] & 24 == 24
    elif specialization['kind'] == 'unused_argument':
        assert (owner, name, desc, entry['source_desc']) == ('jimm/ContactList', 'optionsChanged', '(ZZ)V', '(Z)V')
        assert specialization['slot'] == 1
        assert declaration['code'] == after['code'] and declaration['handlers'] == after['handlers']
        for instruction in declaration['code']:
            parts = instruction.split(' ')
            if parts[0] in {'21', '22', '23', '24', '25', '54', '55', '56', '57', '58', '132', '169'}:
                assert parts[1] != '1', instruction
    else:
        raise AssertionError(('Unknown authoring specialization', specialization))
    return {'source': owner + '.' + name + desc, 'declaration_and_modifiers_verified': True,
            'reference_access_flags': before['access'], 'authoring_access_flags': declaration['access'],
            'same_static_synchronized_abstract_flags': declaration['access'] & 1064 == before['access'] & 1064,
            'same_descriptor': desc == entry['source_desc'], 'specialization': specialization,
            'optimizer_change': optimizer_change}


def collect(old, optimized, authoring, entries=None):
    entries = audit.EXACT_METHODS['methods'] if entries is None else entries
    keys = {reference_key(e) for e in entries}
    assert len(keys) == len(entries)
    targets = {source_key(e) for e in entries}
    assert len(targets) == len(entries), 'Different native methods may not claim the same source body'
    rounds = sorted({e['round'] for e in entries})
    assert rounds == list(range(1, max(rounds) + 1)), rounds
    symbols = dict(audit.SYMBOLS)
    result = []
    try:
        # Undo this inventory's aliases before any body is compared. A round may
        # use only established identities and matches proven in earlier rounds.
        audit.SYMBOLS.clear()
        audit.SYMBOLS.update(audit.EXACT_BASE_SYMBOLS)
        for entry in entries:
            assert entry['previous_symbol'] == audit.EXACT_BASE_SYMBOLS.get(reference_key(entry)), entry
        used_targets = set()
        for owner, name, desc, source_name in audit.METHODS:
            if owner + '.' + name + desc in keys:
                continue
            source_owner = audit.SOURCE_OWNERS.get(owner, audit.CLASSES[owner])
            candidate = audit.resolve_method(optimized[source_owner]['methods'], source_name, translated(desc))
            used_targets.add(source_owner + '.' + candidate['name'] + candidate['desc'])
        assert not targets & used_targets, 'A new identity reuses an already identified source method'
        for round_no in rounds:
            batch = [e for e in entries if e['round'] == round_no]
            for entry in batch:
                owner, name, desc = entry['reference_owner'], entry['reference_name'], entry['reference_desc']
                before = member(old[owner]['methods'], name, desc)
                source_owner = audit.SOURCE_OWNERS.get(owner, audit.CLASSES[owner])
                assert entry['source_owner'] == source_owner and entry['source_desc'] == translated(desc)
                assert not before['access'] & 1024, 'An abstract declaration is not an executable body'
                left = audit.normalized(before['code'])
                handlers = audit.normalized_handlers(before['handlers'])
                candidates = [m for m in optimized[source_owner]['methods'] if m['desc'] == translated(desc)
                              and m['access'] & 1064 == before['access'] & 1064
                              and (m['name'] == name if name.startswith('<') else not m['name'].startswith('<'))
                              and m['code'] == left and m['handlers'] == handlers]
                assert len(candidates) == 1, ('Unique full instruction match', reference_key(entry), round_no,
                                              [m['name'] for m in candidates])
                after = candidates[0]
                assert (after['name'], after['desc']) == (entry['source_name'], entry['source_desc']), entry
                assert len(left) == entry['reference_instructions'], entry
                proof = authoring_proof(entry, before, after, authoring)
                result.append({'reference': reference_key(entry), 'source': source_key(entry), 'round': round_no,
                               'unique_full_body_match': True, 'instructions': len(left),
                               'static_synchronized_and_abstract_modifiers_verified': True,
                               'same_normalized_instructions': True, 'same_normalized_handlers': True,
                               'reference_normalized_sha256': audit.digest(left),
                               'source_normalized_sha256': audit.digest(after['code']), 'authoring': proof})
            # No member in this batch can help identify another member of the same batch.
            for entry in batch:
                audit.SYMBOLS[reference_key(entry)] = source_key(entry)
    finally:
        audit.SYMBOLS.clear()
        audit.SYMBOLS.update(symbols)
    return result


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run(
            [recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    reference = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    optimized = ROOT / 'build/source/MIDP2-RU/preverified.jar'
    authoring = ROOT / 'build/source/MIDP2-RU/classes.jar'
    methods = collect(dump(reference), dump(optimized), dump(authoring))
    report = {'scope': audit.EXACT_METHODS['scope'], 'reference_sha256': recover.sha(reference),
              'source_optimized_class_jar_sha256': recover.sha(optimized),
              'source_unoptimized_class_jar_sha256': recover.sha(authoring),
              'methods_verified': len(methods), 'exact_instructions': sum(m['instructions'] for m in methods),
              'rounds': max(m['round'] for m in methods),
              'authoring_descriptor_specializations': sum(not m['authoring']['same_descriptor'] for m in methods),
              'authoring_private_helper_staticizations': sum(bool(m['authoring']['optimizer_change']) for m in methods),
              'methods': methods}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS method graph:', len(methods), 'unique complete bodies;', report['exact_instructions'],
          'instructions;', report['rounds'], 'dependency rounds;', report['authoring_descriptor_specializations'],
          'parameter specializations /', report['authoring_private_helper_staticizations'], 'private helper staticizations')


if __name__ == '__main__':
    main()
