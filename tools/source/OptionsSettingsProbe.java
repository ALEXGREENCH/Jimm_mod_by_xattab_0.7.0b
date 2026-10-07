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

/** Genuine defaults, font-style update and alpha cache transitions across device inputs. */
public class OptionsSettingsProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;
    static Class<?> options,vl,io;static Field table,display,cursor,caption,softbar,gradient,font;
    static Method defaults,alpha,style;static Display actual;static boolean scripted;
    static int observations,defaultCalls,fontCalls,alphaCalls;static Field[] deviceFields;
    static Class<?> load(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> c,String key,Class<?> type)throws Exception{return PopupProbe.field(c,spec.getProperty(key),type);}
    static Method method(Class<?> c,String key)throws Exception{return PopupProbe.method(c,spec.getProperty(key),void.class);}
    static String call(Method m)throws Exception {
        try{m.invoke(null);return "ok";}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static void row(String value){out.println(value);observations++;}
    static Object[] values(int profile) {
        Object[] v=new Object[256];if(profile==0)return v;
        for(int k=0;k<v.length;k++)v[k]=k<64||k>=224?"seed"+k:k<128?Integer.valueOf(k*7):k<192?Boolean.valueOf((k&1)==0):Long.valueOf(Long.MIN_VALUE+k);
        return v;
    }
    static String encoded(Object value)throws Exception{return value==null?"null":value.getClass().getName()+":"+Base64.getEncoder().encodeToString(String.valueOf(value).getBytes("UTF-8"));}
    static void defaults()throws Exception {
        for(int device=0;device<(1<<deviceFields.length);device++)for(int profile=0;profile<2;profile++) {
            for(int i=0;i<deviceFields.length;i++)deviceFields[i].setBoolean(null,(device&(1<<i))!=0);
            table.set(null,values(profile));row("defaults:"+device+":"+profile+":"+call(defaults));defaultCalls++;
            Object[] v=(Object[])table.get(null);for(int k=0;k<v.length;k++)row("value:"+device+":"+profile+":"+k+":"+encoded(v[k]));
        }
        for(Field f:deviceFields)f.setBoolean(null,false);
        for(int v:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,4,8,16,Integer.MAX_VALUE})for(int before:new int[]{-1,0,2,99}) {
            Object[] tableValue=values(1);tableValue[112]=v;table.set(null,tableValue);font.setInt(null,before);
            row("font:"+v+":"+before+":"+call(style)+":"+font.getInt(null));fontCalls++;
        }
    }
    static void alpha()throws Exception {
        for(boolean present:new boolean[]{false,true})for(int level:scripted?new int[]{Integer.MIN_VALUE,-1,0,1,2,3,256,Integer.MAX_VALUE}:new int[]{0})
            for(int failure:scripted?new int[]{0,1,2}:new int[]{0})for(int cached:new int[]{Integer.MIN_VALUE,-1,0,1,7,Integer.MAX_VALUE})
                for(int value:new int[]{Integer.MIN_VALUE,-1,0,1,128,255,256,Integer.MAX_VALUE})for(int broken:new int[]{-1,0,1,2}) {
                    Object[] v=values(1);v[100]=value;v[116]=value+1;v[117]=value-1;if(broken>=0)v[new int[]{100,116,117}[broken]]="wrong type";
                    table.set(null,v);cursor.setInt(null,317);caption.setInt(null,-217);softbar.setInt(null,42);gradient.setInt(null,cached);display.set(null,present?actual:null);
                    Vector calls=null;if(scripted){io.getField("levels").setInt(null,level);io.getField("failure").setInt(null,failure);calls=(Vector)io.getField("calls").get(null);calls.clear();}
                    row("alpha:"+present+":"+level+":"+failure+":"+cached+":"+value+":"+broken+":"+call(alpha)+":"+cursor.getInt(null)+":"+caption.getInt(null)+":"+softbar.getInt(null)+":"+gradient.getInt(null)+(scripted?":"+calls:""));alphaCalls++;
                }
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}scripted=Boolean.parseBoolean(args[3]);
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> p=new ArrayList<String>();Collections.addAll(p,"--rms","memory",args[0]);
        emulator.initParams(p,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();actual=Display.getDisplay(host);
        options=load("options");table=field(options,"table",Object[].class);table.set(null,values(1));Class<?> jimm=Class.forName("jimm.Jimm",true,loader);display=field(jimm,"display",Display.class);display.set(null,actual);
        vl=load("vl");cursor=field(options,"cursor",int.class);caption=field(options,"caption",int.class);softbar=field(options,"softbar",int.class);gradient=field(vl,"gradient",int.class);font=field(options,"font",int.class);
        defaults=method(options,"defaults");alpha=method(options,"alpha");style=method(options,"style");if(scripted)io=Class.forName("OptionsSettingsIO",true,loader);
        String devices=spec.getProperty("devices");String[] flags=devices.length()==0?new String[0]:devices.split(",");deviceFields=new Field[flags.length];for(int i=0;i<flags.length;i++)deviceFields[i]=PopupProbe.field(jimm,flags[i],boolean.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));defaults();alpha();row("coverage:"+defaultCalls+":"+fontCalls+":"+alphaCalls);out.close();System.out.println("PASS settings state: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
