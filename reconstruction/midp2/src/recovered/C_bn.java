package recovered;

import java.util.TimerTask;

final class C_bn extends TimerTask {
   private final C_aw f_bn_a_2406f9;

   C_bn(C_aw var1) {
      this.f_bn_a_2406f9 = var1;
   }

   public final void run() {
      C_aw.m_a_cb377421(this.f_bn_a_2406f9);
      if (C_aw.m_a_cb377432(this.f_bn_a_2406f9)) {
         C_aw.m_a_9bb7c46a(this.f_bn_a_2406f9, !C_aw.m_b_cb377432(this.f_bn_a_2406f9));
      } else {
         C_aw.m_b_9bb7c46a(this.f_bn_a_2406f9, !C_aw.m_c_cb377432(this.f_bn_a_2406f9));
      }

      if (C_aw.m_b_cb377421(this.f_bn_a_2406f9) > C_aw.m_c_cb377421(this.f_bn_a_2406f9)) {
         C_aw.m_a_36162b95(this.f_bn_a_2406f9).cancel();
         C_aw.m_a_9bb7c46a(this.f_bn_a_2406f9, false);
         C_aw.m_b_9bb7c46a(this.f_bn_a_2406f9, false);
         if (!C_aw.m_a_cb377432(this.f_bn_a_2406f9)) {
            C_aw.m_a_cb37742e(this.f_bn_a_2406f9);
         }
      }

      C_w.m_g_9b75();
   }
}
