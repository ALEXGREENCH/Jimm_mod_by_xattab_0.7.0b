/** Deterministic clock/screen/scheduler boundary for the real animation loop. */
public class GraphicsIO {
    public static Object screen;
    public static Runnable observer;
    public static long[] times;
    public static int ticks,limit,repaints,starts;
    public static class Stop extends Error { }
    public static long time(){return times[ticks];}
    public static Object screen(){return screen;}
    public static void start(Thread thread){starts++;}
    public static void repaint(Object list){repaints++;}
    public static void sleep(long duration){if(observer!=null)observer.run();if(++ticks>limit)throw new Stop();}
}
