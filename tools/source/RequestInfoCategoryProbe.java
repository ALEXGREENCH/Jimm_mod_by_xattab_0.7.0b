import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Actual optimized category methods and each JAR's own real language loader. */
public class RequestInfoCategoryProbe {
    static boolean ref;
    static int observations;
    static PrintWriter out;
    static String n(String a,String b){return ref?a:b;}
    static Field f(Class<?> owner,String a,String b,Class<?> type)throws Exception{for(Field f:owner.getDeclaredFields())if(f.getName().equals(n(a,b))&&f.getType()==type){f.setAccessible(true);return f;}throw new NoSuchFieldException(owner+"."+n(a,b));}
    static Method m(Class<?> owner,String a,String b,Class<?> result,Class<?>...types)throws Exception{for(Method m:owner.getDeclaredMethods())if(m.getName().equals(n(a,b))&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),types)){m.setAccessible(true);return m;}throw new NoSuchMethodException(owner+"."+n(a,b));}
    static String units(String text){StringBuilder b=new StringBuilder();for(int i=0;i<text.length();i++){String s=Integer.toHexString(text.charAt(i));for(int j=s.length();j<4;j++)b.append('0');b.append(s);}return b.toString();}
    static void row(String s){out.println(s);observations++;}
    static Class<?> request,bundle;
    static Method select,key,code,name,item,lookup;
    static String invoke(Method method,Object...args)throws Exception{
        try{Object result=method.invoke(null,args);return result==null?"null":result instanceof String?units((String)result):String.valueOf(result);}
        catch(InvocationTargetException x){if(x.getCause() instanceof Error)throw new AssertionError("Broken category fixture",x.getCause());return "exception:"+x.getCause().getClass().getName();}
    }
    static void run(String[] args)throws Exception{
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet();ClassLoader loader=midlet.getClass().getClassLoader();request=Class.forName(n("ce","jimm.comm.RequestInfoAction"),true,loader);bundle=Class.forName(n("ai","jimm.util.ResourceBundle"),true,loader);
        select=m(request,"a","getSelectIndex",int.class,int.class);key=m(request,"b","getCategoriesString",String.class,int.class);code=m(request,"b","getCategoriesCode",int.class,int.class);name=m(request,"a","getCategoriesName",String.class,int.class);item=m(request,"a","initInterestsDataItem",void.class,String.class,int.class,int.class);lookup=m(bundle,"a","getString",String.class,String.class);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        int[] codes=(int[])f(request,"b","codeIndexes",int[].class).get(null);String[] names=(String[])f(request,"a","interestNames",String[].class).get(null);
        if(codes.length!=51||names.length!=51)throw new AssertionError("incomplete native categories");
        for(int i=0;i<51;i++)row("native-"+i+":"+invoke(code,i)+":"+invoke(name,i)+":"+invoke(lookup,names[i]));
        for(int i=-32768;i<=65535;i++)row("lookup-"+i+":"+invoke(select,i)+":"+invoke(key,i));
        for(int i:new int[]{Integer.MIN_VALUE,-32769,-1,0,50,51,52,65536,Integer.MAX_VALUE})row("boundary-"+i+":"+invoke(select,i)+":"+invoke(key,i)+":"+invoke(code,i)+":"+invoke(name,i));
        int[] originalCodes=codes.clone();String[] originalNames=names.clone();
        for(int length:new int[]{-1,0,1,50,51})for(int index:new int[]{-1,0,1,49,50,51}){
            f(request,"b","codeIndexes",int[].class).set(null,length<0?null:Arrays.copyOf(originalCodes,length));f(request,"a","interestNames",String[].class).set(null,length<0?null:Arrays.copyOf(originalNames,length));
            row("set-"+length+'-'+index+":"+invoke(item,"changed",index,0)+":"+invoke(select,0)+":"+invoke(key,0)+":"+invoke(code,index)+":"+invoke(name,index));
        }
        f(request,"b","codeIndexes",int[].class).set(null,originalCodes.clone());f(request,"a","interestNames",String[].class).set(null,originalNames.clone());
        for(int i=0;i<3;i++){row("duplicate-"+i+":"+invoke(item,i==2?null:"duplicate",i,137)+":"+invoke(select,137)+":"+invoke(key,137));}
        f(request,"b","codeIndexes",int[].class).set(null,null);row("partial-set:"+invoke(item,"partial",0,7)+":"+invoke(name,0));
        out.close();System.out.println("PASS categories: "+observations+" observations");
    }
    public static void main(String[] args){try{run(args);System.exit(0);}catch(Throwable x){x.printStackTrace();System.exit(1);}}
}
