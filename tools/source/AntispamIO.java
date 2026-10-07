import java.util.*;

/** Explicit external clock and input queue; no application algorithm is copied. */
public class AntispamIO {
    public static long now=1268179200123L;
    public static int clockReads,dateReads,queueFault,dateFault;
    public static Object monitor;
    public static String queueTrace="";
    public static long time(){clockReads++;return now;}
    public static class Clock extends Date {
        public Clock(){super(now);dateReads++;if(dateFault==1)throw new IllegalStateException("clock");if(dateFault==2)throw new AssertionError("clock");}
    }
    public static class Queue extends Vector {
        public synchronized void addElement(Object value){
            queueTrace+=Thread.holdsLock(monitor)+";";
            if(queueFault==1)throw new IllegalStateException("queue");
            if(queueFault==2)throw new AssertionError("queue");
            super.addElement(value);
        }
    }
}
