import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/** Real main-menu selection, fresh editor state, RMS templates, save/back/disable and splash icon. */
public class XStatusProbe extends FileTransferProbe {
    static Class<?> menuType,xform,selector,icons,splash;
    static Object main;
    static TextField title(Object e)throws Exception{return (TextField)f(xform,"a","titleTextField",TextField.class).get(e);}
    static TextField description(Object e)throws Exception{return (TextField)f(xform,"b","descTextField",TextField.class).get(e);}
    static ChoiceGroup choices(Object e)throws Exception{return (ChoiceGroup)f(xform,"a","choiceGroup",ChoiceGroup.class).get(e);}
    static void record(String value)throws Exception{
        try{RecordStore.deleteRecordStore("xtraz");}catch(RecordStoreNotFoundException ignored){}
        if(value!=null){byte[] b=value.getBytes("UTF-8");RecordStore rms=RecordStore.openRecordStore("xtraz",true);rms.addRecord(b,0,b.length);rms.closeRecordStore();}
    }
    static void rms(String label)throws Exception{
        RecordStore rms=RecordStore.openRecordStore("xtraz",true);String s=label+":records="+rms.getNumRecords();
        for(int id=1;id<rms.getNextRecordID();id++)s+=":"+id+"="+b64(rms.getRecord(id));rms.closeRecordStore();row(s);
    }
    static Object open(int index)throws Exception{
        integer(92,index==0?37:index-1);m(menuType,"d","showXStatusSelector",void.class).invoke(null);
        Object selected=current();row("selector:"+selector.isInstance(selected)+":"+m(selector,"b","getCurrSelectedIdx",int.class).invoke(selected)+":"+menu(selected));
        command(main,cmd("j","cmdSelect"));row("selector-cleared:"+(f(menuType,"a","selector",selector).get(null)==null));
        return form.isInstance(current())?f(vl,"a","commandListener",CommandListener.class).get(current()):null;
    }
    static void screen(String label,Object e)throws Exception{
        Object screen=f(xform,"a","form",form).get(e);row(label+":active="+(current()==screen)+":index="+f(xform,"a","xstIndex",int.class).getInt(e)+":font="+f(vl,"u","fontSize",int.class).getInt(screen)+":menu="+menu(screen));
        for(TextField t:new TextField[]{title(e),description(e)})row(label+":text="+enc(t.getLabel())+":"+enc(t.getString())+":"+t.getMaxSize()+":"+t.getConstraints());
        ChoiceGroup c=choices(e);row(label+":choices="+c.size()+":"+enc(c.getString(0))+":"+enc(c.getString(1))+":"+c.isSelected(0)+":"+c.isSelected(1));
        row(label+":tree="+structure(f(tree,"a","root",node).get(screen)));
    }
    static void state(String label)throws Exception{
        String s=label;for(int key:new int[]{32,33})s+=":"+key+"="+enc(m(options,"a","getString",String.class,int.class).invoke(null,key));
        for(int key:new int[]{159,171})s+=":"+key+"="+m(options,"a","getBoolean",boolean.class,int.class).invoke(null,key);
        s+=":status="+m(options,"a","getInt",int.class,int.class).invoke(null,92)+":saves="+env.getField("saves").get(null);
        Object expected=m(icq,"a","getCurrentXStatus",icons).invoke(null);s+=":splash="+(f(splash,"a","xstatus_img",icons).get(null)==expected);row(s);
        for(Object item:actions()){Object[] e=(Object[])item;row(label+":event="+e[0]+":"+Arrays.toString((Object[])e[1]));}actions().clear();
    }
    static void formsAndRoutes()throws Exception{
        for(int font:new int[]{0,1,2})for(boolean caps:new boolean[]{false,true})for(int index:new int[]{1,5,37}){
            record(null);resetEnv();integer(111,font);bool(131,caps);bool(159,caps);bool(171,!caps);Object e=open(index);String label="form-"+font+"-"+caps+"-"+index;screen(label,e);state(label);
            title(e).setString("Unsaved");description(e).setString("Not persisted");command(e,new Command("unknown",Command.SCREEN,42));row(label+":unknown="+(current()==f(xform,"a","form",form).get(e)));
            command(e,cmd("c","cmdBack"));row(label+":back="+selector.isInstance(current())+":"+m(selector,"b","getCurrSelectedIdx",int.class).invoke(current()));
            command(main,cmd("j","cmdSelect"));Object next=f(vl,"a","commandListener",CommandListener.class).get(current());row(label+":fresh="+(next!=e));screen(label+"-reopen",next);rms(label);
        }
    }
    static void saveAndDisable()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(boolean happy:new boolean[]{false,true})for(boolean changed:new boolean[]{false,true}){
            record(null);resetEnv();bool(159,false);bool(171,happy);integer(111,0);Object e=open(3);f(icq,"b","connected",boolean.class).setBoolean(null,connected);
            title(e).setString("Title \u0444");description(e).setString("Line 1\nLine 2\t\u2603");choices(e).setSelectedIndex(0,true);choices(e).setSelectedIndex(1,changed?!happy:happy);
            actions().clear();command(e,cmd("s","cmdSave"));String label="save-"+connected+"-"+happy+"-"+changed;state(label);rms(label);
            Object next=open(3);screen(label+"-reload",next);actions().clear();command(next,cmd("s","cmdSave"));state(label+"-same");rms(label+"-same");
            actions().clear();open(0);state(label+"-disabled");rms(label+"-disabled");f(icq,"b","connected",boolean.class).setBoolean(null,false);
        }
    }
    static void malformed()throws Exception{
        for(String data:new String[]{"","missing tab","one\ttext\t\r","no-tab\t\r","one\ttext\t\rtail","one\ttext\t\rtwo\ttext\t\r","123456789012345678901\ttoo-long-title\t\r"})for(int index:new int[]{1,2,37}){
            record(data);actions().clear();Object e=open(index);String label="malformed-"+enc(data)+"-"+index;screen(label,e);rms(label);
        }
    }
    static void exerciseXStatus()throws Exception{
        setupFiles();menuType=load("ag","jimm.MainMenu");xform=load("bl","jimm.XStatusForm");selector=load("af","jimm.util.Selector");icons=load("e","DrawControls.Icon");splash=load("cv","jimm.SplashCanvas");
        main=f(menuType,"a","_this",menuType).get(null);if(main==null)main=instance(menuType,new Class<?>[0]);formsAndRoutes();saveAndDisable();malformed();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseXStatus();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
