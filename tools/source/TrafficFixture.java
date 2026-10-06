import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep traffic accounting/RMS and screen construction; fix reset time and capture title/back boundaries. */
public class TrafficFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String screen=ref?"a":"jimm/Traffic$TrafficScreen",list=ref?"m":"jimm/ContactList",ui=ref?"cf":"jimm/JimmUI";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(screen)||owner.equals(list)||owner.equals(ui)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(list)&&name.equals(ref?"a":"updateTitle")&&desc.equals("(I)V")){MessageFixture.capture(m,"traffic-title",desc);return null;}
                            if(owner.equals(ui)&&name.equals(ref?"a":"backToLastScreen")&&desc.equals("()V")){MessageFixture.capture(m,"traffic-back",desc);return null;}
                            if(!owner.equals(screen))return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);
                                    }else super.visitMethodInsn(op,target,name,desc,itf);
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
