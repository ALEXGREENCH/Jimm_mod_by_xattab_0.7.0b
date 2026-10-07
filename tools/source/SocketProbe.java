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

/** Execute original socket methods, including fragmented frames and exceptional cleanup. */
public class SocketProbe extends TransportProbe {
    static Class<?> to,snac;
    static int connects,closes,sends,receives;
    static Object channel()throws Exception{return channel(socket);}
    static Throwable call(Method method,Object object,Object...args)throws Exception{
        try{method.invoke(object,args);return null;}catch(InvocationTargetException e){
            Throwable t=e.getCause();if(t instanceof Error&&!t.getClass().getName().equals("SocketIO$ScriptError"))throw (Error)t;return t;
        }
    }
    static Object get(Object e,String n)throws Exception{return endpoint.getField(n).get(e);}
    static void val(Object e,String n,Object v)throws Exception{endpoint.getField(n).set(e,v);}
    static void flag(Object c,boolean v)throws Exception{m(connection,"a","setInputCloseFlag",void.class,boolean.class).invoke(c,v);}
    static Field stream(String n,Class<?> t)throws Exception{return f(socket,"a",n,t);}
    static void attach(Object c,Object e)throws Exception{
        stream("sc",SocketConnection.class).set(c,e);
        stream("is",InputStream.class).set(c,endpoint.getMethod("openInputStream").invoke(e));
        stream("os",OutputStream.class).set(c,endpoint.getMethod("openOutputStream").invoke(e));
    }
    static void snap(String label,Object c,Throwable failure)throws Exception{
        StringBuilder b=new StringBuilder(label).append(":throw=").append(exception(failure));
        b.append(":flag=").append(m(connection,"a","getInputCloseFlag",boolean.class).invoke(c));
        b.append(":streams=").append(stream("sc",SocketConnection.class).get(c)!=null).append('/').append(stream("is",InputStream.class).get(c)!=null).append('/').append(stream("os",OutputStream.class).get(c)!=null);
        b.append(":thread=").append(f(connection,"a","rcvThread",Thread.class).get(c)!=null);
        b.append(":seq=").append(f(icq,"a","flapSEQ",int.class).getInt(null)).append('/').append(f(socket,"a","nextIcqSequence",int.class).getInt(c));
        b.append(":traffic=").append(f(traffic,"f","sessionInTraffic",int.class).getInt(null)).append('/').append(f(traffic,"g","sessionOutTraffic",int.class).getInt(null));
        b.append(":error=").append(io.getField("errors").getInt(null)).append('/').append(exception((Throwable)io.getField("lastError").get(null)));
        for(String n:new String[]{"starts","sleeps","slept","yields","signals"})b.append(':').append(n).append('=').append(io.getField(n).get(null));
        b.append(":log=").append(io.getField("log").get(null));
        Vector es=(Vector)io.getField("endpoints").get(null);
        for(Object e:es){b.append(":io=").append(b64(((ByteArrayOutputStream)get(e,"sent")).toByteArray()));
            for(String n:new String[]{"position","readCalls","availableCalls","inputClosed","outputClosed","closed","flushes"})b.append('/').append(get(e,n));
        }
        Vector packets=(Vector)f(connection,"a","rcvdPackets",Vector.class).get(c);
        if(packets==null)b.append(":queue=null");else{b.append(":queue=").append(packets.size());for(Object p:packets)b.append('/').append(b64((byte[])p));}
        row(b.toString());
    }
    static void fresh()throws Exception{reset();f(icq,"a","flapSEQ",int.class).setInt(null,77);System.setProperty("jimm.fixture.seed","1273665600000");setInt(64,0);}
    static void connectCases()throws Exception{
        Method connect=m(socket,"a","connect",void.class,String.class);
        for(String host:new String[]{"login.invalid:5190","bad:port","",null})for(boolean stopped:new boolean[]{false,true})for(int fault=0;fault<=5;fault++){
            fresh();Object c=channel();flag(c,stopped);endpoint(new byte[0]);io.getField("openFailure").setInt(null,fault);
            snap("connect-"+host+'-'+stopped+'-'+fault,c,call(connect,c,host));connects++;
        }
        for(boolean stopped:new boolean[]{false,true})for(String field:new String[]{"openInputFault","openOutputFault"})for(int fault=1;fault<=6;fault++){
            fresh();Object c=channel();flag(c,stopped);Object e=endpoint(new byte[0]);property(e,field,fault);
            snap("connect-stream-"+stopped+'-'+field+'-'+fault,c,call(connect,c,"login.invalid:5190"));connects++;
        }
        for(long seed:new long[]{0,1,-1,1273665600000L,Long.MIN_VALUE,Long.MAX_VALUE}){
            fresh();System.setProperty("jimm.fixture.seed",String.valueOf(seed));Object c=channel();endpoint(new byte[0]);
            snap("connect-seed-"+seed,c,call(connect,c,"login.invalid:5190"));connects++;
        }
    }
    static void closeCases()throws Exception{
        Method close=m(socket,"a","close",void.class);
        for(int mask=0;mask<8;mask++)for(String field:new String[]{"inputCloseFault","outputCloseFault","socketCloseFault"})for(int fault=0;fault<=4;fault++){
            fresh();Object c=channel(),e=endpoint(new byte[0]);attach(c,e);property(e,field,fault);
            if((mask&1)!=0)stream("is",InputStream.class).set(c,null);
            if((mask&2)!=0)stream("os",OutputStream.class).set(c,null);
            if((mask&4)!=0)stream("sc",SocketConnection.class).set(c,null);
            for(int i=0;i<2;i++){snap("close-"+mask+'-'+field+'-'+fault+'-'+i,c,call(close,c));closes++;}
        }
    }
    static Object packet(int kind,int length)throws Exception{
        byte[] data=new byte[length];for(int i=0;i<length;i++)data[i]=(byte)(i*17);
        if(kind==0)return instance(snac,new Class<?>[]{int.class,int.class,long.class,byte[].class,byte[].class},4,6,123L,new byte[]{5,6},data);
        return instance(to,new Class<?>[]{int.class,long.class,int.class,int.class,String.class,int.class,byte[].class,byte[].class},0,123L,0,9,"12345",60,new byte[]{5,6},data);
    }
    static void sendCases()throws Exception{
        Method send=m(socket,"a","sendPacket",void.class,packet);
        for(int seq:new int[]{Integer.MIN_VALUE,-1,0,32766,32767,65535,65536,Integer.MAX_VALUE})for(int iseq:new int[]{-1,0,2,65535,Integer.MAX_VALUE})for(int kind=0;kind<2;kind++)for(int fault=0;fault<4;fault++){
            fresh();Object c=channel(),e=endpoint(new byte[0]);attach(c,e);f(icq,"a","flapSEQ",int.class).setInt(null,seq);f(socket,"a","nextIcqSequence",int.class).setInt(c,iseq);
            if(fault==1)property(e,"writeFault",1);if(fault==2)property(e,"flushFault",1);if(fault==3)stream("os",OutputStream.class).set(c,null);
            for(int i=0;i<2;i++){
                Object p=packet(kind,i==0?0:31);snap("send-"+seq+'-'+iseq+'-'+kind+'-'+fault+'-'+i,c,call(send,c,p));sends++;
                row("packet-sequence:"+f(packet,"c","sequence",int.class).getInt(p));
                if(kind==1)row("packet-icq:"+f(to,"d","icqSequence",int.class).getInt(p));
            }
        }
        for(boolean absent:new boolean[]{false,true}){
            fresh();Object c=channel(),e=endpoint(new byte[0]);attach(c,e);if(absent)stream("os",OutputStream.class).set(c,null);
            snap("send-null-"+absent,c,call(send,c,(Object)null));sends++;
        }
    }
    static byte[] frame(int channel,int length)throws Exception{
        byte[] b=new byte[6+length];b[0]=42;b[1]=(byte)channel;word(b,2,65535);word(b,4,length);for(int i=0;i<length;i++)b[6+i]=(byte)(i*17);return b;
    }
    static void receive(String label,byte[] body,int chunk,int connProp,int zeros,int zeroLength)throws Exception{
        fresh();Object c=channel(),e=endpoint(body);attach(c,e);property(e,"chunk",chunk);property(e,"zeroReads",zeros);property(e,"zeroLengthMode",zeroLength);
        setInt(64,connProp);f(icq,"a","c",connection).set(null,c);
        Vector old=new Vector();old.add("discarded");f(connection,"a","rcvdPackets",Vector.class).set(c,old);
        snap(label,c,call(m(socket,"run","run",void.class),c));receives++;
    }
    static void readCases()throws Exception{
        for(int channel:new int[]{0,1,2,3,4,5,255})for(int length:new int[]{0,1,2,5,6,7,16,31})for(int chunk:new int[]{1,2,5,6,7,65536})for(int cp:new int[]{0,1,2}){
            receive("frame-"+channel+'-'+length+'-'+chunk+'-'+cp,frame(channel,length),chunk,cp,2,0);
        }
        byte[] joined=join(frame(1,4),frame(2,10),frame(5,0),frame(255,3));
        for(int end=0;end<=joined.length;end++)for(int chunk:new int[]{1,5,65536})for(int cp:new int[]{0,1})for(int z=0;z<2;z++)receive("truncated-"+end+'-'+chunk+'-'+cp+'-'+z,Arrays.copyOf(joined,end),chunk,cp,0,z);
        for(int magic=0;magic<256;magic++){byte[] b=frame(2,1);b[0]=(byte)magic;receive("magic-"+magic,b,2,0,0,0);}
        for(int length:new int[]{255,256,4096,65535})for(int chunk:new int[]{64,1024,65536})receive("large-"+length+'-'+chunk,frame(2,length),chunk,0,0,0);
    }
    static void readErrors()throws Exception{
        for(int fault=1;fault<=3;fault++)for(int after:new int[]{0,5,6,10})for(boolean current:new boolean[]{false,true})for(final boolean stop:new boolean[]{false,true}){
            fresh();final Object c=channel();Object e=endpoint(frame(2,10));attach(c,e);property(e,"chunk",1);property(e,"readFault",fault);property(e,"readFailAfter",after);
            f(icq,"a","c",connection).set(null,current?c:null);
            if(stop)val(e,"beforeRead",new Runnable(){public void run(){try{flag(c,true);}catch(Exception x){throw new AssertionError(x);}}});
            snap("read-error-"+fault+'-'+after+'-'+current+'-'+stop,c,call(m(socket,"run","run",void.class),c));receives++;
        }
        for(int av:new int[]{0,1,3})for(int fail=0;fail<=2;fail++)for(final int callback:new int[]{0,1,2}){
            fresh();final Object c=channel();Object e=endpoint(frame(2,1));attach(c,e);setInt(64,1);property(e,"zeroAvailable",av);io.getField("sleepFailure").setInt(null,fail);
            io.getField("onSleep").set(null,new Runnable(){public void run(){try{if(callback==1)stream("is",InputStream.class).set(c,null);if(callback==2)flag(c,true);}catch(Exception x){throw new AssertionError(x);}}});
            snap("available-"+av+'-'+fail+'-'+callback,c,call(m(socket,"run","run",void.class),c));receives++;
        }
        for(int fault=1;fault<=3;fault++){
            fresh();Object c=channel(),e=endpoint(frame(2,1));attach(c,e);setInt(64,1);property(e,"availableFault",fault);f(icq,"a","c",connection).set(null,c);
            snap("available-error-"+fault,c,call(m(socket,"run","run",void.class),c));receives++;
        }
        for(boolean stopped:new boolean[]{false,true}){fresh();Object c=channel();flag(c,stopped);snap("read-null-"+stopped,c,call(m(socket,"run","run",void.class),c));receives++;}
    }
    static void localCases()throws Exception{
        for(String address:new String[]{"192.0.2.9","0.0.0.0","255.255.255.255","256.-1.2.3","1.2.3","x.2.3.4","",null})for(int fault=0;fault<=3;fault++){
            fresh();Object c=channel(),e=endpoint(new byte[0]);attach(c,e);val(e,"localAddress",address);property(e,"localFault",fault);
            Throwable failure;Object result=null;try{result=m(socket,"a","getLocalIP",byte[].class).invoke(c);failure=null;}catch(InvocationTargetException x){failure=x.getCause();}
            row("local-ip-"+address+'-'+fault+":"+(result==null?"null":b64((byte[])result))+":"+exception(failure));
        }
        for(int port:new int[]{Integer.MIN_VALUE,-1,0,5190,65535,65536,Integer.MAX_VALUE})for(int fault=0;fault<=3;fault++){
            fresh();Object c=channel(),e=endpoint(new byte[0]);attach(c,e);property(e,"localPort",port);property(e,"localFault",fault);
            try{row("local-port-"+port+'-'+fault+":"+m(socket,"b","getLocalPort",int.class).invoke(c));}catch(InvocationTargetException x){row("local-port-"+port+'-'+fault+":"+exception(x.getCause()));}
        }
    }
    static void exerciseSocket()throws Exception{
        owner=m(icq,"a","getIcq",icq).invoke(null);setString(17,"Recovery probe");setString(18,"fixture-profile");
        connectCases();closeCases();sendCases();readCases();readErrors();localCases();
        if(connects<70||closes<200||sends<600||receives<1000)throw new AssertionError("socket case series incomplete");
        row("connects:"+connects+"/closes:"+closes+"/sends:"+sends+"/receives:"+receives);
    }
    public static void main(String[] args){try{runSocket(args);System.exit(0);}catch(Throwable x){x.printStackTrace();System.exit(1);}}
    static void runSocket(String[] args)throws Exception{
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet();ClassLoader loader=midlet.getClass().getClassLoader();
        icq=Class.forName(n("r","jimm.comm.Icq"),true,loader);connection=Class.forName(n("ap","jimm.comm.Icq$Connection"),true,loader);socket=Class.forName(n("bf","jimm.comm.Icq$SOCKETConnection"),true,loader);
        options=Class.forName(n("cj","jimm.Options"),true,loader);error=Class.forName(n("bv","jimm.JimmException"),true,loader);traffic=Class.forName(n("x","jimm.Traffic"),true,loader);
        packet=Class.forName(n("an","jimm.comm.Packet"),true,loader);snac=Class.forName(n("ak","jimm.comm.SnacPacket"),true,loader);to=Class.forName(n("bu","jimm.comm.ToIcqSrvPacket"),true,loader);
        io=Class.forName("SocketIO",true,loader);endpoint=Class.forName("SocketIO$Endpoint",true,loader);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));final Throwable[] failure=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exerciseSocket();}catch(Throwable x){failure[0]=x;}finally{done.countDown();}}});
        if(!done.await(60,TimeUnit.SECONDS))throw new AssertionError("socket probe timeout");out.close();if(failure[0]!=null)throw new AssertionError("socket probe failed",failure[0]);
        System.out.println("PASS probe: "+observations+" socket observations");
    }
}
