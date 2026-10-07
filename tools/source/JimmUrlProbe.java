import java.lang.reflect.*;
import java.util.*;
import java.security.*;
import javax.microedition.lcdui.*;

/** Actual URL parser, JimmUI list creation, text metrics, commands and terminal failures. */
public class JimmUrlProbe extends ClipboardProbe {
    static Class<?> urlIO, line, part, icon;
    static int cases, lists, requests, fragments, commands, rasters;
    static Object previous;
    static Vector requests() throws Exception { return (Vector)urlIO.getField("urls").get(null); }
    static String utf(Object value) {
        if (value == null) return "null";
        String text = value.toString(); StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) result.append(String.format("%04x", (int)text.charAt(i)));
        return result.toString();
    }
    static String calls() throws Exception {
        StringBuilder result = new StringBuilder(); for (Object value : requests()) result.append(utf(value)).append(';');
        requests += requests().size(); requests().clear(); return result.toString();
    }
    static Object current() throws Exception { return m(ui,"a","getCurrentScreen",Object.class).invoke(null); }
    static String actual(Method method, Object receiver, Object... args) throws Exception {
        try { return String.valueOf(method.invoke(receiver,args)); }
        catch (InvocationTargetException e) {
            Throwable cause=e.getCause();
            if(cause instanceof LinkageError) throw (LinkageError)cause;
            return "exception:"+cause.getClass().getName();
        }
    }
    static void menus(String label, Object screen) throws Exception {
        StringBuilder result = new StringBuilder();
        for(String[] names:new String[][]{{"a","leftMenu"},{"b","rightMenu"}}) {
            Command c=(Command)f(vl,names[0],names[1],Command.class).get(screen);
            result.append(c==null?"null":utf(c.getLabel())+'/'+c.getCommandType()+'/'+c.getPriority()).append('|');
        }
        for (String[] names : new String[][]{{"a","leftMenuItems"},{"b","rightMenuItems"}}) {
            result.append('[');
            for (Object value : (Vector)f(vl,names[0],names[1],Vector.class).get(screen)) {
                if (value instanceof Command) { Command c = (Command)value; result.append(utf(c.getLabel())).append('/').append(c.getCommandType()).append('/').append(c.getPriority()).append(';'); }
                else result.append("unexpected:").append(value.getClass().getName());
            }
            result.append(']');
        }
        row(label + ":menus=" + result + ":listener=" + (f(vl,"a","commandListener",CommandListener.class).get(screen) == controller));
    }
    static void state(String label) throws Exception {
        Object screen = f(ui,"e","URLList",text).get(null);
        row(label + ":background=" + (f(ui,"b","lastScreenBeforeUrlSelect",Object.class).get(null) == previous)
            + ":list=" + (screen != null) + ":active=" + (current() == screen) + ":previous=" + (current() == previous));
        if (screen == null) return;
        lists++;
        // Exercise actual painting so cached line heights and item dimensions are materialized.
        f(vl,"H","forcedWidth",int.class).setInt(screen,176);f(vl,"I","forcedHeight",int.class).setInt(screen,220);
        Image image=Image.createImage(176,220);Graphics g=image.getGraphics();g.setColor(0xcc00cc);g.fillRect(0,0,176,220);
        m(vl,"a","paintAllOnGraphics",void.class,Graphics.class,int.class,int.class,int.class).invoke(screen,g,1,-1,-1);
        int[] rgb=new int[176*220];image.getRGB(rgb,0,176,0,0,176,220);MessageDigest hash=MessageDigest.getInstance("SHA-256");
        for(int pixel:rgb){hash.update((byte)(pixel>>>24));hash.update((byte)(pixel>>>16));hash.update((byte)(pixel>>>8));hash.update((byte)pixel);}
        row(label+":raster="+b64(hash.digest()));rasters++;
        row(label + ":info-alias=" + (f(ui,"c","infoTextList",text).get(null) == screen)
            + ":caption=" + utf(f(vl,"a","caption",String.class).get(screen))
            + ":selected=" + f(vl,"b","currItem",int.class).getInt(screen) + ":top=" + f(vl,"d","topItem",int.class).getInt(screen)
            + ":text=" + utf(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(screen,0,true)));
        menus(label,screen);
        for (Object l : (Vector)f(text,"a","lines",Vector.class).get(screen)) {
            StringBuilder result = new StringBuilder();
            for (Object p : (Vector)f(line,"a","items",Vector.class).get(l)) {
                fragments++; result.append(utf(f(part,"a","text",String.class).get(p))).append('@')
                    .append(f(part,"a","fontAndColor",int.class).getInt(p)).append('/')
                    .append(f(part,"b","itemHeigthAndWidth",int.class).getInt(p)).append(';');
            }
            row(label + ":line=" + f(line,"a","bigTextIndex",int.class).getInt(l) + ':'
                + f(line,"b","height",int.class).getInt(l) + ':' + (int)f(line,"a","last_charaster",char.class).getChar(l) + ':' + result);
        }
    }
    static void exerciseUrls() throws Exception {
        ui=load("cf","jimm.JimmUI"); vl=load("cd","DrawControls.VirtualList"); text=load("bi","DrawControls.TextList");
        line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");urlIO=load("JimmUrlIO","JimmUrlIO");
        controller=instance(ui,new Class<?>[0]);previous=instance(text,new Class<?>[]{String.class},"Background");
        String[] values={null,"","no links","http://one.invalid/path","https://one.invalid/\u010d\u043f?x=1",
            "http://one.invalid/ http://two.invalid/path","https://one.invalid/x\nhttp://two.invalid/y\nftp://three.invalid/z",
            "www.one.invalid www.two.invalid", "http://same.invalid/ http://same.invalid/",
            "(http://one.invalid/a), https://two.invalid/q?x=1&y=2!", "http://one.invalid/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa https://two.invalid/bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"};
        for (int font : new int[]{0,1,2}) for (int style : new int[]{0,1,2,3,4,5,6,7}) for (int mode=0;mode<4;mode++) for (int index=0;index<values.length;index++) {
            integer(111,font); f(options,"g","fontStyle",int.class).setInt(null,style); urlIO.getField("mode").setInt(null,mode);requests().clear();
            for(String[] names:new String[][]{{"e","URLList"},{"c","infoTextList"},{"d","lstSelector"},{"f","tlContactMenu"},{"g","serverLists"}})f(ui,names[0],names[1],text).set(null,null);
            m(ui,"b","selectScreen",void.class,Object.class).invoke(null,previous);
            String label="url-"+font+'-'+style+'-'+mode+'-'+index;
            String result=actual(m(ui,"a","gotoURL",void.class,String.class,Object.class),null,values[index],previous);
            row(label+":result="+result+":requests="+calls());state(label);
            Object screen=f(ui,"e","URLList",text).get(null);
            if(screen!=null) {
                boolean select=index%2==0;
                int size=(Integer)m(text,"a","getSize",int.class).invoke(screen);
                // Select the last physical row, including wrapped URLs; retain real tagged text lookup.
                if(size>0)f(vl,"b","currItem",int.class).setInt(screen,size-1);
                result=actual(m(ui,"commandAction","commandAction",void.class,Command.class,Displayable.class),controller,
                    cmd(select?"j":"c",select?"cmdSelect":"cmdBack"),null);
                commands++;
                row(label+":command="+(select?"select":"back")+":result="+result+":requests="+calls());state(label+"-after");
            }
            cases++;
        }
        if(lists<500 || requests<100 || fragments<1000)throw new AssertionError("Missing successful URL paths: "+lists+"/"+requests+"/"+fragments);
        row("coverage:"+cases+":"+lists+":"+requests+":"+fragments+":"+commands+":"+rasters);
    }
    public static void main(String[] args) {try {run(args,new Exercise(){public void run()throws Exception{exerciseUrls();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
