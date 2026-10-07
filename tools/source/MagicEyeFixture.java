import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Three journal routes and Date construction; real formatting and TextList host logic. */
public class MagicEyeFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String owner=ref?"ah":"jimm/util/MagicEye",ui=ref?"cf":"jimm/JimmUI",contacts=ref?"m":"jimm/ContactList",contact=ref?"z":"jimm/ContactItem";
        String parent=args[1]+".text-list.jar";VirtualListFixture.main(new String[]{args[0],parent,args[2],args[3]});final int[] hits=new int[3],clock=new int[2];
        try(JarFile in=new JarFile(parent);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(owner+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)){
                        public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                            if(target.equals(contacts)&&name.equals(ref?"a":"activate")&&desc.equals("()V")){hits[0]++;super.visitMethodInsn(INVOKESTATIC,"MagicEyeIO","contacts",desc,false);}
                            else if(target.equals(ui)&&name.equals(ref?"a":"backToLastScreen")&&desc.equals("()V")){hits[1]++;super.visitMethodInsn(INVOKESTATIC,"MagicEyeIO","back",desc,false);}
                            else if(target.equals(ui)&&name.equals(ref?"a":"showContactMenu")&&desc.equals("(L"+contact+";)V")){hits[2]++;super.visitMethodInsn(INVOKESTATIC,"MagicEyeIO","menu","(Ljava/lang/Object;)V",false);}
                            else super.visitMethodInsn(op,target,name,desc,itf);
                        }
                    };}},0);bytes=w.toByteArray();
                }
                if(e.getName().equals((ref?"co":"jimm/comm/Util")+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){MethodVisitor m=super.visitMethod(a,n,d,s,ex);if(!n.equals(ref?"a":"createCurrentDate")||!d.equals("(ZZ)J"))return m;return new MethodVisitor(ASM9,m){
                        public void visitTypeInsn(int op,String target){if(op==NEW&&target.equals("java/util/Date")){clock[0]++;target="MagicEyeIO$Clock";}super.visitTypeInsn(op,target);}
                        public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){clock[1]++;target="MagicEyeIO$Clock";}super.visitMethodInsn(op,target,name,desc,itf);}
                    };}},0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(!Arrays.equals(hits,new int[]{1,1,1}))throw new AssertionError("MagicEye routing sites: "+Arrays.toString(hits));
            if(!Arrays.equals(clock,new int[]{1,1}))throw new AssertionError("MagicEye clock sites: "+Arrays.toString(clock));
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"MagicEyeIO*.class")){
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}
            }
        }
    }
}
