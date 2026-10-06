import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Preserve menu/controllers and private-status packets; capture native display, status sending and callbacks. */
public class MenuFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String menu=ref?"ag":"jimm/MainMenu",list=ref?"m":"jimm/ContactList",icq=ref?"r":"jimm/comm/Icq",error=ref?"bv":"jimm/JimmException",splash=ref?"cv":"jimm/SplashCanvas",emotions=ref?"bo":"jimm/Emotions";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");byte[] bytes=in.getInputStream(e).readAllBytes();
                if(Arrays.asList(menu,list,icq,emotions,splash).contains(owner)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(list)&&name.equals(ref?"f":"safeSave")&&desc.equals("()V")){MessageFixture.capture(m,"menu-save-groups",desc);return null;}
                            if(owner.equals(splash)&&name.equals(ref?"a":"addTimerTask")&&desc.equals("(Ljava/lang/String;L"+(ref?"aa":"jimm/comm/Action")+";Z)V")){MessageFixture.capture(m,"menu-wait",desc);return null;}
                            if(owner.equals(list)&&name.equals(ref?"b":"afterConnect")&&desc.equals("(Z)V")){MessageFixture.capture(m,"menu-after-connect",desc);return null;}
                            if(owner.equals(emotions)&&name.equals(ref?"a":"selectEmotion")&&desc.equals("(Ljavax/microedition/lcdui/TextBox;Ljava/lang/Object;)V")){MessageFixture.capture(m,"menu-smile",desc);return null;}
                            if(owner.equals(icq)&&name.equals(ref?"a":"setOnlineStatus")&&desc.equals("(I)V")){
                                m.visitCode();m.visitLdcInsn("menu-status");m.visitInsn(ICONST_1);m.visitTypeInsn(ANEWARRAY,"java/lang/Object");m.visitInsn(DUP);m.visitInsn(ICONST_0);m.visitVarInsn(ILOAD,0);m.visitMethodInsn(INVOKESTATIC,"java/lang/Integer","valueOf","(I)Ljava/lang/Integer;",false);m.visitInsn(AASTORE);m.visitMethodInsn(INVOKESTATIC,"MessageIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);
                                Label done=new Label(),critical=new Label();m.visitFieldInsn(GETSTATIC,"MenuIO","failure","I");m.visitJumpInsn(IFEQ,done);m.visitFieldInsn(GETSTATIC,"MenuIO","failure","I");m.visitInsn(ICONST_1);m.visitJumpInsn(IF_ICMPEQ,critical);
                                m.visitTypeInsn(NEW,error);m.visitInsn(DUP);m.visitIntInsn(BIPUSH,120);m.visitInsn(ICONST_0);m.visitInsn(ICONST_1);m.visitMethodInsn(INVOKESPECIAL,error,"<init>","(IIZ)V",false);m.visitInsn(ATHROW);
                                m.visitLabel(critical);m.visitTypeInsn(NEW,error);m.visitInsn(DUP);m.visitIntInsn(BIPUSH,120);m.visitInsn(ICONST_0);m.visitMethodInsn(INVOKESPECIAL,error,"<init>","(II)V",false);m.visitInsn(ATHROW);m.visitLabel(done);m.visitInsn(RETURN);m.visitMaxs(6,1);m.visitEnd();return null;
                            }
                            if(!owner.equals(menu))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"MenuIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                                else if(target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"MenuIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);out.putNextEntry(new JarEntry("MenuIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
