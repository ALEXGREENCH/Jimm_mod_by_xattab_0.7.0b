package recovered;

/** 0.6 source correspondence (inferred): DrawControls.AniIcon. Release class: o. */

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class C_o extends C_f {
   C_f[] f_o_a_2a87a6;
   int[] f_o_a_b4e;
   int f_o_a_49 = 0;
   boolean f_o_a_5a = false;
   long f_o_a_4a = 0L;

   public C_o(C_f var1, int var2) {
      super(var1.m_a_5017a17f(), 0, 0, var1.f_f_c_49, var1.f_f_d_49);
      this.f_o_a_2a87a6 = new C_f[var2];
      this.f_o_a_b4e = new int[var2];
   }

   protected final Image m_a_5017a17f() {
      return this.f_o_a_2a87a6[this.f_o_a_49].m_a_5017a17f();
   }

   public final void m_a_11c44857(Graphics var1, int var2, int var3) {
      this.f_o_a_2a87a6[this.f_o_a_49].m_a_11c44857(var1, var2, var3);
      this.f_o_a_5a = true;
   }
}
