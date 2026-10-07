import java.util.Vector;
import javax.microedition.lcdui.Display;

/** Terminal hotkey destinations and device calls; subject branches and option state remain real. */
public class HotKeyIO {
    public static Vector events=new Vector();
    public static String failLabel="";
    public static Throwable failure;
    public static long now;
    public static int clocks;
    public static boolean result;
    static void event(String label,Object[] args)throws Throwable {
        events.addElement(new Object[]{label,args});
        if(label.equals(failLabel)&&failure!=null)throw failure;
    }
    public static long time(){clocks++;return now;}
    public static void history(String uin,String name)throws Throwable{event("history",new Object[]{uin,name});}
    public static void info(String uin,String name)throws Throwable{event("info",new Object[]{uin,name});}
    public static void write(Object contact,String text)throws Throwable{event("write",new Object[]{contact,text});}
    public static void save()throws Throwable{event("save",new Object[0]);}
    public static void options()throws Throwable{event("options",new Object[0]);}
    public static void menu()throws Throwable{event("menu",new Object[0]);}
    public static void minimize(boolean value)throws Throwable{event("minimize",new Object[]{Boolean.valueOf(value)});}
    public static void client(Object contact)throws Throwable{event("client",new Object[]{contact});}
    public static void colors()throws Throwable{event("colors",new Object[0]);}
    public static void colorsArg(boolean unused)throws Throwable{colors();}
    public static void changed(boolean sort)throws Throwable{event("changed",new Object[]{Boolean.valueOf(sort)});}
    public static void changedTwo(boolean sort,boolean unused)throws Throwable{changed(sort);}
    public static void activate()throws Throwable{event("activate",new Object[0]);}
    public static boolean sound(boolean value)throws Throwable{event("sound",new Object[]{Boolean.valueOf(value)});return result;}
    public static void magic()throws Throwable{event("magic",new Object[0]);}
    public static void delete(String uin,int type)throws Throwable{event("delete",new Object[]{uin,Integer.valueOf(type)});}
    public static void xtraz(String uin,int id)throws Throwable{event("xtraz",new Object[]{uin,Integer.valueOf(id)});}
    public static void xtrazOnly(String uin)throws Throwable{xtraz(uin,0);}
    public static void phone()throws Throwable{event("phone",new Object[0]);}
    public static void lock()throws Throwable{event("lock",new Object[0]);}
    public static boolean platform(Object owner,String url)throws Throwable{event("platform",new Object[]{owner,url});return result;}
    public static void oldFullscreen(boolean value)throws Throwable{event("old-fullscreen",new Object[]{Boolean.valueOf(value)});}
    public static void lights(int group,int value)throws Throwable{event("lights",new Object[]{Integer.valueOf(group),Integer.valueOf(value)});}
    public static boolean flash(Display display,int duration)throws Throwable{event("flash",new Object[]{display,Integer.valueOf(duration)});return result;}
}
