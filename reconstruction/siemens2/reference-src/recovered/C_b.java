package recovered;

import com.siemens.mp.io.file.FileConnection;
import com.siemens.mp.io.file.FileSystemRegistry;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;

public final class C_b extends C_at {
   private FileConnection f_b_a_1074fc08;

   public static String[] m_a_855a3144(String var0, boolean var1) {
      String var2 = null;

      try {
         if (var0.equals("/")) {
            Vector var5 = new Vector();
            Enumeration var8 = FileSystemRegistry.listRoots();

            while (var8.hasMoreElements()) {
               var5.addElement((String)var8.nextElement());
            }

            var2 = new String[var5.size()];
            var5.copyInto(var2);
         } else {
            FileConnection var6;
            Enumeration var10 = (var6 = (FileConnection)Connector.open("file://" + var0)).list();
            var6.close();
            Vector var7;
            (var7 = new Vector()).addElement("../");

            while (var10.hasMoreElements()) {
               String var3 = (String)var10.nextElement();
               if (!(var1 & !var3.endsWith("/"))) {
                  var7.addElement(var3);
               }
            }

            var2 = new String[var7.size()];
            var7.copyInto(var2);
         }

         return var2;
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new C_aq(191, 0, true);
      }
   }

   public final void m_a_aad3b1ff(String var1) {
      this.f_b_a_1074fc08 = (FileConnection)Connector.open("file://" + var1);
   }

   public final OutputStream m_a_75f818f3() {
      if (!this.f_b_a_1074fc08.exists()) {
         this.f_b_a_1074fc08.create();
      } else if (this.f_b_a_1074fc08.exists() & true) {
         this.f_b_a_1074fc08.delete();
         this.f_b_a_1074fc08.create();
      }

      return this.f_b_a_1074fc08.openOutputStream();
   }

   public final InputStream m_a_b52a89f8() {
      return this.f_b_a_1074fc08.openInputStream();
   }

   public final void m_a_9b75() {
      try {
         if (this.f_b_a_1074fc08 != null) {
            this.f_b_a_1074fc08.close();
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   public final long m_a_9b69() {
      return this.f_b_a_1074fc08 != null ? this.f_b_a_1074fc08.fileSize() : -1L;
   }

   public final String m_a_73cf11cb() {
      return this.f_b_a_1074fc08 != null ? this.f_b_a_1074fc08.getName() : null;
   }
}
