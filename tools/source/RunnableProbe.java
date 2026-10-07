import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.MIDletBridge;

/** Execute every dispatcher case, retained wrappers, payload aliasing and reconnect branches. */
public class RunnableProbe extends FileTransferProbe {
    static Class<?> dispatcher,spy,midletClass,plain,notice,listener,message;
    static Object midlet;static int callbacks,queueCount;
    static HashSet<String> labels=new HashSet<>();
    static String invoke(Method method,Object target,Object...args)throws Exception{try{return String.valueOf(method.invoke(target,args));}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken dispatcher fixture",e.getCause());return "exception:"+e.getCause().getClass().getName();}}
    static Vector events()throws Exception{return (Vector)spy.getField("events").get(null);}
    static Vector queue()throws Exception{return (Vector)spy.getField("queued").get(null);}
    static String value(Object o)throws Exception{
        if(o==null)return "null";if(o instanceof String)return "str:"+enc(o);if(o instanceof byte[])return "bytes:"+b64((byte[])o);
        if(o instanceof Object[]){StringBuilder b=new StringBuilder("[");for(Object v:(Object[])o)b.append(value(v)).append(',');return b.append(']').toString();}
        if(o instanceof Number||o instanceof Boolean)return o.getClass().getSimpleName()+":"+o;
        if(dispatcher.isInstance(o))return "task:"+f(dispatcher,"a","type",int.class).getInt(o)+":"+value(f(dispatcher,"a","data",Object[].class).get(o));
        if(contact.isInstance(o))return "contact:"+m(contact,"a","getStringValue",String.class,int.class).invoke(o,0);
        if(message.isInstance(o))return "message:"+enc(f(message,"c","sndrUin",String.class).get(o))+":"+f(message,"b","messageType",int.class).getInt(o);
        return "other:"+o.getClass().getName();
    }
    static void fresh()throws Exception{spy.getMethod("reset").invoke(null);events.getMethod("reset").invoke(null);f(dispatcher,"a","midlet",midletClass).set(null,midlet);}
    static void snapshot(String label,String result)throws Exception{
        StringBuilder b=new StringBuilder(label).append(':').append(result).append("/retry:").append(f(icq,"b","reconnect_attempts",int.class).getInt(null)).append("/queue:").append(queue().size());
        for(Object item:events()){Object[] e=(Object[])item;labels.add((String)e[0]);b.append('/').append(e[0]).append(':').append(value(e[1]));if(!e[0].equals("queue")&&!e[0].equals("gc")&&!e[0].equals("sleep")&&!e[0].equals("spam"))callbacks++;if(e[0].equals("queue"))queueCount++;}row(b.toString());events().clear();
    }
    static Object task(int type,Object[] data)throws Exception{return instance(dispatcher,new Class<?>[]{int.class,Object[].class},type,data);}
    static Object[] payload(int type)throws Exception{
        Object[] values=new Object[13];values[0]="12345";values[1]=Integer.valueOf(-1);values[2]=Integer.valueOf(37);values[3]=new byte[]{1,2,3,4};values[4]=new byte[]{5,6,7,8};for(int i=5;i<13;i++)values[i]=Integer.valueOf(0x12340000+i);
        if(type==1)values[0]=instance(plain,new Class<?>[]{String.class,String.class,long.class,String.class,boolean.class},"12345","54321",1273665600L,"text",false);
        if(type==1||type==9)values[1]=Boolean.TRUE;if(type==6)values[0]=new String[]{"12345",null,"Info"};if(type==8)values[0]=contactItem;return values;
    }
    static void dispatch()throws Exception{
        for(int type=-1;type<=255;type++){fresh();Object a=task(type,payload(type));snapshot("run-"+type,invoke(m(dispatcher,"run","run",void.class),a));}
        for(int type:new int[]{1,4,5,6,8,9,13})for(int size:new int[]{-1,0,1,2,3,4,5,6,12,13,14})for(int mode=0;mode<4;mode++){
            fresh();Object[] values=size<0?null:Arrays.copyOf(payload(type),size);if(values!=null)for(int i=0;i<values.length;i++){if(mode==1)values[i]=null;if(mode==2)values[i]="wrong";if(mode==3)values[i]=Integer.valueOf(1);}snapshot("bad-data-"+type+"-"+size+"-"+mode,invoke(m(dispatcher,"run","run",void.class),task(type,values)));
        }
        fresh();Object[] args=payload(9);Object a=task(9,args);row("constructor-alias:"+(f(dispatcher,"a","data",Object[].class).get(a)==args));args[0]="changed";args[1]=Boolean.FALSE;snapshot("constructor-mutated",invoke(m(dispatcher,"run","run",void.class),a));
        for(int incoming:new int[]{-1,0,1024,Integer.MAX_VALUE,Integer.MIN_VALUE})for(int outgoing:new int[]{-1,0,4096,Integer.MAX_VALUE,Integer.MIN_VALUE}){fresh();f(traffic,"f","sessionInTraffic",int.class).setInt(null,incoming);f(traffic,"g","sessionOutTraffic",int.class).setInt(null,outgoing);snapshot("caption-traffic-"+incoming+"-"+outgoing,invoke(m(dispatcher,"run","run",void.class),task(7,null)));}
    }
    static void flush(String label)throws Exception{Vector copy=new Vector(queue());queue().clear();int i=0;for(Object a:copy)snapshot(label+"-"+(i++),invoke(m(dispatcher,"run","run",void.class),a));}
    static void wrappers()throws Exception{
        for(int type:new int[]{1,4,5,6,8,9,13,77})for(boolean single:new boolean[]{false,true})for(int failure=0;failure<2;failure++){
            fresh();spy.getField("queueFailure").setInt(null,failure);Object[] args=payload(type);String result=single?invoke(m(dispatcher,"a","callSerially",void.class,int.class,Object.class),null,type,args[0]):invoke(m(dispatcher,"a","callSerially",void.class,int.class,Object[].class),null,type,args);snapshot("queue-"+type+"-"+single+"-"+failure,result);flush("queued-run");
        }
        for(String uin:new String[]{null,"","12345","\u041f\u0440\u0438\u0432\u0435\u0442"})for(boolean typing:new boolean[]{false,true}){fresh();snapshot("typing-"+enc(uin)+"-"+typing,invoke(m(dispatcher,"a","BeginTyping",void.class,String.class,boolean.class),null,uin,typing));flush("typing-run");}
        fresh();Object[] args=payload(5);snapshot("array-queued",invoke(m(dispatcher,"a","callSerially",void.class,int.class,Object[].class),null,5,args));args[0]="changed";args[1]=Integer.valueOf(10);((byte[])args[3])[0]=99;flush("array-mutated");
        fresh();f(dispatcher,"a","midlet",midletClass).set(null,null);snapshot("null-midlet",invoke(m(dispatcher,"a","callSerially",void.class,int.class,Object[].class),null,9,payload(9)));
        fresh();snapshot("null-array-queue",invoke(m(dispatcher,"a","callSerially",void.class,int.class,Object[].class),null,9,(Object)null));flush("null-array-run");
    }
    static void getters()throws Exception{
        for(int size=0;size<=5;size++)for(Object seed:new Object[]{null,Boolean.FALSE,Boolean.TRUE,Integer.valueOf(0),Integer.valueOf(-1),Long.valueOf(1),"x"})for(int index=-1;index<=size+1;index++){
            Object[] data=new Object[size];Arrays.fill(data,seed);fresh();row("getInt-"+size+"-"+value(seed)+"-"+index+":"+invoke(m(dispatcher,"a","getInt",int.class,Object[].class,int.class),null,data,index));
            for(int n:new int[]{0,-1,Integer.MIN_VALUE,Integer.MAX_VALUE}){Object[] copy=data.clone();String result=invoke(m(dispatcher,"a","setInt",void.class,Object[].class,int.class,int.class),null,copy,index,n);row("setInt-"+size+"-"+value(seed)+"-"+index+"-"+n+":"+result+":"+value(copy));}
        }
        Method getBool=ref?m(dispatcher,"a$4e9ee315","getBoolean",boolean.class,Object[].class):m(dispatcher,"a","getBoolean",boolean.class,Object[].class,int.class);
        for(int size=-1;size<=3;size++)for(Object seed:new Object[]{null,Boolean.FALSE,Boolean.TRUE,Integer.valueOf(1),Long.valueOf(1),"x"}){Object[] values=size<0?null:new Object[size];if(values!=null)Arrays.fill(values,seed);row("getBoolean-"+size+"-"+value(seed)+":"+(ref?invoke(getBool,null,new Object[]{values}):invoke(getBool,null,values,1)));}
        row("null-int-get:"+invoke(m(dispatcher,"a","getInt",int.class,Object[].class,int.class),null,null,0));row("null-int-set:"+invoke(m(dispatcher,"a","setInt",void.class,Object[].class,int.class,int.class),null,null,0,1));
        Object[] boxed=new Object[2];m(dispatcher,"a","setInt",void.class,Object[].class,int.class,int.class).invoke(null,boxed,1,7);Object first=boxed[1];m(dispatcher,"a","setInt",void.class,Object[].class,int.class,int.class).invoke(null,boxed,1,7);row("setInt-new-wrapper:"+(first!=boxed[1])+":"+(boxed[1]!=Integer.valueOf(7)));
    }
    static void reconnect()throws Exception{
        for(int delay:new int[]{-1,0,1,10,2147483,2147484,Integer.MAX_VALUE,Integer.MIN_VALUE})for(boolean disconnected:new boolean[]{false,true})for(int retries:new int[]{-1,0,1,2,Integer.MAX_VALUE,Integer.MIN_VALUE})for(int failure=0;failure<3;failure++){
            fresh();integer(73,delay);m(icq,"b","setDisconnected",void.class,boolean.class).invoke(null,disconnected);f(icq,"b","reconnect_attempts",int.class).setInt(null,retries);spy.getField("sleepFailure").setInt(null,failure);
            snapshot("reconnect-"+delay+"-"+disconnected+"-"+retries+"-"+failure,invoke(m(dispatcher,"run","run",void.class),task(14,null)));
        }
        Object[] optionsData=(Object[])f(options,"a","options",Object[].class).get(null);
        for(Object option:new Object[]{null,"wrong",Boolean.TRUE,Long.valueOf(10)})for(boolean disconnected:new boolean[]{false,true}){fresh();optionsData[73]=option;m(icq,"b","setDisconnected",void.class,boolean.class).invoke(null,disconnected);f(icq,"b","reconnect_attempts",int.class).setInt(null,2);snapshot("reconnect-bad-option-"+value(option)+"-"+disconnected,invoke(m(dispatcher,"run","run",void.class),task(14,null)));}
        integer(73,0);
    }
    static void messages()throws Exception{
        Class<?> jimm=load("jimm.Jimm","jimm.Jimm");Method add=m(dispatcher,"a","addMessageSerially",void.class,message);
        for(int spam:new int[]{0,1})for(boolean se:new boolean[]{false,true})for(boolean bring:new boolean[]{false,true})for(int failure=0;failure<2;failure++){
            fresh();f(jimm,"b","is_phone_SE",boolean.class).setBoolean(null,se);bool(151,bring);spy.getField("forcedSpam").setInt(null,spam);spy.getField("queueFailure").setInt(null,failure);Object msg=payload(1)[0];snapshot("message-"+spam+"-"+se+"-"+bring+"-"+failure,invoke(add,null,msg));flush("message-run");
        }
        // Retain the actual isSpam predicate in these integration cases; its full behavior has a separate incoming-message test.
        for(boolean enabled:new boolean[]{false,true})for(boolean known:new boolean[]{false,true})for(int type:new int[]{1,4,5}){
            reset(false);fresh();bool(162,enabled);bool(151,false);f(jimm,"b","is_phone_SE",boolean.class).setBoolean(null,false);string(35,"Challenge");string(36,"Welcome");string(37,"Answer");
            for(String[] names:new String[][]{{"a","uins"},{"b","uin1"},{"c","uin2"}})f(listener,names[0],names[1],Vector.class).set(null,new Vector());if(known)((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);
            Object msg=type==1?payload(1)[0]:instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},type,"12345",true,"reason");snapshot("real-spam-"+enabled+"-"+known+"-"+type,invoke(add,null,msg));flush("real-spam-run");
        }
    }
    static void callers()throws Exception{
        Method forward=m(listener,"a","forward",void.class,packet);Object receiver=Modifier.isStatic(forward.getModifiers())?null:instance(listener,new Class<?>[0]);
        for(int family:new int[]{2,3})for(int status:new int[]{0,1,2,32,256,65535}){
            reset(false);fresh();bool(162,false);bool(166,false);bool(183,true);m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(contactItem,16,false);((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);
            Object p=snac(family,family==3?11:6,0,join(new byte[]{5},"12345".getBytes("US-ASCII"),word(0),word(2),tlv(6,new byte[]{0,0,(byte)(status>>8),(byte)status}),tlv(29,new byte[0])));snapshot("presence-caller-"+family+"-"+status,invoke(forward,receiver,p));flush("presence-caller-run");
        }
    }
    static void exerciseRunnable()throws Exception{
        setupFiles();dispatcher=load("l","jimm.RunnableImpl");spy=load("RunnableIO","RunnableIO");midlet=MIDletBridge.getCurrentMIDlet();midletClass=MIDlet.class;message=load("ac","jimm.comm.Message");plain=load("av","jimm.comm.PlainMessage");notice=load("s","jimm.comm.SystemNotice");listener=load("ae","jimm.comm.ActionListener");f(icq,"b","reconnect_attempts",int.class).setInt(null,0);m(icq,"b","setDisconnected",void.class,boolean.class).invoke(null,false);
        dispatch();wrappers();getters();reconnect();messages();callers();if(callbacks<300||queueCount<30)throw new AssertionError("Dispatcher success guards: "+callbacks+" "+queueCount);
        for(String label:new String[]{"typing","message","offline","caption","presence","add-contact","reset-contacts","info","back","menu","next-server","before-connect","connect","minimized","sound"})if(!labels.contains(label))throw new AssertionError("Missing successful callback: "+label);row("callbacks:"+callbacks+"/queue-attempts:"+queueCount);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseRunnable();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
