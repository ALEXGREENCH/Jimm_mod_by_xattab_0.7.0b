import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep search parsing/forms; fix the clock and capture outgoing navigation/action boundaries. */
public class SearchFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String action=ref?"o":"jimm/comm/SearchAction",splash=ref?"cv":"jimm/SplashCanvas",ui=ref?"cf":"jimm/JimmUI",icq=ref?"r":"jimm/comm/Icq",error=ref?"bv":"jimm/JimmException";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(action)||owner.equals(splash)||owner.equals(ui)||owner.equals(icq)||owner.equals(error)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String label=null;
                            if(owner.equals(splash)&&name.equals(ref?"a":"addTimerTask")&&desc.equals("(Ljava/lang/String;L"+(ref?"aa":"jimm/comm/Action")+";Z)V"))label="search-timer";
                            if(owner.equals(ui)&&name.equals(ref?"a":"writeMessage")&&desc.equals("(L"+(ref?"z":"jimm/ContactItem")+";Ljava/lang/String;)V"))label="search-write";
                            if(owner.equals(ui)&&name.equals(ref?"a":"requiestUserInfo")&&desc.equals("(Ljava/lang/String;Ljava/lang/String;)V"))label="search-info";
                            if(owner.equals(icq)&&name.equals(ref?"a":"addToContactList")&&desc.equals("(L"+(ref?"z":"jimm/ContactItem")+";)V"))label="search-add";
                            if(owner.equals(error)&&name.equals(ref?"a":"handleException")&&desc.equals("(L"+error+";)V"))label="search-error";
                            if(label!=null){MessageFixture.capture(m,label,desc);return null;}
                            if(!owner.equals(action))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
