import java.util.*;

/** Deterministic scheduler boundary: probes explicitly deliver each timer tick. */
public class BlinkIO {
    public static Vector tasks=new Vector(),log=new Vector();
    public static void reset(){tasks.removeAllElements();log.removeAllElements();}
    public static void schedule(Timer timer,TimerTask task,long delay,long period){tasks.addElement(task);log.addElement("schedule:"+tasks.indexOf(task)+":"+delay+":"+period);}
    public static boolean cancel(TimerTask task){log.addElement("cancel:"+tasks.indexOf(task));return true;}
}
