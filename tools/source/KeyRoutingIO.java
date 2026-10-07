/** Records routing destinations; actual switch, conditions and exception propagation stay in VirtualList. */
public class KeyRoutingIO {
    public static StringBuffer log=new StringBuffer();
    public static boolean auto;
    public static int failure;
    public static void reset(boolean enabled,int fail){log.setLength(0);auto=enabled;failure=fail;}
    static void fail(int site){if(failure==site)throw new IllegalStateException("site-"+site);if(failure==site+10)throw new AssertionError("site-"+site);}
    public static void light(){log.append("light;");fail(1);}
    public static void flash(boolean state){log.append("flash:").append(state).append(';');fail(1);}
    public static boolean option(int key){log.append("option:").append(key).append(';');fail(2);return auto;}
    public static void status(){log.append("status;");fail(3);}
    public static void reaction(Object list,int key,int type){log.append("reaction:").append(key).append(',').append(type).append(';');fail(4);}
    public static void callback(int key,int type){log.append("callback:").append(key).append(',').append(type).append(';');fail(5);}
}
