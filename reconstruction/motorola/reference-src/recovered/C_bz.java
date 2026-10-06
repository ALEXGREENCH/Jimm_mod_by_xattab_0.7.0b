package recovered;

public final class C_bz extends C_az {
   private String f_bz_a_523beb0a;

   public C_bz(String var1) {
      super(false, true);
      this.f_bz_a_523beb0a = var1;
   }

   protected final void m_a_9b75() {
      byte[] var1 = C_cf.m_a_afa28ebe(this.f_bz_a_523beb0a);
      byte[] var2;
      C_cf.m_a_e306985c(var2 = new byte[1 + var1.length], 0, var1.length);
      System.arraycopy(var1, 0, var2, 1, var1.length);
      C_bv var3 = new C_bv(19, 22, 3L, new byte[0], var2);
      C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var3);
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
