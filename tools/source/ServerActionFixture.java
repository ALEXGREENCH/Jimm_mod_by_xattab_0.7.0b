import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Deterministic time and contactChanged observation, on the existing connection/UI boundaries. */
public class ServerActionFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String action=ref?"cq":"jimm/comm/ServerListsAction",list=ref?"m":"jimm/ContactList",contact=ref?"z":"jimm/ContactItem",util=ref?"co":"jimm/comm/Util";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();String owner=entry.getName().replace(".class","");
                if(owner.equals(action)||owner.equals(list)||owner.equals(util)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,errors);
                            if(owner.equals(list)&&name.equals(ref?"a":"contactChanged")&&desc.equals("(L"+contact+";ZZ)V")){
                                m.visitCode();m.visitVarInsn(ALOAD,0);m.visitVarInsn(ILOAD,1);m.visitVarInsn(ILOAD,2);m.visitMethodInsn(INVOKESTATIC,"ServerActionIO","changed","(Ljava/lang/Object;ZZ)V",false);m.visitInsn(RETURN);m.visitMaxs(3,3);m.visitEnd();return null;
                            }
                            if(!owner.equals(action)&&!(owner.equals(util)&&name.equals(ref?"a":"createCurrentDate")&&desc.equals("(ZZ)J")))return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitTypeInsn(int op,String type){super.visitTypeInsn(op,type.equals("java/util/Date")&&op==NEW?"ServerActionIO$Clock":type);}
                                public void visitMethodInsn(int op,String target,String n,String d,boolean itf){
                                    if(target.equals("java/lang/System")&&n.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",d,false);
                                    else if(target.equals("java/util/Date")&&n.equals("<init>")&&d.equals("()V"))super.visitMethodInsn(op,"ServerActionIO$Clock",n,d,itf);
                                    else super.visitMethodInsn(op,target,n,d,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"ServerActionIO*.class")){for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}}
        }
    }
}
