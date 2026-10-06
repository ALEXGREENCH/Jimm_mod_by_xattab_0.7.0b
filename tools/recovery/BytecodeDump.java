import java.util.*;
import java.util.jar.*;
import java.nio.file.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/** Stable symbolic JVM instructions, including branch targets and exception regions. */
public class BytecodeDump {
 static String q(Object o){String s=String.valueOf(o);StringBuilder b=new StringBuilder("\"");for(char c:s.toCharArray()){if(c=='"'||c=='\\')b.append('\\').append(c);else if(c<32)b.append(String.format("\\u%04x",(int)c));else b.append(c);}return b.append('"').toString();}
 static String arr(Collection<String> a){List<String>b=new ArrayList<>();for(String s:a)b.add(q(s));return "["+String.join(",",b)+"]";}
 static String dump(byte[] bytes){
  ClassNode c=new ClassNode();new ClassReader(bytes).accept(c,0);List<String> ms=new ArrayList<>();
  for(MethodNode m:c.methods){
   Map<LabelNode,Integer> labels=new IdentityHashMap<>();int pos=0;
   for(AbstractInsnNode i:m.instructions){if(i instanceof LabelNode)labels.put((LabelNode)i,pos);if(i.getOpcode()>=0)pos++;}
   List<String> code=new ArrayList<>(),strings=new ArrayList<>(),refs=new ArrayList<>(),handlers=new ArrayList<>();
   for(AbstractInsnNode i:m.instructions){int op=i.getOpcode();if(op<0)continue;String s=""+op;
    if(i instanceof IntInsnNode)s+=" "+((IntInsnNode)i).operand;
    if(i instanceof VarInsnNode)s+=" "+((VarInsnNode)i).var;
    if(i instanceof TypeInsnNode)s+=" "+((TypeInsnNode)i).desc;
    if(i instanceof FieldInsnNode){FieldInsnNode f=(FieldInsnNode)i;s+=" "+f.owner+"."+f.name+" "+f.desc;refs.add(s);}
    if(i instanceof MethodInsnNode){MethodInsnNode f=(MethodInsnNode)i;s+=" "+f.owner+"."+f.name+f.desc;refs.add(s);}
    if(i instanceof JumpInsnNode)s+=" "+labels.get(((JumpInsnNode)i).label);
    if(i instanceof LdcInsnNode){Object v=((LdcInsnNode)i).cst;s+=" "+v.getClass().getSimpleName()+":"+v;if(v instanceof String)strings.add((String)v);}
    if(i instanceof IincInsnNode){IincInsnNode v=(IincInsnNode)i;s+=" "+v.var+" "+v.incr;}
    if(i instanceof TableSwitchInsnNode){TableSwitchInsnNode v=(TableSwitchInsnNode)i;s+=" "+v.min+" "+v.max+" "+labels.get(v.dflt);for(LabelNode l:v.labels)s+=" "+labels.get(l);}
    if(i instanceof LookupSwitchInsnNode){LookupSwitchInsnNode v=(LookupSwitchInsnNode)i;s+=" "+v.keys+" "+labels.get(v.dflt);for(LabelNode l:v.labels)s+=" "+labels.get(l);}
    if(i instanceof MultiANewArrayInsnNode){MultiANewArrayInsnNode v=(MultiANewArrayInsnNode)i;s+=" "+v.desc+" "+v.dims;}
    if(i instanceof InvokeDynamicInsnNode)throw new IllegalArgumentException("invokedynamic unsupported for CLDC");
    code.add(s);
   }
   for(TryCatchBlockNode t:m.tryCatchBlocks)handlers.add(labels.get(t.start)+" "+labels.get(t.end)+" "+labels.get(t.handler)+" "+t.type);
   ms.add("{\"name\":"+q(m.name)+",\"desc\":"+q(m.desc)+",\"access\":"+m.access+",\"code\":"+arr(code)+",\"handlers\":"+arr(handlers)+",\"strings\":"+arr(strings)+",\"refs\":"+arr(refs)+"}");
  }
  return "{\"name\":"+q(c.name)+",\"super\":"+q(c.superName)+",\"version\":"+c.version+",\"methods\":["+String.join(",",ms)+"]}";
 }
 public static void main(String[] args)throws Exception{
  List<String> classes=new ArrayList<>();Path p=Paths.get(args[0]);
  if(Files.isDirectory(p)){try(java.util.stream.Stream<Path>s=Files.walk(p)){for(Path f:(Iterable<Path>)s.filter(x->x.toString().endsWith(".class"))::iterator)classes.add(dump(Files.readAllBytes(f)));}}
  else try(JarFile j=new JarFile(args[0])){for(JarEntry e:Collections.list(j.entries()))if(e.getName().endsWith(".class"))classes.add(dump(j.getInputStream(e).readAllBytes()));}
  System.out.print("["+String.join(",",classes)+"]");
 }
}
