import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture only the platformRequest calls in the real JimmUI controller. */
public class JimmUrlFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        boolean ref = args[2].equals("reference");
        String ui = ref ? "cf" : "jimm/JimmUI";
        final int[] calls = {0};
        final int[] gotoCalls = {0};
        try (JarFile in = new JarFile(args[0]); JarOutputStream out = new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries = in.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().equals("JimmUrlIO.class")) continue;
                byte[] bytes = in.getInputStream(entry).readAllBytes();
                if (entry.getName().equals(ui + ".class")) {
                    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9, writer) {
                        public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] exceptions) {
                            MethodVisitor target = super.visitMethod(access, name, desc, sig, exceptions);
                            final boolean isGoto = name.equals(ref ? "a" : "gotoURL") && desc.equals("(Ljava/lang/String;Ljava/lang/Object;)V");
                            return new MethodVisitor(ASM9, target) {
                                public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                                    if (name.equals("platformRequest") && desc.equals("(Ljava/lang/String;)Z")) {
                                        if (opcode != INVOKEVIRTUAL || (!owner.equals("jimm/Jimm") && !owner.equals("javax/microedition/midlet/MIDlet")))
                                            throw new AssertionError("Unexpected platformRequest: " + owner);
                                        calls[0]++; if (isGoto) gotoCalls[0]++;
                                        super.visitMethodInsn(INVOKESTATIC, "JimmUrlIO", "request", "(Ljava/lang/Object;Ljava/lang/String;)Z", false);
                                    } else super.visitMethodInsn(opcode, owner, name, desc, itf);
                                }
                            };
                        }
                    }, 0);
                    bytes = writer.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName())); out.write(bytes); out.closeEntry();
            }
            out.putNextEntry(new JarEntry("JimmUrlIO.class"));
            out.write(Files.readAllBytes(Paths.get(args[3], "JimmUrlIO.class"))); out.closeEntry();
        }
        if (gotoCalls[0] != 1 || calls[0] < 2) throw new AssertionError("Missing platformRequest interception: " + Arrays.toString(calls));
        System.out.println("PASS URL fixture: " + calls[0] + " terminal calls, " + gotoCalls[0] + " inside gotoURL");
    }
}
