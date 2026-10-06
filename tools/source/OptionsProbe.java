import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import javax.microedition.lcdui.*;

/** Real options panels, controls and persistence; menu/hotkey/color/account transitions. */
public class OptionsProbe extends FileTransferProbe {
    static Class<?> of,line,part,icon,selector,popup;
    static Object c;
    static Object[] initial,before;
    static Object value(String old,String name,Class<?> type)throws Exception{return f(of,old,name,type).get(c);}
    static Object menu()throws Exception{return value("a","optionsMenu",text);}
    static Object panel()throws Exception{return value("a","optionsForm",form);}
    static Object[] values()throws Exception{return (Object[])f(options,"a","options",Object[].class).get(null);}
    static void fresh(boolean connected,int font)throws Exception{
        f(options,"a","options",Object[].class).set(null,initial.clone());integer(111,font);integer(83,2);integer(94,7);string(0,"12345");string(228,"secret");string(14,"");string(15,"");
        f(icq,"b","connected",boolean.class).setBoolean(null,connected);f(icq,"a","setPoint",boolean.class).setBoolean(null,connected);
        c=instance(of,new Class<?>[0]);f(options,"a","optionsForm",of).set(null,c);m(of,"c","activate",void.class).invoke(c);before=values().clone();resetEnv();
    }
    static void select(int tag)throws Exception{m(text,"a","selectTextByIndex",void.class,int.class).invoke(menu(),tag);command(c,cmd("j","cmdSelect"));}
    static String itemText(Item item)throws Exception{
        String s=enc(item.getLabel());
        if(item instanceof TextField){TextField t=(TextField)item;s+="="+enc(t.getString())+":"+t.getMaxSize()+":"+t.getConstraints();}
        else if(item instanceof ChoiceGroup){ChoiceGroup g=(ChoiceGroup)item;s+=":"+f(choice,"a","choiceType",int.class).getInt(g);for(int i=0;i<g.size();i++)s+="["+enc(g.getString(i))+":"+g.isSelected(i)+"]";}
        else if(item instanceof Gauge){Gauge g=(Gauge)item;s+="="+g.getValue()+":"+g.getMaxValue()+":"+g.isInteractive();}return s;
    }
    static Vector<Item> items(Object parent)throws Exception{
        Vector<Item> result=new Vector<>();Object d=f(node,"a","data",Object.class).get(parent);if(formItem.isInstance(d)){Item i=(Item)f(formItem,"a","item",Item.class).get(d);if(i!=null)result.add(i);}
        Vector children=(Vector)f(node,"a","items",Vector.class).get(parent);if(children!=null)for(Object child:children)for(Item i:items(child))if(!result.contains(i))result.add(i);return result;
    }
    static void layout(String label,Object s)throws Exception{
        row(label+":caption="+enc(f(vl,"a","caption",String.class).get(s))+":font="+f(vl,"u","fontSize",int.class).getInt(s)+":mode="+f(vl,"w","cursorMode",int.class).getInt(s)+":menu="+menu(s));
        String commands="";for(Object item:(Vector)f(vl,"a","leftMenuItems",Vector.class).get(s))commands+=commandName((Command)item)+";";row(label+":commands="+commands);
        if(form.isInstance(s)){Object root=f(tree,"a","root",node).get(s);row(label+":tree="+structure(root));int i=0;for(Item item:items(root))row(label+":item-"+i+++":"+itemText(item));}
        else if(text.isInstance(s)){
            StringBuilder b=new StringBuilder();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(s)){b.append('[').append(f(line,"a","bigTextIndex",int.class).getInt(l)).append(':');for(Object p:(Vector)f(line,"a","items",Vector.class).get(l))b.append(enc(f(part,"a","text",String.class).get(p))).append('@').append(f(part,"a","fontAndColor",int.class).getInt(p)).append(';');b.append(']');}row(label+":lines="+b);
        }
    }
    static void mutate()throws Exception{
        for(Item item:items(f(tree,"a","root",node).get(panel()))){
            if(item instanceof TextField){TextField t=(TextField)item;int type=t.getConstraints()&0xffff;t.setString(type==TextField.NUMERIC?"1":type==TextField.DECIMAL?"1":"X");}
            else if(item instanceof ChoiceGroup){ChoiceGroup g=(ChoiceGroup)item;if(f(choice,"a","choiceType",int.class).getInt(g)==Choice.MULTIPLE){for(int i=0;i<g.size();i++)g.setSelectedIndex(i,!g.isSelected(i));}else if(g.size()>1)g.setSelectedIndex((g.getSelectedIndex()+1)%g.size(),true);}
            else if(item instanceof Gauge){Gauge g=(Gauge)item;if(g.isInteractive())g.setValue(g.getValue()==0?1:0);}
        }
    }
    static void changes(String label)throws Exception{
        String s="";Object[] v=values();for(int i=0;i<v.length;i++)if(!Objects.equals(v[i],before[i]))s+=i+"="+enc(v[i])+";";row(label+":options="+s+":saves="+env.getField("saves").getInt(null));
        for(Object item:actions()){Object[] event=(Object[])item;row(label+":event="+event[0]+":"+enc(Arrays.toString((Object[])event[1])));}actions().clear();
        row(label+":active="+(current()==menu()?"menu":current()==panel()?"form":popup.isInstance(current())?"popup":"other")+":editor="+(f(ui,"a","messageTextbox",TextBox.class).get(null)!=null));
    }
    static void panels()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int font:new int[]{0,2})for(int tag:new int[]{0,1,2,3,6,7,8,9,10,12})for(boolean edit:new boolean[]{false,true}){
            fresh(connected,font);String label="panel-"+connected+"-"+font+"-"+tag+"-"+edit;select(tag);layout(label,panel());if(edit)mutate();f(ui,"a","messageTextbox",TextBox.class).set(null,new TextBox("test","test",100,0));actions().clear();String outcome=invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null);row(label+":save="+outcome);changes(label);
        }
    }
    static int[] tags(Object list)throws Exception{
        LinkedHashSet<Integer> result=new LinkedHashSet<>();for(Object l:(Vector)f(text,"a","lines",Vector.class).get(list))result.add(f(line,"a","bigTextIndex",int.class).getInt(l));int[] a=new int[result.size()];int i=0;for(int tag:result)a[i++]=tag;return a;
    }
    static Object sub(String old,String name)throws Exception{return value(old,name,text);}
    static void pick(Object list,int tag)throws Exception{m(text,"a","selectTextByIndex",void.class,int.class).invoke(list,tag);}
    static void menus()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int font:new int[]{0,2}){
            fresh(connected,font);String label="menu-"+connected+"-"+font;layout(label,menu());pick(menu(),9);m(of,"c","activate",void.class).invoke(c);row(label+":retained="+m(text,"b","getCurrTextIndex",int.class).invoke(menu()));
            for(int tag:new int[]{1,3,6,7,8,9,10,12}){select(tag);command(c,cmd("c","cmdBack"));row(label+":back-"+tag+"="+(current()==menu()));}
            command(c,cmd("c","cmdBack"));changes(label);
        }
    }
    static void clients()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int client:new int[]{7,1,0,12,13,14,18,15,8,11,21,2,3,6,22}){
            fresh(connected,0);select(11);Object list=sub("e","clientIdMenu");String label="client-"+connected+"-"+client;layout(label,list);pick(list,client);actions().clear();command(c,cmd("j","cmdSelect"));changes(label);
        }
    }
    static void hotkeys()throws Exception{
        for(boolean extended:new boolean[]{false,true}){
        fresh(false,0);bool(175,extended);select(5);int[] keyTags=tags(sub("b","keysMenu"));
        for(int key:keyTags)for(int action:new int[]{0,2,3,8,4,5,6,7,9,10,11,12,14,15,17,13,16})for(boolean cancel:new boolean[]{false,true}){
            fresh(false,0);bool(175,extended);select(5);Object keys=sub("b","keysMenu");pick(keys,key);command(c,cmd("j","cmdSelect"));Object actionsMenu=sub("c","actionMenu");String label="key-"+extended+"-"+key+"-"+action+"-"+cancel;if(action==0&&!cancel){layout(label+"-keys",keys);layout(label+"-actions",actionsMenu);}pick(actionsMenu,action);command(c,cancel?cmd("c","cmdBack"):cmd("j","cmdSelect"));command(c,cmd("s","cmdSave"));changes(label);
        }
        }
        for(boolean enabled:new boolean[]{false,true}){fresh(false,0);select(5);String label="ext-"+enabled;command(c,(Command)value(enabled?"a":"b",enabled?"enableExtKeyCommand":"disableExtKeyCommand",Command.class));layout(label,sub("b","keysMenu"));changes(label);}
    }
    static void colors()throws Exception{
        for(int font:new int[]{0,2})for(boolean save:new boolean[]{false,true}){
            fresh(false,font);select(4);Object colors=sub("d","colorsMenu");String label="colors-"+font+"-"+save;layout(label,colors);for(int tag:tags(colors))integer(tag,0x123456+tag);integer(80,0x0000ff);command(c,save?cmd("s","cmdSave"):cmd("c","cmdBack"));changes(label);select(4);layout(label+"-reopen",colors);integer(tags(colors)[0],0x010203);command(c,cmd("c","cmdBack"));changes(label+"-cancel");
        }
    }
    static void colorImage(String label,Object chooser,Class<?> cl)throws Exception{
        Canvas canvas=(Canvas)chooser;int w=canvas.getWidth(),h=canvas.getHeight();Image image=Image.createImage(w,h);m(cl,"paint","paint",void.class,Graphics.class).invoke(chooser,image.getGraphics());int[] rgb=new int[w*h];image.getRGB(rgb,0,w,0,0,w,h);MessageDigest hash=MessageDigest.getInstance("SHA-256");for(int p:rgb){hash.update((byte)(p>>>24));hash.update((byte)(p>>>16));hash.update((byte)(p>>>8));hash.update((byte)p);}row(label+":image="+w+"x"+h+":"+b64(hash.digest())+":digit="+f(cl,"h","a",int.class).getInt(chooser)+":text="+enc(f(cl,"a","s",String.class).get(chooser)));
    }
    static void colorChooser()throws Exception{
        Class<?> cl=load("ar","jimm.ColorChooser");for(int color:new int[]{102,107,114})for(boolean apply:new boolean[]{false,true}){
            fresh(false,0);select(4);pick(sub("d","colorsMenu"),color);command(c,cmd("j","cmdSelect"));row("chooser-"+color+"-"+apply+":selector="+selector.isInstance(current()));command(c,cmd("j","cmdSelect"));Object chooser=Class.forName("OptionsIO",true,loader).getField("screen").get(null);String label="chooser-"+color+"-"+apply;row(label+":class="+cl.isInstance(chooser));colorImage(label+"-initial",chooser,cl);Canvas canvas=(Canvas)chooser;int step=0;for(int action:new int[]{Canvas.LEFT,Canvas.LEFT,Canvas.RIGHT,Canvas.UP,Canvas.DOWN,Canvas.UP,Canvas.RIGHT,Canvas.RIGHT,Canvas.RIGHT,Canvas.RIGHT,Canvas.RIGHT,Canvas.RIGHT,Canvas.DOWN}){row(label+":key-"+step+"="+invoke(m(cl,"keyPressed","keyPressed",void.class,int.class),chooser,canvas.getKeyCode(action)));colorImage(label+"-"+step++,chooser,cl);}row(label+":finish="+invoke(m(cl,"keyPressed","keyPressed",void.class,int.class),chooser,apply?canvas.getKeyCode(Canvas.FIRE):-7));changes(label);command(c,cmd("c","cmdBack"));changes(label+"-back");
        }
    }
    static void device(int which)throws Exception{
        Class<?> j=load("jimm.Jimm","jimm.Jimm");for(String[] names:new String[][]{{"a","is_phone_FLY"},{"b","is_phone_SE"},{"d","is_smart_SE"},{"e","is_phone_NOKIA"},{"f","supportsNokiaLight"}})f(j,names[0],names[1],boolean.class).setBoolean(null,names[1].equals("is_phone_FLY")?which==4:names[1].equals("is_phone_SE")?which==1||which==2:names[1].equals("is_smart_SE")?which==2:which==3);
    }
    static void devices()throws Exception{
        for(int which=0;which<5;which++)for(int font:new int[]{0,2})for(boolean edit:new boolean[]{false,true}){
            fresh(false,font);device(which);integer(112,2);m(options,"b","updateFontStyle",void.class).invoke(null);select(3);String label="device-"+which+"-"+font+"-"+edit;layout(label,panel());if(edit)mutate();actions().clear();row(label+":save="+invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null));changes(label);
        }
        device(0);
    }
    static void accounts()throws Exception{
        for(boolean connected:new boolean[]{false,true})for(int count:new int[]{1,2,3,4,5}){
            fresh(connected,0);select(0);String label="account-"+connected+"-"+count;
            for(int i=1;i<count;i++)command(c,(Command)value("f","cmdAddNewAccount",Command.class));layout(label,panel());
            TextField[] u=(TextField[])value("a","uinTextField",TextField[].class),p=(TextField[])value("b","passwordTextField",TextField[].class);for(int i=0;i<u.length;i++){u[i].setString(""+(12345+i));p[i].setString("pass"+i);}
            ChoiceGroup account=(ChoiceGroup)value("p","choiceCurAccount",ChoiceGroup.class);if(account!=null){account.setSelectedIndex(count-1,true);m(of,"itemStateChanged","itemStateChanged",void.class,Item.class).invoke(c,account);}actions().clear();command(c,cmd("s","cmdSave"));changes(label);
        }
    }
    static byte[] serialize()throws Exception{ByteArrayOutputStream b=new ByteArrayOutputStream();m(options,"a","writeOptions",void.class,DataOutputStream.class).invoke(null,new DataOutputStream(b));return b.toByteArray();}
    static void files()throws Exception{
        fresh(false,0);string(2,"restored nick");bool(161,true);integer(65,2);byte[] saved=serialize();
        for(boolean importing:new boolean[]{false,true})for(int failure:new int[]{0,1,3,4,5,6,7,8,9,10}){
            fresh(false,0);String label="file-"+importing+"-"+failure;String path=importing?"/card/jimm_options.sav":"/card/";env.getMethod("file",String.class,byte[].class).invoke(null,"/card/jimm_options.sav",saved);env.getField("fileFailure").setInt(null,failure);f(of,"d","importingOptions",boolean.class).setBoolean(c,importing);f(of,"b","optionsPath",String.class).set(c,path);row(label+":run="+invoke(m(of,"run","run",void.class),c));changes(label);row(label+":log="+env.getField("fileLog").get(null)+":closed="+env.getField("fileCloses").getInt(null)+":"+env.getField("inputCloses").getInt(null)+":"+env.getField("outputCloses").getInt(null));row(label+":bytes="+b64((byte[])((Hashtable)env.getField("files").get(null)).get("/card/jimm_options.sav"))+":path="+enc(f(of,"b","optionsPath",String.class).get(c))+":controller="+(f(options,"a","optionsForm",of).get(null)!=null));
        }
    }
    static void exerciseOptions()throws Exception{
        setupFiles();of=load("cg","jimm.OptionsForm");line=load("bm","DrawControls.TextLine");part=load("bc","DrawControls.TextItem");icon=load("e","DrawControls.Icon");selector=load("af","jimm.util.Selector");popup=load("ci","DrawControls.VirtualAlert");initial=values().clone();panels();menus();clients();hotkeys();colors();colorChooser();accounts();devices();files();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseOptions();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
