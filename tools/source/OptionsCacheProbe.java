import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute the real cached settings entry point and menu, without application rewrites. */
public class OptionsCacheProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;
    static Class<?> options,form,text;static Field cache,menu,table;
    static Method open,select,index,contents;static Object canvas;static Field control;static int opens,observations;
    static Class<?> load(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> owner,String key,Class<?> type)throws Exception{return PopupProbe.field(owner,spec.getProperty(key),type);}
    static Method method(Class<?> owner,String key,Class<?> type,Class<?>...args)throws Exception{return PopupProbe.method(owner,spec.getProperty(key),type,args);}
    static Object create()throws Exception{Constructor<?> c=form.getDeclaredConstructor();c.setAccessible(true);return c.newInstance();}
    static void row(String value){out.println(value);observations++;}
    static void enter(String label,Object previous)throws Exception {
        try{open.invoke(null);}catch(InvocationTargetException e){throw new AssertionError("Settings activation failed: "+label,e.getCause());}
        Object next=cache.get(null),list=menu.get(next);
        String value=(String)contents.invoke(list,0,true,-1);
        row(label+":"+(next==previous)+":"+(next!=null)+":"+(control.get(canvas)==list)+":"+index.invoke(list)+":"+Base64.getEncoder().encodeToString(value.getBytes("UTF-8"))+":"+field(form,"language",String.class).get(next)+":"+field(form,"offline",boolean.class).get(next)+":"+field(form,"empty",boolean.class).get(next)+":"+field(form,"groups",boolean.class).get(next));opens++;
    }
    static void exercise()throws Exception {
        for(boolean seeded:new boolean[]{false,true})for(int connection:new int[]{0,2})for(int font:new int[]{0,2})
            for(int selection:new int[]{-1,0,2,9,12,99})for(int flags=0;flags<8;flags++) {
                Object[] values=(Object[])table.get(null);values[83]=connection;values[111]=font;
                Object previous=seeded?create():null;cache.set(null,previous);
                String label=seeded+":"+connection+":"+font+":"+selection+":"+flags;
                for(int round=0;round<3;round++) {
                    int mask=(flags+round)%8;values[3]="cache-"+round;values[130]=(mask&1)!=0;values[129]=(mask&2)!=0;values[136]=(mask&4)!=0;
                    enter("open:"+label+":"+round,previous);previous=cache.get(null);
                    if(round==0)select.invoke(menu.get(previous),selection);
                }
                cache.set(null,null);enter("clear:"+label,previous);
            }
        if(opens!=1536)throw new AssertionError("Missing settings opens: "+opens);row("coverage:"+opens);
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();options=load("options");
        Object[] values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        table=field(options,"table",Object[].class);table.set(null,values);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));
        if(spec.containsKey("bitmap")) {Class<?> bitmap=load("bitmap");Object font=bitmap.getDeclaredConstructor(String.class).newInstance("/font.prs");field(bitmap,"bitmapField",bitmap).set(null,font);}
        form=load("form");text=load("text");cache=field(options,"cache",form);menu=field(form,"menu",text);
        Class<?> vl=text.getSuperclass(),vc=load("canvasType");canvas=field(vl,"canvas",vc).get(null);control=field(vc,"control",vl);
        open=method(options,"open",void.class);select=method(text,"select",void.class,int.class);index=method(text,"index",int.class);
        contents=method(text,"contents",String.class,int.class,boolean.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));final Throwable[] failure=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(host).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){failure[0]=e;}finally{done.countDown();}}});
        if(!done.await(55,TimeUnit.SECONDS))throw new AssertionError("Settings cache probe timeout");out.close();if(failure[0]!=null)throw new AssertionError("Settings cache probe failed",failure[0]);
        System.out.println("PASS settings cache: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
