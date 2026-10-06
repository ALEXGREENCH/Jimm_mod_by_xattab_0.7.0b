import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real main-menu lists, deferred status edits, cached return, group forms and privacy selection. */
public class MenuProbe extends OptionsProbe {
    static Class<?> mt,boundary,splash;
    static Object main;
    static Object menuField(String old,String name,Class<?> type)throws Exception{return f(mt,old,name,type).get(null);}
    static Object mainList()throws Exception{return menuField("d","list",text);}
    static Object statusList()throws Exception{return menuField("a","statusList",text);}
    static void fresh(boolean connected,int font,int status)throws Exception{
        f(options,"a","options",Object[].class).set(null,initial.clone());integer(111,font);integer(110,1);integer(92,37);integer(83,2);bool(155,false);
        m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)status);
        f(icq,"b","connected",boolean.class).setBoolean(null,connected);f(icq,"a","setPoint",boolean.class).setBoolean(null,connected);
        f(mt,"a","statusList",text).set(null,null);f(mt,"b","privateStatusActList",text).set(null,null);f(mt,"c","groupActList",text).set(null,null);f(mt,"a","textBoxForm",form).set(null,null);f(mt,"a","selector",selector).set(null,null);f(mt,"a","statusMessage",TextBox.class).set(null,null);
        f(ui,"a","curScreenTag",int.class).setInt(null,-1);f(splash,"r","status_index",int.class).setInt(null,999);boundary.getField("screen").set(null,null);boundary.getField("failure").setInt(null,0);
        m(mt,"b","activate",void.class).invoke(null);resetEnv();
    }
    static void selectMain(int tag)throws Exception{m(mt,"b","activate",void.class).invoke(null);pick(mainList(),tag);command(main,cmd("j","cmdSelect"));}
    static void nativeCommand(Command c,TextBox screen)throws Exception{m(mt,"commandAction","commandAction",void.class,Command.class,Displayable.class).invoke(main,c,screen);}
    static long online()throws Exception{return ref?(Long)m(options,"a$134622","getLong",long.class).invoke(null):(Long)m(options,"a$134622","getLong",long.class,int.class).invoke(null,192);}
    static void state(String label)throws Exception{
        row(label+":status="+online()+":privacy="+m(options,"a","getInt",int.class,int.class).invoke(null,110)+":saves="+env.getField("saves").getInt(null)+":splash="+f(splash,"r","status_index",int.class).getInt(null)+":cached="+(statusList()!=null)+":restore="+f(mt,"a","haveToRestoreStatus",boolean.class).getBoolean(null)+":just="+f(list,"a","justConnected",boolean.class).getBoolean(null));
        for(Object event:actions()){Object[] e=(Object[])event;Object[] a=(Object[])e[1];String s=label+":event="+e[0];if(e[0].equals("menu-smile"))s+=":"+(a[0]==a[1]);else if(e[0].equals("action")||e[0].equals("menu-wait")){Object task=a[e[0].equals("action")?0:1];Class<?> update=load("ct","jimm.comm.UpdateContactListAction"),group=load("bb","jimm.GroupItem");Object g=f(update,"a","gItem",group).get(task);s+=":"+f(update,"b","action",int.class).getInt(task)+":"+enc(m(group,"b","getName",String.class).invoke(g));if(e[0].equals("menu-wait"))s+=":"+enc(m(resource,"a","getString",String.class,String.class).invoke(null,a[0]))+":"+a[2];}else if(e[0].equals("file-error"))s+=":"+f(error,"a","_ErrCode",int.class).getInt(a[0])+":"+f(error,"a","critical",boolean.class).getBoolean(a[0]);else s+=":"+Arrays.toString(a);row(s);}actions().clear();
        Vector packets=(Vector)io.getField("packets").get(null);for(Object p:packets)row(label+":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));packets.clear();
    }
    static void mainMenus()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(boolean silent:new boolean[]{false,true})for(boolean eye:new boolean[]{false,true})for(int device=0;device<3;device++){
            Class<?> jimm=load("jimm.Jimm","jimm.Jimm");f(jimm,"b","is_phone_SE",boolean.class).setBoolean(null,device==1);f(jimm,"d","is_smart_SE",boolean.class).setBoolean(null,device==2);
            fresh(connected,2,32);bool(150,silent);bool(172,eye);pick(mainList(),7);m(mt,"a","build",void.class).invoke(null);String label="main-"+connected+"-"+silent+"-"+eye+"-"+device;layout(label,mainList());row(label+":selected="+m(text,"b","getCurrTextIndex",int.class).invoke(mainList()));
        }
        for(int x:new int[]{0,5,37,255})for(int privacy:new int[]{0,1,2,3,4,5,8}){fresh(false,0,0);integer(92,x);integer(110,privacy);m(mt,"a","build",void.class).invoke(null);layout("icons-"+x+"-"+privacy,mainList());}
        for(boolean heap:new boolean[]{false,true}){fresh(false,0,0);bool(155,heap);resetEnv();m(mt,"b","activate",void.class).invoke(null);row("activate-"+heap+":gc="+env.getField("collections").getInt(null)+":active="+(current()==mainList()));}
    }
    static void statusRoutes()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(boolean caps:new boolean[]{false,true})for(int font:new int[]{0,2})for(int selected:new int[]{0,32,12288,16384,20480,24576,8193,1,4,16,2,256}){
            fresh(connected,font,32);bool(131,caps);f(mt,"a","haveToRestoreStatus",boolean.class).setBoolean(null,true);for(int k:new int[]{7,19,20,21,22,23,24,25,26})string(k,"saved-"+k);
            selectMain(7);Object cached=statusList();String label="status-"+connected+"-"+caps+"-"+font+"-"+selected;layout(label,cached);pick(cached,selected);command(main,cmd("j","cmdSelect"));state(label+"-selected");TextBox box=(TextBox)boundary.getField("screen").get(null);
            if(box!=null){row(label+":editor="+enc(box.getTitle())+":"+enc(box.getString())+":"+box.getConstraints()+":"+box.getMaxSize());box.setString("unsaved");nativeCommand(new Command("unknown",Command.SCREEN,42),box);state(label+"-unknown");nativeCommand((Command)cmd("n","cmdInsertEmo"),box);state(label+"-smile");nativeCommand((Command)cmd("c","cmdBack"),box);row(label+":return="+(current()==cached)+":same="+(statusList()==cached)+":selected="+m(text,"b","getCurrTextIndex",int.class).invoke(cached));state(label+"-back");
                pick(cached,selected);command(main,cmd("j","cmdSelect"));box=(TextBox)boundary.getField("screen").get(null);box.setString("new \u0444\nmessage");nativeCommand((Command)cmd("a","cmdOk"),box);state(label+"-saved");for(int k:new int[]{7,19,20,21,22,23,24,25,26})row(label+":saved-"+k+"="+enc(m(options,"a","getString",String.class,int.class).invoke(null,k)));
            }
        }
        for(int failure:new int[]{1,2})for(int selected:new int[]{0,1,256}){fresh(true,0,32);selectMain(7);pick(statusList(),selected);boundary.getField("failure").setInt(null,failure);command(main,cmd("j","cmdSelect"));TextBox box=(TextBox)boundary.getField("screen").get(null);if(box!=null)nativeCommand((Command)cmd("a","cmdOk"),box);state("status-error-"+failure+"-"+selected);}
        fresh(false,0,32);selectMain(7);Object cached=statusList();command(main,cmd("c","cmdBack"));row("status-cancel:"+(statusList()==null)+":"+(current()==mainList()));selectMain(7);row("status-fresh:"+(cached!=statusList()));
    }
    static void privacyRoutes()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int font:new int[]{0,2})for(int selected:new int[]{1,2,3,4,5}){
            fresh(connected,font,0);integer(110,selected);selectMain(9);Object screen=menuField("b","privateStatusActList",text);String label="privacy-"+connected+"-"+font+"-"+selected;layout(label,screen);f(list,"a","justConnected",boolean.class).setBoolean(null,false);command(main,cmd("j","cmdSelect"));state(label);
            selectMain(9);command(main,cmd("c","cmdBack"));row(label+":back="+(current()==mainList()));
        }
    }
    static void groupForms()throws Exception{
        for(int font:new int[]{0,1,2}){fresh(true,font,0);selectMain(10);Object screen=menuField("c","groupActList",text);layout("groups-"+font,screen);pick(screen,1);command(main,cmd("j","cmdSelect"));Object group=menuField("a","textBoxForm",form);layout("add-group-"+font,group);TextField name=(TextField)menuField("a","uinTextField",TextField.class);name.setString("Group \u0444");row("group-name:"+enc(name.getString()));command(main,cmd("b","cmdCancel"));row("group-cancel:"+(current()==mainList())+":"+(menuField("a","textBoxForm",form)==null));
        }
        for(int font:new int[]{0,2})for(int mode:new int[]{1,2,3}){
            fresh(true,font,0);Class<?> groupType=load("bb","jimm.GroupItem");Object group=instance(groupType,new Class<?>[]{int.class,String.class},4,"Existing");Vector groups=(Vector)f(list,"b","gItems",Vector.class).get(null);groups.clear();groups.add(group);selectMain(10);Object management=menuField("c","groupActList",text);pick(management,mode);command(main,cmd("j","cmdSelect"));String label="group-action-"+font+"-"+mode;
            if(mode!=1){layout(label+"-selector",current());command(controller,cmd("a","cmdOk"));}
            if(mode!=3){Object groupForm=menuField("a","textBoxForm",form);layout(label+"-form",groupForm);((TextField)menuField("a","uinTextField",TextField.class)).setString("Updated");command(main,menuField("a","sendCommand",Command.class));}
            row(label+":mode="+menuField("a","status",int.class)+":name="+enc(m(groupType,"b","getName",String.class).invoke(group)));state(label);
        }
        fresh(false,0,0);row("irrelevant-send:"+invoke(m(mt,"commandAction","commandAction",void.class,Command.class,Displayable.class),main,menuField("a","sendCommand",Command.class),null));row("irrelevant-cancel:"+invoke(m(mt,"commandAction","commandAction",void.class,Command.class,Displayable.class),main,cmd("b","cmdCancel"),null));
    }
    static void exerciseMenu()throws Exception{
        setupFiles();mt=load("ag","jimm.MainMenu");boundary=load("MenuIO","MenuIO");splash=load("cv","jimm.SplashCanvas");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");selector=load("af","jimm.util.Selector");initial=values().clone();main=menuField("a","_this",mt);if(main==null)main=instance(mt,new Class<?>[0]);mainMenus();statusRoutes();privacyRoutes();groupForms();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseMenu();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
