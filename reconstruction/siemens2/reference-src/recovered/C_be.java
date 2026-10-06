package recovered;

import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class C_be {
   private static C_bx f_be_a_240ad9 = new C_bx();
   private static Image f_be_a_9b93e07e;
   private Font f_be_a_67f45fce;
   private Font f_be_b_67f45fce;
   private Font f_be_c_67f45fce;
   public static final int f_be_a_49 = 4;
   private static Font f_be_d_67f45fce = Font.getFont(64, 1 + C_bp.f_bp_g_49, 8);
   private C_ay f_be_a_240737;
   private String f_be_a_523beb0a;
   private boolean f_be_b_5a = false;
   private C_h f_be_a_12a1f = null;
   protected int f_be_b_49 = 0;
   private boolean f_be_c_5a = false;
   private static C_bw f_be_a_240aba = new C_bw();
   private int f_be_j_49 = 0;
   private int f_be_k_49 = 0;
   private boolean f_be_d_5a = false;
   private C_f f_be_a_129e1;
   private C_f f_be_b_129e1;
   private C_f f_be_c_129e1;
   private C_f f_be_d_129e1;
   private C_f f_be_e_129e1;
   private static int f_be_l_49 = 2;
   private static int f_be_m_49 = 2;
   private static int f_be_n_49;
   protected int f_be_c_49 = 1;
   private int f_be_o_49 = 0;
   private int f_be_p_49 = 8;
   private int f_be_q_49 = 16777215;
   private int f_be_r_49 = 0;
   private int f_be_s_49 = 12632256;
   private int f_be_t_49 = 0;
   private int f_be_u_49 = 2;
   private static int f_be_v_49;
   private static int f_be_w_49;
   public static boolean f_be_a_5a = false;
   private CommandListener f_be_a_a2d63fba;
   private static int f_be_x_49 = -1;
   private static int f_be_y_49 = -1;
   private static int[] f_be_a_b4e = null;
   public static int f_be_d_49;
   private static int f_be_z_49;
   private static int f_be_A_49;
   private static Image f_be_b_9b93e07e = null;
   private int f_be_B_49 = -1;
   private int f_be_C_49 = -1;
   public static int f_be_e_49;
   public static int f_be_f_49;
   public static int f_be_g_49;
   public static int f_be_h_49;
   public int f_be_i_49;
   private static Font f_be_e_67f45fce = Font.getFont(64, 1 + C_bp.f_bp_g_49, 8);
   private static Font f_be_f_67f45fce = Font.getFont(64, C_bp.f_bp_g_49, 8);
   private Command f_be_a_1570d10e;
   private Command f_be_b_1570d10e;
   private Vector f_be_a_48a69a2c = new Vector();
   private Vector f_be_b_48a69a2c = new Vector();
   private boolean f_be_e_5a = true;
   private boolean f_be_f_5a = true;

   public static void m_a_297a162c(Display var0) {
      f_be_a_240ad9.f_bx_a_4a58c677 = var0;
   }

   public final void m_b_1385ff(boolean var1) {
      if (this.f_be_d_5a != var1) {
         this.f_be_d_5a = var1;
         if (this.m_b_9b79()) {
            f_be_a_240ad9.repaint();
         }
      }
   }

   public static void m_c_1385ff(boolean var0) {
      if (f_be_a_240ad9.f_bx_a_24088c != null) {
         f_be_a_240ad9.f_bx_a_24088c.m_b_1385ff(var0);
      }
   }

   public C_be(String var1) {
      this.m_c_aad3b1ff(var1);
      this.p_be_a_9b75();
      this.f_be_u_49 = 2;
   }

   protected abstract int m_a_9b68();

   protected abstract void m_a_d82231e8(int var1, C_bw var2);

   final Font m_a_a729dba6(int var1) {
      switch (var1) {
         case 0:
            return this.f_be_a_67f45fce;
         case 1:
            return this.f_be_b_67f45fce;
         case 2:
            return this.f_be_c_67f45fce;
         default:
            return Font.getFont(0, var1, this.f_be_p_49);
      }
   }

   private int p_be_b_9b68() {
      return this.p_be_l_9b68() - this.p_be_k_9b68() - this.p_be_m_9b68();
   }

   public final void m_b_13462e(int var1) {
      if (this.f_be_p_49 != var1) {
         this.f_be_p_49 = var1;
         this.p_be_a_9b75();
         this.m_f_9b75();
         this.m_e_9b75();
      }
   }

   public final void d$1385ff() {
      this.f_be_c_5a = true;
   }

   public static int m_b_134621(int var0) {
      return f_be_a_240ad9.getGameAction(var0);
   }

   public final void m_c_9b75() {
      if (this.m_b_9b79()) {
         f_be_a_240ad9.repaint();
      }
   }

   public final void m_a_48a013c6(C_f var1) {
      if (this.f_be_a_129e1 != var1) {
         this.f_be_a_129e1 = var1;
         this.m_e_9b75();
      }
   }

   public final void m_b_48a013c6(C_f var1) {
      if (this.f_be_b_129e1 != var1) {
         this.f_be_b_129e1 = var1;
         this.m_e_9b75();
      }
   }

   public final void m_c_48a013c6(C_f var1) {
      if (this.f_be_c_129e1 != var1) {
         this.f_be_c_129e1 = var1;
         this.m_e_9b75();
      }
   }

   public final void m_d_48a013c6(C_f var1) {
      if (this.f_be_d_129e1 != var1) {
         this.f_be_d_129e1 = var1;
         this.m_e_9b75();
      }
   }

   public final void m_e_48a013c6(C_f var1) {
      if (this.f_be_e_129e1 != var1) {
         this.f_be_e_129e1 = var1;
         this.m_e_9b75();
      }
   }

   public final void m_a_cb385cec(C_ay var1) {
      this.f_be_a_240737 = var1;
   }

   public static C_be m_a_46a7a50d() {
      return f_be_a_240ad9.isShown() ? f_be_a_240ad9.f_bx_a_24088c : null;
   }

   public void m_a_c4b201b5(int var1, int var2, int var3, int var4) {
      this.f_be_s_49 = var2;
      this.f_be_t_49 = var1;
      this.f_be_q_49 = var3;
      this.f_be_r_49 = var4;
      if (this.m_b_9b79()) {
         f_be_a_240ad9.repaint();
      }
   }

   private void p_be_a_9b75() {
      this.f_be_a_67f45fce = Font.getFont(0, 0, this.f_be_p_49);
      this.f_be_b_67f45fce = Font.getFont(0, 1, this.f_be_p_49);
      this.f_be_c_67f45fce = Font.getFont(0, 2, this.f_be_p_49);
   }

   public final int m_c_9b68() {
      return this.f_be_p_49;
   }

   public final int m_d_9b68() {
      return this.f_be_r_49;
   }

   public final int m_e_9b68() {
      int var1 = this.m_a_9b68();
      int var2 = 0;
      int var3 = 0;
      int var5 = this.p_be_b_9b68();
      int var4 = this.f_be_o_49;
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
      if (this.f_be_u_49 != 3) {
         this.f_be_u_49 = 3;
         this.m_e_9b75();
      }
   }

   public final int m_f_9b68() {
      return this.f_be_u_49;
   }

   public final boolean m_b_9b79() {
      return f_be_a_240ad9.f_bx_a_24088c == this && f_be_a_240ad9.isShown();
   }

   public final void m_b_297a162c(Display var1) {
      if (!this.m_b_9b79()) {
         f_be_a_240ad9.f_bx_a_24088c = this;
         f_be_a_240ad9.m_a_9b75();
         var1.setCurrent(f_be_a_240ad9);
         this.m_c_9b75();
      }
   }

   public final void m_a_75ca2789(Display var1, Alert var2) {
      f_be_a_240ad9.f_bx_a_24088c = this;
      f_be_a_240ad9.m_a_9b75();
      var1.setCurrent(var2, f_be_a_240ad9);
      this.m_c_9b75();
   }

   protected final void m_d_9b75() {
      f_be_a_240ad9.setCommandListener(this.f_be_a_a2d63fba);
      this.f_be_C_49 = this.f_be_B_49 = -1;
      this.f_be_i_49 = 0;
   }

   public int m_a_134621(int var1) {
      int var3 = 0;
      int var2 = this.m_h_9b68();
      if (this.f_be_a_12a1f != null) {
         var3 = this.f_be_a_12a1f.m_b_9b68();
      }

      return var2 > var3 ? var2 : var3;
   }

   public final void m_e_9b75() {
      if (!this.f_be_b_5a) {
         if (this.m_b_9b79()) {
            f_be_a_240ad9.repaint();
         }
      }
   }

   public final void m_a_48a0fc84(C_h var1) {
      this.f_be_a_12a1f = var1;
      this.m_e_9b75();
   }

   protected final void m_d_13462e(int var1) {
      int var2 = this.m_a_9b68();
      if (this.f_be_c_5a) {
         switch (var1) {
            case -1:
               if (this.f_be_b_49 < 0) {
                  this.f_be_b_49 = var2 - 1;
                  return;
               }
               break;
            case 1:
               if (this.f_be_b_49 >= var2) {
                  this.f_be_b_49 = 0;
                  return;
               }
               break;
            default:
               if (this.f_be_b_49 < 0) {
                  this.f_be_b_49 = 0;
               }

               if (this.f_be_b_49 >= var2) {
                  this.f_be_b_49 = var2 - 1;
               }

               return;
         }
      } else {
         if (this.f_be_b_49 < 0) {
            this.f_be_b_49 = 0;
         }

         if (this.f_be_b_49 >= var2) {
            this.f_be_b_49 = var2 - 1;
         }
      }
   }

   protected final void m_f_9b75() {
      int var1 = this.m_a_9b68();
      int var2 = this.m_e_9b68();
      if (var1 == 0) {
         this.f_be_o_49 = 0;
      } else {
         if (this.f_be_b_49 >= this.f_be_o_49 + var2 - 1) {
            this.f_be_o_49 = this.f_be_b_49 - var2 + 1;
         }

         if (this.f_be_b_49 < this.f_be_o_49) {
            this.f_be_o_49 = this.f_be_b_49;
         }

         if (var1 - this.f_be_o_49 <= var2) {
            this.f_be_o_49 = var1 > var2 ? var1 - var2 : 0;
         }

         if (this.f_be_o_49 < 0) {
            this.f_be_o_49 = 0;
         }
      }
   }

   protected final boolean m_b_134632(int var1) {
      return var1 >= this.f_be_o_49 && var1 <= this.f_be_o_49 + this.m_e_9b68();
   }

   protected final void m_g_9b75() {
      this.f_be_j_49 = this.f_be_b_49;
      this.f_be_k_49 = this.f_be_o_49;
   }

   protected final void m_h_9b75() {
      if (this.f_be_j_49 != this.f_be_b_49 || this.f_be_k_49 != this.f_be_o_49) {
         this.m_e_9b75();
      }

      if (this.f_be_j_49 != this.f_be_b_49 && this.f_be_a_240737 != null) {
         this.f_be_a_240737.m_a_cb3d5d01(this);
      }
   }

   public void m_a_2563266(int var1, boolean var2) {
      this.m_g_9b75();
      if (var2 && this.f_be_u_49 == 3) {
         this.f_be_o_49 += var1;
      }

      this.f_be_b_49 += var1;
      this.m_d_13462e(var1);
      this.m_f_9b75();
      this.m_h_9b75();
   }

   public final void m_e_1385ff(boolean var1) {
      var1 = var1 ? this.m_e_9b68() : -this.m_e_9b68();
      this.m_a_2563266(var1, false);
   }

   protected boolean m_a_9b79() {
      return this.m_a_8eb9d707(this.m_a_c667a636(4));
   }

   private static void a$486912df(int var0, int var1) {
      if ((f_be_n_49 += var0) >= var1) {
         f_be_n_49 = 0;
      }

      if (f_be_n_49 < 0) {
         f_be_n_49 = var1 - 1;
      }

      if (f_be_n_49 >= f_be_w_49 + f_be_v_49) {
         f_be_w_49 = f_be_n_49 - f_be_v_49 + 1;
      }

      if (f_be_n_49 < f_be_w_49) {
         f_be_w_49 = f_be_n_49;
      }
   }

   private void p_be_a_255f295(int var1, int var2) {
      boolean var3 = false;
      int var4 = f_be_n_49;
      Vector var5 = null;
      Vector var6 = null;
      switch (this.f_be_i_49) {
         case 1:
            var3 = true;
            var5 = this.f_be_a_48a69a2c;
            break;
         case 2:
            var3 = true;
            var5 = this.f_be_b_48a69a2c;
      }

      int var7 = this.f_be_i_49;
      switch (p_be_d_134621(var1)) {
         case 1:
         case 1610547263:
            if (var3) {
               a$486912df(-1, var5.size());
            } else if (!f_be_a_5a) {
               this.m_a_2563266(-1, false);
            }
            break;
         case 6:
         case 1610547265:
            if (var3) {
               a$486912df(1, var5.size());
            } else if (!f_be_a_5a) {
               this.m_a_2563266(1, false);
            }
            break;
         case 8:
            if (var2 == 1) {
               if (var3) {
                  this.f_be_i_49 = 0;
                  this.m_a_8eb9d707((Command)var5.elementAt(f_be_n_49));
                  this.m_e_9b75();
               } else if (!f_be_a_5a) {
                  C_w.f_w_a_5a = false;
                  boolean var17 = this.m_a_9b79();
                  boolean var10 = false;
                  if (!var17 && this.f_be_a_240737 != null) {
                     this.f_be_a_240737.m_b_cb3d5d01(this);
                  }
               }
            }
            break;
         case 1000001:
            if (var2 == 1) {
               Vector var16;
               label115: {
                  Vector var14 = null;
                  if (this.f_be_a_1570d10e != null) {
                     if (this.f_be_a_48a69a2c.size() == 0) {
                        C_w.f_w_a_5a = true;
                        if (this.m_a_8eb9d707(this.f_be_a_1570d10e)) {
                           var16 = null;
                           break label115;
                        }
                     } else if (this.f_be_i_49 == 1) {
                        this.f_be_i_49 = 0;
                     } else {
                        if (!this.f_be_e_5a) {
                           p_be_a_b14f20a1(this.f_be_a_48a69a2c);
                           this.f_be_e_5a = true;
                        }

                        this.f_be_i_49 = 1;
                        var14 = this.f_be_a_48a69a2c;
                     }
                  }

                  var16 = var14;
               }

               var6 = var16;
            }
            break;
         case 1000002:
            if (var2 == 1) {
               Vector var10000;
               label108: {
                  Vector var13 = null;
                  if (this.f_be_b_1570d10e != null) {
                     if (this.f_be_b_48a69a2c.size() == 0) {
                        C_w.f_w_a_5a = true;
                        if (this.m_a_8eb9d707(this.f_be_b_1570d10e)) {
                           var10000 = null;
                           break label108;
                        }
                     } else if (this.f_be_i_49 == 2) {
                        this.f_be_i_49 = 0;
                     } else {
                        if (!this.f_be_f_5a) {
                           p_be_a_b14f20a1(this.f_be_b_48a69a2c);
                           this.f_be_f_5a = true;
                        }

                        this.f_be_i_49 = 2;
                        var13 = this.f_be_b_48a69a2c;
                     }
                  }

                  var10000 = var13;
               }

               var6 = var10000;
            }
            break;
         case 1000003:
            if (var2 == 1) {
               switch (this.f_be_i_49) {
                  case 1:
                  case 2:
                     this.f_be_i_49 = 0;
                     this.m_e_9b75();
                     break;
                  default:
                     Command var9;
                     if ((var9 = this.m_a_c667a636(2)) != null && this.m_a_8eb9d707(var9)) {
                        return;
                     }
               }
            }
      }

      if (var6 != null) {
         f_be_n_49 = 0;
         int var15;
         int var8 = p_be_e_134621(var15 = var6.size());
         int var11 = this.p_be_b_9b68();
         if (var8 > var11) {
            f_be_v_49 = var11 / f_be_f_67f45fce.getHeight();
            f_be_w_49 = var15 - f_be_v_49;
         } else {
            f_be_v_49 = var15;
            f_be_w_49 = 0;
         }
      }

      if ((!var3 || var4 == f_be_n_49) && var7 == this.f_be_i_49) {
         if (var2 == 1 && !f_be_a_5a) {
            switch (var1) {
               case 49:
                  this.m_g_9b75();
                  this.f_be_b_49 = this.f_be_o_49 = 0;
                  this.m_h_9b75();
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
                  this.m_g_9b75();
                  int var12 = this.m_a_9b68() - 1;
                  this.f_be_b_49 = var12;
                  this.m_f_9b75();
                  this.m_h_9b75();
                  return;
               case 57:
                  this.m_a_2563266(this.m_e_9b68(), false);
            }
         }
      } else {
         this.m_e_9b75();
      }
   }

   private void p_be_b_255f295(int var1, int var2) {
      switch (var2) {
         case 1:
            if (C_bp.m_a_134632(153)) {
               C_ck.m_a_9b75();
            }

            this.p_be_a_255f295(var1, var2);
            break;
         case 2:
            this.p_be_a_255f295(var1, var2);
      }

      if (this.f_be_a_240737 != null) {
         this.f_be_a_240737.m_a_f1688121(this, var1, var2);
      }
   }

   protected final void m_e_13462e(int var1) {
      this.p_be_b_255f295(var1, 1);
   }

   protected final void m_f_13462e(int var1) {
      this.p_be_b_255f295(var1, 2);
   }

   public final void m_a_6f63a2af(CommandListener var1) {
      this.f_be_a_a2d63fba = var1;
      if (this.m_b_9b79()) {
         f_be_a_240ad9.setCommandListener(this.f_be_a_a2d63fba);
      }
   }

   protected final boolean m_a_8eb9d707(Command var1) {
      if (this.f_be_a_a2d63fba != null && var1 != null) {
         this.f_be_a_a2d63fba.commandAction(var1, null);
         return true;
      } else {
         return false;
      }
   }

   private static int p_be_d_134621(int var0) {
      String var1 = null;

      try {
         var1 = f_be_a_240ad9.getKeyName(var0).toLowerCase();
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
         case 22:
         case 106:
         case 112:
         case 57346:
            return 1000002;
         case -202:
         case 21:
         case 105:
         case 113:
         case 57345:
            return 1000001;
         default:
            try {
               int var4;
               if ((var4 = f_be_a_240ad9.getGameAction(var0)) > 0) {
                  return var4;
               }
            } catch (Exception var2) {
            }

            return var0;
      }
   }

   public final void m_c_aad3b1ff(String var1) {
      if (this.f_be_a_523beb0a == null || !this.f_be_a_523beb0a.equals(var1)) {
         this.f_be_a_523beb0a = var1;
         this.m_e_9b75();
      }
   }

   public final String m_b_73cf11cb() {
      return this.f_be_a_523beb0a;
   }

   public final void m_g_13462e(int var1) {
      this.m_g_9b75();
      this.f_be_b_49 = this.f_be_o_49 = var1;
      this.m_f_9b75();
      this.m_h_9b75();
   }

   public final void m_h_13462e(int var1) {
      this.m_g_9b75();
      this.f_be_b_49 = var1;
      this.m_f_9b75();
      this.m_h_9b75();
   }

   public final int m_g_9b68() {
      return this.f_be_b_49;
   }

   private int p_be_k_9b68() {
      int var1 = 0;
      if (this.f_be_a_523beb0a != null) {
         var1 = f_be_d_67f45fce.getHeight() + 2;
      }

      C_f var2;
      int var3;
      if (this.f_be_a_129e1 != null && (var3 = (var2 = this.f_be_a_129e1).f_f_d_49 + 1) > var1) {
         var1 = var3;
      }

      return var1;
   }

   protected boolean m_a_134632(int var1) {
      return this.f_be_b_49 == var1 && this.f_be_u_49 != 3;
   }

   public static void m_a_b329a056(InputStream var0, boolean var1) {
      if (var0 == null && var1) {
         f_be_a_9b93e07e = null;
      } else {
         try {
            f_be_a_9b93e07e = Image.createImage(C_bp.m_a_47921032(34));
         } catch (Exception var3) {
            try {
               f_be_a_9b93e07e = Image.createImage(var0);
            } catch (Exception var2) {
               f_be_a_9b93e07e = null;
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
         if (f_be_a_b4e == null || f_be_a_b4e.length < var27) {
            f_be_a_b4e = null;
            f_be_a_b4e = new int[var27];
         }

         if (f_be_d_49 != var26 || f_be_z_49 != var1 || f_be_A_49 != var2) {
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
                     f_be_a_b4e[var28++] = var32;
                  }
               }
            }

            f_be_d_49 = var26;
            f_be_z_49 = var1;
            f_be_A_49 = var2;
         }

         int var29 = var24;

         for (int var33 = var3; var33 < var5; var33 += 32) {
            var0.drawRGB(f_be_a_b4e, 0, 32, var33, var4, var29 > 32 ? 32 : var29, var26, true);
            var29 -= 32;
         }
      }
   }

   public final int m_h_9b68() {
      return this.m_a_a729dba6(0).getHeight();
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

   private void p_be_a_26c5358c(Graphics var1, int var2, int var3, int var4) {
      var2 = this.m_e_9b68();
      var3 = this.p_be_m_9b68();
      var4 = this.p_be_k_9b68();
      int var10003 = this.m_h_9b68();
      int var9 = 0;
      int var8 = var10003;
      Graphics var6 = var1;
      C_be var5 = this;
      int var10 = -1;
      int var11 = -1;
      int var12 = this.p_be_b_9b68();
      var9 = this.m_a_9b68();
      int var15 = this.m_i_9b68() - f_be_a_49;
      var12 = var4 + var12;
      int var16 = f_be_a_240ad9.getWidth();
      int var17 = f_be_a_240ad9.getHeight();
      var1.setColor(this.f_be_q_49);
      var1.fillRect(0, 0, var16, f_be_a_240ad9.getHeight());
      if (f_be_a_9b93e07e != null) {
         var1.drawImage(f_be_a_9b93e07e, var16 / 2, var17 / 2, 3);
      }

      int var14 = var4;
      int var18 = 0;

      for (int var13 = this.f_be_o_49; var13 < var9; var13++) {
         var18 = var5.m_a_134621(var13);
         if (var5.m_a_134632(var13)) {
            if (var10 == -1) {
               var10 = var14;
            }

            var11 = var14 + var18 - 1;
         }

         if ((var14 += var18) >= var12) {
            break;
         }
      }

      if (var10 != -1) {
         if (C_bp.f_bp_d_49 > 10) {
            m_a_64ddb84c(var1, m_a_255f288(C_bp.f_bp_c_49, -32), C_bp.f_bp_c_49, 1, var10 + 1, var15 - 1, var11, C_bp.f_bp_d_49);
         } else {
            var1.setStrokeStyle(1);
         }

         var1.setColor(m_a_255f288(C_bp.f_bp_c_49, -48));
         boolean var10000 = var5.f_be_o_49 >= 1 ? var5.m_a_134632(var5.f_be_o_49 - 1) : false;
         boolean var19 = false;
         if (!var10000) {
            var1.drawLine(1, var10, var15 - 2, var10);
         }

         var1.drawLine(0, var10 + 1, 0, var11 - 1);
         var1.drawLine(var15 - 1, var10 + 1, var15 - 1, var11 - 1);
         var1.drawLine(1, var11, var15 - 2, var11);
      }

      f_be_a_240aba.m_a_9b75();
      var14 = var4;
      boolean var53 = false;

      for (int var43 = var5.f_be_o_49; var43 < var9; var43++) {
         var16 = var5.m_a_134621(var43);
         var6.setStrokeStyle(0);
         var17 = var5.f_be_c_49;
         var18 = var15 - 2;
         int var7 = var14 + var16;
         var5.m_a_c1306d97(var6, var43, var17, var14, var18, var7, var8);
         if ((var14 += var16) >= var12) {
            break;
         }
      }

      var10 = this.m_i_9b68() - f_be_a_49;
      var11 = this.p_be_l_9b68() - var3;
      int var30 = (var12 = this.m_a_9b68()) > var2 && var12 > 0;
      int var44;
      if ((var44 = m_a_255f288(m_a_255f288(this.f_be_q_49, 32), -32)) == 0) {
         var44 = 8421504;
      }

      var1.setStrokeStyle(0);
      var1.setColor(m_a_255f288(var44, -96));
      var1.drawLine(var10, var4, var10, var11 - 1);
      var1.setColor(m_a_255f288(var44, -64));
      var1.drawLine(var10 + 1, var4, var10 + 1, var11 - 1);
      var1.setColor(m_a_255f288(var44, -32));
      var1.drawLine(var10 + 2, var4, var10 + 2, var11 - 1);
      var1.setColor(var44);
      var1.drawLine(var10 + 3, var4, var10 + 3, var11 - 1);
      if (var30) {
         if ((var14 = (var11 - var4) * var2 / var12) < 7) {
            var14 = 7;
         }

         f_be_y_49 = (f_be_x_49 = this.f_be_o_49 * (var11 - var14 - var4) / (var12 - var2) + var4) + var14;
         var1.setColor(m_a_255f288(var44, -192));
         var1.drawRect(var10, f_be_x_49, f_be_a_49 - 1, f_be_y_49 - f_be_x_49 - 1);
         var1.setColor(m_a_255f288(this.f_be_s_49, 96));
         var1.drawLine(var10 + 1, f_be_x_49 + 1, var10 + 1, f_be_y_49 - 2);
         var1.drawLine(var10 + 1, f_be_x_49 + 1, var10 + f_be_a_49 - 2, f_be_x_49 + 1);
         var1.setColor(this.f_be_s_49);
         var1.drawLine(var10 + 2, f_be_x_49 + 2, var10 + 2, f_be_y_49 - 2);
      }

      if (var3 != 0) {
         var30 = this.p_be_l_9b68() - var3;
         var10 = this.p_be_l_9b68();
         var11 = this.m_i_9b68();
         var12 = var3 / 4;
         if (!this.f_be_d_5a) {
            m_a_64ddb84c(var1, m_a_255f288(this.f_be_s_49, -48), this.f_be_s_49, 0, var30, var11, var10, C_bp.f_bp_f_49);
            var1.setFont(f_be_e_67f45fce);
            var44 = (var30 + var10 - f_be_e_67f45fce.getHeight()) / 2 + 1;
            var1.setColor(m_c_134621(this.f_be_s_49));
            if (this.f_be_a_1570d10e != null) {
               var1.drawString(this.f_be_a_1570d10e.getLabel(), var12, var44, 20);
            }

            if (this.f_be_b_1570d10e != null) {
               var1.drawString(this.f_be_b_1570d10e.getLabel(), var11 - var12, var44, 24);
            }

            if (C_bp.m_a_134632(173)) {
               var1.drawString(C_ce.m_a_2416688b(true, false), var11 / 2, var44, 17);
            } else {
               var15 = var11 / 2 - 2;
               var12 = (var30 + var10) / 2;
               var1.drawLine(var15 - 3, var12 - 1, var15, var12 + 2);
               var1.drawLine(var15 - 3, var12, var15, var12 + 3);
               var1.drawLine(var15 - 3, var12 + 1, var15, var12 + 4);
               var1.drawLine(var15 - 3, var12 + 2, var15, var12 + 5);
               var1.drawLine(var15, var12 + 2, var15 + 6, var12 - 4);
               var1.drawLine(var15, var12 + 3, var15 + 6, var12 - 3);
               var1.drawLine(var15, var12 + 4, var15 + 6, var12 - 2);
               var1.drawLine(var15, var12 + 5, var15 + 6, var12 - 1);
            }

            if (C_bp.f_bp_f_49 > 170) {
               var1.setColor(m_a_255f288(this.f_be_s_49, -128));
               var1.drawLine(0, var30, var11, var30);
            }
         }
      }

      switch (this.f_be_i_49) {
         case 1:
            this.p_be_a_da40b5c4(var1, this.f_be_a_48a69a2c, this.p_be_l_9b68() - var3, 4, 1, -1, -1);
            break;
         case 2:
            this.p_be_a_da40b5c4(var1, this.f_be_b_48a69a2c, this.p_be_l_9b68() - var3, 8, 1, -1, -1);
      }

      if (this.f_be_a_523beb0a != null) {
         var8 = this.m_i_9b68();
         var1.setFont(f_be_d_67f45fce);
         var30 = this.p_be_k_9b68();
         m_a_64ddb84c(var1, this.f_be_s_49, m_a_255f288(this.f_be_s_49, -48), 0, 0, var8, var30, C_bp.f_bp_e_49);
         if (C_bp.f_bp_e_49 > 170) {
            var1.setColor(m_a_255f288(this.f_be_s_49, -128));
            var1.drawLine(0, var30 - 1, var8, var30 - 1);
         }

         int var35 = f_be_l_49;
         if (this.f_be_a_129e1 != null) {
            this.f_be_a_129e1.m_b_11c44857(var1, var35, var30 / 2);
            C_f var20;
            var35 += (var20 = this.f_be_a_129e1).f_f_c_49 + 1;
         }

         if (C_bp.m_a_134632(176) && this.f_be_b_129e1 != null && this.f_be_b_129e1 != C_x.m_a_485a59b9(37)) {
            this.f_be_b_129e1.m_b_11c44857(var1, var35, var30 / 2);
            C_f var21 = this.f_be_b_129e1;
            var35 += this.f_be_b_129e1.f_f_c_49 + 1;
         }

         if (C_bp.m_a_134632(177) && this.f_be_c_129e1 != null) {
            this.f_be_c_129e1.m_b_11c44857(var1, var35, var30 / 2);
            C_f var22 = this.f_be_c_129e1;
            var35 += this.f_be_c_129e1.f_f_c_49 + 1;
         }

         if (C_bp.m_a_134632(180) && this.f_be_d_129e1 != null) {
            this.f_be_d_129e1.m_b_11c44857(var1, var35, var30 / 2);
            C_f var23 = this.f_be_d_129e1;
            var35 += this.f_be_d_129e1.f_f_c_49 + 1;
         }

         var1.setColor(m_c_134621(this.f_be_s_49));
         if (this.f_be_a_129e1 != null) {
            var1.drawString(this.f_be_a_523beb0a, var35, (var30 - f_be_d_67f45fce.getHeight()) / 2, 20);
         } else {
            var1.drawString(this.f_be_a_523beb0a, var8 / 2, (var30 - f_be_d_67f45fce.getHeight()) / 2, 17);
         }

         if (C_bp.m_a_134632(178) && this.f_be_e_129e1 != null) {
            var10 = var8 - f_be_m_49;
            this.f_be_e_129e1.m_c_11c44857(var1, var10, var30 / 2);
         }
      }
   }

   protected final void m_a_272d79b7(Graphics var1) {
      if (!this.f_be_b_5a) {
         if (f_be_a_240ad9.isDoubleBuffered()) {
            this.p_be_a_26c5358c(var1, 1, -1, -1);
         } else {
            try {
               if (f_be_b_9b93e07e == null) {
                  f_be_b_9b93e07e = Image.createImage(this.m_i_9b68(), this.p_be_l_9b68());
               }

               this.p_be_a_26c5358c(f_be_b_9b93e07e.getGraphics(), 1, -1, -1);
               var1.drawImage(f_be_b_9b93e07e, 0, 0, 20);
            } catch (Exception var2) {
               this.p_be_a_26c5358c(var1, 1, -1, -1);
            }
         }
      }
   }

   protected void m_a_c1306d97(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      f_be_a_240aba.m_a_9b75();
      this.m_a_d82231e8(var2, f_be_a_240aba);
      int var8 = f_be_a_240aba.f_bw_c_49 + var3 + 1;
      if (f_be_a_240aba.f_bw_a_129e1 != null) {
         f_be_a_240aba.f_bw_a_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var9 = f_be_a_240aba.f_bw_a_129e1;
         var8 += f_be_a_240aba.f_bw_a_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_b_129e1 != null) {
         f_be_a_240aba.f_bw_b_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var10 = f_be_a_240aba.f_bw_b_129e1;
         var8 += f_be_a_240aba.f_bw_b_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_c_129e1 != null) {
         f_be_a_240aba.f_bw_c_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var11 = f_be_a_240aba.f_bw_c_129e1;
         var8 += f_be_a_240aba.f_bw_c_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_d_129e1 != null) {
         f_be_a_240aba.f_bw_d_129e1.m_b_11c44857(var1, var8, (var4 + var6) / 2);
         C_f var12 = f_be_a_240aba.f_bw_d_129e1;
         var8 += f_be_a_240aba.f_bw_d_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_a_523beb0a != null) {
         var1.setFont(this.m_a_a729dba6(f_be_a_240aba.f_bw_a_49));
         var1.setColor(f_be_a_240aba.f_bw_b_49);
         var1.drawString(f_be_a_240aba.f_bw_a_523beb0a, var8, (var4 + var6 - var7) / 2, 20);
      }

      int var13 = f_be_a_240ad9.getWidth() - f_be_a_49 - 2;
      if (f_be_a_240aba.f_bw_h_129e1 != null) {
         f_be_a_240aba.f_bw_h_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var14 = f_be_a_240aba.f_bw_h_129e1;
         var13 -= f_be_a_240aba.f_bw_h_129e1.f_f_c_49 + 2;
      }

      if (f_be_a_240aba.f_bw_g_129e1 != null) {
         f_be_a_240aba.f_bw_g_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var15 = f_be_a_240aba.f_bw_g_129e1;
         var13 -= f_be_a_240aba.f_bw_g_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_f_129e1 != null) {
         f_be_a_240aba.f_bw_f_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
         C_f var16 = f_be_a_240aba.f_bw_f_129e1;
         var13 -= f_be_a_240aba.f_bw_f_129e1.f_f_c_49 + 1;
      }

      if (f_be_a_240aba.f_bw_e_129e1 != null) {
         f_be_a_240aba.f_bw_e_129e1.m_c_11c44857(var1, var13, (var4 + var6) / 2);
      }
   }

   public final void m_i_9b75() {
      this.f_be_b_5a = true;
   }

   protected void m_b_9b75() {
   }

   public final void m_j_9b75() {
      this.f_be_b_5a = false;
      this.m_b_9b75();
      this.m_e_9b75();
   }

   protected final boolean m_c_9b79() {
      return this.f_be_b_5a;
   }

   private int p_be_l_9b68() {
      return this.f_be_C_49 == -1 ? f_be_a_240ad9.getHeight() : this.f_be_C_49;
   }

   protected final int m_i_9b68() {
      return this.f_be_B_49 == -1 ? f_be_a_240ad9.getWidth() : this.f_be_B_49;
   }

   public static int m_j_9b68() {
      return f_be_a_240ad9.getWidth();
   }

   public static void m_k_9b75() {
      if (C_bp.m_a_134632(143)) {
         f_be_e_49 = 2;
         f_be_f_49 = 1;
         f_be_g_49 = 4;
         f_be_h_49 = 3;
      } else {
         f_be_e_49 = 1;
         f_be_f_49 = 2;
         f_be_g_49 = 3;
         f_be_h_49 = 4;
      }
   }

   protected final Command m_a_c667a636(int var1) {
      if (this.f_be_a_1570d10e != null && this.f_be_a_1570d10e.getCommandType() == var1) {
         return this.f_be_a_1570d10e;
      } else if (this.f_be_b_1570d10e != null && this.f_be_b_1570d10e.getCommandType() == var1) {
         return this.f_be_b_1570d10e;
      } else {
         for (int var2 = this.f_be_a_48a69a2c.size() - 1; var2 >= 0; var2--) {
            Command var3;
            if ((var3 = (Command)this.f_be_a_48a69a2c.elementAt(var2)).getCommandType() == var1) {
               return var3;
            }
         }

         for (int var4 = this.f_be_b_48a69a2c.size() - 1; var4 >= 0; var4--) {
            Command var5;
            if ((var5 = (Command)this.f_be_b_48a69a2c.elementAt(var4)).getCommandType() == var1) {
               return var5;
            }
         }

         return null;
      }
   }

   private int p_be_m_9b68() {
      return this.f_be_d_5a ? 0 : f_be_e_67f45fce.getHeight() + 2;
   }

   public final void m_a_48817c60(Command var1, int var2) {
      switch (var2) {
         case 1:
            this.f_be_a_1570d10e = var1;
            this.m_e_9b75();
            return;
         case 2:
            this.f_be_b_1570d10e = var1;
            this.m_e_9b75();
            return;
         case 3:
            if (this.f_be_a_48a69a2c.indexOf(var1) == -1) {
               this.f_be_a_48a69a2c.addElement(var1);
               this.f_be_e_5a = false;
               return;
            }
            break;
         case 4:
            if (this.f_be_b_48a69a2c.indexOf(var1) == -1) {
               this.f_be_b_48a69a2c.addElement(var1);
               this.f_be_f_5a = false;
            }
      }
   }

   public final void m_a_8eb9d703(Command var1) {
      if (var1 == this.f_be_a_1570d10e) {
         this.f_be_a_1570d10e = null;
         this.f_be_a_48a69a2c.removeAllElements();
         this.m_e_9b75();
      } else if (var1 == this.f_be_b_1570d10e) {
         this.f_be_b_1570d10e = null;
         this.f_be_b_48a69a2c.removeAllElements();
         this.m_e_9b75();
      } else {
         this.f_be_a_48a69a2c.removeElement(var1);
         this.f_be_b_48a69a2c.removeElement(var1);
      }
   }

   public final void m_l_9b75() {
      this.f_be_a_1570d10e = null;
      this.f_be_b_1570d10e = null;
      this.f_be_a_48a69a2c.removeAllElements();
      this.f_be_b_48a69a2c.removeAllElements();
   }

   private static int p_be_e_134621(int var0) {
      return f_be_f_67f45fce.getHeight() * var0 + 4;
   }

   private boolean p_be_a_8a5c62f0(Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      switch (var6) {
         case 1:
            var1.setColor(this.f_be_r_49);
            boolean var7 = false;
            int var9 = (var3 + var5 - 2) / 2;

            for (int var10 = -1; var10 <= 1; var10++) {
               int var8 = (var2 + var4) / 2 - var10 * 5;
               var1.fillRect(var8, var9, 2, 2);
            }
         default:
            return false;
      }
   }

   private boolean p_be_a_da40b5c4(Graphics var1, Vector var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = (var6 = f_be_f_67f45fce.getHeight()) / 4;
      var5 = var2.size();
      int var8 = 0;
      int var9 = p_be_e_134621(f_be_v_49);

      for (int var10 = 0; var10 < var5; var10++) {
         Command var11 = (Command)var2.elementAt(var10);
         int var12;
         if ((var12 = f_be_f_67f45fce.stringWidth(var11.getLabel())) > var8) {
            var8 = var12;
         }
      }

      if ((var8 = var8 + (var7 << 1)) > f_be_a_240ad9.getWidth() - 4) {
         var8 = f_be_a_240ad9.getWidth() - 4;
      }

      int var22 = var3 - var9 - 1;
      int var23 = 0;
      switch (var4) {
         case 8:
            var23 = this.m_i_9b68() - var8 - 1;
         default:
            m_a_64ddb84c(var1, m_a_255f288(this.f_be_q_49, -32), this.f_be_q_49, var23, var22, var23 + var8, var22 + var9, 255);
            if (f_be_w_49 != 0) {
               this.p_be_a_8a5c62f0(var1, var23, var22, var23 + var8, var22 + 3, 1);
               boolean var24 = false;
            }

            if (f_be_w_49 + f_be_v_49 != var5) {
               this.p_be_a_8a5c62f0(var1, var23, var22 + var9 - 3, var23 + var8, var22 + var9, 1);
               boolean var25 = false;
            }

            var1.setFont(f_be_f_67f45fce);
            int var26 = var22 + 3;
            int var13 = f_be_w_49;

            for (int var15 = 0; var15 < f_be_v_49; var15++) {
               if (var13 == f_be_n_49) {
                  m_a_64ddb84c(var1, m_a_255f288(C_bp.f_bp_c_49, -32), C_bp.f_bp_c_49, var23 + 1, var26 - 1, var23 + var8, var26 + var6, 255);
                  var1.setColor(m_a_255f288(C_bp.f_bp_c_49, -48));
                  var1.drawRect(var23 + 1, var26 - 1, var8 - 2, var6);
               }

               var26 += var6;
               var13++;
            }

            int var27 = var22 + 3;
            int var14 = f_be_w_49;

            for (int var16 = 0; var16 < f_be_v_49; var16++) {
               Command var18 = (Command)var2.elementAt(var14);
               var1.setColor(var14 == f_be_n_49 ? m_c_134621(C_bp.f_bp_c_49) : this.f_be_t_49);
               var1.drawString(var18.getLabel(), var23 + var7, var27, 20);
               var27 += var6;
               var14++;
            }

            var1.setColor(this.f_be_r_49);
            var1.drawRect(var23, var22, var8, var9);
            return false;
      }
   }

   private static void p_be_a_b14f20a1(Vector var0) {
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
      m_k_9b75();
   }
}
