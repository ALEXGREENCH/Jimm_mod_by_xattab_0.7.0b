import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/** Real template editing, default selection, promotion, native editor and memory-RMS bytes. */
public class TemplatesProbe extends TextListProbe {
    static Class<?> templates;
    static Object control,previous;
    static TextBox draft;
    static String[] commandOld={"a","b","c","d","e","f","g","h","i"};
    static String[] commandNames={"selectTemplateCommand","backCommand","newTemplateCommand","editTemplateCommand","deleteCurrentTemplateCommand","clearCommand","addCommand","editCommand","cancelCommand"};
    static Object value(String old,String name,Class<?> type)throws Exception{return f(templates,old,name,type).get(null);}
    static Command templateCommand(int index)throws Exception{return (Command)value(commandOld[index],commandNames[index],Command.class);}
    static Object screen()throws Exception{return value("a","templateList",text);}
    static Vector templateValues()throws Exception{return (Vector)value("a","templates",Vector.class);}
    static void cleanStore(){try{RecordStore.deleteRecordStore("tmpl");}catch(Exception ignored){}}
    static void seed(String[] values)throws Exception{cleanStore();if(values.length==0)return;RecordStore store=RecordStore.openRecordStore("tmpl",true);try{for(String v:values){byte[] b=v.getBytes("UTF-8");store.addRecord(b,0,b.length);}}finally{store.closeRecordStore();}}
    static void rms(String label)throws Exception {
        RecordStore store=null;StringBuilder b=new StringBuilder(label+":rms=");
        try{store=RecordStore.openRecordStore("tmpl",false);b.append(store.getNumRecords()).append(':').append(store.getNextRecordID());for(int i=1;i<store.getNextRecordID();i++){b.append('[').append(i).append(':');try{b.append(b64(store.getRecord(i)));}catch(InvalidRecordIDException missing){b.append("hole");}b.append(']');}}
        catch(RecordStoreNotFoundException missing){b.append("absent");}finally{if(store!=null)store.closeRecordStore();}row(b.toString());
    }
    static void fresh(String[] data,int style,boolean swap,boolean nativePrevious)throws Exception {
        seed(data);integer(112,style);integer(118,1);bool(143,swap);f(options,"g","fontStyle",int.class).setInt(null,style);m(vl,"d","assignSoftKeys",void.class).invoke(null);alpha(171,171,128);background(true);
        control=instance(templates,new Class<?>[0]);draft=new TextBox("Draft","before AFTER",2000,TextField.ANY);previous=nativePrevious?draft:make("Previous",176,220,3);boundary.getField("screen").set(null,null);
        m(templates,"a","selectTemplate",void.class,TextBox.class,Object.class).invoke(null,draft,previous);force();
    }
    static void force()throws Exception{Object list=screen();if(list!=null){f(vl,"H","forcedWidth",int.class).setInt(list,176);f(vl,"I","forcedHeight",int.class).setInt(list,220);}}
    static void snapshot(String label)throws Exception {
        StringBuilder b=new StringBuilder(label+":values=");for(Object v:templateValues())b.append('[').append(enc(v)).append(']');Object list=screen();TextBox edit=(TextBox)value("a","templateTextbox",TextBox.class);
        b.append(":list=").append(list!=null).append(":draft=").append(enc(draft.getString())).append(":caret=").append(f(templates,"a","caretPos",int.class).getInt(null)).append(":active=").append(list!=null&&current()==list).append(":returned=").append(current()==previous||boundary.getField("screen").get(null)==previous).append(":editor=");
        if(edit==null)b.append("null");else b.append(enc(edit.getTitle())).append(':').append(enc(edit.getString())).append(':').append(edit.getMaxSize()).append(':').append(edit.getConstraints()).append(':').append(boundary.getField("screen").get(null)==edit);row(b.toString());
        if(list!=null){s=list;state(label+"-lines");String commands="";for(Object c:(Vector)f(vl,"a","leftMenuItems",Vector.class).get(list))commands+=commandName((Command)c)+";";row(label+":commands="+commands+":default="+commandName((Command)f(vl,"c","defaultCommand",Command.class).get(list))+":left="+commandName((Command)f(vl,"a","leftMenu",Command.class).get(list))+":right="+commandName((Command)f(vl,"b","rightMenu",Command.class).get(list)));}rms(label);
    }
    static void action(String label,int index)throws Exception {row(label+":result="+invoke(m(templates,"commandAction","commandAction",void.class,Command.class,Displayable.class),control,templateCommand(index),null));force();snapshot(label);}
    static void layouts()throws Exception {
        for(int style:new int[]{0,2})for(boolean swap:new boolean[]{false,true})for(boolean nativePrevious:new boolean[]{false,true})for(String[] data:new String[][]{{},{"one"},{"one","\u0442\u0435\u043a\u0441\u0442\nsecond","long word word word word word word word word word word word word word word"}}) {
            fresh(data,style,swap,nativePrevious);String label="layout-"+style+"-"+swap+"-"+nativePrevious+"-"+data.length;snapshot(label);render(label,screen());action(label+"-back",1);
        }
    }
    static void edit()throws Exception {
        for(boolean adding:new boolean[]{false,true})for(int count:new int[]{1,3})for(int selected=0;selected<count;selected++)for(String input:new String[]{"","replacement", "\u041f\u0440\u0438\u0432\u0435\u0442\n\ud83d\ude00"})for(boolean cancel:new boolean[]{false,true}) {
            fresh(count==1?new String[]{"first"}:new String[]{"first","second","third"},2,false,false);m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),selected);String label="edit-"+adding+"-"+count+"-"+selected+"-"+enc(input)+"-"+cancel;
            action(label+"-open",adding?2:3);TextBox box=(TextBox)value("a","templateTextbox",TextBox.class);box.setString(input);action(label+"-finish",cancel?8:adding?6:7);render(label,screen());
        }
        for(int count:new int[]{1,3})for(int selected=0;selected<count;selected++){fresh(count==1?new String[]{"first"}:new String[]{"first","second","third"},0,true,false);m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),selected);action("delete-"+count+"-"+selected,4);render("delete-"+count+"-"+selected,screen());}
        fresh(new String[0],0,false,false);action("empty-add-open",2);((TextBox)value("a","templateTextbox",TextBox.class)).setString("");action("empty-add-save",6);action("empty-add-select",0);
    }
    static void choose()throws Exception {
        for(int count:new int[]{0,1,3})for(int selected=0;selected<Math.max(count,1);selected++)for(int caret:new int[]{-1,0,4,12,100})for(boolean click:new boolean[]{false,true})for(boolean nativePrevious:new boolean[]{false,true}) {
            fresh(count==0?new String[0]:count==1?new String[]{"template"}:new String[]{"first","second","\u041f\u0440\u0438\u0432\u0435\u0442"},2,false,nativePrevious);m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),selected);f(templates,"a","caretPos",int.class).setInt(null,caret);String label="choose-"+count+"-"+selected+"-"+caret+"-"+click+"-"+nativePrevious;
            if(click){row(label+":result="+invoke(m(templates,"b","vlItemClicked",void.class,vl),control,screen()));force();snapshot(label);}else action(label,0);
        }
    }
    static void clears()throws Exception {
        for(int count:new int[]{0,1,3})for(boolean yes:new boolean[]{false,true}){fresh(count==0?new String[0]:count==1?new String[]{"first"}:new String[]{"first","second","third"},2,false,false);String label="clear-"+count+"-"+yes;action(label+"-open",5);Object popup=current();row(label+":popup="+load("ci","DrawControls.VirtualAlert").isInstance(popup)+":tag="+f(ui,"a","curScreenTag",int.class).getInt(null));command(popup,cmd(yes?"d":"c",yes?"cmdYes":"cmdBack"));force();snapshot(label+"-finish");}
    }
    static void keyboardAndChangedDraft()throws Exception {
        for(int selected=0;selected<3;selected++){fresh(new String[]{"first","second","third"},2,false,false);m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),selected);row("keyboard-"+selected+":result="+invoke(m(vl,"c","keyReaction",void.class,int.class,int.class),screen(),-5,1));force();snapshot("keyboard-"+selected);}
        for(int capacity:new int[]{5,100})for(boolean nativePrevious:new boolean[]{false,true}){
            fresh(new String[]{"first","second","third"},2,false,nativePrevious);m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),1);draft.setString("x");draft.setMaxSize(capacity);action("changed-draft-"+capacity+"-"+nativePrevious,0);
        }
    }
    static void persistence()throws Exception {
        for(int hole:new int[]{-1,1,2,3}){seed(new String[]{"first","\u041f\u0440\u0438\u0432\u0435\u0442","third"});if(hole!=-1){RecordStore store=RecordStore.openRecordStore("tmpl",false);store.deleteRecord(hole);store.closeRecordStore();}control=instance(templates,new Class<?>[0]);StringBuilder b=new StringBuilder("load-hole-"+hole+":");for(Object value:templateValues())b.append(enc(value)).append(';');row(b.toString());rms("load-hole-"+hole);}
        fresh(new String[]{"original"},0,false,false);action("held-open-new",2);((TextBox)value("a","templateTextbox",TextBox.class)).setString("added");RecordStore held=RecordStore.openRecordStore("tmpl",false);
        try{row("held-open-save:result="+invoke(m(templates,"commandAction","commandAction",void.class,Command.class,Displayable.class),control,templateCommand(6),null));}finally{try{held.closeRecordStore();row("held-close:closed");}catch(RecordStoreNotOpenException alreadyClosed){row("held-close:already-closed");}}force();snapshot("held-open-save");
        cleanStore();control=instance(templates,new Class<?>[0]);row("missing-store-load:"+templateValues().size());rms("missing-store-load");
    }
    static void exerciseTemplates()throws Exception {
        setupFiles();templates=load("aq","jimm.Templates");boundary=load("MenuIO","MenuIO");shim=load("RecoveryList","RecoveryList");icon=load("e","DrawControls.Icon");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");
        for(int i=0;i<9;i++)row("command-"+i+":"+commandName(templateCommand(i)));layouts();edit();choose();clears();keyboardAndChangedDraft();persistence();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTemplates();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
