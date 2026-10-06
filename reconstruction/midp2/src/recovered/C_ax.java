package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.DirectConnectionAction. Release class: ax. */

import java.util.Date;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import jimm.Jimm;

public final class C_ax extends C_ba {
   private int f_ax_a_49;
   private C_bg f_ax_a_2408ca;
   private int f_ax_b_49;
   private long f_ax_a_4a;
   private boolean f_ax_b_5a;

   public C_ax(C_bg var1) {
      super(true, true);
      this.f_ax_a_2408ca = var1;
      this.f_ax_b_49 = 0;
      this.f_ax_a_4a = 0L;
      this.f_ax_b_5a = false;
   }

   protected final void m_a_9b75() throws recovered.C_ar {
      super.f_ba_a_24048d.getClass();
      (C_ac.f_ac_a_240c0f = new C_cc())
         .m_a_aad3b1ff(C_cf.m_c_79834524(this.f_ax_a_2408ca.m_a_46a7a37a().m_a_255806f(225)) + ":" + this.f_ax_a_2408ca.m_a_46a7a37a().m_b_134621(74));
      byte[] var1;
      C_cf.m_a_e306985c(var1 = new byte[48], 0, 255);
      C_cf.m_a_7dcd9a57(var1, 1, 8L, false);
      C_cf.m_b_e306985c(var1, 3, 11008);
      C_cf.m_a_7dcd9a57(var1, 5, Long.parseLong(this.f_ax_a_2408ca.m_a_73cf11cb()), false);
      C_cf.m_b_e306985c(var1, 9, 0);
      C_cf.m_a_7dcd9a57(var1, 11, C_ac.f_ac_a_240c0f.m_b_9b68(), false);
      C_cf.m_a_7dcd9a57(var1, 15, Long.parseLong(C_bq.m_a_47921032(254)), false);
      System.arraycopy(var1, 19, C_ac.f_ac_a_240c0f.m_a_12d408(), 0, 4);
      C_cf.m_a_e3069c1d(var1, 23, -1442971648L);
      C_cf.m_a_e306985c(var1, 27, 4);
      C_cf.m_a_7dcd9a57(var1, 28, C_ac.f_ac_a_240c0f.m_b_9b68(), false);
      C_cf.m_a_7dcd9a57(var1, 32, this.f_ax_a_2408ca.m_a_46a7a37a().m_b_134621(193), false);
      C_cf.m_a_e3069c1d(var1, 36, 1342177280L);
      C_cf.m_a_e3069c1d(var1, 40, 50331648L);
      C_cf.m_a_e3069c1d(var1, 44, 0L);
      C_cq var2 = new C_cq(var1);
      C_ac.f_ac_a_240c0f.m_a_cb4b0023(var2);
   }

   protected final boolean m_a_cb4b0027(C_cd var1) throws recovered.C_ar {
      int var2 = 0;
      if (var1 instanceof C_cq) {
         byte[] var4;
         if ((var4 = ((C_cq)var1).f_cq_a_b47).length >= 4 && C_cf.m_a_49634b7b(var4, 0) == 16777216L) {
            byte[] var7;
            C_cf.m_a_e3069c1d(var7 = new byte[4], 0, 16777216L);
            C_cq var21 = new C_cq(var7);
            C_ac.f_ac_a_240c0f.m_a_cb4b0023(var21);
            byte[] var8 = new byte[19 + C_bq.m_a_47921032(254).length() + 1];
            var2 = 0;
            C_cf.m_a_e306985c(var8, 0, 0);
            C_cf.m_a_e3069c1d(var8, 1, 0L);
            C_cf.m_a_7dcd9a57(var8, 5, 1L, false);
            C_cf.m_a_7dcd9a57(var8, 9, this.f_ax_a_2408ca.f_bg_a_49, false);
            C_cf.m_a_7dcd9a57(var8, 13, 64L, false);
            C_cf.m_a_e306985c(var8, 17, C_bq.m_a_47921032(254).length() + 1);
            C_cf.m_a_e306985c(var8, 18, 0);
            byte[] var23;
            System.arraycopy(var23 = C_cf.m_a_afa28ebe(C_bq.m_a_47921032(254)), 0, var8, 19, var23.length);
            var2 = 19 + var23.length;
            C_cf.m_a_e306985c(var8, var2, 0);
            C_cq var9 = new C_cq(var8);
            C_ac.f_ac_a_240c0f.m_a_cb4b0023(var9);
            this.f_ax_a_49 = 1;
            var2 = 1;
         } else if (this.f_ax_a_49 == 1 && C_cf.m_a_49634b7a(var4, 0) == 1) {
            byte[] var6;
            C_cf.m_a_e306985c(var6 = new byte[16 + this.f_ax_a_2408ca.f_bg_a_523beb0a.length() + 1 + 3], 0, 2);
            C_cf.m_a_e306985c(var6, 1, 0);
            C_cf.m_a_e306985c(var6, 2, this.f_ax_a_2408ca.f_bg_a_523beb0a.length() + 1);
            C_cf.m_a_e306985c(var6, 3, 0);
            byte[] var14;
            System.arraycopy(var14 = C_cf.m_a_afa28ebe(this.f_ax_a_2408ca.f_bg_a_523beb0a), 0, var6, 4, var14.length);
            var2 = 4 + var14.length;
            C_cf.m_a_e306985c(var6, var2, 0);
            C_cf.m_b_e306985c(var6, ++var2, 256);
            C_cf.m_a_e306985c(var6, var2 + 2, 0);
            var2 += 3;
            C_cf.m_a_7dcd9a57(var6, var2, this.f_ax_a_2408ca.f_bg_a_49, false);
            var2 += 4;
            C_cf.m_a_e3069c1d(var6, var2, 0L);
            var2 += 4;
            C_cf.m_a_7dcd9a57(var6, var2, 100L, false);
            C_cq var20 = new C_cq(var6);
            C_ac.f_ac_a_240c0f.m_a_cb4b0023(var20);
            this.f_ax_a_49 = 2;
            var2 = 1;
         } else if (this.f_ax_a_49 == 2 && C_cf.m_a_49634b7a(var4, 0) == 3 && C_cf.m_a_49634b7a(var4, 13) == 1) {
            Date var10 = new Date();
            this.f_ax_a_4a = var10.getTime();

            while (true) {
               var2 = this.f_ax_b_49;
               C_bg var5 = this.f_ax_a_2408ca;
               if (var2 > var5.f_bg_a_49 / 2048 || this.f_ax_b_5a) {
                  Date var13 = new Date();
                  this.f_ax_a_4a = var13.getTime() - this.f_ax_a_4a;

                  try {
                     Thread.sleep(5000L);
                  } catch (Exception var3) {
                  }

                  C_ac.f_ac_a_240c0f.m_a_9b75() ;
                  Thread.yield();
                  C_ac.f_ac_a_240c0f = null;
                  this.f_ax_a_2408ca.m_a_46a7a37a().m_a_cb3e45bf(null);
                  var2 = 1;
                  if (!this.f_ax_b_5a) {
                     this.f_ax_a_49 = 3;
                  }
                  break;
               }

               C_cq var12 = new C_cq(this.f_ax_a_2408ca.m_a_255806f(this.f_ax_b_49));
               C_ac.f_ac_a_240c0f.m_a_cb4b0023(var12);
               this.f_ax_b_49++;
            }
         }
      }

      return var2 != 0;
   }

   public final int m_a_9b68() {
      int var2;
      try {
         var2 = this.f_ax_b_49 * 100 / (this.f_ax_a_2408ca.f_bg_a_49 >>> 11);
      } catch (Exception var1) {
         var2 = 0;
      }

      return var2;
   }

   public final boolean m_a_9b79() {
      return this.f_ax_a_49 == 3;
   }

   public final boolean m_b_9b79() {
      return this.f_ax_a_49 == -1 || this.f_ax_b_5a;
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            Alert var3;
            (var3 = new Alert(
                  C_bt.m_a_e96ea081("r2"),
                  C_bt.m_a_e96ea081("r2")
                     + " "
                     + C_bt.m_a_e96ea081("57")
                     + " "
                     + C_bt.m_a_e96ea081("06")
                     + ".\n"
                     + C_bt.m_a_e96ea081("x5")
                     + ": "
                     + (
                        this.f_ax_a_4a != 0L
                           ? this.f_ax_a_2408ca.f_bg_a_49 / this.f_ax_a_4a + "." + this.f_ax_a_2408ca.f_bg_a_49 % this.f_ax_a_4a / (this.f_ax_a_4a / 10L)
                           : "0.0"
                     )
                     + " "
                     + C_bt.m_a_e96ea081("f3"),
                  null,
                  AlertType.INFO
               ))
               .setTimeout(2000);
            C_w.m_a_ab8148d2(var3);
         default:
            return;
         case 2:
            this.f_ax_b_5a = true;
            C_w.m_a_9b75() ;
            return;
         case 3:
            Alert var2 = new Alert(
               C_bt.m_a_e96ea081("r2"),
               C_bt.m_a_e96ea081("r2") + " " + C_bt.m_a_e96ea081("57") + " " + C_bt.m_a_e96ea081("W3") + " " + C_bt.m_a_e96ea081("06") + "!",
               null,
               AlertType.WARNING
            );
            C_w.m_a_46a7a52c().m_a_75ca2789(Jimm.f_jimm_Jimm_a_4a58c677, var2);
      }
   }
}
