import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Change only enumerated resource/MMAPI calls inside the two genuine sound methods. */
public class SoundFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        String subject=args[3],create=args[4],notification=args[5];
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;byte[] data=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(subject+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(data).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!(n.equals(create)&&d.equals("(Ljava/lang/String;)Ljavax/microedition/media/Player;")||n.equals(notification)&&d.equals("(I)V")))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                                if(op==INVOKEVIRTUAL&&owner.equals("java/lang/Class")&&name.equals("getResourceAsStream")&&desc.equals("(Ljava/lang/String;)Ljava/io/InputStream;"))super.visitMethodInsn(INVOKESTATIC,"SoundIO","resource","(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;",false);
                                else if(op==INVOKESTATIC&&owner.equals("javax/microedition/media/Manager")&&name.equals("createPlayer")&&desc.equals("(Ljava/io/InputStream;Ljava/lang/String;)Ljavax/microedition/media/Player;"))super.visitMethodInsn(INVOKESTATIC,"SoundIO","create",desc,false);
                                else if(op==INVOKESTATIC&&owner.equals("javax/microedition/media/Manager")&&name.equals("playTone")&&desc.equals("(III)V"))super.visitMethodInsn(INVOKESTATIC,"SoundIO","tone",desc,false);
                                else if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Display")&&name.equals("vibrate")&&desc.equals("(I)Z"))super.visitMethodInsn(INVOKESTATIC,"SoundIO","vibrate","(Ljavax/microedition/lcdui/Display;I)Z",false);
                                else super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);data=w.toByteArray();
                }GraphicsFixture.entry(out,e.getName(),data);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Sound,,GraphicsMIDlet");a.putValue("MIDlet-Name","Sound probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream bytes=new ByteArrayOutputStream();manifest.write(bytes);GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",bytes.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            try(DirectoryStream<Path> helpers=Files.newDirectoryStream(Paths.get(args[2]),"SoundIO*.class")){for(Path p:helpers)GraphicsFixture.entry(out,p.getFileName().toString(),Files.readAllBytes(p));}
        }
    }
}
