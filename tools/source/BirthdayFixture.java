import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Actual birthday/RMS/date logic; only Date, worker launch and sleeping are captured. */
public class BirthdayFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String birthday=ref?"cs":"jimm/util/NoticeOnBirthDay",util=ref?"co":"jimm/comm/Util";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");byte[] bytes=in.getInputStream(e).readAllBytes();
                if(owner.equals(birthday)||owner.equals(util)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] errors){MethodVisitor m=super.visitMethod(a,n,d,s,errors);
                        if(!owner.equals(birthday)&&!(n.equals(ref?"a":"createCurrentDate")&&d.equals("(ZZ)J")))return m;
                        return new MethodVisitor(ASM9,m){public void visitTypeInsn(int op,String target){super.visitTypeInsn(op,op==NEW&&target.equals("java/util/Date")?"BirthdayIO$Clock":target);}
                            public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V"))super.visitMethodInsn(op,"BirthdayIO$Clock",name,desc,itf);
                                else if(target.equals("java/lang/Thread")&&name.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"BirthdayIO","sleep",desc,false);
                                else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"BirthdayIO","start","(Ljava/lang/Thread;)V",false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                    }},0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"BirthdayIO*.class")){for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}}
        }
    }
}
