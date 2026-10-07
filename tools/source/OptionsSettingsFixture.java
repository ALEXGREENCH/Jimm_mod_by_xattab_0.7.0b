import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Keep all application instructions except one enumerated Display API call. */
public class OptionsSettingsFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean scripted=Boolean.parseBoolean(args[4]);
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry entry=entries.nextElement();if(entry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;
                byte[] bytes=in.getInputStream(entry).readAllBytes();
                if(scripted&&entry.getName().equals(args[3]+".class")) {
                    ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] ex) {
                        MethodVisitor m=super.visitMethod(access,name,desc,sig,ex);
                        if(!name.equals(args[5])||!desc.equals("()V"))return m;
                        return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String n,String d,boolean itf) {
                            if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Display")&&n.equals("numAlphaLevels")&&d.equals("()I"))super.visitMethodInsn(INVOKESTATIC,"OptionsSettingsIO","alphaLevels","(Ljavax/microedition/lcdui/Display;)I",false);
                            else super.visitMethodInsn(op,owner,n,d,itf);
                        }};
                    }},0);bytes=writer.toByteArray();
                }
                GraphicsFixture.entry(out,entry.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Settings,,GraphicsMIDlet");a.putValue("MIDlet-Name","Settings probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream data=new ByteArrayOutputStream();manifest.write(data);
            GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",data.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            if(scripted)GraphicsFixture.entry(out,"OptionsSettingsIO.class",Files.readAllBytes(Paths.get(args[2],"OptionsSettingsIO.class")));
        }
    }
}
