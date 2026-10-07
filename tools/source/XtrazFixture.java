import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Fix Xtraz request time and observe its chat/log destinations, retaining packet and XML logic. */
public class XtrazFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String xtraz=ref?"ba":"jimm/comm/XtrazSM",chat=ref?"bt":"jimm/ChatHistory",magic=ref?"ah":"jimm/util/MagicEye",contact=ref?"z":"jimm/ContactItem";int[] captured={0};
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();String owner=entry.getName().replace(".class","");byte[] bytes=in.getInputStream(entry).readAllBytes();
                if(owner.equals(xtraz)||owner.equals(chat)||owner.equals(magic)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,errors);
                            if(owner.equals(chat)&&name.equals(ref?"a":"newChatForm")&&desc.equals("(L"+contact+";Ljava/lang/String;)V")){MessageFixture.capture(m,"xtraz-chat",desc);captured[0]++;return null;}
                            if(owner.equals(magic)&&name.equals(ref?"a":"addAction")&&desc.equals("(Ljava/lang/String;Ljava/lang/String;Z)V")){MessageFixture.capture(m,"xtraz-log",desc);captured[0]++;return null;}
                            if(!owner.equals(xtraz))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String n,String d,boolean itf){
                                if(target.equals("java/lang/System")&&n.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",d,false);
                                else super.visitMethodInsn(op,target,n,d,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
        }
        if(captured[0]!=2)throw new AssertionError("Missing Xtraz destinations: "+captured[0]);
    }
}
