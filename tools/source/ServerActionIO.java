import java.util.*;

/** Contact-list notification boundary; the tested action and contact IDs remain real. */
public class ServerActionIO {
    public static Vector changes=new Vector();
    public static void changed(Object contact,boolean resort,boolean status){changes.addElement(new Object[]{contact,Boolean.valueOf(resort),Boolean.valueOf(status)});}
    public static class Clock extends Date {public Clock(){super(LoginIO.now);}}
}
