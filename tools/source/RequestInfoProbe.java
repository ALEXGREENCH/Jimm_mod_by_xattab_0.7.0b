import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/** Real profile reply parsing, timeout, own-account flags, rename and birthday integration. */
public class RequestInfoProbe extends BirthdayProbe {
    static Class<?> notifications;
    static int initializations,forwards,accepted,deliveries;
    static String invoke(Method method,Object target,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(target,args));}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof Error)throw new AssertionError("Broken request-info fixture",t);
            if(error.isInstance(t))return "error:"+f(error,"a","_ErrCode",int.class).getInt(t)+":"+units(t.getMessage());return "exception:"+t.getClass().getName();}
    }
    static String units(String text){if(text==null)return "null";StringBuilder b=new StringBuilder();for(int i=0;i<text.length();i++){String s=Integer.toHexString(text.charAt(i));for(int j=s.length();j<4;j++)b.append('0');b.append(s);}return b.toString();}
    static Object make(String uin,String nick)throws Exception{return instance(request,new Class<?>[]{String.class,String.class},uin,nick);}
    static String[] table(Object a)throws Exception{return (String[])f(request,"b","strData",String[].class).get(a);}
    static String localized(String key)throws Exception{return key==null?"null":units((String)m(resource,"a","getString",String.class,String.class).invoke(null,key));}
    static String data(String[] values)throws Exception{if(values==null)return "null";StringBuilder b=new StringBuilder();for(int i=0;i<values.length;i++)b.append(i).append('=').append(i>=40&&i<44?localized(values[i]):units(values[i])).append(';');return b.toString();}
    static Vector eventsOf(String name)throws Exception{return (Vector)notifications.getField(name).get(null);}
    static void freshRequest(boolean cp)throws Exception{
        fresh();notifications.getMethod("reset").invoke(null);actions().clear();bool(133,cp);string(0,"54321");integer(86,0);
        f(request,"b","StartMainRequestInfo",boolean.class).setBoolean(null,false);f(request,"a","indexCategories",int[].class).set(null,new int[]{99,100,101,102});
        f(icq,"a","setPoint",boolean.class).setBoolean(null,false);f(icq,"a","myNick",String.class).set(null,"Old nickname");bool(160,false);bool(183,false);
    }
    static void snap(String label,Object a,String result)throws Exception{
        StringBuilder b=new StringBuilder(label).append(':').append(result).append("/counter:").append(f(request,"a","packetCounter",int.class).getInt(a));
        for(String[] names:new String[][]{{"c","infoShown"},{"d","showInfoText"},{"e","notFound"}})b.append('/').append(names[1]).append(':').append(f(request,names[0],names[1],boolean.class).getBoolean(a));
        Date date=(Date)f(request,"a","init",Date.class).get(a);b.append("/init:").append(date==null?"null":date.getTime());
        b.append("/done:").append(invoke(m(request,"a","isCompleted",boolean.class),a)).append("/error:").append(invoke(m(request,"b","isError",boolean.class),a));
        b.append("/data:").append(data(table(a))).append("/bday:").append(units((String)f(request,"b","uin_bDay",String.class).get(a))).append(':').append(f(request,"b","day_bDay",int.class).getInt(a)).append(':').append(f(request,"c","month_bDay",int.class).getInt(a));
        b.append("/categories:").append(Arrays.toString((int[])f(request,"a","indexCategories",int[].class).get(null)));
        b.append("/own:").append(f(request,"b","StartMainRequestInfo",boolean.class).getBoolean(null)).append(':').append(f(icq,"a","setPoint",boolean.class).getBoolean(null)).append(':').append(units((String)f(icq,"a","myNick",String.class).get(null))).append(':').append(m(options,"a","getBoolean",boolean.class,int.class).invoke(null,160)).append(':').append(m(options,"a","getBoolean",boolean.class,int.class).invoke(null,183));
        for(Object c:(Vector)f(list,"a","cItems",Vector.class).get(null))b.append("/contact:").append(units((String)m(contact,"a","getStringValue",String.class,int.class).invoke(c,0))).append(':').append(units((String)f(contact,"a","name",String.class).get(c))).append(':').append(m(contact,"b","getIntValue",int.class,int.class).invoke(c,196));
        for(Object event:eventsOf("shown")){Object[] e=(Object[])event;b.append("/show:").append(e[0]).append(':').append(e[1]==table(a)).append(':').append(data((String[])e[1]));deliveries++;}eventsOf("shown").clear();
        for(Object event:eventsOf("changes")){Object[] e=(Object[])event;b.append("/changed:").append(e[0]==contactItem).append(':').append(e[1]).append(':').append(e[2]);}eventsOf("changes").clear();
        for(Object event:eventsOf("renamed")){String[] e=(String[])event;b.append("/chat-rename:").append(units(e[0])).append(':').append(units(e[1]));}eventsOf("renamed").clear();
        for(Object e:actions()){Object[] event=(Object[])e;b.append("/job:").append(event[0]);if(event[0].equals("action")){Object action=((Object[])event[1])[0];b.append(':').append(f(update,"b","action",int.class).getInt(action));}}actions().clear();
        Vector sent=(Vector)io.getField("packets").get(null);for(Object p:sent)b.append("/packet:").append(b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));sent.clear();
        b.append("/birthday:").append(vector(0)).append(':').append(vector(1)).append(':').append(vector(2)).append(':').append(rms());row(b.toString());
    }
    static byte[] az(String text,boolean cp)throws Exception{byte[] b=text.getBytes(cp?"windows-1251":"UTF-8");return join(le(b.length+1),b,new byte[]{0});}
    static byte[] general(String nick,boolean cp,int auth,int aware)throws Exception{
        ByteArrayOutputStream b=new ByteArrayOutputStream();for(String s:new String[]{nick,"First","Last","a@example.test","City","State","Phone","Fax","Address","Cell","Zip"})b.write(az(s,cp));b.write(le(1));b.write(new byte[]{7,(byte)auth,(byte)aware});return b.toByteArray();
    }
    static byte[] more(int age,int gender,int year,int month,int day,boolean cp)throws Exception{return join(le(age),new byte[]{(byte)gender},az("https://example.test",cp),le(year),new byte[]{(byte)month,(byte)day});}
    static byte[] work(boolean cp)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();for(String s:new String[]{"Work city","State","Phone","Fax","Address","Zip"})b.write(az(s,cp));b.write(le(1));for(String s:new String[]{"Company","Department","Position"})b.write(az(s,cp));return b.toByteArray();}
    static byte[] interests(int count,boolean cp)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();b.write(count);for(int i=0;i<Math.max(0,count);i++){b.write(le(100+i));b.write(az("Interest "+i,cp));}return b.toByteArray();}
    static Object reply(int type,int success,byte[] body)throws Exception{return meta(2010,join(le(type),new byte[]{(byte)success},body));}
    static void step(String label,Object a,Object p)throws Exception{String result=invoke(m(request,"a","forward",boolean.class,packet),a,p);forwards++;if(result.equals("true"))accepted++;snap(label,a,result);}
    static void initCases()throws Exception{
        for(String uin:new String[]{null,"","0","-1","12345","2147483648","4294967295","9223372036854775807","9223372036854775808","bad"})for(int failure=0;failure<3;failure++){
            freshRequest(true);Object a=make(uin,uin);snap("constructor-"+uin+'-'+failure,a,"new");io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);
            snap("init-"+uin+'-'+failure,a,invoke(m(request,"a","init",void.class),a));initializations++;
        }
        for(long now:new long[]{0,1,-1,1273665600000L,Long.MIN_VALUE,Long.MAX_VALUE,Long.MAX_VALUE-10000}){
            freshRequest(true);io.getField("now").setLong(null,now);Object a=make("12345","Known");snap("clock-init-"+now,a,invoke(m(request,"a","init",void.class),a));initializations++;
            for(long delta:new long[]{-1,0,9999,10000,10001}){io.getField("now").setLong(null,now+delta);snap("clock-"+now+'-'+delta,a,"tick");}
        }
    }
    static void replyCases()throws Exception{
        for(boolean cp:new boolean[]{false,true})for(String nick:new String[]{"","Nick","\u041f\u0440\u0438\u0432\u0435\u0442 \u010d","a\u0000b\r\nc"})for(boolean own:new boolean[]{false,true})for(int success:new int[]{0,10,255}){
            byte[][] bodies={general(nick,cp,0,1),work(cp),more(25,2,1990,5,12,cp),az("About \u0410\u010d",cp),interests(4,cp),new byte[0]};int[] types={200,210,220,230,240,250};
            for(int k=0;k<types.length;k++)for(int length=0;length<=bodies[k].length+3;length++){
                freshRequest(cp);Object a=make(own?"54321":"12345","Known");f(request,"b","uin_bDay",String.class).set(a,own?"54321":"12345");
                step("part-"+cp+'-'+units(nick)+'-'+own+'-'+success+'-'+types[k]+'-'+length,a,reply(types[k],success,Arrays.copyOf(bodies[k],length)));
            }
        }
        for(boolean own:new boolean[]{false,true})for(boolean initial:new boolean[]{false,true})for(int auth:new int[]{0,1,255})for(int aware:new int[]{0,1,255})for(String nick:new String[]{"","Nickname"}){
            freshRequest(true);Object a=make(own?"54321":"12345","Known");f(request,"b","StartMainRequestInfo",boolean.class).setBoolean(null,initial);step("own-"+own+'-'+initial+'-'+auth+'-'+aware+'-'+nick,a,reply(200,10,general(nick,true,auth,aware)));
        }
        for(int count:new int[]{-128,-1,0,1,4,5,8,127,255})for(boolean own:new boolean[]{false,true}){
            freshRequest(true);Object a=make(own?"54321":"12345","Known");f(request,"b","uin_bDay",String.class).set(a,own?"54321":"12345");step("interest-count-"+count+'-'+own,a,reply(240,10,interests(count,true)));
        }
    }
    static void sequences()throws Exception{
        int[][] orders={{200,210,220,230,240,250},{240,230,220,210,200,250},{220,200,240,210,230,250},{200,200,200,200,200,250},{250,200,220,240,230,210}};
        for(int n=0;n<orders.length;n++)for(boolean own:new boolean[]{false,true})for(boolean numericNick:new boolean[]{false,true})for(boolean known:new boolean[]{false,true})for(int failure=0;failure<3;failure++){
            freshRequest(true);String uin=own?"54321":"12345";Object a=make(uin,numericNick?uin:"Known");if(known){contactItem=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,uin,uin,false,true);((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);}
            notifications.getField("showFailure").setInt(null,failure);
            for(int i=0;i<orders[n].length;i++){int type=orders[n][i];byte[] body=type==200?general("New name",true,1,1):type==210?work(true):type==220?more(25,1,1990,5,12,true):type==230?az("About",true):type==240?interests(4,true):new byte[0];step("flow-"+n+'-'+own+'-'+numericNick+'-'+known+'-'+failure+'-'+i,a,reply(type,10,body));}
            step("flow-late-"+n+'-'+own+'-'+numericNick+'-'+known+'-'+failure,a,reply(230,10,az("Late",true)));
        }
        for(int counter:new int[]{Integer.MIN_VALUE,-1,0,4,5,Integer.MAX_VALUE})for(boolean shown:new boolean[]{false,true})for(boolean show:new boolean[]{false,true})for(boolean notFound:new boolean[]{false,true}){
            freshRequest(true);Object a=make("12345","Known");f(request,"a","packetCounter",int.class).setInt(a,counter);f(request,"c","infoShown",boolean.class).setBoolean(a,shown);f(request,"d","showInfoText",boolean.class).setBoolean(a,show);f(request,"e","notFound",boolean.class).setBoolean(a,notFound);step("seed-counter-"+counter+'-'+shown+'-'+show+'-'+notFound,a,reply(230,10,az("Note",true)));
        }
        for(int type:new int[]{0,199,200,209,210,219,220,229,230,239,240,249,250,251,65535})for(int size=0;size<4;size++){
            freshRequest(true);Object a=make("12345","Known");step("type-header-"+type+'-'+size,a,meta(2010,Arrays.copyOf(join(le(type),new byte[]{10}),size)));
        }
        for(int sub:new int[]{0,65,66,2000,2010,65535}){freshRequest(true);Object a=make("12345","Known");step("sub-"+sub,a,meta(sub,join(le(230),new byte[]{10},az("Note",true))));}
        freshRequest(true);Object a=make("12345","Known");step("null-packet",a,null);step("hello-packet",a,hello());step("other-snac",a,snac(1,99,0,new byte[0]));
    }
    static void exerciseRequestInfo()throws Exception{
        setupFiles();request=load("ce","jimm.comm.RequestInfoAction");birthday=load("cs","jimm.util.NoticeOnBirthDay");timer=load("at","jimm.TimerTasks");util=load("co","jimm.comm.Util");spy=load("BirthdayIO","BirthdayIO");notifications=load("RequestInfoIO","RequestInfoIO");initCases();replyCases();sequences();
        if(initializations<30||forwards<1000||accepted<100||deliveries<20)throw new AssertionError("Request-info coverage guards");row("initializations:"+initializations+"/forwards:"+forwards+"/accepted:"+accepted+"/deliveries:"+deliveries);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseRequestInfo();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
