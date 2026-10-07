import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

/** Real combined FLAP parser, including the modern offset-zero specialization. */
public class PacketRootProbe extends PacketProbe {
    static Method root;static Class<?> direct;static int cases;
    static String contents(Object p)throws Exception{
        return p.getClass()==direct?"DCPacket/data:"+hex((byte[])field(direct,ref?"a":"data",byte[].class).get(p)):fields(p);
    }
    static void parseRoot(String label,byte[] buf,int length,boolean mutate)throws Exception{
        Object p=root.getParameterTypes().length==2?invoke(root,null,buf,length):invoke(root,null,buf,0,length);
        if(p==null||p instanceof String){row(label+":"+p);cases++;return;}
        row(label+":"+contents(p)+"/wire:"+value(wire(p)));cases++;parsed++;
        if(mutate){String before=contents(p);Arrays.fill(buf,(byte)0xa5);row(label+"/after-input-mutation:"+before.equals(contents(p))+":"+contents(p)+"/wire:"+value(wire(p)));}
    }
    static void exercise()throws Exception{
        ArrayList<byte[]> seeds=seeds();int n=0;
        for(byte[] seed:seeds){String label="root-seed-"+(n++);parseRoot(label,seed.clone(),seed.length,true);
            for(int cut=0;cut<=seed.length;cut++)parseRoot(label+"-cut-"+cut,Arrays.copyOf(seed,cut),cut,false);
            for(int length:new int[]{-1,0,1,2,5,6,seed.length-1,seed.length+1,65535,Integer.MAX_VALUE})parseRoot(label+"-len-"+length,seed.clone(),length,false);
            for(int length:new int[]{0,1,3,4,9,10,11,12,23,24,25,35,36,37,255,65535}){byte[] buf=seed.clone();buf[4]=(byte)(length>>8);buf[5]=(byte)length;parseRoot(label+"-declared-"+length,buf,buf.length,false);}
        }
        for(int channel=0;channel<256;channel++)parseRoot("root-channel-"+channel,frame(channel,new byte[0]),6,false);
        for(int length:new int[]{-1,0,1,2,6,Integer.MAX_VALUE})parseRoot("root-null-"+length,null,length,false);
        for(int prefix=0;prefix<256;prefix++)for(int size:new int[]{0,1,2,3,6,7,255,256}){
            byte[] buf=new byte[size];Arrays.fill(buf,(byte)prefix);parseRoot("root-prefix-"+prefix+"-"+size,buf,size,true);
        }
        for(int command:new int[]{1,2,3,4})for(int flags:new int[]{0,1,32768,32769,65535})for(int declared:new int[]{0,1,2,3,10,24,255,65535}){
            byte[] buf=snac(21,command,flags,cat(bytes(declared>>8,declared,1,2),new byte[40]));parseRoot("root-ext-"+command+"-"+flags+"-"+declared,buf,buf.length,false);
        }
        Random random=new Random(20100512);for(int round=0;round<8000;round++){
            byte[] buf=seeds.get(random.nextInt(seeds.size())).clone();for(int i=0,count=1+random.nextInt(3);i<count;i++)buf[random.nextInt(buf.length)]=(byte)random.nextInt(256);
            parseRoot("root-mutation-"+round,buf,buf.length,false);
        }
    }
    public static void main(String[] args)throws Exception{
        ref=args[2].equals("reference");try(InputStream in=Files.newInputStream(Paths.get(args[5]))){configuration.load(in);}dcName=configuration.getProperty("DCPacket");errorName=configuration.getProperty("error");
        ArrayList<URL> urls=new ArrayList<>();urls.add(Paths.get(args[0]).toUri().toURL());urls.add(Paths.get(args[1]).toUri().toURL());for(String lib:new String[]{"microemu.jar","microemu-jsr-75.jar","microemu-jsr-120.jar","microemu-nokiaui.jar"})urls.add(Paths.get(args[3],lib).toUri().toURL());
        loader=new URLClassLoader(urls.toArray(new URL[0]),ClassLoader.getPlatformClassLoader());Class<?> io=Class.forName("ResourceIO",true,loader);Hashtable files=(Hashtable)io.getField("files").get(null);files.put("/langlist.lng",language(true));files.put("/RU.lng",language(false));
        Class<?> options=Class.forName(configuration.getProperty("options"),true,loader);Object[] values=new Object[256];for(int i=0;i<256;i++)values[i]=i<64||i>=224?"":i<128?Integer.valueOf(0):i<192?Boolean.FALSE:Long.valueOf(0);for(Field f:options.getDeclaredFields())if(f.getType()==Object[].class){f.setAccessible(true);f.set(null,values);}
        for(int i=0;i<6;i++)types[i]=Class.forName(configuration.getProperty(names[i]),true,loader);direct=Class.forName(dcName,true,loader);
        root=configuration.getProperty("rootDesc").equals("([BI)L"+configuration.getProperty("Packet").replace('.','/')+";")
            ?method(types[0],configuration.getProperty("rootName"),types[0],byte[].class,int.class)
            :method(types[0],configuration.getProperty("rootName"),types[0],byte[].class,int.class,int.class);
        out=new PrintWriter(Files.newBufferedWriter(Paths.get(args[4]),java.nio.charset.StandardCharsets.UTF_8));for(boolean cp:new boolean[]{true,false}){values[133]=cp;row("cp1251:"+cp);exercise();}
        if(parsed<1000)throw new AssertionError("Too few successful root dispatches");row("coverage:"+cases+":"+parsed);out.close();loader.close();System.out.println("PASS packet root: "+rows+" observations, "+parsed+" successful parses");System.exit(0);
    }
}
