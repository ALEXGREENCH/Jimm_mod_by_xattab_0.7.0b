import javax.microedition.lcdui.Canvas;
import java.util.Vector;

/** Script the two device APIs only; no application classification or expected result. */
public class ExtendedKeysIO {
    public static String name;
    public static int game, nameFailure, gameFailure;
    public static Vector calls=new Vector();
    public static String keyName(Canvas receiver,int key) {
        calls.addElement("name:"+key+":"+(receiver!=null));
        if(nameFailure==1)throw new IllegalArgumentException("name");
        if(nameFailure==2)throw new IllegalStateException("name");
        if(nameFailure==3)throw new AssertionError("name");
        return name;
    }
    public static int gameAction(Canvas receiver,int key) {
        calls.addElement("game:"+key+":"+(receiver!=null));
        if(gameFailure==1)throw new IllegalArgumentException("game");
        if(gameFailure==2)throw new IllegalStateException("game");
        if(gameFailure==3)throw new AssertionError("game");
        return game;
    }
}
