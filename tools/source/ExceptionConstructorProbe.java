import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.microemu.*;
import org.microemu.app.*;
import org.microemu.app.util.*;
import org.microemu.device.j2se.*;

/** Real constructors, field state and localized descriptions; no application fixtures. */
public class ExceptionConstructorProbe {
    static Properties configuration = new Properties();
    static Class<?> error;
    static Field code, critical, display, peer;
    static Method description;
    static Constructor<?> ordinary, noncritical, connection;
    static PrintWriter out, boundary;
    static int observations, boundaryObservations, constructorCalls;
    static String mode;

    static Field field(String role) throws Exception {
        Class<?> type = role.equals("code") ? int.class : boolean.class;
        for (Field f : error.getDeclaredFields()) if (f.getName().equals(configuration.getProperty(role)) && f.getType() == type) {
            f.setAccessible(true); return f;
        }
        throw new NoSuchFieldException(role);
    }
    static String units(String value) {
        if (value == null) return "null";
        StringBuilder b = new StringBuilder();
        for (int i=0; i<value.length(); i++) {
            String hex = Integer.toHexString(value.charAt(i));
            for (int j=hex.length(); j<4; j++) b.append('0');
            b.append(hex);
        }
        return b.toString();
    }
    static Constructor<?> constructor(Class<?>... args) throws Exception {
        Constructor<?> c = error.getDeclaredConstructor(args); c.setAccessible(true); return c;
    }
    static Object make(Constructor<?> ctor, Object... args) throws Exception {
        constructorCalls++; return ctor.newInstance(args);
    }
    static String state(Object value, int expectedCode, boolean expectedCritical, boolean expectedDisplay, boolean expectedPeer, String expectedMessage) throws Exception {
        String message = ((Throwable)value).getMessage();
        if (code.getInt(value) != expectedCode || critical.getBoolean(value) != expectedCritical
            || display.getBoolean(value) != expectedDisplay || peer.getBoolean(value) != expectedPeer
            || !Objects.equals(message, expectedMessage)) throw new AssertionError("Constructor state/message: " + expectedCode);
        return code.getInt(value) + ":" + critical.getBoolean(value) + ":" + display.getBoolean(value) + ":" + peer.getBoolean(value)
            + ":" + units(message) + ":" + units(((Throwable)value).getLocalizedMessage());
    }
    static void row(String label, Object value, int err, boolean crit, boolean shown, boolean isPeer, String message) throws Exception {
        out.println(label + ":" + state(value, err, crit, shown, isPeer, message)); observations++;
    }
    static void extra(String label, Object value, int err, boolean shown, boolean isPeer, String message) throws Exception {
        boundary.println(label + ":" + state(value, err, false, shown, isPeer, message)); boundaryObservations++;
    }
    static Object peer(int err, int ext) throws Exception {
        return mode.equals("optimized") ? make(connection, err, ext, (byte)0) : make(connection, err, ext, true, true);
    }
    static void exercise() throws Exception {
        ArrayList<Integer> errors = new ArrayList<Integer>();
        for (int value : new int[]{Integer.MIN_VALUE, -65536, -256, -2, -1}) errors.add(value);
        for (int value=0; value<=1024; value++) errors.add(value);
        for (int value : new int[]{32767, 32768, 65535, 65536, Integer.MAX_VALUE}) errors.add(value);
        int[] extensions = {Integer.MIN_VALUE, -65536, -32768, -256, -1, 0, 1, 3, 127, 255, 256, 32767, 65535, 65536, Integer.MAX_VALUE};
        for (int err : errors) for (int ext : extensions) {
            String label = err + ":" + ext;
            String message = (String)description.invoke(null, err, ext);
            out.println("description:" + label + ":" + units(message)); observations++;
            row("critical:" + label, make(ordinary, err, ext), err, true, true, false, message);
            for (boolean shown : new boolean[]{false, true})
                row("ordinary:" + label + ":" + shown, make(noncritical, err, ext, shown), err, false, shown, false, message);
            row("peer:" + label, peer(err, ext), err, false, true, true, message);
            // Explicitly separate out-of-domain reflection from the actual true,true call domain.
            if (mode.equals("optimized")) {
                for (byte dummy : new byte[]{-128, -1, 0, 1, 127})
                    extra("unused-byte:" + label + ":" + dummy, make(connection, err, ext, dummy), err, true, true, message);
            } else for (boolean shown : new boolean[]{false, true}) for (boolean isPeer : new boolean[]{false, true}) {
                boolean nativeMode = mode.equals("reference");
                extra("boolean-domain:" + label + ":" + shown + ":" + isPeer, make(connection, err, ext, shown, isPeer),
                    err, nativeMode || shown, nativeMode || isPeer, message);
            }
        }
        out.println("constructor-calls-in-common-domain:" + errors.size() * extensions.length * 4); observations++;
    }
    static void run(String[] args) throws Exception {
        mode = args[1];
        try (InputStream input = new FileInputStream(args[4])) { configuration.load(input); }
        Headless h = new Headless(); Field ef = Headless.class.getDeclaredField("emulator"); ef.setAccessible(true); Common emulator = (Common)ef.get(h);
        ArrayList<String> params = new ArrayList<String>(); Collections.addAll(params, "--rms", "memory", args[0]);
        if (configuration.containsKey("vendorLight")) Collections.addAll(params, "--appclass", configuration.getProperty("vendorLight"));
        emulator.initParams(params, new DeviceEntry("Default", null, "org/microemu/device/default/device.xml", true, false), J2SEDevice.class);
        emulator.initMIDlet(true);
        ClassLoader loader = MIDletBridge.getCurrentMIDlet().getClass().getClassLoader();
        error = Class.forName(configuration.getProperty("error").replace('/', '.'), true, loader);
        code = field("code"); critical = field("critical"); display = field("display"); peer = field("peer");
        description = error.getDeclaredMethod(configuration.getProperty("description"), int.class, int.class); description.setAccessible(true);
        ordinary = constructor(int.class, int.class); noncritical = constructor(int.class, int.class, boolean.class);
        connection = mode.equals("optimized") ? constructor(int.class, int.class, byte.class) : constructor(int.class, int.class, boolean.class, boolean.class);
        try (PrintWriter common = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[2]), "UTF-8"));
             PrintWriter extra = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[3]), "UTF-8"))) {
            out = common; boundary = extra; exercise();
        }
        System.out.println("PASS exception constructors: " + observations + " common observations, " + boundaryObservations + " explicit boundary observations, " + constructorCalls + " actual constructor calls");
        if (configuration.containsKey("vendorLight")) {
            Class<?> provider = Class.forName(configuration.getProperty("vendorLight"), true, loader);
            System.out.println("VENDOR-LIGHT:" + provider.getField("onCalls").getInt(null) + ":" + provider.getField("offCalls").getInt(null));
        }
    }
    public static void main(String[] args) { try { run(args); System.exit(0); } catch (Throwable e) { e.printStackTrace(); System.exit(1); } }
}
