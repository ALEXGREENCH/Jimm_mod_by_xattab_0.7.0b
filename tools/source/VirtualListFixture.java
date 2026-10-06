import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Keep actual painting/input/controllers; supply identical list data and callback counters. */
public class VirtualListFixture implements Opcodes {
    static final String HELPER="RecoveryList";
    static void increment(MethodVisitor m,String field){m.visitVarInsn(ALOAD,0);m.visitInsn(DUP);m.visitFieldInsn(GETFIELD,HELPER,field,"I");m.visitInsn(ICONST_1);m.visitInsn(IADD);m.visitFieldInsn(PUTFIELD,HELPER,field,"I");}
    static byte[] list(boolean ref){
        String list=ref?"cd":"DrawControls/VirtualList",item=ref?"cn":"DrawControls/ListItem",icon=ref?"e":"DrawControls/Icon",callbacks=ref?"bx":"DrawControls/VirtualListCommands";
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);w.visit(V1_5,ACC_PUBLIC,HELPER,null,list,new String[]{callbacks,"javax/microedition/lcdui/CommandListener"});
        for(String field:new String[]{"count","rowHeight","itemStyle","itemColor","commands","moves","clicks","hooks","hookIndex","hookMode","rowOffset"})w.visitField(ACC_PUBLIC,field,"I",null,null).visitEnd();
        w.visitField(ACC_PUBLIC,"consumeHook","Z",null,null).visitEnd();for(String field:new String[]{"rowImage","rowXStatus","rowHappy","rowBirthday","rowAuth","rowIgnore","rowVisibility","rowClient"})w.visitField(ACC_PUBLIC,field,"L"+icon+";",null,null).visitEnd();w.visitField(ACC_PUBLIC,"lastCommand","Ljava/lang/String;",null,null).visitEnd();
        MethodVisitor m=w.visitMethod(ACC_PUBLIC,"<init>","(Ljava/lang/String;)V",null,null);m.visitCode();m.visitVarInsn(ALOAD,0);m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKESPECIAL,list,"<init>","(Ljava/lang/String;)V",false);
        for(String field:new String[]{"count","rowHeight","itemColor"}){m.visitVarInsn(ALOAD,0);m.visitLdcInsn(field.equals("count")?3:field.equals("rowHeight")?16:0x123456);m.visitFieldInsn(PUTFIELD,HELPER,field,"I");}
        m.visitVarInsn(ALOAD,0);m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKEVIRTUAL,list,ref?"a":"setVLCommands","(L"+callbacks+";)V",false);m.visitVarInsn(ALOAD,0);m.visitVarInsn(ALOAD,0);m.visitMethodInsn(INVOKEVIRTUAL,list,ref?"a":"setCommandListener","(Ljavax/microedition/lcdui/CommandListener;)V",false);m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();
        for(String[] method:new String[][]{{ref?"a":"getSize","()I","count"},{ref?"a":"getItemHeight","(I)I","rowHeight"}}){m=w.visitMethod(ACC_PUBLIC,method[0],method[1],null,null);m.visitCode();m.visitVarInsn(ALOAD,0);m.visitFieldInsn(GETFIELD,HELPER,method[2],"I");m.visitInsn(IRETURN);m.visitMaxs(0,0);m.visitEnd();}
        m=w.visitMethod(ACC_PUBLIC,ref?"a":"get","(IL"+item+";)V",null,null);m.visitCode();m.visitVarInsn(ALOAD,2);m.visitLdcInsn("Sample row");m.visitFieldInsn(PUTFIELD,item,ref?"a":"text","Ljava/lang/String;");
        for(String[] field:new String[][]{{ref?"a":"fontStyle","itemStyle","I"},{ref?"b":"color","itemColor","I"},{ref?"c":"horizOffset","rowOffset","I"},{ref?"a":"image","rowImage","L"+icon+";"},{ref?"b":"xStatusImg","rowXStatus","L"+icon+";"},{ref?"c":"happyImg","rowHappy","L"+icon+";"},{ref?"d":"bDayImg","rowBirthday","L"+icon+";"},{ref?"e":"authImg","rowAuth","L"+icon+";"},{ref?"f":"ignoreImg","rowIgnore","L"+icon+";"},{ref?"g":"visibilityImg","rowVisibility","L"+icon+";"},{ref?"h":"clientImg","rowClient","L"+icon+";"}}){m.visitVarInsn(ALOAD,2);m.visitVarInsn(ALOAD,0);m.visitFieldInsn(GETFIELD,HELPER,field[1],field[2]);m.visitFieldInsn(PUTFIELD,item,field[0],field[2]);}
        m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();
        m=w.visitMethod(ACC_PROTECTED,ref?"a":"pointerPressedOnUtem","(IIII)Z",null,null);m.visitCode();increment(m,"hooks");m.visitVarInsn(ALOAD,0);m.visitVarInsn(ILOAD,1);m.visitFieldInsn(PUTFIELD,HELPER,"hookIndex","I");m.visitVarInsn(ALOAD,0);m.visitVarInsn(ILOAD,4);m.visitFieldInsn(PUTFIELD,HELPER,"hookMode","I");m.visitVarInsn(ALOAD,0);m.visitFieldInsn(GETFIELD,HELPER,"consumeHook","Z");m.visitInsn(IRETURN);m.visitMaxs(0,0);m.visitEnd();
        for(String[] method:new String[][]{{ref?"a":"vlCursorMoved","(L"+list+";)V","moves"},{ref?"b":"vlItemClicked","(L"+list+";)V","clicks"},{ref?"a":"vlKeyPress","(L"+list+";II)V",""}}){m=w.visitMethod(ACC_PUBLIC,method[0],method[1],null,null);m.visitCode();if(!method[2].isEmpty())increment(m,method[2]);m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();}
        m=w.visitMethod(ACC_PUBLIC,"commandAction","(Ljavax/microedition/lcdui/Command;Ljavax/microedition/lcdui/Displayable;)V",null,null);m.visitCode();increment(m,"commands");m.visitVarInsn(ALOAD,0);m.visitVarInsn(ALOAD,1);m.visitMethodInsn(INVOKEVIRTUAL,"javax/microedition/lcdui/Command","getLabel","()Ljava/lang/String;",false);m.visitFieldInsn(PUTFIELD,HELPER,"lastCommand","Ljava/lang/String;");m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();w.visitEnd();return w.toByteArray();
    }
    public static void main(String[] args)throws Exception{
        boolean ref=args[2].equals("reference");String vl=ref?"cd":"DrawControls/VirtualList",fixed=args[1]+".menu.jar";MenuFixture.main(new String[]{args[0],fixed,args[2],args[3]});
        try(JarFile in=new JarFile(fixed);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))){
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()){
                JarEntry e=entries.nextElement();byte[] bytes=in.getInputStream(e).readAllBytes();
                if(e.getName().equals(vl+".class")){
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,w){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){return new MethodVisitor(ASM9,super.visitMethod(access,name,desc,sig,exceptions)){public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf){if(owner.equals("java/lang/System")&&name.equals("currentTimeMillis"))super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);else super.visitMethodInsn(op,owner,name,desc,itf);}};}},0);bytes=w.toByteArray();
                }
                out.putNextEntry(new JarEntry(e.getName()));out.write(bytes);out.closeEntry();
            }
            out.putNextEntry(new JarEntry(HELPER+".class"));out.write(list(ref));out.closeEntry();
        }
    }
}
