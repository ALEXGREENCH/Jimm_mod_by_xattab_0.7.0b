package recovered;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

final class C_b extends C_at {
   private FileConnection f_b_a_dac33527;

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
            Enumeration var10 = (var6 = (FileConnection)Connector.open("file://localhost" + var0)).list();
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

   public static long m_a_aad3b1f3(String var0) {
      long var1 = 0L;
      FileConnection var3;
      var1 = (var3 = (FileConnection)Connector.open("file://localhost/" + var0)).totalSize();
      var3.close();
      return var1;
   }

   public final void m_a_aad3b1ff(String var1) {
      this.f_b_a_dac33527 = (FileConnection)Connector.open("file://" + var1);
   }

   public final OutputStream m_a_75f818f3() {
      if (!this.f_b_a_dac33527.exists()) {
         this.f_b_a_dac33527.create();
      } else if (this.f_b_a_dac33527.exists() & true) {
         this.f_b_a_dac33527.delete();
         this.f_b_a_dac33527.create();
      }

      return this.f_b_a_dac33527.openOutputStream();
   }

   public final InputStream m_a_b52a89f8() {
      return this.f_b_a_dac33527.openInputStream();
   }

   public final void m_a_9b75() {
      try {
         if (this.f_b_a_dac33527 != null) {
            this.f_b_a_dac33527.close();
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   public final long m_a_9b69() {
      return this.f_b_a_dac33527 != null ? this.f_b_a_dac33527.fileSize() : -1L;
   }
}
