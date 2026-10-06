package recovered;

/** 0.6 source correspondence (inferred): jimm.MainMenu. Release class: bp. */

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDletStateChangeException;
import jimm.Jimm;

public final class C_bp implements CommandListener {
   private static C_bp f_bp_a_2409e1;
   private static Command f_bp_a_1570d10e = new Command(C_bt.m_a_e96ea081("b"), 4, 1);
   private static Command f_bp_b_1570d10e = new Command(C_bt.m_a_e96ea081("92"), 7, 1);
   private static C_t f_bp_a_12b93;
   private static C_t f_bp_b_12b93;
   private static C_t f_bp_c_12b93;
   private static int f_bp_a_49 = 0;
   private static C_bo f_bp_a_2409c2;
   private static C_t f_bp_d_12b93;
   private static int[] f_bp_a_b4e;
   private static Form f_bp_a_67f46df9;
   private static TextField f_bp_a_a555694c;
   private static TextBox f_bp_a_fd805d5b;
   public static boolean f_bp_a_5a;

   public C_bp() {
      f_bp_a_2409e1 = this;
   }

   public static C_f m_a_2477940() {
      return C_x.m_a_485a59b9(C_bq.m_a_134621(92));
   }

   public static C_f m_b_2477940() {
      byte var0 = 4;
      switch (C_bq.m_a_134621(110)) {
         case 1:
            var0 = 0;
            break;
         case 2:
            var0 = 4;
            break;
         case 3:
            var0 = 1;
            break;
         case 4:
            var0 = 2;
            break;
         case 5:
            var0 = 3;
      }

      return C_w.f_w_e_12a00.m_a_485a59b9(var0);
   }

   public static void m_a_9b75() {
      C_bi.m_a_9c7d0d74(f_bp_d_12b93, false);
      boolean var0 = C_ac.m_b_9b79();
      f_bp_d_12b93.m_j_9b75();
      int var1 = f_bp_d_12b93.m_b_9b68();
      f_bp_d_12b93.m_m_9b75();
      f_bp_d_12b93.m_a_9b75();
      if (var0) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "k3", C_w.f_w_b_12a00.m_a_485a59b9(0), 6, true);
         C_bi.m_a_4686f14a(f_bp_d_12b93, "_2", C_w.f_w_b_12a00.m_a_485a59b9(18), 2, true);
      } else {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "y1", C_w.f_w_b_12a00.m_a_485a59b9(17), 1, true);
      }

      C_t var10000 = f_bp_d_12b93;
      int var2 = C_bi.m_a_1349e2(C_bq.a$134622());
      C_bi.m_a_4686f14a(var10000, "a5", C_w.m_a_247795f().m_a_485a59b9(var2), 7, true);
      if (m_a_2477940() != null) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "b5", m_a_2477940(), 8, true);
      }

      if (m_b_2477940() != null) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "x4", m_b_2477940(), 9, true);
      }

      boolean var3 = C_bq.m_a_134632(150);
      C_bi.m_a_4686f14a(f_bp_d_12b93, C_bt.m_a_e96ea081(var3 ? "n5" : "o5"), m_a_4949e94a(var3), 14, true);
      if (var0) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "B3", C_w.f_w_b_12a00.m_a_485a59b9(1), 10, true);
         C_bi.m_a_4686f14a(f_bp_d_12b93, "P3", C_w.f_w_b_12a00.m_a_485a59b9(9), 15, true);
      } else {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "E1", C_w.f_w_b_12a00.m_a_485a59b9(1), 3, true);
      }

      if (C_bq.m_a_134632(172)) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "70", C_w.f_w_b_12a00.m_a_485a59b9(33), 16, true);
      }

      if (!Jimm.f_jimm_Jimm_b_5a && !Jimm.f_jimm_Jimm_c_5a) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "21", C_x.f_x_a_12a00.m_a_485a59b9(14), 13, true);
      }

      C_bi.m_a_4686f14a(f_bp_d_12b93, "d4", C_w.f_w_b_12a00.m_a_485a59b9(3), 4, true);
      C_bi.m_a_4686f14a(f_bp_d_12b93, "96", C_w.f_w_b_12a00.m_a_485a59b9(24), 5, true);
      C_bi.m_a_4686f14a(f_bp_d_12b93, "j0", C_w.f_w_b_12a00.m_a_485a59b9(4), 11, true);
      if (Jimm.f_jimm_Jimm_b_5a) {
         C_bi.m_a_4686f14a(f_bp_d_12b93, "K3", C_w.f_w_b_12a00.m_a_485a59b9(27), 12, true);
      }

      C_bi.m_a_4686f14a(f_bp_d_12b93, "92", C_w.f_w_b_12a00.m_a_485a59b9(28), 17, true);
      f_bp_d_12b93.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
      if (var0) {
         f_bp_d_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
      } else {
         f_bp_d_12b93.m_a_48817c60(f_bp_b_1570d10e, C_bf.f_bf_f_49);
      }

      f_bp_d_12b93.m_a_13462e(var1);
      f_bp_d_12b93.m_k_9b75();
      f_bp_d_12b93.d$1385ff();
      f_bp_d_12b93.m_a_6f63a2af(f_bp_a_2409e1);
   }

   public static void m_a_ab8148d2(Alert var0) {
      m_a_9b75();
      f_bp_d_12b93.m_a_75ca2789(Jimm.f_jimm_Jimm_a_4a58c677, var0);
   }

   public static void m_b_9b75() {
      if (C_bq.m_a_134632(155)) {
         System.gc();
      }

      m_a_9b75();
      f_bp_d_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      C_bi.m_a_5d527811(f_bp_d_12b93);
   }

   private static void a$78a4d1d0(String var0, String var1, String var2) {
      f_bp_a_67f46df9 = new Form(C_bt.m_a_e96ea081(var0));
      f_bp_a_a555694c = new TextField(C_bt.m_a_e96ea081(var1), var2, 16, 0);
      f_bp_a_67f46df9.append(f_bp_a_a555694c);
      f_bp_a_67f46df9.addCommand(f_bp_a_1570d10e);
      f_bp_a_67f46df9.addCommand(C_bi.f_bi_b_1570d10e);
      f_bp_a_67f46df9.setCommandListener(f_bp_a_2409e1);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bp_a_67f46df9);
   }

   private static void p_bp_a_1385ff(boolean var0) {
      if (!var0 && C_w.m_d_9b68() > 0) {
         C_bi.a$4d6ecd91(C_bt.m_a_e96ea081("G0"), C_bt.m_a_e96ea081("Y2"), f_bp_a_2409e1, 1);
      } else {
         C_ac.m_b_9b75();

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
         }

         try {
            Jimm.f_jimm_Jimm_a_3fbeb738.destroyApp(true);
         } catch (MIDletStateChangeException var1) {
         }
      }
   }

   public static C_f m_a_4949e94a(boolean var0) {
      return var0 ? C_w.f_w_b_12a00.m_a_485a59b9(16) : C_w.f_w_b_12a00.m_a_485a59b9(15);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_bp_b_1570d10e) {
         p_bp_a_1385ff(false) ;
      } else if (C_bi.m_a_cb3dd164(f_bp_b_12b93)) {
         if (var1 == C_bi.f_bi_i_1570d10e) {
            C_ac.m_a_132be7((byte)f_bp_b_12b93.m_b_9b68());
            p_bp_f_9b75() ;
         } else {
            if (var1 == C_bi.f_bi_c_1570d10e) {
               m_b_9b75();
            }
         }
      } else if (C_bi.m_a_cb3dd164(f_bp_c_12b93)) {
         if (var1 == C_bi.f_bi_i_1570d10e) {
            this.p_bp_b_13462e(f_bp_c_12b93.m_b_9b68());
         } else {
            if (var1 == C_bi.f_bi_c_1570d10e) {
               m_b_9b75();
            }
         }
      } else if (C_bi.m_a_9b68() == 2) {
         if (var1 == C_bi.f_bi_a_1570d10e) {
            String var12 = C_w.m_a_485a5a92(f_bp_a_b4e[C_bi.m_b_9b68()]).m_b_73cf11cb();
            a$78a4d1d0("T4", "V2", var12);
         } else {
            m_b_9b75();
         }
      } else if (C_bi.m_a_9b68() == 3) {
         if (var1 == C_bi.f_bi_a_1570d10e) {
            C_cn var11 = new C_cn(C_w.m_a_485a5a92(f_bp_a_b4e[C_bi.m_b_9b68()]), 2);

            try {
               C_ac.m_a_cb3b8b85(var11);
               C_cp.m_a_335e07a5("17", var11, false);
            } catch (C_ar var3) {
               C_ar.m_a_aef55300(var3);
            }
         } else {
            m_b_9b75();
         }
      } else if (var1 != C_bi.f_bi_c_1570d10e || !C_bi.m_a_cb3dd164(f_bp_a_12b93) && !C_bi.m_a_cb3dd164(f_bp_a_2409c2)) {
         if (var1 == C_bi.f_bi_c_1570d10e && C_bi.m_a_cb3dd164(f_bp_d_12b93)) {
            C_w.m_a_9b75();
         } else if (var1 == C_bi.f_bi_i_1570d10e && C_bi.m_a_cb3dd164(f_bp_a_2409c2)) {
            int var15;
            if ((var15 = C_bo.m_b_9b68()) == 0) {
               C_bq.m_a_255f295(92, 37);
               C_bq.m_a_4f708078(32, "");
               C_bq.m_a_4f708078(33, "");
               C_bq.m_c_9b75();
               p_bp_f_9b75() ;
               if (C_ac.m_b_9b79()) {
                  try {
                     C_am.m_a_9b75();
                  } catch (C_ar var4) {
                     C_ar.m_a_aef55300(var4);
                  }
               }

               C_cp.m_a_48a013c6(C_ac.m_a_2477940());
            } else {
               C_af.m_a_13462e(var15);
            }

            f_bp_a_2409c2 = null;
         } else if (var1 == f_bp_a_1570d10e && var2 == f_bp_a_67f46df9 && f_bp_a_67f46df9 != null) {
            C_cn var9 = null;
            switch (f_bp_a_49) {
               case 1:
                  C_m var14 = new C_m(f_bp_a_a555694c.getString());
                  var9 = new C_cn(var14, 1);
                  break;
               case 2:
                  C_m var10;
                  C_m var18 = var10 = C_w.m_a_485a5a92(f_bp_a_b4e[C_bi.m_b_9b68()]);
                  String var17 = f_bp_a_a555694c.getString();
                  var18.f_m_a_523beb0a = new String(var17);
                  C_w.m_f_9b75();
                  var9 = new C_cn(var10, 3);
            }

            f_bp_a_49 = 0;

            try {
               C_ac.m_a_cb3b8b85(var9);
            } catch (C_ar var5) {
               C_ar.m_a_aef55300(var5);
               if (var5.f_ar_a_5a) {
                  return;
               }
            }

            C_cp.m_a_335e07a5("17", var9, false);
         } else if (var1 == C_bi.f_bi_b_1570d10e && var2 == f_bp_a_67f46df9) {
            m_b_9b75();
            f_bp_a_67f46df9 = null;
         } else if (C_bi.m_a_48817c53(var1, 1) == 3) {
            p_bp_a_1385ff(true) ;
         } else if (C_bi.m_a_48817c53(var1, 1) == 4) {
            C_w.m_a_9b75();
         } else if (var1 == C_bi.f_bi_i_1570d10e && C_bi.m_a_cb3dd164(f_bp_d_12b93)) {
            int var10000 = f_bp_d_12b93.m_b_9b68();
            boolean var8 = false;
            switch (var10000) {
               case 1:
                  C_ac.f_ac_b_49 = C_bq.m_a_134621(91);
                  C_w.m_c_9b75();
                  C_ac.m_a_9b75();
                  return;
               case 2:
                  C_ac.m_b_9b75();
                  Thread.yield();
                  m_b_9b75();
                  return;
               case 3:
                  C_w.m_a_9b75();
                  return;
               case 4:
                  C_bq.m_d_9b75();
                  return;
               case 5:
                  C_a var13 = C_at.f_at_a_12946;
                  C_at.f_at_a_12946.m_a_1385ff(true);
                  var13.f_a_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 6:
                  C_cp.m_c_9b75();
                  return;
               case 7:
                  m_c_9b75();
                  return;
               case 8:
                  m_d_9b75();
                  return;
               case 9:
                  m_e_9b75();
                  return;
               case 10:
                  C_bi.m_a_9c7d0d74(f_bp_c_12b93 = new C_t(C_bt.m_a_e96ea081("B3")), false);
                  f_bp_c_12b93.c$13462e();
                  f_bp_c_12b93.d$1385ff();
                  f_bp_c_12b93.m_j_9b75();
                  C_bi.m_a_4686f14a(f_bp_c_12b93, "a", C_w.f_w_b_12a00.m_a_485a59b9(2), 0, true);
                  C_bi.m_a_4686f14a(f_bp_c_12b93, "n0", C_w.f_w_b_12a00.m_a_485a59b9(29), 1, true);
                  C_bi.m_a_4686f14a(f_bp_c_12b93, "T4", C_w.f_w_b_12a00.m_a_485a59b9(11), 2, true);
                  C_bi.m_a_4686f14a(f_bp_c_12b93, "O1", C_w.f_w_b_12a00.m_a_485a59b9(6), 3, true);
                  f_bp_c_12b93.m_k_9b75();
                  f_bp_c_12b93.m_a_6f63a2af(f_bp_a_2409e1);
                  f_bp_c_12b93.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
                  f_bp_c_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
                  f_bp_c_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 11:
                  C_bi.m_b_9b75();
                  return;
               case 12:
                  Jimm.m_a_1385ff(true);
                  return;
               case 13:
                  C_p.m_a_9b75();
                  return;
               case 14:
                  C_w.m_a_138603(false);
                  m_a_9b75();
                  return;
               case 15:
                  C_bi.m_a_e925fa09(C_bq.m_a_47921032(254), "");
                  return;
               case 16:
                  C_br.m_a_9b75();
                  return;
               case 17:
                  p_bp_a_1385ff(false) ;
            }
         } else if (var1 == C_bi.f_bi_i_1570d10e && C_bi.m_a_cb3dd164(f_bp_a_12b93)) {
            boolean var16 = false;
            int var7 = f_bp_a_12b93.m_b_9b68();
            C_bq.m_a_255f656(192, var7);
            if (var7 != 256 && var7 != 512 && var7 != 0 && var7 != 32) {
               (f_bp_a_fd805d5b = new TextBox(C_bt.m_a_e96ea081("B5"), C_bq.m_a_47921032(p_bp_a_134621(var7)), 255, 0)).addCommand(C_bi.f_bi_i_1570d10e);
               f_bp_a_fd805d5b.setCommandListener(f_bp_a_2409e1);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bp_a_fd805d5b);
            } else {
               var16 = true;
            }

            C_bq.m_c_9b75();
            f_bp_a_12b93 = null;
            if (var16) {
               p_bp_a_13462e(var7) ;
            }
         } else if (var2 == f_bp_a_fd805d5b && var1 == C_bi.f_bi_i_1570d10e) {
            int var6;
            C_bq.m_a_4f708078(p_bp_a_134621(var6 = (int)C_bq.a$134622()), f_bp_a_fd805d5b.getString());
            C_bq.m_c_9b75();
            p_bp_a_13462e(var6) ;
         } else {
            if (C_bi.m_a_48817c53(var1, 4) == 1) {
               this.p_bp_b_13462e(C_bi.m_b_9b68());
            }
         }
      } else {
         m_b_9b75();
         f_bp_a_12b93 = null;
         f_bp_a_2409c2 = null;
      }
   }

   public static void m_c_9b75() {
      f_bp_a_5a = false;
      C_bi.m_a_9c7d0d74(f_bp_a_12b93 = new C_t(C_bt.m_a_e96ea081("a5")), false);
      f_bp_a_12b93.c$13462e();
      f_bp_a_12b93.d$1385ff();
      f_bp_a_12b93.m_j_9b75();
      C_bi.m_a_4686f14a(f_bp_a_12b93, "T5", C_w.f_w_a_12a00.m_a_485a59b9(7), 0, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "M5", C_w.f_w_a_12a00.m_a_485a59b9(1), 32, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "U5", C_w.f_w_a_12a00.m_a_485a59b9(8), 12288, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "V5", C_w.f_w_a_12a00.m_a_485a59b9(9), 16384, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "W5", C_w.f_w_a_12a00.m_a_485a59b9(10), 20480, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "X5", C_w.f_w_a_12a00.m_a_485a59b9(11), 24576, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "Y5", C_w.f_w_a_12a00.m_a_485a59b9(12), 8193, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "L5", C_w.f_w_a_12a00.m_a_485a59b9(0), 1, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "Q5", C_w.f_w_a_12a00.m_a_485a59b9(4), 4, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "R5", C_w.f_w_a_12a00.m_a_485a59b9(5), 16, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "N5", C_w.f_w_a_12a00.m_a_485a59b9(2), 2, true);
      C_bi.m_a_4686f14a(f_bp_a_12b93, "O5", C_w.f_w_a_12a00.m_a_485a59b9(3), 256, true);
      if (m_b_2477940() == null) {
         C_bi.m_a_4686f14a(f_bp_a_12b93, "P5", C_w.f_w_a_12a00.m_a_485a59b9(13), 512, true);
      }

      f_bp_a_12b93.m_k_9b75();
      f_bp_a_12b93.m_a_13462e((int)C_bq.a$134622());
      f_bp_a_12b93.m_a_6f63a2af(f_bp_a_2409e1);
      f_bp_a_12b93.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
      f_bp_a_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
      f_bp_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static void m_d_9b75() {
      C_bi.m_a_9c7d0d74(f_bp_a_2409c2 = new C_bo(1, C_bq.m_a_134621(92) == 37 ? 0 : C_bq.m_a_134621(92) + 1), false);
      f_bp_a_2409c2.d$1385ff();
      f_bp_a_2409c2.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
      f_bp_a_2409c2.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
      f_bp_a_2409c2.m_a_6f63a2af(f_bp_a_2409e1);
      f_bp_a_2409c2.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static void m_e_9b75() {
      C_bi.m_a_9c7d0d74(f_bp_b_12b93 = new C_t(C_bt.m_a_e96ea081("x4")), false);
      f_bp_b_12b93.c$13462e();
      f_bp_b_12b93.d$1385ff();
      f_bp_b_12b93.m_j_9b75();
      C_bi.m_a_4686f14a(f_bp_b_12b93, "y4", C_w.f_w_e_12a00.m_a_485a59b9(0), 1, true);
      C_bi.m_a_4686f14a(f_bp_b_12b93, "z4", C_w.f_w_e_12a00.m_a_485a59b9(1), 3, true);
      C_bi.m_a_4686f14a(f_bp_b_12b93, "A4", C_w.f_w_e_12a00.m_a_485a59b9(2), 4, true);
      C_bi.m_a_4686f14a(f_bp_b_12b93, "B4", C_w.f_w_e_12a00.m_a_485a59b9(3), 5, true);
      C_bi.m_a_4686f14a(f_bp_b_12b93, "C4", C_w.f_w_e_12a00.m_a_485a59b9(4), 2, true);
      f_bp_b_12b93.m_k_9b75();
      f_bp_b_12b93.m_a_6f63a2af(f_bp_a_2409e1);
      f_bp_b_12b93.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
      f_bp_b_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
      f_bp_b_12b93.m_a_13462e(C_bq.m_a_134621(110));
      f_bp_b_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private static void p_bp_a_13462e(int var0) {
      if (var0 == 256) {
         try {
            C_ac.m_a_132be7((byte)3);
         } catch (Exception var1) {
         }
      }

      if (C_ac.m_b_9b79()) {
         try {
            C_ac.m_a_13462e(var0);
         } catch (C_ar var2) {
            C_ar.m_a_aef55300(var2);
            if (var2.f_ar_a_5a) {
               return;
            }
         }
      }

      C_cp.m_a_13462e(C_bi.m_a_1349e2(C_ac.m_c_9b68()));
      p_bp_f_9b75() ;
   }

   private static int p_bp_a_134621(int var0) {
      byte var1 = 7;
      switch (var0) {
         case 1:
            var1 = 7;
            break;
         case 2:
            var1 = 19;
            break;
         case 4:
            var1 = 20;
            break;
         case 16:
            var1 = 21;
            break;
         case 8193:
            var1 = 26;
            break;
         case 12288:
            var1 = 22;
            break;
         case 16384:
            var1 = 23;
            break;
         case 20480:
            var1 = 24;
            break;
         case 24576:
            var1 = 25;
      }

      return var1;
   }

   private void p_bp_b_13462e(int var1) {
      switch (var1) {
         case 0:
            new C_cg(false).m_a_46a7a988().m_a_13462e(2);
            return;
         case 1:
            f_bp_a_49 = 1;
            a$78a4d1d0("n0", "V2", null);
            return;
         case 2:
            f_bp_a_49 = 2;
            f_bp_a_b4e = C_bi.m_a_62c27a66("T4", 2, this, 1, -1);
            return;
         case 3:
            f_bp_a_b4e = C_bi.m_a_62c27a66("O1", 3, this, 2, -1);
      }
   }

   private static void p_bp_f_9b75() {
      if (C_ac.m_b_9b79()) {
         C_w.m_a_9b75();
      } else {
         m_b_9b75();
      }
   }

   static {
      (f_bp_d_12b93 = new C_t(C_bt.m_a_e96ea081("E3"))).c$13462e();
   }
}
