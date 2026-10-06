import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.objectweb.asm.*;

/** Keep graphics/loaders/animation intact; isolate startup and capture clock, sleep and refresh. */
public class GraphicsFixture implements Opcodes {
    static byte[] midlet(){
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);w.visit(V1_5,ACC_PUBLIC,"GraphicsMIDlet",null,"javax/microedition/midlet/MIDlet",null);
        MethodVisitor m=w.visitMethod(ACC_PUBLIC,"<init>","()V",null,null);m.visitCode();m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKESPECIAL,"javax/microedition/midlet/MIDlet","<init>","()V",false);m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();
        for(String name:new String[]{"startApp","pauseApp","destroyApp"}){m=w.visitMethod(ACC_PUBLIC,name,name.equals("destroyApp")?"(Z)V":"()V",null,null);m.visitCode();m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();}w.visitEnd();return w.toByteArray();
    }
    static byte[] png(int width,int height)throws Exception {
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int y=0;y<height;y++)for(int x=0;x<width;x++)image.setRGB(x,y,(x+y)%7==0?0:0xff000000|((x*31)&255)<<16|((y*47)&255)<<8|((x*17+y*13)&255));
        ByteArrayOutputStream out=new ByteArrayOutputStream();ImageIO.write(image,"png",out);return out.toByteArray();
    }
    static void entry(JarOutputStream out,String name,byte[] data)throws Exception{out.putNextEntry(new JarEntry(name));out.write(data);out.closeEntry();}
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String target=args[3];
        final String animation=ref?(target.equals("MIDP2")?"cm":target.equals("MOTOROLA")?"cl":"ck"):"DrawControls/AniImageList";
        final String ui=ref?(target.equals("MIDP2")?"cf":"cd"):"jimm/JimmUI",list=ref?(target.equals("MIDP2")?"cd":"cb"):"DrawControls/VirtualList";
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF"))continue;byte[] data=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(animation+".class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(data).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] errors){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,errors)){
                        public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                            if(owner.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"GraphicsIO","time",desc,false);
                            else if(owner.equals("java/lang/Thread")&&name.equals("sleep"))super.visitMethodInsn(INVOKESTATIC,"GraphicsIO","sleep",desc,false);
                            else if(owner.equals("java/lang/Thread")&&name.equals("start"))super.visitMethodInsn(INVOKESTATIC,"GraphicsIO","start","(Ljava/lang/Thread;)V",false);
                            else if(owner.equals(ui)&&desc.equals("()Ljava/lang/Object;"))super.visitMethodInsn(INVOKESTATIC,"GraphicsIO","screen",desc,false);
                            else if(owner.equals(list)&&desc.equals("()V"))super.visitMethodInsn(INVOKESTATIC,"GraphicsIO","repaint","(Ljava/lang/Object;)V",false);
                            else super.visitMethodInsn(op,owner,name,desc,itf);
                        }
                    };}},0);data=w.toByteArray();
                }entry(out,e.getName(),data);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Graphics,,GraphicsMIDlet");a.putValue("MIDlet-Name","Graphics probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream bytes=new ByteArrayOutputStream();manifest.write(bytes);entry(out,"META-INF/MANIFEST.MF",bytes.toByteArray());entry(out,"GraphicsMIDlet.class",midlet());
            for(String name:new String[]{"GraphicsIO","GraphicsIO$Stop"}){ClassWriter w=new ClassWriter(0);new ClassReader(Files.readAllBytes(Paths.get(args[4],name+".class"))).accept(new ClassVisitor(ASM9,w){public void visit(int v,int a,String n,String s,String p,String[] i){super.visit(V1_5,a,n,s,p,i);}},ClassReader.SKIP_FRAMES);entry(out,name+".class",w.toByteArray());}
            entry(out,"graphics/atlas.png",png(53,31));entry(out,"graphics/broken.png",new byte[]{0,1,2,3});
            for(int scenario=0;scenario<3;scenario++) {
                ByteArrayOutputStream data=new ByteArrayOutputStream();data.write(2);
                data.write(4);data.write(3);data.write(new byte[]{0,1,2,2,3,0});
                data.write(4);data.write(scenario==2?0:2);if(scenario!=2)data.write(new byte[]{1,1,3,3});
                entry(out,"graphics/anim"+scenario+"/animate.bin",data.toByteArray());entry(out,"graphics/anim"+scenario+"/1.png",png(64,16));if(scenario!=1)entry(out,"graphics/anim"+scenario+"/2.png",png(80,24));
            }
        }
    }
}
