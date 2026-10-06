package recovered;

public final class C_j extends C_cb {
   private byte[] f_j_a_b47;
   private String f_j_a_523beb0a;
   private String f_j_b_523beb0a;

   private C_j(int var1) {
      super.f_cb_c_49 = var1;
      this.f_j_a_b47 = null;
      this.f_j_a_523beb0a = null;
      this.f_j_b_523beb0a = null;
   }

   public C_j() {
      this(-1);
   }

   private C_j(int var1, byte[] var2) {
      super.f_cb_c_49 = var1;
      this.f_j_a_b47 = new byte[var2.length];
      System.arraycopy(var2, 0, this.f_j_a_b47, 0, var2.length);
      this.f_j_a_523beb0a = null;
      this.f_j_b_523beb0a = null;
   }

   public C_j(byte[] var1) {
      this(-1, var1);
   }

   private C_j(int var1, String var2, String var3) {
      super.f_cb_c_49 = var1;
      this.f_j_a_b47 = null;
      this.f_j_a_523beb0a = new String(var2);
      this.f_j_b_523beb0a = new String(var3);
   }

   public C_j(String var1, String var2) {
      this(-1, var1, var2);
   }

   public final int m_a_9b68() {
      if (this.f_j_a_b47 == null && this.f_j_a_523beb0a == null) {
         return 1;
      } else {
         return this.f_j_a_523beb0a != null ? 3 : 2;
      }
   }

   public final byte[] m_a_12d408() {
      int var1 = 10;
      if (this.m_a_9b68() == 2) {
         var1 = 10 + 4 + this.f_j_a_b47.length;
      } else if (this.m_a_9b68() == 3) {
         var1 = 10 + 4 + this.f_j_a_523beb0a.length() + 4 + this.f_j_b_523beb0a.length();
      }

      byte[] var2 = new byte[var1];
      int var3 = 0;
      C_ce.m_a_e306985c(var2, 0, 42);
      C_ce.m_a_e306985c(var2, 1, 1);
      C_ce.m_b_e306985c(var2, 2, super.f_cb_c_49);
      C_ce.m_b_e306985c(var2, 4, var1 - 6);
      C_ce.m_a_e3069c1d(var2, 6, 1L);
      if (this.m_a_9b68() == 2) {
         C_ce.m_b_e306985c(var2, 10, 6);
         C_ce.m_b_e306985c(var2, 12, this.f_j_a_b47.length);
         System.arraycopy(this.f_j_a_b47, 0, var2, 14, this.f_j_a_b47.length);
      } else if (this.m_a_9b68() == 3) {
         C_ce.m_b_e306985c(var2, 10, 1);
         C_ce.m_b_e306985c(var2, 12, this.f_j_a_523beb0a.length());
         byte[] var5;
         System.arraycopy(var5 = C_ce.m_a_afa28ebe(this.f_j_a_523beb0a), 0, var2, 14, var5.length);
         var3 = 10 + 4 + var5.length;
         C_ce.m_b_e306985c(var2, var3, 2);
         C_ce.m_b_e306985c(var2, var3 + 2, this.f_j_b_523beb0a.length());
         byte[] var4;
         System.arraycopy(var4 = C_ce.m_a_4962d961(C_ce.m_a_afa28ebe(this.f_j_b_523beb0a)), 0, var2, var3 + 4, var4.length);
      }

      return var2;
   }

   public static C_cb m_a_3c792e2a(byte[] var0, int var1, int var2) {
      int var3 = C_ce.m_b_49634b7a(var0, var1 + 2);
      int var10000 = C_ce.m_b_49634b7a(var0, var1 + 4);
      boolean var4 = false;
      if (var10000 >= 4 && C_ce.m_a_49634b7b(var0, var1 + 6) == 1L) {
         byte[] var12 = null;
         String var5 = null;
         String var6 = null;
         String var7 = null;
         byte[] var8 = null;
         int var9 = var1 + 10;

         while (var9 < var1 + var2) {
            byte[] var10;
            if ((var10 = C_ce.m_a_e3062636(var0, var9)) == null) {
               throw new C_aq(132, 1);
            }

            int var11 = C_ce.m_b_49634b7a(var0, var9);
            var9 += 4 + var10.length;
            switch (var11) {
               case 1:
                  var5 = C_ce.m_a_79834524(var10);
                  break;
               case 2:
                  var6 = C_ce.m_a_79834524(C_ce.m_a_4962d961(var10));
                  break;
               case 3:
                  var7 = C_ce.m_a_79834524(var10);
                  break;
               case 4:
               case 5:
               case 7:
               case 8:
               case 9:
               case 10:
               case 11:
               case 12:
               case 13:
               case 16:
               case 17:
               case 18:
               case 19:
               case 21:
               default:
                  throw new C_aq(132, 2);
               case 6:
                  var12 = var10;
                  break;
               case 14:
                  C_ce.m_a_79834524(var10);
                  break;
               case 15:
                  C_ce.m_a_79834524(var10);
               case 20:
               case 23:
               case 24:
               case 25:
               case 26:
                  break;
               case 22:
                  var8 = var10;
            }
         }

         if (var12 == null && var5 == null && var6 == null && var7 == null && var8 == null) {
            return new C_j(var3);
         } else if (var12 != null && var5 == null && var6 == null && var7 == null && var8 == null) {
            return new C_j(var3, var12);
         } else if (var12 == null && var5 != null && var6 != null && var7 != null && var8 != null) {
            return new C_j(var3, var5, var6);
         } else {
            throw new C_aq(132, 3);
         }
      } else {
         throw new C_aq(132, 0);
      }
   }
}
