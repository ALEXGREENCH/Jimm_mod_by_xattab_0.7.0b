import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

/** Genuine Util.writeByteArray calls, stream side effects and virtual printStackTrace dispatch. */
public class UtilWriteProbe {
    static int prints;
    static class IoFailure extends IOException {public void printStackTrace(){prints++;}}
    static class RuntimeFailure extends IllegalStateException {public void printStackTrace(){prints++;}}
    static class FatalFailure extends AssertionError {public void printStackTrace(){prints++;}}
    static class Sink extends ByteArrayOutputStream {
        final int fail;int writes;
        Sink(int fail){this.fail=fail;}
        public void write(byte[] data)throws IOException{
            writes++;
            if(fail>=4)super.write(new byte[]{12,34});
            if(fail==1||fail==4)throw new IoFailure();
            if(fail==2||fail==5)throw new RuntimeFailure();
            if(fail==3)throw new FatalFailure();
            super.write(data);
        }
    }
    public static void main(String[] args)throws Exception{
        ClassLoader loader=new URLClassLoader(new URL[]{Paths.get(args[0]).toUri().toURL(),Paths.get(args[3],"microemu.jar").toUri().toURL(),Paths.get(args[3],"microemu-nokiaui.jar").toUri().toURL()},ClassLoader.getPlatformClassLoader());
        Class<?> util=Class.forName(args[1].equals("reference")?"co":"jimm.comm.Util",true,loader);
        Method method=util.getDeclaredMethod(args[1].equals("reference")?"a":"writeByteArray",ByteArrayOutputStream.class,byte[].class);method.setAccessible(true);
        List<String> rows=new ArrayList<>();PrintStream stderr=System.err;
        for(int size:new int[]{-1,0,1,2,16,255,256,1024})for(int fail=0;fail<=5;fail++)for(boolean absent:new boolean[]{false,true}){
            byte[] bytes=size<0?null:new byte[size];if(bytes!=null)for(int i=0;i<size;i++)bytes[i]=(byte)(i*37+11);
            Sink sink=new Sink(fail);prints=0;ByteArrayOutputStream diagnostic=new ByteArrayOutputStream();String error="none";
            System.setErr(new PrintStream(diagnostic));
            try{method.invoke(null,absent?null:sink,bytes);}catch(InvocationTargetException e){error=e.getCause().getClass().getName();}finally{System.setErr(stderr);}
            rows.add(size+"/"+fail+"/"+absent+":writes="+sink.writes+":bytes="+Base64.getEncoder().encodeToString(sink.toByteArray())+":prints="+prints+":stderr="+(diagnostic.size()>0)+":error="+error);
        }
        Files.write(Paths.get(args[2]),rows,java.nio.charset.StandardCharsets.UTF_8);System.out.println("PASS utility writes: "+rows.size()+" observations");System.exit(0);
    }
}
