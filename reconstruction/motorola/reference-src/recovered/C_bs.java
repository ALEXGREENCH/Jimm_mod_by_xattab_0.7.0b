package recovered;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Hashtable;

public final class C_bs {
   public static String[] f_bs_a_6dccaaa5;
   private static String f_bs_a_523beb0a;
   private static Hashtable f_bs_a_d18d4967;

   public static void m_a_aad3b1ff(String var0) {
      if (!f_bs_a_523beb0a.equals(var0)) {
         for (int var1 = 0; var1 < f_bs_a_6dccaaa5.length; var1++) {
            if (f_bs_a_6dccaaa5[var1].equals(var0)) {
               f_bs_a_523beb0a = new String(var0);
               p_bs_a_9b75();
               return;
            }
         }
      }
   }

   private static void p_bs_a_9b75() {
      try {
         InputStream var0 = (f_bs_a_d18d4967 = new Hashtable()).getClass().getResourceAsStream("/" + f_bs_a_523beb0a + ".lng");
         DataInputStream var1;
         short var2 = (var1 = new DataInputStream(var0)).readShort();

         for (int var3 = 0; var3 < var2; var3++) {
            f_bs_a_d18d4967.put(var1.readUTF(), var1.readUTF());
         }

         var0.close();
      } catch (Exception var4) {
      }
   }

   public static synchronized String m_a_e96ea081(String var0) {
      if (f_bs_a_d18d4967 == null) {
         p_bs_a_9b75();
      }

      if (var0 == null) {
         return null;
      } else {
         String var1;
         return (var1 = (String)f_bs_a_d18d4967.get(var0)) != null ? var1 : var0;
      }
   }

   public static synchronized String a$7a1ba7c4(String var0) {
      var0 = m_a_e96ea081(var0);
      return var0 + "...";
   }

   static {
      try {
         InputStream var0 = new Object().getClass().getResourceAsStream("/langlist.lng");
         DataInputStream var1;
         short var2;
         f_bs_a_6dccaaa5 = new String[var2 = (var1 = new DataInputStream(var0)).readShort()];

         for (int var3 = 0; var3 < var2; var3++) {
            f_bs_a_6dccaaa5[var3] = var1.readUTF();
         }

         var0.close();
      } catch (Exception var4) {
      }

      f_bs_a_523beb0a = f_bs_a_6dccaaa5[0];
      f_bs_a_d18d4967 = null;
   }
}
