import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Fix elapsed-time input and capture info/progress boundaries, retaining menu/controller logic. */
public class ContactMenuFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String ui=ref?"cf":"jimm/JimmUI",list=ref?"m":"jimm/ContactList",splash=ref?"cv":"jimm/SplashCanvas";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(ui)||owner.equals(list)||owner.equals(splash)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(splash)&&name.equals(ref?"a":"addTimerTask")&&desc.equals("(Ljava/lang/String;L"+(ref?"aa":"jimm/comm/Action")+";Z)V")){
                                MessageFixture.capture(m,"contact-timer",desc);return null;
                            }
                            if(owner.equals(ui)&&name.equals(ref?"a":"fillUserInfo")&&desc.equals("([Ljava/lang/String;L"+(ref?"bi":"DrawControls/TextList")+";)V")){
                                MessageFixture.capture(m,"client-info",desc);return null;
                            }
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                else if(target.equals("java/util/Timer")&&name.equals("schedule")&&desc.equals("(Ljava/util/TimerTask;J)V")){
                                    // Retain the queued task and delay without letting its timer race the probe.
                                    super.visitMethodInsn(INVOKESTATIC,"ContactMenuFixtureIO","schedule","(Ljava/util/Timer;Ljava/util/TimerTask;J)V",false);
                                }
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter helper=new ClassWriter(0);helper.visit(V1_5,ACC_PUBLIC,"ContactMenuFixtureIO",null,"java/lang/Object",null);
            MethodVisitor m=helper.visitMethod(ACC_PUBLIC|ACC_STATIC,"schedule","(Ljava/util/Timer;Ljava/util/TimerTask;J)V",null,null);
            MessageFixture.capture(m,"contact-schedule","(Ljava/util/Timer;Ljava/util/TimerTask;J)V");helper.visitEnd();
            out.putNextEntry(new JarEntry("ContactMenuFixtureIO.class"));out.write(helper.toByteArray());out.closeEntry();
        }
    }
}
