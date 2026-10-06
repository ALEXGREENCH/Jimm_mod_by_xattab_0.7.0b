package recovered;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public final class C_q extends C_bz implements Runnable {
   private SocketConnection f_q_a_b4b0cc51;
   private InputStream f_q_a_91ffb459;
   private OutputStream f_q_a_33c83ab2;
   private int f_q_a_49;

   public C_q(C_ac var1) {
   }

   public final synchronized void m_a_aad3b1ff(String var1) {
      try {
         this.f_q_a_b4b0cc51 = (SocketConnection)Connector.open("socket://" + var1, 3);
         this.f_q_a_91ffb459 = this.f_q_a_b4b0cc51.openInputStream();
         this.f_q_a_33c83ab2 = this.f_q_a_b4b0cc51.openOutputStream();
         super.f_bz_a_5a = false;
         super.f_bz_a_72a5fc31 = new Thread(this);
         super.f_bz_a_72a5fc31.start();
         C_ac.f_ac_a_49 = C_ac.m_b_9b68();
         this.f_q_a_49 = 2;
      } catch (ConnectionNotFoundException var2) {
         throw new C_aq(121, 0);
      } catch (IllegalArgumentException var3) {
         throw new C_aq(122, 0);
      } catch (IOException var4) {
         throw new C_aq(120, 20);
      }
   }

   public final synchronized void m_a_9b75() {
      super.f_bz_a_5a = true;

      try {
         this.f_q_a_91ffb459.close();
      } catch (Exception var24) {
      } finally {
         this.f_q_a_91ffb459 = null;
      }

      try {
         this.f_q_a_33c83ab2.close();
      } catch (Exception var22) {
      } finally {
         this.f_q_a_33c83ab2 = null;
      }

      try {
         this.f_q_a_b4b0cc51.close();
      } catch (Exception var20) {
      } finally {
         this.f_q_a_b4b0cc51 = null;
      }

      Thread.yield();
   }

   public final void m_a_cb4a1765(C_cb var1) {
      C_aq var2;
      if (this.f_q_a_33c83ab2 == null && !C_ac.m_a_cb34b9f8(var2 = new C_aq(123, 0))) {
         throw var2;
      } else {
         synchronized (this.f_q_a_33c83ab2) {
            var1.m_b_13462e(C_ac.m_a_9b68());
            if (var1 instanceof C_ar) {
               C_ar var10000 = (C_ar)var1;
               int var4 = this.f_q_a_49++;
               var10000.f_ar_d_49 = var4;
            }

            try {
               byte[] var8 = var1.m_a_12d408();
               this.f_q_a_33c83ab2.write(var8);
               this.f_q_a_33c83ab2.flush();
               C_as.m_b_13462e(var8.length + 51);
            } catch (IOException var5) {
               this.m_a_9b75();
               C_aq var7;
               if (!C_ac.m_a_cb34b9f8(var7 = new C_aq(120, 3))) {
                  throw var7;
               }
            }
         }
      }
   }

   public final int m_a_9b68() {
      try {
         return this.f_q_a_b4b0cc51.getLocalPort();
      } catch (IOException var1) {
         return 0;
      }
   }

   public final byte[] m_a_12d408() {
      try {
         return C_ce.m_c_afa28ebe(this.f_q_a_b4b0cc51.getLocalAddress());
      } catch (IOException var1) {
         return new byte[4];
      }
   }

   public final void run() {
      byte[] var1 = new byte[6];
      synchronized (this) {
         super.f_bz_a_48a69a2c = new Vector();
      }

      try {
         while (!super.f_bz_a_5a) {
            int var4 = 0;
            if (C_bp.m_a_134621(64) == 1) {
               while (this.f_q_a_91ffb459.available() == 0) {
                  Thread.sleep(250L);
               }

               if (this.f_q_a_91ffb459 == null) {
                  break;
               }
            }

            int var3;
            while ((var3 = this.f_q_a_91ffb459.read(var1, var4, var1.length - var4)) != -1 && (var4 += var3) < var1.length) {
            }

            if (var3 == -1) {
               break;
            }

            if (C_ce.m_a_49634b7a(var1, 0) != 42) {
               throw new C_aq(124, 0);
            }

            byte[] var2 = new byte[C_ce.m_b_49634b7a(var1, 4)];
            var4 = 0;

            while ((var3 = this.f_q_a_91ffb459.read(var2, var4, var2.length - var4)) != -1 && (var4 += var3) < var2.length) {
            }

            if (var3 == -1) {
               break;
            }

            byte[] var15 = new byte[var1.length + var2.length];
            System.arraycopy(var1, 0, var15, 0, var1.length);
            System.arraycopy(var2, 0, var15, var1.length, var2.length);
            C_as.m_a_13462e(var4 + 57);
            synchronized (super.f_bz_a_48a69a2c) {
               super.f_bz_a_48a69a2c.addElement(var15);
            }

            synchronized (C_ac.m_a_810c345d()) {
               C_ac.m_a_810c345d().notify();
            }
         }
      } catch (NullPointerException var8) {
         if (!super.f_bz_a_5a) {
            C_aq.m_a_481c933f(new C_aq(120, 3));
         }

         super.f_bz_a_5a = false;
      } catch (InterruptedException var9) {
      } catch (C_aq var10) {
         if (!C_ac.m_a_cb34b9f8(var10)) {
            C_aq.m_a_481c933f(var10);
         }
      } catch (IOException var11) {
         C_aq var12;
         if (!super.f_bz_a_5a && !C_ac.m_a_cb34b9f8(var12 = new C_aq(120, 1))) {
            C_aq.m_a_481c933f(var12);
         }
      }
   }
}
