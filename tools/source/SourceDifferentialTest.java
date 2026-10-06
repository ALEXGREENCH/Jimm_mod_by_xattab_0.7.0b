import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

/** Executes original May release bytecode and maintained src in isolated loaders. */
public final class SourceDifferentialTest {
    private static int checks;
    private static ClassLoader loader(String jar, String libs) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(Paths.get(jar).toUri().toURL());
        // Real implementations must precede compile-only Java ME signatures.
        urls.add(Paths.get(libs, "microemu.jar").toUri().toURL());
        for (String name : new String[]{"microemu-jsr-75.jar", "microemu-jsr-120.jar", "microemu-nokiaui.jar"})
            urls.add(Paths.get(libs, name).toUri().toURL());
        return new URLClassLoader(urls.toArray(new URL[0]), ClassLoader.getPlatformClassLoader());
    }
    private static Method method(Class<?> cls, String name, Class<?> result, Class<?>... params) throws Exception {
        for (Method m : cls.getDeclaredMethods())
            if (m.getName().equals(name) && m.getReturnType() == result && Arrays.equals(m.getParameterTypes(), params)) {
                m.setAccessible(true); return m;
            }
        throw new NoSuchMethodException(cls + "." + name);
    }
    private static Object invoke(Method m, Object... args) throws Exception {
        try { return m.invoke(null, args); }
        catch (InvocationTargetException e) { return "exception:" + e.getCause().getClass().getName(); }
    }
    private static void equal(Object a, Object b, String label) {
        if (!Objects.deepEquals(a, b)) throw new AssertionError(label + ": " + a + " != " + b);
        checks++;
    }
    private static Field options(Class<?> cls) throws Exception {
        for (Field f : cls.getDeclaredFields()) if (f.getType() == Object[].class) {
            f.setAccessible(true); return f;
        }
        throw new NoSuchFieldException("options");
    }
    private static byte[] save(Method method) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        method.invoke(null, new DataOutputStream(out)); return out.toByteArray();
    }
    public static void main(String[] args) throws Exception {
        ClassLoader reference = loader(args[0], args[2]), rebuilt = loader(args[1], args[2]);
        Class<?> oldUtil = Class.forName("co", true, reference);
        Class<?> newUtil = Class.forName("jimm.comm.Util", true, rebuilt);
        Method oldVersion = method(oldUtil, "c", String.class, byte[].class, int.class, int.class);
        Method newVersion = method(newUtil, "detectClientVersion", String.class, byte[].class, int.class, int.class);
        Method oldStatus = method(oldUtil, "a", int.class, int.class);
        Method newStatus = method(newUtil, "translateStatusSend", int.class, int.class);
        Random random = new Random(20100512);
        for (int n = 0; n < 10000; n++) {
            byte[] data = new byte[48]; random.nextBytes(data);
            int client = n % 54, offset = n % 17;
            equal(invoke(oldVersion, data, client, offset), invoke(newVersion, data, client, offset), "client version " + client);
        }
        for (int status = -1; status < 65536; status++)
            equal(invoke(oldStatus, status), invoke(newStatus, status), "outgoing status " + status);
        Class<?> oldOptions = Class.forName("cj", true, reference);
        Class<?> newOptions = Class.forName("jimm.Options", true, rebuilt);
        Field oldValues = options(oldOptions), newValues = options(newOptions);
        Method oldWrite = method(oldOptions, "a", void.class, DataOutputStream.class);
        Method newWrite = method(newOptions, "writeOptions", void.class, DataOutputStream.class);
        Method oldRead = method(oldOptions, "a", void.class, DataInputStream.class);
        Method newRead = method(newOptions, "readOptions", void.class, DataInputStream.class);
        for (int n = 0; n < 100; n++) {
            Object[] values = new Object[256];
            for (int key = 0; key < 256; key++) {
                if (random.nextInt(5) == 0) continue;
                values[key] = key < 64 || key >= 224 ? "test\u0000\u0410\u044f\u010d " + key + ":" + n
                    : key < 128 ? Integer.valueOf(random.nextInt())
                    : key < 192 ? Boolean.valueOf(random.nextBoolean()) : Long.valueOf(random.nextLong());
            }
            oldValues.set(null, values.clone()); newValues.set(null, values.clone());
            byte[] encoded = save(oldWrite);
            equal(encoded, save(newWrite), "options serialization " + n);
            oldValues.set(null, new Object[256]); newValues.set(null, new Object[256]);
            oldRead.invoke(null, new DataInputStream(new ByteArrayInputStream(encoded)));
            newRead.invoke(null, new DataInputStream(new ByteArrayInputStream(encoded)));
            equal(oldValues.get(null), newValues.get(null), "options decoding " + n);
            equal(encoded, save(newWrite), "options roundtrip " + n);
        }
        System.out.println("PASS: " + checks + " May-reference differential checks (client versions, outgoing statuses, options streams)");
        System.exit(0);
    }
}
