import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep history/RMS/UI intact; intercept worker scheduling and external URL requests. */
public class HistoryFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String history=ref?"bg":"jimm/HistoryStorageList",util=ref?"co":"jimm/comm/Util";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(history+".class")||e.getName().equals(util+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,exceptions)){
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);
                                    }else if(target.equals("javax/microedition/rms/RecordStore")&&name.equals("getSizeAvailable")){
                                        super.visitInsn(POP);super.visitLdcInsn(65536000);
                                    }else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","start","(Ljava/lang/Thread;)V",false);
                                    else if(name.equals("platformRequest")&&desc.equals("(Ljava/lang/String;)Z")){
                                        super.visitMethodInsn(INVOKESTATIC,history,"probeURL","(Ljava/lang/Object;Ljava/lang/String;)Z",false);
                                    }else super.visitMethodInsn(op,target,name,desc,itf);
                                }
                            };
                        }
                        public void visitEnd(){
                            MethodVisitor m=super.visitMethod(ACC_PRIVATE|ACC_STATIC,"probeURL","(Ljava/lang/Object;Ljava/lang/String;)Z",null,null);
                            m.visitCode();m.visitLdcInsn("history-url");m.visitInsn(ICONST_1);m.visitTypeInsn(ANEWARRAY,"java/lang/Object");m.visitInsn(DUP);m.visitInsn(ICONST_0);m.visitVarInsn(ALOAD,1);m.visitInsn(AASTORE);m.visitMethodInsn(INVOKESTATIC,"MessageIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);m.visitInsn(ICONST_0);m.visitInsn(IRETURN);m.visitMaxs(0,0);m.visitEnd();super.visitEnd();
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
        }
    }
}
