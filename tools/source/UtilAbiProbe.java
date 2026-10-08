import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

/** Invoke actual native/raw/optimized helpers; record effects, exceptions and deterministic host dates. */
public final class UtilAbiProbe {
    static Class<?> util,clock; static Properties config; static Object[] table;
    static Method wrapper,core,factory,word,dword; static boolean raw;
    static Method find(String name,Class<?> result,Class<?>...params)throws Exception {
        for(Method m:util.getDeclaredMethods())if(m.getName().equals(name)&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),params)){m.setAccessible(true);return m;}
        throw new NoSuchMethodException(name);
    }
    static Object invoke(Method m,Object...args)throws Exception {
        try{return m.invoke(null,args);}catch(InvocationTargetException e){return "exception:"+e.getCause().getClass().getName();}
    }
    static final class ReadTrace extends InputStream {
        final byte[] data; final int fail; int pos,calls; final List<String> trace=new ArrayList<>();
        ReadTrace(byte[] data,int fail){this.data=data;this.fail=fail;}
        public int read()throws IOException {
            int call=calls++;
            if(call==fail){trace.add(call+":IOException");throw new IOException("scripted read");}
            int value=pos==data.length?-1:data[pos++]&255;trace.add(call+":"+value);return value;
        }
    }
    static final class WriteTrace extends ByteArrayOutputStream {
        final int fail; final boolean after; int calls;final List<Integer> trace=new ArrayList<>();
        WriteTrace(int fail,boolean after){this.fail=fail;this.after=after;super.write(77);super.write(88);}
        public synchronized void write(int value) {
            int call=calls++;trace.add(value);
            if(call==fail&&!after)throw new IllegalStateException("before write");
            super.write(value);
            if(call==fail&&after)throw new IllegalStateException("after write");
        }
    }
    static List<String> streams()throws Exception {
        List<String> lines=new ArrayList<>(); Random random=new Random(20100512);
        for(int sample=0;sample<1280;sample++) {
            byte[] data=sample==0?null:new byte[sample<10?sample-1:random.nextInt(18)];if(data!=null)random.nextBytes(data);
            Object result=raw?invoke(factory,data,0):invoke(factory,(Object)data);
            if(result instanceof DataInputStream) {
                if(data.length>0){data[0]^=91;data[data.length-1]^=57;}
                DataInputStream input=(DataInputStream)result;
                lines.add("factory:"+sample+":"+input.available()+":"+Arrays.toString(input.readAllBytes())+":"+input.read());
            } else lines.add("factory:"+sample+":"+result);
            byte[] bytes=data==null?new byte[0]:data;
            for(int fail=-1;fail<=2;fail++) {
                ReadTrace trace=new ReadTrace(bytes,fail);DataInputStream input=new DataInputStream(trace);
                Object value=raw?invoke(word,input,false):invoke(word,input);
                lines.add("word:"+sample+":"+fail+":"+value+":"+trace.trace+":"+trace.calls+":"+trace.pos+":"+Arrays.toString(Arrays.copyOfRange(bytes,trace.pos,bytes.length)));
            }
        }
        lines.add("word:null:"+(raw?invoke(word,null,false):invoke(word,new Object[]{null})));
        int[] edges={0,1,-1,127,128,255,256,65535,65536,16777215,16777216,0x80000000,0x7fffffff,0xff000001,0x01234567,0x89abcdef};
        for(int sample=0;sample<1280;sample++) {
            int value=sample<edges.length?edges[sample]:random.nextInt();
            for(int fail=-1;fail<4;fail++)for(boolean after:new boolean[]{false,true}) {
                WriteTrace stream=new WriteTrace(fail,after);Object result=raw?invoke(dword,stream,value,false):invoke(dword,stream,value);
                lines.add("dword:"+sample+":"+value+":"+fail+":"+after+":"+result+":"+stream.trace+":"+stream.calls+":"+Arrays.toString(stream.toByteArray()));
            }
        }
        lines.add("dword:null:"+(raw?invoke(dword,null,7,false):invoke(dword,null,7)));
        return lines;
    }
    static List<String> dates(boolean dateOnly)throws Exception {
        List<String> lines=new ArrayList<>();
        long[] epochs={-2208988800000L,-1L,0L,951782399000L,951782400000L,1269734400000L,1273622400000L,2147483647000L,4107542400000L,Long.MIN_VALUE,Long.MAX_VALUE};
        int[] offsets={-24,-12,-3,0,1,3,14,24,Integer.MIN_VALUE,Integer.MAX_VALUE};
        for(String zone:new String[]{"UTC","Europe/Moscow","America/New_York","Asia/Kathmandu"}) {
            TimeZone.setDefault(TimeZone.getTimeZone(zone));
            for(long epoch:epochs)for(int local:offsets)for(int gmt:offsets) {
                table[90]=Integer.valueOf(local);table[87]=Integer.valueOf(gmt);
                clock.getField("epoch").setLong(null,epoch);clock.getField("reads").setInt(null,0);
                Object value=dateOnly?invoke(core,false,true):(raw?invoke(wrapper,false):invoke(wrapper));
                lines.add(zone+":"+epoch+":"+local+":"+gmt+":"+value+":"+clock.getField("reads").getInt(null));
                if(clock.getField("reads").getInt(null)!=1)throw new AssertionError("Actual date method must read captured external clock once");
            }
        }
        return lines;
    }
    public static void main(String[] args)throws Exception {
        config=new Properties();try(InputStream input=Files.newInputStream(Paths.get(args[2]))){config.load(input);}
        Path libs=Paths.get(args[1]);Locale previousLocale=Locale.getDefault();TimeZone previousZone=TimeZone.getDefault();
        try(URLClassLoader loader=new URLClassLoader(new URL[]{Paths.get(args[0]).toUri().toURL(),libs.resolve("microemu.jar").toUri().toURL(),libs.resolve("microemu-nokiaui.jar").toUri().toURL()},ClassLoader.getPlatformClassLoader())) {
            Locale.setDefault(Locale.US);raw=config.getProperty("mode").equals("raw");util=Class.forName(config.getProperty("util"),true,loader);clock=Class.forName("UtilAbiClock",true,loader);
            Class<?> options=Class.forName(config.getProperty("options"),true,loader);Field field=null;
            for(Field f:options.getDeclaredFields())if(f.getType()==Object[].class){if(field!=null)throw new AssertionError("Ambiguous options table");field=f;}
            if(field==null)throw new AssertionError("Missing options table");field.setAccessible(true);table=new Object[256];table[133]=Boolean.TRUE;table[3]="RU";table[87]=table[90]=Integer.valueOf(0);field.set(null,table);
            wrapper=raw?find(config.getProperty("date"),long.class,boolean.class):find(config.getProperty("date"),long.class);
            factory=raw?find(config.getProperty("factory"),DataInputStream.class,byte[].class,int.class):find(config.getProperty("factory"),DataInputStream.class,byte[].class);
            word=raw?find(config.getProperty("word"),int.class,DataInputStream.class,boolean.class):find(config.getProperty("word"),int.class,DataInputStream.class);
            dword=raw?find(config.getProperty("dword"),void.class,ByteArrayOutputStream.class,int.class,boolean.class):find(config.getProperty("dword"),void.class,ByteArrayOutputStream.class,int.class);
            String coreName=config.getProperty("core");if(!coreName.isEmpty())core=find(coreName,long.class,boolean.class,boolean.class);
            List<String> streams=streams(),dates=dates(false);Files.write(Paths.get(args[3]+"-streams.txt"),streams);Files.write(Paths.get(args[3]+"-dates.txt"),dates);
            int coreCount=0;if(!config.getProperty("target").equals("MOTOROLA")){List<String> values=dates(true);coreCount=values.size();Files.write(Paths.get(args[3]+"-date-only.txt"),values);}
            System.out.println("PASS Util ABI probe: "+streams.size()+" stream observations, "+dates.size()+" date wrapper observations, "+coreCount+" date-only observations");
        } finally {Locale.setDefault(previousLocale);TimeZone.setDefault(previousZone);}
    }
}
