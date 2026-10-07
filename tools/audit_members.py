#!/usr/bin/env python3
"""Validate class identities and typed declarations added to the main source audit."""
import json
import os
import recover
import audit_source as audit
from audit_util import member, translated

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-member-inventory.json'


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run(
            [recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    optimized = ROOT / 'build/source/MIDP2-RU/preverified.jar'
    authoring = ROOT / 'build/source/MIDP2-RU/classes.jar'
    old, new, source = dump(original), dump(optimized), dump(authoring)
    assert set(old) == set(audit.CLASSES), 'The complete original class set must be named'
    classes = []
    for before, after in audit.CLASSES.items():
        assert after in source, (before, after)
        parent = audit.CLASSES.get(old[before]['super'], old[before]['super'])
        interfaces = {audit.CLASSES.get(n, n) for n in old[before]['interfaces']}
        # Record hierarchy differences introduced by optimizer merging/removal;
        # naming a class does not declare its hierarchy or whole body identical.
        classes.append({'reference': before, 'source': after, 'authoring_class_present': True,
                        'same_superclass': parent == source[after]['super'],
                        'same_interfaces': interfaces == set(source[after]['interfaces']),
                        'reference_parent': parent, 'source_parent': source[after]['super']})
    fields = []
    for raw, named in audit.REMAINING_SYMBOLS['fields'].items():
        before_owner, rest = raw.split('.', 1); before_name, before_desc = rest.split(' ', 1)
        after_owner, rest = named.split('.', 1); after_name, after_desc = rest.split(' ', 1)
        assert after_desc == translated(before_desc), (raw, named)
        before = member(old[before_owner]['fields'], before_name, before_desc)
        declaration = member(source[after_owner]['fields'], after_name, after_desc)
        assert before['access'] & 72 == declaration['access'] & 72, (raw, named)
        optimized_fields = [f for f in new[after_owner]['fields']
                            if (f['name'], f['desc']) == (after_name, after_desc)]
        assert len(optimized_fields) <= 1
        if optimized_fields:
            assert before['access'] & 72 == optimized_fields[0]['access'] & 72
        fields.append({'reference': raw, 'source': named, 'authoring_declaration_verified': True,
                       'static_and_volatile_modifiers_verified': True,
                       'source_optimized_field_present': bool(optimized_fields)})
    contact_fields = [f for f in fields if f['reference'].startswith('z.')]
    assert len(contact_fields) == len(old['z']['fields']) == 42
    assert len({f['source'] for f in contact_fields}) == 42
    methods = []
    for entry in audit.REMAINING_SYMBOLS['additional_methods']:
        before = member(old[entry['reference_owner']]['methods'], entry['reference_name'], entry['reference_desc'])
        after = member(new[entry['source_owner']]['methods'], entry['source_name'], entry['source_desc'])
        assert entry['source_desc'] == translated(before['desc'])
        assert before['access'] & 1064 == after['access'] & 1064
        same_instructions = audit.normalized(before['code']) == after['code']
        same_handlers = audit.normalized_handlers(before['handlers']) == after['handlers']
        methods.append(dict(entry, signature_and_modifiers_verified=True,
                            reference_instructions=len(before['code']), source_instructions=len(after['code']),
                            same_normalized_bytecode=same_instructions and same_handlers,
                            abstract_declaration=bool(before['access'] & 1024)))
    contacts = audit.REMAINING_SYMBOLS['contact_method_inventory']
    assert len(contacts) == len(old['z']['methods']) == 66
    assert {(m['reference_name'], m['reference_desc']) for m in contacts} == {
        (m['name'], m['desc']) for m in old['z']['methods']}
    main = json.loads((ROOT / 'preservation/reports/source-bytecode-comparison.json').read_text(encoding='utf-8'))
    assert main['rebuilt_sha256'] == recover.sha(optimized), 'Run audit_source.py for the current build first'
    contact_methods = {m['reference']: m for m in main['methods'] if m['reference'].startswith('z.')}
    for entry in contacts:
        key = 'z.' + entry['reference_name'] + entry['reference_desc']
        assert (key in contact_methods) == entry['same_optimized_signature']
        if entry['source_name'] is None:
            assert entry['compiler_expression'] and not entry['same_optimized_signature']
        else:
            candidates = [m for m in source['jimm/ContactItem']['methods'] if m['name'] == entry['source_name']]
            assert candidates, (key, entry['source_name'])
            if not entry['source_name'].startswith('access$'):
                before = member(old['z']['methods'], entry['reference_name'], entry['reference_desc'])
                assert any(before['access'] & 1064 == m['access'] & 1064 for m in candidates), key
    capability_builds = []
    for target, old_target in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        reference = ROOT / 'preservation/wayback-originals' / ('Jimm_' + old_target + '_RU') / 'Jimm.jar'
        rebuilt = ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        before, after = dump(reference), dump(rebuilt)
        contact_candidates = [c for c in before.values() if any(m['name'] == '<init>'
            and m['desc'] == '(IILjava/lang/String;Ljava/lang/String;ZZ)V' for m in c['methods'])]
        assert len(contact_candidates) == 1, target
        contact = contact_candidates[0]
        expected = ['21 1', '25 0', '180 ' + contact['name'] + '.a I', '126', '153 7', '4', '172', '3', '172']
        matching = [m for m in contact['methods'] if m['desc'] == '(I)Z' and m['code'] == expected]
        assert len(matching) == 1, target
        native = matching[0]
        built = member(after['jimm/ContactItem']['methods'], 'hasCapability', '(I)Z')
        assert native['access'] & 40 == built['access'] & 40 == 32, target
        named = [s.replace(contact['name'] + '.a I', 'jimm/ContactItem.caps I') for s in expected]
        assert named == built['code'] and native['handlers'] == built['handlers'], target
        capability_builds.append({'target': target, 'reference_owner': contact['name'],
                                  'reference_name': native['name'], 'reference_sha256': recover.sha(reference),
                                  'source_optimized_class_jar_sha256': recover.sha(rebuilt),
                                  'synchronized_modifier_verified': True, 'same_normalized_bytecode': True,
                                  'instructions': len(expected)})
    report = {'scope': audit.REMAINING_SYMBOLS['scope'] + ' All original classes have an authoring-class identity; '
                      'hierarchy differences are recorded rather than suppressed. Contact synthetic expressions and '
                      'removed/specialized signatures remain explicit. Member naming is not whole-program equivalence.',
              'reference_sha256': recover.sha(original), 'source_optimized_class_jar_sha256': recover.sha(optimized),
              'source_unoptimized_class_jar_sha256': recover.sha(authoring),
              'original_classes_named': len(classes), 'new_signatures_verified': len(methods),
              'new_field_declarations_verified': len(fields), 'contact_fields_inventoried': len(contact_fields),
              'contact_methods_inventoried': len(contacts), 'contact_same_optimized_signatures': len(contact_methods),
              'contact_exact_bytecode_methods': sum(m['same_normalized_bytecode'] for m in contact_methods.values()),
              'classes': classes, 'fields': fields, 'methods': methods, 'contact_methods': contacts,
              'has_capability_builds': capability_builds}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS member inventory:', len(classes), 'original classes named;', len(methods),
          'additional signatures; ContactItem 66 methods / 42 fields /', len(contact_methods),
          'same optimized signatures')


if __name__ == '__main__':
    main()
