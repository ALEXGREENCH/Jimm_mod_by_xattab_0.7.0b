import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Preserve application bytes; add minimal host and optionally replace exactly two device calls. */
public class ExtendedKeysFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean scripted=Boolean.parseBoolean(args[4]);String subject=args[3];
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(scripted&&e.getName().equals(subject+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals(args[5])||!d.equals("(I)I"))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                if(op==INVOKEVIRTUAL&&name.equals("getKeyName")&&desc.equals("(I)Ljava/lang/String;"))super.visitMethodInsn(INVOKESTATIC,"ExtendedKeysIO","keyName","(Ljavax/microedition/lcdui/Canvas;I)Ljava/lang/String;",false);
                                else if(op==INVOKEVIRTUAL&&name.equals("getGameAction")&&desc.equals("(I)I"))super.visitMethodInsn(INVOKESTATIC,"ExtendedKeysIO","gameAction","(Ljavax/microedition/lcdui/Canvas;I)I",false);
                                else super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Keys,,GraphicsMIDlet");a.putValue("MIDlet-Name","Keys probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream data=new ByteArrayOutputStream();manifest.write(data);
            GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",data.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            if(scripted)GraphicsFixture.entry(out,"ExtendedKeysIO.class",Files.readAllBytes(Paths.get(args[2],"ExtendedKeysIO.class")));
        }
    }
}
