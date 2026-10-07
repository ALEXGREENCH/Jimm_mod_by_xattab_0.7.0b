import java.util.Vector;
import javax.microedition.lcdui.Display;

/** Device alpha-level inputs only; application state and classification stay in genuine methods. */
public final class OptionsSettingsIO {
    public static int levels,failure;
    public static Vector calls=new Vector();
    public static int alphaLevels(Display display) {
        if(display==null)throw new NullPointerException();
        calls.addElement("levels");
        if(failure==1)throw new IllegalStateException("device input");
        if(failure==2)throw new Error("device input");
        return levels;
    }
}
