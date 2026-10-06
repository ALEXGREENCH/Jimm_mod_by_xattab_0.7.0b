import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** May clipboard sequences, MagicEye formatting and default-command routing. */
public class ClipboardProbe extends LoginProbe {
    static Class<?> ui,vl,text,magic;
    static Object controller,eye,eyeList;
    static String enc(Object s)throws Exception{return s==null?"null":b64(s.toString().getBytes("UTF-8"));}
    static Object cmd(String old,String name)throws Exception{return f(ui,old,name,Command.class).get(null);}
    static void clear()throws Exception{m(ui,"c","clearClipBoardText",void.class).invoke(null);}
    static String get(boolean quote)throws Exception{return (String)m(ui,"a","getClipBoardText",String.class,boolean.class).invoke(null,quote);}
    static void put(boolean incoming,String date,String from,String value,String prefix)throws Exception{
        m(ui,"a","setClipBoardText",void.class,boolean.class,String.class,String.class,String.class,String.class).invoke(null,incoming,date,from,value,prefix);
    }
    static void snap(String label)throws Exception{row(label+":"+m(ui,"a","clipBoardIsEmpty",boolean.class).invoke(null)+":"+enc(get(false))+":"+enc(get(true)));}
    static void command(Object target,Object c)throws Exception{m(target.getClass(),"commandAction","commandAction",void.class,Command.class,Displayable.class).invoke(target,c,null);}
    static void key(Object target,Object sender,int code,int type)throws Exception{m(target.getClass(),"a","vlKeyPress",void.class,vl,int.class,int.class).invoke(target,sender,code,type);}
    static boolean hasAppend(Object screen)throws Exception{return ((Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen)).contains(cmd("g","cmdCopyAppend"));}
    static void text(Object screen,String value,int index)throws Exception{
        m(text,"a","addBigText",text,String.class,int.class,int.class,int.class).invoke(screen,value,0x123456,0,index);
        m(text,"a","doCRLF",text,int.class).invoke(screen,index);
    }
    static void exerciseClipboard()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");magic=load("ah","jimm.util.MagicEye");
        controller=f(ui,"a","_this",ui).get(null);eye=f(magic,"a","instance",magic).get(null);eyeList=f(magic,"a","list",text).get(null);
        snap("initial");put(true,"***error***",null,"initial",get(true));snap("initial-append");
        String[] values={null,"","one","two\nlines","\n\n","\r\n","\u041f\u0440\u0438\u0432\u0435\u0442 \u010d"};
        int id=0;
        for(String date:new String[]{null,"12.05.2010","***error***",new String("***error***")})for(String value:values)for(boolean incoming:new boolean[]{false,true}){
            clear();snap("clear-"+id);put(incoming,date,id%2==0?null:"Nick",value,get(true));snap("set-"+id);
            put(!incoming,"***error***","Next","tail\n",get(true));snap("append-"+id++);
        }
        for(boolean seeded:new boolean[]{false,true}){
            clear();if(seeded)put(true,"***error***",null,"seed",get(true));
            Object screen=m(ui,"a","getInfoTextList",text,String.class,boolean.class).invoke(null,"Info",true);
            text(screen,"Alpha",0);text(screen,"Beta\nGamma",1);
            m(ui,"b","selectScreen",void.class,Object.class).invoke(null,screen);
            row("info-menu-"+seeded+":"+hasAppend(screen));
            for(String[] c:new String[][]{{"f","cmdCopyText"},{"g","cmdCopyAppend"},{"h","cmdCopyAll"},{"g","cmdCopyAppend"}}){
                command(controller,cmd(c[0],c[1]));snap("info-"+seeded+"-"+c[1]);row("info-menu:"+hasAppend(screen));
            }
            for(int type:new int[]{0,1,2,3}){put(false,"d","n","seed",get(true));key(controller,screen,42,type);snap("info-star-"+type);}
            key(controller,eyeList,42,1);snap("info-other-sender");
        }
        // Default commands take precedence over bars and items, and are not menu items.
        Object screen=instance(text,new Class<?>[]{String.class},"Commands");
        Method add=m(vl,"a","addCommandEx",void.class,Command.class,int.class),find=m(vl,"a","findMenuByType",Command.class,int.class);
        Command first=new Command("first",Command.OK,1),second=new Command("second",Command.OK,2),def=new Command("default",Command.OK,3);
        add.invoke(screen,first,1);add.invoke(screen,second,3);add.invoke(screen,def,5);
        row("default:"+(find.invoke(screen,Command.OK)==def)+":"+((Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen)).size());
        m(vl,"a","removeCommandEx",void.class,Command.class).invoke(screen,def);row("default-remove:"+(find.invoke(screen,Command.OK)==first));
        add.invoke(screen,def,5);m(vl,"m","removeAllCommands",void.class).invoke(screen);row("default-clear:"+(find.invoke(screen,Command.OK)==null));
        chats(); history(); magic();
    }
    static void chats()throws Exception{
        Class<?> chat=load("y","jimm.ChatTextList"),history=load("bt","jimm.ChatHistory"),data=load("ca","jimm.MessData");
        Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);
        f(ui,"a","curScreenTag",int.class).setInt(null,-1);f(icq,"a","myNick",String.class).set(null,"Me");
        for(boolean incoming:new boolean[]{false,true})for(int offset:new int[]{0,3}){
            Object view=instance(chat,new Class<?>[]{String.class,contact},"Known",c),screen=f(chat,"a","textList",text).get(view);
            text(screen,"Header body\nsecond",0);
            ((Vector)f(chat,"a","messData",Vector.class).get(view)).add(instance(data,new Class<?>[]{boolean.class,long.class,int.class,boolean.class},incoming,1273665600L,offset,false));
            ((Hashtable)f(history,"a","historyTable",Hashtable.class).get(null)).put("12345",view);
            clear();m(chat,"a","buildMenu",void.class,contact).invoke(view,c);row("chat-menu:"+hasAppend(screen));
            String label="chat-"+incoming+"-"+offset;
            command(view,cmd("f","cmdCopyText"));snap(label+":copy");row(label+":menu="+hasAppend(screen));
            command(view,cmd("g","cmdCopyAppend"));snap(label+":append");
            for(int type:new int[]{0,1,2,3}){key(view,screen,42,type);snap(label+":star-"+type);}
        }
    }
    static void history()throws Exception{
        Class<?> history=load("q","jimm.HistoryStorage"),view=load("bg","jimm.HistoryStorageList"),record=load("bz","jimm.CachedRecord");
        for(boolean seeded:new boolean[]{false,true})for(boolean detail:new boolean[]{false,true})for(byte type:new byte[]{0,1}){
            clear();if(seeded)put(true,"***error***",null,"seed",get(true));
            Object screen=instance(view,new Class<?>[0]),message=detail?instance(text,new Class<?>[]{String.class},"Message"):null;
            f(history,"a","list",view).set(null,screen);f(view,"a","messText",text).set(null,message);
            Object r=instance(record,new Class<?>[0]);f(record,"a","type",byte.class).setByte(r,type);
            f(record,"b","text",String.class).set(r,"Saved\ntext");f(record,"c","date",String.class).set(r,"12.05.2010");f(record,"d","from",String.class).set(r,"Nick");
            Hashtable cache=new Hashtable();cache.put(0,r);f(history,"a","cachedRecords",Hashtable.class).set(null,cache);
            f(vl,"b","currItem",int.class).setInt(screen,0);
            String label="history-"+seeded+"-"+detail+"-"+type;row(label+":menu="+hasAppend(screen));
            command(screen,cmd("f","cmdCopyText"));snap(label+":copy");
            row(label+":menus="+hasAppend(screen)+":"+(message!=null&&hasAppend(message)));
            command(screen,cmd("g","cmdCopyAppend"));snap(label+":append");
            for(int event:new int[]{0,1,2,3}){key(screen,message==null?screen:message,42,event);snap(label+":star-"+event);}
            f(vl,"b","currItem",int.class).setInt(screen,-1);command(screen,cmd("f","cmdCopyText"));snap(label+":unselected");
        }
    }
    static void magic()throws Exception{
        f(list,"a","cItems",Vector.class).set(null,new Vector());
        Object known=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);
        ((Vector)f(list,"a","cItems",Vector.class).get(null)).add(known);
        Class<?> line=load("bm","DrawControls.TextLine"),part=load("bc","DrawControls.TextItem");
        for(int style:new int[]{0,2})for(boolean enabled:new boolean[]{false,true})for(boolean highlight:new boolean[]{false,true})for(String uin:new String[]{"12345","67890"}){
            command(eye,cmd("r","cmdClearText"));clear();bool(172,enabled);integer(103,0x123456);integer(113,0x654321);f(options,"g","fontStyle",int.class).setInt(null,style);
            m(magic,"a","addAction",void.class,String.class,String.class,boolean.class).invoke(null,uin,"event",highlight);
            m(magic,"a","addAction",void.class,String.class,String.class,String.class).invoke(null,uin,"details","detail\ntext");
            m(magic,"a","activate",void.class).invoke(null);
            String label="magic-"+style+"-"+enabled+"-"+highlight+"-"+uin;
            row(label+":text="+enc(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(eyeList,0,true))+":count="+f(magic,"a","counter",int.class).getInt(eye)+":append="+hasAppend(eyeList));
            Vector lines=(Vector)f(text,"a","lines",Vector.class).get(eyeList);StringBuilder formats=new StringBuilder();
            for(Object l:lines)for(Object p:(Vector)f(line,"a","items",Vector.class).get(l)){
                formats.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(';');
            }
            row(label+":formats="+formats);
            if(enabled){
                command(eye,cmd("f","cmdCopyText"));snap(label+":copy");
                command(eye,cmd("g","cmdCopyAppend"));snap(label+":append");
                command(eye,cmd("h","cmdCopyAll"));snap(label+":all");
                for(int type:new int[]{0,1,2,3}){key(eye,eyeList,42,type);snap(label+":star-"+type);}
            }
            command(eye,cmd("r","cmdClearText"));row(label+":clear="+f(magic,"a","counter",int.class).getInt(eye)+":"+hasAppend(eyeList));
        }
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseClipboard();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
