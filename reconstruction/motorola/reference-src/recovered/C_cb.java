package recovered;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public final class C_cb implements Runnable {
   private SocketConnection f_cb_a_b4b0cc51;
   private InputStream f_cb_a_91ffb459;
   private OutputStream f_cb_a_33c83ab2;
   private volatile boolean f_cb_a_5a;
   private volatile Thread f_cb_a_72a5fc31;
   private Vector f_cb_a_48a69a2c;

   public final synchronized void m_a_aad3b1ff(String var1) {
      try {
         this.f_cb_a_b4b0cc51 = (SocketConnection)Connector.open("socket://" + var1, 3);
         this.f_cb_a_91ffb459 = this.f_cb_a_b4b0cc51.openInputStream();
         this.f_cb_a_33c83ab2 = this.f_cb_a_b4b0cc51.openOutputStream();
         this.f_cb_a_5a = false;
         this.f_cb_a_72a5fc31 = new Thread(this);
         this.f_cb_a_72a5fc31.start();
      } catch (ConnectionNotFoundException var2) {
         throw new C_aq(126, 0, true, true);
      } catch (IllegalArgumentException var3) {
         throw new C_aq(127, 0, true, true);
      } catch (IOException var4) {
         throw new C_aq(125, 0, true, true);
      }
   }

   public final synchronized void m_a_9b75() {
      this.f_cb_a_5a = true;

      try {
         this.f_cb_a_91ffb459.close();
      } catch (Exception var24) {
      } finally {
         this.f_cb_a_91ffb459 = null;
      }

      try {
         this.f_cb_a_33c83ab2.close();
      } catch (Exception var22) {
      } finally {
         this.f_cb_a_33c83ab2 = null;
      }

      try {
         this.f_cb_a_b4b0cc51.close();
      } catch (Exception var20) {
      } finally {
         this.f_cb_a_b4b0cc51 = null;
      }

      Thread.yield();
   }

   public final synchronized int m_a_9b68() {
      return this.f_cb_a_48a69a2c == null ? 0 : this.f_cb_a_48a69a2c.size();
   }

   public final C_cc m_a_46a7a890() {
      byte[] var1;
      synchronized (this.f_cb_a_48a69a2c) {
         if (this.f_cb_a_48a69a2c.size() == 0) {
            return null;
         }

         var1 = (byte[])this.f_cb_a_48a69a2c.elementAt(0);
         this.f_cb_a_48a69a2c.removeElementAt(0);
      }

      return C_cc.m_a_3c792e49(var1, 0, var1.length);
   }

   public final void m_a_cb4a8bc4(C_cc var1) {
      if (this.f_cb_a_33c83ab2 == null) {
         throw new C_aq(128, 0, true, true);
      } else {
         synchronized (this.f_cb_a_33c83ab2) {
            try {
               byte[] var5 = var1.m_a_12d408();
               this.f_cb_a_33c83ab2.write(var5);
               this.f_cb_a_33c83ab2.flush();
               C_as.m_b_13462e(var5.length + 51);
            } catch (IOException var3) {
               this.m_a_9b75();
            }
         }
      }
   }

   public final int m_b_9b68() {
      try {
         return this.f_cb_a_b4b0cc51.getLocalPort();
      } catch (IOException var1) {
         return 0;
      }
   }

   public final byte[] m_a_12d408() {
      try {
         return C_cf.m_c_afa28ebe(this.f_cb_a_b4b0cc51.getLocalAddress());
      } catch (IOException var1) {
         return new byte[4];
      }
   }

   public final void run() {
      byte[] var1 = new byte[2];
      synchronized (this) {
         this.f_cb_a_48a69a2c = new Vector();
      }

      try {
         while (!this.f_cb_a_5a) {
            int var4 = 0;
            if (C_bp.m_a_134621(64) == 1) {
               while (this.f_cb_a_91ffb459.available() == 0) {
                  Thread.sleep(250L);
               }

               if (this.f_cb_a_91ffb459 == null) {
                  break;
               }
            }

            int var3;
            while ((var3 = this.f_cb_a_91ffb459.read(var1, var4, var1.length - var4)) != -1 && (var4 += var3) < var1.length) {
            }

            if (var3 == -1) {
               break;
            }

            byte[] var2 = new byte[C_cf.m_a_e306d820(var1, 0, false)];
            var4 = 0;

            while ((var3 = this.f_cb_a_91ffb459.read(var2, var4, var2.length - var4)) != -1 && (var4 += var3) < var2.length) {
            }

            if (var3 == -1) {
               break;
            }

            C_as.m_a_13462e(var4 + 53);
            synchronized (this.f_cb_a_48a69a2c) {
               this.f_cb_a_48a69a2c.addElement(var2);
            }

            synchronized (C_ac.m_a_810c345d()) {
               C_ac.m_a_810c345d().notify();
            }
         }
      } catch (NullPointerException var8) {
         if (!this.f_cb_a_5a) {
            C_aq.m_a_481c933f(new C_aq(125, 3, true, true));
         }
      } catch (InterruptedException var9) {
      } catch (IOException var10) {
         if (!this.f_cb_a_5a) {
            C_aq.m_a_481c933f(new C_aq(125, 1, true, true));
         }
      }
   }
}
