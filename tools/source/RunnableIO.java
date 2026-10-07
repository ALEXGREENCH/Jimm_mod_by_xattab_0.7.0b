import java.util.*;
import javax.microedition.lcdui.Display;

/** Observe event delivery and script scheduling/sleep failures without replacing RunnableImpl. */
public class RunnableIO {
    public static Vector events=new Vector(),queued=new Vector();
    public static int queueFailure,sleepFailure,forcedSpam=-1;
    public static void reset(){events.removeAllElements();queued.removeAllElements();queueFailure=sleepFailure=0;forcedSpam=-1;}
    public static void record(String label,Object[] args){events.addElement(new Object[]{label,args});}
    public static void enqueue(Display display,Runnable task){
        if(display==null)throw new NullPointerException();record("queue",new Object[]{task});
        if(queueFailure!=0)throw new IllegalStateException("scripted queue failure");queued.addElement(task);
    }
    public static void collect(){record("gc",new Object[0]);}
    public static void sleep(long delay)throws InterruptedException{
        record("sleep",new Object[]{new Long(delay)});if(delay<0||sleepFailure==2)throw new IllegalArgumentException("scripted sleep");if(sleepFailure==1)throw new InterruptedException("scripted sleep");
    }
    public static boolean spam(Object message){record("spam",new Object[]{message});return forcedSpam==1;}
}
