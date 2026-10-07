#!/usr/bin/env python3
"""Retain complete roster callback/page bodies and exception tables on three platforms."""
import difflib
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from test_roster_keys import configuration


def main():
    recover.bootstrap();audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    builds = []
    for target, platform in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        evidence = []
        for mode, path in [('reference', recover.ROOT / f'preservation/wayback-originals/Jimm_{platform}_RU/Jimm.jar'),
                           ('raw', recover.ROOT / f'build/source/{target}-RU/classes.jar'),
                           ('optimized', recover.ROOT / f'dist/source/Jimm-{target}-RU.jar')]:
            data = classes(path, cp)
            spec, vl, callback = configuration(target, mode == 'reference', data)
            cl = next(c for c in data if c['name'] == spec['list'])
            other = {key: next(m for m in cl['methods'] if m['name'] == name and m['desc'] == desc)
                     for key, name, desc in [('cursor', 'a' if mode == 'reference' else 'vlCursorMoved', '(L' + spec['vl'] + ';)V'),
                                            ('click', 'b' if mode == 'reference' else 'vlItemClicked', '(L' + spec['vl'] + ';)V'),
                                            ('images', 'a' if mode == 'reference' else 'getImageList', '()Lf;' if mode == 'reference' else '()LDrawControls/ImageList;')]}
            assert all(other[k]['code'] == ['177'] and not other[k]['handlers'] and other[k]['access'] & 9 == 1 for k in ['cursor', 'click'])
            images = other['images']
            image_field = spec['list'] + '.a Lf;' if mode == 'reference' else 'jimm/ContactList.imageList LDrawControls/ImageList;'
            assert images['code'] == ['178 ' + image_field, '176'] and images['access'] == 9 and images['handlers'] == []
            pages = [m for m in vl['methods'] if m['desc'] == '(Z)V' and any(s.startswith('182 ' + vl['name'] + '.') and s.endswith('(IZ)V') for s in m['code'])]
            assert len(pages) == 1
            page = pages[0]
            assert page['access'] & 9 == 1 and page['handlers'] == []
            calls = [s[4:] for s in page['code'] if s.startswith('182 ')]
            assert len(calls) == 3 and calls[0] == calls[1] and calls[0].endswith('()I') and calls[2].endswith('(IZ)V')
            mapping = {spec['vl'] + '.' + page['name'] + '(Z)V': 'DrawControls/VirtualList.moveCursorByPage(Z)V',
                       calls[0]: 'DrawControls/VirtualList.getVisCount()I', calls[2]: 'DrawControls/VirtualList.moveCursor(IZ)V',
                       spec['vl'] + '.' + spec['gameAction'] + '(I)I': 'DrawControls/VirtualList.getGameAction(I)I'}
            mapping[image_field] = 'jimm/ContactList.imageList LDrawControls/ImageList;'
            if mode == 'reference' and target == 'MIDP2':
                mapping.update(audit.SYMBOLS)
            else:
                mapping.update({spec[k]: name for k, name in [('list', 'jimm/ContactList'), ('vl', 'DrawControls/VirtualList'),
                                                              ('tree', 'DrawControls/VirtualTree'), ('node', 'DrawControls/TreeNode'),
                                                              ('contact', 'jimm/ContactItem')]})
            def normalize(code):
                result = []
                for s in code:
                    if mode == 'reference' and target == 'MIDP2':
                        s = audit.normalized([s])[0]
                    op, sep, value = s.partition(' ')
                    value = mapping.get(value, value)
                    for k in ['list', 'vl', 'tree', 'node', 'contact']:
                        value = value.replace('L' + spec[k] + ';', 'L' + {'list': 'jimm/ContactList', 'vl': 'DrawControls/VirtualList', 'tree': 'DrawControls/VirtualTree', 'node': 'DrawControls/TreeNode', 'contact': 'jimm/ContactItem'}[k] + ';')
                    result.append(op + sep + value)
                return result
            page_call = '182 ' + spec['vl'] + '.' + page['name'] + '(Z)V'
            action_calls = [s for s in callback['code'] if s in ['182 ' + spec['vl'] + '.' + spec['gameAction'] + '(I)I', '184 ' + spec['vl'] + '.' + spec['gameAction'] + '(I)I']]
            assert len(action_calls) == 1 and callback['code'].count(page_call) == 2
            at = callback['code'].index(action_calls[0])
            guards = [i for i in range(max(0, at - 6), at) if callback['code'][i].startswith('160 ')]
            assert len(guards) == 1
            assert callback['code'][guards[0] - 1] == ('6' if target == 'MOTOROLA' else '4')
            assert '16 52' in callback['code'][at:] and '16 54' in callback['code'][at:]
            switches = [s for s in callback['code'][at:] if s.startswith(('170 ', '171 '))]
            assert len(switches) == 1
            if switches[0].startswith('170 '):
                op, low, high, default, left, unused3, unused4, right = switches[0].split()
                assert (low, high) == ('2', '5') and unused3 == unused4 == default and left != default and right != default
            else:
                assert switches[0].startswith('171 [2, 5] ')
            assert callback['handlers'] and all(s.endswith(' java/lang/Exception') for s in callback['handlers'])
            assert all(int(s.split()[0]) > 0 for s in callback['handlers'])
            bodies = {key: {'method': spec['list'] + '.' + m['name'] + m['desc'], 'access': m['access'],
                            'instructions': normalize(m['code']), 'handlers': m['handlers']} for key, m in other.items()}
            bodies.update({'mode': mode, 'input_sha256': recover.sha(path), 'aliases': mapping,
                             'callback': {'method': spec['list'] + '.' + callback['name'] + callback['desc'], 'access': callback['access'],
                                          'instructions': normalize(callback['code']), 'handlers': callback['handlers']},
                             'page': {'method': spec['vl'] + '.' + page['name'] + page['desc'], 'access': page['access'],
                                      'instructions': normalize(page['code']), 'handlers': page['handlers']},
                             'trigger': int(spec['trigger'])})
            evidence.append(bodies)
        comparisons = []
        for source in evidence[1:]:
            for key in ['callback', 'page', 'cursor', 'click', 'images']:
                left, right = evidence[0][key], source[key]
                exact = left['instructions'] == right['instructions'] and left['handlers'] == right['handlers']
                if key == 'page' and source['mode'] == 'optimized':assert exact, (target, key)
                if key in ['cursor', 'click', 'images']:assert exact, (target, key)
                comparisons.append({'mode': source['mode'], 'role': key, 'same_whole_body_and_handlers': exact,
                                    'same_handlers': left['handlers'] == right['handlers'],
                                    'full_instruction_diff': list(difflib.unified_diff(left['instructions'], right['instructions'], lineterm=''))})
        builds.append({'target': target, 'evidence': evidence, 'comparisons': comparisons})
    report = {'scope': 'Five complete typed methods in reference/raw/optimized classes on three platforms. All instructions, local slots, '
                       'jumps and handler bounds are retained. The optimized page helper must match its 14-instruction native body. '
                       'The callback retains explicit whole-body differences from compiler/optimizer and resource-name rewriting. '
                       'Its platform trigger, actual action lookup, two page calls, numeric 4/6 exclusions and Exception handlers are checked. '
                       'moveCursorByPage is an inferred source name; the lost author archive does not establish that spelling.', 'builds': builds}
    (recover.ROOT / 'preservation/reports/source-roster-keys-bytecode.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS roster key bytecode: whole callback/page bodies, three platform guards, complete diffs and handlers')


if __name__ == '__main__':
    main()
