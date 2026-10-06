package recovered;

public final class C_ai extends C_cc {
   private String f_ai_a_523beb0a;
   String f_ai_b_523beb0a;
   byte[] f_ai_a_b47;
   private int f_ai_a_49;
   private String f_ai_c_523beb0a;

   private C_ai(int var1, String var2, String var3, byte[] var4) {
      super.f_cc_c_49 = var1;
      this.f_ai_a_523beb0a = var2;
      this.f_ai_b_523beb0a = var3;
      this.f_ai_a_b47 = new byte[var4.length];
      System.arraycopy(var4, 0, this.f_ai_a_b47, 0, var4.length);
      this.f_ai_a_49 = -1;
      this.f_ai_c_523beb0a = null;
   }

   private C_ai(int var1, int var2, String var3) {
      super.f_cc_c_49 = var1;
      this.f_ai_a_523beb0a = null;
      this.f_ai_b_523beb0a = null;
      this.f_ai_a_b47 = null;
      this.f_ai_a_49 = var2;
      this.f_ai_c_523beb0a = new String(var3);
   }

   private C_ai(int var1) {
      super.f_cc_c_49 = var1;
      this.f_ai_a_523beb0a = null;
      this.f_ai_b_523beb0a = null;
      this.f_ai_a_b47 = null;
      this.f_ai_a_49 = -1;
      this.f_ai_c_523beb0a = null;
   }

   public C_ai() {
      this(-1);
   }

   public final int m_a_9b68() {
      if (this.f_ai_a_523beb0a != null) {
         return 1;
      } else {
         return this.f_ai_a_49 >= 0 ? 2 : 3;
      }
   }

   public final int m_b_9b68() {
      return this.m_a_9b68() == 2 ? this.f_ai_a_49 : -1;
   }

   public final byte[] m_a_12d408() {
      int var1 = 6;
      if (this.m_a_9b68() == 1) {
         var1 = 6 + 4 + this.f_ai_a_523beb0a.length() + 4 + this.f_ai_b_523beb0a.length() + 4 + this.f_ai_a_b47.length;
      } else if (this.m_a_9b68() == 2) {
         var1 = 12 + 4 + this.f_ai_c_523beb0a.length();
      }

      byte[] var2;
      C_cf.m_a_e306985c(var2 = new byte[var1], 0, 42);
      C_cf.m_a_e306985c(var2, 1, 4);
      C_cf.m_b_e306985c(var2, 2, super.f_cc_c_49);
      C_cf.m_b_e306985c(var2, 4, var1 - 6);
      if (this.m_a_9b68() == 1) {
         C_cf.m_b_e306985c(var2, 6, 1);
         C_cf.m_b_e306985c(var2, 8, this.f_ai_a_523beb0a.length());
         byte[] var4;
         System.arraycopy(var4 = C_cf.m_a_afa28ebe(this.f_ai_a_523beb0a), 0, var2, 10, var4.length);
         var1 = 6 + 4 + var4.length;
         C_cf.m_b_e306985c(var2, var1, 5);
         C_cf.m_b_e306985c(var2, var1 + 2, this.f_ai_b_523beb0a.length());
         byte[] var3;
         System.arraycopy(var3 = C_cf.m_a_afa28ebe(this.f_ai_b_523beb0a), 0, var2, var1 + 4, var3.length);
         var1 += 4 + var3.length;
         C_cf.m_b_e306985c(var2, var1, 6);
         C_cf.m_b_e306985c(var2, var1 + 2, this.f_ai_a_b47.length);
         System.arraycopy(this.f_ai_a_b47, 0, var2, var1 + 4, this.f_ai_a_b47.length);
      } else if (this.m_a_9b68() == 2) {
         C_cf.m_b_e306985c(var2, 6, 1);
         C_cf.m_b_e306985c(var2, 8, this.f_ai_a_523beb0a.length());
         byte[] var7;
         System.arraycopy(var7 = C_cf.m_a_afa28ebe(this.f_ai_a_523beb0a), 0, var2, 10, var7.length);
         var1 = 6 + 4 + var7.length;
         C_cf.m_b_e306985c(var2, var1, 4);
         C_cf.m_b_e306985c(var2, var1 + 2, this.f_ai_c_523beb0a.length());
         byte[] var9;
         System.arraycopy(var9 = C_cf.m_a_afa28ebe(this.f_ai_c_523beb0a), 0, var2, var1 + 4, var9.length);
         C_cf.m_b_e306985c(var2, var1, 8);
         C_cf.m_b_e306985c(var2, var1 + 2, 2);
         C_cf.m_b_e306985c(var2, var1 + 4, this.f_ai_a_49);
      }

      return var2;
   }

   public static C_cc m_a_3c792e49(byte[] var0, int var1, int var2) {
      int var3 = C_cf.m_b_49634b7a(var0, var1 + 2);
      String var4 = null;
      String var5 = null;
      byte[] var6 = null;
      int var7 = -1;
      String var8 = null;
      int var9 = var1 + 6;

      while (var9 < var1 + var2) {
         byte[] var10;
         if ((var10 = C_cf.m_a_e3062636(var0, var9)) == null) {
            throw new C_aq(135, 0);
         }

         int var11 = C_cf.m_b_49634b7a(var0, var9);
         var9 += 4 + var10.length;
         switch (var11) {
            case 1:
               var4 = C_cf.m_a_79834524(var10);
            case 2:
            case 3:
            case 7:
            case 10:
            default:
               break;
            case 4:
            case 11:
               var8 = C_cf.m_a_79834524(var10);
               break;
            case 5:
               var5 = C_cf.m_a_79834524(var10);
               break;
            case 6:
               var6 = var10;
               break;
            case 8:
            case 9:
               var7 = C_cf.m_b_49634b7a(var10, 0);
         }
      }

      if (var4 == null && var5 == null && var6 == null && var7 == -1 && var8 == null) {
         return new C_ai(var3);
      } else if (var4 != null && var5 != null && var6 != null && var7 == -1 && var8 == null) {
         return new C_ai(var3, var4, var5, var6);
      } else if (var5 == null && var6 == null && var7 != -1 && var8 != null) {
         return new C_ai(var3, var7, var8);
      } else {
         throw new C_aq(135, 2);
      }
   }
}
