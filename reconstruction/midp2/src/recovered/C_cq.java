package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.DCPacket. Release class: cq. */

public final class C_cq extends C_cd {
   byte[] f_cq_a_b47;

   public C_cq(byte[] var1) {
      this.f_cq_a_b47 = var1;
   }

   public final byte[] m_a_12d408() {
      byte[] var1;
      C_cf.m_a_7dcd25f8(var1 = new byte[this.f_cq_a_b47.length + 2], 0, this.f_cq_a_b47.length, false);
      System.arraycopy(this.f_cq_a_b47, 0, var1, 2, this.f_cq_a_b47.length);
      return var1;
   }

   public static C_cd m_a_3c792e68(byte[] var0, int var1, int var2) {
      return new C_cq(var0);
   }
}
