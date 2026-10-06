import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Substitute transport boundaries, scheduling and error delivery; retain protocol bodies. */
public class TransportFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        boolean ref=args[2].equals("reference");
        String http=ref?"ay":"jimm/comm/Icq$HTTPConnection",socks=ref?"cb":"jimm/comm/Icq$SOCKSConnection";
        String error=ref?"bv":"jimm/JimmException",handler=ref?"a":"handleException";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();int patched=0;
            while(entries.hasMoreElements()) {
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();
                if(entry.getName().equals(http+".class")||entry.getName().equals(socks+".class")||entry.getName().equals(error+".class")) {
                    boolean isError=entry.getName().equals(error+".class");ClassWriter writer=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            MethodVisitor mv=super.visitMethod(access,name,desc,sig,exceptions);
                            if(isError && name.equals(handler) && desc.equals("(L"+error+";)V")) {
                                mv.visitCode();mv.visitVarInsn(ALOAD,0);mv.visitFieldInsn(PUTSTATIC,"TransportIO","lastError","Ljava/lang/Object;");
                                mv.visitFieldInsn(GETSTATIC,"TransportIO","errors","I");mv.visitInsn(ICONST_1);mv.visitInsn(IADD);
                                mv.visitFieldInsn(PUTSTATIC,"TransportIO","errors","I");mv.visitInsn(RETURN);mv.visitMaxs(2,1);mv.visitEnd();return null;
                            }
                            if(isError)return mv;
                            return new MethodVisitor(ASM9,mv) {
                                public void visitMethodInsn(int opcode,String owner,String name,String desc,boolean itf) {
                                    if(owner.equals("javax/microedition/io/Connector")&&name.equals("open")&&desc.equals("(Ljava/lang/String;I)Ljavax/microedition/io/Connection;"))
                                        super.visitMethodInsn(INVOKESTATIC,"TransportIO","open",desc,false);
                                    else if(owner.equals("java/lang/Thread")&&name.equals("sleep"))
                                        super.visitMethodInsn(INVOKESTATIC,"TransportIO","sleep",desc,false);
                                    else if(owner.equals("java/lang/Thread")&&name.equals("start"))
                                        super.visitMethodInsn(INVOKESTATIC,"TransportIO","start","(Ljava/lang/Thread;)V",false);
                                    else if(name.equals("wait")&&desc.equals("()V"))
                                        super.visitMethodInsn(INVOKESTATIC,"TransportIO","await","(Ljava/lang/Object;)V",false);
                                    else super.visitMethodInsn(opcode,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();patched++;
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
            if(patched!=3)throw new AssertionError("Transport fixture classes missing");
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"TransportIO*.class")) {
                for(Path path:files) {
                    ClassWriter writer=new ClassWriter(0);
                    new ClassReader(Files.readAllBytes(path)).accept(new ClassVisitor(ASM9,writer) {
                        public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}
                    },ClassReader.SKIP_FRAMES);
                    out.putNextEntry(new JarEntry(path.getFileName().toString()));out.write(writer.toByteArray());out.closeEntry();
                }
            }
        }
        System.out.println("PASS transport fixture: scripted Connector, scheduling and exception sink");
    }
}
