import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Schedule the profile worker explicitly; capture the progress screen and fix the save-action clock. */
public class ProfileFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");
        String editor=ref?"br":"jimm/EditInfo",save=ref?"as":"jimm/comm/SaveInfoAction",splash=ref?"cv":"jimm/SplashCanvas";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(editor)||owner.equals(save)||owner.equals(splash)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(splash)&&name.equals(ref?"a":"addTimerTask")&&desc.equals("(Ljava/lang/String;L"+(ref?"aa":"jimm/comm/Action")+";Z)V")){
                                MessageFixture.capture(m,"profile-timer",desc);return null;
                            }
                            if(owner.equals(splash))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(owner.equals(editor)&&target.equals("java/lang/Thread")&&name.equals("start")){super.visitInsn(POP);
                                    super.visitLdcInsn("profile-worker");super.visitInsn(ICONST_0);super.visitTypeInsn(ANEWARRAY,"java/lang/Object");
                                    super.visitMethodInsn(INVOKESTATIC,"MessageIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);return;}
                                if(target.equals("java/lang/Thread")&&name.equals("sleep")){super.visitMethodInsn(INVOKESTATIC,"LoginIO","sleep",desc,false);return;}
                                if(target.equals("java/lang/System")&&name.equals("currentTimeMillis")){super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);return;}
                                if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){
                                    super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);desc="(J)V";
                                }
                                super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
