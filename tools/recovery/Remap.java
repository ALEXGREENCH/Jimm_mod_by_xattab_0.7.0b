import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;

/** Make ProGuard's return-type/field overloads expressible in Java source. */
public class Remap {
    public static void main(String[] args) throws Exception {
        final Map<String, ClassNode> classes = new TreeMap<>();
        try (JarFile jar = new JarFile(args[0])) {
            for (JarEntry e : Collections.list(jar.entries())) if (e.getName().endsWith(".class")) {
                ClassNode n = new ClassNode();
                new ClassReader(jar.getInputStream(e)).accept(n, 0);
                classes.put(n.name, n);
            }
        }
        final Properties names = new Properties();
        boolean reverse = args.length > 2 && args[2].equals("--reverse");
        if (args.length > 2 && !reverse) try (InputStream in = Files.newInputStream(Paths.get(args[2]))) { names.load(in); }
        Remapper mapper = new Remapper() {
            public String map(String name) {
                return classes.containsKey(name) ? names.getProperty(name, name.equals("jimm/Jimm") ? name : "recovered/C_" + name) : name;
            }
            public String mapFieldName(String owner, String name, String desc) {
                String current = owner;
                while (classes.containsKey(current)) {
                    ClassNode n = classes.get(current);
                    for (FieldNode f : n.fields) if (f.name.equals(name) && f.desc.equals(desc))
                        return "f_" + current.replace('/', '_') + "_" + name + "_" + Integer.toHexString(desc.hashCode());
                    current = n.superName;
                }
                return name;
            }
            public String mapMethodName(String owner, String name, String desc) {
                ClassNode n = classes.get(owner);
                if (n != null && name.length() <= 2) for (MethodNode m : n.methods)
                    if (m.name.equals(name) && m.desc.equals(desc) && (m.access & Opcodes.ACC_PRIVATE) != 0)
                        return "p_" + owner.replace('/', '_') + "_" + name + "_" + Integer.toHexString(desc.hashCode());
                return classes.containsKey(owner) && name.length() <= 2 ? "m_" + name + "_" + Integer.toHexString(desc.hashCode()) : name;
            }
        };
        if (reverse) {
            final Map<String,String> reverseClasses=new HashMap<>(), reverseFields=new HashMap<>(), reverseMethods=new HashMap<>();
            for(ClassNode c:classes.values()) {
                reverseClasses.put(mapper.map(c.name),c.name);
                for(FieldNode f:c.fields)reverseFields.put(mapper.mapFieldName(c.name,f.name,f.desc),f.name);
                for(MethodNode m:c.methods)reverseMethods.put(mapper.mapMethodName(c.name,m.name,m.desc)+mapper.mapMethodDesc(m.desc),m.name);
            }
            Remapper backward=new Remapper(){
                public String map(String n){return reverseClasses.getOrDefault(n,n);}
                public String mapFieldName(String o,String n,String d){return reverseFields.getOrDefault(n,n);}
                public String mapMethodName(String o,String n,String d){return reverseMethods.getOrDefault(n+d,n);}
            };
            try(JarOutputStream out=new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
                for(ClassNode c:classes.values()) {
                    Path p=Paths.get(args[3],mapper.map(c.name)+".class");
                    ClassWriter w=new ClassWriter(0);
                    new ClassReader(Files.readAllBytes(p)).accept(new ClassRemapper(w,backward),0);
                    out.putNextEntry(new JarEntry(c.name+".class"));out.write(w.toByteArray());out.closeEntry();
                }
            }
            return;
        }
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(Paths.get(args[1])))) {
            for (ClassNode n : classes.values()) {
                ClassWriter w = new ClassWriter(0);
                n.accept(new ClassRemapper(w, mapper));
                out.putNextEntry(new JarEntry(mapper.map(n.name) + ".class"));
                out.write(w.toByteArray()); out.closeEntry();
            }
        }
    }
}
