import java.io.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Replace only the network entry point; lifecycle methods remain original. */
public class NetworkFixture {
    public static void main(String[] args) throws Exception {
        boolean reference=args[2].equals("reference");
        final String owner=reference?"r":"jimm/comm/Icq";
        final String connect=reference?"b":"connect";
        final String setDisconnected=reference?"b":"setDisconnected";
        final String initialSequence=reference?"b":"getInitialFlapSequence";
        JarInputStream input=new JarInputStream(new FileInputStream(args[0]));
        FileOutputStream file=new FileOutputStream(args[1]);
        JarOutputStream output=input.getManifest()==null?new JarOutputStream(file):new JarOutputStream(file,input.getManifest());
        boolean patched=false;
        JarEntry entry;
        while((entry=input.getNextJarEntry())!=null) {
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();byte[] buffer=new byte[8192];int count;
            while((count=input.read(buffer))!=-1) bytes.write(buffer,0,count);
            byte[] data=bytes.toByteArray();
            if(entry.getName().equals(owner+".class")) {
                final boolean[] replaced=new boolean[2];
                ClassReader reader=new ClassReader(data);final ClassWriter writer=new ClassWriter(0);
                reader.accept(new ClassVisitor(Opcodes.ASM9,writer) {
                    public MethodVisitor visitMethod(int access,String name,String desc,String signature,String[] exceptions) {
                        if(name.equals(connect)&&desc.equals("()V")) {
                            replaced[0]=true;
                            MethodVisitor m=super.visitMethod(access,name,desc,signature,exceptions);
                            m.visitCode();
                            m.visitLdcInsn("jimm.fixture.connect");m.visitLdcInsn("called");
                            m.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/System","setProperty","(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",false);
                            m.visitInsn(Opcodes.POP);m.visitInsn(Opcodes.ICONST_0);
                            m.visitMethodInsn(Opcodes.INVOKESTATIC,owner,setDisconnected,"(Z)V",false);
                            m.visitInsn(Opcodes.RETURN);m.visitMaxs(2,0);m.visitEnd();return null;
                        }
                        MethodVisitor visitor=super.visitMethod(access,name,desc,signature,exceptions);
                        if(name.equals(initialSequence)&&desc.equals("()I"))
                            return new MethodVisitor(Opcodes.ASM9,visitor) {
                                public void visitMethodInsn(int opcode,String owner,String name,String desc,boolean itf) {
                                    if(owner.equals("java/lang/System")&&name.equals("currentTimeMillis")) {
                                        replaced[1]=true;
                                        super.visitLdcInsn("jimm.fixture.seed");
                                        super.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/System","getProperty","(Ljava/lang/String;)Ljava/lang/String;",false);
                                        super.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/Long","parseLong","(Ljava/lang/String;)J",false);
                                    } else super.visitMethodInsn(opcode,owner,name,desc,itf);
                                }
                            };
                        return visitor;
                    }
                },0);
                if(!replaced[0]||!replaced[1])throw new AssertionError("Lifecycle fixture entry points missing");
                data=writer.toByteArray();patched=true;
            }
            output.putNextEntry(new JarEntry(entry.getName()));output.write(data);output.closeEntry();
        }
        input.close();output.close();if(!patched)throw new AssertionError("Icq class missing");
    }
}
