import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;

/** Retain the complete sendMessage body, its constructors and typing packet code. */
public class SendTextFixture implements Opcodes {
    public static void main(String[] args)throws Exception {
        boolean ref=args[2].equals("reference");String ui=ref?"cf":"jimm/JimmUI",contact=ref?"z":"jimm/ContactItem";
        String send=ref?"cl":"jimm/comm/SendMessageAction",icq=ref?"r":"jimm/comm/Icq",chat=ref?"bt":"jimm/ChatHistory";
        String history=ref?"q":"jimm/HistoryStorage",error=ref?"bv":"jimm/JimmException",light=ref?"aj":"DrawControls/LightControl";
        String util=ref?"co":"jimm/comm/Util";
        Map<String,Integer> hits=new TreeMap<>();int[] methods={0},clocks={0},dates={0};
        try(JarFile in=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            Enumeration<JarEntry> entries=in.entries();while(entries.hasMoreElements()) {
                JarEntry entry=entries.nextElement();byte[] bytes=in.getInputStream(entry).readAllBytes();String owner=entry.getName().replace(".class","");
                if(owner.equals(ui)||owner.equals(send)||owner.equals(util)) {
                    ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS);new ClassReader(bytes).accept(new ClassVisitor(ASM9,writer) {
                        public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] ex) {
                            MethodVisitor target=super.visitMethod(access,name,desc,sig,ex);
                            boolean sender=owner.equals(ui)&&name.equals(ref?"a":"sendMessage")&&desc.equals("(Ljava/lang/String;L"+contact+";)V");
                            boolean date=owner.equals(util)&&name.equals(ref?"a":"createCurrentDate")&&desc.equals("(ZZ)J");
                            if(sender)methods[0]++;
                            if(!sender&&!owner.equals(send)&&!date)return target;
                            return new MethodVisitor(ASM9,target) {
                                public void visitMethodInsn(int opcode,String destination,String name,String desc,boolean itf) {
                                    if(date&&destination.equals("java/util/Date")&&name.equals("<init>")&&desc.equals("()V")) {
                                        dates[0]++;super.visitMethodInsn(INVOKESTATIC,"LoginIO","time","()J",false);
                                        super.visitMethodInsn(opcode,destination,name,"(J)V",itf);return;
                                    }
                                    if(owner.equals(send)&&destination.equals("java/lang/System")&&name.equals("currentTimeMillis")&&desc.equals("()J")) {
                                        clocks[0]++;super.visitMethodInsn(INVOKESTATIC,"LoginIO","time",desc,false);return;
                                    }
                                    String label=null,adapted=desc;
                                    if(sender) {
                                        if(destination.equals(icq)&&name.equals(ref?"a":"requestAction")&&desc.equals("(L"+(ref?"aa":"jimm/comm/Action")+";)V")){label="request";adapted="(Ljava/lang/Object;)V";}
                                        else if(destination.equals(error)&&name.equals(ref?"a":"handleException")&&desc.equals("(L"+error+";)V")){label="error";adapted="(Ljava/lang/Object;)V";}
                                        else if(destination.equals(chat)&&name.equals(ref?"a":"addMyMessage")&&desc.equals("(L"+contact+";Ljava/lang/String;JLjava/lang/String;J)V")){label="chat";adapted="(Ljava/lang/Object;Ljava/lang/String;JLjava/lang/String;J)V";}
                                        else if(destination.equals(history)&&name.equals(ref?"a":"addText")&&desc.equals("(Ljava/lang/String;Ljava/lang/String;BLjava/lang/String;J)V"))label="history";
                                        else if(destination.equals("java/lang/Thread")&&name.equals("sleep")&&desc.equals("(J)V"))label="sleep";
                                        else if(destination.equals(light)&&name.equals(ref?"a":"flash")&&desc.equals("(Z)V"))label="light";
                                    }
                                    if(label!=null) {
                                        if(opcode!=INVOKESTATIC)throw new AssertionError("Unexpected terminal invocation "+label);
                                        hits.put(label,hits.getOrDefault(label,0)+1);super.visitMethodInsn(INVOKESTATIC,"SendTextIO",label,adapted,false);
                                    } else super.visitMethodInsn(opcode,destination,name,desc,itf);
                                }
                            };
                        }
                    },0);bytes=writer.toByteArray();
                }
                out.putNextEntry(new JarEntry(entry.getName()));out.write(bytes);out.closeEntry();
            }
            out.putNextEntry(new JarEntry("SendTextIO.class"));out.write(Files.readAllBytes(Paths.get(args[3],"SendTextIO.class")));out.closeEntry();
        }
        if(methods[0]!=1||clocks[0]!=1||dates[0]!=1)throw new AssertionError("Missing sender/ID/date clock");
        for(String label:new String[]{"request","error","chat","history","sleep"})if(!Integer.valueOf(1).equals(hits.get(label)))throw new AssertionError("Capture counts "+hits);
        if(ref&&!Integer.valueOf(1).equals(hits.get("light")))throw new AssertionError("Missing native light call");
        System.out.println("PASS sender fixture: "+hits+", one ID clock, one Date clock");
    }
}
