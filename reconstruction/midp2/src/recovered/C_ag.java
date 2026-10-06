package recovered;

/** 0.6 source correspondence (inferred): DrawControls.TextLine. Release class: ag. */

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class C_ag {
   public Vector f_ag_a_48a69a2c = new Vector();
   private int f_ag_b_49 = -1;
   public int f_ag_a_49 = -1;
   char f_ag_a_43;

   final C_n m_a_485a5ab1(int var1) {
      return (C_n)this.f_ag_a_48a69a2c.elementAt(var1);
   }

   public final void m_a_48a3b6be(C_n var1) {
      this.f_ag_a_48a69a2c.addElement(var1);
   }

   final int m_a_134621(int var1) {
      if (this.f_ag_b_49 == -1) {
         this.f_ag_b_49 = var1;

         for (int var3 = this.f_ag_a_48a69a2c.size() - 1; var3 >= 0; var3--) {
            int var2;
            if ((var2 = this.m_a_485a5ab1(var3).m_a_134621(var1)) > this.f_ag_b_49) {
               this.f_ag_b_49 = var2;
            }
         }
      }

      return this.f_ag_b_49;
   }

   final int m_b_134621(int var1) {
      int var2 = 0;

      for (int var3 = this.f_ag_a_48a69a2c.size() - 1; var3 >= 0; var3--) {
         var2 += this.m_a_485a5ab1(var3).m_b_134621(var1);
      }

      return var2;
   }

   final void m_a_dcdfe9b7(int var1, int var2, Graphics var3, int var4, C_bf var5) {
      int var6 = this.f_ag_a_48a69a2c.size();
      int var7 = this.m_a_134621(var4);

      for (int var10 = 0; var10 < var6; var10++) {
         C_n var8 = this.m_a_485a5ab1(var10);
         int var9 = var2 + (var7 - var8.m_a_134621(var4)) / 2;
         if (var8.f_n_a_129e1 != null) {
            var8.f_n_a_129e1.m_a_11c44857(var3, ++var1, var9);
         } else if (var8.f_n_a_523beb0a != null) {
            var3.setColor(var8.f_n_a_49 & 16777215);
            var3.setFont(var5.m_a_a729dba6(var8.f_n_a_49 >> 24));
            var3.drawString(var8.f_n_a_523beb0a, var1, var9, 20);
         }

         var1 += var8.m_b_134621(var4);
      }
   }

   final void m_a_c7ce505f(StringBuffer var1) {
      for (int var2 = 0; var2 < this.f_ag_a_48a69a2c.size(); var2++) {
         var1.append(this.m_a_485a5ab1(var2).f_n_a_523beb0a);
      }
   }
}
