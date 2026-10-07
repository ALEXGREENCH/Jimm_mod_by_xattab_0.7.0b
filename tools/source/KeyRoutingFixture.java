import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Isolated VirtualList key routing: suppress class initialization and capture its immediate destinations only. */
public class KeyRoutingFixture implements Opcodes {
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String platform=args[4];
        String list=ref?(platform.equals("MIDP2")?"cd":"cb"):"DrawControls/VirtualList";
        String light=ref?"aj":"DrawControls/LightControl";
        String options=ref?(platform.equals("MIDP2")?"cj":platform.equals("MOTOROLA")?"ci":"ch"):"jimm/Options";
        String timer=ref?(platform.equals("SIEMENS2")?"ar":"at"):"jimm/TimerTasks";
        String reaction=ref?(platform.equals("MIDP2")?"c":"b"):"keyReaction";
        int[] hits=new int[5];
        ClassWriter child=new ClassWriter(0);child.visit(V1_5,ACC_PUBLIC,"RecoveryKeyList",null,list,null);
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(list+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){
                        public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){
                            if((a&ACC_ABSTRACT)!=0){MethodVisitor m=child.visitMethod(ACC_PUBLIC,n,d,s,ex);m.visitCode();Type result=Type.getReturnType(d);switch(result.getSort()){case Type.VOID:m.visitInsn(RETURN);break;case Type.LONG:m.visitInsn(LCONST_0);m.visitInsn(LRETURN);break;case Type.DOUBLE:m.visitInsn(DCONST_0);m.visitInsn(DRETURN);break;case Type.FLOAT:m.visitInsn(FCONST_0);m.visitInsn(FRETURN);break;case Type.ARRAY:case Type.OBJECT:m.visitInsn(ACONST_NULL);m.visitInsn(ARETURN);break;default:m.visitInsn(ICONST_0);m.visitInsn(IRETURN);}m.visitMaxs(2,Type.getArgumentsAndReturnSizes(d)>>2);m.visitEnd();}
                            MethodVisitor m=super.visitMethod(a,n,d,s,ex);
                            if(n.equals("<clinit>")){hits[0]++;m.visitCode();m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();return null;}
                            boolean route=d.equals("(I)V")&&Arrays.asList(ref?"f":"keyPressed",ref?"g":"keyRepeated",ref?"h":"keyReleased").contains(n)||d.equals("(II)V")&&n.equals(ref?"a":"doKeyreaction");
                            if(!route)return m;
                            return new MethodVisitor(ASM9,m){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){
                                if(owner.equals(light)&&name.equals(ref?"a":platform.equals("MOTOROLA")?"flash":"reset")&&(desc.equals("()V")||desc.equals("(Z)V"))){hits[1]++;super.visitMethodInsn(INVOKESTATIC,"KeyRoutingIO",desc.equals("()V")?"light":"flash",desc,false);}
                                else if(owner.equals(options)&&name.equals(ref?"a":"getBoolean")&&desc.equals("(I)Z")){hits[2]++;super.visitMethodInsn(INVOKESTATIC,"KeyRoutingIO","option",desc,false);}
                                else if(owner.equals(timer)&&name.equals(ref?"a":"setStatusTimer")&&desc.equals("()V")){hits[3]++;super.visitMethodInsn(INVOKESTATIC,"KeyRoutingIO","status",desc,false);}
                                else if(owner.equals(list)&&name.equals(reaction)&&desc.equals("(II)V")){hits[4]++;super.visitMethodInsn(INVOKESTATIC,"KeyRoutingIO","reaction","(Ljava/lang/Object;II)V",false);}
                                else super.visitMethodInsn(op,owner,name,desc,itf);
                            }};
                        }
                    },0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            if(!Arrays.equals(hits,new int[]{1,platform.equals("SIEMENS2")?0:1,1,1,2}))throw new AssertionError("Key routing capture sites "+Arrays.toString(hits));
            child.visitEnd();out.putNextEntry(new JarEntry("RecoveryKeyList.class"));out.write(child.toByteArray());out.closeEntry();
            out.putNextEntry(new JarEntry("KeyRoutingIO.class"));out.write(Files.readAllBytes(Paths.get(args[3],"KeyRoutingIO.class")));out.closeEntry();
        }
    }
}
