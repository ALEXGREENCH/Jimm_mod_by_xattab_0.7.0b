import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Actual timer ticks, status restoration, disabled timers, keepalive and caption lifecycles. */
public class TimerProbe extends FileTransferProbe {
    static Class<?> timer,clock,splash,main,icons,icon;
    static Object[] initial;
    static void status(int value)throws Exception{m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)value);}
    static long status()throws Exception{return (Long)m(options,"a$134622","getLong",long.class,ref?new Class<?>[0]:new Class<?>[]{int.class}).invoke(null,ref?new Object[0]:new Object[]{192});}
    static void fresh(boolean connected,int state)throws Exception{
        f(options,"a","options",Object[].class).set(null,initial.clone());status(state);bool(147,false);bool(153,false);bool(161,false);f(icq,"b","connected",boolean.class).setBoolean(null,connected);f(main,"a","haveToRestoreStatus",boolean.class).setBoolean(null,false);f(timer,"b","oldStatus",int.class).setInt(null,32);f(timer,"a","delay",int.class).setInt(null,120000);clock.getMethod("reset").invoke(null);f(timer,"a","timer",Timer.class).set(null,null);m(timer,"a","setStatusTimer",void.class).invoke(null);resetEnv();((Vector)clock.getField("log").get(null)).clear();f(splash,"r","status_index",int.class).setInt(null,999);f(vl,"a","capImage",icon).set(f(list,"a","tree",tree).get(null),null);
    }
    static TimerTask task(int type)throws Exception{return (TimerTask)instance(timer,new Class<?>[]{int.class},type);}
    static void snapshot(String label,TimerTask task)throws Exception{
        Object treeObject=f(list,"a","tree",tree).get(null),cap=f(vl,"a","capImage",icon).get(treeObject);Object imageList=f(list,"a","imageList",icons).get(null);int image=-1;for(int i=0;cap!=null&&i<35;i++){if(cap==m(icons,"a","elementAt",icon,int.class).invoke(imageList,i)){image=i;break;}}
        row(label+":status="+status()+":old="+f(timer,"b","oldStatus",int.class).getInt(null)+":restore="+f(main,"a","haveToRestoreStatus",boolean.class).getBoolean(null)+":splash="+f(splash,"r","status_index",int.class).getInt(null)+":tree="+image+":saves="+env.getField("saves").getInt(null)+":gc="+env.getField("collections").getInt(null)+":task="+(task==null?"null":m(timer,"a","getType",int.class).invoke(task)+"/"+m(timer,"a","isCanceled",boolean.class).invoke(task)+"/"+f(timer,"a","wasError",boolean.class).getBoolean(task)));
        row(label+":scheduler="+clock.getField("log").get(null));((Vector)clock.getField("log").get(null)).clear();
        int i=0;for(Object item:actions()){Object[] e=(Object[])item;String value=e[0].equals("file-error")?f(error,"a","_ErrCode",int.class).getInt(((Object[])e[1])[0])+"":enc(Arrays.toString((Object[])e[1]));row(label+":event-"+i+++"="+e[0]+":"+value);}actions().clear();
    }
    static void automatic()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(boolean auto:new boolean[]{false,true})for(boolean keylock:new boolean[]{false,true})for(boolean restore:new boolean[]{false,true})for(int state:new int[]{-1,0,1,2,4,16,32,8193,12288,16384,20480,24576,256,512,12345}){
            fresh(connected,state);bool(153,auto);bool(161,keylock);f(main,"a","haveToRestoreStatus",boolean.class).setBoolean(null,restore);TimerTask task=task(200);String label="auto-"+connected+"-"+auto+"-"+keylock+"-"+restore+"-"+state;
            for(int tick=0;tick<3;tick++){row(label+":tick-"+tick+"="+invoke(m(timer,"run","run",void.class),task));snapshot(label+"-"+tick,task);}
        }
        fresh(true,0);bool(153,true);bool(161,true);clock.getField("failStatus").setBoolean(null,true);TimerTask task=task(200);row("status-error:tick="+invoke(m(timer,"run","run",void.class),task));snapshot("status-error",task);
        fresh(true,2);bool(153,true);bool(161,true);task=task(200);row("cancelled-auto:cancel="+task.cancel());row("cancelled-auto:tick="+invoke(m(timer,"run","run",void.class),task));snapshot("cancelled-auto",task);
        fresh(false,0);f(timer,"a","timer",Timer.class).set(null,null);row("disabled-null:tick="+invoke(m(timer,"run","run",void.class),task(200)));
    }
    static void restoration()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(boolean enabled:new boolean[]{false,true})for(boolean remembered:new boolean[]{false,true})for(int state:new int[]{-1,0,1,2,4,16,32,256})for(int previous:new int[]{0,32,8193}){
            fresh(connected,state);bool(147,enabled);f(main,"a","haveToRestoreStatus",boolean.class).setBoolean(null,remembered);f(timer,"b","oldStatus",int.class).setInt(null,previous);String label="restore-"+connected+"-"+enabled+"-"+remembered+"-"+state+"-"+previous;row(label+":setup="+invoke(m(timer,"a","setStatusTimer",void.class),null));snapshot(label,null);
        }
        for(int delay:new int[]{-2147483648,-1,0,1,1000,300000,2147483647}){
            fresh(false,0);f(timer,"a","delay",int.class).setInt(null,delay);row("delay-"+delay+":setup="+invoke(m(timer,"a","setStatusTimer",void.class),null));snapshot("delay-"+delay,null);
        }
    }
    static void keepalive()throws Exception{
        Class<?> util=load("co","jimm.comm.Util");long today=(Long)m(util,"a","createCurrentDate",long.class,boolean.class,boolean.class).invoke(null,false,true);
        for(boolean connected:new boolean[]{false,true})for(boolean enabled:new boolean[]{false,true})for(boolean day:new boolean[]{false,true})for(boolean error:new boolean[]{false,true}){
            fresh(connected,0);bool(128,enabled);f(timer,"a","currData",long.class).setLong(null,today-(day?86400:0));io.getField("failSend").setBoolean(null,error);TimerTask task=task(100);row("keep-"+connected+"-"+enabled+"-"+day+"-"+error+":tick="+invoke(m(timer,"run","run",void.class),task));Vector packets=(Vector)io.getField("packets").get(null);row("keep:packets="+packets.size());for(Object p:packets)row("keep:bytes="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));snapshot("keep",task);
        }
    }
    static void flash()throws Exception{
        Object first=instance(text,new Class<?>[]{String.class},"Old"),second=instance(text,new Class<?>[]{String.class},"Other");
        for(int type:new int[]{4,5})for(int count:new int[]{-1,0,1,2,3,8})for(String value:new String[]{"","abc","abcdefghi"})for(int mode:new int[]{0,1,2,3}){
            fresh(false,0);Object screen=mode<2?first:new Form("Old");if(mode<2)m(vl,"b","activate",void.class,Display.class).invoke(mode==0?first:second,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));clock.getField("shown").setBoolean(null,mode==2);m(ui,"a","setCaption",void.class,Object.class,String.class).invoke(null,screen,"Old");TimerTask task=(TimerTask)instance(timer,new Class<?>[]{Object.class,String.class,int.class,int.class},screen,value,count,type);String label="flash-"+type+"-"+count+"-"+value.length()+"-"+mode;
            for(int tick=0;tick<10;tick++){row(label+":tick-"+tick+"="+invoke(m(timer,"run","run",void.class),task));row(label+":caption-"+tick+"="+enc(m(ui,"a","getCaption",String.class,Object.class).invoke(null,screen))+":counter="+f(timer,"d","flashCounter",int.class).getInt(task)+":owner="+(f(timer,"a","flashDispl",Object.class).get(task)!=null));snapshot(label+"-"+tick,task);}
        }
    }
    static void captions()throws Exception{
        for(boolean canceled:new boolean[]{false,true})for(int type:new int[]{4,5}){
            fresh(false,0);Object screen=instance(text,new Class<?>[]{String.class},"Before");m(vl,"b","activate",void.class,Display.class).invoke(screen,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));TimerTask task=(TimerTask)instance(timer,new Class<?>[]{Object.class,String.class,int.class,int.class},screen,"Aftercaption",2,type);if(canceled)task.cancel();String label="caption-"+type+"-"+canceled;row(label+":tick="+invoke(m(timer,"run","run",void.class),task));row(label+":changed="+enc(m(ui,"a","getCaption",String.class,Object.class).invoke(null,screen)));m(timer,"b","flashRestoreOldCaption",void.class).invoke(task);row(label+":restored="+enc(m(ui,"a","getCaption",String.class,Object.class).invoke(null,screen)));snapshot(label,task);
            m(vl,"b","activate",void.class,Display.class).invoke(instance(text,new Class<?>[]{String.class},"Hidden"),Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));row(label+":hidden="+invoke(m(timer,"run","run",void.class),task));snapshot(label+"-hidden",task);
        }
        for(int type:new int[]{4,5})for(int count:new int[]{0,2}){fresh(false,0);TimerTask task=(TimerTask)instance(timer,new Class<?>[]{Object.class,String.class,int.class,int.class},null,null,count,type);row("null-caption-"+type+"-"+count+":"+invoke(m(timer,"run","run",void.class),task));snapshot("null-caption",task);}
    }
    static void special()throws Exception{
        for(int type:new int[]{1,2,3,0,10,99,-2}){fresh(true,32);f(splash,"b","showKeylock",boolean.class).setBoolean(null,true);TimerTask task=task(type);row("special-"+type+":tick="+invoke(m(timer,"run","run",void.class),task));row("special-"+type+":hint="+f(splash,"b","showKeylock",boolean.class).getBoolean(null));snapshot("special-"+type,task);}
        Class<?> inert=Class.forName("RecoveryAction",true,loader);for(boolean cancel:new boolean[]{false,true})for(boolean completed:new boolean[]{false,true})for(boolean failed:new boolean[]{false,true}){
            fresh(false,0);Object a=inert.getConstructor().newInstance();inert.getField("completed").setBoolean(a,completed);inert.getField("error").setBoolean(a,failed);TimerTask task=(TimerTask)instance(timer,new Class<?>[]{action},a);if(cancel)row("action:cancel="+task.cancel());for(int tick=0;tick<3;tick++){row("action-"+cancel+"-"+completed+"-"+failed+"-"+tick+":"+invoke(m(timer,"run","run",void.class),task)+":events="+inert.getField("events").getInt(a));snapshot("action",task);}
        }
    }
    static void exerciseTimer()throws Exception{
        setupFiles();timer=load("at","jimm.TimerTasks");clock=load("TimerIO","TimerIO");splash=load("cv","jimm.SplashCanvas");main=load("ag","jimm.MainMenu");icons=load("f","DrawControls.ImageList");icon=load("e","DrawControls.Icon");action=load("aa","jimm.comm.Action");initial=((Object[])f(options,"a","options",Object[].class).get(null)).clone();automatic();restoration();keepalive();flash();captions();special();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTimer();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
