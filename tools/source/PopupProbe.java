import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Execute the same popup transitions in the May JAR and maintained source. */
public class PopupProbe {
    static boolean reference;
    static Class<?> ui, popup, list, textList, options, templates, mainMenu, contact;
    static PrintWriter out;
    static int cases;
    static String name(String obfuscated, String source) { return reference ? obfuscated : source; }
    static Field field(Class<?> type, String name, Class<?> valueType) throws Exception {
        for (Class<?> c = type; c != null; c = c.getSuperclass())
            for (Field f : c.getDeclaredFields())
                if (f.getName().equals(name) && f.getType() == valueType) { f.setAccessible(true); return f; }
        throw new NoSuchFieldException(type + "." + name);
    }
    static Method method(Class<?> type, String name, Class<?> result, Class<?>... params) throws Exception {
        for (Class<?> c = type; c != null; c = c.getSuperclass())
            for (Method m : c.getDeclaredMethods())
                if (m.getName().equals(name) && m.getReturnType() == result && Arrays.equals(m.getParameterTypes(), params)) {
                    m.setAccessible(true); return m;
                }
        throw new NoSuchMethodException(type + "." + name);
    }
    static Object command(String oldName, String newName) throws Exception {
        return field(ui, name(oldName, newName), Command.class).get(null);
    }
    static void commandAction(Object controller, Object command) throws Exception {
        method(controller.getClass(), "commandAction", void.class, Command.class, Displayable.class).invoke(controller, command, null);
    }
    static Object current() throws Exception { return method(ui, name("a", "getCurrentScreen"), Object.class).invoke(null); }
    static void activate(Object screen) throws Exception {
        method(ui, name("b", "selectScreen"), void.class, Object.class).invoke(null, screen);
    }
    static String encode(String text) throws Exception { return Base64.getEncoder().encodeToString(text.getBytes("UTF-8")); }
    static void snapshot(String label, Object expectedPrevious) throws Exception {
        Object screen = current();
        StringBuilder row = new StringBuilder(label);
        row.append(":popup=").append(popup.isInstance(screen));
        row.append(":tag=").append(field(ui, name("a", "curScreenTag"), int.class).getInt(null));
        if (popup.isInstance(screen)) {
            row.append(":text=").append(encode((String)field(popup, name("a", "text"), String.class).get(screen)));
            row.append(":previous=").append(field(popup, name("a", "previousScreen"), Object.class).get(screen) == expectedPrevious);
            row.append(":listener=").append(field(list, name("a", "commandListener"), CommandListener.class).get(screen) == screen);
            for (String[] f : new String[][]{{"o", "popupWidth"}, {"p", "popupHeight"}, {"q", "visibleLines"}})
                row.append(':').append(f[1]).append('=').append(field(popup, name(f[0],f[1]), int.class).getInt(screen));
        } else row.append(":previous=").append(screen == expectedPrevious);
        out.println(row); cases++;
    }
    static Object alert(Object previous, String text, int tag) throws Exception {
        Constructor<?> c = popup.getDeclaredConstructor(Object.class, String.class, int.class); c.setAccessible(true);
        Object value = c.newInstance(previous, text, tag); activate(value); return value;
    }
    static void exercise() throws Exception {
        Constructor<?> tc = textList.getDeclaredConstructor(String.class); tc.setAccessible(true);
        Object background = tc.newInstance("Background"); activate(background);
        Object back = command("c", "cmdBack");
        for (int tag : new int[]{-1,1,2,3,4}) {
            alert(background, "Confirmation " + tag, tag);
            snapshot("back-before-"+tag, background);
            commandAction(current(), back);
            snapshot("back-after-"+tag, background);
        }
        method(mainMenu, name("a", "doExit"), void.class, boolean.class, boolean.class).invoke(null, true, false);
        snapshot("exit-confirm", background);
        commandAction(current(), back);
        snapshot("exit-cancel", background);

        // Clearing templates returns to their empty list, with the editor context retained.
        Constructor<?> constructor = templates.getDeclaredConstructor(); constructor.setAccessible(true);
        Object controller = constructor.newInstance();
        Vector values = (Vector)field(templates, name("a", "templates"), Vector.class).get(null);
        values.removeAllElements(); values.addElement("one"); values.addElement("two");
        TextBox editor = new TextBox("Editor", "draft", 1000, TextField.ANY);
        method(templates, name("a", "selectTemplate"), void.class, TextBox.class, Object.class).invoke(null, editor, background);
        Object templateScreen = current();
        Object clear = field(templates, name("f", "clearCommand"), Command.class).get(null);
        commandAction(controller, clear);
        snapshot("templates-confirm", templateScreen);
        commandAction(current(), back);
        snapshot("templates-cancel", templateScreen);
        out.println("templates-retained:"+values.size()); cases++;
        commandAction(controller, clear);
        commandAction(current(), command("d", "cmdYes"));
        snapshot("templates-cleared", templateScreen);
        out.println("templates-count:"+values.size()); cases++;
        commandAction(controller,field(templates,name("a","selectTemplateCommand"),Command.class).get(null));
        snapshot("templates-empty-select",templateScreen);
        out.println("templates-empty-draft:"+encode(editor.getString())); cases++;

        Method setInt = method(options, name("a", "setInt"), void.class, int.class, int.class);
        Method message = method(ui, name("a", "showPopupWindow"), void.class, String.class, String.class, String.class);
        field(contact, name("c", "currentUin"), String.class).set(null, "12345");
        Field messageEditor = field(ui, name("a", "messageTextbox"), TextBox.class);
        for (int mode=0;mode<=2;mode++) for (boolean existing : new boolean[]{false,true})
            for (boolean matching : new boolean[]{false,true}) for (boolean haveEditor : new boolean[]{false,true}) {
                activate(background);
                if (existing) alert(background, "Existing", -1);
                else field(ui, name("a", "curScreenTag"), int.class).setInt(null,-1);
                messageEditor.set(null, haveEditor ? editor : null);
                setInt.invoke(null,84,mode);
                message.invoke(null,matching?"12345":"54321","Sender","First message");
                snapshot("message-"+mode+"-"+existing+"-"+matching+"-"+haveEditor,background);
                message.invoke(null,matching?"12345":"54321","Sender","Second message");
                snapshot("repeat-"+mode+"-"+existing+"-"+matching+"-"+haveEditor,background);
            }
        // A long popup exercises wrapping, scroll limits and rebuilding appended text.
        StringBuilder longText = new StringBuilder();
        for(int i=0;i<35;i++) longText.append("Line ").append(i).append(" wraps over the narrow phone screen.\n");
        Object longPopup=alert(background,longText.toString(),-1);
        snapshot("long-text",background);
        Method key = method(popup,name("a","doKeyreaction"),void.class,int.class,int.class);
        Object lines=field(popup,name("a","lines"),textList).get(longPopup);
        Field top=field(list,name("d","topItem"),int.class);
        for(int code:new int[]{-2,-2,-1,-1,-1}) {
            key.invoke(longPopup,code,1);
            out.println("scroll-"+code+":"+top.getInt(lines));cases++;
        }
    }
    public static void main(String[] args) {
        try { run(args); System.exit(0); }
        catch(Throwable failure) { failure.printStackTrace(); System.exit(1); }
    }
    static void run(String[] args) throws Exception {
        reference=args[1].equals("reference");
        Headless headless=new Headless(); Field ef=Headless.class.getDeclaredField("emulator"); ef.setAccessible(true);
        Common emulator=(Common)ef.get(headless); ArrayList<String> arguments=new ArrayList<String>();
        Collections.addAll(arguments,"--rms","memory",args[0]);
        emulator.initParams(arguments,new DeviceEntry("Default",null,"org/microemu/device/default/device.xml",true,false),J2SEDevice.class);
        emulator.initMIDlet(true);
        MIDlet midlet=MIDletBridge.getCurrentMIDlet(); ClassLoader loader=midlet.getClass().getClassLoader();
        ui=Class.forName(name("cf","jimm.JimmUI"),true,loader);
        popup=Class.forName(name("ci","DrawControls.VirtualAlert"),true,loader);
        list=Class.forName(name("cd","DrawControls.VirtualList"),true,loader);
        textList=Class.forName(name("bi","DrawControls.TextList"),true,loader);
        options=Class.forName(name("cj","jimm.Options"),true,loader);
        templates=Class.forName(name("aq","jimm.Templates"),true,loader);
        mainMenu=Class.forName(name("ag","jimm.MainMenu"),true,loader);
        contact=Class.forName(name("z","jimm.ContactItem"),true,loader);
        out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]),"UTF-8"));
        final Throwable[] error=new Throwable[1];final CountDownLatch done=new CountDownLatch(1);
        Display.getDisplay(midlet).callSerially(new Runnable(){public void run(){try{exercise();}catch(Throwable e){error[0]=e;}finally{done.countDown();}}});
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Popup UI timed out");
        out.close();if(error[0]!=null)throw new AssertionError("Popup UI failed",error[0]);
        System.out.println("PASS probe: "+cases+" popup transition observations");
    }
}
