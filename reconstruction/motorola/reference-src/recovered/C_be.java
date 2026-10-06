package recovered;

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

final class C_be implements CommandListener, ItemStateListener {
   private boolean f_be_a_5a;
   private boolean f_be_b_5a;
   private boolean f_be_c_5a;
   private int f_be_b_49;
   private String f_be_a_523beb0a;
   private Command f_be_a_1570d10e;
   private Command f_be_b_1570d10e;
   private Command f_be_c_1570d10e;
   private C_s f_be_a_12b74;
   private Form f_be_a_67f46df9;
   private TextField[] f_be_a_a1fd9827;
   private TextField[] f_be_b_a1fd9827;
   private TextField f_be_a_a555694c;
   private TextField f_be_b_a555694c;
   private ChoiceGroup f_be_a_aa0ede5b;
   private TextField f_be_c_a555694c;
   private ChoiceGroup f_be_b_aa0ede5b;
   private ChoiceGroup f_be_c_aa0ede5b;
   private TextField f_be_d_a555694c;
   private ChoiceGroup f_be_d_aa0ede5b;
   private ChoiceGroup f_be_e_aa0ede5b;
   private ChoiceGroup f_be_f_aa0ede5b;
   private ChoiceGroup f_be_g_aa0ede5b;
   private ChoiceGroup f_be_h_aa0ede5b;
   private ChoiceGroup f_be_i_aa0ede5b;
   private ChoiceGroup f_be_j_aa0ede5b;
   private ChoiceGroup f_be_k_aa0ede5b;
   private TextField f_be_e_a555694c;
   private ChoiceGroup f_be_l_aa0ede5b;
   private ChoiceGroup f_be_m_aa0ede5b;
   private ChoiceGroup f_be_n_aa0ede5b;
   private ChoiceGroup f_be_o_aa0ede5b;
   private ChoiceGroup f_be_p_aa0ede5b;
   private ChoiceGroup f_be_q_aa0ede5b;
   private Gauge f_be_a_978a2ca0;
   private Gauge f_be_b_978a2ca0;
   private Gauge f_be_c_978a2ca0;
   private ChoiceGroup f_be_r_aa0ede5b;
   private ChoiceGroup f_be_s_aa0ede5b;
   private ChoiceGroup f_be_t_aa0ede5b;
   private ChoiceGroup f_be_u_aa0ede5b;
   private Gauge f_be_d_978a2ca0;
   private TextField f_be_f_a555694c;
   private TextField f_be_g_a555694c;
   private TextField f_be_h_a555694c;
   private TextField f_be_i_a555694c;
   private ChoiceGroup f_be_v_aa0ede5b;
   private ChoiceGroup f_be_w_aa0ede5b;
   private ChoiceGroup f_be_x_aa0ede5b;
   private ChoiceGroup f_be_y_aa0ede5b;
   private TextField f_be_j_a555694c;
   private ChoiceGroup f_be_z_aa0ede5b;
   private TextField f_be_k_a555694c;
   private TextField f_be_l_a555694c;
   private TextField f_be_m_a555694c;
   private TextField f_be_n_a555694c;
   private ChoiceGroup f_be_A_aa0ede5b;
   private TextField f_be_o_a555694c;
   private TextField f_be_p_a555694c;
   private TextField f_be_q_a555694c;
   private TextField f_be_r_a555694c;
   private ChoiceGroup f_be_B_aa0ede5b;
   private TextField f_be_s_a555694c;
   private TextField f_be_t_a555694c;
   private TextField f_be_u_a555694c;
   private TextField f_be_v_a555694c;
   private ChoiceGroup f_be_C_aa0ede5b;
   private TextField f_be_w_a555694c;
   private ChoiceGroup f_be_D_aa0ede5b;
   private ChoiceGroup f_be_E_aa0ede5b;
   private TextField f_be_x_a555694c;
   private TextField f_be_y_a555694c;
   private TextField f_be_z_a555694c;
   private TextField f_be_A_a555694c;
   private TextField f_be_B_a555694c;
   private C_s f_be_b_12b74;
   private C_s f_be_c_12b74;
   private C_s f_be_d_12b74;
   private C_s f_be_e_12b74;
   private final String[] f_be_a_6dccaaa5 = C_cf.m_a_639c22ad("o2|b3|75|B7|p2|g4|H3|l3|Q1|T2|r5|a0|T1|27", '|');
   private final int[] f_be_a_b4e = new int[]{0, 2, 3, 8, 4, 5, 6, 7, 10, 11, 12, 14, 15, 16};
   private Command f_be_d_1570d10e = new Command(C_bs.m_a_e96ea081("p0"), 8, 3);
   private Command f_be_e_1570d10e = new Command(C_bs.a$7a1ba7c4("02"), 8, 3);
   private int f_be_c_49;
   private Vector f_be_a_48a69a2c = new Vector();
   private Vector f_be_b_48a69a2c = new Vector();
   private int f_be_d_49 = C_bp.f_bp_b_b4e.length / 2;
   private static C_bm f_be_a_240984;

   public C_be() {
      this.f_be_b_12b74 = new C_s(C_bs.m_a_e96ea081("d2"));
      C_bf.m_a_9c60de72(this.f_be_b_12b74, false);
      this.f_be_b_12b74.m_a_6f63a2af(this);
      this.f_be_c_12b74 = new C_s(C_bs.m_a_e96ea081("e2"));
      C_bf.m_a_9c60de72(this.f_be_c_12b74, false);
      this.f_be_c_12b74.m_a_6f63a2af(this);
      this.f_be_a_1570d10e = new Command(C_bs.m_a_e96ea081("a"), 1, 1);
      this.f_be_b_1570d10e = new Command(C_bs.m_a_e96ea081("j0"), 8, 2);
      this.f_be_c_1570d10e = new Command(C_bs.m_a_e96ea081("k0"), 8, 2);
      this.f_be_a_12b74 = new C_s(C_bs.m_a_e96ea081("g4"));
      C_bf.m_a_9c60de72(this.f_be_a_12b74, false);
      this.f_be_d_12b74 = new C_s(C_bs.m_a_e96ea081("o1"));
      this.f_be_e_12b74 = new C_s(C_bs.m_a_e96ea081("n1"));
      this.f_be_a_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
      this.f_be_a_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      this.f_be_a_12b74.m_a_6f63a2af(this);
      this.f_be_a_67f46df9 = new Form(C_bs.m_a_e96ea081("g4"));
      this.f_be_a_67f46df9.addCommand(this.f_be_a_1570d10e);
      this.f_be_a_67f46df9.addCommand(C_bf.f_bf_c_1570d10e);
      this.f_be_a_67f46df9.setCommandListener(this);
      this.f_be_a_67f46df9.setItemStateListener(this);
   }

   public final void m_a_9b75() {
      this.f_be_a_12b74.m_a_13462e(9);
      this.commandAction(C_bf.f_bf_i_1570d10e, null);
   }

   private String p_be_a_d9e4d430(String var1, int var2, boolean var3) {
      String var4 = var3 ? C_bs.m_a_e96ea081(var1) : var1;
      var2 = var3 ? C_bp.m_a_134621(var2) : var2;

      for (int var6 = 0; var6 < this.f_be_a_6dccaaa5.length; var6++) {
         if (this.f_be_a_b4e[var6] == var2) {
            return var4 + ": " + C_bs.m_a_e96ea081(this.f_be_a_6dccaaa5[var6]);
         }
      }

      return var4 + ": <???>";
   }

   private void p_be_d_9b75() {
      int var1 = this.f_be_b_12b74.m_b_9b68();
      boolean var2 = C_bp.m_a_134632(175);
      this.f_be_b_12b74.m_a_9b75();
      if (!var2) {
         boolean var5 = false;
         String var4 = "f2";
         Object var3 = null;
         C_bf.m_a_d35e089(this.f_be_b_12b74, this.p_be_a_d9e4d430(var4, 77, true), null, 77, true);
      }

      char var13 = '\u0000';
      String var8 = "h2";
      String var6 = null;
      C_bf.m_a_d35e089(this.f_be_b_12b74, this.p_be_a_d9e4d430(var8, 79, true), null, 79, true);
      var8 = "j2";
      C_bf.m_a_d35e089(this.f_be_b_12b74, this.p_be_a_d9e4d430("j2", 80, true), null, 80, true);
      var8 = "l2";
      C_bf.m_a_d35e089(this.f_be_b_12b74, this.p_be_a_d9e4d430("l2", 82, true), null, 82, true);
      if (var2) {
         if ((var6 = C_bp.m_a_47921032(40)).length() < C_bp.f_bp_a_49) {
            var6 = "";

            for (int var11 = 0; var11 < C_bp.f_bp_a_49; var11++) {
               var6 = var6 + '\u0000';
            }

            C_bp.m_a_4f708078(40, var6);
         }

         for (int var12 = 0; var12 < C_bp.f_bp_a_49; var12++) {
            var13 = var6.charAt(var12);
            C_bf.m_a_d35e089(this.f_be_b_12b74, this.p_be_a_d9e4d430("0+" + C_bp.f_bp_a_6dccaaa5[var12], var13, false), null, var12 + 1024, true);
         }
      }

      this.f_be_b_12b74.m_a_13462e(var1);
      this.f_be_b_12b74.d$1385ff();
      this.f_be_b_12b74.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_f_49);
      this.f_be_b_12b74.m_a_48817c60(this.f_be_a_1570d10e, C_bd.f_bd_h_49);
      if (var2) {
         this.f_be_b_12b74.m_a_8eb9d703(this.f_be_b_1570d10e);
         this.f_be_b_12b74.m_a_48817c60(this.f_be_c_1570d10e, C_bd.f_bd_h_49);
      } else {
         this.f_be_b_12b74.m_a_8eb9d703(this.f_be_c_1570d10e);
         this.f_be_b_12b74.m_a_48817c60(this.f_be_b_1570d10e, C_bd.f_bd_h_49);
      }

      this.f_be_b_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      this.f_be_b_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void m_b_9b75() {
      int var1 = this.f_be_d_12b74.m_b_9b68();
      this.f_be_d_12b74.m_a_9b75();
      C_bf.m_a_9c60de72(this.f_be_d_12b74, false);
      this.f_be_d_12b74.c$13462e();
      this.f_be_d_12b74.d$1385ff();
      C_f var2 = C_v.f_v_b_12a1f.m_a_485a59b9(32);
      this.f_be_d_12b74.m_i_9b75();
      C_bf.m_a_d35e089(this.f_be_d_12b74, "t1", var2, 106, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "p1", var2, 102, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "x1", var2, 115, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "w1", var2, 114, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "q1", var2, 103, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "r1", var2, 104, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "s1", var2, 105, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "u1", var2, 108, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "v1", var2, 113, true);
      C_bf.m_a_d35e089(this.f_be_d_12b74, "Y0", var2, 107, true);
      this.f_be_d_12b74.m_j_9b75();
      this.f_be_d_12b74.m_a_13462e(var1);
      this.f_be_d_12b74.m_a_48817c60(this.f_be_a_1570d10e, C_bd.f_bd_f_49);
      this.f_be_d_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      this.f_be_d_12b74.m_a_6f63a2af(this);
      this.f_be_d_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private void p_be_e_9b75() {
      this.f_be_a_48a69a2c.removeAllElements();
      this.f_be_b_48a69a2c.removeAllElements();

      for (int var1 = 0; var1 < this.f_be_d_49; var1++) {
         int var2 = var1 << 1;
         String var3 = C_bp.m_a_47921032(C_bp.f_bp_b_b4e[var2]);
         if (var1 == 0 || var3.length() != 0) {
            this.f_be_a_48a69a2c.addElement(var3);
            this.f_be_b_48a69a2c.addElement(C_bp.m_a_47921032(C_bp.f_bp_b_b4e[var2 + 1]));
         }
      }

      this.f_be_c_49 = C_bp.m_a_134621(86);
   }

   private static String p_be_a_e96ea081(String var0) {
      return var0 != null && var0.length() != 0 ? var0 : "---";
   }

   private void p_be_f_9b75() {
      int var1;
      if ((var1 = this.f_be_a_48a69a2c.size()) != 1) {
         if (this.f_be_o_aa0ede5b == null) {
            this.f_be_o_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("h4"), 4);
         }

         this.f_be_o_aa0ede5b.deleteAll();

         for (int var2 = 0; var2 < var1; var2++) {
            this.f_be_o_aa0ede5b.append(p_be_a_e96ea081((String)this.f_be_a_48a69a2c.elementAt(var2)), null);
         }

         this.f_be_a_67f46df9.append(this.f_be_o_aa0ede5b);
         if (this.f_be_c_49 >= var1) {
            this.f_be_c_49 = var1 - 1;
         }

         this.f_be_o_aa0ede5b.setSelectedIndex(this.f_be_c_49, true);
      }

      this.f_be_a_a1fd9827 = new TextField[var1];
      this.f_be_b_a1fd9827 = new TextField[var1];

      for (int var5 = 0; var5 < var1; var5++) {
         if (var1 > 1) {
            this.f_be_a_67f46df9.append("---");
         }

         String var3 = var1 == 1 ? "" : "-" + (var5 + 1);
         TextField var4 = new TextField(C_bs.m_a_e96ea081("i6") + var3, (String)this.f_be_a_48a69a2c.elementAt(var5), 12, 2);
         TextField var6 = new TextField(C_bs.m_a_e96ea081("r4") + var3, (String)this.f_be_b_48a69a2c.elementAt(var5), 32, 65536);
         this.f_be_a_67f46df9.append(var4);
         this.f_be_a_67f46df9.append(var6);
         this.f_be_a_a1fd9827[var5] = var4;
         this.f_be_b_a1fd9827[var5] = var6;
      }

      if (var1 != this.f_be_d_49) {
         this.f_be_a_67f46df9.addCommand(this.f_be_d_1570d10e);
      }

      if (var1 != 1) {
         this.f_be_a_67f46df9.addCommand(this.f_be_e_1570d10e);
      }
   }

   private void p_be_g_9b75() {
      int var1 = this.f_be_a_48a69a2c.size();

      for (int var4 = 0; var4 < this.f_be_d_49; var4++) {
         String var2;
         String var3;
         if (var4 < var1) {
            var2 = (String)this.f_be_a_48a69a2c.elementAt(var4);
            var3 = (String)this.f_be_b_48a69a2c.elementAt(var4);
         } else {
            var3 = C_bp.f_bp_a_523beb0a;
            var2 = C_bp.f_bp_a_523beb0a;
         }

         C_bp.m_a_4f708078(C_bp.f_bp_b_b4e[var4 << 1], var2);
         C_bp.m_a_4f708078(C_bp.f_bp_b_b4e[(var4 << 1) + 1], var3);
      }

      if (this.f_be_c_49 >= var1) {
         this.f_be_c_49 = var1 - 1;
      }

      C_bp.m_a_255f295(86, this.f_be_c_49);
   }

   private void p_be_h_9b75() {
      this.f_be_a_48a69a2c.removeAllElements();
      this.f_be_b_48a69a2c.removeAllElements();

      for (int var1 = 0; var1 < this.f_be_a_a1fd9827.length; var1++) {
         this.f_be_a_48a69a2c.addElement(this.f_be_a_a1fd9827[var1].getString());
         this.f_be_b_48a69a2c.addElement(this.f_be_b_a1fd9827[var1].getString());
      }

      this.f_be_c_49 = this.f_be_o_aa0ede5b == null ? 0 : this.f_be_o_aa0ede5b.getSelectedIndex();
   }

   public final void itemStateChanged(Item var1) {
      int var2;
      if (this.f_be_a_a1fd9827 != null && (var2 = this.f_be_a_a1fd9827.length) != 1) {
         for (int var3 = 0; var3 < var2; var3++) {
            if (this.f_be_a_a1fd9827[var3] == var1) {
               this.f_be_o_aa0ede5b.set(var3, p_be_a_e96ea081(this.f_be_a_a1fd9827[var3].getString()), null);
               return;
            }
         }
      }

      if (var1 == this.f_be_A_aa0ede5b && this.f_be_A_aa0ede5b.isSelected(2)) {
         C_bp.m_a_255f295(69, this.f_be_A_aa0ede5b.getSelectedIndex());
         new C_ab(null).m_a_9b75();
      }
   }

   protected final void m_c_9b75() {
      this.f_be_a_523beb0a = C_bp.m_a_47921032(3);
      this.f_be_b_5a = C_bp.m_a_134632(130);
      this.f_be_c_5a = C_bp.m_a_134632(129);
      this.f_be_a_5a = C_bp.m_a_134632(136);
      C_bp.m_a_134621(65);
      this.f_be_a_12b74.m_a_9b75();
      this.f_be_a_12b74.c$13462e();
      this.f_be_a_12b74.d$1385ff();
      if (C_ac.m_a_9b79()) {
         C_bf.m_a_d35e089(this.f_be_a_12b74, "h4", C_v.f_v_b_12a1f.m_a_485a59b9(12), 0, true);
      }

      C_bf.m_a_d35e089(this.f_be_a_12b74, "n4", C_v.f_v_b_12a1f.m_a_485a59b9(13), 1, true);
      if (C_bp.m_a_134621(83) == 2) {
         C_bf.m_a_d35e089(this.f_be_a_12b74, "h7", C_v.f_v_b_12a1f.m_a_485a59b9(21), 2, true);
      }

      C_bf.m_a_d35e089(this.f_be_a_12b74, "l4", C_v.f_v_b_12a1f.m_a_485a59b9(14), 3, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "o1", C_v.f_v_b_12a1f.m_a_485a59b9(32), 4, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "m4", C_v.f_v_b_12a1f.m_a_485a59b9(22), 5, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "k4", C_v.f_v_a_12a1f.m_a_485a59b9(7), 12, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "p4", C_v.f_v_b_12a1f.m_a_485a59b9(15), 6, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "i4", C_v.f_v_b_12a1f.m_a_485a59b9(25), 7, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "86", C_v.f_v_b_12a1f.m_a_485a59b9(26), 8, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "D0", C_v.f_v_b_12a1f.m_a_485a59b9(0), 10, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "n1", C_v.f_v_c_12a1f.m_a_485a59b9(C_bp.m_a_134621(94)), 11, true);
      C_bf.m_a_d35e089(this.f_be_a_12b74, "P3", C_v.f_v_b_12a1f.m_a_485a59b9(10), 9, true);
      this.f_be_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private static void p_be_a_85a876da(ChoiceGroup var0, String var1) {
      String[] var3 = C_cf.m_a_639c22ad(var1, '|');

      for (int var2 = 0; var2 < var3.length; var2++) {
         var0.append(C_bs.m_a_e96ea081(var3[var2]), null);
      }
   }

   private static void p_be_a_2f66d769(ChoiceGroup var0, String var1, int var2) {
      p_be_a_85a876da(var0, var1);
      var0.setSelectedIndex(var0.size() - 1, C_bp.m_a_134632(var2));
   }

   public final void commandAction(Command var1, Displayable var2) {
      boolean var3 = false;
      if (C_bf.m_a_cb3ce8a6(this.f_be_b_12b74) && var1 == C_bf.f_bf_i_1570d10e) {
         if (this.f_be_c_12b74.m_a_9b68() == 0) {
            for (int var20 = 0; var20 < this.f_be_a_6dccaaa5.length; var20++) {
               C_bf.m_a_d35e089(this.f_be_c_12b74, this.f_be_a_6dccaaa5[var20], null, this.f_be_a_b4e[var20], true);
            }
         }

         this.f_be_c_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
         this.f_be_c_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
         int var21;
         int var30 = (var21 = this.f_be_b_12b74.m_b_9b68()) < 1024 ? C_bp.m_a_134621(var21) : C_bp.m_a_47921032(40).charAt(var21 - 1024);
         this.f_be_c_12b74.m_a_13462e(var30);
         this.f_be_c_12b74.d$1385ff();
         this.f_be_c_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else if (C_bf.m_a_cb3ce8a6(this.f_be_c_12b74)) {
         if (var1 == C_bf.f_bf_i_1570d10e) {
            int var19;
            if ((var19 = this.f_be_b_12b74.m_b_9b68()) < 1024) {
               C_bp.m_a_255f295(var19, this.f_be_c_12b74.m_b_9b68());
            } else {
               StringBuffer var29;
               (var29 = new StringBuffer(C_bp.m_a_47921032(40))).setCharAt(var19 - 1024, (char)this.f_be_c_12b74.m_b_9b68());
               C_bp.m_a_4f708078(40, new String(var29));
            }
         }

         this.p_be_d_9b75();
      } else if (C_bf.m_a_cb3ce8a6(this.f_be_d_12b74) && var1 == C_bf.f_bf_i_1570d10e) {
         C_bf.m_a_5d527811(this.f_be_d_12b74);
         C_bf.m_a_9c60de72(f_be_a_240984 = new C_bm(2, C_bm.m_d_134621(C_bp.m_a_134621(this.f_be_d_12b74.m_b_9b68()))), false);
         f_be_a_240984.d$1385ff();
         f_be_a_240984.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
         f_be_a_240984.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
         f_be_a_240984.m_a_6f63a2af(this);
         f_be_a_240984.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      } else {
         if (C_bf.m_a_cb3ce8a6(f_be_a_240984)) {
            if (var1 == C_bf.f_bf_i_1570d10e) {
               C_bf.m_a_5d527811(f_be_a_240984);
               C_ch var18 = new C_ch(this.f_be_d_12b74.m_b_9b68(), C_bm.m_b_9b68());
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var18);
               return;
            }

            if (var1 == C_bf.f_bf_c_1570d10e) {
               C_bp.f_bp_a_24088c.m_b_9b75();
               return;
            }
         }

         if (C_bf.m_a_cb3ce8a6(this.f_be_e_12b74) && var1 == C_bf.f_bf_i_1570d10e) {
            C_bp.m_a_255f295(94, this.f_be_e_12b74.m_b_9b68());
            C_bp.m_c_9b75();
            this.m_c_9b75();
            p_be_i_9b75();
         } else if (var1 == C_bf.f_bf_i_1570d10e) {
            this.p_be_j_9b75();
            this.f_be_a_67f46df9.addCommand(this.f_be_a_1570d10e);
            C_bt.m_a_1385ff(true);
            switch (this.f_be_a_12b74.m_b_9b68()) {
               case 0:
                  this.p_be_e_9b75();
                  this.p_be_f_9b75();
                  break;
               case 1:
                  this.f_be_a_a555694c = new TextField(C_bs.m_a_e96ea081("95"), C_bp.m_a_47921032(1), 255, 0);
                  this.f_be_b_a555694c = new TextField(C_bs.m_a_e96ea081("b5"), C_bp.m_a_47921032(2), 5, 2);
                  this.f_be_c_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("z1"), 4);
                  p_be_a_85a876da(this.f_be_c_aa0ede5b, "o5|73");
                  p_be_a_85a876da(this.f_be_c_aa0ede5b, "h7");
                  this.f_be_c_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(83), true);
                  this.f_be_a_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("k3"), 2);
                  p_be_a_2f66d769(this.f_be_a_aa0ede5b, "f7", 128);
                  this.f_be_c_a555694c = new TextField(C_bs.m_a_e96ea081("96"), C_bp.m_a_47921032(13), 4, 2);
                  this.f_be_b_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("A1"), 2);
                  p_be_a_85a876da(this.f_be_b_aa0ede5b, "F3|I0|L0|L4");
                  this.f_be_b_aa0ede5b.setSelectedIndex(0, C_bp.m_a_134632(144));
                  this.f_be_b_aa0ede5b.setSelectedIndex(1, C_bp.m_a_134621(64) != 0);
                  this.f_be_b_aa0ede5b.setSelectedIndex(2, C_bp.m_a_134632(138));
                  this.f_be_b_aa0ede5b.setSelectedIndex(3, C_bp.m_a_134632(149));
                  this.f_be_d_a555694c = new TextField(C_bs.m_a_e96ea081("M4"), String.valueOf(C_bp.m_a_134621(91)), 2, 2);
                  this.f_be_a_67f46df9.append(this.f_be_a_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_b_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_c_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_a_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_c_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_b_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_d_a555694c);
                  break;
               case 2:
                  this.f_be_E_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("i7"), 4);
                  this.f_be_E_aa0ede5b.append(C_bs.m_a_e96ea081("k7"), null);
                  this.f_be_E_aa0ede5b.append(C_bs.m_a_e96ea081("l7"), null);
                  this.f_be_E_aa0ede5b.append(C_bs.m_a_e96ea081("m7"), null);
                  this.f_be_E_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(76), true);
                  this.f_be_x_a555694c = new TextField(C_bs.m_a_e96ea081("n7"), C_bp.m_a_47921032(8), 32, 0);
                  this.f_be_y_a555694c = new TextField(C_bs.m_a_e96ea081("o7"), C_bp.m_a_47921032(9), 5, 2);
                  this.f_be_z_a555694c = new TextField(C_bs.m_a_e96ea081("p7"), C_bp.m_a_47921032(11), 32, 0);
                  this.f_be_A_a555694c = new TextField(C_bs.m_a_e96ea081("q7"), C_bp.m_a_47921032(12), 32, 65536);
                  this.f_be_B_a555694c = new TextField(C_bs.m_a_e96ea081("r7"), C_bp.m_a_47921032(10), 5, 2);
                  this.f_be_a_67f46df9.append(this.f_be_E_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_x_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_y_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_z_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_A_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_B_a555694c);
                  break;
               case 3:
                  if (C_bs.f_bs_a_6dccaaa5.length > 1) {
                     this.f_be_d_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("q3"), 4);

                     for (int var17 = 0; var17 < C_bs.f_bs_a_6dccaaa5.length; var17++) {
                        this.f_be_d_aa0ede5b.append(C_bs.m_a_e96ea081("lang_" + C_bs.f_bs_a_6dccaaa5[var17]), null);
                        if (C_bs.f_bs_a_6dccaaa5[var17].equals(C_bp.m_a_47921032(3))) {
                           this.f_be_d_aa0ede5b.setSelectedIndex(var17, true);
                        }
                     }
                  }

                  this.f_be_e_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("P3"), 2);
                  p_be_a_2f66d769(this.f_be_e_aa0ede5b, "T2", 145);
                  p_be_a_2f66d769(this.f_be_e_aa0ede5b, "26", 143);
                  this.f_be_C_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("H1"), 2);
                  p_be_a_2f66d769(this.f_be_C_aa0ede5b, "i5", 136);
                  p_be_a_2f66d769(this.f_be_C_aa0ede5b, "13", 130);
                  p_be_a_2f66d769(this.f_be_C_aa0ede5b, "23", 134);
                  p_be_a_2f66d769(this.f_be_C_aa0ede5b, "33", 129);
                  p_be_a_2f66d769(this.f_be_C_aa0ede5b, "h5", 173);
                  this.f_be_h_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("F2"), 4);
                  this.f_be_h_aa0ede5b.append(C_bs.m_a_e96ea081("z2"), null);
                  this.f_be_h_aa0ede5b.append(C_bs.m_a_e96ea081("A2"), null);
                  this.f_be_h_aa0ede5b.append(C_bs.m_a_e96ea081("B2"), null);
                  this.f_be_h_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(111), true);
                  this.f_be_i_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("G2"), 4);
                  this.f_be_i_aa0ede5b.append(C_bs.m_a_e96ea081("C2"), null);
                  this.f_be_i_aa0ede5b.append(C_bs.m_a_e96ea081("D2"), null);
                  this.f_be_i_aa0ede5b.append(C_bs.m_a_e96ea081("E2"), null);
                  this.f_be_i_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(112), true);
                  this.f_be_f_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("v5"), 4);
                  this.f_be_f_aa0ede5b.append(C_bs.m_a_e96ea081("w5"), null);
                  this.f_be_f_aa0ede5b.append(C_bs.m_a_e96ea081("x5"), null);
                  this.f_be_f_aa0ede5b.append(C_bs.m_a_e96ea081("y5"), null);
                  this.f_be_f_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(65), true);
                  this.f_be_g_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("k5"), 2);
                  p_be_a_2f66d769(this.f_be_g_aa0ede5b, "e5", 176);
                  p_be_a_2f66d769(this.f_be_g_aa0ede5b, "A4", 177);
                  p_be_a_2f66d769(this.f_be_g_aa0ede5b, "_3", 180);
                  p_be_a_2f66d769(this.f_be_g_aa0ede5b, "l5", 178);
                  p_be_a_2f66d769(this.f_be_g_aa0ede5b, "j5", 179);
                  this.f_be_j_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("81"), 2);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "91", 135);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "a1", 164);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "b1", 165);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "k6", 141);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "c7", 132);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "66", 131);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "j6", 137);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "I7", 142);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "I1", 133);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "16", 174);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "04", 154);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "g0", 181);
                  p_be_a_2f66d769(this.f_be_j_aa0ede5b, "f0", 182);
                  this.f_be_w_a555694c = new TextField(C_bs.m_a_e96ea081("_"), String.valueOf(C_bp.m_a_134621(74)), 4, 2);
                  this.f_be_D_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("0"), 2);
                  p_be_a_2f66d769(this.f_be_D_aa0ede5b, "f7", 140);
                  this.f_be_a_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("d6"), true, 10, C_bp.m_a_134621(100) / 25);
                  this.f_be_b_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("e6"), true, 10, C_bp.m_a_134621(116) / 25);
                  this.f_be_c_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("f6"), true, 10, C_bp.m_a_134621(117) / 25);
                  if (this.f_be_d_aa0ede5b != null) {
                     this.f_be_a_67f46df9.append(this.f_be_d_aa0ede5b);
                  }

                  this.f_be_a_67f46df9.append(this.f_be_C_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_h_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_i_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_f_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_g_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_j_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_D_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_w_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_e_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_a_978a2ca0);
                  this.f_be_a_67f46df9.append(this.f_be_b_978a2ca0);
                  this.f_be_a_67f46df9.append(this.f_be_c_978a2ca0);
                  break;
               case 4:
                  this.m_b_9b75();
                  return;
               case 5:
                  this.p_be_d_9b75();
                  return;
               case 6:
                  this.f_be_s_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("c4"), 4);
                  this.f_be_s_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_s_aa0ede5b.append(C_bs.m_a_e96ea081("U0"), null);
                  this.f_be_s_aa0ede5b.append(C_bs.m_a_e96ea081("p5"), null);
                  this.f_be_g_a555694c = new TextField(null, C_bp.m_a_47921032(5), 32, 0);
                  this.f_be_s_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(68), true);
                  this.f_be_y_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_y_aa0ede5b, "Z0", 156);
                  p_be_a_2f66d769(this.f_be_y_aa0ede5b, "_1", 157);
                  this.f_be_j_a555694c = new TextField(C_bs.m_a_e96ea081("11"), String.valueOf(C_bp.m_a_134621(93)), 3, 2);
                  this.f_be_t_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("b4"), 4);
                  this.f_be_t_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_t_aa0ede5b.append(C_bs.m_a_e96ea081("U0"), null);
                  this.f_be_t_aa0ede5b.append(C_bs.m_a_e96ea081("p5"), null);
                  this.f_be_h_a555694c = new TextField(null, C_bp.m_a_47921032(41), 32, 0);
                  this.f_be_t_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(99), true);
                  this.f_be_z_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_z_aa0ede5b, "_1", 184);
                  this.f_be_k_a555694c = new TextField(C_bs.m_a_e96ea081("11"), String.valueOf(C_bp.m_a_134621(89)), 3, 2);
                  this.f_be_r_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("I3"), 4);
                  this.f_be_r_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_r_aa0ede5b.append(C_bs.m_a_e96ea081("U0"), null);
                  this.f_be_r_aa0ede5b.append(C_bs.m_a_e96ea081("p5"), null);
                  this.f_be_r_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(66), true);
                  this.f_be_d_978a2ca0 = new Gauge(C_bs.m_a_e96ea081("p6"), true, 10, C_bp.m_a_134621(67) / 10);
                  this.f_be_f_a555694c = new TextField(null, C_bp.m_a_47921032(4), 32, 0);
                  this.f_be_i_a555694c = new TextField(null, C_bp.m_a_47921032(16), 32, 0);
                  this.f_be_u_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("h6"), 4);
                  this.f_be_u_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_u_aa0ede5b.append(C_bs.m_a_e96ea081("g6"), null);
                  this.f_be_u_aa0ede5b.append(C_bs.m_a_e96ea081("U0"), null);
                  this.f_be_u_aa0ede5b.append(C_bs.m_a_e96ea081("p5"), null);
                  this.f_be_u_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(88), true);
                  this.f_be_v_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_v_aa0ede5b, "Y1", 146);
                  this.f_be_m_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("n6"), 4);
                  this.f_be_m_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_m_aa0ede5b.append(C_bs.m_a_e96ea081("f7"), null);
                  this.f_be_m_aa0ede5b.append(C_bs.m_a_e96ea081("57"), null);
                  this.f_be_m_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(75), true);
                  this.f_be_l_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("x4"), 4);
                  this.f_be_l_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_l_aa0ede5b.append(C_bs.m_a_e96ea081("G4"), null);
                  this.f_be_l_aa0ede5b.append(C_bs.m_a_e96ea081("H4"), null);
                  this.f_be_l_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(84), true);
                  this.f_be_a_67f46df9.append(this.f_be_d_978a2ca0);
                  this.f_be_a_67f46df9.append(this.f_be_r_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_f_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_m_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_s_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_g_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_y_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_j_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_t_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_h_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_z_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_k_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_u_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_i_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_v_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_l_aa0ede5b);
                  this.f_be_n_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("P3"), 2);
                  p_be_a_2f66d769(this.f_be_n_aa0ede5b, "21", 151);
                  p_be_a_2f66d769(this.f_be_n_aa0ede5b, "L1", 152);
                  p_be_a_2f66d769(this.f_be_n_aa0ede5b, "z5", 168);
                  this.f_be_a_67f46df9.append(this.f_be_n_aa0ede5b);
                  break;
               case 7:
                  this.f_be_s_a555694c = new TextField(C_bs.m_a_e96ea081("K1"), C_cf.m_b_47921032(C_bp.m_a_134621(70)), 6, 0);
                  this.f_be_t_a555694c = new TextField(C_bs.m_a_e96ea081("J1"), C_cf.m_b_47921032(C_bp.m_a_134621(71)), 6, 0);
                  this.f_be_u_a555694c = new TextField(C_bs.m_a_e96ea081("v4"), String.valueOf(C_bp.m_a_134621(72) / 1024), 4, 2);
                  this.f_be_v_a555694c = new TextField(C_bs.m_a_e96ea081("N1"), C_bp.m_a_47921032(6), 4, 0);
                  this.f_be_a_67f46df9.append(this.f_be_s_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_t_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_u_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_v_a555694c);
                  break;
               case 8:
                  boolean var15 = false;
                  this.f_be_p_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("86"), 4);

                  for (int var25 = -12; var25 <= 13; var25++) {
                     this.f_be_p_aa0ede5b.append("GMT" + (var25 < 0 ? "" : "+") + var25 + ":00", null);
                  }

                  this.f_be_p_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(87) + 12, true);
                  int[] var26 = C_cf.m_a_255f4d5(C_cf.a$1385f3());
                  this.f_be_q_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("A3"), 4);
                  int var32 = var26[1];
                  int var16 = var26[2];

                  for (int var27 = 0; var27 < 24; var27++) {
                     this.f_be_q_aa0ede5b.append(var27 + ":" + var32, null);
                  }

                  this.f_be_q_aa0ede5b.setSelectedIndex(var16, true);
                  Calendar var28;
                  (var28 = Calendar.getInstance()).setTime(new Date());
                  this.f_be_b_49 = var28.get(11);
                  this.f_be_a_67f46df9.append(this.f_be_p_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_q_aa0ede5b);
                  break;
               case 9:
                  this.f_be_w_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_w_aa0ede5b, "j1", 155);
                  p_be_a_2f66d769(this.f_be_w_aa0ede5b, "a0", 172);
                  this.f_be_A_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("V0"), 4);
                  this.f_be_A_aa0ede5b.append(C_bs.m_a_e96ea081("Y3"), null);
                  this.f_be_A_aa0ede5b.append(C_bs.m_a_e96ea081("f7"), null);
                  this.f_be_A_aa0ede5b.append(C_bs.m_a_e96ea081("I2"), null);
                  this.f_be_A_aa0ede5b.setSelectedIndex(C_bp.m_a_134621(69), true);
                  this.f_be_o_a555694c = new TextField(null, C_bp.m_a_47921032(34), 255, 0);
                  this.f_be_n_a555694c = new TextField(C_bs.m_a_e96ea081("B5"), C_bp.m_a_47921032(38), 20, 65536);
                  this.f_be_a_67f46df9.append(this.f_be_w_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_A_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_o_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_n_a555694c);
                  break;
               case 10:
                  this.f_be_p_a555694c = new TextField(C_bs.m_a_e96ea081("F0"), C_bp.m_a_47921032(35), 255, 0);
                  this.f_be_q_a555694c = new TextField(C_bs.m_a_e96ea081("G0"), C_bp.m_a_47921032(37), 255, 0);
                  this.f_be_r_a555694c = new TextField(C_bs.m_a_e96ea081("H0"), C_bp.m_a_47921032(36), 255, 0);
                  this.f_be_B_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_B_aa0ede5b, "E0", 162);
                  this.f_be_a_67f46df9.append(this.f_be_p_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_q_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_r_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_B_aa0ede5b);
                  break;
               case 11:
                  this.f_be_e_12b74.m_a_9b75();
                  C_bf.m_a_9c60de72(this.f_be_e_12b74, false);
                  this.f_be_e_12b74.c$13462e();
                  this.f_be_e_12b74.d$1385ff();
                  this.f_be_e_12b74.m_i_9b75();
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "Jimm", C_v.f_v_c_12a1f.m_a_485a59b9(7), 7, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "Miranda", C_v.f_v_c_12a1f.m_a_485a59b9(1), 1, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "QIP 2005a", C_v.f_v_c_12a1f.m_a_485a59b9(0), 0, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "QIP PDA (Symbian)", C_v.f_v_c_12a1f.m_a_485a59b9(12), 12, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "QIP PDA (Windows)", C_v.f_v_c_12a1f.m_a_485a59b9(13), 13, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "QIP Infium", C_v.f_v_c_12a1f.m_a_485a59b9(14), 14, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "ICQ 5.1", C_v.f_v_c_12a1f.m_a_485a59b9(18), 18, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "ICQ 6", C_v.f_v_c_12a1f.m_a_485a59b9(15), 15, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "StICQ", C_v.f_v_c_12a1f.m_a_485a59b9(8), 8, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "VmICQ", C_v.f_v_c_12a1f.m_a_485a59b9(11), 11, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "mChat", C_v.f_v_c_12a1f.m_a_485a59b9(21), 21, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "&RQ", C_v.f_v_c_12a1f.m_a_485a59b9(2), 2, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "R&Q", C_v.f_v_c_12a1f.m_a_485a59b9(3), 3, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "Kopete", C_v.f_v_c_12a1f.m_a_485a59b9(6), 6, false);
                  C_bf.m_a_d35e089(this.f_be_e_12b74, "Mac ICQ", C_v.f_v_c_12a1f.m_a_485a59b9(22), 22, false);
                  this.f_be_e_12b74.m_j_9b75();
                  this.f_be_e_12b74.m_a_13462e(C_bp.m_a_134621(94));
                  this.f_be_e_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
                  this.f_be_e_12b74.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
                  this.f_be_e_12b74.m_a_6f63a2af(this);
                  this.f_be_e_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
                  return;
               case 12:
                  this.f_be_x_aa0ede5b = new ChoiceGroup(null, 2);
                  p_be_a_2f66d769(this.f_be_x_aa0ede5b, "U1", 170);
                  p_be_a_2f66d769(this.f_be_x_aa0ede5b, "41", 169);
                  p_be_a_2f66d769(this.f_be_x_aa0ede5b, "B0", 166);
                  p_be_a_2f66d769(this.f_be_x_aa0ede5b, "Q0", 167);
                  if (C_ac.f_ac_b_5a && C_ac.m_b_9b79()) {
                     p_be_a_2f66d769(this.f_be_x_aa0ede5b, "Web Aware", 160);
                     p_be_a_2f66d769(this.f_be_x_aa0ede5b, "K0", 183);
                  }

                  this.f_be_l_a555694c = new TextField(C_bs.m_a_e96ea081("f3"), C_bp.m_a_47921032(31), 11, 0);
                  this.f_be_m_a555694c = new TextField(C_bs.m_a_e96ea081("g3"), String.valueOf(C_bp.m_a_134621(98)), 5, 2);
                  this.f_be_k_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("M0"), 2);
                  p_be_a_2f66d769(this.f_be_k_aa0ede5b, "N0", 147);
                  p_be_a_2f66d769(this.f_be_k_aa0ede5b, "O0", 153);
                  this.f_be_e_a555694c = new TextField(C_bs.m_a_e96ea081("P0"), String.valueOf(C_bp.m_a_134621(109)), 3, 2);
                  this.f_be_a_67f46df9.append(this.f_be_x_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_l_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_m_a555694c);
                  this.f_be_a_67f46df9.append(this.f_be_k_aa0ede5b);
                  this.f_be_a_67f46df9.append(this.f_be_e_a555694c);
            }

            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_be_a_67f46df9);
         } else if (var1 == C_bf.f_bf_c_1570d10e) {
            if (var2 != this.f_be_a_67f46df9 && !this.f_be_e_12b74.m_b_9b79()) {
               C_bp.f_bp_a_24088c = null;
               C_bn.m_b_9b75();
            } else {
               this.f_be_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            }
         } else if (var1 == this.f_be_a_1570d10e) {
            switch (this.f_be_a_12b74.m_b_9b68()) {
               case 0:
                  this.p_be_h_9b75();
                  this.p_be_g_9b75();
                  break;
               case 1:
                  C_bp.m_a_4f708078(1, this.f_be_a_a555694c.getString());
                  C_bp.m_a_4f708078(2, this.f_be_b_a555694c.getString());
                  C_bp.m_a_255f295(83, this.f_be_c_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_2563266(128, this.f_be_a_aa0ede5b.isSelected(0));
                  C_bp.m_a_4f708078(13, this.f_be_c_a555694c.getString());
                  C_bp.m_a_2563266(144, this.f_be_b_aa0ede5b.isSelected(0));
                  if (this.f_be_b_aa0ede5b.isSelected(1)) {
                     C_bp.m_a_255f295(64, 1);
                  } else {
                     C_bp.m_a_255f295(64, 0);
                  }

                  C_bp.m_a_2563266(138, this.f_be_b_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(149, this.f_be_b_aa0ede5b.isSelected(3));
                  C_bp.m_a_255f295(91, Integer.parseInt(this.f_be_d_a555694c.getString()));
                  break;
               case 2:
                  C_bp.m_a_255f295(76, this.f_be_E_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_4f708078(8, this.f_be_x_a555694c.getString());
                  C_bp.m_a_4f708078(9, this.f_be_y_a555694c.getString());
                  C_bp.m_a_4f708078(11, this.f_be_z_a555694c.getString());
                  C_bp.m_a_4f708078(12, this.f_be_A_a555694c.getString());
                  C_bp.m_a_4f708078(10, this.f_be_B_a555694c.getString());
                  break;
               case 3:
                  if (C_bs.f_bs_a_6dccaaa5.length > 1) {
                     C_bp.m_a_4f708078(3, C_bs.f_bs_a_6dccaaa5[this.f_be_d_aa0ede5b.getSelectedIndex()]);
                  }

                  boolean var13 = false;
                  C_bp.m_a_2563266(145, this.f_be_e_aa0ede5b.isSelected(0));
                  C_bf.m_a_1385ff(false);
                  C_bp.m_a_2563266(143, this.f_be_e_aa0ede5b.isSelected(1));
                  C_bd.m_k_9b75();
                  int var23 = this.f_be_f_aa0ede5b.getSelectedIndex();
                  boolean var31 = this.f_be_C_aa0ede5b.isSelected(0);
                  boolean var14 = this.f_be_C_aa0ede5b.isSelected(1);
                  C_bp.m_a_255f295(111, this.f_be_h_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(112, this.f_be_i_aa0ede5b.getSelectedIndex());
                  C_bp.m_b_9b75();
                  C_bp.m_a_255f295(65, var23);
                  C_bp.m_a_2563266(130, var14);
                  C_bp.m_a_2563266(134, this.f_be_C_aa0ede5b.isSelected(2));
                  boolean var24 = this.f_be_C_aa0ede5b.isSelected(3);
                  C_bp.m_a_2563266(129, var24);
                  C_bp.m_a_2563266(173, this.f_be_C_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(176, this.f_be_g_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(177, this.f_be_g_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(180, this.f_be_g_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(178, this.f_be_g_aa0ede5b.isSelected(3));
                  C_bp.m_a_2563266(179, this.f_be_g_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(135, this.f_be_j_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(164, this.f_be_j_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(165, this.f_be_j_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(141, this.f_be_j_aa0ede5b.isSelected(3));
                  C_bp.m_a_2563266(132, this.f_be_j_aa0ede5b.isSelected(4));
                  C_bp.m_a_2563266(131, this.f_be_j_aa0ede5b.isSelected(5));
                  C_bp.m_a_2563266(137, this.f_be_j_aa0ede5b.isSelected(6));
                  C_bp.m_a_2563266(142, this.f_be_j_aa0ede5b.isSelected(7));
                  C_bp.m_a_2563266(133, this.f_be_j_aa0ede5b.isSelected(8));
                  C_bp.m_a_2563266(174, this.f_be_j_aa0ede5b.isSelected(9));
                  C_bp.m_a_2563266(154, this.f_be_j_aa0ede5b.isSelected(10));
                  C_bp.m_a_2563266(181, this.f_be_j_aa0ede5b.isSelected(11));
                  C_bp.m_a_2563266(182, this.f_be_j_aa0ede5b.isSelected(12));
                  C_bp.m_a_2563266(136, var31);
                  C_v.m_a_1385ff(var31 != this.f_be_a_5a || var14 != this.f_be_b_5a || var24 != this.f_be_c_5a);
                  C_bp.m_a_255f295(100, this.f_be_a_978a2ca0.getValue() * 25);
                  C_bp.m_a_255f295(116, this.f_be_b_978a2ca0.getValue() * 25);
                  C_bp.m_a_255f295(117, this.f_be_c_978a2ca0.getValue() * 25);
                  C_bp.m_a_255f295(74, Integer.parseInt(this.f_be_w_a555694c.getString()));
                  C_bp.m_a_2563266(140, this.f_be_D_aa0ede5b.isSelected(0));
                  if (!this.f_be_a_523beb0a.equals(C_bp.m_a_47921032(3))) {
                     C_bp.m_a_2563266(148, true);
                  }
               case 4:
               case 5:
               case 11:
               default:
                  break;
               case 6:
                  C_bp.m_a_255f295(66, this.f_be_r_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(75, this.f_be_m_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(68, this.f_be_s_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(99, this.f_be_t_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(88, this.f_be_u_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_255f295(67, this.f_be_d_978a2ca0.getValue() * 10);
                  C_bp.m_a_4f708078(4, this.f_be_f_a555694c.getString());
                  C_bp.m_a_4f708078(5, this.f_be_g_a555694c.getString());
                  C_bp.m_a_4f708078(41, this.f_be_h_a555694c.getString());
                  C_bp.m_a_4f708078(16, this.f_be_i_a555694c.getString());
                  C_bp.m_a_2563266(156, this.f_be_y_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(157, this.f_be_y_aa0ede5b.isSelected(1));
                  C_bp.m_a_255f295(93, Integer.parseInt(this.f_be_j_a555694c.getString()));
                  C_bp.m_a_2563266(184, this.f_be_z_aa0ede5b.isSelected(0));
                  C_bp.m_a_255f295(89, Integer.parseInt(this.f_be_k_a555694c.getString()));
                  C_bp.m_a_2563266(146, this.f_be_v_aa0ede5b.isSelected(0));
                  C_bp.m_a_255f295(84, this.f_be_l_aa0ede5b.getSelectedIndex());
                  C_bp.m_a_2563266(151, this.f_be_n_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(152, this.f_be_n_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(168, this.f_be_n_aa0ede5b.isSelected(2));
                  break;
               case 7:
                  C_bp.m_a_255f295(70, C_cf.m_a_aad3b1f2(this.f_be_s_a555694c.getString()));
                  this.f_be_s_a555694c.setString(C_cf.m_b_47921032(C_bp.m_a_134621(70)));
                  C_bp.m_a_255f295(71, C_cf.m_a_aad3b1f2(this.f_be_t_a555694c.getString()));
                  this.f_be_t_a555694c.setString(C_cf.m_b_47921032(C_bp.m_a_134621(71)));
                  C_bp.m_a_255f295(72, Integer.parseInt(this.f_be_u_a555694c.getString()) << 10);
                  C_bp.m_a_4f708078(6, this.f_be_v_a555694c.getString());
                  break;
               case 8:
                  int var10 = this.f_be_p_aa0ede5b.getSelectedIndex() - 12;
                  C_bp.m_a_255f295(87, var10);
                  int var11;
                  if ((var11 = this.f_be_q_aa0ede5b.getSelectedIndex() - var10) < 0) {
                     var11 += 24;
                  }

                  if (var11 >= 24) {
                     var11 -= 24;
                  }

                  int var12 = var11 - this.f_be_b_49;

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
                  C_bp.m_a_2563266(155, this.f_be_w_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(172, this.f_be_w_aa0ede5b.isSelected(1));
                  int var9 = this.f_be_A_aa0ede5b.getSelectedIndex();
                  C_bp.m_a_255f295(69, var9);
                  switch (var9) {
                     case 0:
                        C_bp.m_a_4f708078(34, "/back.png");
                        C_bd.m_a_b329a056(null, true);
                        break;
                     case 1:
                        C_bp.m_a_4f708078(34, this.f_be_o_a555694c.getString());
                        C_bd.m_a_b329a056(null, false);
                  }

                  C_bp.m_a_4f708078(38, this.f_be_n_a555694c.getString());
                  break;
               case 10:
                  C_bp.m_a_4f708078(35, this.f_be_p_a555694c.getString());
                  C_bp.m_a_4f708078(37, this.f_be_q_a555694c.getString());
                  C_bp.m_a_4f708078(36, this.f_be_r_a555694c.getString());
                  C_bp.m_a_2563266(162, this.f_be_B_aa0ede5b.isSelected(0));
                  break;
               case 12:
                  boolean var7 = false;
                  C_bp.m_a_2563266(170, this.f_be_x_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(169, this.f_be_x_aa0ede5b.isSelected(1));
                  C_bp.m_a_2563266(166, this.f_be_x_aa0ede5b.isSelected(2));
                  C_bp.m_a_2563266(167, this.f_be_x_aa0ede5b.isSelected(3));
                  if (C_ac.f_ac_b_5a && C_ac.m_b_9b79()) {
                     C_bp.m_a_2563266(160, this.f_be_x_aa0ede5b.isSelected(4));
                     C_bp.m_a_2563266(183, this.f_be_x_aa0ede5b.isSelected(5));
                  }

                  C_bp.m_a_4f708078(31, this.f_be_l_a555694c.getString());
                  C_bp.m_a_255f295(98, Integer.parseInt(this.f_be_m_a555694c.getString()));
                  C_bp.m_a_2563266(147, this.f_be_k_aa0ede5b.isSelected(0));
                  C_bp.m_a_2563266(153, this.f_be_k_aa0ede5b.isSelected(1));
                  C_bp.m_a_255f295(109, Integer.parseInt(this.f_be_e_a555694c.getString()));
                  C_ck.f_ck_a_49 = C_bp.m_a_134621(109) * 60000;
                  var3 = true;
            }

            if (this.f_be_d_12b74.m_b_9b79()) {
               C_bf.m_a_1385ff(true);
            }

            C_bp.m_c_9b75();
            C_bf.m_a_9c60de72(this.f_be_a_12b74, false);
            C_bp.m_a_9b75();
            if (var3) {
               C_ac.m_f_9b75();
               p_be_i_9b75();
            }

            this.m_c_9b75();
         } else if (var1 == this.f_be_b_1570d10e) {
            C_bp.m_a_2563266(175, true);
            this.p_be_d_9b75();
         } else if (var1 == this.f_be_c_1570d10e) {
            C_bp.m_a_2563266(175, false);
            this.p_be_d_9b75();
         } else if (var1 == this.f_be_d_1570d10e) {
            this.p_be_h_9b75();
            this.f_be_a_48a69a2c.addElement(C_bp.f_bp_a_523beb0a);
            this.f_be_b_48a69a2c.addElement(C_bp.f_bp_a_523beb0a);
            this.p_be_j_9b75();
            this.p_be_f_9b75();
         } else if (var1 != this.f_be_e_1570d10e) {
            if (C_bf.m_a_48817c53(var1, 1) == 1) {
               this.p_be_h_9b75();
               int var6 = C_bf.m_b_9b68();
               this.f_be_a_48a69a2c.removeElementAt(var6);
               this.f_be_b_48a69a2c.removeElementAt(var6);
               this.p_be_j_9b75();
               this.p_be_f_9b75();
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_be_a_67f46df9);
            }
         } else {
            this.p_be_h_9b75();
            int var5;
            String[] var22 = new String[var5 = this.f_be_a_48a69a2c.size()];

            for (int var4 = 0; var4 < var5; var4++) {
               var22[var4] = p_be_a_e96ea081((String)this.f_be_a_48a69a2c.elementAt(var4));
            }

            C_bf.m_a_1cc7afeb("02", var22, this, 1, false);
         }
      }
   }

   private static void p_be_i_9b75() {
      if (C_ac.m_b_9b79()) {
         try {
            C_al.m_a_9b75();
            C_al.m_a_13462e(C_ac.m_d_9b68() | (int)C_bp.a$134622());
            return;
         } catch (Exception var0) {
         }
      }
   }

   private void p_be_j_9b75() {
      this.f_be_a_67f46df9.removeCommand(this.f_be_d_1570d10e);
      this.f_be_a_67f46df9.removeCommand(this.f_be_e_1570d10e);
      this.f_be_a_67f46df9.deleteAll();
   }
}
