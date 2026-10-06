import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Restore the real OptionsForm; capture surrounding network/menu/light and fix its clock. */
public class OptionsFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String of=ref?"cg":"jimm/OptionsForm",menu=ref?"ag":"jimm/MainMenu",light=ref?"aj":"DrawControls/LightControl",other=ref?"bq":"jimm/comm/OtherAction",icq=ref?"r":"jimm/comm/Icq",list=ref?"m":"jimm/ContactList",util=ref?"co":"jimm/comm/Util",history=ref?"q":"jimm/HistoryStorage",chat=ref?"bt":"jimm/ChatHistory";
        try(JarFile in=new JarFile(args[0]);JarFile original=new JarFile(args[4]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");byte[] bytes=(owner.equals(of)?original:in).getInputStream((owner.equals(of)?original:in).getJarEntry(e.getName())).readAllBytes();
                if(Arrays.asList(of,menu,light,other,icq,list,util,history,chat).contains(owner)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String event=null;
                            if(owner.equals(menu)&&name.equals(ref?"b":"activate")&&desc.equals("()V"))event="options-main";
                            if(owner.equals(light)&&name.equals(ref?"a":"flash")&&desc.equals("(Z)V"))event="options-light";
                            if(owner.equals(other)&&name.equals(ref?"a":"setStandartUserInfo")&&desc.equals("(Z)V"))event="options-info";
                            if(owner.equals(other)&&name.equals(ref?"a":"setStatus")&&desc.equals("(I)V"))event="options-status";
                            if(owner.equals(icq)&&name.equals(ref?"g":"setPoint")&&desc.equals("()V"))event="options-point";
                            // The May optimizer removes the unused second sort argument.
                            if(owner.equals(list)&&name.equals(ref?"a":"optionsChanged")&&desc.equals(ref?"(Z)V":"(ZZ)V")){MessageFixture.capture(m,"options-changed","(Z)V");return null;}
                            if(owner.equals(history)&&name.equals(ref?"b":"setColorScheme")&&desc.equals("()V"))event="options-history-colors";
                            if(owner.equals(chat)&&name.equals(ref?"a":"setColorScheme")&&desc.equals("()V"))event="options-chat-colors";
                            if(event!=null){MessageFixture.capture(m,event,desc);return null;}
                            if(!owner.equals(of)&&!owner.equals(util))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);}
                                else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","start","(Ljava/lang/Thread;)V",false);
                                else if(owner.equals(of)&&target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"OptionsIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                                else super.visitMethodInsn(op,target,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"OptionsIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);
            out.putNextEntry(new JarEntry("OptionsIO.class"));out.write(w.toByteArray());out.closeEntry();
        }
    }
}
