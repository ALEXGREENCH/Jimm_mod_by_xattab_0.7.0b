#!/usr/bin/env python3
"""Run actual selector/status/info controller methods without rewriting the controller."""
import json
import os
import recover
from test_source import ROOT, TEST, CACHE, run
from test_editor import prepare
from test_jimm_urls import dump


def main():
    runtime, cp = prepare()
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp', recover.cp([TEST, *runtime]),
         '-d', TEST, ROOT / 'tools/source/JimmUIStateProbe.java'], 'compile-jimm-ui-state')
    original = ROOT / 'preservation/wayback-originals/Jimm_MIDP2_RU/Jimm.jar'
    artifact = ROOT / 'dist/source/Jimm-MIDP2-RU.jar'
    raw = ROOT / 'build/source/MIDP2-RU/classes.jar'
    outputs, proofs = [], []
    modes = [('reference', original, TEST / 'clipboard-reference.jar'), ('source', raw, TEST / 'clipboard-source.jar')]
    login, host = TEST / 'jimm-ui-state-optimized-login.jar', TEST / 'jimm-ui-state-optimized-host.jar'
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'LoginFixture', artifact,
         login, 'source', TEST], 'jimm-ui-state-optimized-login')
    run([recover.java(), '-cp', recover.cp([TEST, CACHE / 'asm.jar']), 'SplashFixture', login, host, 'source'], 'jimm-ui-state-optimized-host')
    modes.append(('optimized-source', artifact, host))
    for mode, untouched, base in modes:
        owner = 'cf' if mode == 'reference' else 'jimm/JimmUI'
        assert dump(untouched, owner, cp) == dump(base, owner, cp), ('Controller must be entirely unchanged', mode)
        output = TEST / ('jimm-ui-state-' + mode + '.txt')
        run([recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
             '-cp', recover.cp([TEST, *runtime]), 'JimmUIStateProbe', base,
             'reference' if mode == 'reference' else 'source', output], 'jimm-ui-state-' + mode)
        outputs.append(output.read_bytes())
        proofs.append({'mode': mode, 'input_sha256': recover.sha(untouched), 'host_sha256': recover.sha(base),
                       'whole_controller_unchanged': True})
    assert outputs[0] == outputs[1] == outputs[2], 'Controller state differs; compare jimm-ui-state logs'
    lines = outputs[0].decode('utf-8').splitlines()
    assert lines[-1] == 'coverage:864:47:16:108'
    report = {'scope': 'Actual MIDP2 JimmUI selectors/OK/cancel callbacks and partial failures, active tag/index, '
                       'all 14 status/label/icon tables, cached information list and both formatter overloads with '
                       'section/index state, real TextList fragments/font/menu/listeners. All controller declarations, '
                       'instructions, exception handlers and initializers are wholly unchanged in native/raw/optimized '
                       'hosts. Existing login/date startup boundaries apply. Group-move callbacks deliberately have '
                       'no contact and exercise their real early null failure rather than network/RMS. '
                       'No complete controller/startup, arbitrary device rendering or physical device claim.',
              'reference_sha256': recover.sha(original), 'source_jar_sha256': recover.sha(artifact),
              'source_unoptimized_class_jar_sha256': recover.sha(raw), 'observations': len(lines),
              'selector_calls': 864, 'status_calls': 47, 'info_calls': 16, 'format_calls': 108,
              'optimized_observations': len(lines), 'differences': 0, 'fixture_proofs': proofs}
    (ROOT / 'preservation/reports/source-jimm-ui-state.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS JimmUI state:', len(lines), 'observations, 864 selectors, 47 statuses, 16 info lists, 108 formats; unchanged actual controllers')


if __name__ == '__main__':
    main()
