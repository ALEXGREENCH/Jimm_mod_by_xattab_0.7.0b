import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Run the complete disconnected status update in a fresh emulator process. */
public class OnlineStatusInitializationProbe {
    public static void main(String[] args) {try {
        boolean ref=args[2].equals("reference");
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        ClassLoader loader=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();
        Class<?> options=Class.forName(ref?"cj":"jimm.Options",true,loader);
        Field table=PopupProbe.field(options,ref?"a":"options",Object[].class);
        Object[] values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[3]="RU";values[40]=new String(new char[12]);values[88]=37;values[98]=8;values[173]=Boolean.TRUE;values[192]=Long.valueOf(-999);table.set(null,values);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);
        PopupProbe.field(jimm,ref?"a":"display",javax.microedition.lcdui.Display.class).set(null,javax.microedition.lcdui.Display.getDisplay(MIDletBridge.getCurrentMIDlet()));
        PopupProbe.field(jimm,ref?"a":"VERSION",String.class).set(null,"0.7.0b");
        Class.forName(ref?"m":"jimm.ContactList",true,loader).getDeclaredConstructor().newInstance();
        Class<?> icq=Class.forName(ref?"r":"jimm.comm.Icq",true,loader);
        PopupProbe.field(icq,ref?"b":"connected",boolean.class).setBoolean(null,false);
        Class<?> events=Class.forName("OnlineStatusInitializationEvents",true,loader);
        Field count=events.getField("count");int before=count.getInt(null);
        events.getField("fail").setBoolean(null,Boolean.parseBoolean(args[3]));
        Method set=PopupProbe.method(icq,ref?"a":"setOnlineStatus",void.class,int.class);
        String result="ok";
        try {set.invoke(null,Integer.valueOf(32));} catch(InvocationTargetException ex) {result=ex.getCause().getClass().getName();}
        if(before!=0)throw new AssertionError("OtherAction was initialized by setup");
        try(PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"))) {
            out.println("before="+before+":after="+count.getInt(null)+":result="+result+":status="+values[192]);
        }
        System.exit(0);
    }catch(Throwable ex){ex.printStackTrace();System.exit(1);}}
}
