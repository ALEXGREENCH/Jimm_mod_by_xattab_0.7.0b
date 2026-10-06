import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/** History records/search/navigation and exact UTF-8/CP1251 export bytes against May. */
public class HistoryProbe extends FileTransferProbe {
    static Class<?> storage,view,record,popup,line,part,icon;
    static Object screen;
    static final String UIN="12345";
    static final String[] texts={"Alpha first", "  Beta \u0444\r\nsecond :)  ","alpha third http://example.test/a", "ALPHA last http://one.test/a http://two.test/b"};
    static Object own(String old,String name)throws Exception{return f(view,old,name,Command.class).get(null);}
    static void activate(Object s)throws Exception{m(ui,"b","selectScreen",void.class,Object.class).invoke(null,s);}
    static void seed(String uin,String[] values)throws Exception{
        m(storage,"a","clearHistory",void.class,String.class).invoke(null,uin);
        for(int i=0;i<values.length;i++)m(storage,"a","addText",void.class,String.class,String.class,byte.class,String.class,long.class).invoke(null,uin,values[i],(byte)(i%2),"Sender-"+i,1273665600L+i*60);
    }
    static void open()throws Exception{
        m(storage,"a","clearCache",void.class).invoke(null);m(storage,"a","showHistoryList",void.class,String.class,String.class).invoke(null,UIN,"Known");screen=f(storage,"a","list",view).get(null);
    }
    static void index(int i)throws Exception{f(vl,"b","currItem",int.class).setInt(screen,i);}
    static String state()throws Exception{
        Object s=current();String result="index="+f(vl,"b","currItem",int.class).getInt(screen)+":active="+(s==screen?"list":popup.isInstance(s)?"popup":form.isInstance(s)?"form":text.isInstance(s)?"text":"other");
        if(popup.isInstance(s))result+=":"+enc(f(popup,"a","text",String.class).get(s));return result;
    }
    static void layout(String label,Object s)throws Exception{
        row(label+":caption="+enc(f(vl,"a","caption",String.class).get(s))+":font="+f(vl,"u","fontSize",int.class).getInt(s)+":menu="+menu(s));
        if(!text.isInstance(s))return;StringBuilder result=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(s)){
            result.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':');for(Object p:(Vector)f(line,"a","items",Vector.class).get(l)){
                Object img=f(part,"a","image",icon).get(p);result.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append('/').append(img==null?"-":m(icon,"a","getWidth",int.class).invoke(img)+"x"+m(icon,"b","getHeight",int.class).invoke(img)).append(';');
            }result.append(']');
        }row(label+":lines="+result);
    }
    static void records()throws Exception{
        seed(UIN,texts);open();layout("history-list",screen);
        for(int i=-1;i<=texts.length;i++)for(boolean cached:new boolean[]{false,true}){
            Object r=m(storage,cached?"b":"a",cached?"getCachedRecord":"getRecord",record,String.class,int.class).invoke(null,UIN,i);String s="record-"+i+"-"+cached+":";
            if(r==null)s+="null";else{
                for(String[] n:new String[][]{{"d","from"},{"b","text"},{"c","date"},{"a","shortText"}})s+=enc(f(record,n[0],n[1],String.class).get(r))+":";
                s+=f(record,"a","type",byte.class).getByte(r)+":"+f(record,"a","contains_url",boolean.class).getBoolean(r);
            }row(s);
        }
        for(int style:new int[]{0,2})for(int i=0;i<texts.length;i++){
            f(options,"g","fontStyle",int.class).setInt(null,style);index(i);command(screen,cmd("j","cmdSelect"));Object detail=current();layout("detail-"+style+"-"+i,detail);
            command(screen,own("c","cmdMsgNext"));row("next-"+style+"-"+i+":"+state());command(screen,own("d","cmdMsgPrev"));row("prev-"+style+"-"+i+":"+state());
            command(screen,cmd("c","cmdBack"));row("detail-back:"+state());
        }
        for(int i:new int[]{2,3}){
            activate(screen);index(i);actions().clear();command(screen,own("j","cmdGotoURL"));row("url-"+i+":"+state());
            if(current()!=screen){layout("url-list",current());command(screen,cmd("j","cmdSelect"));command(screen,cmd("c","cmdBack"));row("url-back:"+state());}
            for(Object event:actions())row("url-event:"+((Object[])event)[0]+":"+enc(Arrays.toString((Object[])((Object[])event)[1])));
        }
        activate(screen);command(screen,own("g","cmdInfo"));row("info:"+state());
    }
    static void searches()throws Exception{
        for(int font:new int[]{0,1,2}){
            integer(111,font);open();command(screen,cmd("e","cmdFind"));Object find=f(view,"a","frmFind",form).get(screen);layout("find-form-"+font,find);row("find-tree:"+structure(f(tree,"a","root",node).get(find)));
            TextField input=(TextField)f(view,"a","tfldFind",TextField.class).get(screen);ChoiceGroup group=(ChoiceGroup)f(view,"a","chsFind",choice).get(screen);
            row("find-default:"+enc(input.getString())+":"+input.getMaxSize()+":"+input.getConstraints()+":"+enc(group.getLabel())+":"+group.isSelected(0)+":"+group.isSelected(1));
            for(String query:new String[]{"alpha","Alpha","missing","","\u0444"})for(boolean sensitive:new boolean[]{false,true})for(boolean back:new boolean[]{false,true})for(int start:new int[]{0,2,3}){
                activate(find);index(start);input.setString(query);group.setSelectedIndex(0,back);group.setSelectedIndex(1,sensitive);command(screen,cmd("e","cmdFind"));
                row("find-"+font+"-"+enc(query)+"-"+sensitive+"-"+back+"-"+start+":"+state());
            }
            activate(find);command(screen,cmd("a","cmdOk"));row("find-ok:"+state());command(screen,new Command("unknown",Command.SCREEN,99));row("find-unknown:"+state());command(screen,cmd("c","cmdBack"));row("find-back:"+state());
            command(screen,cmd("e","cmdFind"));row("find-reused:"+(current()==find)+":"+enc(input.getString()));
            activate(screen);actions().clear();command(screen,own("e","cmdBack"));row("history-back:"+(f(storage,"a","list",view).get(null)==null)+":"+(f(view,"a","frmFind",form).get(screen)==null));events("history-back");
        }
    }
    static void exports()throws Exception{
        Vector contacts=(Vector)f(list,"a","cItems",Vector.class).get(null);contacts.removeAllElements();contacts.add(contactItem);
        Object second=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},18,4,"12346","",false,true);contacts.add(second);seed("12346",new String[]{"other \u010d"});
        for(boolean cp:new boolean[]{false,true})for(boolean all:new boolean[]{false,true})for(int failure:new int[]{0,1,5,6,7,8,9,10}){
            open();resetEnv();bool(133,cp);env.getMethod("file",String.class,byte[].class).invoke(null,"/card/jimm_hist_12345.txt",new byte[]{42});env.getField("fileFailure").setInt(null,failure);
            command(screen,own(all?"i":"h",all?"cmdExportAll":"cmdExport"));m(view,"b","onDirectorySelect",void.class,String.class).invoke(screen,"/card/");m(view,"run","run",void.class).invoke(screen);
            String label="export-"+cp+"-"+all+"-"+failure;row(label+":starts="+env.getField("starts").getInt(null)+":log="+env.getField("fileLog").get(null));
            row(label+":progress="+env.getField("progress").get(null)+":messages="+enc(env.getField("messages").get(null))+":closed="+env.getField("fileCloses").getInt(null)+":"+env.getField("outputCloses").getInt(null)+":flush="+env.getField("flushes").getInt(null));
            TreeMap files=new TreeMap((Hashtable)env.getField("files").get(null));for(Object key:files.keySet())row(label+":file="+key+":"+b64((byte[])files.get(key)));events(label);
        }
        seed(UIN,new String[0]);open();resetEnv();f(view,"b","exportUin",String.class).set(screen,UIN);f(view,"c","directory",String.class).set(screen,"/card/");m(view,"run","run",void.class).invoke(screen);row("export-empty:"+env.getField("fileLog").get(null)+":"+env.getField("progress").get(null));
    }
    static void deletion()throws Exception{
        for(int selection:new int[]{0,1,2,3})for(int action:new int[]{0,1,2}){
            seed(UIN,texts);seed("12346",new String[]{"other"});RecordStore.openRecordStore("other-history",true).closeRecordStore();RecordStore.openRecordStore("unrelated",true).closeRecordStore();open();command(screen,own("f","cmdClear"));f(ui,"c","lastSelectedItemIndex",int.class).setInt(null,selection);
            command(screen,action==0?cmd("a","cmdOk"):action==1?cmd("c","cmdBack"):new Command("unknown",Command.SCREEN,99));String[] stores=RecordStore.listRecordStores();Arrays.sort(stores);row("clear-"+selection+"-"+action+":"+Arrays.toString(stores)+":"+state());
        }
    }
    static void boundaries()throws Exception{
        seed(UIN,texts);open();
        for(int start:new int[]{-1,4,5})for(boolean back:new boolean[]{false,true}){
            index(start);row("search-outside-"+start+"-"+back+":"+m(storage,"a","find_intern",boolean.class,String.class,String.class,boolean.class,boolean.class).invoke(null,UIN,"Alpha",true,back));
        }
        index(1);m(storage,"a","addText",void.class,String.class,String.class,byte.class,String.class,long.class).invoke(null,UIN,"while reading",(byte)0,"Sender",1273665900L);row("append-reading:"+state());
        index(4);m(storage,"a","addText",void.class,String.class,String.class,byte.class,String.class,long.class).invoke(null,UIN,"at end",(byte)1,"Me",1273665900L);row("append-at-end:"+state());
        // The original cache is keyed only by record number; switching UIN alone leaves it populated.
        Object first=m(storage,"b","getCachedRecord",record,String.class,int.class).invoke(null,UIN,0);
        seed("12346",new String[]{"different"});m(storage,"a","getRecordCount",int.class,String.class).invoke(null,"12346");
        Object stale=m(storage,"b","getCachedRecord",record,String.class,int.class).invoke(null,"12346",0);row("cache-after-clear-other:"+(first==stale)+":"+enc(f(record,"b","text",String.class).get(stale)));
        m(storage,"a","getRecordCount",int.class,String.class).invoke(null,UIN);Object switched=m(storage,"b","getCachedRecord",record,String.class,int.class).invoke(null,UIN,0);row("cache-switch:"+(stale==switched)+":"+enc(f(record,"b","text",String.class).get(switched)));
        m(storage,"a","clearCache",void.class).invoke(null);m(storage,"a","find",void.class,String.class,String.class,boolean.class,boolean.class).invoke(null,UIN,"missing",false,false);row("find-without-list:"+(f(storage,"a","list",view).get(null)==null));
        seed("99999",new String[0]);RecordStore rms=RecordStore.openRecordStore("hist99999",true);
        for(byte[] bytes:new byte[][]{{},{0},{1,0,2,65},{0,0,0,0,0}})rms.addRecord(bytes,0,bytes.length);rms.closeRecordStore();
        for(int i=0;i<4;i++)row("malformed-"+i+":"+(m(storage,"a","getRecord",record,String.class,int.class).invoke(null,"99999",i)==null));
    }
    static void exerciseHistory()throws Exception{
        setupFiles();storage=load("q","jimm.HistoryStorage");view=load("bg","jimm.HistoryStorageList");record=load("bz","jimm.CachedRecord");popup=load("ci","DrawControls.VirtualAlert");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");
        f(icq,"a","myNick",String.class).set(null,"Me");integer(103,0x123456);integer(108,0x102030);integer(113,0x405060);records();searches();exports();deletion();boundaries();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseHistory();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}

