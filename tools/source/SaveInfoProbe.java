import java.io.*;
import java.lang.reflect.*;
import java.security.MessageDigest;
import java.util.*;

/** Real profile save serialization, array aliases, selected accounts, acknowledgments and state. */
public class SaveInfoProbe extends FileTransferProbe {
    static Class<?> saver,util,from,spy,base;
    static Field dataField,dateField,counterField,errorField,categoriesField,point;
    static int initializations,forwards,accepted,sendAttempts,successfulSends,notifications;
    static String result(Method method,Object value,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(value,args));}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof Error&&!t.getClass().getName().equals("SaveInfoIO$FaultError"))throw new AssertionError("Broken save-info fixture",t);
            if(error.isInstance(t))return "error:"+f(error,"a","_ErrCode",int.class).getInt(t)+":"+PasswordProbe.units(t.getMessage());return "exception:"+t.getClass().getName();}
    }
    static String digest(String[] values)throws Exception{
        if(values==null)return "null";ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);out.writeInt(values.length);
        for(String value:values){out.writeInt(value==null?-1:value.length());if(value!=null)for(int i=0;i<value.length();i++)out.writeChar(value.charAt(i));}
        return b64(MessageDigest.getInstance("SHA-256").digest(bytes.toByteArray()));
    }
    static String[] valid()throws Exception{
        String[] values=new String[48];Arrays.fill(values,"");values[1]="Nickname";values[38]="First";values[39]="Last";values[3]="x@example.test";values[13]="12.05.1990";
        values[11]=(String)m(util,"c","genderToString",String.class,int.class).invoke(null,1);for(int i=44;i<48;i++)values[i]="interest "+i;return values;
    }
    static Object make(String[] values)throws Exception{return instance(saver,new Class<?>[]{String[].class},(Object)values);}
    static void fresh()throws Exception{reset(false);spy.getMethod("reset").invoke(null);integer(86,0);string(0,"111");string(14,"222");string(15,"333");bool(133,true);categoriesField.set(null,new int[]{100,101,102,103});point.setBoolean(null,false);}
    static void snap(String label,Object value,String[] supplied,String outcome)throws Exception{
        Date date=(Date)dateField.get(value);StringBuilder b=new StringBuilder(label).append(':').append(outcome).append("/same-data:").append(dataField.get(value)==supplied)
            .append("/data:").append(digest((String[])dataField.get(value))).append("/input:").append(digest(supplied))
            .append("/counter:").append(counterField.getInt(value)).append("/errors:").append(errorField.getInt(value)).append("/date:").append(date==null?"null":date.getTime())
            .append("/done:").append(result(m(saver,"a","isCompleted",boolean.class),value)).append("/error:").append(result(m(saver,"b","isError",boolean.class),value))
            .append("/progress:").append(result(m(saver,"a","getProgress",int.class),value)).append("/point:").append(point.getBoolean(null))
            .append("/categories:").append(Arrays.toString((int[])categoriesField.get(null)));
        Vector sent=(Vector)io.getField("packets").get(null);for(Object packetValue:sent){sendAttempts++;if(outcome.equals("null"))successfulSends++;b.append("/packet:");try{b.append(b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(packetValue)));}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken packet serializer",e.getCause());b.append("serialize-exception:").append(e.getCause().getClass().getName());}}sent.clear();
        int calls=spy.getField("notifications").getInt(null);notifications+=calls;spy.getField("notifications").setInt(null,0);b.append("/notifications:").append(calls);row(b.toString());
    }
    static void init(String label,Object value,String[] supplied)throws Exception{String outcome=result(m(saver,"a","init",void.class),value);initializations++;snap(label,value,supplied,outcome);}
    static void step(String label,Object value,Object received)throws Exception{String outcome=result(m(saver,"a","forward",boolean.class,packet),value,received);forwards++;if(outcome.equals("true"))accepted++;snap(label,value,(String[])dataField.get(value),outcome);}
    static Object incoming(int sub,byte[] bytes)throws Exception{return instance(from,new Class<?>[]{int.class,long.class,int.class,int.class,String.class,int.class,byte[].class,byte[].class},7,0L,0,11,"12345",sub,new byte[0],bytes);}
    static byte[] ack(int type,int flag){return new byte[]{(byte)type,(byte)(type>>8),(byte)flag};}
    static String repeated(int n){char[] chars=new char[n];Arrays.fill(chars,'X');return new String(chars);}
    static void initCases()throws Exception{
        for(int length=-1;length<=50;length++)for(int mode=0;mode<3;mode++){
            fresh();String[] values=length<0?null:Arrays.copyOf(valid(),length);if(values!=null&&mode==1)Arrays.fill(values,null);if(values!=null&&mode==2)Arrays.fill(values,"");Object value=make(values);
            snap("constructor-"+length+'-'+mode,value,values,"new");init("array-"+length+'-'+mode,value,values);
        }
        String[] texts={null,"","ASCII","\u041f\u0440\u0438\u0432\u0435\u0442 \u010d","a\u0000b\r\nc","\ud83d\ude00\u4e2d","\ud800x\udfff",repeated(256),repeated(1024)};
        for(boolean cp:new boolean[]{false,true})for(int field:new int[]{1,38,39,12,22,4,44,45,46,47,3,11})for(int t=0;t<texts.length;t++){
            fresh();bool(133,cp);String[] values=valid();values[field]=texts[t];Object value=make(values);init("field-"+cp+'-'+field+'-'+t,value,values);
            values[field]="changed after construction";io.getField("now").setLong(null,12345L);init("alias-field-"+cp+'-'+field+'-'+t,value,values);
        }
        for(String birthday:new String[]{null,"",".","..","1.2","1.2.3","1.2.3.4",".2.3","1..3","1.2.","bad.date.text"," 1.2.3","+1.-2.65536","-1.0.0","2147483647.-2147483648.65535","2147483648.1.1","1.1.2147483648","12.05.2010","29.02.2009","\u0661.2.3"}){
            fresh();String[] values=valid();values[13]=birthday;Object value=make(values);init("birthday-"+PasswordProbe.units(birthday),value,values);
        }
        for(int length=-1;length<=5;length++)for(int category:new int[]{Integer.MIN_VALUE,-1,0,1,100,65535,65536,Integer.MAX_VALUE}){
            fresh();int[] categories=length<0?null:new int[length];if(categories!=null)Arrays.fill(categories,category);categoriesField.set(null,categories);String[] values=valid();Object value=make(values);init("categories-"+length+'-'+category,value,values);
        }
        String female=(String)m(util,"c","genderToString",String.class,int.class).invoke(null,1),male=(String)m(util,"c","genderToString",String.class,int.class).invoke(null,2);
        for(int kind=0;kind<6;kind++){fresh();String[] values=valid();values[11]=kind==0?female:kind==1?new String(female):kind==2?male:kind==3?new String(male):kind==4?null:"";Object value=make(values);init("gender-identity-"+kind,value,values);}
        for(int size:new int[]{32766,32767,32768,65534,65535,65536}){fresh();String[] values=valid();values[22]=repeated(size);Object value=make(values);init("word-length-overflow-"+size,value,values);}
    }
    static void accountsAndFailures()throws Exception{
        for(int account:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,Integer.MAX_VALUE}){fresh();integer(86,account);String[] values=valid();Object value=make(values);init("distinct-account-"+account,value,values);}
        for(int account:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,Integer.MAX_VALUE})for(String uin:new String[]{"","0","-1","12345","2147483648","4294967295","9223372036854775807","9223372036854775808","bad"})for(int failure=0;failure<3;failure++){
            fresh();string(0,uin);string(14,uin);string(15,uin);integer(86,account);String[] values=valid();Object value=make(values);dateField.set(value,new Date(123L));counterField.setInt(value,7);errorField.setInt(value,5);
            io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);init("account-"+account+'-'+uin+'-'+failure,value,values);
            reset(false);integer(86,0);string(0,"456");io.getField("now").setLong(null,999L);init("account-retry-"+account+'-'+uin+'-'+failure,value,values);
        }
        fresh();String[] values=valid();Object value=make(values);init("valid-before-null-setting",value,values);((Object[])f(options,"a","options",Object[].class).get(null))[0]=null;init("null-selected-uin",value,values);
    }
    static void replies()throws Exception{
        for(int type:new int[]{0,3130,3134,3135,3136,65535})for(int flag=0;flag<256;flag++)for(int size=0;size<=4;size++){
            fresh();Object value=make(valid());dateField.set(value,new Date(1000L));io.getField("now").setLong(null,6000L);byte[] bytes=Arrays.copyOf(ack(type,flag),size);Object p=incoming(2010,bytes);
            step("ack-"+type+'-'+flag+'-'+size,value,p);if(type==3135&&size>=3)step("ack-repeat-"+flag+'-'+size,value,p);
        }
        for(int sub:new int[]{Integer.MIN_VALUE,-1,0,65,66,2000,2010,65535,65536,Integer.MAX_VALUE}){fresh();Object value=make(valid());step("subcommand-"+sub,value,incoming(sub,ack(3135,10)));}
        fresh();Object value=make(valid());step("null-packet",value,null);step("hello-packet",value,instance(load("h","jimm.comm.ConnectPacket"),new Class<?>[0]));step("ordinary-snac",value,instance(load("ak","jimm.comm.SnacPacket"),new Class<?>[]{int.class,int.class,long.class,byte[].class,byte[].class},1,99,0L,new byte[0],ack(3135,10)));step("null-data",value,incoming(2010,null));
        Random random=new Random(7002010L);for(int k=0;k<500;k++){byte[] bytes=new byte[random.nextInt(33)];random.nextBytes(bytes);Object p=incoming(2010,bytes);step("random-"+k,value,p);}
        int[] extremes={Integer.MIN_VALUE,-1,0,1,2,Integer.MAX_VALUE};for(int count:extremes)for(int errors:extremes)for(int flag:new int[]{0,10,255}){
            fresh();value=make(valid());counterField.setInt(value,count);errorField.setInt(value,errors);for(int repeat=0;repeat<3;repeat++)step("overflow-"+count+'-'+errors+'-'+flag+'-'+repeat,value,incoming(2010,ack(3135,flag)));
        }
    }
    static void timersAndEvents()throws Exception{
        for(long initial:new long[]{Long.MIN_VALUE,-1,0,1,1000,Long.MAX_VALUE-5000,Long.MAX_VALUE-4999,Long.MAX_VALUE})for(int errors:new int[]{Integer.MIN_VALUE,-1,0,1,Integer.MAX_VALUE}){
            fresh();String[] values=valid();Object value=make(values);io.getField("now").setLong(null,initial);init("clock-init-"+initial+'-'+errors,value,values);errorField.setInt(value,errors);
            for(long delta:new long[]{-1,0,4999,5000,5001,Long.MIN_VALUE,Long.MAX_VALUE}){io.getField("now").setLong(null,initial+delta);snap("clock-tick-"+initial+'-'+errors+'-'+delta,value,values,"tick");}
        }
        for(int errors:new int[]{Integer.MIN_VALUE,-1,0,1,Integer.MAX_VALUE}){fresh();Object value=make(valid());errorField.setInt(value,errors);snap("null-date-first-"+errors,value,(String[])dataField.get(value),"seed");}
        for(int event:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,4,Integer.MAX_VALUE})for(int failure=0;failure<4;failure++)for(boolean old:new boolean[]{false,true}){
            fresh();Object value=make(valid());point.setBoolean(null,old);spy.getField("failure").setInt(null,failure);for(int repeat=0;repeat<3;repeat++)snap("event-"+event+'-'+failure+'-'+old+'-'+repeat,value,(String[])dataField.get(value),result(m(saver,"a","onEvent",void.class,int.class),value,event));
        }
    }
    static void exerciseSave()throws Exception{
        owner=m(icq,"a","getIcq",icq).invoke(null);saver=load("as","jimm.comm.SaveInfoAction");util=load("co","jimm.comm.Util");from=load("ck","jimm.comm.FromIcqSrvPacket");spy=load("SaveInfoIO","SaveInfoIO");request=load("ce","jimm.comm.RequestInfoAction");base=load("aa","jimm.comm.Action");
        dataField=f(saver,"a","strData",String[].class);dateField=f(saver,"a","init",Date.class);counterField=f(saver,"a","packetCounter",int.class);errorField=f(saver,"b","errorCounter",int.class);categoriesField=f(request,"a","indexCategories",int[].class);point=f(icq,"a","setPoint",boolean.class);
        initCases();accountsAndFailures();replies();timersAndEvents();fresh();if(initializations<500||forwards<5000||accepted<10||successfulSends<500||notifications<20)throw new AssertionError("SaveInfo coverage guards: "+initializations+"/"+forwards+"/"+accepted+"/"+successfulSends+"/"+notifications);
        row("initializations:"+initializations+"/forwards:"+forwards+"/accepted:"+accepted+"/send-attempts:"+sendAttempts+"/successful-sends:"+successfulSends+"/notifications:"+notifications);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseSave();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
