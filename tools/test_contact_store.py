#!/usr/bin/env python3
"""Execute complete genuine contact-list RMS loading on three native/raw/optimized targets."""
import json
import os
import zipfile
import recover
import audit_source as audit
from audit_send_text import classes
from build_source import write_jar
from test_source import ROOT, TEST, CACHE, run


def configuration(target, reference, data):
    candidates = [(c,m) for c in data for m in c['methods'] if 'contactlist' in m['strings'] and
                  '184 javax/microedition/rms/RecordStore.listRecordStores()[Ljava/lang/String;' in m['code']]
    assert len(candidates) == 1
    owner,load = candidates[0];assert load['access'] == 10 and load['desc'] == '()V'
    prefix = load['code'][:8]
    assert prefix[:3] == ['187 java/util/Vector','89','183 java/util/Vector.<init>()V'] and prefix[4:7] == prefix[:3]
    assert prefix[3].startswith('179 '+owner['name']+'.') and prefix[3].endswith(' Ljava/util/Vector;')
    assert prefix[7].startswith('179 '+owner['name']+'.') and prefix[7].endswith(' Ljava/util/Vector;')
    spec = {'list':owner['name'],'load':load['name'],'contacts':prefix[3].split(' ')[1].split('.')[-1],
            'groups':prefix[7].split(' ')[1].split('.')[-1], 'options':({'MIDP2':'cj','MOTOROLA':'ci','SIEMENS2':'ch'}[target]) if reference else 'jimm/Options',
            'table':'a' if reference else 'options','display':'a' if reference else 'display'}
    savers = [m for m in owner['methods'] if 'contactlist' in m['strings'] and
              '184 javax/microedition/rms/RecordStore.deleteRecordStore(Ljava/lang/String;)V' in m['code']]
    assert len(savers) == 1 and savers[0]['desc'] == '()V' and savers[0]['access'] & 8
    spec['save'] = savers[0]['name']
    created = [s[4:] for s in load['code'] if s.startswith('187 ') and not s.startswith('187 java/')]
    assert len(created) == 2
    spec.update(contact=created[0],group=created[1])
    for role,call in [('time','182 java/io/DataInputStream.readInt()I'),('count','182 java/io/DataInputStream.readUnsignedShort()I')]:
        reads = [i for i,s in enumerate(load['code']) if s == call and load['code'][i+1].startswith('179 '+owner['name']+'.')]
        assert len(reads) == 1
        spec[role] = load['code'][reads[0]+1].split(' ')[1].split('.')[-1]
    version = [s for s in load['code'] if s.startswith('178 jimm/Jimm.') and s.endswith(' Ljava/lang/String;')]
    assert len(version) == 1;spec['version'] = version[0].split(' ')[1].split('.')[-1]
    contact = next(c for c in data if c['name'] == spec['contact'])
    streams = [m for m in contact['methods'] if m['desc'] == '(Ljava/io/DataInputStream;)V']
    stream_code = streams[0]['code'] if streams else load['code']
    writes = [s for s in stream_code if s.startswith('181 '+contact['name']+'.')]
    assert [s.split(' ')[-1] for s in writes[:4]] == ['I','I','I','Ljava/lang/String;']
    for role,instruction in zip(['packed','flags','uin','name'],writes[:4]):spec[role] = instruction.split(' ')[1].split('.')[-1]
    spec['roster'] = next(s for s in writes if s.endswith(' [B')).split(' ')[1].split('.')[-1]
    setters = [s for s in stream_code if s.startswith('182 '+contact['name']+'.') and s.endswith('(I)V')]
    assert len(setters) >= 2
    privacy_setter = next(m for m in contact['methods'] if setters[0] == '182 '+contact['name']+'.'+m['name']+m['desc'])
    fields = [s for s in privacy_setter['code'] if s.startswith('181 '+contact['name']+'.') and s.endswith(' I')]
    assert len(fields) == 1;spec['privacy'] = fields[0].split(' ')[1].split('.')[-1]
    if len(setters) >= 3:
        ignored_setter = next(m for m in contact['methods'] if setters[2] == '182 '+contact['name']+'.'+m['name']+m['desc'])
        fields = [s for s in ignored_setter['code'] if s.startswith('181 '+contact['name']+'.') and s.endswith(' I')]
        assert len(fields) == 1;spec['ignored'] = fields[0].split(' ')[1].split('.')[-1]
    else:
        spec['ignored'] = [s for s in writes if s.endswith(' I')][3].split(' ')[1].split('.')[-1]
    group = next(c for c in data if c['name'] == spec['group'])
    group_load = [m for m in group['methods'] if m['desc'] == '(Ljava/io/DataInputStream;)V']
    group_code = group_load[0]['code'] if group_load else load['code']
    for role,descriptor in [('groupId','I'),('groupName','Ljava/lang/String;')]:
        stores = [s for s in group_code if s.startswith('181 '+group['name']+'.') and s.endswith(' '+descriptor)]
        assert len(stores) == 1;spec[role] = stores[0].split(' ')[1].split('.')[-1]
    for role,descriptor in [('contacts','Ljava/util/Vector;'),('groups','Ljava/util/Vector;'),('time','I'),('count','I')]:
        assert any(f['name'] == spec[role] and f['desc'] == descriptor and f['access'] & 8 for f in owner['fields'])
    return spec,load


def main():
    runtime = [CACHE/n for n in ['microemu.jar','microemu-nokiaui.jar','microemu-jsr-75.jar','microemu-jsr-120.jar']]
    run([os.environ.get('JAVAC','javac'),'-encoding','UTF-8','-cp',recover.cp([TEST,CACHE/'asm.jar',*runtime]),'-d',TEST,
         *[ROOT/('tools/source/'+n+'.java') for n in ['GraphicsFixture','ExtendedKeysFixture','PopupProbe','ContactStoreProbe']]],'compile-contact-store')
    cp=recover.cp([CACHE/n for n in recover.ASM])
    run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,ROOT/'tools/recovery/BytecodeDump.java'],'compile-contact-store-dump')
    builds=[]
    for target,platform in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        original=ROOT/'preservation/wayback-originals'/('Jimm_'+platform+'_RU')/'Jimm.jar'
        raw=ROOT/'build/source'/(target+'-RU')/'classes.jar';delivered=ROOT/'dist/source'/('Jimm-'+target+'-RU.jar')
        host=TEST/('contact-store-'+target+'-authoring.jar')
        with zipfile.ZipFile(delivered) as z:entries={n:z.read(n) for n in z.namelist() if not n.endswith('.class')}
        with zipfile.ZipFile(raw) as z:entries.update({n:z.read(n) for n in z.namelist()})
        write_jar(host,entries);outputs=[];proofs=[]
        for mode,path in [('reference',original),('source',host),('optimized-source',delivered)]:
            data=classes(path,cp);spec,subject=configuration(target,mode=='reference',data)
            tag='contact-store-'+target+'-'+mode;settings,fixture,output=[TEST/(tag+ext) for ext in ['.properties','.jar','.txt']]
            settings.write_text(''.join(k+'='+v.replace('/','.')+'\n' for k,v in spec.items()),encoding='utf-8',newline='\n')
            run([recover.java(),'-cp',recover.cp([TEST,CACHE/'asm.jar']),'ExtendedKeysFixture',path,fixture,TEST,'unused','false','unused'],tag+'-fixture')
            with zipfile.ZipFile(path) as before,zipfile.ZipFile(fixture) as after:
                assert all(before.read(n)==after.read(n) for n in before.namelist() if n.lower()!='meta-inf/manifest.mf')
                assert set(after.namelist())-set(before.namelist()) <= {'GraphicsMIDlet.class'}
            run([recover.java(),'-Djava.awt.headless=true','-Dsun.reflect.inflationThreshold=2147483647','-cp',recover.cp([TEST,*runtime]),'ContactStoreProbe',fixture,output,settings],tag)
            result=output.read_bytes();coverage=list(map(int,result.splitlines()[-1].decode().split(':')[1:]));assert len(coverage)==7 and coverage[1]==coverage[0]*2
            outputs.append(result);proofs.append({'mode':mode,'input_sha256':recover.sha(path),'fixture_sha256':recover.sha(fixture),'output_sha256':recover.sha(output),
                                                'all_input_application_bytes_unchanged':True,'owner':spec['list'],'load':subject['name']+subject['desc'],'instructions':len(subject['code']),
                                                'handlers':subject['handlers'],'coverage':coverage,'typed_observation_fields':spec})
        assert outputs[0]==outputs[1]==outputs[2],('Real contact RMS load differs',target)
        builds.append({'target':target,'reference_sha256':recover.sha(original),'raw_sha256':recover.sha(raw),'delivered_sha256':recover.sha(delivered),
                       'observations':len(outputs[0].splitlines()),'fixture_proofs':proofs})
    report={'scope':'Complete unchanged contact-list RMS load, actual ContactItem/GroupItem constructors and real '
            'MicroEmulator memory RecordStore. No application class or resource bytes are changed; a host MIDlet '
            'and manifest isolate startup. Typed Options and Jimm.VERSION/display are explicit inputs. '
            'Version/header/metadata prefixes, signed contact flags and roster lengths, short reads, UTF/Unicode, '
            'unknown item tags, mixed/empty records, record splits, deletion holes and held-open stores are replayed '
            'twice. Vector replacement/retained aliases, timestamp/count, all persistent contact/group fields, '
            'actual complete ContactList.save reserialization when appVersion is non-null, '
            'group id/name, complete surviving RMS bytes, real exceptions and successful deletion after load are '
            'observed. No scripted RMS calls, copied loader or computed expected application output. '
            'No whole roster lifecycle, physical RMS implementation, every constructor field or complete startup claim.', 'builds':builds}
    (ROOT/'preservation/reports/source-contact-store.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS contact RMS:',len(builds),'targets;',sum(b['observations'] for b in builds),'observations; original/raw/optimized match')
    return report


if __name__ == '__main__':main()
