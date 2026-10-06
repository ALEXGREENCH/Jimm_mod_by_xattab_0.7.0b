import java.lang.reflect.*;
import java.util.*;
import java.security.*;
import java.io.*;
import javax.microedition.lcdui.*;

/** Full-frame pixels, caption bounds/routes, repeated taps, row hooks and scrollbar drags. */
public class VirtualListProbe extends MenuProbe {
    static Class<?> shim,canvas,images;
    static int rasters;
    static String[] boundsOld={"z","A","B","C"},boundsNames={"captionStatusEnd","captionXStatusEnd","captionPrivateEnd","captionSoundStart"};
    static Object make(String caption,int width,int height,int count)throws Exception{
        Object s=instance(shim,new Class<?>[]{String.class},caption);shim.getField("count").setInt(s,count);f(vl,"H","forcedWidth",int.class).setInt(s,width);f(vl,"I","forcedHeight",int.class).setInt(s,height);m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Open",Command.OK,1),1);m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Back",Command.BACK,2),2);return s;
    }
    static void alpha(int caption,int bar,int cursor)throws Exception{
        f(options,"e","captionAlpha",int.class).setInt(null,caption);f(options,"f","softbarAlpha",int.class).setInt(null,bar);f(options,"d","cursorAlpha",int.class).setInt(null,cursor);f(options,"c","cursorColor",int.class).setInt(null,0x6688aa);f(vl,"j","gradientHeight",int.class).setInt(null,0);bool(173,false);
    }
    static Image background(boolean filled)throws Exception{
        Image image=null;if(filled){image=Image.createImage(43,71);Graphics g=image.getGraphics();g.setColor(0xff0022);g.fillRect(0,0,43,71);g.setColor(0x11ee55);g.fillRect(1,1,9,40);g.setColor(0x6633dd);g.fillRect(13,23,17,45);}f(vl,"a","bgimage",Image.class).set(null,image);return image;
    }
    static Object symbol(int w,int h,int color)throws Exception{Image i=Image.createImage(w,h);Graphics g=i.getGraphics();g.setColor(color);g.fillRect(0,0,w,h);g.setColor(color^0xffffff);g.drawLine(0,0,w-1,h-1);return instance(icon,new Class<?>[]{Image.class,int.class,int.class,int.class,int.class},i,0,0,w,h);}
    static void icons(Object s,int mask)throws Exception{
        Object[] pictures={symbol(12,13,0x1144ee),symbol(9,11,0xeeaa22),symbol(10,12,0x778800),symbol(7,7,0xaa44cc),symbol(11,9,0x00aaff)};
        String[] old={"a","b","c","d","e"},names={"capImage","capXstImage","capPrivateImage","capHappyImage","capSoundImage"};for(int i=0;i<5;i++)f(vl,old[i],names[i],icon).set(s,(mask&(1<<i))==0?null:pictures[i]);for(int key:new int[]{176,177,178,180})bool(key,true);
    }
    static int[] limits(Object s)throws Exception{int[] a=new int[4];for(int i=0;i<4;i++)a[i]=f(vl,boundsOld[i],boundsNames[i],int.class).getInt(s);return a;}
    static void render(String label,Object s)throws Exception{
        int w=f(vl,"H","forcedWidth",int.class).getInt(s),h=f(vl,"I","forcedHeight",int.class).getInt(s);Image image=Image.createImage(w,h);Graphics g=image.getGraphics();g.setColor(0xcc00cc);g.fillRect(0,0,w,h);m(vl,"a","paintAllOnGraphics",void.class,Graphics.class,int.class,int.class,int.class).invoke(s,g,1,-1,-1);
        int[] rgb=new int[w*h];image.getRGB(rgb,0,w,0,0,w,h);MessageDigest hash=MessageDigest.getInstance("SHA-256");for(int p:rgb){hash.update((byte)(p>>>24));hash.update((byte)(p>>>16));hash.update((byte)(p>>>8));hash.update((byte)p);}row(label+":raster="+w+"x"+h+":"+b64(hash.digest())+":bounds="+Arrays.toString(limits(s))+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight());rasters++;
        if(System.getProperty("jimm.fixture.png","").equals(label)){java.awt.image.BufferedImage b=new java.awt.image.BufferedImage(w,h,java.awt.image.BufferedImage.TYPE_INT_ARGB);b.setRGB(0,0,w,h,rgb,0,w);javax.imageio.ImageIO.write(b,"png",new File(System.getProperty("jimm.fixture.png.dir","."),"vlist-"+n("reference","source")+".png"));}
    }
    static void frames()throws Exception{
        for(boolean bg:new boolean[]{false,true})for(boolean full:new boolean[]{false,true})for(int count:new int[]{0,3,30})for(int header:new int[]{0,170,171,255})for(int bar:new int[]{0,170,171,255})for(int cursor:new int[]{0,10,11,128,255}){
            alpha(header,bar,cursor);background(bg);Object s=make("Caption",176,220,count);icons(s,31);f(vl,"d","fullScreen",boolean.class).setBoolean(s,full);render("frame-"+bg+"-"+full+"-"+count+"-"+header+"-"+bar+"-"+cursor,s);
        }
        for(int mask=0;mask<32;mask++)for(String caption:new String[]{null,"","A long caption that crosses all the available space"})for(int size:new int[]{90,176}){alpha(128,128,128);background(true);Object s=make(caption,size,size==90?80:220,8);icons(s,mask);render("icons-"+mask+"-"+enc(caption)+"-"+size,s);}
        for(int font:new int[]{0,8,16})for(int style:new int[]{0,1,2,3})for(int count:new int[]{1,7,30})for(int selected:new int[]{0,3}){alpha(255,171,10);background(true);Object s=make("Fonts",176,110,count);shim.getField("itemStyle").setInt(s,style);m(vl,"b","setFontSize",void.class,int.class).invoke(s,font);f(vl,"b","currItem",int.class).setInt(s,selected);render("font-"+font+"-"+style+"-"+count+"-"+selected,s);}
    }
    static void pointer(Object s,int x,int y)throws Exception{m(vl,"b","pointerPressed",void.class,int.class,int.class).invoke(s,x,y);}
    static String route(Object screen)throws Exception{return screen==menuField("a","statusList",text)?"status":screen==menuField("b","privateStatusActList",text)?"privacy":screen==menuField("a","selector",selector)?"xstatus":screen==f(list,"a","tree",tree).get(null)?"contacts":screen==mainList()?"menu":"other";}
    static void captionRoutes()throws Exception{
        Object contactTree=f(list,"a","tree",tree).get(null);f(vl,"H","forcedWidth",int.class).setInt(contactTree,176);f(vl,"I","forcedHeight",int.class).setInt(contactTree,220);m(vl,"c","setCaption",void.class,String.class).invoke(contactTree,"Contacts");
        // Activation is captured by FileTransferFixture; provide equal bar data explicitly.
        f(vl,"a","leftMenu",Command.class).set(contactTree,new Command("Open",Command.OK,1));f(vl,"b","rightMenu",Command.class).set(contactTree,new Command("Back",Command.BACK,2));
        for(int mask:new int[]{0,1,2,4,8,16,31})for(boolean active:new boolean[]{false,true}){
            icons(contactTree,mask);alpha(255,255,128);background(false);render("caption-route-"+mask+"-"+active,contactTree);int[] b=limits(contactTree);int cap=(Integer)m(vl,"m","getCapHeight",int.class).invoke(contactTree);
            for(int x:new int[]{-1,0,b[0]-1,b[0],b[1]-1,b[1],b[2]-1,b[2],b[3],b[3]+1,175,176,200})for(int y:new int[]{-1,0,cap-1}){
                m(vl,"b","activate",void.class,Display.class).invoke(active?contactTree:mainList(),Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(contactTree,176);f(vl,"I","forcedHeight",int.class).setInt(contactTree,220);f(mt,"a","statusList",text).set(null,null);bool(150,false);bool(168,true);resetEnv();f(list,"b","enterContactMenu",boolean.class).setBoolean(null,true);
                pointer(contactTree,x,y);String label="tap-cap-"+mask+"-"+active+"-"+x+"-"+y;row(label+":route="+route(current())+":silent="+m(options,"a","getBoolean",boolean.class,int.class).invoke(null,150)+":vibra="+m(options,"a","getInt",int.class,int.class).invoke(null,75)+":saves="+env.getField("saves").getInt(null)+":enter="+f(list,"b","enterContactMenu",boolean.class).getBoolean(null));actions().clear();
            }
        }
    }
    static void inputState(String label,Object s)throws Exception{String row=label+":selected="+f(vl,"b","currItem",int.class).getInt(s)+":top="+f(vl,"d","topItem",int.class).getInt(s)+":drag="+f(vl,"i","lastPointerTopItem",int.class).getInt(null)+":y="+f(vl,"h","lastPointerYCrd",int.class).getInt(null);for(String field:new String[]{"commands","moves","hooks","hookIndex","hookMode"})row+=":"+field+"="+shim.getField(field).get(s);row+=":last="+enc(shim.getField("lastCommand").get(s));VirtualListProbe.row(row);}
    static void rowInput()throws Exception{
        for(boolean hook:new boolean[]{false,true})for(int start:new int[]{0,1,2}){
            Object s=make("Rows",176,220,20);shim.getField("consumeHook").setBoolean(s,hook);m(vl,"b","activate",void.class,Display.class).invoke(s,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(s,176);f(vl,"I","forcedHeight",int.class).setInt(s,220);f(vl,"b","currItem",int.class).setInt(s,start);int cap=(Integer)m(vl,"m","getCapHeight",int.class).invoke(s);f(vl,"i","lastPointerTopItem",int.class).setInt(null,-1);f(vl,"h","lastPointerYCrd",int.class).setInt(null,-1);
            for(int tap=0;tap<3;tap++){pointer(s,10,cap+16+5);inputState("row-"+hook+"-"+start+"-"+tap,s);}Object nativeCanvas=f(vl,"a","virtualCanvas",canvas).get(null);m(canvas,"pointerReleased","pointerReleased",void.class,int.class,int.class).invoke(nativeCanvas,10,cap+16+5);inputState("release-"+hook+"-"+start,s);
            for(int y:new int[]{cap,cap+1,219,220})for(int x:new int[]{0,1,2,154,156,170,176}){pointer(s,x,y);inputState("edge-"+hook+"-"+start+"-"+x+"-"+y,s);}
        }
        for(int count:new int[]{3,20,100})for(int dragY:new int[]{-100,0,40,219,1000}){Object s=make("Drag",176,220,count);m(vl,"b","activate",void.class,Display.class).invoke(s,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(s,176);f(vl,"I","forcedHeight",int.class).setInt(s,220);pointer(s,170,40);Object nativeCanvas=f(vl,"a","virtualCanvas",canvas).get(null);m(canvas,"pointerDragged","pointerDragged",void.class,int.class,int.class).invoke(nativeCanvas,170,dragY);inputState("drag-"+count+"-"+dragY,s);}
    }
    static void exerciseVirtualList()throws Exception{
        setupFiles();mt=load("ag","jimm.MainMenu");boundary=load("MenuIO","MenuIO");shim=load("RecoveryList","RecoveryList");canvas=load("am","DrawControls.VirtualCanvas");icon=load("e","DrawControls.Icon");images=load("f","DrawControls.ImageList");selector=load("af","jimm.util.Selector");main=menuField("a","_this",mt);if(main==null)main=instance(mt,new Class<?>[0]);frames();captionRoutes();rowInput();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseVirtualList();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
