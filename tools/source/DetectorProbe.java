import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;
import org.microemu.*;
public class DetectorProbe {
 static Method method(Class<?> c,String n,Class<?> r,Class<?>... p)throws Exception {
  for(Method m:c.getDeclaredMethods())if(m.getName().equals(n)&&m.getReturnType()==r&&Arrays.equals(m.getParameterTypes(),p)){m.setAccessible(true);return m;}
  throw new NoSuchMethodException(c+"."+n);
 }
 public static void main(String[] args) {
  try { run(args);System.exit(0); }catch(Throwable e){e.printStackTrace();System.exit(1);}
 }
 static void run(String[] args)throws Exception {
  boolean ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emu=(Common)ef.get(h);
  ArrayList<String> a=new ArrayList<String>();Collections.addAll(a,"--rms","memory",args[0]);
  emu.initParams(a,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emu.initMIDlet(true);
  ClassLoader l=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();
  Class<?> util=Class.forName(ref?"co":"jimm.comm.Util",true,l),contact=Class.forName(ref?"z":"jimm.ContactItem",true,l),list=Class.forName(ref?"m":"jimm.ContactList",true,l);
  if(ref){
   ArrayList<byte[]> fixtures=new ArrayList<byte[]>();fixtures.add(new byte[0]);
   for(Field f:util.getDeclaredFields())if(f.getType()==byte[].class&&Modifier.isStatic(f.getModifiers())){f.setAccessible(true);byte[] v=(byte[])f.get(null);if(v!=null&&v.length<=16&&v.length>0){byte[] cap=new byte[16];System.arraycopy(v,0,cap,0,v.length);fixtures.add(cap);}}
   Random random=new Random(700);int known=fixtures.size();
   for(int i=0;i<300;i++){byte[] caps=new byte[16*(1+random.nextInt(8))];for(int p=0;p<caps.length;p+=16){byte[] cap=fixtures.get(1+random.nextInt(known-1));System.arraycopy(cap,0,caps,p,16);if((i&3)==0)caps[p+15]=(byte)random.nextInt();}fixtures.add(caps);}
   DataOutputStream out=new DataOutputStream(new FileOutputStream(args[2]));out.writeInt(fixtures.size());for(byte[] v:fixtures){out.writeInt(v.length);out.write(v);}out.close();
  }
  Field items=null;for(Field f:list.getDeclaredFields())if(f.getName().equals(ref?"a":"cItems")&&f.getType()==Vector.class){f.setAccessible(true);items=f;}
  Method set=method(contact,ref?"a":"setStringValue",void.class,int.class,String.class),getInt=method(contact,ref?"b":"getIntValue",int.class,int.class),getString=method(contact,ref?"a":"getStringValue",String.class,int.class);
  Method detect=method(util,ref?"a":"detectUserClient",void.class,String.class,int.class,int.class,int.class,byte[].class,int.class,boolean.class);
  Method status=method(util,ref?"a":"translateStatusReceived",int.class,int.class,contact);
  Constructor<?> ctor=contact.getDeclaredConstructor();ctor.setAccessible(true);
  DataInputStream input=new DataInputStream(new FileInputStream(args[2]));int count=input.readInt();PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[3]),"UTF-8"));
  int[] fp={0,-1,-2,-129,-2458,0x08000905,0x06022009,0x3BA8DBAF,9034};
  for(int i=0;i<count;i++){byte[] caps=new byte[input.readInt()];input.readFully(caps);
   for(int n=0;n<fp.length;n++){
    Object item=ctor.newInstance();set.invoke(item,0,"12345");Vector<Object> contacts=new Vector<Object>();contacts.add(item);items.set(null,contacts);
    detect.invoke(null,"12345",fp[n],fp[(n+1)%fp.length],fp[(n+2)%fp.length],caps, n+2,false);
    String version=(String)getString.invoke(item,3);
    out.print(i+":"+n+":"+getInt.invoke(item,76)+":"+getInt.invoke(item,75)+":"+Base64.getEncoder().encodeToString((version==null?"<null>":version).getBytes("UTF-8")));
    for(int s:new int[]{-1,0,1,2,4,16,32,256,512,8193,16384})out.print(":"+status.invoke(null,s,item));
    out.println();
   }
  }out.close();input.close();System.out.println("PASS probe: "+count*fp.length+" detector/status cases");
 }
}
