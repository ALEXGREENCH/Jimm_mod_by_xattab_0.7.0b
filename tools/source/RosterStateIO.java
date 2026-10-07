import java.util.Vector;
import javax.microedition.lcdui.*;
import java.lang.reflect.*;

/** External display/repaint inputs; application selection, activation and RMS remain genuine. */
public class RosterStateIO {
    public static Vector calls=new Vector();
    public static Class monitored;
    public static boolean shown=true;
    public static int failureAt,failure,seen;
    static void call(String name,boolean receiver) {
        calls.addElement(name+":"+receiver+":"+Thread.holdsLock(monitored));seen++;
        if(seen==failureAt){if(failure==1)throw new IllegalStateException("display");if(failure==2)throw new AssertionError("display");}
    }
    public static boolean isShown(Displayable screen){call("shown",screen!=null);return shown;}
    public static void repaint(Canvas canvas){call("repaint",canvas!=null);}
    public static void show(Display display,Displayable screen){call("show",display!=null);if(display==null)throw new NullPointerException("display");}
    public static void enqueue(Display display,Runnable runnable){
        call("queue",display!=null);
        try{int type=0;Object[] data=null;for(Field f:runnable.getClass().getDeclaredFields())if(!Modifier.isStatic(f.getModifiers())){f.setAccessible(true);if(f.getType()==int.class)type=f.getInt(runnable);if(f.getType()==Object[].class)data=(Object[])f.get(runnable);}
            StringBuffer text=new StringBuffer("task:").append(type).append(':');if(data!=null)for(Object v:data){if(v==null)text.append("null");else if(v instanceof String){String s=(String)v;for(int i=0;i<s.length();i++)text.append(Integer.toHexString(s.charAt(i))).append(',');}else throw new AssertionError("Unexpected error task payload");text.append(';');}calls.addElement(text.toString());
        }catch(IllegalAccessException e){throw new AssertionError(e);}
    }
}
