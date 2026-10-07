import java.util.Vector;

/** Controlled terminal platformRequest result; URL parsing and list layout remain real. */
public class JimmUrlIO {
    public static Vector urls = new Vector();
    public static int mode;
    public static boolean request(Object receiver, String url) {
        if (receiver == null) throw new NullPointerException("receiver");
        urls.addElement(url);
        if (mode == 2) throw new IllegalArgumentException("fixture");
        if (mode == 3) throw new AssertionError("fixture");
        return mode == 1;
    }
}
