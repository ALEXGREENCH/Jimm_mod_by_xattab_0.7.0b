import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real text wrapping, tagged selection, caches, images, scrolling and complete pixels. */
public class TextListProbe extends VirtualListProbe {
    static Class<?> line,part,itemData;
    static Object s;
    static Vector<String> callbacks=new Vector<String>();
    static void fresh(int font,int style,int width,int height,int mode,int shadow,boolean wrap)throws Exception {
        callbacks.clear();alpha(171,171,128);background(true);bool(132,wrap);integer(118,shadow);f(options,"g","fontStyle",int.class).setInt(null,style);m(vl,"d","assignSoftKeys",void.class).invoke(null);
        s=instance(text,new Class<?>[]{String.class},"Text list");m(vl,"b","setFontSize",void.class,int.class).invoke(s,font);f(vl,"w","cursorMode",int.class).setInt(s,mode);
        f(vl,"H","forcedWidth",int.class).setInt(s,width);f(vl,"I","forcedHeight",int.class).setInt(s,height);
        m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Open",Command.OK,1),1);m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Back",Command.BACK,2),2);
        Class<?> commands=load("bx","DrawControls.VirtualListCommands");Object listener=Proxy.newProxyInstance(loader,new Class<?>[]{commands},new InvocationHandler(){public Object invoke(Object p,Method m,Object[] a){callbacks.add(a.length==3?"key:"+a[1]+":"+a[2]:m.getName().equals(n("a","vlCursorMoved"))?"moved":"clicked");return null;}});m(vl,"a","setVLCommands",void.class,commands).invoke(s,listener);
    }
    static Vector lines()throws Exception{return (Vector)f(text,"a","lines",Vector.class).get(s);}
    static String image(Object i)throws Exception{return i==null?"null":m(icon,"a","getWidth",int.class).invoke(i)+"x"+m(icon,"b","getHeight",int.class).invoke(i);}
    static void state(String label)throws Exception {
        StringBuilder b=new StringBuilder(label+":size="+m(text,"a","getSize",int.class).invoke(s)+":selected="+f(vl,"b","currItem",int.class).getInt(s)+":top="+f(vl,"d","topItem",int.class).getInt(s)+":tag="+m(text,"b","getCurrTextIndex",int.class).invoke(s)+":callbacks="+callbacks+":lines=");callbacks.clear();
        for(Object l:lines()) {
            b.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':').append((int)f(line,"a","last_charaster",char.class).getChar(l)).append(':').append(f(line,"b","height",int.class).getInt(l));
            for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))b.append('{').append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(':').append(f(part,"b","itemHeigthAndWidth",int.class).getInt(p)).append(':').append(image(f(part,"a","image",icon).get(p))).append('}');b.append(']');
        }row(b.toString());
    }
    static void append(String value,int color,int style,int tag)throws Exception{m(text,"a","addBigText",text,String.class,int.class,int.class,int.class).invoke(s,value,color,style,tag);}
    static void internal(String value,int color,int style,int tag,boolean crlf,char last)throws Exception {
        if(ref)m(text,"a","internAdd",void.class,String.class,int.class,int.class,int.class,boolean.class,char.class).invoke(s,value,color,style,tag,crlf,last);
        else m(text,"a","internAdd",void.class,String.class,int.class,int.class,int.class,int.class,boolean.class,char.class).invoke(s,value,color,-1,style,tag,crlf,last);
    }
    static void query(String label)throws Exception {
        for(int tag:new int[]{-1,0,1,2,99})for(boolean whole:new boolean[]{false,true})for(int offset:new int[]{-1,0,2,10000})
            row(label+"-"+tag+"-"+whole+"-"+offset+":"+enc(invoke(m(text,"a","getTextByIndex",String.class,int.class,boolean.class,int.class),s,offset,whole,tag)));
    }
    static void layouts()throws Exception {
        String[] values={null,"","alpha beta gamma delta epsilon", "\r\nalpha\r beta\n\nend\n", "   leading  and  repeated spaces   ", "\u041f\u0440\u0438\u0432\u0435\u0442\t\u010d\u00edslo\nlongwordwithoutanyspaces1234567890"};
        for(int font:new int[]{0,8,16})for(int style:new int[]{0,1,2,3})for(boolean wrap:new boolean[]{false,true})for(int width:new int[]{40,90,176})for(int shadow:new int[]{0,1})for(int v=0;v<values.length;v++) {
            fresh(font,style,width,130,3,shadow,wrap);String label="layout-"+font+"-"+style+"-"+wrap+"-"+width+"-"+shadow+"-"+v;
            append("prefix ",0x223344,style,0);append(values[v],0x994422,style,1);m(text,"a","doCRLF",text,int.class).invoke(s,2);append("tail",0x336699,style,2);state(label);render(label,s);state(label+"-painted");
            for(int tag:new int[]{0,1,2,99,-1}){m(text,"a","selectTextByIndex",void.class,int.class).invoke(s,tag);row(label+"-select-"+tag+":"+f(vl,"b","currItem",int.class).getInt(s)+":"+m(text,"b","getCurrTextIndex",int.class).invoke(s)+":"+enc(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(s,0,false)));}
        }
    }
    static void mutations()throws Exception {
        for(int font:new int[]{0,8,16})for(int mode:new int[]{1,2,3}) {
            fresh(font,2,90,120,mode,1,false);String label="mutate-"+font+"-"+mode;state(label+"-empty");query(label+"-empty");
            internal(null,0xff112233,2,-1,false,'\0');internal("first",0x334455,1,7,false,'\0');internal("second",0x556677,2,8,true,' ');internal("third",0x112233,0,7,true,'\n');m(text,"a","doCRLF",text,int.class).invoke(s,99);state(label+"-chunks");query(label+"-chunks");render(label,s);
            m(text,"a","addImage",text,icon,String.class,int.class).invoke(s,symbol(25,13,0x228833),":image:",1);append(" after image and words",0x994422,3,1);m(text,"a","addImage",text,icon,String.class,int.class).invoke(s,symbol(150,30,0x7733dd),null,2);state(label+"-images");query(label+"-images");render(label+"-images",s);
            for(int selected:new int[]{-1,0,1,100}) {
                f(vl,"b","currItem",int.class).setInt(s,selected);
                for(int index:new int[]{-1,0,1,lines().size()-1,100})row(label+"-select-check-"+selected+"-"+index+":"+invoke(m(text,"a","isItemSelected",boolean.class,int.class),s,index));
            }
            f(text,"a","msgTable",Hashtable.class).get(s);((Hashtable)f(text,"a","msgTable",Hashtable.class).get(s)).put(Long.valueOf(5),Integer.valueOf(2));m(text,"a","clear",void.class).invoke(s);state(label+"-clear");row(label+"-msg-table:"+((Hashtable)f(text,"a","msgTable",Hashtable.class).get(s)).size());
        }
    }
    static void navigation()throws Exception {
        for(int mode:new int[]{1,2,3})for(boolean cycling:new boolean[]{false,true})for(int height:new int[]{80,150})for(boolean top:new boolean[]{false,true}) {
            fresh(8,0,90,height,mode,0,true);for(int tag=0;tag<8;tag++){append("block-"+tag+" with some longer text and another word",0x334455,tag%4,tag);m(text,"a","doCRLF",text,int.class).invoke(s,tag);}
            f(vl,"c","cyclingCursor",boolean.class).setBoolean(s,cycling);String label="nav-"+mode+"-"+cycling+"-"+height+"-"+top;state(label);
            for(int step:new int[]{1,1,1,1,3,-1,-1,-1,0,-3,100,-100}){row(label+"-move-"+step+":"+invoke(m(text,"a","moveCursor",void.class,int.class,boolean.class),s,step,top));state(label+"-move-"+step);}
            for(int key:new int[]{50,56,49,51,-1,-2,42,48}){m(vl,"c","keyReaction",void.class,int.class,int.class).invoke(s,key,1);state(label+"-key-"+key);}
        }
    }
    static void caches()throws Exception {
        for(int style:new int[]{0,1,2,3,4,7,127,128,255})for(int initial:new int[]{0,8,16})for(String value:new String[]{null,"","A", "wide and narrow WWW iii"}) {
            Object p=instance(part,new Class<?>[0]);f(part,"a","fontAndColor",int.class).setInt(p,(style<<24)|0x123456);f(part,"a","text",String.class).set(p,value);String label="cache-"+style+"-"+initial+"-"+enc(value);
            for(int font:new int[]{initial,8,0,16}) {
                row(label+"-"+font+":"+invoke(m(part,"a","getHeight",int.class,int.class),p,font)+":"+invoke(m(part,"b","getWidth",int.class,int.class),p,font)+":"+f(part,"b","itemHeigthAndWidth",int.class).getInt(p));
            }
            f(part,"a","text",String.class).set(p,"changed");row(label+"-changed:"+invoke(m(part,"b","getWidth",int.class,int.class),p,initial));
            f(part,"a","image",icon).set(p,symbol(7,11,0x112233));row(label+"-image:"+invoke(m(part,"a","getHeight",int.class,int.class),p,initial)+":"+invoke(m(part,"b","getWidth",int.class,int.class),p,initial));
        }
        for(int length:new int[]{3000,6000,12000}) {
            Object p=instance(part,new Class<?>[0]);f(part,"a","text",String.class).set(p,String.join("",Collections.nCopies(length,"W")));
            row("cache-large-"+length+":"+invoke(m(part,"b","getWidth",int.class,int.class),p,0)+":"+f(part,"b","itemHeigthAndWidth",int.class).getInt(p));
        }
    }
    static void staticText()throws Exception {
        for(int style:new int[]{0,2})for(int shadow:new int[]{0,1})for(int width:new int[]{40,90,176})for(String value:new String[]{null,"","alpha beta gamma delta", "\r\nline\n\nlast"}) {
            fresh(8,style,width,130,3,shadow,true);String label="static-"+style+"-"+shadow+"-"+width+"-"+enc(value);
            Object count=ref?m(text,"a$ba2c332","getLineNumbers",int.class,String.class,int.class,int.class,int.class).invoke(null,value,width,8,style):m(text,"a$ba2c332","getLineNumbers",int.class,String.class,int.class,int.class,int.class,int.class).invoke(null,value,width,8,style,0);
            row(label+":lines="+count);Image frame=Image.createImage(190,100);Graphics g=frame.getGraphics();g.setColor(0x443322);g.fillRect(0,0,190,100);g.setClip(3,5,160,80);
            m(text,"a","showText",void.class,Graphics.class,String.class,int.class,int.class,int.class,int.class,int.class,int.class).invoke(null,g,value,7,9,width,8,style,0x226688);
            int[] rgb=new int[190*100];frame.getRGB(rgb,0,190,0,0,190,100);java.security.MessageDigest hash=java.security.MessageDigest.getInstance("SHA-256");for(int p:rgb){hash.update((byte)(p>>>24));hash.update((byte)(p>>>16));hash.update((byte)(p>>>8));hash.update((byte)p);}
            row(label+":raster="+b64(hash.digest())+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight());rasters++;
        }
    }
    static void exerciseTextLists()throws Exception {
        setupFiles();icon=load("e","DrawControls.Icon");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");itemData=load("cn","DrawControls.ListItem");layouts();mutations();navigation();caches();staticText();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTextLists();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
