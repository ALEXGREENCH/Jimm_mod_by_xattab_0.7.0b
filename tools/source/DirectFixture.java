import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep peer protocol/streams, substitute socket factory, scheduling, clock and final UI message. */
public class DirectFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String peer=ref?"ao":"jimm/comm/Icq$PeerConnection",direct=ref?"bw":"jimm/comm/DirectConnectionAction",list=ref?"m":"jimm/ContactList";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(peer)||owner.equals(direct)||owner.equals(list)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(list)){
                                if(name.equals(ref?"a":"activate")&&desc.equals("(Ljava/lang/String;)V")){MessageFixture.capture(m,"direct-result",desc);return null;}
                                return m;
                            }
                            return new MethodVisitor(ASM9,m){
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);return;
                                    }
                                    if(target.equals("javax/microedition/io/Connector")&&name.equals("open"))super.visitMethodInsn(INVOKESTATIC,"TransportIO",name,desc,false);
                                    else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"TransportIO","start","(Ljava/lang/Thread;)V",false);
                                    else if(target.equals("java/lang/Thread")&&name.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"TransportIO","sleep",desc,false);
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
