import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep every application instruction except one enumerated ChoiceGroup getter call. */
public class FormChoicesFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();int sites=0;
            while(entries.hasMoreElements()) {
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();
                if(entry.getName().equals(args[3]+".class")) {
                    final int[] changes={0};ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(args[4])||!d.equals(args[5]))return m;
                            return new MethodVisitor(ASM9,m) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/ChoiceGroup")&&name.equals("getString")&&desc.equals("(I)Ljava/lang/String;")) {
                                        super.visitMethodInsn(INVOKESTATIC,"FormChoicesIO","getString","(Ljavax/microedition/lcdui/ChoiceGroup;I)Ljava/lang/String;",false);changes[0]++;
                                    }else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();sites+=changes[0];
                }
                GraphicsFixture.entry(out,entry.getName(),bytes);
            }
            if(sites!=1)throw new AssertionError("Expected exactly one selected getter call: "+sites);
            GraphicsFixture.entry(out,"FormChoicesIO.class",Files.readAllBytes(Paths.get(args[2],"FormChoicesIO.class")));
        }
    }
}
