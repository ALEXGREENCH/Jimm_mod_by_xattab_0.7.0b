import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

/** Execute the genuine key entrypoints and doKeyreaction, including calls interrupted by scripted failures. */
public class KeyRoutingProbe {
    static Method method(Class<?> c,String name,Class<?>...params)throws Exception{for(Method m:c.getDeclaredMethods())if(m.getName().equals(name)&&m.getReturnType()==void.class&&Arrays.equals(m.getParameterTypes(),params)){m.setAccessible(true);return m;}throw new NoSuchMethodException(name);}
    public static void main(String[] args)throws Exception{
        boolean ref=args[1].equals("reference");String target=args[4];
        ClassLoader loader=new URLClassLoader(new URL[]{Paths.get(args[0]).toUri().toURL(),Paths.get(args[3],"microemu.jar").toUri().toURL(),Paths.get(args[3],"microemu-nokiaui.jar").toUri().toURL()},ClassLoader.getPlatformClassLoader());
        Class<?> vl=Class.forName(ref?(target.equals("MIDP2")?"cd":"cb"):"DrawControls.VirtualList",true,loader),io=Class.forName("KeyRoutingIO",true,loader),shim=Class.forName("RecoveryKeyList",true,loader);
        Class<?> unsafeClass=Class.forName("sun.misc.Unsafe");Field singleton=unsafeClass.getDeclaredField("theUnsafe");singleton.setAccessible(true);Object unsafe=singleton.get(null);
        Object list=unsafeClass.getMethod("allocateInstance",Class.class).invoke(unsafe,shim);
        Class<?> callbacks=Class.forName(ref?(target.equals("MIDP2")?"bx":target.equals("MOTOROLA")?"bw":"bv"):"DrawControls.VirtualListCommands",false,loader);
        Field commands=null;
        for(Field f:vl.getDeclaredFields())if(f.getType()==callbacks){if(commands!=null)throw new AssertionError("Ambiguous key callback field");commands=f;}
        if(commands==null)throw new AssertionError("Missing key callback interface");commands.setAccessible(true);
        Method callback=io.getMethod("callback",int.class,int.class),reset=io.getMethod("reset",boolean.class,int.class);
        Object listener=java.lang.reflect.Proxy.newProxyInstance(loader,new Class<?>[]{commands.getType()},(proxy,m,values)->{if(values!=null&&values.length==3){try{callback.invoke(null,values[1],values[2]);}catch(InvocationTargetException e){throw e.getCause();}}return null;});
        Method[] calls={method(vl,ref?"a":"doKeyreaction",int.class,int.class),method(vl,ref?"f":"keyPressed",int.class),method(vl,ref?"g":"keyRepeated",int.class),method(vl,ref?"h":"keyReleased",int.class)};
        List<String> rows=new ArrayList<>();
        for(int route=0;route<4;route++)for(int type:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,4,Integer.MAX_VALUE})for(int key:new int[]{-1000,-1,0,42,48,52,57,Integer.MAX_VALUE})for(boolean auto:new boolean[]{false,true})for(boolean listen:new boolean[]{false,true})for(int fail:new int[]{0,1,2,3,4,5,11,12,13,14,15}){
            if(route!=0&&type!=0)continue;
            commands.set(list,listen?listener:null);reset.invoke(null,auto,fail);String error="none";
            try{if(route==0)calls[route].invoke(list,key,type);else calls[route].invoke(list,key);}catch(InvocationTargetException e){error=e.getCause().getClass().getName()+":"+e.getCause().getMessage();}
            rows.add(route+"/"+type+"/"+key+"/"+auto+"/"+listen+"/"+fail+":"+io.getField("log").get(null)+"error="+error);
        }
        Files.write(Paths.get(args[2]),rows,java.nio.charset.StandardCharsets.UTF_8);System.out.println("PASS key routing: "+rows.size()+" observations");System.exit(0);
    }
}
