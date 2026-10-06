package recovered;

/** 0.6 source correspondence (inferred): jimm.JimmException. Release class: ar. */

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import jimm.Jimm;

public final class C_ar extends Exception {
   boolean f_ar_a_5a;
   private boolean f_ar_b_5a;
   private boolean f_ar_c_5a;
   public int f_ar_a_49;

   public static String m_a_ec2edeab(int var0, int var1) {
      int var2;
      String var3;
      return (var2 = (var3 = C_bt.m_a_e96ea081("error_" + var0)).indexOf("EXT")) != -1 ? var3.substring(0, var2) + var1 + var3.substring(var2 + 3) : var3;
   }

   public C_ar(int var1, int var2) {
      super(m_a_ec2edeab(var1, var2));
      this.f_ar_a_49 = var1;
      this.f_ar_a_5a = true;
      this.f_ar_b_5a = true;
      this.f_ar_c_5a = false;
   }

   public C_ar(int var1, int var2, boolean var3) {
      super(m_a_ec2edeab(var1, var2));
      this.f_ar_a_49 = var1;
      this.f_ar_a_5a = false;
      this.f_ar_b_5a = var3;
      this.f_ar_c_5a = false;
   }

   public C_ar(int var1, int var2, boolean var3, boolean var4) {
      super(m_a_ec2edeab(var1, var2));
      this.f_ar_a_49 = var1;
      this.f_ar_a_5a = false;
      this.f_ar_b_5a = true;
      this.f_ar_c_5a = true;
   }

   public static synchronized Alert m_a_aef55300(C_ar var0) {
      Object var1 = null;
      if (var0.f_ar_a_5a) {
         if (var0.f_ar_c_5a) {
            C_ac.m_e_9b75();
         } else {
            C_ac.m_d_9b75();
         }

         C_w.m_d_9b75();
         C_cp.m_a_13462e(C_bi.m_a_1349e2(-1L));
         C_cp.m_a_48a013c6(null);
         Alert var3;
         (var3 = new Alert(C_bt.m_a_e96ea081("82"), var0.getMessage(), null, AlertType.ERROR)).setTimeout(-2);
         if (C_cp.m_a_9b79()) {
            if (C_bq.m_a_47921032(38).length() > 0) {
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var3, C_cp.f_cp_a_240da2);
            } else {
               C_cp.m_d_9b75();
            }
         } else {
            C_v.m_a_1ef468a(13, var3);
         }

         return var3;
      } else if (var0.f_ar_b_5a) {
         Alert var2;
         (var2 = new Alert(C_bt.m_a_e96ea081("37"), var0.getMessage(), null, AlertType.WARNING)).setTimeout(-2);
         C_cp.m_d_9b75();
         if (C_ac.m_b_9b79()) {
            C_w.m_a_ab8148d2(var2);
         } else {
            C_v.m_a_1ef468a(13, var2);
         }

         return var2;
      } else {
         return null;
      }
   }
}
