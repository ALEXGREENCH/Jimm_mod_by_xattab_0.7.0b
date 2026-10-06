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

/** Replay server login and SSI packets through both implementations. */
public class LoginProbe extends NetworkStateProbe {
    static Class<?> io,packet,contact,list,update,item,http;
    static Object owner;
    static ClassLoader loader;
    static byte[] empty=new byte[0];
    static String b64(byte[] b){return b==null?"null":Base64.getEncoder().encodeToString(b);}
    static byte[] join(byte[]...parts)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();for(byte[] p:parts)b.write(p);return b.toByteArray();}
    static byte[] word(int v){return new byte[]{(byte)(v>>8),(byte)v};}
    static byte[] tlv(int type,byte[] b)throws Exception{return join(word(type),word(b.length),b);}
    static Object flap(int channel,byte[] data)throws Exception{
        byte[] b=join(new byte[]{42,(byte)channel,0,0},word(data.length),data);
        return m(packet,"a","parse",packet,byte[].class,int.class,int.class).invoke(null,b,0,b.length);
    }
    static Object snac(int family,int command,int flags,byte[] data)throws Exception{return flap(2,join(word(family),word(command),word(flags),new byte[4],data));}
    static Object hello()throws Exception{return flap(1,new byte[]{0,0,0,1});}
    static void integer(int key,int value)throws Exception{m(options,"a","setInt",void.class,int.class,int.class).invoke(null,key,value);}
    static void bool(int key,boolean v)throws Exception{m(options,"a","setBoolean",void.class,int.class,boolean.class).invoke(null,key,v);}
    static void string(int key,String v)throws Exception{m(options,"a","setString",void.class,int.class,String.class).invoke(null,key,v);}
    static void reset(boolean useHttp)throws Exception{
        io.getMethod("reset").invoke(null);
        Object channel=instance(useHttp?http:Class.forName("LoginConnection",true,loader),new Class<?>[]{icq},owner);
        f(icq,"a","c",connection).set(null,channel);f(icq,"b","connected",boolean.class).setBoolean(null,false);
        f(list,"a","cItems",Vector.class).set(null,new Vector());f(list,"b","gItems",Vector.class).set(null,new Vector());
        f(list,"d","haveToBeCleared",boolean.class).setBoolean(null,true);
        string(10,"1");
    }
    static Object login(String uin,String password)throws Exception{return instance(action,new Class<?>[]{String.class,String.class,String.class,String.class},uin,password,"login.invalid","5190");}
    static String invoke(Method method,Object value,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(value,args));}catch(InvocationTargetException e){Throwable cause=e.getCause();
            if(error.isInstance(cause))return "error:"+f(error,"a","_ErrCode",int.class).getInt(cause)+":"+b64(cause.getMessage().getBytes("UTF-8"));
            return "exception:"+cause.getClass().getName();}
    }
    static void snapshot(String label,Object a,String result)throws Exception{
        String s=label+":"+result+":state="+f(action,"b","state",int.class).getInt(a)+":active="+f(action,"b","active",boolean.class).getBoolean(a)
            +":time="+f(action,"a","lastActivity",long.class).getLong(a)+":connected="+f(icq,"b","connected",boolean.class).getBoolean(null)
            +":connections="+io.getField("connections").get(null)+":closes="+io.getField("closes").getInt(null)+":sleeps="+io.getField("sleeps").getInt(null);
        for(String[] names:new String[][]{{"a","ignoreList"},{"b","invisibleList"},{"c","visibleList"}}){
            Hashtable table=(Hashtable)f(action,names[0],names[1],Hashtable.class).get(a);s+=":"+new TreeMap(table);}
        Vector packets=(Vector)io.getField("packets").get(null);
        for(Object p:packets)s+=":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p));packets.clear();
        for(Object c:(Vector)f(list,"a","cItems",Vector.class).get(null))s+=":contact="+b64(save(c));
        s+=":groups="+((Vector)f(list,"b","gItems",Vector.class).get(null)).size();row(s);
    }
    static void step(String label,Object a,Object p)throws Exception{snapshot(label,a,invoke(m(action,"a","forward",boolean.class,packet),a,p));}
    static byte[] record(String name,int group,int id,int type,byte[] data)throws Exception{
        byte[] b=name.getBytes("UTF-8");return join(word(b.length),b,word(group),word(id),word(type),word(data.length),data);
    }
    static byte[] roster(byte[]...records)throws Exception{return join(new byte[]{0},word(records.length),join(records),new byte[]{1,2,3,4});}
    static byte[] save(Object c)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();m(contact,"a","saveToStream",void.class,DataOutputStream.class).invoke(c,new DataOutputStream(b));return b.toByteArray();}
    static void storage()throws Exception{
        for(byte[] data:new byte[][]{null,empty,tlv(0x6d,new byte[]{1,2,3}),join(tlv(0x15c,empty),tlv(0x15d,new byte[]{-1,0,42}))})for(boolean auth:new boolean[]{false,true}){
            Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Fixture",auth,true);
            m(contact,"a","setIPValue",void.class,int.class,byte[].class).invoke(c,227,data);
            row("storage-identity:"+b64(data)+":"+(m(contact,"a","getIPValue",byte[].class,int.class).invoke(c,227)==data));
            byte[] serialized=save(c);Object loaded=instance(contact,new Class<?>[0]);DataInputStream input=new DataInputStream(new ByteArrayInputStream(serialized));input.readByte();
            m(contact,"a","loadFromStream",void.class,DataInputStream.class).invoke(loaded,input);
            row("storage:"+auth+":"+b64(serialized)+":"+b64(save(loaded))+":remaining="+input.available());
            for(int type=1;type<=5;type++)for(int group:new int[]{0,9}){
                Object a=instance(update,new Class<?>[]{item,int.class},c,type);
                row("pack:"+auth+":"+type+":"+group+":"+b64((byte[])m(update,"a","packRosterItem",byte[].class,contact,int.class).invoke(a,c,group)));
            }
        }
    }
    static void statuses()throws Exception{
        Class<?> other=load("bq","jimm.comm.OtherAction");
        for(int client=0;client<=22;client++)for(boolean statusOnly:new boolean[]{false,true}){
            reset(false);integer(94,client);integer(88,statusOnly?0:1);Object a=login("12345","secret");
            snapshot("capabilities-"+client+"-"+statusOnly,a,invoke(m(other,"a","setStandartUserInfo",void.class,boolean.class),null,statusOnly));
            snapshot("status-"+client,a,invoke(m(other,"a","setStatus",void.class,int.class),null,0));
        }
        integer(94,7);integer(88,1);
        for(int status:new int[]{0,32,8193,12288,16384,20480,24576}){
            reset(false);Object a=login("12345","secret");m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)status);
            snapshot("extended-"+status,a,invoke(m(other,"a","setStatus",void.class,int.class),null,status));
            snapshot("extended-clear-"+status,a,invoke(m(other,"a","setStatus",void.class,int.class),null,0));
        }
        m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,0L);
        for(int xstatus:new int[]{-1,0,1,36,37,38,255})for(String text:new String[]{"", "ASCII", "\u041f\u0440\u0438\u0432\u0435\u0442", String.join("",Collections.nCopies(260,"X"))}){
            reset(false);Object a=login("12345","secret");string(32,text);string(33,"message");
            snapshot("mood-"+xstatus+"-"+text.length(),a,invoke(m(icq,"b","setXStatus",void.class,int.class),null,xstatus));
        }
    }
    static void extraLogin()throws Exception{
        reset(false);Object a=login("12345","secret"),b=login("12345","secret");
        Hashtable table=(Hashtable)f(action,"a","ignoreList",Hashtable.class).get(a);table.put("54321",17);
        snapshot("tables-a",a,"initial");snapshot("tables-b",b,"isolated");snapshot("tables-reinit",a,invoke(m(action,"a","init",void.class),a));
        for(int state:new int[]{1,2,4,5,6,8}){
            reset(false);a=login("12345","secret");f(action,"b","state",int.class).setInt(a,state);io.getField("failSend").setBoolean(null,true);
            f(action,"c","cookie",byte[].class).set(a,new byte[]{1,2,3});
            step("send-failure-"+state,a,state==2?snac(23,7,0,new byte[]{0,1,42}):hello());
        }
        for(boolean cache:new boolean[]{false,true})for(int type:new int[]{0,25,27,99}){
            reset(false);bool(169,cache);a=login("12345","secret");f(action,"b","state",int.class).setInt(a,7);
            step("cached-"+cache+"-"+type,a,snac(19,6,1,roster(record("54321",4,17,type,tlv(0x6d,new byte[]{2})))));
        }
        for(int sub:new int[]{65,66,99})for(int length:new int[]{0,1,13,14,17}){
            reset(false);a=login("12345","secret");f(action,"b","state",int.class).setInt(a,9);
            byte[] data=new byte[length];
            step("offline-"+sub+"-"+length,a,snac(21,3,0,join(word(1),word(length+10),new byte[]{(byte)(length+8),0,57,48,0,0,(byte)sub,0,0,0},data)));
        }
    }
    static void exercise()throws Exception{
        owner=m(icq,"a","getIcq",icq).invoke(null);storage();
        for(String credentials:new String[]{"", "12345"})for(int failures:new int[]{0,1,3})for(int age:new int[]{0,30000,30001}){
            reset(false);string(10,"3");Object a=login(credentials,"secret");io.getField("failures").setInt(null,failures);io.getField("now").setLong(null,1273665600000L+age);
            snapshot("init-"+credentials+"-"+failures+"-"+age,a,invoke(m(action,"a","init",void.class),a));
        }
        for(boolean md5:new boolean[]{false,true})for(boolean useHttp:new boolean[]{false,true}){
            reset(useHttp);bool(144,md5);Object a=login("12345","secret");String label="flow-"+md5+"-"+useHttp;
            snapshot(label+"-init",a,invoke(m(action,"a","init",void.class),a));step(label+"-hello",a,hello());
            if(md5)step(label+"-challenge",a,snac(23,7,0,join(word(4),new byte[]{1,2,3,4})));
            byte[] cookie=join(tlv(1,"12345".getBytes("US-ASCII")),tlv(5,"bos.invalid:5190".getBytes("US-ASCII")),tlv(6,new byte[]{1,4,9,16}));
            step(label+"-cookie",a,md5?snac(23,3,0,cookie):flap(4,cookie));if(md5)step(label+"-goodbye",a,flap(4,empty));
            step(label+"-bos",a,hello());step(label+"-families",a,snac(1,3,0,empty));step(label+"-roster-request",a,snac(1,24,0,empty));
            byte[] data=join(tlv(0x131,"Fixture".getBytes("UTF-8")),tlv(0x6d,new byte[]{2,4}),tlv(0x15c,empty),tlv(0x15d,new byte[]{6}),tlv(0x7777,new byte[]{8}),tlv(0x66,empty));
            step(label+"-partial",a,snac(19,6,1,roster(record("Friends",4,0,1,empty),record("54321",4,17,0,data),record("54321",0,31,2,empty))));
            step(label+"-final",a,snac(19,6,0,roster(record("54321",0,32,3,empty),record("54321",0,33,14,empty),record("",0,45,4,tlv(0xca,new byte[]{4})))));
            step(label+"-ready",a,snac(1,15,0,empty));step(label+"-offline-error",a,snac(21,1,0,empty));
        }
        for(boolean md5:new boolean[]{false,true})for(int code:new int[]{0,1,4,5,7,8,21,22,24,29,99}){
            reset(false);bool(144,md5);Object a=login("12345","secret");f(action,"b","state",int.class).setInt(a,3);
            step("auth-error-"+md5+"-"+code,a,md5?snac(23,3,0,tlv(8,word(code))):flap(4,join(tlv(8,word(code)),tlv(4,empty))));
        }
        byte[] valid=roster(record("54321",4,17,0,tlv(0x6d,new byte[]{1,2,3})));
        for(int length=0;length<valid.length;length++){
            reset(false);Object a=login("12345","secret");f(action,"b","state",int.class).setInt(a,7);
            step("truncated-roster-"+length,a,snac(19,6,0,Arrays.copyOf(valid,length)));
        }
        for(int state=-1;state<=10;state++){
            reset(false);Object a=login("12345","secret");f(action,"b","state",int.class).setInt(a,state);
            row("progress-"+state+":"+m(action,"a","getProgress",int.class).invoke(a)+":"+m(action,"a","getProgressMsg",String.class).invoke(a));
            step("unrelated-"+state,a,snac(9,99,0,empty));
        }
        extraLogin();statuses();
    }
    public static void main(String[] args){try{run(args);System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
    static Class<?> load(String a,String b)throws Exception{return Class.forName(n(a,b),true,loader);}
    static void run(String[] args)throws Exception{
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);
        Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet();loader=midlet.getClass().getClassLoader();
        icq=load("r","jimm.comm.Icq");connection=load("ap","jimm.comm.Icq$Connection");options=load("cj","jimm.Options");error=load("bv","jimm.JimmException");
        action=load("n","jimm.comm.ConnectAction");contact=load("z","jimm.ContactItem");list=load("m","jimm.ContactList");item=load("bs","jimm.ContactListItem");
        update=load("ct","jimm.comm.UpdateContactListAction");packet=load("an","jimm.comm.Packet");http=load("ay","jimm.comm.Icq$HTTPConnection");io=load("LoginIO","LoginIO");
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));final Throwable[] failure=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){failure[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Login probe timeout");out.close();if(failure[0]!=null)throw new AssertionError("Login probe failed",failure[0]);
        System.out.println("PASS probe: "+observations+" login/roster observations");
    }
}
