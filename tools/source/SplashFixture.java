import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Deterministic date text and an inert Action; all splash/controller code is original. */
public class SplashFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        boolean ref = args[2].equals("reference");
        String action = ref ? "aa" : "jimm/comm/Action";
        String util = ref ? "co" : "jimm/comm/Util";
        byte[] actionBytes;
        try (JarFile input = new JarFile(args[0]); JarOutputStream out = new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            actionBytes = input.getInputStream(input.getJarEntry(action + ".class")).readAllBytes();
            Enumeration<JarEntry> entries = input.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                byte[] bytes = input.getInputStream(entry).readAllBytes();
                if (entry.getName().equals(util + ".class")) {
                    ClassWriter writer = new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9, writer) {
                        public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] exceptions) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, sig, exceptions);
                            String date = name.equals(ref ? "a" : "getDateString") && desc.equals("(ZZ)Ljava/lang/String;") ? "12.05.2010 12:00" :
                                name.equals(ref ? "a" : "getCurrentDay") && desc.equals("()Ljava/lang/String;") ? "Wednesday" : null;
                            if (date == null) return mv;
                            mv.visitCode(); mv.visitLdcInsn(date); mv.visitInsn(ARETURN); mv.visitMaxs(1, Type.getArgumentTypes(desc).length); mv.visitEnd();
                            return null;
                        }
                    }, 0);
                    bytes = writer.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName())); out.write(bytes); out.closeEntry();
            }
            ClassWriter writer = new ClassWriter(0);
            writer.visit(V1_5, ACC_PUBLIC, "RecoveryAction", null, action, null);
            writer.visitField(ACC_PUBLIC, "events", "I", null, null).visitEnd();
            writer.visitField(ACC_PUBLIC, "completed", "Z", null, null).visitEnd();
            writer.visitField(ACC_PUBLIC, "error", "Z", null, null).visitEnd();
            MethodVisitor init = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            init.visitCode(); init.visitVarInsn(ALOAD,0); init.visitInsn(ICONST_0); init.visitInsn(ICONST_0);
            init.visitMethodInsn(INVOKESPECIAL,action,"<init>","(ZZ)V",false); init.visitInsn(RETURN); init.visitMaxs(3,1); init.visitEnd();
            new ClassReader(actionBytes).accept(new ClassVisitor(ASM9) {
                public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] exceptions) {
                    boolean event = name.equals(ref ? "a" : "onEvent") && desc.equals("(I)V");
                    if ((access & ACC_ABSTRACT) == 0 && !event) return null;
                    MethodVisitor mv = writer.visitMethod(ACC_PUBLIC,name,desc,null,exceptions);
                    mv.visitCode();
                    if (event) {
                        mv.visitVarInsn(ALOAD,0); mv.visitInsn(DUP); mv.visitFieldInsn(GETFIELD,"RecoveryAction","events","I");
                        mv.visitIntInsn(BIPUSH,10); mv.visitInsn(IMUL); mv.visitVarInsn(ILOAD,1); mv.visitInsn(IADD);
                        mv.visitFieldInsn(PUTFIELD,"RecoveryAction","events","I"); mv.visitInsn(RETURN);
                    } else if (desc.equals("()Z") && name.equals(ref ? "a" : "isCompleted")) {
                        mv.visitVarInsn(ALOAD,0);mv.visitFieldInsn(GETFIELD,"RecoveryAction","completed","Z");mv.visitInsn(IRETURN);
                    } else if (desc.equals("()Z") && name.equals(ref ? "b" : "isError")) {
                        mv.visitVarInsn(ALOAD,0);mv.visitFieldInsn(GETFIELD,"RecoveryAction","error","Z");mv.visitInsn(IRETURN);
                    } else if (Type.getReturnType(desc).equals(Type.VOID_TYPE)) mv.visitInsn(RETURN);
                    else { mv.visitInsn(ICONST_0); mv.visitInsn(IRETURN); }
                    mv.visitMaxs(4, 1 + Type.getArgumentTypes(desc).length); mv.visitEnd(); return null;
                }
            }, ClassReader.SKIP_CODE);
            writer.visitEnd(); out.putNextEntry(new JarEntry("RecoveryAction.class")); out.write(writer.toByteArray()); out.closeEntry();
        }
        System.out.println("PASS splash fixture: fixed date text and inert action");
    }
}
