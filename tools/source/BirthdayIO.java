import java.util.Date;

/** Fix current Date and run the real worker at a deterministic scheduling boundary. */
public class BirthdayIO {
    public static StringBuffer log=new StringBuffer();
    public static int sleeps,starts,sleepFailure;
    public static void reset(){log.setLength(0);sleeps=starts=sleepFailure=0;}
    public static void sleep(long millis)throws InterruptedException{sleeps++;log.append("sleep:").append(millis).append(';');if(sleepFailure==1)throw new InterruptedException("scripted");if(sleepFailure==2)throw new IllegalArgumentException("scripted");}
    public static void start(Thread thread){starts++;log.append("start;");thread.run();}
    public static class Clock extends Date {public Clock(){super(LoginIO.now);}}
}
