package recovered;

/** 0.6 source correspondence (inferred): DrawControls.ImageList. Release class: g. */

import java.util.Vector;
import javax.microedition.lcdui.Image;

public class C_g {
   private C_f[] f_g_a_2a87a6;
   protected int f_g_a_49 = 0;
   protected int f_g_b_49 = 0;

   public C_f m_a_485a59b9(int var1) {
      return var1 < this.m_a_9b68() && var1 >= 0 ? this.f_g_a_2a87a6[var1] : null;
   }

   public int m_a_9b68() {
      return this.f_g_a_2a87a6 != null ? this.f_g_a_2a87a6.length : 0;
   }

   public final int m_b_9b68() {
      return this.f_g_b_49;
   }

   public final void m_a_afa300e4(String var1, int var2) throws java.io.IOException {
      Image var8;
      int var3 = (var8 = Image.createImage(var1)).getHeight();
      int var4 = var8.getWidth();
      this.f_g_a_49 = var4 / var2;
      this.f_g_b_49 = var3;
      Vector var9 = new Vector();

      for (int var6 = 0; var6 < var3; var6 += this.f_g_b_49) {
         for (int var7 = 0; var7 < var4; var7 += this.f_g_a_49) {
            C_f var5 = new C_f(var8, var7, var6, this.f_g_a_49, this.f_g_b_49);
            var9.addElement(var5);
         }
      }

      this.f_g_a_2a87a6 = new C_f[var9.size()];
      var9.copyInto(this.f_g_a_2a87a6);
   }

   public void m_a_44bd8e9f(String var1, int var2, int var3) throws java.io.IOException {
      Image var10;
      int var4 = (var10 = Image.createImage(var1)).getHeight();
      int var5 = var10.getWidth();
      if (var2 == -1) {
         var2 = Math.min(var4, var5);
      }

      if (var3 == -1) {
         var3 = var4;
      }

      this.f_g_a_49 = var2;
      this.f_g_b_49 = var3;
      Vector var6 = new Vector();

      for (int var8 = 0; var8 < var4; var8 += var3) {
         for (int var9 = 0; var9 < var5; var9 += var2) {
            C_f var7 = new C_f(var10, var9, var8, var2, var3);
            var6.addElement(var7);
         }
      }

      this.f_g_a_2a87a6 = new C_f[var6.size()];
      var6.copyInto(this.f_g_a_2a87a6);
   }

   public static C_g m_a_44af1569(String var0) {
      C_g var1 = new C_g();

      try {
         var1.m_a_44bd8e9f(var0, -1, -1);
      } catch (Exception var2) {
      }

      return var1;
   }
}
