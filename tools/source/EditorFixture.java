import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Change only enumerated controller invocations; all real editor instructions remain. */
public class EditorFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String ui=ref?"cf":"jimm/JimmUI";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equals("EditorIO.class"))continue;
                byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(ui+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            return new MethodVisitor(ASM9,super.visitMethod(a,n,d,s,ex)) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    String call=null,signature=null;
                                    if(op==INVOKEVIRTUAL&&(owner.equals("javax/microedition/lcdui/TextBox")||owner.equals("javax/microedition/lcdui/Displayable"))) {
                                        if(name.equals("addCommand")&&desc.equals("(Ljavax/microedition/lcdui/Command;)V")){call="add";signature="(Ljavax/microedition/lcdui/Displayable;Ljavax/microedition/lcdui/Command;)V";}
                                        if(name.equals("setCommandListener")&&desc.equals("(Ljavax/microedition/lcdui/CommandListener;)V")){call="listener";signature="(Ljavax/microedition/lcdui/Displayable;Ljavax/microedition/lcdui/CommandListener;)V";}
                                        if(name.equals("setConstraints")&&desc.equals("(I)V")){call="constraints";signature="(Ljavax/microedition/lcdui/TextBox;I)V";}
                                    }
                                    if(op==INVOKEVIRTUAL&&owner.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V")){call="show";signature="(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V";}
                                    if(op==INVOKESTATIC&&owner.equals("java/lang/System")&&name.equals("gc")&&desc.equals("()V")){call="gc";signature=desc;}
                                    if(op==INVOKESTATIC&&owner.equals(ref?"aj":"DrawControls/LightControl")&&name.equals(ref?"a":"flash")&&desc.equals("(Z)V")){call="light";signature=desc;}
                                    if(op==INVOKESTATIC&&owner.equals(ref?"r":"jimm/comm/Icq")&&name.equals(ref?"a":"beginTyping")&&desc.equals("(Ljava/lang/String;Z)V")){call="typing";signature=desc;}
                                    if(call==null)super.visitMethodInsn(op,owner,name,desc,itf);
                                    else super.visitMethodInsn(INVOKESTATIC,"EditorIO",call,signature,false);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            out.putNextEntry(new JarEntry("EditorIO.class"));out.write(Files.readAllBytes(Paths.get(args[3],"EditorIO.class")));out.closeEntry();
        }
    }
}
