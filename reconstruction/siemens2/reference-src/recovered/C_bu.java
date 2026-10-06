package recovered;

public class C_bu extends C_cb {
   protected int f_bu_a_49;
   protected int f_bu_b_49;
   int f_bu_d_49;
   protected long f_bu_a_4a;
   protected byte[] f_bu_a_b47;
   protected byte[] f_bu_b_b47;

   public C_bu(int var1, int var2, int var3, int var4, long var5, byte[] var7, byte[] var8) {
      super.f_cb_c_49 = var1;
      this.f_bu_a_49 = var2;
      this.f_bu_b_49 = var3;
      this.f_bu_d_49 = var4;
      this.f_bu_a_4a = var5;
      this.f_bu_a_b47 = var7;
      this.f_bu_b_b47 = var8;
   }

   public C_bu(int var1, int var2, long var3, byte[] var5, byte[] var6) {
      this(-1, var1, var2, 0, var3, var5, var6);
   }

   public final int m_b_9b68() {
      return this.f_bu_a_49;
   }

   public final int m_c_9b68() {
      return this.f_bu_b_49;
   }

   public final byte[] m_b_12d408() {
      byte[] var1 = new byte[this.f_bu_b_b47.length];
      System.arraycopy(this.f_bu_b_b47, 0, var1, 0, var1.length);
      return var1;
   }

   public byte[] m_a_12d408() {
      byte[] var1;
      C_ce.m_a_e306985c(var1 = new byte[16 + this.f_bu_b_b47.length + (this.f_bu_a_b47.length > 0 ? 2 + this.f_bu_a_b47.length : 0)], 0, 42);
      C_ce.m_a_e306985c(var1, 1, 2);
      C_ce.m_b_e306985c(var1, 2, super.f_cb_c_49);
      C_ce.m_b_e306985c(var1, 4, 10 + this.f_bu_b_b47.length + (this.f_bu_a_b47.length > 0 ? 2 + this.f_bu_a_b47.length : 0));
      C_ce.m_b_e306985c(var1, 6, this.f_bu_a_49);
      C_ce.m_b_e306985c(var1, 8, this.f_bu_b_49);
      C_ce.m_b_e306985c(var1, 10, this.f_bu_a_b47.length > 0 ? '耀' : 0);
      C_ce.m_a_e3069c1d(var1, 12, this.f_bu_a_4a);
      if (this.f_bu_a_b47.length > 0) {
         C_ce.m_b_e306985c(var1, 16, this.f_bu_a_b47.length);
         System.arraycopy(this.f_bu_a_b47, 0, var1, 18, this.f_bu_a_b47.length);
         System.arraycopy(this.f_bu_b_b47, 0, var1, 18 + this.f_bu_a_b47.length, this.f_bu_b_b47.length);
      } else {
         System.arraycopy(this.f_bu_b_b47, 0, var1, 16, this.f_bu_b_b47.length);
      }

      return var1;
   }

   public static C_cb m_a_3c792e2a(byte[] var0, int var1, int var2) {
      int var3 = C_ce.m_b_49634b7a(var0, var1 + 2);
      int var4;
      if ((var4 = C_ce.m_b_49634b7a(var0, var1 + 4)) < 10) {
         throw new C_aq(133, 0);
      } else {
         int var5 = C_ce.m_b_49634b7a(var0, var1 + 6);
         int var6 = C_ce.m_b_49634b7a(var0, var1 + 8);
         if (var5 == 21 && var6 == 2) {
            return C_ar.m_a_3c792e2a(var0, var1, var2);
         } else if (var5 == 21 && var6 == 3) {
            return C_br.m_a_3c792e2a(var0, var1, var2);
         } else {
            var2 = C_ce.m_b_49634b7a(var0, var1 + 10);
            long var8 = C_ce.m_a_49634b7b(var0, var1 + 12);
            byte[] var7;
            byte[] var10;
            if (var2 == 32768) {
               if (var4 < 12) {
                  throw new C_aq(133, 1);
               }

               int var11 = C_ce.m_b_49634b7a(var0, var1 + 16);
               if (var4 < var11 + 12) {
                  throw new C_aq(133, 2);
               }

               var7 = new byte[var11];
               System.arraycopy(var0, var1 + 6 + 10 + 2, var7, 0, var11);
               var10 = new byte[var4 - 10 - 2 - var11];
               System.arraycopy(var0, var1 + 6 + 10 + 2 + var11, var10, 0, var4 - 10 - 2 - var11);
            } else {
               var7 = new byte[0];
               var10 = new byte[var4 - 10];
               System.arraycopy(var0, var1 + 16, var10, 0, var4 - 10);
            }

            return new C_bu(var3, var5, var6, var2, var8, var7, var10);
         }
      }
   }
}
