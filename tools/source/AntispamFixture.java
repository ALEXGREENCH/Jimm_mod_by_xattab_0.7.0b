import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Preserve subject code; capture the external Date constructor and message-ID clock only. */
public class AntispamFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        Properties spec=new Properties();try(java.io.InputStream in=Files.newInputStream(Paths.get(args[3]))){spec.load(in);}
        String util=spec.getProperty("util").replace('.','/'),send=spec.getProperty("send").replace('.','/');boolean clocked=Boolean.parseBoolean(args[4]);
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            for(JarEntry e:Collections.list(in.entries())) {
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(clocked&&(e.getName().equals(util+".class")||e.getName().equals(send+".class"))) {
                    final boolean date=e.getName().equals(util+".class");ClassWriter w=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!(date&&n.equals(spec.getProperty("dateMethod"))&&d.equals(spec.getProperty("dateDesc"))||!date&&n.equals("<init>")&&d.equals(spec.getProperty("sendCtorDesc"))))return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitTypeInsn(int op,String owner){if(date&&op==NEW&&owner.equals("java/util/Date"))owner="AntispamIO$Clock";super.visitTypeInsn(op,owner);}
                                public void visitMethodInsn(int op,String owner,String n,String d,boolean itf){
                                    if(date&&op==INVOKESPECIAL&&owner.equals("java/util/Date")&&n.equals("<init>")&&d.equals("()V"))owner="AntispamIO$Clock";
                                    if(!date&&op==INVOKESTATIC&&owner.equals("java/lang/System")&&n.equals("currentTimeMillis")&&d.equals("()J")){owner="AntispamIO";n="time";}
                                    super.visitMethodInsn(op,owner,n,d,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[2]),"AntispamIO*.class")) {
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}
            }
        }
    }
}
