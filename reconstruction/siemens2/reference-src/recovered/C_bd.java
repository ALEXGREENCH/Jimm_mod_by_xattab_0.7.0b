package recovered;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public final class C_bd extends C_bz implements Runnable {
   private final byte[] f_bd_a_b47 = new byte[]{5, 2, 0, 2};
   private SocketConnection f_bd_a_b4b0cc51;
   private InputStream f_bd_a_91ffb459;
   private OutputStream f_bd_a_33c83ab2;
   private boolean f_bd_b_5a = false;
   private int f_bd_a_49;

   public C_bd(C_ac var1) {
   }

   // $VF: Could not inline inconsistent finally blocks
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static synchronized String p_bd_a_8ea82db7(String var0, String var1) {
      if (C_ce.m_a_aad3b203(var0)) {
         return var0;
      } else {
         try {
            SocketConnection var8;
            String var9 = (var8 = (SocketConnection)Connector.open("socket://" + var0 + ":" + var1, 3)).getAddress();

            try {
               try {
                  var8.close();
               } catch (Exception var5) {
               }
            } catch (Throwable var6) {
               throw var6;
            }

            return var9;
         } catch (Exception var7) {
            return "0.0.0.0";
         }
      }
   }

   public final synchronized void m_a_aad3b1ff(String var1) {
      int var2 = C_bp.m_a_134621(76);
      this.f_bd_b_5a = false;
      String var3 = "";
      String var4 = "";
      if (var2 != 0) {
         int var9 = 0;

         for (int var8 = 0; var8 < var1.length(); var8++) {
            if (var1.charAt(var8) == ':') {
               var9 = var8;
               break;
            }
         }

         var3 = var1.substring(0, var9);
         var4 = var1.substring(var9 + 1);
      }

      try {
         switch (var2) {
            case 0:
               this.p_bd_a_efecb97b((byte)4, var3, var4);
               break;
            case 1:
               this.p_bd_a_efecb97b((byte)5, var3, var4);
               break;
            case 2:
               try {
                  this.p_bd_a_efecb97b((byte)5, var3, var4);
               } catch (Exception var6) {
               }

               if (!this.f_bd_b_5a) {
                  this.p_bd_b_9b75();

                  try {
                     Thread.sleep(2000L);
                  } catch (InterruptedException var5) {
                  }

                  this.p_bd_a_efecb97b((byte)4, var3, var4);
               }
         }

         super.f_bz_a_5a = false;
         super.f_bz_a_72a5fc31 = new Thread(this);
         super.f_bz_a_72a5fc31.start();
         C_ac.f_ac_a_49 = C_ac.m_b_9b68();
         this.f_bd_a_49 = 2;
      } catch (C_aq var7) {
         throw var7;
      }
   }

   private synchronized void p_bd_a_efecb97b(byte var1, String var2, String var3) {
      String var4 = C_bp.m_a_47921032(8);
      String var5 = C_bp.m_a_47921032(9);
      String var6 = C_bp.m_a_47921032(11);
      String var7 = C_bp.m_a_47921032(12);
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;

      try {
         this.f_bd_a_b4b0cc51 = (SocketConnection)Connector.open("socket://" + var4 + ":" + var5, 3);
         this.f_bd_a_91ffb459 = this.f_bd_a_b4b0cc51.openInputStream();
         this.f_bd_a_33c83ab2 = this.f_bd_a_b4b0cc51.openOutputStream();
         if (var1 == 5) {
            this.f_bd_a_33c83ab2.write(this.f_bd_a_b47);
            this.f_bd_a_33c83ab2.flush();

            while (this.f_bd_a_91ffb459.available() == 0 && var8 < 50) {
               try {
                  var8++;
                  Thread.sleep(100L);
               } catch (InterruptedException var13) {
               }
            }

            if (this.f_bd_a_91ffb459.available() == 0) {
               throw new C_aq(118, 226);
            }

            var9 = this.f_bd_a_91ffb459.read();
            var10 = this.f_bd_a_91ffb459.read();
            if (var9 == 5 && var10 == 2) {
               OutputStream var10000 = this.f_bd_a_33c83ab2;
               byte[] var27;
               C_ce.m_a_e306985c(var27 = new byte[3 + var6.length() + var7.length()], 0, 1);
               C_ce.m_a_e306985c(var27, 1, var6.length());
               C_ce.m_a_e306985c(var27, var6.length() + 2, var7.length());
               byte[] var28 = C_ce.m_a_afa28ebe(var6);
               byte[] var23 = C_ce.m_a_afa28ebe(var7);
               System.arraycopy(var28, 0, var27, 2, var28.length);
               System.arraycopy(var23, 0, var27, var28.length + 3, var23.length);
               var10000.write(var27);
               this.f_bd_a_33c83ab2.flush();

               while (this.f_bd_a_91ffb459.available() == 0 && var8 < 50) {
                  try {
                     var8++;
                     Thread.sleep(100L);
                  } catch (InterruptedException var12) {
                  }
               }

               if (this.f_bd_a_91ffb459.available() == 0) {
                  throw new C_aq(118, 227);
               }

               this.f_bd_a_91ffb459.read();
               if ((var10 = this.f_bd_a_91ffb459.read()) != 0) {
                  throw new C_aq(227, var10);
               }
            } else if (var9 != 5 || var10 != 0) {
               throw new C_aq(226, 0);
            }
         }

         OutputStream var34 = this.f_bd_a_33c83ab2;
         byte[] var24;
         if (var1 == 5) {
            var1 = 7 + var2.length();
            var24 = C_ce.m_a_afa28ebe(var2);
         } else {
            var1 = 9;
            var24 = C_ce.m_c_afa28ebe(p_bd_a_8ea82db7(var2, var3));
         }

         byte[] var21;
         (var21 = new byte[var1])[0] = (byte)var1;
         var21[1] = 1;
         if (var1 == 5) {
            var21[2] = 0;
            var21[3] = 3;
            C_ce.m_a_e306985c(var21, 4, var24.length);
            System.arraycopy(var24, 0, var21, 5, var24.length);
            C_ce.m_b_e306985c(var21, 5 + var24.length, Integer.parseInt(var3));
         } else {
            C_ce.m_b_e306985c(var21, 2, Integer.parseInt(var3));
            System.arraycopy(var24, 0, var21, 4, var24.length);
            var21[8] = 0;
         }

         var34.write(var21);
         this.f_bd_a_33c83ab2.flush();

         while (this.f_bd_a_91ffb459.available() == 0 && var8 < 50) {
            try {
               var8++;
               Thread.sleep(100L);
            } catch (InterruptedException var11) {
            }
         }

         if (this.f_bd_a_91ffb459.available() == 0) {
            throw new C_aq(118, 226);
         } else {
            var9 = this.f_bd_a_91ffb459.read();
            var10 = this.f_bd_a_91ffb459.read();
            if (var9 == 5) {
               switch (var10) {
                  case 0:
                     this.f_bd_a_91ffb459.read();
                     if (this.f_bd_a_91ffb459.read() == 1) {
                        byte[] var18 = new byte[6];
                        this.f_bd_a_91ffb459.read(var18);
                     } else {
                        byte[] var19 = new byte[this.f_bd_a_91ffb459.read() + 2];
                        this.f_bd_a_91ffb459.read(var19);
                     }
                     break;
                  default:
                     throw new C_aq(226, var10);
               }
            } else {
               switch (var10) {
                  case 90:
                     byte[] var20 = new byte[6];
                     this.f_bd_a_91ffb459.read(var20);
                     break;
                  default:
                     throw new C_aq(226, var10);
               }
            }

            this.f_bd_b_5a = true;
         }
      } catch (ConnectionNotFoundException var14) {
         throw new C_aq(121, 226);
      } catch (IllegalArgumentException var15) {
         throw new C_aq(122, 226);
      } catch (IOException var16) {
         throw new C_aq(120, 226);
      }
   }

   public final synchronized void m_a_9b75() {
      super.f_bz_a_5a = true;
      this.p_bd_b_9b75();
      Thread.yield();
   }

   private synchronized void p_bd_b_9b75() {
      try {
         this.f_bd_a_91ffb459.close();
      } catch (Exception var22) {
      } finally {
         this.f_bd_a_91ffb459 = null;
      }

      try {
         this.f_bd_a_33c83ab2.close();
      } catch (Exception var20) {
      } finally {
         this.f_bd_a_33c83ab2 = null;
      }

      try {
         this.f_bd_a_b4b0cc51.close();
         return;
      } catch (Exception var24) {
      } finally {
         this.f_bd_a_b4b0cc51 = null;
      }
   }

   public final void m_a_cb4a1765(C_cb var1) {
      C_aq var2;
      if (this.f_bd_a_33c83ab2 == null && !C_ac.m_a_cb34b9f8(var2 = new C_aq(123, 0))) {
         throw var2;
      } else {
         synchronized (this.f_bd_a_33c83ab2) {
            var1.m_b_13462e(C_ac.m_a_9b68());
            if (var1 instanceof C_ar) {
               C_ar var10000 = (C_ar)var1;
               int var4 = this.f_bd_a_49++;
               var10000.f_ar_d_49 = var4;
            }

            try {
               byte[] var7 = var1.m_a_12d408();
               this.f_bd_a_33c83ab2.write(var7);
               this.f_bd_a_33c83ab2.flush();
               C_as.m_b_13462e(var7.length + 51);
            } catch (IOException var5) {
               this.m_a_9b75();
            }
         }
      }
   }

   public final int m_a_9b68() {
      try {
         return this.f_bd_a_b4b0cc51.getLocalPort();
      } catch (IOException var1) {
         return 0;
      }
   }

   public final byte[] m_a_12d408() {
      try {
         return C_ce.m_c_afa28ebe(this.f_bd_a_b4b0cc51.getLocalAddress());
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
               while (this.f_bd_a_91ffb459.available() == 0) {
                  Thread.sleep(250L);
               }

               if (this.f_bd_a_91ffb459 == null) {
                  break;
               }
            }

            int var3;
            while ((var3 = this.f_bd_a_91ffb459.read(var1, var4, var1.length - var4)) != -1 && (var4 += var3) < var1.length) {
            }

            if (var3 == -1) {
               break;
            }

            if (C_ce.m_a_49634b7a(var1, 0) != 42) {
               throw new C_aq(124, 0);
            }

            byte[] var2 = new byte[C_ce.m_b_49634b7a(var1, 4)];
            var4 = 0;

            while ((var3 = this.f_bd_a_91ffb459.read(var2, var4, var2.length - var4)) != -1 && (var4 += var3) < var2.length) {
            }

            if (var3 == -1) {
               break;
            }

            byte[] var14 = new byte[var1.length + var2.length];
            System.arraycopy(var1, 0, var14, 0, var1.length);
            System.arraycopy(var2, 0, var14, var1.length, var2.length);
            C_as.m_a_13462e(var4 + 57);
            synchronized (super.f_bz_a_48a69a2c) {
               super.f_bz_a_48a69a2c.addElement(var14);
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
         C_aq.m_a_481c933f(var10);
      } catch (IOException var11) {
         if (!super.f_bz_a_5a) {
            C_aq.m_a_481c933f(new C_aq(120, 1));
         }

         super.f_bz_a_5a = false;
      }
   }
}
