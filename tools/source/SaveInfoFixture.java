import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Only SaveInfoAction's default Date, clock read and ContactList.activate call are captured. */
public class SaveInfoFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference"),whole=args.length>4;if(whole&&ref&&!Files.isSameFile(Paths.get(args[0]),Paths.get(args[4])))throw new AssertionError("Never substitute the reference JAR");String owner=ref?"as":"jimm/comm/SaveInfoAction",list=ref?"m":"jimm/ContactList",icq=ref?"r":"jimm/comm/Icq",base=ref?"ap":"jimm/comm/Icq$Connection",packet=ref?"an":"jimm/comm/Packet";final int[] hits=new int[4];
        try(JarFile in=new JarFile(whole?args[4]:args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(owner+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)){
                            public void visitTypeInsn(int op,String target){if(op==NEW&&target.equals("java/util/Date")){hits[0]++;target="SaveInfoIO$Clock";}super.visitTypeInsn(op,target);}
                            public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){hits[1]++;super.visitMethodInsn(op,"SaveInfoIO$Clock",name,desc,itf);}
                                else if(target.equals("java/lang/System")&&name.equals("currentTimeMillis")&&desc.equals("()J")){hits[2]++;super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);}
                                else if(target.equals(list)&&name.equals(ref?"a":"activate")&&desc.equals("()V")){hits[3]++;super.visitMethodInsn(INVOKESTATIC,"SaveInfoIO","activate",desc,false);}
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }
                        };}
                    },0);bytes=w.toByteArray();
                }
                if(whole&&e.getName().equals(icq+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);final int[] blocked={0};new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){MethodVisitor m=super.visitMethod(a,n,d,s,ex);if(n.equals(ref?"b":"connect")&&d.equals("()V")){blocked[0]++;m.visitCode();m.visitTypeInsn(NEW,"java/lang/AssertionError");m.visitInsn(DUP);m.visitLdcInsn("Unexpected live login in save-info probe");m.visitMethodInsn(INVOKESPECIAL,"java/lang/AssertionError","<init>","(Ljava/lang/Object;)V",false);m.visitInsn(ATHROW);m.visitMaxs(3,0);m.visitEnd();return null;}return m;}},0);if(blocked[0]!=1)throw new AssertionError("Missing live-login boundary");bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(!Arrays.equals(hits,new int[]{1,1,1,1}))throw new AssertionError("SaveInfo boundary sites: "+Arrays.toString(hits));
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"SaveInfoIO*.class")){
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}
            }
            if(whole){
                // The actual optimized bases have public no-argument constructors. Keep their ABI intact.
                final boolean[] ctor={false};new ClassReader(in.getInputStream(in.getJarEntry(base+".class")).readAllBytes()).accept(new ClassVisitor(ASM9){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){if(n.equals("<init>")&&d.equals("()V")&&(a&ACC_PUBLIC)!=0)ctor[0]=true;return null;}},ClassReader.SKIP_CODE);if(!ctor[0])throw new AssertionError("Optimized connection constructor changed");
                ClassWriter w=new ClassWriter(0);w.visit(V1_5,ACC_PUBLIC,"LoginConnection",null,base,null);MethodVisitor m=w.visitMethod(ACC_PUBLIC,"<init>","(L"+icq+";)V",null,null);m.visitCode();m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKESPECIAL,base,"<init>","()V",false);m.visitInsn(RETURN);m.visitMaxs(1,2);m.visitEnd();
                LoginFixture.error=ref?"bv":"jimm/JimmException";LoginFixture.connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"connect","(Ljava/lang/String;)V",null,null),0);LoginFixture.connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"sendPacket","(L"+packet+";)V",null,null),1);LoginFixture.connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"close","()V",null,null),2);
                m=w.visitMethod(ACC_PUBLIC,"run","()V",null,null);m.visitCode();m.visitInsn(RETURN);m.visitMaxs(0,1);m.visitEnd();w.visitEnd();GraphicsFixture.entry(out,"LoginConnection.class",w.toByteArray());
                ClassWriter helper=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"LoginIO.class"))).accept(new ClassVisitor(ASM9,helper){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,"LoginIO.class",helper.toByteArray());
            }
        }
    }
}
