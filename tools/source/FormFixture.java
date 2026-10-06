import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep the actual form/model/renderer; capture native display and explicit GC only. */
public class FormFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String form=ref?"d":"DrawControls/VirtualForm",fixed=args[1]+".list.jar";
        VirtualListFixture.main(new String[]{args[0],fixed,args[2],args[3]});
        try(JarFile in=new JarFile(fixed);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(form+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,exceptions)) {
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf) {
                                    if(target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"MenuIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                                    else if(target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                                    else super.visitMethodInsn(op,target,name,desc,itf);
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
