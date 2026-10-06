package recovered;

/** 0.6 source correspondence (inferred): DrawControls.VirtualCanvas. Release class: by. */

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

final class C_by extends Canvas implements Runnable {
   C_bf f_by_a_2408ab;
   private Timer f_by_a_f6e8e442 = new Timer();
   private TimerTask f_by_a_d50713bd;
   private int f_by_a_49;
   Display f_by_a_4a58c677;

   public C_by() {
      this.setFullScreenMode(true);
   }

   protected final void paint(Graphics var1) {
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_a_272d79b7(var1);
      }
   }

   protected final void showNotify() {
      this.m_a_9b75();
      this.setFullScreenMode(true);
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_e_9b75();
      }
   }

   protected final void hideNotify() {
      this.m_a_9b75();
   }

   public final void run() {
      if (this.f_by_a_d50713bd != null) {
         this.f_by_a_2408ab.m_f_13462e(this.f_by_a_49);
      }
   }

   protected final void keyPressed(int var1) {
      this.m_a_9b75();
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_e_13462e(var1);
      }

      this.f_by_a_49 = var1;
      this.f_by_a_d50713bd = new C_bl(this);
      this.f_by_a_f6e8e442.schedule(this.f_by_a_d50713bd, 500L, 75L);
   }

   protected final void keyReleased(int var1) {
      this.m_a_9b75();
   }

   final void m_a_9b75() {
      if (this.f_by_a_d50713bd != null) {
         this.f_by_a_d50713bd.cancel();
      }

      this.f_by_a_49 = 0;
      this.f_by_a_d50713bd = null;
   }

   protected final void pointerDragged(int var1, int var2) {
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_g_13462e(var2);
      }
   }

   protected final void pointerPressed(int var1, int var2) {
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_a_255f295(var1, var2);
      }
   }

   protected final void pointerReleased(int var1, int var2) {
      if (this.f_by_a_2408ab != null) {
         this.f_by_a_2408ab.m_b_255f295(var1, var2);
      }
   }
}
