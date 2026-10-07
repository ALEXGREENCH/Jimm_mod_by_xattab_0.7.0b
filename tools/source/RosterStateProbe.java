import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Whole roster data, chat navigation, typing and sound-mode state on actual application classes. */
public class RosterStateProbe {
    static Properties spec;static ClassLoader loader;static PrintWriter out;static boolean scripted;
    static Class<?> list,contact,group,vl,text,history,chat,ui,options,canvasType,io;
    static Field contacts,last,receiver,enter,table,historyTable,current,chatText,currentUin,built;
    static Object canvas,roster,sentinel;static Object[] persons=new Object[5],values;
    static Method next,unread,items,typing,sound,setInt,getInt,setBoolean,newChat;
    static int observations,dataCases,navCases,typingCases,soundCases,selectedChats,recordsWritten;
    static Class<?> type(String k)throws Exception{return Class.forName(spec.getProperty(k),true,loader);}
    static Field field(Class<?> c,String k,Class<?> t)throws Exception{return PopupProbe.field(c,spec.getProperty(k),t);}
    static Method method(Class<?> c,String k,Class<?> t,Class<?>...p)throws Exception{return PopupProbe.method(c,spec.getProperty(k),t,p);}
    static Object result;
    static String call(Method m,Object...args)throws Exception {
        result=null;try{result=m.invoke(null,args);return "ok";}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return "exception:"+t.getClass().getName();}
    }
    static int id(Object c){if(c==null)return -1;for(int i=0;i<persons.length;i++)if(c==persons[i])return i;return -2;}
    static String counts()throws Exception {StringBuilder b=new StringBuilder();for(Object c:persons){b.append('[');for(int k=67;k<=70;k++)b.append(getInt.invoke(c,k)).append(',');b.append(']');}return b.toString();}
    static Vector calls()throws Exception{return scripted?(Vector)io.getField("calls").get(null):new Vector();}
    static void api(int at,int fault,boolean shown)throws Exception {
        if(scripted){io.getField("failureAt").setInt(null,at);io.getField("failure").setInt(null,fault);io.getField("seen").setInt(null,0);io.getField("shown").setBoolean(null,shown);calls().clear();}
    }
    static void seed()throws Exception {
        values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);values[3]="RU";values[40]=new String(new char[12]);table.set(null,values);
        contacts.set(null,new Vector());historyTable.set(null,new Hashtable());last.set(null,persons[4]);receiver.set(null,persons[4]);enter.setBoolean(null,true);current.set(canvas,sentinel);built.setBoolean(null,false);
        for(int i=0;i<persons.length;i++){setBoolean.invoke(persons[i],16,false);for(int k=67;k<=70;k++)setInt.invoke(persons[i],k,17+i+k-67);}
        api(0,0,true);
    }
    static void row(String s){out.println(s);observations++;}
    static void data()throws Exception {
        for(int slot=67;slot<=70;slot++)for(int value=-1;value<=257;value++) {
            seed();Vector v=new Vector();for(int i=0;i<4;i++){v.addElement(persons[i]);setInt.invoke(persons[i],slot,value+i);}contacts.set(null,v);
            String answer=call(unread);row("unread:"+slot+":"+value+":"+answer+":"+result+":"+counts());dataCases++;
        }
        int[] gids={Integer.MIN_VALUE,-65536,-1,0,1,2,32767,65535,65536,Integer.MAX_VALUE};
        for(int gid:gids)for(int pattern=0;pattern<9;pattern++) {
            seed();Vector v=new Vector();for(int i=0;i<4;i++){setInt.invoke(persons[i],65,gids[(i+pattern)%gids.length]);v.addElement(persons[i]);}if(pattern%2==0)v.addElement(persons[1]);contacts.set(null,v);
            Object g=group.getDeclaredConstructor(int.class,String.class).newInstance(gid,"Group");String answer=call(items,g);StringBuilder b=new StringBuilder();if(result!=null)for(Object c:(Object[])result)b.append(id(c)).append(',');
            row("group:"+gid+":"+pattern+":"+answer+":"+b+":"+counts());dataCases++;
        }
        for(int broken=-2;broken<4;broken++)for(int poison=0;poison<3;poison++) {
            seed();Vector v=new Vector();for(int i=0;i<4;i++)v.addElement(persons[i]);if(broken>=0)v.setElementAt(poison==0?null:poison==1?"wrong":group.getDeclaredConstructor().newInstance(),broken);contacts.set(null,broken==-2?null:v);
            String answer=call(unread);row("unread-broken:"+broken+":"+poison+":"+answer+":"+result+":"+counts());dataCases++;
            answer=call(items,broken==-1?null:group.getDeclaredConstructor(int.class,String.class).newInstance(1,"G"));row("group-broken:"+broken+":"+poison+":"+answer+":"+(result==null)+":"+counts());dataCases++;
        }
        seed();Vector empty=new Vector();contacts.set(null,empty);row("empty-unread:"+call(unread)+":"+result);dataCases++;
        for(int count:new int[]{2105376,2105377}){seed();for(int k=67;k<=70;k++)setInt.invoke(persons[0],k,255);Vector big=new Vector(count);for(int i=0;i<count;i++)big.addElement(persons[0]);contacts.set(null,big);row("overflow:"+count+":"+call(unread)+":"+result+":"+big.size());dataCases++;}
    }
    static String navigationState()throws Exception {
        Object control=current.get(canvas);int active=-1;for(int i=0;i<4;i++){Object c=((Hashtable)historyTable.get(null)).get("100"+i);if(chat.isInstance(c)&&chatText.get(c)==control)active=i;}
        return id(last.get(null))+":"+id(receiver.get(null))+":"+enter.getBoolean(null)+":"+active+":"+currentUin.get(null)+":"+counts();
    }
    static void navigate(int mask,int previous,boolean forward,int broken,int at,int fault,boolean shown)throws Exception {
        seed();Vector v=new Vector();for(int i=0;i<4;i++){v.addElement(persons[i]);if((mask&(1<<i))!=0)newChat.invoke(null,persons[i],"Name "+i);}contacts.set(null,v);
        if(broken==1)v.setElementAt(null,2);if(broken==2)v.setElementAt("wrong",2);if(broken==3)v.addElement(persons[1]);if(broken==4)((Hashtable)historyTable.get(null)).clear();if(broken==5)((Hashtable)historyTable.get(null)).put("1002","wrong");if(broken==6)contacts.set(null,null);
        last.set(null,previous<0?previous==-1?null:persons[4]:persons[previous]);receiver.set(null,persons[4]);enter.setBoolean(null,true);current.set(canvas,sentinel);api(at,fault,shown);
        String answer=call(next,forward);if(answer.equals("ok")&&result!=null)selectedChats++;
        row("next:"+mask+":"+previous+":"+forward+":"+broken+":"+at+":"+fault+":"+shown+":"+answer+":"+result+":"+navigationState()+":"+calls());navCases++;
        if(Thread.holdsLock(list))throw new AssertionError("Roster monitor leaked");
    }
    static void navigation()throws Exception {
        int limit=scripted?16:1;
        for(int mask=0;mask<limit;mask++)for(int previous=-2;previous<4;previous++)for(boolean forward:new boolean[]{false,true})navigate(mask,previous,forward,0,0,0,true);
        for(int broken=1;broken<=6;broken++)for(int previous=-2;previous<4;previous++)for(boolean forward:new boolean[]{false,true})navigate(scripted?15:0,previous,forward,broken,0,0,true);
        if(scripted){for(int at=1;at<=12;at++)for(int fault:new int[]{1,2})for(boolean forward:new boolean[]{false,true})navigate(15,1,forward,0,at,fault,true);
            for(int previous=0;previous<4;previous++)for(boolean forward:new boolean[]{false,true})navigate(15,previous,forward,0,0,0,false);}
        if(scripted&&selectedChats==0)throw new AssertionError("No successful real chat activation");
    }
    static void typings()throws Exception {
        if(!scripted)return;
        for(int state=0;state<6;state++)for(boolean value:new boolean[]{false,true})for(boolean shown:new boolean[]{false,true})for(int at=0;at<=3;at++)for(int fault:new int[]{0,1,2}) {
            seed();newChat.invoke(null,persons[0],"Typing");Object c=((Hashtable)historyTable.get(null)).get("1000");
            current.set(canvas,state==1?chatText.get(c):state==2?roster:sentinel);if(state==3)((Hashtable)historyTable.get(null)).clear();if(state==4)field(list,"treeField",type("tree")).set(null,null);if(state==5)((Hashtable)historyTable.get(null)).put("1000","wrong");
            api(at,fault,shown);String answer=call(typing,"1000",value);row("typing:"+state+":"+value+":"+shown+":"+at+":"+fault+":"+answer+":"+calls());typingCases++;
            field(list,"treeField",type("tree")).set(null,roster);if(Thread.holdsLock(list))throw new AssertionError("Typing monitor leaked");
        }
    }
    static String records()throws Exception {
        try{RecordStore rs=RecordStore.openRecordStore("options",false);StringBuilder b=new StringBuilder();for(int i=1;i<rs.getNextRecordID();i++){byte[] data=rs.getRecord(i);b.append(i).append('=').append(data==null?"null":Base64.getEncoder().encodeToString(data)).append(';');recordsWritten++;}rs.closeRecordStore();return b.toString();}
        catch(RecordStoreNotFoundException absent){return "absent";}
    }
    static void modes()throws Exception {
        for(boolean silent:new boolean[]{false,true})for(boolean vibra:new boolean[]{false,true})for(int vibrator:new int[]{Integer.MIN_VALUE,-1,0,1,2,Integer.MAX_VALUE})for(boolean alert:new boolean[]{false,true})for(boolean shown:new boolean[]{false,true}) {
            if(!scripted&&alert)continue;seed();try{RecordStore.deleteRecordStore("options");}catch(RecordStoreNotFoundException absent){}
            values[150]=silent;values[168]=vibra;values[75]=vibrator;api(0,0,shown);String answer=call(sound,alert);
            row("sound:"+silent+":"+vibra+":"+vibrator+":"+alert+":"+shown+":"+answer+":"+result+":"+values[150]+":"+values[75]+":"+records()+":"+calls());soundCases++;
        }
        if(scripted){
            for(int broken:new int[]{-2,-1,0,75,150,168,224})for(boolean missing:new boolean[]{false,true})for(boolean vibra:new boolean[]{false,true}) {
                seed();try{RecordStore.deleteRecordStore("options");}catch(RecordStoreNotFoundException absent){}values[168]=vibra;
                if(broken==-2)table.set(null,null);else if(broken==-1)table.set(null,Arrays.copyOf(values,150));else values[broken]=missing?null:broken<64||broken>=224?Integer.valueOf(0):"wrong";
                api(0,0,true);String answer=call(sound,false);Object[] actual=(Object[])table.get(null);
                row("sound-broken:"+broken+":"+missing+":"+vibra+":"+answer+":"+result+":"+(actual==null||actual.length<=150?"missing":actual[150])+":"+(actual==null||actual.length<=75?"missing":actual[75])+":"+records()+":"+calls());soundCases++;
            }
            for(int at=1;at<=12;at++)for(int fault:new int[]{1,2}){
                seed();try{RecordStore.deleteRecordStore("options");}catch(RecordStoreNotFoundException absent){}values[168]=true;api(at,fault,true);String answer=call(sound,true);
                row("sound-display:"+at+":"+fault+":"+answer+":"+result+":"+values[150]+":"+values[75]+":"+(current.get(canvas)==roster)+":"+records()+":"+calls());soundCases++;
            }
        }
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}scripted=Boolean.parseBoolean(args[3]);
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common emulator=(Common)ef.get(h);ArrayList<String> params=new ArrayList<String>();Collections.addAll(params,"--rms","memory",args[0]);
        emulator.initParams(params,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);emulator.initMIDlet(true);MIDlet host=MIDletBridge.getCurrentMIDlet();loader=host.getClass().getClassLoader();
        options=type("options");table=field(options,"table",Object[].class);values=new Object[256];for(int k=0;k<256;k++)values[k]=k<64||k>=224?"":k<128?Integer.valueOf(0):k<192?Boolean.FALSE:Long.valueOf(0);values[3]="RU";table.set(null,values);
        Class<?> jimm=Class.forName("jimm.Jimm",true,loader);field(jimm,"display",Display.class).set(null,Display.getDisplay(host));field(jimm,"version",String.class).set(null,"0.7.0b");
        if(spec.containsKey("bitmap")){Class<?> bitmap=type("bitmap");field(bitmap,"bitmapField",bitmap).set(null,bitmap.getDeclaredConstructor(String.class).newInstance("/font.prs"));}
        list=type("list");contact=type("contact");group=type("group");vl=type("vl");text=type("text");history=type("history");chat=type("chat");ui=type("ui");canvasType=type("canvas");
        Class<?> runnable=type("runnable");field(runnable,"midlet",MIDlet.class).set(null,host);
        list.getDeclaredConstructor().newInstance();roster=field(list,"treeField",type("tree")).get(null);sentinel=text.getDeclaredConstructor(String.class).newInstance("Previous");canvas=field(vl,"canvasField",canvasType).get(null);
        contacts=field(list,"contacts",Vector.class);last=field(list,"last",contact);receiver=field(ui,"receiver",contact);enter=field(list,"enter",boolean.class);historyTable=field(history,"historyTable",Hashtable.class);current=field(canvasType,"currentControl",vl);chatText=field(chat,"chatText",text);currentUin=field(contact,"currentUin",String.class);built=field(list,"built",boolean.class);
        next=method(list,"next",String.class,boolean.class);unread=method(list,"unread",int.class);items=method(list,"items",java.lang.reflect.Array.newInstance(contact,0).getClass(),group);typing=method(list,"typing",void.class,String.class,boolean.class);sound=method(list,"sound",boolean.class,boolean.class);setInt=method(contact,"setInt",void.class,int.class,int.class);getInt=method(contact,"getInt",int.class,int.class);setBoolean=method(contact,"setBoolean",void.class,int.class,boolean.class);newChat=method(history,"newChat",void.class,contact,String.class);
        for(int i=0;i<persons.length;i++)persons[i]=contact.getDeclaredConstructor(int.class,int.class,String.class,String.class,boolean.class,boolean.class).newInstance(i+1,i%2,"100"+i,"Name "+i,false,false);
        if(scripted){io=Class.forName("RosterStateIO",true,loader);io.getField("monitored").set(null,list);}
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"));data();navigation();typings();modes();out.println("coverage:"+dataCases+":"+navCases+":"+typingCases+":"+soundCases+":"+selectedChats+":"+recordsWritten);out.close();System.out.println("PASS roster state: "+observations+" observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
