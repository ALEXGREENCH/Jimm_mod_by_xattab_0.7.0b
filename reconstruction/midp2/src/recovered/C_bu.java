package recovered;

/** 0.6 source correspondence (inferred): DrawControls.LightControl. Release class: bu. */

import com.nokia.mid.ui.DeviceControl;
import java.util.TimerTask;
import jimm.Jimm;

public final class C_bu {
   private static boolean f_bu_a_5a = true;
   private static int f_bu_a_49 = C_bq.m_a_134621(74) * 1000;
   private static TimerTask f_bu_a_d50713bd;

   public static void m_a_1385ff(boolean var0) {
      if (Jimm.f_jimm_Jimm_e_5a && C_bq.m_a_134632(140)) {
         if (var0) {
            p_bu_d_9b75();
            return;
         }

         m_a_9b75();
      }
   }

   private static void p_bu_d_9b75() {
      p_bu_e_9b75();
      if (f_bu_a_d50713bd != null) {
         f_bu_a_d50713bd.cancel();
         f_bu_a_d50713bd = null;
      }
   }

   public static void m_a_9b75() {
      if (Jimm.f_jimm_Jimm_e_5a && C_bq.m_a_134632(140)) {
         p_bu_d_9b75();
         f_bu_a_d50713bd = new C_ak();
         Jimm.m_a_94e56161().schedule(f_bu_a_d50713bd, f_bu_a_49);
      }
   }

   public static void m_b_9b75() {
      if (f_bu_a_5a) {
         m_c_9b75();
      } else {
         p_bu_e_9b75();
      }
   }

   public static void m_c_9b75() {
      DeviceControl.setLights(0, 0);
      f_bu_a_5a = false;
   }

   private static void p_bu_e_9b75() {
      DeviceControl.setLights(0, C_bq.m_a_134621(101));
      f_bu_a_5a = true;
   }
}
