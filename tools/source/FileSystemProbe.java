import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Actual JSR75, Siemens and Motorola file adapters on the same scripted external API. */
public class FileSystemProbe extends GraphicsProbe {
    static Class<?> fs,impl,error;
    static boolean nativeMoto;
    static Object call(String name,Class<?> result,Object owner,Class<?>[] types,Object...args)throws Exception{
        try{return m(owner==null?impl:owner.getClass(),"a",name,result,types).invoke(owner,args);}catch(InvocationTargetException failure){Throwable e=failure.getCause();if(e instanceof Error)throw new AssertionError("Broken filesystem fixture: "+name,e);return "exception:"+(error.isInstance(e)?"JimmException:"+f(error,"a","_ErrCode",int.class).getInt(e):e.getClass().getName());}
    }
    static Object call(String name,Class<?> result,Object owner)throws Exception{return call(name,result,owner,new Class<?>[0]);}
    static void reset()throws Exception{io.getMethod("resetFiles").invoke(null);}
    static void failure(int code)throws Exception{io.getField("fileFailure").setInt(null,code);}
    static Object openFile(Object file,String path,int code)throws Exception{Object result=call("openFile",void.class,file,new Class<?>[]{String.class},path);if(code==1?!"exception:java.io.IOException".equals(result):result!=null)throw new AssertionError("Unexpected file open: "+result);return result;}
    static void trace(String label)throws Exception{row(label+":io="+io.getField("fileLog").get(null)+":closes="+io.getField("fileCloses").getInt(null)+":"+io.getField("inputCloses").getInt(null)+":"+io.getField("outputCloses").getInt(null));}
    static void directory(String path,String[] values)throws Exception{io.getMethod("directory",String.class,String[].class).invoke(null,path,values);}
    static void listings()throws Exception {
        String[][] data={{},{"file.txt"},{"Folder/","a.TXT","z/","a","a","\u041a\u0430\u0442\u0430\u043b\u043e\u0433/"},{null,"valid/"},{"../","../","a/","a"}};
        for(String[] roots:new String[][]{{},{"card/","phone/"},nativeMoto?new String[]{"/card/","/phone/"}:new String[]{"card/","phone/"}})for(int code:new int[]{0,12}){
            reset();io.getField("rootEntries").set(null,roots);failure(code);Object value=call("getDirectoryContents",String[].class,null,new Class<?>[]{String.class,boolean.class},"/",false);if(code==0&&!(value instanceof String[]))throw new AssertionError("Unexpected roots: "+value);String label="roots-"+Arrays.toString(roots)+"-"+code;row(label+":"+(value instanceof String[]?Arrays.toString((String[])value):value));trace(label);
        }
        for(String[] values:data)for(boolean dirs:new boolean[]{false,true})for(int code:new int[]{0,1,2,10}){
            reset();String[] supplied=values.clone();if(nativeMoto)for(int i=0;i<supplied.length;i++)if(supplied[i]!=null)supplied[i]="/card/"+supplied[i];directory("/card/",supplied);failure(code);String label="listing-"+Arrays.toString(values)+"-"+dirs+"-"+code;Object value=call("getDirectoryContents",String[].class,null,new Class<?>[]{String.class,boolean.class},"/card/",dirs);row(label+":"+(value instanceof String[]?Arrays.toString((String[])value):value));trace(label);
        }
    }
    static void lifecycle()throws Exception {
        for(boolean exists:new boolean[]{false,true})for(int code:new int[]{0,1,3,4,5,6,7,8,9,10}){
            reset();if(exists)io.getMethod("file",String.class,byte[].class).invoke(null,"/card/a.txt",new byte[]{0,1,2,127,(byte)255});Object file=instance(impl,new Class<?>[0]);String label="file-"+exists+"-"+code;row(label+"-unopened-size:"+call("fileSize",long.class,file));if(!target.equals("MOTOROLA"))row(label+"-unopened-name:"+call("getName",String.class,file));failure(code);row(label+"-open:"+openFile(file,"/card/a.txt",code));row(label+"-size:"+call("fileSize",long.class,file));if(!target.equals("MOTOROLA"))row(label+"-name:"+call("getName",String.class,file));
            Object input=call("openInputStream",InputStream.class,file);if(input instanceof InputStream){InputStream in=(InputStream)input;ByteArrayOutputStream bytes=new ByteArrayOutputStream();for(int c;(c=in.read())!=-1;)bytes.write(c);row(label+"-read:"+Base64.getEncoder().encodeToString(bytes.toByteArray()));in.close();}else row(label+"-read:"+input);
            Object output=call("openOutputStream",OutputStream.class,file);if(output instanceof OutputStream){OutputStream out=(OutputStream)output;try{out.write(new byte[]{9,8,7});out.flush();row(label+"-write:ok");}catch(IOException e){row(label+"-write:"+e.getClass().getName());}finally{out.close();}}else row(label+"-write:"+output);
            row(label+"-close:"+call("close",void.class,file));row(label+"-close-again:"+call("close",void.class,file));byte[] bytes=(byte[])((Hashtable)io.getField("files").get(null)).get("/card/a.txt");row(label+"-bytes:"+(bytes==null?"null":Base64.getEncoder().encodeToString(bytes)));trace(label);
        }
        // A second open replaces the connection; close does not clear its field.
        reset();Object file=instance(impl,new Class<?>[0]);for(String path:new String[]{"/card/a.txt","/phone/other.bin",""}){row("reopen-"+enc(path)+":"+call("openFile",void.class,file,new Class<?>[]{String.class},path));row("reopen-close:"+call("close",void.class,file));}trace("reopen");
    }
    static void factory()throws Exception {
        if(!target.equals("MOTOROLA"))return;
        Object file=m(fs,"a","getInstance",fs).invoke(null);if(file.getClass()!=impl)throw new AssertionError("Wrong selected filesystem");row("factory:true");
        reset();io.getField("rootEntries").set(null,nativeMoto?new String[]{"/card/"}:new String[]{"card/"});Object values=m(fs,"a","getDirectoryContents",String[].class,String.class,boolean.class).invoke(null,"/",false);row("factory-list:"+Arrays.toString((String[])values));
        for(long size:new long[]{-1,0,1023,1024,Long.MIN_VALUE,Long.MAX_VALUE}){reset();io.getField("capacity").setLong(null,size);row("factory-capacity-"+size+":"+m(fs,"a","totalSize",long.class,String.class).invoke(null,"card/"));trace("factory-capacity-"+size);}
    }
    static void exerciseFiles()throws Exception {
        io=load("FileSystemIO","FileSystemIO");fs=load("w","jimm.FileSystem");nativeMoto=target.equals("MOTOROLA")&&!Boolean.getBoolean("jimm.files.jsr");impl=load(nativeMoto?"n":"b",nativeMoto?"jimm.MotorolaFileSystem":"jimm.JSR75FileSystem");error=load(target.equals("MIDP2")?"bv":target.equals("MOTOROLA")?"bu":"bt","jimm.JimmException");listings();lifecycle();factory();
    }
    public static void main(String[] args){try {
        ref=args[1].equals("reference");target=args[3];Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet midlet=MIDletBridge.getCurrentMIDlet();loader=midlet.getClass().getClassLoader();out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));exerciseFiles();out.close();System.out.println("PASS files: "+observations+" observations");System.exit(0);
    }catch(Throwable failure){failure.printStackTrace();System.exit(1);}}
}
