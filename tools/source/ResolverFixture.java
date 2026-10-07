import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture exactly one external Connector call; retain resolver instructions and handlers. */
public class ResolverFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            for(JarEntry e:Collections.list(in.entries())) {
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(args[3]+".class")) {
                    ClassWriter w=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor mv=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(args[4])||!d.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))return mv;
                            return new MethodVisitor(ASM9,mv) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    if(op==INVOKESTATIC&&owner.equals("javax/microedition/io/Connector")&&name.equals("open")&&desc.equals("(Ljava/lang/String;I)Ljavax/microedition/io/Connection;"))
                                        super.visitMethodInsn(op,"ResolverIO",name,desc,false);
                                    else super.visitMethodInsn(op,owner,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[2]),"ResolverIO*.class")) {
                for(Path p:files)GraphicsFixture.entry(out,p.getFileName().toString(),Files.readAllBytes(p));
            }
        }
    }
}
