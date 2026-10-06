package recovered;

import java.io.InputStream;

public final class C_bv extends C_h implements Runnable {
   private C_p[] f_bv_a_2a88dc;
   private Thread f_bv_a_72a5fc31;
   private long f_bv_a_4a;

   public final C_f m_a_485a59b9(int var1) {
      return var1 < this.m_a_9b68() && var1 >= 0 ? this.f_bv_a_2a88dc[var1] : null;
   }

   public final int m_a_9b68() {
      return this.f_bv_a_2a88dc != null ? this.f_bv_a_2a88dc.length : 0;
   }

   public final void m_a_44bd8e9f(String var1, int var2, int var3) {
      try {
         InputStream var15;
         var3 = (var15 = this.getClass().getResourceAsStream(var1 + "/animate.bin")).read();
         this.f_bv_a_2a88dc = new C_p[var3];
         C_h var4 = new C_h();
         int var5 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;

         for (int var10 = 0; var10 < var3; var10++) {
            var5 = var15.read();
            var7 = var15.read();
            var4.m_a_afa300e4(var1 + "/" + (var10 + 1) + ".png", var5);
            boolean var6;
            C_p var18 = (var6 = 0 < var4.m_a_9b68()) ? new C_p(var4.m_a_485a59b9(0), var7) : null;

            for (int var11 = 0; var11 < var7; var11++) {
               var8 = var15.read();
               var9 = var15.read() * 100;
               if (var6) {
                  C_f var12 = var4.m_a_485a59b9(var8);
                  var18.f_p_a_2a87a6[var11] = var12;
                  var18.f_p_a_b4e[var11] = var9;
               }
            }

            this.f_bv_a_2a88dc[var10] = var18;
            if (var6) {
               super.f_h_a_49 = Math.max(super.f_h_a_49, var18.f_f_c_49);
               super.f_h_b_49 = Math.max(super.f_h_b_49, var18.f_f_d_49);
            }
         }
      } catch (Exception var14) {
      }

      if (this.m_a_9b68() > 0) {
         this.f_bv_a_72a5fc31 = new Thread(this);
         this.f_bv_a_72a5fc31.start();
      }
   }

   public final void run() {
      this.f_bv_a_4a = System.currentTimeMillis();
      long var1 = 0L;
      new Object();
      Object var4 = null;

      while (true) {
         try {
            Thread.sleep(100L);
         } catch (Exception var9) {
         }

         var1 = System.currentTimeMillis();
         boolean var10000 = (var4 = C_bg.m_a_810c345d()) instanceof C_be;
         boolean var3 = false;
         if (var10000) {
            boolean var11 = false;

            for (int var5 = 0; var5 < this.m_a_9b68(); var5++) {
               if (this.f_bv_a_2a88dc[var5] != null) {
                  C_p var10001 = this.f_bv_a_2a88dc[var5];
                  long var7 = var1 - this.f_bv_a_4a;
                  C_p var12 = var10001;
                  var10001.f_p_a_4a -= var7;
                  boolean var15;
                  if (var12.f_p_a_4a <= 0L) {
                     var12.f_p_a_49 = (var12.f_p_a_49 + 1) % var12.f_p_a_2a87a6.length;
                     var12.f_p_a_4a = var12.f_p_a_b4e[var12.f_p_a_49];
                     boolean var6 = var12.f_p_a_5a;
                     var12.f_p_a_5a = false;
                     var15 = var6;
                  } else {
                     var15 = false;
                  }

                  var11 |= var15;
               }
            }

            if (var11) {
               ((C_be)var4).m_e_9b75();
            }
         }

         this.f_bv_a_4a = var1;
      }
   }
}
