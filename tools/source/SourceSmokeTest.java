import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;
import org.microemu.*;
public class SourceSmokeTest {
 static Object field(Object o,String name)throws Exception {Field f=o.getClass().getDeclaredField(name);f.setAccessible(true);return f.get(o);}
 static void command(Object o,Object cmd)throws Exception {for(Method m:o.getClass().getDeclaredMethods())if(m.getName().equals("commandAction")){m.setAccessible(true);m.invoke(o,cmd,null);return;}throw new AssertionError();}
 public static void main(String[] args) {
  try { run(args); System.exit(0); }
  catch(Throwable failure) { failure.printStackTrace(); System.exit(1); }
 }
 public static void run(String[] args)throws Exception {
  Headless h=new Headless();Common c=(Common)field(h,"emulator");
  ArrayList<String> a=new ArrayList<String>();Collections.addAll(a,"--rms","memory",args[0]);
  c.initParams(a,new DeviceEntry("Default device",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);
  c.initMIDlet(true);
  Object midlet=MIDletBridge.getCurrentMIDlet();
  if(midlet==null||!midlet.getClass().getName().equals("jimm.Jimm"))throw new AssertionError("Jimm did not start");
  ClassLoader l=midlet.getClass().getClassLoader();Class<?> opts=Class.forName("jimm.Options",true,l),ui=Class.forName("jimm.JimmUI",true,l);
  Object select=ui.getField("cmdSelect").get(null);
  // Open and save every ordinary options panel, using in-memory RMS and no connection.
  final Throwable[] failure=new Throwable[1];
  for(final int index:new int[]{0,1,2,3,6,7,8,9,10,12}) {
   CountDownLatch done=new CountDownLatch(1);
   Display.getDisplay((MIDlet)midlet).callSerially(new Runnable(){public void run(){try {
   opts.getMethod("editOptions").invoke(null);Object controller=opts.getField("optionsForm").get(null);Object menu=field(controller,"optionsMenu");
   menu.getClass().getMethod("selectTextByIndex",int.class).invoke(menu,index);
   command(controller,select);command(controller,ui.getField("cmdSave").get(null));
   } catch(Throwable error) { failure[0]=error; } finally { done.countDown(); } }});
   if(!done.await(10,TimeUnit.SECONDS))throw new AssertionError("UI timeout " + index);
   if(failure[0]!=null)throw new AssertionError("options panel " + index, failure[0]);
  }
  System.out.println("PASS: MIDP2 startup and 10 options panels opened/saved with memory RMS");
 }
}
