import java.util.*;

/** Tick-by-tick May contact blinking, cancellation, restart, option changes and font/color state. */
public class BlinkProbe extends FileTransferProbe {
    static Class<?> clock;
    static Object c;
    static void prepare()throws Exception{m(contact,"c","prepareToBlink",void.class).invoke(c);}
    static void start(boolean online)throws Exception{m(contact,"b","startBlinking",void.class,boolean.class).invoke(c,online);}
    static TimerTask task()throws Exception{return (TimerTask)f(contact,"a","BlinkTimer",TimerTask.class).get(c);}
    static void settings(int mask,int duration)throws Exception{bool(157,(mask&1)!=0);bool(156,(mask&2)!=0);bool(184,(mask&4)!=0);integer(93,duration);integer(89,duration);}
    static void fresh()throws Exception{
        clock.getMethod("reset").invoke(null);actions().clear();c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);
    }
    static void snapshot(String label)throws Exception{
        String s=label;for(String[] names:new String[][]{{"f","blinkingOnline"},{"g","blinkingOffline"},{"h","mustStayVisible"},{"i","blinkOnConnect"}})s+=":"+f(contact,names[0],names[1],boolean.class).getBoolean(c);
        s+=":count="+f(contact,"r","blinkingNumber",int.class).getInt(c)+":limit="+f(contact,"s","blinkLimit",int.class).getInt(c)+":task="+(task()!=null);
        s+=":font="+m(contact,"b","getFontStyle",int.class).invoke(c)+":color="+m(contact,"a","getTextColor",int.class).invoke(c)+":image="+m(contact,"d","getImageIndex",int.class).invoke(c)+":visible="+m(contact,"a","mustBeShownAnyWay",boolean.class).invoke(c);
        row(s+":schedule="+clock.getField("log").get(null));String ev="";for(Object item:actions()){
            Object[] e=(Object[])item;ev+=e[0];if(e[0].equals("blink-contact")){Object[] args=(Object[])e[1];ev+=":"+(args[0]==c)+":"+args[1]+":"+args[2];}ev+=';';
        }row(label+":events="+ev);actions().clear();
    }
    static void exerciseBlink()throws Exception{
        setupFiles();clock=load("BlinkIO","BlinkIO");
        for(int style:new int[]{0,2})for(int mask=0;mask<8;mask++)for(boolean online:new boolean[]{false,true})for(int duration:new int[]{-1,0,1,3}){
            fresh();settings(mask,duration);integer(112,style);f(options,"g","fontStyle",int.class).setInt(null,style);prepare();start(online);String label="blink-"+style+"-"+mask+"-"+online+"-"+duration;snapshot(label);
            TimerTask timer=task();if(timer!=null){for(int tick=0;tick<9;tick++){timer.run();snapshot(label+"-tick-"+tick);}}
        }
        for(boolean first:new boolean[]{false,true})for(boolean next:new boolean[]{false,true})for(int mask:new int[]{0,7}){
            fresh();settings(7,3);prepare();start(first);TimerTask old=task();old.run();snapshot("restart-first");prepare();snapshot("restart-prepare");settings(mask,1);start(next);snapshot("restart-"+first+"-"+next+"-"+mask);old.run();snapshot("restart-old-tick");task().run();snapshot("restart-current-tick");
        }
        for(boolean online:new boolean[]{false,true}){
            fresh();settings(7,1);prepare();start(online);settings(7,100);for(int tick=0;tick<5;tick++){task().run();snapshot("duration-change-"+online+"-"+tick);}
            prepare();settings(7,0);start(online);f(contact,"h","mustStayVisible",boolean.class).setBoolean(c,false);task().run();task().run();snapshot("already-hidden-"+online);
        }
        for(boolean chat:new boolean[]{false,true})for(int style:new int[]{0,1,2,3}){
            fresh();settings(0,0);integer(112,style);f(options,"g","fontStyle",int.class).setInt(null,style==2||style==3?2:0);m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,16,chat);snapshot("font-"+chat+"-"+style);
        }
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseBlink();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
