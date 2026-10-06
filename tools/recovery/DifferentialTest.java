import java.net.*;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.*;

/** Run the same deterministic inputs through release bytecode and recovered source. */
public class DifferentialTest {
 static ClassLoader reference, rebuilt; static int assertions=0;
 static Object[] copy(Object[] a){Object[] b=a.clone();for(int i=0;i<b.length;i++)if(b[i] instanceof byte[])b[i]=((byte[])b[i]).clone();return b;}
 static Object call(ClassLoader l,String cls,String name,Class<?>[] types,Object[] args)throws Exception{
  Method m=Class.forName(cls,true,l).getDeclaredMethod(name,types);m.setAccessible(true);
  try{return m.invoke(null,args);}catch(InvocationTargetException e){return "exception:"+e.getCause().getClass().getName();}
 }
 static void check(String cls,String name,Class<?>[] types,Object... a)throws Exception{
  Object[] left=copy(a),right=copy(a);Object x=call(reference,cls,name,types,left),y=call(rebuilt,cls,name,types,right);
  if(!Objects.deepEquals(x,y)||!Arrays.deepEquals(left,right))throw new AssertionError(cls+"."+name+" "+Arrays.deepToString(a)+" => "+x+" != "+y);
  assertions++;
 }
 static ClassLoader loader(String path,String libs)throws Exception{
  List<URL> urls=new ArrayList<>();urls.add(Paths.get(path).toUri().toURL());
  for(Path p:(Iterable<Path>)Files.list(Paths.get(libs)).filter(x->x.toString().endsWith(".jar"))::iterator)urls.add(p.toUri().toURL());
  return new URLClassLoader(urls.toArray(new URL[0]),ClassLoader.getPlatformClassLoader());
 }
 public static void main(String[] args)throws Exception{
  reference=loader(args[0],args[2]);rebuilt=loader(args[1],args[2]);
  Random random=new Random(700);
  String cls="recovered.C_cf";
  for(int n=0;n<10000;n++){
   byte[] b=new byte[16];random.nextBytes(b);int offset=n%13,value=random.nextInt();long lv=random.nextLong();boolean big=(n&1)==0;
   check(cls,"m_a_49634b7a",new Class[]{byte[].class,int.class},b,offset);
   check(cls,"m_a_e306d820",new Class[]{byte[].class,int.class,boolean.class},b,offset,big);
   check(cls,"m_a_e306d821",new Class[]{byte[].class,int.class,boolean.class},b,offset,big);
   check(cls,"m_a_e306985c",new Class[]{byte[].class,int.class,int.class},b,offset,value);
   check(cls,"m_a_7dcd25f8",new Class[]{byte[].class,int.class,int.class,boolean.class},b,offset,value,big);
   check(cls,"m_a_7dcd9a57",new Class[]{byte[].class,int.class,long.class,boolean.class},b,offset,lv,big);
   check(cls,"m_a_e3062636",new Class[]{byte[].class,int.class},b,offset);
  }
  for(int n=0;n<65536;n++)check("recovered.C_aj","m_a_132f95",new Class[]{char.class},(char)n);
  System.out.println("PASS: "+assertions+" differential checks (byte/word/dword/TLV helpers and all UTF-16 lowercase inputs)");
  System.exit(0);
 }
}
