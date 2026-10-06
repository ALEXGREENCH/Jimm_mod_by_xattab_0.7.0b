import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Substitute heap size, scheduler, browser and HTTP boundaries; keep About/controller/task code. */
public class AboutFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String ui=ref?"cf":"jimm/JimmUI",task=ref?"g":"jimm/JimmUI$GetVersionInfoTimerTask",options=ref?"cj":"jimm/Options";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(ui)||owner.equals(task)||owner.equals(options)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(options)&&name.equals(ref?"d":"safe_save")&&desc.equals("()V")){
                                m.visitCode();m.visitMethodInsn(INVOKESTATIC,"AboutIO","saveOptions","()V",false);m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();return null;
                            }
                            if(owner.equals(options))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/lang/Runtime")&&(name.equals("freeMemory")||name.equals("totalMemory")))super.visitMethodInsn(INVOKESTATIC,"AboutIO",name,"(Ljava/lang/Runtime;)J",false);
                                else if(target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"AboutIO","collect",desc,false);
                                else if(target.equals("java/util/Timer")&&name.equals("schedule")&&desc.equals("(Ljava/util/TimerTask;J)V"))super.visitMethodInsn(INVOKESTATIC,"AboutIO",name,"(Ljava/util/Timer;Ljava/util/TimerTask;J)V",false);
                                else if(name.equals("platformRequest")&&desc.equals("(Ljava/lang/String;)Z"))super.visitMethodInsn(INVOKESTATIC,"AboutIO",name,"(Ljavax/microedition/midlet/MIDlet;Ljava/lang/String;)Z",false);
                                else if(target.equals("javax/microedition/io/Connector")&&name.equals("open")&&desc.equals("(Ljava/lang/String;)Ljavax/microedition/io/Connection;"))super.visitMethodInsn(INVOKESTATIC,"AboutIO",name,desc,false);
                                else if(target.equals("java/lang/Thread")&&name.equals("yield"))super.visitMethodInsn(INVOKESTATIC,"AboutIO","yieldThread",desc,false);
                                else if(target.equals("java/lang/Thread")&&name.equals("setPriority"))super.visitMethodInsn(INVOKESTATIC,"AboutIO",name,"(Ljava/lang/Thread;I)V",false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"*.class")){
                for(Path file:files){String name=file.getFileName().toString();if(!name.startsWith("TransportIO")&&!name.equals("AboutIO.class"))continue;
                    ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);
                    out.putNextEntry(new JarEntry(name));out.write(w.toByteArray());out.closeEntry();
                }
            }
        }
    }
}
