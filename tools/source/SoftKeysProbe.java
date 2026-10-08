import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Invoke unchanged application methods and observe their actual partial field writes. */
public class SoftKeysProbe {
    static Properties spec;static PrintWriter out;static Field table;static Method assign;static Field[] fields,devices;
    static final String[] roles={"leftOffset","rightOffset","MENU_LEFT_BAR","MENU_RIGHT_BAR","MENU_LEFT","xStatusOnRight","fontView"};
    static int observations;
    static Object[] values(boolean swap,boolean status,int font,int offset) {
        Object[] v=new Object[256];
        for(int k=0;k<v.length;k++)v[k]=k<64||k>=224?"seed"+k:k<128?Integer.valueOf(k*7):k<192?Boolean.valueOf((k&1)==0):Long.valueOf(Long.MIN_VALUE+k);
        v[143]=Boolean.valueOf(swap);v[185]=Boolean.valueOf(status);v[118]=Integer.valueOf(font);
        v[95]=Integer.valueOf(offset==0?-2:Integer.MAX_VALUE);v[96]=Integer.valueOf(offset==0?0:Integer.MIN_VALUE);return v;
    }
    static Object[] broken(Object[] v,int scenario) {
        if(scenario==1)return null;
        if(scenario>=2&&scenario<=8)return Arrays.copyOf(v,new int[]{0,95,96,118,143,185,186}[scenario-2]);
        if(scenario>=9) {v[new int[]{95,96,143,185,118}[(scenario-9)/2]]=(scenario&1)==1?null:"wrong type";}
        return v;
    }
    static String call()throws Exception {
        try{assign.invoke(null);return "ok";}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return t.getClass().getName();}
    }
    static void exercise()throws Exception {
        for(int device=0;device<(1<<devices.length);device++)for(boolean swap:new boolean[]{false,true})for(boolean status:new boolean[]{false,true})
            for(int font:new int[]{-2,1,Integer.MAX_VALUE})for(int offset=0;offset<2;offset++)for(int scenario=0;scenario<19;scenario++) {
                for(int i=0;i<devices.length;i++)devices[i].setBoolean(null,(device&(1<<i))!=0);
                for(int i=0;i<fields.length;i++)if(fields[i].getType()==boolean.class)fields[i].setBoolean(null,!status);else fields[i].setInt(null,-301-i);
                for(int step=0;step<3;step++) {
                    Object[] v=values(swap,status,font,offset);if(step!=1)v=broken(v,scenario);Object[] saved=v==null?null:v.clone();table.set(null,v);
                    String result=call();if(table.get(null)!=v||!Arrays.equals(v,saved))throw new AssertionError("Subject changed actual option table");
                    StringBuilder row=new StringBuilder();row.append(device).append(':').append(swap).append(':').append(status).append(':').append(font).append(':').append(offset).append(':').append(scenario).append(':').append(step).append(':').append(result);
                    for(Field f:fields)row.append(':').append(f.get(null));out.write(row.toString()+"\n");observations++;
                }
            }
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> p=new ArrayList<String>();Collections.addAll(p,"--rms","memory",args[0]);
        emulator.initParams(p,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();ClassLoader loader=host.getClass().getClassLoader();Class<?> options=Class.forName(spec.getProperty("options"),true,loader);
        table=PopupProbe.field(options,spec.getProperty("table"),Object[].class);table.set(null,values(false,false,1,0));
        Class<?> vl=Class.forName(spec.getProperty("vl"),true,loader);assign=PopupProbe.method(vl,spec.getProperty("assign"),void.class);
        fields=new Field[roles.length];for(int i=0;i<roles.length;i++)fields[i]=PopupProbe.field(vl,spec.getProperty(roles[i]),i==5?boolean.class:int.class);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);String names=spec.getProperty("devices");String[] flags=names.length()==0?new String[0]:names.split(",");devices=new Field[flags.length];
        for(int i=0;i<flags.length;i++)devices[i]=PopupProbe.field(jimm,flags[i],boolean.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));exercise();out.close();
        System.out.println("PASS soft-key state: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
