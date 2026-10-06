package recovered;

/** 0.6 source correspondence (inferred): jimm.util.StringConvertor. Release class: aj. */

import java.io.InputStream;
import java.util.Vector;

public final class C_aj {
   private final String f_aj_a_523beb0a;
   private final String[] f_aj_a_6dccaaa5;
   private final String[] f_aj_b_6dccaaa5;
   private int f_aj_a_49;
   private static C_aj[] f_aj_a_5266041;

   private static String p_aj_c_e96ea081(String var0) {
      String var1 = "";

      try {
         InputStream var4;
         byte[] var2 = new byte[(var4 = "".getClass().getResourceAsStream(var0)).available()];
         var4.read(var2);
         var1 = C_cf.m_a_20e7da8(var2, 0, var2.length, C_cf.m_a_e3069860(var2, 0, var2.length));
         var4.close();
      } catch (Exception var3) {
      }

      return var1;
   }

   private static String p_aj_d_e96ea081(String var0) {
      if (var0.indexOf(13) < 0) {
         return var0;
      } else {
         StringBuffer var1 = new StringBuffer();
         int var2 = var0.length();

         for (int var3 = 0; var3 < var2; var3++) {
            char var4;
            if ((var4 = var0.charAt(var3)) != 0 && var4 != '\r') {
               var1.append(var4);
            }
         }

         return var1.toString();
      }
   }

   private static String p_aj_e_e96ea081(String var0) {
      char[] var1 = var0.toCharArray();

      for (int var2 = var0.length() - 1; var2 >= 0; var2--) {
         char var10002 = var1[var2];
         char var3 = '\u0000';
         var1[var2] = (var3 = Character.toUpperCase(var10002)) >= 'a' && var3 <= 'z'
               || var3 >= 223 && var3 <= 246
               || var3 >= 248 && var3 <= 255
               || var3 >= 1072 && var3 <= 1119
            ? (var3 > 'z' && (var3 < 1072 || var3 > 1103) ? (var3 > 1071 ? (char)(var3 - 'P') : (char)(var3 - ' ')) : (char)(var3 - ' '))
            : var3;
      }

      String var4;
      return (var4 = new String(var1)).equals(var0) ? var0 : var4;
   }

   public static char m_a_132f95(char var0) {
      if ((var0 = Character.toLowerCase(var0)) >= 'A' && var0 <= 'Z'
         || var0 >= 192 && var0 <= 214
         || var0 >= 216 && var0 <= 222
         || var0 >= 1024 && var0 <= 1071) {
         if (var0 > 'Z' && (var0 < 1040 || var0 > 1071)) {
            return var0 < 1040 ? (char)(var0 + 'P') : (char)(var0 + ' ');
         } else {
            return (char)(var0 + ' ');
         }
      } else {
         return var0;
      }
   }

   private String p_aj_f_e96ea081(String var1) {
      char[] var3 = var1.toCharArray();

      for (int var4 = var1.length() - 1; var4 >= 0; var4--) {
         var3[var4] = m_a_132f95(var3[var4]);
      }

      String var6;
      String var2 = (var6 = new String(var3)).equals(var1) ? var1 : var6;

      for (int var5 = this.f_aj_a_6dccaaa5.length - 1; var5 >= 0; var5--) {
         if (this.f_aj_a_6dccaaa5[var5].equals(var1)) {
            return this.f_aj_b_6dccaaa5[var5];
         }

         if (this.f_aj_a_6dccaaa5[var5].equals(var2)) {
            return p_aj_e_e96ea081(this.f_aj_b_6dccaaa5[var5]);
         }
      }

      return null;
   }

   private static String[] p_aj_a_967e6e30(Vector var0) {
      String[] var1 = new String[var0.size()];
      var0.copyInto(var1);
      return var1;
   }

   private C_aj(String var1, Vector var2, Vector var3) {
      this.f_aj_a_523beb0a = var1;
      this.f_aj_a_6dccaaa5 = p_aj_a_967e6e30(var2);
      this.f_aj_b_6dccaaa5 = p_aj_a_967e6e30(var3);
      this.f_aj_a_49 = 0;

      for (int var4 = 0; var4 < this.f_aj_a_6dccaaa5.length; var4++) {
         this.f_aj_a_49 = Math.max(this.f_aj_a_49, this.f_aj_a_6dccaaa5[var4].length());
      }
   }

   private static void p_aj_a_efa168ab(String var0, Vector var1) {
      byte var2 = 0;
      int var3 = 0;
      String var4 = null;
      Vector var5 = new Vector();
      Vector var6 = new Vector();

      for (int var7 = 0; var7 < var0.length(); var7++) {
         char var8 = var0.charAt(var7);
         switch (var2) {
            case 0:
               if (var8 == '[') {
                  if (var4 != null) {
                     var1.addElement(new C_aj(var4, var5, var6));
                     var5.removeAllElements();
                     var6.removeAllElements();
                     var4 = null;
                  }

                  var3 = var7 + 1;
                  var2 = 1;
               } else if (var8 == '#' || var8 == ';') {
                  var2 = 6;
               } else if (var8 != '\n' && var8 != '\r' && var8 != ' ' && var8 != '\t') {
                  var3 = var7;
                  var2 = 2;
               }
               break;
            case 1:
               if (var8 == ']') {
                  if ((var4 = var0.substring(var3, var7).trim()).length() == 0) {
                     var4 = null;
                  }

                  var3 = var7 + 1;
                  var2 = 0;
               }
               break;
            case 2:
               if (var8 == '=') {
                  var5.addElement(var0.substring(var3, var7).trim());
                  var3 = var7 + 1;
                  var2 = 3;
               }
               break;
            case 3:
               if (var8 == '\n' || var8 == '#' || var8 == ';') {
                  var6.addElement(var0.substring(var3, var7).trim());
                  var3 = var7;
                  var2 = 0;
               }
            case 4:
            case 5:
            default:
               break;
            case 6:
               if (var8 == '\n') {
                  var2 = 0;
               }
         }
      }

      if (var4 != null) {
         var1.addElement(new C_aj(var4, var5, var6));
      }
   }

   private static String p_aj_a_8ea82db7(String var0, String var1) {
      for (int var2 = 0; var2 < f_aj_a_5266041.length; var2++) {
         if (var0.equals(f_aj_a_5266041[var2].f_aj_a_523beb0a)) {
            C_aj var7 = f_aj_a_5266041[var2];
            StringBuffer var8 = new StringBuffer();
            int var3 = 0;

            while (var3 < var1.length()) {
               String var4 = "";

               for (int var5 = Math.min(var3 + var7.f_aj_a_49, var1.length()); var5 > var3; var5--) {
                  var4 = var1.substring(var3, var5);
                  String var6;
                  if ((var6 = var7.p_aj_f_e96ea081(var4)) != null) {
                     var8.append(var6);
                     break;
                  }

                  if (var4.length() == 1) {
                     var8.append(var4);
                     break;
                  }
               }

               var3 += var4.length();
            }

            return var8.toString();
         }
      }

      return var1;
   }

   public static String m_a_e96ea081(String var0) {
      return p_aj_a_8ea82db7("_detransliterate".substring(1), var0);
   }

   public static String m_b_e96ea081(String var0) {
      return p_aj_a_8ea82db7("_transliterate".substring(1), var0);
   }

   static {
      Vector var0 = new Vector();

      try {
         String var1 = p_aj_c_e96ea081("/replaces.txt");
         p_aj_a_efa168ab(p_aj_d_e96ea081(var1.trim() + '\n'), var0);
      } catch (Exception var2) {
      }

      f_aj_a_5266041 = new C_aj[var0.size()];
      var0.copyInto(f_aj_a_5266041);
   }
}
