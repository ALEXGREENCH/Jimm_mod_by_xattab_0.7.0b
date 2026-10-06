package recovered;

/** 0.6 source correspondence (inferred): jimm.EditInfo. Release class: an. */

import java.io.ByteArrayOutputStream;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

public final class C_an extends Form implements Runnable, CommandListener {
   TextField f_an_a_a555694c = new TextField(C_bt.m_a_e96ea081("U3"), null, 20, 0);
   TextField f_an_b_a555694c = new TextField(C_bt.m_a_e96ea081("u2"), null, 20, 0);
   TextField f_an_c_a555694c = new TextField(C_bt.m_a_e96ea081("o3"), null, 20, 0);
   TextField f_an_d_a555694c = new TextField(C_bt.m_a_e96ea081("42"), null, 50, 1);
   TextField f_an_e_a555694c = new TextField(C_bt.m_a_e96ea081("T0"), null, 15, 0);
   TextField f_an_f_a555694c = new TextField(C_bt.m_a_e96ea081("13"), null, 70, 0);
   TextField f_an_g_a555694c = new TextField(C_bt.m_a_e96ea081("24"), null, 600, 0);
   TextField f_an_h_a555694c = new TextField(C_bt.m_a_e96ea081("d1"), null, 50, 0);
   ChoiceGroup f_an_a_aa0ede5b = new ChoiceGroup(C_bt.m_a_e96ea081("R2"), 4);
   private ChoiceGroup f_an_b_aa0ede5b = new ChoiceGroup(C_bt.m_a_e96ea081("b3"), 2);
   private static ChoiceGroup f_an_c_aa0ede5b;
   private static ChoiceGroup f_an_d_aa0ede5b;
   private static ChoiceGroup f_an_e_aa0ede5b;
   private static ChoiceGroup f_an_f_aa0ede5b;
   private TextField f_an_i_a555694c;
   private TextField f_an_j_a555694c;
   private TextField f_an_k_a555694c;
   private TextField f_an_l_a555694c;
   private Command f_an_a_1570d10e = new Command(C_bt.m_a_e96ea081("5"), 2, 0);
   private Command f_an_b_1570d10e = new Command(C_bt.m_a_e96ea081("7"), 4, 1);
   private Displayable f_an_a_4d585e5d;
   static String[] f_an_a_6dccaaa5;
   static C_an f_an_a_2405e2;

   C_an(Displayable var1) {
      super(C_bt.m_a_e96ea081("32"));
      this.f_an_a_4d585e5d = var1;
      f_an_c_aa0ede5b = new ChoiceGroup(null, 4);
      f_an_d_aa0ede5b = new ChoiceGroup(null, 4);
      f_an_e_aa0ede5b = new ChoiceGroup(null, 4);
      f_an_f_aa0ede5b = new ChoiceGroup(null, 4);
      this.f_an_i_a555694c = new TextField(null, null, 60, 0);
      this.f_an_j_a555694c = new TextField(null, null, 60, 0);
      this.f_an_k_a555694c = new TextField(null, null, 60, 0);
      this.f_an_l_a555694c = new TextField(null, null, 60, 0);
      this.f_an_a_aa0ede5b.append("---", null);
      this.f_an_a_aa0ede5b.append(C_bt.m_a_e96ea081("o2"), null);
      this.f_an_a_aa0ede5b.append(C_bt.m_a_e96ea081("A3"), null);
      this.append(this.f_an_a_a555694c);
      this.append(this.f_an_b_a555694c);
      this.append(this.f_an_c_a555694c);
      this.append(this.f_an_a_aa0ede5b);
      this.append(this.f_an_d_a555694c);
      this.append(this.f_an_e_a555694c);
      this.append(this.f_an_f_a555694c);
      this.append(this.f_an_g_a555694c);
      this.append(this.f_an_h_a555694c);
      this.addCommand(this.f_an_b_1570d10e);
      this.addCommand(this.f_an_a_1570d10e);
      this.setCommandListener(this);
   }

   public final void run() {
      for (int var1 = 0; var1 < 51; var1++) {
         f_an_c_aa0ede5b.append(C_bt.m_a_e96ea081(C_bh.m_a_47921032(var1)), null);
         f_an_d_aa0ede5b.append(C_bt.m_a_e96ea081(C_bh.m_a_47921032(var1)), null);
         f_an_e_aa0ede5b.append(C_bt.m_a_e96ea081(C_bh.m_a_47921032(var1)), null);
         f_an_f_aa0ede5b.append(C_bt.m_a_e96ea081(C_bh.m_a_47921032(var1)), null);

         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
         }
      }

      f_an_c_aa0ede5b.setSelectedIndex(C_bh.m_a_134621(C_bh.f_bh_a_b4e[0]), true);
      f_an_d_aa0ede5b.setSelectedIndex(C_bh.m_a_134621(C_bh.f_bh_a_b4e[1]), true);
      f_an_e_aa0ede5b.setSelectedIndex(C_bh.m_a_134621(C_bh.f_bh_a_b4e[2]), true);
      f_an_f_aa0ede5b.setSelectedIndex(C_bh.m_a_134621(C_bh.f_bh_a_b4e[3]), true);
      f_an_a_2405e2.f_an_i_a555694c.setString(f_an_a_6dccaaa5[44] != null ? f_an_a_6dccaaa5[44].substring(1) : null);
      f_an_a_2405e2.f_an_j_a555694c.setString(f_an_a_6dccaaa5[45] != null ? f_an_a_6dccaaa5[45].substring(1) : null);
      f_an_a_2405e2.f_an_k_a555694c.setString(f_an_a_6dccaaa5[46] != null ? f_an_a_6dccaaa5[46].substring(1) : null);
      f_an_a_2405e2.f_an_l_a555694c.setString(f_an_a_6dccaaa5[47] != null ? f_an_a_6dccaaa5[47].substring(1) : null);
      this.append(this.f_an_b_aa0ede5b);
      this.append(f_an_c_aa0ede5b);
      this.append(this.f_an_i_a555694c);
      this.append(f_an_d_aa0ede5b);
      this.append(this.f_an_j_a555694c);
      this.append(f_an_e_aa0ede5b);
      this.append(this.f_an_k_a555694c);
      this.append(f_an_f_aa0ede5b);
      this.append(this.f_an_l_a555694c);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_an_a_1570d10e) {
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_an_a_4d585e5d);
      }

      if (var1 == this.f_an_b_1570d10e) {
         f_an_a_6dccaaa5[1] = this.f_an_a_a555694c.getString();
         f_an_a_6dccaaa5[3] = this.f_an_d_a555694c.getString();
         f_an_a_6dccaaa5[13] = this.f_an_e_a555694c.getString();
         f_an_a_6dccaaa5[38] = this.f_an_b_a555694c.getString();
         f_an_a_6dccaaa5[39] = this.f_an_c_a555694c.getString();
         f_an_a_6dccaaa5[12] = this.f_an_f_a555694c.getString();
         f_an_a_6dccaaa5[22] = this.f_an_g_a555694c.getString();
         f_an_a_6dccaaa5[4] = this.f_an_h_a555694c.getString();
         f_an_a_6dccaaa5[11] = C_cf.m_c_47921032(this.f_an_a_aa0ede5b.getSelectedIndex());
         f_an_a_6dccaaa5[44] = this.f_an_i_a555694c.getString();
         f_an_a_6dccaaa5[45] = this.f_an_j_a555694c.getString();
         f_an_a_6dccaaa5[46] = this.f_an_k_a555694c.getString();
         f_an_a_6dccaaa5[47] = this.f_an_l_a555694c.getString();
         C_bh.f_bh_a_b4e[0] = C_bh.m_b_134621(f_an_c_aa0ede5b.getSelectedIndex());
         C_bh.f_bh_a_b4e[1] = C_bh.m_b_134621(f_an_d_aa0ede5b.getSelectedIndex());
         C_bh.f_bh_a_b4e[2] = C_bh.m_b_134621(f_an_e_aa0ede5b.getSelectedIndex());
         C_bh.f_bh_a_b4e[3] = C_bh.m_b_134621(f_an_f_aa0ede5b.getSelectedIndex());
         ByteArrayOutputStream var4;
         C_cf.m_a_559c4327(var4 = new ByteArrayOutputStream(), 3130, false);
         C_cf.m_a_2a17548d(320, var4, f_an_a_6dccaaa5[38], false);
         C_cj var5 = new C_cj(f_an_a_6dccaaa5);

         try {
            C_ac.m_a_cb3b8b85(var5);
         } catch (C_ar var3) {
            C_ar.m_a_aef55300(var3);
            if (var3.f_ar_a_5a) {
               return;
            }
         }

         C_ac.f_ac_a_523beb0a = this.f_an_a_a555694c.getString().length() > 0 ? this.f_an_a_a555694c.getString() : C_bt.m_a_e96ea081("D3");
         C_cp.m_a_335e07a5("15", var5, false);
      }
   }
}
