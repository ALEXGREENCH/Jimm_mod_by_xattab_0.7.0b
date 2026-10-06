import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/** Chat composition/styles, scrolling, delivery marks, RMS tail, deletion and unread state. */
public class ChatProbe extends FileTransferProbe {
    static Class<?> history,chat,storage,data,line,part,icon,plain,url,notice;
    static Hashtable table()throws Exception{return (Hashtable)f(history,"a","historyTable",Hashtable.class).get(null);}
    static Object screen(Object view)throws Exception{return f(chat,"a","textList",text).get(view);}
    static void active(Object view)throws Exception{
        if(ref)m(chat,"e","activate",void.class).invoke(view);else m(chat,"e","activate",void.class,boolean.class,boolean.class).invoke(view,false,false);
    }
    static void clean()throws Exception{
        table().clear();((Vector)f(list,"a","cItems",Vector.class).get(null)).removeAllElements();actions().clear();bool(142,false);bool(137,false);bool(172,false);
        m(ui,"b","selectScreen",void.class,Object.class).invoke(null,instance(text,new Class<?>[]{String.class},"Background"));
    }
    static Object person(String uin)throws Exception{
        Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,uin,"Nick-"+uin,false,true);
        ((Vector)f(list,"a","cItems",Vector.class).get(null)).add(c);return c;
    }
    static Object view(Object c)throws Exception{
        String uin=(String)m(contact,"b","getUinString",String.class).invoke(c);m(history,"a","newChatForm",void.class,contact,String.class).invoke(null,c,"Nick-"+uin);return table().get(uin);
    }
    static void append(Object v,String body,String link,long time,boolean incoming,boolean offline,Object img,long id)throws Exception{
        m(chat,"a","addTextToForm",void.class,String.class,String.class,String.class,long.class,boolean.class,boolean.class,icon,long.class).invoke(v,incoming?"Sender":"Me",body,link,time,incoming,offline,img,id);
    }
    static void snapshot(String label,Object v)throws Exception{
        Object s=screen(v);row(label+":caption="+enc(f(vl,"a","caption",String.class).get(s))+":font="+f(vl,"u","fontSize",int.class).getInt(s)+":cursor="+f(vl,"b","currItem",int.class).getInt(s)+":top="+f(vl,"d","topItem",int.class).getInt(s)+":menu="+menu(s));
        StringBuilder result=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(s)){
            result.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':');
            for(Object p:(Vector)f(line,"a","items",Vector.class).get(l)){Object img=f(part,"a","image",icon).get(p);result.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append('/').append(img==null?"-":iconIndex(img)).append(';');}result.append(']');
        }row(label+":lines="+result);result.setLength(0);
        for(Object d:(Vector)f(chat,"a","messData",Vector.class).get(v))result.append(f(data,"a","time",long.class).getLong(d)).append('/').append(f(data,"a","rowData",int.class).getInt(d)).append(';');
        row(label+":data="+result+":total="+f(chat,"a","messTotalCounter",int.class).getInt(v)+":acks="+new TreeMap((Hashtable)f(text,"a","msgTable",Hashtable.class).get(s)));
    }
    static String iconIndex(Object img)throws Exception{
        Class<?> images=load("f","DrawControls.ImageList");Object listImages=f(list,"a","imageList",images).get(null);
        int count=(Integer)m(images,"a","size",int.class).invoke(listImages);for(int i=0;i<count;i++)if(m(images,"a","elementAt",icon,int.class).invoke(listImages,i)==img)return "contact-"+i;return "other-"+m(icon,"a","getWidth",int.class).invoke(img)+"x"+m(icon,"b","getHeight",int.class).invoke(img);
    }
    static Object image(int i)throws Exception{Class<?> images=load("f","DrawControls.ImageList");return m(images,"a","elementAt",icon,int.class).invoke(f(list,"a","imageList",images).get(null),i);}
    static void counters(String label,Object c)throws Exception{
        String s=label;for(int k:new int[]{67,68,69,70})s+=":"+k+"="+m(contact,"b","getIntValue",int.class,int.class).invoke(c,k);s+=":has-chat="+m(contact,"a","getBooleanValue",boolean.class,int.class).invoke(c,16);row(s);
    }
    static void storage(String label,String uin)throws Exception{
        row(label+":count="+m(storage,"a","getRecordCount",int.class,String.class).invoke(null,uin));RecordStore rms=RecordStore.openRecordStore("hist"+uin,true);StringBuilder records=new StringBuilder();
        for(int i=1;i<rms.getNextRecordID();i++)records.append(i).append('=').append(b64(rms.getRecord(i))).append(';');rms.closeRecordStore();row(label+":rms="+records);
    }
    static void compositions()throws Exception{
        for(int style:new int[]{0,2})for(boolean small:new boolean[]{false,true})for(boolean incoming:new boolean[]{false,true})for(boolean offline:new boolean[]{false,true})for(boolean xstatus:new boolean[]{false,true}){
            clean();f(options,"g","fontStyle",int.class).setInt(null,style);bool(135,small);bool(165,true);Object c=person("12345"),v=view(c);String label="text-"+style+"-"+small+"-"+incoming+"-"+offline+"-"+xstatus;
            append(v,"Hello :)\nnext line \u0444", "http://example.test/",xstatus?0:1273665600L,incoming,offline,image(14),101);snapshot(label,v);
            for(int j=0;j<7;j++)append(v,"line-"+j+" abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef", "",1273665600L+j,incoming,offline,null,0);
            f(vl,"b","currItem",int.class).setInt(screen(v),0);append(v,"while reading above", "",1273665660L,incoming,false,null,0);snapshot(label+"-scroll",v);
            for(boolean user:new boolean[]{false,true}){bool(170,true);m(history,"a","AckMessage",void.class,String.class,long.class,boolean.class).invoke(null,"12345",101L,user);snapshot(label+"-ack-"+user,v);}
        }
    }
    static void incomingMessages()throws Exception{
        int id=20000;for(boolean visible:new boolean[]{false,true})for(boolean offline:new boolean[]{false,true})for(boolean images:new boolean[]{false,true})for(int kind:new int[]{0,1,2,3,4,5,6}){
            clean();String uin=""+(id++);Object c=person(uin),v=view(c);bool(164,images);bool(137,true);if(visible)active(v);Object message;
            if(kind==0)message=instance(plain,new Class<?>[]{String.class,String.class,long.class,String.class,boolean.class},uin,"54321",1273665600L,"Incoming \u0444",offline);
            else if(kind==1)message=instance(url,new Class<?>[]{String.class,String.class,long.class,String.class,String.class},uin,"54321",1273665600L,"http://example.test/","URL text");
            else message=instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},kind==2?1:kind==3?3:kind==6?6:2,uin,kind==4,kind==5?null:"reason");
            Class<?> base=load("ac","jimm.comm.Message");f(base,"a","newDate",long.class).setLong(message,1273665600L);f(base,"b","offline",boolean.class).setBoolean(message,offline);
            m(history,"a","addMessage",void.class,contact,base).invoke(null,c,message);String label="incoming-"+visible+"-"+offline+"-"+images+"-"+kind;snapshot(label,v);counters(label,c);storage(label,uin);
            String eventNames="";for(Object item:actions())eventNames+=((Object[])item)[0]+";";row(label+":events="+eventNames);
        }
    }
    static void tails()throws Exception{
        int id=30000;for(int style:new int[]{0,2})for(int count:new int[]{0,1,5,8})for(boolean enabled:new boolean[]{false,true}){
            clean();String uin=""+(id++);Object c=person(uin);f(options,"g","fontStyle",int.class).setInt(null,style);
            for(int i=0;i<count;i++)m(storage,"a","addText",void.class,String.class,String.class,byte.class,String.class,long.class).invoke(null,uin,"Stored-"+i+" :)\nline",(byte)(i%2),"Nick-"+i,1273665600L+i);
            bool(142,enabled);Object v=view(c);String label="tail-"+style+"-"+count+"-"+enabled;snapshot(label,v);storage(label,uin);m(history,"a","fillFormHistory",void.class,contact).invoke(null,c);snapshot(label+"-repeat",v);
        }
    }
    static void deletion()throws Exception{
        for(int selected:new int[]{0,1,2,3})for(int command:new int[]{0,1,2}){
            clean();Object a=person("12345"),b=person("12346"),c=person("12347"),v=view(a);view(b);view(c);append(v,"keep me", "",1273665600L,true,false,null,0);active(v);
            m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(a,67,4);command(v,f(chat,"i","cmdDelChat",Command.class).get(null));f(ui,"c","lastSelectedItemIndex",int.class).setInt(null,selected);
            Object cmd=command==0?cmd("a","cmdOk"):command==1?cmd("c","cmdBack"):new Command("unknown",Command.SCREEN,99);command(v,cmd);
            String label="delete-"+selected+"-"+command;row(label+":chats="+new TreeSet(table().keySet())+":active="+(current()==screen(v)));counters(label,a);counters(label,b);counters(label,c);
        }
        clean();Object c=person("12345"),v=view(c);for(int type:new int[]{0,1,2,3}){active(v);key(v,screen(v),-8,type);row("delete-key-"+type+":tag="+m(ui,"a","getCurScreenTag",int.class).invoke(null));}
    }
    static void emotions()throws Exception{
        Class<?> emotions=load("bo","jimm.Emotions"),selector=load("af","jimm.util.Selector");
        for(String[] names:new String[][]{{"a","selEmotionsWord"},{"b","selEmotionsSmileNames"},{"c","textCorrWords"}})row("smile-table-"+names[1]+":"+enc(Arrays.toString((String[])f(emotions,names[0],names[1],String[].class).get(null))));
        for(String[] names:new String[][]{{"a","selEmotionsIndexes"},{"b","textCorrIndexes"}})row("smile-table-"+names[1]+":"+Arrays.toString((int[])f(emotions,names[0],names[1],int[].class).get(null)));
        Method read=m(emotions,"a","readLineFromStream",String.class,DataInputStream.class);
        for(String value:new String[]{"","\n","one","one\r\nnext\n","1\tName\t:)\n","\u0423\u043b\u044b\u0431\u043a\u0430, \u010d :D\n","x\r\ry\n\nend"}){
            DataInputStream in=new DataInputStream(new ByteArrayInputStream(value.getBytes("UTF-8")));for(int i=0;i<5;i++)row("smile-line-"+enc(value)+"-"+i+":"+enc(read.invoke(null,in)));
        }
        for(byte[] bytes:new byte[][]{{-1},{65,-1,66,10},{-61},{-48,-93,9,10},{0,9,10}}){DataInputStream in=new DataInputStream(new ByteArrayInputStream(bytes));for(int i=0;i<3;i++)row("smile-bytes-"+b64(bytes)+"-"+i+":"+enc(read.invoke(null,in)));}
        String[] words=(String[])f(emotions,"a","selEmotionsWord",String[].class).get(null);int i=0;
        for(String word:words)for(boolean enabled:new boolean[]{false,true}){
            clean();bool(141,enabled);Object v=view(person("12345"));String message="["+word.toLowerCase()+"] ["+word.toUpperCase()+"]";append(v,message,"",1273665600L,true,false,null,0);snapshot("smile-word-"+i+"-"+enabled,v);i++;
        }
        Object controller=f(emotions,"a","_this",emotions).get(null);bool(141,true);
        for(boolean remember:new boolean[]{false,true})for(boolean back:new boolean[]{false,true})for(int index:new int[]{0,3,12}){
            clean();bool(163,remember);Object previous=instance(text,new Class<?>[]{String.class},"Editor");TextBox box=new TextBox("Input","ABCD",100,TextField.ANY);
            f(emotions,"a","lastSelectedEmotion",int.class).setInt(null,index);m(emotions,"a","selectEmotion",void.class,TextBox.class,Object.class).invoke(null,box,previous);
            Object selected=f(emotions,"a","selector",selector).get(null);String label="smile-select-"+remember+"-"+back+"-"+index;row(label+":index="+m(selector,"b","getCurrSelectedIdx",int.class).invoke(selected)+":menu="+menu(selected));
            f(emotions,"b","caretPos",int.class).setInt(null,1);actions().clear();command(controller,back?cmd("c","cmdBack"):cmd("j","cmdSelect"));
            row(label+":text="+enc(box.getString())+":return="+(current()==previous)+":cleared="+(f(emotions,"a","selector",selector).get(null)==null)+":remembered="+f(emotions,"a","lastSelectedEmotion",int.class).getInt(null));
            for(Object event:actions())row(label+":event="+((Object[])event)[0]+":"+Arrays.toString((Object[])((Object[])event)[1]));
        }
    }
    static void exerciseChat()throws Exception{
        setupFiles();history=load("bt","jimm.ChatHistory");chat=load("y","jimm.ChatTextList");storage=load("q","jimm.HistoryStorage");data=load("ca","jimm.MessData");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");plain=load("av","jimm.comm.PlainMessage");url=load("bk","jimm.comm.UrlMessage");notice=load("s","jimm.comm.SystemNotice");
        f(icq,"a","myNick",String.class).set(null,"Me");integer(103,0x123456);integer(108,0x102030);integer(113,0x405060);compositions();incomingMessages();tails();deletion();emotions();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseChat();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
