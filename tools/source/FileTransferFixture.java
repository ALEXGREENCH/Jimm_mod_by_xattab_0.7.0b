import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Capture I/O, worker scheduling and destination screens while retaining transfer/browser code. */
public class FileTransferFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String ft=ref?"p":"jimm/FileTransfer",browser=ref?"u":"jimm/FileBrowser",fs=ref?"w":"jimm/FileSystem",jsr=ref?"b":"jimm/JSR75FileSystem",ui=ref?"cf":"jimm/JimmUI",contact=ref?"z":"jimm/ContactItem",list=ref?"m":"jimm/ContactList",options=ref?"cj":"jimm/Options",optionsForm=ref?"cg":"jimm/OptionsForm",vl=ref?"cd":"DrawControls/VirtualList",splash=ref?"cv":"jimm/SplashCanvas",error=ref?"bv":"jimm/JimmException";
        Set<String> owners=new HashSet<>(Arrays.asList(ft,browser,fs,jsr,ui,contact,list,options,optionsForm,vl,splash,error));
        String traffic=ref?"x":"jimm/Traffic";owners.add(traffic);
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();
            while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(owners.contains(owner)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                            MethodVisitor m=super.visitMethod(access,name,desc,sig,exceptions);String label=null;
                            if(owner.equals(ui)&&name.equals(ref?"a":"sendMessage")&&desc.equals("(Ljava/lang/String;L"+contact+";)V"))label="file-send";
                            if(owner.equals(vl)&&name.equals(ref?"a":"setBackGroundImage")&&desc.equals("(Ljava/io/InputStream;Z)V"))label="file-background";
                            if(owner.equals(error)&&name.equals(ref?"a":"handleException")&&desc.equals("(L"+error+";)V"))label="file-error";
                            if(owner.equals(optionsForm)&&name.equals(ref?"a":"callColorSchemeOptions")&&desc.equals("()V"))label="file-colors";
                            if(owner.equals(optionsForm)&&name.equals(ref?"c":"activate")&&desc.equals("()V"))label="file-options";
                            if(owner.equals(list)&&name.equals(ref?"a":"activate")&&desc.equals("()V"))label="file-contact-list";
                            if(owner.equals(contact)&&name.equals(ref?"e":"activate")&&desc.equals("()V")){MessageFixture.capture(m,"file-contact","(Ljava/lang/Object;)V");return null;}
                            if(label!=null){MessageFixture.capture(m,label,desc);return null;}
                            if(owner.equals(options)&&name.equals(ref?"d":"safe_save")&&desc.equals("()V")){
                                m.visitCode();m.visitMethodInsn(INVOKESTATIC,"FileTransferIO","save","()V",false);m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();return null;
                            }
                            boolean body=owner.equals(ft)||owner.equals(browser)||owner.equals(fs)||owner.equals(jsr)||owner.equals(traffic);
                            final String observe=owner.equals(splash)&&name.equals(ref?"l":"setProgress")&&desc.equals("(I)V")?"progress":owner.equals(splash)&&name.equals(ref?"a":"setMessage")&&desc.equals("(Ljava/lang/String;)V")?"message":null;
                            if(!body&&observe==null)return m;
                            return new MethodVisitor(ASM9,m){
                                public void visitCode(){super.visitCode();if(observe!=null){super.visitVarInsn(observe.equals("progress")?ILOAD:ALOAD,0);super.visitMethodInsn(INVOKESTATIC,"FileTransferIO",observe,desc,false);}}
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                                    if(owner.equals(traffic)&&target.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")){
                                        super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);super.visitMethodInsn(op,target,name,"(J)V",itf);return;
                                    }
                                    if(target.equals("javax/microedition/io/Connector")&&name.equals("open"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO",name,desc,false);
                                    else if(target.equals("javax/microedition/io/file/FileSystemRegistry")&&name.equals("listRoots"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","roots",desc,false);
                                    else if(target.equals("javax/microedition/lcdui/Display")&&name.equals("callSerially"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","serial","(Ljavax/microedition/lcdui/Display;Ljava/lang/Runnable;)V",false);
                                    else if(target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","start","(Ljava/lang/Thread;)V",false);
                                    else if(target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"FileTransferIO","collect",desc,false);
                                    else super.visitMethodInsn(op,target,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"*.class")){
                for(Path file:files){String name=file.getFileName().toString();if(!name.startsWith("TransportIO")&&!name.startsWith("FileTransferIO"))continue;
                    ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int version,int access,String name,String sig,String parent,String[] interfaces){super.visit(V1_5,access,name,sig,parent,interfaces);}},ClassReader.SKIP_FRAMES);
                    out.putNextEntry(new JarEntry(name));out.write(w.toByteArray());out.closeEntry();
                }
            }
        }
    }
}
