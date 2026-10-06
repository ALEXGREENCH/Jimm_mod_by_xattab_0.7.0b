import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep the login/roster code; substitute only time and connection/message delivery. */
public class LoginFixture implements Opcodes {
    static String error;
    static void failure(MethodVisitor m){m.visitTypeInsn(NEW,error);m.visitInsn(DUP);m.visitIntInsn(BIPUSH,120);m.visitInsn(ICONST_0);m.visitMethodInsn(INVOKESPECIAL,error,"<init>","(II)V",false);m.visitInsn(ATHROW);}
    static void connectionBody(MethodVisitor m,int kind){
        m.visitCode();
        if(kind==0){m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKESTATIC,"LoginIO","connect","(Ljava/lang/String;)Z",false);Label done=new Label();m.visitJumpInsn(IFEQ,done);failure(m);m.visitLabel(done);}
        if(kind==1){m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKESTATIC,"LoginIO","send","(Ljava/lang/Object;)V",false);m.visitFieldInsn(GETSTATIC,"LoginIO","failSend","Z");Label done=new Label();m.visitJumpInsn(IFEQ,done);failure(m);m.visitLabel(done);}
        if(kind==2){m.visitFieldInsn(GETSTATIC,"LoginIO","closes","I");m.visitInsn(ICONST_1);m.visitInsn(IADD);m.visitFieldInsn(PUTSTATIC,"LoginIO","closes","I");}
        m.visitInsn(RETURN);m.visitMaxs(4,2);m.visitEnd();
    }
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");error=ref?"bv":"jimm/JimmException";
        String login=ref?"n":"jimm/comm/ConnectAction",base=ref?"ap":"jimm/comm/Icq$Connection",icq=ref?"r":"jimm/comm/Icq";
        String packet=ref?"an":"jimm/comm/Packet",http=ref?"ay":"jimm/comm/Icq$HTTPConnection",runnable=ref?"l":"jimm/RunnableImpl",message=ref?"ac":"jimm/comm/Message";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();
                String owner=entry.getName().replace(".class","");
                if(owner.equals(login)||owner.equals(http)||owner.equals(runnable)){
                    ClassWriter w=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);
                            if(owner.equals(http)){
                                int kind=name.equals(ref?"a":"connect")&&desc.equals("(Ljava/lang/String;)V")?0:
                                    name.equals(ref?"a":"sendPacket")&&desc.equals("(L"+packet+";)V")?1:name.equals(ref?"a":"close")&&desc.equals("()V")?2:-1;
                                if(kind!=-1){connectionBody(m,kind);return null;}
                            }
                            if(owner.equals(runnable)&&name.equals(ref?"a":"addMessageSerially")&&desc.equals("(L"+message+";)V")){
                                m.visitCode();m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKESTATIC,"LoginIO","message","(Ljava/lang/Object;)V",false);m.visitInsn(RETURN);m.visitMaxs(1,1);m.visitEnd();return null;
                            }
                            if(!owner.equals(login))return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                                if(owner.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);
                                else if(owner.equals("java/lang/Thread")&&name.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","sleep",desc,false);
                                else super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter w=new ClassWriter(0);w.visit(V1_5,ACC_PUBLIC,"LoginConnection",null,base,null);
            MethodVisitor m=w.visitMethod(ACC_PUBLIC,"<init>","(L"+icq+";)V",null,null);m.visitCode();m.visitVarInsn(ALOAD,0);
            if(!ref)m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKESPECIAL,base,"<init>",ref?"()V":"(L"+icq+";)V",false);m.visitInsn(RETURN);m.visitMaxs(2,2);m.visitEnd();
            connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"connect","(Ljava/lang/String;)V",null,null),0);
            connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"sendPacket","(L"+packet+";)V",null,null),1);
            connectionBody(w.visitMethod(ACC_PUBLIC,ref?"a":"close","()V",null,null),2);
            m=w.visitMethod(ACC_PUBLIC,"run","()V",null,null);m.visitCode();m.visitInsn(RETURN);m.visitMaxs(0,1);m.visitEnd();w.visitEnd();
            out.putNextEntry(new JarEntry("LoginConnection.class"));out.write(w.toByteArray());out.closeEntry();
            ClassWriter helper=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"LoginIO.class"))).accept(new ClassVisitor(ASM9,helper){
                public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}
            },ClassReader.SKIP_FRAMES);
            out.putNextEntry(new JarEntry("LoginIO.class"));out.write(helper.toByteArray());out.closeEntry();
        }
    }
}
