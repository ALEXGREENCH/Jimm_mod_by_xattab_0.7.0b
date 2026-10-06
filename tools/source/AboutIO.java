import java.util.*;
import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;

/** About-screen environment. Uses TransportIO's in-memory HTTP endpoint. */
public class AboutIO extends TransportIO {
    public static Vector tasks=new Vector(), urls=new Vector();
    public static int saves,yields,priority,collections;
    public static void resetAbout(){reset();tasks.removeAllElements();urls.removeAllElements();saves=0;yields=0;priority=0;collections=0;}
    public static Connection open(String url)throws java.io.IOException{return TransportIO.open(url,3);}
    public static long freeMemory(Runtime runtime){return 655360L;}
    public static long totalMemory(Runtime runtime){return 1048576L;}
    public static void collect(){collections++;}
    public static void schedule(Timer timer,TimerTask task,long delay){tasks.addElement(new Object[]{task,new Long(delay)});}
    public static boolean platformRequest(MIDlet midlet,String url){urls.addElement(url);return false;}
    public static void yieldThread(){yields++;}
    public static void setPriority(Thread thread,int value){priority=value;}
    public static void saveOptions(){saves++;}
}
