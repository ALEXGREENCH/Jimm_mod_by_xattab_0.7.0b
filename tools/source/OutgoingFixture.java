import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Fix message-ID time and capture scheduling of the accepted file transfer. */
public class OutgoingFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String icq=ref?"r":"jimm/comm/Icq",error=ref?"bv":"jimm/JimmException";String send=ref?"cl":"jimm/comm/SendMessageAction",splash=ref?"cv":"jimm/SplashCanvas";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(send)||owner.equals(splash)||owner.equals(icq)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(splash)&&name.equals(ref?"a":"addTimerTask")&&desc.equals("(Ljava/lang/String;L"+(ref?"aa":"jimm/comm/Action")+";Z)V")){
                                MessageFixture.capture(m,"file-timer",desc);return null;
                            }
                            if(owner.equals(icq)&&name.equals(ref?"a":"requestAction")&&desc.equals("(L"+(ref?"aa":"jimm/comm/Action")+";)V")){
                                return new MethodVisitor(ASM9,m){public void visitCode(){super.visitCode();
                                    Label success=new Label(),noncritical=new Label();visitFieldInsn(GETSTATIC,"LoginIO","failures","I");visitJumpInsn(IFEQ,success);
                                    visitFieldInsn(GETSTATIC,"LoginIO","failures","I");visitInsn(ICONST_2);visitJumpInsn(IF_ICMPNE,noncritical);
                                    visitTypeInsn(NEW,error);visitInsn(DUP);visitIntInsn(BIPUSH,120);visitInsn(ICONST_0);visitMethodInsn(INVOKESPECIAL,error,"<init>","(II)V",false);visitInsn(ATHROW);
                                    visitLabel(noncritical);visitTypeInsn(NEW,error);visitInsn(DUP);visitIntInsn(BIPUSH,120);visitInsn(ICONST_0);visitInsn(ICONST_1);visitMethodInsn(INVOKESPECIAL,error,"<init>","(IIZ)V",false);visitInsn(ATHROW);visitLabel(success);
                                }};
                            }
                            if(!owner.equals(send))return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(target.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                    else super.visitMethodInsn(op,target,name,desc,itf);
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
