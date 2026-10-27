import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.lcdui.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Observe complete options-menu activation with explicitly supplied option states. */
public class OptionsActivationProbe {
    static boolean ref;static ClassLoader loader;static Class<?> options,controller,text;
    static Field table,language,offline,empty,groups,menu,lines;static Method activate;
    static PrintWriter out;static int count;
    static Field field(Class<?> c,String a,String b,Class<?> t)throws Exception{return PopupProbe.field(c,ref?a:b,t);}
    static String encoded(Object value)throws Exception{return value==null?"null":Base64.getEncoder().encodeToString(value.toString().getBytes("UTF-8"));}
    static void exercise(String label,int cached,Object[] edits)throws Exception {
        Object[] values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);
        values[0]="12345";values[3]="RU";values[40]=new String(new char[12]);values[88]=37;values[98]=8;values[111]=8;values[173]=Boolean.TRUE;
        values[130]=Boolean.TRUE;values[129]=Boolean.TRUE;values[136]=Boolean.TRUE;table.set(null,values);
        Constructor<?> ctor=controller.getDeclaredConstructor();ctor.setAccessible(true);Object subject=ctor.newInstance();
        language.set(subject,"seed");offline.setBoolean(subject,cached!=0);empty.setBoolean(subject,cached!=0);groups.setBoolean(subject,cached!=0);
        for(int i=0;i<edits.length;i+=2)values[(Integer)edits[i]]=edits[i+1];
        String result="ok";try{activate.invoke(subject);}catch(InvocationTargetException e){result=e.getCause().getClass().getName();}
        Object list=menu.get(subject);
        out.println(label+":"+cached+":"+result+":"+encoded(language.get(subject))+":"+offline.getBoolean(subject)+":"+empty.getBoolean(subject)+":"+groups.getBoolean(subject)+":rows="+((Vector)lines.get(list)).size()+":values="+encoded(Arrays.deepToString(values)));count++;
    }
    static void cases()throws Exception {
        for(int cached=0;cached<2;cached++) {
            for(int mask=0;mask<8;mask++)exercise("valid:"+mask,cached,new Object[]{130,(mask&1)!=0,129,(mask&2)!=0,136,(mask&4)!=0});
            Object[] corrupt={null,"wrong",Integer.valueOf(1),Boolean.TRUE,Long.valueOf(1)};
            for(int option:new int[]{3,130,129,136,65})for(int i=0;i<corrupt.length;i++)exercise("typed:"+option+":"+i,cached,new Object[]{option,corrupt[i]});
            for(int sort:new int[]{Integer.MIN_VALUE,-1,0,1,2,Integer.MAX_VALUE})exercise("sort:"+sort,cached,new Object[]{65,sort});
            exercise("both:null-wrong",cached,new Object[]{129,null,136,"wrong"});
            exercise("both:wrong-null",cached,new Object[]{129,"wrong",136,null});
        }
        if(count!=82)throw new AssertionError(count);
    }
    public static void main(String[] args){try {
        ref=args[2].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        loader=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();options=Class.forName(ref?"cj":"jimm.Options",true,loader);table=field(options,"a","options",Object[].class);
        Object[] initial=new Object[256];for(int k=0;k<256;k++)initial[k]=k<64||k>=224?"":k<128?0:k<192?Boolean.FALSE:Long.valueOf(0);initial[3]="RU";initial[111]=8;table.set(null,initial);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"a","display",Display.class).set(null,Display.getDisplay(MIDletBridge.getCurrentMIDlet()));field(jimm,"a","VERSION",String.class).set(null,"0.7.0b");
        Class.forName(ref?"m":"jimm.ContactList",true,loader).getDeclaredConstructor().newInstance();
        controller=Class.forName(ref?"cg":"jimm.OptionsForm",true,loader);text=Class.forName(ref?"bi":"DrawControls.TextList",true,loader);
        language=field(controller,"a","lastUILang",String.class);offline=field(controller,"b","lastHideOffline",boolean.class);empty=field(controller,"c","lastHideEmpty",boolean.class);groups=field(controller,"a","lastGroupsUsed",boolean.class);
        menu=field(controller,"a","optionsMenu",text);lines=field(text,"a","lines",Vector.class);activate=PopupProbe.method(controller,ref?"c":"activate",void.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));CountDownLatch done=new CountDownLatch(1);Throwable[] failure={null};
        Display.getDisplay(MIDletBridge.getCurrentMIDlet()).callSerially(new Runnable(){public void run(){try{cases();}catch(Throwable e){failure[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Activation timed out");out.close();if(failure[0]!=null)throw new AssertionError(failure[0]);System.out.println("PASS options activation: "+count+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
