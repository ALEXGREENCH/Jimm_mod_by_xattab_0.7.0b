import java.io.*;
import javax.microedition.io.*;

/** Script only Connector.open; observe monitors inside real resolver callbacks. */
public class ResolverIO {
    public static Class owner;
    public static Object receiver;
    public static int fault;
    public static String address;
    public static StringBuffer log = new StringBuffer();
    public static void reset(int value,String result){fault=value;address=result;log.setLength(0);}
    static void record(String call){log.append(call).append('/').append(Thread.holdsLock(owner)).append('/').append(Thread.holdsLock(receiver)).append(';');}
    static void fail(int io,int unchecked,int error)throws IOException {
        if(fault==io)throw new IOException("fixture");
        if(fault==unchecked)throw new IllegalArgumentException("fixture");
        if(fault==error)throw new AssertionError("fixture");
    }
    public static Connection open(String url,int mode)throws IOException {
        record("open:"+url+":"+mode);fail(1,2,3);
        if(fault==4)return null;
        if(fault==5)return new Connection(){public void close(){}};
        return new Endpoint();
    }
    public static class Endpoint implements SocketConnection {
        public String getAddress()throws IOException{record("address");fail(6,7,8);return address;}
        public void close()throws IOException{record("close");fail(9,10,11);}
        public String getLocalAddress(){return "192.0.2.1";}
        public int getLocalPort(){return 1;}public int getPort(){return 1;}
        public void setSocketOption(byte option,int value){}public int getSocketOption(byte option){return 0;}
        public InputStream openInputStream(){return new ByteArrayInputStream(new byte[0]);}
        public OutputStream openOutputStream(){return new ByteArrayOutputStream();}
        public DataInputStream openDataInputStream(){return new DataInputStream(openInputStream());}
        public DataOutputStream openDataOutputStream(){return new DataOutputStream(openOutputStream());}
    }
}
