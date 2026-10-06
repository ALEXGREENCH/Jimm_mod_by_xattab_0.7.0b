package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.SendMessageAction. Release class: bv. */

public final class C_bv extends C_ba {
   private C_c f_bv_a_12984;
   private C_bg f_bv_a_2408ca;
   private int f_bv_a_49 = 65535;
   private int f_bv_b_49 = 0;
   private int f_bv_c_49 = 0;
   private static int f_bv_d_49 = 0;

   public C_bv(C_bm var1) {
      super(false, true);
      if (var1 instanceof C_c) {
         this.f_bv_a_12984 = (C_c)var1;
         this.f_bv_a_2408ca = null;
         this.f_bv_b_49 = (int)System.currentTimeMillis();
         this.f_bv_c_49 = p_bv_b_9b68();
      } else {
         if (var1 instanceof C_bg) {
            this.f_bv_a_12984 = null;
            this.f_bv_a_2408ca = (C_bg)var1;
         }
      }
   }

   public final long m_a_9b69() {
      return ((long)this.f_bv_b_49 << 32) + this.f_bv_c_49;
   }

   private static synchronized int p_bv_b_9b68() {
      return f_bv_d_49++;
   }

   protected final void m_a_9b75() throws recovered.C_ar {
      this.f_bv_a_49--;
      C_aw var1;
      if (this.f_bv_a_12984 != null) {
         var1 = this.f_bv_a_12984.m_a_46a7a37a();
      } else {
         var1 = this.f_bv_a_2408ca.m_a_46a7a37a();
      }

      byte var2 = 1;
      boolean var3 = var1.m_b_134632(2);
      if (this.f_bv_a_2408ca != null && var1.m_b_134621(192) != -1) {
         var2 = 2;
      }

      if (this.f_bv_a_12984 != null && this.f_bv_a_12984.f_bm_b_49 >= 1000 && this.f_bv_a_12984.f_bm_b_49 <= 1004) {
         var2 = 2;
      }

      if (C_bq.m_a_134632(164)
         && C_bq.m_a_134632(170)
         && var1.m_b_134632(1)
         && var1.m_b_134621(76) != 28
         && var1.m_b_134621(76) != 5
         && var1.m_b_134621(76) != 9
         && var1.m_b_134621(76) != 10
         && var1.m_b_134621(192) != -1) {
         var2 = 2;
      }

      if (var2 == 1) {
         byte[] var6 = C_cf.m_a_afa28ebe(var1.m_a_47921032(0));
         byte[] var9;
         if (var3) {
            var9 = C_cf.m_b_afa28ebe(C_cf.m_b_e96ea081(this.f_bv_a_12984.f_c_a_523beb0a));
         } else {
            var9 = C_cf.m_a_afa28ebe(C_cf.m_b_e96ea081(this.f_bv_a_12984.f_c_a_523beb0a));
         }

         byte[] var4 = new byte[11 + var6.length + 4 + (var3 ? 6 : 5) + 4 + 4 + var9.length + 4];
         int var5 = 0;
         C_cf.m_a_e3069c1d(var4, 0, this.f_bv_b_49);
         C_cf.m_a_e3069c1d(var4, 4, this.f_bv_c_49);
         C_cf.m_b_e306985c(var4, 8, 1);
         C_cf.m_a_e306985c(var4, 10, var6.length);
         System.arraycopy(var6, 0, var4, 11, var6.length);
         var5 = 10 + 1 + var6.length;
         C_cf.m_b_e306985c(var4, var5, 2);
         C_cf.m_b_e306985c(var4, var5 + 2, (var3 ? 6 : 5) + 4 + 4 + var9.length);
         int var16 = var5 + 4;
         C_cf.m_b_e306985c(var4, var16, 1281);
         if (var3) {
            C_cf.m_b_e306985c(var4, var16 + 2, 2);
            C_cf.m_b_e306985c(var4, var16 + 4, 262);
            var5 = var16 + 6;
         } else {
            C_cf.m_b_e306985c(var4, var16 + 2, 1);
            C_cf.m_a_e306985c(var4, var16 + 4, 1);
            var5 = var16 + 5;
         }

         C_cf.m_b_e306985c(var4, var5, 257);
         C_cf.m_b_e306985c(var4, var5 + 2, 4 + var9.length);
         int var18 = var5 + 4;
         if (var3) {
            C_cf.m_a_e3069c1d(var4, var18, 131072L);
         } else {
            C_cf.m_a_e3069c1d(var4, var18, 0L);
         }

         var5 = var18 + 4;
         System.arraycopy(var9, 0, var4, var5, var9.length);
         var5 += var9.length;
         C_cf.m_b_e306985c(var4, var5, 6);
         C_cf.m_b_e306985c(var4, var5 + 2, 0);
         C_bw var12 = new C_bw(4, 6, 0L, new byte[0], var4);
         C_ac.f_ac_a_240bf0.m_a_cb4b0023(var12);
      } else if (var2 == 2) {
         byte[] var7 = C_cf.m_a_afa28ebe(var1.m_a_47921032(0));
         byte[] var10;
         byte[] var14;
         if (this.f_bv_a_2408ca == null) {
            var10 = C_cf.m_a_44c4d6c8(C_cf.m_b_e96ea081(this.f_bv_a_12984.f_c_a_523beb0a), true);
            var14 = new byte[0];
         } else {
            Object var11 = null;
            var10 = C_cf.m_a_afa28ebe(this.f_bv_a_2408ca.f_bg_b_523beb0a);
            var14 = C_cf.m_a_afa28ebe(this.f_bv_a_2408ca.f_bg_a_523beb0a);
         }

         int var21 = 0;
         if (this.f_bv_a_2408ca == null) {
            var21 = 163 + var7.length + var10.length;
         } else {
            var21 = 192 + var7.length + var10.length + var14.length + 1;
         }

         byte[] var13;
         C_cf.m_a_e3069c1d(var13 = new byte[var21], 0, this.f_bv_b_49);
         C_cf.m_a_e3069c1d(var13, 4, this.f_bv_c_49);
         C_cf.m_b_e306985c(var13, 8, 2);
         C_cf.m_a_e306985c(var13, 10, var7.length);
         System.arraycopy(var7, 0, var13, 11, var7.length);
         var21 = 10 + 1 + var7.length;
         C_cf.m_b_e306985c(var13, var21, 5);
         int var24 = var21 + 2;
         if (this.f_bv_a_2408ca == null) {
            C_cf.m_a_7dcd25f8(var13, var24, 144 + var10.length, true);
         } else {
            C_cf.m_b_e306985c(var13, var24, 173 + var10.length + var14.length + 1);
         }

         var21 = var24 + 2;
         C_cf.m_b_e306985c(var13, var21, 0);
         var21 += 2;
         C_cf.m_a_e3069c1d(var13, var21, this.f_bv_b_49);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, this.f_bv_c_49);
         var21 += 4;
         System.arraycopy(C_cf.f_cf_a_b47, 0, var13, var21, 16);
         var21 += 16;
         C_cf.m_a_e3069c1d(var13, var21, 655362L);
         var21 += 4;
         C_cf.m_b_e306985c(var13, var21, 1);
         var21 += 2;
         C_cf.m_a_e3069c1d(var13, var21, 983040L);
         int var32 = var21 + 4;
         if (this.f_bv_a_2408ca != null) {
            C_cf.m_b_e306985c(var13, var32, 3);
            C_cf.m_b_e306985c(var13, var32 + 2, 4);
            System.arraycopy(C_ac.f_ac_a_240bf0.m_a_12d408(), 0, var13, var32 + 4, 4);
            var21 = var32 + 8;
            C_cf.m_b_e306985c(var13, var21, 5);
            C_cf.m_b_e306985c(var13, var21 + 2, 2);
            C_cf.m_b_e306985c(var13, var21 + 4, C_ac.f_ac_a_240bf0.m_a_9b68());
            var32 = var21 + 6;
         }

         C_cf.m_b_e306985c(var13, var32, 10001);
         var21 = var32 + 2;
         if (this.f_bv_a_2408ca == null) {
            C_cf.m_a_7dcd25f8(var13, var21, 104 + var10.length, true);
         } else {
            C_cf.m_b_e306985c(var13, var21, 119 + var10.length + var14.length + 1);
         }

         var21 += 2;
         C_cf.m_b_e306985c(var13, var21, 6912);
         var21 += 2;
         C_cf.m_b_e306985c(var13, var21, 2048);
         var21 += 2;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_b_e306985c(var13, var21, 0);
         int var42 = var21 + 2;
         C_cf.m_a_e306985c(var13, var42, 3);
         var42++;
         if (this.f_bv_a_2408ca == null) {
            C_cf.m_a_e3069c1d(var13, var42, 0L);
         } else {
            C_cf.m_a_e3069c1d(var13, var42, 4L);
         }

         var21 = var42 + 4;
         C_cf.m_a_7dcd25f8(var13, var21, this.f_bv_a_49, false);
         var21 += 2;
         C_cf.m_a_7dcd25f8(var13, var21, 14, false);
         var21 += 2;
         C_cf.m_a_7dcd25f8(var13, var21, this.f_bv_a_49, false);
         var21 += 2;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         var21 += 4;
         C_cf.m_a_e3069c1d(var13, var21, 0L);
         int var50 = var21 + 4;
         if (this.f_bv_a_2408ca == null) {
            C_cf.m_a_7dcd25f8(var13, var50, this.f_bv_a_12984.f_bm_b_49, false);
         } else {
            C_cf.m_a_7dcd25f8(var13, var50, this.f_bv_a_2408ca.f_bm_b_49, false);
         }

         var21 = var50 + 2;
         C_cf.m_a_7dcd25f8(var13, var21, C_cf.m_b_134621((int)C_bq.a$134622()), false);
         var21 += 2;
         C_cf.m_a_7dcd25f8(var13, var21, 2, false);
         int var53 = var21 + 2;
         if (this.f_bv_a_2408ca == null) {
            C_cf.m_a_7dcd25f8(var13, var53, var10.length + 1, false);
            var21 = var53 + 2;
            System.arraycopy(var10, 0, var13, var21, var10.length);
            var21 += var10.length;
            C_cf.m_a_e306985c(var13, var21, 0);
            C_cf.m_a_e3069c1d(var13, ++var21, 0L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, -256L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 637534208L);
            var21 += 4;
            System.arraycopy(C_cf.f_cf_c_b47, 0, var13, var21, 38);
            var21 += 38;
         } else {
            C_cf.m_a_7dcd25f8(var13, var53, 1, false);
            var21 = var53 + 2;
            C_cf.m_a_e306985c(var13, var21, 0);
            C_cf.m_a_7dcd25f8(var13, ++var21, 41, false);
            var21 += 2;
            C_cf.m_a_e3069c1d(var13, var21, -265481511L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 814863121L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, -1915289584L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 1258702382L);
            var21 += 4;
            C_cf.m_b_e306985c(var13, var21, 0);
            var21 += 2;
            C_cf.m_a_7dcd9a57(var13, var21, 4L, false);
            var21 += 4;
            System.arraycopy(C_cf.m_a_afa28ebe("File"), 0, var13, var21, 4);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 256L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 65536L);
            var21 += 4;
            C_cf.m_a_e3069c1d(var13, var21, 0L);
            var21 += 4;
            C_cf.m_b_e306985c(var13, var21, 0);
            var21 += 2;
            C_cf.m_a_e306985c(var13, var21, 0);
            C_cf.m_a_7dcd9a57(var13, ++var21, 18 + var10.length + var14.length + 1, false);
            var21 += 4;
            C_cf.m_a_7dcd9a57(var13, var21, var10.length, false);
            var21 += 4;
            System.arraycopy(var10, 0, var13, var21, var10.length);
            var21 += var10.length;
            C_cf.m_a_e3069c1d(var13, var21, -1937636830L);
            var21 += 4;
            C_cf.m_a_7dcd25f8(var13, var21, var14.length + 1, false);
            var21 += 2;
            System.arraycopy(var14, 0, var13, var21, var14.length);
            var21 += var14.length;
            C_cf.m_a_e306985c(var13, var21, 0);
            C_cf.m_a_7dcd9a57(var13, ++var21, this.f_bv_a_2408ca.f_bg_a_49, false);
            var21 += 4;
            C_cf.m_a_7dcd9a57(var13, var21, 35970L, false);
            var21 += 4;
         }

         C_cf.m_a_7dcd25f8(var13, var21, 3, true);
         var21 += 2;
         C_cf.m_b_e306985c(var13, var21, 0);
         C_bw var8 = new C_bw(4, 6, 0L, new byte[0], var13);
         C_ac.f_ac_a_240bf0.m_a_cb4b0023(var8);
      }

      this.f_bv_a_49--;
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
