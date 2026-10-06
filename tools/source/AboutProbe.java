import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Differential About text, link selection and version/announcement response handling. */
public class AboutProbe extends ClipboardProbe {
    static Class<?> env,endpoint,task,main,part,line,icon;
    static void about()throws Exception{if(ref)m(ui,"b","about",void.class).invoke(null);else m(ui,"b","about",void.class,Displayable.class).invoke(null,new Object[]{null});}
    static Object screen()throws Exception{return f(ui,"b","aboutTextList",text).get(null);}
    static void resetEnv()throws Exception{env.getMethod("resetAbout").invoke(null);}
    static void notice(String value)throws Exception{((Object[])f(options,"a","options",Object[].class).get(null))[39]=value;}
    static void setVersion(String value)throws Exception{f(ui,"a","version",String.class).set(null,value);}
    static void snapshot(String label)throws Exception{
        Object screen=screen();String result=label+":version="+enc(f(ui,"a","version",String.class).get(null))+":notice="+enc(f(ui,"b","aboutNotice",String.class).get(null))+":stored="+enc(m(options,"a","getString",String.class,int.class).invoke(null,39));
        for(String field:new String[]{"saves","yields","priority","collections"})result+=":"+field+"="+env.getField(field).get(null);
        result+=":trace="+env.getField("log").get(null)+":urls="+env.getField("urls").get(null);
        for(Object t:(Vector)env.getField("tasks").get(null)){Object[] values=(Object[])t;result+=":task="+task.isInstance(values[0])+"/"+values[1];}
        row(result);
        if(screen!=null){row(label+":text="+enc(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(screen,0,true)));
            StringBuilder rows=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(screen)){
                rows.append(f(line,"a","bigTextIndex",int.class).getInt(l)).append('[');
                for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))rows.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append('/').append(f(part,"a","image",icon).get(p)!=null).append(';');rows.append(']');}
            row(label+":rows="+rows);
            row(label+":menu="+((Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen)).size()+":"+(f(vl,"c","defaultCommand",Command.class).get(screen)==cmd("j","cmdSelect"))+":"+f(vl,"u","fontSize",int.class).getInt(screen));
        }
    }
    static void exerciseAbout()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");controller=f(ui,"a","_this",ui).get(null);
        env=load("AboutIO","AboutIO");endpoint=load("TransportIO$Endpoint","TransportIO$Endpoint");task=load("g","jimm.JimmUI$GetVersionInfoTimerTask");main=load("jimm.Jimm","jimm.Jimm");part=load("bc","DrawControls.TextItem");line=load("bm","DrawControls.TextLine");icon=load("e","DrawControls.Icon");
        row("default-notice:"+enc(m(options,"a","getString",String.class,int.class).invoke(null,39)));
        for(int style:new int[]{0,2})for(String notice:new String[]{null,"","12345","123456","News\\n\u041f\u0440\u0438\u0432\u0435\u0442"})for(String version:new String[]{null,"0.7.0b new"}){
            resetEnv();notice(notice);setVersion(version);f(options,"g","fontStyle",int.class).setInt(null,style);integer(112,1);integer(111,2);
            f(main,"d","sonyEricssonPlatform",String.class).set(null,style==0?null:"jp-8");about();String label="about-"+style+"-"+enc(notice)+"-"+enc(version);snapshot(label);
            for(int id:new int[]{1000,1001,1002,-1}){m(text,"a","selectTextByIndex",void.class,int.class).invoke(screen(),id);command(controller,cmd("j","cmdSelect"));row(label+":select-"+id+":"+env.getField("urls").get(null));}
            command(controller,cmd("c","cmdBack"));row(label+":back="+(screen()==null));
        }
        for(int visible=0;visible<3;visible++)for(int size:new int[]{0,5,9,10,11,12,25,35,45})for(int chunk:new int[]{1,4,1024}){
            byte[] bytes=Arrays.copyOf("0123456789\nNotice text \u041f\u0440\u0438\u0432\u0435\u0442".getBytes("UTF-8"),size);
            response("reply-"+visible+"-"+size+"-"+chunk,visible,bytes,size,chunk,200,0,0);
        }
        for(int visible=0;visible<3;visible++){
            for(int fault:new int[]{1,4,5,7,9})response("fault-"+visible+"-"+fault,visible,"0123456789\nnotice".getBytes("UTF-8"),17,4,200,fault,0);
            for(int code:new int[]{201,301,404,500})response("http-"+visible+"-"+code,visible,new byte[0],0,4,code,0,0);
            for(int error:new int[]{1,2,3})response("open-"+visible+"-"+error,visible,new byte[0],0,4,200,0,error);
            for(int size:new int[]{-1,30})response("length-"+visible+"-"+size,visible,"0123456789\nnotice".getBytes("UTF-8"),size,2,200,0,0);
        }
    }
    static void response(String label,int visible,byte[] bytes,long length,int chunk,int code,int fault,int open)throws Exception{
        resetEnv();notice("Old notice");setVersion(null);f(ui,"b","aboutNotice",String.class).set(null,"Old notice");
        if(visible!=0){about();if(visible==2)m(ui,"b","selectScreen",void.class,Object.class).invoke(null,instance(text,new Class<?>[]{String.class},"Other"));}
        else f(ui,"b","aboutTextList",text).set(null,null);
        Object e=env.getMethod("add",byte[].class).invoke(null,(Object)bytes);endpoint.getField("length").setLong(e,length);endpoint.getField("chunk").setInt(e,chunk);endpoint.getField("response").setInt(e,code);endpoint.getField("failure").setInt(e,fault);env.getField("openFailure").setInt(null,open);
        String result=invoke(m(task,"run","run",void.class),instance(task,new Class<?>[0]));row(label+":result="+result+":closed="+endpoint.getField("closed").getInt(e)+":"+endpoint.getField("inputClosed").getInt(e));snapshot(label);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseAbout();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
