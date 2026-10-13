import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute the complete actual loop and observe its real rows and partial failures. */
public class FormChoicesProbe {
    static Properties spec;static Class<?> form,node,item,io;static Field children,data,expanded,level,text,index,original,image,root;
    static Method subject,pictureAt;static Field pictures;
    static Field f(Class<?> c,String role,Class<?> type)throws Exception{return PopupProbe.field(c,spec.getProperty(role),type);}
    static Object fresh(Class<?> c,Class<?>[] types,Object...args)throws Exception{Constructor<?> k=c.getDeclaredConstructor(types);k.setAccessible(true);return k.newInstance(args);}
    static String tree(Object n,Object expected,Object formValue)throws Exception {
        StringBuilder b=new StringBuilder();Vector rows=(Vector)children.get(n);if(rows==null)return "null";
        for(Object row:rows) {
            Object value=data.get(row);int picture=-1;
            if(!item.isInstance(value))throw new AssertionError("Unexpected form row");
            Object actual=image.get(value);for(int i=0;i<4;i++)if(pictureAt.invoke(pictures.get(formValue),i)==actual)picture=i;
            if(actual==null||picture<0)throw new AssertionError("Actual form icon missing from image list");
            b.append(index.getInt(value)).append('/').append(text.get(value)).append('/').append(original.get(value)==expected).append('/').append(picture).append('/').append(expanded.getBoolean(row)).append('/').append(level.getInt(row)).append(';');
        }
        return b.toString();
    }
    static String call(Object receiver,Object parent,Item value)throws Exception {
        try{subject.invoke(receiver,parent,value);return "ok";}catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof LinkageError)throw(LinkageError)t;return t.getClass().getName();}
    }
    public static void main(String[] args){try {
        spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[2]))){spec.load(in);}
        Headless h=new Headless();Field ef=Headless.class.getDeclaredField("emulator");ef.setAccessible(true);Common em=(Common)ef.get(h);ArrayList<String> p=new ArrayList<String>();Collections.addAll(p,"--rms","memory",args[0]);
        em.initParams(p,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);em.initMIDlet(true);
        MIDlet host=MIDletBridge.getCurrentMIDlet();ClassLoader loader=host.getClass().getClassLoader();Class<?> options=Class.forName(spec.getProperty("options"),true,loader);
        f(options,"table",Object[].class).set(null,SoftKeysProbe.values(false,false,1,0));
        form=Class.forName(spec.getProperty("form"),true,loader);node=Class.forName(spec.getProperty("node"),true,loader);item=Class.forName(spec.getProperty("item"),true,loader);io=Class.forName("FormChoicesIO",true,loader);
        Class<?> choices=Class.forName(spec.getProperty("choice"),true,loader),icon=Class.forName(spec.getProperty("icon"),true,loader),images=Class.forName(spec.getProperty("images"),true,loader);
        subject=PopupProbe.method(form,spec.getProperty("subject"),void.class,node,Item.class);
        root=f(form,"root",node);children=f(node,"children",Vector.class);data=f(node,"data",Object.class);expanded=f(node,"expanded",boolean.class);level=f(node,"level",int.class);
        text=f(item,"text",String.class);index=f(item,"index",int.class);original=f(item,"original",Item.class);image=f(item,"image",icon);pictures=f(form,"pictures",images);pictureAt=PopupProbe.method(images,spec.getProperty("pictureAt"),icon,int.class);
        int observations=0;
        try(PrintWriter out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8"))) {
            for(int count:new int[]{0,1,3,5})for(int type:new int[]{Choice.EXCLUSIVE,Choice.MULTIPLE,Choice.POPUP})for(int parentKind=0;parentKind<3;parentKind++)for(int mutation=0;mutation<10;mutation++)for(int trigger:new int[]{0,1,9}) {
                Object owner=fresh(form,new Class<?>[]{String.class},"choices");Object parent=parentKind<2?root.get(owner):fresh(node,new Class<?>[]{Object.class},"detached");
                ChoiceGroup model=(ChoiceGroup)fresh(choices,new Class<?>[]{String.class,int.class},"group",type);for(int i=0;i<count;i++)model.append("item"+i,null);if(count>0)model.setSelectedIndex(count-1,true);
                io.getField("expected").set(null,model);io.getField("mutation").setInt(null,mutation);io.getField("trigger").setInt(null,trigger);io.getField("fired").setBoolean(null,false);io.getField("events").set(null,"");
                String result=call(owner,parentKind==0?null:parent,model);StringBuilder state=new StringBuilder();for(int i=0;i<model.size();i++)state.append(model.getString(i)).append('/').append(model.isSelected(i)).append(';');
                out.write(count+":"+type+":"+parentKind+":"+mutation+":"+trigger+":"+result+":"+tree(parent,model,owner)+":"+state+":"+io.getField("events").get(null)+"\n");observations++;
            }
            for(int parentKind=0;parentKind<3;parentKind++)for(boolean absent:new boolean[]{false,true}) {
                Object owner=fresh(form,new Class<?>[]{String.class},"guard");Object parent=parentKind<2?root.get(owner):fresh(node,new Class<?>[]{Object.class},"detached");
                Item value=absent?null:new StringItem("label","text");io.getField("events").set(null,"");
                out.write("guard:"+parentKind+":"+absent+":"+call(owner,parentKind==0?null:parent,value)+":"+tree(parent,value,owner)+":"+io.getField("events").get(null)+"\n");observations++;
            }
        }
        if(observations!=1086)throw new AssertionError(observations);System.out.println("PASS form choices: "+observations+" actual loop observations");System.exit(0);
    }catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
