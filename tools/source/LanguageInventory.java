import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/** Use the original language parser/key allocator to recover names for a generated .lng. */
public class LanguageInventory {
    public static void main(String[] args)throws Exception {
        Class<?> task=Class.forName("langs.LangsTask");
        Method read=task.getDeclaredMethod("readLangFile",String.class),key=task.getDeclaredMethod("keyToShortKey",String.class);
        read.setAccessible(true);key.setAccessible(true);
        Vector pairs=(Vector)read.invoke(null,args[0]);
        if(args.length>2) {
            Set<String> present=new HashSet<String>();for(Object pair:pairs)present.add(((String[])pair)[0]);
            Vector ideal=(Vector)read.invoke(null,args[2]);
            // The supported Czech table omits one ideal-language key, allocated when written.
            for(Object pair:ideal)if(!present.contains(((String[])pair)[0]))pairs.add(pair);
        }
        try(DataOutputStream out=new DataOutputStream(new FileOutputStream(args[1]))) {
            out.writeShort(pairs.size());
            for(Object pair:pairs) {
                String[] value=(String[])pair;out.writeUTF(value[0]);out.writeUTF((String)key.invoke(null,value[0]));out.writeUTF(value[1]);
            }
        }
    }
}
