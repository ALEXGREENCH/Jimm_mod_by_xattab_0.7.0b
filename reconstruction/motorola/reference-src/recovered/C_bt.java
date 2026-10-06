package recovered;

import jimm.Jimm;

public final class C_bt {
   private static boolean f_bt_a_5a = true;
   private static int f_bt_a_49 = C_bp.m_a_134621(74) * 1000;

   public static void m_a_1385ff(boolean var0) {
      if (!C_bp.m_a_134632(140)) {
         if (var0) {
            Jimm.f_jimm_Jimm_a_4a58c677.flashBacklight(Integer.MAX_VALUE);
            return;
         }

         Jimm.f_jimm_Jimm_a_4a58c677.flashBacklight(f_bt_a_49);
      }
   }

   public static void m_a_9b75() {
      if (f_bt_a_5a) {
         m_b_9b75();
      } else {
         m_c_9b75();
      }
   }

   public static void m_b_9b75() {
      Jimm.f_jimm_Jimm_a_4a58c677.flashBacklight(1);
      f_bt_a_5a = false;
   }

   public static void m_c_9b75() {
      Jimm.f_jimm_Jimm_a_4a58c677.flashBacklight(Integer.MAX_VALUE);
      f_bt_a_5a = true;
   }
}
