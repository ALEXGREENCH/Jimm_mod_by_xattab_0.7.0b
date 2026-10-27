import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Observe entry to OtherAction initialization; preserve the status method. */
public class OnlineStatusInitializationFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        boolean restorePrefix=args.length>4&&args[4].equals("restore-prefix");
        String owner=restorePrefix?"jimm/comm/Icq":args[3].equals("reference")?"bq":"jimm/comm/OtherAction";
        boolean undo=args.length>4&&args[4].equals("undo");
        int[] captured={0};
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(owner+".class")||restorePrefix&&e.getName().equals("jimm/comm/OtherAction.class")) {
                    ClassWriter writer=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public FieldVisitor visitField(int a,String n,String d,String s,Object value) {
                            if(restorePrefix&&e.getName().equals("jimm/comm/OtherAction.class")&&n.equals("CLI_SETSTATUS_DATA")&&d.equals("[B"))
                                a=(a&~ACC_PRIVATE)|ACC_PUBLIC;
                            return super.visitField(a,n,d,s,value);
                        }
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor mv=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(restorePrefix?"setOnlineStatus":"<clinit>")||!d.equals(restorePrefix?"(I)V":"()V"))return mv;
                            captured[0]++;
                            return new MethodVisitor(ASM9,mv) {
                                public void visitCode() {
                                    super.visitCode();
                                    if(restorePrefix) {
                                        super.visitFieldInsn(GETSTATIC,"jimm/comm/OtherAction","CLI_SETSTATUS_DATA","[B");
                                        super.visitInsn(POP);
                                    } else if(!undo)super.visitMethodInsn(INVOKESTATIC,"OnlineStatusInitializationEvents","initialized","()V",false);
                                }
                                public void visitMethodInsn(int op,String c,String n,String d,boolean itf) {
                                    if(undo&&op==INVOKESTATIC&&c.equals("OnlineStatusInitializationEvents")&&n.equals("initialized")&&d.equals("()V"))return;
                                    super.visitMethodInsn(op,c,n,d,itf);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();
                }
                if(!undo||!e.getName().equals("OnlineStatusInitializationEvents.class"))GraphicsFixture.entry(out,e.getName(),bytes);
            }
            if(!undo&&!restorePrefix)GraphicsFixture.entry(out,"OnlineStatusInitializationEvents.class",Files.readAllBytes(Paths.get(args[2],"OnlineStatusInitializationEvents.class")));
        }
        if(captured[0]!=1)throw new AssertionError("Expected one initializer: "+captured[0]);
    }
}
