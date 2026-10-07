import java.lang.reflect.*;
import java.lang.management.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

/** Real sender orchestration, message constructors and synchronized typing packets. */
public class SendTextProbe extends LoginProbe {
    static Class<?> plain,message,send,boundary;
    static Object recipient;
    static int sends,requests,chats,histories,sleeps,lights,typingCalls,typingPackets,monitors;
    static String text(Object value)throws Exception {
        if(value==null)return "null";
        String s=(String)value;MessageDigest digest=MessageDigest.getInstance("SHA-256");
        for(int i=0;i<s.length();i++){char c=s.charAt(i);digest.update((byte)(c>>>8));digest.update((byte)c);}
        return s.length()+"/"+b64(digest.digest());
    }
    static String failure(Throwable value)throws Exception {
        if(error.isInstance(value))return "JimmException/"+f(error,"a","_ErrCode",int.class).getInt(value)+"/"+f(error,"a","critical",boolean.class).getBoolean(value)+"/"+text(value.getMessage());
        // JDK 17's helpful-NPE text embeds compiler/obfuscation names, not CLDC behavior.
        if(value instanceof NullPointerException)return value.getClass().getName();
        return value.getClass().getName()+"/"+text(value.getMessage());
    }
    static String actual(Method method,Object target,Object...args)throws Exception {
        try{return String.valueOf(method.invoke(target,args));}
        catch(InvocationTargetException e){Throwable value=e.getCause();if(value instanceof LinkageError)throw(LinkageError)value;return "exception:"+failure(value);}
    }
    static void put(String field,Object value)throws Exception{boundary.getField(field).set(null,value);}
    static Vector events()throws Exception{return(Vector)boundary.getField("events").get(null);}
    static Object jimmError(boolean critical)throws Exception {
        return critical?instance(error,new Class<?>[]{int.class,int.class},120,0):instance(error,new Class<?>[]{int.class,int.class,boolean.class},120,0,true);
    }
    static String atom(Object value)throws Exception {
        if(value==null)return "null";
        if(value instanceof String)return "text:"+text(value);
        if(value instanceof Throwable)return failure((Throwable)value);
        if(contact.isInstance(value))return value==recipient?"recipient":"other-contact";
        return value.getClass().getName()+"="+value;
    }
    static String action(Object value)throws Exception {
        Object msg=f(send,"a","plainMsg",plain).get(value);
        Object receiver=f(message,"a","rcvr",contact).get(msg);
        return "text:"+text(f(plain,"a","text",String.class).get(msg))+"/sender:"+text(f(message,"c","sndrUin",String.class).get(msg))
            +"/receiver:"+(receiver==null?"null":receiver==recipient?"recipient":"other")
            +"/type:"+f(message,"b","messageType",int.class).getInt(msg)+"/date:"+f(message,"a","newDate",long.class).getLong(msg)
            +"/id:"+f(send,"b","msgId1",int.class).getInt(value)+","+f(send,"c","msgId2",int.class).getInt(value)
            +"/seq:"+f(send,"a","SEQ1",int.class).getInt(value);
    }
    static void sender(String label,String value,boolean history,boolean receiver,String failLabel,int ordinal,Throwable fail,boolean diagnostic,long now,int counter)throws Exception {
        bool(137,history);string(0,"54321");f(icq,"a","myNick",String.class).set(null,"Nick \u043f\u010d");
        f(send,"d","msgCounter",int.class).setInt(null,counter);io.getField("now").setLong(null,now);
        events().clear();put("failureLabel",failLabel);put("failureOrdinal",Integer.valueOf(ordinal));put("failure",fail);put("hits",Integer.valueOf(0));
        put("requestFailure",diagnostic?jimmError(false):null);put("requestFailureOrdinal",Integer.valueOf(1));put("requestHits",Integer.valueOf(0));
        String result=actual(m(ui,"a","sendMessage",void.class,String.class,contact),null,value,receiver?recipient:null);
        row(label+":result="+result+":counter="+f(send,"d","msgCounter",int.class).getInt(null));
        for(Object entry:events()) {
            Object[] event=(Object[])entry,args=(Object[])event[1];String name=(String)event[0];StringBuilder b=new StringBuilder(label+":"+name);
            if(name.equals("request")){requests++;b.append('=').append(action(args[0]));}
            else {if(name.equals("chat"))chats++;if(name.equals("history"))histories++;if(name.equals("sleep"))sleeps++;if(name.equals("light"))lights++;
                for(Object arg:args)b.append('|').append(atom(arg));}
            row(b.toString());
        }
        sends++;
    }
    static String sample(int size,int kind) {
        String pattern=kind==0?"a":kind==1?"\u043f\u010d\u4e2d\r\n":"\ud83d\ude00x\u0000\ud800q\udc00";
        StringBuilder b=new StringBuilder();while(b.length()<size)b.append(pattern);return b.substring(0,size);
    }
    static void sending()throws Exception {
        int index=0;
        for(boolean history:new boolean[]{false,true})for(boolean receiver:new boolean[]{false,true}) {
            sender("empty-"+index++,null,history,receiver,"",1,null,false,1273665600000L,0);
            sender("empty-"+index++,"",history,receiver,"",1,null,false,1273665600000L,0);
            for(int length:new int[]{1,1023,1024,1025,2047,2048,2049,3071,3072,3073,4097,8193})for(int kind=0;kind<3;kind++)
                sender("send-"+index+++'-'+length+'-'+kind,sample(length,kind),history,receiver,"",1,null,false,1273665600000L,0);
        }
        for(boolean history:new boolean[]{false,true})for(long now:new long[]{0,1,-1,2147483648L,4294967296L})for(int counter:new int[]{0,1,-1,2147483647,-2147483648})
            sender("ids-"+index++,sample(3073,2),history,true,"",1,null,false,now,counter);
        for(boolean history:new boolean[]{false,true})for(int ordinal:new int[]{1,2,4})for(int kind=0;kind<4;kind++) {
            Throwable fault=kind==0?(Throwable)jimmError(false):kind==1?(Throwable)jimmError(true):kind==2?new IllegalArgumentException("fixture"):new AssertionError("fixture");
            sender("queue-fault-"+index++,sample(4097,2),history,true,"request",ordinal,fault,false,1273665600000L,0);
        }
        for(boolean history:new boolean[]{false,true})for(String label:new String[]{"error","chat","history","sleep","light"})for(int ordinal:new int[]{1,2,4})for(int kind=0;kind<3;kind++) {
            Throwable fault=kind==0?new IllegalStateException("fixture"):kind==1?new InterruptedException("fixture"):new AssertionError("fixture");
            sender("callback-fault-"+index++,sample(4097,1),history,true,label,ordinal,fault,label.equals("error"),1273665600000L,0);
        }
    }
    static void typing()throws Exception {
        Method method=m(icq,"a","beginTyping",void.class,String.class,boolean.class);owner=m(icq,"a","getIcq",icq).invoke(null);
        row("typing-modifiers:"+(method.getModifiers()&40));
        ArrayList<String> values=new ArrayList<>();Collections.addAll(values,null,"","54321","\u043f\u010d\u4e2d\ud83d\ude00");
        for(int length:new int[]{1,254,255,256,257,511,512})for(int kind=0;kind<3;kind++)values.add(sample(length,kind));
        int index=0;
        for(boolean disabled:new boolean[]{false,true})for(boolean start:new boolean[]{false,true})for(int channel=0;channel<3;channel++)for(String value:values) {
            io.getMethod("reset").invoke(null);bool(146,disabled);io.getField("failSend").setBoolean(null,channel==1);
            f(icq,"a","c",connection).set(null,channel==2?null:instance(load("LoginConnection","LoginConnection"),ref?new Class<?>[]{icq}:new Class<?>[]{icq},owner));
            String label="typing-"+index++,result=actual(method,owner,value,start);row(label+":result="+result);
            for(Object p:(Vector)io.getField("packets").get(null)){row(label+":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));typingPackets++;}
            typingCalls++;
        }
        for(boolean fault:new boolean[]{false,true})monitor(method,fault);
    }
    static void monitor(final Method method,boolean fault)throws Exception {
        io.getMethod("reset").invoke(null);bool(146,false);io.getField("failSend").setBoolean(null,fault);
        f(icq,"a","c",connection).set(null,instance(load("LoginConnection","LoginConnection"),new Class<?>[]{icq},owner));
        final CountDownLatch started=new CountDownLatch(1);final String[] result={null};final Throwable[] probeFailure={null};
        Thread worker=new Thread(new Runnable(){public void run(){started.countDown();try{result[0]=actual(method,owner,"54321",true);}catch(Throwable e){probeFailure[0]=e;}}},"typing-class-monitor");worker.setDaemon(true);
        boolean blocked=false;
        synchronized(icq) {
            worker.start();if(!started.await(2,TimeUnit.SECONDS))throw new AssertionError("Typing worker did not start");
            long deadline=System.nanoTime()+TimeUnit.SECONDS.toNanos(2);ThreadMXBean threads=ManagementFactory.getThreadMXBean();
            while(System.nanoTime()<deadline) {
                ThreadInfo info=threads.getThreadInfo(worker.getId());
                if(info!=null&&info.getThreadState()==Thread.State.BLOCKED&&info.getLockOwnerId()==Thread.currentThread().getId()&&info.getLockInfo().getIdentityHashCode()==System.identityHashCode(icq)){blocked=true;break;}
                if(!worker.isAlive())break;Thread.sleep(1);
            }
        }
        worker.join(2000);if(worker.isAlive())throw new AssertionError("Typing worker did not finish");
        if(probeFailure[0]!=null)throw new AssertionError("Typing monitor probe failed",probeFailure[0]);
        row("typing-monitor-"+fault+":"+blocked+":"+result[0]);monitors++;
    }
    static void exerciseSender()throws Exception {
        ui=load("cf","jimm.JimmUI");plain=load("av","jimm.comm.PlainMessage");message=load("ac","jimm.comm.Message");send=load("cl","jimm.comm.SendMessageAction");boundary=load("SendTextIO","SendTextIO");
        recipient=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Recipient \u043f\u010d",false,true);
        typing();sending();
        if(requests<300||chats<200||histories<100||typingPackets<50)throw new AssertionError("Sender paths missing");
        row("coverage:"+sends+":"+requests+":"+chats+":"+histories+":"+sleeps+":"+lights+":"+typingCalls+":"+typingPackets+":"+monitors);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseSender();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
