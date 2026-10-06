import java.util.*;
import java.lang.reflect.*;

/** Script hardware and delivery time; preserve real TimerTask scheduling/cancellation state. */
public class LightIO {
    public static boolean supported=true, result=true, stopped;
    public static int failure;
    public static StringBuffer log=new StringBuffer();
    public static Vector tasks=new Vector();
    public static Field on;
    private static final Clock clock=new Clock();
    public static void reset(){supported=true;result=true;stopped=false;failure=0;log.setLength(0);tasks.removeAllElements();}
    public static void observeInit(){try {log.append("init-on:").append(on.getBoolean(null)).append(';');}catch(Exception e){throw new AssertionError(e);}}
    public static boolean supported(){return supported;}
    private static void fail(){if(failure==1)throw new SecurityException("scripted");if(failure==2)throw new IllegalArgumentException("scripted");}
    public static void lights(int number,int level){log.append("lights:").append(number).append(',').append(level).append(';');fail();}
    public static boolean flash(javax.microedition.lcdui.Display display,int duration){if(display==null)throw new NullPointerException();log.append("flash:").append(duration).append(';');fail();return result;}
    public static Timer timer(){log.append("timer;");return clock;}
    public static boolean cancel(TimerTask task){boolean value=task.cancel();log.append("cancel:").append(value).append(';');return value;}
    public static class Clock extends Timer {
        public Clock(){super();super.cancel();}
        public void schedule(TimerTask task,long delay){
            log.append("schedule:").append(delay).append(';');
            if(delay<0)throw new IllegalArgumentException("Negative delay");
            if(stopped)throw new IllegalStateException("Scripted stopped timer");
            // Schedule on a real Timer far in the future, then stop delivery.
            // Timer.cancel() leaves a scheduled task's state intact; TimerTask.cancel() remains real.
            Timer marker=new Timer();try{marker.schedule(task,Long.MAX_VALUE/4);}finally{marker.cancel();}
            tasks.addElement(task);
        }
    }
}
