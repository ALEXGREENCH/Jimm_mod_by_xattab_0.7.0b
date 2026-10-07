import java.util.Vector;
import javax.microedition.lcdui.*;

/** Device boundaries; editor commands, listener and constraints delegate to the actual API. */
public class EditorIO {
    public static Vector events=new Vector();
    public static Displayable shown;
    public static String failLabel="";
    public static Throwable failure;
    static void event(String label,Object[] args)throws Throwable {
        events.addElement(new Object[]{label,args});
        if(label.equals(failLabel)&&failure!=null)throw failure;
    }
    public static void add(Displayable box,Command cmd)throws Throwable{event("add",new Object[]{cmd});box.addCommand(cmd);}
    public static void listener(Displayable box,CommandListener listener)throws Throwable{event("listener",new Object[]{listener});box.setCommandListener(listener);}
    public static void constraints(TextBox box,int mode)throws Throwable{event("constraints",new Object[]{Integer.valueOf(mode)});box.setConstraints(mode);}
    public static void show(Display display,Displayable next)throws Throwable{event("display",new Object[]{next});shown=next;}
    public static void gc()throws Throwable{event("gc",new Object[0]);}
    public static void light(boolean value)throws Throwable{event("light",new Object[]{Boolean.valueOf(value)});}
    public static void typing(String uin,boolean value)throws Throwable{event("typing",new Object[]{uin,Boolean.valueOf(value)});}
}
