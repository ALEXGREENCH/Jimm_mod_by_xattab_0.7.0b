import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture socket I/O and scheduling, with the actual error object and actual receiver. */
public class SocketFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String socket=ref?"bf":"jimm/comm/Icq$SOCKETConnection",error=ref?"bv":"jimm/JimmException";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();int patched=0;
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(socket)||owner.equals(error)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(owner.equals(error)){
                                if(n.equals(ref?"a":"handleException")&&d.equals("(L"+error+";)V")){
                                    m.visitCode();m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKESTATIC,"SocketIO","error","(Ljava/lang/Object;)V",false);m.visitInsn(RETURN);m.visitMaxs(1,1);m.visitEnd();return null;
                                }return m;
                            }
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("javax/microedition/io/Connector")&&name.equals("open"))super.visitMethodInsn(INVOKESTATIC,"SocketIO",name,desc,false);
                                else if(target.equals("java/lang/Thread")&&name.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"SocketIO",name,desc,false);
                                else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"SocketIO",name,"(Ljava/lang/Thread;)V",false);
                                else if(target.equals("java/lang/Thread")&&name.equals("yield"))super.visitMethodInsn(INVOKESTATIC,"SocketIO","yieldThread",desc,false);
                                else if(target.equals("java/lang/Object")&&name.equals("notify")&&desc.equals("()V"))super.visitMethodInsn(INVOKESTATIC,"SocketIO","signal","(Ljava/lang/Object;)V",false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();patched++;
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(patched!=2)throw new AssertionError("socket fixture owners missing");
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"SocketIO*.class")){for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}}
        }
    }
}
