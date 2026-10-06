import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Display;

/** In-memory JSR75 and HTTP boundaries; no host files or remote connections are opened. */
public class FileTransferIO extends TransportIO {
    public static Hashtable files=new Hashtable(),directories=new Hashtable();
    public static Vector serials=new Vector(),progress=new Vector(),messages=new Vector();
    public static int starts,saves,collections,fileFailure,fileCloses,inputCloses,outputCloses,flushes;
    public static StringBuffer fileLog=new StringBuffer();
    public static void resetFiles(){reset();files.clear();directories.clear();serials.removeAllElements();progress.removeAllElements();messages.removeAllElements();starts=saves=collections=fileFailure=fileCloses=inputCloses=outputCloses=flushes=0;fileLog.setLength(0);}
    public static void file(String path,byte[] body){files.put(path,body);}
    public static void directory(String path,String[] entries){directories.put(path,entries);}
    public static Enumeration roots(){return enumeration(new String[]{"card/"});}
    static Enumeration enumeration(String[] strings){Vector v=new Vector();if(strings!=null)for(int i=0;i<strings.length;i++)v.addElement(strings[i]);return v.elements();}
    public static Connection open(String url)throws IOException{return open(url,3);}
    public static Connection open(String url,int mode)throws IOException{
        if(!url.startsWith("file:"))return TransportIO.open(url,mode);
        String path=url.substring(url.startsWith("file://localhost")?"file://localhost".length():"file://".length());while(path.startsWith("//"))path=path.substring(1);
        fileLog.append("open:").append(path).append(':').append(mode).append(';');if(fileFailure==1)throw new IOException("file-open");
        return (Connection)Proxy.newProxyInstance(FileTransferIO.class.getClassLoader(),new Class[]{FileConnection.class},new FileHandler(path));
    }
    static class FileHandler implements InvocationHandler {
        final String path;FileHandler(String p){path=p;}
        public Object invoke(Object proxy,Method method,Object[] args)throws Throwable{
            String name=method.getName();
            if(name.equals("toString"))return path;if(name.equals("hashCode"))return new Integer(System.identityHashCode(proxy));if(name.equals("equals"))return Boolean.valueOf(proxy==args[0]);
            fileLog.append(name).append(':').append(path).append(';');
            if(name.equals("close")){fileCloses++;if(fileFailure==10)throw new IOException("file-close");return null;}
            if(name.equals("isDirectory"))return Boolean.valueOf(path.endsWith("/"));
            if(name.equals("exists"))return Boolean.valueOf(files.containsKey(path)||directories.containsKey(path));
            if(name.equals("canRead")||name.equals("canWrite")||name.equals("isOpen"))return Boolean.TRUE;
            if(name.equals("isHidden"))return Boolean.FALSE;
            if(name.equals("list")){if(fileFailure==2)throw new IOException("file-list");return enumeration((String[])directories.get(path));}
            if(name.equals("totalSize")||name.equals("availableSize")||name.equals("usedSize"))return new Long(1048576L);
            if(name.equals("fileSize")){if(fileFailure==3)throw new IOException("file-size");byte[] b=(byte[])files.get(path);return new Long(b==null?0:b.length);}
            if(name.equals("getName"))return path.substring(path.lastIndexOf('/')+1);
            if(name.equals("getPath"))return path.substring(0,path.lastIndexOf('/')+1);
            if(name.equals("getURL"))return "file://localhost"+path;
            if(name.equals("create")){if(fileFailure==5)throw new IOException("file-create");files.put(path,new byte[0]);return null;}
            if(name.equals("delete")){if(fileFailure==9)throw new IOException("file-delete");files.remove(path);return null;}
            if(name.equals("openOutputStream")||name.equals("openDataOutputStream")){
                if(fileFailure==6)throw new IOException("file-output");
                OutputStream out=new OutputStream(){
                    final ByteArrayOutputStream bytes=new ByteArrayOutputStream();
                    public void write(int value)throws IOException{if(fileFailure==7)throw new IOException("file-write");bytes.write(value);files.put(path,bytes.toByteArray());}
                    public void flush()throws IOException{flushes++;if(fileFailure==8)throw new IOException("file-flush");}
                    public void close(){outputCloses++;}
                };return name.equals("openDataOutputStream")?new DataOutputStream(out):out;
            }
            if(name.equals("openInputStream")||name.equals("openDataInputStream")){
                if(fileFailure==4)throw new IOException("file-input");byte[] body=(byte[])files.get(path);if(body==null)throw new IOException("missing fixture file");
                InputStream in=new ByteArrayInputStream(body){public void close(){inputCloses++;}};return name.equals("openDataInputStream")?new DataInputStream(in):in;
            }
            throw new UnsupportedOperationException("Unexpected file boundary: "+name);
        }
    }
    public static void serial(Display display,Runnable task){serials.addElement(task);}
    public static void start(Thread thread){starts++;}
    public static void save(){saves++;}
    public static void collect(){collections++;}
    public static void progress(int value){progress.addElement(new Integer(value));}
    public static void message(String value){messages.addElement(value);}
}
