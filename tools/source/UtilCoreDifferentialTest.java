import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/** Run real protocol/encoding helpers from May and authoring bytecode, including partial writes and MD5 state. */
public final class UtilCoreDifferentialTest {
    static Class<?> oldUtil, newUtil;
    static String nativeUtilName="co",nativeOptionsName="cj";
    static int checks;
    static final Map<String, Integer> covered = new TreeMap<String, Integer>();

    static ClassLoader loader(Path jar, Path libs)throws Exception {
        return new URLClassLoader(new URL[]{jar.toUri().toURL(), libs.resolve("microemu.jar").toUri().toURL(),
                libs.resolve("microemu-nokiaui.jar").toUri().toURL()}, ClassLoader.getPlatformClassLoader());
    }
    static Method method(Class<?> owner,String name,Class<?> result,Class<?>...params)throws Exception {
        for(Method m:owner.getDeclaredMethods())if(m.getName().equals(name)&&m.getReturnType()==result&&Arrays.equals(m.getParameterTypes(),params)){
            m.setAccessible(true);return m;
        }
        throw new NoSuchMethodException(owner.getName()+"."+name+Arrays.toString(params));
    }
    static Field field(Class<?> owner,String name,Class<?> type)throws Exception {
        for(Field f:owner.getDeclaredFields())if(f.getName().equals(name)&&f.getType()==type){f.setAccessible(true);return f;}
        throw new NoSuchFieldException(owner.getName()+"."+name);
    }
    static Object invoke(Method method,Object[] args)throws Exception {
        try{return method.invoke(null,args);}catch(InvocationTargetException e){return new Failure(e.getCause().getClass().getName());}
    }
    static final class Failure {
        final String type;
        Failure(String type){this.type=type;}
        public boolean equals(Object other){return other instanceof Failure&&type.equals(((Failure)other).type);}
        public String toString(){return "exception:"+type;}
    }
    static Object[] copy(Object[] args)throws IOException {
        Object[] result=args.clone();
        IdentityHashMap<Object,Object> copies=new IdentityHashMap<Object,Object>();
        for(int i=0;i<result.length;i++){
            Object input=result[i];if(input==null)continue;
            if(copies.containsKey(input)){result[i]=copies.get(input);continue;}
            if(input instanceof byte[])result[i]=((byte[])input).clone();
            else if(input instanceof long[])result[i]=((long[])input).clone();
            else if(input.getClass()==ByteArrayOutputStream.class){ByteArrayOutputStream stream=new ByteArrayOutputStream();stream.write(((ByteArrayOutputStream)input).toByteArray());result[i]=stream;}
            else if(input instanceof DataInputStream){DataInputStream stream=(DataInputStream)input;if(!stream.markSupported())throw new AssertionError("Unscripted input stream");stream.mark(Integer.MAX_VALUE);byte[] bytes=stream.readAllBytes();stream.reset();result[i]=new DataInputStream(new ByteArrayInputStream(bytes));}
            copies.put(input,result[i]);
        }
        return result;
    }
    static String describe(Object value){
        if(value instanceof byte[])return Arrays.toString((byte[])value);
        if(value instanceof long[])return Arrays.toString((long[])value);
        if(value instanceof Object[])return Arrays.deepToString((Object[])value);
        return String.valueOf(value);
    }
    static void equal(Object old,Object fresh,String label){
        if(!Objects.deepEquals(old,fresh))throw new AssertionError(label+": "+describe(old)+" != "+describe(fresh));
        checks++;
    }
    static final class Pair {
        final String name;
        final Method old, fresh;
        Object lastOld,lastNew;
        Pair(String oldName,String sourceName,Class<?> result,Class<?>...params)throws Exception {
            name=sourceName+Arrays.toString(params);old=method(oldUtil,oldName,result,params);fresh=method(newUtil,sourceName,result,params);
        }
        Object call(Object...args)throws Exception {
            Object[] left=copy(args),right=copy(args);
            Object a=invoke(old,left),b=invoke(fresh,right);lastOld=a;lastNew=b;
            equal(a,b,name+" args "+describe(args));
            for(int i=0;i<left.length;i++){
                if(left[i] instanceof byte[]||left[i] instanceof long[]){equal(left[i],right[i],name+" argument "+i);if(a instanceof byte[]||a instanceof long[])equal(a==left[i],b==right[i],name+" returned argument alias "+i);}
                else if(left[i] instanceof ByteArrayOutputStream)equal(((ByteArrayOutputStream)left[i]).toByteArray(),((ByteArrayOutputStream)right[i]).toByteArray(),name+" stream bytes");
                else if(left[i] instanceof DataInputStream)equal(((DataInputStream)left[i]).readAllBytes(),((DataInputStream)right[i]).readAllBytes(),name+" unread stream bytes");
            }
            covered.put(name,covered.containsKey(name)?covered.get(name)+1:1);
            return a;
        }
    }
    static void options(ClassLoader loader,String name,boolean cp)throws Exception {
        Class<?> cls=Class.forName(name,true,loader);Field table=null;
        for(Field f:cls.getDeclaredFields())if(f.getType()==Object[].class){if(table!=null)throw new AssertionError("Ambiguous options table");table=f;}
        if(table==null)throw new AssertionError("Missing options table");table.setAccessible(true);
        Object[] values=new Object[256];values[133]=Boolean.valueOf(cp);values[3]="RU";table.set(null,values);
    }
    static void binary()throws Exception {
        Pair getByte=new Pair("a","getByte",int.class,byte[].class,int.class);
        Pair getWord=new Pair("a","getWord",int.class,byte[].class,int.class,boolean.class);
        Pair getWordDefault=new Pair("b","getWord",int.class,byte[].class,int.class);
        Pair getDWord=new Pair("a","getDWord",long.class,byte[].class,int.class,boolean.class);
        Pair getDWordDefault=new Pair("a","getDWord",long.class,byte[].class,int.class);
        Pair putByte=new Pair("a","putByte",void.class,byte[].class,int.class,int.class);
        Pair putWord=new Pair("a","putWord",void.class,byte[].class,int.class,int.class,boolean.class);
        Pair putWordDefault=new Pair("b","putWord",void.class,byte[].class,int.class,int.class);
        Pair putDWord=new Pair("a","putDWord",void.class,byte[].class,int.class,long.class,boolean.class);
        Pair putDWordDefault=new Pair("a","putDWord",void.class,byte[].class,int.class,long.class);
        Pair tlv=new Pair("a","getTlv",byte[].class,byte[].class,int.class);
        Pair toLong=new Pair("a","byteArrayToLong",long.class,byte[].class);
        Pair hex=new Pair("b","byteArrayToHexString",String.class,byte[].class);
        Pair password=new Pair("a","decipherPassword",byte[].class,byte[].class);
        Pair comparison=new Pair("a","byteArrayEquals",boolean.class,byte[].class,int.class,byte[].class,int.class,int.class);
        Pair merge=new Pair("a","mergeCapabilities",byte[].class,byte[].class,byte[].class);
        Random random=new Random(20100512);
        for(int sample=0;sample<1800;sample++){
            byte[] data=sample==0?null:new byte[sample<70?sample-1:random.nextInt(65)];if(data!=null)random.nextBytes(data);
            int len=data==null?0:data.length;int off=sample<70?sample%7-2:random.nextInt(len+7)-3;
            int value=sample%7==0?Integer.MIN_VALUE:sample%7==1?Integer.MAX_VALUE:random.nextInt();
            long longValue=sample%7==0?Long.MIN_VALUE:sample%7==1?Long.MAX_VALUE:random.nextLong();
            getByte.call(data,off);getWordDefault.call(data,off);getDWordDefault.call(data,off);
            putByte.call(data,off,value);putWordDefault.call(data,off,value);putDWordDefault.call(data,off,longValue);
            for(boolean big:new boolean[]{false,true}){getWord.call(data,off,big);getDWord.call(data,off,big);putWord.call(data,off,value,big);putDWord.call(data,off,longValue,big);}
            tlv.call(data,off);toLong.call((Object)data);hex.call((Object)data);password.call((Object)data);
            byte[] second=data==null?new byte[0]:data.clone();if(second.length>0&&sample%2==0)second[second.length/2]^=1;
            // May's optimizer replaces the second offset by 0 while retaining its parameter slot.
            for(int size:new int[]{-1,0,1,2,16,len,len+1,Integer.MAX_VALUE})comparison.call(data,off,second,0,size);
            merge.call(data,second);
        }
        for(int size:new int[]{0,1,15,16,17,31,32})for(int off:new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE,-16,-1,0,1,size}){
            byte[] data=new byte[size];getByte.call(data,off);getWord.call(data,off,true);getDWord.call(data,off,false);tlv.call(data,off);comparison.call(data,off,data,0,16);
        }
        for(int size=0;size<100;size++){
            byte[] data=new byte[size];random.nextBytes(data);
            if(size>=4)for(int length:new int[]{0,1,size-4,size-3,65535}){data[2]=(byte)(length>>>8);data[3]=(byte)length;tlv.call(data,0);}
        }
    }
    static void strings(ClassLoader oldLoader,ClassLoader newLoader)throws Exception {
        Pair cpEncode=new Pair("d","stringToByteArray1251",byte[].class,String.class);
        Pair cpDecode=new Pair("d","byteArray1251ToString",String.class,byte[].class,int.class,int.class);
        Pair ucsEncode=new Pair("b","stringToUcs2beByteArray",byte[].class,String.class);
        Pair ucsDecode=new Pair("b","ucs2beByteArrayToString",String.class,byte[].class,int.class,int.class);
        Pair utf=new Pair("a","isDataUTF8",boolean.class,byte[].class,int.class,int.class);
        Pair decode=new Pair("a","byteArrayToString",String.class,byte[].class,int.class,int.class,boolean.class);
        Pair decodeSlice=new Pair("a","byteArrayToString",String.class,byte[].class,int.class,int.class);
        Pair decodeArray=new Pair("a","byteArrayToString",String.class,byte[].class,boolean.class);
        Pair decodeDefault=new Pair("a","byteArrayToString",String.class,byte[].class);
        Pair encode=new Pair("a","stringToByteArray",byte[].class,String.class,boolean.class);
        Pair encodeDefault=new Pair("a","stringToByteArray",byte[].class,String.class);
        for(int ch=0;ch<65536;ch++){
            String value=String.valueOf((char)ch);cpEncode.call(value);ucsEncode.call(value);
        }
        for(int value=0;value<256;value++)cpDecode.call(new byte[]{(byte)value},0,1);
        String[] values={null,"","ASCII","a\u0000b","\r\n\t","\u0410\u044f\u0401\u0451\u0490\u0456\u010d","\ud800","\udfff","\ud83d\ude00","a\ud800b",new String(new char[65535]).replace('\0','a'),new String(new char[65536]).replace('\0','a'),new String(new char[22000]).replace('\0','\u0800')};
        Random random=new Random(700);
        for(boolean cp:new boolean[]{false,true}){
            options(oldLoader,nativeOptionsName,cp);options(newLoader,"jimm.Options",cp);
            for(String value:values){encodeDefault.call(value);for(boolean utf8:new boolean[]{false,true})encode.call(value,utf8);}
            for(int sample=0;sample<1600;sample++){
                byte[] bytes=sample==0?null:new byte[sample<260?sample%18:random.nextInt(40)];if(bytes!=null)random.nextBytes(bytes);
                if(bytes!=null&&sample<256)Arrays.fill(bytes,(byte)sample);
                int length=bytes==null?0:bytes.length;decodeDefault.call((Object)bytes);
                for(boolean utf8:new boolean[]{false,true})decodeArray.call(bytes,utf8);
                for(int off:new int[]{-1,0,1,length-1,length+1})for(int size:new int[]{-1,0,1,2,length-off,length-off+1}){
                    cpDecode.call(bytes,off,size);ucsDecode.call(bytes,off,size);utf.call(bytes,off,size);decodeSlice.call(bytes,off,size);
                    for(boolean utf8:new boolean[]{false,true})decode.call(bytes,off,size,utf8);
                }
            }
        }
    }
    static void text()throws Exception {
        Pair cr=new Pair("a","removeCr",String.class,String.class);
        Pair lf=new Pair("b","restoreCrLf",String.class,String.class);
        Pair rtf=new Pair("e","DecodeRTF",String.class,String.class);
        Pair urls=new Pair("a","parseMessageForURL",Vector.class,String.class);
        Pair urlChar=new Pair("a","isURLChar",boolean.class,char.class,boolean.class);
        Pair explode=new Pair("a","explode",String[].class,String.class,char.class);
        Pair toBytes=new Pair("a","explodeToBytes",byte[].class,String.class,char.class,int.class);
        Pair intDefault=new Pair("a","strToIntDef",int.class,String.class,int.class);
        Pair two=new Pair("a","makeTwo",String.class,int.class);
        Pair intDecimal=new Pair("b","intToDecimal",String.class,int.class);
        Pair decimalInt=new Pair("a","decimalToInt",int.class,String.class);
        Pair ip=new Pair("c","ipToString",String.class,byte[].class);
        Pair parseIp=new Pair("c","ipToByteArray",byte[].class,String.class);
        Pair isIp=new Pair("a","isIP",boolean.class,String.class);
        for(int ch=0;ch<65536;ch++)for(boolean before:new boolean[]{false,true})urlChar.call((char)ch,before);
        String[] values={null,"","a","0","-1","1.2","000.001","-0.001","1.23456","..1",".","a..b","127.0.0.1","256.-1.9.0","one.example \"x.y\"","http://example.com/alpha?q=1","a\u0000b\r\nc\td","{\\rtf1 hello}","{\\rtf1 \\par \\tab \\'e0}","\\","{","}","\\par","\ud800.x\udfff","2147483647","-2147483648","2147483648"};
        Random random=new Random(77);
        for(String value:values){cr.call(value);lf.call(value);rtf.call(value);urls.call(value);parseIp.call(value);isIp.call(value);decimalInt.call(value);
            // The original optimized body substitutes 0 for the default parameter.
            intDefault.call(value,0);
            for(char separator:new char[]{0,'.',',','|',' '}){explode.call(value,separator);for(int radix:new int[]{-1,2,10,16,36,37})toBytes.call(value,separator,radix);}}
        for(int sample=0;sample<2000;sample++){
            // Larger negative remainders make the historical padding loop grow without terminating.
            int value=sample<100?sample-20:random.nextInt()&Integer.MAX_VALUE;two.call(value);Object decimal=intDecimal.call(value);if(decimal instanceof String)decimalInt.call(decimal);
            byte[] bytes=new byte[sample%8];random.nextBytes(bytes);ip.call((Object)bytes);
            String alphabet="aZ09.-:/ \"\r\n\t\u00e9\u0410";StringBuilder text=new StringBuilder();for(int i=0;i<sample%40;i++)text.append(alphabet.charAt(random.nextInt(alphabet.length())));String str=text.toString();
            cr.call(str);lf.call(str);urls.call(str);decimalInt.call(str);
        }
    }
    static void md5()throws Exception {
        Pair md5=new Pair("b","calculateMD5",byte[].class,byte[].class);
        Pair[] rounds={new Pair("a","FF",long.class,long.class,long.class,long.class,long.class,long.class,long.class,long.class),new Pair("b","GG",long.class,long.class,long.class,long.class,long.class,long.class,long.class,long.class),new Pair("c","HH",long.class,long.class,long.class,long.class,long.class,long.class,long.class,long.class),new Pair("d","II",long.class,long.class,long.class,long.class,long.class,long.class,long.class,long.class)};
        Pair update=new Pair("a","md5Update",void.class,byte[].class,int.class);
        Pair memcpy=new Pair("a","md5Memcpy",void.class,byte[].class,byte[].class,int.class,int.class,int.class);
        Pair encode=new Pair("a","Encode",void.class,byte[].class,long[].class,int.class);
        Pair unsigned=new Pair("a","b2iu",long.class,byte.class);
        Field[] old={field(oldUtil,"a",long[].class),field(oldUtil,"b",long[].class),field(oldUtil,"ad",byte[].class),field(oldUtil,"ae",byte[].class)};
        Field[] fresh={field(newUtil,"state",long[].class),field(newUtil,"count",long[].class),field(newUtil,"buffer",byte[].class),field(newUtil,"digest",byte[].class)};
        Random random=new Random(64);
        Object oldDigest=null,newDigest=null;
        for(int sample=0;sample<500;sample++){
            int length=sample<260?sample:sample==260?65536:random.nextInt(4096);byte[] input=sample==499?null:new byte[length];if(input!=null)random.nextBytes(input);
            md5.call((Object)input);
            for(int i=0;i<old.length;i++)equal(old[i].get(null),fresh[i].get(null),"MD5 state "+i+" sample "+sample);
            if(sample==0){oldDigest=md5.lastOld;newDigest=md5.lastNew;}
            equal(oldDigest==md5.lastOld,newDigest==md5.lastNew,"shared MD5 result sample "+sample);
            equal(md5.lastOld==old[3].get(null),md5.lastNew==fresh[3].get(null),"MD5 return alias sample "+sample);
            if(sample==100||sample==250){((byte[])md5.lastOld)[0]^=1;((byte[])md5.lastNew)[0]^=1;equal(old[3].get(null),fresh[3].get(null),"MD5 return mutation sample "+sample);}
            if(input!=null&&sample!=100&&sample!=250)equal(old[3].get(null),MessageDigest.getInstance("MD5").digest(input),"native MD5 standard vector "+sample);
        }
        for(int value=-128;value<128;value++)unsigned.call((byte)value);
        for(int sample=0;sample<1000;sample++){
            long[] values={random.nextLong(),random.nextLong(),random.nextLong(),random.nextLong(),random.nextLong(),sample<140?sample-70:random.nextLong(),random.nextLong()};
            for(Pair round:rounds)round.call(values[0],values[1],values[2],values[3],values[4],values[5],values[6]);
            byte[] a=new byte[sample%80],b=new byte[(sample*3)%83];random.nextBytes(a);random.nextBytes(b);
            int off=sample%9-3,len=sample%89-5;memcpy.call(a,b,off,off+1,len);encode.call(a,new long[]{random.nextLong(),random.nextLong()},len);
            if(sample<150){update.call(b,len);for(int i=0;i<old.length;i++)equal(old[i].get(null),fresh[i].get(null),"partial MD5 update "+sample+" field "+i);}
        }
    }
    static void streams()throws Exception {
        Pair read=new Pair("a","readAsciiz",String.class,DataInputStream.class);
        Pair word=new Pair("a","writeWord",void.class,ByteArrayOutputStream.class,int.class,boolean.class);
        Pair single=new Pair("a","writeByte",void.class,ByteArrayOutputStream.class,int.class);
        Pair string=new Pair("a","writeLenAndString",void.class,ByteArrayOutputStream.class,String.class,boolean.class);
        Pair tlv=new Pair("a","writeAsciizTLV",void.class,int.class,ByteArrayOutputStream.class,String.class,boolean.class);
        Pair defaultTlv=new Pair("a","writeAsciizTLV",void.class,int.class,ByteArrayOutputStream.class,String.class);
        Pair interest=new Pair("a","writeAsciizTLVInterest",void.class,int.class,ByteArrayOutputStream.class,int.class,String.class);
        String[] strings={null,"","plain","\u0410\u044f\u0401","a\u0000b","\ud800x\udfff",new String(new char[65536]).replace('\0','x')};
        for(String text:strings)for(int value:new int[]{Integer.MIN_VALUE,-1,0,1,255,256,65535,Integer.MAX_VALUE})for(boolean absent:new boolean[]{false,true}){
            ByteArrayOutputStream stream=absent?null:new ByteArrayOutputStream();if(stream!=null)stream.write(42);
            single.call(stream,value);defaultTlv.call(value,stream,text);interest.call(490,stream,value,text);
            for(boolean big:new boolean[]{false,true}){word.call(stream,value,big);string.call(stream,text,big);tlv.call(value,stream,text,big);}
        }
        Method oldWord=method(oldUtil,"a$175c50c1",int.class,DataInputStream.class),newWord=method(newUtil,"getWord",int.class,DataInputStream.class,boolean.class);
        Method oldFactory=method(oldUtil,"a$6f0c2d54",DataInputStream.class,byte[].class),newFactory=method(newUtil,"getDataInputStream",DataInputStream.class,byte[].class,int.class);
        Method oldDword=method(oldUtil,"b$559c4327",void.class,ByteArrayOutputStream.class,int.class),newDword=method(newUtil,"writeDWord",void.class,ByteArrayOutputStream.class,int.class,boolean.class);
        Random random=new Random(90);
        for(int sample=0;sample<1200;sample++){
            byte[] bytes=sample==0?null:new byte[sample%120];if(bytes!=null){random.nextBytes(bytes);if(bytes.length>1&&sample%4!=0){int length=sample%4==1?0:sample%4==2?bytes.length-2:bytes.length;bytes[0]=(byte)length;bytes[1]=(byte)(length>>>8);}}
            DataInputStream left=bytes==null?null:new DataInputStream(new ByteArrayInputStream(bytes)),right=bytes==null?null:new DataInputStream(new ByteArrayInputStream(bytes));
            equal(invoke(oldWord,new Object[]{left}),invoke(newWord,new Object[]{right,false}),"little-endian stream word "+sample);
            if(left!=null)equal(left.readAllBytes(),right.readAllBytes(),"word unread bytes "+sample);
            covered.put("getWord[DataInputStream, false]",sample+1);
            read.call((Object)(bytes==null?null:new DataInputStream(new ByteArrayInputStream(bytes))));
            byte[] input=bytes==null?null:bytes.clone();
            Object a=invoke(oldFactory,new Object[]{bytes}),b=invoke(newFactory,new Object[]{input,0});
            if(a instanceof Failure)equal(a,b,"stream factory error "+sample);
            else{if(bytes.length>0){bytes[0]^=1;input[0]^=1;}equal(((DataInputStream)a).readAllBytes(),((DataInputStream)b).readAllBytes(),"stream factory bytes/alias "+sample);}
            covered.put("getDataInputStream[byte[], 0]",sample+1);
            ByteArrayOutputStream out=sample%4==0?null:new ByteArrayOutputStream(),fresh=out==null?null:new ByteArrayOutputStream();int value=random.nextInt();
            equal(invoke(oldDword,new Object[]{out,value}),invoke(newDword,new Object[]{fresh,value,false}),"little-endian stream dword "+sample);
            if(out!=null)equal(out.toByteArray(),fresh.toByteArray(),"dword output bytes "+sample);
            covered.put("writeDWord[ByteArrayOutputStream, int, false]",sample+1);
        }
    }
    public static void main(String[] args)throws Exception {
        if(args.length>4){Properties configuration=new Properties();try(InputStream input=new FileInputStream(args[4])){configuration.load(input);}nativeUtilName=configuration.getProperty("util",nativeUtilName);nativeOptionsName=configuration.getProperty("options",nativeOptionsName);}
        ClassLoader oldLoader=loader(Paths.get(args[0]),Paths.get(args[2])),newLoader=loader(Paths.get(args[1]),Paths.get(args[2]));
        oldUtil=Class.forName(nativeUtilName,true,oldLoader);newUtil=Class.forName("jimm.comm.Util",true,newLoader);
        options(oldLoader,nativeOptionsName,true);options(newLoader,"jimm.Options",true);
        binary();strings(oldLoader,newLoader);text();md5();streams();
        List<String> inventory=new ArrayList<String>();for(Map.Entry<String,Integer> entry:covered.entrySet())inventory.add(entry.getValue()+" "+entry.getKey());Files.write(Paths.get(args[3]),inventory,java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("PASS utility core: "+checks+" observations, "+covered.size()+" real method pairs");System.exit(0);
    }
}
