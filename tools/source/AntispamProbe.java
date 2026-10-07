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

/** Whole anti-spam execution with real contacts, journal, messages and Icq queueing. */
public class AntispamProbe {
    static Properties spec;static ClassLoader loader;static boolean clocked;static PrintWriter out;
    static Class<?> options,listener,plain,message,contact,list,icq,send,action,io,text,eyeType;
    static Field table,passed,seen,counts,contacts,queue,connected,own,waiter,eyeCounter,eyeUins;
    static Object owner,eye,eyeList;static Method spam,checked,getText,clear;
    static Object[] values;static int observations,filterCases,helperCases,sequenceCases,queued,journalEntries;
    static Class<?> type(String key)throws Exception{return Class.forName(spec.getProperty(key).replace('/','.'),true,loader);}
    static Field field(Class<?> c,String key,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(key),t);}
    static Method method(Class<?> c,String key,Class<?> t,Class<?>...p)throws Exception{return PopupProbe.method(c,spec.getProperty(key),t,p);}
    static String units(Object value){if(value==null)return "null";if(!(value instanceof String))return value.getClass().getName()+":"+value;String s=(String)value;StringBuilder b=new StringBuilder();for(int i=0;i<s.length();i++)b.append(Integer.toHexString(s.charAt(i))).append('.');return b.toString();}
    static String vector(Object value){if(value==null)return "null";StringBuilder b=new StringBuilder("[");for(Object x:(Vector)value)b.append(units(x)).append(';');return b.append(']').toString();}
    static void row(String value){out.println(value);observations++;}
    static Object person(String uin)throws Exception{return contact.getDeclaredConstructor(int.class,int.class,String.class,String.class,boolean.class,boolean.class).newInstance(1,1,uin,"Name",false,false);}
    static void seed(boolean enabled,boolean known,boolean admitted,boolean offline,long status,String answer,boolean connectedValue,boolean journal)throws Exception{
        values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[3]="RU";values[162]=enabled;values[172]=journal;values[173]=Boolean.TRUE;values[35]="question";values[36]="welcome";values[37]=answer;values[192]=Long.valueOf(status);table.set(null,values);
        Vector p=new Vector(),s=new Vector(),c=new Vector();if(admitted)p.addElement("1000");passed.set(null,p);seen.set(null,s);counts.set(null,c);
        Vector items=new Vector();if(known)items.addElement(person("1000"));contacts.set(null,items);
        connected.setBoolean(null,connectedValue);own.set(null,owner);waiter.set(null,new Object());
        io.getField("queueTrace").set(null,"");io.getField("queueFault").setInt(null,0);io.getField("dateFault").setInt(null,0);
        io.getField("clockReads").setInt(null,0);io.getField("dateReads").setInt(null,0);io.getField("monitor").set(null,owner);
        queue.set(null,Class.forName("AntispamIO$Queue",true,loader).getDeclaredConstructor().newInstance());
        field(send,"counterField",int.class).setInt(null,0);eyeCounter.setInt(eye,1);((Vector)eyeUins.get(eye)).removeAllElements();clear.invoke(eyeList);
    }
    static Object incoming(String uin,String body,boolean offline)throws Exception{return plain.getDeclaredConstructor(String.class,String.class,long.class,String.class,boolean.class).newInstance(uin,"self",123L,body,offline);}
    static String invoke(Method m,Object arg)throws Exception{
        try{return "return:"+m.invoke(null,arg);}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static void snapshot(String label,Object msg)throws Exception{
        String outcome=invoke(spam,msg);StringBuilder b=new StringBuilder(label+":"+outcome+":passed="+vector(passed.get(null))+":seen="+vector(seen.get(null))+":counts="+vector(counts.get(null)));
        Vector actions=(Vector)queue.get(null);b.append(":queue=");if(actions==null)b.append("null");else for(Object a:actions){
            Object p=field(send,"plainField",plain).get(a),rcvr=field(message,"recipient",contact).get(p);
            b.append('[').append(units(field(plain,"messageText",String.class).get(p))).append(':').append(units(field(message,"sender",String.class).get(p)))
                .append(':').append(field(message,"messageType",int.class).getInt(p)).append(':').append(field(message,"offline",boolean.class).getBoolean(p))
                .append(':').append(rcvr!=null).append(':').append(field(action,"actionOwner",icq).get(a)==owner).append(':').append(field(send,"id2",int.class).getInt(a));
            if(clocked)b.append(':').append(field(send,"id1",int.class).getInt(a)).append(':').append(field(message,"messageDate",long.class).getLong(p));
            b.append(']');queued++;
        }
        b.append(":counter=").append(field(send,"counterField",int.class).getInt(null)).append(":locks=").append(io.getField("queueTrace").get(null))
            .append(":journal=").append(eyeCounter.getInt(eye)).append(':').append(vector(eyeUins.get(eye)));
        int counter=eyeCounter.getInt(eye);for(int n=1;n<counter;n++){b.append('{').append(units(getText.invoke(eyeList,0,true,n))).append('}');journalEntries++;}
        if(clocked)b.append(":clocks=").append(io.getField("clockReads").getInt(null)).append('/').append(io.getField("dateReads").getInt(null));
        row(b.toString());if(Thread.holdsLock(owner)||Thread.holdsLock(eye))throw new AssertionError("Application monitor leaked");
    }
    static void filters()throws Exception{
        for(boolean enabled:new boolean[]{false,true})for(boolean known:new boolean[]{false,true})for(boolean admitted:new boolean[]{false,true})for(boolean offline:new boolean[]{false,true})for(boolean active:new boolean[]{false,true})for(long status:new long[]{0,256,512,1})for(String body:new String[]{null,"","answer","ANSWER","wrong","\u041e\u0442\u0432\u0435\u0442"}){
            seed(enabled,known,admitted,offline,status,"answer",active,false);snapshot("filter:"+enabled+":"+known+":"+admitted+":"+offline+":"+active+":"+status+":"+units(body),incoming("1000",body,offline));filterCases++;
        }
        for(boolean enabled:new boolean[]{false,true}){seed(enabled,false,false,false,0,"answer",true,false);snapshot("null-message:"+enabled,null);filterCases++;}
        for(boolean enabled:new boolean[]{false,true})for(boolean known:new boolean[]{false,true}){
            seed(enabled,known,false,false,0,"answer",true,false);Class<?> ft=type("fileMessage");Object rcvr=person("1000");
            Object file=Boolean.parseBoolean(spec.getProperty("fileHasType"))
                ?ft.getDeclaredConstructor(String.class,contact,int.class,String.class,String.class,InputStream.class,int.class).newInstance("1000",rcvr,26,"a.txt","file",new ByteArrayInputStream(new byte[0]),0)
                :ft.getDeclaredConstructor(String.class,contact,String.class,String.class,InputStream.class,int.class).newInstance("1000",rcvr,"a.txt","file",new ByteArrayInputStream(new byte[0]),0);
            snapshot("nonplain:"+enabled+":"+known,file);filterCases++;
        }
    }
    static void histories()throws Exception{
        String[] sequence={"wrong","wrong","wrong","wrong","answer","ANSWER","wrong"};
        for(boolean offline:new boolean[]{false,true})for(boolean active:new boolean[]{false,true})for(String answer:new String[]{"","answer","\u041e\u0442\u0432\u0435\u0442"})for(boolean journal:new boolean[]{false,true}){
            if(!clocked&&journal)continue;seed(true,false,false,offline,0,answer,active,journal);
            for(int i=0;i<sequence.length;i++){snapshot("sequence:"+offline+":"+active+":"+units(answer)+":"+journal+":"+i,incoming("1000",sequence[i],offline));sequenceCases++;}
            for(String uin:new String[]{"2000","1000","2000"}){snapshot("sequence-uins:"+units(uin)+":"+journal,incoming(uin,"wrong",false));sequenceCases++;}
        }
        for(String counter:new String[]{null,"","0","2","3","-1","2147483647","-2147483648","2147483648","wrong"})for(String body:new String[]{null,"answer","wrong"}){
            seed(true,false,false,false,0,"answer",true,false);((Vector)seen.get(null)).addElement("1000");((Vector)counts.get(null)).addElement(counter);
            snapshot("counter:"+units(counter)+":"+units(body),incoming("1000",body,false));sequenceCases++;
        }
        for(int broken=0;broken<8;broken++){
            seed(true,false,false,false,0,"answer",true,false);
            if(broken==0)passed.set(null,null);if(broken==1)((Vector)passed.get(null)).addElement(null);if(broken==2)seen.set(null,null);
            if(broken==3){((Vector)seen.get(null)).addElement("1000");counts.set(null,null);}if(broken==4)((Vector)seen.get(null)).addElement("1000");
            if(broken==5){((Vector)seen.get(null)).addElement("1000");((Vector)counts.get(null)).addElement(Integer.valueOf(0));}
            if(broken==6)((Vector)seen.get(null)).addElement(null);if(broken==7)values[37]=null;
            snapshot("broken-state:"+broken,incoming("1000","answer",false));sequenceCases++;
        }
        for(String answer:new String[]{"answer","\u041e\u0442\u0432\u0435\u0442","\u010d\u00edslo","r\u0103spuns","\u00df","\u0130",""})for(String body:new String[]{"answer","ANSWER","\u041e\u0422\u0412\u0415\u0422","\u043e\u0442\u0432\u0435\u0442","\u010c\u00cdSLO","R\u0102SPUNS","SS","i",""}){
            seed(true,false,false,false,0,answer,true,false);snapshot("phrase:"+units(answer)+":"+units(body),incoming("1000",body,false));sequenceCases++;
        }
    }
    static void failures()throws Exception{
        for(int fault=0;fault<5;fault++)for(boolean answer:new boolean[]{false,true}){
            seed(true,false,false,false,0,"answer",true,false);
            if(fault<3)io.getField("queueFault").setInt(null,fault);if(fault==3)own.set(null,null);if(fault==4)waiter.set(null,null);
            snapshot("queue-fault:"+fault+":"+answer,incoming("1000",answer?"answer":"wrong",false));sequenceCases++;
        }
        if(clocked)for(int fault=1;fault<=2;fault++)for(boolean journal:new boolean[]{false,true}){
            seed(true,false,false,false,0,"answer",true,journal);io.getField("dateFault").setInt(null,fault);
            snapshot("date-fault:"+fault+":"+journal,incoming("1000","answer",false));sequenceCases++;
        }
        for(int broken=0;broken<5;broken++){
            seed(true,false,false,false,0,"answer",true,false);if(broken==0)table.set(null,null);if(broken==1)table.set(null,Arrays.copyOf(values,160));if(broken==2)values[162]=null;if(broken==3)values[162]="wrong";if(broken==4)values[192]="wrong";
            snapshot("options-fault:"+broken,incoming("1000","answer",false));sequenceCases++;
        }
    }
    static void helper()throws Exception{
        for(int state=0;state<7;state++)for(String uin:new String[]{null,"","1000","2000","\u041e\u0442\u0432\u0435\u0442"}){
            seed(true,false,false,false,0,"answer",true,false);Vector p=(Vector)passed.get(null);
            if(state==1)p.addElement("1000");if(state==2)p.addElement("2000");if(state==3)p.addElement(null);if(state==4)p.addElement(Integer.valueOf(1));if(state==5){p.addElement("1000");p.addElement(null);}if(state==6)passed.set(null,null);
            row("checked:"+state+":"+units(uin)+":"+invoke(checked,uin)+":"+vector(passed.get(null)));helperCases++;
        }
    }
    public static void main(String[] args){try{
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}clocked=Boolean.parseBoolean(args[3]);
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();
        options=type("options");table=field(options,"table",Object[].class);values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);values[3]="RU";values[173]=Boolean.TRUE;table.set(null,values);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));field(jimm,"version",String.class).set(null,"0.7.0b");
        if(spec.containsKey("bitmap")){Class<?> bitmap=type("bitmap");field(bitmap,"bitmapField",bitmap).set(null,bitmap.getDeclaredConstructor(String.class).newInstance("/font.prs"));}
        list=type("list");list.getDeclaredConstructor().newInstance();contacts=field(list,"contacts",Vector.class);contact=type("contact");listener=type("listener");plain=type("plain");message=type("message");icq=type("icq");send=type("send");action=type("action");io=Class.forName("AntispamIO",true,loader);text=type("text");eyeType=type("eye");
        owner=icq.getDeclaredConstructor().newInstance();connected=field(icq,"connected",boolean.class);own=field(icq,"own",icq);waiter=field(icq,"wait",Object.class);queue=field(icq,"queue",Vector.class);
        passed=field(listener,"passed",Vector.class);seen=field(listener,"seen",Vector.class);counts=field(listener,"counts",Vector.class);spam=method(listener,"spam",boolean.class,message);checked=method(listener,"checked",boolean.class,String.class);
        eye=field(eyeType,"eyeInstance",eyeType).get(null);eyeList=field(eyeType,"eyeList",text).get(null);eyeCounter=field(eyeType,"eyeCounter",int.class);eyeUins=field(eyeType,"eyeUins",Vector.class);clear=method(text,"clearText",void.class);getText=method(text,"getText",String.class,int.class,boolean.class,int.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));filters();histories();failures();helper();
        if(queued<1||(clocked&&journalEntries<1))throw new AssertionError("Queue/journal scenarios did not execute");
        row("coverage:"+filterCases+":"+sequenceCases+":"+helperCases+":"+queued+":"+journalEntries);out.close();System.out.println("PASS antispam: "+observations+" observations");System.exit(0);
    }catch(Throwable t){t.printStackTrace();System.exit(1);}}
}
