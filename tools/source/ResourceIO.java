import java.io.*;
import java.util.*;

/** Script resource bytes/read/close failures without replacing the language parser. */
public class ResourceIO {
    public static Hashtable files=new Hashtable();
    public static StringBuffer log=new StringBuffer();
    public static int failureAt=-1,closes;
    public static boolean failClose;
    public static InputStream open(Class owner,String path){
        log.append("open:").append(path).append(';');byte[] data=(byte[])files.get(path);
        return data==null?null:new Bytes(data,path);
    }
    public static class Bytes extends InputStream {
        byte[] bytes;int pos;String path;
        Bytes(byte[] bytes,String path){this.bytes=bytes;this.path=path;}
        public int read()throws IOException{if(pos==failureAt)throw new IOException("scripted read");return pos<bytes.length?bytes[pos++]&255:-1;}
        public void close()throws IOException{closes++;log.append("close:").append(path).append(':').append(pos).append(';');if(failClose)throw new IOException("scripted close");}
    }
}
