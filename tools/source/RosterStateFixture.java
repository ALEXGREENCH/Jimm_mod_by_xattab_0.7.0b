import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Substitute seven VirtualList display calls and one actual RunnableImpl enqueue call. */
public class RosterStateFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean scripted=Boolean.parseBoolean(args[4]);
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(scripted&&(e.getName().equals(args[3]+".class")||e.getName().equals(args[5]+".class"))) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Displayable")&&name.equals("isShown")&&desc.equals("()Z"))super.visitMethodInsn(INVOKESTATIC,"RosterStateIO","isShown","(Ljavax/microedition/lcdui/Displayable;)Z",false);
                                else if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Canvas")&&name.equals("repaint")&&desc.equals("()V"))super.visitMethodInsn(INVOKESTATIC,"RosterStateIO","repaint","(Ljavax/microedition/lcdui/Canvas;)V",false);
                                else if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"RosterStateIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                                else if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Display")&&name.equals("callSerially")&&desc.equals("(Ljava/lang/Runnable;)V"))super.visitMethodInsn(INVOKESTATIC,"RosterStateIO","enqueue","(Ljavax/microedition/lcdui/Display;Ljava/lang/Runnable;)V",false);
                                else super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Roster state,,GraphicsMIDlet");a.putValue("MIDlet-Name","Roster state probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream data=new ByteArrayOutputStream();manifest.write(data);
            GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",data.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            if(scripted)GraphicsFixture.entry(out,"RosterStateIO.class",Files.readAllBytes(Paths.get(args[2],"RosterStateIO.class")));
        }
    }
}
