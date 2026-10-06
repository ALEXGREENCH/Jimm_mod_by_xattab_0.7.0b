package recovered;

/** 0.6 source correspondence (inferred): DrawControls.VirtualList. Release class: bf. */

import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jimm.Jimm;

public abstract class C_bf {
   private static C_by f_bf_a_240af8 = new C_by();
   private static Image f_bf_a_9b93e07e;
   private Font f_bf_a_67f45fce;
   private Font f_bf_b_67f45fce;
   private Font f_bf_c_67f45fce;
   public static final int f_bf_a_49 = 4;
   private static Font f_bf_d_67f45fce = Font.getFont(64, 1 + C_bq.f_bq_g_49, 8);
   private C_ay f_bf_a_240737;
   private String f_bf_a_523beb0a;
   private boolean f_bf_b_5a = false;
   private C_g f_bf_a_12a00 = null;
   protected int f_bf_b_49 = 0;
   private boolean f_bf_c_5a = false;
   private static C_ca f_bf_a_240bd1 = new C_ca();
   private int f_bf_j_49 = 0;
   private int f_bf_k_49 = 0;
   private boolean f_bf_d_5a = false;
   private C_f f_bf_a_129e1;
   private C_f f_bf_b_129e1;
   private C_f f_bf_c_129e1;
   private C_f f_bf_d_129e1;
   private C_f f_bf_e_129e1;
   private static int f_bf_l_49;
   private static int f_bf_m_49;
   private static int f_bf_n_49;
   protected int f_bf_c_49 = 1;
   private int f_bf_o_49 = 0;
   private int f_bf_p_49 = 8;
   private int f_bf_q_49 = 16777215;
   private int f_bf_r_49 = 0;
   private int f_bf_s_49 = 12632256;
   private int f_bf_t_49 = 0;
   private int f_bf_u_49 = 2;
   private static int f_bf_v_49;
   private static int f_bf_w_49;
   public static boolean f_bf_a_5a = false;
   private CommandListener f_bf_a_a2d63fba;
   private static long f_bf_a_4a = 0L;
   private static int f_bf_x_49 = -1;
   private static int f_bf_y_49 = -1;
   private int f_bf_z_49;
   private int f_bf_A_49;
   private int f_bf_B_49;
   private int f_bf_C_49;
   private static int f_bf_D_49 = -1;
   private static int f_bf_E_49 = -1;
   private static int[] f_bf_a_b4e = null;
   public static int f_bf_d_49;
   private static int f_bf_F_49;
   private static int f_bf_G_49;
   private static Image f_bf_b_9b93e07e = null;
   private int f_bf_H_49 = -1;
   private int f_bf_I_49 = -1;
   public static int f_bf_e_49;
   public static int f_bf_f_49;
   public static int f_bf_g_49;
   public static int f_bf_h_49;
   public int f_bf_i_49;
   private static Font f_bf_e_67f45fce = Font.getFont(64, 1 + C_bq.f_bq_g_49, 8);
   private static Font f_bf_f_67f45fce = Font.getFont(64, C_bq.f_bq_g_49, 8);
   private Command f_bf_a_1570d10e;
   private Command f_bf_b_1570d10e;
   private Vector f_bf_a_48a69a2c = new Vector();
   private Vector f_bf_b_48a69a2c = new Vector();
   private boolean f_bf_e_5a = true;
   private boolean f_bf_f_5a = true;

   public static void m_c_9b75() {
      f_bf_l_49 = Jimm.f_jimm_Jimm_d_5a ? C_bq.m_a_134621(95) + 2 : 2;
      f_bf_m_49 = Jimm.f_jimm_Jimm_b_5a ? C_bq.m_a_134621(96) + 2 : 2;
   }

   public static void m_a_297a162c(Display var0) {
      f_bf_a_240af8.f_by_a_4a58c677 = var0;
   }

   public final void m_b_1385ff(boolean var1) {
      if (this.f_bf_d_5a != var1) {
         this.f_bf_d_5a = var1;
         if (this.m_b_9b79()) {
            f_bf_a_240af8.repaint();
         }
      }
   }

   public static void m_c_1385ff(boolean var0) {
      if (f_bf_a_240af8.f_by_a_2408ab != null) {
         f_bf_a_240af8.f_by_a_2408ab.m_b_1385ff(var0);
      }
   }

   public C_bf(String var1) {
      this.m_c_aad3b1ff(var1);
      this.p_bf_a_9b75();
      this.f_bf_u_49 = 2;
   }

   protected abstract int m_a_9b68();

   protected abstract void m_a_d826493f(int var1, C_ca var2);

   final Font m_a_a729dba6(int var1) {
      switch (var1) {
         case 0:
            return this.f_bf_a_67f45fce;
         case 1:
            return this.f_bf_b_67f45fce;
         case 2:
            return this.f_bf_c_67f45fce;
         default:
            return Font.getFont(0, var1, this.f_bf_p_49);
      }
   }

   private int p_bf_b_9b68() {
      return this.p_bf_m_9b68() - this.p_bf_l_9b68() - this.p_bf_n_9b68();
   }

   public final void m_b_13462e(int var1) {
      if (this.f_bf_p_49 != var1) {
         this.f_bf_p_49 = var1;
         this.p_bf_a_9b75();
         this.m_g_9b75();
         this.m_f_9b75();
      }
   }

   public final void d$1385ff() {
      this.f_bf_c_5a = true;
   }

   public static int m_b_134621(int var0) {
      return f_bf_a_240af8.getGameAction(var0);
   }

   public final void m_d_9b75() {
      if (this.m_b_9b79()) {
         f_bf_a_240af8.repaint();
      }
   }

   public final void m_a_48a013c6(C_f var1) {
      if (this.f_bf_a_129e1 != var1) {
         this.f_bf_a_129e1 = var1;
         this.m_f_9b75();
      }
   }

   public final void m_b_48a013c6(C_f var1) {
      if (this.f_bf_b_129e1 != var1) {
         this.f_bf_b_129e1 = var1;
         this.m_f_9b75();
      }
   }

   public final void m_c_48a013c6(C_f var1) {
      if (this.f_bf_c_129e1 != var1) {
         this.f_bf_c_129e1 = var1;
         this.m_f_9b75();
      }
   }

   public final void m_d_48a013c6(C_f var1) {
      if (this.f_bf_d_129e1 != var1) {
         this.f_bf_d_129e1 = var1;
         this.m_f_9b75();
      }
   }

   public final void m_e_48a013c6(C_f var1) {
      if (this.f_bf_e_129e1 != var1) {
         this.f_bf_e_129e1 = var1;
         this.m_f_9b75();
      }
   }

   public final void m_a_cb385cec(C_ay var1) {
      this.f_bf_a_240737 = var1;
   }

   public static C_bf m_a_46a7a52c() {
      return f_bf_a_240af8.isShown() ? f_bf_a_240af8.f_by_a_2408ab : null;
   }

   public void m_a_c4b201b5(int var1, int var2, int var3, int var4) {
      this.f_bf_s_49 = var2;
      this.f_bf_t_49 = var1;
      this.f_bf_q_49 = var3;
      this.f_bf_r_49 = var4;
      if (this.m_b_9b79()) {
         f_bf_a_240af8.repaint();
      }
   }

   private void p_bf_a_9b75() {
      this.f_bf_a_67f45fce = Font.getFont(0, 0, this.f_bf_p_49);
      this.f_bf_b_67f45fce = Font.getFont(0, 1, this.f_bf_p_49);
      this.f_bf_c_67f45fce = Font.getFont(0, 2, this.f_bf_p_49);
   }

   public final int m_c_9b68() {
      return this.f_bf_p_49;
   }

   public final int m_d_9b68() {
      return this.f_bf_r_49;
   }

   public final int m_e_9b68() {
      int var1 = this.m_a_9b68();
      int var2 = 0;
      int var3 = 0;
      int var5 = this.p_bf_b_9b68();
      int var4 = this.f_bf_o_49;
      if (var1 == 0) {
         return 0;
      } else {
         if (var4 < 0) {
            var4 = 0;
         }

         if (var4 >= var1) {
            var4 = var1 - 1;
         }

         while (var4 < var1 - 1) {
            if ((var2 += this.m_a_134621(var4)) > var5) {
               return var3;
            }

            var3++;
            var4++;
         }

         var2 = var5;
         int var7 = 0;

         for (int var8 = var1 - 1; var8 >= 0 && (var2 -= this.m_a_134621(var8)) >= 0; var8--) {
            var7++;
         }

         return var7;
      }
   }

   public final void c$13462e() {
      if (this.f_bf_u_49 != 3) {
         this.f_bf_u_49 = 3;
         this.m_f_9b75();
      }
   }

   public final int m_f_9b68() {
      return this.f_bf_u_49;
   }

   public final boolean m_b_9b79() {
      return f_bf_a_240af8.f_by_a_2408ab == this && f_bf_a_240af8.isShown();
   }

   public final void m_b_297a162c(Display var1) {
      if (!this.m_b_9b79()) {
         f_bf_a_240af8.f_by_a_2408ab = this;
         f_bf_a_240af8.m_a_9b75();
         var1.setCurrent(f_bf_a_240af8);
         this.m_d_9b75();
      }
   }

   public final void m_a_75ca2789(Display var1, Alert var2) {
      f_bf_a_240af8.f_by_a_2408ab = this;
      f_bf_a_240af8.m_a_9b75();
      var1.setCurrent(var2, f_bf_a_240af8);
      this.m_d_9b75();
   }

   protected final void m_e_9b75() {
      f_bf_a_240af8.setCommandListener(this.f_bf_a_a2d63fba);
      this.f_bf_I_49 = this.f_bf_H_49 = -1;
      this.f_bf_i_49 = 0;
   }

   public int m_a_134621(int var1) {
      int var3 = 0;
      int var2 = this.m_h_9b68();
      if (this.f_bf_a_12a00 != null) {
         var3 = this.f_bf_a_12a00.m_b_9b68();
      }

      return var2 > var3 ? var2 : var3;
   }

   public final void m_f_9b75() {
      if (!this.f_bf_b_5a) {
         if (this.m_b_9b79()) {
            f_bf_a_240af8.repaint();
         }
      }
   }

   public final void m_a_48a08825(C_g var1) {
      this.f_bf_a_12a00 = var1;
      this.m_f_9b75();
   }

   protected final void m_d_13462e(int var1) {
      int var2 = this.m_a_9b68();
      if (this.f_bf_c_5a) {
         switch (var1) {
            case -1:
               if (this.f_bf_b_49 < 0) {
                  this.f_bf_b_49 = var2 - 1;
                  return;
               }
               break;
            case 1:
               if (this.f_bf_b_49 >= var2) {
                  this.f_bf_b_49 = 0;
                  return;
               }
               break;
            default:
               if (this.f_bf_b_49 < 0) {
                  this.f_bf_b_49 = 0;
               }

               if (this.f_bf_b_49 >= var2) {
                  this.f_bf_b_49 = var2 - 1;
               }

               return;
         }
      } else {
         if (this.f_bf_b_49 < 0) {
            this.f_bf_b_49 = 0;
         }

         if (this.f_bf_b_49 >= var2) {
            this.f_bf_b_49 = var2 - 1;
         }
      }
   }

   protected final void m_g_9b75() {
      int var1 = this.m_a_9b68();
      int var2 = this.m_e_9b68();
      if (var1 == 0) {
         this.f_bf_o_49 = 0;
      } else {
         if (this.f_bf_b_49 >= this.f_bf_o_49 + var2 - 1) {
            this.f_bf_o_49 = this.f_bf_b_49 - var2 + 1;
         }

         if (this.f_bf_b_49 < this.f_bf_o_49) {
            this.f_bf_o_49 = this.f_bf_b_49;
         }

         if (var1 - this.f_bf_o_49 <= var2) {
            this.f_bf_o_49 = var1 > var2 ? var1 - var2 : 0;
         }

         if (this.f_bf_o_49 < 0) {
            this.f_bf_o_49 = 0;
         }
      }
   }

   protected final boolean m_c_134632(int var1) {
      return var1 >= this.f_bf_o_49 && var1 <= this.f_bf_o_49 + this.m_e_9b68();
   }

   protected final void m_h_9b75() {
      this.f_bf_j_49 = this.f_bf_b_49;
      this.f_bf_k_49 = this.f_bf_o_49;
   }

   protected final void m_i_9b75() {
      if (this.f_bf_j_49 != this.f_bf_b_49 || this.f_bf_k_49 != this.f_bf_o_49) {
         this.m_f_9b75();
      }

      if (this.f_bf_j_49 != this.f_bf_b_49 && this.f_bf_a_240737 != null) {
         this.f_bf_a_240737.m_a_cb3dd160(this);
      }
   }

   public void m_a_2563266(int var1, boolean var2) {
      this.m_h_9b75();
      if (var2 && this.f_bf_u_49 == 3) {
         this.f_bf_o_49 += var1;
      }

      this.f_bf_b_49 += var1;
      this.m_d_13462e(var1);
      this.m_g_9b75();
      this.m_i_9b75();
   }

   public final void m_e_1385ff(boolean var1) {
      int delta = var1 ? this.m_e_9b68() : -this.m_e_9b68();
      this.m_a_2563266(delta, false);
   }

   protected boolean m_a_9b79() {
      return this.m_a_8eb9d707(this.m_a_c667a636(4));
   }

   private Vector p_bf_a_6a39c0ed() {
      Vector var1 = null;
      if (this.f_bf_a_1570d10e != null) {
         if (this.f_bf_a_48a69a2c.size() == 0) {
            C_w.f_w_a_5a = true;
            if (this.m_a_8eb9d707(this.f_bf_a_1570d10e)) {
               return null;
            }
         } else if (this.f_bf_i_49 == 1) {
            this.f_bf_i_49 = 0;
         } else {
            if (!this.f_bf_e_5a) {
               p_bf_b_b14f20a1(this.f_bf_a_48a69a2c);
               this.f_bf_e_5a = true;
            }

            this.f_bf_i_49 = 1;
            var1 = this.f_bf_a_48a69a2c;
         }
      }

      return var1;
   }

   private Vector p_bf_b_6a39c0ed() {
      Vector var1 = null;
      if (this.f_bf_b_1570d10e != null) {
         if (this.f_bf_b_48a69a2c.size() == 0) {
            C_w.f_w_a_5a = true;
            if (this.m_a_8eb9d707(this.f_bf_b_1570d10e)) {
               return null;
            }
         } else if (this.f_bf_i_49 == 2) {
            this.f_bf_i_49 = 0;
         } else {
            if (!this.f_bf_f_5a) {
               p_bf_b_b14f20a1(this.f_bf_b_48a69a2c);
               this.f_bf_f_5a = true;
            }

            this.f_bf_i_49 = 2;
            var1 = this.f_bf_b_48a69a2c;
         }
      }

      return var1;
   }

   private void p_bf_a_b14f20a1(Vector var1) {
      if (var1 != null) {
         f_bf_n_49 = 0;
         int var4;
         int var2 = p_bf_e_134621(var4 = var1.size());
         int var3 = this.p_bf_b_9b68();
         if (var2 > var3) {
            f_bf_v_49 = var3 / f_bf_f_67f45fce.getHeight();
            f_bf_w_49 = var4 - f_bf_v_49;
         } else {
            f_bf_v_49 = var4;
            f_bf_w_49 = 0;
         }
      }
   }

   private static void p_bf_a_486912df(int var0, int var1, boolean var2) {
      if (!var2) {
         if ((f_bf_n_49 += var0) >= var1) {
            f_bf_n_49 = 0;
         }

         if (f_bf_n_49 < 0) {
            f_bf_n_49 = var1 - 1;
         }

         if (f_bf_n_49 >= f_bf_w_49 + f_bf_v_49) {
            f_bf_w_49 = f_bf_n_49 - f_bf_v_49 + 1;
         }

         if (f_bf_n_49 < f_bf_w_49) {
            f_bf_w_49 = f_bf_n_49;
            return;
         }
      } else {
         if ((f_bf_w_49 += var0) < 0) {
            f_bf_w_49 = 0;
         }

         if (f_bf_w_49 >= var1 - f_bf_v_49) {
            f_bf_w_49 = var1 - f_bf_v_49;
         }
      }
   }

   private void p_bf_c_255f295(int var1, int var2) {
      boolean var3 = false;
      int var4 = f_bf_n_49;
      Vector var5 = null;
      Vector var6 = null;
      switch (this.f_bf_i_49) {
         case 1:
            var3 = true;
            var5 = this.f_bf_a_48a69a2c;
            break;
         case 2:
            var3 = true;
            var5 = this.f_bf_b_48a69a2c;
      }

      int var7 = this.f_bf_i_49;
      switch (p_bf_d_134621(var1)) {
         case 1:
         case 1610547263:
            if (var3) {
               p_bf_a_486912df(-1, var5.size(), false);
            } else if (!f_bf_a_5a) {
               this.m_a_2563266(-1, false);
            }
            break;
         case 6:
         case 1610547265:
            if (var3) {
               p_bf_a_486912df(1, var5.size(), false);
            } else if (!f_bf_a_5a) {
               this.m_a_2563266(1, false);
            }
            break;
         case 8:
            if (var2 == 1) {
               if (var3) {
                  this.f_bf_i_49 = 0;
                  this.m_a_8eb9d707((Command)var5.elementAt(f_bf_n_49));
                  this.m_f_9b75();
               } else if (!f_bf_a_5a) {
                  C_w.f_w_a_5a = false;
                  boolean var10000 = this.m_a_9b79();
                  boolean var9 = false;
                  if (!var10000 && this.f_bf_a_240737 != null) {
                     this.f_bf_a_240737.m_b_cb3dd160(this);
                  }
               }
            }
            break;
         case 1000001:
            if (var2 == 1) {
               var6 = this.p_bf_a_6a39c0ed();
            }
            break;
         case 1000002:
            if (var2 == 1) {
               var6 = this.p_bf_b_6a39c0ed();
            }
            break;
         case 1000003:
            if (var2 == 1) {
               switch (this.f_bf_i_49) {
                  case 1:
                  case 2:
                     this.f_bf_i_49 = 0;
                     this.m_f_9b75();
                     break;
                  default:
                     Command var8;
                     if ((var8 = this.m_a_c667a636(2)) != null && this.m_a_8eb9d707(var8)) {
                        return;
                     }
               }
            }
      }

      this.p_bf_a_b14f20a1(var6);
      if ((!var3 || var4 == f_bf_n_49) && var7 == this.f_bf_i_49) {
         if (var2 == 1 && !f_bf_a_5a) {
            switch (var1) {
               case 49:
                  this.m_h_9b75();
                  this.f_bf_b_49 = this.f_bf_o_49 = 0;
                  this.m_i_9b75();
                  return;
               case 50:
               case 52:
               case 53:
               case 54:
               case 56:
               default:
                  break;
               case 51:
                  this.m_a_2563266(-this.m_e_9b68(), false);
                  return;
               case 55:
                  this.m_h_9b75();
                  int var10 = this.m_a_9b68() - 1;
                  this.f_bf_b_49 = var10;
                  this.m_g_9b75();
                  this.m_i_9b75();
                  return;
               case 57:
                  this.m_a_2563266(this.m_e_9b68(), false);
            }
         }
      } else {
         this.m_f_9b75();
      }
   }

   private void p_bf_d_255f295(int var1, int var2) {
      switch (var2) {
         case 1:
            C_bu.m_a_9b75();
            if (C_bq.m_a_134632(153)) {
               C_cm.m_a_9b75();
            }

            this.p_bf_c_255f295(var1, var2);
            break;
         case 2:
            this.p_bf_c_255f295(var1, var2);
      }

      if (this.f_bf_a_240737 != null) {
         this.f_bf_a_240737.m_a_f31d59c0(this, var1, var2);
      }
   }

   protected final void m_e_13462e(int var1) {
      this.p_bf_d_255f295(var1, 1) ;
   }

   protected final void m_f_13462e(int var1) {
      this.p_bf_d_255f295(var1, 2) ;
   }

   public final void m_a_6f63a2af(CommandListener var1) {
      this.f_bf_a_a2d63fba = var1;
      if (this.m_b_9b79()) {
         f_bf_a_240af8.setCommandListener(this.f_bf_a_a2d63fba);
      }
   }

   protected final boolean m_a_8eb9d707(Command var1) {
      if (this.f_bf_a_a2d63fba != null && var1 != null) {
         this.f_bf_a_a2d63fba.commandAction(var1, null);
         return true;
      } else {
         return false;
      }
   }

   private static int p_bf_d_134621(int var0) {
      String var1 = null;

      try {
         var1 = f_bf_a_240af8.getKeyName(var0).toLowerCase();
      } catch (IllegalArgumentException var3) {
      }

      if (var1 != null) {
         if ("soft1".equals(var1)
            || "soft 1".equals(var1)
            || "soft_1".equals(var1)
            || "softkey 1".equals(var1)
            || "sk2(left)".equals(var1)
            || var1.startsWith("left soft")) {
            return 1000001;
         }

         if ("soft2".equals(var1)
            || "soft 2".equals(var1)
            || "soft_2".equals(var1)
            || "softkey 4".equals(var1)
            || "sk1(right)".equals(var1)
            || var1.startsWith("right soft")) {
            return 1000002;
         }

         if ("on/off".equals(var1)) {
            return 1000003;
         }
      }

      switch (var0) {
         case -203:
         case -7:
         case 22:
         case 106:
         case 112:
         case 57346:
            return 1000002;
         case -202:
         case -6:
         case 21:
         case 105:
         case 113:
         case 57345:
            return 1000001;
         case -11:
            return 1000003;
         default:
            try {
               int var4;
               if ((var4 = f_bf_a_240af8.getGameAction(var0)) > 0) {
                  return var4;
               }
            } catch (Exception var2) {
            }

            return var0;
      }
   }

   protected final void m_g_13462e(int var1) {
      if (f_bf_y_49 != -1) {
         int var2 = this.p_bf_m_9b68() - this.p_bf_l_9b68();
         int var3 = this.m_a_9b68();
         int var4 = this.m_e_9b68();
         if (var3 != var4) {
            this.m_h_9b75();
            this.f_bf_o_49 = f_bf_y_49 + var3 * (var1 - f_bf_x_49) / var2;
            if (this.f_bf_o_49 < 0) {
               this.f_bf_o_49 = 0;
            }

            if (this.f_bf_o_49 > var3 - var4) {
               this.f_bf_o_49 = var3 - var4;
            }

            this.m_i_9b75();
         }
      }
   }

   protected boolean m_a_134632(int var1) {
      return false;
   }

   private int p_bf_k_9b68() {
      return this.m_i_9b68() - 5 * f_bf_a_49;
   }

   protected final void m_a_255f295(int var1, int var2) {
      if (var2 < this.p_bf_l_9b68()) {
         if (C_w.f_w_a_129a3.m_b_9b79()) {
            if (var1 < this.f_bf_z_49) {
               C_bp.m_c_9b75();
            } else if (var1 < this.f_bf_A_49) {
               C_bp.m_d_9b75();
            } else if (var1 < this.f_bf_B_49) {
               C_bp.m_e_9b75();
            } else {
               if (this.f_bf_C_49 != 0 && var1 > this.f_bf_C_49) {
                  C_w.m_a_138603(true);
               }
            }
         }
      } else {
         C_w.f_w_a_5a = false;
         if (var1 >= this.p_bf_k_9b68() && var2 < this.p_bf_m_9b68() - this.p_bf_n_9b68()) {
            f_bf_x_49 = var2;
            f_bf_y_49 = this.f_bf_o_49;
         } else {
            f_bf_y_49 = -1;
            f_bf_a_4a = System.currentTimeMillis();
            if (f_bf_b_9b93e07e == null) {
               f_bf_b_9b93e07e = Image.createImage(this.m_i_9b68(), this.p_bf_m_9b68());
            }

            this.p_bf_a_26c5358c(f_bf_b_9b93e07e.getGraphics(), 2, var1, var2);
            f_bf_x_49 = var2;
         }
      }
   }

   protected final void m_b_255f295(int var1, int var2) {
      if (System.currentTimeMillis() - f_bf_a_4a > 250L && var1 < this.p_bf_k_9b68()) {
         this.p_bf_a_26c5358c(f_bf_b_9b93e07e.getGraphics(), 3, var1, var2);
      }
   }

   public final void m_c_aad3b1ff(String var1) {
      if (this.f_bf_a_523beb0a == null || !this.f_bf_a_523beb0a.equals(var1)) {
         this.f_bf_a_523beb0a = var1;
         this.m_f_9b75();
      }
   }

   public final String m_b_73cf11cb() {
      return this.f_bf_a_523beb0a;
   }

   public final void m_h_13462e(int var1) {
      this.m_h_9b75();
      this.f_bf_b_49 = this.f_bf_o_49 = var1;
      this.m_g_9b75();
      this.m_i_9b75();
   }

   public final void m_i_13462e(int var1) {
      this.m_h_9b75();
      this.f_bf_b_49 = var1;
      this.m_g_9b75();
      this.m_i_9b75();
   }

   public final int m_g_9b68() {
      return this.f_bf_b_49;
   }

   private int p_bf_l_9b68() {
      int var1 = 0;
      if (this.f_bf_a_523beb0a != null) {
         var1 = f_bf_d_67f45fce.getHeight() + 2;
      }

      C_f var2;
      int var3;
      if (this.f_bf_a_129e1 != null && (var3 = (var2 = this.f_bf_a_129e1).f_f_d_49 + 1) > var1) {
         var1 = var3;
      }

      return var1;
   }

   protected boolean m_b_134632(int var1) {
      return this.f_bf_b_49 == var1 && this.f_bf_u_49 != 3;
   }

   public static void m_a_b329a056(InputStream var0, boolean var1) {
      if (var0 == null && var1) {
         f_bf_a_9b93e07e = null;
      } else {
         try {
            f_bf_a_9b93e07e = Image.createImage(C_bq.m_a_47921032(34));
         } catch (Exception var3) {
            try {
               f_bf_a_9b93e07e = Image.createImage(var0);
            } catch (Exception var2) {
               f_bf_a_9b93e07e = null;
            }
         }
      }
   }

   public static void m_a_64ddb84c(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var1 >> 16 & 0xFF;
      int var9 = var1 >> 8 & 0xFF;
      int var10 = var1 & 0xFF;
      int var11 = var2 >> 16 & 0xFF;
      int var12 = var2 >> 8 & 0xFF;
      int var13 = var2 & 0xFF;
      if (var7 == 255) {
         if ((var7 = (var6 - var4) / 3) < 0) {
            var7 = -var7;
         }

         if (var7 < 8) {
            var7 = 8;
         }

         int var14 = 0;
         int var15 = 0;

         for (int var16 = 0; var16 < var7; var16++) {
            var14 = var16 * (var6 - var4) / var7 + var4;
            var15 = (var16 + 1) * (var6 - var4) / var7 + var4;
            if (var14 != var15) {
               var0.setColor(
                  var16 * (var11 - var8) / (var7 - 1) + var8, var16 * (var12 - var9) / (var7 - 1) + var9, var16 * (var13 - var10) / (var7 - 1) + var10
               );
               var0.fillRect(var3, var14, var5 - var3, var15 - var14);
            }
         }
      } else {
         int var22 = var7 << 24;
         int var24 = var5 - var3;
         int var26 = var6 - var4;
         if (var24 <= 0 || var26 <= 0) {
            return;
         }

         int var27 = var26 << 5;
         if (f_bf_a_b4e == null || f_bf_a_b4e.length < var27) {
            f_bf_a_b4e = null;
            f_bf_a_b4e = new int[var27];
         }

         if (f_bf_d_49 != var26 || f_bf_F_49 != var1 || f_bf_G_49 != var2) {
            int var28 = 0;
            int var17 = 0;
            int var18 = 0;
            int var19 = 0;

            for (int var20 = 0; var20 < var26; var20++) {
               var17 = var20 * (var6 - var4) / var26;
               var18 = (var20 + 1) * (var6 - var4) / var26;
               if (var17 != var18) {
                  var17 = var20 * (var11 - var8) / (var26 - 1) + var8;
                  var18 = var20 * (var12 - var9) / (var26 - 1) + var9;
                  var19 = var20 * (var13 - var10) / (var26 - 1) + var10;
                  int var32 = var17 << 16 | var18 << 8 | var19 | var22;

                  for (int var36 = 0; var36 < 32; var36++) {
                     f_bf_a_b4e[var28++] = var32;
                  }
               }
            }

            f_bf_d_49 = var26;
            f_bf_F_49 = var1;
            f_bf_G_49 = var2;
         }

         int var29 = var24;

         for (int var33 = var3; var33 < var5; var33 += 32) {
            var0.drawRGB(f_bf_a_b4e, 0, 32, var33, var4, var29 > 32 ? 32 : var29, var26, true);
            var29 -= 32;
         }
      }
   }

   public final int m_h_9b68() {
      return this.m_a_a729dba6(0).getHeight();
   }

   private boolean p_bf_a_8a5c62f0(Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = -1;
      int var8 = -1;
      int var9 = this.p_bf_b_9b68();
      int var10 = this.m_a_9b68();
      int var12 = this.m_i_9b68() - f_bf_a_49;
      int var13 = var2 + var9;
      if (var4 == 1) {
         int var14 = f_bf_a_240af8.getWidth();
         int var15 = f_bf_a_240af8.getHeight();
         var1.setColor(this.f_bf_q_49);
         var1.fillRect(0, 0, var14, f_bf_a_240af8.getHeight());
         if (f_bf_a_9b93e07e != null) {
            var1.drawImage(f_bf_a_9b93e07e, var14 / 2, var15 / 2, 3);
         }

         int var11 = var2;
         int var16 = 0;

         for (int var19 = this.f_bf_o_49; var19 < var10; var19++) {
            var16 = this.m_a_134621(var19);
            if (this.m_b_134632(var19)) {
               if (var7 == -1) {
                  var7 = var11;
               }

               var8 = var11 + var16 - 1;
            }

            if ((var11 += var16) >= var13) {
               break;
            }
         }

         if (var7 != -1) {
            if (C_bq.f_bq_d_49 > 10) {
               m_a_64ddb84c(var1, m_a_255f288(C_bq.f_bq_c_49, -32), C_bq.f_bq_c_49, 1, var7 + 1, var12 - 1, var8, C_bq.f_bq_d_49);
            } else {
               var1.setStrokeStyle(1);
            }

            var1.setColor(m_a_255f288(C_bq.f_bq_c_49, -48));
            boolean var10000 = this.f_bf_o_49 >= 1 ? this.m_b_134632(this.f_bf_o_49 - 1) : false;
            boolean var17 = false;
            if (!var10000) {
               var1.drawLine(1, var7, var12 - 2, var7);
            }

            var1.drawLine(0, var7 + 1, 0, var8 - 1);
            var1.drawLine(var12 - 1, var7 + 1, var12 - 1, var8 - 1);
            var1.drawLine(1, var8, var12 - 2, var8);
         }
      }

      f_bf_a_240bd1.m_a_9b75();
      int var21 = var2;
      int var22 = 0;
      int var24 = 0;
      int var27 = 0;
      boolean var29 = false;

      for (int var20 = this.f_bf_o_49; var20 < var10; var20++) {
         var22 = this.m_a_134621(var20);
         var1.setStrokeStyle(0);
         var24 = this.f_bf_c_49;
         var27 = var12 - 2;
         var2 = var21 + var22;
         if (var4 == 1) {
            this.m_a_c1306d97(var1, var20, var24, var21, var27, var2, var3);
         } else if (var21 < var6 && var6 < var2 && var24 < var5 && var5 < var27) {
            switch (var4) {
               case 2:
                  if (this.f_bf_b_49 != var20) {
                     this.f_bf_b_49 = var20;
                     if (this.f_bf_a_240737 != null) {
                        this.f_bf_a_240737.m_a_cb3dd160(this);
                     }

                     this.m_f_9b75();
                  }
                  break;
               case 3:
                  this.m_a_9b79();
            }

            this.m_a_134632(var5 - var24);
            return true;
         }

         if ((var21 += var22) >= var13) {
            break;
         }
      }

      return false;
   }

   public static int m_a_255f288(int var0, int var1) {
      int var2 = (var0 & 0xFF) + var1;
      int var3 = (var0 >> 8 & 0xFF) + var1;
      int var4 = (var0 >> 16 & 0xFF) + var1;
      if (var2 < 0) {
         var2 = 0;
      }

      if (var2 > 255) {
         var2 = 255;
      }

      if (var3 < 0) {
         var3 = 0;
      }

      if (var3 > 255) {
         var3 = 255;
      }

      if (var4 < 0) {
         var4 = 0;
      }

      if (var4 > 255) {
         var4 = 255;
      }

      return var2 | var3 << 8 | var4 << 16;
   }

   public static int m_c_134621(int var0) {
      int var1 = var0 & 0xFF;
      int var2 = var0 >> 8 & 0xFF;
      var0 = var0 >> 16 & 0xFF;
      return var1 + var2 + var0 > 381 ? 0 : 16777215;
   }

   private void p_bf_a_26c5358c(Graphics var1, int var2, int var3, int var4) {
      int var5 = this.m_e_9b68();
      int var6 = this.p_bf_n_9b68();
      int var7 = this.p_bf_l_9b68();
      switch (var2) {
         case 1:
            this.p_bf_a_8a5c62f0(var1, var7, this.m_h_9b68(), var2, var3, var4);
            int var11 = this.m_i_9b68() - f_bf_a_49;
            int var10 = this.p_bf_m_9b68() - var6;
            int var12;
            boolean var13 = (var12 = this.m_a_9b68()) > var5 && var12 > 0;
            int var14;
            if ((var14 = m_a_255f288(m_a_255f288(this.f_bf_q_49, 32), -32)) == 0) {
               var14 = 8421504;
            }

            var1.setStrokeStyle(0);
            var1.setColor(m_a_255f288(var14, -96));
            var1.drawLine(var11, var7, var11, var10 - 1);
            var1.setColor(m_a_255f288(var14, -64));
            var1.drawLine(var11 + 1, var7, var11 + 1, var10 - 1);
            var1.setColor(m_a_255f288(var14, -32));
            var1.drawLine(var11 + 2, var7, var11 + 2, var10 - 1);
            var1.setColor(var14);
            var1.drawLine(var11 + 3, var7, var11 + 3, var10 - 1);
            if (var13) {
               int thumb;
               if ((thumb = (var10 - var7) * var5 / var12) < 7) {
                  thumb = 7;
               }

               f_bf_E_49 = (f_bf_D_49 = this.f_bf_o_49 * (var10 - thumb - var7) / (var12 - var5) + var7) + thumb;
               var1.setColor(m_a_255f288(var14, -192));
               var1.drawRect(var11, f_bf_D_49, f_bf_a_49 - 1, f_bf_E_49 - f_bf_D_49 - 1);
               var1.setColor(m_a_255f288(this.f_bf_s_49, 96));
               var1.drawLine(var11 + 1, f_bf_D_49 + 1, var11 + 1, f_bf_E_49 - 2);
               var1.drawLine(var11 + 1, f_bf_D_49 + 1, var11 + f_bf_a_49 - 2, f_bf_D_49 + 1);
               var1.setColor(this.f_bf_s_49);
               var1.drawLine(var11 + 2, f_bf_D_49 + 2, var11 + 2, f_bf_E_49 - 2);
            }

            if (var6 != 0) {
               this.p_bf_a_b1e1eefb(var1, var6, var2, var3, var4);
            }

            this.p_bf_b_b1e1eefb(var1, var6, var2, var3, var4);
            break;
         case 2:
         case 3:
            if (var6 != 0 && this.p_bf_a_b1e1eefb(var1, var6, var2, var3, var4)) {
               return;
            }

            if (this.p_bf_b_b1e1eefb(var1, var6, var2, var3, var4)) {
               return;
            }

            if (this.p_bf_a_8a5c62f0(var1, var7, this.m_h_9b68(), var2, var3, var4)) {
               return;
            }
      }

      if (this.f_bf_a_523beb0a != null) {
         if (var2 != 1) {
            this.p_bf_l_9b68();
            return;
         }

         this.f_bf_z_49 = 0;
         this.f_bf_A_49 = 0;
         this.f_bf_B_49 = 0;
         this.f_bf_C_49 = 0;
         int var9 = this.m_i_9b68();
         var1.setFont(f_bf_d_67f45fce);
         int var21 = this.p_bf_l_9b68();
         m_a_64ddb84c(var1, this.f_bf_s_49, m_a_255f288(this.f_bf_s_49, -48), 0, 0, var9, var21, C_bq.f_bq_e_49);
         if (C_bq.f_bq_e_49 > 170) {
            var1.setColor(m_a_255f288(this.f_bf_s_49, -128));
            var1.drawLine(0, var21 - 1, var9, var21 - 1);
         }

         int var22 = f_bf_l_49;
         if (this.f_bf_a_129e1 != null) {
            this.f_bf_a_129e1.m_b_11c44857(var1, var22, var21 / 2);
            C_f var15;
            var22 += (var15 = this.f_bf_a_129e1).f_f_c_49 + 1;
            this.f_bf_z_49 = var22;
         }

         if (C_bq.m_a_134632(176) && this.f_bf_b_129e1 != null && this.f_bf_b_129e1 != C_x.m_a_485a59b9(37)) {
            this.f_bf_b_129e1.m_b_11c44857(var1, var22, var21 / 2);
            C_f var16 = this.f_bf_b_129e1;
            var22 += this.f_bf_b_129e1.f_f_c_49 + 1;
            this.f_bf_A_49 = var22;
         }

         if (C_bq.m_a_134632(177) && this.f_bf_c_129e1 != null) {
            this.f_bf_c_129e1.m_b_11c44857(var1, var22, var21 / 2);
            C_f var17 = this.f_bf_c_129e1;
            var22 += this.f_bf_c_129e1.f_f_c_49 + 1;
            this.f_bf_B_49 = var22;
         }

         if (C_bq.m_a_134632(180) && this.f_bf_d_129e1 != null) {
            this.f_bf_d_129e1.m_b_11c44857(var1, var22, var21 / 2);
            C_f var18 = this.f_bf_d_129e1;
            var22 += this.f_bf_d_129e1.f_f_c_49 + 1;
         }

         var1.setColor(m_c_134621(this.f_bf_s_49));
         if (this.f_bf_a_129e1 != null) {
            var1.drawString(this.f_bf_a_523beb0a, var22, (var21 - f_bf_d_67f45fce.getHeight()) / 2, 20);
         } else {
            var1.drawString(this.f_bf_a_523beb0a, var9 / 2, (var21 - f_bf_d_67f45fce.getHeight()) / 2, 17);
         }

         if (C_bq.m_a_134632(178) && this.f_bf_e_129e1 != null) {
            var22 = var9 - f_bf_m_49;
            this.f_bf_e_129e1.m_c_11c44857(var1, var22, var21 / 2);
            C_f var19 = this.f_bf_e_129e1;
            this.f_bf_C_49 = var22 - this.f_bf_e_129e1.f_f_c_49;
         }
      }
   }

   protected final void m_a_272d79b7(Graphics var1) {
      if (!this.f_bf_b_5a) {
         if (f_bf_a_240af8.isDoubleBuffered()) {
            this.p_bf_a_26c5358c(var1, 1, -1, -1);
         } else {
            try {
               if (f_bf_b_9b93e07e == null) {
                  f_bf_b_9b93e07e = Image.createImage(this.m_i_9b68(), this.p_bf_m_9b68());
               }

               this.p_bf_a_26c5358c(f_bf_b_9b93e07e.getGraphics(), 1, -1, -1);
               var1.drawImage(f_bf_b_9b93e07e, 0, 0, 20);
            } catch (Exception var2) {
               this.p_bf_a_26c5358c(var1, 1, -1, -1);
            }
         }
      }
   }

   protected void m_a_c1306d97(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      f_bf_a_240bd1.m_a_9b75();
      this.m_a_d826493f(var2, f_bf_a_240bd1);
      int var8 = f_bf_a_240bd1.f_ca_c_49 + var3 + 1;
      if (f_bf_a_240bd1.f_ca_a_129e1 != null) {
         f_bf_a_240bd1.f_ca_a_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var9 = f_bf_a_240bd1.f_ca_a_129e1;
         var8 += f_bf_a_240bd1.f_ca_a_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_b_129e1 != null) {
         f_bf_a_240bd1.f_ca_b_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var10 = f_bf_a_240bd1.f_ca_b_129e1;
         var8 += f_bf_a_240bd1.f_ca_b_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_c_129e1 != null) {
         f_bf_a_240bd1.f_ca_c_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var11 = f_bf_a_240bd1.f_ca_c_129e1;
         var8 += f_bf_a_240bd1.f_ca_c_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_d_129e1 != null) {
         f_bf_a_240bd1.f_ca_d_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var12 = f_bf_a_240bd1.f_ca_d_129e1;
         var8 += f_bf_a_240bd1.f_ca_d_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_a_523beb0a != null) {
         var1.setFont(this.m_a_a729dba6(f_bf_a_240bd1.f_ca_a_49));
         var1.setColor(f_bf_a_240bd1.f_ca_b_49);
         var1.drawString(f_bf_a_240bd1.f_ca_a_523beb0a, var8, (var4 + var6 - var7) / 2, 20);
      }

      int var13 = f_bf_a_240af8.getWidth() - f_bf_a_49 - 2;
      if (f_bf_a_240bd1.f_ca_h_129e1 != null) {
         f_bf_a_240bd1.f_ca_h_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var14 = f_bf_a_240bd1.f_ca_h_129e1;
         var13 -= f_bf_a_240bd1.f_ca_h_129e1.f_f_c_49 + 2;
      }

      if (f_bf_a_240bd1.f_ca_g_129e1 != null) {
         f_bf_a_240bd1.f_ca_g_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var15 = f_bf_a_240bd1.f_ca_g_129e1;
         var13 -= f_bf_a_240bd1.f_ca_g_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_f_129e1 != null) {
         f_bf_a_240bd1.f_ca_f_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var16 = f_bf_a_240bd1.f_ca_f_129e1;
         var13 -= f_bf_a_240bd1.f_ca_f_129e1.f_f_c_49 + 1;
      }

      if (f_bf_a_240bd1.f_ca_e_129e1 != null) {
         f_bf_a_240bd1.f_ca_e_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
      }
   }

   public final void m_j_9b75() {
      this.f_bf_b_5a = true;
   }

   protected void m_b_9b75() {
   }

   public final void m_k_9b75() {
      this.f_bf_b_5a = false;
      this.m_b_9b75();
      this.m_f_9b75();
   }

   protected final boolean m_c_9b79() {
      return this.f_bf_b_5a;
   }

   private int p_bf_m_9b68() {
      return this.f_bf_I_49 == -1 ? f_bf_a_240af8.getHeight() : this.f_bf_I_49;
   }

   protected final int m_i_9b68() {
      return this.f_bf_H_49 == -1 ? f_bf_a_240af8.getWidth() : this.f_bf_H_49;
   }

   public static int m_j_9b68() {
      return f_bf_a_240af8.getWidth();
   }

   public static void m_l_9b75() {
      if (C_bq.m_a_134632(143)) {
         f_bf_e_49 = 2;
         f_bf_f_49 = 1;
         f_bf_g_49 = 4;
         f_bf_h_49 = 3;
      } else {
         f_bf_e_49 = 1;
         f_bf_f_49 = 2;
         f_bf_g_49 = 3;
         f_bf_h_49 = 4;
      }
   }

   private boolean p_bf_a_b1e1eefb(Graphics var1, int var2, int var3, int var4, int var5) {
      int var6 = this.p_bf_m_9b68() - var2;
      int var7 = this.p_bf_m_9b68();
      int var8 = this.m_i_9b68();
      int var10 = var2 / 4;
      if (var3 != 3 && !this.f_bf_d_5a) {
         if (var3 == 1) {
            m_a_64ddb84c(var1, m_a_255f288(this.f_bf_s_49, -48), this.f_bf_s_49, 0, var6, var8, var7, C_bq.f_bq_f_49);
         }

         var1.setFont(f_bf_e_67f45fce);
         int var9 = (var6 + var7 - f_bf_e_67f45fce.getHeight()) / 2 + 1;
         var1.setColor(m_c_134621(this.f_bf_s_49));
         if (this.f_bf_a_1570d10e != null) {
            if (var3 == 2 && p_bf_a_6046c8d9(var4, var5, 0, var6, this.m_i_9b68() / 2, var7)) {
               Vector var13 = this.p_bf_a_6a39c0ed();
               this.p_bf_a_b14f20a1(var13);
               this.m_f_9b75();
               return true;
            }

            var1.drawString(this.f_bf_a_1570d10e.getLabel(), var10, var9, 20);
         }

         if (this.f_bf_b_1570d10e != null) {
            if (var3 == 2 && p_bf_a_6046c8d9(var4, var5, this.m_i_9b68() / 2, var6, this.m_i_9b68(), var7)) {
               Vector var12 = this.p_bf_b_6a39c0ed();
               this.p_bf_a_b14f20a1(var12);
               this.m_f_9b75();
               return true;
            }

            var1.drawString(this.f_bf_b_1570d10e.getLabel(), var8 - var10, var9, 24);
         }

         if (C_bq.m_a_134632(173)) {
            var1.drawString(C_cf.m_a_2416688b(true, false), var8 / 2, var9, 17);
         } else {
            var2 = var8 / 2 - 2;
            var3 = (var6 + var7) / 2;
            var1.drawLine(var2 - 3, var3 - 1, var2, var3 + 2);
            var1.drawLine(var2 - 3, var3, var2, var3 + 3);
            var1.drawLine(var2 - 3, var3 + 1, var2, var3 + 4);
            var1.drawLine(var2 - 3, var3 + 2, var2, var3 + 5);
            var1.drawLine(var2, var3 + 2, var2 + 6, var3 - 4);
            var1.drawLine(var2, var3 + 3, var2 + 6, var3 - 3);
            var1.drawLine(var2, var3 + 4, var2 + 6, var3 - 2);
            var1.drawLine(var2, var3 + 5, var2 + 6, var3 - 1);
         }

         if (C_bq.f_bq_f_49 > 170) {
            var1.setColor(m_a_255f288(this.f_bf_s_49, -128));
            var1.drawLine(0, var6, var8, var6);
         }

         return false;
      } else {
         return false;
      }
   }

   protected final Command m_a_c667a636(int var1) {
      if (this.f_bf_a_1570d10e != null && this.f_bf_a_1570d10e.getCommandType() == var1) {
         return this.f_bf_a_1570d10e;
      } else if (this.f_bf_b_1570d10e != null && this.f_bf_b_1570d10e.getCommandType() == var1) {
         return this.f_bf_b_1570d10e;
      } else {
         for (int var2 = this.f_bf_a_48a69a2c.size() - 1; var2 >= 0; var2--) {
            Command var3;
            if ((var3 = (Command)this.f_bf_a_48a69a2c.elementAt(var2)).getCommandType() == var1) {
               return var3;
            }
         }

         for (int var4 = this.f_bf_b_48a69a2c.size() - 1; var4 >= 0; var4--) {
            Command var5;
            if ((var5 = (Command)this.f_bf_b_48a69a2c.elementAt(var4)).getCommandType() == var1) {
               return var5;
            }
         }

         return null;
      }
   }

   private int p_bf_n_9b68() {
      return this.f_bf_d_5a ? 0 : f_bf_e_67f45fce.getHeight() + 2;
   }

   public final void m_a_48817c60(Command var1, int var2) {
      switch (var2) {
         case 1:
            this.f_bf_a_1570d10e = var1;
            this.m_f_9b75();
            return;
         case 2:
            this.f_bf_b_1570d10e = var1;
            this.m_f_9b75();
            return;
         case 3:
            if (this.f_bf_a_48a69a2c.indexOf(var1) == -1) {
               this.f_bf_a_48a69a2c.addElement(var1);
               this.f_bf_e_5a = false;
               return;
            }
            break;
         case 4:
            if (this.f_bf_b_48a69a2c.indexOf(var1) == -1) {
               this.f_bf_b_48a69a2c.addElement(var1);
               this.f_bf_f_5a = false;
            }
      }
   }

   public final void m_a_8eb9d703(Command var1) {
      if (var1 == this.f_bf_a_1570d10e) {
         this.f_bf_a_1570d10e = null;
         this.f_bf_a_48a69a2c.removeAllElements();
         this.m_f_9b75();
      } else if (var1 == this.f_bf_b_1570d10e) {
         this.f_bf_b_1570d10e = null;
         this.f_bf_b_48a69a2c.removeAllElements();
         this.m_f_9b75();
      } else {
         this.f_bf_a_48a69a2c.removeElement(var1);
         this.f_bf_b_48a69a2c.removeElement(var1);
      }
   }

   public final void m_m_9b75() {
      this.f_bf_a_1570d10e = null;
      this.f_bf_b_1570d10e = null;
      this.f_bf_a_48a69a2c.removeAllElements();
      this.f_bf_b_48a69a2c.removeAllElements();
   }

   private boolean p_bf_b_b1e1eefb(Graphics var1, int var2, int var3, int var4, int var5) {
      switch (this.f_bf_i_49) {
         case 1:
            return this.p_bf_a_da40b5c4(var1, this.f_bf_a_48a69a2c, this.p_bf_m_9b68() - var2, 4, var3, var4, var5);
         case 2:
            return this.p_bf_a_da40b5c4(var1, this.f_bf_b_48a69a2c, this.p_bf_m_9b68() - var2, 8, var3, var4, var5);
         default:
            return false;
      }
   }

   private static int p_bf_e_134621(int var0) {
      return f_bf_f_67f45fce.getHeight() * var0 + 4;
   }

   private static boolean p_bf_a_6046c8d9(int var0, int var1, int var2, int var3, int var4, int var5) {
      return var2 <= var0 && var0 < var4 && var3 <= var1 && var1 < var5;
   }

   private boolean p_bf_a_a45f35b0(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      switch (var6) {
         case 1:
            var1.setColor(this.f_bf_r_49);
            boolean var11 = false;
            int var13 = (var3 + var5 - 2) / 2;

            for (int var14 = -1; var14 <= 1; var14++) {
               int var12 = (var2 + var4) / 2 - var14 * 5;
               var1.fillRect(var12, var13, 2, 2);
            }
            break;
         case 2:
         case 3:
            if (p_bf_a_6046c8d9(var7, var8, var2, var3, var4, var5)) {
               p_bf_a_486912df(var9, var10, true);
               this.m_f_9b75();
               return true;
            }
      }

      return false;
   }

   private boolean p_bf_a_da40b5c4(Graphics var1, Vector var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      int var9 = (var8 = f_bf_f_67f45fce.getHeight()) / 4;
      int var10 = var2.size();
      int var11 = 0;
      int var12 = p_bf_e_134621(f_bf_v_49);

      for (int var13 = 0; var13 < var10; var13++) {
         Command var14 = (Command)var2.elementAt(var13);
         int var15;
         if ((var15 = f_bf_f_67f45fce.stringWidth(var14.getLabel())) > var11) {
            var11 = var15;
         }
      }

      if ((var11 = var11 + (var9 << 1)) > f_bf_a_240af8.getWidth() - 4) {
         var11 = f_bf_a_240af8.getWidth() - 4;
      }

      int var22 = var3 - var12 - 1;
      int var23 = 0;
      switch (var4) {
         case 8:
            var23 = this.m_i_9b68() - var11 - 1;
         default:
            if (var5 == 1) {
               m_a_64ddb84c(var1, m_a_255f288(this.f_bf_q_49, -32), this.f_bf_q_49, var23, var22, var23 + var11, var22 + var12, 255);
            }

            if (f_bf_w_49 != 0) {
               boolean var10000 = this.p_bf_a_a45f35b0(var1, var23, var22, var23 + var11, var22 + 3, var5, var6, var7, -1, var10);
               boolean var24 = false;
               if (var10000) {
                  return true;
               }
            }

            if (f_bf_w_49 + f_bf_v_49 != var10) {
               boolean var28 = this.p_bf_a_a45f35b0(var1, var23, var22 + var12 - 3, var23 + var11, var22 + var12, var5, var6, var7, 1, var10);
               boolean var25 = false;
               if (var28) {
                  return true;
               }
            }

            var1.setFont(f_bf_f_67f45fce);
            int var26 = var22 + 3;
            int var16 = f_bf_w_49;

            for (int var18 = 0; var18 < f_bf_v_49; var18++) {
               if (var16 == f_bf_n_49 && var5 == 1) {
                  m_a_64ddb84c(var1, m_a_255f288(C_bq.f_bq_c_49, -32), C_bq.f_bq_c_49, var23 + 1, var26 - 1, var23 + var11, var26 + var8, 255);
                  var1.setColor(m_a_255f288(C_bq.f_bq_c_49, -48));
                  var1.drawRect(var23 + 1, var26 - 1, var11 - 2, var8);
               }

               var26 += var8;
               var16++;
            }

            int var27 = var22 + 3;
            int var17 = f_bf_w_49;

            for (int var19 = 0; var19 < f_bf_v_49; var19++) {
               Command var20 = (Command)var2.elementAt(var17);
               switch (var5) {
                  case 1:
                     var1.setColor(var17 == f_bf_n_49 ? m_c_134621(C_bq.f_bq_c_49) : this.f_bf_t_49);
                     var1.drawString(var20.getLabel(), var23 + var9, var27, 20);
                     break;
                  case 2:
                     if (p_bf_a_6046c8d9(var6, var7, var23, var27, var23 + var11, var27 + var8)) {
                        this.f_bf_i_49 = 0;
                        this.m_f_9b75();
                        this.m_a_8eb9d707(var20);
                        return true;
                     }
               }

               var27 += var8;
               var17++;
            }

            if (var5 == 1) {
               var1.setColor(this.f_bf_r_49);
               var1.drawRect(var23, var22, var11, var12);
            }

            return false;
      }
   }

   private static void p_bf_b_b14f20a1(Vector var0) {
      int var1 = var0.size() - 1;

      boolean var2;
      do {
         var2 = false;

         for (int var3 = 0; var3 < var1; var3++) {
            Command var4 = (Command)var0.elementAt(var3 + 1);
            Command var5 = (Command)var0.elementAt(var3);
            if (var4.getPriority() < var5.getPriority()) {
               var0.setElementAt(var4, var3);
               var0.setElementAt(var5, var3 + 1);
               var2 = true;
            }
         }
      } while (var2);
   }

   static {
      m_c_9b75();
      m_l_9b75();
   }
}
