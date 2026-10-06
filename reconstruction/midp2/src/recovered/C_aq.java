package recovered;

public final class C_aq extends C_ba {
   private C_ad f_aq_a_2404ac;

   public C_aq(C_ad var1) {
      super(false, true);
      this.f_aq_a_2404ac = var1;
   }

   protected final void m_a_9b75() throws recovered.C_ar {
      byte[] var2 = C_cf.m_a_afa28ebe(this.f_aq_a_2404ac.f_bm_c_523beb0a);
      byte[] var3 = C_cf.m_a_44c4d6c8(this.f_aq_a_2404ac.f_ad_a_523beb0a, true);
      if (this.f_aq_a_2404ac.f_ad_a_49 == 4) {
         byte[] var7;
         if (this.f_aq_a_2404ac.f_ad_a_5a) {
            var7 = new byte[1 + var2.length + 1 + 4];
         } else {
            var7 = new byte[1 + var2.length + 1 + 2 + var3.length];
         }

         int var11 = 0;
         C_cf.m_a_e306985c(var7, 0, var2.length);
         System.arraycopy(var2, 0, var7, 1, var2.length);
         int var12 = 0 + 1 + var2.length;
         if (this.f_aq_a_2404ac.f_ad_a_5a) {
            C_cf.m_a_e306985c(var7, var12, 1);
            C_cf.m_b_e306985c(var7, ++var12, 0);
            var11 = var12 + 2;
            C_cf.m_b_e306985c(var7, var11, 0);
         } else {
            C_cf.m_a_e306985c(var7, var12, 0);
            C_cf.m_b_e306985c(var7, ++var12, var3.length);
            var11 = var12 + 2;
            System.arraycopy(var3, 0, var7, var11, var3.length);
         }

         C_bw var6 = new C_bw(19, 26, 26L, new byte[0], var7);
         C_ac.f_ac_a_240bf0.m_a_cb4b0023(var6);
      } else {
         if (this.f_aq_a_2404ac.f_ad_a_49 == 5) {
            byte[] var1 = new byte[1 + var2.length + 2 + var3.length + 2];
            int var4 = 0;
            C_cf.m_a_e306985c(var1, 0, var2.length);
            System.arraycopy(var2, 0, var1, 1, var2.length);
            var4 = 0 + 1 + var2.length;
            C_cf.m_b_e306985c(var1, var4, var3.length);
            var4 += 2;
            System.arraycopy(var3, 0, var1, var4, var3.length);
            var4 += var3.length;
            C_cf.m_b_e306985c(var1, var4, 0);
            C_bw var5 = new C_bw(19, 24, 24L, new byte[0], var1);
            C_ac.f_ac_a_240bf0.m_a_cb4b0023(var5);
         }
      }
   }

   protected final boolean m_a_cb4b0027(C_cd var1) {
      return false;
   }

   public final boolean m_a_9b79() {
      return true;
   }

   public final boolean m_b_9b79() {
      return false;
   }
}
