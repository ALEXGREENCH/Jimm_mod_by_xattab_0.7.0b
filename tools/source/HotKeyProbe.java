import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Actual dispatcher and both facade entries, real options/contact/XStatus/light state on three platforms. */
public class HotKeyProbe {
    static Properties spec;
    static ClassLoader loader;
    static Class<?> ui,contact,options,vl,light,xstatus,jimm,boundary;
    static Object selected,other,midlet;
    static Method action,single,doubled,setBoolean,getBoolean,setInt,setString;
    static Field lock,menu,zero,lightOn,readXtraz,openChat,statusField;
    static PrintWriter out;
    static int observations,calls,actions,singles,doubles,events,lightCalls,locks;
    static Class<?> load(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> c,String n,Class<?> t)throws Exception{return PopupProbe.field(c,n,t);}
    static Method method(Class<?> c,String n,Class<?> result,Class<?>...types)throws Exception{return PopupProbe.method(c,n,result,types);}
    static void row(String s){out.println(s);observations++;}
    static String utf(Object value){return JimmUrlProbe.utf(value);}
    static Vector log()throws Exception{return(Vector)boundary.getField("events").get(null);}
    static void put(String field,Object value)throws Exception{boundary.getField(field).set(null,value);}
    static boolean option(int key)throws Exception{return(Boolean)getBoolean.invoke(null,key);}
    static String result(Method m,Object...args)throws Exception {
        try{return String.valueOf(m.invoke(null,args));}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static Object create(String uin,String name)throws Exception {
        Object value=contact.getDeclaredConstructor().newInstance();
        Method set=method(contact,spec.getProperty("setContactString"),void.class,int.class,String.class);set.invoke(value,0,uin);set.invoke(value,1,name);return value;
    }
    static void reset(int flags,int status,long previous,long now,boolean shifted)throws Exception {
        log().clear();put("failLabel","");put("failure",null);put("now",Long.valueOf(now));put("clocks",Integer.valueOf(0));put("result",Boolean.valueOf((flags&4)!=0));
        setBoolean.invoke(null,130,(flags&1)!=0);setBoolean.invoke(null,145,(flags&2)!=0);setBoolean.invoke(null,172,(flags&4)!=0);
        setInt.invoke(null,101,(flags&8)==0?37:0);lock.setLong(null,previous);menu.set(null,other);zero.setBoolean(null,shifted);
        if(lightOn!=null)lightOn.setBoolean(null,(flags&8)!=0);
        Object xs=statusField.get(selected);method(xstatus,spec.getProperty("setStatus"),void.class,int.class).invoke(xs,status);
        readXtraz.setBoolean(selected,(flags&1)!=0);openChat.setBoolean(selected,(flags&2)!=0);
    }
    static void snapshot(String label,String outcome,Object receiver)throws Exception {
        Object remembered=menu.get(null);
        row(label+":result="+outcome+":lock="+lock.getLong(null)+":zero="+zero.getBoolean(null)+":menu="+(remembered==null?"null":remembered==selected?"selected":remembered==other?"other":"unknown")
            +":hide="+option(130)+":full="+option(145)+":read="+readXtraz.getBoolean(selected)+":open="+openChat.getBoolean(selected)+":light="+(lightOn==null?"none":lightOn.getBoolean(null))+":clocks="+boundary.getField("clocks").getInt(null));
        for(Object value:log()) {
            Object[] e=(Object[])value,args=(Object[])e[1];String name=(String)e[0];StringBuilder s=new StringBuilder(label+":"+name);
            if(name.equals("lights")||name.equals("flash"))lightCalls++;if(name.equals("lock"))locks++;
            for(Object arg:args) {
                if(arg==null||arg instanceof String)s.append('|').append(utf(arg));
                else if(contact.isInstance(arg))s.append('|').append(arg==selected?"selected":arg==other?"other":"unknown-contact");
                else if(jimm.isInstance(arg))s.append('|').append(arg==midlet?"midlet":"other-midlet");
                else s.append('|').append(arg);
            }
            row(s.toString());events++;
        }
        calls++;
    }
    static void direct()throws Exception {
        int id=0;
        for(int a:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,Integer.MAX_VALUE})for(int type:new int[]{-1,0,1,2,3,4})for(boolean present:new boolean[]{false,true})for(int flags=0;flags<16;flags++) {
            reset(flags,flags%4==0?37:flags%4==1?-1:flags%4==2?0:36,1000L,1901L,false);Object receiver=present?selected:null;
            snapshot("action-"+id++,result(action,a,receiver,type),receiver);actions++;
        }
        for(int type:new int[]{1,2,3})for(long previous:new long[]{-1,0,1000,Long.MIN_VALUE,Long.MAX_VALUE})for(long now:new long[]{-1,0,899,900,901,1900,1901,Long.MIN_VALUE,Long.MAX_VALUE}) {
            reset(0,37,previous,now,false);snapshot("lock-bound-"+id++,result(action,7,selected,type),selected);actions++;
        }
        String[] labels={"history","info","write","save","changed","activate","options","menu","minimize","client","colors","sound","magic","delete","xtraz","phone","lock","platform","lights","old-fullscreen"};
        for(String fail:labels)for(int kind=0;kind<2;kind++)for(int a=2;a<=17;a++) {
            reset(4,0,1000,1901,false);put("failLabel",fail);put("failure",kind==0?new IllegalStateException("injected"):new AssertionError("injected"));
            int type=Integer.parseInt(spec.getProperty("press"));if(a==7)type=2;
            snapshot("fault-"+fail+'-'+kind+'-'+a,result(action,a,selected,type),selected);actions++;
        }
    }
    static void facade()throws Exception {
        int id=0;
        for(int a=0;a<=18;a++)for(int key:new int[]{-11,-10,-9,0,35,42,48,49,52,54,57,1000})for(int type:new int[]{0,1,2,3,4})for(boolean enabled:new boolean[]{false,true}) {
            reset(id%16,0,1000,1901,false);setBoolean.invoke(null,175,enabled);for(int k:new int[]{77,78,79,80,81,82})setInt.invoke(null,k,a);
            snapshot("single-"+id++,result(single,selected,key,type),selected);singles++;
        }
        for(boolean shifted:new boolean[]{false,true})for(int type:new int[]{0,1,2,3,4})for(int key:new int[]{-11,-10,35,42,48,49,50,51,52,53,54,55,56,57,1000})for(int a=0;a<=18;a++) {
            reset(id%16,0,1000,1901,shifted);char[] value=new char[12];Arrays.fill(value,(char)a);setString.invoke(null,40,new String(value));
            snapshot("double-"+id++,result(doubled,selected,key,type),selected);doubles++;
        }
        for(String value:new String[]{null,"",new String(new char[]{2}),new String(new char[]{0,1,2}),new String(new char[]{65535,11,16,17,18,0,7,8,9,10,13,14})})for(int key:new int[]{48,49,52,54,42,35,1000}) {
            reset(0,0,1000,1901,true);
            if(value==null)((Object[])field(options,spec.getProperty("optionTable"),Object[].class).get(null))[40]=null;
            else setString.invoke(null,40,value);
            snapshot("bad-actions-"+id++,result(doubled,selected,key,Integer.parseInt(spec.getProperty("press"))),selected);doubles++;
        }
    }
    static void exercise()throws Exception {
        options=load("options");Object[] table=new Object[256];for(int k=0;k<256;k++)table[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        field(options,spec.getProperty("optionTable"),Object[].class).set(null,table);
        setBoolean=method(options,spec.getProperty("setBoolean"),void.class,int.class,boolean.class);getBoolean=method(options,spec.getProperty("getBoolean"),boolean.class,int.class);
        setInt=method(options,spec.getProperty("setInt"),void.class,int.class,int.class);setString=method(options,spec.getProperty("setString"),void.class,int.class,String.class);
        boundary=load("io");contact=load("contact");xstatus=load("xstatus");jimm=load("jimm");
        Class<?> unsafe=Class.forName("sun.misc.Unsafe");Field instance=unsafe.getDeclaredField("theUnsafe");instance.setAccessible(true);midlet=unsafe.getMethod("allocateInstance",Class.class).invoke(instance.get(null),jimm);
        field(jimm,spec.getProperty("midletField"),jimm).set(null,midlet);
        for(String name:new String[]{spec.getProperty("menuURL"),spec.getProperty("lightURL")})if(name!=null)field(jimm,name,String.class).set(null,name.equals(spec.getProperty("menuURL"))?"menu:\u010d":"light:\u043f");
        ui=load("ui");vl=load("vl");light=spec.getProperty("light").isEmpty()?null:load("light");
        action=method(ui,spec.getProperty("action"),void.class,int.class,contact,int.class);single=method(ui,spec.getProperty("single"),void.class,contact,int.class,int.class);doubled=method(ui,spec.getProperty("double"),boolean.class,contact,int.class,int.class);
        lock=field(ui,spec.getProperty("lock"),long.class);menu=field(ui,spec.getProperty("contactMenu"),contact);zero=field(vl,spec.getProperty("zero"),boolean.class);
        lightOn=light==null?null:field(light,spec.getProperty("lightOn"),boolean.class);
        selected=create("0012345","\u043f\u010d");other=create("54321",null);
        readXtraz=field(contact,spec.getProperty("readXtraz"),boolean.class);openChat=field(contact,spec.getProperty("openChat"),boolean.class);statusField=field(contact,spec.getProperty("xstatusField"),xstatus);
        direct();facade();
        if(events<1000||locks<10)throw new AssertionError("Missing successful hotkey paths");
        row("coverage:"+calls+":"+actions+":"+singles+":"+doubles+":"+events+":"+lightCalls+":"+locks);
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));exercise();out.close();System.out.println("PASS hotkeys: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
