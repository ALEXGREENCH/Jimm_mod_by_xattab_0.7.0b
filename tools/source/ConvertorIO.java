import java.io.*;

/** Script the resource stream and Character result, leaving the original parser/fallbacks intact. */
public class ConvertorIO {
    public static byte[] bytes;
    public static int available=-1,readLimit=-1,failure,caseMode,closes;
    public static StringBuffer log=new StringBuffer();
    public static InputStream open(Class owner,String path){log.append("open:").append(owner.getName()).append(':').append(path).append(';');return bytes==null?null:new Stream();}
    public static char lower(char c){return caseMode==2?c:caseMode==1?(c>='A'&&c<='Z'?(char)(c+32):c):Character.toLowerCase(c);}
    public static char upper(char c){return caseMode==2?c:caseMode==1?(c>='a'&&c<='z'?(char)(c-32):c):Character.toUpperCase(c);}
    public static class Stream extends InputStream {
        int pos;
        public int available()throws IOException{log.append("available;");if(failure==1)throw new IOException("available");return available==-1?bytes.length:available;}
        public int read()throws IOException{if(failure==2)throw new IOException("read");return pos<bytes.length?bytes[pos++]&255:-1;}
        public int read(byte[] target)throws IOException{log.append("read:").append(target.length).append(';');if(failure==2)throw new IOException("read");if(pos>=bytes.length)return -1;int size=Math.min(target.length,bytes.length-pos);if(readLimit>=0)size=Math.min(size,readLimit);System.arraycopy(bytes,pos,target,0,size);pos+=size;return size;}
        public void close()throws IOException{closes++;log.append("close:").append(pos).append(';');if(failure==3)throw new IOException("close");}
    }
}
