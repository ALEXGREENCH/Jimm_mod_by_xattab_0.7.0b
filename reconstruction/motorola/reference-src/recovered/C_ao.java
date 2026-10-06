package recovered;

public final class C_ao extends C_az {
   private C_ad f_ao_a_2404ac;

   public C_ao(C_ad var1) {
      super(false, true);
      this.f_ao_a_2404ac = var1;
   }

   protected final void m_a_9b75() {
      byte[] var2 = C_cf.m_a_afa28ebe(this.f_ao_a_2404ac.f_bl_c_523beb0a);
      byte[] var3 = C_cf.m_a_44c4d6c8(this.f_ao_a_2404ac.f_ad_a_523beb0a, true);
      if (this.f_ao_a_2404ac.f_ad_a_49 == 4) {
         byte[] var7;
         if (this.f_ao_a_2404ac.f_ad_a_5a) {
            var7 = new byte[1 + var2.length + 1 + 4];
         } else {
            var7 = new byte[1 + var2.length + 1 + 2 + var3.length];
         }

         int var11 = 0;
         C_cf.m_a_e306985c(var7, 0, var2.length);
         System.arraycopy(var2, 0, var7, 1, var2.length);
         int var12 = 0 + 1 + var2.length;
         if (this.f_ao_a_2404ac.f_ad_a_5a) {
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

         C_bv var6 = new C_bv(19, 26, 26L, new byte[0], var7);
         C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var6);
      } else {
         if (this.f_ao_a_2404ac.f_ad_a_49 == 5) {
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
            C_bv var5 = new C_bv(19, 24, 24L, new byte[0], var1);
            C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var5);
         }
      }
   }

   protected final boolean m_a_cb4a8bc8(C_cc var1) {
      return false;
   }

   public final boolean m_a_9b79() {
      return true;
   }

   public final boolean m_b_9b79() {
      return false;
   }
}
