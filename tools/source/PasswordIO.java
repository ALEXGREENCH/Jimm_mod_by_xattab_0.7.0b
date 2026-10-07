import java.util.Vector;
import javax.microedition.midlet.MIDletStateChangeException;

/** Password screen destinations only; native TextBox and controller decisions remain real. */
public class PasswordIO {
    public static Vector events=new Vector();
    public static String failing="";
    public static int mode, ordinal=1, attempts;
    public static void reset(){events.removeAllElements();failing="";mode=0;ordinal=1;attempts=0;}
    static void record(String name,Object[] args){
        events.addElement(new Object[]{name,args});
        if(name.equals(failing)&&++attempts==ordinal){
            if(mode==2)throw new IllegalArgumentException("scripted destination failure");
            if(mode==3)throw new NullPointerException("scripted destination failure");
        }
    }
    public static void display(Object receiver,Object screen){if(receiver==null)throw new NullPointerException();record("display",new Object[]{receiver,screen});}
    public static void flash(boolean on){record("light",new Object[]{Boolean.valueOf(on)});}
    public static void activate(){record("activate",new Object[0]);}
    public static void before(){record("before",new Object[0]);}
    public static void connect(){record("connect",new Object[0]);}
    public static void unlock(boolean show){record("unlock",new Object[]{Boolean.valueOf(show)});}
    public static void destroy(Object receiver,boolean unconditional)throws MIDletStateChangeException{
        if(receiver==null)throw new NullPointerException();record("destroy",new Object[]{receiver,Boolean.valueOf(unconditional)});
        if(failing.equals("destroy")&&attempts==ordinal&&mode==1)throw new MIDletStateChangeException("scripted refusal");
    }
}
