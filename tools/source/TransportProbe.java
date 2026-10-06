import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Compare HTTP envelopes/monitor parsing and SOCKS authentication with scripted streams. */
public class TransportProbe extends NetworkStateProbe {
    static Class<?> io,endpoint,http,socks,packet,traffic;
    static Object owner;
    static String b64(byte[] value){return Base64.getEncoder().encodeToString(value);}
    static void reset()throws Exception{
        io.getMethod("reset").invoke(null);
        f(traffic,"f","sessionInTraffic",int.class).setInt(null,0);
        f(traffic,"g","sessionOutTraffic",int.class).setInt(null,0);
    }
    static Object endpoint(byte[] body)throws Exception{return io.getMethod("add",byte[].class).invoke(null,(Object)body);}
    static void property(Object e,String name,int value)throws Exception{endpoint.getField(name).setInt(e,value);}
    static void setString(int id,String value)throws Exception{m(options,"a","setString",void.class,int.class,String.class).invoke(null,id,value);}
    static void setInt(int id,int value)throws Exception{m(options,"a","setInt",void.class,int.class,int.class).invoke(null,id,value);}
    static Object channel(Class<?> type)throws Exception{return instance(type,new Class<?>[]{icq},owner);}
    static String exception(Throwable value)throws Exception {
        if(value==null)return "none";
        if(!error.isInstance(value))return value.getClass().getName();
        return f(error,"a","_ErrCode",int.class).getInt(value)+":"+b64(value.getMessage().getBytes("UTF-8"));
    }
    static Throwable invoke(Method method,Object object,Object...args)throws Exception {
        try{method.invoke(object,args);return null;}catch(InvocationTargetException e){return e.getCause();}
    }
    static void snapshot(String label,Object c,Throwable failure)throws Exception {
        StringBuilder s=new StringBuilder(label).append(":thrown=").append(exception(failure));
        s.append(":flag=").append(m(connection,"a","getInputCloseFlag",boolean.class).invoke(c));
        s.append(":errors=").append(io.getField("errors").getInt(null));
        s.append(":last=").append(exception((Throwable)io.getField("lastError").get(null)));
        s.append(":sleep=").append(io.getField("sleeps").getInt(null)).append('/').append(io.getField("slept").getLong(null));
        s.append(":traffic=").append(f(traffic,"f","sessionInTraffic",int.class).getInt(null)).append('/').append(f(traffic,"g","sessionOutTraffic",int.class).getInt(null));
        s.append(":trace=").append(io.getField("log").get(null));
        Vector endpoints=(Vector)io.getField("endpoints").get(null);
        for(int i=0;i<endpoints.size();i++) {
            Object e=endpoints.elementAt(i);s.append(":endpoint").append(i).append('=').append(b64(((ByteArrayOutputStream)endpoint.getField("sent").get(e)).toByteArray()));
            for(String key:new String[]{"inputClosed","outputClosed","closed","flushes"})s.append('/').append(endpoint.getField(key).getInt(e));
        }
        if(http.isInstance(c)) {
            s.append(":seq=").append(f(http,"a","seq",int.class).getInt(c));
            s.append(":connSeq=").append(f(http,"c","connSeq",int.class).getInt(c));
            s.append(":monitor=").append(f(http,"a","monitorURL",String.class).get(c));
            s.append(":online=").append(f(icq,"b","connected",boolean.class).getBoolean(null));
            s.append(":input=").append(f(http,"a","ism",InputStream.class).get(c)!=null);
            s.append(":output=").append(f(http,"a","osd",OutputStream.class).get(c)!=null);
            s.append(":hcm=").append(f(http,"a","hcm",HttpConnection.class).get(c)!=null);
            s.append(":hcd=").append(f(http,"b","hcd",HttpConnection.class).get(c)!=null);
            Vector values=(Vector)f(connection,"a","rcvdPackets",Vector.class).get(c);
            if(values!=null)for(Object value:values)s.append(":packet=").append(b64((byte[])value));
        } else s.append(":connected=").append(f(socks,"a","is_connected",boolean.class).getBoolean(c));
        row(s.toString());
    }
    static void setupHttp(Object c,int seq)throws Exception {
        f(http,"b","sid",String.class).set(c,"0123456789abcdef");f(http,"c","proxy_host",String.class).set(c,"proxy.invalid");
        f(http,"b","proxy_port",int.class).setInt(c,8080);f(http,"c","connSeq",int.class).setInt(c,seq);
    }
    static void httpSend()throws Exception {
        Method send=m(http,"a","sendPacket",void.class,packet,byte[].class,int.class,int.class);
        for(int response:new int[]{200,500})for(int fault:new int[]{0,2,3,4,6,7}) {
            reset();Object c=channel(http);setupHttp(c,2);Object e=endpoint(new byte[0]);property(e,"failure",fault);property(e,"response",response);
            snapshot("http-send-"+response+"-"+fault,c,invoke(send,c,null,new byte[]{1,2,3},5,2));
        }
        for(int fault=1;fault<=3;fault++) {
            reset();io.getField("openFailure").setInt(null,fault);Object c=channel(http);setupHttp(c,1);
            snapshot("http-open-error-"+fault,c,invoke(send,c,null,new byte[0],6,1));
        }
        for(int seq:new int[]{0,1,7})for(String host:new String[]{"login.invalid:5190","bad:port","missing-port"}) {
            reset();Object c=channel(http);setupHttp(c,seq);for(int i=0;i<3;i++)endpoint(new byte[0]);
            snapshot("http-connect-"+seq+"-"+host,c,invoke(m(http,"a","connect",void.class,String.class),c,host));
        }
        // Closing a missing/failing monitor must still attempt the data connection.
        for(boolean monitor:new boolean[]{false,true})for(int fault:new int[]{0,6}) {
            reset();Object c=channel(http);Object e=endpoint(new byte[0]);property(e,"failure",fault);
            if(monitor)f(http,"a","hcm",HttpConnection.class).set(c,e);
            f(http,"b","hcd",HttpConnection.class).set(c,e);
            snapshot("http-close-"+monitor+"-"+fault,c,invoke(m(http,"a","close",void.class),c));
        }
    }
    static byte[] join(byte[]...parts)throws Exception {ByteArrayOutputStream out=new ByteArrayOutputStream();for(byte[] p:parts)out.write(p);return out.toByteArray();}
    static void word(byte[] b,int off,int n){b[off]=(byte)(n>>>8);b[off+1]=(byte)n;}
    static byte[] wire(byte[] body)throws Exception{byte[] n=new byte[2];word(n,0,body.length);return join(n,body);}
    static byte[] envelope(int type,int seq,byte[] payload)throws Exception {
        byte[] body=new byte[12+payload.length];word(body,0,0x443);word(body,2,type);word(body,10,seq);System.arraycopy(payload,0,body,12,payload.length);return wire(body);
    }
    static void httpRead()throws Exception {
        byte[] flap={42,2,0,9,0,4,1,2,3,4};byte[] host="proxy.example".getBytes("US-ASCII");
        byte[] hello=new byte[30+host.length];word(hello,0,0x443);word(hello,2,2);
        for(int i=0;i<16;i++)hello[10+i]=(byte)i;word(hello,26,host.length);System.arraycopy(host,0,hello,28,host.length);word(hello,28+host.length,8080);
        byte[][] cases={wire(hello),envelope(5,1,join(flap,flap)),join(envelope(5,1,Arrays.copyOf(flap,8)),envelope(5,1,Arrays.copyOfRange(flap,8,10))),
            envelope(7,1,new byte[0]),envelope(7,2,new byte[0]),envelope(4,1,new byte[0]),envelope(5,1,new byte[]{1,2,3,4,5,6}),new byte[]{0,12,4,67,0}};
        for(int i=0;i<cases.length;i++)for(int chunk:new int[]{1,3,1024}) {
            reset();final Object c=channel(http);setupHttp(c,1);Object e=endpoint(cases[i]);property(e,"chunk",chunk);
            io.getField("exhausted").set(null,new Runnable(){public void run(){try{m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,true);}catch(Exception failure){throw new RuntimeException(failure);}}});
            f(icq,"b","connected",boolean.class).setBoolean(null,true);
            snapshot("http-monitor-"+i+"-"+chunk,c,invoke(m(http,"run","run",void.class),c));
        }
        for(int fault:new int[]{1,4,5,7,9})for(boolean stopped:new boolean[]{false,true}) {
            reset();Object c=channel(http);setupHttp(c,1);Object e=endpoint(envelope(4,1,new byte[0]));property(e,"failure",fault);
            m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,stopped);f(icq,"b","connected",boolean.class).setBoolean(null,true);
            snapshot("http-monitor-error-"+fault+"-"+stopped,c,invoke(m(http,"run","run",void.class),c));
        }
        for(long length:new long[]{-2,-1}) {
            reset();final Object c=channel(http);setupHttp(c,1);
            Object first=endpoint(envelope(5,1,Arrays.copyOf(flap,8)));
            Object second=endpoint(envelope(5,1,Arrays.copyOfRange(flap,8,10)));
            endpoint.getField("length").setLong(first,length);endpoint.getField("length").setLong(second,length);
            io.getField("exhausted").set(null,new Runnable(){public void run(){try{m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,true);}catch(Exception failure){throw new RuntimeException(failure);}}});
            f(icq,"b","connected",boolean.class).setBoolean(null,true);
            snapshot("http-cross-request-fragment-"+length,c,invoke(m(http,"run","run",void.class),c));
        }
    }
    static void socks()throws Exception {
        setString(8,"proxy.invalid");setString(9,"1080");setString(11,"user");setString(12,"pass");
        byte[][] replies={new byte[]{5,0,5,0,0,1,0,0,0,0,0,0},new byte[]{5,2,1,0,5,0,0,1,0,0,0,0,0,0},
            new byte[]{5,2,1,7},new byte[]{5,-1},new byte[]{4,0},new byte[]{5,0,5,4},new byte[]{5,0,5,0,0,3,1,65,0,0},new byte[0],new byte[]{5,2},new byte[]{5,2,1,0}};
        Method handshake=m(socks,"a","connect_socks",void.class,byte.class,String.class,String.class);
        for(int i=0;i<replies.length;i++)for(boolean stopped:new boolean[]{false,true}) {
            reset();Object c=channel(socks);endpoint(replies[i]);m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,stopped);
            snapshot("socks5-"+i+"-"+stopped,c,invoke(handshake,c,(byte)5,"login.invalid","5190"));
        }
        for(int version:new int[]{4,5})for(String host:new String[]{"203.0.113.4","login.invalid"}) {
            reset();Object c=channel(socks);endpoint(version==4?new byte[]{0,90,0,0,0,0,0,0}:replies[0]);endpoint(new byte[0]);
            snapshot("socks-destination-"+version+"-"+host,c,invoke(handshake,c,(byte)version,host,"5190"));
        }
        for(int failure=1;failure<=3;failure++)for(boolean stopped:new boolean[]{false,true}) {
            reset();io.getField("openFailure").setInt(null,failure);Object c=channel(socks);
            m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,stopped);
            snapshot("socks-open-error-"+failure+"-"+stopped,c,invoke(handshake,c,(byte)5,"login.invalid","5190"));
        }
        for(int failure:new int[]{1,2,3,5,8})for(boolean stopped:new boolean[]{false,true}) {
            reset();Object c=channel(socks);Object e=endpoint(replies[0]);property(e,"failure",failure);
            m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,stopped);
            snapshot("socks-stream-error-"+failure+"-"+stopped,c,invoke(handshake,c,(byte)5,"login.invalid","5190"));
        }
        for(int mode:new int[]{0,1,2})for(boolean reject:new boolean[]{false,true}) {
            reset();setInt(76,mode);Object c=channel(socks);endpoint(reject?new byte[]{5,-1}:replies[0]);endpoint(new byte[]{0,90,0,0,0,0,0,0});endpoint(new byte[0]);
            snapshot("socks-mode-"+mode+"-"+reject,c,invoke(m(socks,"a","connect",void.class,String.class),c,"login.invalid:5190"));
        }
    }
    static void exercise()throws Exception {
        owner=m(icq,"a","getIcq",icq).invoke(null);setString(17,"Recovery probe");setString(18,"fixture-profile");
        httpSend();httpRead();socks();
    }
    public static void main(String[] args){try{run(args);System.exit(0);}catch(Throwable failure){failure.printStackTrace();System.exit(1);}}
    static void run(String[] args)throws Exception {
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet();ClassLoader loader=midlet.getClass().getClassLoader();
        icq=Class.forName(n("r","jimm.comm.Icq"),true,loader);connection=Class.forName(n("ap","jimm.comm.Icq$Connection"),true,loader);
        options=Class.forName(n("cj","jimm.Options"),true,loader);error=Class.forName(n("bv","jimm.JimmException"),true,loader);
        traffic=Class.forName(n("x","jimm.Traffic"),true,loader);
        http=Class.forName(n("ay","jimm.comm.Icq$HTTPConnection"),true,loader);socks=Class.forName(n("cb","jimm.comm.Icq$SOCKSConnection"),true,loader);
        packet=Class.forName(n("an","jimm.comm.Packet"),true,loader);io=Class.forName("TransportIO",true,loader);endpoint=Class.forName("TransportIO$Endpoint",true,loader);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        final Throwable[] failure=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){failure[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Transport probe timeout");out.close();
        if(failure[0]!=null)throw new AssertionError("Transport probe failed",failure[0]);
        System.out.println("PASS probe: "+observations+" HTTP/SOCKS observations");
    }
}
