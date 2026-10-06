import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Keep controller/task bodies intact; script hardware, device predicate and external clock. */
public class LightFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String light=ref?"aj":"DrawControls/LightControl",options=ref?(args[3].equals("MIDP2")?"cj":"ci"):"jimm/Options";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(light+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String method,String descriptor,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,method,descriptor,sig,errors)){
                        public void visitFieldInsn(int op,String owner,String name,String desc){if(op==GETSTATIC&&owner.equals("jimm/Jimm")&&name.equals(ref?"f":"supportsNokiaLight")&&desc.equals("Z"))super.visitMethodInsn(INVOKESTATIC,"LightIO","supported","()Z",false);else super.visitFieldInsn(op,owner,name,desc);}
                        public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                            if(method.equals("<clinit>")&&owner.equals(options)&&desc.equals("(I)I"))super.visitMethodInsn(INVOKESTATIC,"LightIO","observeInit","()V",false);
                            if(owner.equals("com/nokia/mid/ui/DeviceControl")&&name.equals("setLights"))super.visitMethodInsn(INVOKESTATIC,"LightIO","lights",desc,false);
                            else if(owner.equals("javax/microedition/lcdui/Display")&&name.equals("flashBacklight"))super.visitMethodInsn(INVOKESTATIC,"LightIO","flash","(Ljavax/microedition/lcdui/Display;I)Z",false);
                            else if(owner.equals("jimm/Jimm")&&desc.equals("()Ljava/util/Timer;"))super.visitMethodInsn(INVOKESTATIC,"LightIO","timer",desc,false);
                            else if(owner.equals("java/util/TimerTask")&&name.equals("cancel"))super.visitMethodInsn(INVOKESTATIC,"LightIO","cancel","(Ljava/util/TimerTask;)Z",false);
                            else super.visitMethodInsn(op,owner,name,desc,itf);
                        }
                    };}},0);bytes=w.toByteArray();
                }GraphicsFixture.entry(out,e.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Light,,GraphicsMIDlet");a.putValue("MIDlet-Name","Backlight probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream bytes=new ByteArrayOutputStream();manifest.write(bytes);GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",bytes.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[4]),"LightIO*.class")){for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,file.getFileName().toString(),w.toByteArray());}}
        }
    }
}
