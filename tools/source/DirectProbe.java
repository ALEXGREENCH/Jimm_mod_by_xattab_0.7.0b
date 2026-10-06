import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.io.*;

/** May direct transfer handshake, file segments, cancellation and peer socket framing. */
public class DirectProbe extends FileTransferProbe {
    static Class<?> direct,peer,dc;
    static Object channel,ep;
    static Object dc(byte[] bytes)throws Exception{return instance(dc,new Class<?>[]{byte[].class},(Object)bytes);}
    static Object newPeer()throws Exception{return instance(peer,ref?new Class<?>[0]:new Class<?>[]{icq},ref?new Object[0]:new Object[]{owner});}
    static Object file(InputStream input,int size)throws Exception{
        if(ref)return instance(fileMessage,new Class<?>[]{String.class,contact,String.class,String.class,InputStream.class,int.class},"54321",contactItem,"test \u0444.txt","description",input,size);
        return instance(fileMessage,new Class<?>[]{String.class,contact,int.class,String.class,String.class,InputStream.class,int.class},"54321",contactItem,26,"test \u0444.txt","description",input,size);
    }
    static byte[] bytes(int size){byte[] data=new byte[size];for(int i=0;i<size;i++)data[i]=(byte)(i*37);return data;}
    static Object setup(int size,InputStream input)throws Exception{
        resetEnv();integer(64,0);f(traffic,"f","sessionInTraffic",int.class).setInt(null,0);ep=env.getMethod("add",byte[].class).invoke(null,(Object)new byte[0]);
        Object value=instance(direct,new Class<?>[]{fileMessage},file(input,size));
        f(load("aa","jimm.comm.Action"),"a","icq",icq).set(value,owner);return value;
    }
    static void resultEvents(String label)throws Exception{
        for(Object item:actions()){Object[] e=(Object[])item;Object[] args=(Object[])e[1];row(label+":event="+e[0]+(args.length>0?":"+enc(error.isInstance(args[0])?((Throwable)args[0]).getMessage():args[0]):""));}actions().clear();
    }
    static void state(String label,Object value,String result)throws Exception{
        channel=f(icq,"a","peerC",peer).get(null);
        row(label+":"+result+":state="+f(direct,"a","state",int.class).getInt(value)+":packets="+f(direct,"b","packets",int.class).getInt(value)+":time="+f(direct,"a","timestamp",long.class).getLong(value)
            +":progress="+m(direct,"a","getProgress",int.class).invoke(value)+":done="+m(direct,"a","isCompleted",boolean.class).invoke(value)+":error="+m(direct,"b","isError",boolean.class).invoke(value)
            +":peer="+(channel!=null)+":message="+(m(contact,"a","getFTM",fileMessage).invoke(contactItem)!=null)+":out="+f(traffic,"g","sessionOutTraffic",int.class).getInt(null)
            +":sleeps="+env.getField("sleeps").getInt(null)+":"+env.getField("slept").getLong(null));
        row(label+":wire="+b64(((ByteArrayOutputStream)endpoint.getField("sent").get(ep)).toByteArray())+":closes="+endpoint.getField("inputClosed").get(ep)+":"+endpoint.getField("outputClosed").get(ep)+":"+endpoint.getField("closed").get(ep));
        resultEvents(label);
    }
    static void step(String label,Object value,byte[] packet)throws Exception{state(label,value,invoke(m(direct,"a","forward",boolean.class,LoginProbe.packet),value,dc(packet)));}
    static class SegmentInput extends ByteArrayInputStream {
        int limit,failAt,calls,cancelAt=-1;Object value;
        SegmentInput(int size,int limit,int failAt){super(bytes(size));this.limit=limit;this.failAt=failAt;}
        public synchronized int read(byte[] b,int off,int len){
            calls++;try{io.getField("now").setLong(null,io.getField("now").getLong(null)+25);
                if(calls==cancelAt)m(direct,"a","onEvent",void.class,int.class).invoke(value,2);
            }catch(Exception e){throw new RuntimeException(e);}
            if(calls==failAt)throw new IllegalStateException("fixture file read");return super.read(b,off,Math.min(limit,len));
        }
    }
    static void transfers()throws Exception{
        byte[] ack={1,0,0,0},ready=new byte[14];ready[0]=3;ready[13]=1;
        for(int size:new int[]{0,1,2047,2048,2049,4096})for(int mode:new int[]{0,1,2,3}){
            SegmentInput input=new SegmentInput(size,mode==1?17:2048,mode==2?1:-1);Object value=setup(size,input);input.value=value;if(mode==3)input.cancelAt=1;
            String label="transfer-"+size+"-"+mode;state(label+"-init",value,invoke(m(direct,"a","init",void.class),value));step(label+"-ack",value,ack);step(label+"-start",value,new byte[]{1});step(label+"-data",value,ready);
            state(label+"-complete",value,invoke(m(direct,"a","onEvent",void.class,int.class),value,1));
        }
        for(int failure:new int[]{1,2,3}){
            Object value=setup(5,new ByteArrayInputStream(bytes(5)));env.getField("openFailure").setInt(null,failure);state("open-fault-"+failure,value,invoke(m(direct,"a","init",void.class),value));
        }
        for(int failure:new int[]{1,2,3,6}){
            Object value=setup(5,new ByteArrayInputStream(bytes(5)));endpoint.getField("failure").setInt(ep,failure);state("stream-fault-"+failure,value,invoke(m(direct,"a","init",void.class),value));
        }
        for(int state:new int[]{-1,0,1,2,3})for(int length=0;length<=14;length++){
            Object value=setup(4,new ByteArrayInputStream(bytes(4)));invoke(m(direct,"a","init",void.class),value);f(direct,"a","state",int.class).setInt(value,state);
            byte[] data=Arrays.copyOf(ready,length);step("partial-"+state+"-"+length,value,data);
        }
        for(int event:new int[]{0,1,2,3,9})for(long elapsed:new long[]{0,1,9,10,125}){
            Object value=setup(4096,new ByteArrayInputStream(bytes(4096)));f(direct,"a","timestamp",long.class).setLong(value,elapsed);
            state("event-"+event+"-"+elapsed,value,invoke(m(direct,"a","onEvent",void.class,int.class),value,event));
        }
    }
    static void peerState(String label,String result)throws Exception{
        Vector packets=(Vector)f(peer,"a","rcvdPackets",Vector.class).get(channel);String data="";if(packets!=null)for(Object packet:packets)data+=b64((byte[])packet)+";";
        row(label+":"+result+":closed="+f(peer,"a","inputCloseFlag",boolean.class).getBoolean(channel)+":queued="+data+":available="+m(peer,"a","available",int.class).invoke(channel)+":in="+f(traffic,"f","sessionInTraffic",int.class).getInt(null));
        row(label+":io="+env.getField("log").get(null)+":sent="+b64(((ByteArrayOutputStream)endpoint.getField("sent").get(ep)).toByteArray())+":out="+f(traffic,"g","sessionOutTraffic",int.class).getInt(null));resultEvents(label);
    }
    static void sockets()throws Exception{
        byte[] data=join(new byte[]{4,0,1,0,0,0},new byte[]{3,0,6,9,8});
        for(int length=0;length<=data.length;length++)for(int chunk:new int[]{1,3,1024}){
            resetEnv();integer(64,0);f(traffic,"f","sessionInTraffic",int.class).setInt(null,0);ep=env.getMethod("add",byte[].class).invoke(null,(Object)Arrays.copyOf(data,length));endpoint.getField("chunk").setInt(ep,chunk);channel=newPeer();
            m(peer,"a","connect",void.class,String.class).invoke(channel,"peer.invalid:5190");peerState("receive-"+length+"-"+chunk,invoke(m(peer,"run","run",void.class),channel));
            Vector packets=(Vector)f(peer,"a","rcvdPackets",Vector.class).get(channel);int count=packets.size();
            for(int i=0;i<=count;i++){
                Object packet=m(peer,"a","getPacket",LoginProbe.packet).invoke(channel);row("received:"+(packet==null?"null":b64((byte[])m(LoginProbe.packet,"a","toByteArray",byte[].class).invoke(packet))));
            }
            m(peer,"a","close",void.class).invoke(channel);
        }
        for(int failure:new int[]{0,1,2,3,5,6,9}){
            resetEnv();integer(64,0);ep=env.getMethod("add",byte[].class).invoke(null,(Object)data);endpoint.getField("failure").setInt(ep,failure);channel=newPeer();
            String result=invoke(m(peer,"a","connect",void.class,String.class),channel,"peer.invalid:5190");peerState("peer-connect-"+failure,result);
            peerState("peer-send-"+failure,invoke(m(peer,"a","sendPacket",void.class,LoginProbe.packet),channel,dc(bytes(9))));
            peerState("peer-run-"+failure,invoke(m(peer,"run","run",void.class),channel));
            peerState("peer-close-"+failure,invoke(m(peer,"a","close",void.class),channel));
            row("peer-local-after-close:"+invoke(m(peer,"b","getLocalPort",int.class),channel)+":"+invoke(m(peer,"a","getLocalIP",byte[].class),channel));
        }
    }
    static void exerciseDirect()throws Exception{
        setupFiles();direct=load("bw","jimm.comm.DirectConnectionAction");peer=load("ao","jimm.comm.Icq$PeerConnection");dc=load("au","jimm.comm.DCPacket");
        m(contact,"a","setIPValue",void.class,int.class,byte[].class).invoke(contactItem,225,new byte[]{10,20,30,40});
        m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(contactItem,74,4567);m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(contactItem,193,123456);
        transfers();sockets();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseDirect();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
