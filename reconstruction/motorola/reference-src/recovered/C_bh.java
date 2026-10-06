package recovered;

import java.io.DataInputStream;
import java.util.Date;

public final class C_bh extends C_az {
   private boolean f_bh_c_5a;
   public static boolean f_bh_b_5a = false;
   private boolean f_bh_d_5a = true;
   public static int[] f_bh_a_b4e = new int[]{0, 0, 0, 0};
   private static int[] f_bh_b_b4e = new int[51];
   private static String[] f_bh_a_6dccaaa5 = new String[51];
   private String[] f_bh_b_6dccaaa5 = new String[48];
   private Date f_bh_a_711fe21;
   private int f_bh_a_49;
   private boolean f_bh_e_5a = false;
   private String f_bh_a_523beb0a;
   private String f_bh_b_523beb0a;

   public C_bh(String var1, String var2) {
      super(false, true);
      this.f_bh_a_523beb0a = var2;
      this.f_bh_c_5a = false;
      this.f_bh_a_49 = 0;
      this.f_bh_b_6dccaaa5[0] = var1;
   }

   protected final void m_a_9b75() {
      byte[] var1;
      C_cf.m_a_7dcd25f8(var1 = new byte[6], 0, 1202, false);
      C_cf.m_a_7dcd9a57(var1, 2, Long.parseLong(this.f_bh_b_6dccaaa5[0]), false);
      C_ar var2 = new C_ar(C_bp.m_a_47921032(254), 2000, new byte[0], var1);
      C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var2);
      this.f_bh_a_711fe21 = new Date();
   }

   protected final synchronized boolean m_a_cb4a8bc8(C_cc var1) {
      boolean var2 = false;
      if (var1 instanceof C_br) {
         C_br var9;
         if ((var9 = (C_br)var1).f_br_e_49 != 2010) {
            return false;
         }

         DataInputStream var10 = C_cf.a$6f0c2d54(var9.m_b_12d408());

         try {
            int var3 = C_cf.a$175c50c1(var10);
            int var4 = var10.readByte();
            switch (var3) {
               case 200:
                  this.f_bh_b_6dccaaa5[1] = C_cf.m_a_b4050118(var10);
                  String var15 = C_cf.m_a_b4050118(var10);
                  String var18 = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[38] = var15;
                  this.f_bh_b_6dccaaa5[39] = var18;
                  if (var15.length() != 0 || var18.length() != 0) {
                     this.f_bh_b_6dccaaa5[2] = var15 + " " + var18;
                  }

                  this.f_bh_b_6dccaaa5[3] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[4] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[5] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[6] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[7] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[8] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[9] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_523beb0a = this.f_bh_b_6dccaaa5[0];
                  if (f_bh_b_5a && this.f_bh_b_523beb0a.equals(C_bp.m_a_47921032(254))) {
                     C_cf.m_a_b4050118(var10);
                     C_cf.a$175c50c1(var10);
                     byte[] var20 = new byte[3];
                     var10.readFully(var20);
                     C_bp.m_a_2563266(160, C_cf.m_a_49634b7a(var20, 2) != 0);
                     C_bp.m_a_2563266(183, C_cf.m_a_49634b7a(var20, 1) == 0);
                     C_ac.f_ac_b_5a = true;
                     this.f_bh_c_5a = true;
                     if (this.f_bh_b_6dccaaa5[1].length() > 0) {
                        C_ac.f_ac_a_523beb0a = this.f_bh_b_6dccaaa5[1];
                     }

                     f_bh_b_5a = false;
                  }

                  this.f_bh_a_49++;
                  var2 = true;
                  break;
               case 210:
                  for (int var14 = 14; var14 <= 18; var14++) {
                     this.f_bh_b_6dccaaa5[var14] = C_cf.m_a_b4050118(var10);
                  }

                  C_cf.m_a_b4050118(var10);
                  C_cf.a$175c50c1(var10);
                  this.f_bh_b_6dccaaa5[19] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[20] = C_cf.m_a_b4050118(var10);
                  this.f_bh_b_6dccaaa5[21] = C_cf.m_a_b4050118(var10);
                  this.f_bh_a_49++;
                  var2 = true;
                  break;
               case 220:
                  var3 = C_cf.a$175c50c1(var10);
                  this.f_bh_b_6dccaaa5[10] = var3 != 0 ? Integer.toString(var3) : new String();
                  this.f_bh_b_6dccaaa5[11] = C_cf.m_c_47921032(var10.readByte());
                  this.f_bh_b_6dccaaa5[12] = C_cf.m_a_b4050118(var10);
                  var4 = C_cf.a$175c50c1(var10);
                  byte var19 = var10.readByte();
                  byte var21 = var10.readByte();
                  this.f_bh_b_6dccaaa5[13] = var4 != 0 ? var21 + "." + var19 + "." + var4 : new String();
                  this.f_bh_a_49++;
                  var2 = true;
                  break;
               case 230:
                  this.f_bh_b_6dccaaa5[22] = C_cf.m_a_b4050118(var10);
                  this.f_bh_a_49++;
                  var2 = true;
                  break;
               case 240:
                  for (int var11 = 0; var11 < 4; var11++) {
                     f_bh_a_b4e[var11] = 0;
                  }

                  byte var12 = var10.readByte();

                  for (int var5 = 0; var5 < var12; var5++) {
                     int var6 = var5 + 40;
                     int var7 = var5 + 44;
                     var4 = C_cf.a$175c50c1(var10);
                     if (this.f_bh_b_523beb0a.equals(C_bp.m_a_47921032(254))) {
                        f_bh_a_b4e[var5] = var4;
                     }

                     this.f_bh_b_6dccaaa5[var6] = p_bh_b_47921032(var4);
                     this.f_bh_b_6dccaaa5[var7] = "\n" + C_cf.m_a_b4050118(var10);
                  }

                  this.f_bh_a_49++;
                  var2 = true;
                  break;
               case 250:
                  if (var4 != 10) {
                     this.f_bh_e_5a = true;
                  }

                  this.f_bh_a_49++;
                  var2 = true;
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         if (this.m_a_9b79() && !this.f_bh_c_5a && this.f_bh_d_5a) {
            C_u.m_a_1ef468a(6, this.f_bh_b_6dccaaa5);
            if (this.f_bh_b_6dccaaa5[0].equals(this.f_bh_a_523beb0a)) {
               C_v.m_a_513388b0(this.f_bh_b_6dccaaa5[0]).m_a_aad3b1ff(this.f_bh_b_6dccaaa5[1]);
            }

            this.f_bh_c_5a = true;
         }
      }

      return var2;
   }

   private static void p_bh_a_44bd8e9f(String var0, int var1, int var2) {
      f_bh_a_6dccaaa5[var1] = var0;
      f_bh_b_b4e[var1] = var2;
   }

   private static String p_bh_b_47921032(int var0) {
      for (int var1 = 0; var1 < 51; var1++) {
         if (f_bh_b_b4e[var1] == var0) {
            return f_bh_a_6dccaaa5[var1];
         }
      }

      return null;
   }

   public static int m_a_134621(int var0) {
      for (int var1 = 0; var1 < 51; var1++) {
         if (f_bh_b_b4e[var1] == var0) {
            return var1;
         }
      }

      return 0;
   }

   public static int m_b_134621(int var0) {
      return f_bh_b_b4e[var0];
   }

   public static String m_a_47921032(int var0) {
      return f_bh_a_6dccaaa5[var0];
   }

   public final synchronized boolean m_a_9b79() {
      return this.f_bh_a_49 >= 5 || this.f_bh_e_5a;
   }

   public final synchronized boolean m_b_9b79() {
      return this.f_bh_a_711fe21.getTime() + 10000L < System.currentTimeMillis();
   }

   static {
      p_bh_a_44bd8e9f("---", 0, 0);
      p_bh_a_44bd8e9f("n", 1, 137);
      p_bh_a_44bd8e9f("o", 2, 134);
      p_bh_a_44bd8e9f("p", 3, 135);
      p_bh_a_44bd8e9f("q", 4, 136);
      p_bh_a_44bd8e9f("r", 5, 109);
      p_bh_a_44bd8e9f("s", 6, 144);
      p_bh_a_44bd8e9f("t", 7, 101);
      p_bh_a_44bd8e9f("u", 8, 128);
      p_bh_a_44bd8e9f("v", 9, 147);
      p_bh_a_44bd8e9f("w", 10, 125);
      p_bh_a_44bd8e9f("x", 11, 146);
      p_bh_a_44bd8e9f("y", 12, 121);
      p_bh_a_44bd8e9f("z", 13, 131);
      p_bh_a_44bd8e9f("A", 14, 120);
      p_bh_a_44bd8e9f("B", 15, 115);
      p_bh_a_44bd8e9f("C", 16, 124);
      p_bh_a_44bd8e9f("D", 17, 116);
      p_bh_a_44bd8e9f("E", 18, 132);
      p_bh_a_44bd8e9f("F", 19, 114);
      p_bh_a_44bd8e9f("G", 20, 107);
      p_bh_a_44bd8e9f("H", 21, 149);
      p_bh_a_44bd8e9f("I", 22, 110);
      p_bh_a_44bd8e9f("J", 23, 100);
      p_bh_a_44bd8e9f("K", 24, 145);
      p_bh_a_44bd8e9f("L", 25, 112);
      p_bh_a_44bd8e9f("M", 26, 103);
      p_bh_a_44bd8e9f("N", 27, 104);
      p_bh_a_44bd8e9f("O", 28, 129);
      p_bh_a_44bd8e9f("P", 29, 142);
      p_bh_a_44bd8e9f("Q", 30, 105);
      p_bh_a_44bd8e9f("R", 31, 108);
      p_bh_a_44bd8e9f("S", 32, 141);
      p_bh_a_44bd8e9f("T", 33, 143);
      p_bh_a_44bd8e9f("U", 34, 126);
      p_bh_a_44bd8e9f("V", 35, 113);
      p_bh_a_44bd8e9f("W", 36, 118);
      p_bh_a_44bd8e9f("X", 37, 123);
      p_bh_a_44bd8e9f("Y", 38, 133);
      p_bh_a_44bd8e9f("Z", 39, 130);
      p_bh_a_44bd8e9f("_0", 40, 119);
      p_bh_a_44bd8e9f("00", 41, 127);
      p_bh_a_44bd8e9f("10", 42, 139);
      p_bh_a_44bd8e9f("20", 43, 117);
      p_bh_a_44bd8e9f("30", 44, 111);
      p_bh_a_44bd8e9f("40", 45, 150);
      p_bh_a_44bd8e9f("50", 46, 102);
      p_bh_a_44bd8e9f("60", 47, 148);
      p_bh_a_44bd8e9f("70", 48, 138);
      p_bh_a_44bd8e9f("80", 49, 106);
      p_bh_a_44bd8e9f("90", 50, 122);
   }
}
