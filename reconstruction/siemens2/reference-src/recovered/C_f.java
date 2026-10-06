package recovered;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class C_f {
   private Image f_f_a_9b93e07e;
   private int f_f_a_49 = 0;
   private int f_f_b_49 = 0;
   int f_f_c_49 = 0;
   int f_f_d_49 = 0;

   public C_f(Image var1, int var2, int var3, int var4, int var5) {
      this.f_f_a_9b93e07e = var1;
      this.f_f_a_49 = var2;
      this.f_f_b_49 = var3;
      this.f_f_c_49 = var4;
      this.f_f_d_49 = var5;
   }

   protected Image m_a_5017a17f() {
      return this.f_f_a_9b93e07e;
   }

   public void m_a_11c44857(Graphics var1, int var2, int var3) {
      if (this.f_f_a_9b93e07e != null) {
         int var4 = var1.getClipX();
         int var5 = var1.getClipY();
         int var6 = var1.getClipHeight();
         int var7 = var1.getClipWidth();
         int var8 = var3 - this.f_f_b_49;
         int var9 = var2 - this.f_f_a_49;
         var1.clipRect(var2, var3, this.f_f_c_49, this.f_f_d_49);
         var1.drawImage(this.m_a_5017a17f(), var9, var8, 20);
         var1.setClip(var4, var5, var7, var6);
      }
   }

   public final void m_b_11c44857(Graphics var1, int var2, int var3) {
      this.m_a_11c44857(var1, var2, var3 - this.f_f_d_49 / 2);
   }

   public final void m_c_11c44857(Graphics var1, int var2, int var3) {
      this.m_a_11c44857(var1, var2 - this.f_f_c_49, var3 - this.f_f_d_49 / 2);
   }
}
