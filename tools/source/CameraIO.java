import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.media.*;
import javax.microedition.media.control.VideoControl;

/** Scripted MMAPI boundary. Never accesses a physical camera. */
public class CameraIO implements InvocationHandler {
    public static StringBuffer log=new StringBuffer();
    public static String fail="";
    public static int failureType=1, failCount, snapshotSkips, snapshotCalls;
    public static boolean noControl;
    public static byte[] image;
    public static void reset(){log.setLength(0);fail="";failureType=1;failCount=0;snapshotSkips=snapshotCalls=0;noControl=false;}
    static void call(String name)throws IOException,MediaException {
        log.append(name).append(';');
        if(name.startsWith(fail)&&fail.length()>0&&failCount-->0){
            if(failureType==0)throw new IOException("camera");
            if(failureType==1)throw new MediaException("camera");
            if(failureType==2)throw new SecurityException("camera");
            throw new IllegalStateException("camera");
        }
    }
    public static Player createPlayer(String url)throws IOException,MediaException {
        call("create:"+url);return (Player)Proxy.newProxyInstance(CameraIO.class.getClassLoader(),new Class[]{Player.class},new CameraIO());
    }
    int state=Player.UNREALIZED;
    public Object invoke(Object proxy,Method method,Object[] args)throws Throwable {
        String name=method.getName();String suffix="";
        if(args!=null)for(Object a:args)suffix+=":"+(a instanceof javax.microedition.lcdui.Canvas?"canvas":String.valueOf(a));
        call(name+suffix);
        if(name.equals("realize")){state=Player.REALIZED;return null;}
        if(name.equals("start")){state=Player.STARTED;return null;}
        if(name.equals("stop")){state=Player.PREFETCHED;return null;}
        if(name.equals("close")){state=Player.CLOSED;return null;}
        if(name.equals("getState"))return new Integer(state);
        if(name.equals("getControl"))return noControl?null:Proxy.newProxyInstance(CameraIO.class.getClassLoader(),new Class[]{VideoControl.class},this);
        if(name.equals("getSnapshot"))return snapshotCalls++<snapshotSkips?null:image;
        if(name.equals("initDisplayMode"))return null;
        if(name.equals("setVisible")||name.equals("setDisplayLocation")||name.equals("setDisplaySize")||name.equals("setDisplayFullScreen")||name.equals("deallocate"))return null;
        throw new UnsupportedOperationException("Unexpected camera boundary: "+name);
    }
}
