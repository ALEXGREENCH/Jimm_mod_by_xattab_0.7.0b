package recovered;

import com.siemens.mp.game.Light;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

final class C_bf implements CommandListener, ItemStateListener {
   private boolean f_bf_a_5a;
   private boolean f_bf_b_5a;
   private boolean f_bf_c_5a;
   private int f_bf_b_49;
   private String f_bf_a_523beb0a;
   private Command f_bf_a_1570d10e;
   private Command f_bf_b_1570d10e;
   private Command f_bf_c_1570d10e;
   private C_t f_bf_a_12b93;
   private Form f_bf_a_67f46df9;
   private TextField[] f_bf_a_a1fd9827;
   private TextField[] f_bf_b_a1fd9827;
   private TextField f_bf_a_a555694c;
   private TextField f_bf_b_a555694c;
   private ChoiceGroup f_bf_a_aa0ede5b;
   private TextField f_bf_c_a555694c;
   private ChoiceGroup f_bf_b_aa0ede5b;
   private ChoiceGroup f_bf_c_aa0ede5b;
   private TextField f_bf_d_a555694c;
   private ChoiceGroup f_bf_d_aa0ede5b;
   private ChoiceGroup f_bf_e_aa0ede5b;
   private ChoiceGroup f_bf_f_aa0ede5b;
   private ChoiceGroup f_bf_g_aa0ede5b;
   private ChoiceGroup f_bf_h_aa0ede5b;
   private ChoiceGroup f_bf_i_aa0ede5b;
   private ChoiceGroup f_bf_j_aa0ede5b;
   private ChoiceGroup f_bf_k_aa0ede5b;
   private ChoiceGroup f_bf_l_aa0ede5b;
   private TextField f_bf_e_a555694c;
   private ChoiceGroup f_bf_m_aa0ede5b;
   private ChoiceGroup f_bf_n_aa0ede5b;
   private ChoiceGroup f_bf_o_aa0ede5b;
   private ChoiceGroup f_bf_p_aa0ede5b;
   private ChoiceGroup f_bf_q_aa0ede5b;
   private ChoiceGroup f_bf_r_aa0ede5b;
   private Gauge f_bf_a_978a2ca0;
   private Gauge f_bf_b_978a2ca0;
   private Gauge f_bf_c_978a2ca0;
   private ChoiceGroup f_bf_s_aa0ede5b;
   private ChoiceGroup f_bf_t_aa0ede5b;
   private ChoiceGroup f_bf_u_aa0ede5b;
   private ChoiceGroup f_bf_v_aa0ede5b;
   private Gauge f_bf_d_978a2ca0;
   private TextField f_bf_f_a555694c;
   private TextField f_bf_g_a555694c;
   private TextField f_bf_h_a555694c;
   private TextField f_bf_i_a555694c;
   private ChoiceGroup f_bf_w_aa0ede5b;
   private ChoiceGroup f_bf_x_aa0ede5b;
   private ChoiceGroup f_bf_y_aa0ede5b;
   private ChoiceGroup f_bf_z_aa0ede5b;
   private TextField f_bf_j_a555694c;
   private ChoiceGroup f_bf_A_aa0ede5b;
   private TextField f_bf_k_a555694c;
   private TextField f_bf_l_a555694c;
   private TextField f_bf_m_a555694c;
   private TextField f_bf_n_a555694c;
   private ChoiceGroup f_bf_B_aa0ede5b;
   private TextField f_bf_o_a555694c;
   private TextField f_bf_p_a555694c;
   private TextField f_bf_q_a555694c;
   private TextField f_bf_r_a555694c;
   private ChoiceGroup f_bf_C_aa0ede5b;
   private TextField f_bf_s_a555694c;
   private TextField f_bf_t_a555694c;
   private TextField f_bf_u_a555694c;
   private TextField f_bf_v_a555694c;
   private ChoiceGroup f_bf_D_aa0ede5b;
   private ChoiceGroup f_bf_E_aa0ede5b;
   private TextField f_bf_w_a555694c;
   private TextField f_bf_x_a555694c;
   private TextField f_bf_y_a555694c;
   private TextField f_bf_z_a555694c;
   private TextField f_bf_A_a555694c;
   private C_t f_bf_b_12b93;
   private C_t f_bf_c_12b93;
   private C_t f_bf_d_12b93;
   private C_t f_bf_e_12b93;
   private final String[] f_bf_a_6dccaaa5 = C_ce.m_a_639c22ad("j2|63|25|w7|k2|b4|C3|g3|I3|L1|O2|m5|o3|50|O1|X6", '|');
   private final int[] f_bf_a_b4e = new int[]{0, 2, 3, 8, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16};
   private Command f_bf_d_1570d10e = new Command(C_bs.m_a_e96ea081("k0"), 8, 3);
   private Command f_bf_e_1570d10e = new Command(C_bs.a$7a1ba7c4("W1"), 8, 3);
   private int f_bf_c_49;
   private Vector f_bf_a_48a69a2c = new Vector();
   private Vector f_bf_b_48a69a2c = new Vector();
   private int f_bf_d_49 = C_bp.f_bp_b_b4e.length / 2;
   private static C_bn f_bf_a_2409a3;

   public C_bf() {
      this.f_bf_b_12b93 = new C_t(C_bs.m_a_e96ea081("82"));
      C_bg.m_a_9c6ef5f3(this.f_bf_b_12b93, false);
      this.f_bf_b_12b93.m_a_6f63a2af(this);
      this.f_bf_c_12b93 = new C_t(C_bs.m_a_e96ea081("92"));
      C_bg.m_a_9c6ef5f3(this.f_bf_c_12b93, false);
      this.f_bf_c_12b93.m_a_6f63a2af(this);
      this.f_bf_a_1570d10e = new Command(C_bs.m_a_e96ea081("5"), 1, 1);
      this.f_bf_b_1570d10e = new Command(C_bs.m_a_e96ea081("e0"), 8, 2);
      this.f_bf_c_1570d10e = new Command(C_bs.m_a_e96ea081("f0"), 8, 2);
      this.f_bf_a_12b93 = new C_t(C_bs.m_a_e96ea081("b4"));
      C_bg.m_a_9c6ef5f3(this.f_bf_a_12b93, false);
      this.f_bf_d_12b93 = new C_t(C_bs.m_a_e96ea081("j1"));
      this.f_bf_e_12b93 = new C_t(C_bs.m_a_e96ea081("i1"));
      this.f_bf_a_12b93.m_a_48817c60(C_bg.f_bg_c_1570d10e, C_be.f_be_f_49);
      this.f_bf_a_12b93.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
      this.f_bf_a_12b93.m_a_6f63a2af(this);
      this.f_bf_a_67f46df9 = new Form(C_bs.m_a_e96ea081("b4"));
      this.f_bf_a_67f46df9.addCommand(this.f_bf_a_1570d10e);
      this.f_bf_a_67f46df9.addCommand(C_bg.f_bg_c_1570d10e);
      this.f_bf_a_67f46df9.setCommandListener(this);
      this.f_bf_a_67f46df9.setItemStateListener(this);
   }

   public final void m_a_9b75() {
      this.f_bf_a_12b93.m_a_13462e(9);
      this.commandAction(C_bg.f_bg_i_1570d10e, null);
   }

   private String p_bf_a_d9e4d430(String var1, int var2, boolean var3) {
      String var4 = var3 ? C_bs.m_a_e96ea081(var1) : var1;
      var2 = var3 ? C_bp.m_a_134621(var2) : var2;

      for (int var6 = 0; var6 < this.f_bf_a_6dccaaa5.length; var6++) {
         if (this.f_bf_a_b4e[var6] == var2) {
            return var4 + ": " + C_bs.m_a_e96ea081(this.f_bf_a_6dccaaa5[var6]);
         }
      }

      return var4 + ": <???>";
   }

   private void p_bf_d_9b75() {
      int var1 = this.f_bf_b_12b93.m_b_9b68();
      boolean var2 = C_bp.m_a_134632(175);
      this.f_bf_b_12b93.m_a_9b75();
      if (!var2) {
         boolean var5 = false;
         String var4 = "a2";
         Object var3 = null;
         C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430(var4, 77, true), null, 77, true);
      }

      char var15 = '\u0000';
      String var8 = "c2";
      String var6 = null;
      C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430(var8, 79, true), null, 79, true);
      var8 = "e2";
      C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430("e2", 80, true), null, 80, true);
      var8 = "h2";
      C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430("h2", 78, true), null, 78, true);
      var8 = "g2";
      C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430("g2", 82, true), null, 82, true);
      var8 = "i2";
      C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430("i2", 81, true), null, 81, true);
      if (var2) {
         if ((var6 = C_bp.m_a_47921032(40)).length() < C_bp.f_bp_a_49) {
            var6 = "";

            for (int var13 = 0; var13 < C_bp.f_bp_a_49; var13++) {
               var6 = var6 + '\u0000';
            }

            C_bp.m_a_4f708078(40, var6);
         }

         for (int var14 = 0; var14 < C_bp.f_bp_a_49; var14++) {
            var15 = var6.charAt(var14);
            C_bg.m_a_4686f14a(this.f_bf_b_12b93, this.p_bf_a_d9e4d430("0+" + C_bp.f_bp_a_6dccaaa5[var14], var15, false), null, var14 + 1024, true);
         }
      }

      this.f_bf_b_12b93.m_a_13462e(var1);
      this.f_bf_b_12b93.d$1385ff();
      this.f_bf_b_12b93.m_a_48817c60(C_bg.f_bg_h_1570d10e, C_be.f_be_f_49);
      this.f_bf_b_12b93.m_a_48817c60(this.f_bf_a_1570d10e, C_be.f_be_h_49);
      if (var2) {
         this.f_bf_b_12b93.m_a_8eb9d703(this.f_bf_b_1570d10e);
         this.f_bf_b_12b93.m_a_48817c60(this.f_bf_c_1570d10e, C_be.f_be_h_49);
      } else {
         this.f_bf_b_12b93.m_a_8eb9d703(this.f_bf_c_1570d10e);
         this.f_bf_b_12b93.m_a_48817c60(this.f_bf_b_1570d10e, C_be.f_be_h_49);
      }

      this.f_bf_b_12b93.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
      this.f_bf_b_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void m_b_9b75() {
      int var1 = this.f_bf_d_12b93.m_b_9b68();
      this.f_bf_d_12b93.m_a_9b75();
      C_bg.m_a_9c6ef5f3(this.f_bf_d_12b93, false);
      this.f_bf_d_12b93.c$13462e();
      this.f_bf_d_12b93.d$1385ff();
      C_f var2 = C_w.f_w_b_12a1f.m_a_485a59b9(32);
      this.f_bf_d_12b93.m_i_9b75();
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "o1", var2, 106, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "k1", var2, 102, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "s1", var2, 115, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "r1", var2, 114, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "l1", var2, 103, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "m1", var2, 104, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "n1", var2, 105, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "p1", var2, 108, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "q1", var2, 113, true);
      C_bg.m_a_4686f14a(this.f_bf_d_12b93, "T0", var2, 107, true);
      this.f_bf_d_12b93.m_j_9b75();
      this.f_bf_d_12b93.m_a_13462e(var1);
      this.f_bf_d_12b93.m_a_48817c60(this.f_bf_a_1570d10e, C_be.f_be_f_49);
      this.f_bf_d_12b93.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
      this.f_bf_d_12b93.m_a_6f63a2af(this);
      this.f_bf_d_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private void p_bf_e_9b75() {
      this.f_bf_a_48a69a2c.removeAllElements();
      this.f_bf_b_48a69a2c.removeAllElements();

      for (int var1 = 0; var1 < this.f_bf_d_49; var1++) {
         int var2 = var1 << 1;
         String var3 = C_bp.m_a_47921032(C_bp.f_bp_b_b4e[var2]);
         if (var1 == 0 || var3.length() != 0) {
            this.f_bf_a_48a69a2c.addElement(var3);
            this.f_bf_b_48a69a2c.addElement(C_bp.m_a_47921032(C_bp.f_bp_b_b4e[var2 + 1]));
         }
      }

      this.f_bf_c_49 = C_bp.m_a_134621(86);
   }

   private static String p_bf_a_e96ea081(String var0) {
      return var0 != null && var0.length() != 0 ? var0 : "---";
   }

   private void p_bf_f_9b75() {
      int var1;
      if ((var1 = this.f_bf_a_48a69a2c.size()) != 1) {
         if (this.f_bf_p_aa0ede5b == null) {
            this.f_bf_p_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("c4"), 4);
         }

         this.f_bf_p_aa0ede5b.deleteAll();

         for (int var2 = 0; var2 < var1; var2++) {
            this.f_bf_p_aa0ede5b.append(p_bf_a_e96ea081((String)this.f_bf_a_48a69a2c.elementAt(var2)), null);
         }

         this.f_bf_a_67f46df9.append(this.f_bf_p_aa0ede5b);
         if (this.f_bf_c_49 >= var1) {
            this.f_bf_c_49 = var1 - 1;
         }

         this.f_bf_p_aa0ede5b.setSelectedIndex(this.f_bf_c_49, true);
      }

      this.f_bf_a_a1fd9827 = new TextField[var1];
      this.f_bf_b_a1fd9827 = new TextField[var1];

      for (int var5 = 0; var5 < var1; var5++) {
         if (var1 > 1) {
            this.f_bf_a_67f46df9.append("---");
         }

         String var3 = var1 == 1 ? "" : "-" + (var5 + 1);
         TextField var4 = new TextField(C_bs.m_a_e96ea081("c6") + var3, (String)this.f_bf_a_48a69a2c.elementAt(var5), 12, 2);
         TextField var6 = new TextField(C_bs.m_a_e96ea081("m4") + var3, (String)this.f_bf_b_48a69a2c.elementAt(var5), 32, 65536);
         this.f_bf_a_67f46df9.append(var4);
         this.f_bf_a_67f46df9.append(var6);
         this.f_bf_a_a1fd9827[var5] = var4;
         this.f_bf_b_a1fd9827[var5] = var6;
      }

      if (var1 != this.f_bf_d_49) {
         this.f_bf_a_67f46df9.addCommand(this.f_bf_d_1570d10e);
      }

      if (var1 != 1) {
         this.f_bf_a_67f46df9.addCommand(this.f_bf_e_1570d10e);
      }
   }

   private void p_bf_g_9b75() {
      int var1 = this.f_bf_a_48a69a2c.size();

      for (int var4 = 0; var4 < this.f_bf_d_49; var4++) {
         String var2;
         String var3;
         if (var4 < var1) {
            var2 = (String)this.f_bf_a_48a69a2c.elementAt(var4);
            var3 = (String)this.f_bf_b_48a69a2c.elementAt(var4);
         } else {
            var3 = C_bp.f_bp_a_523beb0a;
            var2 = C_bp.f_bp_a_523beb0a;
         }

         C_bp.m_a_4f708078(C_bp.f_bp_b_b4e[var4 << 1], var2);
         C_bp.m_a_4f708078(C_bp.f_bp_b_b4e[(var4 << 1) + 1], var3);
      }

      if (this.f_bf_c_49 >= var1) {
         this.f_bf_c_49 = var1 - 1;
      }

      C_bp.m_a_255f295(86, this.f_bf_c_49);
   }

   private void p_bf_h_9b75() {
      this.f_bf_a_48a69a2c.removeAllElements();
      this.f_bf_b_48a69a2c.removeAllElements();

      for (int var1 = 0; var1 < this.f_bf_a_a1fd9827.length; var1++) {
         this.f_bf_a_48a69a2c.addElement(this.f_bf_a_a1fd9827[var1].getString());
         this.f_bf_b_48a69a2c.addElement(this.f_bf_b_a1fd9827[var1].getString());
      }

      this.f_bf_c_49 = this.f_bf_p_aa0ede5b == null ? 0 : this.f_bf_p_aa0ede5b.getSelectedIndex();
   }

   public final void itemStateChanged(Item var1) {
      int var2;
      if (this.f_bf_a_a1fd9827 != null && (var2 = this.f_bf_a_a1fd9827.length) != 1) {
         for (int var3 = 0; var3 < var2; var3++) {
            if (this.f_bf_a_a1fd9827[var3] == var1) {
               this.f_bf_p_aa0ede5b.set(var3, p_bf_a_e96ea081(this.f_bf_a_a1fd9827[var3].getString()), null);
               return;
            }
         }
      }

      if (var1 == this.f_bf_B_aa0ede5b && this.f_bf_B_aa0ede5b.isSelected(2)) {
         C_bp.m_a_255f295(69, this.f_bf_B_aa0ede5b.getSelectedIndex());
         new C_ab(1, null).m_a_9b75();
      }
   }

   protected final void m_c_9b75() {
      this.f_bf_a_523beb0a = C_bp.m_a_47921032(3);
      this.f_bf_b_5a = C_bp.m_a_134632(130);
      this.f_bf_c_5a = C_bp.m_a_134632(129);
      this.f_bf_a_5a = C_bp.m_a_134632(136);
      C_bp.m_a_134621(65);
      this.f_bf_a_12b93.m_a_9b75();
      this.f_bf_a_12b93.c$13462e();
      this.f_bf_a_12b93.d$1385ff();
      if (C_ac.m_a_9b79()) {
         C_bg.m_a_4686f14a(this.f_bf_a_12b93, "c4", C_w.f_w_b_12a1f.m_a_485a59b9(12), 0, true);
      }

      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "i4", C_w.f_w_b_12a1f.m_a_485a59b9(13), 1, true);
      if (C_bp.m_a_134621(83) == 2) {
         C_bg.m_a_4686f14a(this.f_bf_a_12b93, "c7", C_w.f_w_b_12a1f.m_a_485a59b9(21), 2, true);
      }

      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "g4", C_w.f_w_b_12a1f.m_a_485a59b9(14), 3, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "j1", C_w.f_w_b_12a1f.m_a_485a59b9(32), 4, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "h4", C_w.f_w_b_12a1f.m_a_485a59b9(22), 5, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "f4", C_w.f_w_a_12a1f.m_a_485a59b9(7), 12, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "k4", C_w.f_w_b_12a1f.m_a_485a59b9(15), 6, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "d4", C_w.f_w_b_12a1f.m_a_485a59b9(25), 7, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "26", C_w.f_w_b_12a1f.m_a_485a59b9(26), 8, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "y0", C_w.f_w_b_12a1f.m_a_485a59b9(0), 10, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "i1", C_w.f_w_c_12a1f.m_a_485a59b9(C_bp.m_a_134621(94)), 11, true);
      C_bg.m_a_4686f14a(this.f_bf_a_12b93, "K3", C_w.f_w_b_12a1f.m_a_485a59b9(10), 9, true);
      this.f_bf_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private static void p_bf_a_85a876da(ChoiceGroup var0, String var1) {
      String[] var3 = C_ce.m_a_639c22ad(var1, '|');

      for (int var2 = 0; var2 < var3.length; var2++) {
         var0.append(C_bs.m_a_e96ea081(var3[var2]), null);
      }
   }

   private static void p_bf_a_2f66d769(ChoiceGroup var0, String var1, int var2) {
      p_bf_a_85a876da(var0, var1);
      var0.setSelectedIndex(var0.size() - 1, C_bp.m_a_134632(var2));
   }

   public final void commandAction(Command var1, Displayable var2) {
      boolean var3 = false;
      if (C_bg.m_a_cb3d5d05(this.f_bf_b_12b93) && var1 == C_bg.f_bg_i_1570d10e) {
         if (this.f_bf_c_12b93.m_a_9b68() == 0) {
            for (int var20 = 0; var20 < this.f_bf_a_6dccaaa5.length; var20++) {
               C_bg.m_a_4686f14a(this.f_bf_c_12b93, this.f_bf_a_6dccaaa5[var20], null, this.f_bf_a_b4e[var20], true);
            }
         }

         this.f_bf_c_12b93.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
         this.f_bf_c_12b93.m_a_48817c60(C_bg.f_bg_c_1570d10e, C_be.f_be_f_49);
         int var21;
         int var30 = (var21 = this.f_bf_b_12b93.m_b_9b68()) < 1024 ? C_bp.m_a_134621(var21) : C_bp.m_a_47921032(40).charAt(var21 - 1024);
         this.f_bf_c_12b93.m_a_13462e(var30);
         this.f_bf_c_12b93.d$1385ff();
         this.f_bf_c_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else if (C_bg.m_a_cb3d5d05(this.f_bf_c_12b93)) {
         if (var1 == C_bg.f_bg_i_1570d10e) {
            int var19;
            if ((var19 = this.f_bf_b_12b93.m_b_9b68()) < 1024) {
               C_bp.m_a_255f295(var19, this.f_bf_c_12b93.m_b_9b68());
            } else {
               StringBuffer var29;
               (var29 = new StringBuffer(C_bp.m_a_47921032(40))).setCharAt(var19 - 1024, (char)this.f_bf_c_12b93.m_b_9b68());
               C_bp.m_a_4f708078(40, new String(var29));
            }
         }

         this.p_bf_d_9b75();
      } else if (C_bg.m_a_cb3d5d05(this.f_bf_d_12b93) && var1 == C_bg.f_bg_i_1570d10e) {
         C_bg.m_a_5d527811(this.f_bf_d_12b93);
         C_bg.m_a_9c6ef5f3(f_bf_a_2409a3 = new C_bn(2, C_bn.m_d_134621(C_bp.m_a_134621(this.f_bf_d_12b93.m_b_9b68()))), false);
         f_bf_a_2409a3.d$1385ff();
         f_bf_a_2409a3.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
         f_bf_a_2409a3.m_a_48817c60(C_bg.f_bg_c_1570d10e, C_be.f_be_f_49);
         f_bf_a_2409a3.m_a_6f63a2af(this);
         f_bf_a_2409a3.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else {
         if (C_bg.m_a_cb3d5d05(f_bf_a_2409a3)) {
            if (var1 == C_bg.f_bg_i_1570d10e) {
               C_bg.m_a_5d527811(f_bf_a_2409a3);
               C_cg var18 = new C_cg(this.f_bf_d_12b93.m_b_9b68(), C_bn.m_b_9b68());
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var18);
               return;
            }

            if (var1 == C_bg.f_bg_c_1570d10e) {
               C_bp.f_bp_a_2408ab.m_b_9b75();
               return;
            }
         }

         if (C_bg.m_a_cb3d5d05(this.f_bf_e_12b93) && var1 == C_bg.f_bg_i_1570d10e) {
            C_bp.m_a_255f295(94, this.f_bf_e_12b93.m_b_9b68());
            C_bp.m_c_9b75();
            this.m_c_9b75();
            p_bf_i_9b75();
         } else if (var1 == C_bg.f_bg_i_1570d10e) {
            this.p_bf_j_9b75();
            this.f_bf_a_67f46df9.addCommand(this.f_bf_a_1570d10e);
            switch (this.f_bf_a_12b93.m_b_9b68()) {
               case 0:
                  this.p_bf_e_9b75();
                  this.p_bf_f_9b75();
                  break;
               case 1:
                  this.f_bf_a_a555694c = new TextField(C_bs.m_a_e96ea081("45"), C_bp.m_a_47921032(1), 255, 0);
                  this.f_bf_b_a555694c = new TextField(C_bs.m_a_e96ea081("65"), C_bp.m_a_47921032(2), 5, 2);
                  this.f_bf_c_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("u1"), 4);
                  p_bf_a_85a876da(this.f_bf_c_aa0ede5b, "j5|23");
                  p_bf_a_85a876da(this.f_bf_c_aa0ede5b, "c7");
                  this.f_bf_c_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(83), true);
                  this.f_bf_a_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("f3"), 2);
                  p_bf_a_2f66d769(this.f_bf_a_aa0ede5b, "97", 128);
                  this.f_bf_c_a555694c = new TextField(C_bs.m_a_e96ea081("36"), C_bp.m_a_47921032(13), 4, 2);
                  this.f_bf_b_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("v1"), 2);
                  p_bf_a_85a876da(this.f_bf_b_aa0ede5b, "A3|D0|G0|G4");
                  p_bf_a_85a876da(this.f_bf_b_aa0ede5b, "a5");
                  this.f_bf_b_aa0ede5b.setSelectedIndex(0, C_bp.m_a_134632(144));
                  this.f_bf_b_aa0ede5b.setSelectedIndex(1, C_bp.m_a_134621(64) != 0);
                  this.f_bf_b_aa0ede5b.setSelectedIndex(2, C_bp.m_a_134632(138));
                  this.f_bf_b_aa0ede5b.setSelectedIndex(3, C_bp.m_a_134632(149));
                  this.f_bf_b_aa0ede5b.setSelectedIndex(4, C_bp.m_a_134632(139));
                  this.f_bf_d_a555694c = new TextField(C_bs.m_a_e96ea081("H4"), String.valueOf(C_bp.m_a_134621(91)), 2, 2);
                  this.f_bf_a_67f46df9.append(this.f_bf_a_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_b_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_c_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_a_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_c_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_b_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_d_a555694c);
                  break;
               case 2:
                  this.f_bf_E_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("d7"), 4);
                  this.f_bf_E_aa0ede5b.append(C_bs.m_a_e96ea081("f7"), null);
                  this.f_bf_E_aa0ede5b.append(C_bs.m_a_e96ea081("g7"), null);
                  this.f_bf_E_aa0ede5b.append(C_bs.m_a_e96ea081("h7"), null);
                  this.f_bf_E_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(76), true);
                  this.f_bf_w_a555694c = new TextField(C_bs.m_a_e96ea081("i7"), C_bp.m_a_47921032(8), 32, 0);
                  this.f_bf_x_a555694c = new TextField(C_bs.m_a_e96ea081("j7"), C_bp.m_a_47921032(9), 5, 2);
                  this.f_bf_y_a555694c = new TextField(C_bs.m_a_e96ea081("k7"), C_bp.m_a_47921032(11), 32, 0);
                  this.f_bf_z_a555694c = new TextField(C_bs.m_a_e96ea081("l7"), C_bp.m_a_47921032(12), 32, 65536);
                  this.f_bf_A_a555694c = new TextField(C_bs.m_a_e96ea081("m7"), C_bp.m_a_47921032(10), 5, 2);
                  this.f_bf_a_67f46df9.append(this.f_bf_E_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_w_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_x_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_y_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_z_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_A_a555694c);
                  break;
               case 3:
                  if (C_bs.f_bs_a_6dccaaa5.length > 1) {
                     this.f_bf_d_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("l3"), 4);

                     for (int var17 = 0; var17 < C_bs.f_bs_a_6dccaaa5.length; var17++) {
                        this.f_bf_d_aa0ede5b.append(C_bs.m_a_e96ea081("lang_" + C_bs.f_bs_a_6dccaaa5[var17]), null);
                        if (C_bs.f_bs_a_6dccaaa5[var17].equals(C_bp.m_a_47921032(3))) {
                           this.f_bf_d_aa0ede5b.setSelectedIndex(var17, true);
                        }
                     }
                  }

                  this.f_bf_f_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("M0"), 4);
                  this.f_bf_f_aa0ede5b.append(C_bs.m_a_e96ea081("N0"), null);
                  this.f_bf_f_aa0ede5b.append(C_bs.m_a_e96ea081("O0"), null);
                  if (C_bp.m_a_134632(158)) {
                     this.f_bf_f_aa0ede5b.setSelectedIndex(0, true);
                  } else {
                     this.f_bf_f_aa0ede5b.setSelectedIndex(1, true);
                  }

                  this.f_bf_e_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("K3"), 2);
                  p_bf_a_2f66d769(this.f_bf_e_aa0ede5b, "O2", 145);
                  p_bf_a_2f66d769(this.f_bf_e_aa0ede5b, "X5", 143);
                  this.f_bf_D_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("C1"), 2);
                  p_bf_a_2f66d769(this.f_bf_D_aa0ede5b, "d5", 136);
                  p_bf_a_2f66d769(this.f_bf_D_aa0ede5b, "X2", 130);
                  p_bf_a_2f66d769(this.f_bf_D_aa0ede5b, "Y2", 134);
                  p_bf_a_2f66d769(this.f_bf_D_aa0ede5b, "Z2", 129);
                  p_bf_a_2f66d769(this.f_bf_D_aa0ede5b, "c5", 173);
                  this.f_bf_i_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("A2"), 4);
                  this.f_bf_i_aa0ede5b.append(C_bs.m_a_e96ea081("u2"), null);
                  this.f_bf_i_aa0ede5b.append(C_bs.m_a_e96ea081("v2"), null);
                  this.f_bf_i_aa0ede5b.append(C_bs.m_a_e96ea081("w2"), null);
                  this.f_bf_i_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(111), true);
                  this.f_bf_j_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("B2"), 4);
                  this.f_bf_j_aa0ede5b.append(C_bs.m_a_e96ea081("x2"), null);
                  this.f_bf_j_aa0ede5b.append(C_bs.m_a_e96ea081("y2"), null);
                  this.f_bf_j_aa0ede5b.append(C_bs.m_a_e96ea081("z2"), null);
                  this.f_bf_j_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(112), true);
                  this.f_bf_g_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("q5"), 4);
                  this.f_bf_g_aa0ede5b.append(C_bs.m_a_e96ea081("r5"), null);
                  this.f_bf_g_aa0ede5b.append(C_bs.m_a_e96ea081("s5"), null);
                  this.f_bf_g_aa0ede5b.append(C_bs.m_a_e96ea081("t5"), null);
                  this.f_bf_g_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(65), true);
                  this.f_bf_h_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("f5"), 2);
                  p_bf_a_2f66d769(this.f_bf_h_aa0ede5b, "95", 176);
                  p_bf_a_2f66d769(this.f_bf_h_aa0ede5b, "v4", 177);
                  p_bf_a_2f66d769(this.f_bf_h_aa0ede5b, "V2", 180);
                  p_bf_a_2f66d769(this.f_bf_h_aa0ede5b, "g5", 178);
                  p_bf_a_2f66d769(this.f_bf_h_aa0ede5b, "e5", 179);
                  this.f_bf_k_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("31"), 2);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "41", 135);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "51", 164);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "61", 165);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "e6", 141);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "67", 132);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "06", 131);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "d6", 137);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "D7", 142);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "D1", 133);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "W3", 154);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "b0", 181);
                  p_bf_a_2f66d769(this.f_bf_k_aa0ede5b, "a0", 182);
                  this.f_bf_a_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("76"), true, 10, C_bp.m_a_134621(100) / 25);
                  this.f_bf_b_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("86"), true, 10, C_bp.m_a_134621(116) / 25);
                  this.f_bf_c_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("96"), true, 10, C_bp.m_a_134621(117) / 25);
                  if (this.f_bf_d_aa0ede5b != null) {
                     this.f_bf_a_67f46df9.append(this.f_bf_d_aa0ede5b);
                  }

                  if (!Jimm.f_jimm_Jimm_a_5a) {
                     this.f_bf_a_67f46df9.append(this.f_bf_f_aa0ede5b);
                  }

                  this.f_bf_a_67f46df9.append(this.f_bf_D_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_i_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_j_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_g_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_h_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_k_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_e_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_a_978a2ca0);
                  this.f_bf_a_67f46df9.append(this.f_bf_b_978a2ca0);
                  this.f_bf_a_67f46df9.append(this.f_bf_c_978a2ca0);
                  break;
               case 4:
                  this.m_b_9b75();
                  return;
               case 5:
                  this.p_bf_d_9b75();
                  return;
               case 6:
                  this.f_bf_t_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("74"), 4);
                  this.f_bf_t_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_t_aa0ede5b.append(C_bs.m_a_e96ea081("P0"), null);
                  this.f_bf_t_aa0ede5b.append(C_bs.m_a_e96ea081("k5"), null);
                  this.f_bf_g_a555694c = new TextField(null, C_bp.m_a_47921032(5), 32, 0);
                  this.f_bf_t_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(68), true);
                  this.f_bf_z_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_z_aa0ede5b, "U0", 156);
                  p_bf_a_2f66d769(this.f_bf_z_aa0ede5b, "V0", 157);
                  this.f_bf_j_a555694c = new TextField(C_bs.m_a_e96ea081("X0"), String.valueOf(C_bp.m_a_134621(93)), 3, 2);
                  this.f_bf_u_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("64"), 4);
                  this.f_bf_u_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_u_aa0ede5b.append(C_bs.m_a_e96ea081("P0"), null);
                  this.f_bf_u_aa0ede5b.append(C_bs.m_a_e96ea081("k5"), null);
                  this.f_bf_h_a555694c = new TextField(null, C_bp.m_a_47921032(41), 32, 0);
                  this.f_bf_u_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(99), true);
                  this.f_bf_A_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_A_aa0ede5b, "V0", 184);
                  this.f_bf_k_a555694c = new TextField(C_bs.m_a_e96ea081("X0"), String.valueOf(C_bp.m_a_134621(89)), 3, 2);
                  this.f_bf_s_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("D3"), 4);
                  this.f_bf_s_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_s_aa0ede5b.append(C_bs.m_a_e96ea081("P0"), null);
                  this.f_bf_s_aa0ede5b.append(C_bs.m_a_e96ea081("k5"), null);
                  this.f_bf_s_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(66), true);
                  this.f_bf_d_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("j6"), true, 10, C_bp.m_a_134621(67) / 10);
                  this.f_bf_f_a555694c = new TextField(null, C_bp.m_a_47921032(4), 32, 0);
                  this.f_bf_i_a555694c = new TextField(null, C_bp.m_a_47921032(16), 32, 0);
                  this.f_bf_v_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("b6"), 4);
                  this.f_bf_v_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_v_aa0ede5b.append(C_bs.m_a_e96ea081("a6"), null);
                  this.f_bf_v_aa0ede5b.append(C_bs.m_a_e96ea081("P0"), null);
                  this.f_bf_v_aa0ede5b.append(C_bs.m_a_e96ea081("k5"), null);
                  this.f_bf_v_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(88), true);
                  this.f_bf_w_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_w_aa0ede5b, "T1", 146);
                  this.f_bf_n_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("h6"), 4);
                  this.f_bf_n_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_n_aa0ede5b.append(C_bs.m_a_e96ea081("97"), null);
                  this.f_bf_n_aa0ede5b.append(C_bs.m_a_e96ea081("_7"), null);
                  this.f_bf_n_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(75), true);
                  this.f_bf_m_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("s4"), 4);
                  this.f_bf_m_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_m_aa0ede5b.append(C_bs.m_a_e96ea081("B4"), null);
                  this.f_bf_m_aa0ede5b.append(C_bs.m_a_e96ea081("C4"), null);
                  this.f_bf_m_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(84), true);
                  this.f_bf_a_67f46df9.append(this.f_bf_d_978a2ca0);
                  this.f_bf_a_67f46df9.append(this.f_bf_s_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_f_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_n_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_t_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_g_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_z_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_j_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_u_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_h_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_A_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_k_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_v_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_i_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_w_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_m_aa0ede5b);
                  this.f_bf_o_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("K3"), 2);
                  p_bf_a_2f66d769(this.f_bf_o_aa0ede5b, "Y0", 151);
                  p_bf_a_2f66d769(this.f_bf_o_aa0ede5b, "G1", 152);
                  p_bf_a_2f66d769(this.f_bf_o_aa0ede5b, "u5", 168);
                  this.f_bf_a_67f46df9.append(this.f_bf_o_aa0ede5b);
                  break;
               case 7:
                  this.f_bf_s_a555694c = new TextField(C_bs.m_a_e96ea081("F1"), C_ce.m_b_47921032(C_bp.m_a_134621(70)), 6, 0);
                  this.f_bf_t_a555694c = new TextField(C_bs.m_a_e96ea081("E1"), C_ce.m_b_47921032(C_bp.m_a_134621(71)), 6, 0);
                  this.f_bf_u_a555694c = new TextField(C_bs.m_a_e96ea081("q4"), String.valueOf(C_bp.m_a_134621(72) / 1024), 4, 2);
                  this.f_bf_v_a555694c = new TextField(C_bs.m_a_e96ea081("I1"), C_bp.m_a_47921032(6), 4, 0);
                  this.f_bf_a_67f46df9.append(this.f_bf_s_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_t_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_u_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_v_a555694c);
                  break;
               case 8:
                  boolean var15 = false;
                  this.f_bf_q_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("26"), 4);

                  for (int var25 = -12; var25 <= 13; var25++) {
                     this.f_bf_q_aa0ede5b.append("GMT" + (var25 < 0 ? "" : "+") + var25 + ":00", null);
                  }

                  this.f_bf_q_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(87) + 12, true);
                  int[] var26 = C_ce.m_a_255f4d5(C_ce.a$1385f3());
                  this.f_bf_r_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("v3"), 4);
                  int var32 = var26[1];
                  int var16 = var26[2];

                  for (int var27 = 0; var27 < 24; var27++) {
                     this.f_bf_r_aa0ede5b.append(var27 + ":" + var32, null);
                  }

                  this.f_bf_r_aa0ede5b.setSelectedIndex(var16, true);
                  Calendar var28;
                  (var28 = Calendar.getInstance()).setTime(new Date());
                  this.f_bf_b_49 = var28.get(11);
                  this.f_bf_a_67f46df9.append(this.f_bf_q_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_r_aa0ede5b);
                  break;
               case 9:
                  this.f_bf_x_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_x_aa0ede5b, "e1", 155);
                  p_bf_a_2f66d769(this.f_bf_x_aa0ede5b, "50", 172);
                  this.f_bf_B_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("Q0"), 4);
                  this.f_bf_B_aa0ede5b.append(C_bs.m_a_e96ea081("T3"), null);
                  this.f_bf_B_aa0ede5b.append(C_bs.m_a_e96ea081("97"), null);
                  this.f_bf_B_aa0ede5b.append(C_bs.m_a_e96ea081("D2"), null);
                  this.f_bf_B_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(69), true);
                  this.f_bf_o_a555694c = new TextField(null, C_bp.m_a_47921032(34), 255, 0);
                  this.f_bf_n_a555694c = new TextField(C_bs.m_a_e96ea081("w5"), C_bp.m_a_47921032(38), 20, 65536);
                  this.f_bf_a_67f46df9.append(this.f_bf_x_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_B_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_o_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_n_a555694c);
                  break;
               case 10:
                  this.f_bf_p_a555694c = new TextField(C_bs.m_a_e96ea081("A0"), C_bp.m_a_47921032(35), 255, 0);
                  this.f_bf_q_a555694c = new TextField(C_bs.m_a_e96ea081("B0"), C_bp.m_a_47921032(37), 255, 0);
                  this.f_bf_r_a555694c = new TextField(C_bs.m_a_e96ea081("C0"), C_bp.m_a_47921032(36), 255, 0);
                  this.f_bf_C_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_C_aa0ede5b, "z0", 162);
                  this.f_bf_a_67f46df9.append(this.f_bf_p_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_q_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_r_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_C_aa0ede5b);
                  break;
               case 11:
                  this.f_bf_e_12b93.m_a_9b75();
                  C_bg.m_a_9c6ef5f3(this.f_bf_e_12b93, false);
                  this.f_bf_e_12b93.c$13462e();
                  this.f_bf_e_12b93.d$1385ff();
                  this.f_bf_e_12b93.m_i_9b75();
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "Jimm", C_w.f_w_c_12a1f.m_a_485a59b9(7), 7, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "Miranda", C_w.f_w_c_12a1f.m_a_485a59b9(1), 1, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "QIP 2005a", C_w.f_w_c_12a1f.m_a_485a59b9(0), 0, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "QIP PDA (Symbian)", C_w.f_w_c_12a1f.m_a_485a59b9(12), 12, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "QIP PDA (Windows)", C_w.f_w_c_12a1f.m_a_485a59b9(13), 13, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "QIP Infium", C_w.f_w_c_12a1f.m_a_485a59b9(14), 14, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "ICQ 5.1", C_w.f_w_c_12a1f.m_a_485a59b9(18), 18, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "ICQ 6", C_w.f_w_c_12a1f.m_a_485a59b9(15), 15, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "StICQ", C_w.f_w_c_12a1f.m_a_485a59b9(8), 8, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "VmICQ", C_w.f_w_c_12a1f.m_a_485a59b9(11), 11, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "mChat", C_w.f_w_c_12a1f.m_a_485a59b9(21), 21, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "&RQ", C_w.f_w_c_12a1f.m_a_485a59b9(2), 2, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "R&Q", C_w.f_w_c_12a1f.m_a_485a59b9(3), 3, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "Kopete", C_w.f_w_c_12a1f.m_a_485a59b9(6), 6, false);
                  C_bg.m_a_4686f14a(this.f_bf_e_12b93, "Mac ICQ", C_w.f_w_c_12a1f.m_a_485a59b9(22), 22, false);
                  this.f_bf_e_12b93.m_j_9b75();
                  this.f_bf_e_12b93.m_a_13462e(C_bp.m_a_134621(94));
                  this.f_bf_e_12b93.m_a_48817c60(C_bg.f_bg_c_1570d10e, C_be.f_be_f_49);
                  this.f_bf_e_12b93.m_a_48817c60(C_bg.f_bg_i_1570d10e, C_be.f_be_e_49);
                  this.f_bf_e_12b93.m_a_6f63a2af(this);
                  this.f_bf_e_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 12:
                  this.f_bf_y_aa0ede5b = new ChoiceGroup(null, 2);
                  p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "P1", 170);
                  p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "_1", 169);
                  p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "w0", 166);
                  p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "L0", 167);
                  if (C_ac.f_ac_b_5a && C_ac.m_b_9b79()) {
                     p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "Web Aware", 160);
                     p_bf_a_2f66d769(this.f_bf_y_aa0ede5b, "F0", 183);
                  }

                  this.f_bf_l_a555694c = new TextField(C_bs.m_a_e96ea081("a3"), C_bp.m_a_47921032(31), 11, 0);
                  this.f_bf_m_a555694c = new TextField(C_bs.m_a_e96ea081("b3"), String.valueOf(C_bp.m_a_134621(98)), 5, 2);
                  this.f_bf_l_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("H0"), 2);
                  p_bf_a_2f66d769(this.f_bf_l_aa0ede5b, "I0", 147);
                  p_bf_a_2f66d769(this.f_bf_l_aa0ede5b, "J0", 153);
                  this.f_bf_e_a555694c = new TextField(C_bs.m_a_e96ea081("K0"), String.valueOf(C_bp.m_a_134621(109)), 3, 2);
                  this.f_bf_a_67f46df9.append(this.f_bf_y_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_l_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_m_a555694c);
                  this.f_bf_a_67f46df9.append(this.f_bf_l_aa0ede5b);
                  this.f_bf_a_67f46df9.append(this.f_bf_e_a555694c);
            }

            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_bf_a_67f46df9);
         } else if (var1 == C_bg.f_bg_c_1570d10e) {
            if (var2 != this.f_bf_a_67f46df9 && !this.f_bf_e_12b93.m_b_9b79()) {
               C_bp.f_bp_a_2408ab = null;
               C_bo.m_b_9b75();
            } else {
               this.f_bf_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            }
         } else if (var1 == this.f_bf_a_1570d10e) {
            switch (this.f_bf_a_12b93.m_b_9b68()) {
               case 0:
                  this.p_bf_h_9b75();
                  this.p_bf_g_9b75();
                  break;
               case 1:
                  C_bp.m_a_4f708078(1, this.f_bf_a_a555694c.getString());
                  C_bp.m_a_4f708078(2, this.f_bf_b_a555694c.getString());
                  C_bp.m_a_255f295(83, this.f_bf_c_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_2563266(128, this.f_bf_a_aa0ede5b.isSelected(0));
                  C_bp.m_a_4f708078(13, this.f_bf_c_a555694c.getString());
                  C_bp.m_a_2563266(144, this.f_bf_b_aa0ede5b.isSelected(0));
                  if (this.f_bf_b_aa0ede5b.isSelected(1)) {
                     C_bp.m_a_255f295(64, 1);
                  } else {
                     C_bp.m_a_255f295(64, 0);
                  }

                  C_bp.m_a_2563266(138, this.f_bf_b_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(149, this.f_bf_b_aa0ede5b.isSelected(3));
                  C_bp.m_a_2563266(139, this.f_bf_b_aa0ede5b.isSelected(4));
                  C_bp.m_a_255f295(91, Integer.parseInt(this.f_bf_d_a555694c.getString()));
                  break;
               case 2:
                  C_bp.m_a_255f295(76, this.f_bf_E_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_4f708078(8, this.f_bf_w_a555694c.getString());
                  C_bp.m_a_4f708078(9, this.f_bf_x_a555694c.getString());
                  C_bp.m_a_4f708078(11, this.f_bf_y_a555694c.getString());
                  C_bp.m_a_4f708078(12, this.f_bf_z_a555694c.getString());
                  C_bp.m_a_4f708078(10, this.f_bf_A_a555694c.getString());
                  break;
               case 3:
                  if (C_bs.f_bs_a_6dccaaa5.length > 1) {
                     C_bp.m_a_4f708078(3, C_bs.f_bs_a_6dccaaa5[this.f_bf_d_aa0ede5b.getSelectedIndex()]);
                  }

                  boolean var13 = false;
                  C_bp.m_a_2563266(145, this.f_bf_e_aa0ede5b.isSelected(0));
                  C_bg.m_a_1385ff(false);
                  C_bp.m_a_2563266(143, this.f_bf_e_aa0ede5b.isSelected(1));
                  C_be.m_k_9b75();
                  C_bp.m_a_2563266(158, this.f_bf_f_aa0ede5b.isSelected(0));
                  if (this.f_bf_f_aa0ede5b.isSelected(0)) {
                     Light.setLightOn();
                  } else {
                     Light.setLightOff();
                  }

                  int var23 = this.f_bf_g_aa0ede5b.getSelectedIndex();
                  boolean var31 = this.f_bf_D_aa0ede5b.isSelected(0);
                  boolean var14 = this.f_bf_D_aa0ede5b.isSelected(1);
                  C_bp.m_a_255f295(111, this.f_bf_i_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(112, this.f_bf_j_aa0ede5b.getSelectedIndex());
                  C_bp.m_b_9b75();
                  C_bp.m_a_255f295(65, var23);
                  C_bp.m_a_2563266(130, var14);
                  C_bp.m_a_2563266(134, this.f_bf_D_aa0ede5b.isSelected(2));
                  boolean var24 = this.f_bf_D_aa0ede5b.isSelected(3);
                  C_bp.m_a_2563266(129, var24);
                  C_bp.m_a_2563266(173, this.f_bf_D_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(176, this.f_bf_h_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(177, this.f_bf_h_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(180, this.f_bf_h_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(178, this.f_bf_h_aa0ede5b.isSelected(3));
                  C_bp.m_a_2563266(179, this.f_bf_h_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(135, this.f_bf_k_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(164, this.f_bf_k_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(165, this.f_bf_k_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(141, this.f_bf_k_aa0ede5b.isSelected(3));
                  C_bp.m_a_2563266(132, this.f_bf_k_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(131, this.f_bf_k_aa0ede5b.isSelected(5));
                  C_bp.m_a_2563266(137, this.f_bf_k_aa0ede5b.isSelected(6));
                  C_bp.m_a_2563266(142, this.f_bf_k_aa0ede5b.isSelected(7));
                  C_bp.m_a_2563266(133, this.f_bf_k_aa0ede5b.isSelected(8));
                  C_bp.m_a_2563266(154, this.f_bf_k_aa0ede5b.isSelected(9));
                  C_bp.m_a_2563266(181, this.f_bf_k_aa0ede5b.isSelected(10));
                  C_bp.m_a_2563266(182, this.f_bf_k_aa0ede5b.isSelected(11));
                  C_bp.m_a_2563266(136, var31);
                  C_w.m_a_1385ff(var31 != this.f_bf_a_5a || var14 != this.f_bf_b_5a || var24 != this.f_bf_c_5a);
                  C_bp.m_a_255f295(100, this.f_bf_a_978a2ca0.getValue() * 25);
                  C_bp.m_a_255f295(116, this.f_bf_b_978a2ca0.getValue() * 25);
                  C_bp.m_a_255f295(117, this.f_bf_c_978a2ca0.getValue() * 25);
                  if (!this.f_bf_a_523beb0a.equals(C_bp.m_a_47921032(3))) {
                     C_bp.m_a_2563266(148, true);
                  }
               case 4:
               case 5:
               case 11:
               default:
                  break;
               case 6:
                  C_bp.m_a_255f295(66, this.f_bf_s_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(75, this.f_bf_n_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(68, this.f_bf_t_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(99, this.f_bf_u_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(88, this.f_bf_v_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(67, this.f_bf_d_978a2ca0.getValue() * 10);
                  C_bp.m_a_4f708078(4, this.f_bf_f_a555694c.getString());
                  C_bp.m_a_4f708078(5, this.f_bf_g_a555694c.getString());
                  C_bp.m_a_4f708078(41, this.f_bf_h_a555694c.getString());
                  C_bp.m_a_4f708078(16, this.f_bf_i_a555694c.getString());
                  C_bp.m_a_2563266(156, this.f_bf_z_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(157, this.f_bf_z_aa0ede5b.isSelected(1));
                  C_bp.m_a_255f295(93, Integer.parseInt(this.f_bf_j_a555694c.getString()));
                  C_bp.m_a_2563266(184, this.f_bf_A_aa0ede5b.isSelected(0));
                  C_bp.m_a_255f295(89, Integer.parseInt(this.f_bf_k_a555694c.getString()));
                  C_bp.m_a_2563266(146, this.f_bf_w_aa0ede5b.isSelected(0));
                  C_bp.m_a_255f295(84, this.f_bf_m_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_2563266(151, this.f_bf_o_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(152, this.f_bf_o_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(168, this.f_bf_o_aa0ede5b.isSelected(2));
                  break;
               case 7:
                  C_bp.m_a_255f295(70, C_ce.m_a_aad3b1f2(this.f_bf_s_a555694c.getString()));
                  this.f_bf_s_a555694c.setString(C_ce.m_b_47921032(C_bp.m_a_134621(70)));
                  C_bp.m_a_255f295(71, C_ce.m_a_aad3b1f2(this.f_bf_t_a555694c.getString()));
                  this.f_bf_t_a555694c.setString(C_ce.m_b_47921032(C_bp.m_a_134621(71)));
                  C_bp.m_a_255f295(72, Integer.parseInt(this.f_bf_u_a555694c.getString()) << 10);
                  C_bp.m_a_4f708078(6, this.f_bf_v_a555694c.getString());
                  break;
               case 8:
                  int var10 = this.f_bf_q_aa0ede5b.getSelectedIndex() - 12;
                  C_bp.m_a_255f295(87, var10);
                  int var11;
                  if ((var11 = this.f_bf_r_aa0ede5b.getSelectedIndex() - var10) < 0) {
                     var11 += 24;
                  }

                  if (var11 >= 24) {
                     var11 -= 24;
                  }

                  int var12 = var11 - this.f_bf_b_49;

                  while (var12 >= 12) {
                     var12 -= 24;
                  }

                  while (var12 < -12) {
                     var12 += 24;
                  }

                  C_bp.m_a_255f295(90, var12);
                  break;
               case 9:
                  boolean var8 = false;
                  C_bp.m_a_2563266(155, this.f_bf_x_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(172, this.f_bf_x_aa0ede5b.isSelected(1));
                  int var9 = this.f_bf_B_aa0ede5b.getSelectedIndex();
                  C_bp.m_a_255f295(69, var9);
                  switch (var9) {
                     case 0:
                        C_bp.m_a_4f708078(34, "/back.png");
                        C_be.m_a_b329a056(null, true);
                        break;
                     case 1:
                        C_bp.m_a_4f708078(34, this.f_bf_o_a555694c.getString());
                        C_be.m_a_b329a056(null, false);
                  }

                  C_bp.m_a_4f708078(38, this.f_bf_n_a555694c.getString());
                  break;
               case 10:
                  C_bp.m_a_4f708078(35, this.f_bf_p_a555694c.getString());
                  C_bp.m_a_4f708078(37, this.f_bf_q_a555694c.getString());
                  C_bp.m_a_4f708078(36, this.f_bf_r_a555694c.getString());
                  C_bp.m_a_2563266(162, this.f_bf_C_aa0ede5b.isSelected(0));
                  break;
               case 12:
                  boolean var7 = false;
                  C_bp.m_a_2563266(170, this.f_bf_y_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(169, this.f_bf_y_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(166, this.f_bf_y_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(167, this.f_bf_y_aa0ede5b.isSelected(3));
                  if (C_ac.f_ac_b_5a && C_ac.m_b_9b79()) {
                     C_bp.m_a_2563266(160, this.f_bf_y_aa0ede5b.isSelected(4));
                     C_bp.m_a_2563266(183, this.f_bf_y_aa0ede5b.isSelected(5));
                  }

                  C_bp.m_a_4f708078(31, this.f_bf_l_a555694c.getString());
                  C_bp.m_a_255f295(98, Integer.parseInt(this.f_bf_m_a555694c.getString()));
                  C_bp.m_a_2563266(147, this.f_bf_l_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(153, this.f_bf_l_aa0ede5b.isSelected(1));
                  C_bp.m_a_255f295(109, Integer.parseInt(this.f_bf_e_a555694c.getString()));
                  C_ck.f_ck_a_49 = C_bp.m_a_134621(109) * 60000;
                  var3 = true;
            }

            if (this.f_bf_d_12b93.m_b_9b79()) {
               C_bg.m_a_1385ff(true);
            }

            C_bp.m_c_9b75();
            C_bg.m_a_9c6ef5f3(this.f_bf_a_12b93, false);
            C_bp.m_a_9b75();
            if (var3) {
               C_ac.m_f_9b75();
               p_bf_i_9b75();
            }

            this.m_c_9b75();
         } else if (var1 == this.f_bf_b_1570d10e) {
            C_bp.m_a_2563266(175, true);
            this.p_bf_d_9b75();
         } else if (var1 == this.f_bf_c_1570d10e) {
            C_bp.m_a_2563266(175, false);
            this.p_bf_d_9b75();
         } else if (var1 == this.f_bf_d_1570d10e) {
            this.p_bf_h_9b75();
            this.f_bf_a_48a69a2c.addElement(C_bp.f_bp_a_523beb0a);
            this.f_bf_b_48a69a2c.addElement(C_bp.f_bp_a_523beb0a);
            this.p_bf_j_9b75();
            this.p_bf_f_9b75();
         } else if (var1 != this.f_bf_e_1570d10e) {
            if (C_bg.m_a_48817c53(var1, 1) == 1) {
               this.p_bf_h_9b75();
               int var6 = C_bg.m_b_9b68();
               this.f_bf_a_48a69a2c.removeElementAt(var6);
               this.f_bf_b_48a69a2c.removeElementAt(var6);
               this.p_bf_j_9b75();
               this.p_bf_f_9b75();
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_bf_a_67f46df9);
            }
         } else {
            this.p_bf_h_9b75();
            int var5;
            String[] var22 = new String[var5 = this.f_bf_a_48a69a2c.size()];

            for (int var4 = 0; var4 < var5; var4++) {
               var22[var4] = p_bf_a_e96ea081((String)this.f_bf_a_48a69a2c.elementAt(var4));
            }

            C_bg.m_a_1cc7afeb("W1", var22, this, 1, false);
         }
      }
   }

   private static void p_bf_i_9b75() {
      if (C_ac.m_b_9b79()) {
         try {
            C_al.m_a_9b75();
            C_al.m_a_13462e(C_ac.m_d_9b68() | (int)C_bp.a$134622());
            return;
         } catch (Exception var0) {
         }
      }
   }

   private void p_bf_j_9b75() {
      this.f_bf_a_67f46df9.removeCommand(this.f_bf_d_1570d10e);
      this.f_bf_a_67f46df9.removeCommand(this.f_bf_e_1570d10e);
      this.f_bf_a_67f46df9.deleteAll();
   }
}
