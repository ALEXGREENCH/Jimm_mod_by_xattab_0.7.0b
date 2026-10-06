import java.io.*;
import java.util.Vector;
import javax.microedition.io.*;

/** Scripted I/O boundary used only in transport test JARs. Never opens a network socket. */
public class TransportIO {
    public static Vector endpoints = new Vector();
    public static StringBuffer log = new StringBuffer();
    public static int cursor, openFailure, errors, sleeps;
    public static long slept;
    public static Object lastError;
    public static Runnable exhausted;
    public static void reset() {
        endpoints.removeAllElements();log.setLength(0);cursor=0;openFailure=0;
        errors=0;sleeps=0;slept=0;lastError=null;exhausted=null;
    }
    public static Endpoint add(byte[] body) { Endpoint e=new Endpoint(body);endpoints.addElement(e);return e; }
    public static Connection open(String url,int mode) throws IOException {
        log.append("open:").append(url).append(':').append(mode).append(';');
        if(openFailure==1)throw new ConnectionNotFoundException("fixture");
        if(openFailure==2)throw new IllegalArgumentException("fixture");
        if(openFailure==3)throw new IOException("fixture");
        if(cursor>=endpoints.size()) {
            if(exhausted!=null)exhausted.run();
            throw new IOException("end of script");
        }
        Endpoint endpoint=(Endpoint)endpoints.elementAt(cursor++);
        endpoint.url=url;return endpoint;
    }
    public static void sleep(long value) { sleeps++;slept+=value; }
    public static void start(Thread thread) { log.append("start;"); }
    public static void await(Object lock) { log.append("wait;"); }
    public static class Endpoint implements HttpConnection,SocketConnection {
        public byte[] body;
        public int chunk=1024, failure, response=200, inputClosed, outputClosed, closed, flushes;
        public long length=-2;
        public String url,method="GET";
        public ByteArrayOutputStream sent=new ByteArrayOutputStream();
        public Endpoint(byte[] bytes){body=bytes;}
        public InputStream openInputStream() throws IOException {
            if(failure==1)throw new IOException("input");
            if(failure==9)return null;
            final ByteArrayInputStream input=new ByteArrayInputStream(body);
            return new InputStream() {
                public int available() throws IOException {if(failure==8)throw new IOException("available");return input.available();}
                public int read() throws IOException {if(failure==5)throw new IOException("read");return input.read();}
                public int read(byte[] b,int offset,int count) throws IOException {
                    if(failure==5)throw new IOException("read");return input.read(b,offset,Math.min(chunk,count));
                }
                public void close() throws IOException { inputClosed++;if(failure==6)throw new IOException("close-input"); }
            };
        }
        public OutputStream openOutputStream() throws IOException {
            if(failure==2)throw new IOException("output");
            return new OutputStream() {
                public void write(int value) throws IOException { if(failure==3)throw new IOException("write");sent.write(value); }
                public void flush(){flushes++;}
                public void close() throws IOException { outputClosed++;if(failure==6)throw new IOException("close-output"); }
            };
        }
        public DataInputStream openDataInputStream() throws IOException{return new DataInputStream(openInputStream());}
        public DataOutputStream openDataOutputStream() throws IOException{return new DataOutputStream(openOutputStream());}
        public void close() throws IOException{closed++;if(failure==6)throw new IOException("close");}
        public void setRequestMethod(String value) throws IOException{method=value;log.append("method:").append(value).append(';');}
        public String getRequestMethod(){return method;}
        public void setRequestProperty(String key,String value) throws IOException {
            log.append("header:").append(key).append('=').append(value).append(';');
            if(failure==7)throw new IOException("property");
        }
        public String getRequestProperty(String key){return null;}
        public int getResponseCode() throws IOException{if(failure==4)throw new IOException("response");return response;}
        public String getResponseMessage(){return "fixture";}
        public String getURL(){return url;}public String getProtocol(){return "http";}
        public String getHost(){return "proxy.invalid";}public String getFile(){return "/";}
        public String getRef(){return null;}public String getQuery(){return null;}public int getPort(){return 8080;}
        public long getExpiration(){return 0;}public long getDate(){return 0;}public long getLastModified(){return 0;}
        public String getHeaderField(String name){return null;}public String getHeaderField(int index){return null;}
        public String getHeaderFieldKey(int index){return null;}public int getHeaderFieldInt(String name,int value){return value;}
        public long getHeaderFieldDate(String name,long value){return value;}
        public String getType(){return "application/octet-stream";}public String getEncoding(){return null;}
        public long getLength(){return length==-2?body.length:length;}
        public String getAddress(){return "203.0.113.8";}public String getLocalAddress(){return "192.0.2.9";}
        public int getLocalPort(){return 4321;}public void setSocketOption(byte option,int value){}
        public int getSocketOption(byte option){return 0;}
    }
}
