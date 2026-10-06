package recovered;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class C_c extends C_be {
   C_e f_c_a_129c2 = new C_e(null);
   private Vector f_c_a_48a69a2c;
   private boolean f_c_b_5a = false;
   private int f_c_j_49 = 6;
   private boolean f_c_c_5a = true;
   private boolean f_c_d_5a = true;
   private int f_c_k_49;
   public C_k f_c_a_12a7c;
   private C_e f_c_b_129c2;

   public C_c() {
      super(null);
      this.f_c_a_129c2.f_e_a_5a = true;
      this.f_c_b_129c2 = null;
      this.f_c_d_5a = false;
   }

   private C_e p_c_a_485a599a(int var1) {
      return (C_e)this.f_c_a_48a69a2c.elementAt(var1);
   }

   private void p_c_m_9b75() {
      if (this.f_c_b_5a || this.f_c_a_48a69a2c == null) {
         this.p_c_n_9b75();
      }
   }

   public final void m_a_13462e(int var1) {
      this.f_c_j_49 = var1;
      this.m_e_9b75();
   }

   public final void m_a_1385ff(boolean var1) {
      if (var1 != this.f_c_c_5a) {
         this.f_c_c_5a = var1;
         this.m_e_9b75();
      }
   }

   public final C_e m_a_2477921() {
      return this.m_g_9b68() >= 0 && this.m_g_9b68() < this.f_c_a_48a69a2c.size() ? this.p_c_a_485a599a(this.m_g_9b68()) : null;
   }

   public final void m_a_489f9f67(C_e var1) {
      if (this.m_c_9b79()) {
         this.f_c_b_129c2 = var1;
      } else {
         this.p_c_m_9b75();
         if (this.m_a_2477921() != var1) {
            int var2 = this.f_c_a_48a69a2c.size();

            for (int var3 = 0; var3 < var2; var3++) {
               if (this.p_c_a_485a599a(var3) == var1) {
                  this.m_h_13462e(var3);
                  return;
               }
            }

            Vector var4 = new Vector();
            Object var5 = null;
            this.p_c_a_fc94b265(var4, this.f_c_a_129c2, var1);
            if ((var2 = var4.size()) != 0) {
               for (int var7 = 0; var7 < var2; var7++) {
                  ((C_e)var4.elementAt(var7)).f_e_a_5a = true;
               }

               this.p_c_n_9b75();
               this.m_a_489f9f67(var1);
               this.f_c_b_5a = true;
               this.m_e_9b75();
            }
         }
      }
   }

   private boolean p_c_a_fc94b265(Vector var1, C_e var2, C_e var3) {
      int var4 = var2.m_a_9b68();

      for (int var6 = 0; var6 < var4; var6++) {
         C_e var5;
         if ((var5 = var2.m_a_485a599a(var6)) == var3) {
            return true;
         }

         if (this.p_c_a_fc94b265(var1, var5, var3)) {
            var1.addElement(var5);
            return true;
         }
      }

      return false;
   }

   protected final boolean m_a_9b79() {
      C_e var1;
      if ((var1 = this.m_a_2477921()) == null) {
         return false;
      } else if (this.f_c_d_5a) {
         if (var1.m_a_9b68() != 0) {
            var1.f_e_a_5a = !var1.f_e_a_5a;
            this.p_c_n_9b75();
            this.m_e_9b75();
         }

         return false;
      } else {
         this.m_a_8eb9d707(this.m_a_c667a636(4));
         return true;
      }
   }

   protected final int m_a_9b68() {
      this.p_c_m_9b75();
      return this.f_c_a_48a69a2c.size();
   }

   private void p_c_n_9b75() {
      this.f_c_b_5a = false;
      if (this.f_c_a_48a69a2c == null) {
         this.f_c_a_48a69a2c = new Vector();
      }

      this.f_c_a_48a69a2c.removeAllElements();
      int var1 = this.f_c_a_129c2.m_a_9b68();

      for (int var2 = 0; var2 < var1; var2++) {
         this.p_c_b_cb54c07c(this.f_c_a_129c2.m_a_485a599a(var2), 0);
      }

      this.m_d_13462e(3);
   }

   private void p_c_b_cb54c07c(C_e var1, int var2) {
      this.f_c_a_48a69a2c.addElement(var1);
      var1.f_e_a_49 = var2;
      if (var1.m_a_9b79()) {
         int var3 = var1.m_a_9b68();

         for (int var4 = 0; var4 < var3; var4++) {
            this.p_c_b_cb54c07c(var1.m_a_485a599a(var4), var2 + 1);
         }
      }
   }

   protected final void m_a_d82231e8(int var1, C_bw var2) {
      this.p_c_m_9b75();
      C_e var3 = this.p_c_a_485a599a(var1);
      this.f_c_a_12a7c.m_a_e3fd46a1(var3, var2);
      var2.f_bw_c_49 = var3.f_e_a_49 * this.f_c_j_49;
      if (this.f_c_c_5a && var3.m_a_9b68() != 0) {
         var2.f_bw_c_49 = var2.f_bw_c_49 + 3 * this.f_c_k_49 / 4;
      }
   }

   protected final void m_a_c1306d97(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.f_c_k_49 = var7;
      this.p_c_m_9b75();
      super.m_a_c1306d97(var1, var2, var3, var4, var5, var6, var7);
      if (this.f_c_c_5a) {
         C_e var11 = this.p_c_a_485a599a(var2);
         int var8 = var3 + var11.f_e_a_49 * this.f_c_j_49;
         if ((var5 = (var7 << 1) / 3) < 7) {
            var5 = 7;
         }

         if (var5 % 2 == 0) {
            var5--;
         }

         if (var11.m_a_9b68() != 0) {
            var3 = (var4 + var6 - var5) / 2;
            var4 = var1.getColor();
            var1.setColor(8421504);
            var1.drawRect(var8, var3, var5 - 1, var5 - 1);
            var6 = var8 + var5 / 2;
            var7 = var3 + var5 / 2;
            var1.drawLine(var8 + 2, var7, var8 + var5 - 3, var7);
            if (!var11.m_a_9b79()) {
               var1.drawLine(var6, var3 + 2, var6, var3 + var5 - 3);
            }

            var1.setColor(var4);
         }
      }
   }

   public final C_e m_a_c92c35af(C_e var1, Object var2) {
      if (var1 == null) {
         var1 = this.f_c_a_129c2;
      }

      var2 = new C_e(var2);
      var1.m_a_9f354893(var2);
      this.f_c_b_5a = true;
      this.m_e_9b75();
      return var2;
   }

   private C_e p_c_a_9d74ebe1(C_e var1, C_e var2) {
      if (var1.m_a_489f9f5a(var2) != -1) {
         return var1;
      } else {
         int var3 = var1.m_a_9b68();

         for (int var5 = 0; var5 < var3; var5++) {
            C_e var4;
            if ((var4 = this.p_c_a_9d74ebe1(var1.m_a_485a599a(var5), var2)) != null) {
               return var4;
            }
         }

         return null;
      }
   }

   public final boolean m_a_489f9f6b(C_e var1) {
      this.p_c_o_9b75();
      C_e var2;
      if ((var2 = this.p_c_a_9d74ebe1(this.f_c_a_129c2, var1)) == null) {
         return false;
      } else {
         int var3;
         if ((var3 = var2.m_a_489f9f5a(var1)) == -1) {
            return false;
         } else {
            var2.m_a_13462e(var3);
            this.m_d_13462e(3);
            this.f_c_b_5a = true;
            this.m_e_9b75();
            this.p_c_p_9b75();
            return true;
         }
      }
   }

   public final void m_b_489f9f67(C_e var1) {
      this.p_c_o_9b75();
      if (var1 == null) {
         var1 = this.f_c_a_129c2;
      }

      var1.m_a_48a259a1(this.f_c_a_12a7c);
      if (var1.m_a_9b79()) {
         this.f_c_b_5a = true;
         this.m_e_9b75();
      }

      this.p_c_p_9b75();
   }

   public final void m_a_e40c7b6e(C_e var1, C_e var2, int var3) {
      if (var1 == null) {
         var1 = this.f_c_a_129c2;
      }

      this.p_c_o_9b75();
      var1.m_a_cb54c07c(var2, var3);
      if (var1.m_a_9b79()) {
         this.f_c_b_5a = true;
         this.m_e_9b75();
      }

      this.p_c_p_9b75();
   }

   public final void m_a_cb54c07c(C_e var1, int var2) {
      if (var1 == null) {
         var1 = this.f_c_a_129c2;
      }

      this.p_c_o_9b75();
      var1.f_e_a_48a69a2c.removeElementAt(var2);
      if (var1.m_a_9b79()) {
         this.f_c_b_5a = true;
         this.m_e_9b75();
      }

      this.p_c_p_9b75();
   }

   public static int m_a_496bbe28(C_e var0, C_e var1) {
      return var0.f_e_a_48a69a2c == null ? -1 : var0.f_e_a_48a69a2c.indexOf(var1);
   }

   public final void m_a_cb55004d(C_e var1, boolean var2) {
      if (var1.f_e_a_5a != var2) {
         var1.f_e_a_5a = var2;
         this.f_c_b_5a = true;
         this.m_e_9b75();
      }
   }

   public final void m_a_9b75() {
      this.f_c_a_129c2.m_a_9b75();
      this.p_c_n_9b75();
      this.m_d_13462e(3);
      this.m_f_9b75();
      this.m_e_9b75();
   }

   private void p_c_o_9b75() {
      this.f_c_b_129c2 = this.m_a_2477921();
   }

   protected final void m_b_9b75() {
      this.p_c_p_9b75();
   }

   private void p_c_p_9b75() {
      if (!this.m_c_9b79()) {
         this.m_a_489f9f67(this.f_c_b_129c2);
         this.f_c_b_129c2 = null;
      }
   }
}
