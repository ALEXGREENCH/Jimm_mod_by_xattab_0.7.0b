package recovered;

public final class C_br extends C_bv {
   private int f_br_d_49;
   private String f_br_a_523beb0a;
   int f_br_e_49;

   private C_br(int var1, long var2, int var4, int var5, String var6, int var7, byte[] var8, byte[] var9) {
      super(var1, 21, 3, var4, var2, var8, var9);
      this.f_br_d_49 = var5;
      this.f_br_a_523beb0a = var6;
      this.f_br_e_49 = var7;
   }

   public final byte[] m_a_12d408() {
      byte[] var1;
      C_cf.m_a_e306985c(var1 = new byte[30 + super.f_bv_b_b47.length + (super.f_bv_a_b47.length > 0 ? 2 + super.f_bv_a_b47.length : 0)], 0, 42);
      C_cf.m_a_e306985c(var1, 1, 2);
      C_cf.m_b_e306985c(var1, 2, super.f_cc_c_49);
      C_cf.m_b_e306985c(var1, 4, 24 + super.f_bv_b_b47.length + (super.f_bv_a_b47.length > 0 ? 2 + super.f_bv_a_b47.length : 0));
      C_cf.m_b_e306985c(var1, 6, super.f_bv_a_49);
      C_cf.m_b_e306985c(var1, 8, super.f_bv_b_49);
      C_cf.m_b_e306985c(var1, 10, super.f_bv_a_b47.length > 0 ? '耀' : 0);
      C_cf.m_a_e3069c1d(var1, 12, super.f_bv_a_4a);
      C_cf.m_b_e306985c(var1, 12, this.f_br_d_49 - 1);
      if (super.f_bv_a_b47.length > 0) {
         C_cf.m_b_e306985c(var1, 16, super.f_bv_a_b47.length);
         System.arraycopy(super.f_bv_a_b47, 0, var1, 18, super.f_bv_a_b47.length);
         C_cf.m_b_e306985c(var1, 18 + super.f_bv_a_b47.length, 1);
         C_cf.m_b_e306985c(var1, 20 + super.f_bv_a_b47.length, 10 + super.f_bv_b_b47.length);
         C_cf.m_a_7dcd25f8(var1, 22 + super.f_bv_a_b47.length, 8 + super.f_bv_b_b47.length, false);
         C_cf.m_a_7dcd9a57(var1, 24 + super.f_bv_a_b47.length, Long.parseLong(this.f_br_a_523beb0a), false);
         C_cf.m_a_7dcd25f8(var1, 28 + super.f_bv_a_b47.length, this.f_br_e_49, false);
         C_cf.m_a_7dcd25f8(var1, 30 + super.f_bv_a_b47.length, this.f_br_d_49, false);
         System.arraycopy(super.f_bv_b_b47, 0, var1, 32 + super.f_bv_a_b47.length, super.f_bv_b_b47.length);
      } else {
         C_cf.m_b_e306985c(var1, 16, 1);
         C_cf.m_b_e306985c(var1, 18, 10 + super.f_bv_b_b47.length);
         C_cf.m_a_7dcd25f8(var1, 20, 8 + super.f_bv_b_b47.length, false);
         C_cf.m_a_7dcd9a57(var1, 22, Long.parseLong(this.f_br_a_523beb0a), false);
         C_cf.m_a_7dcd25f8(var1, 26, this.f_br_e_49, false);
         C_cf.m_a_7dcd25f8(var1, 28, this.f_br_d_49, false);
         System.arraycopy(super.f_bv_b_b47, 0, var1, 30, super.f_bv_b_b47.length);
      }

      return var1;
   }

   public static C_cc m_a_3c792e49(byte[] var0, int var1, int var2) {
      var2 = C_cf.m_b_49634b7a(var0, var1 + 2);
      int var3;
      if ((var3 = C_cf.m_b_49634b7a(var0, var1 + 4)) < 24) {
         throw new C_aq(137, 0);
      } else {
         int var4 = C_cf.m_b_49634b7a(var0, var1 + 10);
         long var6 = C_cf.m_a_49634b7b(var0, var1 + 12);
         byte[] var5;
         byte[] var8;
         String var9;
         int var10;
         int var11;
         if (var4 == 32768) {
            if (var3 < 36) {
               throw new C_aq(137, 1);
            }

            int var12 = C_cf.m_b_49634b7a(var0, var1 + 16);
            if (var3 < var12 + 36) {
               throw new C_aq(137, 2);
            }

            var5 = new byte[var12];
            System.arraycopy(var0, var1 + 6 + 10 + 2, var5, 0, var12);
            var9 = String.valueOf(C_cf.m_a_e306d821(var0, var1 + 6 + 10 + 6 + 2 + var12, false));
            var10 = C_cf.m_a_e306d820(var0, var1 + 6 + 10 + 10 + 2 + var12, false);
            var11 = C_cf.m_a_e306d820(var0, var1 + 6 + 10 + 12 + 2 + var12, false);
            var8 = new byte[var3 - 10 - 14 - 2 - var12];
            System.arraycopy(var0, var1 + 6 + 10 + 14 + 2 + var12, var8, 0, var3 - 10 - 14 - 2 - var12);
         } else {
            var9 = String.valueOf(C_cf.m_a_e306d821(var0, var1 + 6 + 10 + 6, false));
            var10 = C_cf.m_a_e306d820(var0, var1 + 6 + 10 + 10, false);
            var11 = C_cf.m_a_e306d820(var0, var1 + 6 + 10 + 12, false);
            var5 = new byte[0];
            var8 = new byte[var3 - 10 - 14];
            System.arraycopy(var0, var1 + 6 + 10 + 14, var8, 0, var3 - 10 - 14);
         }

         return new C_br(var2, var6, var4, var11, var9, var10, var5, var8);
      }
   }
}
