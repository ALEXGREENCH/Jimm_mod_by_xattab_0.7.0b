import java.lang.reflect.*;
import java.util.*;
import java.security.*;
import javax.microedition.lcdui.*;

/** Real form rows, choices, gauge pixels, touch/key input and TextBox editor commands. */
public class FormProbe extends VirtualListProbe {
    static Class<?> bar,itemData;
    static Object s;
    static Vector<String> callbacks=new Vector<String>();
    static ArrayList<Item> fields=new ArrayList<Item>();
    static String image(Object image)throws Exception {
        if(image==null)return "null";
        String v=m(icon,"a","getWidth",int.class).invoke(image)+"x"+m(icon,"b","getHeight",int.class).invoke(image);
        if(bar.isInstance(image))v+=":"+f(bar,"a","value",int.class).getInt(image)+":"+f(bar,"b","stepWidth",int.class).getInt(image);
        else {
            Object pictures=f(form,"a","formImages",images).get(s);
            for(int i=0;i<4;i++)if(m(images,"a","elementAt",icon,int.class).invoke(pictures,i)==image)v+=":choice"+i;
        }
        return v;
    }
    static String branch(Object n)throws Exception {
        Object data=f(node,"a","data",Object.class).get(n);String value="null";
        if(data instanceof String)value="label:"+enc(data);
        else if(formItem.isInstance(data)) {
            Item item=(Item)f(formItem,"a","item",Item.class).get(data);
            value="item:"+fields.indexOf(item)+":"+enc(f(formItem,"a","text",String.class).get(data))+":"+f(formItem,"a","choiceIndex",int.class).getInt(data)+":"+image(f(formItem,"a","image",icon).get(data));
        }
        StringBuilder b=new StringBuilder(value+":"+f(node,"a","expanded",boolean.class).getBoolean(n)+"[");
        Vector children=(Vector)f(node,"a","items",Vector.class).get(n);if(children!=null)for(Object child:children)b.append(branch(child)).append(';');return b.append(']').toString();
    }
    static void state(String label)throws Exception {
        int size=(Integer)m(vl,"a","getSize",int.class).invoke(s);
        row(label+":rows="+size+":selected="+f(vl,"b","currItem",int.class).getInt(s)+":top="+f(vl,"d","topItem",int.class).getInt(s)+":locked="+f(vl,"b","dontRepaint",boolean.class).getBoolean(s)+":first="+f(form,"b","firstItem",boolean.class).getBoolean(s)+":tree="+branch(f(tree,"a","root",node).get(s))+":callbacks="+callbacks);callbacks.clear();
        StringBuilder b=new StringBuilder();for(Item item:fields) {
            b.append(fields.indexOf(item)).append(':').append(enc(item.getLabel()));
            if(item instanceof TextField)b.append('=').append(enc(((TextField)item).getString()));
            else if(item instanceof Gauge)b.append('=').append(((Gauge)item).getValue());
            else if(item instanceof ChoiceGroup){ChoiceGroup c=(ChoiceGroup)item;for(int i=0;i<c.size();i++)b.append(':').append(enc(c.getString(i))).append('/').append(c.isSelected(i));}
            b.append(';');
        }row(label+":model="+b);
    }
    static void fresh(int font,int style,int width,boolean swap)throws Exception {
        callbacks.clear();fields.clear();alpha(171,171,128);background(true);bool(174,swap);integer(118,style);f(options,"g","fontStyle",int.class).setInt(null,style);
        s=instance(form,new Class<?>[]{String.class},"Form controls");
        f(form,"a","itemStateListener",ItemStateListener.class).set(s,new ItemStateListener(){public void itemStateChanged(Item item){callbacks.add("changed:"+fields.indexOf(item));}});
        f(vl,"H","forcedWidth",int.class).setInt(s,width);f(vl,"I","forcedHeight",int.class).setInt(s,220);m(vl,"b","setFontSize",void.class,int.class).invoke(s,font);
        row("editor-commands-"+swap+":"+commandName((Command)f(form,"c","editOk",Command.class).get(s))+":"+commandName((Command)f(form,"d","editCancel",Command.class).get(s)));
    }
    static void append(Item item)throws Exception {fields.add(item);m(form,"a","append",void.class,Item.class).invoke(s,item);}
    static ChoiceGroup choices(String label,int type,int count)throws Exception {
        ChoiceGroup c=(ChoiceGroup)instance(choice,new Class<?>[]{String.class,int.class},label,type);for(int i=0;i<count;i++)c.append("choice-"+i,null);return c;
    }
    static void activate()throws Exception {m(vl,"b","activate",void.class,Display.class).invoke(s,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(s,176);f(vl,"I","forcedHeight",int.class).setInt(s,220);}
    static String nativeCommands(TextBox editor)throws Exception {
        Method get=Displayable.class.getDeclaredMethod("getCommands");get.setAccessible(true);StringBuilder b=new StringBuilder();
        for(Object c:(Vector)get.invoke(editor))b.append(commandName((Command)c)).append(';');return b.toString();
    }
    static Object at(int i)throws Exception{return m(tree,"a","getDrawItem",node,int.class).invoke(s,i);}
    static void select(int i)throws Exception{m(vl,"k","setCurrentItem",void.class,int.class).invoke(s,i);}
    static void op(String label,String old,String name,Class<?>[] types,Object...args)throws Exception {row(label+":result="+invoke(m(form,old,name,void.class,types),s,args));state(label);}
    static void drawRows(String label)throws Exception {
        for(int i=0;i<(Integer)m(vl,"a","getSize",int.class).invoke(s);i++) {
            Object rowData=instance(itemData,new Class<?>[0]);f(itemData,"a","fontStyle",int.class).setInt(rowData,2);m(form,"a","vtGetItemDrawData",void.class,node,itemData).invoke(s,at(i),rowData);
            row(label+":row-"+i+":"+enc(f(itemData,"a","text",String.class).get(rowData))+":"+f(itemData,"a","fontStyle",int.class).getInt(rowData)+":"+f(itemData,"b","color",int.class).getInt(rowData)+":"+image(f(itemData,"a","image",icon).get(rowData)));
        }
    }
    static void layouts()throws Exception {
        for(int font:new int[]{0,8,16})for(int style:new int[]{0,2})for(int width:new int[]{90,176})for(boolean swap:new boolean[]{false,true}) {
            fresh(font,style,width,swap);String label="layout-"+font+"-"+style+"-"+width+"-"+swap;state(label+"-empty");
            append(new TextField("Text label","A long field with\na newline and enough text to exceed the screen width",200,TextField.ANY));
            append(new TextField(null,"password",100,TextField.PASSWORD));append(new StringItem("String label","String body"));append(new StringItem(null,"Only body"));
            append(new Gauge("Gauge label",true,10,3));ChoiceGroup c=choices("Choices",Choice.EXCLUSIVE,3);c.setSelectedIndex(2,true);append(c);
            append(choices(null,Choice.MULTIPLE,2));append(choices("Empty",Choice.POPUP,0));state(label+"-append");drawRows(label);render(label,s);
            ((TextField)fields.get(0)).setString("Externally changed");((Gauge)fields.get(4)).setValue(8);((ChoiceGroup)fields.get(5)).set(1,"Renamed choice",null);((ChoiceGroup)fields.get(5)).setSelectedIndex(1,true);
            activate();state(label+"-reactivate");drawRows(label+"-reactivate");render(label+"-reactivate",s);
            op(label+"-clear","a","clear",new Class<?>[0]);append(new TextField(null,"First again",100,0));state(label+"-refill");
        }
    }
    static void choiceInput()throws Exception {
        for(int type:new int[]{Choice.EXCLUSIVE,Choice.MULTIPLE,Choice.POPUP})for(String label:new String[]{null,"","Group"})for(int selected:new int[]{0,1,2}) {
            fresh(0,0,176,false);ChoiceGroup c=choices(label,type,3);c.setSelectedIndex(selected,true);append(c);activate();String id="choice-"+type+"-"+enc(label)+"-"+selected;state(id);
            for(int i=0;i<3;i++) {select(label==null||label.length()==0?i:i+1);op(id+"-click-"+i,"b","vlItemClicked",new Class<?>[]{vl},s);op(id+"-reclick-"+i,"b","vlItemClicked",new Class<?>[]{vl},s);}
            if(label!=null&&label.length()>0){select(0);op(id+"-group","b","vlItemClicked",new Class<?>[]{vl},s);}
        }
    }
    static void keysAndTouch()throws Exception {
        for(int max:new int[]{1,3,10,200})for(int value:new int[]{0,1}) {
            fresh(0,0,176,false);append(new Gauge("Gauge",true,max,value));append(new TextField("Text","test",100,0));activate();String label="gauge-"+max+"-"+value;
            select(1);for(int key:new int[]{-3,-4,52,54,42,49,51,-1,-2})for(int type:new int[]{1,2})op(label+"-key-"+key+"-"+type,"a","vlKeyPress",new Class<?>[]{vl,int.class,int.class},s,key,type);
            for(int x:new int[]{-20,0,1,50,175,176,500}) {select(1);row(label+"-hook-"+x+":"+invoke(m(form,"a","pointerPressedOnUtem",boolean.class,int.class,int.class,int.class,int.class),s,99,x,2,1));state(label+"-hook-"+x);}
            for(int i:new int[]{0,2}) {select(i);for(int key:new int[]{42,49,51,-1,-2,0})op(label+"-header-"+i+"-"+key,"a","vlKeyPress",new Class<?>[]{vl,int.class,int.class},s,key,1);}
        }
    }
    static void editors()throws Exception {
        for(int constraints:new int[]{TextField.ANY,TextField.PASSWORD,TextField.PASSWORD|TextField.UNEDITABLE,TextField.NUMERIC})for(boolean swap:new boolean[]{false,true})for(boolean listener:new boolean[]{false,true}) {
            fresh(0,0,176,swap);if(!listener)f(form,"a","itemStateListener",ItemStateListener.class).set(s,null);
            TextField field=new TextField(null,constraints==TextField.NUMERIC?"123":"original",40,constraints);append(field);activate();select(0);
            String label="editor-"+constraints+"-"+swap+"-"+listener;boundary.getField("screen").set(null,null);op(label+"-open","b","vlItemClicked",new Class<?>[]{vl},s);
            TextBox editor=(TextBox)boundary.getField("screen").get(null);row(label+":native="+enc(editor.getTitle())+":"+enc(editor.getString())+":"+editor.getMaxSize()+":"+editor.getConstraints()+":commands="+nativeCommands(editor));
            editor.setString(constraints==TextField.NUMERIC?"456":"changed");op(label+"-cancel","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},f(form,"d","editCancel",Command.class).get(s),editor);
            m(form,"b","vlItemClicked",void.class,vl).invoke(s,s);editor=(TextBox)boundary.getField("screen").get(null);editor.setString(constraints==TextField.NUMERIC?"789":"saved");op(label+"-save","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},f(form,"c","editOk",Command.class).get(s),editor);
            m(form,"b","vlItemClicked",void.class,vl).invoke(s,s);editor=(TextBox)boundary.getField("screen").get(null);
            m(ui,"a","setClipBoardText",void.class,boolean.class,String.class,String.class,String.class,String.class).invoke(null,false,"","",constraints==TextField.NUMERIC?"9":"paste","");
            op(label+"-paste","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},cmd("m","cmdPaste"),editor);row(label+":pasted="+enc(editor.getString()));
            actions().clear();op(label+"-smile","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},cmd("n","cmdInsertEmo"),editor);row(label+":smile-events="+actions().size());
            op(label+"-unknown","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},new Command("Other",Command.OK,1),editor);
            op(label+"-noneditor","commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},f(form,"c","editOk",Command.class).get(s),new Form("Native form"));
        }
    }
    static void softbars()throws Exception {
        for(int keys=0;keys<4;keys++)for(int opacity:new int[]{0,170,171,255})for(boolean bg:new boolean[]{false,true}) {
            fresh(0,0,176,false);append(new TextField("Text","field",100,0));alpha(opacity,opacity,128);background(bg);
            if((keys&1)!=0)m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Open",Command.OK,1),1);
            if((keys&2)!=0)m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(s,new Command("Back",Command.BACK,2),2);
            render("softbar-"+keys+"-"+opacity+"-"+bg,s);
        }
    }
    static void bars()throws Exception {
        for(int width:new int[]{1,90,176})for(int height:new int[]{4,9,20})for(int max:new int[]{1,3,200})for(int value:new int[]{-5,0,1,max,max+5})for(int x:new int[]{-3,0,5})for(boolean gauge:new boolean[]{false,true}) {
            Object b=instance(bar,new Class<?>[]{int.class,int.class,int.class,int.class,boolean.class},width,height,value,max,gauge);
            String label="bar-"+width+"-"+height+"-"+max+"-"+value+"-"+x+"-"+gauge;
            for(int changed:new int[]{value,0,max+1}) {
                m(bar,"a","setValue",void.class,int.class).invoke(b,changed);Image frame=Image.createImage(190,40);Graphics g=frame.getGraphics();g.setColor(0x551144);g.fillRect(0,0,190,40);g.setClip(1,2,185,36);
                m(bar,"a","drawByLeft",void.class,Graphics.class,int.class,int.class).invoke(b,g,x,15);
                int[] rgb=new int[190*40];frame.getRGB(rgb,0,190,0,0,190,40);MessageDigest hash=MessageDigest.getInstance("SHA-256");for(int p:rgb){hash.update((byte)(p>>>24));hash.update((byte)(p>>>16));hash.update((byte)(p>>>8));hash.update((byte)p);}
                row(label+"-"+changed+":"+image(b)+":raster="+b64(hash.digest())+":clip="+g.getClipX()+","+g.getClipY()+","+g.getClipWidth()+","+g.getClipHeight());rasters++;
            }
        }
        for(int max:new int[]{-1,0})for(int width:new int[]{0,90}) {
            String result;try{instance(bar,new Class<?>[]{int.class,int.class,int.class,int.class,boolean.class},width,9,0,max,true);result="created";}catch(InvocationTargetException e){result="exception:"+e.getCause().getClass().getName();}row("bar-constructor-"+max+"-"+width+":"+result);
        }
    }
    static void saveRefresh()throws Exception {
        for(boolean firstGroup:new boolean[]{false,true})for(boolean expanded:new boolean[]{false,true}) {
            fresh(0,0,176,false);ChoiceGroup c=choices("Group",Choice.EXCLUSIVE,3);TextField field=new TextField("Field","original",50,0);
            if(firstGroup)append(c);append(field);if(!firstGroup)append(c);
            Object root=f(tree,"a","root",node).get(s);Vector children=(Vector)f(node,"a","items",Vector.class).get(root);Object textRow=null;
            for(Object n:children){Object data=f(node,"a","data",Object.class).get(n);if(formItem.isInstance(data)&&f(formItem,"a","item",Item.class).get(data)==field)textRow=n;else if(formItem.isInstance(data)&&f(formItem,"a","item",Item.class).get(data)==c)m(tree,"a","setExpandFlag",void.class,node,boolean.class).invoke(s,n,expanded);}
            activate();m(tree,"a","setCurrentItem",void.class,node).invoke(s,textRow);c.set(1,"Updated choice",null);c.setSelectedIndex(1,true);
            TextBox edit=new TextBox("Field","saved",50,0);String label="save-refresh-"+firstGroup+"-"+expanded;
            op(label,"commandAction","commandAction",new Class<?>[]{Command.class,Displayable.class},f(form,"c","editOk",Command.class).get(s),edit);render(label,s);
        }
        fresh(0,0,176,false);append(new TextField("Header","text",50,0));append(choices(null,Choice.EXCLUSIVE,3));activate();select(2);
        op("mixed-root-choice","b","vlItemClicked",new Class<?>[]{vl},s);
    }
    static void rowDefaults()throws Exception {
        for(int style:new int[]{0,1,2,3}) {
            f(options,"g","fontStyle",int.class).setInt(null,style);Object data=instance(itemData,new Class<?>[0]);row("row-default-"+style+":"+f(itemData,"a","fontStyle",int.class).getInt(data));
            String[] old={"a","b","c","d","e","f","g","h"},names={"image","xStatusImg","happyImg","bDayImg","authImg","ignoreImg","visibilityImg","clientImg"};
            Object picture=symbol(4,5,0x221144);for(int i=0;i<8;i++)f(itemData,old[i],names[i],icon).set(data,picture);f(itemData,"a","text",String.class).set(data,"text");f(itemData,"a","fontStyle",int.class).setInt(data,99);f(itemData,"b","color",int.class).setInt(data,0x334455);f(itemData,"c","horizOffset",int.class).setInt(data,123);
            m(itemData,"a","clear",void.class).invoke(data);String value="row-clear-"+style+":"+f(itemData,"a","fontStyle",int.class).getInt(data)+":"+f(itemData,"b","color",int.class).getInt(data)+":"+f(itemData,"c","horizOffset",int.class).getInt(data)+":"+enc(f(itemData,"a","text",String.class).get(data));for(int i=0;i<8;i++)value+=":"+(f(itemData,old[i],names[i],icon).get(data)==null);row(value);
        }
    }
    static void exerciseForms()throws Exception {
        setupFiles();icon=load("e","DrawControls.Icon");images=load("f","DrawControls.ImageList");bar=load("c","DrawControls.FormIcon");itemData=load("cn","DrawControls.ListItem");boundary=load("MenuIO","MenuIO");
        layouts();choiceInput();keysAndTouch();editors();softbars();bars();saveRefresh();rowDefaults();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseForms();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
