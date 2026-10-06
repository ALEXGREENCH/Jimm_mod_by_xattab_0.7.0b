import java.util.Vector;

/** Deterministic connection boundary for the login-state probe. */
public class LoginIO {
    public static Vector packets=new Vector(),messages=new Vector();
    public static StringBuffer connections=new StringBuffer();
    public static int failures,closes,sleeps;
    public static boolean failSend;
    public static long now=1273665600000L;
    public static void reset(){packets.removeAllElements();messages.removeAllElements();connections.setLength(0);failures=closes=sleeps=0;failSend=false;now=1273665600000L;}
    public static boolean connect(String address){connections.append(address).append(';');return failures-->0;}
    public static void send(Object packet){packets.addElement(packet);}
    public static void message(Object message){messages.addElement(message);}
    public static long time(){return now;}
    public static void sleep(long delay){now+=delay;sleeps++;}
}
