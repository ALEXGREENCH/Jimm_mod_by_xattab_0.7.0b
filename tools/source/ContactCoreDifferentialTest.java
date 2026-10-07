import java.io.*;
import java.lang.reflect.*;
import java.lang.management.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/** Genuine contact constructors, packed properties, IP data, counters and stream state. */
public final class ContactCoreDifferentialTest {
    static Class<?> oldContact, newContact;
    static int observations, constructorCalls, constructed, constructorFailures, saves, loads;
    static final Map<String, Integer> coverage = new TreeMap<String, Integer>();

    static void equal(Object old, Object fresh, String label) {
        if (!Objects.deepEquals(old, fresh))
            throw new AssertionError(label + ": " + UtilCoreDifferentialTest.describe(old)
                                     + " != " + UtilCoreDifferentialTest.describe(fresh));
        observations++;
    }

    static Object invoke(Method method, Object receiver, Object[] args) throws Exception {
        try { return method.invoke(receiver, args); }
        catch (InvocationTargetException error) {
            if (error.getCause() instanceof LinkageError) throw (LinkageError)error.getCause();
            return new UtilCoreDifferentialTest.Failure(error.getCause().getClass().getName());
        }
    }

    static final class Pair {
        final Method old, fresh;
        final String name;
        Object lastOld, lastNew;
        Object[] oldArgs, newArgs;
        Pair(String before, String after, Class<?> result, Class<?>... params) throws Exception {
            this(before, after, result, params, params);
        }
        Pair(String before, String after, Class<?> result, Class<?>[] oldParams, Class<?>[] params) throws Exception {
            name = after + Arrays.toString(params);
            old = UtilCoreDifferentialTest.method(oldContact, before, result, oldParams);
            fresh = UtilCoreDifferentialTest.method(newContact, after, result, params);
            if ((old.getModifiers() & 40) != (fresh.getModifiers() & 40))
                throw new AssertionError("Modifier mismatch " + name + ": reference " + (old.getModifiers() & 40)
                                         + ", source " + (fresh.getModifiers() & 40));
        }
        Object separate(Object a, Object b, Object[] left, Object[] right) throws Exception {
            oldArgs = left; newArgs = right;
            lastOld = invoke(old, a, left); lastNew = invoke(fresh, b, right);
            equal(lastOld, lastNew, name + " args " + UtilCoreDifferentialTest.describe(left));
            for (int i = 0; i < left.length; i++) if (left[i] instanceof byte[]) {
                equal(left[i], right[i], name + " mutated input " + i);
                if (lastOld instanceof byte[] || lastNew instanceof byte[])
                    equal(lastOld == left[i], lastNew == right[i], name + " returned input alias " + i);
            }
            coverage.put(name, coverage.containsKey(name) ? coverage.get(name) + 1 : 1);
            return lastOld;
        }
        Object call(Object a, Object b, Object... args) throws Exception {
            return separate(a, b, UtilCoreDifferentialTest.copy(args), UtilCoreDifferentialTest.copy(args));
        }
    }

    static Pair setString, getString, setInt, getInt, setBool, getBool, getSort, lower,
                setIP, getIP, toIP, fromIP, cap, addCap, unread, increase, reset, available,
                visible, invisible, ignored, setVisible, setInvisible, setIgnored, save, load;

    static void options(ClassLoader loader, String name) throws Exception {
        Class<?> cls = Class.forName(name, true, loader);
        Field table = UtilCoreDifferentialTest.field(cls, name.equals("cj") ? "a" : "options", Object[].class);
        Object[] values = new Object[256];
        for (int key = 0; key < values.length; key++)
            values[key] = key < 64 || key >= 224 ? "RU" : key < 128 ? Integer.valueOf(0)
                : key < 192 ? Boolean.FALSE : Long.valueOf(0);
        table.set(null, values);
        Class<?> birthdays = Class.forName(name.equals("cj") ? "cs" : "jimm.util.NoticeOnBirthDay", true, loader);
        for (String[] names : new String[][]{{"a", "a0"}, {"b", "a1"}, {"c", "a2"}}) {
            Vector<String> entries = new Vector<String>();
            entries.add(names[0].equals("a") ? "~contact-core-sentinel" : "1");
            UtilCoreDifferentialTest.field(birthdays, name.equals("cj") ? names[0] : names[1], Vector.class).set(null, entries);
        }
    }

    static Object construct(Class<?> cls, Object[] args) throws Exception {
        Constructor<?> ctor = cls.getDeclaredConstructor(args.length == 0 ? new Class<?>[0]
            : new Class<?>[]{int.class, int.class, String.class, String.class, boolean.class, boolean.class});
        ctor.setAccessible(true);
        try { return ctor.newInstance(args); }
        catch (InvocationTargetException error) {
            if (error.getCause() instanceof LinkageError) throw (LinkageError)error.getCause();
            return new UtilCoreDifferentialTest.Failure(error.getCause().getClass().getName());
        }
    }

    static Object[] contacts(Object... args) throws Exception {
        Object a = construct(oldContact, args), b = construct(newContact, args);
        constructorCalls++;
        if (a instanceof UtilCoreDifferentialTest.Failure || b instanceof UtilCoreDifferentialTest.Failure) {
            equal(a, b, "constructor " + Arrays.toString(args));
            constructorFailures++;
            return null;
        }
        equal(oldContact.isInstance(a), newContact.isInstance(b), "constructed contact");
        constructed++;
        return new Object[]{a, b};
    }

    static final int[] KEYS = {0, 1, 2, 3, 5, 6, 63, 64, 65, 66, 67, 68, 69, 70, 71,
        72, 73, 74, 75, 76, 77, 190, 191, 192, 193, 194, 195, 196, 197, 225, 226, 227,
        -1, Integer.MIN_VALUE, Integer.MAX_VALUE};

    static void snapshot(Object[] pair, String label) throws Exception {
        for (int key : KEYS) getInt.call(pair[0], pair[1], key);
        for (int key : new int[]{-1, 0, 1, 2, 3, 4, 5, 6, Integer.MAX_VALUE})
            getString.call(pair[0], pair[1], key);
        for (int mask : new int[]{0, 1, 2, 3, 4, 8, 16, 32, 63, -1, Integer.MIN_VALUE})
            getBool.call(pair[0], pair[1], mask);
        unread.call(pair[0], pair[1]);
        visible.call(pair[0], pair[1]); invisible.call(pair[0], pair[1]); ignored.call(pair[0], pair[1]);
        for (int key : new int[]{225, 226, 227, -1}) getIP.call(pair[0], pair[1], key);
    }

    static void constructors() throws Exception {
        Object[] blank = contacts(); snapshot(blank, "empty constructor");
        for (String uin : new String[]{null, "", "x", "0", "-1", "12345", "2147483647", "2147483648", "\ud800"})
            for (String name : new String[]{null, "", "Name", "\u0418\u043c\u044f\u0000\ud800"})
                for (boolean noAuth : new boolean[]{false, true}) for (boolean added : new boolean[]{false, true}) {
                    Object[] pair = contacts(123, 456, uin, name, noAuth, added);
                    if (pair != null) {
                        snapshot(pair, "parameter constructor");
                        lower.call(pair[0], pair[1]);
                    }
                }
    }

    static void properties() throws Exception {
        Object[] pair = contacts();
        Random random = new Random(20100512);
        for (int sample = 0; sample < 1800; sample++) {
            int key = sample < 260 ? sample - 2 : KEYS[random.nextInt(KEYS.length)];
            int value = sample % 5 == 0 ? Integer.MIN_VALUE : sample % 5 == 1 ? Integer.MAX_VALUE : random.nextInt();
            setInt.call(pair[0], pair[1], key, value);
            for (int query : KEYS) getInt.call(pair[0], pair[1], query);
            int mask = sample < 32 ? 1 << sample : value;
            setBool.call(pair[0], pair[1], mask, sample % 2 == 0);
            for (int query : new int[]{0, mask, ~mask, -1, 1, 2, 4, 8, 16, 32})
                getBool.call(pair[0], pair[1], query);
            cap.call(pair[0], pair[1], mask);
            // May specializes addCapability to CAPF_TYPING (256) and removes its argument.
            addCap.separate(pair[0], pair[1], new Object[0], new Object[]{256});
            cap.call(pair[0], pair[1], mask); getInt.call(pair[0], pair[1], 75);
        }
        String[] strings = {null, "", "Name", "123", "-1", "2147483648", "\u0410\u044f", "a\u0000b", "\ud800"};
        for (int key = -1; key < 9; key++) for (String value : strings) {
            setString.call(pair[0], pair[1], key, value);
            for (int query = -1; query < 9; query++) getString.call(pair[0], pair[1], query);
            lower.call(pair[0], pair[1]); lower.call(pair[0], pair[1]);
        }
        for (int sample = 0; sample < 500; sample++) {
            int value = sample < 5 ? new int[]{Integer.MIN_VALUE, -1, 0, 65535, Integer.MAX_VALUE}[sample] : random.nextInt();
            setVisible.call(pair[0], pair[1], value); setInvisible.call(pair[0], pair[1], ~value);
            setIgnored.call(pair[0], pair[1], value);
            visible.call(pair[0], pair[1]); invisible.call(pair[0], pair[1]); ignored.call(pair[0], pair[1]);
        }
    }

    static void ipAndCounters() throws Exception {
        Object[] pair = contacts(); Random random = new Random(227);
        for (int sample = 0; sample < 1200; sample++) {
            int value = sample < 5 ? new int[]{0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE}[sample] : random.nextInt();
            Object encoded = toIP.call(null, null, value); fromIP.call(null, null, encoded);
            byte[] bytes = sample == 0 ? null : new byte[sample % 10];
            if (bytes != null) random.nextBytes(bytes);
            fromIP.call(null, null, (Object)bytes);
            for (int key : new int[]{225, 226, 227, -1, 0}) {
                setIP.call(pair[0], pair[1], key, bytes);
                Object inputOld = setIP.oldArgs[1], inputNew = setIP.newArgs[1];
                getIP.call(pair[0], pair[1], key);
                if (key == 227) equal(getIP.lastOld == inputOld, getIP.lastNew == inputNew, "roster data alias");
            }
            int type = sample < 15 ? sample - 5 : random.nextInt(7);
            increase.call(pair[0], pair[1], type);
            for (int query : new int[]{-1, 0, 1, 2, 3, 4, 5, Integer.MAX_VALUE})
                available.call(pair[0], pair[1], query);
            unread.call(pair[0], pair[1]);
            if (sample % 17 == 0) reset.call(pair[0], pair[1]);
        }
    }

    static final class Output extends OutputStream {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final int failAt;
        int writes;
        Output(int failAt) { this.failAt = failAt; }
        public void write(int value) throws IOException {
            writes++;
            if (bytes.size() == failAt) throw new IOException("contact stream");
            bytes.write(value);
        }
    }

    static final class Input extends InputStream {
        final byte[] bytes;
        final int chunk, failAt;
        int position, reads;
        Input(byte[] bytes, int chunk, int failAt) { this.bytes = bytes; this.chunk = chunk; this.failAt = failAt; }
        public int read() throws IOException {
            reads++;
            if (position == failAt) throw new IOException("contact stream");
            return position == bytes.length ? -1 : bytes[position++] & 255;
        }
        public int read(byte[] result, int offset, int size) throws IOException {
            reads++;
            if (position == failAt) throw new IOException("contact stream");
            if (size == 0) return 0;
            if (position == bytes.length) return -1;
            int count = Math.min(size, Math.min(chunk, bytes.length - position));
            if (failAt >= 0) count = Math.min(count, failAt - position);
            System.arraycopy(bytes, position, result, offset, count); position += count;
            return count;
        }
    }

    static byte[] serialize(Object[] pair, int failAt) throws Exception {
        Output a = new Output(failAt), b = new Output(failAt);
        save.separate(pair[0], pair[1], new Object[]{new DataOutputStream(a)}, new Object[]{new DataOutputStream(b)});
        saves++;
        equal(a.bytes.toByteArray(), b.bytes.toByteArray(), "contact stream output");
        equal(a.writes, b.writes, "contact stream writes");
        return a.bytes.toByteArray();
    }

    static void deserialize(Object[] pair, byte[] bytes, int chunk, int failAt) throws Exception {
        Input a = new Input(bytes, chunk, failAt), b = new Input(bytes, chunk, failAt);
        load.separate(pair[0], pair[1], new Object[]{new DataInputStream(a)}, new Object[]{new DataInputStream(b)});
        loads++;
        equal(a.position, b.position, "contact stream input position");
        equal(a.reads, b.reads, "contact stream read calls");
        snapshot(pair, "contact after input");
    }

    static void streams() throws Exception {
        Random random = new Random(65535);
        String[] names = {null, "", "name", "a\u0000b", "\u0410\u044f\ud800", "\ud83d\ude00",
            new String(new char[21845]).replace('\0', '\u0800'), new String(new char[21846]).replace('\0', '\u0800')};
        for (int sample = 0; sample < 80; sample++) {
            Object[] pair = contacts();
            for (int key : new int[]{64, 65, 67, 68, 69, 70, 71, 75, 191, 192, 193, 194, 195, 196})
                setInt.call(pair[0], pair[1], key, random.nextInt());
            setBool.call(pair[0], pair[1], -1, sample % 2 == 0);
            setString.call(pair[0], pair[1], 0, Integer.toString(random.nextInt()));
            setString.call(pair[0], pair[1], 1, names[sample % names.length]);
            lower.call(pair[0], pair[1]);
            setVisible.call(pair[0], pair[1], random.nextInt());
            setInvisible.call(pair[0], pair[1], random.nextInt());
            setIgnored.call(pair[0], pair[1], random.nextInt());
            int size = new int[]{0, 1, 17, 32767, 32768, 65535, 65536, 65537}[sample % 8];
            byte[] roster = sample % 9 == 0 ? null : new byte[size];
            if (roster != null) random.nextBytes(roster);
            setIP.call(pair[0], pair[1], 227, roster);
            byte[] encoded = serialize(pair, -1);
            if (!(save.lastOld instanceof UtilCoreDifferentialTest.Failure)) {
                // The record's type byte belongs to the caller, not loadFromStream.
                byte[] payload = Arrays.copyOfRange(encoded, 1, encoded.length);
                for (int chunk : new int[]{1, 2, 17, Integer.MAX_VALUE}) {
                    Object[] restored = contacts();
                    setString.call(restored[0], restored[1], 1, "previous cached name");
                    lower.call(restored[0], restored[1]);
                    deserialize(restored, payload, chunk, -1);
                    lower.call(restored[0], restored[1]);
                    serialize(restored, -1);
                }
            }
            for (int failAt : new int[]{0, 1, 4, 5, 9, 10, 15, 30, encoded.length}) serialize(pair, failAt);
        }
        for (int sample = 0; sample < 180; sample++) {
            byte[] bytes = new byte[sample % 80]; random.nextBytes(bytes);
            for (int chunk : new int[]{1, 7, Integer.MAX_VALUE}) {
                Object[] pair = contacts();
                setString.call(pair[0], pair[1], 1, "existing");
                setIP.call(pair[0], pair[1], 227, new byte[]{1, 2, 3});
                deserialize(pair, bytes, chunk, sample % 3 == 0 ? sample % (bytes.length + 1) : -1);
            }
        }
        Object[] pair = contacts();
        save.call(pair[0], pair[1], (Object)null); saves++;
        load.call(pair[0], pair[1], (Object)null); loads++; snapshot(pair, "null streams");
    }

    static void monitor(final Method method, final Object receiver, String label) throws Exception {
        final Object pending = new Object();
        final AtomicReference<Object> result = new AtomicReference<Object>(pending);
        final CountDownLatch started = new CountDownLatch(1);
        Thread worker = new Thread(new Runnable() { public void run() {
            started.countDown();
            try { result.set(invoke(method, receiver, new Object[]{256})); }
            catch (Throwable error) { result.set(error); }
        }}, "contact-capability-monitor");
        worker.setDaemon(true);
        boolean blocked = false;
        synchronized (receiver) {
            worker.start();
            if (!started.await(2, TimeUnit.SECONDS)) throw new AssertionError("Monitor worker did not start");
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            ThreadMXBean threads = ManagementFactory.getThreadMXBean();
            while (System.nanoTime() < deadline) {
                ThreadInfo info = threads.getThreadInfo(worker.getId());
                if (info != null && info.getThreadState() == Thread.State.BLOCKED
                        && info.getLockOwnerId() == Thread.currentThread().getId()
                        && info.getLockInfo().getIdentityHashCode() == System.identityHashCode(receiver)) {
                    blocked = true; break;
                }
                if (result.get() != pending) break;
                Thread.sleep(1);
            }
        }
        worker.join(2000);
        equal(worker.isAlive(), false, label + " completed after monitor release");
        equal(blocked, true, label + " blocked on the actual contact monitor");
        equal(result.get(), Boolean.TRUE, label + " capability result");
    }

    static void monitor() throws Exception {
        Object[] pair = contacts(); setInt.call(pair[0], pair[1], 75, 256);
        monitor(cap.old, pair[0], "original"); monitor(cap.fresh, pair[1], "source");
    }

    public static void main(String[] args) throws Exception {
        Path libs = Paths.get(args[2]);
        ClassLoader oldLoader = UtilCoreDifferentialTest.loader(Paths.get(args[0]), libs);
        ClassLoader newLoader = UtilCoreDifferentialTest.loader(Paths.get(args[1]), libs);
        options(oldLoader, "cj"); options(newLoader, "jimm.Options");
        oldContact = Class.forName("z", true, oldLoader); newContact = Class.forName("jimm.ContactItem", true, newLoader);
        setString = new Pair("a", "setStringValue", void.class, int.class, String.class);
        getString = new Pair("a", "getStringValue", String.class, int.class);
        setInt = new Pair("a", "setIntValue", void.class, int.class, int.class);
        getInt = new Pair("b", "getIntValue", int.class, int.class);
        setBool = new Pair("a", "setBooleanValue", void.class, int.class, boolean.class);
        getBool = new Pair("a", "getBooleanValue", boolean.class, int.class);
        lower = new Pair("a", "getSortText", String.class);
        setIP = new Pair("a", "setIPValue", void.class, int.class, byte[].class);
        getIP = new Pair("a", "getIPValue", byte[].class, int.class);
        toIP = new Pair("b", "longIPToByteAray", byte[].class, int.class);
        fromIP = new Pair("a", "arrayToLongIP", int.class, byte[].class);
        cap = new Pair("b", "hasCapability", boolean.class, int.class);
        addCap = new Pair("a$13462e", "addCapability", void.class, new Class<?>[0], new Class<?>[]{int.class});
        unread = new Pair("e", "getUnreadMessCount", int.class);
        increase = new Pair("b", "increaseMessageCount", void.class, int.class);
        reset = new Pair("a", "resetUnreadMessages", void.class);
        available = new Pair("c", "isMessageAvailable", boolean.class, int.class);
        visible = new Pair("m", "getVisibleId", int.class);
        invisible = new Pair("n", "getInvisibleId", int.class);
        ignored = new Pair("l", "getIgnoreId", int.class);
        setVisible = new Pair("e", "setVisibleId", void.class, int.class);
        setInvisible = new Pair("f", "setInvisibleId", void.class, int.class);
        setIgnored = new Pair("d", "setIgnoreId", void.class, int.class);
        save = new Pair("a", "saveToStream", void.class, DataOutputStream.class);
        load = new Pair("a", "loadFromStream", void.class, DataInputStream.class);
        constructors(); properties(); ipAndCounters(); streams(); monitor();
        if (constructorFailures != 80 || constructed < 600 || saves < 800 || loads < 600)
            throw new AssertionError("Missing successful constructor or stream paths");
        List<String> lines = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : coverage.entrySet()) lines.add(entry.getValue() + " " + entry.getKey());
        Files.write(Paths.get(args[3]), lines);
        System.out.println("PASS contact core: " + observations + " observations, " + coverage.size()
                           + " real method pairs, " + constructorCalls + " constructor pairs");
        System.out.println("constructed:" + constructed + "/constructorFailures:" + constructorFailures
                           + "/saves:" + saves + "/loads:" + loads);
        System.exit(0);
    }
}
