import java.util.Vector;

/** Terminal queue/chat/history/diagnostic/device boundaries for the real sender. */
public class SendTextIO {
    public static Vector events=new Vector();
    public static String failureLabel="";
    public static int failureOrdinal=1, hits;
    public static Throwable failure;
    public static Throwable requestFailure;
    public static int requestFailureOrdinal=1, requestHits;
    static void record(String label,Object[] args)throws Throwable {
        events.addElement(new Object[]{label,args});
        if(label.equals(failureLabel) && ++hits==failureOrdinal && failure!=null)throw failure;
    }
    public static void request(Object action)throws Throwable{
        record("request",new Object[]{action});
        if(++requestHits==requestFailureOrdinal && requestFailure!=null)throw requestFailure;
    }
    public static void error(Object error)throws Throwable{record("error",new Object[]{error});}
    public static void chat(Object receiver,String text,long date,String name,long id)throws Throwable{record("chat",new Object[]{receiver,text,Long.valueOf(date),name,Long.valueOf(id)});}
    public static void history(String uin,String text,byte type,String name,long date)throws Throwable{record("history",new Object[]{uin,text,Byte.valueOf(type),name,Long.valueOf(date)});}
    public static void sleep(long delay)throws Throwable{record("sleep",new Object[]{Long.valueOf(delay)});}
    public static void light(boolean value)throws Throwable{record("light",new Object[]{Boolean.valueOf(value)});}
}
