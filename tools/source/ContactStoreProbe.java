import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute unchanged roster RMS loading with real memory stores and malformed wire records. */
public class ContactStoreProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;
    static Class<?> list,contact,group;static Field contacts,groups,time,count,version;static Method load,saveList;
    static int cases,loads,successes,loadedContacts,loadedGroups,observations,deletedStores,saves;
    static Class<?> type(String key)throws Exception{return Class.forName(spec.getProperty(key),true,loader);}
    static Field field(Class<?> c,String key,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(key),t);}
    static String b64(byte[] v){return v==null?"null":Base64.getEncoder().encodeToString(v);}
    static void row(String s){out.println(s);observations++;}
    static byte[] bytes(int mode,int id,int flags,int uin,String name,int rosterLength,byte[] data)throws Exception {
        ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);d.writeByte(mode);d.writeInt(id);
        if(mode==0){d.writeByte(flags);d.writeInt(uin);}d.writeUTF(name);
        if(mode==0){d.writeInt(Integer.MIN_VALUE);d.writeInt(0x76543210);d.writeInt(-1);d.writeShort(rosterLength);d.write(data);}return b.toByteArray();
    }
    static byte[] version(String value)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();new DataOutputStream(b).writeUTF(value);return b.toByteArray();}
    static byte[] meta(int stamp,int number)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);d.writeInt(stamp);d.writeShort(number);return b.toByteArray();}
    static byte[] join(byte[]...parts)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();for(byte[] p:parts)b.write(p);return b.toByteArray();}
    static String snapshot()throws Exception {
        Vector cv=(Vector)contacts.get(null),gv=(Vector)groups.get(null);loadedContacts+=cv.size();loadedGroups+=gv.size();
        StringBuilder b=new StringBuilder(time.getInt(null)+":"+count.getInt(null)+":contacts="+cv.size());
        for(Object c:cv)b.append('[').append(field(contact,"packed",int.class).getInt(c)).append(':').append(field(contact,"flags",int.class).getInt(c)).append(':').append(field(contact,"uin",int.class).getInt(c)).append(':').append(b64(((String)field(contact,"name",String.class).get(c)).getBytes("UTF-8"))).append(':').append(field(contact,"privacy",int.class).getInt(c)).append(':').append(field(contact,"ignored",int.class).getInt(c)).append(':').append(b64((byte[])field(contact,"roster",byte[].class).get(c))).append(']');
        b.append(":groups=").append(gv.size());for(Object g:gv)b.append('[').append(field(group,"groupId",int.class).getInt(g)).append(':').append(b64(((String)field(group,"groupName",String.class).get(g)).getBytes("UTF-8"))).append(']');return b.toString();
    }
    static void clean()throws Exception{try{RecordStore.deleteRecordStore("contactlist");}catch(RecordStoreNotFoundException absent){}}
    static void exercise(String label,byte[][] records,int hole,String appVersion,boolean held)throws Exception {
        clean();RecordStore holding=null;
        if(records!=null){RecordStore store=RecordStore.openRecordStore("contactlist",true);for(byte[] record:records)store.addRecord(record,0,record.length);if(hole>0)store.deleteRecord(hole);store.closeRecordStore();if(held)holding=RecordStore.openRecordStore("contactlist",false);}
        version.set(null,appVersion);
        for(int repeat=0;repeat<2;repeat++) {
            Vector beforeC=new Vector(),beforeG=new Vector();beforeC.addElement("sentinel");beforeG.addElement("sentinel");contacts.set(null,beforeC);groups.set(null,beforeG);time.setInt(null,-731);count.setInt(null,65537);
            String outcome="ok";try{load.invoke(null);successes++;}catch(InvocationTargetException e){Throwable cause=e.getCause();if(cause instanceof LinkageError)throw(LinkageError)cause;outcome="exception:"+cause.getClass().getName();}
            row("load:"+label+":"+repeat+":"+outcome+":"+(contacts.get(null)==beforeC)+":"+(groups.get(null)==beforeG)+":"+beforeC.size()+":"+beforeG.size()+":"+snapshot());loads++;
        }
        if(holding!=null){String state;try{state=String.valueOf(holding.getNumRecords());}catch(RecordStoreNotOpenException closed){state="already-closed";}row("held:"+label+":"+state);
            try{holding.closeRecordStore();row("held-close:"+label+":closed");}catch(RecordStoreNotOpenException closed){row("held-close:"+label+":already-closed");}}
        if(records!=null)rms("input:"+label);
        if(appVersion!=null){saveList.invoke(null);saves++;rms("saved:"+label);}
        if(records!=null||appVersion!=null){RecordStore.deleteRecordStore("contactlist");deletedStores++;}
        row("cleanup:"+label+":"+Arrays.toString(RecordStore.listRecordStores()));cases++;
    }
    static void rms(String label)throws Exception {
        RecordStore store=RecordStore.openRecordStore("contactlist",false);StringBuilder b=new StringBuilder("rms:"+label+":"+store.getNumRecords()+":"+store.getNextRecordID());
        try{for(int id=1;id<store.getNextRecordID();id++){b.append('[').append(id).append(':');try{b.append(b64(store.getRecord(id)));}catch(InvalidRecordIDException missing){b.append("hole");}b.append(']');}}finally{store.closeRecordStore();}row(b.toString());
    }
    static void scenarios()throws Exception {
        byte[] v=version("0.7.0b"),m=meta(Integer.MIN_VALUE,65535),g=bytes(1,Integer.MAX_VALUE,0,0,"Group \u0413\u0440\u0443\u043f\u043f\u0430",0,new byte[0]);
        byte[] c=bytes(0,0x1234ffff,-1,Integer.MIN_VALUE,"Name \u0000\u0418\u043c\u044f",3,new byte[]{1,2,3});
        exercise("absent",null,-1,"0.7.0b",false);exercise("empty",new byte[0][],-1,"0.7.0b",false);
        for(String app:new String[]{null,"","0.6.0a","0.7.0b"})for(String stored:new String[]{"","0.6.0a","0.7.0b","\u0412\u0435\u0440\u0441\u0438\u044f"})exercise("version:"+app+":"+b64(stored.getBytes("UTF-8")),new byte[][]{version(stored),m,c},-1,app,false);
        for(int end=0;end<=v.length;end++)exercise("header-prefix:"+end,new byte[][]{Arrays.copyOf(v,end),m,c},-1,"0.7.0b",false);
        for(int stamp:new int[]{Integer.MIN_VALUE,-1,0,1,Integer.MAX_VALUE})for(int number:new int[]{0,1,32767,32768,65535,65536,-1})exercise("metadata:"+stamp+":"+number,new byte[][]{v,meta(stamp,number),join(g,c)},-1,"0.7.0b",false);
        for(int end=0;end<=m.length;end++)exercise("metadata-prefix:"+end,new byte[][]{v,Arrays.copyOf(m,end),c},-1,"0.7.0b",false);
        for(int packed:new int[]{Integer.MIN_VALUE,-1,0,0x1234ffff,Integer.MAX_VALUE})for(int flags:new int[]{-128,-1,0,1,2,8,15,127})for(int length:new int[]{-32768,-1,0,1,2,5,32767}) {
            byte[] wire=bytes(0,packed,flags,Integer.MAX_VALUE,"\u0418\u043c\u044f \u0000 \ud83d\ude00",length,new byte[]{9,8});
            exercise("contact:"+packed+":"+flags+":"+length,new byte[][]{v,m,join(g,wire)},-1,"0.7.0b",false);
        }
        for(int end=0;end<=c.length;end++)exercise("contact-prefix:"+end,new byte[][]{v,m,join(g,Arrays.copyOf(c,end))},-1,"0.7.0b",false);
        for(int end=0;end<=g.length;end++)exercise("group-prefix:"+end,new byte[][]{v,m,join(c,Arrays.copyOf(g,end))},-1,"0.7.0b",false);
        for(int hole:new int[]{-1,1,2,3,4})for(boolean held:new boolean[]{false,true})exercise("hole:"+hole+":"+held,new byte[][]{v,m,join(g,c),join(c,g)},hole,"0.7.0b",held);
        for(byte[] input:new byte[][]{{},{2},{-1,127,2},join(new byte[]{2,-1},c,g),join(c,new byte[]{-1,2},g),join(c,g,new byte[]{127,-1}),join(c,c,g,g)})exercise("batch:"+b64(input),new byte[][]{v,m,new byte[0],input},-1,"0.7.0b",false);
        for(int split:new int[]{0,1,c.length-1,c.length})exercise("record-split:"+split,new byte[][]{v,m,Arrays.copyOf(c,split),Arrays.copyOfRange(c,split,c.length),g},-1,"0.7.0b",false);
        if(successes<200||loadedContacts<200||loadedGroups<400)throw new AssertionError("Missing non-empty successful RMS paths: "+successes+"/"+loadedContacts+"/"+loadedGroups);
        row("coverage:"+cases+":"+loads+":"+successes+":"+loadedContacts+":"+loadedGroups+":"+deletedStores+":"+saves);
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();
        Class<?> options=type("options");Object[] table=new Object[256];for(int k=0;k<table.length;k++)table[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);field(options,"table",Object[].class).set(null,table);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));version=field(jimm,"version",String.class);
        list=type("list");contact=type("contact");group=type("group");contacts=field(list,"contacts",Vector.class);groups=field(list,"groups",Vector.class);time=field(list,"time",int.class);count=field(list,"count",int.class);
        load=PopupProbe.method(list,spec.getProperty("load"),void.class);saveList=PopupProbe.method(list,spec.getProperty("save"),void.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));scenarios();out.close();System.out.println("PASS contact RMS: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
