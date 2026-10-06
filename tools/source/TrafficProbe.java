import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/** Traffic screen styles, counters, persisted bytes, reload/reset and malformed RMS. */
public class TrafficProbe extends FileTransferProbe {
    static Class<?> controllerType,line,part;
    static Object trafficObject,screenController;
    static void set(String old,String name,int value)throws Exception{f(traffic,old,name,int.class).setInt(null,value);}
    static void values(int in,int out)throws Exception{
        set("a","allInTraffic",5120);set("b","allOutTraffic",8192);set("f","sessionInTraffic",in);set("g","sessionOutTraffic",out);set("c","savedCost",12345);set("h","costPerDaySum",0);
        f(traffic,"a","savedSince",Date.class).set(null,new Date(946684800000L));f(traffic,"b","lastTimeUsed",Date.class).set(null,new Date(1L));
        m(traffic,"a","getSessionTraffic",int.class).invoke(null);integer(72,1024);integer(70,1000);integer(71,7);string(6,"RUB");
    }
    static void state(String label)throws Exception{
        String s=label;for(String[] names:new String[][]{{"a","allInTraffic"},{"b","allOutTraffic"},{"d","all_traffic"},{"e","session_traffic"},{"f","sessionInTraffic"},{"g","sessionOutTraffic"},{"c","savedCost"},{"h","costPerDaySum"}})s+=":"+names[1]+"="+f(traffic,names[0],names[1],int.class).getInt(null);
        for(String[] names:new String[][]{{"a","savedSince"},{"b","lastTimeUsed"}})s+=":"+names[1]+"="+((Date)f(traffic,names[0],names[1],Date.class).get(null)).getTime();row(s);
    }
    static void screen(String label)throws Exception{
        Object list=f(controllerType,"a","trafficTextList",text).get(screenController);String s=label+":caption="+enc(f(vl,"a","caption",String.class).get(list))+":font="+f(vl,"u","fontSize",int.class).getInt(list)+":menu="+menu(list);row(s);
        StringBuilder rows=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(list)){
            rows.append('[');for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))rows.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(';');rows.append(']');
        }row(label+":rows="+rows);
    }
    static void refresh()throws Exception{m(controllerType,"a","update",void.class,boolean.class).invoke(screenController,true);}
    static void fresh()throws Exception{
        trafficObject=instance(traffic,new Class<?>[0]);screenController=f(traffic,"a","trafficScreen",controllerType).get(null);
    }
    static void record(byte[] bytes)throws Exception{
        RecordStore store=RecordStore.openRecordStore("traffic",true);while(store.getNumRecords()<4)store.addRecord(null,0,0);store.setRecord(2,bytes,0,bytes.length);store.closeRecordStore();
    }
    static void persisted(String label)throws Exception{
        RecordStore store=RecordStore.openRecordStore("traffic",false);String s=label+":records="+store.getNumRecords();for(int i=1;i<=store.getNumRecords();i++)s+=":"+i+"="+b64(store.getRecord(i));store.closeRecordStore();row(s);
    }
    static byte[] saved(int in,int out,long since,long last,int cost)throws Exception{
        ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);d.writeInt(in);d.writeInt(out);d.writeLong(since);d.writeLong(last);d.writeInt(cost);return b.toByteArray();
    }
    static void events()throws Exception{for(Object e:actions()){Object[] item=(Object[])e;row("traffic-event:"+item[0]+":"+Arrays.toString((Object[])item[1]));}actions().clear();}
    static void displays()throws Exception{
        for(int style:new int[]{0,2})for(int amount:new int[]{0,1,1023,1024,2048,65536}){
            f(options,"g","fontStyle",int.class).setInt(null,style);fresh();values(amount,amount*2);refresh();String label="screen-"+style+"-"+amount;screen(label);state(label);
            refresh();screen(label+"-repeat");state(label+"-repeat");
        }
        fresh();values(1024,2048);actions().clear();m(traffic,"a","addInTraffic",void.class,int.class).invoke(null,17);m(traffic,"b","addOutTraffic",void.class,int.class).invoke(null,31);state("added");events();
        Command back=(Command)f(controllerType,"b","okCommand",Command.class).get(screenController);command(screenController,back);events();
    }
    static void storage()throws Exception{
        for(int amount:new int[]{0,1,1024,12345}){
            fresh();values(amount,amount*3);String label="store-"+amount;row(label+":"+invoke(m(traffic,"a","save",void.class),null));state(label);persisted(label);
            fresh();state(label+"-reload");refresh();screen(label+"-reload");
            values(amount,amount*3);command(screenController,(Command)f(controllerType,"a","resetCommand",Command.class).get(screenController));state(label+"-reset");persisted(label+"-reset");
        }
        byte[] full=saved(4096,8192,946684800000L,1273665600000L,12345);
        for(int len=0;len<=full.length;len++){record(Arrays.copyOf(full,len));fresh();state("load-prefix-"+len);}
        record(join(full,new byte[]{1,2,3}));fresh();state("load-trailing");
    }
    static void dates()throws Exception{
        for(long now:new long[]{0,86400000L,951782400000L,1273665600000L,1293840000000L})for(long last:new long[]{1L,now,now-86400000L}){
            values(1024,0);io.getField("now").setLong(null,now);f(traffic,"a","savedSince",Date.class).set(null,new Date(now));f(traffic,"b","lastTimeUsed",Date.class).set(null,new Date(last));
            String label="date-"+now+"-"+last;for(int type:new int[]{4,11,12,13,21,22,23,99})row(label+"-"+type+":"+m(traffic,"a","getTrafficString",String.class,int.class).invoke(null,type));state(label);
        }
    }
    static void exerciseTraffic()throws Exception{
        setupFiles();controllerType=load("a","jimm.Traffic$TrafficScreen");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");
        displays();storage();dates();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTraffic();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
