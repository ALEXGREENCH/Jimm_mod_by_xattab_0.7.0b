import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;

/** Recover checked Exceptions attributes discarded by the release obfuscator. */
public class InferExceptions {
 static Map<String,ClassNode> all = new HashMap<>();
 static Map<MethodNode,Set<String>> thrown = new HashMap<>();
 static Map<MethodNode,Frame<BasicValue>[]> frames = new HashMap<>();
 static boolean subtype(String child,String parent) {
  if(child.equals(parent) || parent.equals("java/lang/Throwable"))return true;
  try { return Class.forName(parent.replace('/','.')).isAssignableFrom(Class.forName(child.replace('/','.'))); } catch(Throwable e){}
  ClassNode c=all.get(child);return c!=null && c.superName!=null && subtype(c.superName,parent);
 }
 static boolean checked(String s) {return subtype(s,"java/lang/Throwable") && !subtype(s,"java/lang/RuntimeException") && !subtype(s,"java/lang/Error");}
 static MethodNode method(String owner,String name,String desc) {
  ClassNode c=all.get(owner);
  if(c==null)return null;
  for(MethodNode m:c.methods)if(m.name.equals(name)&&m.desc.equals(desc))return m;
  MethodNode r=method(c.superName,name,desc); if(r!=null)return r;
  for(String i:c.interfaces){r=method(i,name,desc);if(r!=null)return r;}return null;
 }
 public static void main(String[] args)throws Exception {
  List<ClassNode> own=new ArrayList<>();
  for(int i=0;i<args.length;i++)try(JarFile j=new JarFile(args[i])){
   for(JarEntry e:Collections.list(j.entries()))if(e.getName().endsWith(".class")){
    ClassNode c=new ClassNode();new ClassReader(j.getInputStream(e)).accept(c,0);all.put(c.name,c);if(i==0)own.add(c);
   }
  }
  BasicInterpreter bi=new BasicInterpreter(Opcodes.ASM9){
   public BasicValue newValue(Type t){return t==null?BasicValue.UNINITIALIZED_VALUE:t==Type.VOID_TYPE?null:new BasicValue(t);}
   public BasicValue merge(BasicValue a,BasicValue b){return a.equals(b)?a:BasicValue.REFERENCE_VALUE;}
  };
  for(ClassNode c:all.values())for(MethodNode m:c.methods)thrown.put(m,new TreeSet<>(m.exceptions));
  for(ClassNode c:own)for(MethodNode m:c.methods)if(m.instructions.size()>0)frames.put(m,new Analyzer<>(bi).analyze(c.name,m));
  boolean changed=true;
  for(int pass=0;changed && pass<100;pass++){
   changed=false;
   for(ClassNode c:own)for(MethodNode m:c.methods){
    Set<String> result=thrown.get(m);
    for(int k=0;k<m.instructions.size();k++){
     AbstractInsnNode ins=m.instructions.get(k);Set<String> ex=new HashSet<>();
     if(ins instanceof MethodInsnNode){MethodInsnNode call=(MethodInsnNode)ins;MethodNode target=method(call.owner,call.name,call.desc);if(target!=null)ex.addAll(thrown.get(target));
      else try{Class<?> cls=Class.forName(call.owner.replace('/','.'));for(java.lang.reflect.Method rm:cls.getMethods())if(rm.getName().equals(call.name)&&Type.getMethodDescriptor(rm).equals(call.desc))for(Class<?> t:rm.getExceptionTypes())ex.add(t.getName().replace('.','/'));}catch(Throwable e){}
     }
     if(ins.getOpcode()==Opcodes.ATHROW){Frame<BasicValue> f=frames.get(m)[k];if(f!=null){Type t=f.getStack(f.getStackSize()-1).getType();if(t!=null && !t.getInternalName().equals("java/lang/Throwable"))ex.add(t.getInternalName());}}
     for(String x:ex)if(checked(x)){
      boolean caught=false;
      for(TryCatchBlockNode t:m.tryCatchBlocks)if(k>=m.instructions.indexOf(t.start)&&k<m.instructions.indexOf(t.end)&&(t.type==null||subtype(x,t.type)))caught=true;
      if(!caught)changed|=result.add(x);
     }
    }
    if(!m.name.startsWith("<") && (m.access&(Opcodes.ACC_STATIC|Opcodes.ACC_PRIVATE))==0){
     List<String> parents=new ArrayList<>(c.interfaces);if(c.superName!=null)parents.add(c.superName);
     for(String p:parents){MethodNode base=method(p,m.name,m.desc);if(base!=null && all.get(p)!=null && own.contains(all.get(p)))changed|=thrown.get(base).addAll(result);}
    }
   }
  }
  for(ClassNode c:own)for(MethodNode m:c.methods)if(!thrown.get(m).isEmpty())System.out.println(c.name+"\t"+m.name+"\t"+String.join(",",thrown.get(m)));
 }
}
