package recovered;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import jimm.Jimm;

public final class C_ac implements Runnable {
   private static C_ac f_ac_a_24048d;
   public static String f_ac_a_523beb0a = C_bs.m_a_e96ea081("B3");
   private static byte[] f_ac_a_b47 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
   private static boolean f_ac_c_5a = false;
   private static Vector f_ac_a_48a69a2c = new Vector();
   private static volatile Thread f_ac_a_72a5fc31;
   static int f_ac_a_49;
   private static String f_ac_b_523beb0a;
   private static Object f_ac_a_5f790d9c = new Object();
   public static C_bz f_ac_a_240b17;
   static C_ca f_ac_a_240bd1;
   private static Vector f_ac_b_48a69a2c;
   private static C_ck f_ac_a_240d07;
   public static int f_ac_b_49;
   public static volatile boolean f_ac_a_5a = false;
   public static boolean f_ac_b_5a;
   private int f_ac_c_49 = 0;

   public C_ac() {
      f_ac_a_24048d = this;
   }

   public static C_ac m_a_46a7a10e() {
      return f_ac_a_24048d;
   }

   public static void m_a_cb38d14b(C_az var0) {
      var0.m_a_cb2e5cc2(f_ac_a_24048d);
      if (!var0.m_c_9b79()) {
         throw new C_aq(140, 0);
      } else {
         synchronized (f_ac_a_24048d) {
            f_ac_a_48a69a2c.addElement(var0);
         }

         if (var0 instanceof C_y) {
            (f_ac_a_72a5fc31 = new Thread(f_ac_a_24048d)).start();
         }

         synchronized (f_ac_a_5f790d9c) {
            f_ac_a_5f790d9c.notify();
         }
      }
   }

   public static synchronized void m_a_cb37742e(C_aw var0) {
      C_cm var3 = new C_cm(var0, 1);

      try {
         m_a_cb38d14b(var3);
      } catch (C_aq var2) {
         C_aq.m_a_481c933f(var2);
         if (var2.f_aq_a_5a) {
            return;
         }
      }

      C_cn.m_a_33097a9f("Z6", var3, false);
   }

   private static String p_ac_b_73cf11cb() {
      return C_ce.m_a_def8391f(C_ce.m_a_639c22ad(C_bp.m_a_47921032(1).replace('\n', ' '), ' ')[0], "\r", "", false);
   }

   public static synchronized void m_a_9b75() {
      f_ac_a_5a = true;
      f_ac_b_5a = false;
      if (C_bp.m_a_134632(139)) {
         HttpConnection var0 = null;

         try {
            Object var1 = null;
            (var0 = (HttpConnection)Connector.open("http://sc.jimm.im/")).setRequestProperty("Host", "");
            var0.setRequestProperty("x-operamini-fetures", "AzLJyMVEAvzJjEFF0VxGxQ==");
            var0.openDataInputStream();
         } catch (Exception var2) {
         }
      }

      C_y var5 = new C_y(C_bp.m_a_47921032(254), C_bp.m_a_47921032(255), p_ac_b_73cf11cb(), C_bp.m_a_47921032(2));

      try {
         m_a_cb38d14b(var5);
      } catch (C_aq var3) {
         if (!m_a_cb34b9f8(var3)) {
            C_aq.m_a_481c933f(var3);
         }
      }

      C_cn.m_a_13462e(C_bg.m_a_1349e2(C_bp.a$134622()));
      C_cn.m_a_48a013c6(C_x.m_a_485a59b9(C_bp.m_a_134621(92)));
      C_cn.m_a_33097a9f(p_ac_b_73cf11cb(), var5, true);
      f_ac_b_523beb0a = C_ce.m_a_2416688b(true, false);
   }

   public static synchronized void m_b_9b75() {
      f_ac_a_5a = false;
      if (f_ac_a_240b17 != null) {
         f_ac_a_240b17.m_a_9b75();
      }

      m_d_9b75();
      m_e_9b75();

      try {
         C_as.m_a_9b75();
      } catch (Exception var0) {
      }

      C_v.m_a_d3ad8a43(10, null);
   }

   public static synchronized boolean m_a_cb377432(C_aw var0) {
      if (var0.m_a_134632(8)) {
         String var1 = var0.m_a_47921032(0);
         byte[] var2;
         C_ce.m_a_e306985c(var2 = new byte[1 + var1.length()], 0, var1.length());
         System.arraycopy(var1.getBytes(), 0, var2, 1, var1.length());

         try {
            f_ac_a_240b17.m_a_cb4a1765(new C_bu(3, 5, 0L, new byte[0], var2));
         } catch (C_aq var3) {
            C_aq.m_a_481c933f(var3);
         }

         C_w.m_a_cb37742e(var0);
         C_w.m_a_9b75();
      } else {
         C_cm var5 = new C_cm(var0, 2);

         try {
            m_a_cb38d14b(var5);
         } catch (C_aq var4) {
            C_aq.m_a_481c933f(var4);
            if (var4.f_aq_a_5a) {
               return false;
            }
         }

         C_cn.m_a_33097a9f("Z6", var5, false);
      }

      return true;
   }

   public static synchronized void m_a_afa340b5(String var0, boolean var1) {
      if (!C_bp.m_a_134632(146)) {
         byte[] var4 = C_ce.m_a_afa28ebe(var0);
         int var2 = f_ac_a_b47.length + 1 + var4.length + 2;
         int var3 = 0;
         byte[] var6 = new byte[var2];
         System.arraycopy(f_ac_a_b47, 0, var6, 0, f_ac_a_b47.length);
         var3 = 0 + f_ac_a_b47.length;
         C_ce.m_a_e306985c(var6, var3, var4.length);
         System.arraycopy(var4, 0, var6, ++var3, var4.length);
         var3 += var4.length;
         C_ce.m_b_e306985c(var6, var3, var1 ? 2 : 0);
         C_bu var5 = new C_bu(4, 20, 0L, new byte[0], var6);
         f_ac_a_240b17.m_a_cb4a1765(var5);
      }
   }

   public static synchronized boolean m_a_9b79() {
      return !f_ac_c_5a;
   }

   public static synchronized boolean m_b_9b79() {
      return f_ac_c_5a;
   }

   protected static synchronized void m_c_9b75() {
      f_ac_b_49 = C_bp.m_a_134621(91);
      f_ac_c_5a = true;
   }

   public static int m_a_9b68() {
      return f_ac_a_49 = ++f_ac_a_49 % 32768;
   }

   public static int m_b_9b68() {
      Random var10000 = new Random(System.currentTimeMillis());
      Object var0 = null;
      int var4 = var10000.nextInt();
      int var3 = 0;

      int var1;
      int var2;
      for (var2 = var1 = var5 = var4 & 32767; var5 != 0; var5 /= 8) {
         var3 += var5;
      }

      int var6;
      return (((var6 = var2 - var3) & 0xFF ^ var1 & 0xFF) + (var6 & -256) & 7 ^ var1) + 3;
   }

   public static synchronized void m_d_9b75() {
      f_ac_a_72a5fc31 = null;
      synchronized (f_ac_a_5f790d9c) {
         f_ac_a_5f790d9c.notify();
      }

      f_ac_c_5a = false;
      Jimm.m_a_9b75();
      if (f_ac_b_48a69a2c != null) {
         f_ac_b_48a69a2c.removeAllElements();
      }

      if (f_ac_a_48a69a2c != null) {
         f_ac_a_48a69a2c.removeAllElements();
      }
   }

   public static synchronized void m_e_9b75() {
      f_ac_a_240bd1 = null;
   }

   public final void run() {
      Thread var2 = Thread.currentThread();
      C_az var3 = null;
      if (C_bp.m_a_134621(83) == 0) {
         f_ac_a_240b17 = new C_q(this);
      } else if (C_bp.m_a_134621(83) == 1) {
         f_ac_a_240b17 = new C_i(this);
      } else if (C_bp.m_a_134621(83) == 2) {
         f_ac_a_240b17 = new C_bd(this);
      }

      f_ac_b_48a69a2c = new Vector();
      new C_bk();
      f_ac_a_240d07 = new C_ck(100);
      long var4 = Integer.parseInt(C_bp.m_a_47921032(13)) * 1000;
      Jimm.m_a_94e56161().schedule(f_ac_a_240d07, var4, var4);

      try {
         while (f_ac_a_72a5fc31 == var2) {
            synchronized (this) {
               label223: {
                  label202:
                  if (f_ac_a_48a69a2c.size() > 0) {
                     if (f_ac_b_48a69a2c.size() == 1) {
                        C_az var10000 = (C_az)f_ac_b_48a69a2c.elementAt(0);
                        Object var1 = null;
                        if (var10000.f_az_a_5a) {
                           break label202;
                        }
                     }

                     if (f_ac_a_48a69a2c != null && f_ac_a_48a69a2c.size() != 0) {
                        var3 = (C_az)f_ac_a_48a69a2c.elementAt(0);
                     }

                     label195: {
                        if (f_ac_b_48a69a2c.size() > 0) {
                           Object var17 = null;
                           if (var3.f_az_a_5a) {
                              break label195;
                           }
                        }

                        if (var3.m_c_9b79()) {
                           if (f_ac_a_48a69a2c != null && f_ac_a_48a69a2c.size() != 0) {
                              f_ac_a_48a69a2c.removeElementAt(0);
                           }
                           break label223;
                        }
                     }

                     var3 = null;
                     break label223;
                  }

                  var3 = null;
               }
            }

            if (var3 == null && f_ac_a_240b17.m_b_9b68() == 0) {
               try {
                  synchronized (f_ac_a_5f790d9c) {
                     f_ac_a_5f790d9c.wait();
                  }
               } catch (InterruptedException var9) {
               }
            } else if (var3 != null) {
               try {
                  var3.m_a_9b75();
                  f_ac_b_48a69a2c.addElement(var3);
               } catch (C_aq var13) {
                  if (!m_a_cb34b9f8(var13)) {
                     C_aq.m_a_481c933f(var13);
                  }

                  if (var13.f_aq_a_5a) {
                     throw var13;
                  }
               }
            }

            boolean var18;
            if (f_ac_a_240bd1 != null) {
               if (f_ac_a_240bd1.m_a_9b68() > 0) {
                  var18 = true;
               } else {
                  var18 = false;
               }
            } else {
               var18 = false;
            }

            while (f_ac_a_240b17.m_b_9b68() > 0 || var18) {
               C_cb var21 = null;

               try {
                  if (f_ac_a_240b17.m_b_9b68() > 0) {
                     var21 = f_ac_a_240b17.m_a_46a7a871();
                  } else if (var18) {
                     var21 = f_ac_a_240bd1.m_a_46a7a871();
                  }
               } catch (C_aq var10) {
                  if (!m_a_cb34b9f8(var10)) {
                     C_aq.m_a_481c933f(var10);
                  }

                  if (var10.f_aq_a_5a) {
                     throw var10;
                  }
               }

               boolean var19 = false;

               for (int var5 = 0; var5 < f_ac_b_48a69a2c.size(); var5++) {
                  try {
                     if (((C_az)f_ac_b_48a69a2c.elementAt(var5)).m_a_cb4a1769(var21)) {
                        var19 = true;
                        break;
                     }
                  } catch (C_aq var11) {
                     if (!m_a_cb34b9f8(var11)) {
                        C_aq.m_a_481c933f(var11);
                     }

                     if (var11.f_aq_a_5a) {
                        throw var11;
                     }
                  }
               }

               if (!var19) {
                  try {
                     C_bk.m_a_cb4a1765(var21);
                  } catch (C_aq var12) {
                     if (!m_a_cb34b9f8(var12)) {
                        C_aq.m_a_481c933f(var12);
                     }

                     if (var12.f_aq_a_5a) {
                        throw var12;
                     }
                  }
               }

               if (f_ac_a_240bd1 != null) {
                  if (f_ac_a_240bd1.m_a_9b68() > 0) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }
               } else {
                  var18 = false;
               }
            }

            for (int var22 = 0; var22 < f_ac_b_48a69a2c.size(); var22++) {
               if (((C_az)f_ac_b_48a69a2c.elementAt(var22)).m_a_9b79() || ((C_az)f_ac_b_48a69a2c.elementAt(var22)).m_b_9b79()) {
                  f_ac_b_48a69a2c.removeElementAt(var22--);
               }
            }
         }
      } catch (NullPointerException var15) {
      } catch (C_aq var16) {
      }

      if (!C_bp.m_a_134632(149)) {
         f_ac_a_240b17.m_a_9b75();
         m_d_9b75();
         C_v.m_a_d3ad8a43(10, null);
      }
   }

   public static synchronized boolean m_a_cb34b9f8(C_aq var0) {
      Object var1 = null;
      int var6 = var0.f_aq_a_49;
      if (f_ac_b_49-- > 0 && C_bp.m_a_134632(149) && var0.f_aq_a_5a && f_ac_a_5a) {
         boolean var9;
         switch (var6) {
            case 110:
            case 111:
            case 112:
            case 117:
            case 122:
            case 127:
               var9 = false;
               break;
            case 113:
            case 114:
            case 115:
            case 116:
            case 118:
            case 119:
            case 120:
            case 121:
            case 123:
            case 124:
            case 125:
            case 126:
            default:
               var9 = true;
         }

         if (var9) {
            m_b_9b75();
            C_w.m_c_9b75();

            try {
               Thread.sleep(3000L);
            } catch (InterruptedException var4) {
            }

            int var5 = (var1 = C_bp.m_a_47921032(1)).indexOf(32) < 0 ? 10 : 32;
            if (((Object[])(var1 = C_ce.m_a_639c22ad((String)var1, (char)var5))).length >= 2) {
               StringBuffer var2 = new StringBuffer();

               for (int var3 = 1; var3 < ((Object[])var1).length; var3++) {
                  var2.append((String)((Object[])var1)[var3]);
                  var2.append((char)var5);
               }

               var2.append((String)((Object[])var1)[0]);
               C_bp.m_a_4f708078(1, var2.toString());
            }

            m_a_9b75();
            return true;
         }
      }

      return false;
   }

   public static int m_c_9b68() {
      return m_b_9b79() ? (int)C_bp.a$134622() : -1;
   }

   public static C_f m_a_2477940() {
      return m_b_9b79() ? C_x.m_a_485a59b9(C_bp.m_a_134621(92)) : null;
   }

   public static int m_d_9b68() {
      int var0 = 0;
      if (C_bp.m_a_134632(160)) {
         var0 = 0 | 268500992;
      } else {
         var0 = 0 | 268435456;
      }

      if (C_bp.m_a_134632(171)) {
         var0 |= 268959744;
      }

      return var0;
   }

   public static void m_f_9b75() {
      if (f_ac_b_5a) {
         boolean var0 = C_bp.m_a_134632(160);
         boolean var1 = C_bp.m_a_134632(183);
         byte[] var2;
         C_ce.m_b_e306985c(var2 = new byte[12], 0, 14860);
         C_ce.m_b_e306985c(var2, 2, 3075);
         C_ce.m_b_e306985c(var2, 4, 256);
         C_ce.m_a_e306985c(var2, 6, var0 ? 1 : 0);
         C_ce.m_b_e306985c(var2, 7, 63490);
         C_ce.m_b_e306985c(var2, 9, 256);
         C_ce.m_a_e306985c(var2, 11, var1 ? 0 : 1);
         C_ar var4 = new C_ar(2L, 0, C_bp.m_a_47921032(254), new byte[0], var2);

         try {
            f_ac_a_240b17.m_a_cb4a1765(var4);
            return;
         } catch (C_aq var3) {
            C_aq.m_a_481c933f(var3);
            if (var3.f_aq_a_5a) {
               return;
            }
         }
      }
   }

   public static void m_a_13462e(int var0) {
      C_bp.m_a_255f656(192, var0);
      C_bp.m_c_9b75();
      if (m_b_9b79()) {
         if (C_bo.m_b_2477940() == null) {
            byte var1 = 4;
            if (var0 == 512) {
               var1 = 2;
            }

            if (var0 == 256) {
               var1 = 3;
            }

            m_a_132be7(var1);
         }

         C_al.m_a_13462e(m_d_9b68() | var0);
      }

      C_w.m_e_9b75();
      f_ac_b_523beb0a = C_ce.m_a_2416688b(true, false);
   }

   public static void m_b_13462e(int var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      Object var2 = null;
      byte[] var3 = C_ce.m_a_44c4d6c8(C_bp.m_a_47921032(32) + " " + C_bp.m_a_47921032(33), true);
      if (var0 != 255) {
         C_ce.m_a_559c4327(var1, 29, true);
         String var4 = var0 != 37 ? "icqmood" + var0 : "";
         C_ce.m_a_559c4327(var1, 0, true);
         C_ce.m_a_559c4327(var1, 2, true);
         C_ce.m_a_5557994d(var1, 4);
         C_ce.m_a_5557994d(var1, var3.length + 4);
         C_ce.m_a_559c4327(var1, var3.length, true);
         C_ce.m_a_55a417bd(var1, var3);
         C_ce.m_a_559c4327(var1, 0, true);
         C_ce.m_a_559c4327(var1, 14, true);
         C_ce.m_a_e13aaa14(var1, var4, false);
      }

      if (var1.size() != 0) {
         C_ce.m_b_e306985c((byte[])(var2 = var1.toByteArray()), 2, ((Object[])var2).length - 4);
         C_bu var5 = new C_bu(1, 30, 30L, new byte[0], (byte[])var2);
         f_ac_a_240b17.m_a_cb4a1765(var5);
      }
   }

   public static String m_a_73cf11cb() {
      return f_ac_b_523beb0a;
   }

   public static void m_a_132be7(byte var0) {
      if (m_b_9b79()) {
         try {
            C_al.m_a_132be7(var0);
         } catch (C_aq var1) {
            C_aq.m_a_481c933f(var1);
         }
      }

      C_bp.m_a_255f295(110, var0);
      C_bp.m_c_9b75();
   }

   public final void m_c_13462e(int var1) {
      this.f_ac_c_49 = var1;
   }

   public final int m_e_9b68() {
      return this.f_ac_c_49;
   }

   static Object m_a_810c345d() {
      return f_ac_a_5f790d9c;
   }
}
