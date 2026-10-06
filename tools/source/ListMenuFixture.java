import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Restore real ContactList.activate; retain real menu/painting logic and capture sound/GC. */
public class ListMenuFixture implements Opcodes {
    static MethodVisitor boundary(MethodVisitor m) {
        return new MethodVisitor(ASM9,m) {
            public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                if(owner.equals("java/lang/System")&&name.equals("gc"))
                    super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                else super.visitMethodInsn(op,owner,name,desc,itf);
            }
        };
    }
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");
        String list=ref?"m":"jimm/ContactList",vl=ref?"cd":"DrawControls/VirtualList",options=ref?"cj":"jimm/Options",util=ref?"co":"jimm/comm/Util";
        int fontStyle=args.length>5?Integer.parseInt(args[5]):0;
        String intermediate=args[1]+".vl.jar";
        VirtualListFixture.main(new String[]{args[0],intermediate,args[2],args[3]});
        try(JarFile in=new JarFile(intermediate);JarFile original=new JarFile(args[4]);
            JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(owner.equals(list)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            if(name.equals(ref?"a":"activate")&&desc.equals("()V"))return null;
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(name.equals(ref?"b":"playSoundNotification")&&desc.equals("(I)V")) {
                                MessageFixture.capture(m,"list-sound",desc);return null;
                            }
                            return m;
                        }
                    },0);
                    new ClassReader(original.getInputStream(original.getJarEntry(e.getName())).readAllBytes()).accept(new ClassVisitor(ASM9) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            if(name.equals(ref?"a":"activate")&&desc.equals("()V"))
                                return boundary(w.visitMethod(access,name,desc,sig,exceptions));
                            return null;
                        }
                    },0);
                    bytes=w.toByteArray();
                } else if(owner.equals(vl)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(!name.equals("<clinit>"))return m;
                            return new MethodVisitor(ASM9,m) {
                                public void visitCode() {
                                    super.visitCode();super.visitLdcInsn(fontStyle);
                                    super.visitFieldInsn(PUTSTATIC,options,ref?"g":"fontStyle","I");
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                } else if(owner.equals(util)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,exceptions)) {
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf) {
                                    if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")) {
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);
                                        super.visitMethodInsn(op,target,name,"(J)V",itf);
                                    } else super.visitMethodInsn(op,target,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
