import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

/** Real language class initialization, switches, lazy loading, partial dictionaries and failures. */
public class ResourceProbe {
    static boolean ref;static Path jar;static PrintWriter out;static int rows;static URLClassLoader loader;static Class<?> resource,io;static Method get,set;
    static Field field(Class<?> c,String old,String name,Class<?> type)throws Exception{for(Field f:c.getDeclaredFields())if(f.getName().equals(ref?old:name)&&f.getType()==type){f.setAccessible(true);return f;}throw new NoSuchFieldException(name);}
    static Method method(String old,String name,Class<?> result,Class<?>...args)throws Exception{for(Method m:resource.getDeclaredMethods())if(m.getName().equals(ref?old:name)&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),args)){m.setAccessible(true);return m;}throw new NoSuchMethodException(name);}
    static String enc(Object value)throws Exception{return value==null?"null":Base64.getEncoder().encodeToString(value.toString().getBytes("UTF-8"));}
    static void row(String value){out.println(value);rows++;}
    static byte[] list(int declared,String...languages)throws Exception{ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream stream=new DataOutputStream(bytes);stream.writeShort(declared);for(String language:languages)stream.writeUTF(language);return bytes.toByteArray();}
    static byte[] pack(int declared,String...pairs)throws Exception{ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream stream=new DataOutputStream(bytes);stream.writeShort(declared);for(String pair:pairs)stream.writeUTF(pair);return bytes.toByteArray();}
    static void fresh(byte[] available,Hashtable<String,byte[]> files,int failure,boolean close)throws Exception{
        if(loader!=null)loader.close();loader=new URLClassLoader(new URL[]{jar.toUri().toURL()},ClassLoader.getPlatformClassLoader());io=Class.forName("ResourceIO",true,loader);Hashtable map=(Hashtable)io.getField("files").get(null);if(available!=null)map.put("/langlist.lng",available);map.putAll(files);io.getField("failureAt").setInt(null,failure);io.getField("failClose").setBoolean(null,close);resource=Class.forName(ref?"ai":"jimm.util.ResourceBundle",false,loader);
    }
    static Object invoke(Method method,Object...args)throws Exception{try{return method.invoke(null,args);}catch(InvocationTargetException e){Throwable failure=e.getCause();if(failure instanceof LinkageError)throw new AssertionError("Broken resource fixture",failure);return "exception:"+failure.getClass().getName();}}
    static String table()throws Exception{Hashtable map=(Hashtable)field(resource,"a","resources",Hashtable.class).get(null);if(map==null)return "null";ArrayList<String> entries=new ArrayList<>();for(Object k:map.keySet())entries.add(enc(k)+"="+enc(map.get(k)));Collections.sort(entries);return entries.toString();}
    static void snapshot(String label)throws Exception{row(label+":available="+Arrays.toString((String[])field(resource,"a","langAvailable",String[].class).get(null))+":current="+enc(field(resource,"a","currUiLanguage",String.class).get(null))+":resources="+table()+":io="+io.getField("log").get(null)+":closes="+io.getField("closes").getInt(null));}
    static boolean initialize(String label)throws Exception{try{Class.forName(resource.getName(),true,loader);get=method("a","getString",String.class,String.class);set=method("a","setCurrUiLanguage",void.class,String.class);snapshot(label);return true;}catch(ExceptionInInitializerError e){row(label+":exception:"+e.getCause().getClass().getName()+":io="+io.getField("log").get(null));return false;}}
    static void lookup(String label,String key)throws Exception{Object value=invoke(get,new Object[]{key});row(label+":"+enc(value));}
    static void scripted()throws Exception{
        Hashtable<String,byte[]> files=new Hashtable<>();files.put("/RU.lng",pack(4,"a","first","b","\u041f\u0440\u0438\u0432\u0435\u0442\u0000\ud83d\ude00","a","last","empty",""));files.put("/EN.lng",pack(1,"a","english"));files.put("/CZ.lng",pack(2,"a","\u010desky","b","line\ntext"));
        for(byte[] available:new byte[][]{null,new byte[0],list(-1),list(0),list(1,"RU"),list(2,"RU"),list(3,"RU","EN","CZ"),list(2,"","RU"),list(2,"RU","RU")})for(int failure:new int[]{-1,0,1,2,4})for(boolean close:new boolean[]{false,true}){
            fresh(available,files,failure,close);String label="init-"+Base64.getEncoder().encodeToString(available==null?new byte[]{-1}:available)+"-"+failure+"-"+close;if(!initialize(label))continue;
            for(String key:new String[]{null,"a","b","empty","missing"})lookup(label+"-get-"+enc(key),key);snapshot(label+"-loaded");
            for(String language:new String[]{"RU","EN","CZ","unknown",null,"","RU"}){row(label+"-switch-"+enc(language)+":"+invoke(set,new Object[]{language}));snapshot(label+"-switched");lookup(label+"-after-switch","a");}
        }
        byte[] valid=pack(3,"a","one","b","two","c","three");
        for(int cut=0;cut<=valid.length;cut++)for(boolean close:new boolean[]{false,true}){
            files.clear();files.put("/RU.lng",Arrays.copyOf(valid,cut));fresh(list(1,"RU"),files,-1,close);String label="truncated-"+cut+"-"+close;if(!initialize(label))throw new AssertionError("Valid list failed");for(String key:new String[]{null,"a","b","c","missing"})lookup(label+"-get-"+enc(key),key);snapshot(label+"-loaded");lookup(label+"-again","a");snapshot(label+"-cached");
        }
        for(byte[] bytes:new byte[][]{pack(-1),pack(0),pack(1,"a","value"),pack(2,"a","first","a","last"),new byte[]{0,1,0,1,(byte)0xff,0,1,65}})for(int failure:new int[]{-1,0,1,2,3,4,8}){
            files.clear();files.put("/RU.lng",bytes);fresh(list(1,"RU"),files,-1,false);if(!initialize("failure-init"))throw new AssertionError();io.getField("failureAt").setInt(null,failure);lookup("pack-failure-"+Base64.getEncoder().encodeToString(bytes)+"-"+failure,"a");snapshot("pack-failure-state");
        }
        files.clear();fresh(list(2,"RU","EN"),files,-1,false);if(!initialize("missing-pack-init"))throw new AssertionError();lookup("missing-pack","a");snapshot("missing-pack-state");((Hashtable)io.getField("files").get(null)).put("/RU.lng",pack(1,"a","late"));lookup("late-with-cached-empty","a");field(resource,"a","resources",Hashtable.class).set(null,null);lookup("late-after-null-cache","a");snapshot("late-loaded");
        // The shipped optimizer specializes the flags overload to FLAG_ELLIPSIS.
        Method dots=ref?method("a$7a1ba7c4","getString",String.class,String.class):method("a","getString",String.class,String.class,int.class);
        for(String key:new String[]{null,"a","unknown"})row("ellipsis-"+enc(key)+":"+enc(ref?invoke(dots,new Object[]{key}):invoke(dots,key,1)));
    }
    static void actual(Path packs,Path keys)throws Exception{
        Hashtable<String,byte[]> files=new Hashtable<>();String[] languages={"RU","UA","RO","EN","CZ"};for(String language:languages)files.put("/"+language+".lng",Files.readAllBytes(packs.resolve(language+".lng")));fresh(list(5,languages),files,-1,false);if(!initialize("actual-init"))throw new AssertionError();
        for(String language:languages){Object result=invoke(set,language);if(result!=null)throw new AssertionError("Actual language switch failed");for(String line:Files.readAllLines(keys.resolve(language+".tsv"))){String[] parts=line.split("\t",-1);String old=new String(Base64.getDecoder().decode(parts[0]),"UTF-8"),name=new String(Base64.getDecoder().decode(parts[1]),"UTF-8"),expected=new String(Base64.getDecoder().decode(parts[2]),"UTF-8");Object value=invoke(get,ref?old:name);if(!expected.equals(value))throw new AssertionError("Actual lookup: "+language+" "+name+" "+value);row("actual-"+language+"-"+parts[3]+":"+enc(value));}row("actual-language:"+enc(field(resource,"a","currUiLanguage",String.class).get(null)));}
    }
    public static void main(String[] args)throws Exception{jar=Paths.get(args[0]);ref=args[1].equals("reference");out=new PrintWriter(Files.newBufferedWriter(Paths.get(args[2]),java.nio.charset.StandardCharsets.UTF_8));scripted();actual(Paths.get(args[3]),Paths.get(args[4]));out.close();if(loader!=null)loader.close();System.out.println("PASS language loader: "+rows+" observations");}
}
