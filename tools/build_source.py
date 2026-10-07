#!/usr/bin/env python3
"""Build the maintained src tree with SiJaPP, LangsTask, ECJ and ProGuard."""
import argparse
import os
from pathlib import Path
import shutil
import zipfile
import recover as recovery

ROOT = recovery.ROOT
CACHE = recovery.CACHE
MODULES = 'SMILES,TRAFFIC,HISTORY,FILES,PROXY,ANISMILES'


def write_jar(path, entries):
    with zipfile.ZipFile(path, 'w', compression=zipfile.ZIP_DEFLATED) as jar:
        for name, data in sorted(entries.items()):
            info = zipfile.ZipInfo(name, (2010, 5, 12, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            jar.writestr(info, data)


def build(target='MIDP2', language='RU', modules=MODULES, compile_only=False, smile_pack='big'):
    recovery.bootstrap()
    dest = ROOT / 'build/source' / (target + '-' + language)
    if dest.exists():
        assert dest.resolve().is_relative_to((ROOT / 'build/source').resolve())
        shutil.rmtree(dest)
    sources, classes, resources = (dest / x for x in ('src', 'classes', 'res'))
    for path in (sources, classes, resources):
        path.mkdir(parents=True)
    task_classes = dest / 'tasks'
    task_cp = recovery.cp([CACHE / 'ant.jar', CACHE / 'ant-launcher.jar'])
    task_sources = list((ROOT / 'util/sijapp/src').rglob('*.java'))
    task_sources += list((ROOT / 'util/langs/src').rglob('*.java'))
    task_sources += [ROOT / 'tools/source/Preprocess.java']
    recovery.run([os.environ.get('JAVAC', 'javac'), '-encoding', 'UTF-8',
                  '-cp', task_cp, '-d', task_classes, *task_sources])
    task_cp = recovery.cp([task_classes, CACHE / 'ant.jar', CACHE / 'ant-launcher.jar'])
    def preprocess(*args):
        recovery.run([recovery.java(), '-Dfile.encoding=UTF-8', '-cp', task_cp,
                      'Preprocess', *args])
    preprocess('source', ROOT / 'src', sources, target, modules)
    replacements = {'VERSION': '0.7.0b', 'VERSION-JAVA': '0.7.0', 'DATE': '12.05.2010',
                    'TARGET': target, 'MODULES': modules, 'MIDP-PROFILE': 'MIDP-2.0'}
    for path in sources.rglob('*'):
        if path.suffix in ('.java', '.lang'):
            data = path.read_bytes()
            for key, value in replacements.items():
                data = data.replace(('###' + key + '###').encode(), value.encode())
            path.write_bytes(data)
    preprocess('languages', sources, resources, language)
    files = sorted(sources.rglob('*.java')) + sorted((ROOT / 'tools/api-stubs').rglob('*.java'))
    argfile = dest / 'sources.txt'
    argfile.write_text('\n'.join('"' + p.as_posix() + '"' for p in files), encoding='utf-8')
    libs = [CACHE / n for n in recovery.LIBS]
    libs += list((ROOT / 'res' / target / 'lib').glob('*.jar'))
    recovery.run([recovery.java(), '-jar', CACHE / 'ecj.jar', '-encoding', 'UTF-8',
                  '-source', '1.3', '-target', '1.1', '-g:none', '-nowarn',
                  '-bootclasspath', recovery.cp(libs), '-d', classes, '@' + str(argfile)])
    if compile_only:
        return dest
    recovery.write_jar(dest / 'classes.jar', {
        p.relative_to(classes).as_posix(): p.read_bytes() for p in classes.rglob('*.class')
        if not p.relative_to(classes).as_posix().startswith(('javax/', 'com/'))})
    recovery.write_jar(dest / 'api-stubs.jar', {
        p.relative_to(classes).as_posix(): p.read_bytes() for p in classes.rglob('*.class')
        if p.relative_to(classes).as_posix().startswith(('javax/', 'com/'))})
    config = ['-injars classes.jar', '-outjars preverified.jar']
    config += ['-libraryjars "' + p.as_posix() + '"' for p in libs + [dest / 'api-stubs.jar']]
    config += ['-keep public class * extends javax.microedition.midlet.MIDlet',
               '-allowaccessmodification', '-dontobfuscate', '-optimizationpasses 2',
               # Retain explicit empty-buffer append sequences seen in the May release.
               '-optimizations !code/simplification/string',
               '-microedition', '-target 1.1', '-dontnote']
    (dest / 'build.pro').write_text('\n'.join(config) + '\n')
    recovery.run([recovery.java(), '-jar', CACHE / 'proguard.jar', '@' + str(dest / 'build.pro')])
    # Use source-owned resources; the preserved reference JAR is never an input here.
    entries = {}
    for directory in [ROOT / 'res/ALL_TARGETS']:
        for path in directory.rglob('*'):
            if path.is_file() and 'lib' not in path.relative_to(directory).parts:
                entries[path.name] = path.read_bytes()
    selected = set(modules.split(','))
    module_dirs = ['FILES'] if 'FILES' in selected else []
    if selected & {'SMILES', 'ANISMILES', 'GIFSMILES'}:
        packs = {'big': 'SMILES_BIG', 'small': 'SMILES_SMALL',
                 'animated-big': 'ANISMILES_BIG', 'animated-small': 'ANISMILES_SMALL', 'gif': 'GIFSMILES'}
        required = 'ANISMILES' if smile_pack.startswith('animated-') else 'GIFSMILES' if smile_pack == 'gif' else None
        if required and required not in selected:
            raise ValueError(smile_pack + ' requires module ' + required)
        module_dirs.append(packs[smile_pack])
    for module_dir in module_dirs:
        directory = ROOT / 'res/MODULES' / module_dir
        for path in directory.rglob('*'):
            if path.is_file():
                name = path.name if module_dir == 'FILES' else path.relative_to(directory).as_posix()
                entries[name] = path.read_bytes()
    # Phone-specific graphics/sounds override shared module resources (notably Siemens fs.png).
    directory = ROOT / 'res' / target
    for path in directory.rglob('*'):
        if path.is_file() and 'lib' not in path.relative_to(directory).parts:
            entries[path.name] = path.read_bytes()
    for path in resources.iterdir():
        if path.is_file():
            entries[path.name] = path.read_bytes()
    with zipfile.ZipFile(dest / 'preverified.jar') as jar:
        entries.update({n: jar.read(n) for n in jar.namelist() if n.endswith('.class')})
    manifest = (ROOT / 'res/MANIFEST.MF').read_text()
    for key, value in replacements.items():
        manifest = manifest.replace('###' + key + '###', value)
    manifest = '\r\n'.join(line for line in manifest.splitlines()
                           if line and not line.startswith(('MIDlet-Jar-Size:', 'MIDlet-Jar-URL:')))
    entries['META-INF/MANIFEST.MF'] = (manifest + '\r\n\r\n').encode('utf-8')
    dist = ROOT / 'dist/source'
    dist.mkdir(parents=True, exist_ok=True)
    jar = dist / ('Jimm-' + target + '-' + language + '.jar')
    write_jar(jar, entries)
    jar.with_suffix('.jad').write_bytes((manifest + '\r\nMIDlet-Jar-URL: ' + jar.name +
        '\r\nMIDlet-Jar-Size: ' + str(jar.stat().st_size) + '\r\n').encode('utf-8'))
    print('Built from src:', jar, recovery.sha(jar))
    return dest


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--target', choices=['MIDP2', 'MOTOROLA', 'SIEMENS2'], default='MIDP2')
    parser.add_argument('--language', choices=['RU', 'UA', 'RO', 'EN', 'CZ'], default='RU')
    parser.add_argument('--modules', default=MODULES)
    parser.add_argument('--compile-only', action='store_true')
    parser.add_argument('--smile-pack', choices=['big', 'small', 'animated-big', 'animated-small', 'gif'], default='big')
    args = parser.parse_args()
    build(args.target, args.language, args.modules, args.compile_only, args.smile_pack)
