import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture precisely NEW Date and its zero-argument constructor in one actual date method. */
public final class UtilAbiFixture implements Opcodes {
    public static void main(String[] args) throws Exception {
        String owner=args[2], selected=args[3], descriptor=args[4]; int[] captures={0,0};
        try(JarFile input=new JarFile(args[0]); JarOutputStream output=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            for(JarEntry entry:Collections.list(input.entries())) {
                byte[] bytes=input.getInputStream(entry).readAllBytes();
                if(entry.getName().equals(owner+".class")) {
                    ClassWriter writer=new ClassWriter(0);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions) {
                            MethodVisitor visitor=super.visitMethod(access,name,desc,sig,exceptions);
                            if(!name.equals(selected)||!desc.equals(descriptor))return visitor;
                            return new MethodVisitor(ASM9,visitor) {
                                public void visitTypeInsn(int op,String type) {
                                    if(op==NEW&&type.equals("java/util/Date")){type="UtilAbiClock$Clock";captures[0]++;}
                                    super.visitTypeInsn(op,type);
                                }
                                public void visitMethodInsn(int op,String type,String name,String desc,boolean itf) {
                                    if(op==INVOKESPECIAL&&type.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")) {
                                        type="UtilAbiClock$Clock";captures[1]++;
                                    }
                                    super.visitMethodInsn(op,type,name,desc,itf);
                                }
                            };
                        }
                    },0); bytes=writer.toByteArray();
                }
                output.putNextEntry(new JarEntry(entry.getName())); output.write(bytes);output.closeEntry();
            }
            for(String name:new String[]{"UtilAbiClock","UtilAbiClock$Clock"}) {
                output.putNextEntry(new JarEntry(name+".class"));output.write(Files.readAllBytes(Paths.get(args[5],name+".class")));output.closeEntry();
            }
        }
        if(captures[0]!=1||captures[1]!=1)throw new AssertionError(Arrays.toString(captures));
        System.out.println("PASS Util ABI fixture: exactly two external Date constructor captures");
    }
}
