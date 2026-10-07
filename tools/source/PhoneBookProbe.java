import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Native PhoneBook forms, command identity, repeated state and actual SMS interface calls/catches. */
public class PhoneBookProbe extends FileTransferProbe {
    static Class<?> phone,spy,jimm;
    static Field singleton,number,sms,display,midlet;
    static Field[] buttons=new Field[4];static Command[] originalButtons=new Command[4];
    static Object originalDisplay,originalMidlet;
    static TextBox other;static int activations,commands,destinations;
    static Object controller()throws Exception{return singleton.get(null);}
    static TextBox num()throws Exception{return (TextBox)number.get(controller());}
    static TextBox text()throws Exception{return (TextBox)sms.get(controller());}
    static String units(String s){return PasswordProbe.units(s);}
    static String result(Method method,Object target,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(target,args));}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof Error&&!t.getClass().getName().equals("PhoneBookIO$FaultError"))throw new AssertionError("Broken PhoneBook fixture",t);return "exception:"+t.getClass().getName();}
    }
    static String identity(Object value)throws Exception{
        if(value==null)return "null";if(value==originalDisplay)return "display";if(value==originalMidlet)return "midlet";if(value==other)return "other";
        Object c=controller();if(c!=null){if(value==num())return "number";if(value==text())return "sms";}
        if(value==spy.getField("endpoint").get(null))return "endpoint";if(value==spy.getField("text").get(null))return "message";
        if(value instanceof String)return units((String)value);throw new AssertionError("Unrecognized event identity "+value.getClass());
    }
    static void nativeBox(StringBuilder b,String name,TextBox box,Object c)throws Exception{
        b.append('/').append(name).append(':');if(box==null){b.append("null");return;}
        b.append(units(box.getTitle())).append(':').append(units(box.getString())).append(':').append(box.getMaxSize()).append(':').append(box.getConstraints());
        Method listener=Displayable.class.getDeclaredMethod("getCommandListener");listener.setAccessible(true);b.append(":listener=").append(listener.invoke(box)==c);
        Method method=Displayable.class.getDeclaredMethod("getCommands");method.setAccessible(true);
        for(Object value:(Vector)method.invoke(box)){Command command=(Command)value;b.append("/cmd:").append(units(command.getLabel())).append(':').append(command.getCommandType()).append(':').append(command.getPriority());for(Command original:originalButtons)b.append(':').append(command==original);}
    }
    static void snap(String label,String outcome,Object old,TextBox oldNumber,TextBox oldSms)throws Exception{
        Object c=controller();StringBuilder b=new StringBuilder(label).append(':').append(outcome).append("/instance:").append(c!=null).append("/same:").append(c==old);
        if(c!=null){b.append("/same-number:").append(num()==oldNumber).append("/same-sms:").append(text()==oldSms);nativeBox(b,"number",num(),c);nativeBox(b,"sms",text(),c);}
        Object endpoint=spy.getField("endpoint").get(null), message=spy.getField("text").get(null);
        b.append("/connection:").append(endpoint!=null);if(endpoint!=null)b.append(':').append(endpoint.getClass().getField("sent").getBoolean(endpoint)).append(':').append(endpoint.getClass().getField("closed").getBoolean(endpoint));
        b.append("/message:").append(message!=null);if(message!=null)b.append(':').append(units((String)message.getClass().getField("payload").get(message)));
        Vector events=(Vector)spy.getField("events").get(null);for(Object value:events){Object[] event=(Object[])value;b.append("/event:").append(event[0]);destinations++;for(Object arg:(Object[])event[1])b.append(':').append(identity(arg));}events.clear();row(b.toString());
    }
    static void fresh()throws Exception{spy.getMethod("reset").invoke(null);singleton.set(null,null);display.set(null,originalDisplay);midlet.set(null,originalMidlet);for(int i=0;i<4;i++)buttons[i].set(null,originalButtons[i]);}
    static void activate(String label)throws Exception{Object old=controller();TextBox oldN=old==null?null:num(),oldS=old==null?null:text();String outcome=result(m(phone,"a","activate",void.class),null);activations++;snap(label,outcome,old,oldN,oldS);}
    static void press(String label,Command command,Displayable supplied)throws Exception{Object c=controller();TextBox oldN=num(),oldS=text();String outcome=result(m(phone,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,command,supplied);commands++;snap(label,outcome,c,oldN,oldS);}
    static void input(String label,TextBox box,String value)throws Exception{
        String outcome="null";try{box.setString(value);}catch(Exception e){outcome="exception:"+e.getClass().getName();}snap(label,outcome,controller(),num(),text());
    }
    static String repeated(int count){char[] a=new char[count];Arrays.fill(a,'7');return new String(a);}
    static void normalCases()throws Exception{
        String[] numbers={null,"","1","+12345","123 456","(123)-456","*123#","abc","\u0030\u0000\u0031","\u041d\u043e\u043c\u0435\u0440","\ud800x\udfff",repeated(30),repeated(31)};
        String[] messages={null,"","hello","\u0422\u0435\u043a\u0441\u0442","\ud800x\udfff","a\r\nb\u0000c",repeated(500),repeated(501)};
        for(int n=0;n<numbers.length;n++)for(int s=0;s<messages.length;s++)for(int kind=0;kind<7;kind++)for(int supplied=0;supplied<3;supplied++){
            String label="normal-"+n+'-'+s+'-'+kind+'-'+supplied;fresh();activate(label+"-open");input(label+"-number",num(),numbers[n]);
            press(label+"-sms",originalButtons[1],null);if(text()!=null)input(label+"-text",text(),messages[s]);
            Command command=kind<4?originalButtons[kind]:kind==4?new Command(originalButtons[2].getLabel(),originalButtons[2].getCommandType(),originalButtons[2].getPriority()):kind==5?new Command("unknown",Command.SCREEN,42):null;
            spy.getField("platformResult").setBoolean(null,(n&1)!=0);press(label,command,supplied==0?num():supplied==1?text():other);
            activate(label+"-reopen");press(label+"-resend",originalButtons[3],null);
        }
    }
    static void script(String target,int mode,int ordinal)throws Exception{spy.getField("failing").set(null,target);spy.getField("mode").setInt(null,mode);spy.getField("ordinal").setInt(null,ordinal);}
    static void faultCases()throws Exception{
        for(String target:new String[]{"open","new","payload","send","close","call","menu","display"})for(int mode=1;mode<=4;mode++)for(int ordinal=1;ordinal<=2;ordinal++){
            String label="fault-"+target+'-'+mode+'-'+ordinal;fresh();activate(label+"-open");input(label+"-number",num(),"+12345");press(label+"-sms",originalButtons[1],null);input(label+"-text",text(),"hello");script(target,mode,ordinal);
            for(int repeat=0;repeat<3;repeat++)press(label+'-'+repeat,target.equals("call")?originalButtons[2]:target.equals("display")?originalButtons[1]:target.equals("menu")?originalButtons[0]:originalButtons[3],other);
        }
        for(int conn=0;conn<3;conn++)for(int message=0;message<3;message++){
            fresh();activate("type-open");num().setString("1");press("type-sms",originalButtons[1],null);text().setString("hello");spy.getField("connectionKind").setInt(null,conn);spy.getField("messageKind").setInt(null,message);
            for(int repeat=0;repeat<2;repeat++)press("types-"+conn+'-'+message+'-'+repeat,originalButtons[3],null);
        }
        for(int missing=0;missing<4;missing++)for(int button=0;button<4;button++){
            fresh();activate("null-open");num().setString("1");press("null-sms",originalButtons[1],null);text().setString("hello");
            if(missing==0)number.set(controller(),null);if(missing==1)sms.set(controller(),null);if(missing==2)display.set(null,null);if(missing==3)midlet.set(null,null);
            press("null-"+missing+'-'+button,originalButtons[button],null);
        }
        for(int mode=1;mode<=4;mode++)for(int ordinal=1;ordinal<=2;ordinal++){
            fresh();script("display",mode,ordinal);for(int repeat=0;repeat<3;repeat++)activate("activation-fault-"+mode+'-'+ordinal+'-'+repeat);
        }
    }
    static void aliasCases()throws Exception{
        for(int changed=0;changed<4;changed++)for(int replacement=-1;replacement<4;replacement++)for(int kind=0;kind<5;kind++){
            fresh();activate("alias-open");num().setString("1");press("alias-sms",originalButtons[1],null);text().setString("hello");buttons[changed].set(null,replacement==-1?null:originalButtons[replacement]);
            press("alias-"+changed+'-'+replacement+'-'+kind,kind==4?null:originalButtons[kind],other);activate("alias-reopen");
        }
    }
    static void exercisePhone()throws Exception{
        setupFiles();phone=load("bd","jimm.util.PhoneBook");spy=load("PhoneBookIO","PhoneBookIO");jimm=load("jimm.Jimm","jimm.Jimm");singleton=f(phone,"a","instance",phone);number=f(phone,"b","inputNumber",TextBox.class);sms=f(phone,"a","SmsTextBox",TextBox.class);
        display=f(jimm,"a","display",Display.class);midlet=f(jimm,"a","jimm",jimm);originalDisplay=display.get(null);originalMidlet=midlet.get(null);other=new TextBox("other","other",30,TextField.ANY);
        String[] raw={"a","b","c","d"}, names={"cmdBack","cmdSms","cmdCall","cmdSend"};for(int i=0;i<4;i++){buttons[i]=f(phone,raw[i],names[i],Command.class);if(Modifier.isFinal(buttons[i].getModifiers()))throw new AssertionError("PhoneBook commands unexpectedly final");originalButtons[i]=(Command)buttons[i].get(null);}
        normalCases();faultCases();aliasCases();
        fresh();activate("stale-open");num().setString("123");press("stale-sms",originalButtons[1],null);text().setString("old text");
        activate("stale-reopen");num().setString("+456");press("stale-send-to-new-number",originalButtons[3],text());press("stale-send-repeat",originalButtons[3],other);
        press("stale-sms-recreate",originalButtons[1],num());press("stale-send-empty",originalButtons[3],null);fresh();
        if(activations<1000||commands<1000||destinations<1000)throw new AssertionError("PhoneBook coverage guards");row("activations:"+activations+"/commands:"+commands+"/destinations:"+destinations);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exercisePhone();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
