package recovered;

import java.io.IOException;
import java.util.Timer;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jimm.Jimm;

public final class C_cn extends Canvas {
   public static C_cn f_cn_a_240d64;
   public static final Command f_cn_a_1570d10e = new Command(C_bs.m_a_e96ea081("3"), 2, 1);
   private static Timer f_cn_a_f6e8e442;
   private static Timer f_cn_b_f6e8e442;
   private static Image f_cn_a_9b93e07e;
   private static Image f_cn_b_9b93e07e = null;
   private static Image f_cn_c_9b93e07e;
   private static Font f_cn_a_67f45fce = Font.getFont(0, 1 + C_bp.f_bp_g_49, 16);
   private static Font f_cn_b_67f45fce;
   private static int f_cn_a_49 = (f_cn_b_67f45fce = Font.getFont(0, C_bp.f_bp_g_49, 8)).getHeight();
   private static String f_cn_a_523beb0a;
   private static int f_cn_b_49;
   private static boolean f_cn_b_5a;
   private static int f_cn_c_49;
   public static long f_cn_a_4a;
   protected static boolean f_cn_a_5a;
   private static int f_cn_d_49;
   private static C_f f_cn_a_129e1;

   private static Image p_cn_a_5017a17f() {
      if (f_cn_b_9b93e07e == null) {
         try {
            f_cn_b_9b93e07e = Image.createImage("/batt.png");
         } catch (IOException var0) {
         }
      }

      return f_cn_b_9b93e07e;
   }

   public C_cn(String var1) {
      f_cn_a_240d64 = this;
      this.setFullScreenMode(true);
      m_a_aad3b1ff(var1);
      f_cn_a_5a = false;
   }

   public C_cn() {
      this(null);
   }

   public static synchronized void m_a_aad3b1ff(String var0) {
      f_cn_a_523beb0a = new String(var0);
      f_cn_a_240d64.repaint();
   }

   public static synchronized void m_a_13462e(int var0) {
      f_cn_d_49 = var0;
   }

   public static synchronized void m_a_48a013c6(C_f var0) {
      f_cn_a_129e1 = var0;
   }

   private static Image p_cn_b_5017a17f() {
      if (f_cn_a_9b93e07e == null) {
         try {
            f_cn_a_9b93e07e = Image.createImage("/logo.png");
         } catch (Exception var0) {
            f_cn_a_9b93e07e = null;
         }
      }

      return f_cn_a_9b93e07e;
   }

   public static void m_a_9b75() {
      if (f_cn_b_f6e8e442 != null) {
         f_cn_b_f6e8e442.cancel();
         f_cn_b_f6e8e442 = null;
      }

      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cn_a_240d64);
   }

   public static void m_a_8eb9d703(Command var0) {
      f_cn_a_240d64.addCommand(var0);
   }

   public static void m_b_8eb9d703(Command var0) {
      f_cn_a_240d64.removeCommand(var0);
   }

   public static void m_a_6f63a2af(CommandListener var0) {
      f_cn_a_240d64.setCommandListener(var0);
   }

   public static void m_b_9b75() {
      f_cn_a_240d64.repaint();
   }

   public static synchronized void m_b_13462e(int var0) {
      if (f_cn_b_49 != var0) {
         int var1 = f_cn_b_49;
         f_cn_b_49 = var0;
         if (var0 < var1) {
            f_cn_a_240d64.repaint();
         } else {
            f_cn_a_240d64.repaint(0, f_cn_a_240d64.getHeight() - f_cn_a_49 - 2, f_cn_a_240d64.getWidth(), f_cn_a_49 + 2);
         }
      }
   }

   public static synchronized void m_c_9b75() {
      f_cn_a_240d64.removeCommand(f_cn_a_1570d10e);
      if (!f_cn_b_5a) {
         f_cn_b_5a = true;
         m_a_aad3b1ff(C_bs.m_a_e96ea081("j3"));
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cn_a_240d64);
         (f_cn_b_f6e8e442 = new Timer()).schedule(new C_ck(1), 20000L, 20000L);
         m_b_13462e(0);
         Jimm.f_jimm_Jimm_c_5a = true;
      }
   }

   public static synchronized void m_d_9b75() {
      if (f_cn_b_5a) {
         f_cn_b_5a = false;
         f_cn_c_49 = 0;
         if (f_cn_b_f6e8e442 != null) {
            f_cn_b_f6e8e442.cancel();
         }
      }

      C_w.m_a_9b75();
   }

   public static boolean m_a_9b79() {
      return f_cn_b_5a;
   }

   protected final void hideNotify() {
      f_cn_a_9b93e07e = null;
   }

   public static synchronized void m_e_9b75() {
      if (f_cn_b_5a) {
         f_cn_c_49++;
         f_cn_a_240d64.repaint();
      }
   }

   protected final void keyPressed(int var1) {
      if (f_cn_b_5a) {
         if (var1 == 35 || var1 == 42) {
            f_cn_a_4a = System.currentTimeMillis();
            return;
         }

         if (f_cn_a_f6e8e442 != null) {
            f_cn_a_f6e8e442.cancel();
         }

         f_cn_a_5a = true;
         this.repaint();
      }
   }

   private static void p_cn_c_13462e(int var0) {
      if (f_cn_b_5a) {
         if (var0 != 35 && var0 != 42) {
            f_cn_a_4a = 0L;
         } else {
            if (f_cn_a_4a != 0L && System.currentTimeMillis() - f_cn_a_4a > 900L) {
               if (C_bp.m_a_47921032(38).length() > 0) {
                  C_av.m_a_51c0f892(Jimm.f_jimm_Jimm_a_4a58c677.getCurrent());
                  return;
               }

               m_d_9b75();
               f_cn_a_4a = 0L;
            }
         }
      }
   }

   protected final void keyReleased(int var1) {
      p_cn_c_13462e(var1);
   }

   protected final void keyRepeated(int var1) {
      p_cn_c_13462e(var1);
   }

   protected final void paint(Graphics var1) {
      int var2;
      int var3 = C_be.m_c_134621(var2 = C_bp.m_a_134621(115));
      int var4 = this.getHeight() - f_cn_a_49;
      if (var1.getClipY() < var4 - 2) {
         var1.setColor(var2);
         var1.fillRect(0, 0, this.getWidth(), this.getHeight());
         Image var5;
         if ((var5 = p_cn_b_5017a17f()) != null) {
            var1.drawImage(var5, this.getWidth() / 2, this.getHeight() / 2, 3);
         } else {
            var1.setColor(var3);
            var1.setFont(f_cn_a_67f45fce);
            var1.drawString("jimm", this.getWidth() / 2, this.getHeight() / 2 + 5, 65);
            var1.setFont(f_cn_b_67f45fce);
         }

         if (f_cn_c_9b93e07e != null) {
            var1.drawImage(f_cn_c_9b93e07e, this.getWidth() / 2, 2, 17);
         }

         if (f_cn_b_5a && f_cn_c_49 > 0) {
            int var6 = this.getHeight() - 2 * f_cn_a_49;
            C_w.f_w_a_12a1f.m_a_485a59b9(14).m_a_11c44857(var1, 1, var6 - 9);
            var1.setColor(var3);
            var1.setFont(f_cn_b_67f45fce);
            var1.drawString("# " + f_cn_c_49, C_w.f_w_a_12a1f.m_a_485a59b9(14).f_f_c_49 + 4, var6 - 5, 20);
         }

         String var13;
         if ((var13 = System.getProperty("MPJC_CAP")) != null && f_cn_b_5a) {
            String var14 = var13 + "%";
            int var7 = this.getWidth() - f_cn_b_67f45fce.stringWidth(var14) - 1;
            int var8 = this.getHeight() - 2 * f_cn_a_49;
            if (p_cn_a_5017a17f() != null) {
               var1.drawImage(p_cn_a_5017a17f(), var7 - p_cn_a_5017a17f().getWidth() - 1, var8 - 9, 20);
            }

            var1.setColor(var3);
            var1.setFont(f_cn_b_67f45fce);
            var1.drawString(var14, var7, var8 - 5, 20);
         }

         var1.setColor(var3);
         var1.setFont(f_cn_b_67f45fce);
         var1.drawString(C_ce.m_a_2416688b(false, false), this.getWidth() / 2, 12, 17);
         var1.drawString(C_ce.m_a_73cf11cb(), this.getWidth() / 2, 13 + f_cn_b_67f45fce.getHeight(), 17);
         if (f_cn_a_5a) {
            int var15 = this.getWidth() / 10 << 3;
            int var10000 = Font.getFont(0, C_bp.f_bp_g_49, 0).getHeight();
            String var10001 = C_bs.m_a_e96ea081("h3");
            int var10002 = var15 - 8;
            boolean var10 = false;
            int var18 = var10002;
            String var11 = var10001;
            C_t var22;
            (var22 = new C_t(null)).m_b_13462e(0);
            var22.m_a_ba2c33f(var11, 0, 0, -1, var18);
            int var9 = var10000 * var22.m_a_9b68() + 8;
            var18 = this.getWidth() / 2 - (this.getWidth() / 10 << 2);
            int var23 = this.getHeight() / 2 - var9 / 2;
            var1.setColor(var3);
            var1.fillRect(var18, var23, var15, var9);
            var1.setColor(var2);
            var1.drawRect(var18 + 2, var23 + 2, var15 - 5, var9 - 5);
            C_t.m_a_60d6f02(var1, C_bs.m_a_e96ea081("h3"), var18 + 4, var23 + 4, var15 - 8, var9 - 8, 0, C_bp.f_bp_g_49, C_be.m_c_134621(var3));
            (f_cn_a_f6e8e442 = new Timer()).schedule(new C_ck(2), 2000L);
         }
      }

      var1.setColor(var3);
      var1.setStrokeStyle(1);
      var1.drawLine(0, var4 - 3, this.getWidth(), var4 - 3);
      var1.setColor(var3);
      var1.setFont(f_cn_b_67f45fce);
      C_f var12 = null;
      int var16 = 0;
      if (f_cn_d_49 != -1) {
         var16 = (var12 = C_w.f_w_a_12a1f.m_a_485a59b9(f_cn_d_49)).f_f_c_49;
      }

      int var20 = 0;
      int var24 = this.getWidth() / 2 + var16 / 2;
      int var17 = this.getWidth() / 2 - f_cn_b_67f45fce.stringWidth(f_cn_a_523beb0a) / 2 + var16 / 2;
      if (f_cn_a_129e1 != null && f_cn_a_129e1 != C_x.m_a_485a59b9(37) && this.getWidth() > 129) {
         C_f var21 = f_cn_a_129e1;
         var20 = f_cn_a_129e1.f_f_c_49;
      }

      var1.drawString(f_cn_a_523beb0a, var24, this.getHeight(), 33);
      if (var20 != 0) {
         f_cn_a_129e1.m_c_11c44857(var1, var17, this.getHeight() - f_cn_a_49 / 2);
      }

      if (var12 != null) {
         var12.m_c_11c44857(var1, var17 - var20, this.getHeight() - f_cn_a_49 / 2);
      }

      int var25;
      if ((var25 = this.getWidth() * f_cn_b_49 / 100) >= 1) {
         var1.setClip(0, var4 - 2, var25, f_cn_a_49 + 2);
         var1.setColor(var3);
         var1.fillRect(0, var4 - 2, var25, f_cn_a_49 + 2);
         var1.setColor(var2);
         var1.drawString(f_cn_a_523beb0a, var24, this.getHeight(), 33);
         if (var20 != 0) {
            f_cn_a_129e1.m_c_11c44857(var1, var17, this.getHeight() - f_cn_a_49 / 2);
         }

         if (var12 != null) {
            var12.m_c_11c44857(var1, var17 - var20, this.getHeight() - f_cn_a_49 / 2);
         }
      }
   }

   public static void m_f_9b75() {
      if (f_cn_d_49 != 8) {
         new Timer().schedule(new C_ck(3), 15000L);
      }
   }

   public static void m_a_33097a9f(String var0, C_az var1, boolean var2) {
      if (f_cn_b_f6e8e442 != null) {
         f_cn_b_f6e8e442.cancel();
         f_cn_b_f6e8e442 = null;
      }

      f_cn_b_5a = false;
      C_ck var3 = new C_ck(var1);
      f_cn_a_240d64.removeCommand(f_cn_a_1570d10e);
      if (var2) {
         f_cn_a_240d64.addCommand(f_cn_a_1570d10e);
         f_cn_a_240d64.setCommandListener(var3);
      }

      m_a_aad3b1ff(C_bs.m_a_e96ea081(var0));
      m_b_13462e(0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cn_a_240d64);
      Jimm.m_a_94e56161().schedule(var3, 1000L, 1000L);
   }

   static {
      Font.getFont(0, C_bp.f_bp_g_49, 8);

      try {
         f_cn_c_9b93e07e = Image.createImage("/notice.png");
      } catch (IOException var0) {
      }

      f_cn_d_49 = -1;
      f_cn_a_129e1 = null;
   }
}
