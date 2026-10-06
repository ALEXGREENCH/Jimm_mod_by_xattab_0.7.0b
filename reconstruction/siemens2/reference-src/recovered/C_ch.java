package recovered;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.Date;

public final class C_ch extends C_az {
   private String[] f_ch_a_6dccaaa5 = new String[48];
   private Date f_ch_a_711fe21;
   private int f_ch_a_49;
   private int f_ch_b_49;

   public C_ch(String[] var1) {
      super(false, true);
      this.f_ch_a_6dccaaa5 = var1;
   }

   protected final synchronized void m_a_9b75() {
      ByteArrayOutputStream var1;
      C_ce.m_a_559c4327(var1 = new ByteArrayOutputStream(), 3130, false);
      C_ce.m_a_2a17548d(340, var1, this.f_ch_a_6dccaaa5[1], false);
      C_ce.m_a_2a17548d(320, var1, this.f_ch_a_6dccaaa5[38], false);
      C_ce.m_a_2a17548d(330, var1, this.f_ch_a_6dccaaa5[39], false);
      C_ce.m_a_2a17548d(531, var1, this.f_ch_a_6dccaaa5[12], false);
      C_ce.m_a_2a17548d(600, var1, this.f_ch_a_6dccaaa5[22], false);
      C_ce.m_a_2a17548d(400, var1, this.f_ch_a_6dccaaa5[4], false);
      C_ce.m_a_c9e49450(490, var1, C_bi.f_bi_a_b4e[0], this.f_ch_a_6dccaaa5[44]);
      C_ce.m_a_c9e49450(490, var1, C_bi.f_bi_a_b4e[1], this.f_ch_a_6dccaaa5[45]);
      C_ce.m_a_c9e49450(490, var1, C_bi.f_bi_a_b4e[2], this.f_ch_a_6dccaaa5[46]);
      C_ce.m_a_c9e49450(490, var1, C_bi.f_bi_a_b4e[3], this.f_ch_a_6dccaaa5[47]);
      String var2;
      if ((var2 = this.f_ch_a_6dccaaa5[3]) != null && var2.length() != 0) {
         C_ce.m_a_2a17548d(350, var1, this.f_ch_a_6dccaaa5[3], false);
      }

      String[] var4;
      if ((var2 = this.f_ch_a_6dccaaa5[13]) != null && (var4 = C_ce.m_a_639c22ad(var2, '.')).length == 3) {
         C_ce.m_a_559c4327(var1, 570, false);
         C_ce.m_a_559c4327(var1, 6, false);
         C_ce.m_a_559c4327(var1, Integer.parseInt(var4[2]), false);
         C_ce.m_a_559c4327(var1, Integer.parseInt(var4[1]), false);
         C_ce.m_a_559c4327(var1, Integer.parseInt(var4[0]), false);
      }

      C_ce.m_a_559c4327(var1, 380, false);
      C_ce.m_a_559c4327(var1, 1, false);
      C_ce.m_a_5557994d(var1, C_ce.m_b_aad3b1f2(this.f_ch_a_6dccaaa5[11]));
      C_ar var5 = new C_ar(C_bp.m_a_47921032(254), 2000, new byte[0], var1.toByteArray());
      C_ac.f_ac_a_240b17.m_a_cb4a1765(var5);
      this.f_ch_a_711fe21 = new Date();
   }

   protected final synchronized boolean m_a_cb4a1769(C_cb var1) {
      boolean var2 = false;
      if (var1 instanceof C_br) {
         C_br var5;
         if ((var5 = (C_br)var1).f_br_e_49 != 2010) {
            return false;
         }

         DataInputStream var6 = C_ce.a$6f0c2d54(var5.m_b_12d408());

         try {
            switch (C_ce.a$175c50c1(var6)) {
               case 3135:
                  if (var6.readByte() != 10) {
                     this.f_ch_b_49++;
                  } else {
                     var2 = true;
                     this.f_ch_a_49++;
                  }
            }
         } catch (Exception var4) {
         }
      }

      return var2;
   }

   public final synchronized boolean m_a_9b79() {
      return this.f_ch_a_49 >= 1;
   }

   public final synchronized boolean m_b_9b79() {
      return this.f_ch_a_711fe21.getTime() + 5000L < System.currentTimeMillis() || this.f_ch_b_49 > 0;
   }

   public final int m_a_9b68() {
      return this.f_ch_a_49 > 0 ? 100 : 0;
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            C_w.m_a_9b75();
            C_ac.f_ac_b_5a = true;
      }
   }
}
