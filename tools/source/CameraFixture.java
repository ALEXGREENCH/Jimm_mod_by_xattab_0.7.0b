import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Only replace the MMAPI factory and GC in the camera class; keep its state machine. */
public class CameraFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String camera=ref?"cc":"jimm/FileTransfer$ViewFinder";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(camera+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,exceptions)){
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                                    if(owner.equals("javax/microedition/media/Manager")&&name.equals("createPlayer"))super.visitMethodInsn(INVOKESTATIC,"CameraIO",name,desc,false);
                                    else if(owner.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                                    else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"CameraIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);
            out.putNextEntry(new JarEntry("CameraIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
