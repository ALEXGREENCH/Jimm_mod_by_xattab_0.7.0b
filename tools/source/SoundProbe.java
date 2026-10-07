import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute actual sound helpers and notifications, with scripted device API objects only. */
public class SoundProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;static Class<?> list,io,options;
    static Field player,self,built,locked,table,display;static Method create,test,close,volume,notify,event,select;
    static Object controller;static Object[] initialValues;static Display actual;static int observations,creations,tests,closures,volumes,events,selections,notifications;
    static Class<?> type(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> c,String key,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(key),t);}
    static Method method(Class<?> c,String key,Class<?> t,Class<?>...args)throws Exception{return PopupProbe.method(c,spec.getProperty(key),t,args);}
    static String encoded(Object value)throws Exception{return value==null?"null":Base64.getEncoder().encodeToString(String.valueOf(value).getBytes("UTF-8"));}
    static void setting(String name,Object value)throws Exception{io.getField(name).set(null,value);}
    static Object[] values()throws Exception{return (Object[])table.get(null);}
    static void reset(int state,int at,int failure)throws Exception {
        io.getMethod("reset").invoke(null);table.set(null,initialValues.clone());self.set(null,controller);setting("expectedListener",controller);built.setBoolean(null,true);locked.setBoolean(null,false);display.set(null,actual);
        player.set(null,state<0?null:io.getMethod("seeded",int.class).invoke(null,state));setting("failAt",at);setting("failure",failure);
    }
    static void resource(String name)throws Exception{if(name!=null)((Hashtable)io.getField("resources").get(null)).put(name,new byte[]{7,0,9});}
    static String execute(Method m,Object receiver,Object...args)throws Exception {
        try{Object result=m.invoke(receiver,args);return result instanceof Player?"player:"+(result==player.get(null)):"ok:"+result;}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static void observation(String label,String outcome,int option)throws Exception {
        String snapshot=(String)io.getMethod("snapshot",Player.class).invoke(null,player.get(null));Vector calls=(Vector)io.getField("calls").get(null);StringBuilder b=new StringBuilder();
        for(Object call:calls)b.append('[').append(encoded(call)).append(']');
        out.println(label+":"+outcome+":"+snapshot+":lock="+Thread.holdsLock(controller)+(option>=0?":option="+encoded(values()[option]):"")+":calls="+b);observations++;
    }
    static int[][] faults(int maximum,boolean checked){ArrayList<int[]> f=new ArrayList<int[]>();f.add(new int[]{0,0});for(int i=1;i<=maximum;i++)for(int kind=1;kind<=(checked?3:2);kind++)f.add(new int[]{i,kind});return f.toArray(new int[0][]);}
    static void helpers()throws Exception {
        String[] paths={null,"",".","name","/","a.wav","a.MP3","a.mid","a.MIDI","a.amr","a.AMR","a.ogg","a.","a.mp3?x","dir.a/file","/dir/a.mp3","//a.mid","x.y.mp3","a.mp3/part","\u0418\u043c\u044f.wav","a\u0000.mp3","a\n.mid"};
        for(String path:paths)for(int mask=0;mask<4;mask++)for(int state:new int[]{-1,100,400})for(int[] fault:faults(9,true))for(boolean check:new boolean[]{false,true}) {
            reset(state,fault[0],fault[1]);if((mask&1)!=0)resource(path);if((mask&2)!=0&&path!=null)resource("/"+path);
            String result=execute(check?test:create,null,(Object)path);observation((check?"test:":"create:")+encoded(path)+":"+mask+":"+state+":"+fault[0]+":"+fault[1],result,-1);if(check)tests++;else creations++;
        }
        for(int state:new int[]{-1,0,100,200,300,400,999})for(int[] fault:faults(3,false)) {
            reset(state,fault[0],fault[1]);observation("close:"+state+":"+fault[0]+":"+fault[1],execute(close,null),-1);closures++;
        }
        for(int state:new int[]{-1,100,400})for(int control=0;control<3;control++)for(int level:new int[]{Integer.MIN_VALUE,-1,0,1,50,100,101,Integer.MAX_VALUE})for(int[] fault:faults(4,true)) {
            reset(state,fault[0],fault[1]);setting("controlMode",control);observation("volume:"+state+":"+control+":"+level+":"+fault[0]+":"+fault[1],execute(volume,null,level),-1);volumes++;
        }
        for(int state:new int[]{-1,100,400})for(String name:new String[]{null,"","endOfMedia","ENDOFMEDIA","stopped"})for(int[] fault:faults(1,false)) {
            reset(state,fault[0],fault[1]);observation("event:"+state+":"+encoded(name)+":"+fault[0]+":"+fault[1],execute(event,controller,player.get(null),name,new Object()),-1);events++;
        }
        for(boolean check:new boolean[]{false,true})for(int state:new int[]{-1,100,400}) {
            reset(state,0,0);setting("nullPlayer",true);resource("a.mp3");observation("null-player:"+check+":"+state,execute(check?test:create,null,"a.mp3"),-1);if(check)tests++;else creations++;
        }
    }
    static void selections()throws Exception {
        for(int option:new int[]{4,5,16,41})for(String base:new String[]{null,"/sound."})for(String before:new String[]{null,"old.mp3"})for(int mask=0;mask<8;mask++)for(boolean fallback:new boolean[]{false,true})for(int[] fault:new int[][]{{0,0},{1,1},{1,2},{2,1},{2,2},{3,1},{3,2},{5,1},{5,2},{8,1},{8,2},{12,1},{12,2},{16,1},{16,2},{2,3},{8,3}}) {
            reset(-1,fault[0],fault[1]);values()[option]=before;String prefix=fallback?"/":"";
            if((mask&1)!=0&&before!=null)resource(prefix+before);if((mask&2)!=0)resource(prefix+base+"wav");if((mask&4)!=0)resource(prefix+base+"mp3");
            observation("select:"+option+":"+encoded(base)+":"+encoded(before)+":"+mask+":"+fallback+":"+fault[0]+":"+fault[1],execute(select,null,base,option),option);selections++;
        }
    }
    static void notification(int kind,boolean ready,boolean silent,boolean lock,int vibrator,int mode,int level,int mask,int at,int failure,boolean instance,boolean shown)throws Exception {
        reset(400,at,failure);if(!instance){self.set(null,null);setting("expectedListener",null);}if(!shown)display.set(null,null);built.setBoolean(null,ready);locked.setBoolean(null,lock);
        Object[] v=values();v[75]=vibrator;v[150]=silent;v[66]=v[68]=v[88]=v[99]=mode;v[67]=level;
        v[4]="message.mp3";v[5]="online.mid";v[16]="typing.amr";v[41]="offline.wav";
        for(String path:new String[]{"message.mp3","online.mid","typing.amr","offline.wav"})if((mask&2)!=0)resource(path);
        if((mask&1)!=0)resource("silence.wav");
        observation("notify:"+kind+":"+ready+":"+silent+":"+lock+":"+vibrator+":"+mode+":"+level+":"+mask+":"+at+":"+failure+":"+instance+":"+shown,execute(notify,null,kind),-1);notifications++;
    }
    static void notifications()throws Exception {
        int sequence=0;for(int kind:new int[]{-1,0,1,2,3,4,5,Integer.MAX_VALUE})for(boolean ready:new boolean[]{false,true})for(boolean silent:new boolean[]{false,true})for(boolean lock:new boolean[]{false,true})for(int vibrator:new int[]{-1,0,1,2,3})for(int mode:new int[]{-1,0,1,2,3,Integer.MAX_VALUE}) {
            int level=new int[]{Integer.MIN_VALUE,-1,0,100,Integer.MAX_VALUE}[sequence++%5];notification(kind,ready,silent,lock,vibrator,mode,level,3,0,0,true,true);
        }
        for(int kind:new int[]{1,2,3,4,9})for(int mode:new int[]{1,2,3})for(int level:new int[]{-1,70,Integer.MAX_VALUE})for(int mask=0;mask<4;mask++)for(int[] fault:faults(16,true))notification(kind,true,false,false,1,mode,level,mask,fault[0],fault[1],true,true);
        for(int kind:new int[]{1,2,3,4})for(boolean ready:new boolean[]{false,true})for(boolean instance:new boolean[]{false,true})for(boolean shown:new boolean[]{false,true})for(boolean silent:new boolean[]{false,true})notification(kind,ready,silent,true,2,2,70,3,0,0,instance,shown);
        for(int kind:new int[]{1,2,3,4})for(int mode:new int[]{1,2,3})for(int broken:new int[]{-3,-2,-1,4,5,16,41,66,67,68,75,88,99,150})for(boolean missing:new boolean[]{false,true}) {
            reset(400,0,0);Object[] v=values();v[75]=1;v[150]=false;v[66]=v[68]=v[88]=v[99]=mode;v[67]=70;v[4]="message.mp3";v[5]="online.mid";v[16]="typing.amr";v[41]="offline.wav";
            for(String path:new String[]{"silence.wav","message.mp3","online.mid","typing.amr","offline.wav"})resource(path);
            if(broken==-3)table.set(null,null);else if(broken==-2)table.set(null,new Object[0]);else if(broken==-1)table.set(null,Arrays.copyOf(v,150));else v[broken]=missing?null:broken<64?Integer.valueOf(17):"wrong type";
            observation("notify-poison:"+kind+":"+mode+":"+broken+":"+missing,execute(notify,null,kind),-1);notifications++;
        }
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();actual=Display.getDisplay(host);
        options=type("options");table=field(options,"table",Object[].class);Object[] v=new Object[256];for(int k=0;k<v.length;k++)v[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);v[3]="RU";initialValues=v;table.set(null,v);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);display=field(jimm,"display",Display.class);display.set(null,actual);field(jimm,"version",String.class).set(null,"0.7.0b");
        if(spec.containsKey("bitmap")){Class<?> bitmap=type("bitmap");Object font=bitmap.getDeclaredConstructor(String.class).newInstance("/font.prs");field(bitmap,"bitmapField",bitmap).set(null,font);}
        list=type("list");io=Class.forName("SoundIO",true,loader);player=field(list,"player",Player.class);self=field(list,"self",list);built=field(list,"built",boolean.class);Class<?> splash=type("splash");locked=field(splash,"locked",boolean.class);
        Constructor<?> ctor=list.getDeclaredConstructor();ctor.setAccessible(true);controller=ctor.newInstance();
        create=method(list,"create",Player.class,String.class);test=method(list,"test",boolean.class,String.class);close=method(list,"close",void.class);volume=method(list,"volume",void.class,int.class);notify=method(list,"notify",void.class,int.class);event=method(list,"event",void.class,Player.class,String.class,Object.class);select=method(options,"select",void.class,String.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));helpers();selections();notifications();out.println("coverage:"+creations+":"+tests+":"+closures+":"+volumes+":"+events+":"+selections+":"+notifications);out.close();System.out.println("PASS genuine sound: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
