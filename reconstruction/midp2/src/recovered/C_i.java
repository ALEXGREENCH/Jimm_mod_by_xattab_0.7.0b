package recovered;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class C_i extends C_cb implements Runnable {
   private HttpConnection f_i_a_473d1dc;
   private HttpConnection f_i_b_473d1dc;
   private InputStream f_i_a_91ffb459;
   private OutputStream f_i_a_33c83ab2;
   private String f_i_a_523beb0a;
   private int f_i_a_49 = 0;
   private String f_i_b_523beb0a;
   private String f_i_c_523beb0a;
   private int f_i_b_49;
   private int f_i_c_49 = 0;

   public C_i(C_ac var1) {
      this.f_i_a_523beb0a = "http://http.proxy.icq.com/hello";
      C_ac.f_ac_a_49 = C_ac.m_b_9b68();
   }

   public final synchronized void m_a_aad3b1ff(String var1) throws recovered.C_ar {
      try {
         this.f_i_c_49++;
         if (this.f_i_c_49 == 1) {
            super.f_cb_a_5a = false;
            super.f_cb_a_72a5fc31 = new Thread(this);
            super.f_cb_a_72a5fc31.start();
            this.wait();
         }

         String var2 = var1.substring(0, var1.indexOf(":"));
         int var6 = Integer.parseInt(var1.substring(var1.indexOf(":") + 1));
         byte[] var3;
         C_cf.m_b_e306985c(var3 = new byte[var2.length() + 4], 0, var2.length());
         System.arraycopy(C_cf.m_a_afa28ebe(var2), 0, var3, 2, var2.length());
         C_cf.m_b_e306985c(var3, 2 + var2.length(), var6);
         this.p_i_a_8412f60a(null, var3, 3, this.f_i_c_49);
         if (this.f_i_c_49 != 1) {
            C_ai var7 = new C_ai();
            this.p_i_a_8412f60a(var7, null, 5, this.f_i_c_49 - 1);
            this.p_i_a_8412f60a(null, new byte[0], 6, this.f_i_c_49 - 1);
         }
      } catch (IllegalArgumentException var4) {
         throw new C_ar(127, 0);
      } catch (InterruptedException var5) {
      }
   }

   public final synchronized void m_a_9b75() {
      super.f_cb_a_5a = true;

      try {
         this.f_i_a_91ffb459.close();
      } catch (Exception var25) {
      } finally {
         this.f_i_a_91ffb459 = null;
      }

      try {
         this.f_i_a_33c83ab2.close();
      } catch (Exception var23) {
      } finally {
         this.f_i_a_33c83ab2 = null;
      }

      try {
         this.f_i_a_473d1dc.close();
         this.f_i_b_473d1dc.close();
      } catch (Exception var21) {
      } finally {
         this.f_i_a_473d1dc = null;
         this.f_i_b_473d1dc = null;
      }

      Thread.yield();
   }

   private void p_i_a_8412f60a(C_cd var1, byte[] var2, int var3, int var4) throws recovered.C_ar {
      try {
         this.f_i_b_473d1dc = (HttpConnection)Connector.open(
            "http://" + this.f_i_c_523beb0a + ":" + this.f_i_b_49 + "/data?sid=" + this.f_i_b_523beb0a + "&seq=" + this.f_i_a_49, 3
         );
         this.f_i_b_473d1dc.setRequestProperty("User-Agent", C_bq.m_a_47921032(17));
         this.f_i_b_473d1dc.setRequestProperty("x-wap-profile", C_bq.m_a_47921032(18));
         this.f_i_b_473d1dc.setRequestProperty("Cache-Control", "no-store no-cache");
         this.f_i_b_473d1dc.setRequestProperty("Pragma", "no-cache");
         this.f_i_b_473d1dc.setRequestMethod("POST");
         this.f_i_a_33c83ab2 = this.f_i_b_473d1dc.openOutputStream();
      } catch (IOException var16) {
         this.m_a_9b75();
      }

      if (this.f_i_a_33c83ab2 == null) {
         throw new C_ar(128, 0, true);
      } else {
         synchronized (this.f_i_a_33c83ab2) {
            try {
               if (var2 == null) {
                  var2 = var1.m_a_12d408();
               }

               byte[] var17;
               C_cf.m_b_e306985c(var17 = new byte[14 + var2.length], 0, var2.length + 12);
               C_cf.m_b_e306985c(var17, 2, 1091);
               C_cf.m_b_e306985c(var17, 4, var3);
               C_cf.m_a_e3069c1d(var17, 6, 0L);
               C_cf.m_a_e3069c1d(var17, 10, var4);
               System.arraycopy(var2, 0, var17, 14, var2.length);
               this.f_i_a_33c83ab2.write(var17);
               if (this.f_i_b_473d1dc.getResponseCode() != 200) {
                  this.m_a_9b75();
               } else {
                  this.f_i_a_49++;
               }

               try {
                  this.f_i_a_33c83ab2.close();
                  this.f_i_b_473d1dc.close();
               } catch (Exception var12) {
               } finally {
                  this.f_i_a_33c83ab2 = null;
                  this.f_i_b_473d1dc = null;
               }

               C_at.m_b_13462e(var17.length + 40 + 190 + 14 + 170);
            } catch (IOException var14) {
               this.m_a_9b75();
            }
         }
      }
   }

   public final void m_a_cb4b0023(C_cd var1) throws recovered.C_ar {
      this.p_i_a_8412f60a(var1, null, 5, this.f_i_c_49);
   }

   public final void run() {
      byte[] var1 = new byte[2];
      byte[] var3 = new byte[0];
      int var4 = 0;
      boolean var7 = false;
      synchronized (this) {
         super.f_cb_a_48a69a2c = new Vector();
      }

      try {
         while (!super.f_cb_a_5a) {
            this.f_i_a_473d1dc = (HttpConnection)Connector.open(this.f_i_a_523beb0a, 3);
            this.f_i_a_473d1dc.setRequestProperty("User-Agent", C_bq.m_a_47921032(17));
            this.f_i_a_473d1dc.setRequestProperty("x-wap-profile", C_bq.m_a_47921032(18));
            this.f_i_a_473d1dc.setRequestProperty("Cache-Control", "no-store no-cache");
            this.f_i_a_473d1dc.setRequestProperty("Pragma", "no-cache");
            this.f_i_a_473d1dc.setRequestMethod("GET");
            this.f_i_a_91ffb459 = this.f_i_a_473d1dc.openInputStream();
            if (this.f_i_a_473d1dc.getResponseCode() != 200) {
               throw new IOException();
            }

            int var33 = 0;

            do {
               int var6 = 0;

               int var5;
               while ((var5 = this.f_i_a_91ffb459.read(var1, var6, var1.length - var6)) != -1) {
                  var6 += var5;
                  var33 += var5;
                  if (var6 >= var1.length) {
                     break;
                  }
               }

               if (var5 == -1) {
                  break;
               }

               byte[] var2 = new byte[C_cf.m_b_49634b7a(var1, 0)];
               int var30 = 0;

               while ((var5 = this.f_i_a_91ffb459.read(var2, var30, var2.length - var30)) != -1) {
                  var30 += var5;
                  var33 += var5;
                  if (var30 >= var2.length) {
                     break;
                  }
               }

               if (var5 == -1) {
                  break;
               }

               if (C_cf.m_b_49634b7a(var2, 2) != 5) {
                  if (C_cf.m_b_49634b7a(var2, 2) == 7) {
                     if (C_cf.m_b_49634b7a(var2, 10) == this.f_i_c_49) {
                        throw new C_ar(221, 0);
                     }
                  } else if (C_cf.m_b_49634b7a(var2, 2) == 2) {
                     synchronized (this) {
                        byte[] var32 = new byte[16];
                        System.arraycopy(var2, 10, var32, 0, 16);
                        this.f_i_b_523beb0a = C_cf.m_b_79834524(var32);
                        byte[] var8 = new byte[C_cf.m_b_49634b7a(var2, 26)];
                        System.arraycopy(var2, 28, var8, 0, var8.length);
                        this.f_i_c_523beb0a = C_cf.m_a_79834524(var8);
                        this.f_i_b_49 = C_cf.m_b_49634b7a(var2, 28 + var8.length);
                        this.f_i_a_523beb0a = "http://" + this.f_i_c_523beb0a + ":" + this.f_i_b_49 + "/monitor?sid=" + this.f_i_b_523beb0a;
                        this.notify();
                     }
                  }
               } else {
                  int var29 = 12;

                  while (var29 < var2.length) {
                     if (var4 == 0) {
                        if (C_cf.m_a_49634b7a(var2, var29) != 42) {
                           throw new C_ar(124, 0);
                        }

                        var3 = new byte[C_cf.m_b_49634b7a(var2, var29 + 4) + 6];
                     }

                     if (var2.length - var29 >= var3.length - var4) {
                        System.arraycopy(var2, var29, var3, var4, var3.length - var4);
                        var29 += var3.length - var4;
                        var4 = var3.length;
                     } else {
                        System.arraycopy(var2, var29, var3, var4, var2.length - var29);
                        var4 += var2.length - var29;
                        var29 += var2.length - var29;
                     }

                     if (var4 == var3.length) {
                        synchronized (super.f_cb_a_48a69a2c) {
                           super.f_cb_a_48a69a2c.addElement(var3);
                        }

                        var4 = 0;
                     }
                  }

                  synchronized (C_ac.m_a_810c345d()) {
                     C_ac.m_a_810c345d().notify();
                  }
               }
            } while (var33 < this.f_i_a_473d1dc.getLength());

            C_at.m_a_13462e(var33 + 42 + 185 + 175);

            try {
               this.f_i_a_91ffb459.close();
               this.f_i_a_473d1dc.close();
            } catch (Exception var19) {
            } finally {
               this.f_i_a_91ffb459 = null;
               this.f_i_a_473d1dc = null;
            }
         }
      } catch (NullPointerException var25) {
         if (!super.f_cb_a_5a) {
            C_ar.m_a_aef55300(new C_ar(125, 3));
         }
      } catch (C_ar var26) {
         C_ar.m_a_aef55300(var26);
      } catch (IOException var27) {
         if (!super.f_cb_a_5a) {
            C_ar.m_a_aef55300(new C_ar(125, 1));
         }
      }
   }
}
