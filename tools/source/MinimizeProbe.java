import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import sun.misc.Unsafe;

/** Record actual TextBox instanceof decisions and complete display/destination effects. */
public final class MinimizeProbe {
    public static void main(String[] args)throws Exception {
        Path libs=Paths.get(args[1]);boolean reference=args[2].equals("reference");
        Field unsafeField=Unsafe.class.getDeclaredField("theUnsafe");unsafeField.setAccessible(true);Unsafe unsafe=(Unsafe)unsafeField.get(null);
        try(URLClassLoader loader=new URLClassLoader(new URL[]{Paths.get(args[0]).toUri().toURL(),libs.resolve("microemu.jar").toUri().toURL(),libs.resolve("microemu-nokiaui.jar").toUri().toURL()},ClassLoader.getPlatformClassLoader())) {
            Class<?> io=Class.forName("MinimizeIO",true,loader),displayType=Class.forName("javax.microedition.lcdui.Display",true,loader);
            Class<?> jimm=Class.forName("jimm.Jimm",true,loader);List<java.util.Timer> timers=new ArrayList<>();
            try {
                for(Field f:jimm.getDeclaredFields())if(f.getType()==java.util.Timer.class&&Modifier.isStatic(f.getModifiers())){f.setAccessible(true);java.util.Timer timer=(java.util.Timer)f.get(null);if(timer!=null)timers.add(timer);}
                Field display=null;for(Field f:jimm.getDeclaredFields())if(f.getType()==displayType&&Modifier.isStatic(f.getModifiers())){if(display!=null)throw new AssertionError("Ambiguous display");display=f;}
                if(display==null)throw new AssertionError("Missing display");display.setAccessible(true);
                Object receiver=unsafe.allocateInstance(displayType);io.getField("display").set(null,receiver);
                String[] names={"other","editor","subclass","workScreen"};String[] types={"MinimizeIO$Other","javax.microedition.lcdui.TextBox","MinimizeIO$Editor","MinimizeIO$Other"};
                for(int i=0;i<names.length;i++)io.getField(names[i]).set(null,unsafe.allocateInstance(Class.forName(types[i],true,loader)));
                Object[] screens={null,io.getField("other").get(null),io.getField("editor").get(null),io.getField("subclass").get(null)};
                Method method=jimm.getDeclaredMethod(reference?"a":"setMinimized",boolean.class);method.setAccessible(true);
                Method identity=io.getMethod("identity",Object.class);List<String> lines=new ArrayList<>();
                for(int screen=0;screen<4;screen++)for(boolean shown:new boolean[]{false,true})for(boolean missing:new boolean[]{false,true})for(boolean mini:new boolean[]{false,true})
                    for(String failure:new String[]{"set","get","shown","work"})for(int mode=0;mode<3;mode++)for(boolean after:new boolean[]{false,true})for(int ordinal=1;ordinal<=2;ordinal++) {
                        io.getMethod("reset").invoke(null);io.getField("current").set(null,screens[screen]);io.getField("shown").setBoolean(null,shown);io.getField("failing").set(null,failure);io.getField("mode").setInt(null,mode);io.getField("after").setBoolean(null,after);io.getField("ordinal").setInt(null,ordinal);display.set(null,missing?null:receiver);
                        boolean[] steps={mini,false,true,false};
                        for(int step=0;step<steps.length;step++) {
                            Object outcome=null;try{method.invoke(null,steps[step]);}catch(InvocationTargetException e){outcome="exception:"+e.getCause().getClass().getName();}
                            lines.add(screen+":"+shown+":"+missing+":"+mini+":"+failure+":"+mode+":"+after+":"+ordinal+":"+step+":"+outcome
                                      +":"+io.getField("trace").get(null)+":"+Arrays.toString((int[])io.getField("calls").get(null))+":"+identity.invoke(null,io.getField("current").get(null)));
                        }
                    }
                // Keep the genuinely frozen Windows baseline comparable on Linux CI as well.
                Files.write(Paths.get(args[3]),(String.join("\r\n",lines)+"\r\n").getBytes(StandardCharsets.UTF_8));
                System.out.println("PASS minimize probe: "+lines.size()+" actual controller observations with TextBox/subclass, null receivers, ordered effects and failures");
            } finally {for(java.util.Timer timer:timers)timer.cancel();}
        }
    }
}
