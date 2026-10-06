package recovered;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public final class C_ck extends TimerTask implements CommandListener {
   private static int f_ck_b_49 = (int)C_bp.a$134622();
   public static int f_ck_a_49 = C_bp.m_a_134621(109) * 60000;
   private static Timer f_ck_a_f6e8e442;
   private int f_ck_c_49 = -1;
   private C_az f_ck_a_240756;
   private boolean f_ck_a_5a = false;
   private boolean f_ck_b_5a = false;
   private Object f_ck_a_5f790d9c;
   private String f_ck_a_523beb0a;
   private String f_ck_b_523beb0a;
   private int f_ck_d_49;
   public static long f_ck_a_4a = C_ce.m_a_25deca9(false, true);

   public C_ck(C_az var1) {
      this.f_ck_a_240756 = var1;
   }

   public C_ck(int var1) {
      this.f_ck_c_49 = var1;
   }

   public C_ck(Object var1, String var2, int var3, int var4) {
      this.f_ck_a_5f790d9c = var1;
      this.f_ck_a_523beb0a = var2;
      this.f_ck_b_523beb0a = C_bg.m_a_69d680af(var1);
      this.f_ck_d_49 = var4 == 4 ? var3 : 0;
      this.f_ck_c_49 = var4;
   }

   public final boolean cancel() {
      this.f_ck_b_5a = true;
      return super.cancel();
   }

   public final boolean m_a_9b79() {
      return this.f_ck_b_5a;
   }

   public final int m_a_9b68() {
      return this.f_ck_c_49;
   }

   public final void run() {
      if (!this.f_ck_a_5a) {
         if (this.f_ck_c_49 == -1) {
            String var1;
            if ((var1 = this.f_ck_a_240756.m_a_73cf11cb()) != null) {
               C_cn.m_a_aad3b1ff(var1);
            }

            C_cn.m_b_13462e(this.f_ck_a_240756.m_a_9b68());
            if (this.f_ck_a_240756.m_a_9b79()) {
               this.cancel();
               this.f_ck_a_240756.m_a_13462e(1);
            } else {
               if (this.f_ck_a_240756.m_b_9b79()) {
                  this.f_ck_a_5a = true;
                  this.cancel();
                  this.f_ck_a_240756.m_a_13462e(3);
               }
            }
         } else {
            switch (this.f_ck_c_49) {
               case 1:
                  C_cn.m_b_9b75();
                  return;
               case 2:
                  C_cn.f_cn_a_5a = false;
                  C_cn.m_b_9b75();
                  return;
               case 3:
                  C_cn.m_a_aad3b1ff(C_bs.m_a_e96ea081("j3"));
                  C_cn.m_a_13462e(C_bg.m_a_1349e2(C_ac.m_c_9b68()));
                  C_cn.m_a_48a013c6(C_ac.m_a_2477940());
                  C_cn.m_b_9b75();
                  return;
               case 4:
                  if (this.f_ck_d_49 == 0) {
                     C_bg.m_a_75ba1f9b(this.f_ck_a_5f790d9c, this.f_ck_b_523beb0a);
                     this.cancel();
                     this.f_ck_a_5f790d9c = null;
                     return;
                  }

                  if (this.p_ck_b_9b79()) {
                     return;
                  }

                  C_bg.m_a_75ba1f9b(this.f_ck_a_5f790d9c, (this.f_ck_d_49 & 1) == 0 ? this.f_ck_a_523beb0a : " ");
                  this.f_ck_d_49--;
                  return;
               case 5:
                  if (this.p_ck_b_9b79()) {
                     return;
                  }

                  C_bg.m_a_75ba1f9b(this.f_ck_a_5f790d9c, this.f_ck_a_523beb0a.substring(this.f_ck_d_49));
                  this.f_ck_d_49++;
                  if (this.f_ck_d_49 > this.f_ck_a_523beb0a.length() - 5) {
                     this.f_ck_d_49 = 0;
                     return;
                  }
                  break;
               case 100:
                  if (C_ac.m_b_9b79() && C_bp.m_a_134632(128)) {
                     try {
                        C_ac.f_ac_a_240b17.m_a_cb4a1765(new C_cb(new byte[0]));
                     } catch (C_aq var2) {
                        C_aq.m_a_481c933f(var2);
                        if (var2.f_aq_a_5a) {
                           this.cancel();
                        }
                     }
                  }

                  System.gc();
                  if (f_ck_a_4a != C_ce.m_a_25deca9(false, true)) {
                     C_ci.m_a_9b75();
                     return;
                  }
                  break;
               case 200:
                  if (C_ac.m_b_9b79() && C_bp.m_a_134632(153)) {
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
                           f_ck_b_49 = (int)C_bp.a$134622();
                           p_ck_a_13462e(1);
                           C_bo.f_bo_a_5a = true;
                           return;
                        case 1:
                           if (C_bo.f_bo_a_5a) {
                              p_ck_a_13462e(4);
                              C_bo.f_bo_a_5a = true;
                           }
                        default:
                           return;
                     }
                  }

                  f_ck_a_f6e8e442.cancel();
            }
         }
      }
   }

   public static void m_a_9b75() {
      if (f_ck_a_f6e8e442 != null) {
         f_ck_a_f6e8e442.cancel();
         f_ck_a_f6e8e442 = null;
      }

      (f_ck_a_f6e8e442 = new Timer()).schedule(new C_ck(200), f_ck_a_49, f_ck_a_49);
      int var0;
      if (C_bo.f_bo_a_5a && C_bp.m_a_134632(147) && ((var0 = (int)C_bp.a$134622()) == 1 || var0 == 4)) {
         p_ck_a_13462e(f_ck_b_49);
      }
   }

   private static void p_ck_a_13462e(int var0) {
      try {
         C_bp.m_a_255f656(192, var0);
         C_bp.m_c_9b75();
         C_ac.m_a_13462e(var0);
         var0 = C_bg.m_a_1349e2(C_ac.m_c_9b68());
         C_w.f_w_a_12984.m_a_48a013c6(C_w.f_w_a_12a1f.m_a_485a59b9(var0));
         C_cn.m_a_13462e(var0);
      } catch (C_aq var1) {
         C_aq.m_a_481c933f(var1);
      }
   }

   private boolean p_ck_b_9b79() {
      boolean var1 = false;
      if (this.f_ck_a_5f790d9c instanceof C_be) {
         var1 = C_bg.m_a_cb3d5d05((C_be)this.f_ck_a_5f790d9c);
      } else if (this.f_ck_a_5f790d9c instanceof Displayable) {
         var1 = ((Displayable)this.f_ck_a_5f790d9c).isShown();
      }

      if (!var1) {
         C_bg.m_a_75ba1f9b(this.f_ck_a_5f790d9c, this.f_ck_b_523beb0a);
         this.cancel();
         this.f_ck_a_5f790d9c = null;
      }

      return !var1;
   }

   public final void m_b_9b75() {
      C_bg.m_a_75ba1f9b(this.f_ck_a_5f790d9c, this.f_ck_b_523beb0a);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == C_cn.f_cn_a_1570d10e) {
         this.f_ck_a_240756.m_a_13462e(2);
         this.cancel();
      }
   }
}
