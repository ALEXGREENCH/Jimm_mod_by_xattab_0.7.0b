import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import javax.microedition.lcdui.*;

/** Actual browser sorting, navigation, callbacks, rows and JSR75 lifecycle on scripted I/O. */
public class FileBrowserProbe extends TreeProbe {
    static Object browserController;
    static Class<?> listener,fs,jsr;
    static boolean withContact;
    static Object browserTree()throws Exception{return f(browser,"a","tree",tree).get(null);}
    static String[] entries={"z.TXT","Beta/","alpha/","A.png","b.txt","noextension",".hidden","\u044f\u044f\u044f.txt","\u0410\u0410\u0410/","same","same","same/"};
    static Command open()throws Exception{return (Command)f(browser,"a","openCommand",Command.class).get(null);}
    static void configure(boolean dirs,boolean fromOptions)throws Exception{m(browser,"a","setParameters",void.class,boolean.class,boolean.class).invoke(null,dirs,fromOptions);}
    static void receiver()throws Exception {
        Object proxy=Proxy.newProxyInstance(loader,new Class<?>[]{listener},new InvocationHandler(){public Object invoke(Object proxy,Method method,Object[] args){
            if(args==null||args.length==0)return withContact?contactItem:null;
            callbacks.add((method.getName().equals(n("a","onFileSelect"))?"file:":"directory:")+args[0]);return null;
        }});m(browser,"a","setListener",void.class,listener).invoke(null,proxy);
    }
    static void freshBrowser(boolean dirs,boolean fromOptions,boolean contact,int style)throws Exception {
        resetEnv();callbacks.clear();withContact=contact;integer(112,style);f(options,"g","fontStyle",int.class).setInt(null,style);integer(118,1);bool(143,false);alpha(171,171,128);background(true);m(vl,"d","assignSoftKeys",void.class).invoke(null);
        env.getField("rootEntries").set(null,new String[]{"card/","phone/","Z:/"});env.getMethod("directory",String.class,String[].class).invoke(null,"/card/",entries);env.getMethod("directory",String.class,String[].class).invoke(null,"/card/Beta/",new String[]{"child/","leaf.bin"});env.getMethod("directory",String.class,String[].class).invoke(null,"/card/Beta/child/",new String[0]);
        browserController=instance(browser,new Class<?>[0]);configure(dirs,fromOptions);receiver();m(browser,"a","activate",void.class).invoke(null);t=browserTree();root=f(tree,"a","root",node).get(t);viewport(176,220);
    }
    static void viewport(int width,int height)throws Exception{f(vl,"H","forcedWidth",int.class).setInt(t,width);f(vl,"I","forcedHeight",int.class).setInt(t,height);}
    static void selectName(String value)throws Exception {
        root=f(tree,"a","root",node).get(t);for(Object entry:(Vector)f(node,"a","items",Vector.class).get(root))if(value.equals(data(entry))){m(tree,"a","setCurrentItem",void.class,node).invoke(t,entry);return;}throw new AssertionError("Missing browser node: "+value);
    }
    static void browserState(String label)throws Exception {
        Object selected=m(tree,"a","getCurrentItem",node).invoke(t);StringBuilder b=new StringBuilder(label+":path="+enc(f(browser,"a","currDir",String.class).get(null))+":items="+Arrays.toString((String[])f(browser,"a","items",String[].class).get(null))+":selected="+enc(data(selected))+":row="+f(vl,"b","currItem",int.class).getInt(t)+":top="+f(vl,"d","topItem",int.class).getInt(t)+":caption="+enc(f(vl,"a","caption",String.class).get(t))+":locked="+f(vl,"b","dontRepaint",boolean.class).getBoolean(t)+":active="+(current()==t)+":dirs="+f(browser,"a","needToSelectDirectory",boolean.class).getBoolean(null)+":options="+f(browser,"b","returnToOptions",boolean.class).getBoolean(null)+":menu="+menu(t)+":commands="+commands(t)+":callbacks="+callbacks+":tree="+branch(f(tree,"a","root",node).get(t)));callbacks.clear();row(b.toString());
        row(label+":io="+env.getField("fileLog").get(null)+":closes="+env.getField("fileCloses").getInt(null));
        events(label);actions().clear();
    }
    static void action(String label,Object command)throws Exception{row(label+":result="+invoke(m(browser,"commandAction","commandAction",void.class,Command.class,Displayable.class),browserController,command,null));browserState(label);}
    static void frames()throws Exception {
        for(boolean dirs:new boolean[]{false,true})for(int style:new int[]{0,1,2,3})for(int width:new int[]{90,176}){
            freshBrowser(dirs,false,false,style);viewport(width,width==90?110:220);String label="frame-root-"+dirs+"-"+style+"-"+width;browserState(label);SelectorProbe.render(label,t);selectName("card/");action(label+"-open",open());
            Vector children=(Vector)f(node,"a","items",Vector.class).get(f(tree,"a","root",node).get(t));for(Object child:children){m(tree,"a","setCurrentItem",void.class,node).invoke(t,child);String selected=id(child);browserState(label+"-"+selected);SelectorProbe.render(label+"-"+selected,t);}
        }
    }
    static void navigation()throws Exception {
        for(boolean dirs:new boolean[]{false,true})for(boolean fromOptions:new boolean[]{false,true})for(boolean contact:new boolean[]{false,true}){
            freshBrowser(dirs,fromOptions,contact,2);String label="nav-"+dirs+"-"+fromOptions+"-"+contact;selectName("card/");action(label+"-root-open",open());selectName("Beta/");
            action(label+"-directory-select",cmd("j","cmdSelect"));action(label+"-directory-save",cmd("s","cmdSave"));action(label+"-directory-open",open());selectName("child/");action(label+"-child-open",open());action(label+"-parent",open());if(!dirs){selectName("leaf.bin");action(label+"-leaf-open",open());action(label+"-leaf-select",cmd("j","cmdSelect"));action(label+"-leaf-save",cmd("s","cmdSave"));}action(label+"-unknown",new Command("Unknown",Command.ITEM,99));selectName("../");action(label+"-parent2",open());selectName("../");action(label+"-parent3",open());action(label+"-back",cmd("c","cmdBack"));
            // Real native screen removes the browser from command routing.
            boundary.getMethod("show",Display.class,Displayable.class).invoke(null,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()),new TextBox("Other","",20,0));action(label+"-inactive",open());
        }
    }
    static void captionsAndErrors()throws Exception {
        for(String path:new String[]{"/","/card/","/card/Beta/","card/","","/card"})for(String name:new String[]{"../","Folder/","file.txt",".hidden","name.","noextension","UPPER.JPG","a.b.c",""})for(boolean dirs:new boolean[]{false,true}) {
            freshBrowser(dirs,false,false,0);f(browser,"a","currDir",String.class).set(null,path);row("caption-"+enc(path)+"-"+enc(name)+"-"+dirs+":result="+invoke(m(browser,"a","updateTreeCaptionAndCommands",void.class,String.class),null,name));browserState("caption");
        }
        for(long capacity:new long[]{-1,0,1023,1024,1048576,Long.MIN_VALUE,Long.MAX_VALUE}){freshBrowser(false,false,false,0);env.getField("capacity").setLong(null,capacity);row("capacity-"+capacity+":result="+invoke(m(browser,"a","updateTreeCaptionAndCommands",void.class,String.class),null,"card/"));browserState("capacity-"+capacity);}
        for(int failure:new int[]{1,2,10,11}){freshBrowser(false,false,false,0);env.getField("fileFailure").setInt(null,failure);row("root-caption-failure-"+failure+":result="+invoke(m(browser,"a","updateTreeCaptionAndCommands",void.class,String.class),null,"card/"));browserState("root-caption-failure-"+failure);selectName("card/");action("directory-failure-"+failure,open());}
        for(String[] roots:new String[][]{{},{"card/"},{"x:/","x:/"}}){freshBrowser(false,false,false,0);env.getField("rootEntries").set(null,roots);row("roots-"+Arrays.toString(roots)+":result="+invoke(m(browser,"a","activate",void.class),null));browserState("roots");}
        freshBrowser(false,false,false,0);env.getField("fileFailure").setInt(null,12);row("roots-failure:result="+invoke(m(browser,"a","activate",void.class),null));browserState("roots-failure");
    }
    static void filesystem()throws Exception {
        for(boolean dirs:new boolean[]{false,true})for(String[] data:new String[][]{{},{"file.txt"},entries,{"../","../","a/","a","a/"},{null,"valid/"}})for(int failure:new int[]{0,1,2,10}){
            resetEnv();env.getMethod("directory",String.class,String[].class).invoke(null,"/card/",data);env.getField("fileFailure").setInt(null,failure);String label="listing-"+dirs+"-"+Arrays.toString(data)+"-"+failure;try{row(label+":"+Arrays.toString((String[])m(jsr,"a","getDirectoryContents",String[].class,String.class,boolean.class).invoke(null,"/card/",dirs)));}catch(InvocationTargetException error){row(label+":exception="+"JimmException"+":"+f(FileBrowserProbe.error,"a","_ErrCode",int.class).getInt(error.getCause()));}row(label+":io="+env.getField("fileLog").get(null));
        }
        for(boolean exists:new boolean[]{false,true})for(int failure:new int[]{0,1,3,4,5,6,7,8,9,10}){
            resetEnv();if(exists)env.getMethod("file",String.class,byte[].class).invoke(null,"/card/a.txt",new byte[]{0,1,2,127,(byte)255});Object file=instance(jsr,new Class<?>[0]);String label="file-"+exists+"-"+failure;row(label+"-unopened-size:"+invoke(m(jsr,"a","fileSize",long.class),file));row(label+"-unopened-name:"+invoke(m(jsr,"a","getName",String.class),file));env.getField("fileFailure").setInt(null,failure);row(label+"-open:"+invoke(m(jsr,"a","openFile",void.class,String.class),file,"/card/a.txt"));row(label+"-size:"+invoke(m(jsr,"a","fileSize",long.class),file));row(label+"-name:"+invoke(m(jsr,"a","getName",String.class),file));
            try{InputStream stream=(InputStream)m(jsr,"a","openInputStream",InputStream.class).invoke(file);ByteArrayOutputStream data=new ByteArrayOutputStream();for(int c;(c=stream.read())!=-1;)data.write(c);row(label+"-read:"+b64(data.toByteArray()));stream.close();}catch(InvocationTargetException e){row(label+"-read:exception="+e.getCause().getClass().getName());}
            try{OutputStream stream=(OutputStream)m(jsr,"a","openOutputStream",OutputStream.class).invoke(file);try{stream.write(new byte[]{9,8,7});stream.flush();row(label+"-write:ok");}catch(IOException e){row(label+"-write:exception="+e.getClass().getName());}finally{stream.close();}}catch(InvocationTargetException e){row(label+"-write:exception="+e.getCause().getClass().getName());}
            row(label+"-close:"+invoke(m(jsr,"a","close",void.class),file));row(label+"-close-again:"+invoke(m(jsr,"a","close",void.class),file));row(label+":io="+env.getField("fileLog").get(null)+":closes="+env.getField("fileCloses").getInt(null)+":"+env.getField("inputCloses").getInt(null)+":"+env.getField("outputCloses").getInt(null)+":bytes="+b64((byte[])((Hashtable)env.getField("files").get(null)).get("/card/a.txt")));
        }
    }
    static void exerciseBrowser()throws Exception {
        setupFiles();icon=load("e","DrawControls.Icon");itemData=load("cn","DrawControls.ListItem");boundary=load("MenuIO","MenuIO");listener=load("k","jimm.FileBrowserListener");fs=load("w","jimm.FileSystem");jsr=load("b","jimm.JSR75FileSystem");frames();navigation();captionsAndErrors();filesystem();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseBrowser();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
