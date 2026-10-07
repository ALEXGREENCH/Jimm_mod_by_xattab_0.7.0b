import java.util.*;

/** Profile delivery and contact notification boundaries; real action/rename/RMS code executes. */
public class RequestInfoIO {
    public static Vector shown=new Vector(),changes=new Vector(),renamed=new Vector();
    public static int showFailure;
    public static void reset(){shown.removeAllElements();changes.removeAllElements();renamed.removeAllElements();showFailure=0;}
    public static void show(int job,Object data){shown.addElement(new Object[]{Integer.valueOf(job),data});if(showFailure==1)throw new IllegalArgumentException("scripted");if(showFailure==2)throw new NullPointerException("scripted");}
    public static void changed(Object contact,boolean resort,boolean status){changes.addElement(new Object[]{contact,Boolean.valueOf(resort),Boolean.valueOf(status)});}
    public static void renamed(String uin,String name){renamed.addElement(new String[]{uin,name});}
    public static class Clock extends Date {public Clock(){super(LoginIO.now);}}
}
