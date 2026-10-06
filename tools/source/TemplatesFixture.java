import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Real templates, lists and RMS; capture native display and the smart-SE device predicate. */
public class TemplatesFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String templates=ref?"aq":"jimm/Templates",ui=ref?"cf":"jimm/JimmUI",fixed=args[1]+".list.jar";
        VirtualListFixture.main(new String[]{args[0],fixed,args[2],args[3]});
        try(JarFile in=new JarFile(fixed);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(templates)||owner.equals(ui)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,errors)){
                        public void visitFieldInsn(int op,String target,String name,String desc) {
                            if(owner.equals(templates)&&op==GETSTATIC&&target.equals("jimm/Jimm")&&name.equals("d")&&desc.equals("Z"))super.visitMethodInsn(INVOKESTATIC,"TemplatesIO","smartSE","()Z",false);else super.visitFieldInsn(op,target,name,desc);
                        }
                        public void visitMethodInsn(int op,String target,String name,String desc,boolean itf) {
                            if(target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"MenuIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                            else if(owner.equals(templates)&&target.equals("jimm/Jimm")&&name.equals("is_smart_SE")&&desc.equals("()Z"))super.visitMethodInsn(INVOKESTATIC,"TemplatesIO","smartSE",desc,false);
                            else super.visitMethodInsn(op,target,name,desc,itf);
                        }
                    };}},0);bytes=w.toByteArray();
                }out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"TemplatesIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);out.putNextEntry(new JarEntry("TemplatesIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
