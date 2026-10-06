import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Restore the real Emotions after menu fixtures; capture only resource/device/editor boundaries. */
public class EmotionsFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String emotion=ref?"bo":"jimm/Emotions",image=ref?"f":"DrawControls/ImageList",animation=ref?"cm":"DrawControls/AniImageList",ui=ref?"cf":"jimm/JimmUI",light=ref?"aj":"DrawControls/LightControl",fixed=args[1]+".list.jar";
        VirtualListFixture.main(new String[]{args[0],fixed,args[2],args[3]});
        try(JarFile in=new JarFile(fixed);JarFile original=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();String owner=e.getName().replace(".class","");byte[] bytes=(owner.equals(emotion)?original:in).getInputStream((owner.equals(emotion)?original:in).getJarEntry(e.getName())).readAllBytes();
                if(Arrays.asList(emotion,image,animation,ui).contains(owner)){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,errors)){public void visitMethodInsn(int op,String target,String name,String desc,boolean itf){
                        if((owner.equals(emotion)||owner.equals(animation))&&target.equals("java/lang/Class")&&name.equals("getResourceAsStream"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","resource","(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;",false);
                        else if((owner.equals(image)||owner.equals(animation))&&target.equals("javax/microedition/lcdui/Image")&&name.equals("createImage")&&desc.equals("(Ljava/lang/String;)Ljavax/microedition/lcdui/Image;"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","image",desc,false);
                        else if(owner.equals(animation)&&target.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","start","(Ljava/lang/Thread;)V",false);
                        else if(owner.equals(emotion)&&target.equals(light)&&desc.equals("(Z)V"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","flash",desc,false);
                        else if(owner.equals(emotion)&&target.equals("java/lang/System")&&name.equals("gc"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","collect",desc,false);
                        else if(owner.equals(emotion)&&target.equals("javax/microedition/lcdui/TextBox")&&name.equals("getCaretPosition"))super.visitMethodInsn(INVOKESTATIC,"EmotionsIO","caret","(Ljavax/microedition/lcdui/TextBox;)I",false);
                        else if(owner.equals(ui)&&target.equals("javax/microedition/lcdui/Display")&&name.equals("setCurrent")&&desc.equals("(Ljavax/microedition/lcdui/Displayable;)V"))super.visitMethodInsn(INVOKESTATIC,"MenuIO","show","(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V",false);
                        else super.visitMethodInsn(op,target,name,desc,itf);
                    }};}},0);bytes=w.toByteArray();
                }GraphicsFixture.entry(out,e.getName(),bytes);
            }
            try(DirectoryStream<Path> files=Files.newDirectoryStream(Paths.get(args[3]),"*.class")){for(Path file:files){String name=file.getFileName().toString();if(!name.startsWith("ResourceIO")&&!name.startsWith("EmotionsIO"))continue;ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);GraphicsFixture.entry(out,name,w.toByteArray());}}
            GraphicsFixture.entry(out,"emotions/atlas.png",GraphicsFixture.png(53,31));GraphicsFixture.entry(out,"emotions/broken.png",new byte[]{0,1,2,3});GraphicsFixture.entry(out,"emotions/anim/1.png",GraphicsFixture.png(64,16));GraphicsFixture.entry(out,"emotions/anim/2.png",GraphicsFixture.png(80,24));
        }
    }
}
