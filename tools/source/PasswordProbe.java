import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real native password TextBox, singleton lifetime, exact command identity and controller state. */
public class PasswordProbe extends FileTransferProbe {
    static Class<?> password, splash, jimm, spy;
    static TextBox previous1, previous2;
    static Field singleton, protectedFlag, locked, pound, retries, display, midlet;
    static int activations, commands, notifications;
    static Object originalDisplay, originalMidlet;
    static Command button(String a,String b)throws Exception{return (Command)cmd(a,b);}
    static Object controller()throws Exception{return singleton.get(null);}
    static TextBox box(Object c)throws Exception{return (TextBox)f(password,"a","passwordTextField",TextBox.class).get(c);}
    static String units(String s){if(s==null)return "null";StringBuilder b=new StringBuilder();for(int i=0;i<s.length();i++){String x=Integer.toHexString(s.charAt(i));for(int j=x.length();j<4;j++)b.append('0');b.append(x);}return b.toString();}
    static String result(Method method,Object target,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(target,args));}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken password fixture",e.getCause());return "exception:"+e.getCause().getClass().getName();}
    }
    static Vector destinations()throws Exception{return (Vector)spy.getField("events").get(null);}
    static String screen(Object value)throws Exception{
        if(value==null)return "null";if(value==previous1)return "previous1";if(value==previous2)return "previous2";
        Object c=controller();return c!=null&&value==box(c)?"password-box":"unexpected:"+value.getClass().getName();
    }
    static void snap(String label,String outcome,Object old)throws Exception{
        Object c=controller();StringBuilder b=new StringBuilder(label).append(':').append(outcome).append("/instance:").append(c!=null).append("/same:").append(c==old)
            .append("/protected:").append(protectedFlag.getBoolean(null)).append("/locked:").append(locked.getBoolean(null)).append("/pound:").append(pound.getLong(null)).append("/retries:").append(retries.getInt(null)).append("/saved:").append(units((String)((Object[])f(options,"a","options",Object[].class).get(null))[38]));
        if(c!=null){TextBox text=box(c);b.append("/previous:").append(screen(f(password,"a","_PreviousForm",Displayable.class).get(c)))
            .append("/textbox:").append(units(text.getTitle())).append(':').append(units(text.getString())).append(':').append(text.getMaxSize()).append(':').append(text.getConstraints());
            Method listener=Displayable.class.getDeclaredMethod("getCommandListener");listener.setAccessible(true);b.append("/listener:").append(listener.invoke(text)==c);
            Method getCommands=Displayable.class.getDeclaredMethod("getCommands");getCommands.setAccessible(true);
            for(Object command:(Vector)getCommands.invoke(text)){Command cmd=(Command)command;b.append("/command:").append(units(cmd.getLabel())).append(':').append(cmd.getCommandType()).append(':').append(cmd.getPriority())
                .append(':').append(cmd==button("a","cmdOk")).append(':').append(cmd==button("b","cmdCancel"));}
        }
        for(Object event:destinations()){Object[] e=(Object[])event, args=(Object[])e[1];String kind=(String)e[0];b.append("/event:").append(kind);notifications++;
            if(kind.equals("display"))b.append(':').append(args[0]==originalDisplay).append(':').append(screen(args[1]));
            else if(kind.equals("destroy"))b.append(':').append(args[0]==originalMidlet).append(':').append(args[1]);
            else for(Object arg:args)b.append(':').append(arg);
        }
        destinations().clear();row(b.toString());
    }
    static void fresh(String saved,boolean secure,boolean online,boolean lock,boolean auto,int retry,long time)throws Exception{
        spy.getMethod("reset").invoke(null);singleton.set(null,null);if(saved==null)((Object[])f(options,"a","options",Object[].class).get(null))[38]=null;else string(38,saved);bool(138,auto);integer(91,retry);
        protectedFlag.setBoolean(null,secure);f(icq,"b","connected",boolean.class).setBoolean(null,online);locked.setBoolean(null,lock);pound.setLong(null,time);retries.setInt(null,777);
        display.set(null,originalDisplay);midlet.set(null,originalMidlet);
    }
    static void activate(String label,Displayable previous)throws Exception{Object old=controller();String outcome=result(m(password,"a","activate",void.class,Displayable.class),null,previous);activations++;snap(label,outcome,old);}
    static void press(String label,Command command,Displayable supplied)throws Exception{Object c=controller();String outcome=result(m(password,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,command,supplied);commands++;snap(label,outcome,c);}
    static Displayable previous(int n){return n==0?null:n==1?previous1:previous2;}
    static void activationCases()throws Exception{
        String[] saved={null,"","secret","01234567890123456789","\u041f\u0430\u0440\u043e\u043b\u044c","too-long-012345678901234567890"};
        for(int p=0;p<saved.length;p++)for(boolean secure:new boolean[]{false,true})for(boolean online:new boolean[]{false,true})
            for(boolean lock:new boolean[]{false,true})for(boolean auto:new boolean[]{false,true})for(int previous=0;previous<3;previous++)for(int retry:new int[]{Integer.MIN_VALUE,-1,0,5,Integer.MAX_VALUE}){
                String label="activate-"+p+'-'+secure+'-'+online+'-'+lock+'-'+auto+'-'+previous+'-'+retry;
                fresh(saved[p],secure,online,lock,auto,retry,Long.MAX_VALUE);activate(label,previous(previous));box(controller()).setString("dirty");
                protectedFlag.setBoolean(null,!secure);f(icq,"b","connected",boolean.class).setBoolean(null,!online);bool(138,!auto);
                activate(label+"-repeat",previous((previous+1)%3));press(label+"-cancel",button("b","cmdCancel"),null);
            }
    }
    static void commandCases()throws Exception{
        String[] saved={null,"","secret","SECRET","01234567890123456789","\u041f\u0430\u0440\u043e\u043b\u044c","\ud800x\udfff"};
        for(int p=0;p<saved.length;p++)for(int entry=0;entry<5;entry++)for(int kind=0;kind<5;kind++)for(int supplied=0;supplied<3;supplied++)
            for(boolean online:new boolean[]{false,true})for(boolean lock:new boolean[]{false,true})for(boolean auto:new boolean[]{false,true}){
                String label="command-"+p+'-'+entry+'-'+kind+'-'+supplied+'-'+online+'-'+lock+'-'+auto;
                fresh(saved[p],true,online,lock,auto,5,123456789L);activate(label+"-open",previous1);
                String entered=entry==0?"":entry==1?saved[p]:entry==2?"wrong":entry==3?"SECRET":"too-long-012345678901234567890";
                TextBox text=box(controller());String outcome="null";
                try{text.setString(entered);}catch(Exception e){outcome="exception:"+e.getClass().getName();}
                snap(label+"-entry",outcome,controller());
                if(!outcome.equals("null"))continue;
                Command command=kind==0?button("a","cmdOk"):kind==1?button("b","cmdCancel"):kind==2?new Command(button("a","cmdOk").getLabel(),button("a","cmdOk").getCommandType(),button("a","cmdOk").getPriority()):kind==3?new Command("unknown",Command.SCREEN,42):null;
                press(label,command,supplied==0?text:supplied==1?previous2:null);
            }
    }
    static void faultCases()throws Exception{
        fresh("secret",true,false,false,false,5,99);snap("null-saved-setter",result(m(options,"a","setString",void.class,int.class,String.class),null,38,null),controller());
        for(String target:new String[]{"display","light","activate","before","connect","unlock","destroy"})for(int mode:new int[]{1,2,3})for(int ordinal=1;ordinal<=2;ordinal++){
            if(mode==1&&!target.equals("destroy"))continue;
            boolean secure=target.equals("display")||target.equals("light"), online=target.equals("activate");
            fresh("secret",secure,online,false,!target.equals("unlock"),-5,Long.MIN_VALUE);
            spy.getField("failing").set(null,target);spy.getField("mode").setInt(null,mode);spy.getField("ordinal").setInt(null,ordinal);
            for(int repeat=0;repeat<3;repeat++){
                String label="fault-"+target+'-'+mode+'-'+ordinal+'-'+repeat;activate(label,previous1);
                if(target.equals("destroy"))press(label+"-cancel",button("b","cmdCancel"),previous2);
            }
        }
        for(String target:new String[]{"before","connect","unlock"})for(int mode:new int[]{2,3})for(boolean online:new boolean[]{false,true}){
            fresh("secret",true,online,true,!target.equals("unlock"),-5,Long.MAX_VALUE);activate("ok-fault-open",previous1);box(controller()).setString("secret");
            spy.getField("failing").set(null,target);spy.getField("mode").setInt(null,mode);
            for(int repeat=0;repeat<2;repeat++)press("ok-fault-"+target+'-'+mode+'-'+online+'-'+repeat,button("a","cmdOk"),previous2);
        }
        for(boolean cancellation:new boolean[]{false,true}){
            fresh("secret",true,true,false,false,5,99);if(cancellation)activate("null-display-open",previous1);display.set(null,null);
            if(cancellation)press("null-display-cancel",button("b","cmdCancel"),null);else activate("null-display-activate",previous1);display.set(null,originalDisplay);
        }
        fresh("secret",true,false,false,false,5,99);activate("null-midlet-open",previous1);midlet.set(null,null);press("null-midlet-cancel",null,null);midlet.set(null,originalMidlet);
    }
    static void exercisePassword()throws Exception{
        setupFiles();password=load("by","jimm.EnterPassword");splash=load("cv","jimm.SplashCanvas");jimm=load("jimm.Jimm","jimm.Jimm");spy=load("PasswordIO","PasswordIO");
        singleton=f(password,"a","instance",password);protectedFlag=f(jimm,"h","isPasswordProtected",boolean.class);locked=f(splash,"c","isLocked",boolean.class);pound=f(splash,"a","poundPressTime",long.class);retries=f(icq,"b","reconnect_attempts",int.class);
        display=f(jimm,"a","display",Display.class);midlet=f(jimm,"a","jimm",jimm);originalDisplay=display.get(null);originalMidlet=midlet.get(null);
        previous1=new TextBox("Previous 1","one",30,TextField.ANY);previous2=new TextBox("Previous 2","two",30,TextField.ANY);
        activationCases();commandCases();faultCases();
        if(activations<1000||commands<1000||notifications<1000)throw new AssertionError("Password coverage guards");row("activations:"+activations+"/commands:"+commands+"/destinations:"+notifications);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exercisePassword();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
