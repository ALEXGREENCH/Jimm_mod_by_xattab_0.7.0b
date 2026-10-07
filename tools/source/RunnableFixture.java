import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Restore the entire real dispatcher before capturing its downstream calls and event queue boundary. */
public class RunnableFixture implements Opcodes {
    static void capture(MethodVisitor m,String label,String desc){
        MessageFixture.capture(new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String o,String n,String d,boolean itf){super.visitMethodInsn(op,o.equals("MessageIO")?"RunnableIO":o,n,d,itf);}},label,desc);
    }
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String runner=ref?"l":"jimm/RunnableImpl",list=ref?"m":"jimm/ContactList",icq=ref?"r":"jimm/comm/Icq",ui=ref?"cf":"jimm/JimmUI",menu=ref?"ag":"jimm/MainMenu",listener=ref?"ae":"jimm/comm/ActionListener";
        String message=ref?"ac":"jimm/comm/Message",contact=ref?"z":"jimm/ContactItem";HashMap<String,String> calls=new HashMap<>();
        String[][] specs={
            {list,ref?"a":"BeginTyping","(Ljava/lang/String;Z)V","typing"},
            {list,ref?"a":"addMessage","(L"+message+";Z)V","message"},
            {list,ref?"a$505cff1c":"update",ref?"(Ljava/lang/String;)V":"(Ljava/lang/String;I)V","offline"},
            {list,ref?"a":"updateTitle","(I)V","caption"},
            {list,ref?"a":"update","(Ljava/lang/String;II[B[BIIIIIIII)V","presence"},
            {list,ref?"b":"addContactItem","(L"+contact+";)V","add-contact"},
            {list,ref?"d":"setStatusesOffline","()V","reset-contacts"},
            {list,ref?"b":"playSoundNotification","(I)V","sound"},
            {list,ref?"c":"beforeConnect","()V","before-connect"},
            {ui,ref?"a":"showUserInfo","([Ljava/lang/String;)V","info"},
            {ui,ref?"a":"backToLastScreen","()V","back"},
            {menu,ref?"a":"activate","(Ljava/lang/String;)V","menu"},
            {icq,ref?"a":"nextSrvHost","()V","next-server"},
            {icq,ref?"b":"connect","()V","connect"},
            {"jimm/Jimm",ref?"a":"setMinimized","(Z)V","minimized"}
        };
        for(String[] spec:specs)calls.put(spec[0]+"."+spec[1]+spec[2],spec[3]);Set<String> observed=new HashSet<>();
        try(JarFile in=new JarFile(args[0]);JarFile actual=new JarFile(args[4]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();String owner=entry.getName().replace(".class","");byte[] bytes=owner.equals(runner)?actual.getInputStream(actual.getJarEntry(entry.getName())).readAllBytes():in.getInputStream(entry).readAllBytes();
                if(owner.equals(runner)||owner.equals(list)||owner.equals(icq)||owner.equals(ui)||owner.equals(menu)||owner.equals(listener)||owner.equals("jimm/Jimm")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,errors);String key=owner+"."+name+desc,label=calls.get(key);
                            if(label!=null){observed.add(key);
                                // The shipped offline callback has its constant -1 status argument removed by ProGuard.
                                if(ref&&label.equals("offline")){m.visitCode();m.visitLdcInsn(label);m.visitInsn(ICONST_2);m.visitTypeInsn(ANEWARRAY,"java/lang/Object");m.visitInsn(DUP);m.visitInsn(ICONST_0);m.visitVarInsn(ALOAD,0);m.visitInsn(AASTORE);m.visitInsn(DUP);m.visitInsn(ICONST_1);m.visitInsn(ICONST_M1);m.visitMethodInsn(INVOKESTATIC,"java/lang/Integer","valueOf","(I)Ljava/lang/Integer;",false);m.visitInsn(AASTORE);m.visitMethodInsn(INVOKESTATIC,"RunnableIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);m.visitInsn(RETURN);m.visitMaxs(5,1);m.visitEnd();}
                                else capture(m,label,desc);return null;
                            }
                            if(owner.equals(listener)&&name.equals(ref?"a":"isSpam")&&desc.equals("(L"+message+";)Z"))return new MethodVisitor(ASM9,m){public void visitCode(){super.visitCode();Label actual=new Label();visitFieldInsn(GETSTATIC,"RunnableIO","forcedSpam","I");visitJumpInsn(IFLT,actual);visitVarInsn(ALOAD,0);visitMethodInsn(INVOKESTATIC,"RunnableIO","spam","(Ljava/lang/Object;)Z",false);visitInsn(IRETURN);visitLabel(actual);}};
                            if(!owner.equals(runner))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String target,String n,String d,boolean itf){
                                if(target.equals("javax/microedition/lcdui/Display")&&n.equals("callSerially"))super.visitMethodInsn(INVOKESTATIC,"RunnableIO","enqueue","(Ljavax/microedition/lcdui/Display;Ljava/lang/Runnable;)V",false);
                                else if(target.equals("java/lang/System")&&n.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"RunnableIO","collect",d,false);
                                else if(target.equals("java/lang/Thread")&&n.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"RunnableIO","sleep",d,false);
                                else super.visitMethodInsn(op,target,n,d,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"RunnableIO.class"))).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,"RunnableIO.class",w.toByteArray());
        }
        if(!observed.equals(calls.keySet())){Set<String> missing=new HashSet<>(calls.keySet());missing.removeAll(observed);throw new AssertionError("Missing dispatcher boundaries: "+missing);}
    }
}
