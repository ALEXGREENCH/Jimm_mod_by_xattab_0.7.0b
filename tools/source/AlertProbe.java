import java.lang.reflect.*;
import java.util.*;
import java.security.MessageDigest;
import javax.microedition.lcdui.*;

/** Real Canvas alert model, full pixels, frozen font, rebuilding and page/touch scrolling. */
public class AlertProbe extends TextListProbe {
    static Object alert;
    static String longText;
    static void fresh(int size,int style,boolean swap,int shadow,Object previous,String value,boolean both)throws Exception {
        integer(111,size);integer(112,style);integer(118,shadow);bool(143,swap);
        f(options,"g","fontStyle",int.class).setInt(null,style==Font.STYLE_ITALIC?Font.STYLE_ITALIC:Font.STYLE_PLAIN);
        m(vl,"d","assignSoftKeys",void.class).invoke(null);alpha(171,171,128);background(true);
        alert=instance(popup,new Class<?>[]{Object.class,String.class,int.class},previous,value,3);
        if(both)m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(alert,new Command("Yes",Command.OK,1),1);
    }
    static Object content()throws Exception{return f(popup,"a","lines",text).get(alert);}
    static void snapshot(String label)throws Exception {
        Font font=(Font)f(popup,"a","font",Font.class).get(alert);
        row(label+":font="+font.getFace()+","+font.getStyle()+","+font.getSize()+":popup="+f(popup,"o","popupWidth",int.class).getInt(alert)+","+f(popup,"p","popupHeight",int.class).getInt(alert)+","+f(popup,"q","visibleLines",int.class).getInt(alert)+":tag="+f(ui,"a","curScreenTag",int.class).getInt(null)+":text="+enc(f(popup,"a","text",String.class).get(alert)));
        s=content();state(label+"-lines");
    }
    static void frame(String label)throws Exception {
        Image image=Image.createImage(176,220);Graphics g=image.getGraphics();g.setColor(0xcc00cc);g.fillRect(0,0,176,220);g.setClip(2,3,170,210);
        m(popup,"a","paint",void.class,Graphics.class).invoke(alert,g);
        int[] rgb=new int[176*220];image.getRGB(rgb,0,176,0,0,176,220);MessageDigest hash=MessageDigest.getInstance("SHA-256");
        for(int p:rgb){hash.update((byte)(p>>>24));hash.update((byte)(p>>>16));hash.update((byte)(p>>>8));hash.update((byte)p);}
        row(label+":raster="+b64(hash.digest())+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight()+":color="+g.getColor()+":font="+g.getFont().getStyle()+","+g.getFont().getSize());rasters++;
    }
    static void layoutAlerts()throws Exception {
        Object previous=make("Behind alert",176,220,20);
        String[] values={null,"", "Short \u0442\u0435\u043a\u0441\u0442",longText};
        for(int size:new int[]{0,1,2})for(int style:new int[]{0,1,2})for(boolean swap:new boolean[]{false,true})for(int shadow:new int[]{0,1})for(boolean both:new boolean[]{false,true})for(boolean listBehind:new boolean[]{false,true})for(int v=0;v<values.length;v++) {
            fresh(size,style,swap,shadow,listBehind?previous:new TextBox("Editor","native screen",100,TextField.ANY),values[v],both);
            String label="layout-"+size+"-"+style+"-"+swap+"-"+shadow+"-"+both+"-"+listBehind+"-"+v;snapshot(label);frame(label);
        }
        for(String value:values){fresh(0,0,false,0,null,value,false);snapshot("null-background-"+enc(value));frame("null-background-"+enc(value));}
    }
    static void rebuild()throws Exception {
        Object previous=make("Rebuild",176,220,3);
        for(int size:new int[]{0,1,2})for(int style:new int[]{0,1,2})for(boolean repaint:new boolean[]{false,true}) {
            fresh(size,style,false,1,previous,longText,true);String label="rebuild-"+size+"-"+style+"-"+repaint;
            Object old=content();f(vl,"d","topItem",int.class).setInt(old,10);integer(111,(size+1)%3);integer(112,2);f(options,"g","fontStyle",int.class).setInt(null,2);
            for(String value:new String[]{"changed\r\n  words \n\nend",longText,null}) {
                f(popup,"a","text",String.class).set(alert,value);m(popup,"d","updateText",void.class,boolean.class).invoke(alert,repaint);
                row(label+"-new-model:"+(old!=content()));old=content();snapshot(label+"-"+enc(value));frame(label+"-"+enc(value));
            }
        }
    }
    static void pageScroll()throws Exception {
        Object previous=make("Scrolling",176,220,3);
        for(int size:new int[]{0,1,2})for(int style:new int[]{0,1,2})for(String value:new String[]{"one line",longText}) {
            fresh(size,style,false,1,previous,value,true);String label="scroll-"+size+"-"+style+"-"+(value==longText);
            for(int type:new int[]{1,2,3})for(int key:new int[]{-2,-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,-1,50,56}) {
                m(popup,"a","doKeyreaction",void.class,int.class,int.class).invoke(alert,key,type);
                row(label+"-"+key+"-"+type+":"+f(vl,"d","topItem",int.class).getInt(content()));
            }snapshot(label);frame(label);
        }
    }
    static void touchScroll()throws Exception {
        Object previous=make("Touch scrolling",176,220,3);
        for(int size:new int[]{0,1,2})for(String value:new String[]{null,"one line",longText})for(int start:new int[]{-1,0,1,10000})for(int y:new int[]{-100,0,40,80,150,500}) {
            fresh(size,0,false,0,previous,value,false);f(vl,"i","lastPointerTopItem",int.class).setInt(null,start);f(vl,"h","lastPointerYCrd",int.class).setInt(null,40);
            Object result=ref?invoke(m(popup,"i","pointerDragged",void.class,int.class),alert,y):invoke(m(popup,"i","pointerDragged",void.class,int.class,int.class),alert,90,y);
            row("touch-"+size+"-"+(value==longText)+"-"+enc(value==longText?"long":value)+"-"+start+"-"+y+":"+result+":"+f(vl,"d","topItem",int.class).getInt(content()));
        }
    }
    static void activeInput()throws Exception {
        Object previous=make("Active background",176,220,20);
        for(boolean swap:new boolean[]{false,true})for(boolean both:new boolean[]{false,true}) {
            fresh(0,0,swap,1,previous,longText,both);String label="active-"+swap+"-"+both;
            m(vl,"b","activate",void.class,Display.class).invoke(alert,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));
            f(vl,"H","forcedWidth",int.class).setInt(alert,176);f(vl,"I","forcedHeight",int.class).setInt(alert,220);render(label,alert);snapshot(label);
            for(int x:new int[]{0,80,170,175})for(int y:new int[]{0,24,25,60,100,150,200,219}) {
                pointer(alert,x,y);Object nativeCanvas=f(vl,"a","virtualCanvas",load("am","DrawControls.VirtualCanvas")).get(null);
                m(nativeCanvas.getClass(),"pointerDragged","pointerDragged",void.class,int.class,int.class).invoke(nativeCanvas,x,y+20);
                row(label+"-touch-"+x+"-"+y+":"+f(vl,"d","topItem",int.class).getInt(content())+":"+f(vl,"i","lastPointerTopItem",int.class).getInt(null)+":"+f(ui,"a","curScreenTag",int.class).getInt(null));
                m(nativeCanvas.getClass(),"pointerReleased","pointerReleased",void.class,int.class,int.class).invoke(nativeCanvas,x,y+20);
            }frame(label+"-after-input");
        }
    }
    static void exerciseAlerts()throws Exception {
        setupFiles();popup=load("ci","DrawControls.VirtualAlert");shim=load("RecoveryList","RecoveryList");icon=load("e","DrawControls.Icon");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");
        StringBuilder b=new StringBuilder();for(int i=0;i<40;i++)b.append("Line ").append(i).append(" wraps over the narrow phone screen.\n");longText=b.toString();
        layoutAlerts();rebuild();pageScroll();touchScroll();activeInput();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseAlerts();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
