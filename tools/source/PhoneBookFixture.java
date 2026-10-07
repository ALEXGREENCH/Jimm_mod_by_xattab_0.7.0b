import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Only PhoneBook's physical display, platform request, SMS open and main-menu destinations. */
public class PhoneBookFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String owner=ref?"bd":"jimm/util/PhoneBook",menu=ref?"ag":"jimm/MainMenu";final int[] hits=new int[4];
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(owner+".class")){
                    // Optionally exercise the delivered optimized controller against the same host destinations.
                    if(args.length>4){if(ref)throw new AssertionError("Never substitute the reference controller");try(JarFile optimized=new JarFile(args[4])){bytes=optimized.getInputStream(optimized.getJarEntry(owner+".class")).readAllBytes();}}
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)){
                            public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V")){hits[0]++;super.visitMethodInsn(INVOKESTATIC,"PhoneBookIO","display","(Ljava/lang/Object;Ljava/lang/Object;)V",false);}
                                else if(target.equals("javax/microedition/midlet/MIDlet")&&name.equals("platformRequest")&&desc.equals("(Ljava/lang/String;)Z")){hits[1]++;super.visitMethodInsn(INVOKESTATIC,"PhoneBookIO","call","(Ljava/lang/Object;Ljava/lang/String;)Z",false);}
                                else if(target.equals("javax/microedition/io/Connector")&&name.equals("open")&&desc.equals("(Ljava/lang/String;)Ljavax/microedition/io/Connection;")){hits[2]++;super.visitMethodInsn(INVOKESTATIC,"PhoneBookIO","open",desc,false);}
                                else if(target.equals(menu)&&name.equals(ref?"b":"activate")&&desc.equals("()V")){hits[3]++;super.visitMethodInsn(INVOKESTATIC,"PhoneBookIO","menu",desc,false);}
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }
                        };}
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(!Arrays.equals(hits,new int[]{2,1,1,2}))throw new AssertionError("PhoneBook boundary sites: "+Arrays.toString(hits));
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"PhoneBookIO*.class")){
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}
            }
        }
    }
}
