import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/** Capture the native editor handed to Display without asynchronous screen switching. */
public class OptionsIO {
    public static Displayable screen;
    public static void show(Display display, Displayable next) { screen = next; }
}
