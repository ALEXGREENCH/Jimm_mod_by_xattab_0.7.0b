package recovered;

import java.util.Enumeration;
import java.util.Hashtable;

public final class C_ap {
   private static Hashtable f_ap_a_d18d4967 = new Hashtable();
   private static int f_ap_a_49 = 1;
   private static C_f f_ap_a_129e1;

   protected static synchronized void m_a_f97a5fd3(C_aw var0, C_bl var1) {
      String var2 = var0.m_b_73cf11cb();
      if (!f_ap_a_d18d4967.containsKey(var2)) {
         m_a_b7252e78(var0, var0.f_aw_a_523beb0a);
      }

      C_au var3 = (C_au)f_ap_a_d18d4967.get(var2);
      boolean var4 = var1.m_a_9b79();
      boolean var5 = C_bp.m_a_134632(164);
      boolean var6 = var3.m_a_9b79();
      if (var1 instanceof C_d) {
         C_d var7 = (C_d)var1;
         f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(14) : null;
         if (!var6) {
            var0.m_b_13462e(1);
         }

         Object var9 = null;
         m_a_dc197d0(var2, var0.f_aw_a_523beb0a, var7.f_d_a_523beb0a, "", var7.f_bl_a_4a, true, var4, f_ap_a_129e1, 0L);
         if (C_bp.m_a_134632(137)) {
            C_aa.m_a_48303787(var2, var7.f_d_a_523beb0a, (byte)0, var0.f_aw_a_523beb0a, var7.f_bl_a_4a);
         }

         if (!var4) {
            C_bf.m_a_eb1e1393(var2, var0.f_aw_a_523beb0a, var7.f_d_a_523beb0a);
            C_bf.m_a_a7a36e14(C_bf.m_a_810c345d(), var0, var7.f_d_a_523beb0a);
         }
      } else if (var1 instanceof C_ae) {
         C_ae var11 = (C_ae)var1;
         f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(14) : null;
         if (!var3.m_a_9b79()) {
            var0.m_b_13462e(2);
         }

         Object var10 = null;
         m_a_dc197d0(var2, var0.f_aw_a_523beb0a, var11.f_ae_b_523beb0a, var11.f_ae_a_523beb0a, var11.f_bl_a_4a, false, var4, f_ap_a_129e1, 0L);
      } else if (var1 instanceof C_ad) {
         String var12 = "";
         C_ad var8 = (C_ad)var1;
         if (!var6) {
            var0.m_b_13462e(3);
         }

         if (var8.f_ad_a_49 == 1) {
            f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(17) : null;
            var12 = C_bs.m_a_e96ea081("g7") + var8.f_bl_c_523beb0a;
         } else if (var8.f_ad_a_49 == 6) {
            f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(17) : null;
            var12 = "User " + var8.f_ad_a_523beb0a + C_bs.m_a_e96ea081(" has removed himself from your contact list");
         } else if (var8.f_ad_a_49 == 3) {
            var0.m_b_13462e(4);
            f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(16) : null;
            var12 = var8.f_bl_c_523beb0a + C_bs.m_a_e96ea081("77") + var8.f_ad_a_523beb0a;
         } else if (var8.f_ad_a_49 == 2) {
            if (var8.f_ad_a_5a) {
               var0.m_a_2563266(2, false);
               f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(17) : null;
               var12 = C_bs.m_a_e96ea081("X2") + var8.f_bl_c_523beb0a;
            } else if (var8.f_ad_a_523beb0a != null) {
               f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(16) : null;
               var12 = C_bs.m_a_e96ea081("W1") + var8.f_bl_c_523beb0a + ". " + C_bs.m_a_e96ea081("K4") + ": " + var8.f_ad_a_523beb0a;
            } else {
               f_ap_a_129e1 = var5 ? C_v.f_v_a_12a1f.m_a_485a59b9(16) : null;
               var12 = C_bs.m_a_e96ea081("W1") + var8.f_bl_c_523beb0a + ". " + C_bs.m_a_e96ea081("44");
            }
         }

         m_a_dc197d0(var2, C_bs.m_a_e96ea081("46"), var12, "", var8.f_bl_a_4a, false, var4, f_ap_a_129e1, 0L);
         C_bq.m_a_e925fa09(var2, var12);
      }

      var3.m_b_9b75();
      var3.m_c_9b75();
   }

   public static synchronized void m_a_44be42cf(String var0, long var1, boolean var3) {
      if (f_ap_a_d18d4967.containsKey(var0)) {
         int var4;
         if ((var4 = var3 ? 21 : 20) >= C_v.f_v_a_12a1f.m_a_9b68()) {
            return;
         }

         C_f var6 = C_v.f_v_a_12a1f.m_a_485a59b9(var4);
         var3 = var3 || !C_bp.m_a_134632(170);
         ((C_au)f_ap_a_d18d4967.get(var0)).m_a_d25f948(var1, var6, var3);
      }
   }

   protected static synchronized void m_a_dd0db36e(C_aw var0, String var1, long var2, String var4, long var5) {
      String var7 = var0.m_b_73cf11cb();
      if (!f_ap_a_d18d4967.containsKey(var7)) {
         m_a_b7252e78(var0, var4);
      }

      f_ap_a_129e1 = C_bp.m_a_134632(164) ? C_v.f_v_a_12a1f.m_a_485a59b9(14) : null;
      m_a_dc197d0(var7, C_ac.f_ac_a_523beb0a, var1, "", var2, false, false, f_ap_a_129e1, var5);
   }

   public static synchronized void m_a_dc197d0(String var0, String var1, String var2, String var3, long var4, boolean var6, boolean var7, C_f var8, long var9) {
      ((C_au)f_ap_a_d18d4967.get(var0)).m_a_3074c21a(var1, var2, var3, var4, var6, var7, var8, var9);
   }

   public static void m_a_e925fa09(String var0, String var1) {
      C_au var2;
      int var3;
      if ((var3 = (var2 = m_a_51338872(var0)).f_au_a_12b74.m_b_9b68()) != -1) {
         C_au var4;
         int var5;
         C_bb var6;
         C_bf.m_a_4c1e0f27(
            (var6 = (C_bb)var2.m_a_6a39c0ed().elementAt(var3)).m_a_9b79(),
            C_cf.m_a_87d767d1(true, true, var6.f_bb_a_4a),
            var6.m_a_9b79() ? var1 : C_ac.f_ac_a_523beb0a,
            m_a_51338872(var0)
               .f_au_a_12b74
               .m_a_4dee1afa(
                  ((var5 = (var4 = m_a_51338872(var0)).f_au_a_12b74.m_b_9b68()) == -1 ? null : (C_bb)var4.m_a_6a39c0ed().elementAt(var5)).f_bb_a_49 & 16777215,
                  false
               ),
            C_bf.m_a_a9514c81(true)
         );
      }
   }

   public static C_au m_a_51338872(String var0) {
      return f_ap_a_d18d4967.containsKey(var0) ? (C_au)f_ap_a_d18d4967.get(var0) : null;
   }

   public static void m_a_aad3b1ff(String var0) {
      C_aw var1 = C_v.m_a_513388b0(var0);
      f_ap_a_d18d4967.remove(var0);
      var1.m_a_2563266(16, false);
      var1.m_a_255f295(67, 0);
      var1.m_a_255f295(68, 0);
      var1.m_a_255f295(69, 0);
   }

   public static void m_a_afa300e4(String var0, int var1) {
      switch (var1) {
         case 1:
            m_a_aad3b1ff(var0);
            return;
         case 2:
         case 3:
            Enumeration var2 = f_ap_a_d18d4967.keys();

            while (var2.hasMoreElements()) {
               String var3 = (String)var2.nextElement();
               if (var1 != 2 || !var3.equals(var0)) {
                  m_a_aad3b1ff(var3);
               }
            }
      }
   }

   public static boolean m_a_aad3b203(String var0) {
      return f_ap_a_d18d4967.containsKey(var0) ? ((C_au)f_ap_a_d18d4967.get(var0)).m_a_9b79() : false;
   }

   public static boolean m_b_aad3b203(String var0) {
      return f_ap_a_d18d4967.containsKey(var0);
   }

   public static void m_a_b7252e78(C_aw var0, String var1) {
      C_au var3 = new C_au(var1, var0);
      String var2 = var0.m_b_73cf11cb();
      f_ap_a_d18d4967.put(var2, var3);
      m_b_aad3b1ff(var2);
      var0.m_a_2563266(16, true);
      m_a_cb37742e(var0);
      var0.m_f_9b75();
   }

   public static void m_a_cb37742e(C_aw var0) {
      String var1 = var0.f_aw_a_523beb0a;
      String var2 = var0.m_b_73cf11cb();
      if (C_bp.m_a_134632(142)) {
         int var3;
         if ((var3 = C_aa.m_a_aad3b1f2(var2)) == 0) {
            return;
         }

         if (!f_ap_a_d18d4967.containsKey(var2)) {
            m_a_b7252e78(var0, var1);
         }

         C_au var5;
         if ((var5 = (C_au)f_ap_a_d18d4967.get(var2)).f_au_a_12b74.m_a_9b68() != 0) {
            return;
         }

         int var6 = var3 > 5 ? 5 : var3;

         for (int var7 = var3 - var6; var7 < var3; var7++) {
            C_ba var4 = C_aa.m_a_9e194c2(var2, var7);
            var5.f_au_a_12b74
               .m_a_68a79fe2("[" + var4.f_ba_d_523beb0a + " " + var4.f_ba_c_523beb0a + "]", C_au.m_a_1385f2(var4.f_ba_a_42 == 0), C_bp.f_bp_g_49, -1);
            var5.f_au_a_12b74.m_a_485a5b4c(-1);
            C_ah.m_a_e9f435a0(var5.f_au_a_12b74, var4.f_ba_b_523beb0a, C_bp.f_bp_g_49, 8421504, -1);
            var5.f_au_a_12b74.m_a_485a5b4c(-1);
         }
      }
   }

   public static void m_b_e925fa09(String var0, String var1) {
      C_au var2;
      if ((var2 = (C_au)f_ap_a_d18d4967.get(var0)) != null) {
         var2.f_au_a_523beb0a = var1;
         m_b_aad3b1ff(var0);
      }
   }

   public static void m_b_aad3b1ff(String var0) {
      m_c_aad3b1ff(var0);
      C_au var2 = (C_au)f_ap_a_d18d4967.get(var0);
      String var1 = var2.f_au_a_523beb0a + " (" + f_ap_a_49 + "/" + f_ap_a_d18d4967.size() + ")";
      var2.f_au_a_12b74.m_c_aad3b1ff(var1);
   }

   public static void m_a_9b75() {
      Enumeration var0 = f_ap_a_d18d4967.elements();

      while (var0.hasMoreElements()) {
         C_bf.m_a_9c60de72(((C_au)var0.nextElement()).f_au_a_12b74, C_bp.m_a_134632(145));
      }
   }

   public static void m_c_aad3b1ff(String var0) {
      if (var0 != null) {
         Enumeration var1 = f_ap_a_d18d4967.elements();
         Object var2 = f_ap_a_d18d4967.get(var0);
         f_ap_a_49 = 1;

         while (var1.hasMoreElements() && var1.nextElement() != var2) {
            f_ap_a_49++;
         }
      }
   }

   public static boolean m_a_cb377432(C_aw var0) {
      if (var0 == null) {
         return false;
      } else {
         C_au var1;
         if ((var1 = m_a_51338872(var0.m_b_73cf11cb())) != null) {
            var1.m_a_cb37742e(var0);
            var1.m_e_9b75();
         }

         return var1 != null;
      }
   }

   public static void m_b_cb37742e(C_aw var0) {
      if (var0 != null) {
         C_au var1;
         if ((var1 = m_a_51338872(var0.m_b_73cf11cb())) != null) {
            var1.m_a_9b75();
         }
      }
   }

   static {
      if (C_bp.m_a_134632(135) || C_bp.m_a_134621(111) == 2) {
         (C_bo.f_bo_a_2409c2 = new C_bo("/font.prs")).m_a_aad3b1ff("/font.png");
      }

      f_ap_a_129e1 = null;
   }
}
