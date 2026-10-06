import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Isolate startup and capture Connector/roots/device support; keep filesystem bodies intact. */
public class FileSystemFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String target=args[3];Set<String> owners=new HashSet<>(Arrays.asList(ref?"b":"jimm/JSR75FileSystem",ref?"w":"jimm/FileSystem",ref?"n":"jimm/MotorolaFileSystem"));
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owners.contains(owner)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,errors)){
                        public void visitFieldInsn(int op,String owner,String name,String desc){if(target.equals("MOTOROLA")&&op==GETSTATIC&&owner.equals("jimm/Jimm")&&name.equals(ref?"a":"supports_JSR75")&&desc.equals("Z"))super.visitMethodInsn(INVOKESTATIC,"FileSystemIO","jsr","()Z",false);else super.visitFieldInsn(op,owner,name,desc);}
                        public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                            if(owner.equals("javax/microedition/io/Connector")&&name.equals("open")&&desc.equals("(Ljava/lang/String;)Ljavax/microedition/io/Connection;"))super.visitMethodInsn(INVOKESTATIC,"FileSystemIO","open",desc,false);
                            else if(owner.endsWith("/FileSystemRegistry")&&name.equals("listRoots"))super.visitMethodInsn(INVOKESTATIC,"FileSystemIO",desc.equals("()[Ljava/lang/String;")?"rootsMoto":"roots",desc,false);
                            else super.visitMethodInsn(op,owner,name,desc,itf);
                        }
                    };}},0);bytes=w.toByteArray();
                }GraphicsFixture.entry(out,e.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Files,,GraphicsMIDlet");a.putValue("MIDlet-Name","Filesystem probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream bytes=new ByteArrayOutputStream();manifest.write(bytes);GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",bytes.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[4]),"*.class")){for(Path file:files){String name=file.getFileName().toString();if(!name.startsWith("FileSystemIO")&&!name.startsWith("FileTransferIO")&&!name.startsWith("TransportIO"))continue;ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,name,w.toByteArray());}}
            if(args.length>5)try(JarFile api=new JarFile(args[5])){String name=target.equals("MOTOROLA")?"com/motorola/io/FileConnection.class":"com/siemens/mp/io/file/FileConnection.class";GraphicsFixture.entry(out,name,api.getInputStream(api.getJarEntry(name)).readAllBytes());}
        }
    }
}
