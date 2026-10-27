public class OnlineStatusInitializationEvents {
    public static int count;
    public static boolean fail;
    public static void initialized() {
        count++;
        if (fail) throw new IllegalStateException("initializer probe");
    }
}
