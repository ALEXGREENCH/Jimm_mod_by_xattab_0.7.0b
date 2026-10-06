package recovered;

/** 0.6 source correspondence (inferred): jimm.TimerTasks. Release class: cm. */

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public final class C_cm extends TimerTask implements CommandListener {
   private static int f_cm_b_49 = (int)C_bq.a$134622();
   public static int f_cm_a_49 = C_bq.m_a_134621(109) * 60000;
   private static Timer f_cm_a_f6e8e442;
   private int f_cm_c_49 = -1;
   private C_ba f_cm_a_240810;
   private boolean f_cm_a_5a = false;
   private boolean f_cm_b_5a = false;
   private Object f_cm_a_5f790d9c;
   private String f_cm_a_523beb0a;
   private String f_cm_b_523beb0a;
   private int f_cm_d_49;
   public static long f_cm_a_4a = C_cf.m_a_25deca9(false, true);

   public C_cm(C_ba var1) {
      this.f_cm_a_240810 = var1;
   }

   public C_cm(int var1) {
      this.f_cm_c_49 = var1;
   }

   public C_cm(Object var1, String var2, int var3, int var4) {
      this.f_cm_a_5f790d9c = var1;
      this.f_cm_a_523beb0a = var2;
      this.f_cm_b_523beb0a = C_bi.m_a_69d680af(var1);
      this.f_cm_d_49 = var4 == 4 ? var3 : 0;
      this.f_cm_c_49 = var4;
   }

   public final boolean cancel() {
      this.f_cm_b_5a = true;
      return super.cancel();
   }

   public final boolean m_a_9b79() {
      return this.f_cm_b_5a;
   }

   public final int m_a_9b68() {
      return this.f_cm_c_49;
   }

   public final void run() {
      if (!this.f_cm_a_5a) {
         if (this.f_cm_c_49 == -1) {
            String var1;
            if ((var1 = this.f_cm_a_240810.m_a_73cf11cb()) != null) {
               C_cp.m_a_aad3b1ff(var1);
            }

            C_cp.m_b_13462e(this.f_cm_a_240810.m_a_9b68());
            if (this.f_cm_a_240810.m_a_9b79()) {
               this.cancel();
               this.f_cm_a_240810.m_a_13462e(1);
            } else {
               if (this.f_cm_a_240810.m_b_9b79()) {
                  this.f_cm_a_5a = true;
                  this.cancel();
                  this.f_cm_a_240810.m_a_13462e(3);
               }
            }
         } else {
            switch (this.f_cm_c_49) {
               case 1:
                  C_cp.m_b_9b75();
                  return;
               case 2:
                  C_cp.f_cp_a_5a = false;
                  C_cp.m_b_9b75();
                  return;
               case 3:
                  C_cp.m_a_aad3b1ff(C_bt.m_a_e96ea081("l3"));
                  C_cp.m_a_13462e(C_bi.m_a_1349e2(C_ac.m_c_9b68()));
                  C_cp.m_a_48a013c6(C_ac.m_a_2477940());
                  C_cp.m_b_9b75();
                  return;
               case 4:
                  if (this.f_cm_d_49 == 0) {
                     C_bi.m_a_75ba1f9b(this.f_cm_a_5f790d9c, this.f_cm_b_523beb0a);
                     this.cancel();
                     this.f_cm_a_5f790d9c = null;
                     return;
                  }

                  if (this.p_cm_b_9b79()) {
                     return;
                  }

                  C_bi.m_a_75ba1f9b(this.f_cm_a_5f790d9c, (this.f_cm_d_49 & 1) == 0 ? this.f_cm_a_523beb0a : " ");
                  this.f_cm_d_49--;
                  return;
               case 5:
                  if (this.p_cm_b_9b79()) {
                     return;
                  }

                  C_bi.m_a_75ba1f9b(this.f_cm_a_5f790d9c, this.f_cm_a_523beb0a.substring(this.f_cm_d_49));
                  this.f_cm_d_49++;
                  if (this.f_cm_d_49 > this.f_cm_a_523beb0a.length() - 5) {
                     this.f_cm_d_49 = 0;
                     return;
                  }
                  break;
               case 100:
                  if (C_ac.m_b_9b79() && C_bq.m_a_134632(128)) {
                     try {
                        C_ac.f_ac_a_240bf0.m_a_cb4b0023(new C_cd(new byte[0]));
                     } catch (C_ar var2) {
                        C_ar.m_a_aef55300(var2);
                        if (var2.f_ar_a_5a) {
                           this.cancel();
                        }
                     }
                  }

                  System.gc();
                  if (f_cm_a_4a != C_cf.m_a_25deca9(false, true)) {
                     C_cl.m_a_9b75();
                     return;
                  }
                  break;
               case 200:
                  if (C_ac.m_b_9b79() && C_bq.m_a_134632(153)) {
                     switch (C_ac.m_c_9b68()) {
                        case 0:
                        case 2:
                        case 16:
                        case 32:
                        case 8193:
                        case 12288:
                        case 16384:
                        case 20480:
                        case 24576:
                           f_cm_b_49 = (int)C_bq.a$134622();
                           p_cm_a_13462e(1);
                           C_bp.f_bp_a_5a = true;
                           return;
                        case 1:
                           if (C_bp.f_bp_a_5a) {
                              p_cm_a_13462e(4);
                              C_bp.f_bp_a_5a = true;
                           }
                        default:
                           return;
                     }
                  }

                  f_cm_a_f6e8e442.cancel();
            }
         }
      }
   }

   public static void m_a_9b75() {
      if (f_cm_a_f6e8e442 != null) {
         f_cm_a_f6e8e442.cancel();
         f_cm_a_f6e8e442 = null;
      }

      (f_cm_a_f6e8e442 = new Timer()).schedule(new C_cm(200), f_cm_a_49, f_cm_a_49);
      int var0;
      if (C_bp.f_bp_a_5a && C_bq.m_a_134632(147) && ((var0 = (int)C_bq.a$134622()) == 1 || var0 == 4)) {
         p_cm_a_13462e(f_cm_b_49);
      }
   }

   private static void p_cm_a_13462e(int var0) {
      try {
         C_bq.m_a_255f656(192, var0);
         C_bq.m_c_9b75();
         C_ac.m_a_13462e(var0);
         var0 = C_bi.m_a_1349e2(C_ac.m_c_9b68());
         C_w.f_w_a_129a3.m_a_48a013c6(C_w.f_w_a_12a00.m_a_485a59b9(var0));
         C_cp.m_a_13462e(var0);
      } catch (C_ar var1) {
         C_ar.m_a_aef55300(var1);
      }
   }

   private boolean p_cm_b_9b79() {
      boolean var1 = false;
      if (this.f_cm_a_5f790d9c instanceof C_bf) {
         var1 = C_bi.m_a_cb3dd164((C_bf)this.f_cm_a_5f790d9c);
      } else if (this.f_cm_a_5f790d9c instanceof Displayable) {
         var1 = ((Displayable)this.f_cm_a_5f790d9c).isShown();
      }

      if (!var1) {
         C_bi.m_a_75ba1f9b(this.f_cm_a_5f790d9c, this.f_cm_b_523beb0a);
         this.cancel();
         this.f_cm_a_5f790d9c = null;
      }

      return !var1;
   }

   public final void m_b_9b75() {
      C_bi.m_a_75ba1f9b(this.f_cm_a_5f790d9c, this.f_cm_b_523beb0a);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == C_cp.f_cp_a_1570d10e) {
         this.f_cm_a_240810.m_a_13462e(2);
         this.cancel();
      }
   }
}
