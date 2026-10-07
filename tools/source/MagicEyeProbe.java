import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real journal state, tagged text, raster, exact commands and partial failure behavior. */
public class MagicEyeProbe extends TextListProbe {
    static Class<?> spy,vlCallbacks;
    static Field journalCount,journalUins,journalListener;
    static int additions,activations,commands,keys,destinations;
    static String units(Object value){return PasswordProbe.units((String)value);}
    static String result(Method method,Object target,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(target,args));}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof Error&&!t.getClass().getName().equals("MagicEyeIO$FaultError"))throw new AssertionError("Broken journal fixture",t);return "exception:"+t.getClass().getName();}
    }
    static void callCommand(Object target,Object button)throws Exception{commands++;command(target,button);}
    static void fresh(int width,int style,boolean enabled)throws Exception{
        spy.getMethod("reset").invoke(null);journalListener.set(null,eye);f(magic,"a","instance",magic).set(null,eye);callCommand(eye,cmd("r","cmdClearText"));clear();
        bool(172,enabled);bool(132,true);integer(103,0x123456);integer(113,0x654321);integer(118,0);f(options,"g","fontStyle",int.class).setInt(null,style);
        f(vl,"H","forcedWidth",int.class).setInt(eyeList,width);f(vl,"I","forcedHeight",int.class).setInt(eyeList,160);f(vl,"b","dontRepaint",boolean.class).setBoolean(eyeList,false);
        f(list,"a","cItems",Vector.class).set(null,new Vector());((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);f(ui,"a","lastScreen",Object.class).set(null,null);alpha(171,171,128);background(false);
    }
    static String button(Object button)throws Exception{
        if(button==null)return "null";Command c=(Command)button;return units(c.getLabel())+":"+c.getCommandType()+":"+c.getPriority();
    }
    static void state(String label,String outcome)throws Exception{
        Vector uins=(Vector)journalUins.get(eye);StringBuilder b=new StringBuilder(label+":"+outcome+"/count:"+journalCount.getInt(eye)+"/uins:");for(Object uin:uins)b.append(units(uin)).append(';');
        b.append("/locked:").append(f(vl,"b","dontRepaint",boolean.class).getBoolean(eyeList)).append("/size:").append(m(text,"a","getSize",int.class).invoke(eyeList))
            .append("/tag:").append(m(text,"b","getCurrTextIndex",int.class).invoke(eyeList)).append("/selected:").append(f(vl,"b","currItem",int.class).getInt(eyeList)).append("/top:").append(f(vl,"d","topItem",int.class).getInt(eyeList))
            .append("/ui-state:").append(f(vl,"n","uiState",int.class).getInt(eyeList)).append("/last:").append(f(ui,"a","lastScreen",Object.class).get(null)==eyeList)
            .append("/active:").append(current()==eyeList).append("/clipboard:").append(units(get(false))).append(':').append(units(get(true)))
            .append("/left:").append(button(f(vl,"a","leftMenu",Command.class).get(eyeList))).append("/right:").append(button(f(vl,"b","rightMenu",Command.class).get(eyeList)))
            .append("/default:").append(button(f(vl,"c","defaultCommand",Command.class).get(eyeList))).append("/items:");
        for(Object c:(Vector)f(vl,"a","leftMenuItems",Vector.class).get(eyeList))b.append(button(c)).append(';');
        b.append("/listener:").append(f(vl,"a","commandListener",CommandListener.class).get(eyeList)==journalListener.get(null)).append("/callbacks:").append(f(vl,"a","vlCommands",vlCallbacks).get(eyeList)==journalListener.get(null));
        b.append("/text:");for(Object l:(Vector)f(text,"a","lines",Vector.class).get(eyeList)){
            b.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':').append((int)f(line,"a","last_charaster",char.class).getChar(l));
            for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))b.append('{').append(units(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append('}');b.append(']');
        }
        Vector events=(Vector)spy.getField("events").get(null);for(Object event:events){destinations++;Object[] e=(Object[])event;b.append("/destination:").append(e[0]);if(e[1]!=null)b.append(':').append(units(m(contact,"b","getUinString",String.class).invoke(e[1]))).append(':').append(units(f(contact,"a","name",String.class).get(e[1]))).append(':').append(e[1]==contactItem);}events.clear();row(b.toString());
    }
    static void add(String label,String uin,String action,String msg,boolean highlight,int wrapper)throws Exception{
        Method method=wrapper==0?m(magic,"a","registerAction",void.class,String.class,String.class,String.class,boolean.class):wrapper==1?m(magic,"a","addAction",void.class,String.class,String.class,String.class):m(magic,"a","addAction",void.class,String.class,String.class,boolean.class);
        String outcome=wrapper==0?result(method,eye,uin,action,msg,highlight):wrapper==1?result(method,null,uin,action,msg):result(method,null,uin,action,highlight);additions++;state(label,outcome);
    }
    static void activate(String label)throws Exception{activations++;state(label,result(m(magic,"a","activate",void.class),null));}
    static void commandCase(String label,Object target,Object c)throws Exception{commands++;state(label,result(m(magic,"commandAction","commandAction",void.class,Command.class,Displayable.class),target,c,null));}
    static void additions()throws Exception{
        String[] values={null,"","ASCII","\u041f\u0440\u0438\u0432\u0435\u0442 \u010d","a\u0000b\r\nc","\ud83d\ude00\ud800x\udfff"};
        for(boolean enabled:new boolean[]{false,true})for(int style:new int[]{0,1,2,3})for(int width:new int[]{70,176})for(int value=0;value<values.length;value++)for(int wrapper=0;wrapper<3;wrapper++){
            fresh(width,style,enabled);String label="add-"+enabled+'-'+style+'-'+width+'-'+value+'-'+wrapper;add(label,value%2==0?"12345":values[value],value==0?null:value==1?"":value==2?"Y1":"unknown.action",values[value],value%2==0,wrapper);
            activate(label+"-activate");if(enabled&&value>=3&&wrapper==0&&width==176&&style==0){s=eyeList;f(vl,"H","forcedWidth",int.class).setInt(s,width);f(vl,"I","forcedHeight",int.class).setInt(s,160);render(label,s);}
        }
        for(int count:new int[]{Integer.MIN_VALUE,-1,0,1,Integer.MAX_VALUE}){fresh(176,0,true);journalCount.setInt(eye,count);for(int repeat=0;repeat<3;repeat++)add("overflow-"+count+'-'+repeat,"12345","unknown.action","detail\ntext",true,0);}
        fresh(176,0,false);f(list,"a","cItems",Vector.class).set(null,null);add("disabled-corrupt-list",null,null,null,true,0);bool(172,true);add("enabled-corrupt-list",null,null,null,true,0);
    }
    static void commandCases()throws Exception{
        for(int tag:new int[]{-1,0,1,2,3,Integer.MAX_VALUE})for(boolean seeded:new boolean[]{false,true}){
            fresh(90,2,true);add("seed-one","12345","event","one",false,1);add("seed-two","67890","event","two\nlines",false,1);m(text,"a","selectTextByIndex",void.class,int.class).invoke(eyeList,tag);if(seeded)put(true,"***error***",null,"seed",get(true));activate("menu-"+tag+'-'+seeded);
            for(String[] c:new String[][]{{"f","cmdCopyText"},{"g","cmdCopyAppend"},{"h","cmdCopyAll"},{"r","cmdClearText"}})commandCase("command-"+tag+'-'+seeded+'-'+c[1],eye,cmd(c[0],c[1]));
            commandCase("unknown",eye,new Command("unknown",Command.SCREEN,77));commandCase("null",eye,null);
        }
        Object menu=f(magic,"a","cmdContactMenu",Command.class).get(null);
        for(int tag:new int[]{-1,0,1,2,3,Integer.MAX_VALUE})for(int fail=0;fail<3;fail++){
            fresh(176,0,true);add("route-one","12345","event",null,false,1);add("route-two","67890","event",null,false,1);m(text,"a","selectTextByIndex",void.class,int.class).invoke(eyeList,tag);spy.getField("failure").setInt(null,fail);commandCase("contact-"+tag+'-'+fail,eye,menu);
        }
        for(Object uin:new Object[]{null,"","12345","67890","bad",Integer.valueOf(7)}){fresh(176,0,true);add("corrupt-route","12345","event",null,false,1);((Vector)journalUins.get(eye)).setElementAt(uin,0);m(text,"a","selectTextByIndex",void.class,int.class).invoke(eyeList,1);commands++;String outcome=result(m(magic,"commandAction","commandAction",void.class,Command.class,Displayable.class),eye,menu,null);if(uin instanceof String||uin==null)state("route-value-"+units(uin),outcome);else{((Vector)journalUins.get(eye)).setElementAt("restored",0);state("route-wrong-type",outcome);}}
        for(boolean lastIsList:new boolean[]{false,true})for(int fail=0;fail<3;fail++){fresh(176,0,true);f(ui,"a","lastScreen",Object.class).set(null,lastIsList?eyeList:null);f(vl,"n","uiState",int.class).setInt(eyeList,2);spy.getField("failure").setInt(null,fail);commandCase("back-"+lastIsList+'-'+fail,eye,cmd("c","cmdBack"));}
    }
    static void keyCases()throws Exception{
        for(int sender=0;sender<3;sender++)for(int key:new int[]{Integer.MIN_VALUE,-1,0,41,42,43,Integer.MAX_VALUE})for(int type:new int[]{Integer.MIN_VALUE,-1,0,1,2,3,Integer.MAX_VALUE}){
            fresh(176,0,true);add("key-seed","12345","event","body",false,1);put(true,"***error***",null,"seed",get(true));Object source=sender==0?eyeList:sender==1?null:instance(text,new Class<?>[]{String.class},"other");keys++;state("key-"+sender+'-'+key+'-'+type,result(m(magic,"a","vlKeyPress",void.class,vl,int.class,int.class),eye,source,key,type));
        }
        fresh(176,0,true);add("callbacks-seed","12345","event",null,false,1);for(Object source:new Object[]{eyeList,null}){state("click",result(m(magic,"b","vlItemClicked",void.class,vl),eye,source));state("cursor",result(m(magic,"a","vlCursorMoved",void.class,vl),eye,source));}
        Object singleton=eye;Object replacement=instance(magic,new Class<?>[0]);state("constructor-listener","same-singleton:"+(f(magic,"a","instance",magic).get(null)==singleton)+"/listener-replaced:"+(journalListener.get(null)==replacement));activate("new-listener-activate");
        // Clear resets the receiving controller's counter/vector while locking the singleton.
        commandCase("new-controller-clear",replacement,cmd("r","cmdClearText"));state("new-controller-state","count:"+journalCount.getInt(replacement)+"/uins:"+((Vector)journalUins.get(replacement)).size());
    }
    static void exerciseMagic()throws Exception{
        setupFiles();magic=load("ah","jimm.util.MagicEye");eye=f(magic,"a","instance",magic).get(null);eyeList=f(magic,"a","list",text).get(null);spy=load("MagicEyeIO","MagicEyeIO");vlCallbacks=load("bx","DrawControls.VirtualListCommands");
        line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");journalCount=f(magic,"a","counter",int.class);journalUins=f(magic,"a","uins",Vector.class);journalListener=f(magic,"b","_this",magic);
        state("initial","ctor");additions();commandCases();keyCases();if(additions<500||activations<300||commands<500||keys<100||destinations<10||rasters<2)throw new AssertionError("MagicEye coverage guard");row("additions:"+additions+"/activations:"+activations+"/commands:"+commands+"/keys:"+keys+"/destinations:"+destinations+"/rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseMagic();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
