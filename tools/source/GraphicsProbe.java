import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.security.MessageDigest;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Real sprite slicing, anchors, animation loop and Motorola bitmap font, on each May platform. */
public class GraphicsProbe {
    static boolean ref;
    static String target;
    static ClassLoader loader;
    static Class<?> icon,list,animated,animations,options,text,io,font;
    static PrintWriter out;
    static int observations,rasters;
    static String n(String old,String name){return ref?old:name;}
    static Class<?> load(String old,String name)throws Exception{return Class.forName(n(old,name),true,loader);}
    static Field f(Class<?> c,String old,String name,Class<?> type)throws Exception {
        for(Class<?> k=c;k!=null;k=k.getSuperclass())for(Field v:k.getDeclaredFields())if(v.getName().equals(n(old,name))&&v.getType()==type){v.setAccessible(true);return v;}throw new NoSuchFieldException(c+"."+n(old,name));
    }
    static Method m(Class<?> c,String old,String name,Class<?> result,Class<?>...args)throws Exception {
        for(Class<?> k=c;k!=null;k=k.getSuperclass())for(Method v:k.getDeclaredMethods())if(v.getName().split("\\$")[0].equals(n(old,name).split("\\$")[0])&&v.getReturnType()==result&&Arrays.equals(v.getParameterTypes(),args)){v.setAccessible(true);return v;}throw new NoSuchMethodException(c+"."+n(old,name));
    }
    static Object instance(Class<?> c,Class<?>[] types,Object...args)throws Exception{Constructor<?> k=c.getDeclaredConstructor(types);k.setAccessible(true);return k.newInstance(args);}
    static Object invoke(Method m,Object owner,Object...args)throws Exception{try{return m.invoke(owner,args);}catch(InvocationTargetException e){return "exception:"+e.getCause().getClass().getName();}}
    static String enc(Object v)throws Exception{return v==null?"null":Base64.getEncoder().encodeToString(v.toString().getBytes("UTF-8"));}
    static void row(String v){out.println(v);observations++;}
    static String hash(Image image)throws Exception{int w=image.getWidth(),h=image.getHeight();int[] rgb=new int[w*h];image.getRGB(rgb,0,w,0,0,w,h);MessageDigest d=MessageDigest.getInstance("SHA-256");for(int p:rgb){d.update((byte)(p>>>24));d.update((byte)(p>>>16));d.update((byte)(p>>>8));d.update((byte)p);}return w+"x"+h+":"+Base64.getEncoder().encodeToString(d.digest());}
    static void frame(String label,Image image,Graphics g)throws Exception{row(label+":raster="+hash(image)+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight()+":color="+g.getColor());rasters++;}
    static Image canvas(){Image image=Image.createImage(80,60);Graphics g=image.getGraphics();g.setColor(0xaabbcc);g.fillRect(0,0,80,60);return image;}
    static Image atlas()throws Exception {InputStream input=loader.getResourceAsStream("graphics/atlas.png");return Image.createImage(input);}
    static Object symbol(int x,int y,int w,int h,boolean empty)throws Exception{return instance(icon,new Class<?>[]{Image.class,int.class,int.class,int.class,int.class},empty?null:atlas(),x,y,w,h);}
    static void iconFrames()throws Exception {
        for(int[] region:new int[][]{{0,0,16,16},{3,5,12,9},{-4,-3,10,10},{40,20,30,20},{0,0,0,0},{0,0,-1,-3}})for(boolean empty:new boolean[]{false,true})for(int position:new int[]{-7,0,7,25,50})for(int[] clip:new int[][]{{0,0,80,60},{4,5,20,20},{0,0,0,0}})for(int mode=0;mode<4;mode++) {
            Object v=symbol(region[0],region[1],region[2],region[3],empty);Image image=canvas();Graphics g=image.getGraphics();g.setClip(clip[0],clip[1],clip[2],clip[3]);
            String label="icon-"+Arrays.toString(region)+"-"+empty+"-"+position+"-"+Arrays.toString(clip)+"-"+mode;
            String[] old={"b","a","c","d"},name={"drawImage","drawByLeft","drawByRight","drawInCenter"};Object result=invoke(m(icon,old[mode],name[mode],void.class,Graphics.class,int.class,int.class),v,g,position,position);if(result!=null)throw new AssertionError(label+":"+result);row(label+":"+result);frame(label,image,g);
        }
    }
    static Object picture(Object sprites,int index)throws Exception{return m(list,"a","elementAt",icon,int.class).invoke(sprites,index);}
    static void sprites(String label,Object sprites)throws Exception {
        int count=(Integer)m(list,"a","size",int.class).invoke(sprites);StringBuilder b=new StringBuilder(label+":size="+count+":height="+m(list,"b","getHeight",int.class).invoke(sprites)+":width="+f(list,"a","width",int.class).getInt(sprites)+":parts=");
        for(int i=0;i<count;i++){Object v=picture(sprites,i);b.append('[').append(f(icon,"a","x",int.class).getInt(v)).append(',').append(f(icon,"b","y",int.class).getInt(v)).append(',').append(m(icon,"a","getWidth",int.class).invoke(v)).append(',').append(m(icon,"b","getHeight",int.class).invoke(v)).append(']');}row(b.toString());
        for(int index:new int[]{-1,0,1,count-1,count,count+1}) {
            Object v=picture(sprites,index);row(label+"-index-"+index+":"+(v!=null));
            if(v!=null){Image image=canvas();Graphics g=image.getGraphics();m(icon,"b","drawImage",void.class,Graphics.class,int.class,int.class).invoke(v,g,9,11);frame(label+"-index-"+index,image,g);}
        }
    }
    static void slicing()throws Exception {
        for(int w:new int[]{-1,1,7,16,100})for(int h:new int[]{-1,1,11,100}){Object v=instance(list,new Class<?>[0]);String label="slice-"+w+"-"+h;row(label+":"+invoke(m(list,"a","load",void.class,String.class,int.class,int.class),v,"/graphics/atlas.png",w,h));sprites(label,v);}
        for(int count:new int[]{0,1,2,3,4,10,53}){Object v=instance(list,new Class<?>[0]);String label="slice-count-"+count;row(label+":"+invoke(m(list,"a","load",void.class,String.class,int.class),v,"/graphics/atlas.png",count));sprites(label,v);}
        for(String path:new String[]{"/graphics/atlas.png","/graphics/missing.png","/graphics/broken.png",null}) {
            Object v=m(list,"a","load",list,String.class).invoke(null,path);sprites("static-"+enc(path),v);
            v=instance(list,new Class<?>[0]);m(list,"a","load",void.class,String.class,int.class,int.class).invoke(v,"/graphics/atlas.png",16,11);row("reload-"+enc(path)+":"+invoke(m(list,"a","load",void.class,String.class,int.class,int.class),v,path,7,7));sprites("reload-"+enc(path),v);
        }
    }
    static Class<?> array(Class<?> c){return java.lang.reflect.Array.newInstance(c,0).getClass();}
    static void animationState(String label,Object sprites)throws Exception {
        Object[] icons=(Object[])f(animations,"a","icons",array(animated)).get(sprites);StringBuilder b=new StringBuilder(label+":size="+m(animations,"a","size",int.class).invoke(sprites)+":dimensions="+f(list,"a","width",int.class).getInt(sprites)+","+f(list,"b","height",int.class).getInt(sprites)+":repaints="+io.getField("repaints").getInt(null)+":time="+f(animations,"a","time",long.class).getLong(sprites)+":frames=");
        if(icons==null)b.append("null");else for(Object v:icons)b.append(v==null?"null;":m(icon,"a","getWidth",int.class).invoke(v)+"x"+m(icon,"b","getHeight",int.class).invoke(v)+","+f(animated,"a","currentFrame",int.class).getInt(v)+","+f(animated,"a","sleepTime",long.class).getLong(v)+","+f(animated,"a","painted",boolean.class).getBoolean(v)+";");row(b.toString());
    }
    static void animateLoop(final String label,final Object sprites,final int screenMode,final boolean paint)throws Exception {
        io.getField("ticks").setInt(null,0);io.getField("limit").setInt(null,8);io.getField("repaints").setInt(null,0);io.getField("times").set(null,new long[]{1000,1000,1001,1100,1200,1301,1801,1791,2400});
        final Object active=instance(text,new Class<?>[]{String.class},"Active");io.getField("observer").set(null,new Runnable(){public void run(){try {
            int tick=io.getField("ticks").getInt(null);animationState(label+"-"+tick,sprites);io.getField("screen").set(null,screenMode==0||(screenMode==2&&tick%2==0)?active:new Object());
            if(paint&&tick%2==0){Object[] icons=(Object[])f(animations,"a","icons",array(animated)).get(sprites);if(icons!=null)for(int i=0;i<icons.length;i++)if(icons[i]!=null){Image image=canvas();Graphics g=image.getGraphics();row(label+"-draw-"+tick+"-"+i+":"+invoke(m(animated,"b","drawImage",void.class,Graphics.class,int.class,int.class),icons[i],g,5,7));frame(label+"-draw-"+tick+"-"+i,image,g);}}
        }catch(Exception e){throw new AssertionError(e);}}});
        Object result=invoke(m(animations,"run","run",void.class),sprites);if(!"exception:GraphicsIO$Stop".equals(result)&&!"exception:java.lang.ArithmeticException".equals(result))throw new AssertionError(label+":"+result);row(label+"-run:"+result);io.getField("observer").set(null,null);
    }
    static void animation()throws Exception {
        for(int scenario=0;scenario<4;scenario++)for(int screen=0;screen<3;screen++)for(boolean paint:new boolean[]{false,true}) {
            Object sprites=instance(animations,new Class<?>[0]);io.getField("starts").setInt(null,0);io.getField("repaints").setInt(null,0);String path=scenario==3?"/missing":("/graphics/anim"+scenario);String label="animation-"+scenario+"-"+screen+"-"+paint;
            row(label+"-load:"+invoke(m(animations,"a","load",void.class,String.class,int.class,int.class),sprites,path,-1,-1)+":starts="+io.getField("starts").getInt(null));animationState(label+"-loaded",sprites);animateLoop(label,sprites,screen,paint);
        }
    }
    static void bitmapFont()throws Exception {
        font=load("cg","DrawControls.TPropFont");Object v=instance(font,new Class<?>[]{String.class},"/font.prs");row("font-height:"+m(font,"a","getHeight",int.class).invoke(v));
        f(font,"a","font",font).set(null,v);
        for(int ch=0;ch<65536;ch++)row("font-width-"+ch+":"+m(font,"a","getStringWidth",int.class,String.class).invoke(v,String.valueOf((char)ch)));
        String[] words={null,"","ASCII 012 abc XYZ","\u0410\u0411\u0430\u0431\u0401\u0451", " \r\n\t \u010d\ud83d\ude00", "long long line reaching outside the clip"};
        for(String word:words)row("font-string-"+enc(word)+":"+invoke(m(font,"a","getStringWidth",int.class,String.class),v,word));
        for(int textColor:new int[]{0,0x224466,0xffffff})for(int blue:new int[]{0x0000ff,0x994422}) {
            m(options,"a","setInt",void.class,int.class,int.class).invoke(null,103,textColor);m(options,"a","setInt",void.class,int.class,int.class).invoke(null,113,blue);m(font,"a","setImage",void.class,String.class).invoke(v,"/font.png");
            for(int color:new int[]{textColor,blue,0xff0000,0x112233})for(int position:new int[]{-7,0,7,50})for(int[] clip:new int[][]{{0,0,80,60},{4,5,20,20},{0,0,0,0}})for(String word:words) {
                Image image=canvas();Graphics g=image.getGraphics();g.setClip(clip[0],clip[1],clip[2],clip[3]);String label="font-draw-"+textColor+"-"+blue+"-"+color+"-"+position+"-"+Arrays.toString(clip)+"-"+enc(word);
                Object result=invoke(m(font,"a","drawString",void.class,Graphics.class,int.class,int.class,String.class,int.class),v,g,position,position,word,color);if(word==null?!"exception:java.lang.NullPointerException".equals(result):result!=null)throw new AssertionError(label+":"+result);row(label+":"+result);frame(label,image,g);
            }
        }
        bitmapText();
    }
    static void bitmapText()throws Exception {
        Class<?> line=load("bl","DrawControls.TextLine"),part=load("bc","DrawControls.TextItem"),vl=load("cb","DrawControls.VirtualList");
        for(int fontSize:new int[]{0,8,16})for(int style:new int[]{0,2})for(boolean wrap:new boolean[]{false,true})for(int width:new int[]{40,90,176})for(String value:new String[]{null,"","word word word", "\u041f\u0440\u0438\u0432\u0435\u0442 \u0401\u0451\n\n\u041a\u043e\u043d\u0435\u0446", "\r\n leading  repeated spaces\n", "longlonglongword0123456789012345678901234567890123456789"}) {
            m(options,"a","setBoolean",void.class,int.class,boolean.class).invoke(null,132,wrap);Object v=instance(text,new Class<?>[]{String.class},"Bitmap text");m(vl,"b","setFontSize",void.class,int.class).invoke(v,fontSize);f(vl,"B","forcedWidth",int.class).setInt(v,width);
            m(text,"a","addBigText",text,String.class,int.class,int.class,int.class).invoke(v,"prefix ",0x994422,style,0);m(text,"a","addBigText",text,String.class,int.class,int.class,int.class).invoke(v,value,0xff0000,style,1);m(text,"a","addBigText",text,String.class,int.class,int.class,int.class).invoke(v," tail",0x224466,style,2);
            int size=(Integer)m(text,"a","getSize",int.class).invoke(v);String label="bitmap-text-"+fontSize+"-"+style+"-"+wrap+"-"+width+"-"+enc(value);StringBuilder b=new StringBuilder(label+":lines=");Image image=canvas();Graphics g=image.getGraphics();int y=3;
            for(int i=0;i<size;i++) {
                Object l=m(text,"a","getLine",line,int.class).invoke(v,i);b.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':').append((int)f(line,"a","last_charaster",char.class).getChar(l)).append(':').append(m(line,"b","getWidth",int.class,int.class).invoke(l,fontSize)).append(':').append(m(line,"a","getHeight",int.class,int.class).invoke(l,fontSize));
                for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))b.append('{').append(enc(f(part,"a","text",String.class).get(p))).append(':').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(':').append(f(part,"b","itemHeigthAndWidth",int.class).getInt(p)).append('}');b.append(']');
                m(text,"a","drawItemData",void.class,Graphics.class,int.class,int.class,int.class,int.class,int.class,int.class).invoke(v,g,i,2,y,width,60,0);y+=(Integer)m(text,"a","getItemHeight",int.class,int.class).invoke(v,i);
            }row(b.toString());frame(label,image,g);
        }
    }
    static void exercise()throws Exception {
        icon=load("e","DrawControls.Icon");list=load("f","DrawControls.ImageList");options=load(target.equals("MIDP2")?"cj":target.equals("MOTOROLA")?"ci":"ch","jimm.Options");
        Object[] values=new Object[256];for(int i=0;i<256;i++)values[i]=i<64||i>=224?"":i<128?Integer.valueOf(0):i<192?Boolean.FALSE:Long.valueOf(0);f(options,"a","options",Object[].class).set(null,values);
        animated=load(target.equals("MIDP2")?"be":target.equals("MOTOROLA")?"bd":"bb","DrawControls.AniIcon");animations=load(target.equals("MIDP2")?"cm":target.equals("MOTOROLA")?"cl":"ck","DrawControls.AniImageList");text=load(target.equals("MIDP2")?"bi":target.equals("MOTOROLA")?"bh":"bg","DrawControls.TextList");io=load("GraphicsIO","GraphicsIO");
        iconFrames();slicing();animation();if(target.equals("MOTOROLA"))bitmapFont();row("rasters:"+rasters);
    }
    public static void main(String[] args){try {
        ref=args[1].equals("reference");target=args[3];Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet midlet=MIDletBridge.getCurrentMIDlet();loader=midlet.getClass().getClassLoader();out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));exercise();out.close();System.out.println("PASS graphics: "+observations+" observations, "+rasters+" frames");System.exit(0);
    }catch(Throwable failure){failure.printStackTrace();System.exit(1);}}
}
