#!/usr/bin/env python3
"""Validate the complete typed Util inventory; retain strict whole-method differences."""
import json
import os
import recover
import audit_source as audit

ROOT = recover.ROOT
REPORT = ROOT / 'preservation/reports/source-util-bytecode.json'


def translated(desc):
    for before, after in audit.CLASSES.items():
        desc = desc.replace('L' + before + ';', 'L' + after + ';')
    return desc


def member(items, name, desc):
    found = [m for m in items if (m['name'], m['desc']) == (name, desc)]
    assert len(found) == 1, (name, desc)
    return found[0]


def initializers(owner):
    blocks, part = {}, []
    for instruction in member(owner['methods'], '<clinit>', '()V')['code']:
        if instruction.startswith('179 ' + owner['name'] + '.'):
            key = instruction[4:]
            assert key not in blocks, key
            blocks[key], part = part, []
        else:
            part.append(instruction)
    return blocks


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path, owner):
        data = json.loads(recover.run([recover.java(), '-cp', recover.cp([audit.OUT, cp]),
                                      'BytecodeDump', path], capture=True))
        return next(c for c in data if c['name'] == owner)

    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    optimized = ROOT / 'build/source/MIDP2-RU/preverified.jar'
    authoring = ROOT / 'build/source/MIDP2-RU/classes.jar'
    old = dump(original, 'co')
    new = dump(optimized, 'jimm/comm/Util')
    source = dump(authoring, 'jimm/comm/Util')
    inventory = audit.UTIL_SYMBOLS
    assert {(m['name'], m['desc']) for m in old['methods']} == {
        (m['reference_name'], m['reference_desc']) for m in inventory['methods']}
    assert len(inventory['methods']) == len(old['methods']) == 87
    assert {(f['name'], f['desc']) for f in old['fields']} == {
        (f['reference_name'], f['desc']) for f in inventory['fields']}
    assert len(inventory['fields']) == len(old['fields']) == 62
    methods, specialized = [], []
    for entry in inventory['methods']:
        before = member(old['methods'], entry['reference_name'], entry['reference_desc'])
        declaration = member(source['methods'], entry['source_name'], entry['source_declaration_desc'])
        assert before['access'] & 40 == declaration['access'] & 40
        result = dict(entry, authoring_declaration_verified=True)
        if entry['source_optimized_name'] is None:
            assert entry['reference_name'] == '<init>'
            assert not any(m['name'] == '<init>' for m in new['methods'])
        else:
            after = member(new['methods'], entry['source_optimized_name'], entry['source_optimized_desc'])
            assert before['access'] & 40 == after['access'] & 40
            same = translated(before['desc']) == after['desc']
            assert same == entry['same_optimized_signature']
            if same:
                result.update(reference_instructions=len(before['code']), source_instructions=len(after['code']),
                              same_normalized_instructions=audit.normalized(before['code']) == after['code'],
                              same_normalized_handlers=audit.normalized_handlers(before['handlers']) == after['handlers'])
                result['same_normalized_bytecode'] = (result['same_normalized_instructions']
                                                      and result['same_normalized_handlers'])
            else:
                specialized.append(entry['source_name'])
        methods.append(result)
    assert sorted(specialized) == ['byteArrayEquals', 'strToIntDef', 'writeAsciizTLVInterest']
    left, right = initializers(old), initializers(new)
    fields = []
    for entry in inventory['fields']:
        before = member(old['fields'], entry['reference_name'], entry['desc'])
        after = member(new['fields'], entry['source_name'], translated(entry['desc']))
        declared = member(source['fields'], entry['source_name'], translated(entry['desc']))
        assert before['access'] & 8 == after['access'] & 8 == declared['access'] & 8
        raw = 'co.' + before['name'] + ' ' + before['desc']
        named = 'jimm/comm/Util.' + after['name'] + ' ' + after['desc']
        a, b = audit.normalized(left[raw]), right[named]
        same_assigned_value = b[-len(a):] == a
        default_zero_omitted = False
        if not same_assigned_value:
            # Identity evidence for this array only. Whole <clinit> is compared
            # without removing this store or any other compiler difference.
            assert raw == 'co.a [I' and named == 'jimm/comm/Util.monthIndexes [I'
            assert a[:6] == ['16 12', '188 10', '89', '3', '3', '79']
            assert b == a[:2] + a[6:]
            default_zero_omitted = True
        fields.append(dict(entry, declaration_verified=True,
                           same_assigned_value_instructions=same_assigned_value,
                           default_zero_store_omitted=default_zero_omitted,
                           preceding_source_instructions=max(0, len(b) - len(a))))
    matched = sum(m.get('same_normalized_bytecode', False) for m in methods)
    report = {'scope': inventory['scope'] + ' Whole-method comparison includes every instruction, local slot, '
                      'branch and exception region. A field identity is not a whole-initializer equality claim. '
                      'Canonical names continue the inherited source; lost author spellings are not proven.',
              'reference_sha256': recover.sha(original), 'source_optimized_class_jar_sha256': recover.sha(optimized),
              'source_unoptimized_class_jar_sha256': recover.sha(authoring),
              'method_inventory': len(methods), 'field_inventory': len(fields),
              'same_optimized_signatures': sum(m['same_optimized_signature'] for m in methods),
              'same_normalized_bytecode_methods': matched, 'methods': methods, 'fields': fields}
    REPORT.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS Util inventory: 87 methods, 62 fields; 83 same optimized signatures;', matched,
          'exact instruction/handler sequences')


if __name__ == '__main__':
    main()
