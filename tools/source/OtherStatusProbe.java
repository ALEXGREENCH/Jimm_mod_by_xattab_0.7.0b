import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute complete OtherAction status/capability serialization and real socket sends. */
public class OtherStatusProbe {
    static boolean ref,clocked;static ClassLoader loader;static PrintWriter out;
    static Class<?> icq,connection,socket,options,other,guid,util,error;
    static Field table,privateId,sequence,channel,statusData,extended,capJimm;
    static Method privateStatus,status,userInfo;static Object owner;static byte[] initialStatus,initialCap;
    static int observations,privacyCases,statusCases,infoCases,successfulWrites;
    static String name(String a,String b){return ref?a:b;}
    static Class<?> type(String a,String b)throws Exception{return Class.forName(name(a,b),true,loader);}
    static Field field(Class<?> c,String a,String b,Class<?> t)throws Exception{return PopupProbe.field(c,name(a,b),t);}
    static Method method(Class<?> c,String a,String b,Class<?> t,Class<?>...p)throws Exception{return PopupProbe.method(c,name(a,b),t,p);}
    static String b64(byte[] b){return b==null?"null":Base64.getEncoder().encodeToString(b);}
    static void row(String s){out.println(s);observations++;}
    static class Stream extends OutputStream {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();StringBuilder calls=new StringBuilder();int fault,writes,attempts,flushes;
        Stream(int f){fault=f;}
        void fail(int where)throws IOException {
            calls.append(where).append('/').append(Thread.holdsLock(this)).append(';');
            if(where==1)attempts++;else flushes++;
            if(fault==where)throw new IOException("fixture");
            if(fault==7&&where==1&&attempts==2||fault==8&&where==2&&flushes==2)throw new IOException("fixture second packet");
            if(fault==3&&where==1)throw new IllegalStateException("fixture");
            if(fault==4&&where==1)throw new AssertionError("fixture");
        }
        public void write(int b)throws IOException{bytes.write(b);}
        public void write(byte[] data)throws IOException{fail(1);bytes.write(data);writes++;successfulWrites++;}
        public void flush()throws IOException{fail(2);}
    }
    static Stream stream;static Object currentSocket;
    static Object[] values;
    static void seed(int id,int fault)throws Exception {
        values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[3]="RU";values[88]=37;values[40]=new String(new char[12]);values[98]=8;values[173]=Boolean.TRUE;table.set(null,values);
        privateId.setInt(owner,id);sequence.setInt(null,32766);statusData.set(null,initialStatus.clone());extended.setBoolean(null,false);System.arraycopy(initialCap,0,(byte[])capJimm.get(null),0,initialCap.length);
        Constructor<?> ctor;Object[] args;
        try{ctor=socket.getDeclaredConstructor(icq);args=new Object[]{owner};}catch(NoSuchMethodException e){ctor=socket.getDeclaredConstructor();args=new Object[0];}
        ctor.setAccessible(true);currentSocket=ctor.newInstance(args);stream=new Stream(fault);
        field(socket,"a","os",OutputStream.class).set(currentSocket,fault==5?null:stream);channel.set(null,fault==6?null:currentSocket);
    }
    static String invoke(Method m,Object arg)throws Exception {
        try{m.invoke(null,arg);return "ok";}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;
            return error.isInstance(t)?"error:"+field(error,"a","_ErrCode",int.class).getInt(t)+":"+field(error,"a","critical",boolean.class).getBoolean(t)
                +":"+field(error,"b","displayMsg",boolean.class).getBoolean(t)+":"+field(error,"c","peer",boolean.class).getBoolean(t)
                +":"+b64(t.getMessage()==null?null:t.getMessage().getBytes("UTF-8")):"exception:"+t.getClass().getName();}
    }
    static void snapshot(String label,String result)throws Exception {
        boolean closed=(Boolean)method(connection,"a","getInputCloseFlag",boolean.class).invoke(currentSocket);
        row(label+":"+result+":id="+privateId.getInt(owner)+":seq="+sequence.getInt(null)+":closed="+closed+":writes="+stream.writes
            +":bytes="+b64(stream.bytes.toByteArray())+":calls="+stream.calls+":status="+b64((byte[])statusData.get(null))
            +":extended="+extended.getBoolean(null)+":jimm="+b64((byte[])capJimm.get(null)));
        if(Thread.holdsLock(stream))throw new AssertionError("Stream monitor leaked");
    }
    static void privacy()throws Exception {
        for(int id:new int[]{Integer.MIN_VALUE,-65536,-1,1,255,65535,65536,Integer.MAX_VALUE})for(int s:new int[]{-128,-1,0,1,2,3,4,5,127})for(int f=0;f<=6;f++) {
            seed(id,f);snapshot("privacy:"+id+":"+s+":"+f,invoke(privateStatus,Byte.valueOf((byte)s)));privacyCases++;
        }
        if(clocked)for(long tick:new long[]{0,1,-1,1273665600000L,Long.MAX_VALUE})for(int s:new int[]{-128,0,4,127})for(int f=0;f<=6;f++) {
            seed(0,f);type("OtherStatusClock","OtherStatusClock").getField("now").setLong(null,tick);
            snapshot("privacy-new:"+tick+":"+s+":"+f,invoke(privateStatus,Byte.valueOf((byte)s)));privacyCases++;
        }
    }
    static void statuses()throws Exception {
        int[] states={Integer.MIN_VALUE,-1,0,1,32,8193,12288,16384,20480,24576,65536,65568,268500992,268513280,Integer.MAX_VALUE};
        for(int client=-1;client<=23;client++)for(int s:states)for(boolean sent:new boolean[]{false,true})for(int f:new int[]{0,1,2,5,6,7,8}) {
            seed(17,f);values[94]=client;values[192]=Long.valueOf((long)s);extended.setBoolean(null,sent);
            snapshot("status:"+client+":"+s+":"+sent+":"+f,invoke(status,Integer.valueOf(s)));statusCases++;
        }
    }
    static void info()throws Exception {
        Class<?> array=Array.newInstance(guid,0).getClass();userInfo=method(other,"a","setUserInfo",void.class,array);
        for(int length:new int[]{0,1,2,15,16,17,255,256,257})for(int f=0;f<=6;f++) {
            seed(17,f);Object guids=Array.newInstance(guid,length);
            for(int i=0;i<length;i++){byte[] data=new byte[16];for(int j=0;j<16;j++)data[j]=(byte)(i*17+j*11);Array.set(guids,i,guid.getDeclaredConstructor(byte[].class).newInstance((Object)data));}
            snapshot("info:"+length+":"+f,invoke(userInfo,guids));infoCases++;
        }
        for(int broken=-2;broken<=3;broken++)for(int f:new int[]{0,1,2}) {
            seed(17,f);Object arrayValue=null;
            if(broken!=-2){arrayValue=Array.newInstance(guid,2);Array.set(arrayValue,0,guid.getDeclaredConstructor(byte[].class).newInstance((Object)new byte[16]));
                if(broken>=0)Array.set(arrayValue,1,guid.getDeclaredConstructor(byte[].class).newInstance((Object)new byte[broken==0?15:broken==1?16:broken==2?17:0]));}
            snapshot("info-broken:"+broken+":"+f,invoke(userInfo,arrayValue));infoCases++;
        }
    }
    public static void main(String[] args){try {
        ref=args[2].equals("reference");clocked=Boolean.parseBoolean(args[3]);Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        loader=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();options=type("cj","jimm.Options");table=field(options,"a","options",Object[].class);
        Object[] initial=new Object[256];for(int k=0;k<256;k++)initial[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);initial[3]="RU";initial[173]=Boolean.TRUE;table.set(null,initial);
        icq=type("r","jimm.comm.Icq");connection=type("ap","jimm.comm.Icq$Connection");socket=type("bf","jimm.comm.Icq$SOCKETConnection");
        other=type("bq","jimm.comm.OtherAction");guid=type("bh","jimm.comm.GUID");util=type("co","jimm.comm.Util");error=type("bv","jimm.JimmException");
        owner=icq.getDeclaredConstructor().newInstance();table=field(options,"a","options",Object[].class);privateId=field(icq,"c","privateId",int.class);
        sequence=field(icq,"a","flapSEQ",int.class);channel=field(icq,"a","c",connection);statusData=field(other,"a","CLI_SETSTATUS_DATA",byte[].class);
        extended=field(other,"a","extendedStatusSent",boolean.class);capJimm=field(util,"u","CAP_JIMM",byte[].class);
        initialStatus=((byte[])statusData.get(null)).clone();initialCap=((byte[])capJimm.get(null)).clone();
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"a","display",javax.microedition.lcdui.Display.class).set(null,javax.microedition.lcdui.Display.getDisplay(MIDletBridge.getCurrentMIDlet()));
        field(jimm,"a","VERSION",String.class).set(null,"0.7.0b");type("m","jimm.ContactList").getDeclaredConstructor().newInstance();
        privateStatus=method(other,"a","setPrivateStatus",void.class,byte.class);status=method(other,"a","setStatus",void.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));privacy();statuses();info();
        row("coverage:"+privacyCases+":"+statusCases+":"+infoCases+":"+successfulWrites);out.close();System.out.println("PASS other status: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
