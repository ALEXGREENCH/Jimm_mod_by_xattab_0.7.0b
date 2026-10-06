package recovered;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

final class C_bx extends Canvas implements Runnable {
   C_be f_bx_a_24088c;
   private Timer f_bx_a_f6e8e442 = new Timer();
   private TimerTask f_bx_a_d50713bd;
   private int f_bx_a_49;
   Display f_bx_a_4a58c677;

   public C_bx() {
      this.setFullScreenMode(true);
   }

   protected final void paint(Graphics var1) {
      if (this.f_bx_a_24088c != null) {
         this.f_bx_a_24088c.m_a_272d79b7(var1);
      }
   }

   protected final void showNotify() {
      this.m_a_9b75();
      this.setFullScreenMode(true);
      if (this.f_bx_a_24088c != null) {
         this.f_bx_a_24088c.m_d_9b75();
      }
   }

   protected final void hideNotify() {
      this.m_a_9b75();
   }

   public final void run() {
      if (this.f_bx_a_d50713bd != null) {
         this.f_bx_a_24088c.m_f_13462e(this.f_bx_a_49);
      }
   }

   protected final void keyPressed(int var1) {
      this.m_a_9b75();
      if (this.f_bx_a_24088c != null) {
         this.f_bx_a_24088c.m_e_13462e(var1);
      }

      this.f_bx_a_49 = var1;
      this.f_bx_a_d50713bd = new C_bl(this);
      this.f_bx_a_f6e8e442.schedule(this.f_bx_a_d50713bd, 500L, 75L);
   }

   protected final void keyReleased(int var1) {
      this.m_a_9b75();
   }

   final void m_a_9b75() {
      if (this.f_bx_a_d50713bd != null) {
         this.f_bx_a_d50713bd.cancel();
      }

      this.f_bx_a_49 = 0;
      this.f_bx_a_d50713bd = null;
   }
}
