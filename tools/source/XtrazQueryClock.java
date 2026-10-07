/** Explicit external input to the real Xtraz transaction clock. */
public class XtrazQueryClock {
    public static long now;
    public static int reads;
    public static long time(){reads++;return now;}
}
