import java.io.*;
import java.lang.reflect.*;
import java.security.*;
import java.util.*;
import javax.microedition.lcdui.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Untouched optimized XStatus/GUID methods, real native tables, language loader and icons. */
public class XStatusCatalogProbe {
    static boolean ref;
    static int observations, parses;
    static PrintWriter out;
    static Class<?> status, guid, icon, imageList;
    static Field index, bytes, guidTable, names, imageTable;
    static Method parse, set, getIndex, getGUID, getImage, getName, count, images, draw;
    static Object value, list, contactValue;
    static byte[][] fixtures;
    static String n(String a, String b) { return ref ? a : b; }
    static Field f(Class<?> c, String a, String b, Class<?> type) throws Exception {
        for (Field f : c.getDeclaredFields()) if (f.getName().equals(n(a,b)) && f.getType()==type) { f.setAccessible(true); return f; }
        throw new NoSuchFieldException(c + "." + n(a,b));
    }
    static Method m(Class<?> c, String a, String b, Class<?> result, Class<?>... args) throws Exception {
        for (Method m : c.getDeclaredMethods()) if (m.getName().equals(n(a,b)) && m.getReturnType()==result && Arrays.equals(m.getParameterTypes(),args)) { m.setAccessible(true); return m; }
        throw new NoSuchMethodException(c + "." + n(a,b));
    }
    static String units(String s) {
        if (s==null) return "null";
        StringBuilder b=new StringBuilder();
        for (int i=0;i<s.length();i++) { String x=Integer.toHexString(s.charAt(i)); for(int j=x.length();j<4;j++) b.append('0'); b.append(x); }
        return b.toString();
    }
    static String b64(byte[] b) { return b==null ? "null" : Base64.getEncoder().encodeToString(b); }
    static void row(String s) { out.println(s); observations++; }
    static String failure(Throwable t) {
        if (t instanceof Error) throw new AssertionError("Unexpected VM/fixture error",t);
        return "exception:" + t.getClass().getName();
    }
    static Object call(Method method, Object target, Object... args) throws Exception {
        try { return method.invoke(target,args); }
        catch (InvocationTargetException e) { return failure(e.getCause()); }
    }
    static String glyph(Object obj) throws Exception {
        if (obj==null) return "null";
        if (obj instanceof String) return (String)obj;
        Object[] nativeIcons=(Object[])imageTable.get(list);
        int position=-1;
        if (nativeIcons!=null) for(int i=0;i<nativeIcons.length;i++) if(nativeIcons[i]==obj) { position=i; break; }
        Image canvas=Image.createImage(20,20); Graphics g=canvas.getGraphics();g.setColor(0x345678);g.fillRect(0,0,20,20);g.setClip(2,3,15,14);
        draw.invoke(obj,g,1,2);int[] rgb=new int[400];canvas.getRGB(rgb,0,20,0,0,20,20);
        MessageDigest hash=MessageDigest.getInstance("SHA-256");for(int p:rgb)hash.update(new byte[]{(byte)(p>>24),(byte)(p>>16),(byte)(p>>8),(byte)p});
        return position+":"+b64(hash.digest())+":"+g.getClipX()+":"+g.getClipY()+":"+g.getClipWidth()+":"+g.getClipHeight();
    }
    static String returnedGUID(Object obj) throws Exception {
        if(obj==null)return "null";
        if(obj instanceof String)return (String)obj;
        Object[] table=(Object[])guidTable.get(null);int position=-1;
        for(int i=0;i<table.length;i++)if(table[i]==obj){position=i;break;}
        return position+":"+b64((byte[])bytes.get(obj));
    }
    static void getters(String label,int selected) throws Exception {
        row(label+":"+call(set,value,selected)+":"+index.getInt(value)+":"+call(getIndex,value)
            +":"+returnedGUID(call(getGUID,null,selected))+":"+glyph(call(getImage,null,selected))
            +":"+units((String)call(getName,null,selected))+":"+(call(images,null)==list));
    }
    static void parseCase(String label,byte[] payload) throws Exception {
        byte[] before=payload==null?null:payload.clone();Object result=call(parse,contactValue,(Object)payload);parses++;
        row(label+":"+result+":"+index.getInt(value)+":"+call(getIndex,value)+":"+Arrays.equals(before,payload));
    }
    static byte[] join(byte[]... parts) throws Exception {
        ByteArrayOutputStream b=new ByteArrayOutputStream();for(byte[] p:parts)b.write(p);return b.toByteArray();
    }
    static void nativeData() throws Exception {
        Object[] table=(Object[])guidTable.get(null);
        row("native-count:"+table.length+":"+call(count,null)+":"+((String[])names.get(null)).length);
        for(int i=0;i<table.length;i++)row("native-guid-"+i+":"+returnedGUID(call(getGUID,null,i)));
        for(int i=-64;i<=64;i++)getters("index-"+i,i);
        for(int i:new int[]{Integer.MIN_VALUE,-65536,-32769,-32768,127,255,256,32767,32768,65535,65536,Integer.MAX_VALUE})getters("extreme-"+i,i);
        row("constructor-index:"+index.getInt(status.getDeclaredConstructor().newInstance()));
    }
    static void parsing() throws Exception {
        for(int seed:new int[]{Integer.MIN_VALUE,-2,-1,0,31,36,37,Integer.MAX_VALUE}) {
            call(set,value,seed);parseCase("reset-null-"+seed,null);
            call(set,value,seed);parseCase("reset-empty-"+seed,new byte[0]);
            call(set,value,seed);parseCase("reset-short-"+seed,new byte[]{1});
        }
        for(int n=0;n<fixtures.length;n++) {
            for(int length=0;length<=35;length++)parseCase("prefix-"+n+'-'+length,Arrays.copyOf(fixtures[n],length));
            for(int offset=0;offset<=12;offset++) {
                byte[] unknown=new byte[offset*16];Arrays.fill(unknown,(byte)0x55);
                parseCase("offset-"+n+'-'+offset,join(unknown,fixtures[n],new byte[]{1}));
            }
            for(int byteIndex=0;byteIndex<16;byteIndex++)for(int replacement=0;replacement<256;replacement++) {
                byte[] payload=fixtures[n].clone();payload[byteIndex]=(byte)replacement;
                parseCase("mutate-"+n+'-'+byteIndex+'-'+replacement,payload);
            }
        }
        for(int a=0;a<fixtures.length;a++)for(int b=0;b<fixtures.length;b++)parseCase("first-"+a+'-'+b,join(fixtures[a],fixtures[b]));
        for(int length=0;length<257;length++)parseCase("unknown-"+length,new byte[length]);
        Random random=new Random(70037);
        for(int k=0;k<1000;k++) {
            byte[] payload=new byte[random.nextInt(257)];random.nextBytes(payload);
            if(k%3==0 && payload.length>=16) {int offset=random.nextInt(payload.length/16)*16;System.arraycopy(fixtures[random.nextInt(fixtures.length)],0,payload,offset,16);}
            parseCase("random-"+k,payload);
        }
    }
    static void mutations() throws Exception {
        Object[] table=(Object[])guidTable.get(null);
        for(int target:new int[]{0,31,36}) {
            Object old=table[target];table[target]=null;parseCase("null-guid-"+target,fixtures[target]);getters("null-guid-getter-"+target,target);table[target]=old;
            byte[] original=(byte[])bytes.get(old);
            for(int length:new int[]{-1,0,1,15,16,17,32}) {
                byte[] changed=length<0?null:Arrays.copyOf(fixtures[target],length);bytes.set(old,changed);
                parseCase("guid-length-"+target+'-'+length,fixtures[target]);getters("guid-length-getter-"+target+'-'+length,target);
            }
            bytes.set(old,original);
            byte[] aliased=original.clone();Constructor<?> ctor=guid.getDeclaredConstructor(byte[].class);ctor.setAccessible(true);Object made=ctor.newInstance((Object)aliased);
            row("constructor-alias-"+target+":"+(bytes.get(made)==aliased));table[target]=made;aliased[0]^=1;
            parseCase("alias-old-"+target,original);parseCase("alias-new-"+target,aliased);row("alias-return-"+target+":"+(call(getGUID,null,target)==made)+":"+returnedGUID(made));table[target]=old;
        }
        Object last=table[36];table[36]=table[0];parseCase("duplicate-first",fixtures[0]);table[36]=last;
        Object first=table[0];table[0]=table[36];parseCase("duplicate-alias",fixtures[36]);table[0]=first;
        String[] captions=(String[])names.get(null);String original= captions[0];captions[0]=null;getters("null-caption",0);captions[0]="missing-key";getters("missing-caption",0);captions[0]=original;
        Object[] originalIcons=(Object[])imageTable.get(list);
        for(int length:new int[]{-1,0,1,31,36,37}) {
            imageTable.set(list,length<0?null:Arrays.copyOf(originalIcons,length));
            for(int selected:new int[]{-1,0,30,31,35,36,37,Integer.MAX_VALUE})getters("image-length-"+length+'-'+selected,selected);
        }
        imageTable.set(list,originalIcons);
    }
    static void run(String[] args) throws Exception {
        ref=args[1].equals("reference");Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);
        ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);
        ClassLoader loader=MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();status=Class.forName(n("bj","jimm.comm.XStatus"),true,loader);guid=Class.forName(n("bh","jimm.comm.GUID"),true,loader);icon=Class.forName(n("e","DrawControls.Icon"),true,loader);imageList=Class.forName(n("f","DrawControls.ImageList"),true,loader);
        index=f(status,"a","index",int.class);bytes=f(guid,"a","guid",byte[].class);guidTable=f(status,"a","xguids",Array.newInstance(guid,0).getClass());names=f(status,"a","xstatus",String[].class);
        Class<?> contact=Class.forName(n("z","jimm.ContactItem"),true,loader);parse=m(contact,"a","setXStatus",void.class,byte[].class);set=m(status,"a","setStatusIndex",void.class,int.class);getIndex=m(status,"b","getStatusIndex",int.class);getGUID=m(status,"a","getStatusGUID",guid,int.class);getImage=m(status,"a","getStatusImage",icon,int.class);getName=m(status,"a","getStatusAsString",String.class,int.class);count=m(status,"a","getXStatusCount",int.class);images=m(status,"a","getXStatusImageList",imageList);draw=m(icon,"b","drawImage",void.class,Graphics.class,int.class,int.class);
        list=call(images,null);imageTable=f(imageList,"a","icons",Array.newInstance(icon,0).getClass());contactValue=contact.getDeclaredConstructor().newInstance();value=m(contact,"a","getXStatus",status).invoke(contactValue);
        if(ref) {Object[] table=(Object[])guidTable.get(null);try(DataOutputStream file=new DataOutputStream(new FileOutputStream(args[2]))) {file.writeInt(table.length);for(Object obj:table){byte[] b=(byte[])bytes.get(obj);file.writeInt(b.length);file.write(b);}}}
        try(DataInputStream file=new DataInputStream(new FileInputStream(args[2]))) { fixtures=new byte[file.readInt()][];for(int i=0;i<fixtures.length;i++) { fixtures[i]=new byte[file.readInt()];file.readFully(fixtures[i]); } }
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[3]),"UTF-8"));nativeData();parsing();mutations();row("parse-calls:"+parses);out.close();System.out.println("PASS XStatus catalog: "+observations+" observations, "+parses+" parser calls");
    }
    public static void main(String[] args) {try {run(args);System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
