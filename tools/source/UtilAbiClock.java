/** External clock provider for the date constructor only; never included in delivered JARs. */
public final class UtilAbiClock {
    public static long epoch;
    public static int reads;
    public static final class Clock extends java.util.Date {
        public Clock() { super(read()); }
    }
    public static long read() { reads++; return epoch; }
}
