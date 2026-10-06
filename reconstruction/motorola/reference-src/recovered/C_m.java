package recovered;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public final class C_m {
   public static void m_a_afa300e4(String var0, int var1) {
      String var15 = "<N><QUERY>"
         + C_cf.m_d_e96ea081("<Q><PluginID>srvMng</PluginID></Q>")
         + "</QUERY><NOTIFY>"
         + C_cf.m_d_e96ea081("<srv><id>cAwaySrv</id><req><id>AwayStat</id><trans>")
         + 0
         + C_cf.m_d_e96ea081("</trans><senderId>" + C_bp.m_a_47921032(254) + "</senderId></req></srv>")
         + "</NOTIFY></N>";
      int var10001 = C_cf.m_a_9b68();
      long var7 = System.currentTimeMillis();
      var1 = var10001;
      int var3 = 0;
      int var4 = 92 + var15.length();
      byte[] var5;
      C_cf.m_a_7dcd9a57(var5 = new byte[11 + var0.length() + 95 + var4 + 4], 0, var7, false);
      C_cf.m_a_7dcd9a57(var5, 4, 0L, false);
      C_cf.m_b_e306985c(var5, 8, 2);
      C_cf.m_a_e306985c(var5, 10, var0.length());
      System.arraycopy(C_cf.m_a_afa28ebe(var0), 0, var5, 11, var0.length());
      var3 = 11 + var0.length();
      int var49 = var4 + 55;
      boolean var13 = false;
      long var11 = 0L;
      int var6 = var49;
      C_cf.m_b_e306985c(var5, var3, 5);
      var3 += 2;
      C_cf.m_b_e306985c(var5, var3, var6 + 36);
      var3 += 2;
      C_cf.m_b_e306985c(var5, var3, 0);
      var3 += 2;
      C_cf.m_a_7dcd9a57(var5, var3, var7, false);
      var3 += 4;
      C_cf.m_a_7dcd9a57(var5, var3, 0L, false);
      var3 += 4;
      C_cf.m_a_e3069c1d(var5, var3, 155587401L);
      C_cf.m_a_e3069c1d(var5, var3 + 4, 1283396049L);
      C_cf.m_a_e3069c1d(var5, var3 + 8, -2111683515L);
      C_cf.m_a_e3069c1d(var5, var3 + 12, 1398013952L);
      var3 += 16;
      C_cf.m_a_e3069c1d(var5, var3, 655362L);
      var3 += 4;
      C_cf.m_a_e3069c1d(var5, var3, 0L);
      var3 += 2;
      C_cf.m_a_e3069c1d(var5, var3, 983040L);
      var3 += 4;
      C_cf.m_b_e306985c(var5, var3, 10001);
      var3 += 2;
      C_cf.m_b_e306985c(var5, var3, var4 + 51);
      var3 += 2;
      C_cf.m_a_7dcd25f8(var5, var3, 27, false);
      var3 += 2;
      C_cf.m_a_e306985c(var5, var3, 8);
      C_cf.m_a_e3069c1d(var5, ++var3, 0L);
      C_cf.m_a_e3069c1d(var5, var3 + 4, 0L);
      C_cf.m_a_e3069c1d(var5, var3 + 8, 0L);
      C_cf.m_a_e3069c1d(var5, var3 + 12, 0L);
      var3 += 16;
      C_cf.m_a_e3069c1d(var5, var3, 3L);
      var3 += 4;
      C_cf.m_a_e3069c1d(var5, var3, 0L);
      var3 += 4;
      C_cf.m_a_7dcd25f8(var5, var3, var1, false);
      var3 += 2;
      C_cf.m_a_7dcd25f8(var5, var3, 14, false);
      var3 += 2;
      C_cf.m_a_7dcd25f8(var5, var3, var1, false);
      var3 += 2;
      C_cf.m_a_e3069c1d(var5, var3, 0L);
      var3 += 4;
      C_cf.m_a_e3069c1d(var5, var3, 0L);
      var3 += 4;
      C_cf.m_a_e3069c1d(var5, var3, 0L);
      var3 += 4;
      C_cf.m_a_e306985c(var5, var3, 26);
      C_cf.m_a_e306985c(var5, ++var3, 0);
      C_cf.m_a_7dcd25f8(var5, ++var3, 0, false);
      var3 += 2;
      C_cf.m_b_e306985c(var5, var3, 256);
      var3 += 2;
      var3 = p_m_a_49634b7a(var5, var3);
      var3 = p_m_b_49634b7a(var5, var3);
      C_cf.m_a_7dcd25f8(var5, var3, var15.length() + 4, false);
      var3 += 4;
      C_cf.m_a_7dcd25f8(var5, var3, var15.length(), false);
      var3 += 4;
      System.arraycopy(C_cf.m_a_afa28ebe(var15), 0, var5, var3, var15.length());
      var3 += var15.length();
      C_cf.m_a_e3069c1d(var5, var3, 196608L);
      C_bv var14 = new C_bv(4, 6, 0L, new byte[0], var5);
      C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var14);
   }

   static byte[] m_a_40349d6f(String var0, int var1, long var2, long var4, String var6) {
      byte[] var7 = null;
      int var8 = 0;

      try {
         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         new DataOutputStream(var9).writeUTF(var6);
         var8 = (var7 = var9.toByteArray()).length - 2;
      } catch (Exception var14) {
      }

      int var36 = 0;
      byte[] var35;
      byte[] var10000 = var35 = new byte[var0.length() + 64 + 84 + var8 + 8];
      boolean var15 = false;
      byte[] var16 = var10000;
      C_cf.m_a_7dcd9a57(var10000, 0, var2, false);
      C_cf.m_a_7dcd9a57(var16, 4, var4, false);
      C_cf.m_b_e306985c(var16, 8, 2);
      C_cf.m_a_e306985c(var16, 10, var0.length());
      System.arraycopy(C_cf.m_a_afa28ebe(var0), 0, var16, 11, var0.length());
      int var17 = 11 + var0.length();
      C_cf.m_b_e306985c(var16, var17, 3);
      int var18 = var17 + 2;
      C_cf.m_a_7dcd25f8(var16, var18, 27, false);
      int var19 = var18 + 2;
      C_cf.m_b_e306985c(var16, var19, 8);
      C_cf.m_a_e3069c1d(var16, ++var19, 0L);
      C_cf.m_a_e3069c1d(var16, var19 + 4, 0L);
      C_cf.m_a_e3069c1d(var16, var19 + 8, 0L);
      C_cf.m_a_e3069c1d(var16, var19 + 12, 0L);
      int var21 = var19 + 16;
      C_cf.m_a_e3069c1d(var16, var21, 3L);
      int var22 = var21 + 4;
      C_cf.m_a_e3069c1d(var16, var22, 4L);
      int var23 = var22 + 4;
      C_cf.m_a_7dcd25f8(var16, var23, var1, false);
      int var24 = var23 + 2;
      C_cf.m_a_7dcd25f8(var16, var24, 14, false);
      int var25 = var24 + 2;
      C_cf.m_a_7dcd25f8(var16, var25, var1, false);
      var1 = var25 + 2;
      C_cf.m_a_e3069c1d(var16, var1, 0L);
      var1 += 4;
      C_cf.m_a_e3069c1d(var16, var1, 0L);
      var1 += 4;
      C_cf.m_a_e3069c1d(var16, var1, 0L);
      var1 += 4;
      C_cf.m_a_e306985c(var16, var1, 26);
      C_cf.m_a_e306985c(var16, ++var1, 0);
      C_cf.m_a_7dcd25f8(var16, ++var1, 0, false);
      var1 += 2;
      C_cf.m_b_e306985c(var16, var1, 0);
      var1 += 2;
      var36 = p_m_a_49634b7a(var35, var1);
      var36 = p_m_b_49634b7a(var35, var36);
      C_cf.m_a_7dcd25f8(var35, var36, var8 + 4, false);
      var36 += 4;
      C_cf.m_a_7dcd25f8(var35, var36, var8, false);
      int var40 = var36 + 4;
      if (var8 > 0) {
         System.arraycopy(var7, 2, var35, var40, var8);
      }

      return var35;
   }

   private static int p_m_a_49634b7a(byte[] var0, int var1) {
      C_cf.m_a_7dcd25f8(var0, var1, 1, false);
      var1 += 2;
      C_cf.m_a_e306985c(var0, var1, 0);
      return var1 + 1;
   }

   private static int p_m_b_49634b7a(byte[] var0, int var1) {
      C_cf.m_a_7dcd25f8(var0, var1, 79, false);
      var1 += 2;
      C_cf.m_a_e3069c1d(var0, var1, 996193263L);
      C_cf.m_a_e3069c1d(var0, var1 + 4, -668308411L);
      C_cf.m_a_e3069c1d(var0, var1 + 8, -1528783782L);
      C_cf.m_a_e3069c1d(var0, var1 + 12, 1583868005L);
      var1 += 16;
      C_cf.m_a_7dcd25f8(var0, var1, 8, false);
      var1 += 2;
      C_cf.m_a_7dcd9a57(var0, var1, 42L, false);
      var1 += 4;
      System.arraycopy(C_cf.m_a_afa28ebe("Script Plug-in: Remote Notification Arrive"), 0, var0, var1, 42);
      var1 += 42;
      C_cf.m_a_e3069c1d(var0, var1, 256L);
      var1 += 4;
      C_cf.m_a_e3069c1d(var0, var1, 0L);
      var1 += 4;
      C_cf.m_a_e3069c1d(var0, var1, 0L);
      var1 += 4;
      C_cf.m_b_e306985c(var0, var1, 0);
      var1 += 2;
      C_cf.m_a_e306985c(var0, var1, 0);
      return var1 + 1;
   }
}
