package recovered;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public final class C_v implements Runnable {
   private int f_v_a_49;
   private Object[] f_v_a_7b09cd37;
   public static MIDlet f_v_a_13cbe5dc;

   private C_v(int var1, Object[] var2) {
      this.f_v_a_49 = var1;
      this.f_v_a_7b09cd37 = var2;
   }

   public final void run() {
      switch (this.f_v_a_49) {
         case 1:
            C_w.m_a_9cdfb1fb((C_bm)this.f_v_a_7b09cd37[0], a$4e9ee315(this.f_v_a_7b09cd37));
            return;
         case 4:
            C_w.a$505cff1c((String)this.f_v_a_7b09cd37[0]);
            return;
         case 5:
            C_w.m_a_a274af6d(
               (String)this.f_v_a_7b09cd37[0],
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 1),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 2),
               (byte[])this.f_v_a_7b09cd37[3],
               (byte[])this.f_v_a_7b09cd37[4],
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 5),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 6),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 7),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 8),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 9),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 10),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 11),
               p_v_a_b1611cda(this.f_v_a_7b09cd37, 12)
            );
            return;
         case 6:
            C_bg.m_a_3231c38a((String[])this.f_v_a_7b09cd37[0]);
            return;
         case 7:
            C_w.m_a_13462e(C_as.m_a_9b68());
            return;
         case 8:
            C_w.m_b_cb37742e((C_aw)this.f_v_a_7b09cd37[0]);
            return;
         case 9:
            C_w.m_a_afa340b5((String)this.f_v_a_7b09cd37[0], a$4e9ee315(this.f_v_a_7b09cd37));
            return;
         case 10:
            C_w.m_d_9b75();
            return;
         case 11:
            C_bg.m_a_9b75();
            return;
         case 13:
            C_bo.m_a_ab8148d2((Alert)this.f_v_a_7b09cd37[0]);
         case 2:
         case 3:
         case 12:
      }
   }

   public static synchronized void m_a_d3ad8a43(int var0, Object[] var1) {
      Display.getDisplay(f_v_a_13cbe5dc).callSerially(new C_v(var0, var1));
   }

   public static void m_a_1ef468a(int var0, Object var1) {
      m_a_d3ad8a43(var0, new Object[]{var1});
   }

   public static void m_a_cb40fff9(C_bm var0) {
      if (!C_bk.m_a_cb40fffd(var0)) {
         Boolean var1 = new Boolean(true);
         m_a_d3ad8a43(1, new Object[]{var0, var1});
      }
   }

   public static void m_a_afa340b5(String var0, boolean var1) {
      Object[] var2;
      (var2 = new Object[2])[0] = var0;
      var2[1] = new Boolean(var1);
      m_a_d3ad8a43(9, var2);
   }

   private static boolean a$4e9ee315(Object[] var0) {
      return var0[1] == null ? false : (Boolean)var0[1];
   }

   public static void m_a_7ac2f2fc(Object[] var0, int var1, int var2) {
      var0[var1] = new Integer(var2);
   }

   private static int p_v_a_b1611cda(Object[] var0, int var1) {
      return var0[var1] == null ? 0 : (Integer)var0[var1];
   }
}
