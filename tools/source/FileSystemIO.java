import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.io.*;

/** Portable scripted file API; the application retains its platform implementations. */
public class FileSystemIO extends FileTransferIO {
    public static boolean jsr(){return Boolean.getBoolean("jimm.files.jsr");}
    public static String api(){String target=System.getProperties().getProperty("jimm.files.target","MIDP2");return target.equals("SIEMENS2")?"com.siemens.mp.io.file.FileConnection":target.equals("MOTOROLA")&&!jsr()?"com.motorola.io.FileConnection":"javax.microedition.io.file.FileConnection";}
    public static String[] rootsMoto(){if(fileFailure==12)throw new IllegalStateException("file-roots");return rootEntries;}
    public static Connection open(String url)throws IOException {
        String path=url.substring(url.startsWith("file://localhost")?"file://localhost".length():"file://".length());while(path.startsWith("//"))path=path.substring(1);fileLog.append("url:").append(url).append(';');if(fileFailure==1)throw new IOException("file-open");
        try{return (Connection)Proxy.newProxyInstance(FileSystemIO.class.getClassLoader(),new Class[]{Class.forName(api())},new FileHandler(path){
            public Object invoke(Object proxy,Method method,Object[] args)throws Throwable {
                if(method.getName().equals("list")&&method.getReturnType()==String[].class){fileLog.append("list:").append(path).append(';');if(fileFailure==2)throw new IllegalStateException("file-list");return (String[])directories.get(path);}
                Object value=super.invoke(proxy,method,args);if((method.getName().equals("create")||method.getName().equals("delete"))&&method.getReturnType()==boolean.class)return Boolean.TRUE;return value;
            }
        });}catch(ClassNotFoundException missing){throw new IOException(missing.toString());}
    }
}
