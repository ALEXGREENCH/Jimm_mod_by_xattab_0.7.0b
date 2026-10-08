import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Preserve the complete minimize controller except three display calls and its work-screen destination. */
public final class MinimizeFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean reference=args[2].equals("reference");int[] hits=new int[4];
        try(JarFile input=new JarFile(args[0]);JarOutputStream output=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            for(JarEntry entry:Collections.list(input.entries())) {
                byte[] bytes=input.getInputStream(entry).readAllBytes();
                if(entry.getName().equals("jimm/Jimm.class")) {
                    ClassWriter writer=new ClassWriter(0);new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            MethodVisitor visitor=super.visitMethod(access,name,desc,sig,exceptions);
                            if(!name.equals(reference?"a":"setMinimized")||!desc.equals("(Z)V"))return visitor;
                            return new MethodVisitor(ASM9,visitor) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    if(owner.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V")) {
                                        hits[0]++;super.visitMethodInsn(INVOKESTATIC,"MinimizeIO",name,"(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                                    } else if(owner.equals("javax/microedition/lcdui/Display")&&name.equals("getCurrent")&&desc.equals("()Ljavax/microedition/lcdui/Displayable;")) {
                                        hits[1]++;super.visitMethodInsn(INVOKESTATIC,"MinimizeIO",name,"(Ljavax/microedition/lcdui/Display;)Ljavax/microedition/lcdui/Displayable;",false);
                                    } else if(owner.equals("javax/microedition/lcdui/Displayable")&&name.equals("isShown")&&desc.equals("()Z")) {
                                        hits[2]++;super.visitMethodInsn(INVOKESTATIC,"MinimizeIO",name,"(Ljavax/microedition/lcdui/Displayable;)Z",false);
                                    } else if(owner.equals("jimm/Jimm")&&name.equals(reference?"b":"showWorkScreen")&&desc.equals("()V")) {
                                        hits[3]++;super.visitMethodInsn(INVOKESTATIC,"MinimizeIO","work",desc,false);
                                    } else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();
                }
                output.putNextEntry(new JarEntry(entry.getName()));output.write(bytes);output.closeEntry();
            }
            for(String name:new String[]{"MinimizeIO","MinimizeIO$Other","MinimizeIO$Editor"}) {
                output.putNextEntry(new JarEntry(name+".class"));output.write(Files.readAllBytes(Paths.get(args[3],name+".class")));output.closeEntry();
            }
        }
        if(!Arrays.equals(hits,new int[]{1,1,1,1}))throw new AssertionError(Arrays.toString(hits));
        System.out.println("PASS minimize fixture: exactly three display calls and one explicit application destination");
    }
}
