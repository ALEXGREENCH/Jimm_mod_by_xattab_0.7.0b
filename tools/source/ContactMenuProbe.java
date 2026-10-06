import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Compare May contact menus, privacy choices, add-user search and local-info values. */
public class ContactMenuProbe extends ClipboardProbe {
    static Class<?> line,part,events,search,searchAction,group;
    static Vector events()throws Exception{return (Vector)events.getField("events").get(null);}
    static Object current()throws Exception{return m(ui,"a","getCurrentScreen",Object.class).invoke(null);}
    static Object make()throws Exception{return instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);}
    static void value(Object c,int key,int value)throws Exception{m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(c,key,value);}
    static void flag(Object c,int key,boolean value)throws Exception{m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,key,value);}
    static Object menu(Object c)throws Exception{m(ui,"a","showContactMenu",void.class,contact).invoke(null,c);return f(ui,"f","tlContactMenu",text).get(null);}
    static void select(Object screen,int index)throws Exception{m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen,index);command(controller,cmd("j","cmdSelect"));}
    static String view(Object screen)throws Exception{
        StringBuilder result=new StringBuilder();
        for(Object l:(Vector)f(text,"a","lines",Vector.class).get(screen)){
            result.append(f(line,"a","bigTextIndex",int.class).getInt(l)).append('[');
            for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))result.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(';');
            result.append(']');
        }
        result.append(":font=").append(f(vl,"u","fontSize",int.class).getInt(screen));
        result.append(":active=").append(current()==screen);
        return result.toString();
    }
    static void exercise()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");
        events=load("MessageIO","MessageIO");search=load("cp","jimm.Search");searchAction=load("o","jimm.comm.SearchAction");group=load("bb","jimm.GroupItem");
        controller=f(ui,"a","_this",ui).get(null);owner=m(icq,"a","getIcq",icq).invoke(null);reset(false);string(0,"54321");integer(97,1);
        Class<?> jimm=load("jimm.Jimm","jimm.Jimm");
        for(boolean connected:new boolean[]{false,true})for(int flags=0;flags<8;flags++)for(int status:new int[]{-1,0,1,256})for(boolean camera:new boolean[]{false,true}){
            f(icq,"b","connected",boolean.class).setBoolean(null,connected);f(jimm,"g","supportsCamCapture",boolean.class).setBoolean(null,camera);
            Object c=make();flag(c,2,(flags&1)!=0);flag(c,8,(flags&2)!=0);flag(c,32,(flags&4)!=0);value(c,192,status);value(c,73,0);
            row("menu-"+connected+"-"+flags+"-"+status+"-"+camera+":"+view(menu(c)));
        }
        f(jimm,"g","supportsCamCapture",boolean.class).setBoolean(null,false);
        Object c=make();value(c,192,0);flag(c,2,false);flag(c,8,false);flag(c,32,false);
        Vector groups=(Vector)f(list,"b","gItems",Vector.class).get(null);
        for(int count=0;count<3;count++){
            if(count>0)groups.add(instance(group,new Class<?>[]{int.class,String.class},count,"Group "+count));
            for(boolean enabled:new boolean[]{false,true}){bool(136,enabled);row("groups-"+count+"-"+enabled+":"+view(menu(c)));}
        }
        for(int flags=0;flags<8;flags++){
            m(contact,"e","setVisibleId",void.class,int.class).invoke(c,(flags&1)==0?0:17);m(contact,"f","setInvisibleId",void.class,int.class).invoke(c,(flags&2)==0?0:18);m(contact,"d","setIgnoreId",void.class,int.class).invoke(c,(flags&4)==0?0:19);
            select(menu(c),16);Object privacy=f(ui,"g","serverLists",text).get(null);row("privacy-"+flags+":"+view(privacy));
            command(controller,cmd("c","cmdBack"));
        }
        events().clear();m(ui,"b","addUser",void.class,contact).invoke(null,c);
        for(Object e:events()){
            Object[] event=(Object[])e,params=(Object[])event[1];
            if(event[0].equals("action")){
                Object a=params[0],container=f(searchAction,"a","cont",search).get(a);
                row("add-user:"+Arrays.toString((String[])f(searchAction,"a","search",String[].class).get(a))+":lite="+f(search,"a","liteVersion",boolean.class).getBoolean(container));
            }else row("add-user:"+event[0]+":"+params[2]);
        }
        flag(c,8,true);f(icq,"b","connected",boolean.class).setBoolean(null,true);events().clear();select(menu(c),14);row("add-selected:"+events().size());
        Class<?> chat=load("y","jimm.ChatTextList");Object conversation=instance(chat,new Class<?>[]{String.class,contact},"Known",c);
        events().clear();command(conversation,f(chat,"g","cmdAddUrs",Command.class).get(null));row("add-from-chat:"+events().size());
        privacyActions(c);localInfo(c);statusClock(c);
    }
    static void privacyActions(Object c)throws Exception{
        Class<?> privacyAction=load("cq","jimm.comm.ServerListsAction");
        for(int index:new int[]{2,3,14}){
            select(menu(c),16);Object screen=f(ui,"g","serverLists",text).get(null);events().clear();f(list,"a","justConnected",boolean.class).setBoolean(null,false);
            select(screen,index);
            row("privacy-selected-"+index+":suppressed="+f(list,"a","justConnected",boolean.class).getBoolean(null)+":closed="+(f(ui,"g","serverLists",text).get(null)==null));
            for(Object e:events()){
                Object[] event=(Object[])e,params=(Object[])event[1];
                if(event[0].equals("action"))row("privacy-action:"+f(privacyAction,"b","list",int.class).getInt(params[0])+":"+(f(privacyAction,"a","item",contact).get(params[0])==c));
                else if(event[0].equals("contact-schedule")){
                    ((TimerTask)params[1]).run();row("privacy-timer:"+params[2]+":"+f(list,"a","justConnected",boolean.class).getBoolean(null));
                }
            }
        }
    }
    static void statusClock(Object c)throws Exception{
        reset(false);((Vector)f(list,"a","cItems",Vector.class).get(null)).add(c);f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);
        // Repeated offline updates exercise the shared timestamp without sound/blink timers.
        value(c,192,-1);value(c,75,0);bool(172,false);
        Method updateStatus=m(list,"a","update",void.class,String.class,int.class,int.class,byte[].class,byte[].class,int.class,int.class,int.class,int.class,int.class,int.class,int.class,int.class);
        for(long time:new long[]{1273665600000L,1273665601000L,1273665660000L,1273665661000L}){
            io.getField("now").setLong(null,time);f(list,"a","justConnected",boolean.class).setBoolean(null,false);
            String result=invoke(updateStatus,null,"12345",-1,-1,null,null,0,-1,8,0,0,3600,0,0);
            row("status-clock:"+time+":"+result+":"+f(contact,"b","statusUpdateTime",long.class).getLong(c)+":"+f(contact,"a","lastOfflineActivity",long.class).getLong(c));
        }
        for(boolean requestInfo:new boolean[]{false,true}){
            events().clear();f(list,"a","justConnected",boolean.class).setBoolean(null,true);
            m(list,"b","afterConnect",void.class,boolean.class).invoke(null,requestInfo);
            row("after-connect-"+requestInfo+":"+events().size()+":"+f(list,"a","justConnected",boolean.class).getBoolean(null));
            for(Object e:events()){
                Object[] event=(Object[])e,params=(Object[])event[1];
                if(event[0].equals("contact-schedule")){
                    ((TimerTask)params[1]).run();row("after-connect-timer:"+params[2]+":"+f(list,"a","justConnected",boolean.class).getBoolean(null));
                }else row("after-connect-request:"+load("ce","jimm.comm.RequestInfoAction").isInstance(params[0]));
            }
        }
    }
    static void localInfo(Object c)throws Exception{
        long now=io.getField("now").getLong(null);
        for(int caps:new int[]{0,1,2,4,8,16,32,64,128,256,512,8192,-1})for(int online:new int[]{-1,0,1,3600})for(long elapsed:new long[]{0,61000,-7200000}){
            value(c,75,caps);value(c,195,online);f(contact,"b","statusUpdateTime",long.class).setLong(c,now-elapsed);
            value(c,191,1273665000);value(c,194,1273665100);value(c,71,123);value(c,73,8);value(c,74,5190);value(c,76,1);value(c,192,-1);
            m(contact,"a","setStringValue",void.class,int.class,String.class).invoke(c,2,"fixture");m(contact,"a","setStringValue",void.class,int.class,String.class).invoke(c,3,"Yesterday");
            events().clear();m(ui,"c","showClientInfo",void.class,contact).invoke(null,c);
            Object[] event=(Object[])events().lastElement(),params=(Object[])event[1];row("local-"+caps+"-"+online+"-"+elapsed+":"+enc(Arrays.toString((String[])params[0]))+":active="+(current()==params[1]));
        }
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exercise();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
