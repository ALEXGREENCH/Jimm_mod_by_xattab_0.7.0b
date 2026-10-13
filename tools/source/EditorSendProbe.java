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

/** Invoke the complete real handler with actual TextBox and page-vector state. */
public class EditorSendProbe {
    static Properties spec;
    // MicroEmulator 2.0.4 rejects null despite the MIDP TextBox contract.
    static class CompatibleTextBox extends TextBox {
        CompatibleTextBox(String text){super("editor",text,64,0);}
        public void setString(String text){super.setString(text==null?"":text);}
    }
    static Field field(Class<?> c,String key,Class<?> type)throws Exception{return PopupProbe.field(c,spec.getProperty(key),type);}
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common em=(Common)ef.get(h);ArrayList<String> p=new ArrayList<String>();Collections.addAll(p,"--rms","memory",args[0]);
        em.initParams(p,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);em.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();ClassLoader loader=host.getClass().getClassLoader();Class<?> options=Class.forName(spec.getProperty("options"),true,loader);
        Object[] values=SoftKeysProbe.values(false,false,1,0);values[146]=Boolean.TRUE;field(options,"table",Object[].class).set(null,values);
        Class<?> ui=Class.forName(spec.getProperty("ui"),true,loader),jimm=Class.forName("jimm.Jimm",true,loader),io=Class.forName("EditorSendIO",true,loader);
        Constructor<?> ctor=ui.getDeclaredConstructor();ctor.setAccessible(true);Object controller=ctor.newInstance();
        Field se=field(jimm,"se",boolean.class),jp8=spec.containsKey("jp8")?field(jimm,"jp8",boolean.class):null;
        Field box=field(ui,"box",TextBox.class),mode=field(ui,"mode",int.class),current=field(ui,"current",int.class),pages=field(ui,"pages",Vector.class);
        Method handler=PopupProbe.method(ui,"commandAction",void.class,Command.class,Displayable.class);Object send=field(ui,"send",Command.class).get(null);int count=0;
        try(PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"))) {
            for(boolean isSE:new boolean[]{false,true})for(boolean isJP8:new boolean[]{false,true})for(String text:new String[]{"","abc","\u043f\u010d"})
                for(int editorMode:new int[]{200001,-1})for(boolean exists:new boolean[]{false,true})for(int failure=0;failure<7;failure++) {
                    se.setBoolean(null,isSE);if(jp8!=null)jp8.setBoolean(null,isJP8);
                    TextBox actual=new CompatibleTextBox(text);box.set(null,actual);mode.setInt(null,editorMode);current.setInt(controller,0);((Vector)pages.get(controller)).clear();
                    io.getField("events").set(null,"");io.getField("exists").setBoolean(null,exists);
                    io.getField("fail").set(null,failure==0?"":new String[]{"send","chat","back"}[(failure-1)/2]);io.getField("kind").setInt(null,failure==0?0:1+(failure-1)%2);
                    String result="ok";try{handler.invoke(controller,send,actual);}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;result=t.getClass().getName();}
                    Object retained=box.get(null);if(retained!=null&&retained!=actual)throw new AssertionError("Unexpected replacement TextBox");
                    out.write(isSE+":"+isJP8+":"+text+":"+editorMode+":"+exists+":"+failure+":"+result+":"+(retained==actual)+":"+actual.getString()+":"+current.getInt(controller)+":"+pages.get(controller)+":"+io.getField("events").get(null)+"\n");count++;
                }
        }
        if(count!=336)throw new AssertionError(count);System.out.println("PASS editor send: "+count+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
