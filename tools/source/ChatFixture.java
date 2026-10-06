import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Restore unmodified ChatHistory from the input JAR; capture only surrounding UI routes. */
public class ChatFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String chat=ref?"bt":"jimm/ChatHistory",ui=ref?"cf":"jimm/JimmUI",contact=ref?"z":"jimm/ContactItem",light=ref?"aj":"DrawControls/LightControl";
        try(JarFile in=new JarFile(args[0]);JarFile original=new JarFile(args[4]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");
                byte[] bytes=owner.equals(chat)?original.getInputStream(original.getJarEntry(e.getName())).readAllBytes():in.getInputStream(e).readAllBytes();
                if(owner.equals(ui)||owner.equals(light)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String label=null;
                            if(owner.equals(light)){
                                if(name.equals(ref?"a":"flash")&&desc.equals("(Z)V")){MessageFixture.capture(m,"chat-light",desc);return null;}return m;
                            }
                            if(name.equals(ref?"a":"showPopupWindow")&&desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))label="chat-popup";
                            if(name.equals(ref?"a":"showCreepingLine")&&desc.equals("(Ljava/lang/Object;L"+contact+";Ljava/lang/String;)V"))label="chat-creeping";
                            if(name.equals(ref?"a":"writeMessage")&&desc.equals("(L"+contact+";Ljava/lang/String;)V"))label="chat-write";
                            if(name.equals(ref?"a":"authMessage")&&desc.equals("(IL"+contact+";Ljava/lang/String;Ljava/lang/String;)V"))label="chat-auth";
                            if(name.equals(ref?"a":"showContactMenu")&&desc.equals("(L"+contact+";)V"))label="chat-contact";
                            if(name.equals(ref?"b":"addUser")&&desc.equals("(L"+contact+";)V"))label="chat-add";
                            if(label!=null){MessageFixture.capture(m,label,desc);return null;}return m;
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
