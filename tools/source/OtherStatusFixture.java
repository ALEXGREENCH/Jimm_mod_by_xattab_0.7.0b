import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Replace only the clock read in real Util.createRandomId; retain all subject bodies. */
public class OtherStatusFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        boolean ref=args[3].equals("reference");String util=ref?"co":"jimm/comm/Util";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(util+".class")) {
                    ClassWriter w=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(ref?"b":"createRandomId")||!d.equals("()I"))return m;
                            return new MethodVisitor(ASM9,m) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    if(op==INVOKESTATIC&&owner.equals("java/lang/System")&&name.equals("currentTimeMillis")&&desc.equals("()J"))
                                        super.visitMethodInsn(INVOKESTATIC,"OtherStatusClock","time",desc,false);
                                    else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            GraphicsFixture.entry(out,"OtherStatusClock.class",Files.readAllBytes(Paths.get(args[2],"OtherStatusClock.class")));
        }
    }
}
