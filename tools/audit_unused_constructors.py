#!/usr/bin/env python3
"""Account for complete empty constructors without inventing removed optimized entries."""
import json
import os

import recover
import audit_source as audit
from audit_send_text import classes
from audit_exception_constructors import configuration as errors
from test_xstatus_platforms import configuration as statuses


def configuration(data, target):
    def pick(predicate):
        candidates = [c for c in data if predicate(c)]
        assert len(candidates) == 1, [c['name'] for c in candidates]
        return candidates[0]['name']
    guid = statuses(data)['guid']
    def has(c, desc): return any(m['desc'] == desc for m in c['methods'])
    result = {'jimm/util/ResourceBundle': errors(data)['resource'],
              'jimm/comm/OtherAction': pick(lambda c: has(c, '([L' + guid + ';)V') and all(has(c, d) for d in ['(B)V', '(I)V', '(Z)V'])),
              'jimm/comm/Util': pick(lambda c: has(c, '(Ljava/lang/String;III[BIZ)V')),
              'jimm/comm/XtrazSM': pick(lambda c: has(c, '(Ljava/lang/String;IJJLjava/lang/String;)[B'))}
    candidates = [c for c in data if has(c, '(Z)V') and has(c, '()V') and c['super'] == 'java/lang/Object'
                  and sorted(f['desc'] for f in c['fields']) in [['I', 'Ljava/util/TimerTask;', 'Z'], ['I', 'Z']]]
    if target == 'SIEMENS2': assert not candidates
    else:
        assert len(candidates) == 1
        result['DrawControls/LightControl'] = candidates[0]['name']
    return result


def main():
    recover.bootstrap(); audit.OUT.mkdir(parents=True, exist_ok=True)
    cp = recover.cp([recover.CACHE / n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC', 'javac'), '-cp', cp, '-d', audit.OUT, recover.ROOT / 'tools/recovery/BytecodeDump.java'])
    expected = ['25 0', '183 java/lang/Object.<init>()V', '177']; builds = []
    for target, old in [('MIDP2', 'MIDP2'), ('MOTOROLA', 'Moto'), ('SIEMENS2', 'Siemens2')]:
        paths = {'reference': recover.ROOT / f'preservation/wayback-originals/Jimm_{old}_RU/Jimm.jar',
                 'raw': recover.ROOT / f'build/source/{target}-RU/classes.jar',
                 'optimized': recover.ROOT / f'dist/source/Jimm-{target}-RU.jar'}
        data = {mode: classes(path, cp) for mode, path in paths.items()}
        owners = {mode: {c['name']: c for c in values} for mode, values in data.items()}
        native_map = configuration(data['reference'], target); records = []
        for canonical, native in native_map.items():
            evidence = []
            for mode in paths:
                owner = native if mode == 'reference' else canonical
                c = owners[mode].get(owner)
                ctor = None if c is None else next((m for m in c['methods'] if m['name'] == '<init>' and m['desc'] == '()V'), None)
                if mode == 'optimized': assert ctor is None
                else:
                    assert c['super'] == 'java/lang/Object' and ctor is not None
                    assert ctor['access'] == 1 and ctor['code'] == expected and ctor['handlers'] == []
                # No direct constructor call/allocation is hidden by a selected-method inventory.
                sites = [dict(owner=other['name'], complete_method=m,
                              positions=[i for i, s in enumerate(m['code']) if s in ['187 ' + owner, '183 ' + owner + '.<init>()V']])
                         for other in data[mode] for m in other['methods']
                         if any(s in ['187 ' + owner, '183 ' + owner + '.<init>()V'] for s in m['code'])]
                subclasses = [other['name'] for other in data[mode] if other['super'] == owner]
                assert sites == [] and subclasses == []
                if c is not None:
                    assert all(f['access'] & 8 for f in c['fields'])
                    assert all(m['access'] & 8 for m in c['methods'] if m['name'] != '<init>')
                evidence.append(dict(mode=mode, owner=owner, whole_constructor=ctor,
                                     class_absent=c is None, whole_fields=None if c is None else c['fields'],
                                     declared_method_inventory=None if c is None else [dict(name=m['name'], descriptor=m['desc'], access=m['access']) for m in c['methods']],
                                     whole_direct_allocation_and_constructor_callers=sites, direct_subclasses=subclasses,
                                     no_direct_allocation_constructor_call_or_subclass=True))
            records.append(dict(source_owner=canonical, native_owner=native, evidence=evidence,
                                complete_native_raw_instructions_handlers_and_access_equal=True,
                                optimized_matching_constructor_ABI_absent=True,
                                class_merge_boundary='Modern XtrazSM class is absent; its static methods are retained/relocated to Icon, as recorded in source-xtraz-queries.json.' if canonical == 'jimm/comm/XtrazSM' else None))
        light_boundary = None
        if target == 'SIEMENS2':
            assert all('DrawControls/LightControl' not in owners[mode] for mode in ['raw', 'optimized'])
            source = recover.ROOT / 'src/DrawControls/LightControl.java'
            directive = '//#sijapp cond.if target is "MOTOROLA" | target is "MIDP2"#'
            assert directive in source.read_bytes().decode('cp1251')
            light_boundary = dict(source_sha256=recover.sha(source), actual_outer_sijapp_directive=directive,
                                  native_candidate_absent=True, full_siemens_raw_and_optimized_class_absent=True)
        builds.append(dict(target=target, input_sha256={mode: recover.sha(path) for mode, path in paths.items()},
                           constructors=records, siemens_light_preprocessor_boundary=light_boundary))
    assert sum(len(b['constructors']) for b in builds) == 14
    report = dict(scope='Fourteen complete native/raw empty public constructors on three May RU platforms: ResourceBundle, OtherAction, Util, XtrazSM and MIDP2/Motorola LightControl. '
                  'Every three-instruction body, empty handler list and public access match exactly. Full declared fields/method inventory confirms all other members are static. '
                  'All application methods are scanned for direct NEW and constructor invocations, and all direct subclasses are scanned; none exists on native/raw/optimized. '
                  'Modern optimized matching constructor entries are genuinely absent. XtrazSM itself is merged away; Icon constructors are not substituted for it. '
                  'Siemens has no corresponding native LightControl candidate and no raw/optimized class, consistent with its actual outer SiJaPP directive. '
                  'No general reachability/reflective-instantiation, class initialization behavior, static helper equivalence or physical device runtime claim is made by this constructor inventory. '
                  'There is no product source or delivered JAR change.', builds=builds)
    (recover.ROOT / 'preservation/reports/source-unused-constructor-bytecode.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS unused constructors: 14 complete native/raw bodies, handlers and access; no direct callers/subclasses; real optimized absence and Siemens SiJaPP boundary')
    return report


if __name__ == '__main__': main()
