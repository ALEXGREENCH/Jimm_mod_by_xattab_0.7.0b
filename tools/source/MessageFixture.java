import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** On top of LoginFixture, capture UI scheduling, chat appends, and outgoing actions. */
public class MessageFixture implements Opcodes {
    static void capture(MethodVisitor m,String label,String desc){
        m.visitCode();m.visitLdcInsn(label);Type[] args=Type.getArgumentTypes(desc);m.visitLdcInsn(args.length);m.visitTypeInsn(ANEWARRAY,"java/lang/Object");
        int slot=0;
        for(int i=0;i<args.length;i++){
            Type t=args[i];m.visitInsn(DUP);m.visitLdcInsn(i);m.visitVarInsn(t.getOpcode(ILOAD),slot);slot+=t.getSize();
            String box=t.getSort()==Type.BOOLEAN?"Boolean":t.getSort()==Type.INT?"Integer":t.getSort()==Type.LONG?"Long":null;
            if(box!=null)m.visitMethodInsn(INVOKESTATIC,"java/lang/"+box,"valueOf","("+t.getDescriptor()+")Ljava/lang/"+box+";",false);
            m.visitInsn(AASTORE);
        }
        m.visitMethodInsn(INVOKESTATIC,"MessageIO","record","(Ljava/lang/String;[Ljava/lang/Object;)V",false);m.visitInsn(RETURN);m.visitMaxs(8,slot);m.visitEnd();
    }
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String runner=ref?"l":"jimm/RunnableImpl",icq=ref?"r":"jimm/comm/Icq",chat=ref?"bt":"jimm/ChatHistory";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owner.equals(runner)||owner.equals(icq)||owner.equals(chat)){
                    ClassWriter w=new ClassWriter(0);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String label=null;
                            if(owner.equals(runner)&&name.equals(ref?"a":"callSerially")&&desc.equals("(I[Ljava/lang/Object;)V"))label="ui";
                            if(owner.equals(icq)&&name.equals(ref?"a":"requestAction")&&desc.equals(ref?"(Laa;)V":"(Ljimm/comm/Action;)V"))label="action";
                            if(owner.equals(chat)&&name.equals(ref?"a":"AckMessage")&&desc.equals("(Ljava/lang/String;JZ)V"))label="ack";
                            if(owner.equals(chat)&&name.equals(ref?"a":"addTextToForm")&&desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZZL"+(ref?"e":"DrawControls/Icon")+";J)V"))label="text";
                            if(label!=null){capture(m,label,desc);return null;}return m;
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            ClassWriter helper=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[3],"MessageIO.class"))).accept(new ClassVisitor(ASM9,helper){
                public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}
            },ClassReader.SKIP_FRAMES);
            out.putNextEntry(new JarEntry("MessageIO.class"));out.write(helper.toByteArray());out.closeEntry();
        }
    }
}
