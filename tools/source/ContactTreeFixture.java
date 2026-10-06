import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Retain roster/tree code; make its clock and persistence boundaries deterministic. */
public class ContactTreeFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String list=ref?"m":"jimm/ContactList";
        String intermediate=args[1]+".menus.jar";
        ListMenuFixture.main(new String[]{args[0],intermediate,args[2],args[3],args[4],"0"});
        try(JarFile in=new JarFile(intermediate);JarFile original=new JarFile(args[4]);
            JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(list+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            if(name.equals(ref?"f":"safeSave")&&desc.equals("()V"))return null;
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(name.equals(ref?"b":"save")&&desc.equals("()V")) {
                                MessageFixture.capture(m,"tree-save",desc);return null;
                            }
                            return new MethodVisitor(ASM9,m) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    if(owner.equals("java/lang/System")&&name.equals("currentTimeMillis"))
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                    else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);
                    new ClassReader(original.getInputStream(original.getJarEntry(e.getName())).readAllBytes()).accept(new ClassVisitor(ASM9) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            if(name.equals(ref?"f":"safeSave")&&desc.equals("()V"))
                                return w.visitMethod(access,name,desc,sig,exceptions);
                            return null;
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
