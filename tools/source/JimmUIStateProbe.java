import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Actual controller selectors, status tables, cached info list and formatter state. */
public class JimmUIStateProbe extends JimmUrlProbe {
    static int selectors, statusCalls, infoCalls, formatCalls;
    static Object background;
    static Vector callbacks=new Vector();
    static Method tag, lastIndex;
    static String result(Method m,Object receiver,Object...args)throws Exception{return actual(m,receiver,args);}
    static void view(String label,Object screen)throws Exception {
        row(label+":exists="+(screen!=null));if(screen==null)return;
        row(label+":caption="+utf(f(vl,"a","caption",String.class).get(screen))+":font="+f(vl,"u","fontSize",int.class).getInt(screen)
            +":text="+utf(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(screen,0,true)));
        menus(label,screen);
        StringBuilder values=new StringBuilder();
        for(Object l:(Vector)f(text,"a","lines",Vector.class).get(screen)) {
            values.append(f(line,"a","bigTextIndex",int.class).getInt(l)).append('[');
            for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))values.append(utf(f(part,"a","text",String.class).get(p))).append('@')
                .append(f(part,"a","fontAndColor",int.class).getInt(p)).append('/').append(f(part,"a","image",icon).get(p)!=null).append(';');
            values.append(']');
        }
        row(label+":parts="+values);
    }
    static void selectorState(String label)throws Exception {
        Object selected=f(ui,"d","lstSelector",text).get(null);
        row(label+":tag="+f(ui,"a","curScreenTag",int.class).getInt(null)+":active-tag="+tag.invoke(null)
            +":last="+lastIndex.invoke(null)+":listener="+(f(ui,"a","listener",CommandListener.class).get(null)!=null)+":callbacks="+callbacks);
        view(label,selected);
    }
    static void selection()throws Exception {
        final CommandListener callback=new CommandListener(){public void commandAction(Command c,Displayable d){callbacks.addElement(utf(c.getLabel())+"/"+c.getCommandType()+"/"+c.getPriority()+"/"+(d==null));}};
        Method show=m(ui,"a","showSelector",void.class,String.class,String[].class,CommandListener.class,int.class,boolean.class);
        Method handler=m(ui,"commandAction","commandAction",void.class,Command.class,Displayable.class);
        for(int font:new int[]{0,1,2})for(int style:new int[]{0,2})for(boolean translate:new boolean[]{false,true})
            for(String[] data:new String[][]{null,new String[0],{"8"},{"8","3"},{null,"missing"},{"x","x"}})
            for(String title:new String[]{null,"8","missing"})for(int value:new int[]{-1,0,5,Integer.MIN_VALUE}) {
                String label="selector-"+selectors++;callbacks.clear();integer(88,font);f(options,"g","fontStyle",int.class).setInt(null,style);
                m(ui,"g","resetLstSelector",void.class).invoke(null);f(ui,"b","clciContactMenu",contact).set(null,null);
                f(ui,"c","lastSelectedItemIndex",int.class).setInt(null,-17);
                m(ui,"b","selectScreen",void.class,Object.class).invoke(null,background);
                String[] entries=data==null?null:data.clone();row(label+":open="+result(show,null,title,entries,callback,value,translate));
                row(label+":entries="+utf(Arrays.toString(entries)));selectorState(label);
                Object selected=f(ui,"d","lstSelector",text).get(null);
                if(selected!=null)m(text,"a","selectTextByIndex",void.class,int.class).invoke(selected,1);
                row(label+":ok="+result(handler,controller,cmd("a","cmdOk"),null));selectorState(label+"-ok");
                row(label+":cancel="+result(handler,controller,cmd("b","cmdCancel"),null));selectorState(label+"-cancel");
            }
    }
    static void statuses()throws Exception {
        long[] values=(long[])f(ui,"a","statuses",long[].class).get(null);row("statuses:"+Arrays.toString(values));
        row("images:"+Arrays.toString((int[])f(ui,"b","imageIndexes",int[].class).get(null)));
        row("status-keys:"+Arrays.toString((String[])f(ui,"c","statusStrings",String[].class).get(null)));
        row("std-selector:"+Arrays.toString((String[])f(ui,"a","stdSelector",String[].class).get(null)));
        ArrayList<Long> tests=new ArrayList<Long>();for(long v:values){tests.add(v);tests.add(v-1);tests.add(v+1);}
        for(long v:new long[]{-1,0,12345,Long.MIN_VALUE,Long.MAX_VALUE})tests.add(v);
        for(long value:tests){row("status-"+statusCalls+++":"+value+":"+result(m(ui,"b","getStatusIndex",int.class,long.class),null,value)
            +":"+result(m(ui,"a","getStatusImageIndex",int.class,long.class),null,value)+":"+utf(result(m(ui,"a","getStatusString",String.class,long.class),null,value)));}
    }
    static void information()throws Exception {
        Method info=m(ui,"a","getInfoTextList",text,String.class,boolean.class);
        for(boolean seed:new boolean[]{false,true})for(boolean commands:new boolean[]{false,true})for(String title:new String[]{null,"","Name","\u043f\u010d"}) {
            clear();if(seed)put(true,"date","name","body",get(true));String label="info-"+infoCalls++;
            Object first=info.invoke(null,title,commands);view(label,first);
            Object second=info.invoke(null,title,commands);row(label+":new-instance="+(first!=second)+":cached="+(f(ui,"c","infoTextList",text).get(null)==second));
        }
        for(int style:new int[]{0,2,3})for(String section:new String[]{null,"8","missing"})for(String labelKey:new String[]{null,"8","missing"})
            for(String value:new String[]{null,"","x","\u043f\u010d\nlong"}) {
                Object screen=instance(text,new Class<?>[]{String.class},"Info");String label="format-"+formatCalls++;
                f(options,"g","fontStyle",int.class).setInt(null,style);f(ui,"b","uiBigTextIndex",int.class).setInt(null,7);f(ui,"g","uiSectName",String.class).set(null,section);
                row(label+":add="+result(m(ui,"a","addToTextList",void.class,String.class,String.class,text),null,value,labelKey,screen));
                row(label+":state="+f(ui,"b","uiBigTextIndex",int.class).getInt(null)+":"+utf(f(ui,"g","uiSectName",String.class).get(null)));view(label,screen);
                String[] data={value};row(label+":indexed="+result(m(ui,"a","addToTextList",void.class,int.class,String[].class,String.class,text),null,0,data,labelKey,screen));
                row(label+":indexed-state="+f(ui,"b","uiBigTextIndex",int.class).getInt(null)+":"+utf(f(ui,"g","uiSectName",String.class).get(null)));view(label+"-indexed",screen);
            }
    }
    static void exerciseState()throws Exception {
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");
        line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");controller=f(ui,"a","_this",ui).get(null);
        tag=m(ui,"a","getCurScreenTag",int.class);lastIndex=m(ui,"b","getLastSelIndex",int.class);
        background=instance(text,new Class<?>[]{String.class},"Background");
        statuses();selection();information();
        if(selectors!=864||statusCalls!=47||infoCalls!=16||formatCalls!=108)throw new AssertionError("Missing controller state cases");
        row("coverage:"+selectors+":"+statusCalls+":"+infoCalls+":"+formatCalls);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseState();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
