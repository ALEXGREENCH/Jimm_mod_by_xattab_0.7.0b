import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

/** Actual FLAP/SNAC/TLV parsers and serializers; only language resource I/O is captured. */
public class PacketProbe {
    static boolean ref;static URLClassLoader loader;static PrintWriter out;static int rows,parsed;
    static final String[] old={"an","ak","h","bn","bu","ck"};
    static final String[] names={"Packet","SnacPacket","ConnectPacket","DisconnectPacket","ToIcqSrvPacket","FromIcqSrvPacket"};
    static Class<?>[] types=new Class<?>[6];
    static Properties configuration=new Properties();static String dcName="au",errorName="bv";static int dcCases;
    static String hex(byte[] b){return b==null?"null":Base64.getEncoder().encodeToString(b);}
    static String value(Object o){return o instanceof byte[]?hex((byte[])o):o instanceof String?"str:"+hex(((String)o).getBytes(java.nio.charset.StandardCharsets.UTF_8)):String.valueOf(o);}
    static void row(String s){out.println(s);rows++;}
    static Method method(Class<?> c,String name,Class<?> result,Class<?>...args)throws Exception{for(Method m:c.getDeclaredMethods())if(m.getName().equals(name)&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),args)){m.setAccessible(true);return m;}throw new NoSuchMethodException(c+"."+name);}
    static Field field(Class<?> c,String name,Class<?> type)throws Exception{for(Field f:c.getDeclaredFields())if(f.getName().equals(name)&&f.getType()==type){f.setAccessible(true);return f;}throw new NoSuchFieldException(c+"."+name);}
    static Object failure(Throwable t){if(t instanceof Error)throw new AssertionError("Broken packet fixture",t);String kind=t.getClass().getName();if(kind.equals(ref?errorName:"jimm.JimmException"))return "JimmException:"+t.getMessage();return "exception:"+kind;}
    static Object invoke(Method m,Object receiver,Object...args)throws Exception{try{return m.invoke(receiver,args);}catch(InvocationTargetException e){return failure(e.getCause());}}
    static Object create(int kind,Class<?>[] params,Object...args)throws Exception{Constructor<?> c=types[kind].getDeclaredConstructor(params);c.setAccessible(true);try{return c.newInstance(args);}catch(InvocationTargetException e){return failure(e.getCause());}}
    static int kind(Object packet){for(int i=5;i>=0;i--)if(packet.getClass()==types[i])return i;if(packet.getClass().getName().equals(ref?dcName:"jimm.comm.DCPacket"))return 6;throw new AssertionError("Unexpected packet "+packet.getClass());}
    static String fields(Object packet)throws Exception{
        int k=kind(packet);if(k==6)return "DCPacket";StringBuilder s=new StringBuilder(names[k]);
        s.append("/seq:").append(field(types[0],ref?"c":"sequence",int.class).get(packet));
        if(k==0){s.append("/channel:").append(field(types[0],ref?"a":"flapChannel",int.class).get(packet));s.append("/data:").append(value(field(types[0],ref?"a":"flapData",byte[].class).get(packet)));}
        if(k==1||k>=4){String[] n={"family","command","snacFlags","reference","extData","data"},o={"a","b","d","a","a","b"};Class<?>[] t={int.class,int.class,int.class,long.class,byte[].class,byte[].class};for(int i=0;i<n.length;i++)s.append('/').append(n[i]).append(':').append(value(field(types[1],ref?o[i]:n[i],t[i]).get(packet)));}
        if(k==2){s.append("/cookie:").append(value(field(types[k],ref?"a":"cookie",byte[].class).get(packet)));s.append("/uin:").append(value(field(types[k],ref?"a":"uin",String.class).get(packet)));s.append("/password:").append(value(field(types[k],ref?"b":"password",String.class).get(packet)));}
        if(k==3){String[] n={"uin","server","cookie","error","description"},o={"a","b","a","a","c"};Class<?>[] t={String.class,String.class,byte[].class,int.class,String.class};for(int i=0;i<n.length;i++)s.append('/').append(n[i]).append(':').append(value(field(types[k],ref?o[i]:n[i],t[i]).get(packet)));}
        if(k>=4){s.append("/icqSequence:").append(field(types[k],ref?"d":"icqSequence",int.class).get(packet));s.append("/uin:").append(value(field(types[k],ref?"a":"uin",String.class).get(packet)));s.append("/subcommand:").append(field(types[k],ref?"e":"subcommand",int.class).get(packet));}
        return s.toString();
    }
    static Object wire(Object packet)throws Exception{return invoke(method(packet.getClass(),ref?"a":"toByteArray",byte[].class),packet);}
    static void snapshot(String label,Object packet)throws Exception{if(packet==null||packet instanceof String){row(label+":"+packet);return;}row(label+":"+fields(packet)+"/wire:"+value(wire(packet)));}
    static Object parse(int parser,byte[] buf,int off,int len)throws Exception{return invoke(method(types[parser],ref?"a":"parse",types[0],byte[].class,int.class,int.class),null,buf,off,len);}
    static void parseCase(String label,int parser,byte[] buf,int off,int len,boolean mutate)throws Exception{
        Object p=parse(parser,buf,off,len);snapshot(label,p);if(p!=null&&!(p instanceof String)){parsed++;if(mutate){String before=fields(p);Arrays.fill(buf,(byte)0xa5);row(label+"/input-alias:"+before.equals(fields(p)));}}
    }
    static byte[] cat(byte[]...arrays)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();for(byte[] a:arrays)b.write(a);return b.toByteArray();}
    static byte[] bytes(int...v){byte[] b=new byte[v.length];for(int i=0;i<v.length;i++)b[i]=(byte)v[i];return b;}
    static byte[] tlv(int type,byte[] data)throws Exception{return cat(bytes(type>>8,type,data.length>>8,data.length),data);}
    static byte[] frame(int channel,byte[] data)throws Exception{return cat(bytes(42,channel,0xfe,0xdc,data.length>>8,data.length),data);}
    static byte[] snac(int family,int command,int flags,byte[] payload)throws Exception{return frame(2,cat(bytes(family>>8,family,command>>8,command,flags>>8,flags,0x89,0xab,0xcd,0xef),payload));}
    static void constructors()throws Exception{
        Class<?>[] none={},i={int.class},b={byte[].class},ib={int.class,byte[].class},iss={int.class,String.class,String.class};
        byte[] cookie={0,1,(byte)255,4};
        Object p=ref?create(0,b,cookie):create(0,ib,5,cookie);snapshot("ping-before",p);cookie[0]=7;Object changed=invoke(method(types[0],ref?"a":"setSequence",void.class,int.class),p,65537);if(changed!=null)throw new AssertionError("setSequence failed");snapshot("ping-alias",p);
        for(int seq:new int[]{-1,0,1,65535,65536,Integer.MIN_VALUE,Integer.MAX_VALUE}){
            snapshot("hello-"+seq,create(2,i,seq));snapshot("cookie-"+seq,create(2,ib,seq,bytes(0,255,42)));
            snapshot("ident-"+seq,create(2,iss,seq,"1234567","secret"));snapshot("goodbye-"+seq,create(3,i,seq));
            snapshot("disconnect-cookie-"+seq,create(3,new Class<?>[]{int.class,String.class,String.class,byte[].class},seq,"123","server:5190",bytes(1,2,3)));
            snapshot("disconnect-error-"+seq,create(3,new Class<?>[]{int.class,int.class,String.class},seq,seq,"description"));
        }
        snapshot("hello-default",create(2,none));snapshot("disconnect-default",create(3,none));
        for(String uin:new String[]{null,"","0","-1","4294967295","4294967296","9223372036854775807","9223372036854775808","bad","\u0416"})for(String password:new String[]{null,"","p\u0000x","\u041f\u0430\u0440\u043e\u043b\u044c","\u010d\ud83d\ude00"})snapshot("ident-strings-"+value(uin)+"-"+value(password),create(2,iss,3,uin,password));
        for(int kind:new int[]{2,3})for(byte[] data:new byte[][]{null,new byte[0],bytes(1,2,3),new byte[65536]}){
            Object c=kind==2?create(2,ib,17,data):create(3,new Class<?>[]{int.class,String.class,String.class,byte[].class},17,"123","server",data);snapshot("cookie-copy-"+kind+"-"+(data==null?-1:data.length),c);if(c instanceof String)continue;if(data!=null)Arrays.fill(data,(byte)99);snapshot("cookie-mutated-"+kind,c);
        }
        Class<?>[] snac={int.class,int.class,int.class,int.class,long.class,byte[].class,byte[].class};
        Class<?>[] icq={int.class,long.class,int.class,int.class,String.class,int.class,byte[].class,byte[].class};
        for(int kind:new int[]{1,4,5})for(int flags:new int[]{0,1,32768,32769,65535})for(int size:new int[]{-1,0,1,3,24,65536}){
            byte[] ext=size<0?null:new byte[size],data=bytes(0,255,42,5);if(ext!=null&&ext.length>0)ext[0]=8;
            Object c=kind==1?create(kind,snac,-3,0x10015,0x10003,flags,0x123456789L,ext,data):create(kind,icq,-3,0x123456789L,flags,65536,"4294967296",2000,ext,data);
            snapshot("snac-ctor-"+kind+"-"+flags+"-"+size,c);data[0]=77;if(ext!=null&&ext.length>0)ext[0]=66;snapshot("snac-alias-"+kind+"-"+flags+"-"+size,c);
            Object copy=invoke(method(types[1],ref?"b":"getData",byte[].class),c);if(copy instanceof byte[]){((byte[])copy)[0]=88;row("getData-copy-"+kind+":"+(data[0]==77));}else throw new AssertionError("Data getter failed");
        }
        for(int kind:new int[]{4,5})for(String uin:new String[]{null,"","0","-1","4294967295","9223372036854775807","9223372036854775808","bad"})snapshot("icq-uin-"+kind+"-"+value(uin),create(kind,icq,3,-1L,0,17,uin,65537,new byte[0],bytes(1,2)));
        // Original ProGuard constructors have constant arguments removed. Restore those at the call boundary.
        snapshot("to-specialized-meta",ref?create(4,new Class<?>[]{long.class,int.class,String.class,byte[].class,byte[].class},19L,7,"123",bytes(1),bytes(2)):create(4,new Class<?>[]{long.class,int.class,String.class,int.class,byte[].class,byte[].class},19L,7,"123",2000,bytes(1),bytes(2)));
        snapshot("to-specialized-default",ref?create(4,new Class<?>[]{String.class,int.class,byte[].class,byte[].class},"123",60,new byte[0],bytes(2)):create(4,new Class<?>[]{long.class,String.class,int.class,byte[].class,byte[].class},0L,"123",60,new byte[0],bytes(2)));
        for(int kind:new int[]{1,4,5})for(byte[] ext:new byte[][]{null,new byte[0],bytes(1)})snapshot("null-payload-"+kind+"-"+hex(ext),kind==1?create(kind,snac,1,2,3,0,1L,ext,null):create(kind,icq,1,1L,0,1,"123",60,ext,null));
        p=ref?create(0,b,new Object[]{null}):create(0,ib,5,null);snapshot("null-ping-data",p);
    }
    static ArrayList<byte[]> seeds()throws Exception{
        ArrayList<byte[]> seeds=new ArrayList<>();
        seeds.add(frame(1,bytes(0,0,0,1)));seeds.add(frame(1,cat(bytes(0,0,0,1),tlv(6,bytes(0,255,1)))));
        seeds.add(frame(1,cat(bytes(0,0,0,1),tlv(1,bytes(49,50,51)),tlv(2,bytes(0xfa,0xad)),tlv(3,bytes(118)),tlv(22,new byte[0]))));
        seeds.add(frame(4,new byte[0]));seeds.add(frame(4,cat(tlv(1,bytes(49)),tlv(5,bytes(115)),tlv(6,bytes(1,2,3)))));
        seeds.add(frame(4,cat(tlv(8,bytes(0,17)),tlv(4,bytes(98,121,101)))));
        seeds.add(frame(4,cat(tlv(9,bytes(0,18)),tlv(11,bytes(98,121,101)))));
        for(int flags:new int[]{0,1,32768,32769,65535}){seeds.add(snac(1,3,flags,flags==32768?bytes(0,2,91,92,93):bytes(91,92,93)));for(int cmd:new int[]{2,3})for(int size:new int[]{0,1,10,11,12,13,24}){
            byte[] data=new byte[size];Arrays.fill(data,(byte)42);byte[] body=cat(tlv(1,cat(bytes(size+8,0,0xef,0xcd,0xab,0x89,0xd0,7,0xff,0x7f),data)));
            seeds.add(snac(21,cmd,flags,flags==32768?cat(bytes(0,2,91,92),body):body));
        }}
        return seeds;
    }
    static void parsers()throws Exception{
        ArrayList<byte[]> seeds=seeds();int n=0;
        for(byte[] seed:seeds){int parser=(seed[1]&255)==1?2:(seed[1]&255)==4?3:1;String label="seed-"+(n++);
            for(int target:new int[]{0,parser})for(int off:new int[]{0,1,7}){byte[] buf=cat(new byte[off],seed,bytes(99,88));parseCase(label+"-"+target+"-"+off,target,buf,off,seed.length,true);}
            for(int cut=0;cut<=seed.length;cut++)for(int target:new int[]{0,parser})parseCase(label+"-cut-"+cut+"-"+target,target,Arrays.copyOf(seed,cut),0,cut,false);
            for(int len:new int[]{-1,0,1,2,5,6,seed.length-1,seed.length+1,65535,Integer.MAX_VALUE})for(int target:new int[]{0,parser})parseCase(label+"-len-"+len+"-"+target,target,seed.clone(),0,len,false);
            for(int declared:new int[]{0,1,3,4,9,10,11,12,23,24,25,35,36,37,255,65535}){byte[] buf=seed.clone();buf[4]=(byte)(declared>>8);buf[5]=(byte)declared;parseCase(label+"-flap-"+declared,parser,buf,0,seed.length,false);}
        }
        for(int channel=0;channel<256;channel++)parseCase("channel-"+channel,0,frame(channel,new byte[0]),0,6,false);
        for(int parser=0;parser<6;parser++)parseCase("null-buffer-"+parser,parser,null,0,6,false);
        for(int type=0;type<256;type++)for(int channel:new int[]{1,4})for(byte[] data:new byte[][]{new byte[0],bytes(0),bytes(0,1),bytes(65,0,66,0),bytes(0xff,0xfe,0,65)}){byte[] buf=frame(channel,cat(channel==1?bytes(0,0,0,1):new byte[0],tlv(type,data)));parseCase("tlv-"+channel+"-"+type+"-"+hex(data),channel==1?2:3,buf,0,buf.length,false);}
        for(int parser:new int[]{0,1,2,3,4,5})for(int off:new int[]{-1,0,1,6,8,Integer.MAX_VALUE})for(int len:new int[]{-1,0,1,2,6,8,255})parseCase("bounds-"+parser+"-"+off+"-"+len,parser,new byte[8],off,len,false);
        for(int channel:new int[]{1,4})for(int type:new int[]{1,2,3,4,5,6,8,9,11,14,15,20,22,23,24,25,26,255}){
            byte[] prefix=channel==1?bytes(0,0,0,1):new byte[0];
            byte[] buf=frame(channel,cat(prefix,tlv(type,bytes(49,50)),tlv(type,bytes(51,52))));parseCase("duplicate-"+channel+"-"+type,channel==1?2:3,buf,0,buf.length,false);
            for(int declared:new int[]{0,1,2,3,4,255,65535}){buf=frame(channel,cat(prefix,bytes(type>>8,type,declared>>8,declared,1,2)));parseCase("tlv-length-"+channel+"-"+type+"-"+declared,channel==1?2:3,buf,0,buf.length,false);}
        }
        for(int command:new int[]{1,2,3,4})for(int flags:new int[]{0,1,32768,32769,65535})for(int declared:new int[]{0,1,2,3,10,24,255,65535}){
            byte[] buf=snac(21,command,flags,cat(bytes(declared>>8,declared,1,2),new byte[40]));for(int parser:new int[]{0,1,4,5})parseCase("ext-length-"+command+"-"+flags+"-"+declared+"-"+parser,parser,buf,0,buf.length,false);
        }
        Random random=new Random(20100512);for(int round=0;round<8000;round++){
            byte[] seed=seeds.get(random.nextInt(seeds.size())),buf=seed.clone();for(int i=0,count=1+random.nextInt(3);i<count;i++)buf[random.nextInt(buf.length)]=(byte)random.nextInt(256);
            int parser=round%6;parseCase("mutation-"+round,parser,buf,0,buf.length,false);
        }
        if(parsed<300)throw new AssertionError("Too few successful parser observations: "+parsed);
    }
    static byte[] language(boolean list)throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);if(list){d.writeShort(1);d.writeUTF("RU");}else{d.writeShort(256);for(int i=0;i<256;i++){d.writeUTF("error_"+i);d.writeUTF("code:"+i+":EXT");}}return b.toByteArray();}
    static void directParser()throws Exception{
        Class<?> dc=Class.forName(ref?dcName:"jimm.comm.DCPacket",true,loader);
        Method parse=method(dc,ref?"a":"parse",types[0],byte[].class,int.class,int.class);
        Field data=field(dc,ref?"a":"data",byte[].class);
        for(int size:new int[]{0,1,2,3,255,256})for(int off:new int[]{-1,0,1,6,256,Integer.MAX_VALUE})for(int length:new int[]{-1,0,1,2,6,256,Integer.MAX_VALUE}){
            byte[] buf=new byte[size];for(int i=0;i<size;i++)buf[i]=(byte)(i*37+11);
            Object p=invoke(parse,null,buf,off,length);if(p==null||p instanceof String)throw new AssertionError("Direct parser failed");
            row("direct:"+size+":"+off+":"+length+":"+(data.get(p)==buf)+":"+value(wire(p)));
            if(size>0)buf[0]=77;row("direct-input-alias:"+(data.get(p)==buf)+":"+value(wire(p)));
            Object copy=wire(p);if(!(copy instanceof byte[]))throw new AssertionError("Direct serializer failed");((byte[])copy)[size>0?2:0]=88;
            row("direct-wire-copy:"+hex((byte[])data.get(p)));dcCases++;
        }
        for(int off:new int[]{-1,0,Integer.MAX_VALUE})for(int length:new int[]{-1,0,2,Integer.MAX_VALUE}){
            Object p=invoke(parse,null,null,off,length);if(p==null||p instanceof String)throw new AssertionError("Null direct parser failed");
            row("direct-null:"+off+":"+length+":"+(data.get(p)==null)+":"+value(wire(p)));dcCases++;
        }
        for(int size:new int[]{65535,65536,65537}){
            byte[] buf=new byte[size];Arrays.fill(buf,(byte)0x5a);Object p=invoke(parse,null,buf,0,size);
            row("direct-word-boundary:"+size+":"+(data.get(p)==buf)+":"+value(wire(p)));dcCases++;
        }
        row("direct-cases:"+dcCases);
    }
    public static void main(String[] args)throws Exception{
        ref=args[2].equals("reference");if(args.length>5){try(InputStream in=Files.newInputStream(Paths.get(args[5]))){configuration.load(in);}dcName=configuration.getProperty("DCPacket");errorName=configuration.getProperty("error");}
        ArrayList<URL> urls=new ArrayList<>();urls.add(Paths.get(args[0]).toUri().toURL());urls.add(Paths.get(args[1]).toUri().toURL());for(String lib:new String[]{"microemu.jar","microemu-jsr-75.jar","microemu-jsr-120.jar","microemu-nokiaui.jar"})urls.add(Paths.get(args[3],lib).toUri().toURL());
        loader=new URLClassLoader(urls.toArray(new URL[0]),ClassLoader.getPlatformClassLoader());Class<?> io=Class.forName("ResourceIO",true,loader);Hashtable files=(Hashtable)io.getField("files").get(null);files.put("/langlist.lng",language(true));files.put("/RU.lng",language(false));
        Class<?> options=Class.forName(ref?configuration.getProperty("options","cj"):"jimm.Options",true,loader);Object[] values=new Object[256];for(int i=0;i<256;i++)values[i]=i<64||i>=224?"":i<128?Integer.valueOf(0):i<192?Boolean.FALSE:Long.valueOf(0);values[133]=Boolean.TRUE;for(Field f:options.getDeclaredFields())if(f.getType()==Object[].class){f.setAccessible(true);f.set(null,values);}
        for(int i=0;i<6;i++)types[i]=Class.forName(ref?configuration.getProperty(names[i],old[i]):"jimm.comm."+names[i],true,loader);
        out=new PrintWriter(Files.newBufferedWriter(Paths.get(args[4]),java.nio.charset.StandardCharsets.UTF_8));for(boolean cp:new boolean[]{true,false}){values[133]=Boolean.valueOf(cp);row("cp1251:"+cp);constructors();parsers();if(args.length>5)directParser();}row("successful-parses:"+parsed);out.close();loader.close();System.out.println("PASS packets: "+rows+" observations, "+parsed+" successful parses");System.exit(0);
    }
}
