import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import sun.misc.Unsafe;

/** Observe untouched initializers through actual System properties and partial static state. */
public final class PlatformInitializationProbe {
    static Unsafe unsafe;static Properties spec;static Class<?> jimm;static ArrayList<String> trace;
    static final String[] roles={"microeditionPlatform","microeditionProfiles","sonyEricssonPlatform","is_phone_FLY","is_phone_SE","is_phone_NOKIA","is_smart_SE","supportsNokiaLight","timer"};
    static Field[] fields;
    static Object read(Field f){Object base=unsafe.staticFieldBase(f);long offset=unsafe.staticFieldOffset(f);return f.getType()==boolean.class?Boolean.valueOf(unsafe.getBoolean(base,offset)):unsafe.getObject(base,offset);}
    static String text(Object value){return value==null?"null":Base64.getEncoder().encodeToString(String.valueOf(value).getBytes(StandardCharsets.UTF_8));}
    static String snapshot(){StringBuilder s=new StringBuilder();for(int i=0;i<fields.length;i++){Object v=read(fields[i]);if(i==8)v=Boolean.valueOf(v!=null);s.append('/').append(text(v));}return s.toString();}
    static final class Inputs extends Properties {
        final String platform,profile,sony;final int failure;
        Inputs(Properties base,String p,String r,String s,int f){putAll(base);platform=p;profile=r;sony=s;failure=f;}
        public String getProperty(String key){
            int which=key.equals("microedition.platform")?0:key.equals("microedition.profiles")?1:key.equals("com.sonyericsson.java.platform")?2:-1;
            if(which<0)return super.getProperty(key);
            trace.add(which+snapshot());
            if(failure>0&&(failure-1)/2==which){if((failure&1)==1)throw new SecurityException("property input");throw new AssertionError("property input");}
            return which==0?platform:which==1?profile:sony;
        }
    }
    public static void main(String[] args)throws Exception {
        Field u=Unsafe.class.getDeclaredField("theUnsafe");u.setAccessible(true);unsafe=(Unsafe)u.get(null);
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Properties original=System.getProperties();Locale originalLocale=Locale.getDefault();Path libs=Paths.get(args[1]);List<String> rows=new ArrayList<String>(),jp8Rows=new ArrayList<String>();
        String[] platforms={null,"","FLY","NOKIA","Sony Ericsson","Sony Ericsson Nokia FLY","ERICSSON NOKIA","Sony Ericsson I","SJP Nokia","Nokia FLY Ericsson"};
        String[] profiles={null,"MIDP-2.0"};String[] sony={null,"","JP-8","SJP","JP-8 SJP I"};
        try {
            for(Locale locale:new Locale[]{Locale.US,new Locale("tr","TR")})for(int p=0;p<platforms.length;p++)for(int r=0;r<profiles.length;r++)for(int s=0;s<sony.length;s++)for(int failure=0;failure<7;failure++) {
                Locale.setDefault(locale);
                try(URLClassLoader loader=new URLClassLoader(new URL[]{Paths.get(args[0]).toUri().toURL(),libs.resolve("microemu.jar").toUri().toURL(),libs.resolve("microemu-nokiaui.jar").toUri().toURL(),libs.resolve("microemu-jsr-75.jar").toUri().toURL()},ClassLoader.getPlatformClassLoader())) {
                    jimm=Class.forName("jimm.Jimm",false,loader);fields=new Field[roles.length];
                    for(int i=0;i<roles.length;i++) {Class<?> type=i<3?String.class:i<8?boolean.class:java.util.Timer.class;for(Field f:jimm.getDeclaredFields())if(f.getName().equals(spec.getProperty(roles[i]))&&f.getType()==type){if(fields[i]!=null)throw new AssertionError("Ambiguous field");fields[i]=f;}if(fields[i]==null)throw new AssertionError("Missing "+roles[i]);}
                    trace=new ArrayList<String>();System.setProperties(new Inputs(original,platforms[p],profiles[r],sony[s],failure));String outcome="ok";
                    try{Class.forName("jimm.Jimm",true,loader);}catch(Throwable t){if(t instanceof LinkageError&&!(t instanceof ExceptionInInitializerError))throw(LinkageError)t;outcome=t.getClass().getName()+(t.getCause()==null?"":"/"+t.getCause().getClass().getName());}
                    finally{System.setProperties(original);Object timer=read(fields[8]);if(timer!=null)((java.util.Timer)timer).cancel();}
                    rows.add(locale.toString()+":"+p+":"+r+":"+s+":"+failure+":"+outcome+":"+trace+":"+snapshot());
                    if(spec.containsKey("is_JP8")){Field jp8=null;for(Field f:jimm.getDeclaredFields())if(f.getName().equals(spec.getProperty("is_JP8"))&&f.getType()==boolean.class)jp8=f;if(jp8==null)throw new AssertionError("Missing actual JP8 field");jp8Rows.add(locale.toString()+":"+p+":"+r+":"+s+":"+failure+":"+read(jp8));}
                }
            }
        }finally{System.setProperties(original);Locale.setDefault(originalLocale);}
        Files.write(Paths.get(args[3]),(String.join("\n",rows)+"\n").getBytes(StandardCharsets.UTF_8));
        if(!jp8Rows.isEmpty())Files.write(Paths.get(args[3].substring(0,args[3].length()-4)+".jp8.txt"),(String.join("\n",jp8Rows)+"\n").getBytes(StandardCharsets.UTF_8));
        System.out.println("PASS platform initializer: "+rows.size()+" observations of complete unmodified classes, real System property callbacks and partial state");
    }
}
