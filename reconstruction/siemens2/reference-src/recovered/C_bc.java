package recovered;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

public final class C_bc extends Canvas implements CommandListener {
   private Player f_bc_a_2fd8b6bf;
   private VideoControl f_bc_a_fbb20f6c;
   private boolean f_bc_a_5a;
   private boolean f_bc_b_5a;
   private Image f_bc_a_9b93e07e;
   private byte[] f_bc_a_b47;
   private Command f_bc_a_1570d10e;
   private Command f_bc_b_1570d10e;
   private final C_ab f_bc_a_24046e;

   public C_bc(C_ab var1) {
      this.f_bc_a_24046e = var1;
      this.f_bc_a_2fd8b6bf = null;
      this.f_bc_a_fbb20f6c = null;
      this.f_bc_a_5a = false;
      this.f_bc_b_5a = true;
      this.f_bc_a_1570d10e = new Command(C_bs.m_a_e96ea081("4"), 2, 2);
      this.f_bc_b_1570d10e = new Command(C_bs.m_a_e96ea081("6"), 1, 1);
      this.addCommand(this.f_bc_a_1570d10e);
      this.addCommand(this.f_bc_b_1570d10e);
      this.setCommandListener(this);
   }

   private void p_bc_b_9b75() {
      this.f_bc_a_9b93e07e = null;
      if (this.f_bc_a_fbb20f6c != null) {
         this.f_bc_a_fbb20f6c.setVisible(false);
         this.f_bc_a_fbb20f6c = null;
      }

      if (this.f_bc_a_2fd8b6bf != null) {
         try {
            if (this.f_bc_a_2fd8b6bf.getState() == 400) {
               this.f_bc_a_2fd8b6bf.stop();
            }

            this.f_bc_a_2fd8b6bf.deallocate();
            this.f_bc_a_2fd8b6bf.close();
         } catch (Exception var1) {
         }

         this.f_bc_a_2fd8b6bf = null;
      }

      System.gc();
   }

   protected final void paint(Graphics var1) {
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      var1.setColor(-1);
      var1.fillRect(0, 0, var2, var3);
      if (!this.f_bc_b_5a && this.f_bc_a_9b93e07e != null) {
         var1.drawImage(this.f_bc_a_9b93e07e, var2 / 2, var3 / 2, 3);
      }

      var1.setColor(0);
      if (!this.f_bc_b_5a) {
         var1.drawString(C_bs.m_a_e96ea081("05") + "? ", var2 / 2, 1, 17);
      }
   }

   private void p_bc_a_aad3b1ff(String var1) {
      this.f_bc_a_2fd8b6bf = Manager.createPlayer(var1);
      this.f_bc_a_2fd8b6bf.realize();
      this.f_bc_a_fbb20f6c = (VideoControl)this.f_bc_a_2fd8b6bf.getControl("VideoControl");
   }

   public final synchronized void m_a_9b75() {
      this.p_bc_b_9b75();
      if (!this.f_bc_a_5a) {
         try {
            try {
               this.p_bc_a_aad3b1ff("capture://image");
            } catch (Exception var5) {
               this.p_bc_a_aad3b1ff("capture://video");
            }

            if (this.f_bc_a_fbb20f6c != null) {
               this.f_bc_a_fbb20f6c.initDisplayMode(1, this);
               int var1 = this.getWidth();
               int var2 = this.getHeight();

               try {
                  this.f_bc_a_fbb20f6c.setDisplayLocation(2, 2);
                  this.f_bc_a_fbb20f6c.setDisplaySize(var1 - 4, var2 - 4);
               } catch (MediaException var4) {
                  try {
                     this.f_bc_a_fbb20f6c.setDisplayFullScreen(true);
                  } catch (MediaException var3) {
                  }
               }

               this.f_bc_a_fbb20f6c.setVisible(true);
               this.f_bc_a_2fd8b6bf.start();
               this.f_bc_a_5a = true;
               return;
            }

            C_aq.m_a_481c933f(new C_aq(180, 0, true));
            return;
         } catch (IOException var6) {
            this.p_bc_b_9b75();
            C_aq.m_a_481c933f(new C_aq(181, 0, true));
            return;
         } catch (MediaException var7) {
            this.p_bc_b_9b75();
            C_aq.m_a_481c933f(new C_aq(181, 1, true));
            return;
         } catch (SecurityException var8) {
            this.p_bc_b_9b75();
            C_aq.m_a_481c933f(new C_aq(181, 2, true));
         }
      }
   }

   private byte[] p_bc_a_afa28ebe(String var1) {
      try {
         return this.f_bc_a_fbb20f6c.getSnapshot(var1);
      } catch (Exception var2) {
         return null;
      }
   }

   private void p_bc_c_9b75() {
      if (this.f_bc_a_2fd8b6bf != null) {
         this.f_bc_a_b47 = this.p_bc_a_afa28ebe("encoding=jpeg&width=320&height=240");
         if (this.f_bc_a_b47 == null) {
            this.f_bc_a_b47 = this.p_bc_a_afa28ebe("JPEG");
         }

         if (this.f_bc_a_b47 == null) {
            this.f_bc_a_b47 = this.p_bc_a_afa28ebe(null);
         }

         if (this.f_bc_a_b47 == null) {
            C_aq.m_a_481c933f(new C_aq(183, 0, true));
         }

         this.f_bc_b_5a = false;
         this.p_bc_d_9b75();
         this.f_bc_a_9b93e07e = Image.createImage(this.f_bc_a_b47, 0, this.f_bc_a_b47.length);
         this.f_bc_a_9b93e07e = C_ce.m_a_622be521(this.f_bc_a_9b93e07e, this.getWidth(), this.getHeight());
         this.f_bc_a_fbb20f6c.setVisible(false);
         this.repaint();
      }
   }

   private synchronized void p_bc_d_9b75() {
      if (this.f_bc_a_5a) {
         try {
            this.f_bc_a_fbb20f6c.setVisible(false);
            this.f_bc_a_2fd8b6bf.stop();
         } catch (Exception var1) {
            this.p_bc_b_9b75();
         }

         this.f_bc_a_5a = false;
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_bc_b_1570d10e) {
         if (!this.f_bc_b_5a) {
            this.p_bc_e_9b75();
         } else {
            this.p_bc_c_9b75();
         }
      } else {
         if (var1 == this.f_bc_a_1570d10e) {
            if (!this.f_bc_b_5a) {
               this.f_bc_b_5a = true;
               this.f_bc_a_5a = false;
               this.m_a_9b75();
               return;
            }

            this.p_bc_d_9b75();
            this.p_bc_b_9b75();
            C_w.m_a_9b75();
            this.f_bc_a_24046e.f_ab_a_24084e = null;
         }
      }
   }

   private void p_bc_e_9b75() {
      this.p_bc_d_9b75();
      this.p_bc_b_9b75();
      this.f_bc_a_24046e.m_a_b3296085(new ByteArrayInputStream(this.f_bc_a_b47), this.f_bc_a_b47.length);
      this.f_bc_a_24046e.m_a_e925fa09("jimm_cam" + C_ce.m_a_9b68() + ".jpeg", "");
   }

   public final void keyPressed(int var1) {
      if (this.getGameAction(var1) == 8) {
         if (!this.f_bc_b_5a) {
            this.p_bc_e_9b75();
            return;
         }

         this.p_bc_c_9b75();
      }
   }
}
