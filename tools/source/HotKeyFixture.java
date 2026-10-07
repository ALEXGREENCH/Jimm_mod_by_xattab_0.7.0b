import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;

/** Minimal MIDlet host and enumerated call-site substitutions only. */
public class HotKeyFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        Properties spec=new Properties();try(InputStream in=Files.newInputStream(Paths.get(args[3]))){spec.load(in);}
        Map<String,String[]> calls=new HashMap<>();
        for(String line:Files.readAllLines(Paths.get(args[4]))) {
            String[] p=line.split("\t",-1);if(p.length!=4)throw new AssertionError(line);
            if(calls.put(p[0]+" "+p[1],new String[]{p[2],p[3]})!=null)throw new AssertionError("Duplicate capture");
        }
        String ui=spec.getProperty("ui").replace('.','/'),contact=spec.getProperty("contact").replace('.','/'),light=spec.getProperty("light").replace('.','/');
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry e=entries.nextElement();if(e.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")||e.getName().equals("HotKeyIO.class")||e.getName().equals("GraphicsMIDlet.class"))continue;
                byte[] bytes=in.getInputStream(e).readAllBytes();String owner=e.getName().replace(".class","");
                if(Arrays.asList(ui,contact,light).contains(owner)) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            boolean dispatcher=owner.equals(ui)&&n.equals(spec.getProperty("action"))&&d.startsWith("(IL");
                            boolean history=owner.equals(contact)&&n.equals(spec.getProperty("historyMethod"))&&d.equals("()V");
                            if(!dispatcher&&!history&&!owner.equals(light))return m;
                            return new MethodVisitor(ASM9,m) {
                                public void visitMethodInsn(int op,String target,String name,String desc,boolean itf) {
                                    String[] capture=calls.get(op+" "+target+'.'+name+desc);
                                    if(capture==null)super.visitMethodInsn(op,target,name,desc,itf);
                                    else super.visitMethodInsn(INVOKESTATIC,"HotKeyIO",capture[0],capture[1],false);
                                }
                            };
                        }
                    },0);bytes=w.toByteArray();
                }
                GraphicsFixture.entry(out,e.getName(),bytes);
            }
            Manifest manifest=new Manifest();Attributes a=manifest.getMainAttributes();a.putValue("Manifest-Version","1.0");a.putValue("MIDlet-1","Hotkeys,,GraphicsMIDlet");a.putValue("MIDlet-Name","Hotkey probe");a.putValue("MIDlet-Version","1.0");a.putValue("MIDlet-Vendor","Recovery probe");ByteArrayOutputStream data=new ByteArrayOutputStream();manifest.write(data);
            GraphicsFixture.entry(out,"META-INF/MANIFEST.MF",data.toByteArray());GraphicsFixture.entry(out,"GraphicsMIDlet.class",GraphicsFixture.midlet());GraphicsFixture.entry(out,"HotKeyIO.class",Files.readAllBytes(Paths.get(args[2],"HotKeyIO.class")));
        }
    }
}
