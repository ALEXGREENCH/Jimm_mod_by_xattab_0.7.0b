import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Actual May editor titles, creation/reuse, pagination, insertion and terminal failures. */
public class EditorProbe extends ClipboardProbe {
    static Class<?> boundary,jimm;
    static int titleCalls,pageCalls,writeCalls,commandAdds,typingCalls,gcs,lights;
    static TextBox box()throws Exception{return(TextBox)f(ui,"a","messageTextbox",TextBox.class).get(null);}
    static Vector pages()throws Exception{return(Vector)f(ui,"a","strings",Vector.class).get(controller);}
    static void put(String name,Object value)throws Exception{boundary.getField(name).set(null,value);}
    static Vector events()throws Exception{return(Vector)boundary.getField("events").get(null);}
    static String utf(Object value){return JimmUrlProbe.utf(value);}
    static String actual(Method method,Object target,Object...args)throws Exception {
        try{return String.valueOf(method.invoke(target,args));}
        catch(InvocationTargetException e){Throwable cause=e.getCause();if(cause instanceof LinkageError)throw(LinkageError)cause;
            return "exception:"+(error.isInstance(cause)?"JimmException":cause.getClass().getName());}
    }
    static void resetEditor(TextBox initial,int size,int current,String caption)throws Exception {
        f(ui,"a","messageTextbox",TextBox.class).set(null,initial);
        f(ui,"e","textLimit",int.class).setInt(null,initial==null?0:initial.getMaxSize());
        Vector v=pages();v.clear();for(int i=0;i<size;i++)v.addElement(i%3==0?null:i%3==1?"":"pg"+i);
        f(ui,"d","current",int.class).setInt(controller,current);f(ui,"h","caption",String.class).set(controller,caption);
        events().clear();put("shown",null);put("failLabel","");put("failure",null);
    }
    static void state(String label)throws Exception {
        TextBox b=box();StringBuilder p=new StringBuilder();for(Object v:pages())p.append(utf(v)).append(';');
        row(label+":caption="+utf(f(ui,"h","caption",String.class).get(controller))+":page="+f(ui,"d","current",int.class).getInt(controller)
            +":pages="+p+":limit="+f(ui,"e","textLimit",int.class).getInt(null));
        if(b==null){row(label+":box=null");return;}
        Method commands=Displayable.class.getDeclaredMethod("getCommands");commands.setAccessible(true);
        StringBuilder cmd=new StringBuilder();for(Object value:(Vector)commands.invoke(b)){Command c=(Command)value;cmd.append(utf(c.getLabel())).append('/').append(c.getCommandType()).append('/').append(c.getPriority()).append(';');}
        Field listener=Displayable.class.getDeclaredField("listener");listener.setAccessible(true);
        row(label+":text="+utf(b.getString())+":title="+utf(b.getTitle())+":caret="+b.getCaretPosition()+":constraints="+b.getConstraints()
            +":max="+b.getMaxSize()+":commands="+cmd+":listener="+(listener.get(b)==controller)+":shown="+(boundary.getField("shown").get(null)==b));
    }
    static void log(String label)throws Exception {
        for(Object value:events()) {
            Object[] e=(Object[])value,args=(Object[])e[1];String kind=(String)e[0];StringBuilder b=new StringBuilder(label+":"+kind);
            if(kind.equals("add"))commandAdds++;if(kind.equals("typing"))typingCalls++;if(kind.equals("gc"))gcs++;if(kind.equals("light"))lights++;
            for(Object arg:args) {
                if(arg instanceof Command){Command c=(Command)arg;b.append('|').append(utf(c.getLabel())).append('/').append(c.getCommandType()).append('/').append(c.getPriority());}
                else if(arg instanceof String||arg==null)b.append('|').append(utf(arg));
                else if(arg instanceof Displayable)b.append('|').append(arg==box()?"box":"other");
                else if(arg instanceof CommandListener)b.append('|').append(arg==controller?"controller":"other");
                else b.append('|').append(arg);
            }
            row(b.toString());
        }
        events().clear();
    }
    static void titles()throws Exception {
        for(boolean hidden:new boolean[]{false,true})for(String title:new String[]{null,"","Name","\u043f\u010d","\ud800x"})for(int size:new int[]{0,1,2,4})for(int page:new int[]{-1,0,2,5})for(boolean present:new boolean[]{false,true}) {
            bool(154,hidden);resetEditor(present?new TextBox("old","abc",32,0):null,size,page,"before");String label="title-"+titleCalls;
            row(label+":"+actual(m(ui,"a","setCaption",void.class,String.class),controller,title));state(label);titleCalls++;
        }
    }
    static void pagination()throws Exception {
        for(boolean hidden:new boolean[]{false,true})for(String cap:new String[]{null,"","Name"})for(String insertion:new String[]{null,"","abc","abcdefgh","abcdefghi",SendTextProbe.sample(25,2)})for(int pos:new int[]{-1,0,1,3,4}) {
            bool(154,hidden);resetEditor(new TextBox("old","abc",8,0),0,0,cap);String label="page-"+pageCalls;
            row(label+":insert="+actual(m(ui,"a","insert",void.class,String.class,int.class),controller,insertion,pos));state(label);
            row(label+":save="+actual(m(ui,"h","saveCurPage",void.class),controller));state(label+"-save");
            row(label+":get="+utf(actual(m(ui,"c","getString",String.class),controller)));state(label+"-get");
            f(ui,"d","current",int.class).setInt(controller,0);
            row(label+":first="+actual(m(ui,"i","setCurrentScreen",void.class),controller));state(label+"-first");pageCalls++;
        }
    }
    static void open(String label,Object receiver,String init)throws Exception {
        row(label+":result="+actual(m(ui,"a","writeMessage",void.class,contact,String.class),null,receiver,init)
            +":receiver="+(f(ui,"a","textMessReceiver",contact).get(null)==receiver)+":mode="+f(ui,"f","textMessCurMode",int.class).getInt(null));
        log(label);state(label);writeCalls++;
    }
    static Object receiver(String name,boolean capability)throws Exception {
        Object value=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345",name,false,true);
        f(contact,"a","caps",int.class).setInt(value,capability?256:0);return value;
    }
    static void writing()throws Exception {
        int scenario=0;
        for(int flags=0;flags<16;flags++)for(boolean se:new boolean[]{false,true})for(boolean clipboard:new boolean[]{false,true})for(boolean reuse:new boolean[]{false,true})for(int recipient=0;recipient<3;recipient++)for(String init:new String[]{null,"","\u043f\u010d",SendTextProbe.sample(2049,2)}) {
            bool(154,(flags&1)!=0);bool(131,(flags&2)!=0);bool(181,(flags&4)!=0);bool(182,(flags&8)!=0);
            integer(88,scenario%3);integer(110,scenario%4);f(jimm,"b","is_phone_SE",boolean.class).setBoolean(null,se);
            clear();if(clipboard)put(true,"***error***",null,"seed",get(true));
            Object receiver=recipient==0?null:receiver(recipient==1?"Name":null,scenario%2==0);
            resetEditor(reuse?new TextBox("prior","old",2048,0):null,reuse?2:0,0,"prior");
            String label="open-"+scenario++;open(label,receiver,init);
        }
        Object receiver=receiver("Name",true);integer(88,1);integer(110,0);f(jimm,"b","is_phone_SE",boolean.class).setBoolean(null,true);
        for(String failed:new String[]{"add","constraints","gc","listener","display","typing","light"})for(int kind=0;kind<3;kind++) {
            resetEditor(null,0,0,null);bool(154,false);bool(131,true);bool(181,true);bool(182,true);
            Throwable failure=kind==0?new IllegalStateException("test"):kind==1?new AssertionError("test"):(Throwable)SendTextProbe.jimmError(false);
            put("failLabel",failed);put("failure",failure);String label="failure-"+failed+'-'+kind;
            open(label,receiver,"new");put("failure",null);bool(181,false);bool(182,false);open(label+"-reuse",receiver,null);
        }
    }
    static void exerciseEditor()throws Exception {
        ui=load("cf","jimm.JimmUI");boundary=load("EditorIO","EditorIO");jimm=load("jimm.Jimm","jimm.Jimm");controller=f(ui,"a","_this",ui).get(null);
        // Share only the real JimmException constructor helper from the already tested sender probe.
        SendTextProbe.ref=ref;SendTextProbe.error=error;
        titles();pagination();writing();
        if(commandAdds<1000||typingCalls<100||(ref&&(gcs<100||lights<100)))throw new AssertionError("Missing successful editor routes");
        row("coverage:"+titleCalls+":"+pageCalls+":"+writeCalls+":"+commandAdds+":"+typingCalls+":"+gcs+":"+lights);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseEditor();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
