import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.CRC32;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** State transitions, timer ownership, passwords and complete offscreen splash rasters. */
public class SplashProbe extends PopupProbe {
    static Class<?> splash, action, timer, icq, password;
    static Object screen;
    static ClassLoader loader;
    static Object invoke(String old, String restored, Class<?>[] types, Object... args) throws Exception {
        return method(splash,name(old,restored),void.class,types).invoke(screen,args);
    }
    static void call(String old, String restored) throws Exception { invoke(old,restored,new Class<?>[0]); }
    static Field sf(String old,String restored,Class<?> type) throws Exception { return field(splash,name(old,restored),type); }
    static void setInt(int key,int value) throws Exception { method(options,name("a","setInt"),void.class,int.class,int.class).invoke(null,key,value); }
    static void setPassword(String value) throws Exception { method(options,name("a","setString"),void.class,int.class,String.class).invoke(null,38,value); }
    static void state(String label) throws Exception {
        out.println(label+":locked="+sf("c","isLocked",boolean.class).getBoolean(null)
            +":unread="+sf("q","availableMessages",int.class).getInt(null)
            +":progress="+sf("p","progress",int.class).getInt(null)
            +":status="+sf("r","status_index",int.class).getInt(null)
            +":hint="+sf("b","showKeylock",boolean.class).getBoolean(null)
            +":held="+(sf("a","poundPressTime",long.class).getLong(null)!=0)
            +":splash="+(current()==screen)
            +":left="+(field(list,name("a","leftMenu"),Command.class).get(screen)!=null)
            +":right="+(field(list,name("b","rightMenu"),Command.class).get(screen)!=null)
            +":task="+(sf("a","actionTimer",timer).get(null)!=null)
            +":action="+(sf("a","currentAction",action).get(null)!=null)); cases++;
    }
    static void render(String label) throws Exception {
        Canvas canvas=(Canvas)Display.getDisplay(MIDletBridge.getCurrentMIDlet()).getCurrent();
        int width=canvas.getWidth(),height=canvas.getHeight();
        Image image=Image.createImage(width,height); Graphics g=image.getGraphics();
        method(splash,name("a","paint"),void.class,Graphics.class).invoke(screen,g);
        int[] rgb=new int[width*height];image.getRGB(rgb,0,width,0,0,width,height);
        CRC32 crc=new CRC32();for(int pixel:rgb){crc.update(pixel>>>16);crc.update(pixel>>>8);crc.update(pixel);}
        out.println(label+":"+width+"x"+height+":"+crc.getValue());cases++;
    }
    static void lock() throws Exception { call("n","lockScreen"); }
    static void key(String old,String restored,int code) throws Exception { invoke(old,restored,new Class<?>[]{int.class},code); }
    static void exercise() throws Exception {
        out.println("base:"+list.isAssignableFrom(splash));cases++;
        setPassword("");
        invoke("a","setStatusToDraw",new Class<?>[]{int.class},-1);
        lock();state("locked");call("o","messageAvailable");call("o","messageAvailable");state("two-messages");
        key("l","setProgress",43);lock();state("relock-preserves-state");
        invoke("d","unlock",new Class<?>[]{boolean.class},false);Object menu=current();state("unlock-menu");
        call("o","messageAvailable");state("message-unlocked");
        lock();invoke("d","unlock",new Class<?>[]{boolean.class},true);Object contacts=current();state("unlock-contacts");
        out.println("destinations-differ:"+(menu!=contacts));cases++;
        for(int code:new int[]{35,42,48}) {
            lock();key("f","keyPressed",code);state("pressed-"+code);
            sf("a","poundPressTime",long.class).setLong(null,System.currentTimeMillis()-1000);
            key("h","keyReleased",code);state("released-"+code);
            out.println("offline-menu-"+code+":"+(current()==menu));cases++;
        }
        lock();sf("a","poundPressTime",long.class).setLong(null,System.currentTimeMillis()+10000);
        key("g","keyRepeated",35);state("held-too-short");
        sf("a","poundPressTime",long.class).setLong(null,System.currentTimeMillis()-1000);
        key("g","keyRepeated",42);state("held-star");
        lock();invoke("b","pointerPressed",new Class<?>[]{int.class,int.class},5,0);state("tap-top");
        invoke("b","pointerPressed",new Class<?>[]{int.class,int.class},5,10000);state("tap-bottom");

        // An offline password cancellation must return to the lock, not exit the MIDlet.
        setPassword("secret");lock();invoke("b","pointerPressed",new Class<?>[]{int.class,int.class},5,10000);
        Object controller=field(password,name("a","instance"),password).get(null);
        TextBox textbox=(TextBox)field(password,name("a","passwordTextField"),TextBox.class).get(controller);
        out.println("password-shown:"+(current()==textbox));cases++;
        commandAction(controller,command("b","cmdCancel"));state("password-cancel");
        invoke("b","pointerPressed",new Class<?>[]{int.class,int.class},5,10000);
        textbox.setString("wrong");commandAction(controller,command("a","cmdOk"));
        out.println("wrong-password-stays:"+(current()==textbox));cases++;
        textbox.setString("secret");commandAction(controller,command("a","cmdOk"));state("password-unlock");
        setPassword("");

        Class<?> inert=Class.forName("RecoveryAction",true,loader);
        for(boolean cancelled:new boolean[]{false,true}) for(boolean complete:new boolean[]{false,true}) for(boolean failed:new boolean[]{false,true}) {
            Object value=inert.getConstructor().newInstance();
            inert.getField("completed").setBoolean(value,complete);inert.getField("error").setBoolean(value,failed);
            Constructor<?> constructor=timer.getDeclaredConstructor(action);constructor.setAccessible(true);
            TimerTask tick=(TimerTask)constructor.newInstance(value);
            if(cancelled)tick.cancel();tick.run();
            int firstEvent=inert.getField("events").getInt(value);tick.run();
            out.println("action-tick-"+cancelled+"-"+complete+"-"+failed+":"+firstEvent+":"+inert.getField("events").getInt(value)
                +":cancelled="+field(timer,name("b","canceled"),boolean.class).getBoolean(tick)
                +":error="+field(timer,name("a","wasError"),boolean.class).getBoolean(tick));cases++;
        }
        Object first=inert.getConstructor().newInstance(),second=inert.getConstructor().newInstance();
        Command cancel=(Command)sf("c","cancelCommnad",Command.class).get(null);
        lock();invoke("a","addTimerTask",new Class<?>[]{String.class,action,boolean.class},name("s3","keylock_enabled"),first,true);
        state("cancellable-while-locked");
        out.println("listener-owned:"+(field(list,name("a","commandListener"),CommandListener.class).get(screen)==screen));cases++;
        TimerTask old=(TimerTask)sf("a","actionTimer",timer).get(null);
        invoke("a","addTimerTask",new Class<?>[]{String.class,action,boolean.class},name("s3","keylock_enabled"),second,false);
        out.println("old-task-already-cancelled:"+field(timer,name("b","canceled"),boolean.class).getBoolean(old));cases++;
        state("replacement-not-cancellable");
        commandAction(screen,new Command("Other",Command.OK,1));state("unrelated-command");
        commandAction(screen,cancel);state("cancel-current-action");
        commandAction(screen,cancel);
        out.println("events-first:"+inert.getField("events").getInt(first)+":second:"+inert.getField("events").getInt(second));cases++;
        invoke("a","addTimerTask",new Class<?>[]{String.class,action,boolean.class},name("s3","keylock_enabled"),first,true);
        // Nokia/SE right softkey must reach SplashCanvas.commandAction while locked.
        key("f","keyPressed",-7);state("softkey-cancel");
        out.println("softkey-events:"+inert.getField("events").getInt(first));cases++;
        for(boolean swap:new boolean[]{false,true}) {
            method(options,name("a","setBoolean"),void.class,int.class,boolean.class).invoke(null,143,swap);
            method(list,name("d","assignSoftKeys"),void.class).invoke(null);
            lock();
            invoke("a","addTimerTask",new Class<?>[]{String.class,action,boolean.class},name("s3","keylock_enabled"),first,true);
            state("swapped-softkeys-"+swap);
            key("f","keyPressed",swap?-6:-7);
            state("swapped-softkey-cancel-"+swap);
            out.println("swapped-softkey-events-"+swap+":"+inert.getField("events").getInt(first));cases++;
        }

        call("a","show");sf("b","showKeylock",boolean.class).setBoolean(null,false);
        invoke("a","setMessage",new Class<?>[]{String.class},"Connecting");
        for(int shadow:new int[]{0,1,2}) for(int bg:new int[]{0,0xFFFFFF,0x345678}) {
            setInt(118,shadow);setInt(115,bg);
            method(list,name("d","assignSoftKeys"),void.class).invoke(null);
            for(int progress:new int[]{-1,0,1,25,50,100,101}) {
                key("l","setProgress",progress);render("raster-"+shadow+"-"+bg+"-"+progress);
            }
        }
        setInt(118,0);method(list,name("d","assignSoftKeys"),void.class).invoke(null);key("l","setProgress",0);
        for(int status:new int[]{-1,0,8,14}) {
            invoke("a","setStatusToDraw",new Class<?>[]{int.class},status);
            render("status-raster-"+status);
        }
        Class<?> xstatus=Class.forName(name("bj","jimm.comm.XStatus"),true,loader);
        Class<?> icon=Class.forName(name("e","DrawControls.Icon"),true,loader);
        for(int status:new int[]{0,10,37}) {
            Object value=method(xstatus,name("a","getStatusImage"),icon,int.class).invoke(null,status);
            invoke("f","setXStatusToDraw",new Class<?>[]{icon},value);
            render("xstatus-raster-"+status);
        }
        sf("b","showKeylock",boolean.class).setBoolean(null,true);render("keylock-hint-raster");
        for(int style:new int[]{0,1,2}) {
            setInt(112,style);method(options,name("b","updateFontStyle"),void.class).invoke(null);
            render("keylock-font-style-"+style);
        }
    }
    public static void main(String[] args) {
        try { run(args); System.exit(0); } catch(Throwable failure) { failure.printStackTrace();System.exit(1); }
    }
    static void run(String[] args) throws Exception {
        reference=args[1].equals("reference");
        Headless headless=new Headless(); Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emulator=(Common)ef.get(headless);ArrayList<String> arguments=new ArrayList<String>();
        Collections.addAll(arguments,"--rms","memory",args[0]);
        emulator.initParams(arguments,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);
        emulator.initMIDlet(true);MIDlet midlet=MIDletBridge.getCurrentMIDlet();loader=midlet.getClass().getClassLoader();
        ui=Class.forName(name("cf","jimm.JimmUI"),true,loader);
        list=Class.forName(name("cd","DrawControls.VirtualList"),true,loader);
        options=Class.forName(name("cj","jimm.Options"),true,loader);
        splash=Class.forName(name("cv","jimm.SplashCanvas"),true,loader);
        action=Class.forName(name("aa","jimm.comm.Action"),true,loader);
        timer=Class.forName(name("at","jimm.TimerTasks"),true,loader);
        password=Class.forName(name("by","jimm.EnterPassword"),true,loader);
        screen=sf("a","_this",splash).get(null);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        final Throwable[] error=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){error[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Splash UI timed out");
        out.close();if(error[0]!=null)throw new AssertionError("Splash UI failed",error[0]);
        System.out.println("PASS probe: "+cases+" splash transition/raster observations");
    }
}
