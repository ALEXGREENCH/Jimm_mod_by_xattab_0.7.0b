import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.rms.*;

/** Real birthday dates, contact updates, lazy/partial RMS reads, saves and deterministic worker run. */
public class BirthdayProbe extends FileTransferProbe {
    static Class<?> birthday,timer,util,spy;static int dates,saves,loaded,updates;
    static String invoke(Method m,Object target,Object...args)throws Exception{try{Object v=m.invoke(target,args);return v instanceof int[]?Arrays.toString((int[])v):String.valueOf(v);}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken birthday fixture",e.getCause());return "exception:"+e.getCause().getClass().getName();}}
    static Vector vector(int n)throws Exception{return (Vector)f(birthday,new String[]{"a","b","c"}[n],"a"+n,Vector.class).get(null);}
    static void vectors()throws Exception{for(int i=0;i<3;i++)vector(i).clear();}
    static long time(int year,int month,int day,int hour,int min,int sec)throws Exception{return (Long)m(util,"a","createLongTime",long.class,int.class,int.class,int.class,int.class,int.class,int.class).invoke(null,year,month,day,hour,min,sec);}
    static void today(long t)throws Exception{f(timer,"a","currData",long.class).setLong(null,t);}
    static void seed(String uin,Object day,Object month)throws Exception{vector(0).add(uin);vector(1).add(day);vector(2).add(month);}
    static void clearRms()throws Exception{try{RecordStore.deleteRecordStore("birthday");}catch(RecordStoreNotFoundException ignored){}}
    static void record(byte[] bytes,boolean gap)throws Exception{clearRms();if(bytes==null)return;RecordStore rs=RecordStore.openRecordStore("birthday",true);rs.addRecord(bytes,0,bytes.length);rs.addRecord(new byte[0],0,0);if(gap){rs.deleteRecord(1);rs.addRecord(new byte[]{99},0,1);}rs.closeRecordStore();}
    static byte[] encoded(int count)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);for(int i=0;i<count;i++){d.writeByte(i*3);d.writeInt(12345+i);d.writeByte(i*3+1);d.writeInt(12+i);d.writeByte(i*3+2);d.writeInt(5);}return b.toByteArray();}
    static String rms()throws Exception{
        try{RecordStore rs=RecordStore.openRecordStore("birthday",false);String s="records:"+rs.getNumRecords()+"/next:"+rs.getNextRecordID();for(int i=1;i<rs.getNextRecordID();i++){try{s+="/"+i+":"+b64(rs.getRecord(i));}catch(InvalidRecordIDException e){s+="/"+i+":missing";}}rs.closeRecordStore();return s;}catch(RecordStoreNotFoundException e){return "missing";}
    }
    static void snapshot(String label,String result,boolean withRms)throws Exception{
        String s=label+":"+result+"/table:"+vector(0)+":"+vector(1)+":"+vector(2)+"/bdate:"+f(birthday,"a","bData1",long.class).getLong(null)+"/current:"+f(timer,"a","currData",long.class).getLong(null);
        Vector contacts=(Vector)f(list,"a","cItems",Vector.class).get(null);for(Object c:contacts)s+="/contact:"+m(contact,"a","getStringValue",String.class,int.class).invoke(c,0)+":"+m(contact,"b","getIntValue",int.class,int.class).invoke(c,196);
        s+="/sleep:"+spy.getField("sleeps").getInt(null)+"/starts:"+spy.getField("starts").getInt(null)+"/io:"+spy.getField("log").get(null);if(withRms)s+="/rms:"+rms();row(s);spy.getField("log").set(null,new StringBuffer());
    }
    static void fresh()throws Exception{reset(false);clearRms();vectors();spy.getMethod("reset").invoke(null);contactItem=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);f(birthday,"a","bData1",long.class).setLong(null,0);today(time(2010,5,12,0,0,0));}
    static void dates()throws Exception{
        fresh();seed("12345","12","5");Method check=m(birthday,"a","checkDatacurrData",int.class,String.class),convert=m(util,"a","createDate",int[].class,long.class);
        for(int year:new int[]{1968,1969,1970,1999,2000,2001,2004,2010,2012,2099,2100,2104})for(int month=1;month<=12;month++)for(int day:new int[]{1,27,28,29,30,31}){
            long date=time(year,month,day,0,0,0);vector(1).set(0,String.valueOf(day));vector(2).set(0,String.valueOf(month));for(long delta:new long[]{-2*86400,-86400,-1,0,1,86400,2*86400}){today(date+delta);row("date-"+year+"-"+month+"-"+day+"-"+delta+":"+invoke(check,null,"12345")+"/bdate:"+f(birthday,"a","bData1",long.class).getLong(null)+"/calendar:"+invoke(convert,null,date+delta));dates++;}
        }
        for(int day:new int[]{Integer.MIN_VALUE,-1,0,1,28,29,31,32,365,Integer.MAX_VALUE})for(int month:new int[]{Integer.MIN_VALUE,-1,0,1,2,12,13,14,Integer.MAX_VALUE}){today(time(2010,5,12,0,0,0));vector(1).set(0,String.valueOf(day));vector(2).set(0,String.valueOf(month));row("invalid-date-"+day+"-"+month+":"+invoke(check,null,"12345")+"/bdate:"+f(birthday,"a","bData1",long.class).getLong(null));dates++;}
        for(Object day:new Object[]{null,"","x",Integer.valueOf(12),"2147483648"})for(Object month:new Object[]{null,"","x",Integer.valueOf(5),"2147483648"}){vector(1).set(0,day);vector(2).set(0,month);row("bad-values:"+day+":"+month+":"+invoke(check,null,"12345"));}
        fresh();seed("12345","12","5");for(String uin:new String[]{null,"","missing","12345"})row("lookup-"+uin+":"+invoke(check,null,uin));for(int n=1;n<3;n++){vector(n).clear();row("missing-vector-"+n+":"+invoke(check,null,"12345"));}
    }
    static void records()throws Exception{
        Method load=m(birthday,"b","load",void.class),save=m(birthday,"c","save",void.class);byte[] full=encoded(3);
        for(int size=-1;size<=full.length;size++)for(boolean gap:new boolean[]{false,true}){fresh();record(size<0?null:Arrays.copyOf(full,size),gap);snapshot("load-"+size+"-"+gap,invoke(load,null),true);loaded++;snapshot("reload-"+size+"-"+gap,invoke(load,null),true);loaded++;}
        for(int count:new int[]{0,1,2,85,86,87,100,256})for(boolean gap:new boolean[]{false,true}){
            fresh();record(encoded(1),gap);for(int i=0;i<count;i++)seed(String.valueOf(12345+i),String.valueOf(i%31+1),String.valueOf(i%12+1));snapshot("save-"+count+"-"+gap,invoke(save,null),true);saves++;vectors();snapshot("save-reload-"+count+"-"+gap,invoke(load,null),true);loaded++;
        }
        for(Object uin:new Object[]{null,"","not-number","2147483648",Integer.valueOf(12345)})for(int malformed=0;malformed<3;malformed++){fresh();seed("12345","12","5");vector(malformed).set(0,uin);snapshot("bad-save-"+malformed+"-"+uin,invoke(save,null),true);saves++;}
        for(int n=0;n<3;n++){fresh();seed("12345","12","5");vector(n).clear();snapshot("save-short-vector-"+n,invoke(save,null),true);saves++;}
        fresh();record(full,false);snapshot("lazy-check",invoke(m(birthday,"a","checkDatacurrData",int.class,String.class),null,"12345"),true);vector(0).clear();snapshot("lazy-misaligned",invoke(m(birthday,"a","checkDatacurrData",int.class,String.class),null,"12345"),true);
    }
    static void contacts()throws Exception{
        Method add=m(birthday,"a","additemB",void.class,String.class,int.class,int.class),del=m(birthday,"a","deleteBitem",void.class,String.class);
        for(boolean known:new boolean[]{false,true})for(int flag:new int[]{0,8,32,40})for(int day:new int[]{-1,0,12,32})for(int month:new int[]{-1,0,5,13}){
            fresh();m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(contactItem,8,(flag&8)!=0);m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(contactItem,32,(flag&32)!=0);if(known)((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);snapshot("add-"+known+"-"+flag+"-"+day+"-"+month,invoke(add,null,"12345",day,month),true);updates++;
        }
        for(String uin:new String[]{null,"","12345","other","not-number"}){fresh();((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);seed("12345","12","5");snapshot("delete-"+uin,invoke(del,null,uin),true);}
        fresh();((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);seed("12345","12","5");seed("12345","13","5");snapshot("delete-first-duplicate",invoke(del,null,"12345"),true);snapshot("replace",invoke(add,null,"12345",14,5),true);
    }
    static void workers()throws Exception{
        for(int count:new int[]{0,1,3})for(int failure=0;failure<3;failure++)for(boolean launch:new boolean[]{false,true})for(int hour:new int[]{0,12,23})for(int gmt:new int[]{-12,0,3,14})for(int local:new int[]{-12,0,3,14}){
            fresh();io.getField("now").setLong(null,1273622400000L+hour*3600000L);integer(87,gmt);integer(90,local);spy.getField("sleepFailure").setInt(null,failure);
            Vector contacts=(Vector)f(list,"a","cItems",Vector.class).get(null);for(int i=0;i<count;i++){Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17+i,4,String.valueOf(12345+i),"Contact"+i,false,true);contacts.add(c);seed(String.valueOf(12345+i),String.valueOf(12+i),"5");}
            snapshot("worker-"+count+"-"+failure+"-"+launch+"-"+hour+"-"+gmt+"-"+local,launch?invoke(m(birthday,"a","refreshBday",void.class),null):invoke(m(birthday,"run","run",void.class),f(birthday,"a","_this",birthday).get(null)),true);
        }
    }
    static void exerciseBirthday()throws Exception{
        setupFiles();birthday=load("cs","jimm.util.NoticeOnBirthDay");timer=load("at","jimm.TimerTasks");util=load("co","jimm.comm.Util");spy=load("BirthdayIO","BirthdayIO");dates();records();contacts();workers();if(dates<1000||loaded<100||saves<20||updates<100)throw new AssertionError("Birthday coverage guards");row("dates:"+dates+"/loads:"+loaded+"/saves:"+saves+"/updates:"+updates);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseBirthday();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
