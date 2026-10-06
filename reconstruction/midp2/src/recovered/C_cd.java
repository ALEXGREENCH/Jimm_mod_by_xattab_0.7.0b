package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.Packet. Release class: cd. */

public class C_cd {
   protected int f_cd_c_49;
   private int f_cd_a_49;
   private byte[] f_cd_a_b47;

   final void m_b_13462e(int var1) {
      this.f_cd_c_49 = var1;
   }

   protected C_cd() {
   }

   public C_cd(byte[] var1) {
      this.f_cd_a_49 = 5;
      this.f_cd_a_b47 = var1;
   }

   public byte[] m_a_12d408() {
      byte[] var1;
      C_cf.m_a_e306985c(var1 = new byte[6 + this.f_cd_a_b47.length], 0, 42);
      C_cf.m_a_e306985c(var1, 1, this.f_cd_a_49);
      C_cf.m_b_e306985c(var1, 2, this.f_cd_c_49);
      C_cf.m_b_e306985c(var1, 4, this.f_cd_a_b47.length);
      System.arraycopy(this.f_cd_a_b47, 0, var1, 6, this.f_cd_a_b47.length);
      return var1;
   }

   public static C_cd m_a_3c792e68(byte[] var0, int var1, int var2) throws recovered.C_ar {
      if (var2 < 2) {
         throw new C_ar(130, 0);
      } else if (C_cf.m_a_49634b7a(var0, var1) != 42) {
         return C_cq.m_a_3c792e68(var0, var1, var2) ;
      } else {
         int var3;
         if ((var3 = C_cf.m_a_49634b7a(var0, var1 + 1)) >= 1 && var3 <= 5) {
            if (C_cf.m_b_49634b7a(var0, var1 + 4) + 6 != var2) {
               throw new C_ar(130, 3);
            } else {
               switch (var3) {
                  case 1:
                     return C_j.m_a_3c792e68(var0, var1, var2) ;
                  case 2:
                     return C_bw.m_a_3c792e68(var0, var1, var2) ;
                  case 3:
                  default:
                     return null;
                  case 4:
                     return C_ai.m_a_3c792e68(var0, var1, var2) ;
               }
            }
         } else {
            throw new C_ar(130, 2);
         }
      }
   }
}
