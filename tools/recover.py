#!/usr/bin/env python3
"""Rebuild and audit the supplied MIDP2 RU binary. Python 3.9+, JDK 11+."""
import argparse
import collections
import hashlib
import json
import os
from pathlib import Path
import shutil
import subprocess
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parents[1]
CACHE = ROOT / '.cache/tools'
BUILD = ROOT / 'build/recovery'
ORIGINAL = ROOT / 'preservation/originals/Jimm_MIDP2_RU/Jimm.jar'
LIBS = ['cldcapi10.jar', 'midpapi20.jar', 'microemu.jar', 'microemu-jsr-75.jar',
        'microemu-jsr-120.jar', 'microemu-nokiaui.jar']
ASM = ['asm.jar', 'asm-tree.jar', 'asm-commons.jar', 'asm-analysis.jar']


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def java():
    return os.environ.get('JAVA', 'java')


def cp(paths):
    return os.pathsep.join(str(p) for p in paths)


def run(args, capture=False):
    p = subprocess.run([str(a) for a in args], cwd=ROOT, check=True,
                       stdout=subprocess.PIPE if capture else None,
                       stderr=subprocess.PIPE if capture else None)
    return p.stdout


def bootstrap(decompiler=False):
    CACHE.mkdir(parents=True, exist_ok=True)
    lock = json.loads((ROOT / 'tools/recovery/dependencies.json').read_text())
    for name, spec in lock.items():
        if name == 'vineflower.jar' and not decompiler:
            continue
        dest = CACHE / name
        if not dest.exists():
            print('Download', name, flush=True)
            with urllib.request.urlopen(spec['url'], timeout=60) as response:
                data = response.read()
            if hashlib.sha256(data).hexdigest() != spec['sha256']:
                raise ValueError('Download checksum mismatch: ' + name)
            dest.write_bytes(data)
        if sha(dest) != spec['sha256']:
            raise ValueError('Tool checksum mismatch: ' + name)


def helpers():
    BUILD.mkdir(parents=True, exist_ok=True)
    sources = sorted((ROOT / 'tools/recovery').glob('*.java'))
    run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8', '-cp',
         cp(CACHE / p for p in ASM), '-d', BUILD, *sources])


def helper(name, *args, capture=False):
    return run([java(), '-Dfile.encoding=UTF-8', '-cp',
                cp([BUILD] + [CACHE / p for p in ASM]), name, *args], capture)


def verify_originals():
    count = 0
    for line in (ROOT / 'preservation/SHA256SUMS').read_text().splitlines():
        expected, name = line.split('  ', 1)
        if sha(ROOT / name) != expected:
            raise ValueError('Original changed: ' + name)
        count += 1
    print('PASS:', count, 'preserved archive/JAR/JAD hashes')


def write_jar(path, entries):
    with zipfile.ZipFile(path, 'w', zipfile.ZIP_DEFLATED, compresslevel=9) as z:
        for name, data in sorted(entries.items()):
            # Fixed timestamps identify the revision currently reconstructed.
            # The May 12 website revision is the next target, not this build.
            info = zipfile.ZipInfo(name, (2010, 3, 28, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            info.external_attr = 0o644 << 16
            z.writestr(info, data)


def build():
    verify_originals()
    bootstrap()
    helpers()
    classes = BUILD / 'classes'
    # A clean output prevents old classes from masking missing source files.
    if classes.exists():
        assert classes.resolve().is_relative_to(BUILD.resolve())
        shutil.rmtree(classes)
    classes.mkdir()
    sources = sorted((ROOT / 'reconstruction/midp2/src').rglob('*.java'))
    sources += sorted((ROOT / 'tools/api-stubs').rglob('*.java'))
    argfile = BUILD / 'sources.txt'
    argfile.write_text('\n'.join('"' + p.as_posix() + '"' for p in sources), encoding='utf-8')
    run([java(), '-jar', CACHE / 'ecj.jar', '-encoding', 'UTF-8', '-source', '1.3',
         '-target', '1.1', '-nowarn', '-g:none', '-bootclasspath',
         cp(CACHE / n for n in LIBS), '-d', classes, '@' + str(argfile)])
    helper('Remap', ORIGINAL, BUILD / 'reference-remapped.jar')
    helper('Remap', ORIGINAL, BUILD / 'rebuilt-obfuscated.jar', '--reverse', classes)
    write_jar(BUILD / 'api-stubs.jar',
              {p.relative_to(classes).as_posix(): p.read_bytes()
               for p in (classes / 'javax').rglob('*.class')})
    config = ['-injars "' + (BUILD / 'rebuilt-obfuscated.jar').as_posix() + '"',
              '-outjars "' + (BUILD / 'preverified.jar').as_posix() + '"']
    config += ['-libraryjars "' + (CACHE / n).as_posix() + '"' for n in LIBS]
    config += ['-libraryjars "' + (BUILD / 'api-stubs.jar').as_posix() + '"',
               '-dontshrink', '-dontoptimize', '-dontobfuscate', '-microedition',
               '-target 1.1', '-dontnote']
    pro = BUILD / 'preverify.pro'
    pro.write_text('\n'.join(config) + '\n', encoding='utf-8')
    run([java(), '-jar', CACHE / 'proguard.jar', '@' + str(pro)])
    with zipfile.ZipFile(ORIGINAL) as z:
        resources = {n: z.read(n) for n in z.namelist()
                     if not n.endswith('/') and not n.endswith('.class')}
    with zipfile.ZipFile(BUILD / 'preverified.jar') as z:
        resources.update({n: z.read(n) for n in z.namelist() if n.endswith('.class')})
    # Jar size/URL belong in the JAD, not in a self-referential manifest.
    manifest = resources['META-INF/MANIFEST.MF'].decode('utf-8')
    lines = [s for s in manifest.splitlines()
             if s and not s.startswith(('MIDlet-Jar-Size:', 'MIDlet-Jar-URL:'))]
    resources['META-INF/MANIFEST.MF'] = ('\r\n'.join(lines) + '\r\n\r\n').encode()
    dist = ROOT / 'dist/reconstructed'
    dist.mkdir(parents=True, exist_ok=True)
    jar = dist / 'Jimm-restored-MIDP2-RU.jar'
    write_jar(jar, resources)
    jad = ORIGINAL.with_suffix('.jad').read_text(encoding='utf-8')
    jadlines = [s for s in jad.splitlines()
                if s and not s.startswith(('MIDlet-Jar-Size:', 'MIDlet-Jar-URL:'))]
    jadlines += ['MIDlet-Jar-URL: ' + jar.name, 'MIDlet-Jar-Size: ' + str(jar.stat().st_size)]
    jar.with_suffix('.jad').write_bytes(('\r\n'.join(jadlines) + '\r\n').encode('utf-8'))
    print('Built', jar.relative_to(ROOT), sha(jar))


def dump(path):
    return json.loads(helper('BytecodeDump', path, capture=True))


def compare():
    original = dump(ORIGINAL)
    rebuilt = dump(ROOT / 'dist/reconstructed/Jimm-restored-MIDP2-RU.jar')
    indexed = {(c['name'], m['name'], m['desc']): m for c in rebuilt for m in c['methods']}
    rows = []
    for c in original:
        for m in c['methods']:
            key = (c['name'], m['name'], m['desc'])
            n = indexed.pop(key, None)
            same = n is not None and m['code'] == n['code'] and m['handlers'] == n['handlers']
            rows.append({'class': c['name'], 'method': m['name'], 'descriptor': m['desc'],
                         'has_code': bool(m['code']), 'status': 'exact_symbolic' if same else 'different' if n else 'missing',
                         'original_instructions': len(m['code']), 'rebuilt_instructions': len(n['code']) if n else None})
    result = {'reference_sha256': sha(ORIGINAL), 'classes': len(original),
              'total_methods': len(rows), 'methods_with_code': sum(r['has_code'] for r in rows),
              'exact_symbolic': sum(r['status'] == 'exact_symbolic' for r in rows),
              'exact_symbolic_with_code': sum(r['has_code'] and r['status'] == 'exact_symbolic' for r in rows),
              'missing_methods': sum(r['status'] == 'missing' for r in rows),
              'extra_methods': [list(k) for k in indexed], 'methods': rows,
              'definition': 'Exact opcode, operand, symbolic member reference, local slot, branch target, and exception-handler sequence; ignores constant-pool indexes, debug info and StackMap attributes. Different does not necessarily mean behavior differs.'}
    dest = ROOT / 'preservation/reports/midp2-bytecode-comparison.json'
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(json.dumps(result, indent=2) + '\n', encoding='utf-8')
    print('Symbolic matches:', result['exact_symbolic'], '/', result['total_methods'],
          '(executable:', result['exact_symbolic_with_code'], '/', result['methods_with_code'], ')')
    if result['missing_methods'] or result['extra_methods']:
        raise AssertionError('Method inventory changed')


def differential():
    text = helper('DifferentialTest', BUILD / 'reference-remapped.jar', BUILD / 'classes', CACHE, capture=True).decode('utf-8').replace('\r\n', '\n')
    (ROOT / 'preservation/reports/differential-tests.txt').write_text(text, encoding='utf-8')
    print(text.strip())


def decompile():
    """Reference output only: never overwrite manually repaired source."""
    bootstrap(decompiler=True)
    helpers()
    for variant in ['Jimm_MIDP2_RU', 'Jimm_Siemens2_RU', 'Jimm_Moto_RU']:
        src = ROOT / 'preservation/originals' / variant / 'Jimm.jar'
        remapped = BUILD / (variant + '-remapped.jar')
        helper('Remap', src, remapped)
        run([os.environ.get('JAVA17', java()), '-jar', CACHE / 'vineflower.jar',
             '--verify-merges=1', '--simplify-stack=0', '--decompile-generics=0', '--silent', '--folder',
             '-e=' + str(CACHE / 'microemu.jar'), remapped, BUILD / 'decompiled' / variant])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=['build', 'verify', 'compare', 'test', 'all', 'decompile'])
    args = parser.parse_args()
    if args.command in ('build', 'all'):
        build()
    if args.command == 'verify':
        verify_originals()
    if args.command in ('compare', 'all'):
        compare()
    if args.command in ('test', 'all'):
        differential()
    if args.command == 'decompile':
        decompile()
