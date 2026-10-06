package recovered;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import jimm.Jimm;

public final class C_ah implements C_ax, CommandListener {
   private static C_ah f_ah_a_240528;
   public static C_h f_ah_a_12a1f;
   private static final Vector f_ah_a_48a69a2c = new Vector();
   private static boolean f_ah_a_5a;
   public static int[] f_ah_a_b4e;
   private static int[] f_ah_b_b4e;
   public static String[] f_ah_a_6dccaaa5;
   public static String[] f_ah_b_6dccaaa5;
   private static String[] f_ah_c_6dccaaa5;
   private static boolean[] f_ah_a_b5f;
   public static String f_ah_a_523beb0a;
   private static C_bm f_ah_a_240984;
   private static int f_ah_a_49;
   private static Object f_ah_a_5f790d9c;
   private static TextBox f_ah_a_fd805d5b;

   public C_ah() {
      f_ah_a_5a = false;
      f_ah_a_240528 = this;
      Vector var1 = new Vector();
      Vector var2 = new Vector();
      InputStream var3;
      if ((var3 = this.getClass().getResourceAsStream("/smiles.txt")) != null) {
         DataInputStream var4 = new DataInputStream(var3);

         try {
            StringBuffer var5 = new StringBuffer();
            boolean var6 = false;
            boolean var7 = false;
            p_ah_a_693a5ae2(var5, var4);
            int var14 = Integer.parseInt(var5.toString());

            label62:
            while (true) {
               p_ah_a_693a5ae2(var5, var4);
               Integer var8 = Integer.valueOf(var5.toString());
               p_ah_a_693a5ae2(var5, var4);
               String var9 = var5.toString();
               int var10 = 0;

               while (true) {
                  try {
                     var7 = p_ah_a_693a5ae2(var5, var4);
                  } catch (EOFException var12) {
                     var6 = true;
                  }

                  String var11;
                  if ((var11 = new String(var5).trim()).length() != 0) {
                     p_ah_a_7458586c(var1, var11, var8);
                  }

                  if (var10 == 0) {
                     var2.addElement(new Object[]{var8, var11, var9});
                  }

                  if (var7 || var6) {
                     if (var6) {
                        var3.close();
                        (f_ah_a_12a1f = new C_bw()).m_a_44bd8e9f("/smiles", var14, var14);
                        if (f_ah_a_12a1f.m_a_9b68() == 0) {
                           (f_ah_a_12a1f = new C_h()).m_a_44bd8e9f("/smiles.png", var14, var14);
                        }
                        break label62;
                     }
                     break;
                  }

                  var10++;
               }
            }
         } catch (Exception var13) {
            return;
         }

         int var15;
         f_ah_a_b4e = new int[var15 = var2.size()];
         f_ah_a_6dccaaa5 = new String[var15];
         f_ah_b_6dccaaa5 = new String[var15];

         for (int var17 = 0; var17 < var15; var17++) {
            Object[] var19 = (Object[])var2.elementAt(var17);
            f_ah_a_b4e[var17] = (Integer)var19[0];
            f_ah_a_6dccaaa5[var17] = (String)var19[1];
            f_ah_b_6dccaaa5[var17] = (String)var19[2];
         }

         f_ah_c_6dccaaa5 = new String[var15 = var1.size()];
         f_ah_b_b4e = new int[var15];
         f_ah_a_b5f = new boolean[var15];

         for (int var18 = 0; var18 < var15; var18++) {
            Object[] var20 = (Object[])var1.elementAt(var18);
            f_ah_c_6dccaaa5[var18] = (String)var20[0];
            f_ah_b_b4e[var18] = (Integer)var20[1];
         }

         f_ah_a_5a = true;
      }
   }

   private static void p_ah_a_7458586c(Vector var0, String var1, Integer var2) {
      Object[] var6 = new Object[]{var1, var2};
      int var5 = var1.length();
      int var3 = var0.size();

      for (int var4 = 0; var4 < var3; var4++) {
         if (((String)((Object[])var0.elementAt(var4))[0]).length() <= var5) {
            var0.insertElementAt(var6, var4);
            return;
         }
      }

      var0.addElement(var6);
   }

   private static boolean p_ah_a_693a5ae2(StringBuffer var0, DataInputStream var1) {
      var0.setLength(0);

      byte var2;
      while ((var2 = var1.readByte()) != 44 && var2 != 10 && var2 != 9) {
         if (var2 >= 32) {
            var0.append((char)var2);
         }
      }

      return var2 == 10;
   }

   public static void m_a_e9f435a0(C_s var0, String var1, int var2, int var3, int var4) {
      if (f_ah_a_5a && C_bp.m_a_134632(141)) {
         for (int var5 = f_ah_a_b5f.length - 1; var5 >= 0; var5--) {
            f_ah_a_b5f[var5] = true;
         }

         int var14 = 0;

         while (true) {
            f_ah_a_48a69a2c.removeAllElements();
            int var6 = f_ah_c_6dccaaa5.length;

            for (int var7 = 0; var7 < var6; var7++) {
               String var10001 = f_ah_c_6dccaaa5[var7];
               int var10 = f_ah_b_b4e[var7];
               String var9 = var10001;
               if (f_ah_a_b5f[var7]) {
                  int var13 = var9.length();
                  int var8;
                  if ((var8 = var1.indexOf(var9, var14)) == -1) {
                     f_ah_a_b5f[var7] = false;
                  } else {
                     f_ah_a_48a69a2c.addElement(new int[]{var8, var13, var10});
                  }
               }
            }

            if (f_ah_a_48a69a2c.isEmpty()) {
               if ((var6 = var1.length()) != var14) {
                  var0.m_a_68a79fe2(var1.substring(var14, var6), var3, var2, var4);
               }

               return;
            }

            int var17 = f_ah_a_48a69a2c.size();
            int var15 = 100000;
            Object var18 = null;
            int[] var20 = null;

            for (int var21 = 0; var21 < var17; var21++) {
               if (((Object[])(var18 = (int[])f_ah_a_48a69a2c.elementAt(var21)))[0] < var15) {
                  var15 = (int)((Object[])var18)[0];
                  var20 = (int[])var18;
               }
            }

            if (var14 != var15) {
               var0.m_a_68a79fe2(var1.substring(var14, var15), var3, var2, var4);
            }

            var0.m_a_3371d4d1(f_ah_a_12a1f.m_a_485a59b9(var20[2]), var1.substring(var15, var15 + var20[1]), var4);
            var14 = var15 + var20[1];
         }
      } else {
         var0.m_a_68a79fe2(var1, var3, var2, var4);
      }
   }

   public static void m_a_23a88fec(TextBox var0, Object var1) {
      f_ah_a_5f790d9c = var1;
      f_ah_a_49 = var0.getCaretPosition();
      f_ah_a_fd805d5b = var0;
      C_bf.m_a_9c60de72(f_ah_a_240984 = new C_bm(0, 0), false);
      f_ah_a_240984.d$1385ff();
      f_ah_a_240984.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      f_ah_a_240984.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
      f_ah_a_240984.m_a_6f63a2af(f_ah_a_240528);
      f_ah_a_240984.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == C_bf.f_bf_i_1570d10e) {
         p_ah_a_9b75();
      } else {
         if (var1 == C_bf.f_bf_c_1570d10e) {
            C_bf.m_b_5d527811(f_ah_a_5f790d9c);
            f_ah_a_240984 = null;
            C_bt.m_a_1385ff(true);
         }
      }
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
      p_ah_a_9b75();
   }

   private static void p_ah_a_9b75() {
      f_ah_a_49 = f_ah_a_fd805d5b.getString().length();
      f_ah_a_fd805d5b.insert(" " + f_ah_a_523beb0a + " ", f_ah_a_49);
      C_bf.m_b_5d527811(f_ah_a_5f790d9c);
      f_ah_a_240984 = null;
      System.gc();
      C_bt.m_a_1385ff(true);
   }
}
