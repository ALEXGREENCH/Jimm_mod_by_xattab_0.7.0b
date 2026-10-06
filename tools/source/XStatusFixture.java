import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep selector, editor, options, RMS and splash icon logic; capture surrounding routes/network/light. */
public class XStatusFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String menu=ref?"ag":"jimm/MainMenu",other=ref?"bq":"jimm/comm/OtherAction",light=ref?"aj":"DrawControls/LightControl";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(menu)||owner.equals(other)||owner.equals(light)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String event=null;
                            if(owner.equals(menu)&&desc.equals("()V")){
                                if(name.equals(ref?"a":"build"))event="xstatus-menu-build";
                                if(name.equals(ref?"b":"activate"))event="xstatus-menu-activate";
                            }
                            if(owner.equals(other)&&name.equals(ref?"a":"setStandartUserInfo")&&desc.equals("(Z)V"))event="xstatus-info";
                            if(owner.equals(other)&&name.equals(ref?"a":"setStatus")&&desc.equals("(I)V"))event="xstatus-status";
                            if(owner.equals(light)&&name.equals(ref?"a":"flash")&&desc.equals("(Z)V"))event="xstatus-light";
                            if(event!=null){MessageFixture.capture(m,event,desc);return null;}return m;
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
