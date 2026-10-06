package recovered;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDletStateChangeException;
import jimm.Jimm;

public final class C_bn implements CommandListener {
   private static C_bn f_bn_a_2409a3;
   private static Command f_bn_a_1570d10e = new Command(C_bs.m_a_e96ea081("e"), 4, 1);
   private static Command f_bn_b_1570d10e = new Command(C_bs.m_a_e96ea081("c2"), 7, 1);
   private static C_s f_bn_a_12b74;
   private static C_s f_bn_b_12b74;
   private static C_s f_bn_c_12b74;
   private static int f_bn_a_49 = 0;
   private static C_bm f_bn_a_240984;
   private static C_s f_bn_d_12b74;
   private static int[] f_bn_a_b4e;
   private static Form f_bn_a_67f46df9;
   private static TextField f_bn_a_a555694c;
   private static TextBox f_bn_a_fd805d5b;
   public static boolean f_bn_a_5a;

   public C_bn() {
      f_bn_a_2409a3 = this;
   }

   public static C_f m_a_2477940() {
      return C_w.m_a_485a59b9(C_bp.m_a_134621(92));
   }

   public static C_f m_b_2477940() {
      byte var0 = 4;
      switch (C_bp.m_a_134621(110)) {
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

      return C_v.f_v_e_12a1f.m_a_485a59b9(var0);
   }

   public static void m_a_9b75() {
      C_bf.m_a_9c60de72(f_bn_d_12b74, false);
      boolean var0 = C_ac.m_b_9b79();
      f_bn_d_12b74.m_i_9b75();
      int var1 = f_bn_d_12b74.m_b_9b68();
      f_bn_d_12b74.m_l_9b75();
      f_bn_d_12b74.m_a_9b75();
      if (var0) {
         C_bf.m_a_d35e089(f_bn_d_12b74, "n3", C_v.f_v_b_12a1f.m_a_485a59b9(0), 6, true);
         C_bf.m_a_d35e089(f_bn_d_12b74, "22", C_v.f_v_b_12a1f.m_a_485a59b9(18), 2, true);
      } else {
         C_bf.m_a_d35e089(f_bn_d_12b74, "B1", C_v.f_v_b_12a1f.m_a_485a59b9(17), 1, true);
      }

      C_s var10000 = f_bn_d_12b74;
      int var2 = C_bf.m_a_1349e2(C_bp.a$134622());
      C_bf.m_a_d35e089(var10000, "d5", C_v.m_a_247797e().m_a_485a59b9(var2), 7, true);
      if (m_a_2477940() != null) {
         C_bf.m_a_d35e089(f_bn_d_12b74, "e5", m_a_2477940(), 8, true);
      }

      if (m_b_2477940() != null) {
         C_bf.m_a_d35e089(f_bn_d_12b74, "A4", m_b_2477940(), 9, true);
      }

      boolean var3 = C_bp.m_a_134632(150);
      C_bf.m_a_d35e089(f_bn_d_12b74, C_bs.m_a_e96ea081(var3 ? "q5" : "r5"), m_a_4949e94a(var3), 14, true);
      if (var0) {
         C_bf.m_a_d35e089(f_bn_d_12b74, "E3", C_v.f_v_b_12a1f.m_a_485a59b9(1), 10, true);
         C_bf.m_a_d35e089(f_bn_d_12b74, "S3", C_v.f_v_b_12a1f.m_a_485a59b9(9), 15, true);
      } else {
         C_bf.m_a_d35e089(f_bn_d_12b74, "H1", C_v.f_v_b_12a1f.m_a_485a59b9(1), 3, true);
      }

      if (C_bp.m_a_134632(172)) {
         C_bf.m_a_d35e089(f_bn_d_12b74, "a0", C_v.f_v_b_12a1f.m_a_485a59b9(33), 16, true);
      }

      C_bf.m_a_d35e089(f_bn_d_12b74, "g4", C_v.f_v_b_12a1f.m_a_485a59b9(3), 4, true);
      C_bf.m_a_d35e089(f_bn_d_12b74, "c6", C_v.f_v_b_12a1f.m_a_485a59b9(24), 5, true);
      C_bf.m_a_d35e089(f_bn_d_12b74, "m0", C_v.f_v_b_12a1f.m_a_485a59b9(4), 11, true);
      C_bf.m_a_d35e089(f_bn_d_12b74, "c2", C_v.f_v_b_12a1f.m_a_485a59b9(28), 17, true);
      f_bn_d_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      if (var0) {
         f_bn_d_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
      } else {
         f_bn_d_12b74.m_a_48817c60(f_bn_b_1570d10e, C_bd.f_bd_f_49);
      }

      f_bn_d_12b74.m_a_13462e(var1);
      f_bn_d_12b74.m_j_9b75();
      f_bn_d_12b74.d$1385ff();
      f_bn_d_12b74.m_a_6f63a2af(f_bn_a_2409a3);
   }

   public static void m_a_ab8148d2(Alert var0) {
      m_a_9b75();
      f_bn_d_12b74.m_a_75ca2789(Jimm.f_jimm_Jimm_a_4a58c677, var0);
   }

   public static void m_b_9b75() {
      if (C_bp.m_a_134632(155)) {
         System.gc();
      }

      m_a_9b75();
      f_bn_d_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      C_bf.m_a_5d527811(f_bn_d_12b74);
   }

   private static void a$78a4d1d0(String var0, String var1, String var2) {
      f_bn_a_67f46df9 = new Form(C_bs.m_a_e96ea081(var0));
      f_bn_a_a555694c = new TextField(C_bs.m_a_e96ea081(var1), var2, 16, 0);
      f_bn_a_67f46df9.append(f_bn_a_a555694c);
      f_bn_a_67f46df9.addCommand(f_bn_a_1570d10e);
      f_bn_a_67f46df9.addCommand(C_bf.f_bf_b_1570d10e);
      f_bn_a_67f46df9.setCommandListener(f_bn_a_2409a3);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bn_a_67f46df9);
   }

   private static void p_bn_a_1385ff(boolean var0) {
      if (!var0 && C_v.m_d_9b68() > 0) {
         C_bf.a$4d6ecd91(C_bs.m_a_e96ea081("J0"), C_bs.m_a_e96ea081("03"), f_bn_a_2409a3, 1);
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
      return var0 ? C_v.f_v_b_12a1f.m_a_485a59b9(16) : C_v.f_v_b_12a1f.m_a_485a59b9(15);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_bn_b_1570d10e) {
         p_bn_a_1385ff(false);
      } else if (C_bf.m_a_cb3ce8a6(f_bn_b_12b74)) {
         if (var1 == C_bf.f_bf_i_1570d10e) {
            C_ac.m_a_132be7((byte)f_bn_b_12b74.m_b_9b68());
            p_bn_c_9b75();
         } else {
            if (var1 == C_bf.f_bf_c_1570d10e) {
               m_b_9b75();
            }
         }
      } else if (C_bf.m_a_cb3ce8a6(f_bn_c_12b74)) {
         if (var1 == C_bf.f_bf_i_1570d10e) {
            this.p_bn_b_13462e(f_bn_c_12b74.m_b_9b68());
         } else {
            if (var1 == C_bf.f_bf_c_1570d10e) {
               m_b_9b75();
            }
         }
      } else if (C_bf.m_a_9b68() == 2) {
         if (var1 == C_bf.f_bf_a_1570d10e) {
            String var12 = C_v.m_a_485a5a73(f_bn_a_b4e[C_bf.m_b_9b68()]).m_b_73cf11cb();
            a$78a4d1d0("W4", "Y2", var12);
         } else {
            m_b_9b75();
         }
      } else if (C_bf.m_a_9b68() == 3) {
         if (var1 == C_bf.f_bf_a_1570d10e) {
            C_cm var11 = new C_cm(C_v.m_a_485a5a73(f_bn_a_b4e[C_bf.m_b_9b68()]), 2);

            try {
               C_ac.m_a_cb38d14b(var11);
               C_cn.m_a_33097a9f("47", var11, false);
            } catch (C_aq var3) {
               C_aq.m_a_481c933f(var3);
            }
         } else {
            m_b_9b75();
         }
      } else if (var1 != C_bf.f_bf_c_1570d10e || !C_bf.m_a_cb3ce8a6(f_bn_a_12b74) && !C_bf.m_a_cb3ce8a6(f_bn_a_240984)) {
         if (var1 == C_bf.f_bf_c_1570d10e && C_bf.m_a_cb3ce8a6(f_bn_d_12b74)) {
            C_v.m_a_9b75();
         } else if (var1 == C_bf.f_bf_i_1570d10e && C_bf.m_a_cb3ce8a6(f_bn_a_240984)) {
            int var15;
            if ((var15 = C_bm.m_b_9b68()) == 0) {
               C_bp.m_a_255f295(92, 37);
               C_bp.m_a_4f708078(32, "");
               C_bp.m_a_4f708078(33, "");
               C_bp.m_c_9b75();
               p_bn_c_9b75();
               if (C_ac.m_b_9b79()) {
                  try {
                     C_al.m_a_9b75();
                  } catch (C_aq var4) {
                     C_aq.m_a_481c933f(var4);
                  }
               }

               C_cn.m_a_48a013c6(C_ac.m_a_2477940());
            } else {
               C_af.m_a_13462e(var15);
            }

            f_bn_a_240984 = null;
         } else if (var1 == f_bn_a_1570d10e && var2 == f_bn_a_67f46df9 && f_bn_a_67f46df9 != null) {
            C_cm var9 = null;
            switch (f_bn_a_49) {
               case 1:
                  C_l var14 = new C_l(f_bn_a_a555694c.getString());
                  var9 = new C_cm(var14, 1);
                  break;
               case 2:
                  C_l var10;
                  C_l var18 = var10 = C_v.m_a_485a5a73(f_bn_a_b4e[C_bf.m_b_9b68()]);
                  String var17 = f_bn_a_a555694c.getString();
                  var18.f_l_a_523beb0a = new String(var17);
                  C_v.m_f_9b75();
                  var9 = new C_cm(var10, 3);
            }

            f_bn_a_49 = 0;

            try {
               C_ac.m_a_cb38d14b(var9);
            } catch (C_aq var5) {
               C_aq.m_a_481c933f(var5);
               if (var5.f_aq_a_5a) {
                  return;
               }
            }

            C_cn.m_a_33097a9f("47", var9, false);
         } else if (var1 == C_bf.f_bf_b_1570d10e && var2 == f_bn_a_67f46df9) {
            m_b_9b75();
            f_bn_a_67f46df9 = null;
         } else if (C_bf.m_a_48817c53(var1, 1) == 3) {
            p_bn_a_1385ff(true);
         } else if (C_bf.m_a_48817c53(var1, 1) == 4) {
            C_v.m_a_9b75();
         } else if (var1 == C_bf.f_bf_i_1570d10e && C_bf.m_a_cb3ce8a6(f_bn_d_12b74)) {
            int var10000 = f_bn_d_12b74.m_b_9b68();
            boolean var8 = false;
            switch (var10000) {
               case 1:
                  C_ac.f_ac_b_49 = C_bp.m_a_134621(91);
                  C_v.m_c_9b75();
                  C_ac.m_a_9b75();
                  return;
               case 2:
                  C_ac.m_b_9b75();
                  Thread.yield();
                  m_b_9b75();
                  return;
               case 3:
                  C_v.m_a_9b75();
                  return;
               case 4:
                  C_bp.m_d_9b75();
                  return;
               case 5:
                  C_a var13 = C_as.f_as_a_12946;
                  C_as.f_as_a_12946.m_a_1385ff(true);
                  var13.f_a_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 6:
                  C_cn.m_c_9b75();
                  return;
               case 7:
                  f_bn_a_5a = false;
                  C_bf.m_a_9c60de72(f_bn_a_12b74 = new C_s(C_bs.m_a_e96ea081("d5")), false);
                  f_bn_a_12b74.c$13462e();
                  f_bn_a_12b74.d$1385ff();
                  f_bn_a_12b74.m_i_9b75();
                  C_bf.m_a_d35e089(f_bn_a_12b74, "W5", C_v.f_v_a_12a1f.m_a_485a59b9(7), 0, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "P5", C_v.f_v_a_12a1f.m_a_485a59b9(1), 32, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "X5", C_v.f_v_a_12a1f.m_a_485a59b9(8), 12288, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "Y5", C_v.f_v_a_12a1f.m_a_485a59b9(9), 16384, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "Z5", C_v.f_v_a_12a1f.m_a_485a59b9(10), 20480, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "_6", C_v.f_v_a_12a1f.m_a_485a59b9(11), 24576, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "06", C_v.f_v_a_12a1f.m_a_485a59b9(12), 8193, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "O5", C_v.f_v_a_12a1f.m_a_485a59b9(0), 1, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "T5", C_v.f_v_a_12a1f.m_a_485a59b9(4), 4, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "U5", C_v.f_v_a_12a1f.m_a_485a59b9(5), 16, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "Q5", C_v.f_v_a_12a1f.m_a_485a59b9(2), 2, true);
                  C_bf.m_a_d35e089(f_bn_a_12b74, "R5", C_v.f_v_a_12a1f.m_a_485a59b9(3), 256, true);
                  if (m_b_2477940() == null) {
                     C_bf.m_a_d35e089(f_bn_a_12b74, "S5", C_v.f_v_a_12a1f.m_a_485a59b9(13), 512, true);
                  }

                  f_bn_a_12b74.m_j_9b75();
                  f_bn_a_12b74.m_a_13462e((int)C_bp.a$134622());
                  f_bn_a_12b74.m_a_6f63a2af(f_bn_a_2409a3);
                  f_bn_a_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  f_bn_a_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  f_bn_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 8:
                  C_bf.m_a_9c60de72(f_bn_a_240984 = new C_bm(1, C_bp.m_a_134621(92) == 37 ? 0 : C_bp.m_a_134621(92) + 1), false);
                  f_bn_a_240984.d$1385ff();
                  f_bn_a_240984.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  f_bn_a_240984.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  f_bn_a_240984.m_a_6f63a2af(f_bn_a_2409a3);
                  f_bn_a_240984.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 9:
                  C_bf.m_a_9c60de72(f_bn_b_12b74 = new C_s(C_bs.m_a_e96ea081("A4")), false);
                  f_bn_b_12b74.c$13462e();
                  f_bn_b_12b74.d$1385ff();
                  f_bn_b_12b74.m_i_9b75();
                  C_bf.m_a_d35e089(f_bn_b_12b74, "B4", C_v.f_v_e_12a1f.m_a_485a59b9(0), 1, true);
                  C_bf.m_a_d35e089(f_bn_b_12b74, "C4", C_v.f_v_e_12a1f.m_a_485a59b9(1), 3, true);
                  C_bf.m_a_d35e089(f_bn_b_12b74, "D4", C_v.f_v_e_12a1f.m_a_485a59b9(2), 4, true);
                  C_bf.m_a_d35e089(f_bn_b_12b74, "E4", C_v.f_v_e_12a1f.m_a_485a59b9(3), 5, true);
                  C_bf.m_a_d35e089(f_bn_b_12b74, "F4", C_v.f_v_e_12a1f.m_a_485a59b9(4), 2, true);
                  f_bn_b_12b74.m_j_9b75();
                  f_bn_b_12b74.m_a_6f63a2af(f_bn_a_2409a3);
                  f_bn_b_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  f_bn_b_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  f_bn_b_12b74.m_a_13462e(C_bp.m_a_134621(110));
                  f_bn_b_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 10:
                  C_bf.m_a_9c60de72(f_bn_c_12b74 = new C_s(C_bs.m_a_e96ea081("E3")), false);
                  f_bn_c_12b74.c$13462e();
                  f_bn_c_12b74.d$1385ff();
                  f_bn_c_12b74.m_i_9b75();
                  C_bf.m_a_d35e089(f_bn_c_12b74, "d", C_v.f_v_b_12a1f.m_a_485a59b9(2), 0, true);
                  C_bf.m_a_d35e089(f_bn_c_12b74, "q0", C_v.f_v_b_12a1f.m_a_485a59b9(29), 1, true);
                  C_bf.m_a_d35e089(f_bn_c_12b74, "W4", C_v.f_v_b_12a1f.m_a_485a59b9(11), 2, true);
                  C_bf.m_a_d35e089(f_bn_c_12b74, "R1", C_v.f_v_b_12a1f.m_a_485a59b9(6), 3, true);
                  f_bn_c_12b74.m_j_9b75();
                  f_bn_c_12b74.m_a_6f63a2af(f_bn_a_2409a3);
                  f_bn_c_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  f_bn_c_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  f_bn_c_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 11:
                  C_bf.m_b_9b75();
                  return;
               case 14:
                  C_v.m_a_138603(false);
                  m_a_9b75();
                  return;
               case 15:
                  C_bf.m_a_e925fa09(C_bp.m_a_47921032(254), "");
                  return;
               case 16:
                  C_bq.m_a_9b75();
                  return;
               case 17:
                  p_bn_a_1385ff(false);
               case 12:
               case 13:
            }
         } else if (var1 == C_bf.f_bf_i_1570d10e && C_bf.m_a_cb3ce8a6(f_bn_a_12b74)) {
            boolean var16 = false;
            int var7 = f_bn_a_12b74.m_b_9b68();
            C_bp.m_a_255f656(192, var7);
            if (var7 != 256 && var7 != 512 && var7 != 0 && var7 != 32) {
               (f_bn_a_fd805d5b = new TextBox(C_bs.m_a_e96ea081("E5"), C_bp.m_a_47921032(p_bn_a_134621(var7)), 255, 0)).addCommand(C_bf.f_bf_i_1570d10e);
               f_bn_a_fd805d5b.setCommandListener(f_bn_a_2409a3);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_bn_a_fd805d5b);
            } else {
               var16 = true;
            }

            C_bp.m_c_9b75();
            f_bn_a_12b74 = null;
            if (var16) {
               p_bn_a_13462e(var7);
            }
         } else if (var2 == f_bn_a_fd805d5b && var1 == C_bf.f_bf_i_1570d10e) {
            int var6;
            C_bp.m_a_4f708078(p_bn_a_134621(var6 = (int)C_bp.a$134622()), f_bn_a_fd805d5b.getString());
            C_bp.m_c_9b75();
            p_bn_a_13462e(var6);
         } else {
            if (C_bf.m_a_48817c53(var1, 4) == 1) {
               this.p_bn_b_13462e(C_bf.m_b_9b68());
            }
         }
      } else {
         m_b_9b75();
         f_bn_a_12b74 = null;
         f_bn_a_240984 = null;
      }
   }

   private static void p_bn_a_13462e(int var0) {
      if (var0 == 256) {
         try {
            C_ac.m_a_132be7((byte)3);
         } catch (Exception var1) {
         }
      }

      if (C_ac.m_b_9b79()) {
         try {
            C_ac.m_a_13462e(var0);
         } catch (C_aq var2) {
            C_aq.m_a_481c933f(var2);
            if (var2.f_aq_a_5a) {
               return;
            }
         }
      }

      C_cn.m_a_13462e(C_bf.m_a_1349e2(C_ac.m_c_9b68()));
      p_bn_c_9b75();
   }

   private static int p_bn_a_134621(int var0) {
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

   private void p_bn_b_13462e(int var1) {
      switch (var1) {
         case 0:
            new C_ce(false).m_a_46a7a969().m_a_13462e(2);
            return;
         case 1:
            f_bn_a_49 = 1;
            a$78a4d1d0("q0", "Y2", null);
            return;
         case 2:
            f_bn_a_49 = 2;
            f_bn_a_b4e = C_bf.m_a_62c27a66("W4", 2, this, 1, -1);
            return;
         case 3:
            f_bn_a_b4e = C_bf.m_a_62c27a66("R1", 3, this, 2, -1);
      }
   }

   private static void p_bn_c_9b75() {
      if (C_ac.m_b_9b79()) {
         C_v.m_a_9b75();
      } else {
         m_b_9b75();
      }
   }

   static {
      (f_bn_d_12b74 = new C_s(C_bs.m_a_e96ea081("H3"))).c$13462e();
   }
}
