import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute complete request serialization and real socket output on explicit inputs. */
public class XtrazQueryProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;static int observations,successfulWrites,cases;
    static Class<?> options,icq,socket,connection,util,error,clock;static Object owner,currentSocket;
    static Field table,channel,sequence,counter,traffic;static Method query,closed;static Stream stream;
    static Class<?> type(String key)throws Exception{return Class.forName(spec.getProperty(key).replace('/','.'),true,loader);}
    static Field field(Class<?> c,String key,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(key),t);}
    static String units(String s){if(s==null)return "null";StringBuilder b=new StringBuilder();for(int i=0;i<s.length();i++)b.append(Integer.toHexString(s.charAt(i))).append('.');return b.toString();}
    static String bytes(byte[] data){return Base64.getEncoder().encodeToString(data);}
    static class Stream extends OutputStream {
        ByteArrayOutputStream data=new ByteArrayOutputStream();String trace="";int fault,writes;
        Stream(int f){fault=f;}
        void observe(int where)throws IOException{
            trace+=where+"/"+Thread.holdsLock(this)+";";
            if(fault==where)throw new IOException("input stream fault");
            if(fault==3&&where==1)throw new IllegalStateException("input stream fault");
            if(fault==4&&where==2)throw new AssertionError("input stream fault");
        }
        public void write(int b)throws IOException{data.write(b);}
        public void write(byte[] b)throws IOException{observe(1);data.write(b);writes++;successfulWrites++;}
        public void flush()throws IOException{observe(2);}
    }
    static void seed(String self,boolean cp,int count,int seq,long now,int fault)throws Exception{
        Object[] values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[0]=self;values[3]="RU";values[133]=cp;values[173]=Boolean.TRUE;table.set(null,values);
        counter.setInt(null,count);sequence.setInt(null,seq);traffic.setInt(null,0);clock.getField("now").setLong(null,now);clock.getField("reads").setInt(null,0);
        Constructor<?> ctor;Object[] args;
        try{ctor=socket.getDeclaredConstructor(icq);args=new Object[]{owner};}catch(NoSuchMethodException e){ctor=socket.getDeclaredConstructor();args=new Object[0];}
        ctor.setAccessible(true);currentSocket=ctor.newInstance(args);stream=new Stream(fault);
        field(socket,"output",OutputStream.class).set(currentSocket,fault==5?null:stream);channel.set(null,fault==6?null:currentSocket);
    }
    static void invoke(String label,String uin)throws Exception{
        String result="ok";
        try{if(spec.getProperty("queryDesc").equals("(Ljava/lang/String;)V"))query.invoke(null,uin);else query.invoke(null,uin,0);}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;
            result=error.isInstance(t)?"jimm:"+field(error,"errorCode",int.class).getInt(t)+":"+field(error,"critical",boolean.class).getBoolean(t)
                +":"+field(error,"displayError",boolean.class).getBoolean(t)+":"+field(error,"peer",boolean.class).getBoolean(t)+":"+units(t.getMessage())
                :"exception:"+t.getClass().getName();}
        out.println(label+":"+result+":counter="+counter.getInt(null)+":seq="+sequence.getInt(null)+":closed="+closed.invoke(currentSocket)
            +":traffic="+traffic.getInt(null)+":clock="+clock.getField("reads").getInt(null)+":writes="+stream.writes+":trace="+stream.trace+":bytes="+bytes(stream.data.toByteArray()));
        observations++;cases++;if(Thread.holdsLock(stream))throw new AssertionError("Stream monitor leaked");
    }
    public static void main(String[] args){try{
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();
        options=type("options");table=field(options,"table",Object[].class);Object[] initial=new Object[256];for(int k=0;k<256;k++)initial[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);initial[3]="RU";initial[173]=true;table.set(null,initial);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));field(jimm,"version",String.class).set(null,"0.7.0b");
        if(spec.containsKey("bitmap")){Class<?> c=type("bitmap");field(c,"bitmapField",c).set(null,c.getDeclaredConstructor(String.class).newInstance("/font.prs"));}
        type("list").getDeclaredConstructor().newInstance();icq=type("icq");connection=type("connection");socket=type("socket");util=type("util");error=type("error");clock=type("clock");
        owner=icq.getDeclaredConstructor().newInstance();channel=field(icq,"channel",connection);sequence=field(icq,"sequence",int.class);counter=field(util,"counter",int.class);traffic=field(type("traffic"),"trafficOut",int.class);
        closed=PopupProbe.method(connection,spec.getProperty("closed"),boolean.class);Class<?> xtraz=type("xtraz");
        query=spec.getProperty("queryDesc").equals("(Ljava/lang/String;)V")?PopupProbe.method(xtraz,spec.getProperty("query"),void.class,String.class):PopupProbe.method(xtraz,spec.getProperty("query"),void.class,String.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));char[] longUin=new char[256];Arrays.fill(longUin,'1');
        String[] uins={null,"","1","12345",new String(longUin,0,255),new String(longUin),"\u0410\u010d\ud83d\ude00"};
        for(boolean cp:new boolean[]{false,true})for(String uin:uins)for(long now:new long[]{0,-1,2147483648L,1273665600000L,Long.MAX_VALUE})for(int count:new int[]{0,-1,Integer.MAX_VALUE,Integer.MIN_VALUE})for(int seq:new int[]{-1,32766,65535})for(int fault=0;fault<=6;fault++){
            seed("54321",cp,count,seq,now,fault);invoke("query:"+cp+":"+units(uin)+":"+now+":"+count+":"+seq+":"+fault,uin);
        }
        for(String self:new String[]{null,"","<>&'\"","\u041e\u0442\u0432\u0435\u0442"})for(boolean cp:new boolean[]{false,true}){seed(self,cp,0,0,1,0);invoke("account:"+units(self)+":"+cp,"12345");}
        if(successfulWrites<100)throw new AssertionError("No successful request sends");out.println("coverage:"+cases+":"+successfulWrites);observations++;out.close();System.out.println("PASS Xtraz queries: "+observations+" observations");System.exit(0);
    }catch(Throwable t){t.printStackTrace();System.exit(1);}}
}
