import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Actual selector and list; supply only the constructor's external viewport width. */
public class SelectorFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String selector=ref?"af":"jimm/util/Selector",list=ref?"cd":"DrawControls/VirtualList",fixed=args[1]+".list.jar";
        VirtualListFixture.main(new String[]{args[0],fixed,args[2],args[3]});
        try(JarFile in=new JarFile(fixed);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(selector+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){MethodVisitor method=super.visitMethod(access,name,desc,sig,errors);if(!name.equals("<init>"))return method;return new MethodVisitor(ASM9,method){
                        public void visitMethodInsn(int op,String target,String name,String desc,boolean itf) {
                            if(target.equals(list)&&name.equals(ref?"j":"getWidth")&&desc.equals("()I"))super.visitMethodInsn(INVOKESTATIC,"SelectorIO","width","(Ljava/lang/Object;)I",false);else super.visitMethodInsn(op,target,name,desc,itf);
                        }
                    };}},0);bytes=w.toByteArray();
                }out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"SelectorIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);out.putNextEntry(new JarEntry("SelectorIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
