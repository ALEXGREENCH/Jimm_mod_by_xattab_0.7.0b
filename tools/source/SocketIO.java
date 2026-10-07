import java.io.*;
import java.util.Vector;
import javax.microedition.io.*;

/** Scripted socket streams; protocol, locks and sequence assignment remain in Jimm. */
public class SocketIO {
    public static Vector endpoints=new Vector();
    public static StringBuffer log=new StringBuffer();
    public static int cursor,openFailure,errors,starts,sleeps,yields,signals,sleepFailure;
    public static long slept;
    public static Object lastError;
    public static Runnable onSleep;
    public static void reset(){endpoints.removeAllElements();log.setLength(0);cursor=openFailure=errors=starts=sleeps=yields=signals=sleepFailure=0;slept=0;lastError=null;onSleep=null;}
    public static Endpoint add(byte[] body){Endpoint e=new Endpoint(body);endpoints.addElement(e);return e;}
    public static Connection open(String url,int mode)throws IOException{
        log.append("open:").append(url).append(':').append(mode).append(';');
        if(openFailure==1)throw new ConnectionNotFoundException("scripted");
        if(openFailure==2)throw new IllegalArgumentException("scripted");
        if(openFailure==3)throw new IOException("scripted");
        if(openFailure==4)return null;
        if(openFailure==5)return new Other();
        if(cursor>=endpoints.size())throw new AssertionError("unscripted socket open");
        return (Endpoint)endpoints.elementAt(cursor++);
    }
    public static void start(Thread thread){starts++;log.append("start;");}
    public static void sleep(long value)throws InterruptedException{
        sleeps++;slept+=value;
        if(onSleep!=null)onSleep.run();
        if(sleepFailure==1)throw new InterruptedException("scripted");
        if(sleepFailure==2)throw new IllegalArgumentException("scripted");
        if(sleeps>100)throw new AssertionError("unbounded available polling");
    }
    public static void yieldThread(){yields++;}
    public static void signal(Object lock){signals++;lock.notify();}
    public static void error(Object value){errors++;lastError=value;}
    public static class ScriptError extends Error {}
    public static class Other implements Connection {public void close(){}}
    public static class Endpoint implements SocketConnection {
        public byte[] body;
        public int position,chunk=65536,readCalls,availableCalls,zeroReads,zeroAvailable,zeroLengthMode;
        public int openInputFault,openOutputFault,readFault,readFailAfter=-1,availableFault,writeFault,flushFault;
        public int inputCloseFault,outputCloseFault,socketCloseFault,localFault;
        public int inputClosed,outputClosed,closed,flushes,localPort=4321;
        public String localAddress="192.0.2.9";
        public ByteArrayOutputStream sent=new ByteArrayOutputStream();
        public Runnable beforeRead;
        public Endpoint(byte[] b){body=b;}
        static void fault(int n)throws IOException{
            if(n==1)throw new IOException("scripted");
            if(n==2)throw new IllegalArgumentException("scripted");
            if(n==3)throw new NullPointerException("scripted");
            if(n==4)throw new ScriptError();
            if(n==5)throw new ConnectionNotFoundException("scripted");
        }
        public InputStream openInputStream()throws IOException{
            fault(openInputFault);if(openInputFault==6)return null;
            return new InputStream(){
                public int available()throws IOException{availableCalls++;fault(availableFault);if(zeroAvailable>0){zeroAvailable--;return 0;}return Math.max(1,body.length-position);}
                public int read()throws IOException{byte[] b=new byte[1];int n=read(b,0,1);return n<0?-1:b[0]&255;}
                public int read(byte[] b,int off,int len)throws IOException{
                    if(++readCalls>100000)throw new AssertionError("unbounded socket reads");
                    if(beforeRead!=null)beforeRead.run();
                    if(readFailAfter<0||position>=readFailAfter)fault(readFault);
                    if(b==null)throw new NullPointerException();
                    if(off<0||len<0||off>b.length-len)throw new IndexOutOfBoundsException();
                    if(len==0)return zeroLengthMode==1&&position>=body.length?-1:0;
                    if(zeroReads>0){zeroReads--;return 0;}
                    if(position>=body.length)return -1;
                    int n=Math.min(len,Math.min(chunk,body.length-position));
                    System.arraycopy(body,position,b,off,n);position+=n;return n;
                }
                public void close()throws IOException{inputClosed++;fault(inputCloseFault);}
            };
        }
        public OutputStream openOutputStream()throws IOException{
            fault(openOutputFault);if(openOutputFault==6)return null;
            return new OutputStream(){
                public void write(int n)throws IOException{fault(writeFault);sent.write(n);}
                public void flush()throws IOException{flushes++;fault(flushFault);}
                public void close()throws IOException{outputClosed++;fault(outputCloseFault);}
            };
        }
        public DataInputStream openDataInputStream()throws IOException{return new DataInputStream(openInputStream());}
        public DataOutputStream openDataOutputStream()throws IOException{return new DataOutputStream(openOutputStream());}
        public void close()throws IOException{closed++;fault(socketCloseFault);}
        public int getLocalPort()throws IOException{fault(localFault);return localPort;}
        public String getLocalAddress()throws IOException{fault(localFault);return localAddress;}
        public String getAddress(){return "203.0.113.8";}
        public int getPort(){return 5190;}
        public void setSocketOption(byte option,int value){}
        public int getSocketOption(byte option){return 0;}
    }
}
