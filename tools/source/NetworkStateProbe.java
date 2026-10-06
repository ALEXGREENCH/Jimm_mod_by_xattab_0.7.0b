import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Compare lifecycle transitions with inert streams and the connect entry-point fixture. */
public class NetworkStateProbe {
    static boolean ref;
    static Class<?> icq, connection, socket, options, action, runnable, error, ui, popup;
    static PrintWriter out;
    static int observations;
    static String n(String a,String b){return ref?a:b;}
    static Field f(Class<?> c,String a,String b,Class<?> t)throws Exception{return PopupProbe.field(c,n(a,b),t);}
    static Method m(Class<?> c,String a,String b,Class<?> r,Class<?>...p)throws Exception{return PopupProbe.method(c,n(a,b),r,p);}
    static void row(String s){out.println(s);observations++;}
    static Object instance(Class<?> c,Class<?>[] p,Object...a)throws Exception{Constructor<?> k=c.getDeclaredConstructor(p);k.setAccessible(true);return k.newInstance(a);}
    static class Input extends ByteArrayInputStream {
        boolean closed;Input(){super(new byte[0]);}public void close(){closed=true;}
    }
    static class Output extends ByteArrayOutputStream {
        boolean closed;public void close(){closed=true;}
    }
    static class FailingOutput extends OutputStream {
        boolean closed;public void write(int b)throws IOException{throw new IOException("fixture");}
        public void close(){closed=true;}
    }
    static void transports(Object owner)throws Exception {
        ClassLoader loader=icq.getClassLoader();
        Class<?> packet=Class.forName(n("an","jimm.comm.Packet"),true,loader);
        Class<?> snac=Class.forName(n("ak","jimm.comm.SnacPacket"),true,loader);
        Class<?> socks=Class.forName(n("cb","jimm.comm.Icq$SOCKSConnection"),true,loader);
        Class<?> http=Class.forName(n("ay","jimm.comm.Icq$HTTPConnection"),true,loader);
        for(Class<?> type:new Class<?>[]{socket,socks,http})
            row("close-synchronized:"+(type==socket?"socket":type==socks?"socks":"http")+":"
                +Modifier.isSynchronized(m(type,"a","close",void.class).getModifiers()));
        for(long seed:new long[]{0,1,-1,12345,1273833600000L,Long.MAX_VALUE}) {
            System.setProperty("jimm.fixture.seed",Long.toString(seed));
            row("sequence-seed:"+seed+":"+m(icq,"b","getInitialFlapSequence",int.class).invoke(null));
        }
        for(Class<?> type:new Class<?>[]{socket,socks}) {
            String label=type==socket?"socket":"socks";
            for(boolean force:new boolean[]{false,true}) {
                Object channel=instance(type,new Class<?>[]{icq},owner);
                f(icq,"a","c",connection).set(null,channel);
                m(icq,"a","disconnect",void.class,boolean.class).invoke(null,force);
                row(label+"-retain:"+force+":"+(f(icq,"a","c",connection).get(null)==channel));
            }
            for(int sequence:new int[]{-1,0,32766,32767,65535})for(int length:new int[]{0,3,64}) {
                Object channel=instance(type,new Class<?>[]{icq},owner);Output output=new Output();
                f(type,"a","os",OutputStream.class).set(channel,output);
                f(icq,"a","flapSEQ",int.class).setInt(null,sequence);
                byte[] data=new byte[length];for(int i=0;i<length;i++)data[i]=(byte)(i*17);
                Object value=instance(snac,new Class<?>[]{int.class,int.class,long.class,byte[].class,byte[].class},4,6,123L,new byte[0],data);
                m(type,"a","sendPacket",void.class,packet).invoke(channel,value);
                m(type,"a","sendPacket",void.class,packet).invoke(channel,value);
                row(label+"-send:"+sequence+":"+length+":"+Base64.getEncoder().encodeToString(output.toByteArray()));
            }
            for(boolean absent:new boolean[]{false,true}) {
                Object channel=instance(type,new Class<?>[]{icq},owner);FailingOutput output=new FailingOutput();
                f(type,"a","os",OutputStream.class).set(channel,absent?null:output);
                Object value=instance(snac,new Class<?>[]{int.class,int.class,long.class,byte[].class,byte[].class},4,6,123L,new byte[0],new byte[0]);
                int code=-1;
                try {m(type,"a","sendPacket",void.class,packet).invoke(channel,value);}
                catch(InvocationTargetException exception){code=f(error,"a","_ErrCode",int.class).getInt(exception.getCause());}
                row(label+"-send-error:"+absent+":"+code+":closed="+output.closed+":flag="+m(connection,"a","getInputCloseFlag",boolean.class).invoke(channel));
            }
            m(options,"a","setInt",void.class,int.class,int.class).invoke(null,64,0);
            for(int chunk:new int[]{1,2,6,1024}) {
                Object channel=instance(type,new Class<?>[]{icq},owner);
                final int fragment=chunk;
                InputStream input=new ByteArrayInputStream(new byte[]{42,2,0,1,0,3,1,2,3,42,2,0,2,0,1,4}) {
                    public synchronized int read(byte[] b,int off,int len){return super.read(b,off,Math.min(fragment,len));}
                };
                f(type,"a","is",InputStream.class).set(channel,input);
                m(type,"run","run",void.class).invoke(channel);
                Vector packets=(Vector)f(connection,"a","rcvdPackets",Vector.class).get(channel);
                String result=label+"-receive:"+chunk+":"+packets.size();
                for(Object value:packets)result+=":"+Base64.getEncoder().encodeToString((byte[])value);
                row(result);
            }
        }
    }
    static void exercise()throws Exception{
        Object owner=m(icq,"a","getIcq",icq).invoke(null);
        Field transport=f(icq,"a","c",connection),connected=f(icq,"b","connected",boolean.class);
        Field stopped=f(icq,"c","disconnected",boolean.class),thread=f(icq,"a","thread",Thread.class);
        Field pending=f(icq,"a","reqAction",Vector.class),active=f(icq,"b","actAction",Vector.class);
        Thread marker=new Thread();
        for(boolean force:new boolean[]{false,true})for(boolean present:new boolean[]{false,true})
            for(boolean online:new boolean[]{false,true}) {
                Input input=new Input();Output output=new Output();
                Object channel=instance(socket,new Class<?>[]{icq},owner);
                f(socket,"a","is",InputStream.class).set(channel,input);
                f(socket,"a","os",OutputStream.class).set(channel,output);
                transport.set(null,present?channel:null);connected.setBoolean(null,online);stopped.setBoolean(null,false);thread.set(null,marker);
                Vector queue=new Vector();queue.addElement("queued");pending.set(null,queue);
                Vector actions=new Vector();actions.addElement("active");active.set(null,actions);
                m(icq,"a","disconnect",void.class,boolean.class).invoke(null,force);
                row("disconnect:"+force+":"+present+":"+online+":stopped="+stopped.getBoolean(null)+":online="+connected.getBoolean(null)
                    +":thread="+(thread.get(null)==marker)+":null="+(transport.get(null)==null)+":input="+input.closed+":output="+output.closed
                    +":flag="+m(connection,"a","getInputCloseFlag",boolean.class).invoke(channel)+":queues="+queue.size()+","+actions.size());
            }
        connected.setBoolean(null,true);thread.set(null,marker);
        Vector queue=new Vector();queue.addElement("queued");pending.set(null,queue);
        Vector actions=new Vector();actions.addElement("active");active.set(null,actions);
        m(icq,"e","resetServerCon",void.class).invoke(null);
        row("reset:online="+connected.getBoolean(null)+":thread="+(thread.get(null)==marker)+":queues="+queue.size()+","+actions.size());
        Method retryable=m(icq,"a","isNotCriticalConnectionError",boolean.class,int.class);
        for(int code=90;code<=150;code++)row("retryable:"+code+":"+retryable.invoke(null,code));
        Method setString=m(options,"a","setString",void.class,int.class,String.class),getString=m(options,"a","getString",String.class,int.class);
        m(options,"a","setInt",void.class,int.class,int.class).invoke(null,73,0);
        Field retries=f(icq,"b","reconnect_attempts",int.class);
        for(boolean disconnected:new boolean[]{false,true})for(int count:new int[]{-1,0,1,3}) {
            stopped.setBoolean(null,disconnected);retries.setInt(null,count);setString.invoke(null,1,"first second third");
            System.clearProperty("jimm.fixture.connect");
            Object task=instance(runnable,new Class<?>[]{int.class,Object[].class},14,null);
            m(runnable,"run","run",void.class).invoke(task);
            row("retry-task:"+disconnected+":"+count+":remaining="+retries.getInt(null)+":server="+getString.invoke(null,1)
                +":connect="+System.getProperty("jimm.fixture.connect")+":stopped="+stopped.getBoolean(null));
        }
        // Timeout checks must not display errors or attempt to reconnect.
        for(int state=-1;state<=10;state++)for(boolean busy:new boolean[]{false,true})for(boolean expired:new boolean[]{false,true})
            for(boolean cancelled:new boolean[]{false,true}) {
                Object login=instance(action,new Class<?>[]{String.class,String.class,String.class,String.class},"12345","secret","host","5190");
                f(action,"b","state",int.class).setInt(login,state);
                f(action,"b","active",boolean.class).setBoolean(login,busy);
                f(action,"c","cancel",boolean.class).setBoolean(login,cancelled);
                f(action,"a","lastActivity",long.class).setLong(login,expired?0:Long.MAX_VALUE-60000);
                boolean complete=(Boolean)m(action,"a","isCompleted",boolean.class).invoke(login);
                boolean failed=(Boolean)m(action,"b","isError",boolean.class).invoke(login);
                row("login:"+state+":"+busy+":"+expired+":"+cancelled+":complete="+complete+":error="+failed
                    +":state="+f(action,"b","state",int.class).getInt(login)+":cancel="+f(action,"c","cancel",boolean.class).getBoolean(login));
            }
        transports(owner);
        pending.set(null,new Vector());active.set(null,new Vector());thread.set(null,null);transport.set(null,null);
        // No actual restart is requested by these error paths.
        m(options,"a","setBoolean",void.class,int.class,boolean.class).invoke(null,149,false);
        for(boolean critical:new boolean[]{false,true})for(boolean display:new boolean[]{false,true}) {
            Object exception=critical?instance(error,new Class<?>[]{int.class,int.class},120,1)
                :instance(error,new Class<?>[]{int.class,int.class,boolean.class},120,1,display);
            connected.setBoolean(null,false);stopped.setBoolean(null,false);retries.setInt(null,0);
            m(error,"a","handleException",void.class,error).invoke(null,exception);
            row("error:"+critical+":"+display+":stopped="+stopped.getBoolean(null)+":online="+connected.getBoolean(null));
        }
    }
    public static void main(String[] args){try{run(args);System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
    static void run(String[] args)throws Exception{
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emu=(Common)ef.get(h);ArrayList<String> a=new ArrayList<String>();Collections.addAll(a,"--rms","memory",args[0]);
        emu.initParams(a,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emu.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet();ClassLoader loader=midlet.getClass().getClassLoader();
        icq=Class.forName(n("r","jimm.comm.Icq"),true,loader);connection=Class.forName(n("ap","jimm.comm.Icq$Connection"),true,loader);
        socket=Class.forName(n("bf","jimm.comm.Icq$SOCKETConnection"),true,loader);options=Class.forName(n("cj","jimm.Options"),true,loader);
        action=Class.forName(n("n","jimm.comm.ConnectAction"),true,loader);runnable=Class.forName(n("l","jimm.RunnableImpl"),true,loader);
        error=Class.forName(n("bv","jimm.JimmException"),true,loader);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        final Throwable[] failure=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){failure[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Lifecycle probe timeout");out.close();
        if(failure[0]!=null)throw new AssertionError("Lifecycle probe failed",failure[0]);
        System.out.println("PASS probe: "+observations+" offline lifecycle observations");
    }
}
