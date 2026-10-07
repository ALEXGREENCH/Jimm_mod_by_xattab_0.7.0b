#!/usr/bin/env python3
"""Verify complete alpha flow, helper expansion boundary and typed settings declarations."""
import json
import os
import recover
import audit_source as audit
from audit_send_text import classes
from test_options_settings import configuration


def main():
    cp=recover.cp([recover.CACHE/n for n in recover.ASM])
    recover.run([os.environ.get('JAVAC','javac'),'-cp',cp,'-d',audit.OUT,recover.ROOT/'tools/recovery/BytecodeDump.java'])
    builds=[]
    for target,platform in [('MIDP2','MIDP2'),('MOTOROLA','Moto'),('SIEMENS2','Siemens2')]:
        paths=[('reference',recover.ROOT/'preservation/wayback-originals'/('Jimm_'+platform+'_RU')/'Jimm.jar'),
               ('authoring',recover.ROOT/'build/source'/(target+'-RU')/'classes.jar'),
               ('optimized',recover.ROOT/'dist/source'/('Jimm-'+target+'-RU.jar'))]
        proofs=[]
        for mode,path in paths:
            data=classes(path,cp);spec,owner,alpha=configuration(target,mode=='reference',data)
            helper=('184 '+spec['vl']+'.resetGradient()V') in alpha['code']
            if helper:
                vl=next(c for c in data if c['name']==spec['vl'])
                assert next(m for m in vl['methods'] if (m['name'],m['desc'])==('resetGradient','()V'))['code']==['3','179 '+spec['vl']+'.'+spec['gradient']+' I','177']
            expected=['178 jimm/Jimm.'+spec['display']+' Ljavax/microedition/lcdui/Display;',
                      '182 javax/microedition/lcdui/Display.numAlphaLevels()I','4','164 '+('21' if helper else '22')]
            getter='a' if mode=='reference' else 'getInt'
            for key,field in [(100,'cursor'),(116,'caption'),(117,'softbar')]:
                expected.extend(['17 255','16 '+str(key),'184 '+owner['name']+'.'+getter+'(I)I','100','179 '+owner['name']+'.'+spec[field]+' I'])
            expected += ['184 '+spec['vl']+'.resetGradient()V'] if helper else ['3','179 '+spec['vl']+'.'+spec['gradient']+' I']
            expected += ['177','17 255','89','179 '+owner['name']+'.'+spec['softbar']+' I','89',
                         '179 '+owner['name']+'.'+spec['caption']+' I','179 '+owner['name']+'.'+spec['cursor']+' I','177']
            assert alpha['code']==expected,(target,mode,alpha['code'])
            assert alpha['handlers']==[] and alpha['access']==9
            defaults=next(m for m in owner['methods'] if (m['name'],m['desc'])==(spec['defaults'],'()V'))
            setter='a' if mode=='reference' else 'setInt'
            light=['16 101','16 70','184 '+owner['name']+'.'+setter+'(II)V']
            writes=sum(defaults['code'][i:i+3]==light for i in range(len(defaults['code'])-2))
            assert writes==(1 if target=='MIDP2' else 0)
            proofs.append({'mode':mode,'jar_sha256':recover.sha(path),'owner':owner['name'],'alpha':alpha['name']+alpha['desc'],
                           'complete_instructions':alpha['code'],'gradient_reset_helper_retained':helper,
                           'exact_alpha_control_flow_verified':True,'fallback_preserves_gradient':True,
                           'null_display_fails_before_any_write':True,'defaults_device_fields':spec['devices'].split(',') if spec['devices'] else [],
                           'font_update':spec['style']+'()V','defaults':spec['defaults']+'()V'})
            proofs[-1]['nokia_light_level_default_writes']=writes
        builds.append({'target':target,'proofs':proofs})
    report={'scope':'Whole alpha method with exact original strict >1 branch, unclamped integer subtraction, '
            'ordered field stores, cache invalidation only in the supported branch, both returns and no handlers. '
            'Authored builds may retain the separately verified three-instruction resetGradient helper; native '
            'stores the same field directly. Complete sequences are matched with explicit helper/branch-index '
            'differences, not normalized into a false raw-body identity. Settings declarations and every boolean '
            'device input read by defaults are typed; the 101=70 default is verified as MIDP2-only. '
            'Defaults/font semantics are separately executed. '
            'No full defaults bytecode or whole Options UI equivalence claim.', 'builds':builds}
    (recover.ROOT/'preservation/reports/source-options-settings-bytecode.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8',newline='\n')
    print('PASS settings alpha bytecode: complete native/raw/optimized paths on three targets, explicit helper boundary and typed device/default/font declarations')


if __name__=='__main__':main()
