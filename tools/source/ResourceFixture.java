import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Only capture Class.getResourceAsStream in the original language loader. */
public class ResourceFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        String resource=args.length>4?args[4]:(args[2].equals("reference")?"ai":"jimm/util/ResourceBundle");
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            byte[] bytes=in.getInputStream(in.getJarEntry(resource+".class")).readAllBytes();ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,errors)){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){if(owner.equals("java/lang/Class")&&name.equals("getResourceAsStream"))super.visitMethodInsn(INVOKESTATIC,"ResourceIO","open","(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;",false);else super.visitMethodInsn(op,owner,name,desc,itf);}};}},0);out.putNextEntry(new JarEntry(resource+".class"));out.write(w.toByteArray());out.closeEntry();
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"ResourceIO*.class")){for(Path file:files){out.putNextEntry(new JarEntry(file.getFileName().toString()));out.write(Files.readAllBytes(file));out.closeEntry();}}
        }
    }
}
