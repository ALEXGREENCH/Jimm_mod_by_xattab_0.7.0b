import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Observe the real controller, setter and native ChoiceGroup without application captures. */
public class OptionsItemStateProbe {
    static Properties spec;
    static Field field(Class<?> c,String key,Class<?> type)throws Exception{return PopupProbe.field(c,spec.getProperty(key),type);}
    static Object fresh(Class<?> c,Class<?>[] types,Object...args)throws Exception{Constructor<?> k=c.getDeclaredConstructor(types);k.setAccessible(true);return k.newInstance(args);}
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common em=(Common)ef.get(h);ArrayList<String> p=new ArrayList<String>();Collections.addAll(p,"--rms","memory",args[0]);
        em.initParams(p,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);em.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();ClassLoader loader=host.getClass().getClassLoader();
        Class<?> options=Class.forName(spec.getProperty("options"),true,loader);Object[] values=SoftKeysProbe.values(false,false,1,0);values[111]=Integer.valueOf(0);values[3]="RU";field(options,"table",Object[].class).set(null,values);
        Class<?> controller=Class.forName(spec.getProperty("controller"),true,loader),form=Class.forName(spec.getProperty("form"),true,loader),ui=Class.forName(spec.getProperty("ui"),true,loader);
        Field accounts=field(controller,"accounts",TextField[].class),panel=field(controller,"panel",form),labels=field(controller,"labels",ChoiceGroup.class),background=field(controller,"background",ChoiceGroup.class),last=field(ui,"last",Object.class);
        Method subject=PopupProbe.method(controller,"itemStateChanged",void.class,Item.class);int observations=0;
        try(PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"))) {
            for(int count=-1;count<=3;count++)for(int panelKind=0;panelKind<2;panelKind++)for(int event=-2;event<4;event++)for(int broken=-1;broken<4;broken++) {
                Object c=fresh(controller,new Class<?>[0]),actualPanel=panelKind==0?null:fresh(form,new Class<?>[]{String.class},"accounts"),sentinel=new Object();panel.set(c,actualPanel);last.set(null,sentinel);background.set(c,null);
                TextField[] fields=count<0?null:new TextField[count];ChoiceGroup choice=new ChoiceGroup("accounts",Choice.EXCLUSIVE);
                for(int i=0;i<Math.max(0,count);i++){fields[i]=new TextField("uin","1234"+i,32,TextField.NUMERIC);choice.append("before"+i,null);}
                if(fields!=null&&broken>=0&&broken<fields.length)fields[broken]=null;accounts.set(c,fields);labels.set(c,choice);
                Item incoming=event==-2?null:event>=0&&fields!=null&&event<fields.length?fields[event]:new StringItem("other","text");String answer="ok";
                try{subject.invoke(c,incoming);}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;answer=t.getClass().getName();}
                Object result=last.get(null);String state=result==sentinel?"sentinel":result==null?"null":result==actualPanel?"panel":"unexpected";StringBuilder names=new StringBuilder();for(int i=0;i<choice.size();i++)names.append(choice.getString(i)).append(';');
                out.write(count+":"+panelKind+":"+event+":"+broken+":"+answer+":"+state+":"+names+"\n");observations++;
            }
        }
        if(observations!=300)throw new AssertionError(observations);System.out.println("PASS options item state: "+observations+" unchanged-application observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
