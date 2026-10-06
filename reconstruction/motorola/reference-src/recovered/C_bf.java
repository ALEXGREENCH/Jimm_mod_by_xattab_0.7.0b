package recovered;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import jimm.Jimm;

public final class C_bf implements C_ax, CommandListener {
   public static Object f_bf_a_5f790d9c;
   private static C_s f_bf_b_12b74;
   public static final Command f_bf_a_1570d10e = new Command(C_bs.m_a_e96ea081("b"), 4, 1);
   public static Command f_bf_b_1570d10e = new Command(C_bs.m_a_e96ea081("8"), 2, 14);
   public static final Command f_bf_c_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
   private static Command f_bf_n_1570d10e = new Command(C_bs.m_a_e96ea081("f7"), 4, 1);
   private static Command f_bf_o_1570d10e = new Command(C_bs.m_a_e96ea081("Y3"), 3, 2);
   public static final Command f_bf_d_1570d10e = new Command(C_bs.m_a_e96ea081("w2"), 8, 1);
   public static final Command f_bf_e_1570d10e = new Command(C_bs.m_a_e96ea081("D1"), 8, 4);
   public static final Command f_bf_f_1570d10e = new Command(C_bs.m_a_e96ea081("t0"), 8, 4);
   public static final Command f_bf_g_1570d10e = new Command(C_bs.m_a_e96ea081("E1"), 8, 5);
   private static Command f_bf_p_1570d10e = new Command(C_bs.m_a_e96ea081("62"), 8, 1);
   public static final Command f_bf_h_1570d10e = new Command(C_bs.m_a_e96ea081("f4"), 8, 1);
   public static final Command f_bf_i_1570d10e = new Command(C_bs.m_a_e96ea081("6"), 4, 1);
   private static Command f_bf_q_1570d10e = new Command(C_bs.m_a_e96ea081("e"), 4, 1);
   public static final Command f_bf_j_1570d10e = new Command(C_bs.m_a_e96ea081("I4"), 8, 4);
   public static final Command f_bf_k_1570d10e = new Command(C_bs.m_a_e96ea081("s4"), 8, 4);
   private static Command f_bf_r_1570d10e = new Command(C_bs.m_a_e96ea081("d3"), 8, 3);
   private static Command f_bf_s_1570d10e = new Command(C_bs.m_a_e96ea081("56"), 8, 4);
   public static final Command f_bf_l_1570d10e = new Command(C_bs.m_a_e96ea081("V2"), 8, 7);
   private static Command f_bf_t_1570d10e = new Command(C_bs.m_a_e96ea081("g0"), 8, 8);
   private static Command f_bf_u_1570d10e = new Command(C_bs.m_a_e96ea081("f0"), 8, 9);
   public static final Command f_bf_m_1570d10e = new Command(C_bs.m_a_e96ea081("i1"), 8, 12);
   private static CommandListener f_bf_a_a2d63fba;
   private static Hashtable f_bf_a_d18d4967;
   private static C_bf f_bf_a_2408ab;
   private static int[] f_bf_a_b4e;
   private static Form f_bf_a_67f46df9;
   private static int f_bf_a_49;
   private static C_s f_bf_c_12b74;
   private static String f_bf_a_523beb0a;
   private static String f_bf_b_523beb0a;
   private static String f_bf_c_523beb0a;
   private static String f_bf_d_523beb0a;
   private static String f_bf_e_523beb0a;
   private static String f_bf_f_523beb0a;
   private static boolean f_bf_a_5a;
   private static long f_bf_a_4a;
   private static int f_bf_b_49;
   private static String f_bf_g_523beb0a;
   private static C_s f_bf_d_12b74;
   private static String[] f_bf_b_6dccaaa5;
   public static String[] f_bf_a_6dccaaa5;
   private static C_s f_bf_e_12b74;
   private static int f_bf_c_49;
   private static final long[] f_bf_a_b4f;
   private static int[] f_bf_b_b4e;
   private static final String[] f_bf_c_6dccaaa5;
   private static TextBox f_bf_a_fd805d5b;
   private Vector f_bf_a_48a69a2c = new Vector();
   private int f_bf_d_49 = 0;
   private static int f_bf_e_49;
   private String f_bf_h_523beb0a;
   public static C_aw f_bf_a_2406f9;
   private static int f_bf_f_49;
   private static C_s f_bf_f_12b74;
   private static Object f_bf_b_5f790d9c;
   private static int f_bf_g_49;
   private static TextBox f_bf_b_fd805d5b;
   private static C_aw f_bf_b_2406f9;
   private static C_s f_bf_g_12b74;
   private static C_aw f_bf_c_2406f9;
   public static C_s f_bf_a_12b74;
   private static C_s f_bf_h_12b74;
   private static TextBox f_bf_c_fd805d5b;
   private static C_s f_bf_i_12b74;
   private static C_ck f_bf_a_240d07;

   public static void m_a_5d527811(Object var0) {
      f_bf_a_5f790d9c = var0;
   }

   public static void m_a_9b75() {
      if (C_bp.m_a_134632(155)) {
         System.gc();
      }

      m_b_5d527811(f_bf_a_5f790d9c);
      C_bt.m_a_1385ff(false);
   }

   public static void m_b_5d527811(Object var0) {
      if (var0 instanceof C_bd) {
         ((C_bd)var0).m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else if (var0 instanceof Displayable) {
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent((Displayable)var0);
      } else {
         C_bn.m_b_9b75();
      }
   }

   public C_bf() {
      f_bf_a_2408ab = this;
   }

   private static boolean p_bf_b_9b79() {
      return C_bp.m_a_134621(88) > 0
         && (f_bf_a_2406f9.f_aw_a_49 & 1024) != 0
         && C_bp.m_a_134621(110) != 2
         && (C_bp.m_a_134621(110) != 3 || f_bf_a_2406f9.m_m_9b68() != 0);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (m_a_cb3ce8a6(f_bf_i_12b74)) {
         if (var1 == f_bf_i_1570d10e) {
            int var3 = 0;
            boolean var4 = false;
            int var10000 = f_bf_i_12b74.m_b_9b68();
            boolean var5 = false;
            C_aw var9 = f_bf_c_2406f9;
            var3 = var10000;
            C_cg var10 = new C_cg(var3, var9);

            try {
               C_ac.m_a_cb38d14b(var10);
            } catch (C_aq var16) {
               C_aq.m_a_481c933f(var16);
            }

            f_bf_i_12b74 = null;
            C_v.m_a_9b75();
         } else {
            f_bf_i_12b74 = null;
            C_v.m_a_9b75();
         }
      } else if (m_a_cb3ce8a6(f_bf_a_12b74)) {
         if (var1 == f_bf_a_1570d10e) {
            String var25;
            C_ap.m_a_aad3b1ff(var25 = f_bf_c_2406f9.m_b_73cf11cb());
            boolean var50 = C_ac.m_a_cb377432(f_bf_c_2406f9);
            boolean var47 = false;
            if (var50) {
               C_aa.m_a_aad3b1ff(var25);
            }
         } else {
            m_a_9b75();
            f_bf_a_12b74 = null;
         }
      } else if (f_bf_c_fd805d5b != null && var2 == f_bf_c_fd805d5b) {
         if (var1 == f_bf_a_1570d10e) {
            String var34;
            if ((var34 = f_bf_c_fd805d5b.getString()) != null && var34.length() != 0) {
               f_bf_c_2406f9.m_a_aad3b1ff(var34);
               f_bf_c_fd805d5b.setString(null);
               C_v.m_a_9b75();
            }
         } else {
            m_a_9b75();
            f_bf_c_fd805d5b = null;
         }
      } else if (m_a_cb3ce8a6(f_bf_h_12b74)) {
         label376:
         if (var1 == f_bf_a_1570d10e) {
            C_bz var26 = new C_bz(f_bf_c_2406f9.m_b_73cf11cb());

            try {
               C_ac.m_a_cb38d14b(var26);
            } catch (C_aq var19) {
               C_aq.m_a_481c933f(var19);
               if (var19.f_aq_a_5a) {
                  break label376;
               }
            }

            C_v.m_a_9b75();
         } else {
            m_a_9b75();
            f_bf_h_12b74 = null;
         }
      } else if (m_a_cb3ce8a6(f_bf_g_12b74)) {
         if (var1 == f_bf_i_1570d10e) {
            int var51 = f_bf_g_12b74.m_b_9b68();
            short var27 = 0;
            switch (var51) {
               case 1:
                  m_a_b7252e78(f_bf_c_2406f9, null);
               case 2:
               case 6:
               default:
                  break;
               case 3:
                  long var48;
                  label366:
                  if ((var48 = f_bf_c_2406f9.m_b_134621(192)) != 0L && var48 != -1L && var48 != 256L) {
                     label362: {
                        if (var48 != 1L) {
                           if (var48 == 16L) {
                              var27 = 1001;
                              break label362;
                           }

                           if (var48 == 2L) {
                              var27 = 1003;
                              break label362;
                           }

                           if (var48 == 32L) {
                              var27 = 1004;
                              break label362;
                           }

                           if (var48 == 4L) {
                              var27 = 1002;
                              break label362;
                           }

                           if (var48 == 12288L) {
                              var27 = 1003;
                              break label362;
                           }

                           if (var48 == 16384L) {
                              var27 = 1003;
                              break label362;
                           }

                           if (var48 == 20480L) {
                              var27 = 1004;
                              break label362;
                           }

                           if (var48 == 24576L) {
                              var27 = 1001;
                              break label362;
                           }
                        }

                        var27 = 1000;
                     }

                     f_bf_c_2406f9.f_aw_d_5a = true;
                     C_d var31 = new C_d(C_bp.m_a_47921032(254), f_bf_c_2406f9, var27, C_cf.a$1385f3(), "");
                     C_bu var32 = new C_bu(var31);

                     try {
                        C_ac.m_a_cb38d14b(var32);
                     } catch (C_aq var18) {
                        C_aq.m_a_481c933f(var18);
                        if (var18.f_aq_a_5a) {
                           break label366;
                        }
                     }

                     C_v.m_a_9b75();
                  }
                  break;
               case 4:
                  m_a_a27867fb(10002, f_bf_c_2406f9, "X4", "w4");
                  break;
               case 5:
                  C_ab var52 = new C_ab(f_bf_c_2406f9);
                  Object var29 = null;
                  var52.m_a_9b75();
                  break;
               case 7:
                  f_bf_a_12b74 = a$680342a1(f_bf_c_2406f9, C_bs.m_a_e96ea081("N4"), C_bs.m_a_e96ea081("N4") + " " + f_bf_c_2406f9.f_aw_a_523beb0a + "?");
                  break;
               case 8:
                  f_bf_h_12b74 = a$680342a1(f_bf_c_2406f9, C_bs.m_a_e96ea081("O4"), C_bs.m_a_e96ea081("P4") + f_bf_c_2406f9.f_aw_a_523beb0a + "?");
                  break;
               case 9:
                  (f_bf_c_fd805d5b = new TextBox(C_bs.m_a_e96ea081("V4"), f_bf_c_2406f9.f_aw_a_523beb0a, 64, 0)).addCommand(f_bf_a_1570d10e);
                  f_bf_c_fd805d5b.addCommand(f_bf_b_1570d10e);
                  f_bf_c_fd805d5b.setCommandListener(f_bf_a_2408ab);
                  Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bf_c_fd805d5b);
                  break;
               case 10:
                  C_aa.m_a_e925fa09(f_bf_c_2406f9.m_b_73cf11cb(), f_bf_c_2406f9.f_aw_a_523beb0a);
                  break;
               case 11:
                  p_bf_c_cb37742e(f_bf_c_2406f9);
                  break;
               case 12:
                  m_a_e925fa09(f_bf_c_2406f9.m_b_73cf11cb(), f_bf_c_2406f9.f_aw_a_523beb0a);
                  break;
               case 13:
                  m_c_9b75();
                  m_a_4c1e0f27(true, "***error***", null, f_bf_c_2406f9.m_b_73cf11cb(), m_a_a9514c81(true));
                  C_v.m_a_9b75();
                  break;
               case 14:
                  m_b_cb37742e(f_bf_c_2406f9);
                  break;
               case 15:
                  f_bf_a_b4e = m_a_62c27a66("Y2", 5, f_bf_a_2408ab, 1, f_bf_c_2406f9.m_b_134621(65));
                  break;
               case 16:
                  m_a_9c60de72(f_bf_i_12b74 = new C_s(C_bs.m_a_e96ea081("a5")), false);
                  String var28 = f_bf_c_2406f9.m_m_9b68() == 0 ? "u0" : "S4";
                  String var38 = f_bf_c_2406f9.m_n_9b68() == 0 ? "v0" : "T4";
                  String var44 = f_bf_c_2406f9.m_l_9b68() == 0 ? "w0" : "U4";
                  C_f var6 = f_bf_c_2406f9.m_m_9b68() == 0 ? C_v.f_v_b_12a1f.m_a_485a59b9(5) : C_v.f_v_b_12a1f.m_a_485a59b9(6);
                  C_f var7 = f_bf_c_2406f9.m_n_9b68() == 0 ? C_v.f_v_b_12a1f.m_a_485a59b9(5) : C_v.f_v_b_12a1f.m_a_485a59b9(6);
                  C_f var8 = f_bf_c_2406f9.m_l_9b68() == 0 ? C_v.f_v_b_12a1f.m_a_485a59b9(5) : C_v.f_v_b_12a1f.m_a_485a59b9(6);
                  f_bf_i_12b74.m_i_9b75();
                  m_a_d35e089(f_bf_i_12b74, var28, var6, 2, true);
                  m_a_d35e089(f_bf_i_12b74, var38, var7, 3, true);
                  m_a_d35e089(f_bf_i_12b74, var44, var8, 14, true);
                  f_bf_i_12b74.m_j_9b75();
                  f_bf_i_12b74.c$13462e();
                  f_bf_i_12b74.d$1385ff();
                  f_bf_i_12b74.m_a_48817c60(f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  f_bf_i_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  f_bf_i_12b74.m_a_6f63a2af(f_bf_a_2408ab);
                  f_bf_i_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  break;
               case 17:
                  try {
                     C_m.m_a_afa300e4(f_bf_c_2406f9.m_b_73cf11cb(), 0);
                  } catch (Exception var15) {
                  }

                  f_bf_c_2406f9.f_aw_a_5a = false;
                  f_bf_c_2406f9.f_aw_c_5a = true;
                  C_v.m_a_9b75();
            }
         } else {
            m_a_9b75();
            f_bf_g_12b74 = null;
            f_bf_c_2406f9 = null;
         }
      } else if (f_bf_b_fd805d5b != null && var2 == f_bf_b_fd805d5b) {
         if (var1 == f_bf_q_1570d10e) {
            C_ad var33 = null;
            String var39;
            String var45 = (var39 = f_bf_b_fd805d5b.getString()) != null && var39.length() >= 1 ? var39 : "";
            switch (f_bf_g_49) {
               case 10001:
                  var33 = new C_ad(4, f_bf_b_2406f9.m_b_73cf11cb(), false, var45);
                  break;
               case 10002:
                  var33 = new C_ad(5, f_bf_b_2406f9.m_b_73cf11cb(), false, var45);
            }

            C_ao var20 = new C_ao(var33);
            C_cm var22 = new C_cm(f_bf_b_2406f9, 5);

            try {
               C_ac.m_a_cb38d14b(var20);
               if (f_bf_b_2406f9.m_a_134632(8)) {
                  C_ac.m_a_cb38d14b(var22);
               }
            } catch (C_aq var17) {
               C_aq.m_a_481c933f(var17);
               if (var17.f_aq_a_5a) {
                  return;
               }
            }

            switch (f_bf_g_49) {
               case 10001:
                  f_bf_b_2406f9.m_a_255f295(70, 0);
                  C_ap.m_b_cb37742e(f_bf_b_2406f9);
                  break;
               case 10002:
                  f_bf_b_2406f9.m_a_2563266(8, false);
            }
         }

         if (!C_ap.m_a_cb377432(f_bf_b_2406f9)) {
            C_v.m_a_9b75();
         }

         f_bf_b_fd805d5b = null;
         f_bf_b_2406f9 = null;
         return;
      }

      if (m_a_cb3ce8a6(f_bf_f_12b74)) {
         if (var1 == f_bf_i_1570d10e) {
            try {
               Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest(f_bf_f_12b74.m_a_4dee1afa(0, false));
            } catch (Exception var11) {
            }
         }

         m_b_5d527811(f_bf_b_5f790d9c);
         f_bf_f_12b74 = null;
         f_bf_b_5f790d9c = null;
      } else {
         if (f_bf_a_fd805d5b != null && var2 == f_bf_a_fd805d5b) {
            if (!C_bp.m_a_134632(146) && (var1 == f_bf_b_1570d10e || var1 == f_bf_q_1570d10e) && p_bf_b_9b79()) {
               try {
                  C_ac.m_a_afa340b5(f_bf_a_2406f9.m_b_73cf11cb(), false);
               } catch (C_aq var14) {
               }
            }

            if (var1 == f_bf_b_1570d10e) {
               m_a_9b75();
               return;
            }

            if (var1 == f_bf_q_1570d10e) {
               switch (f_bf_f_49) {
                  case 200001:
                     String var43;
                     if ((var43 = this.p_bf_c_73cf11cb()).length() != 0) {
                        m_a_122835b8(var43, f_bf_a_2406f9);
                        this.b$552c4e01();
                        if (C_ap.m_a_cb377432(f_bf_a_2406f9)) {
                           return;
                        }
                     }

                     m_a_9b75();
                  default:
                     return;
               }
            }

            if (var1 == f_bf_r_1570d10e) {
               C_ah.m_a_23a88fec(f_bf_a_fd805d5b, f_bf_a_fd805d5b);
               return;
            }

            if (var1 == f_bf_s_1570d10e) {
               C_cd.m_a_23a88fec(f_bf_a_fd805d5b, f_bf_a_fd805d5b);
               return;
            }

            if (var1 == f_bf_m_1570d10e) {
               this.b$552c4e01();
               return;
            }

            if (var1 == f_bf_j_1570d10e || var1 == f_bf_k_1570d10e) {
               int var42 = f_bf_a_fd805d5b.getString().length();
               this.p_bf_a_afa300e4(m_a_a9514c81(var1 == f_bf_j_1570d10e), var42);
               return;
            }

            if (var1 == f_bf_u_1570d10e) {
               f_bf_a_fd805d5b.setString(C_aj.m_a_e96ea081(f_bf_a_fd805d5b.getString()));
               return;
            }

            if (var1 == f_bf_t_1570d10e) {
               f_bf_a_fd805d5b.setString(C_aj.m_b_e96ea081(f_bf_a_fd805d5b.getString()));
               return;
            }
         } else if (m_a_cb3ce8a6(f_bf_c_12b74)) {
            if (var1 == f_bf_c_1570d10e) {
               C_bn.m_b_9b75();
               f_bf_c_12b74 = null;
               return;
            }

            if (var1 == f_bf_i_1570d10e) {
               String var37 = null;
               int var54 = f_bf_c_12b74.m_b_9b68();
               boolean var41 = false;
               switch (var54) {
                  case 1000:
                     var37 = C_cf.m_a_55a39fc4(C_cf.f_cf_p_b47, 0, C_cf.f_cf_p_b47.length);
                     break;
                  case 1001:
                     var37 = "http://wapland.org/forum";
                     break;
                  case 1002:
                     var37 = C_cf.m_a_55a39fc4(C_cf.f_cf_q_b47, 0, C_cf.f_cf_p_b47.length);
               }

               if (var37 != null) {
                  try {
                     Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest(var37);
                     return;
                  } catch (Exception var12) {
                     return;
                  }
               }
            }
         } else if (!m_a_cb3ce8a6(f_bf_d_12b74)) {
            if (m_a_cb3ce8a6(f_bf_e_12b74)) {
               f_bf_c_49 = f_bf_e_12b74.m_b_9b68();
               if (var1 == f_bf_b_1570d10e) {
                  m_a_9b75();
                  p_bf_d_9b75();
                  return;
               }

               if (var1 == f_bf_a_1570d10e) {
                  if (f_bf_a_49 == 5) {
                     int var36 = f_bf_c_2406f9.m_b_134621(65);
                     int var40 = f_bf_a_b4e[f_bf_c_49];
                     C_l var46 = C_v.m_a_485a5a73(var36);
                     C_l var21 = C_v.m_a_485a5a73(var40);
                     C_cm var23 = new C_cm(f_bf_c_2406f9, var46, var21);

                     try {
                        C_ac.m_a_cb38d14b(var23);
                        C_cn.m_a_33097a9f("47", var23, false);
                     } catch (C_aq var13) {
                        C_aq.m_a_481c933f(var13);
                     }
                  } else {
                     f_bf_a_a2d63fba.commandAction(var1, var2);
                  }

                  p_bf_d_9b75();
                  return;
               }
            } else if (f_bf_a_67f46df9 != null && var2 == f_bf_a_67f46df9) {
               f_bf_a_a2d63fba.commandAction(var1, var2);
               f_bf_a_67f46df9 = null;
               f_bf_a_49 = -1;
            }
         } else {
            if (var1 == f_bf_b_1570d10e || var1 == f_bf_c_1570d10e) {
               m_a_9b75();
            }

            if (var1 == f_bf_p_1570d10e) {
               String[] var53 = f_bf_b_6dccaaa5;
               Displayable var49 = Jimm.f_jimm_Jimm_a_4a58c677.getCurrent();
               String[] var35 = var53;
               C_am.f_am_a_6dccaaa5 = var53;
               (C_am.f_am_a_2405c3 = new C_am(var49)).f_am_a_aa0ede5b.setSelectedIndex(C_cf.m_b_aad3b1f2(var35[11]), true);
               C_am.f_am_a_2405c3.f_am_a_a555694c.setString(var35[1]);
               C_am.f_am_a_2405c3.f_am_d_a555694c.setString(var35[3]);
               C_am.f_am_a_2405c3.f_am_e_a555694c.setString(var35[13]);
               C_am.f_am_a_2405c3.f_am_b_a555694c.setString(var35[38]);
               C_am.f_am_a_2405c3.f_am_c_a555694c.setString(var35[39]);
               C_am.f_am_a_2405c3.f_am_f_a555694c.setString(var35[12]);
               C_am.f_am_a_2405c3.f_am_g_a555694c.setString(var35[22]);
               C_am.f_am_a_2405c3.f_am_h_a555694c.setString(var35[4]);
               new Thread(C_am.f_am_a_2405c3).start();
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(C_am.f_am_a_2405c3);
               C_bt.m_a_1385ff(true);
               return;
            }

            if (var1 == f_bf_e_1570d10e || var1 == f_bf_g_1570d10e) {
               m_c_9b75();
               p_bf_b_1385ff(var1 == f_bf_g_1570d10e);
               f_bf_d_12b74.m_a_48817c60(f_bf_f_1570d10e, C_bd.f_bd_g_49);
               return;
            }

            if (var1 == f_bf_f_1570d10e) {
               p_bf_b_1385ff(false);
               return;
            }
         }
      }
   }

   private static void p_bf_d_9b75() {
      f_bf_e_12b74 = null;
      f_bf_a_a2d63fba = null;
      f_bf_a_49 = -1;
   }

   public static void m_a_75ba1f9b(Object var0, String var1) {
      if (var0 instanceof C_bd) {
         ((C_bd)var0).m_c_aad3b1ff(var1);
      } else {
         if (var0 instanceof Displayable) {
            ((Displayable)var0).setTitle(var1);
         }
      }
   }

   public static String m_a_69d680af(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = null;
         if (var0 instanceof C_bd) {
            var1 = ((C_bd)var0).m_b_73cf11cb();
         } else if (var0 instanceof Displayable) {
            var1 = ((Displayable)var0).getTitle();
         }

         return var1;
      }
   }

   public static int m_a_9b68() {
      if (f_bf_a_67f46df9 != null && f_bf_a_67f46df9.isShown()) {
         return f_bf_a_49;
      } else if (m_a_cb3ce8a6(f_bf_b_12b74)) {
         return f_bf_a_49;
      } else {
         return m_a_cb3ce8a6(f_bf_e_12b74) ? f_bf_a_49 : -1;
      }
   }

   public static int m_a_48817c53(Command var0, int var1) {
      Object var2;
      return f_bf_a_49 == var1 && (var2 = f_bf_a_d18d4967.get(var0)) != null ? (Integer)var2 : -1;
   }

   public static void a$4d6ecd91(String var0, String var1, CommandListener var2, int var3) {
      f_bf_a_67f46df9 = null;
      f_bf_c_12b74 = null;
      System.gc();
      f_bf_a_49 = var3;
      (f_bf_a_67f46df9 = new Form(var0)).append(var1);
      f_bf_a_67f46df9.addCommand(f_bf_n_1570d10e);
      f_bf_a_67f46df9.addCommand(f_bf_o_1570d10e);
      f_bf_a_a2d63fba = var2;
      f_bf_a_67f46df9.setCommandListener(f_bf_a_2408ab);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bf_a_67f46df9);
   }

   public static C_s a$680342a1(C_aw var0, String var1, String var2) {
      (f_bf_b_12b74 = new C_s(var1)).c$13462e();
      m_a_9c60de72(f_bf_b_12b74, false);
      f_bf_b_12b74.m_b_13462e(C_bp.m_a_134621(111) << 3);
      f_bf_b_12b74.m_a_68a79fe2(var2, f_bf_b_12b74.m_d_9b68(), C_bp.m_a_134621(112), -1);
      f_bf_b_12b74.m_a_48817c60(f_bf_a_1570d10e, C_bd.f_bd_e_49);
      f_bf_b_12b74.m_a_48817c60(f_bf_b_1570d10e, C_bd.f_bd_f_49);
      f_bf_c_2406f9 = var0;
      f_bf_b_12b74.m_a_6f63a2af(f_bf_a_2408ab);
      f_bf_b_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      return f_bf_b_12b74;
   }

   public static void m_b_9b75() {
      System.gc();
      long var0 = Runtime.getRuntime().freeMemory() / 1024L;
      if (f_bf_c_12b74 == null) {
         f_bf_c_12b74 = new C_s(null);
      }

      f_bf_c_12b74.m_i_9b75();
      f_bf_c_12b74.m_a_9b75();
      f_bf_c_12b74.c$13462e();
      m_a_9c60de72(f_bf_c_12b74, false);
      f_bf_c_12b74.m_c_aad3b1ff(C_bs.m_a_e96ea081("m0"));
      f_bf_c_12b74.m_b_13462e(0);
      String var2 = ", ";
      C_f var3 = C_v.f_v_a_12a1f.m_a_485a59b9(15);
      int var4 = C_bp.m_a_134621(112);
      int var5 = f_bf_c_12b74.m_d_9b68();
      if ((f_bf_b_523beb0a = C_bp.m_a_47921032(39)) != null && f_bf_b_523beb0a.length() > 5) {
         if (var3 != null) {
            f_bf_c_12b74.m_a_3371d4d1(var3, null, 1002);
         }

         f_bf_c_12b74.m_a_68a79fe2(f_bf_b_523beb0a, var5, var4, 1002);
         f_bf_c_12b74.m_a_485a5b4c(1002);
      }

      StringBuffer var6;
      (var6 = new StringBuffer()).append(" ").append(C_bs.m_a_e96ea081("n0"));
      f_bf_c_12b74.m_a_68a79fe2(var6.toString(), var5, var4, -1);
      if (var3 != null) {
         f_bf_c_12b74.m_a_3371d4d1(var3, null, 1000);
      }

      f_bf_c_12b74.m_a_68a79fe2(" http://www.jimm.im", var5, var4, 1000);
      f_bf_c_12b74.m_a_485a5b4c(1000);
      if (var3 != null) {
         f_bf_c_12b74.m_a_3371d4d1(var3, null, 1001);
      }

      f_bf_c_12b74.m_a_68a79fe2(" http://wapland.org", var5, var4, 1001);
      f_bf_c_12b74.m_a_485a5b4c(1001);
      (var6 = new StringBuffer()).append("\n").append(C_bs.m_a_e96ea081("M3")).append(": \n").append(Jimm.f_jimm_Jimm_b_523beb0a);
      if (Jimm.f_jimm_Jimm_c_523beb0a != null) {
         var6.append(var2).append(Jimm.f_jimm_Jimm_c_523beb0a);
      }

      String var7;
      if ((var7 = System.getProperty("microedition.locale")) != null) {
         var6.append(var2).append(var7);
      }

      var6.append("\n\n")
         .append(C_bs.m_a_e96ea081("H2"))
         .append(": ")
         .append(var0)
         .append("kb\n")
         .append(C_bs.m_a_e96ea081("b6"))
         .append(": ")
         .append(Runtime.getRuntime().totalMemory() / 1024L)
         .append("kb\n\n")
         .append(C_bs.m_a_e96ea081("s3"))
         .append(": ");
      if (f_bf_a_523beb0a != null) {
         var6.append(f_bf_a_523beb0a);
      } else {
         var6.append("...");
      }

      f_bf_c_12b74.m_a_68a79fe2(var6.toString(), var5, var4, -1);
      f_bf_c_12b74.m_a_48817c60(f_bf_i_1570d10e, C_bd.f_bd_e_49);
      f_bf_c_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
      f_bf_c_12b74.m_a_6f63a2af(f_bf_a_2408ab);
      f_bf_c_12b74.m_j_9b75();
      f_bf_c_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      if (f_bf_a_523beb0a == null) {
         Jimm.m_a_94e56161().schedule(new C_g(), 2000L);
      }
   }

   private static String p_bf_a_8ea82db7(String var0, String var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = var0.length();
      boolean var4 = true;

      for (int var5 = 0; var5 < var3; var5++) {
         char var6 = var0.charAt(var5);
         if (var4) {
            var2.append(var1);
         }

         var2.append(var6);
         var4 = var6 == '\n';
      }

      return var2.toString();
   }

   public static boolean m_a_9b79() {
      return f_bf_c_523beb0a == null;
   }

   public static String m_a_a9514c81(boolean var0) {
      if (!var0) {
         return f_bf_f_523beb0a;
      } else {
         StringBuffer var1 = new StringBuffer();
         if (f_bf_e_523beb0a != null) {
            var1.append(f_bf_e_523beb0a);
         }

         if (f_bf_d_523beb0a != null) {
            var1.append('[').append(f_bf_d_523beb0a).append(']').append('\n');
         }

         if (f_bf_c_523beb0a != null) {
            var1.append(p_bf_a_8ea82db7(f_bf_c_523beb0a, f_bf_a_5a ? ">> " : "<< "));
         }

         return var1.toString();
      }
   }

   public static void m_c_9b75() {
      f_bf_c_523beb0a = null;
      f_bf_d_523beb0a = null;
      f_bf_e_523beb0a = null;
      f_bf_f_523beb0a = "";
   }

   public static void m_a_4c1e0f27(boolean var0, String var1, String var2, String var3, String var4) {
      f_bf_c_523beb0a = var3;
      f_bf_e_523beb0a = var4;
      f_bf_f_523beb0a = f_bf_f_523beb0a + var3;
      if (var1 != "***error***") {
         f_bf_d_523beb0a = var2 + ' ' + var1;
      } else {
         f_bf_d_523beb0a = var2;
      }

      f_bf_a_5a = var0;
   }

   private static void p_bf_b_1385ff(boolean var0) {
      m_a_4c1e0f27(true, "***error***", m_a_69d680af(f_bf_d_12b74), f_bf_d_12b74.m_a_4dee1afa(0, var0), m_a_a9514c81(true));
   }

   public static void m_a_9c60de72(C_bd var0, boolean var1) {
      if (var0 != null) {
         var0.m_a_c4b201b5(C_bp.m_a_134621(103), C_bp.m_a_134621(106), C_bp.m_a_134621(102), C_bp.m_a_134621(103));
         if (var1) {
            var0.m_b_1385ff(true);
         } else {
            var0.m_b_1385ff(false);
         }
      }
   }

   public static void m_a_1385ff(boolean var0) {
      if (var0) {
         C_aa.m_b_9b75();
         C_ap.m_a_9b75();
      }

      m_a_9c60de72(C_v.m_a_46a7a4ee(), C_bp.m_a_134632(145));
   }

   public static boolean m_a_db398112(C_aw var0, int var1, int var2) {
      if (C_bd.f_bd_a_5a && var2 == 3) {
         C_bd.f_bd_a_5a = false;

         for (int var3 = 0; var3 < C_bp.f_bp_a_49; var3++) {
            if (var1 == C_bp.f_bp_a_b4e[var3]) {
               p_bf_a_2a6fa57c(C_bp.m_a_47921032(40).charAt(var3), var0, var2);
               return true;
            }
         }
      }

      return false;
   }

   public static void m_a_db39810e(C_aw var0, int var1, int var2) {
      switch (var1) {
         case 35:
            p_bf_a_2a6fa57c(C_bp.m_a_134621(82), var0, var2);
            break;
         case 42:
            p_bf_a_2a6fa57c(C_bp.m_a_134621(78), var0, var2);
            return;
         case 48:
            if (!C_bp.m_a_134632(175)) {
               p_bf_a_2a6fa57c(C_bp.m_a_134621(77), var0, var2);
               return;
            }

            if (var2 == 3) {
               C_bd.f_bd_a_5a = true;
               return;
            }
            break;
         case 52:
            p_bf_a_2a6fa57c(C_bp.m_a_134621(79), var0, var2);
            return;
         case 54:
            p_bf_a_2a6fa57c(C_bp.m_a_134621(80), var0, var2);
            return;
      }
   }

   private static void p_bf_a_2a6fa57c(int var0, C_aw var1, int var2) {
      if (var2 == 3) {
         f_bf_a_4a = System.currentTimeMillis();
         switch (var0) {
            case 2:
               if (var1 != null) {
                  m_a_e925fa09(var1.m_b_73cf11cb(), var1.f_aw_a_523beb0a);
                  return;
               }
               break;
            case 3:
               if (var1 != null) {
                  m_a_b7252e78(var1, null);
                  return;
               }
               break;
            case 4:
               if (C_bp.m_a_134632(130)) {
                  C_bp.m_a_2563266(130, false);
               } else {
                  C_bp.m_a_2563266(130, true);
               }

               C_bp.m_c_9b75();
               C_v.m_a_1385ff(true);
               C_v.m_a_9b75();
               return;
            case 5:
               C_bp.m_d_9b75();
               return;
            case 6:
               C_bn.m_b_9b75();
               return;
            case 7:
            case 9:
            case 13:
            default:
               break;
            case 8:
               if (var1 != null) {
                  var1.m_b_9b75();
                  return;
               }
               break;
            case 10:
               if (var1 != null) {
                  p_bf_c_cb37742e(var1);
                  return;
               }
               break;
            case 11:
               boolean var6;
               C_bd.m_c_1385ff(var6 = !C_bp.m_a_134632(145));
               C_bp.m_a_2563266(145, var6);
               C_bp.m_c_9b75();
               C_v.m_a_9b75();
               return;
            case 12:
               C_v.m_a_138603(true);
               return;
            case 14:
               if (C_bp.m_a_134632(172)) {
                  C_bq.m_a_9b75();
                  return;
               }
               break;
            case 15:
               C_ap.m_a_afa300e4(null, 3);
               C_v.m_a_9b75();
               return;
            case 16:
               if (var1 instanceof C_aw && var1.m_a_2477b4f().m_b_9b68() != -1) {
                  try {
                     C_m.m_a_afa300e4(var1.m_a_47921032(0), 0);
                  } catch (Exception var5) {
                  }

                  var1.f_aw_a_5a = false;
                  var1.f_aw_c_5a = true;
               }
         }
      } else {
         if (var2 == 2 || var2 == 3) {
            if (f_bf_a_4a == -1L) {
               return;
            }

            long var3 = System.currentTimeMillis() - f_bf_a_4a;
            if (var0 == 7 && var3 > 900L) {
               f_bf_a_4a = -1L;
               C_cn.m_c_9b75();
            }
         }
      }
   }

   private static void p_bf_a_bc5f2585(String var0, String var1, C_s var2) {
      if (f_bf_g_523beb0a != null) {
         var2.m_a_68a79fe2(C_bs.m_a_e96ea081(f_bf_g_523beb0a), var2.m_d_9b68(), 1 + C_bp.f_bp_g_49, -1).m_a_485a5b4c(-1);
         f_bf_g_523beb0a = null;
      }

      var2.m_a_68a79fe2(C_bs.m_a_e96ea081(var1) + ": ", var2.m_d_9b68(), C_bp.f_bp_g_49, f_bf_b_49)
         .m_a_68a79fe2(var0, C_bp.m_a_134621(104), C_bp.f_bp_g_49, f_bf_b_49)
         .m_a_485a5b4c(f_bf_b_49);
      f_bf_b_49++;
   }

   private static void p_bf_a_a21c693(int var0, String[] var1, String var2, C_s var3) {
      String var4;
      if ((var4 = var1[var0]) != null) {
         if (var4.length() != 0) {
            p_bf_a_bc5f2585(var4, var2, var3);
         }
      }
   }

   public static void m_a_2f0d6064(String[] var0, C_s var1) {
      f_bf_g_523beb0a = "B3";
      p_bf_a_a21c693(37, var0, "i6", var1);
      p_bf_a_a21c693(1, var0, "X3", var1);
      p_bf_a_a21c693(2, var0, "T3", var1);
      p_bf_a_a21c693(11, var0, "U2", var1);
      p_bf_a_a21c693(10, var0, "y0", var1);
      p_bf_a_a21c693(3, var0, "72", var1);
      if (var0[24] != null) {
         p_bf_a_bc5f2585(var0[24].equals("1") ? C_bs.m_a_e96ea081("f7") : C_bs.m_a_e96ea081("Y3"), "K0", var1);
      }

      p_bf_a_a21c693(13, var0, "W0", var1);
      p_bf_a_a21c693(12, var0, "43", var1);
      p_bf_a_a21c693(22, var0, "54", var1);
      p_bf_a_a21c693(23, var0, "e3", var1);
      f_bf_g_523beb0a = "e3";
      if (var0[40] != null) {
         p_bf_a_a21c693(44, var0, var0[40], var1);
      }

      if (var0[41] != null) {
         p_bf_a_a21c693(45, var0, var0[41], var1);
      }

      if (var0[42] != null) {
         p_bf_a_a21c693(46, var0, var0[42], var1);
      }

      if (var0[43] != null) {
         p_bf_a_a21c693(47, var0, var0[43], var1);
      }

      if (var0[25] != null) {
         int var2 = Integer.parseInt(var0[25]);
         byte var3 = 0;
         if (var2 == 0) {
            var3 = 6;
         } else if (var2 == 1) {
            var3 = 7;
         } else if (var2 == 2) {
            var3 = 3;
         }

         var1.m_a_68a79fe2(C_bs.m_a_e96ea081("D5") + ": ", var1.m_d_9b68(), C_bp.f_bp_g_49, f_bf_b_49)
            .m_a_3371d4d1(C_v.m_a_247797e().m_a_485a59b9(var3), null, f_bf_b_49)
            .m_a_485a5b4c(f_bf_b_49);
         f_bf_b_49++;
      }

      f_bf_g_523beb0a = "53";
      p_bf_a_a21c693(4, var0, "g1", var1);
      p_bf_a_a21c693(5, var0, "C5", var1);
      p_bf_a_a21c693(8, var0, "x0", var1);
      p_bf_a_a21c693(6, var0, "t4", var1);
      p_bf_a_a21c693(9, var0, "71", var1);
      p_bf_a_a21c693(7, var0, "q2", var1);
      f_bf_g_523beb0a = "d7";
      p_bf_a_a21c693(19, var0, "a6", var1);
      p_bf_a_a21c693(20, var0, "12", var1);
      p_bf_a_a21c693(21, var0, "y4", var1);
      p_bf_a_a21c693(14, var0, "g1", var1);
      p_bf_a_a21c693(15, var0, "C5", var1);
      p_bf_a_a21c693(18, var0, "x0", var1);
      p_bf_a_a21c693(16, var0, "t4", var1);
      p_bf_a_a21c693(17, var0, "q2", var1);
      f_bf_g_523beb0a = "Q1";
      p_bf_a_a21c693(26, var0, "a3", var1);
      p_bf_a_a21c693(27, var0, "w3", var1);
      p_bf_a_a21c693(28, var0, "x3", var1);
      p_bf_a_a21c693(36, var0, "u3", var1);
      p_bf_a_a21c693(29, var0, "y3", var1);
      p_bf_a_a21c693(30, var0, "v3", var1);
      f_bf_g_523beb0a = "DC Information";
      p_bf_a_a21c693(31, var0, "ICQ version", var1);
      p_bf_a_a21c693(32, var0, "Int IP", var1);
      p_bf_a_a21c693(33, var0, "Ext IP", var1);
      p_bf_a_a21c693(34, var0, "Port", var1);
      p_bf_a_a21c693(35, var0, "61", var1);
   }

   public static void m_a_e925fa09(String var0, String var1) {
      (f_bf_d_12b74 = m_a_53d5d013(var0, false)).m_a_6f63a2af(f_bf_a_2408ab);
      f_bf_d_12b74.m_a_cb37e88d(f_bf_a_2408ab);
      if (!C_ac.m_b_9b79()) {
         String[] var9;
         (var9 = new String[48])[1] = var1;
         var9[37] = var0;
         m_a_3231c38a(var9);
         f_bf_d_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else {
         if (var0 == C_bp.m_a_47921032(254)) {
            f_bf_d_12b74.m_a_48817c60(f_bf_p_1570d10e, C_bd.f_bd_g_49);
         }

         C_bh var2 = new C_bh(var0, var1);
         f_bf_d_12b74.m_a_48817c60(f_bf_b_1570d10e, C_bd.f_bd_f_49);

         try {
            C_ac.m_a_cb38d14b(var2);
         } catch (C_aq var3) {
            C_aq.m_a_481c933f(var3);
            if (var3.f_aq_a_5a) {
               return;
            }
         }

         C_s var10000 = f_bf_d_12b74;
         var1 = C_bs.m_a_e96ea081("47");
         C_s var4 = var10000;
         int var10002 = var4.m_d_9b68();
         boolean var5 = false;
         int var8 = var10002;
         C_s var6 = var10000;
         var10000.m_a_68bdbb6d(var1, var8, C_bp.f_bp_g_49, -1, true, '\u0000');
         var6.m_e_9b75();
         f_bf_d_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      }
   }

   public static void m_a_3231c38a(String[] var0) {
      f_bf_b_6dccaaa5 = var0;
      if (f_bf_d_12b74 != null) {
         f_bf_d_12b74.m_a_9b75();
         m_a_2f0d6064(var0, f_bf_d_12b74);
         f_bf_d_12b74.m_a_8eb9d703(f_bf_b_1570d10e);
         f_bf_d_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
         f_bf_d_12b74.m_a_48817c60(f_bf_h_1570d10e, C_bd.f_bd_e_49);
         f_bf_d_12b74.m_a_48817c60(f_bf_e_1570d10e, C_bd.f_bd_g_49);
         if (!m_a_9b79()) {
            f_bf_d_12b74.m_a_48817c60(f_bf_f_1570d10e, C_bd.f_bd_g_49);
         }

         f_bf_d_12b74.m_a_48817c60(f_bf_g_1570d10e, C_bd.f_bd_g_49);
      }
   }

   public static C_s m_a_53d5d013(String var0, boolean var1) {
      (f_bf_d_12b74 = new C_s(null)).m_b_13462e(0);
      f_bf_d_12b74.m_c_aad3b1ff(var0);
      m_a_9c60de72(f_bf_d_12b74, false);
      f_bf_d_12b74.c$13462e();
      if (var1) {
         f_bf_d_12b74.m_a_48817c60(f_bf_h_1570d10e, C_bd.f_bd_e_49);
         f_bf_d_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
         f_bf_d_12b74.m_a_48817c60(f_bf_e_1570d10e, C_bd.f_bd_g_49);
         if (!m_a_9b79()) {
            f_bf_d_12b74.m_a_48817c60(f_bf_f_1570d10e, C_bd.f_bd_g_49);
         }

         f_bf_d_12b74.m_a_48817c60(f_bf_g_1570d10e, C_bd.f_bd_g_49);
         f_bf_d_12b74.m_a_6f63a2af(f_bf_a_2408ab);
         f_bf_d_12b74.m_a_cb37e88d(f_bf_a_2408ab);
      }

      return f_bf_d_12b74;
   }

   public static void m_a_48a5fc99(C_s var0) {
      var0.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static void m_a_1cc7afeb(String var0, String[] var1, CommandListener var2, int var3, boolean var4) {
      if (var4) {
         for (int var5 = 0; var5 < var1.length; var5++) {
            var1[var5] = C_bs.m_a_e96ea081(var1[var5]);
         }
      }

      f_bf_a_49 = var3;
      m_a_9c60de72(f_bf_e_12b74 = new C_s(C_bs.m_a_e96ea081(var0)), false);
      f_bf_e_12b74.c$13462e();
      f_bf_e_12b74.d$1385ff();
      f_bf_e_12b74.m_b_13462e(C_bp.m_a_134621(111) << 3);

      for (int var6 = 0; var6 < var1.length; var6++) {
         m_a_d35e089(f_bf_e_12b74, var1[var6], C_v.f_v_b_12a1f.m_a_485a59b9(10), var6, var4);
      }

      f_bf_e_12b74.m_a_48817c60(f_bf_a_1570d10e, C_bd.f_bd_e_49);
      f_bf_e_12b74.m_a_48817c60(f_bf_b_1570d10e, C_bd.f_bd_f_49);
      f_bf_e_12b74.m_a_6f63a2af(f_bf_a_2408ab);
      f_bf_a_a2d63fba = var2;
      f_bf_e_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static int m_b_9b68() {
      return f_bf_c_49;
   }

   public static void m_a_836af5c3(C_s var0, String var1, int var2, int var3) {
      C_ah.m_a_e9f435a0(var0, var1, C_bp.f_bp_g_49, var2, var3);
      var0.m_a_485a5b4c(var3);
   }

   private static int p_bf_b_1349e2(long var0) {
      for (int var2 = 0; var2 < f_bf_a_b4f.length; var2++) {
         if (f_bf_a_b4f[var2] == var0) {
            return var2;
         }
      }

      return -1;
   }

   public static int m_a_1349e2(long var0) {
      int var2;
      return (var2 = p_bf_b_1349e2(var0)) == -1 ? -1 : f_bf_b_b4e[var2];
   }

   public static String m_a_2f33e691(long var0) {
      int var2;
      return (var2 = p_bf_b_1349e2(var0)) == -1 ? null : C_bs.m_a_e96ea081(f_bf_c_6dccaaa5[var2]);
   }

   public static int[] m_a_62c27a66(String var0, int var1, CommandListener var2, int var3, int var4) {
      C_l[] var5;
      String[] var6 = new String[(var5 = C_v.m_a_46ae24e1()).length];
      int[] var7 = new int[var5.length];
      int var8 = 0;

      for (int var9 = 0; var9 < var5.length; var9++) {
         C_l var10000 = var5[var9];
         Object var10 = null;
         int var13;
         if ((var13 = var10000.f_l_a_49) != var4) {
            switch (var3) {
               case 2:
                  if (C_v.m_a_9bf2fbac(var13).length != 0) {
                     break;
                  }
               default:
                  var7[var8] = var13;
                  var6[var8] = var5[var9].m_b_73cf11cb();
                  var8++;
            }
         }
      }

      if (var8 == 0) {
         Alert var12;
         (var12 = new Alert("", C_bs.m_a_e96ea081("_4"), null, AlertType.INFO)).setTimeout(-2);
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var12);
         return null;
      } else {
         String[] var11 = new String[var8];
         int[] var14 = new int[var8];
         System.arraycopy(var7, 0, var14, 0, var8);
         System.arraycopy(var6, 0, var11, 0, var8);
         m_a_1cc7afeb(C_bs.m_a_e96ea081(var0), var11, var2, var1, false);
         return var14;
      }
   }

   public static void m_a_d35e089(C_s var0, String var1, C_f var2, int var3, boolean var4) {
      var0.m_b_13462e(C_bp.m_a_134621(111) << 3);
      if (var2 != null) {
         var0.m_a_3371d4d1(var2, null, var3);
      }

      var1 = var4 ? C_bs.m_a_e96ea081(var1) : var1;
      var0.m_a_68a79fe2(var1, var0.m_d_9b68(), C_bp.m_a_134621(112), var3);
      var0.m_a_485a5b4c(var3);
   }

   public static boolean m_a_cb3ce8a6(C_bd var0) {
      return var0 == null ? false : var0.m_b_9b79();
   }

   private void p_bf_e_9b75() {
      String var1;
      String var2 = (var1 = f_bf_a_fd805d5b.getString()) == null ? "" : var1;
      if (this.f_bf_d_49 >= this.f_bf_a_48a69a2c.size()) {
         if (var2.length() > 0) {
            this.f_bf_a_48a69a2c.addElement(var2);
            return;
         }
      } else {
         this.f_bf_a_48a69a2c.setElementAt(var2, this.f_bf_d_49);
      }
   }

   private void p_bf_f_9b75() {
      try {
         f_bf_a_fd805d5b.setString((String)this.f_bf_a_48a69a2c.elementAt(this.f_bf_d_49));
      } catch (Exception var1) {
         f_bf_a_fd805d5b.setString(null);
      }

      this.p_bf_a_aad3b1ff(this.f_bf_h_523beb0a);
   }

   private void p_bf_a_aad3b1ff(String var1) {
      this.f_bf_h_523beb0a = var1;
      if (this.f_bf_a_48a69a2c.size() > 1) {
         var1 = "[" + (this.f_bf_d_49 + 1) + "/" + (this.f_bf_a_48a69a2c.size() + 1) + "] ";
         if (this.f_bf_h_523beb0a != null) {
            var1 = var1 + this.f_bf_h_523beb0a;
         }
      } else {
         var1 = this.f_bf_h_523beb0a;
      }

      f_bf_a_fd805d5b.setTitle(var1);
   }

   private String p_bf_c_73cf11cb() {
      this.p_bf_e_9b75();
      StringBuffer var1 = new StringBuffer();

      for (int var2 = 0; var2 < this.f_bf_a_48a69a2c.size(); var2++) {
         String var3;
         if ((var3 = (String)this.f_bf_a_48a69a2c.elementAt(var2)) != null) {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   private void p_bf_a_afa300e4(String var1, int var2) {
      try {
         f_bf_a_fd805d5b.insert(var1, var2);
      } catch (Exception var5) {
         int var4 = this.f_bf_d_49;
         this.p_bf_e_9b75();
         this.f_bf_d_49 = Math.max(Math.min(var4, this.f_bf_a_48a69a2c.size()), 0);
         this.p_bf_f_9b75();
         if (this.f_bf_d_49 < this.f_bf_a_48a69a2c.size()) {
            this.f_bf_a_48a69a2c.removeElementAt(this.f_bf_d_49);
            String var3;
            if ((var3 = f_bf_a_fd805d5b.getString()) == null || var3.length() == 0) {
               var3 = "";
               var2 = 0;
            }

            var1 = var3.substring(0, var2) + var1 + var3.substring(var2);
         }

         int var7 = var1.length();

         for (int var6 = 0; var7 > 0; this.f_bf_d_49++) {
            this.f_bf_a_48a69a2c.insertElementAt(var1.substring(var6, var6 + Math.min(var7, f_bf_e_49)), this.f_bf_d_49);
            var6 += f_bf_e_49;
            var7 -= f_bf_e_49;
         }

         this.f_bf_d_49--;
         this.p_bf_f_9b75();
      }
   }

   private void b$552c4e01() {
      this.f_bf_d_49 = 0;
      this.f_bf_a_48a69a2c.removeAllElements();
      f_bf_a_fd805d5b.setString(null);
   }

   public static void m_a_b7252e78(C_aw var0, String var1) {
      if (f_bf_a_fd805d5b == null) {
         f_bf_e_49 = (f_bf_a_fd805d5b = new TextBox(null, null, 2048, 0)).getMaxSize();
         f_bf_a_fd805d5b.addCommand(f_bf_q_1570d10e);
         f_bf_a_fd805d5b.addCommand(f_bf_b_1570d10e);
         f_bf_a_fd805d5b.addCommand(f_bf_m_1570d10e);
         f_bf_a_fd805d5b.addCommand(f_bf_r_1570d10e);
         f_bf_a_fd805d5b.addCommand(f_bf_s_1570d10e);
      }

      f_bf_a_2406f9 = var0;
      f_bf_f_49 = 200001;

      try {
         int var4 = 0;
         if (C_bp.m_a_134632(131)) {
            var4 = 0 | 2097152;
         }

         f_bf_a_fd805d5b.setConstraints(var4);
      } catch (Exception var3) {
      }

      f_bf_a_2408ab.p_bf_a_aad3b1ff(C_bp.m_a_134632(154) ? null : f_bf_a_2406f9.f_aw_a_523beb0a);
      f_bf_a_fd805d5b.removeCommand(f_bf_t_1570d10e);
      f_bf_a_fd805d5b.removeCommand(f_bf_u_1570d10e);
      if (C_bp.m_a_134632(181)) {
         f_bf_a_fd805d5b.addCommand(f_bf_t_1570d10e);
      }

      if (C_bp.m_a_134632(182)) {
         f_bf_a_fd805d5b.addCommand(f_bf_u_1570d10e);
      }

      if (!m_a_9b79()) {
         f_bf_a_fd805d5b.addCommand(f_bf_j_1570d10e);
         f_bf_a_fd805d5b.addCommand(f_bf_k_1570d10e);
      }

      if (var1 != null) {
         int var5 = f_bf_a_fd805d5b.getString().length();
         f_bf_a_2408ab.p_bf_a_afa300e4(var1, var5);
      }

      f_bf_a_fd805d5b.setCommandListener(f_bf_a_2408ab);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bf_a_fd805d5b);
      if (p_bf_b_9b79()) {
         try {
            C_ac.m_a_afa340b5(f_bf_a_2406f9.m_b_73cf11cb(), true);
         } catch (C_aq var2) {
         }
      }

      C_bt.m_a_1385ff(true);
   }

   public static void m_a_122835b8(String var0, C_aw var1) {
      if (var0 != null && var0.length() != 0) {
         for (short var3 = 0; var3 < var0.length(); var3 += 1024) {
            String var4 = var0.substring(var3, Math.min(var3 + 1024, var0.length()));
            C_d var2 = new C_d(C_bp.m_a_47921032(254), var1, 1, C_cf.a$1385f3(), var4);
            C_bu var5;
            long var6 = (var5 = new C_bu(var2)).m_a_9b69();

            try {
               C_ac.m_a_cb38d14b(var5);
            } catch (C_aq var9) {
               C_aq.m_a_481c933f(var9);
               if (var9.f_aq_a_5a) {
                  return;
               }
            }

            C_ap.m_a_dd0db36e(var1, var4, var2.f_bl_a_4a, var1.f_aw_a_523beb0a, var6);
            if (C_bp.m_a_134632(137)) {
               C_aa.m_a_48303787(var1.m_b_73cf11cb(), var0, (byte)1, C_ac.f_ac_a_523beb0a, var2.f_bl_a_4a);
            }

            if (var3 + 1024 < var0.length()) {
               try {
                  Thread.sleep(100L);
               } catch (Exception var8) {
               }
            }
         }

         C_bt.m_a_1385ff(false);
      }
   }

   public static void m_a_9ba4c01b(String var0, Object var1) {
      f_bf_b_5f790d9c = var1;
      Vector var3;
      if ((var3 = C_cf.m_a_dfd94fa3(var0)).size() == 1) {
         try {
            Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest((String)var3.elementAt(0));
         } catch (Exception var2) {
         }
      } else {
         (f_bf_f_12b74 = m_a_53d5d013(C_bs.m_a_e96ea081("V2"), false)).m_a_48817c60(f_bf_i_1570d10e, C_bd.f_bd_e_49);
         f_bf_f_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
         f_bf_f_12b74.m_a_6f63a2af(f_bf_a_2408ab);

         for (int var4 = 0; var4 < var3.size(); var4++) {
            f_bf_f_12b74.m_a_68a79fe2((String)var3.elementAt(var4), f_bf_f_12b74.m_d_9b68(), C_bp.f_bp_g_49, var4).m_a_485a5b4c(var4);
         }

         f_bf_f_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      }
   }

   public static void m_a_a27867fb(int var0, C_aw var1, String var2, String var3) {
      f_bf_g_49 = var0;
      f_bf_b_2406f9 = var1;
      (f_bf_b_fd805d5b = new TextBox(C_bs.m_a_e96ea081(var2), C_bs.m_a_e96ea081(var3), 500, 0)).addCommand(f_bf_q_1570d10e);
      f_bf_b_fd805d5b.addCommand(f_bf_b_1570d10e);
      f_bf_b_fd805d5b.setCommandListener(f_bf_a_2408ab);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bf_b_fd805d5b);
   }

   public static void m_a_cb37742e(C_aw var0) {
      f_bf_c_2406f9 = var0;
      long var1 = var0.m_b_134621(192);
      m_a_9c60de72(f_bf_g_12b74 = new C_s(f_bf_c_2406f9.f_aw_a_523beb0a), false);
      f_bf_g_12b74.c$13462e();
      f_bf_g_12b74.d$1385ff();
      f_bf_g_12b74.m_i_9b75();
      m_a_d35e089(f_bf_g_12b74, "75", C_v.f_v_a_12a1f.m_a_485a59b9(14), 1, true);
      if (var0.m_a_134632(2)) {
         m_a_d35e089(f_bf_g_12b74, "X4", C_v.f_v_a_12a1f.m_a_485a59b9(16), 4, true);
      }

      if ((var0.m_a_134632(2) || var0.m_a_134632(8) || var0.m_a_134632(32)) && C_ac.m_b_9b79()) {
         m_a_d35e089(f_bf_g_12b74, "r0", C_v.f_v_b_12a1f.m_a_485a59b9(5), 14, true);
      }

      if (var1 != -1L) {
         m_a_d35e089(f_bf_g_12b74, "R2", C_v.f_v_b_12a1f.m_a_485a59b9(8), 5, true);
      }

      if (var1 != 0L && var1 != -1L && var1 != 256L) {
         m_a_d35e089(f_bf_g_12b74, "Y4", C_v.f_v_a_12a1f.m_a_485a59b9(var0.m_a_9b68()), 3, true);
      }

      if (var0.m_a_2477b4f().m_b_9b68() != -1) {
         m_a_d35e089(f_bf_g_12b74, "27", var0.m_a_2477b4f().m_a_2477940(), 17, true);
      }

      m_a_d35e089(f_bf_g_12b74, "b3", C_v.f_v_b_12a1f.m_a_485a59b9(9), 12, true);
      m_a_d35e089(f_bf_g_12b74, "F1", C_v.f_v_b_12a1f.m_a_485a59b9(19), 13, true);
      m_a_d35e089(f_bf_g_12b74, "Q1", C_v.f_v_b_12a1f.m_a_485a59b9(10), 11, true);
      m_a_d35e089(f_bf_g_12b74, "B7", C_v.f_v_b_12a1f.m_a_485a59b9(7), 10, true);
      if (C_v.m_a_46ae24e1().length > 1
         && C_bp.m_a_134632(136)
         && !f_bf_c_2406f9.m_a_134632(2)
         && !f_bf_c_2406f9.m_a_134632(8)
         && !f_bf_c_2406f9.m_a_134632(32)) {
         m_a_d35e089(f_bf_g_12b74, "R3", C_v.f_v_b_12a1f.m_a_485a59b9(20), 15, true);
      }

      m_a_d35e089(f_bf_g_12b74, "a5", C_v.f_v_b_12a1f.m_a_485a59b9(31), 16, true);
      m_a_d35e089(f_bf_g_12b74, "O4", C_v.f_v_b_12a1f.m_a_485a59b9(30), 8, true);
      m_a_d35e089(f_bf_g_12b74, "V4", C_v.f_v_b_12a1f.m_a_485a59b9(11), 9, true);
      m_a_d35e089(f_bf_g_12b74, "N4", C_v.f_v_b_12a1f.m_a_485a59b9(6), 7, true);
      f_bf_g_12b74.m_j_9b75();
      f_bf_g_12b74.m_a_48817c60(f_bf_i_1570d10e, C_bd.f_bd_e_49);
      f_bf_g_12b74.m_a_48817c60(f_bf_c_1570d10e, C_bd.f_bd_f_49);
      f_bf_g_12b74.m_a_6f63a2af(f_bf_a_2408ab);
      f_bf_g_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static void m_b_cb37742e(C_aw var0) {
      C_ce var1 = new C_ce(true);
      String[] var2;
      (var2 = new String[10])[0] = var0.m_b_73cf11cb();
      C_z var4 = new C_z(var1, var2);

      try {
         C_ac.m_a_cb38d14b(var4);
      } catch (C_aq var3) {
         C_aq.m_a_481c933f(var3);
      }

      C_cn.m_a_33097a9f("47", var4, false);
   }

   private static void p_bf_c_cb37742e(C_aw var0) {
      C_s var1 = m_a_53d5d013(var0.m_b_73cf11cb(), true);
      String[] var2 = new String[48];
      long var3;
      if ((var3 = var0.m_b_134621(191)) > 0L) {
         var2[30] = C_cf.m_a_87d767d1(false, false, var3);
      }

      long var5;
      if ((var5 = var0.m_b_134621(194)) > 0L) {
         var2[27] = C_cf.m_a_87d767d1(false, false, var5);
      }

      long var7;
      long var9 = (var7 = var0.m_b_134621(195)) + (System.currentTimeMillis() - var0.f_aw_b_4a) / 1000L;
      if (var7 > 0L) {
         var2[28] = C_cf.m_a_2f33e691(var9);
      }

      if (var0.m_a_47921032(3) != null && var0.m_b_134621(192) == -1) {
         var2[36] = var0.m_a_47921032(3);
      }

      int var12;
      if ((var12 = var0.m_b_134621(71)) > 0) {
         var2[29] = C_cf.m_a_2f33e691(var12);
      }

      int var13;
      if ((var13 = var0.m_b_134621(76)) != 0) {
         var2[26] = C_cf.m_a_f2253399((byte)var13) + " " + var0.m_a_47921032(2);
      }

      var2[31] = Integer.toString(var0.m_b_134621(73));
      var2[32] = C_cf.m_c_79834524(var0.m_a_255806f(225));
      var2[33] = C_cf.m_c_79834524(var0.m_a_255806f(226));
      int var14;
      if ((var14 = var0.m_b_134621(74)) != 0) {
         var2[34] = Integer.toString(var14);
      }

      StringBuffer var15 = new StringBuffer();
      int var11;
      if (((var11 = var0.m_b_134621(75)) & 1) != 0) {
         var15.append("\n[ICQ ServerRelay]");
      }

      if ((var11 & 2) != 0) {
         var15.append("\n[UTF8 Messages]");
      }

      if ((var11 & 4) != 0) {
         var15.append("\n[RTF Messages]");
      }

      if ((var11 & 2048) != 0) {
         var15.append("\n[ICQ 6 (HTML msgs)]");
      }

      if ((var11 & 32768) != 0) {
         var15.append("\n[ICQ Lite]");
      }

      if ((var11 & 8) != 0) {
         var15.append("\n[AIM Icon]");
      }

      if ((var11 & 16) != 0) {
         var15.append("\n[AIM Chat]");
      }

      if ((var11 & 32) != 0) {
         var15.append("\n[ICQ xTraz Support]");
      }

      if ((var11 & 64) != 0) {
         var15.append("\n[File Transfer]");
      }

      if ((var11 & 128) != 0) {
         var15.append("\n[AIM Image]");
      }

      if ((var11 & 256) != 0) {
         var15.append("\n[ICQ Devils]");
      }

      if ((var11 & 512) != 0) {
         var15.append("\n[ICQ DirectConnect]");
      }

      if ((var11 & 1024) != 0) {
         var15.append("\n[Typing Notification]");
      }

      var2[35] = var15.toString();
      m_a_2f0d6064(var2, var1);
      var1.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static Object m_a_810c345d() {
      if (C_bd.m_a_46a7a4ee() != null) {
         return C_bd.m_a_46a7a4ee();
      } else {
         Displayable var0;
         return (var0 = Jimm.f_jimm_Jimm_a_4a58c677.getCurrent()) != null && !(var0 instanceof Canvas) ? var0 : null;
      }
   }

   public static void m_a_418a46c8(Object var0, String var1, int var2) {
      if (var1 != null && var0 != null && !(var0 instanceof Canvas)) {
         if (C_bp.m_a_134632(152)) {
            if (var2 != 4 || f_bf_a_240d07 == null || f_bf_a_240d07.m_a_9b79() || f_bf_a_240d07.m_a_9b68() != 5) {
               if (f_bf_a_240d07 != null) {
                  f_bf_a_240d07.cancel();
                  f_bf_a_240d07.m_b_9b75();
               }

               f_bf_a_240d07 = new C_ck(var0, var1, var2 == 4 ? 14 : 0, var2);
               int var3 = var2 == 4 ? 500 : 300;
               Jimm.m_a_94e56161().schedule(f_bf_a_240d07, var3, var3);
            }
         }
      }
   }

   public static synchronized void m_a_a7a36e14(Object var0, C_aw var1, String var2) {
      if (C_bp.m_a_134632(152) && f_bf_a_2406f9 == var1 && f_bf_a_fd805d5b.isShown()) {
         m_a_418a46c8(var0, var2, 5);
      }
   }

   public static synchronized void m_a_5d632d3a(Object var0, C_aw var1, long var2) {
      if (C_bp.m_a_134632(152) && f_bf_a_2406f9 == var1 && f_bf_a_fd805d5b.isShown()) {
         m_a_418a46c8(var0, m_a_2f33e691(var2), 4);
      }
   }

   public static void m_a_eb1e1393(String var0, String var1, String var2) {
      if (!C_cn.m_a_9b79()) {
         boolean var3 = false;
         boolean var4 = C_ap.m_a_aad3b203(var0);
         boolean var6 = var0.equals(C_aw.f_aw_c_523beb0a);
         boolean var5 = f_bf_a_fd805d5b != null;
         switch (C_bp.m_a_134621(84)) {
            case 0:
               return;
            case 1:
               if (var5) {
                  var3 = !var4 && var6 && f_bf_a_fd805d5b.isShown();
               }
               break;
            case 2:
               var3 = !var4 || var4 && !var6;
         }

         if (var3) {
            var0 = "[" + var1 + "]\n" + var2;
            if (Jimm.f_jimm_Jimm_a_4a58c677.getCurrent() instanceof Alert) {
               Alert var9;
               if ((var9 = (Alert)Jimm.f_jimm_Jimm_a_4a58c677.getCurrent()).getImage() != null) {
                  var9.setImage(null);
               }

               var9.setString(var9.getString() + "\n\n" + var0);
            } else {
               Alert var8;
               (var8 = new Alert(C_bs.m_a_e96ea081("K3"), var0, null, null)).setTimeout(-2);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var8);
            }
         }
      }
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
      if (var1 == f_bf_d_12b74) {
         switch (var2) {
            case 42:
               m_c_9b75();
               p_bf_b_1385ff(false);
               f_bf_d_12b74.m_a_48817c60(f_bf_f_1570d10e, C_bd.f_bd_g_49);
         }
      }
   }

   static String m_a_e96ea081(String var0) {
      f_bf_a_523beb0a = var0;
      return var0;
   }

   static String m_b_e96ea081(String var0) {
      f_bf_b_523beb0a = var0;
      return var0;
   }

   static C_bf m_a_46a7a52c() {
      return f_bf_a_2408ab;
   }

   static C_s m_a_2477ad3() {
      return f_bf_c_12b74;
   }

   static String m_a_73cf11cb() {
      return f_bf_a_523beb0a;
   }

   static String m_b_73cf11cb() {
      return f_bf_b_523beb0a;
   }

   static {
      (f_bf_a_d18d4967 = new Hashtable()).put(f_bf_a_1570d10e, new Integer(1));
      f_bf_a_d18d4967.put(f_bf_b_1570d10e, new Integer(2));
      f_bf_a_d18d4967.put(f_bf_n_1570d10e, new Integer(3));
      f_bf_a_d18d4967.put(f_bf_o_1570d10e, new Integer(4));
      f_bf_a_d18d4967.put(f_bf_d_1570d10e, new Integer(5));
      f_bf_a_d18d4967.put(f_bf_c_1570d10e, new Integer(6));
      if (C_bp.m_a_134632(174)) {
         f_bf_q_1570d10e = new Command(C_bs.m_a_e96ea081("e"), 3, 1);
         f_bf_b_1570d10e = new Command(C_bs.m_a_e96ea081("8"), 8, 14);
      }

      f_bf_a_49 = -1;
      f_bf_a_4a = -1L;
      f_bf_g_523beb0a = null;
      f_bf_d_12b74 = null;
      f_bf_a_6dccaaa5 = C_cf.m_a_639c22ad("M1|z0|A0", '|');
      f_bf_a_b4f = new long[]{1L, 32L, 2L, 256L, 4L, 16L, -1L, 0L, 12288L, 16384L, 20480L, 24576L, 8193L, 512L};
      f_bf_b_b4e = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
      f_bf_c_6dccaaa5 = C_cf.m_a_639c22ad("O5|P5|Q5|R5|T5|U5|V5|W5|X5|Y5|Z5|_6|06|S5", '|');
   }
}
