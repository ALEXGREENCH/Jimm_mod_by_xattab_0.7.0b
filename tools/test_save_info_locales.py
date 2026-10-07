#!/usr/bin/env python3
"""Compare real optimized SaveInfo actions/helpers in each native MIDP2 locale."""
import argparse
import json
import os
import sys
import recover
import test_source as tests


def main(all_languages=False, skip_build=False):
    recover.bootstrap()
    tests.TEST.mkdir(parents=True, exist_ok=True)
    runtime = [recover.CACHE / n for n in ['microemu.jar', 'microemu-nokiaui.jar',
                                          'microemu-jsr-75.jar', 'microemu-jsr-120.jar']]
    tests.run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-sourcepath',
               recover.ROOT / 'tools/source', '-cp', recover.cp([tests.TEST, recover.CACHE / 'asm.jar', *runtime]),
               '-d', tests.TEST, *[recover.ROOT / 'tools/source' / n for n in
                                  ['SaveInfoIO.java', 'SaveInfoFixture.java', 'SaveInfoProbe.java']]], 'save-info-locales-compile')
    tests.run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-source', '7', '-target', '7',
               '-cp', recover.cp([tests.TEST, *runtime]), '-d', tests.TEST,
               recover.ROOT / 'tools/source/SaveInfoIO.java', recover.ROOT / 'tools/source/LoginIO.java'], 'save-info-locales-io')
    java = [recover.java(), '-Djava.awt.headless=true', '-Dsun.reflect.inflationThreshold=2147483647',
            '-cp', recover.cp([tests.TEST, *runtime])]
    report = {'scope': 'Actual original/delivered optimized MIDP2 SaveInfoAction, helpers, packets, '
              'selected-account settings and each real locale ResourceBundle. Whole JARs are used '
              'without authored-method bridges or unoptimized replacements. Only the action clock/ '
              'completion destination, scripted connection and live-login entry boundary are changed. '
              'Successful send returns mean scripted connection acceptance; the real packet serializer '
              'executes separately and its errors are compared literally. No live transport or complete '
              'notification/scheduler effect claim.', 'builds': []}
    for language in ['RU', 'UA', 'RO', 'EN', 'CZ'] if all_languages else ['RU']:
        if not skip_build:
            tests.run([sys.executable, recover.ROOT / 'tools/build_source.py', '--language', language], 'save-info-build-' + language)
        original = recover.ROOT / 'preservation/wayback-originals' / ('Jimm_MIDP2_' + language) / 'Jimm.jar'
        rebuilt = recover.ROOT / 'dist/source' / ('Jimm-MIDP2-' + language + '.jar')
        observations = []
        for path, mode in [(original, 'reference'), (rebuilt, 'source')]:
            prefix = 'save-info-locales-' + language + '-' + mode
            fixture, output = tests.TEST / (prefix + '.jar'), tests.TEST / (prefix + '.txt')
            tests.run([recover.java(), '-cp', recover.cp([tests.TEST, recover.CACHE / 'asm.jar']),
                       'SaveInfoFixture', path, fixture, mode, tests.TEST, path], prefix + '-fixture')
            tests.run([*java, 'SaveInfoProbe', fixture, mode, output], prefix)
            observations.append(output.read_bytes())
        if observations[0] != observations[1]:
            raise AssertionError('SaveInfo locale mismatch: ' + language)
        lines = observations[0].decode('utf-8').splitlines()
        initializations, forwards, accepted, sends, returns, notifications = [int(p.split(':')[1]) for p in lines[-1].split('/')]
        report['builds'].append({'language': language, 'reference_sha256': recover.sha(original),
                                'source_jar_sha256': recover.sha(rebuilt), 'observations': len(lines),
                                'initializations': initializations, 'forward_calls': forwards,
                                'accepted_responses': accepted, 'send_attempts': sends,
                                'successful_scripted_send_returns': returns,
                                'completion_notification_attempts': notifications, 'differences': 0})
    path = recover.ROOT / 'preservation/reports/source-save-info-locales.json'
    path.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8', newline='\n')
    print('PASS SaveInfo locales:', len(report['builds']), 'locales,',
          sum(b['observations'] for b in report['builds']), 'observations')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--all-languages', action='store_true')
    parser.add_argument('--skip-build', action='store_true')
    args = parser.parse_args()
    main(args.all_languages, args.skip_build)
