package recovered;

import java.io.InputStream;

public final class C_bg extends C_bl {
   String f_bg_a_523beb0a;
   String f_bg_b_523beb0a;
   private InputStream f_bg_a_91ffb459;
   int f_bg_a_49;

   public C_bg(String var1, C_aw var2, String var3, String var4, InputStream var5, int var6) {
      super(0L, null, var1, 26);
      super.f_bl_a_2406f9 = var2;
      this.f_bg_a_523beb0a = var3;
      this.f_bg_b_523beb0a = var4;
      this.f_bg_a_91ffb459 = var5;
      this.f_bg_a_49 = var6;
      super.f_bl_a_2406f9.m_a_cb3e45bf(this);
   }

   public final byte[] m_a_255806f(int var1) {
      byte[] var3;
      if (var1 < this.f_bg_a_49 / 2048) {
         var3 = new byte[2049];
      } else {
         var3 = new byte[this.f_bg_a_49 % 2048 + 1];
      }

      C_cf.m_a_e306985c(var3, 0, 6);

      try {
         this.f_bg_a_91ffb459.read(var3, 1, var3.length - 1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return var3;
   }
}
