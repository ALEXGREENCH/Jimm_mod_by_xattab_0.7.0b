import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Profile editor, information text/styles and save packets against the May release. */
public class ProfileProbe extends ClipboardProbe {
    static Class<?> editor,form,choice,request,save,events,tree,node,formItem;
    static String[][] fields={{"a","_NickNameItem"},{"b","_FirstNameItem"},{"c","_LastNameItem"},{"d","_EmailItem"},{"e","_BdayItem"},{"f","_HomePageItem"},{"g","_AboutItem"},{"h","_CityItem"},{"i","intterestText1"},{"j","intterestText2"},{"k","intterestText3"},{"l","intterestText4"}};
    static int[] indexes={1,38,39,3,13,12,22,4,44,45,46,47};
    static Object current()throws Exception{return m(ui,"a","getCurrentScreen",Object.class).invoke(null);}
    static String commandName(Command c)throws Exception{return c==null?"null":enc(c.getLabel())+"/"+c.getCommandType()+"/"+c.getPriority();}
    static String[] data(int mode)throws Exception{
        String[] data=new String[48];for(int i=44;i<48;i++)data[i]=" ";
        if(mode!=0){for(int i:indexes)data[i]="text-"+i;data[1]=mode==2?"":"Nick";data[3]="x@example.test";data[13]="12.05.1990";
            data[11]=(String)m(load("co","jimm.comm.Util"),"c","genderToString",String.class,int.class).invoke(null,mode==2?2:1);
            for(int i=44;i<48;i++)data[i]=" "+data[i];}
        return data;
    }
    static Object make(String[] data,Object previous)throws Exception{
        int before=actions().size();Object e=instance(editor,new Class<?>[]{String[].class,Object.class},data,previous);row("worker:"+(actions().size()-before)+":"+((Object[])actions().lastElement())[0]);f(editor,"a","userInfo",String[].class).set(e,data);return e;
    }
    static void controls(String label,Object e)throws Exception{
        Object screen=f(editor,"a","form",form).get(e);String result=label+":active="+(current()==screen)+":font="+f(vl,"u","fontSize",int.class).getInt(screen);
        for(String side:new String[]{"a","b"})result+=":"+commandName((Command)f(vl,side,side.equals("a")?"leftMenu":"rightMenu",Command.class).get(screen));
        row(result);
        for(String[] names:fields){TextField tf=(TextField)f(editor,names[0],names[1],TextField.class).get(e);
            row(label+":"+names[1]+":"+(tf==null?"null":enc(tf.getLabel())+":"+enc(tf.getString())+":"+tf.getMaxSize()+":"+tf.getConstraints()));}
        for(String[] names:new String[][]{{"a","_SexItem"},{"b","intterest1"},{"c","intterest2"},{"d","intterest3"},{"e","intterest4"}}){
            ChoiceGroup cg=(ChoiceGroup)f(editor,names[0],names[1],choice).get(e);StringBuilder items=new StringBuilder();
            if(cg!=null){items.append(enc(cg.getLabel())).append(':').append(cg.getSelectedIndex());for(int i=0;i<cg.size();i++)items.append(':').append(enc(cg.getString(i)));}
            row(label+":"+names[1]+":"+(cg==null?"null":items.toString()));
        }
        Object root=f(tree,"a","root",node).get(screen);row(label+":tree="+structure(root));
    }
    static String structure(Object n)throws Exception{
        StringBuilder result=new StringBuilder();Object data=f(node,"a","data",Object.class).get(n);
        if(data instanceof String)result.append("label:").append(enc(data));
        else if(formItem.isInstance(data)){
            Item item=(Item)f(formItem,"a","item",Item.class).get(data);
            result.append(item instanceof TextField?"text":item instanceof ChoiceGroup?"choice":"item").append(':').append(enc(f(formItem,"a","text",String.class).get(data))).append(':').append(f(formItem,"a","choiceIndex",int.class).getInt(data));
        }
        result.append('[');Vector children=(Vector)f(node,"a","items",Vector.class).get(n);if(children!=null)for(Object child:children)result.append(structure(child)).append(';');return result.append(']').toString();
    }
    static Vector actions()throws Exception{return (Vector)events.getField("events").get(null);}
    static void exerciseProfile()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");
        editor=load("br","jimm.EditInfo");form=load("d","DrawControls.VirtualForm");choice=load("j","DrawControls.FormChoiceGroup");request=load("ce","jimm.comm.RequestInfoAction");save=load("as","jimm.comm.SaveInfoAction");events=load("MessageIO","MessageIO");
        tree=load("aw","DrawControls.VirtualTree");node=load("ax","DrawControls.TreeNode");formItem=load("i","DrawControls.FormItem");owner=m(icq,"a","getIcq",icq).invoke(null);
        Object previous=instance(text,new Class<?>[]{String.class},"Profile background");
        for(int font:new int[]{0,1,2})for(int mode=0;mode<3;mode++){
            integer(111,font);int[] categories={0,100,200,300};f(request,"a","indexCategories",int[].class).set(null,categories);
            String[] values=data(mode);Object e=make(values,previous);String label="editor-"+font+"-"+mode;controls(label+"-base",e);
            m(editor,"run","run",void.class).invoke(e);controls(label+"-interests",e);
            actions().clear();command(e,cmd("s","cmdSave"));
            row(label+":data="+enc(Arrays.toString(values))+":categories="+Arrays.toString(categories)+":nick="+enc(f(icq,"a","myNick",String.class).get(null)));
            for(Object event:actions()){Object[] row=(Object[])event;Object[] params=(Object[])row[1];
                if(row[0].equals("action"))ProfileProbe.row(label+":save="+save.isInstance(params[0])+":same="+(f(save,"a","strData",String[].class).get(params[0])==values));
                else ProfileProbe.row(label+":"+row[0]+":"+save.isInstance(params[1])+":"+params[2]);
            }
            command(e,cmd("c","cmdBack"));row(label+":back="+(current()==previous));
        }
        String[] a=data(1),b=data(2);Object first=make(a,previous),second=make(b,previous);
        m(editor,"run","run",void.class).invoke(first);m(editor,"run","run",void.class).invoke(second);
        ((TextField)f(editor,"a","_NickNameItem",TextField.class).get(first)).setString("First");command(first,cmd("s","cmdSave"));
        row("isolation:"+a[1]+":"+b[1]+":"+(f(editor,"b","intterest1",choice).get(first)!=f(editor,"b","intterest1",choice).get(second)));
        info();infoCommands();savePackets();saveReplies();
    }
    static void info()throws Exception{
        controller=f(ui,"a","_this",ui).get(null);Class<?> part=load("bc","DrawControls.TextItem"),line=load("bm","DrawControls.TextLine");
        for(int style:new int[]{0,2})for(int mode=0;mode<4;mode++){
            f(options,"g","fontStyle",int.class).setInt(null,style);f(ui,"b","uiBigTextIndex",int.class).setInt(null,0);
            String[] data=new String[48];if(mode!=0){for(int i=0;i<48;i++)data[i]="field-"+i;data[25]=mode==1?"0":mode==2?"1":"2";data[24]=mode==1?"1":"0";}
            Object screen=m(ui,"a","getInfoTextList",text,String.class,boolean.class).invoke(null,"Information",true);
            m(ui,"a","fillUserInfo",void.class,String[].class,text).invoke(null,data,screen);
            row("info-"+style+"-"+mode+":"+enc(m(text,"a","getCurrText",String.class,int.class,boolean.class).invoke(screen,0,true)));
            StringBuilder formats=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(screen))for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))formats.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(';');
            row("info-formats-"+style+"-"+mode+":"+formats);
        }
    }
    static void infoCommands()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int identity=0;identity<3;identity++){
            reset(false);string(0,"12345");f(icq,"b","connected",boolean.class).setBoolean(null,connected);actions().clear();
            String own=(String)m(options,"a","getString",String.class,int.class).invoke(null,254);
            String uin=identity==0?own:identity==1?new String(own):"67890";
            m(ui,"a","requiestUserInfo",void.class,String.class,String.class).invoke(null,uin,"Known");
            Object screen=f(ui,"c","infoTextList",text).get(null);Vector menu=(Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen);
            String label="request-"+connected+"-"+identity;
            row(label+":active="+(current()==screen)+":edit="+menu.contains(cmd("u","cmdEdit"))+":update="+menu.contains(cmd("y","cmdUpdateNick"))+":actions="+actions().size());
            String[] info=data(1);info[37]=uin;m(ui,"a","showUserInfo",void.class,String[].class).invoke(null,(Object)info);
            command(controller,cmd("u","cmdEdit"));Object editScreen=current();Object e=f(vl,"a","commandListener",CommandListener.class).get(editScreen);
            row(label+":editor="+editor.isInstance(e)+":previous="+(f(editor,"a","_PreviousForm",Object.class).get(e)==screen)+":same="+(f(editor,"a","userInfo",String[].class).get(e)==info));
            m(editor,"run","run",void.class).invoke(e);command(e,cmd("c","cmdBack"));row(label+":back="+(current()==screen));
            Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,uin,"Known",false,true);
            ((Vector)f(list,"a","cItems",Vector.class).get(null)).add(c);f(ui,"b","clciContactMenu",contact).set(null,c);actions().clear();
            command(controller,cmd("y","cmdUpdateNick"));row(label+":renamed="+enc(f(contact,"a","name",String.class).get(c))+":actions="+actions().size());
        }
    }
    static byte[] le(int v){return new byte[]{(byte)v,(byte)(v>>8)};}
    static Object meta(int sub,byte[] data)throws Exception{return snac(21,3,0,tlv(1,join(le(data.length+8),new byte[4],le(sub),new byte[2],data)));}
    static void saveReplies()throws Exception{
        for(int sub:new int[]{2010,65,99})for(int type:new int[]{3135,3130,0})for(int flag:new int[]{0,10,255})for(int length:new int[]{0,1,2,3}){
            Object a=instance(save,new Class<?>[]{String[].class},(Object)data(1));f(save,"a","init",Date.class).set(a,new Date(100000L));io.getField("now").setLong(null,105000L);
            byte[] body=Arrays.copyOf(join(le(type),new byte[]{(byte)flag}),length);
            String result=invoke(m(save,"a","forward",boolean.class,packet),a,meta(sub,body));
            row("save-ack-"+sub+"-"+type+"-"+flag+"-"+length+":"+result+":"+f(save,"a","packetCounter",int.class).getInt(a)+":"+f(save,"b","errorCounter",int.class).getInt(a)+":"+m(save,"a","isCompleted",boolean.class).invoke(a)+":"+m(save,"a","getProgress",int.class).invoke(a)+":"+m(save,"b","isError",boolean.class).invoke(a));
            io.getField("now").setLong(null,105001L);row("save-timeout:"+m(save,"b","isError",boolean.class).invoke(a));
        }
    }
    static void savePackets()throws Exception{
        for(String date:new String[]{null,"","12.05.1990","1.2.3","bad.date.text"})for(int mode=0;mode<3;mode++){
            reset(false);string(0,"12345");String[] data=data(mode);data[13]=date;Object a=instance(save,new Class<?>[]{String[].class},(Object)data);
            String result=invoke(m(save,"a","init",void.class),a);row("save-init-"+date+"-"+mode+":"+result);
            for(Object p:(Vector)io.getField("packets").get(null))row("save-packet:"+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));
            row("save-date:"+f(save,"a","init",Date.class).get(a));
        }
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseProfile();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
