import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

/** Real converter initialization, malformed resources/tables, longest matches and Character fallback. */
public class ConvertorProbe {
    static boolean ref;static URL[] urls;static URLClassLoader loader;static Class<?> type,io;static PrintWriter out;static int rows,conversions,parsedTables;
    static String n(String old,String name){return ref?old:name;}
    static Method method(String old,String name,Class<?> result,Class<?>...params)throws Exception{for(Method m:type.getDeclaredMethods())if(m.getName().equals(n(old,name))&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),params)){m.setAccessible(true);return m;}throw new NoSuchMethodException(name);}
    static Field field(String old,String name,Class<?> result)throws Exception{for(Field f:type.getDeclaredFields())if(f.getName().equals(n(old,name))&&f.getType()==result){f.setAccessible(true);return f;}throw new NoSuchFieldException(name);}
    static String units(String s){if(s==null)return "null";byte[] b=new byte[s.length()*2];for(int i=0;i<s.length();i++){b[i*2]=(byte)(s.charAt(i)>>8);b[i*2+1]=(byte)s.charAt(i);}return Base64.getEncoder().encodeToString(b);}
    static String value(Object o){if(o==null)return "null";if(o instanceof String)return "str:"+units((String)o);if(o instanceof Character)return "char:"+(int)((Character)o).charValue();if(o instanceof String[]){StringBuilder s=new StringBuilder("[");for(String v:(String[])o)s.append(units(v)).append(',');return s.append(']').toString();}return String.valueOf(o);}
    static Object failure(Throwable t){if(t instanceof Error)throw new AssertionError("Broken converter fixture",t);return "exception:"+t.getClass().getName();}
    static Object invoke(Method m,Object target,Object...args)throws Exception{try{return m.invoke(target,args);}catch(InvocationTargetException e){return failure(e.getCause());}}
    static void row(String s){out.println(s);rows++;}
    static void configure(byte[] data,int available,int limit,int failure)throws Exception{io.getField("bytes").set(null,data);io.getField("available").setInt(null,available);io.getField("readLimit").setInt(null,limit);io.getField("failure").setInt(null,failure);io.getField("closes").setInt(null,0);io.getField("log").set(null,new StringBuffer());}
    static void fresh(byte[] data,boolean cp,int available,int limit,int failure)throws Exception{
        if(loader!=null)loader.close();loader=new URLClassLoader(urls,ClassLoader.getPlatformClassLoader());io=Class.forName("ConvertorIO",true,loader);configure(data,available,limit,failure);
        Class<?> options=Class.forName(ref?"cj":"jimm.Options",true,loader);Object[] values=new Object[256];for(int i=0;i<256;i++)values[i]=i<64||i>=224?"":i<128?Integer.valueOf(0):i<192?Boolean.FALSE:Long.valueOf(0);values[133]=Boolean.valueOf(cp);for(Field f:options.getDeclaredFields())if(f.getType()==Object[].class){f.setAccessible(true);f.set(null,values);}
        type=Class.forName(ref?"bp":"jimm.util.StringConvertor",true,loader);
    }
    static String state(Object c)throws Exception{
        if(c==null||c instanceof String)return value(c);return units((String)field("a","name",String.class).get(c))+"/from:"+value(field("a","from",String[].class).get(c))+"/to:"+value(field("b","to",String[].class).get(c))+"/max:"+field("a","maxWordLength",int.class).getInt(c);
    }
    static void tables(String label)throws Exception{Object[] converters=(Object[])field("a","converters",Array.newInstance(type,0).getClass()).get(null);StringBuilder s=new StringBuilder(label).append("/tables:").append(converters.length);for(Object c:converters)s.append('/').append(state(c));s.append("/io:").append(io.getField("log").get(null)).append("/closes:").append(io.getField("closes").getInt(null));row(s.toString());}
    static void resource(byte[] original)throws Exception{
        for(boolean cp:new boolean[]{false,true})for(byte[] data:new byte[][]{null,new byte[0],original,"[transliterate]\r\na=b\r\n".getBytes("UTF-8"),"[transliterate]\n\u0430=a\n".getBytes("UTF-8"),new byte[]{(byte)255,0,(byte)192,(byte)194,(byte)128}})for(int available:new int[]{-2,-1,0,1,5})for(int limit:new int[]{-1,0,1,3})for(int failure=0;failure<4;failure++){
            fresh(data,cp,available,limit,failure);tables("resource-"+cp+"-"+(data==null?-1:data.length)+"-"+available+"-"+limit+"-"+failure);
            configure(data,available,limit,failure);Object loaded=invoke(method("c","loadFromResource",String.class,String.class),null,"/custom.txt");row("load:"+value(loaded)+"/io:"+io.getField("log").get(null)+"/closes:"+io.getField("closes").getInt(null));
        }
        fresh(original,true,-1,-1,0);tables("actual-resource");
    }
    static void characters()throws Exception{
        Method lower=method("a","toLowerCase",char.class,char.class),upper=method("e","toUpperCase",String.class,String.class),remove=method("d","removeCr",String.class,String.class);
        char[] all=new char[65536];for(int i=0;i<all.length;i++)all[i]=(char)i;
        for(int mode=0;mode<3;mode++){io.getField("caseMode").setInt(null,mode);for(int i=0;i<65536;i++)row("lower-"+mode+"-"+i+":"+value(invoke(lower,null,(char)i)));row("upper-all-"+mode+":"+value(invoke(upper,null,new String(all))));
            for(String s:new String[]{null,"","ASCII","ascii","\u00df\u00e0\u00f7\u00ff\u0400\u0430\u0451","\u0000\ud800\udfff"}){Object result=invoke(upper,null,s);row("upper-"+mode+"-"+units(s)+":"+value(result)+"/same:"+(result==s));}
        }io.getField("caseMode").setInt(null,0);
        for(String s:new String[]{null,"","a\u0000b","a\rb","a\u0000\rb","\r\n","a\nb","\u0000\r\u0000"}){Object result=invoke(remove,null,s);row("remove-"+units(s)+":"+value(result)+"/same:"+(result==s));}
    }
    static Object construct(String name,Vector from,Vector to)throws Exception{Constructor<?> c=type.getDeclaredConstructor(String.class,Vector.class,Vector.class);c.setAccessible(true);try{return c.newInstance(name,from,to);}catch(InvocationTargetException e){return failure(e.getCause());}}
    static void parse(String label,String content,boolean use)throws Exception{
        Vector v=new Vector();Object result=invoke(method("a","convertorParser",void.class,String.class,Vector.class),null,content,v);StringBuilder b=new StringBuilder(label).append(':').append(value(result));for(Object c:v)b.append('/').append(state(c));row(b.toString());parsedTables+=v.size();
        if(!use)return;Object array=Array.newInstance(type,v.size());for(int i=0;i<v.size();i++)Array.set(array,i,v.get(i));field("a","converters",array.getClass()).set(null,array);
        for(String scheme:new String[]{null,"","transliterate","detransliterate","test","duplicate","missing"})for(String text:new String[]{null,"","a","aa","ab","ABC","schSCHsh","\u041f\u0440\u0438\u0432\u0435\u0442","a\u0000b","\ud800\udfff"}){
            // A named zero-width table loops forever for nonempty text in both historical implementations.
            boolean unsafe=false;if(scheme!=null&&text!=null&&!text.isEmpty())for(Object c:v)if(scheme.equals(field("a","name",String.class).get(c))&&field("a","maxWordLength",int.class).getInt(c)==0)unsafe=true;
            if(unsafe){row(label+"/zero-width:"+units(scheme)+":"+units(text));continue;}
            Object converted=invoke(method("a","convert",String.class,String.class,String.class),null,scheme,text);row(label+"/convert-"+units(scheme)+"-"+units(text)+":"+value(converted)+"/same:"+(converted==text));conversions++;
        }
    }
    static void parser()throws Exception{
        String valid="[transliterate]\na=x\naa=y\nab=z\n[detransliterate]\nx=a\ny=aa\n";
        ArrayList<String> seeds=new ArrayList<>(Arrays.asList(null,"","[test]","[test]\n","[]\na=b\n","a=b\n[test]\nc=d\n",valid,"[test]\na=\n=empty\n","[test]\na=b","[test]\na=b#comment\nc=d;tail\n","[test]\na=b\r\nc=d\r\n","[test]\na=one\na=two\n","[duplicate]\na=first\n[duplicate]\na=second\n","[test]\na=b\n[test]\nab=long\n","[test]\na=b\n[broken","[ test ]\na = b \n","[test]\na=b\u0000\n"));
        for(int i=0;i<=valid.length();i++)seeds.add(valid.substring(0,i));for(int i=0;i<seeds.size();i++)parse("parse-"+i,seeds.get(i),true);
        Random r=new Random(20100512);String chars="[]#;=\r\n \tabc";for(int i=0;i<600;i++){StringBuilder s=new StringBuilder();for(int j=0,n=r.nextInt(150);j<n;j++)s.append(chars.charAt(r.nextInt(chars.length())));parse("parser-mutation-"+i,s.toString(),false);}
    }
    static void constructors()throws Exception{
        for(Vector from:new Vector[]{null,new Vector(),new Vector(Arrays.asList("a")),new Vector(Arrays.asList("a","abc")),new Vector(Arrays.asList((Object)null)),new Vector(Arrays.asList(1))})for(Vector to:new Vector[]{null,new Vector(),new Vector(Arrays.asList("x")),new Vector(Arrays.asList("x","y")),new Vector(Arrays.asList((Object)null)),new Vector(Arrays.asList(1))}){
            Object c=construct("test",from,to);row("constructor:"+value(from)+":"+value(to)+":"+state(c));if(c instanceof String)continue;
            for(String s:new String[]{null,"","a","A","abc","ABC","b"})row("convert-char-"+units(s)+":"+value(invoke(method("f","convertChar",String.class,String.class),c,s)));
        }
        Vector from=new Vector(Arrays.asList("a","ab")),to=new Vector(Arrays.asList("x","y"));Object c=construct("test",from,to);String[] left=(String[])field("a","from",String[].class).get(c),right=(String[])field("b","to",String[].class).get(c);from.set(0,"changed");to.clear();row("vector-copy:"+state(c)+"/same-elements:"+(left[1]==from.get(1)));left[0]="b";right[0]="z";row("array-alias:"+value(invoke(method("f","convertChar",String.class,String.class),c,"b")));row("max-not-recomputed:"+field("a","maxWordLength",int.class).getInt(c));
        Method vector=ref?method("a","vectorToArray",String[].class,Vector.class):method("a","vectorToArray",String[].class,Vector.class);for(Vector v:new Vector[]{null,new Vector(),new Vector(Arrays.asList("x",null)),new Vector(Arrays.asList(1))})row("vectorToArray:"+value(v)+":"+value(invoke(vector,ref?null:c,v)));
    }
    static void actual(byte[] original)throws Exception{
        fresh(original,true,-1,-1,0);String text="\u0430\u0431\u0432\u0433\u0434\u0435\u0451\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f";
        for(int mode=0;mode<3;mode++){io.getField("caseMode").setInt(null,mode);for(String s:new String[]{null,"",text,text.toUpperCase(Locale.ROOT),text+" ' * \"", "schSCHSch shSHSh khKHKh joJOJo zhZHZh","ASCII 123\n\r\u0000","\u010d\u4e2d\ud83d\ude00"})for(String[] name:new String[][]{{"a","detransliterate"},{"b","transliterate"}}){row("actual-"+mode+"-"+name[1]+"-"+units(s)+":"+value(invoke(method(name[0],name[1],String.class,String.class),null,s)));conversions++;}}
    }
    public static void main(String[] args)throws Exception{
        ref=args[2].equals("reference");ArrayList<URL> paths=new ArrayList<>();paths.add(Paths.get(args[0]).toUri().toURL());paths.add(Paths.get(args[1]).toUri().toURL());for(String lib:new String[]{"microemu.jar","microemu-jsr-75.jar","microemu-jsr-120.jar","microemu-nokiaui.jar"})paths.add(Paths.get(args[3],lib).toUri().toURL());urls=paths.toArray(new URL[0]);byte[] original;try(JarFile j=new JarFile(args[5])){original=j.getInputStream(j.getJarEntry("replaces.txt")).readAllBytes();}
        out=new PrintWriter(Files.newBufferedWriter(Paths.get(args[4]),java.nio.charset.StandardCharsets.UTF_8));resource(original);characters();constructors();parser();actual(original);if(conversions<500||parsedTables<50)throw new AssertionError("Converter guards: "+conversions+" "+parsedTables);row("conversions:"+conversions+"/parsed-tables:"+parsedTables);out.close();loader.close();System.out.println("PASS converter: "+rows+" observations, "+conversions+" conversions, "+parsedTables+" parsed tables");
    }
}
