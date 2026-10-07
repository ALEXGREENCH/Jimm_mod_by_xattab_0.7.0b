import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Actual extended-key lookup and private menu/cursor reaction on three release targets. */
public class ExtendedKeysProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;
    static Class<?> vl,text,io;static Method action,reaction;static int observations,lookups,reactions,scriptedCases;
    static int commandCalls,cursorMoves;static Vector commands=new Vector();
    static Field field(Class<?> c,String n,Class<?> t)throws Exception{return PopupProbe.field(c,n,t);}
    static Method method(Class<?> c,String n,Class<?> t,Class<?>...p)throws Exception{return PopupProbe.method(c,n,t,p);}
    static void row(String value){out.println(value);observations++;}
    static String result(Method m,Object receiver,Object...args)throws Exception {
        try{return String.valueOf(m.invoke(receiver,args));}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static Object create()throws Exception{Constructor<?> c=text.getDeclaredConstructor(String.class);c.setAccessible(true);return c.newInstance("Keys");}
    static void natural()throws Exception {
        Object screen=create();TreeSet<Integer> keys=new TreeSet<Integer>();for(int k=-256;k<=256;k++)keys.add(k);
        for(int k:new int[]{57344,57345,57346,57347,1000001,1000002,1000003,1000004,Integer.MIN_VALUE,Integer.MAX_VALUE})keys.add(k);
        for(int k:keys){row("lookup:"+k+":"+result(action,screen,k));lookups++;}
        CommandListener callback=new CommandListener(){public void commandAction(Command c,Displayable d){commands.addElement(c.getLabel()+":"+c.getCommandType()+":"+c.getPriority()+":"+(d==null));commandCalls++;}};
        Method add=method(text,spec.getProperty("addText"),text,String.class,int.class,int.class,int.class);
        Method newline=method(text,spec.getProperty("newline"),text,int.class);
        Method addCommand=method(vl,spec.getProperty("addCommand"),void.class,Command.class,int.class);
        Field current=field(vl,spec.getProperty("current"),int.class),top=field(vl,spec.getProperty("top"),int.class),state=field(vl,spec.getProperty("state"),int.class);
        for(int key:new int[]{-256,-203,-202,-22,-21,-11,-7,-6,-5,-2,-1,0,1,2,5,6,8,9,21,22,48,49,50,51,55,57,105,106,112,113,57345,57346,1000001,1000002,1000003,1000004,Integer.MIN_VALUE,Integer.MAX_VALUE})
            for(int type:new int[]{0,1,2,3,4})for(int menu:new int[]{0,1,2}) {
                screen=create();for(int i=0;i<9;i++){add.invoke(screen,"Row "+i,0x123456,0,i);newline.invoke(screen,i);}
                method(vl,spec.getProperty("listener"),void.class,CommandListener.class).invoke(screen,callback);
                addCommand.invoke(screen,new Command("Left",Command.OK,1),1);addCommand.invoke(screen,new Command("Right",Command.BACK,2),2);
                addCommand.invoke(screen,new Command("Left item",Command.SCREEN,3),3);addCommand.invoke(screen,new Command("Right item",Command.SCREEN,4),4);
                field(vl,spec.getProperty("height"),int.class).setInt(screen,110);field(vl,spec.getProperty("width"),int.class).setInt(screen,176);
                current.setInt(screen,2);top.setInt(screen,0);state.setInt(screen,menu);commands.clear();
                if(reactions==0)row("metrics:"+field(vl,spec.getProperty("fontSize"),int.class).getInt(screen)+":"+method(vl,spec.getProperty("capHeightMethod"),int.class).invoke(screen)+":"+method(vl,spec.getProperty("menuHeightMethod"),int.class).invoke(screen)+":"+method(vl,spec.getProperty("visMethod"),int.class).invoke(screen)+":"+method(text,spec.getProperty("itemHeightMethod"),int.class,int.class).invoke(screen,0));
                field(vl,spec.getProperty("menuIndex"),int.class).setInt(null,0);field(vl,spec.getProperty("topMenu"),int.class).setInt(null,0);
                field(vl,spec.getProperty("visibleMenu"),int.class).setInt(null,1);field(vl,spec.getProperty("zero"),boolean.class).setBoolean(null,false);
                String answer=result(reaction,screen,key,type);int index=current.getInt(screen);if(index!=2)cursorMoves++;
                row("reaction:"+key+":"+type+":"+menu+":"+answer+":"+index+":"+top.getInt(screen)+":"+state.getInt(screen)+":"+field(vl,spec.getProperty("menuIndex"),int.class).getInt(null)+":"+field(vl,spec.getProperty("topMenu"),int.class).getInt(null)+":"+commands);
                reactions++;
            }
        if(commandCalls==0||cursorMoves==0)throw new AssertionError("Missing successful commands/cursor movement: "+commandCalls+"/"+cursorMoves);
        row("coverage:"+lookups+":"+reactions+":"+commandCalls+":"+cursorMoves);
    }
    static void scripted()throws Exception {
        io=Class.forName("ExtendedKeysIO",true,loader);Object screen=create();Vector calls=(Vector)io.getField("calls").get(null);
        String[] names={null,"","x","SOFT1","soft 1","soft_1","softkey 1","sk2(left)","left soft key", "SOFT2","soft 2","soft_2","softkey 4","sk1(right)","right soft key","ON/OFF","back"};
        for(String name:names)for(int key:new int[]{Integer.MIN_VALUE,-203,-202,-22,-21,-11,-7,-6,-1,0,1,6,8,21,22,105,106,57345,57346,Integer.MAX_VALUE})
            for(int game:new int[]{Integer.MIN_VALUE,-1,0,1,6,8,Integer.MAX_VALUE}) {
                io.getField("name").set(null,name);io.getField("game").setInt(null,game);io.getField("nameFailure").setInt(null,0);io.getField("gameFailure").setInt(null,0);calls.clear();
                row("script:"+(name==null?"null":Base64.getEncoder().encodeToString(name.getBytes("UTF-8")))+":"+key+":"+game+":"+result(action,screen,key)+":"+calls);scriptedCases++;
            }
        for(int nameFailure:new int[]{0,1,2,3})for(int gameFailure:new int[]{0,1,2,3})for(int key:new int[]{-22,-21,-6,-7,0,1,Integer.MIN_VALUE,Integer.MAX_VALUE}) {
            io.getField("name").set(null,"x");io.getField("game").setInt(null,6);io.getField("nameFailure").setInt(null,nameFailure);io.getField("gameFailure").setInt(null,gameFailure);calls.clear();
            row("failure:"+nameFailure+":"+gameFailure+":"+key+":"+result(action,screen,key)+":"+calls);scriptedCases++;
        }
        if(scriptedCases!=2508)throw new AssertionError("Missing scripted device cases");row("coverage:"+scriptedCases);
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);loader=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();
        Class<?> options=Class.forName(spec.getProperty("options"),true,loader);Object[] table=new Object[256];for(int k=0;k<256;k++)table[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        field(options,spec.getProperty("table"),Object[].class).set(null,table);vl=Class.forName(spec.getProperty("vl"),true,loader);text=Class.forName(spec.getProperty("text"),true,loader);
        method(vl,spec.getProperty("assign"),void.class).invoke(null);action=method(vl,spec.getProperty("action"),int.class,int.class);reaction=method(vl,spec.getProperty("reaction"),void.class,int.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));if(Boolean.parseBoolean(args[3]))scripted();else natural();out.close();System.out.println("PASS extended keys: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
