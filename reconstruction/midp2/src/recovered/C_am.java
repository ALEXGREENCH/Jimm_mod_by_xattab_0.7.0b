package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.OtherAction. Release class: am. */

import java.util.Vector;

public final class C_am {
   private static C_s f_am_a_12b74;
   private static final C_s f_am_b_12b74 = new C_s(C_cf.m_a_529fb918("09,46,00,00,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16));
   private static final C_s f_am_c_12b74 = new C_s(C_cf.m_a_529fb918("d3,d4,53,19,8b,32,40,3b,ac,c7,d1,a9,e2,b5,81,3e", ',', 16));
   private static final C_s f_am_d_12b74 = new C_s(C_cf.m_a_529fb918("b9,97,08,b5,3a,92,42,02,b0,69,f1,e7,57,bb,2e,17", ',', 16));
   private static final C_s f_am_e_12b74 = new C_s(C_cf.m_a_529fb918("67,36,15,15,61,2d,4c,07,8f,3d,bd,e6,40,8e,a0,41", ',', 16));
   private static final C_s f_am_f_12b74 = new C_s(C_cf.m_a_529fb918("b2,ec,8f,16,7c,6f,45,1b,bd,79,dc,58,49,78,88,b9", ',', 16));
   private static final C_s f_am_g_12b74 = new C_s(C_cf.m_a_529fb918("17,8c,2d,9b,da,a5,45,bb,8d,db,f3,bd,bd,53,a1,0a", ',', 16));
   private static final C_s f_am_h_12b74 = new C_s(C_cf.m_a_529fb918("09,46,01,04,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16));
   private static final C_s f_am_i_12b74 = new C_s(C_cf.m_a_529fb918("09,46,01,01,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16));
   private static byte[] f_am_a_b47 = C_cf.m_a_529fb918(
      "00,06,00,04,11,00,00,00,00,0C,00,25,C0,A8,00,01,00,00,AB,CD,00,00,08,00,00,00,00,00,00,00,50,00,00,00,03,FF,FF,FF,FE,00,01,00,00,FF,FF,FF,FE,00,00",
      ',',
      16
   );

   private static void p_am_a_e3a6666e(C_s[] var0) throws recovered.C_ar {
      byte[] var1;
      (var1 = new byte[(var0.length << 4) + 4])[0] = 0;
      var1[1] = 5;
      var1[2] = (byte)((var0.length << 4) / 256);
      var1[3] = (byte)((var0.length << 4) % 256);

      for (int var2 = 0; var2 < var0.length; var2++) {
         System.arraycopy(var0[var2].f_s_a_b47, 0, var1, (var2 << 4) + 4, 16);
      }

      C_ac.f_ac_a_240bf0.m_a_cb4b0023(new C_bw(2, 4, 0L, new byte[0], var1));
   }

   public static void m_a_9b75() throws recovered.C_ar {
      C_s[] var0 = new C_s[]{new C_s(C_cf.f_cf_t_b47), f_am_b_12b74};
      Vector var1;
      Vector var2 = var1 = new Vector();
      byte[] var3 = C_cf.f_cf_u_b47;
      byte[] var4;
      System.arraycopy(var4 = C_cf.m_a_afa28ebe(C_bq.m_a_47921032(31)), 0, var3, 5, var4.length <= 11 ? var4.length : 11);
      f_am_a_12b74 = new C_s(var3);
      int var10000 = C_bq.m_a_134621(94);
      boolean var6 = false;
      switch (var10000) {
         case 0:
            var2.addElement(new C_s(C_cf.f_cf_g_b47));
            var2.addElement(f_am_c_12b74);
            var2.addElement(new C_s(C_cf.f_cf_v_b47));
            break;
         case 1:
            var2.addElement(new C_s(C_cf.f_cf_d_b47));
            var2.addElement(new C_s(C_cf.f_cf_v_b47));
            break;
         case 2:
            var2.addElement(new C_s(C_cf.f_cf_f_b47));
            var2.addElement(new C_s(C_cf.f_cf_v_b47));
            break;
         case 3:
            var2.addElement(new C_s(C_cf.f_cf_r_b47));
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
         case 16:
         case 17:
         case 19:
         case 20:
         default:
            break;
         case 6:
            var2.addElement(new C_s(C_cf.f_cf_e_b47));
            break;
         case 7:
            var2.addElement(f_am_a_12b74);
            break;
         case 11:
            var2.addElement(new C_s(C_cf.f_cf_k_b47));
            break;
         case 12:
            var2.addElement(new C_s(C_cf.f_cf_i_b47));
            break;
         case 13:
            var2.addElement(new C_s(C_cf.f_cf_h_b47));
            break;
         case 14:
            var2.addElement(new C_s(C_cf.f_cf_j_b47));
            var2.addElement(new C_s(C_cf.f_cf_v_b47));
            break;
         case 15:
            var2.addElement(f_am_e_12b74);
            var2.addElement(new C_s(C_cf.f_cf_m_b47));
            var2.addElement(f_am_f_12b74);
            var2.addElement(new C_s(C_cf.f_cf_n_b47));
            var2.addElement(f_am_g_12b74);
            var2.addElement(f_am_h_12b74);
            var2.addElement(f_am_i_12b74);
            break;
         case 18:
            var2.addElement(new C_s(C_cf.f_cf_m_b47));
            var2.addElement(new C_s(C_cf.f_cf_v_b47));
            var2.addElement(f_am_f_12b74);
            var2.addElement(f_am_d_12b74);
            var2.addElement(new C_s(C_cf.f_cf_r_b47));
            var2.addElement(f_am_e_12b74);
            break;
         case 21:
            var2.addElement(new C_s(C_cf.f_cf_x_b47));
            break;
         case 22:
            var2.addElement(new C_s(C_cf.f_cf_l_b47));
      }

      var1.addElement(new C_s(C_cf.f_cf_a_b47));
      var1.addElement(new C_s(C_cf.f_cf_b_b47));
      var1.addElement(new C_s(C_cf.f_cf_s_b47));
      if (C_bq.m_a_134621(88) > 0) {
         var1.addElement(new C_s(C_cf.f_cf_w_b47));
      }

      int var5;
      C_s var7;
      if ((var7 = C_x.m_a_485a5b4c(var5 = C_bq.m_a_134621(92))) != null && C_bp.m_a_2477940() != null) {
         var1.addElement(var7);
         if (C_bq.m_a_134632(159)) {
            var1.addElement(new C_s(C_cf.f_cf_r_b47));
         }
      }

      C_s[] var8 = new C_s[var1.size() + var0.length];
      var1.copyInto(var8);
      System.arraycopy(var0, 0, var8, var1.size(), var0.length);
      p_am_a_e3a6666e(var8) ;
      C_ac.m_b_13462e(var5);
   }

   public static void m_a_13462e(int var0) throws recovered.C_ar {
      int var1 = C_bq.m_a_134621(94);
      long var2 = -2L;
      long var4 = 65536L;
      long var6 = -2L;
      int var8 = 8;
      switch (var1) {
         case 0:
            var2 = 134220037L;
            var4 = 14L;
            var6 = 15L;
            var8 = 11;
            break;
         case 1:
            var2 = -1L;
            var4 = 198663L;
            var6 = -1L;
            var8 = 8;
            break;
         case 2:
            var2 = -129L;
            var4 = 591880L;
            var6 = 0L;
            var8 = 7;
            break;
         case 3:
            var2 = -2458L;
            var4 = 1104L;
            var6 = 0L;
            var8 = 9;
         case 4:
         case 5:
         case 9:
         case 10:
         case 16:
         case 17:
         case 19:
         case 20:
         default:
            break;
         case 6:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 8;
            break;
         case 7:
            var2 = -2L;
            var4 = 100802569L;
            var6 = -2L;
            var8 = C_bq.m_a_134621(98);
            break;
         case 8:
            var2 = 1000922031L;
            var4 = 1005278067L;
            var6 = 1005277794L;
            var8 = 2;
            break;
         case 11:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 0;
            break;
         case 12:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 11;
            break;
         case 13:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 11;
            break;
         case 14:
            var2 = 9034L;
            var4 = 0L;
            var6 = 0L;
            var8 = 11;
            break;
         case 15:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 9;
            break;
         case 18:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 9;
            break;
         case 21:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 0;
            break;
         case 22:
            var2 = 0L;
            var4 = 0L;
            var6 = 0L;
            var8 = 7;
      }

      C_cf.m_a_7dcd25f8(f_am_a_b47, 21, var8, true);
      C_cf.m_a_7dcd9a57(f_am_a_b47, 35, var2, true);
      C_cf.m_a_7dcd9a57(f_am_a_b47, 39, var4, true);
      C_cf.m_a_7dcd9a57(f_am_a_b47, 43, var6, true);
      C_cf.m_a_e3069c1d(f_am_a_b47, 4, var0);
      C_ac.f_ac_a_240bf0.m_a_cb4b0023(new C_bw(1, 30, 0L, new byte[0], f_am_a_b47));
   }

   public static void m_a_132be7(byte var0) throws recovered.C_ar {
      int var1 = C_ac.m_a_46a7a10e().m_e_9b68();
      byte var2 = 9;
      if (var1 == 0) {
         var1 = C_cf.m_b_9b68();
         var2 = 8;
         C_ac.m_a_46a7a10e().m_c_13462e(var1);
      }

      byte[] var3;
      C_cf.m_b_e306985c(var3 = new byte[15], 0, 0);
      C_cf.m_b_e306985c(var3, 2, 0);
      C_cf.m_b_e306985c(var3, 4, var1);
      C_cf.m_b_e306985c(var3, 6, 4);
      C_cf.m_b_e306985c(var3, 8, 5);
      C_cf.m_b_e306985c(var3, 10, 202);
      C_cf.m_b_e306985c(var3, 12, 1);
      C_cf.m_a_e306985c(var3, 14, var0);
      C_ac.f_ac_a_240bf0.m_a_cb4b0023(new C_bw(19, var2, 0L, new byte[0], var3));
   }
}
