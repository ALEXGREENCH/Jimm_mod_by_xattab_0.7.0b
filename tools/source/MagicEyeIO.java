import java.util.Vector;

/** Terminal routing only; the journal, clipboard and contact lookup remain real. */
public class MagicEyeIO {
    public static Vector events=new Vector();
    public static int failure;
    public static class Clock extends java.util.Date { public Clock(){super(LoginIO.now);} }
    public static class FaultError extends Error {}
    public static void route(String name,Object value){events.addElement(new Object[]{name,value});if(failure==1)throw new IllegalArgumentException("scripted journal destination");if(failure==2)throw new FaultError();}
    public static void contacts(){route("contacts",null);}
    public static void back(){route("back",null);}
    public static void menu(Object contact){route("contact-menu",contact);}
    public static void reset(){events.removeAllElements();failure=0;}
}
