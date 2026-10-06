import java.util.*;
import javax.microedition.lcdui.Displayable;

/** Record scheduling without a running timer thread; probes deliver ticks explicitly. */
public class TimerIO {
    public static Vector timers=new Vector(),tasks=new Vector(),log=new Vector();
    public static boolean shown,failStatus;
    public static void reset(){timers.removeAllElements();tasks.removeAllElements();log.removeAllElements();shown=failStatus=false;}
    public static boolean isShown(Displayable screen){return shown;}
    public static void markCancel(TimerTask task){log.addElement("task-cancel:"+tasks.indexOf(task));}
    public static class Clock extends java.util.Timer {
        public boolean stopped;
        final int id;
        public Clock(){super();super.cancel();id=timers.size();timers.addElement(this);log.addElement("new:"+id);}
        public void schedule(TimerTask task,long delay,long period){
            log.addElement("schedule:"+id+":"+delay+":"+period);
            if(delay<0||period<=0)throw new IllegalArgumentException();
            if(stopped)throw new IllegalStateException();
            tasks.addElement(task);
        }
        public void cancel(){stopped=true;log.addElement("timer-cancel:"+id);}
    }
}
