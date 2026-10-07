import java.util.Vector;
import javax.microedition.lcdui.Canvas;

/** Script only the device action lookup; application key and cursor methods remain real. */
public class RosterKeysIO {
    public static int game, failure;
    public static Vector calls = new Vector();
    public static int gameAction(Canvas canvas, int key) {
        calls.addElement("action:" + key + ":" + (canvas != null));
        if (failure == 1) throw new IllegalArgumentException("device action");
        if (failure == 2) throw new AssertionError("device action");
        return game;
    }
}
