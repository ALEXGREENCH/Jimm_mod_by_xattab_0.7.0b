import java.util.Date;

/** Action clock and completion notification only; serialization and action state stay real. */
public class SaveInfoIO {
    public static int notifications, failure;
    public static class FaultError extends Error {}
    public static void reset(){notifications=failure=0;}
    public static void activate(){notifications++;if(failure==1)throw new IllegalArgumentException("scripted");if(failure==2)throw new NullPointerException("scripted");if(failure==3)throw new FaultError();}
    public static class Clock extends Date {public Clock(){super(LoginIO.now);}}
}
