package recovered;

import com.motorola.io.FileConnection;
import com.motorola.io.FileSystemRegistry;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;

final class C_x extends C_at {
   private FileConnection f_x_a_dcebe21e;

   public static String[] m_a_855a3144(String var0, boolean var1) {
      String var2 = null;

      try {
         if (var0.equals("/")) {
            String[] var3;
            var2 = new String[(var3 = FileSystemRegistry.listRoots()).length];

            for (int var4 = 0; var4 < var3.length; var4++) {
               ((Object[])var2)[var4] = var3[var4].substring(1);
            }
         } else {
            FileConnection var8;
            String[] var10 = (var8 = (FileConnection)Connector.open("file://" + var0)).list();
            var8.close();
            Vector var9;
            (var9 = new Vector(var10.length + 1)).addElement("../");

            for (int var7 = 0; var7 < var10.length; var7++) {
               if (!(var1 & !var10[var7].endsWith("/"))) {
                  var9.addElement(var10[var7].substring(var0.length()));
               }
            }

            var2 = new String[var9.size()];
            var9.copyInto(var2);
         }

         return var2;
      } catch (Exception var5) {
         var5.printStackTrace();
         throw new C_aq(191, 0, true);
      }
   }

   public static long m_a_aad3b1f3(String var0) {
      long var1 = 0L;
      FileConnection var3;
      var1 = (var3 = (FileConnection)Connector.open("file:///" + var0)).totalSize();
      var3.close();
      return var1;
   }

   public final void m_a_aad3b1ff(String var1) {
      this.f_x_a_dcebe21e = (FileConnection)Connector.open("file://" + var1);
   }

   public final OutputStream m_a_75f818f3() {
      if (!this.f_x_a_dcebe21e.exists()) {
         this.f_x_a_dcebe21e.create();
      } else if (this.f_x_a_dcebe21e.exists() & true) {
         this.f_x_a_dcebe21e.delete();
         this.f_x_a_dcebe21e.create();
      }

      return this.f_x_a_dcebe21e.openOutputStream();
   }

   public final InputStream m_a_b52a89f8() {
      return this.f_x_a_dcebe21e.openInputStream();
   }

   public final void m_a_9b75() {
      try {
         if (this.f_x_a_dcebe21e != null) {
            this.f_x_a_dcebe21e.close();
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   public final long m_a_9b69() {
      return this.f_x_a_dcebe21e != null ? this.f_x_a_dcebe21e.fileSize() : -1L;
   }
}
