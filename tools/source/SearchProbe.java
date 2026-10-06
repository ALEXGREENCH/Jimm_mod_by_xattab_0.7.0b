import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** May search controls, result navigation, group selection and wire/state transitions. */
public class SearchProbe extends ProfileProbe {
    static Class<?> searchClass,searchFormClass,searchAction,group;
    static String[][] searchFields={{"a","uinSearchTextBox"},{"b","nickSearchTextBox"},{"c","firstnameSearchTextBox"},{"d","lastnameSearchTextBox"},{"e","emailSearchTextBox"},{"f","citySearchTextBox"},{"g","keywordSearchTextBox"}};
    static Object search(boolean lite)throws Exception{return instance(searchClass,new Class<?>[]{boolean.class},lite);}
    static Object form(Object s)throws Exception{return m(searchClass,"a","getSearchForm",searchFormClass).invoke(s);}
    static Object query(Object sf)throws Exception{return f(searchFormClass,"a","searchForm",form).get(sf);}
    static Object result(Object sf)throws Exception{return f(searchFormClass,"a","screen",text).get(sf);}
    static Vector results(Object s)throws Exception{return (Vector)f(searchClass,"a","results",Vector.class).get(s);}
    static Object action(Object s,String[] data)throws Exception{return instance(searchAction,new Class<?>[]{searchClass,String[].class},s,data);}
    static Object command(Object sf,String old,String name)throws Exception{return f(searchFormClass,old,name,Command.class).get(sf);}
    static void activate(Object sf,int type)throws Exception{m(searchFormClass,"a","activate",void.class,int.class).invoke(sf,type);}
    static String menu(Object screen)throws Exception{
        StringBuilder b=new StringBuilder();for(String side:new String[]{"a","b"})b.append(commandName((Command)f(vl,side,side.equals("a")?"leftMenu":"rightMenu",Command.class).get(screen))).append(';');
        for(Object c:(Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen))b.append(commandName((Command)c)).append(';');
        return b.toString();
    }
    static void screen(String label,Object screen)throws Exception{
        row(label+":active="+(current()==screen)+":caption="+enc(f(vl,"a","caption",String.class).get(screen))+":font="+f(vl,"u","fontSize",int.class).getInt(screen)+":menu="+menu(screen));
        if(form.isInstance(screen))row(label+":tree="+structure(f(tree,"a","root",node).get(screen)));
        else if(text.isInstance(screen))row(label+":text="+enc(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(screen,0,true)));
    }
    static void choice(String label,Object sf,String old,String name)throws Exception{
        ChoiceGroup c=(ChoiceGroup)f(searchFormClass,old,name,choice).get(sf);StringBuilder b=new StringBuilder();
        b.append(enc(c.getLabel())).append(':').append(c.getSelectedIndex());for(int i=0;i<c.size();i++)b.append(':').append(enc(c.getString(i)));
        row(label+":"+name+":"+b);
    }
    static String[] resultData(int n){String[] d=new String[48];d[37]=""+(12345+n);d[1]="Nick "+n;d[2]="Name "+n;d[3]="mail@example.test";d[24]=""+(n%2);d[25]=""+(n%3);d[10]="25";return d;}
    static void forms()throws Exception{
        for(boolean lite:new boolean[]{false,true})for(int font:new int[]{0,1,2}){
            integer(111,font);Object s=search(lite),sf=form(s);String label="form-"+lite+"-"+font;
            row(label+":cached="+(form(s)==sf));screen(label+"-query",query(sf));screen(label+"-result",result(sf));
            for(String[] names:searchFields){TextField tf=(TextField)f(searchFormClass,names[0],names[1],TextField.class).get(sf);row(label+":"+names[1]+":"+enc(tf.getLabel())+":"+tf.getMaxSize()+":"+tf.getConstraints());}
            choice(label,sf,"b","chgrAge");choice(label,sf,"c","gender");choice(label,sf,"d","onlyOnline");
            activate(sf,2);screen(label+"-active",query(sf));activate(sf,3);
            Object alert=current();row(label+":no-results="+popup.isInstance(alert)+":"+(f(popup,"a","previousScreen",Object.class).get(alert)==query(sf))+":"+enc(f(popup,"a","text",String.class).get(alert)));
            command(alert,cmd("c","cmdBack"));row(label+":dismiss="+(current()==query(sf)));
            for(int count:new int[]{0,1,3}){
                results(s).clear();for(int i=0;i<count;i++)results(s).add(resultData(i));f(searchFormClass,"a","selectedIndex",int.class).setInt(sf,0);activate(sf,1);screen(label+"-results-"+count,result(sf));
                if(count>1)for(boolean next:new boolean[]{true,true,true,false,false,false}){m(searchFormClass,"a","nextOrPrev",void.class,boolean.class).invoke(sf,next);row(label+":navigate="+f(searchFormClass,"a","selectedIndex",int.class).getInt(sf));}
            }
            // Search submits exactly the current controls and clears the old results.
            activate(sf,2);String[] values={"12345","Nick","First","Last","mail@example.test","City","word"};
            for(int i=0;i<values.length;i++)((TextField)f(searchFormClass,searchFields[i][0],searchFields[i][1],TextField.class).get(sf)).setString(values[i]);
            ((ChoiceGroup)f(searchFormClass,"b","chgrAge",choice).get(sf)).setSelectedIndex(3,true);((ChoiceGroup)f(searchFormClass,"c","gender",choice).get(sf)).setSelectedIndex(2,true);((ChoiceGroup)f(searchFormClass,"d","onlyOnline",choice).get(sf)).setSelectedIndex(0,true);
            actions().clear();command(sf,command(sf,"a","searchCommand"));row(label+":cleared="+results(s).size());events(label);
            results(s).add(resultData(0));results(s).add(resultData(1));activate(sf,1);
            for(String[] direction:new String[][]{{"p","nextCmd"},{"q","prevCmd"}}){command(sf,cmd(direction[0],direction[1]));row(label+":command-"+direction[1]+":"+f(searchFormClass,"a","selectedIndex",int.class).getInt(sf));}
            for(int event:new int[]{0,1,2,3})for(int key:new int[]{Canvas.KEY_NUM4,Canvas.KEY_NUM6}){
                key(sf,result(sf),key,event);row(label+":key-"+event+"-"+key+":"+f(searchFormClass,"a","selectedIndex",int.class).getInt(sf));
            }
        }
    }
    static void events(String label)throws Exception{
        for(Object e:actions()){
            Object[] event=(Object[])e,params=(Object[])event[1];String s=label+":"+event[0];
            if(event[0].equals("action"))s+=":"+enc(Arrays.toString((String[])f(searchAction,"a","search",String[].class).get(params[0])));
            else if(event[0].equals("search-timer"))s+=":"+searchAction.isInstance(params[1])+":"+params[2];
            else if(event[0].equals("search-add")||event[0].equals("search-write")){
                Object c=params[0];s+=":"+m(contact,"b","getUinString",String.class).invoke(c)+":"+enc(f(contact,"a","name",String.class).get(c));
                for(int k:new int[]{64,65,192})s+=":"+m(contact,"b","getIntValue",int.class,int.class).invoke(c,k);
                for(int k:new int[]{2,8})s+=":"+m(contact,"a","getBooleanValue",boolean.class,int.class).invoke(c,k);
            }else if(event[0].equals("search-info"))s+=":"+Arrays.toString(params);
            else if(event[0].equals("search-error"))s+=":"+f(error,"a","_ErrCode",int.class).getInt(params[0]);
            row(s);
        }
    }
    static void selection()throws Exception{
        for(boolean lite:new boolean[]{false,true})for(int count:new int[]{0,1,3}){
            reset(false);string(0,"54321");Object s=search(lite),sf=form(s);results(s).add(resultData(1));activate(sf,1);
            Vector groups=(Vector)f(list,"b","gItems",Vector.class).get(null);for(int i=0;i<count;i++)groups.add(instance(group,new Class<?>[]{int.class,String.class},10+i,"Group "+i));
            actions().clear();command(sf,command(sf,"b","addCommand"));String label="groups-"+lite+"-"+count;
            Object groupScreen=current();row(label+":canvas="+form.isInstance(groupScreen));
            if(count>0){
                screen(label,groupScreen);ChoiceGroup choices=(ChoiceGroup)f(searchFormClass,"a","groupList",choice).get(sf);row(label+":choices="+choices.size());choices.setSelectedIndex(count-1,true);
                command(sf,cmd("c","cmdBack"));row(label+":back="+(current()==result(sf))+":query-retained="+(query(sf)!=null));
                command(sf,command(sf,"b","addCommand"));((ChoiceGroup)f(searchFormClass,"a","groupList",choice).get(sf)).setSelectedIndex(count-1,true);command(sf,command(sf,"b","addCommand"));events(label);row(label+":query-null="+(query(sf)==null));
            }
            s=search(lite);sf=form(s);results(s).add(resultData(2));activate(sf,1);actions().clear();command(sf,command(sf,"c","cmdSendMessage"));events(label+"-message");
            activate(sf,1);actions().clear();command(sf,command(sf,"d","cmdShowInfo"));events(label+"-info");
            activate(sf,1);command(sf,cmd("c","cmdBack"));row(label+":back-result="+(current()==query(sf)));
        }
    }
    static void snapshot(String label,Object a,Object s,String outcome)throws Exception{
        row(label+":"+outcome+":state="+f(searchAction,"a","state",int.class).getInt(a)+":time="+f(searchAction,"a","lastActivity",long.class).getLong(a)+":complete="+m(searchAction,"a","isCompleted",boolean.class).invoke(a)+":error="+m(searchAction,"b","isError",boolean.class).invoke(a));
        for(Object data:results(s))row(label+":result="+enc(Arrays.toString((String[])data)));
    }
    static void requests()throws Exception{
        String[][] cases=new String[44][10];int index=0;
        for(int field=0;field<10;field++)for(String value:new String[]{"","1","\u041f\u0440\u0438\u0432\u0435\u0442","-1"}){cases[index][field]=value;index++;}
        cases[index++]=new String[]{"4294967295","Nick","First","Last","mail@example.test","City","word","2","1","7"};
        cases[index++]=new String[]{"12345","Nick","First","Last","mail@example.test","City","word","2","1","7"};
        cases[index++]=new String[]{null,null,null,null,null,null,null,null,null,"8"};
        cases[index++]=new String[]{null,null,null,null,null,null,null,null,null,"-2"};
        for(int i=0;i<cases.length;i++){
            reset(false);string(0,"54321");Object s=search(false),a=action(s,cases[i]);String outcome=invoke(m(searchAction,"a","init",void.class),a);snapshot("request-"+i,a,s,outcome);
            row("request-data:"+enc(Arrays.toString(cases[i])));for(Object p:(Vector)io.getField("packets").get(null))row("request-packet:"+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));
        }
    }
    static byte[] reply(int marker,int auth,int status,int gender)throws Exception{
        byte[] b=join(word(marker),new byte[]{10,0,0,57,48,0,0});
        for(String value:new String[]{"Nick","First","Last","mail@example.test"}){byte[] v=(value+"\0").getBytes("UTF-8");b=join(b,le(v.length),v);}
        return join(b,new byte[]{(byte)auth},le(status),new byte[]{(byte)gender},le(25));
    }
    static void replies()throws Exception{
        for(int state:new int[]{-1,0,1,4,5,6,7})for(int marker:new int[]{0xa401,0xae01,0})for(int auth:new int[]{0,1})for(int gender:new int[]{0,1,2}){
            Object s=search(false),a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,state);String label="reply-"+state+"-"+marker+"-"+auth+"-"+gender;
            snapshot(label,a,s,invoke(m(searchAction,"a","forward",boolean.class,packet),a,meta(2010,reply(marker,auth,gender,gender))));
        }
        byte[] valid=reply(0xae01,0,1,1);
        for(int length=0;length<=valid.length;length++){
            Object s=search(false),a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,1);
            snapshot("truncated-"+length,a,s,invoke(m(searchAction,"a","forward",boolean.class,packet),a,meta(2010,Arrays.copyOf(valid,length))));
        }
        for(int flag:new int[]{0,1,10,255})for(int sub:new int[]{2010,65,99}){
            Object s=search(false),a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,1);byte[] b=valid.clone();b[2]=(byte)flag;
            snapshot("reply-flag-"+flag+"-"+sub,a,s,invoke(m(searchAction,"a","forward",boolean.class,packet),a,meta(sub,b)));
        }
        Object s=search(false),a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,1);
        for(int marker:new int[]{0xa401,0xa401,0xae01,0xa401})snapshot("reply-sequence-"+marker,a,s,invoke(m(searchAction,"a","forward",boolean.class,packet),a,meta(2010,reply(marker,0,1,2))));
        for(int state=-1;state<=7;state++)for(int age:new int[]{0,59999,60000,60001}){
            io.getField("now").setLong(null,100000L);s=search(false);a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,state);io.getField("now").setLong(null,100000L+age);
            snapshot("timeout-"+state+"-"+age,a,s,"check");
            snapshot("unrelated-"+state+"-"+age,a,s,invoke(m(searchAction,"a","forward",boolean.class,packet),a,snac(9,99,0,empty)));
        }
        for(int event:new int[]{1,2,3,99})for(int state:new int[]{1,4,6}){
            s=search(false);a=action(s,new String[10]);f(searchAction,"a","state",int.class).setInt(a,state);actions().clear();m(searchAction,"a","onEvent",void.class,int.class).invoke(a,event);
            Object sf=form(s);row("event-"+event+"-"+state+":"+f(searchAction,"a","state",int.class).getInt(a)+":query="+(current()==query(sf))+":result="+(current()==result(sf))+":popup="+popup.isInstance(current()));events("event");
        }
    }
    static void exerciseSearch()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");form=load("d","DrawControls.VirtualForm");choice=load("j","DrawControls.FormChoiceGroup");
        tree=load("aw","DrawControls.VirtualTree");node=load("ax","DrawControls.TreeNode");formItem=load("i","DrawControls.FormItem");popup=load("ci","DrawControls.VirtualAlert");events=load("MessageIO","MessageIO");
        searchClass=load("cp","jimm.Search");searchFormClass=load("cr","jimm.Search$SearchForm");searchAction=load("o","jimm.comm.SearchAction");group=load("bb","jimm.GroupItem");
        owner=m(icq,"a","getIcq",icq).invoke(null);reset(false);string(0,"54321");controller=f(ui,"a","_this",ui).get(null);
        forms();selection();requests();replies();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseSearch();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
