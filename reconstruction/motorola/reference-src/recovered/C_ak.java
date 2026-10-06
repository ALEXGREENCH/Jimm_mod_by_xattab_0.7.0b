package recovered;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import jimm.Jimm;

public final class C_ak implements C_ax, CommandListener, C_k {
   private static Command f_ak_a_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 1, 0);
   private static Command f_ak_b_1570d10e = new Command(C_bs.m_a_e96ea081("6"), 4, 1);
   private static Command f_ak_c_1570d10e = new Command(C_bs.m_a_e96ea081("c"), 4, 1);
   private static boolean f_ak_a_5a;
   private static C_ak f_ak_a_240585;
   private static C_c f_ak_a_12984;
   private static C_t f_ak_a_12b93;
   private static C_h f_ak_a_12a1f = C_h.m_a_44af1588("/fs.png");
   private static String[] f_ak_a_6dccaaa5;
   private static String f_ak_a_523beb0a;

   public C_ak() {
      f_ak_a_240585 = this;
      (f_ak_a_12984 = new C_c()).f_c_a_12a7c = this;
      f_ak_a_12984.m_a_cb37e88d(this);
      f_ak_a_12984.m_a_48a0fc84(f_ak_a_12a1f);
      f_ak_a_12984.m_b_13462e(f_ak_a_12a1f.m_b_9b68() < 16 ? 8 : 0);
      f_ak_a_12984.m_a_13462e(-f_ak_a_12984.m_h_9b68() / 2);
      f_ak_a_12984.m_a_48a013c6(f_ak_a_12a1f.m_a_485a59b9(0));
      C_bf.m_a_9c60de72(f_ak_a_12984, false);
      f_ak_a_12984.d$1385ff();
      f_ak_a_12984.m_a_1385ff(false);
      f_ak_a_12984.m_a_48817c60(f_ak_a_1570d10e, C_bd.f_bd_f_49);
      f_ak_a_12984.m_a_6f63a2af(this);
   }

   private static void p_ak_b_9b75() {
      f_ak_a_12984.m_i_9b75();
      f_ak_a_6dccaaa5 = new String[0];
      f_ak_a_12984.m_a_9b75();
      f_ak_a_12984.m_j_9b75();
   }

   private static int p_ak_a_aad3b1f2(String var0) {
      if (var0.equals("../")) {
         return 0;
      } else {
         return var0.endsWith("/") ? 10 : 20;
      }
   }

   public final int m_a_496bbe28(C_e var1, C_e var2) {
      boolean var4 = false;
      String var5 = (String)var1.f_e_a_5f790d9c;
      String var6 = (String)var2.f_e_a_5f790d9c;
      int var7 = p_ak_a_aad3b1f2(var5);
      int var3 = p_ak_a_aad3b1f2(var6);
      if (var7 == var3) {
         return var5.toLowerCase().compareTo(var6.toLowerCase());
      } else {
         return var7 < var3 ? -1 : 1;
      }
   }

   private static void p_ak_c_9b75() {
      f_ak_a_12984.m_i_9b75();
      f_ak_a_12984.m_a_9b75();

      for (int var0 = 0; var0 < f_ak_a_6dccaaa5.length; var0++) {
         f_ak_a_12984.m_a_c92c35af(null, f_ak_a_6dccaaa5[var0]);
      }

      f_ak_a_12984.m_b_489f9f67(null);
      f_ak_a_12984.m_j_9b75();
      p_ak_a_aad3b1ff((String)f_ak_a_12984.m_a_2477921().f_e_a_5f790d9c);
   }

   public static void m_a_1385ff(boolean var0) {
      f_ak_a_5a = var0;
   }

   public static void m_a_48a670f8(C_t var0) {
      f_ak_a_12b93 = var0;
   }

   private static void p_ak_a_aad3b1ff(String var0) {
      f_ak_a_12984.m_a_8eb9d703(f_ak_c_1570d10e);
      f_ak_a_12984.m_a_8eb9d703(f_ak_b_1570d10e);
      f_ak_a_12984.m_a_8eb9d703(C_bf.f_bf_h_1570d10e);
      if (var0.equals("../")) {
         int var4 = f_ak_a_523beb0a.lastIndexOf(47, f_ak_a_523beb0a.length() - 2);
         f_ak_a_12984.m_a_48817c60(f_ak_c_1570d10e, C_bd.f_bd_e_49);
         f_ak_a_12984.m_c_aad3b1ff(var4 != -1 ? f_ak_a_523beb0a.substring(0, var4 + 1) : "/");
      } else if (var0.endsWith("/") & f_ak_a_523beb0a.equals("/")) {
         try {
            f_ak_a_12984.m_c_aad3b1ff(C_bs.m_a_e96ea081("b6") + ": " + (C_at.m_a_aad3b1f3(var0) >> 10) + "Kb");
         } catch (Exception var3) {
         }

         f_ak_a_12984.m_a_48817c60(f_ak_c_1570d10e, C_bd.f_bd_e_49);
      } else if (var0.endsWith("/") & !f_ak_a_523beb0a.equals("/")) {
         if (f_ak_a_5a) {
            f_ak_a_12984.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_e_49);
            f_ak_a_12984.m_a_48817c60(f_ak_b_1570d10e, C_bd.f_bd_g_49);
            f_ak_a_12984.m_a_48817c60(f_ak_c_1570d10e, C_bd.f_bd_g_49);
         } else {
            f_ak_a_12984.m_a_48817c60(f_ak_c_1570d10e, C_bd.f_bd_e_49);
         }

         f_ak_a_12984.m_c_aad3b1ff(f_ak_a_523beb0a + var0);
      } else {
         f_ak_a_12984.m_a_48817c60(f_ak_b_1570d10e, C_bd.f_bd_e_49);
         int var1 = var0.lastIndexOf(46);
         StringBuffer var2 = new StringBuffer();
         if (var1 != -1) {
            var2 = var2.append(var0.substring(var1 + 1).toUpperCase()).append(" file");
         }

         f_ak_a_12984.m_c_aad3b1ff(var2.toString());
      }
   }

   public final void m_a_e3fdbb00(C_e var1, C_bx var2) {
      String var3 = (String)var1.f_e_a_5f790d9c;
      var2.f_bx_a_523beb0a = var3;
      var2.f_bx_a_129e1 = f_ak_a_12a1f.m_a_485a59b9(var3.endsWith("/") ? 0 : 1);
      var2.f_bx_b_49 = f_ak_a_12984.m_d_9b68();
      var2.f_bx_a_49 = C_bp.m_a_134621(112);
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
      if (var1 == f_ak_a_12984) {
         p_ak_a_aad3b1ff((String)f_ak_a_12984.m_a_2477921().f_e_a_5f790d9c);
      }
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
   }

   public static void m_a_9b75() {
      if (f_ak_a_240585 == null) {
         new C_ak();
      }

      p_ak_b_9b75();
      f_ak_a_523beb0a = "/";
      f_ak_a_6dccaaa5 = C_at.m_a_855a3144("/", f_ak_a_5a);
      p_ak_c_9b75();
      f_ak_a_12984.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (C_bf.m_a_cb3ce8a6(f_ak_a_12984)) {
         if (var1 == f_ak_c_1570d10e) {
            String var6;
            if ((var6 = (String)f_ak_a_12984.m_a_2477921().f_e_a_5f790d9c).equals("../")) {
               int var7;
               f_ak_a_523beb0a = (var7 = f_ak_a_523beb0a.lastIndexOf(47, f_ak_a_523beb0a.length() - 2)) != -1 ? f_ak_a_523beb0a.substring(0, var7 + 1) : "/";
               p_ak_b_9b75();

               try {
                  f_ak_a_6dccaaa5 = C_at.m_a_855a3144(f_ak_a_523beb0a, f_ak_a_5a);
               } catch (C_aq var3) {
                  C_aq.m_a_481c933f(var3);
               }

               p_ak_c_9b75();
               return;
            }

            if (var6.endsWith("/")) {
               f_ak_a_523beb0a = f_ak_a_523beb0a + var6;
               p_ak_b_9b75();

               try {
                  f_ak_a_6dccaaa5 = C_at.m_a_855a3144(f_ak_a_523beb0a, f_ak_a_5a);
               } catch (C_aq var4) {
                  C_aq.m_a_481c933f(var4);
               }

               p_ak_c_9b75();
               return;
            }

            f_ak_a_12b93.m_a_aad3b1ff(f_ak_a_523beb0a + var6);
            return;
         }

         if (var1 == f_ak_b_1570d10e) {
            String var5;
            if ((var5 = (String)f_ak_a_12984.m_a_2477921().f_e_a_5f790d9c).endsWith("/")) {
               f_ak_a_12b93.m_b_aad3b1ff(f_ak_a_523beb0a + var5);
               return;
            }

            f_ak_a_12b93.m_a_aad3b1ff(f_ak_a_523beb0a + var5);
            return;
         }

         if (var1 == f_ak_a_1570d10e) {
            if (f_ak_a_12b93.m_a_46a7a37a() != null || f_ak_a_5a) {
               C_v.m_a_9b75();
               return;
            }

            C_bp.f_bp_a_24088c.m_a_9b75();
         }
      }
   }
}
