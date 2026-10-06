import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep timer branches and captions; capture scheduling, status sending and splash callbacks. */
public class TimerFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String timer=ref?"at":"jimm/TimerTasks",util=ref?"co":"jimm/comm/Util",splash=ref?"cv":"jimm/SplashCanvas",birthday=ref?"cs":"jimm/util/NoticeOnBirthDay",icq=ref?"r":"jimm/comm/Icq",error=ref?"bv":"jimm/JimmException";
        String fixed=args[1]+".clock.jar";SplashFixture.main(new String[]{args[0],fixed,args[2]});
        try(JarFile in=new JarFile(fixed);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");byte[] bytes=in.getInputStream(e).readAllBytes();
                if(Arrays.asList(timer,util,splash,birthday,icq).contains(owner)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String event=null;
                            if(owner.equals(splash)&&name.equals(ref?"b":"Repaint")&&desc.equals("()V"))event="timer-repaint";
                            if(owner.equals(splash)&&name.equals(ref?"n":"lockScreen")&&desc.equals("()V"))event="timer-lock";
                            if(owner.equals(birthday)&&name.equals(ref?"a":"refreshBday")&&desc.equals("()V"))event="timer-birthday";
                            if(event!=null){MessageFixture.capture(m,event,desc);return null;}
                            if(owner.equals(icq)&&name.equals(ref?"a":"setOnlineStatus")&&desc.equals("(I)V")){
                                m.visitCode();m.visitLdcInsn("timer-status");m.visitInsn(ICONST_1);m.visitTypeInsn(ANEWARRAY,"java/lang/Object");m.visitInsn(DUP);m.visitInsn(ICONST_0);m.visitVarInsn(ILOAD,0);m.visitMethodInsn(INVOKESTATIC,"java/lang/Integer","valueOf","(I)Ljava/lang/Integer;",false);m.visitInsn(AASTORE);m.visitMethodInsn(INVOKESTATIC,"MessageIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);m.visitFieldInsn(GETSTATIC,"TimerIO","failStatus","Z");Label ok=new Label();m.visitJumpInsn(IFEQ,ok);m.visitTypeInsn(NEW,error);m.visitInsn(DUP);m.visitIntInsn(BIPUSH,120);m.visitInsn(ICONST_0);m.visitMethodInsn(INVOKESPECIAL,error,"<init>","(II)V",false);m.visitInsn(ATHROW);m.visitLabel(ok);m.visitInsn(RETURN);m.visitMaxs(6,1);m.visitEnd();return null;
                            }
                            if(!owner.equals(timer)&&!owner.equals(util))return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitTypeInsn(int op,String target){super.visitTypeInsn(op,target.equals("java/util/Timer")?"TimerIO$Clock":target);}
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(target.equals("java/util/Timer")&&name.equals("<init>")&&desc.equals("()V"))super.visitMethodInsn(op,"TimerIO$Clock",name,desc,itf);
                                    else if(target.equals("java/util/TimerTask")&&name.equals("cancel")&&op==INVOKESPECIAL){super.visitInsn(DUP);super.visitMethodInsn(INVOKESTATIC,"TimerIO","markCancel","(Ljava/util/TimerTask;)V",false);super.visitMethodInsn(op,target,name,desc,itf);}
                                    else if(target.equals("javax/microedition/lcdui/Displayable")&&name.equals("isShown"))super.visitMethodInsn(INVOKESTATIC,"TimerIO","isShown","(Ljavax/microedition/lcdui/Displayable;)Z",false);
                                    else if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);}
                                    else if(target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                                    else super.visitMethodInsn(op,target,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"TimerIO*.class")){
                for(Path file:files){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);out.putNextEntry(new JarEntry(file.getFileName().toString()));out.write(w.toByteArray());out.closeEntry();}
            }
        }
    }
}
