package recovered;

/** 0.6 source correspondence (inferred): jimm.SplashCanvas. Release class: cp. */

import java.io.IOException;
import java.util.Timer;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jimm.Jimm;

public final class C_cp extends Canvas {
   public static C_cp f_cp_a_240da2;
   public static final Command f_cp_a_1570d10e = new Command(C_bt.m_a_e96ea081("5"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 1);
   private static Timer f_cp_a_f6e8e442;
   private static Timer f_cp_b_f6e8e442;
   private static Image f_cp_a_9b93e07e;
   private static Image f_cp_b_9b93e07e;
   private static Font f_cp_a_67f45fce = Font.getFont(0, 1 + C_bq.f_bq_g_49, 16);
   private static Font f_cp_b_67f45fce;
   private static int f_cp_a_49 = (f_cp_b_67f45fce = Font.getFont(0, C_bq.f_bq_g_49, 8)).getHeight();
   private static String f_cp_a_523beb0a;
   private static int f_cp_b_49;
   private static boolean f_cp_b_5a;
   private static int f_cp_c_49;
   public static long f_cp_a_4a;
   protected static boolean f_cp_a_5a;
   private static int f_cp_d_49;
   private static C_f f_cp_a_129e1;

   public C_cp(String var1) {
      f_cp_a_240da2 = this;
      this.setFullScreenMode(true);
      m_a_aad3b1ff(var1);
      f_cp_a_5a = false;
   }

   public C_cp() {
      this(null);
   }

   public static synchronized void m_a_aad3b1ff(String var0) {
      f_cp_a_523beb0a = new String(var0);
      f_cp_a_240da2.repaint();
   }

   public static synchronized void m_a_13462e(int var0) {
      f_cp_d_49 = var0;
   }

   public static synchronized void m_a_48a013c6(C_f var0) {
      f_cp_a_129e1 = var0;
   }

   private static Image p_cp_a_5017a17f() {
      if (f_cp_a_9b93e07e == null) {
         try {
            f_cp_a_9b93e07e = Image.createImage("/logo.png");
         } catch (Exception var0) {
            f_cp_a_9b93e07e = null;
         }
      }

      return f_cp_a_9b93e07e;
   }

   public static void m_a_9b75() {
      if (f_cp_b_f6e8e442 != null) {
         f_cp_b_f6e8e442.cancel();
         f_cp_b_f6e8e442 = null;
      }

      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cp_a_240da2);
   }

   public static void m_a_8eb9d703(Command var0) {
      f_cp_a_240da2.addCommand(var0);
   }

   public static void m_b_8eb9d703(Command var0) {
      f_cp_a_240da2.removeCommand(var0);
   }

   public static void m_a_6f63a2af(CommandListener var0) {
      f_cp_a_240da2.setCommandListener(var0);
   }

   public static void m_b_9b75() {
      f_cp_a_240da2.repaint();
   }

   public static synchronized void m_b_13462e(int var0) {
      if (f_cp_b_49 != var0) {
         int var1 = f_cp_b_49;
         f_cp_b_49 = var0;
         if (var0 < var1) {
            f_cp_a_240da2.repaint();
         } else {
            f_cp_a_240da2.repaint(0, f_cp_a_240da2.getHeight() - f_cp_a_49 - 2, f_cp_a_240da2.getWidth(), f_cp_a_49 + 2);
         }
      }
   }

   public static synchronized void m_c_9b75() {
      f_cp_a_240da2.removeCommand(f_cp_a_1570d10e);
      if (!f_cp_b_5a) {
         f_cp_b_5a = true;
         m_a_aad3b1ff(C_bt.m_a_e96ea081("l3"));
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cp_a_240da2);
         (f_cp_b_f6e8e442 = new Timer()).schedule(new C_cm(1), 20000L, 20000L);
         m_b_13462e(0);
         Jimm.f_jimm_Jimm_g_5a = true;
      }
   }

   public static synchronized void m_d_9b75() {
      if (f_cp_b_5a) {
         f_cp_b_5a = false;
         f_cp_c_49 = 0;
         if (f_cp_b_f6e8e442 != null) {
            f_cp_b_f6e8e442.cancel();
         }
      }

      C_w.m_a_9b75();
   }

   public static boolean m_a_9b79() {
      return f_cp_b_5a;
   }

   protected final void hideNotify() {
      f_cp_a_9b93e07e = null;
   }

   public static synchronized void m_e_9b75() {
      if (f_cp_b_5a) {
         f_cp_c_49++;
         f_cp_a_240da2.repaint();
      }
   }

   protected final void keyPressed(int var1) {
      if (f_cp_b_5a) {
         if (var1 != 35 && var1 != 42) {
            if (f_cp_a_f6e8e442 != null) {
               f_cp_a_f6e8e442.cancel();
            }

            f_cp_a_5a = true;
            this.repaint();
         } else {
            f_cp_a_4a = System.currentTimeMillis();
         }
      }

      C_bu.m_a_9b75();
   }

   private static void p_cp_g_9b75() {
      if (C_bq.m_a_47921032(38).length() > 0) {
         C_az.m_a_51c0f892(Jimm.f_jimm_Jimm_a_4a58c677.getCurrent());
      } else {
         m_d_9b75();
         f_cp_a_4a = 0L;
      }
   }

   private static void p_cp_c_13462e(int var0) {
      if (f_cp_b_5a) {
         if (var0 != 35 && var0 != 42) {
            f_cp_a_4a = 0L;
         } else {
            if (f_cp_a_4a != 0L && System.currentTimeMillis() - f_cp_a_4a > 900L) {
               p_cp_g_9b75();
            }
         }
      }
   }

   protected final void keyReleased(int var1) {
      p_cp_c_13462e(var1);
   }

   protected final void keyRepeated(int var1) {
      p_cp_c_13462e(var1);
   }

   protected final void pointerPressed(int var1, int var2) {
      if (f_cp_b_5a) {
         if (var2 > this.getHeight() - f_cp_a_49 - 3) {
            p_cp_g_9b75();
         }
      }
   }

   protected final void paint(Graphics var1) {
      int var2;
      int var3 = C_bf.m_c_134621(var2 = C_bq.m_a_134621(115));
      int var4 = this.getHeight() - f_cp_a_49;
      if (var1.getClipY() < var4 - 2) {
         var1.setColor(var2);
         var1.fillRect(0, 0, this.getWidth(), this.getHeight());
         Image var5;
         if ((var5 = p_cp_a_5017a17f()) != null) {
            var1.drawImage(var5, this.getWidth() / 2, this.getHeight() / 2, 3);
         } else {
            var1.setColor(var3);
            var1.setFont(f_cp_a_67f45fce);
            var1.drawString("jimm", this.getWidth() / 2, this.getHeight() / 2 + 5, 65);
            var1.setFont(f_cp_b_67f45fce);
         }

         if (f_cp_b_9b93e07e != null) {
            var1.drawImage(f_cp_b_9b93e07e, this.getWidth() / 2, 2, 17);
         }

         if (f_cp_b_5a && f_cp_c_49 > 0) {
            int var6 = this.getHeight() - 2 * f_cp_a_49;
            C_w.f_w_a_12a00.m_a_485a59b9(14).m_a_11c44857(var1, 1, var6 - 9);
            var1.setColor(var3);
            var1.setFont(f_cp_b_67f45fce);
            var1.drawString("# " + f_cp_c_49, C_w.f_w_a_12a00.m_a_485a59b9(14).f_f_c_49 + 4, var6 - 5, 20);
         }

         var1.setColor(var3);
         var1.setFont(f_cp_b_67f45fce);
         var1.drawString(C_cf.m_a_2416688b(false, false), this.getWidth() / 2, 12, 17);
         var1.drawString(C_cf.m_a_73cf11cb(), this.getWidth() / 2, 13 + f_cp_b_67f45fce.getHeight(), 17);
         if (f_cp_a_5a) {
            int var8 = this.getWidth() / 10 << 3;
            int var10000 = Font.getFont(0, C_bq.f_bq_g_49, 0).getHeight();
            String var10001 = C_bt.m_a_e96ea081("j3");
            int var10002 = var8 - 8;
            boolean var10 = false;
            int var13 = var10002;
            String var11 = var10001;
            C_t var7;
            (var7 = new C_t(null)).m_b_13462e(0);
            var7.m_a_ba2c33f(var11, 0, 0, -1, var13);
            int var9 = var10000 * var7.m_a_9b68() + 8;
            var13 = this.getWidth() / 2 - (this.getWidth() / 10 << 2);
            int var17 = this.getHeight() / 2 - var9 / 2;
            var1.setColor(var3);
            var1.fillRect(var13, var17, var8, var9);
            var1.setColor(var2);
            var1.drawRect(var13 + 2, var17 + 2, var8 - 5, var9 - 5);
            C_t.m_a_60d6f02(var1, C_bt.m_a_e96ea081("j3"), var13 + 4, var17 + 4, var8 - 8, var9 - 8, 0, C_bq.f_bq_g_49, C_bf.m_c_134621(var3));
            (f_cp_a_f6e8e442 = new Timer()).schedule(new C_cm(2), 2000L);
         }
      }

      var1.setColor(var3);
      var1.setStrokeStyle(1);
      var1.drawLine(0, var4 - 3, this.getWidth(), var4 - 3);
      var1.setColor(var3);
      var1.setFont(f_cp_b_67f45fce);
      C_f var12 = null;
      int var15 = 0;
      if (f_cp_d_49 != -1) {
         var15 = (var12 = C_w.f_w_a_12a00.m_a_485a59b9(f_cp_d_49)).f_f_c_49;
      }

      int var18 = 0;
      int var20 = this.getWidth() / 2 + var15 / 2;
      int var21 = this.getWidth() / 2 - f_cp_b_67f45fce.stringWidth(f_cp_a_523beb0a) / 2 + var15 / 2;
      if (f_cp_a_129e1 != null && f_cp_a_129e1 != C_x.m_a_485a59b9(37) && this.getWidth() > 129) {
         C_f var19 = f_cp_a_129e1;
         var18 = f_cp_a_129e1.f_f_c_49;
      }

      var1.drawString(f_cp_a_523beb0a, var20, this.getHeight(), 33);
      if (var18 != 0) {
         f_cp_a_129e1.m_c_11c44857(var1, var21, this.getHeight() - f_cp_a_49 / 2);
      }

      if (var12 != null) {
         var12.m_c_11c44857(var1, var21 - var18, this.getHeight() - f_cp_a_49 / 2);
      }

      if ((var15 = this.getWidth() * f_cp_b_49 / 100) >= 1) {
         var1.setClip(0, var4 - 2, var15, f_cp_a_49 + 2);
         var1.setColor(var3);
         var1.fillRect(0, var4 - 2, var15, f_cp_a_49 + 2);
         var1.setColor(var2);
         var1.drawString(f_cp_a_523beb0a, var20, this.getHeight(), 33);
         if (var18 != 0) {
            f_cp_a_129e1.m_c_11c44857(var1, var21, this.getHeight() - f_cp_a_49 / 2);
         }

         if (var12 != null) {
            var12.m_c_11c44857(var1, var21 - var18, this.getHeight() - f_cp_a_49 / 2);
         }
      }
   }

   public static void m_f_9b75() {
      if (f_cp_d_49 != 8) {
         new Timer().schedule(new C_cm(3), 15000L);
      }
   }

   public static void m_a_335e07a5(String var0, C_ba var1, boolean var2) {
      if (f_cp_b_f6e8e442 != null) {
         f_cp_b_f6e8e442.cancel();
         f_cp_b_f6e8e442 = null;
      }

      f_cp_b_5a = false;
      C_cm var3 = new C_cm(var1);
      f_cp_a_240da2.removeCommand(f_cp_a_1570d10e);
      if (var2) {
         f_cp_a_240da2.addCommand(f_cp_a_1570d10e);
         f_cp_a_240da2.setCommandListener(var3);
      }

      m_a_aad3b1ff(C_bt.m_a_e96ea081(var0));
      m_b_13462e(0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cp_a_240da2);
      Jimm.m_a_94e56161().schedule(var3, 1000L, 1000L);
   }

   static {
      Font.getFont(0, C_bq.f_bq_g_49, 8);

      try {
         f_cp_b_9b93e07e = Image.createImage("/notice.png");
      } catch (IOException var0) {
      }

      f_cp_d_49 = -1;
      f_cp_a_129e1 = null;
   }
}
