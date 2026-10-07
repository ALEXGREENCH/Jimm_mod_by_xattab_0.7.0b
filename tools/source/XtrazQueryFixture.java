import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Change one external clock read; retain the real request, serializer and socket. */
public class XtrazQueryFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        Properties spec=new Properties();try(java.io.InputStream in=Files.newInputStream(Paths.get(args[3]))){spec.load(in);}
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            for(JarEntry e:Collections.list(in.entries())){
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(spec.getProperty("xtraz")+".class")){
                    ClassWriter w=new ClassWriter(0);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(spec.getProperty("query"))||!d.equals(spec.getProperty("queryDesc")))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                                if(op==INVOKESTATIC&&owner.equals("java/lang/System")&&name.equals("currentTimeMillis")&&desc.equals("()J")){owner="XtrazQueryClock";name="time";}
                                super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            GraphicsFixture.entry(out,"XtrazQueryClock.class",Files.readAllBytes(Paths.get(args[2],"XtrazQueryClock.class")));
        }
    }
}
