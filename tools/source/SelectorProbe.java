import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real smile/XStatus/color grids, cursor pixels, navigation and pointer consumption. */
public class SelectorProbe extends VirtualListProbe {
    static Object grid;
    static Class<?> emotions;
    static String[] oldFields={"o","p","q","r","s","t","u","v"};
    static String[] fields={"cols","rows","imgHeight","itemHeight","increment","curCol","imagesCount","selectorType"};
    static int number(int i)throws Exception{return f(selector,oldFields[i],fields[i],int.class).getInt(null);}
    static int length()throws Exception{return (Integer)m(selector,"m","getLength",int.class).invoke(grid);}
    static Object freshGrid(int kind,int selected,int width,int font,int opacity,boolean bg)throws Exception {
        System.setProperty("jimm.selector.width",Integer.toString(width));alpha(171,171,opacity);background(bg);integer(118,1);f(options,"g","fontStyle",int.class).setInt(null,0);m(vl,"d","assignSoftKeys",void.class).invoke(null);
        grid=instance(selector,new Class<?>[]{int.class,int.class},kind,selected);f(vl,"H","forcedWidth",int.class).setInt(grid,width);f(vl,"I","forcedHeight",int.class).setInt(grid,220);m(vl,"b","setFontSize",void.class,int.class).invoke(grid,font);
        m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(grid,new Command("Select",Command.OK,1),5);m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(grid,new Command("Back",Command.BACK,2),2);return grid;
    }
    static void gridState(String label)throws Exception {
        StringBuilder b=new StringBuilder(label+":selected="+m(selector,"b","getCurrSelectedIdx",int.class).invoke(grid)+":row="+f(vl,"b","currItem",int.class).getInt(grid)+":top="+f(vl,"d","topItem",int.class).getInt(grid)+":caption="+enc(f(vl,"a","caption",String.class).get(grid))+":emotion="+enc(f(emotions,"a","emotionText",String.class).get(null))+":length="+length());
        for(int i=0;i<fields.length;i++)b.append(':').append(fields[i]).append('=').append(number(i));row(b.toString());
    }
    static void render(String label,Object screen)throws Exception {
        int w=f(vl,"H","forcedWidth",int.class).getInt(screen),h=f(vl,"I","forcedHeight",int.class).getInt(screen);
        // MicroEmulator drawRGB writes beyond the clip for a partially visible
        // gradient row. Provide backing padding; observe only the real viewport.
        Image image=Image.createImage(w+64,h+64);Graphics g=image.getGraphics();g.setColor(0xcc00cc);g.fillRect(0,0,w+64,h+64);m(vl,"a","paintAllOnGraphics",void.class,Graphics.class,int.class,int.class,int.class).invoke(screen,g,1,-1,-1);
        int[] rgb=new int[w*h];image.getRGB(rgb,0,w,0,0,w,h);java.security.MessageDigest hash=java.security.MessageDigest.getInstance("SHA-256");for(int pixel:rgb){hash.update((byte)(pixel>>>24));hash.update((byte)(pixel>>>16));hash.update((byte)(pixel>>>8));hash.update((byte)pixel);}row(label+":raster="+w+"x"+h+":"+b64(hash.digest())+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight());rasters++;
    }
    static int[] selections(int length,int cols){return new int[]{0,cols-1,cols,length-1};}
    static void layouts()throws Exception {
        for(int kind=0;kind<3;kind++)for(int width:new int[]{90,150,176,240})for(int font:new int[]{0,8})for(int opacity:new int[]{10,11,128})for(boolean bg:new boolean[]{false,true}) {
            freshGrid(kind,0,width,font,opacity,bg);int len=length(),cols=number(0);for(int selected:selections(len,cols)){
                freshGrid(kind,selected,width,font,opacity,bg);String label="layout-"+kind+"-"+width+"-"+font+"-"+opacity+"-"+bg+"-"+selected;gridState(label);render(label,grid);
            }
        }
    }
    static void keyboard()throws Exception {
        for(int kind=0;kind<3;kind++)for(int width:new int[]{90,176,240}){
            freshGrid(kind,0,width,0,128,false);int len=length();for(int selected=0;selected<len;selected++)for(int key:new int[]{-3,-4}){
                freshGrid(kind,selected,width,0,128,false);String label="key-"+kind+"-"+width+"-"+selected+"-"+key;for(int type:new int[]{1,2,3}){row(label+"-"+type+":result="+invoke(m(vl,"c","keyReaction",void.class,int.class,int.class),grid,key,type));gridState(label+"-"+type);}
            }
            for(int selected:selections(len,number(0)))for(int key:new int[]{-1,-2,-5,50,52,54,56,999}){
                freshGrid(kind,selected,width,0,128,false);row("other-key-"+kind+"-"+width+"-"+selected+"-"+key+":result="+invoke(m(vl,"c","keyReaction",void.class,int.class,int.class),grid,key,1));gridState("other-key");
            }
        }
    }
    static void taps()throws Exception {
        for(int kind=0;kind<3;kind++)for(int width:new int[]{90,176,240}){
            freshGrid(kind,0,width,0,128,true);int len=length(),cols=number(0),cell=number(3);for(int selected:selections(len,cols))for(int x:new int[]{-100,-1,0,cell-1,cell,cell*(cols-1),width-1,width,width+100})for(int mode:new int[]{1,2}){
                freshGrid(kind,selected,width,0,128,true);String label="hook-"+kind+"-"+width+"-"+selected+"-"+x+"-"+mode;row(label+":result="+invoke(m(selector,"a","pointerPressedOnUtem",boolean.class,int.class,int.class,int.class,int.class),grid,selected/cols,x,10,mode));gridState(label);
            }
            freshGrid(kind,0,width,0,128,true);m(vl,"b","activate",void.class,Display.class).invoke(grid,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(grid,width);f(vl,"I","forcedHeight",int.class).setInt(grid,220);int cap=(Integer)m(vl,"m","getCapHeight",int.class).invoke(grid);
            for(int x:new int[]{cell/2,cell+cell/2,cell+cell/2,cell/2}){pointer(grid,x,cap+cell/2);gridState("pointer-"+kind+"-"+width+"-"+x);render("pointer",grid);}
        }
    }
    static void palette()throws Exception {
        int[] colors=(int[])f(selector,"a","colorTable",int[].class).get(null);for(int i=0;i<colors.length;i++)row("color-"+i+":"+colors[i]+":"+m(selector,"b","getColorIndex",int.class,int.class).invoke(null,colors[i]));
        for(int color:new int[]{-1,0x123456,0x1000000,Integer.MIN_VALUE,Integer.MAX_VALUE})row("unknown-color-"+color+":"+m(selector,"b","getColorIndex",int.class,int.class).invoke(null,color));
    }
    static void exerciseSelector()throws Exception {
        setupFiles();selector=load("af","jimm.util.Selector");emotions=load("bo","jimm.Emotions");icon=load("e","DrawControls.Icon");canvas=load("am","DrawControls.VirtualCanvas");palette();layouts();keyboard();taps();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseSelector();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
