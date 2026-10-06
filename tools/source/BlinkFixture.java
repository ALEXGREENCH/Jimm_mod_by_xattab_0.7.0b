import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Preserve the contact and its timer body, replacing scheduling and repaint notifications only. */
public class BlinkFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String contact=ref?"z":"jimm/ContactItem",list=ref?"m":"jimm/ContactList";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(contact)||owner.equals(list)||(owner.equals("ch")&&ref)||owner.startsWith("jimm/ContactItem$")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(list)){
                                if(name.equals(ref?"g":"repaintTree")&&desc.equals("()V")){MessageFixture.capture(m,"blink-repaint",desc);return null;}
                                if(name.equals(ref?"a":"contactChanged")&&desc.equals("(L"+contact+";ZZ)V")){MessageFixture.capture(m,"blink-contact",desc);return null;}return m;
                            }
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/util/Timer")&&name.equals("schedule")&&desc.equals("(Ljava/util/TimerTask;JJ)V"))super.visitMethodInsn(INVOKESTATIC,"BlinkIO","schedule","(Ljava/util/Timer;Ljava/util/TimerTask;JJ)V",false);
                                else if(target.equals("java/util/TimerTask")&&name.equals("cancel"))super.visitMethodInsn(INVOKESTATIC,"BlinkIO","cancel","(Ljava/util/TimerTask;)Z",false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"BlinkIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);
            out.putNextEntry(new JarEntry("BlinkIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
