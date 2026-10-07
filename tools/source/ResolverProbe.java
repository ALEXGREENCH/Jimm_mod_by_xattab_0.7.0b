import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Invoke the complete resolver and observe its actual class/instance monitors and cleanup. */
public class ResolverProbe {
    static Class<?> subject,io;static Object receiver;static Method resolve;static PrintWriter out;static int observations;
    static void row(String text){out.println(text);observations++;}
    static String invoke(String host,String port)throws Exception {
        try{return "return:"+resolve.invoke(receiver,host,port);}catch(InvocationTargetException e){
            Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static void exercise()throws Exception {
        String[] hosts={null,"","1","0.0.0.0","255.255.255.255","-1.2.3.4","1.2.3","256.1.1.1","login.invalid","[::1]","a b","\u0442\u0435\u0441\u0442"};
        for(String host:hosts)for(String port:new String[]{null,"","5190","bad","-1"})for(int fault=0;fault<12;fault++)for(String address:new String[]{null,"203.0.113.8"}) {
            io.getMethod("reset",int.class,String.class).invoke(null,fault,address);
            String outcome=invoke(host,port);
            row("resolve:"+host+":"+port+":"+fault+":"+address+":"+outcome+":"+io.getField("log").get(null));
            if(Thread.holdsLock(subject)||Thread.holdsLock(receiver))throw new AssertionError("Monitor leaked");
        }
        for(String host:new String[]{"203.0.113.4","login.invalid"}) {
            io.getMethod("reset",int.class,String.class).invoke(null,0,"203.0.113.8");
            final CountDownLatch ready=new CountDownLatch(1),done=new CountDownLatch(1);final Throwable[] failure={null};final String[] result={null};
            Thread worker=new Thread(new Runnable(){public void run(){ready.countDown();try{result[0]=invoke(host,"5190");}catch(Throwable t){failure[0]=t;}finally{done.countDown();}}});
            boolean blocked;
            synchronized(subject) {
                worker.start();if(!ready.await(5,TimeUnit.SECONDS))throw new AssertionError("Worker not ready");
                long until=System.nanoTime()+TimeUnit.SECONDS.toNanos(5);
                while(worker.getState()!=Thread.State.BLOCKED&&done.getCount()!=0&&System.nanoTime()<until)Thread.yield();
                blocked=worker.getState()==Thread.State.BLOCKED;
            }
            if(!done.await(5,TimeUnit.SECONDS))throw new AssertionError("Resolver did not finish after unlock");
            if(failure[0]!=null)throw new AssertionError(failure[0]);
            row("class-monitor-blocks:"+host+":"+blocked+":"+result[0]+":"+io.getField("log").get(null));
        }
    }
    public static void main(String[] args){try {
        Headless h=new Headless();Field f=Headless.class.getDeclaredField("emulator");f.setAccessible(true);Common emulator=(Common)f.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();ClassLoader loader=host.getClass().getClassLoader();
        subject=Class.forName(args[2].replace('/','.'),true,loader);io=Class.forName("ResolverIO",true,loader);
        Constructor<?> ctor=subject.getDeclaredConstructors()[0];ctor.setAccessible(true);
        if(ctor.getParameterCount()==0)receiver=ctor.newInstance();
        else {Class<?> owner=ctor.getParameterTypes()[0];Constructor<?> parent=owner.getDeclaredConstructor();parent.setAccessible(true);receiver=ctor.newInstance(parent.newInstance());}
        resolve=subject.getDeclaredMethod(args[3],String.class,String.class);resolve.setAccessible(true);
        io.getField("owner").set(null,subject);io.getField("receiver").set(null,receiver);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));exercise();out.close();
        System.out.println("PASS resolver: "+observations+" observations");System.exit(0);
    }catch(Throwable t){t.printStackTrace();System.exit(1);}}
}
