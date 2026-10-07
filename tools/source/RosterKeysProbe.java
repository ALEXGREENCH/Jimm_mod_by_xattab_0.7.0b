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

/** Whole roster callback, real hotkeys/tree lookup and real list cursor movement. */
public class RosterKeysProbe {
    static Properties spec; static ClassLoader loader; static PrintWriter out;
    static Class<?> vl,text,list,tree,node,contact,options,io;
    static Object controller,roster; static Object[] values;
    static Field current,top,zero,drawItems,treeField; static Method callback,add,newline;
    static int observations,moves,callbacks,lookups,consumed;
    static Class<?> type(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> c,String key,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(key),t);}
    static Method method(Class<?> c,String key,Class<?> t,Class<?>...p)throws Exception{return PopupProbe.method(c,spec.getProperty(key),t,p);}
    static String execute(Object sender,int key,int kind)throws Exception {
        try{callback.invoke(controller,sender,key,kind);return "ok";}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw (LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static Object screen(int size,int height,int index,int first)throws Exception {
        Object s=text.getDeclaredConstructor(String.class).newInstance("Roster paging");
        for(int i=0;i<size;i++){add.invoke(s,"Row "+i,0x123456,0,i);newline.invoke(s,i);}
        field(vl,"height",int.class).setInt(s,height);field(vl,"width",int.class).setInt(s,176);
        current.setInt(s,index);top.setInt(s,first);return s;
    }
    static void selection(int kind)throws Exception {
        treeField.set(null,kind==3?null:roster);
        Vector nodes=new Vector();
        if(kind==1||kind==2){Constructor<?> c=node.getDeclaredConstructor(Object.class);c.setAccessible(true);
            Object data=kind==1?"group-like non-contact":contact.getDeclaredConstructor().newInstance();nodes.addElement(c.newInstance(data));}
        drawItems.set(roster,nodes);current.setInt(roster,0);
    }
    static void exercise(String label,Object sender,int key,int kind,boolean pending,boolean enabled,int selected,int game,int failure,boolean scripted)throws Exception {
        selection(selected);zero.setBoolean(null,pending);values[175]=enabled;
        Vector calls=null;if(scripted){io.getField("game").setInt(null,game);io.getField("failure").setInt(null,failure);calls=(Vector)io.getField("calls").get(null);calls.clear();}
        int was=sender==null?0:current.getInt(sender);
        String answer=execute(sender,key,kind);int now=sender==null?0:current.getInt(sender);
        if(now!=was)moves++;if(scripted)lookups+=calls.size();if(pending&&!zero.getBoolean(null))consumed++;
        out.println(label+":"+answer+":"+now+":"+(sender==null?0:top.getInt(sender))+":"+zero.getBoolean(null)+(scripted?":"+calls:""));observations++;callbacks++;
    }
    static void cases(boolean scripted)throws Exception {
        int[] keys={Integer.MIN_VALUE,-256,-26,-22,-21,-11,-10,-8,-7,-6,-4,-3,-2,-1,0,1,2,5,6,8,42,48,49,50,51,52,53,54,55,57,35,57345,57346,1000003,1000004,Integer.MAX_VALUE};
        for(int key:keys)for(int kind:new int[]{0,1,2,3,4})for(boolean pending:new boolean[]{false,true})for(boolean enabled:new boolean[]{false,true})for(int selected:new int[]{0,1,2,3}) {
            // Delete-contact UI is outside this focused paging probe; its no-selection early return is exercised.
            if(key==-8&&selected==2)continue;
            Object s=screen(23,110,8,3);int game=key%2==0?2:5;
            exercise("key:"+key+":"+kind+":"+pending+":"+enabled+":"+selected,s,key,kind,pending,enabled,selected,game,0,scripted);
        }
        int trigger=Integer.parseInt(spec.getProperty("trigger"));
        for(int size:new int[]{0,1,2,23})for(int height:new int[]{1,40,110,300})for(int index:new int[]{-1,0,1,8,22,30})for(int first:new int[]{-1,0,3,22,30})for(int key:new int[]{-3,-4}) {
            Object s=screen(size,height,index,first);
            exercise("metrics:"+size+":"+height+":"+index+":"+first+":"+key,s,key,trigger,false,false,0,key==-3?2:5,0,scripted);
        }
        if(scripted)for(int game:new int[]{Integer.MIN_VALUE,-1,0,1,2,5,6,8,Integer.MAX_VALUE})for(int failure:new int[]{0,1,2})for(int key:new int[]{-3,-4,52,54,49,-8,-26})for(int kind:new int[]{0,1,2,3,4}) {
            exercise("device:"+game+":"+failure+":"+key+":"+kind,screen(23,110,8,3),key,kind,false,false,0,game,failure,true);
        }
        // Receiver-free getGameAction becomes static in ProGuard. Null sender traces are recorded separately.
        if(!scripted)for(int key:new int[]{-3,-4,52,54,49,-8,-26})for(int kind:new int[]{0,1,2,3,4})
            exercise("null:"+key+":"+kind,null,key,kind,false,false,0,0,0,false);
        out.println("coverage:"+callbacks+":"+moves+":"+lookups+":"+consumed);
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();
        options=type("options");values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[3]="RU";values[40]=new String(new char[12]);field(options,"table",Object[].class).set(null,values);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));field(jimm,"version",String.class).set(null,"0.7.0b");
        if(spec.containsKey("bitmap")){Class<?> bitmap=type("bitmap");field(bitmap,"bitmapField",bitmap).set(null,bitmap.getDeclaredConstructor(String.class).newInstance("/font.prs"));}
        vl=type("vl");text=type("text");list=type("list");tree=type("tree");node=type("node");contact=type("contact");
        controller=list.getDeclaredConstructor().newInstance();treeField=field(list,"treeField",tree);roster=treeField.get(null);
        current=field(vl,"current",int.class);top=field(vl,"top",int.class);zero=field(vl,"zero",boolean.class);drawItems=field(tree,"drawItems",Vector.class);
        callback=method(list,"callback",void.class,vl,int.class,int.class);add=method(text,"addText",text,String.class,int.class,int.class,int.class);newline=method(text,"newline",text,int.class);
        boolean scripted=Boolean.parseBoolean(args[3]);if(scripted)io=Class.forName("RosterKeysIO",true,loader);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));cases(scripted);out.close();System.out.println("PASS roster keys: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
