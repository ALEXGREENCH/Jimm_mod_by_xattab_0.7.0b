/** The sole external clock boundary used when a new privacy roster ID is created. */
public class OtherStatusClock {
    public static long now;
    public static long time() { return now; }
}
