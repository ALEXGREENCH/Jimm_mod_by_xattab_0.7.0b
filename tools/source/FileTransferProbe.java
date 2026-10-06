import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** May transfer form/lifecycle, HTTP multipart bytes, scripted file selection and browser routes. */
public class FileTransferProbe extends ProfileProbe {
    static Class<?> ft,browser,env,endpoint,traffic,resource,send,fileMessage;
    static Object contactItem;
    static void resetEnv()throws Exception{env.getMethod("resetFiles").invoke(null);actions().clear();f(traffic,"g","sessionOutTraffic",int.class).setInt(null,0);}
    static Object make(boolean withContact)throws Exception{return instance(ft,new Class<?>[]{int.class,contact},1,withContact?contactItem:null);}
    static Object form(Object transfer)throws Exception{return f(ft,"a","name_Desc",form).get(transfer);}
    static void data(Object transfer,InputStream input,int size)throws Exception{m(ft,"a","setData",void.class,InputStream.class,int.class).invoke(transfer,input,size);}
    static void ask(Object transfer,String name,String description)throws Exception{m(ft,"a","askForNameDesc",void.class,String.class,String.class).invoke(transfer,name,description);}
    static String menu(Object screen)throws Exception{return commandName((Command)f(vl,"a","leftMenu",Command.class).get(screen))+":"+commandName((Command)f(vl,"b","rightMenu",Command.class).get(screen));}
    static void snapshot(String label,Object transfer)throws Exception{
        Object screen=form(transfer);String s=label+":mode="+f(ft,"a","curMode",int.class).getInt(transfer)+":size="+f(ft,"b","fsize",int.class).getInt(transfer)+":form="+(screen!=null)+":input="+(f(ft,"a","fis",InputStream.class).get(transfer)!=null);
        for(String[] names:new String[][]{{"a","fileNameField"},{"b","descriptionField"}}){TextField value=(TextField)f(ft,names[0],names[1],TextField.class).get(transfer);s+=":"+(value==null?"null":enc(value.getLabel())+"="+enc(value.getString())+"/"+value.getMaxSize());}
        ChoiceGroup mode=(ChoiceGroup)f(ft,"a","webTransfer",choice).get(transfer);s+=":choice="+(mode==null?"null":mode.isSelected(0)+":"+enc(mode.getString(0)));
        for(String name:new String[]{"starts","saves","collections"})s+=":"+name+"="+env.getField(name).get(null);
        s+=":configured="+m(options,"a","getInt",int.class,int.class).invoke(null,97);s+=":enter="+f(list,"b","enterContactMenu",boolean.class).getBoolean(null);row(s);
        if(screen!=null){row(label+":screen="+(current()==screen)+":"+f(vl,"u","fontSize",int.class).getInt(screen)+":"+menu(screen));row(label+":tree="+structure(f(tree,"a","root",node).get(screen)));}
    }
    static void events(String label)throws Exception{
        for(Object e:actions()){
            Object[] event=(Object[])e,params=(Object[])event[1];String s=label+":"+event[0];
            if(event[0].equals("file-send"))s+=":"+enc(params[0])+":"+(params[1]==contactItem);
            else if(event[0].equals("file-error"))s+=":"+f(error,"a","_ErrCode",int.class).getInt(params[0])+":"+enc(((Throwable)params[0]).getMessage());
            else if(event[0].equals("file-contact"))s+=":"+(params[0]==contactItem);
            else if(event[0].equals("file-background"))s+=":"+(params[0]!=null)+":"+params[1];
            else if(event[0].equals("action")){
                Object msg=f(send,"a","fileTrans",fileMessage).get(params[0]);s+=":"+enc(f(fileMessage,"a","filename",String.class).get(msg))+":"+enc(f(fileMessage,"b","description",String.class).get(msg))+":"+f(fileMessage,"a","fsize",int.class).getInt(msg)+":"+(f(fileMessage,"a","fis",InputStream.class).get(msg)!=null);
            }
            row(s);
        }
    }
    static void forms()throws Exception{
        for(int font:new int[]{0,1,2})for(int mode:new int[]{0,1})for(int size:new int[]{0,1023,1024,3073}){
            resetEnv();integer(111,font);integer(97,mode);integer(72,2048);integer(70,1500);string(6,"RUB");Object transfer=make(true);data(transfer,new ByteArrayInputStream(new byte[size]),size);ask(transfer,"/card/test.txt","Description");
            String label="form-"+font+"-"+mode+"-"+size;snapshot(label,transfer);
            ChoiceGroup choices=(ChoiceGroup)f(ft,"a","webTransfer",choice).get(transfer);choices.setSelectedIndex(0,mode!=0);m(ft,"itemStateChanged","itemStateChanged",void.class,Item.class).invoke(transfer,choices);snapshot(label+"-toggle",transfer);
            m(ft,"itemStateChanged","itemStateChanged",void.class,Item.class).invoke(transfer,new StringItem("other",null));row(label+":ignored="+env.getField("saves").getInt(null));
            integer(97,mode);f(list,"b","enterContactMenu",boolean.class).setBoolean(null,true);actions().clear();command(transfer,cmd("k","cmdSend"));snapshot(label+"-send",transfer);events(label);
            Class<?> splash=load("cv","jimm.SplashCanvas");Object splashScreen=f(splash,"a","_this",splash).get(null);
            row(label+":cancel="+(f(vl,"b","rightMenu",Command.class).get(splashScreen)==f(splash,"c","cancelCommnad",Command.class).get(null)));
            row(label+":progress="+env.getField("progress").get(null)+":messages="+enc(env.getField("messages").get(null)));
            actions().clear();command(transfer,cmd("c","cmdBack"));snapshot(label+"-back",transfer);events(label);
        }
    }
    static void http()throws Exception{
        String[] replies={"http://files.invalid/a\r\n","prefix http://files.invalid/a\r\n\r\n","https://files.invalid/a","","error"};
        for(String language:new String[]{"RU","EN"})for(int size:new int[]{0,1,1025,2048})for(int variant=0;variant<replies.length;variant++){
            resetEnv();f(resource,"a","langAvailable",String[].class).set(null,new String[]{language});Object transfer=make(true);byte[] bytes=new byte[size];for(int i=0;i<size;i++)bytes[i]=(byte)i;data(transfer,new ByteArrayInputStream(bytes),size);
            f(ft,"c","shortFileName",String.class).set(transfer,"test \u0444.txt");f(ft,"a","curMode",int.class).setInt(transfer,10001);
            Object e=env.getMethod("add",byte[].class).invoke(null,(Object)replies[variant].getBytes("UTF-8"));httpStep("http-"+language+"-"+size+"-"+variant,transfer,e);
        }
        f(resource,"a","langAvailable",String[].class).set(null,new String[]{"RU"});
        for(int failure=0;failure<=9;failure++)for(int response:new int[]{200,404,500}){
            resetEnv();Object transfer=make(true);data(transfer,new ByteArrayInputStream(new byte[]{1,2,3}),3);f(ft,"c","shortFileName",String.class).set(transfer,"test.txt");f(ft,"a","curMode",int.class).setInt(transfer,10001);
            Object e=env.getMethod("add",byte[].class).invoke(null,(Object)"http://files.invalid/a".getBytes("UTF-8"));endpoint.getField("failure").setInt(e,failure);endpoint.getField("response").setInt(e,response);httpStep("http-failure-"+failure+"-"+response,transfer,e);
        }
        for(int failure:new int[]{1,2,3}){
            resetEnv();Object transfer=make(true);data(transfer,new ByteArrayInputStream(new byte[]{1}),1);f(ft,"a","curMode",int.class).setInt(transfer,10001);env.getField("openFailure").setInt(null,failure);httpStep("http-open-"+failure,transfer,null);
        }
    }
    static void httpStep(String label,Object transfer,Object e)throws Exception{
        String outcome=invoke(m(ft,"run","run",void.class),transfer);row(label+":"+outcome+":mode="+f(ft,"a","curMode",int.class).getInt(transfer)+":error="+enc(f(ft,"a","exceptionText",String.class).get(transfer))+":serial="+((Vector)env.getField("serials").get(null)).size()+":traffic="+f(traffic,"g","sessionOutTraffic",int.class).getInt(null));
        row(label+":trace="+env.getField("log").get(null)+":progress="+env.getField("progress").get(null)+":messages="+enc(env.getField("messages").get(null)));
        if(e!=null)row(label+":body="+b64(((ByteArrayOutputStream)endpoint.getField("sent").get(e)).toByteArray())+":closed="+endpoint.getField("inputClosed").get(e)+":"+endpoint.getField("outputClosed").get(e)+":"+endpoint.getField("closed").get(e));events(label);
        if(!((Vector)env.getField("serials").get(null)).isEmpty()){
            actions().clear();m(ft,"run","run",void.class).invoke(transfer);Alert alert=(Alert)f(ft,"a","alert",Alert.class).get(transfer);row(label+":finished="+(f(ft,"a","fis",InputStream.class).get(transfer)==null)+":alert="+(alert==null?"null":enc(alert.getString())+":"+alert.getTimeout()));events(label+"-finish");
        }
    }
    static void files()throws Exception{
        for(boolean withContact:new boolean[]{false,true})for(int failure:new int[]{0,1,3,4}){
            resetEnv();integer(111,0);integer(72,1024);integer(70,1000);integer(97,0);integer(69,0);Object transfer=make(withContact);env.getMethod("file",String.class,byte[].class).invoke(null,"/card/test.txt",new byte[]{1,2,3});env.getField("fileFailure").setInt(null,failure);
            m(ft,"a","onFileSelect",void.class,String.class).invoke(transfer,"/card/test.txt");String label="file-"+withContact+"-"+failure;row(label+":log="+env.getField("fileLog").get(null)+":background="+m(options,"a","getString",String.class,int.class).invoke(null,34)+":background-mode="+m(options,"a","getInt",int.class,int.class).invoke(null,69));snapshot(label,transfer);events(label);
        }
        resetEnv();env.getMethod("directory",String.class,String[].class).invoke(null,"/card/",new String[]{"Folder/","b.TXT","a.png"});
        Object transfer=make(true);m(ft,"a","startFT",void.class).invoke(transfer);Object screen=f(browser,"a","tree",tree).get(null);
        row("browser-root:"+enc(f(vl,"a","caption",String.class).get(screen))+":"+menu(screen)+":"+(current()==screen));
        Object browserController=f(browser,"a","_this",browser).get(null);Command open=(Command)f(browser,"a","openCommand",Command.class).get(null);command(browserController,open);
        row("browser-open:"+f(browser,"a","currDir",String.class).get(null)+":"+Arrays.toString((String[])f(browser,"a","items",String[].class).get(null))+":"+menu(screen));
        for(boolean dirs:new boolean[]{false,true})for(boolean fromOptions:new boolean[]{false,true}){
            m(browser,"a","setParameters",void.class,boolean.class,boolean.class).invoke(null,dirs,fromOptions);
            for(String name:new String[]{"../","Folder/","test.txt","noextension",".hidden"}){
                m(browser,"a","updateTreeCaptionAndCommands",void.class,String.class).invoke(null,name);row("browser-caption-"+dirs+"-"+name+":"+enc(f(vl,"a","caption",String.class).get(screen))+":"+menu(screen)+":"+commands(screen));
            }
            actions().clear();command(browserController,cmd("c","cmdBack"));events("browser-back-"+dirs+"-"+fromOptions);
        }
        Class<?> listener=load("k","jimm.FileBrowserListener");final Vector<String> callbacks=new Vector<>();
        Object receiver=java.lang.reflect.Proxy.newProxyInstance(loader,new Class<?>[]{listener},new InvocationHandler(){public Object invoke(Object proxy,Method method,Object[] args){
            if(args==null||args.length==0)return contactItem;
            callbacks.add((method.getName().equals(n("a","onFileSelect"))?"file:":"directory:")+args[0]);return null;
        }});
        m(browser,"a","setListener",void.class,listener).invoke(null,receiver);
        for(String file:new String[]{"Folder/","a.png"})for(String[] selected:new String[][]{{"j","cmdSelect"},{"s","cmdSave"}}){
            Object root=f(tree,"a","root",node).get(screen);
            for(Object n:(Vector)f(node,"a","items",Vector.class).get(root))if(file.equals(f(node,"a","data",Object.class).get(n)))m(tree,"a","setCurrentItem",void.class,node).invoke(screen,n);
            callbacks.clear();command(browserController,cmd(selected[0],selected[1]));row("browser-selected-"+file+"-"+selected[1]+":"+callbacks);
        }
        // Opening a directory preserves the caller's return-to-options flag.
        env.getMethod("directory",String.class,String[].class).invoke(null,"/card/Folder/",new String[]{"file.bin"});
        Object root=f(tree,"a","root",node).get(screen);for(Object n:(Vector)f(node,"a","items",Vector.class).get(root))if("Folder/".equals(f(node,"a","data",Object.class).get(n)))m(tree,"a","setCurrentItem",void.class,node).invoke(screen,n);
        m(browser,"a","setParameters",void.class,boolean.class,boolean.class).invoke(null,false,true);command(browserController,open);row("browser-nested:"+f(browser,"a","currDir",String.class).get(null));
        actions().clear();command(browserController,cmd("c","cmdBack"));events("browser-nested-back");
    }
    static void costs()throws Exception{
        for(int value:new int[]{0,1,2,9,10,99,100,1000,9999,10000,10001,123456,1000000,2147483647})row("cost-format-"+value+":"+m(traffic,"b","getString",String.class,int.class).invoke(null,value));
        for(int packetLength:new int[]{1,1023,1024,2048,4096})for(int cost:new int[]{0,999,1000,1500,10000})for(int amount:new int[]{0,1,1024,4096}){
            integer(72,packetLength);integer(70,cost);integer(71,7);f(traffic,"e","session_traffic",int.class).setInt(null,amount);f(traffic,"c","savedCost",int.class).setInt(null,11);f(traffic,"h","costPerDaySum",int.class).setInt(null,0);f(traffic,"b","lastTimeUsed",Date.class).set(null,new Date(1L));
            int session=(Integer)m(traffic,"a","generateCostSum",int.class,boolean.class).invoke(null,true),total=(Integer)m(traffic,"a","generateCostSum",int.class,boolean.class).invoke(null,false);
            row("cost-"+packetLength+"-"+cost+"-"+amount+":"+session+":"+total+":"+f(traffic,"h","costPerDaySum",int.class).getInt(null)+":"+((Date)f(traffic,"b","lastTimeUsed",Date.class).get(null)).getTime());
        }
    }
    static String commands(Object screen)throws Exception{StringBuilder b=new StringBuilder();for(Object c:(Vector)f(vl,"a","leftMenuItems",Vector.class).get(screen))b.append(commandName((Command)c)).append(';');return b.toString();}
    static void setupFiles()throws Exception{
        ui=load("cf","jimm.JimmUI");vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");form=load("d","DrawControls.VirtualForm");choice=load("j","DrawControls.FormChoiceGroup");tree=load("aw","DrawControls.VirtualTree");node=load("ax","DrawControls.TreeNode");formItem=load("i","DrawControls.FormItem");events=load("MessageIO","MessageIO");
        ft=load("p","jimm.FileTransfer");browser=load("u","jimm.FileBrowser");env=load("FileTransferIO","FileTransferIO");endpoint=load("TransportIO$Endpoint","TransportIO$Endpoint");traffic=load("x","jimm.Traffic");resource=load("ai","jimm.util.ResourceBundle");send=load("cl","jimm.comm.SendMessageAction");fileMessage=load("ab","jimm.comm.FileTransferMessage");
        owner=m(icq,"a","getIcq",icq).invoke(null);reset(false);string(0,"54321");controller=f(ui,"a","_this",ui).get(null);contactItem=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);
        Class<?> of=load("cg","jimm.OptionsForm");f(options,"a","optionsForm",of).set(null,instance(of,new Class<?>[0]));
    }
    static void exerciseFiles()throws Exception{
        setupFiles();forms();http();files();costs();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseFiles();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
