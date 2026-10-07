import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture terminal/UI destinations in EnterPassword without changing its state, TextBox or branches. */
public class PasswordFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String owner=ref?"by":"jimm/EnterPassword",list=ref?"m":"jimm/ContactList",icq=ref?"r":"jimm/comm/Icq",splash=ref?"cv":"jimm/SplashCanvas",light=ref?"aj":"DrawControls/LightControl";
        final int[] hits=new int[7];
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(owner+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)){
                            public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V")){hits[0]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","display","(Ljava/lang/Object;Ljava/lang/Object;)V",false);}
                                else if(target.equals(light)&&name.equals(ref?"a":"flash")&&desc.equals("(Z)V")){hits[1]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","flash",desc,false);}
                                else if(target.equals(list)&&name.equals(ref?"a":"activate")&&desc.equals("()V")){hits[2]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","activate",desc,false);}
                                else if(target.equals(list)&&name.equals(ref?"c":"beforeConnect")&&desc.equals("()V")){hits[3]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","before",desc,false);}
                                else if(target.equals(icq)&&name.equals(ref?"b":"connect")&&desc.equals("()V")){hits[4]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","connect",desc,false);}
                                else if(target.equals(splash)&&name.equals(ref?"d":"unlock")&&desc.equals("(Z)V")){hits[5]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","unlock",desc,false);}
                                else if(target.equals("jimm/Jimm")&&name.equals("destroyApp")&&desc.equals("(Z)V")){hits[6]++;super.visitMethodInsn(INVOKESTATIC,"PasswordIO","destroy","(Ljava/lang/Object;Z)V",false);}
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }
                        };}
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(!Arrays.equals(hits,new int[]{2,1,1,1,1,2,1}))throw new AssertionError("Password boundary call sites: "+Arrays.toString(hits));
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"PasswordIO*.class")){
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}
            }
        }
    }
}
