package recovered;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

final class C_by extends Canvas implements Runnable {
   C_bd f_by_a_24086d;
   private Timer f_by_a_f6e8e442 = new Timer();
   private TimerTask f_by_a_d50713bd;
   private int f_by_a_49;
   Display f_by_a_4a58c677;

   public C_by() {
      this.setFullScreenMode(true);
   }

   protected final void paint(Graphics var1) {
      if (this.f_by_a_24086d != null) {
         this.f_by_a_24086d.m_a_272d79b7(var1);
      }
   }

   protected final void showNotify() {
      this.m_a_9b75();
      this.setFullScreenMode(true);
      if (this.f_by_a_24086d != null) {
         this.f_by_a_24086d.m_d_9b75();
      }
   }

   protected final void hideNotify() {
      this.m_a_9b75();
   }

   public final void run() {
      if (this.f_by_a_d50713bd != null) {
         this.f_by_a_24086d.m_e_13462e(this.f_by_a_49);
      }
   }

   protected final void keyPressed(int var1) {
      this.m_a_9b75();
      this.f_by_a_49 = var1;
      this.f_by_a_d50713bd = new C_bk(this);
      this.f_by_a_f6e8e442.schedule(this.f_by_a_d50713bd, 500L, 75L);
   }

   protected final void keyReleased(int var1) {
      if (this.f_by_a_24086d != null) {
         this.f_by_a_24086d.m_f_13462e(var1);
      }

      this.m_a_9b75();
   }

   final void m_a_9b75() {
      if (this.f_by_a_d50713bd != null) {
         this.f_by_a_d50713bd.cancel();
      }

      this.f_by_a_49 = 0;
      this.f_by_a_d50713bd = null;
   }
}
