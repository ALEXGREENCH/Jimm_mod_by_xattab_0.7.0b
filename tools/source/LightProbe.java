import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Actual May backlight controller, task delivery, failure ordering and cached timeout. */
public class LightProbe extends GraphicsProbe {
    static Class<?> light;
    static Field on,task;
    static int seed;
    static boolean nokia;
    static void input(String name,Object value)throws Exception{io.getField(name).set(null,value);}
    static void option(int index,Object value)throws Exception{((Object[])f(options,"a","options",Object[].class).get(null))[index]=value;}
    static String old(String name){if(name.equals("flash"))return "a";if(name.equals("reset"))return "a";if(name.equals("changeState"))return nokia?"b":"a";if(name.equals("Off"))return nokia?"c":"b";if(name.equals("On"))return nokia?"e":"c";return "d";}
    static Object call(String name,Object...args)throws Exception{try{return m(light,old(name),name,void.class,args.length==0?new Class<?>[0]:new Class<?>[]{boolean.class}).invoke(null,args);}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken light fixture: "+name,e.getCause());return "exception:"+e.getCause().getClass().getName();}}
    static Object pending()throws Exception{return nokia?task.get(null):null;}
    static void state(String label,Object result)throws Exception{row(label+":"+result+":on="+on.getBoolean(null)+":pending="+(pending()!=null)+":io="+io.getField("log").get(null)+":scheduled="+((Vector)io.getField("tasks").get(null)).size());}
    static void reset(boolean initial,boolean manual,boolean supported,int level,int failure,boolean result)throws Exception {
        io.getMethod("reset").invoke(null);on.setBoolean(null,initial);if(nokia)task.set(null,null);option(140,manual);option(101,level);option(74,123);input("supported",supported);input("failure",failure);input("result",result);
    }
    static void tick(Object value,String label)throws Exception {
        if(value==null){row(label+":no-task");return;}try{Method run=value.getClass().getMethod("run");run.setAccessible(true);run.invoke(value);state(label,null);}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError(e.getCause());state(label,"exception:"+e.getCause().getClass().getName());}
    }
    static void missingDisplay()throws Exception {
        if(nokia)return;
        Field display=f(load("jimm.Jimm","jimm.Jimm"),"a","display",javax.microedition.lcdui.Display.class);Object saved=display.get(null);display.set(null,null);
        try {for(boolean initial:new boolean[]{false,true})for(boolean manual:new boolean[]{false,true})for(String command:new String[]{"On","Off","changeState","flash-true","flash-false"}){
            reset(initial,manual,true,70,0,true);Object value=command.startsWith("flash-")?call("flash",command.equals("flash-true")):call(command);boolean gated=manual&&command.startsWith("flash-");if(gated?value!=null:!"exception:java.lang.NullPointerException".equals(value))throw new AssertionError("Null Display receiver: "+value);if(on.getBoolean(null)!=initial||io.getField("log").get(null).toString().length()!=0)throw new AssertionError("Null receiver reached hardware or changed state");state("null-display-"+initial+"-"+manual+"-"+command,value);
        }}finally{display.set(null,saved);}
    }
    static void exercise()throws Exception {
        nokia=target.equals("MIDP2");options=load(nokia?"cj":"ci","jimm.Options");Object[] values=new Object[256];for(int i=0;i<256;i++)values[i]=i<64?"":i<128?Integer.valueOf(0):i<192?Boolean.FALSE:i<224?Long.valueOf(0):"";f(options,"a","options",Object[].class).set(null,values);option(74,seed);option(101,70);
        f(load("jimm.Jimm","jimm.Jimm"),"a","display",javax.microedition.lcdui.Display.class).set(null,javax.microedition.lcdui.Display.getDisplay(MIDletBridge.getCurrentMIDlet()));
        io=load("LightIO","LightIO");light=Class.forName(n("aj","DrawControls.LightControl"),false,loader);on=f(light,"a","lightOn",boolean.class);io.getField("on").set(null,on);Class.forName(light.getName(),true,loader);task=nokia?f(light,"a","lightTask",java.util.TimerTask.class):null;
        state("initial-"+seed,null);if(!"init-on:true;".equals(io.getField("log").get(null).toString()))throw new AssertionError("Initial light flag was not set before reading timeout");int cached=f(light,"a","TIMEOUT",int.class).getInt(null);if(cached!=seed*1000)throw new AssertionError("Cached timeout");row("timeout:"+cached);
        for(boolean initial:new boolean[]{false,true})for(int level:new int[]{-1,0,1,50,100,101,Integer.MIN_VALUE,Integer.MAX_VALUE})for(int failure:new int[]{0,1,2})for(boolean result:new boolean[]{false,true})for(String command:new String[]{"On","Off","changeState"}){
            reset(initial,false,true,level,failure,result);Object value=call(command);boolean expected=command.equals("On")||command.equals("changeState")&&!initial;if(failure==0){if(value!=null||on.getBoolean(null)!=expected)throw new AssertionError("Manual light state");}else if(on.getBoolean(null)!=initial||!(value instanceof String))throw new AssertionError("Failure updated light state");state("manual-"+initial+"-"+level+"-"+failure+"-"+result+"-"+command,value);
        }
        for(boolean manual:new boolean[]{false,true})for(boolean supported:new boolean[]{false,true})for(boolean constant:new boolean[]{false,true})for(int failure:new int[]{0,1,2})for(boolean result:new boolean[]{false,true}){
            reset(false,manual,supported,70,failure,result);String label="flash-"+manual+"-"+supported+"-"+constant+"-"+failure+"-"+result;Object value=call("flash",constant);state(label,value);if(failure==0&&(!nokia||!manual||!supported||constant||cached>=0)&&value!=null)throw new AssertionError("Unexpected flash failure");if(nokia)tick(pending(),label+"-tick");
        }
        if(nokia) {
            for(boolean manual:new boolean[]{false,true})for(boolean supported:new boolean[]{false,true})for(boolean stopped:new boolean[]{false,true}){
                reset(false,manual,supported,70,0,true);input("stopped",stopped);String label="reset-"+manual+"-"+supported+"-"+stopped;state(label,call("reset"));Object first=pending();state(label+"-again",call("reset"));tick(first,label+"-stale");tick(pending(),label+"-current");state(label+"-constant",call("flash",true));tick(first,label+"-canceled-tick");
            }
            // A hardware failure must leave the existing task in place, before cancellation.
            for(int failure:new int[]{0,1,2})for(String command:new String[]{"reset","cancelTimeout","flash"}){
                reset(false,true,true,70,0,true);call("reset");Object first=pending();input("failure",failure);String label="held-task-"+failure+"-"+command;state(label,command.equals("flash")?call(command,true):call(command));row(label+"-same-task:"+(first==pending()));if(failure!=0&&first!=pending())throw new AssertionError("Failure lost pending task");input("failure",0);tick(first,label+"-old-run");
            }
            reset(false,true,true,70,0,true);state("sequence-reset",call("reset"));state("sequence-off",call("Off"));state("sequence-toggle",call("changeState"));tick(pending(),"sequence-tick");state("sequence-constant",call("flash",true));state("sequence-reset2",call("reset"));
        }
        missingDisplay();
        // Changing option 74 after class initialization must not alter the cached timeout.
        for(int next:new int[]{-1,0,1,10,Integer.MIN_VALUE,Integer.MAX_VALUE}){reset(false,nokia,true,70,0,true);option(74,next);state("cached-"+next,call("flash",false));if(f(light,"a","TIMEOUT",int.class).getInt(null)!=cached)throw new AssertionError("Timeout was re-read");}
    }
    public static void main(String[] args){try{
        ref=args[1].equals("reference");target=args[3];seed=Integer.parseInt(args[4]);Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet midlet=MIDletBridge.getCurrentMIDlet();loader=midlet.getClass().getClassLoader();out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));exercise();out.close();System.out.println("PASS light: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
