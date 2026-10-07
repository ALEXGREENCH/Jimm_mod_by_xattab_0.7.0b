#!/usr/bin/env python3
"""Audit native GUID initialization and retained catalog/contact signatures on all three platforms."""
import json
import os
import recover
import audit_source as audit


def main():
    recover.bootstrap()
    audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT,
                 recover.ROOT / 'tools/recovery/BytecodeDump.java'])

    def dump(path):
        return {c['name']: c for c in json.loads(recover.run(
            [recover.java(), '-cp', recover.cp([audit.OUT, cp]), 'BytecodeDump', path], capture=True))}

    report = {'scope': 'All instructions of native 37-GUID array construction, through its actual field store, '
                       'and 12 comparable retained catalog/GUID/contact signatures per platform. '
                       'Normalization maps confirmed owners and members, retaining local slots and branches. '
                       'The full catalog initializer also contains different locale keys and is not declared '
                       'identical. MIDP2 functional catalog comparisons are recorded separately; this audit '
                       'does not establish full Motorola/Siemens parser or physical-device equivalence.', 'builds': []}
    for target, original_target, status, guid, contact in [('MIDP2', 'MIDP2', 'bj', 'bh', 'z'),
                                                         ('MOTOROLA', 'Moto', 'bi', 'bg', 'z'),
                                                         ('SIEMENS2', 'Siemens2', 'bh', 'bf', 'y')]:
        reference = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_' + original_target + '_RU') / 'Jimm.jar'
        source = recover.ROOT / 'build/source' / (target + '-RU') / 'preverified.jar'
        old, new = dump(reference), dump(source)
        initializer = next(m for m in old[status]['methods'] if m['name'] == '<clinit>')
        util_calls = [line.split()[1].split('.')[0] for line in initializer['code']
                      if line.startswith('184 ') and line.endswith('.a(Ljava/lang/String;CI)[B')]
        if len(util_calls) != 37 or len(set(util_calls)) != 1:
            raise AssertionError('Unconfirmed native GUID decoder: ' + target)
        util = util_calls[0]
        classes = {status: 'jimm/comm/XStatus', guid: 'jimm/comm/GUID', util: 'jimm/comm/Util',
                   'e': 'DrawControls/Icon', 'f': 'DrawControls/ImageList', contact: 'jimm/ContactItem'}
        members = {
            guid + '.a [B': 'jimm/comm/GUID.guid [B',
            status + '.a [L' + guid + ';': 'jimm/comm/XStatus.xguids [Ljimm/comm/GUID;',
            status + '.a [Ljava/lang/String;': 'jimm/comm/XStatus.xstatus [Ljava/lang/String;',
            status + '.a Lf;': 'jimm/comm/XStatus.imageList LDrawControls/ImageList;',
            status + '.a I': 'jimm/comm/XStatus.index I',
            util + '.a(Ljava/lang/String;CI)[B': 'jimm/comm/Util.explodeToBytes(Ljava/lang/String;CI)[B',
            'f.a(I)Le;': 'DrawControls/ImageList.elementAt(I)LDrawControls/Icon;',
            contact + '.a L' + status + ';': 'jimm/ContactItem.xstatus Ljimm/comm/XStatus;',
            contact + '.a()L' + status + ';': 'jimm/ContactItem.getXStatus()Ljimm/comm/XStatus;',
            status + '.a([B)V': 'jimm/comm/XStatus.setXStatus([B)V',
        }
        # Confirm the ResourceBundle member through the actual label getter's calls.
        string_calls = [instruction.split()[1] for method in old[status]['methods']
                        if method['desc'] == '(I)Ljava/lang/String;' for instruction in method['code']
                        if instruction.startswith('184 ') and instruction.endswith('(Ljava/lang/String;)Ljava/lang/String;')]
        for call in string_calls:
            members[call] = 'jimm/util/ResourceBundle.getString(Ljava/lang/String;)Ljava/lang/String;'
        audit.CLASSES = classes
        audit.SYMBOLS = members
        store = '179 ' + status + '.a [L' + guid + ';'
        old_prefix = initializer['code'][:initializer['code'].index(store) + 1]
        source_initializer = next(m for m in new['jimm/comm/XStatus']['methods'] if m['name'] == '<clinit>')
        store = '179 jimm/comm/XStatus.xguids [Ljimm/comm/GUID;'
        new_prefix = source_initializer['code'][:source_initializer['code'].index(store) + 1]
        normalized = audit.normalized(old_prefix)
        if normalized != new_prefix or len(util_calls) != 37:
            raise AssertionError('Native GUID initialization differs: ' + target)
        methods = []
        selection = [(guid, '<init>', '([B)V', '<init>'),
                     (status, '<init>', '()V', '<init>'), (status, 'a', '(I)V', 'setStatusIndex'),
                     (status, 'a', '(I)L' + guid + ';', 'getStatusGUID'),
                     (status, 'a', '(I)Le;', 'getStatusImage'),
                     (status, 'a', '(I)Ljava/lang/String;', 'getStatusAsString'),
                     (status, 'a', '()I', 'getXStatusCount'), (status, 'b', '()I', 'getStatusIndex'),
                     (status, 'a', '()Lf;', 'getXStatusImageList'),
                     (status, '<clinit>', '()V', '<clinit>'),
                     (contact, 'a', '([B)V', 'setXStatus'), (contact, 'a', '()L' + status + ';', 'getXStatus')]
        for owner, name, desc, source_name in selection:
            before = next(m for m in old[owner]['methods'] if (m['name'], m['desc']) == (name, desc))
            source_desc = desc
            for a, b in classes.items():
                source_desc = source_desc.replace('L' + a + ';', 'L' + b + ';')
            after = next(m for m in new[classes[owner]]['methods']
                         if (m['name'], m['desc']) == (source_name, source_desc))
            if (before['access'] & 40) != (after['access'] & 40):
                raise AssertionError('Static/synchronized mismatch: ' + target + ' ' + source_name)
            instructions = audit.normalized(before['code']) == after['code']
            handlers = audit.normalized_handlers(before['handlers']) == after['handlers']
            methods.append({'reference': owner + '.' + name + desc,
                            'source': classes[owner] + '.' + source_name + source_desc,
                            'signature_verified': True, 'static_modifier_verified': True,
                            'synchronized_modifier_verified': True,
                            'same_normalized_bytecode': instructions and handlers})
        report['builds'].append({'target': target, 'reference_sha256': recover.sha(reference),
                                'source_class_jar_sha256': recover.sha(source),
                                'guid_entries': 37, 'guid_initialization_instructions': len(normalized),
                                'same_guid_initialization_bytecode': True,
                                'guid_initialization_sha256': audit.digest(normalized), 'methods': methods})
    path = recover.ROOT / 'preservation/reports/source-xstatus-bytecode.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS XStatus bytecode: 3 native GUID initializers, ' + str(sum(len(b['methods']) for b in report['builds']))
          + ' signatures, ' + str(sum(m['same_normalized_bytecode'] for b in report['builds'] for m in b['methods']))
          + ' instruction/handler matches')


if __name__ == '__main__':
    main()
