package recovered;

public final class C_co extends C_cb {
   byte[] f_co_a_b47;

   public C_co(byte[] var1) {
      this.f_co_a_b47 = var1;
   }

   public final byte[] m_a_12d408() {
      byte[] var1;
      C_ce.m_a_7dcd25f8(var1 = new byte[this.f_co_a_b47.length + 2], 0, this.f_co_a_b47.length, false);
      System.arraycopy(this.f_co_a_b47, 0, var1, 2, this.f_co_a_b47.length);
      return var1;
   }

   public static C_cb m_a_3c792e2a(byte[] var0, int var1, int var2) {
      return new C_co(var0);
   }
}
