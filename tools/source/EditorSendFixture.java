import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture only send/chat/back destinations in the complete commandAction. */
public class EditorSendFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String ui=ref?"cf":"jimm/JimmUI",contact=ref?"Lz;":"Ljimm/ContactItem;";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();
                if(entry.getName().equals(ui+".class")) {
                    ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(!n.equals("commandAction"))return m;
                            return new MethodVisitor(ASM9,m) {
                                public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                                    String call=null,signature=null;
                                    if(op==INVOKESTATIC&&owner.equals(ui)&&name.equals(ref?"a":"sendMessage")&&desc.equals("(Ljava/lang/String;"+contact+")V")){call="send";signature="(Ljava/lang/String;Ljava/lang/Object;)V";}
                                    if(op==INVOKESTATIC&&owner.equals(ui)&&name.equals(ref?"a":"backToLastScreen")&&desc.equals("()V")){call="back";signature=desc;}
                                    if(op==INVOKESTATIC&&owner.equals(ref?"bt":"jimm/ChatHistory")&&name.equals(ref?"a":"activateIfExists")&&desc.equals("("+contact+")Z")){call="chat";signature="(Ljava/lang/Object;)Z";}
                                    if(op==INVOKESPECIAL&&owner.equals(ui)&&name.equals("chatExists")&&desc.equals("("+contact+")Z")){call="chatWithReceiver";signature="(Ljava/lang/Object;Ljava/lang/Object;)Z";}
                                    if(call==null)super.visitMethodInsn(op,owner,name,desc,itf);
                                    else super.visitMethodInsn(INVOKESTATIC,"EditorSendIO",call,signature,false);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();
                }
                GraphicsFixture.entry(out,entry.getName(),bytes);
            }
            GraphicsFixture.entry(out,"EditorSendIO.class",Files.readAllBytes(Paths.get(args[3],"EditorSendIO.class")));
        }
    }
}
