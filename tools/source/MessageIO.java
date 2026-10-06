import java.util.Vector;

/** Boundary observations, loaded in the MIDlet class loader. */
public class MessageIO {
    public static Vector events=new Vector();
    public static void record(String name,Object[] values){events.addElement(new Object[]{name,values});}
    public static void reset(){events.removeAllElements();}
}
