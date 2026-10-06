import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real popup pixels/input, cached layout options and contact-list activation. */
public class ListMenuProbe extends VirtualListProbe {
    static Class<?> app,group;
    static void assign()throws Exception { m(vl,"d","assignSoftKeys",void.class).invoke(null); }
    static int vi(String old,String name)throws Exception { return f(vl,old,name,int.class).getInt(null); }
    static void activateList(Object s,int w,int h)throws Exception {
        m(vl,"b","activate",void.class,Display.class).invoke(s,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));
        f(vl,"H","forcedWidth",int.class).setInt(s,w);f(vl,"I","forcedHeight",int.class).setInt(s,h);
    }
    static Vector items(Object s,boolean right)throws Exception {
        return (Vector)f(vl,right?"b":"a",right?"rightMenuItems":"leftMenuItems",Vector.class).get(s);
    }
    static void menuState(String label,Object s)throws Exception {
        StringBuilder b=new StringBuilder(label+":state="+f(vl,"n","uiState",int.class).getInt(s)+":selected="+vi("s","curMenuItemIndex")+":top="+vi("y","topMenuItem")+":visible="+vi("x","visibleItemsMenuCount"));
        for(boolean right:new boolean[]{false,true}) { b.append(right?":right=":":left=");for(Object c:items(s,right))b.append(commandName((Command)c)).append(';'); }
        b.append(":commands=").append(shim.getField("commands").get(s)).append(":last=").append(enc(shim.getField("lastCommand").get(s)));
        row(b.toString());
    }
    static Object popup(boolean right,int count,int w,int h)throws Exception {
        Object s=make("Popup",w,h,3);activateList(s,w,h);
        for(int i=0;i<count;i++)m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command(i==1?"A very long menu entry that exceeds the canvas width":"Entry "+i,Command.SCREEN,(count-i)%4),right?4:3);
        int bar=(Integer)m(vl,"o","getMenuBarHeight",int.class).invoke(s);
        pointer(s,right?w-10:10,h-bar/2);return s;
    }
    static void popups()throws Exception {
        for(boolean right:new boolean[]{false,true})for(int count:new int[]{0,1,3,20})for(int shadow:new int[]{0,1})for(int color:new int[]{0x001122,0xffffff,0x557799})for(int w:new int[]{90,176}) {
            bool(143,false);integer(118,shadow);assign();alpha(171,128,128);background(true);f(options,"c","cursorColor",int.class).setInt(null,color);
            int h=w==90?80:220;Object s=popup(right,count,w,h);String label="popup-"+right+"-"+count+"-"+shadow+"-"+color+"-"+w;menuState(label,s);render(label,s);
            if(count>0) {
                for(int key:new int[]{-1,-1,-2,-5,-6,-7}) { m(vl,"c","keyReaction",void.class,int.class,int.class).invoke(s,key,1);menuState(label+"-key-"+key,s); }
            }
        }
        for(boolean right:new boolean[]{false,true})for(boolean swap:new boolean[]{false,true})for(int count:new int[]{1,3,20}) {
            bool(143,swap);integer(118,1);assign();alpha(255,255,128);background(false);
            for(int x:new int[]{-1,0,1,40,89,90,175,176})for(int y:new int[]{-1,0,14,15,40,190,205,219,220}) {
                Object s=popup(right,count,176,220);pointer(s,x,y);menuState("popup-tap-"+right+"-"+swap+"-"+count+"-"+x+"-"+y,s);
            }
        }
    }
    static void rowIcons()throws Exception {
        String[] fields={"rowImage","rowXStatus","rowHappy","rowBirthday","rowAuth","rowIgnore","rowVisibility","rowClient"};
        Object[] images=new Object[8];for(int i=0;i<8;i++)images[i]=symbol(5+i,7+i,0x223344+i*0x10111);
        for(boolean right:new boolean[]{false,true})for(boolean changed:new boolean[]{false,true})for(int shadow:new int[]{0,1})for(int mask=0;mask<256;mask++) {
            bool(185,right);integer(118,shadow);assign();if(changed)bool(185,!right);alpha(255,255,255);background(false);
            Object s=make("Icons",176,80,3);for(int i=0;i<8;i++)shim.getField(fields[i]).set(s,(mask&(1<<i))==0?null:images[i]);
            render("row-icons-"+right+"-"+changed+"-"+shadow+"-"+mask,s);
        }
        for(int width:new int[]{1,16,99,100,120})for(int offset:new int[]{-8,0,7,150}) {
            Object s=make("Wide icon",90,80,3);shim.getField("rowImage").set(s,symbol(width,12,0x22aa66));shim.getField("rowOffset").setInt(s,offset);render("row-width-"+width+"-"+offset,s);
        }
    }
    static void offsets()throws Exception {
        for(boolean nokia:new boolean[]{false,true})for(boolean se:new boolean[]{false,true})for(int offset:new int[]{-10,0,12,50})for(boolean swap:new boolean[]{false,true}) {
            f(app,"e","is_phone_NOKIA",boolean.class).setBoolean(null,nokia);f(app,"b","is_phone_SE",boolean.class).setBoolean(null,se);integer(95,offset);integer(96,offset+1);bool(143,swap);assign();alpha(128,171,11);background(false);
            row("offsets-"+nokia+"-"+se+"-"+offset+"-"+swap+":"+vi("q","leftOffset")+":"+vi("r","rightOffset")+":"+vi("k","MENU_LEFT_BAR")+":"+vi("l","MENU_RIGHT_BAR")+":"+vi("m","MENU_LEFT"));
            Object s=make("Offsets",176,80,0);icons(s,31);render("offsets-frame-"+nokia+"-"+se+"-"+offset+"-"+swap,s);
        }
        f(app,"e","is_phone_NOKIA",boolean.class).setBoolean(null,false);f(app,"b","is_phone_SE",boolean.class).setBoolean(null,false);bool(143,false);assign();
    }
    static void activation()throws Exception {
        Object t=f(list,"a","tree",tree).get(null);
        row("startup-bars:"+menu(t));
        for(boolean connected:new boolean[]{false,true})for(boolean groups:new boolean[]{false,true})for(int count:new int[]{0,1})for(boolean notified:new boolean[]{false,true})for(boolean heap:new boolean[]{false,true})for(int font:new int[]{0,2}) {
            reset(false);f(icq,"b","connected",boolean.class).setBoolean(null,connected);f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);f(list,"c","needPlayMessNotif",boolean.class).setBoolean(null,notified);
            bool(136,groups);bool(130,false);bool(134,false);bool(129,false);bool(155,heap);bool(171,true);bool(150,false);integer(111,font);integer(110,2);integer(194,3);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,32L);
            if(count>0) { ((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);((Vector)f(list,"b","gItems",Vector.class).get(null)).add(instance(group,new Class<?>[]{int.class,String.class},4,"Group")); }
            m(vl,"m","removeAllCommands",void.class).invoke(t);m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(t,new Command("Stale",Command.SCREEN,99),3);m(vl,"c","setCyclingCursor",void.class,boolean.class).invoke(t,false);resetEnv();
            m(list,"a","activate",void.class).invoke(null);f(vl,"H","forcedWidth",int.class).setInt(t,176);f(vl,"I","forcedHeight",int.class).setInt(t,220);
            row("activation-metrics:"+f(options,"d","cursorAlpha",int.class).getInt(null)+":"+m(vl,"a","getItemHeight",int.class,int.class).invoke(t,0));
            String label="activate-"+connected+"-"+groups+"-"+count+"-"+notified+"-"+heap+"-"+font;
            row(label+":bars="+menu(t)+":left-items="+items(t,false).size()+":right-items="+items(t,true).size()+":font="+m(vl,"c","getFontSize",int.class).invoke(t)+":cycling="+f(vl,"c","cyclingCursor",boolean.class).getBoolean(t)+":locked="+f(vl,"b","dontRepaint",boolean.class).getBoolean(t)+":current="+(current()==t)+":notice="+f(list,"c","needPlayMessNotif",boolean.class).getBoolean(null)+":gc="+env.getField("collections").getInt(null)+":rows="+m(vl,"a","getSize",int.class).invoke(t));
            for(Object event:actions()) { Object[] e=(Object[])event;if(e[0].equals("list-sound"))row(label+":sound="+Arrays.toString((Object[])e[1])); }actions().clear();
            render(label,t);
        }
    }
    static void groups()throws Exception {
        for(int online:new int[]{-2,0,4,Integer.MAX_VALUE})for(int total:new int[]{-5,0,7})for(boolean hide:new boolean[]{false,true})for(int font:new int[]{0,1,2,3}) {
            Object g=instance(group,new Class<?>[]{int.class,String.class},4,"Group");integer(112,font);bool(130,hide);m(group,"a","setCounters",void.class,int.class,int.class).invoke(g,online,total);
            String label="group-data-"+online+"-"+total+"-"+hide+"-"+font;
            row(label+":text="+enc(m(group,"c","getText",String.class).invoke(g))+":image="+m(group,"k","getImageIndex",int.class).invoke(g)+":font="+m(group,"b","getFontStyle",int.class).invoke(g));
            m(group,"b","updateCounters",void.class,int.class,int.class).invoke(g,1,-1);row(label+":updated="+enc(m(group,"c","getText",String.class).invoke(g)));
        }
        Object t=f(list,"a","tree",tree).get(null),controller=f(list,"a","_this",list).get(null);
        for(boolean expanded:new boolean[]{false,true}) {
            reset(false);bool(136,true);bool(130,false);bool(129,false);bool(155,false);integer(112,0);integer(111,0);f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);
            Object g=instance(group,new Class<?>[]{int.class,String.class},4,"Group");f(group,"a","expanded",boolean.class).setBoolean(g,expanded);((Vector)f(list,"b","gItems",Vector.class).get(null)).add(g);((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);
            for(int step=0;step<3;step++) {
                if(step==1) { m(vl,"k","setCurrentItem",void.class,int.class).invoke(t,0);m(list,"commandAction","commandAction",void.class,Command.class,Displayable.class).invoke(controller,cmd("j","cmdSelect"),null); }
                f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);m(list,"a","activate",void.class).invoke(null);f(vl,"H","forcedWidth",int.class).setInt(t,176);f(vl,"I","forcedHeight",int.class).setInt(t,220);
                row("expanded-"+expanded+"-"+step+":"+f(group,"a","expanded",boolean.class).getBoolean(g)+":"+m(vl,"a","getSize",int.class).invoke(t));render("expanded-frame-"+expanded+"-"+step,t);
            }
        }
    }
    static void exerciseMenus()throws Exception {
        setupFiles();mt=load("ag","jimm.MainMenu");boundary=load("MenuIO","MenuIO");shim=load("RecoveryList","RecoveryList");canvas=load("am","DrawControls.VirtualCanvas");icon=load("e","DrawControls.Icon");app=load("jimm.Jimm","jimm.Jimm");group=load("bb","jimm.GroupItem");selector=load("af","jimm.util.Selector");main=menuField("a","_this",mt);if(main==null)main=instance(mt,new Class<?>[0]);
        for(String[] font:new String[][]{{"d","capFont"},{"e","menuBarFont"},{"f","menuItemsFont"}})row("font-style-"+font[1]+":"+((Font)f(vl,font[0],font[1],Font.class).get(null)).getStyle());
        activation();groups();offsets();rowIcons();popups();row("rasters:"+rasters);
    }
    public static void main(String[] args) {
        try { run(args,new Exercise(){public void run()throws Exception{exerciseMenus();}});System.exit(0); }
        catch(Throwable e) { e.printStackTrace();System.exit(1); }
    }
}
