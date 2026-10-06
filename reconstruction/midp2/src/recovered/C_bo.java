package recovered;

/** 0.6 source correspondence (inferred): jimm.util.Selector. Release class: bo. */

import javax.microedition.lcdui.Graphics;

public final class C_bo extends C_bf implements C_ay {
   private static int f_bo_j_49;
   private static int f_bo_k_49;
   private static int f_bo_l_49;
   private static int f_bo_m_49;
   private static int f_bo_n_49;
   private static int f_bo_o_49;
   private static int f_bo_p_49;
   private static int f_bo_q_49;
   private static C_bo f_bo_a_2409c2;
   public static final int[] f_bo_a_b4e = new int[216];

   public C_bo(int var1, int var2) {
      super(null);
      f_bo_a_2409c2 = this;
      this.m_a_cb385cec(this);
      f_bo_q_49 = var1;
      var1 = C_bf.m_j_9b68() - C_bf.f_bf_a_49;
      this.c$13462e();
      switch (f_bo_q_49) {
         case 0:
            f_bo_m_49 = (f_bo_l_49 = C_ah.f_ah_a_12a00.m_b_9b68()) + 2;
            break;
         case 1:
            f_bo_m_49 = (f_bo_l_49 = C_x.m_a_247795f().m_b_9b68()) + (var1 > 176 ? 6 : 4);
            break;
         case 2:
            f_bo_m_49 = f_bo_l_49 = var1 / (var1 < 150 ? 8 : 12);
      }

      f_bo_j_49 = var1 / f_bo_m_49;
      int var10000 = p_bo_k_9b68();
      boolean var3 = false;
      f_bo_k_49 = (var10000 + f_bo_j_49 - 1) / f_bo_j_49;
      C_bo selected = f_bo_a_2409c2;
      f_bo_a_2409c2.m_i_13462e(var2 / f_bo_j_49);
      f_bo_o_49 = var2 % f_bo_j_49;
      selected.m_f_9b75();
      f_bo_n_49 = (f_bo_m_49 = f_bo_m_49 + (var1 - f_bo_m_49 * f_bo_j_49) / f_bo_j_49) - f_bo_l_49;
      p_bo_a_9b75();
   }

   protected final boolean m_a_134632(int var1) {
      int var2 = f_bo_o_49;
      if ((f_bo_o_49 = var1 / f_bo_m_49) < 0) {
         f_bo_o_49 = 0;
      }

      if (f_bo_o_49 >= f_bo_j_49) {
         f_bo_o_49 = f_bo_j_49 - 1;
      }

      if (var2 != f_bo_o_49) {
         p_bo_a_9b75();
         this.m_f_9b75();
      }

      return false;
   }

   protected final void m_a_c1306d97(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var5 = f_bo_j_49 * var2;
      switch (f_bo_q_49) {
         case 0:
            f_bo_p_49 = C_ah.f_ah_a_12a00.m_a_9b68();
            break;
         case 1:
            f_bo_p_49 = C_x.m_a_247795f().m_a_9b68();
            break;
         case 2:
            f_bo_p_49 = f_bo_a_b4e.length;
      }

      boolean var17 = var2 == this.m_g_9b68();
      int var8 = p_bo_k_9b68();
      int var12 = var3;

      for (int var15 = 0; var15 < f_bo_j_49 && var5 < var8; var5++) {
         var2 = var12 + f_bo_m_49;
         if (var17 && var15 == f_bo_o_49) {
            if (C_bq.f_bq_d_49 > 10) {
               C_bf.m_a_64ddb84c(var1, C_bf.m_a_255f288(C_bq.f_bq_c_49, -48), C_bq.f_bq_c_49, var12, var4, var12 + f_bo_m_49, var4 + f_bo_m_49, C_bq.f_bq_d_49);
            } else {
               var1.setStrokeStyle(1);
            }

            var1.setColor(C_bf.m_a_255f288(C_bq.f_bq_c_49, -48));
            var1.drawRect(var12, var4, f_bo_m_49 - 1, var6 - var4 - 1);
         }

         if (var5 < f_bo_p_49) {
            switch (f_bo_q_49) {
               case 0:
                  C_f var10000 = C_ah.f_ah_a_12a00.m_a_485a59b9(var5);
                  int var10002 = var12 + f_bo_m_49 / 2;
                  int var11 = (var4 + var6) / 2;
                  int var10 = var10002;
                  C_f var13 = var10000;
                  var10000.m_a_11c44857(var1, var10 - var13.f_f_c_49 / 2, var11 - var13.f_f_d_49 / 2);
                  break;
               case 1:
                  int var9 = f_bo_n_49 / 2 + f_bo_n_49 % 2;
                  C_x.m_a_485a59b9(var5 == 0 ? 36 : var5 - 1).m_a_11c44857(var1, var12 + var9, var4 + var9);
                  break;
               case 2:
                  var1.setColor(f_bo_a_b4e[var5]);
                  var1.fillRect(var12 + 2, var4 + 2, f_bo_m_49 - 4, f_bo_m_49 - 4);
            }
         }

         var12 = var2;
         var15++;
      }
   }

   private static void p_bo_a_9b75() {
      int var0 = f_bo_a_2409c2.m_g_9b68() * f_bo_j_49 + f_bo_o_49;
      switch (f_bo_q_49) {
         case 0:
            if (var0 >= C_ah.f_ah_b_6dccaaa5.length) {
               return;
            }

            C_ah.f_ah_a_523beb0a = C_ah.f_ah_a_6dccaaa5[var0];
            f_bo_a_2409c2.m_c_aad3b1ff(C_ah.f_ah_b_6dccaaa5[var0]);
            return;
         case 1:
            if (var0 > C_x.m_a_9b68() + 1) {
               return;
            }

            var0 = var0 == 0 ? 36 : var0 - 1;
            f_bo_a_2409c2.m_c_aad3b1ff(C_x.m_a_47921032(var0));
            return;
         case 2:
            String var1 = "00000" + Integer.toHexString(f_bo_a_b4e[var0]).toUpperCase();
            f_bo_a_2409c2.m_c_aad3b1ff(var1.substring(var1.length() - 6));
      }
   }

   public final int m_a_134621(int var1) {
      return f_bo_m_49;
   }

   protected final int m_a_9b68() {
      return f_bo_k_49;
   }

   private static int p_bo_k_9b68() {
      switch (f_bo_q_49) {
         case 0:
            return C_ah.f_ah_a_b4e.length;
         case 1:
            return C_x.m_a_9b68() + 1;
         case 2:
            return f_bo_a_b4e.length;
         default:
            return -1;
      }
   }

   public static int m_b_9b68() {
      return f_bo_a_2409c2.m_g_9b68() * f_bo_j_49 + f_bo_o_49;
   }

   public static int m_d_134621(int var0) {
      for (int var1 = 0; var1 < f_bo_a_b4e.length; var1++) {
         if (var0 == f_bo_a_b4e[var1]) {
            return var1;
         }
      }

      return 0;
   }

   protected final void m_a_d826493f(int var1, C_ca var2) {
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
      int var5 = f_bo_o_49;
      int var7 = this.m_g_9b68();
      Object var4 = null;
      int var9 = f_bo_k_49;
      switch (C_bf.m_b_134621(var2)) {
         case 2:
            if (f_bo_o_49 != 0) {
               f_bo_o_49--;
            } else if (var7 != 0) {
               f_bo_o_49 = f_bo_j_49 - 1;
               var7--;
            } else {
               f_bo_o_49 = (p_bo_k_9b68() - 1) % f_bo_j_49;
               var7 = var9 - 1;
            }
            break;
         case 5:
            if (f_bo_o_49 < f_bo_j_49 - 1) {
               f_bo_o_49++;
            } else if (var7 <= var9) {
               f_bo_o_49 = 0;
               var7++;
            }

            if (f_bo_o_49 + var7 * f_bo_j_49 > p_bo_k_9b68() - 1) {
               f_bo_o_49 = 0;
               var7 = 0;
            }
      }

      this.m_i_13462e(var7);
      var2 = f_bo_o_49 + this.m_g_9b68() * f_bo_j_49;
      var3 = p_bo_k_9b68();
      if (var2 >= var3) {
         f_bo_o_49 = (var3 - 1) % f_bo_j_49;
      }

      if (var5 != f_bo_o_49) {
         this.m_f_9b75();
         p_bo_a_9b75();
      }
   }

   public final void m_a_cb3dd160(C_bf var1) {
      p_bo_a_9b75();
   }

   public final void m_b_cb3dd160(C_bf var1) {
   }

   static {
      int var0 = 0;

      for (byte var1 = 0; var1 < 256; var1 += 51) {
         for (byte var2 = 0; var2 < 256; var2 += 51) {
            for (byte var3 = 0; var3 < 256; var3 += 51) {
               f_bo_a_b4e[var0] = (var1 << 16) + (var2 << 8) + var3;
               var0++;
            }
         }
      }
   }
}
