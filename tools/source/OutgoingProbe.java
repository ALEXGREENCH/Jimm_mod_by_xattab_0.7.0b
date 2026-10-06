import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/** Outgoing ICBM selection/encoding/IDs and incoming file-acceptance negotiation. */
public class OutgoingProbe extends DirectProbe {
    static Class<?> plain,message,listener,splash;
    static int requestFailure;
    static Object receiver;
    static Method forward;
    static void contactInt(int key,int value)throws Exception{m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(contactItem,key,value);}
    static void prepare(int client,int caps,int status,int flags)throws Exception{
        reset(false);actions().clear();string(0,"54321");contactInt(76,client);contactInt(75,caps);contactInt(192,status);
        bool(164,(flags&1)!=0);bool(170,(flags&2)!=0);f(send,"d","msgCounter",int.class).setInt(null,0);
        m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,0L);
    }
    static Object plain(String text,int type)throws Exception{return instance(plain,new Class<?>[]{String.class,contact,int.class,long.class,String.class},"54321",contactItem,type,123456L,text);}
    static Object action(Object msg)throws Exception{return instance(send,new Class<?>[]{message},msg);}
    static void sent(String label,Object value,String result)throws Exception{
        String s=label+":"+result+":id="+m(send,"a","getMsgId",long.class).invoke(value)+":seq="+f(send,"a","SEQ1",int.class).getInt(value)
            +":counter="+f(send,"d","msgCounter",int.class).getInt(null)+":done="+m(send,"a","isCompleted",boolean.class).invoke(value)+":error="+m(send,"b","isError",boolean.class).invoke(value);
        Vector packets=(Vector)io.getField("packets").get(null);for(Object p:packets)s+=":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p));packets.clear();row(s);
    }
    static void send(String label,Object value)throws Exception{sent(label,value,invoke(m(send,"a","init",void.class),value));}
    static void outgoing()throws Exception{
        for(int client=0;client<=41;client++)for(int caps:new int[]{0,1,2,3,9,0x800001})for(int status:new int[]{-1,0})for(int flags=0;flags<4;flags++){
            prepare(client,caps,status,flags);send("route-"+client+"-"+caps+"-"+status+"-"+flags,action(plain("Text\r\nLine",1)));
        }
        for(int type:new int[]{0,1,4,999,1000,1001,1002,1003,1004,1005})for(int caps:new int[]{0,2,3})for(String text:new String[]{"","x","\u041f\u0440\u0438\u0432\u0435\u0442","a\rb\nc\r\nd","\u00e9\u4e2d\ud83d\ude00"}){
            prepare(0,caps,0,3);send("encoding-"+type+"-"+caps+"-"+b64(text.getBytes("UTF-8")),action(plain(text,type)));
        }
        for(int ownStatus:new int[]{-1,0,1,2,4,16,32,256,8193,12288,16384,20480,24576}){
            prepare(0,3,0,3);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)ownStatus);send("own-status-"+ownStatus,action(plain("status",1)));
        }
        for(long now:new long[]{0,1,2147483647L,2147483648L,4294967295L,4294967296L})for(int counter:new int[]{0,1,2147483647,-2147483648,-1}){
            prepare(0,3,0,3);io.getField("now").setLong(null,now);f(send,"d","msgCounter",int.class).setInt(null,counter);
            Object first=action(plain("first",1)),second=action(plain("second",1));send("ids-"+now+"-"+counter+"-first",first);send("ids-"+now+"-"+counter+"-second",second);send("ids-repeat",first);
        }
        for(int size:new int[]{0,1,2048,65536})for(int status:new int[]{-1,0})for(String name:new String[]{"","a.txt","\u0444\u0430\u0439\u043b.txt"}){
            prepare(0,3,status,3);Object msg=file(new ByteArrayInputStream(new byte[0]),size);f(fileMessage,"a","filename",String.class).set(msg,name);f(fileMessage,"b","description",String.class).set(msg,"Description \u0444\r\nLine");
            send("offer-"+size+"-"+status+"-"+enc(name),action(msg));
        }
        for(boolean file:new boolean[]{false,true}){
            prepare(0,3,0,3);io.getField("failSend").setBoolean(null,true);Object value=action(file?file(new ByteArrayInputStream(new byte[0]),10):plain("failed",1));send("send-fault-"+file,value);
            io.getField("failSend").setBoolean(null,false);send("send-retry-"+file,value);
            sent("forward-unused-"+file,value,invoke(m(send,"a","forward",boolean.class,packet),value,hello()));
        }
    }
    static byte[] dword(int n){return new byte[]{(byte)n,(byte)(n>>8),(byte)(n>>16),(byte)(n>>24)};}
    static byte[] acceptance(int ack,byte[] core,byte[] trailing)throws Exception{
        return join(new byte[26],tlv(3,new byte[]{1,2,3,4}),tlv(4,new byte[]{5,6,7,8}),tlv(10,word(ack)),tlv(0x2711,core),trailing);
    }
    static byte[] core(byte[] filename,boolean size)throws Exception{
        byte[] text="Description".getBytes("US-ASCII"),plugin="File".getBytes("US-ASCII");
        return join(new byte[45],le(26),new byte[4],le(1),new byte[1],new byte[20],dword(plugin.length),plugin,new byte[19],dword(text.length),text,
            word(4567),new byte[2],le(filename.length),filename,size?dword(123456):new byte[0]);
    }
    static void negotiation(String label,boolean active,boolean known,boolean hasFile,byte[] data)throws Exception{
        prepare(0,3,0,3);bool(166,false);bool(162,false);bool(183,true);bool(172,false);
        io.getField("failures").setInt(null,requestFailure);
        if(known)((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);
        m(contact,"a","setFTM",void.class,fileMessage).invoke(contactItem,hasFile?file(new ByteArrayInputStream(new byte[0]),123456):null);
        m(contact,"a","setIPValue",void.class,int.class,byte[].class).invoke(contactItem,225,new byte[4]);m(contact,"a","setIPValue",void.class,int.class,byte[].class).invoke(contactItem,226,new byte[4]);contactInt(74,0);
        if(active)m(splash,"a","show",void.class).invoke(null);else ask(make(true),"file","description");
        Object p=snac(4,7,0,join(new byte[]{1,2,3,4,5,6,7,8},word(2),new byte[]{5},"12345".getBytes("US-ASCII"),new byte[4],tlv(5,data)));
        String s=label+":"+invoke(forward,receiver,p)+":port="+m(contact,"b","getIntValue",int.class,int.class).invoke(contactItem,74)
            +":ip="+b64((byte[])m(contact,"a","getIPValue",byte[].class,int.class).invoke(contactItem,225))+":external="+b64((byte[])m(contact,"a","getIPValue",byte[].class,int.class).invoke(contactItem,226));
        for(Object e:actions()){
            Object[] event=(Object[])e,args=(Object[])event[1];s+=":event="+event[0];
            if(event[0].equals("action"))s+=":direct="+direct.isInstance(args[0])+":file="+(f(direct,"a","ft",fileMessage).get(args[0])!=null);
            else if(event[0].equals("file-timer"))s+=":"+enc(m(resource,"a","getString",String.class,String.class).invoke(null,args[0]))+":"+direct.isInstance(args[1])+":"+args[2];
            else if(event[0].equals("file-error"))s+=":"+enc(((Throwable)args[0]).getMessage());
        }
        row(s);
    }
    static void incomingFiles()throws Exception{
        byte[] filename="file.txt\0".getBytes("US-ASCII"),tail=tlv(4,new byte[]{9,10,11,12});
        for(boolean active:new boolean[]{false,true})for(boolean known:new boolean[]{false,true})for(boolean hasFile:new boolean[]{false,true})for(int ack:new int[]{-1,0,1,2,3})for(boolean size:new boolean[]{false,true}){
            negotiation("accept-"+active+"-"+known+"-"+hasFile+"-"+ack+"-"+size,active,known,hasFile,acceptance(ack,core(filename,size),tail));
        }
        for(int failure:new int[]{1,2})for(boolean size:new boolean[]{false,true}){
            requestFailure=failure;negotiation("queue-failure-"+failure+"-"+size,true,true,true,acceptance(2,core(filename,size),tail));
        }
        requestFailure=0;
        byte[] body=core(filename,true);for(int length=0;length<=body.length;length++)negotiation("accept-core-prefix-"+length,true,true,true,acceptance(2,Arrays.copyOf(body,length),tail));
        byte[] full=acceptance(2,body,tail);for(int length=0;length<=full.length;length++)negotiation("accept-prefix-"+length,true,true,true,Arrays.copyOf(full,length));
        for(byte[] trailing:new byte[][]{new byte[0],tlv(4,new byte[0]),tlv(4,new byte[3]),tlv(4,new byte[4]),tlv(5,new byte[]{1,2})})negotiation("accept-tail-"+b64(trailing),true,true,true,acceptance(2,body,trailing));
    }
    static void exerciseOutgoing()throws Exception{
        setupFiles();plain=load("av","jimm.comm.PlainMessage");message=load("ac","jimm.comm.Message");direct=load("bw","jimm.comm.DirectConnectionAction");listener=load("ae","jimm.comm.ActionListener");splash=load("cv","jimm.SplashCanvas");
        forward=m(listener,"a","forward",void.class,packet);receiver=Modifier.isStatic(forward.getModifiers())?null:instance(listener,new Class<?>[0]);integer(111,0);integer(72,1024);integer(70,1000);
        outgoing();incomingFiles();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseOutgoing();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
