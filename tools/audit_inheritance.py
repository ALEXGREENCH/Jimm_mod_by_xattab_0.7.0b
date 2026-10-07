#!/usr/bin/env python3
"""Validate all original abstract declarations and their real override identities."""
import json
import os
import recover
import audit_source as audit
from audit_util import member, translated

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-inheritance.json'


def split_method(key):
    owner, rest = key.split('.', 1)
    name, desc = rest.split('(', 1)
    return owner, name, '(' + desc


def collect(old, optimized, authoring):
    inventory = audit.INHERITED_SYMBOLS
    native_abstracts = {(owner, m['name'], m['desc']) for owner, c in old.items()
                        for m in c['methods'] if m['access'] & 1024}
    assert native_abstracts == {(m['reference_owner'], m['reference_name'], m['reference_desc'])
                               for m in inventory['abstract_methods']}

    def ancestors(owner):
        if owner not in old:
            return set()
        parents = [old[owner]['super'], *old[owner]['interfaces']]
        result = set(parents)
        for parent in parents:
            result |= ancestors(parent)
        return result

    calls = []
    for before_key, after_key in inventory['method_symbols'].items():
        bo, bn, bd = split_method(before_key)
        so, sn, sd = split_method(after_key)
        assert so == audit.SOURCE_OWNERS.get(bo, audit.CLASSES[bo])
        assert sd == translated(bd), (before_key, after_key)
        before = member(old[bo]['methods'], bn, bd)
        available = [m for m in optimized[so]['methods'] if (m['name'], m['desc']) == (sn, sd)]
        if available:
            assert len(available) == 1
            after = available[0]
        else:
            # The inherited equals declaration is absent only after optimization.
            assert before_key == 'bs.equals(Ljava/lang/Object;)Z'
            after = member(authoring[so]['methods'], sn, sd)
        assert before['access'] & 1064 == after['access'] & 1064, (before_key, after_key)
        calls.append({'reference': before_key, 'source': after_key,
                      'signature_and_modifiers_verified': True,
                      'source_optimized_method_present': bool(available)})

    declarations = []
    for entry in inventory['abstract_methods']:
        bo, bn, bd = entry['reference_owner'], entry['reference_name'], entry['reference_desc']
        so, sn, sd = entry['source_owner'], entry['source_name'], entry['source_desc']
        before = member(old[bo]['methods'], bn, bd)
        after = member(authoring[so]['methods'], sn, sd)
        assert sd == translated(bd) and so == audit.CLASSES[bo]
        assert before['access'] & 1064 == after['access'] & 1064 == 1024, entry
        assert before['access'] == after['access'], ('Abstract access flags', entry)
        assert not before['code'] and not before['handlers']
        assert not after['code'] and not after['handlers']
        available = [m for m in optimized[so]['methods'] if (m['name'], m['desc']) == (sn, sd)]
        assert bool(available) == entry['same_optimized_signature'], entry
        assert len(available) <= 1
        if available:
            assert available[0]['access'] & 1064 == 1024
            if available[0]['access'] != before['access']:
                # This compiler/optimizer widens only these two protected members.
                assert so == 'DrawControls/VirtualList' and (sn, sd) in {
                    ('getSize', '()I'), ('get', '(ILDrawControls/ListItem;)V')}
                assert before['access'] == 1028 and available[0]['access'] == 1025
            assert not available[0]['code'] and not available[0]['handlers']
        assert entry['override_witnesses'], entry
        witnesses = []
        for key in entry['override_witnesses']:
            wo, wn, wd = split_method(key)
            assert bo in ancestors(wo), (entry, key)
            assert (wn, wd) == (bn, bd), (entry, key)
            witness = member(old[wo]['methods'], wn, wd)
            assert not witness['access'] & (8 | 1024), key
            actual_key = audit.SYMBOLS[key]
            ao, an, ad = split_method(actual_key)
            assert ao == audit.CLASSES[wo]
            # This is the essential check when multiple methods have equal bodies.
            assert (an, ad) == (sn, sd), ('Override identity mismatch', key, actual_key, so, sn, sd)
            actual = member(authoring[ao]['methods'], an, ad)
            assert not actual['access'] & (8 | 1024), actual_key
            witnesses.append({'reference': key, 'source': actual_key,
                              'native_hierarchy_and_signature_verified': True,
                              'authoring_override_identity_verified': True})
        declarations.append(dict(entry, authoring_declaration_verified=True,
                                 abstract_modifier_verified=True, declaration_access_flags_verified=True,
                                 reference_access_flags=before['access'], authoring_access_flags=after['access'],
                                 optimized_access_flags=available[0]['access'] if available else None,
                                 same_optimized_access_flags=(available[0]['access'] == before['access']) if available else None,
                                 witnesses=witnesses))

    corrections = []
    for key, target in inventory['group_override_corrections'].items():
        owner, name, desc = split_method(key)
        assert owner == 'bb' and desc == '()I'
        assert audit.SYMBOLS[key] == target
        parent = next(m for m in declarations if (m['reference_owner'], m['reference_name'], m['reference_desc'])
                      == ('bs', name, desc))
        assert any(w['reference'] == key for w in parent['witnesses'])
        assert any(w['reference'] == 'z.' + name + desc for w in parent['witnesses'])
        before = member(old[owner]['methods'], name, desc)
        ao, an, ad = split_method(target)
        after = member(optimized[ao]['methods'], an, ad)
        assert before['code'] == after['code'] == ['2', '172']
        assert before['handlers'] == after['handlers'] == []
        corrections.append({'reference': key, 'source': target,
                            'identity_proven_by_interface_and_both_implementations': True,
                            'same_constant_body': True})
    return calls, declarations, corrections


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
    calls, declarations, corrections = collect(dump(reference), dump(optimized), dump(authoring))
    report = {'scope': audit.INHERITED_SYMBOLS['scope'] + ' Abstract declarations are counted separately from '
                      'executable bodies. This is a complete abstract-member inventory for MIDP2/RU, '
                      'without claiming whole-program behavior or original author spellings.',
              'reference_sha256': recover.sha(reference),
              'source_optimized_class_jar_sha256': recover.sha(optimized),
              'source_unoptimized_class_jar_sha256': recover.sha(authoring),
              'typed_call_aliases_verified': len(calls), 'original_abstract_declarations': len(declarations),
              'abstract_optimized_signatures_verified': sum(m['same_optimized_signature'] for m in declarations),
              'group_override_identities_corrected': len(corrections),
              'calls': calls, 'abstract_declarations': declarations, 'group_overrides': corrections}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS inheritance:', len(declarations), 'original abstract declarations;',
          sum(m['same_optimized_signature'] for m in declarations), 'optimized signatures;', len(calls),
          'typed call aliases;', len(corrections), 'GroupItem override identities')


if __name__ == '__main__':
    main()
