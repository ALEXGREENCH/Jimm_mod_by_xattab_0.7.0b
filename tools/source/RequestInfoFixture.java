import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Only action clock, profile delivery and contact/chat notification destinations are captured. */
public class RequestInfoFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String request=ref?"ce":"jimm/comm/RequestInfoAction",list=ref?"m":"jimm/ContactList",contact=ref?"z":"jimm/ContactItem",chat=ref?"bt":"jimm/ChatHistory",runner=ref?"l":"jimm/RunnableImpl";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();int patched=0;
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(request)||owner.equals(list)||owner.equals(chat)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                        if(owner.equals(list)&&n.equals(ref?"a":"contactChanged")&&d.equals("(L"+contact+";ZZ)V")){
                            m.visitCode();m.visitVarInsn(ALOAD,0);m.visitVarInsn(ILOAD,1);m.visitVarInsn(ILOAD,2);m.visitMethodInsn(INVOKESTATIC,"RequestInfoIO","changed","(Ljava/lang/Object;ZZ)V",false);m.visitInsn(RETURN);m.visitMaxs(3,3);m.visitEnd();return null;
                        }
                        if(owner.equals(chat)&&n.equals(ref?"b":"contactRenamed")&&d.equals("(Ljava/lang/String;Ljava/lang/String;)V")){
                            m.visitCode();m.visitVarInsn(ALOAD,0);m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKESTATIC,"RequestInfoIO","renamed",d,false);m.visitInsn(RETURN);m.visitMaxs(2,2);m.visitEnd();return null;
                        }
                        if(!owner.equals(request))return m;
                        return new MethodVisitor(ASM9,m){public void visitTypeInsn(int op,String target){super.visitTypeInsn(op,op==NEW&&target.equals("java/util/Date")?"RequestInfoIO$Clock":target);}
                            public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                else if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V"))super.visitMethodInsn(op,"RequestInfoIO$Clock",name,desc,itf);
                                else if(target.equals(runner)&&name.equals(ref?"a":"callSerially")&&desc.equals("(ILjava/lang/Object;)V"))super.visitMethodInsn(INVOKESTATIC,"RequestInfoIO","show",desc,false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                    }},0);bytes=w.toByteArray();patched++;
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(patched!=3)throw new AssertionError("request-info fixture owners missing");
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"RequestInfoIO*.class")){for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}}
        }
    }
}
