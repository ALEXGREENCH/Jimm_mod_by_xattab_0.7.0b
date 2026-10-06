package recovered;

import java.io.ByteArrayOutputStream;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

public final class C_am extends Form implements Runnable, CommandListener {
   TextField f_am_a_a555694c = new TextField(C_bs.m_a_e96ea081("S3"), null, 20, 0);
   TextField f_am_b_a555694c = new TextField(C_bs.m_a_e96ea081("s2"), null, 20, 0);
   TextField f_am_c_a555694c = new TextField(C_bs.m_a_e96ea081("m3"), null, 20, 0);
   TextField f_am_d_a555694c = new TextField(C_bs.m_a_e96ea081("22"), null, 50, 1);
   TextField f_am_e_a555694c = new TextField(C_bs.m_a_e96ea081("R0"), null, 15, 0);
   TextField f_am_f_a555694c = new TextField(C_bs.m_a_e96ea081("_3"), null, 70, 0);
   TextField f_am_g_a555694c = new TextField(C_bs.m_a_e96ea081("04"), null, 600, 0);
   TextField f_am_h_a555694c = new TextField(C_bs.m_a_e96ea081("b1"), null, 50, 0);
   ChoiceGroup f_am_a_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("P2"), 4);
   private ChoiceGroup f_am_b_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("93"), 2);
   private static ChoiceGroup f_am_c_aa0ede5b;
   private static ChoiceGroup f_am_d_aa0ede5b;
   private static ChoiceGroup f_am_e_aa0ede5b;
   private static ChoiceGroup f_am_f_aa0ede5b;
   private TextField f_am_i_a555694c;
   private TextField f_am_j_a555694c;
   private TextField f_am_k_a555694c;
   private TextField f_am_l_a555694c;
   private Command f_am_a_1570d10e = new Command(C_bs.m_a_e96ea081("3"), 2, 0);
   private Command f_am_b_1570d10e = new Command(C_bs.m_a_e96ea081("5"), 4, 1);
   private Displayable f_am_a_4d585e5d;
   static String[] f_am_a_6dccaaa5;
   static C_am f_am_a_2405c3;

   C_am(Displayable var1) {
      super(C_bs.m_a_e96ea081("12"));
      this.f_am_a_4d585e5d = var1;
      f_am_c_aa0ede5b = new ChoiceGroup(null, 4);
      f_am_d_aa0ede5b = new ChoiceGroup(null, 4);
      f_am_e_aa0ede5b = new ChoiceGroup(null, 4);
      f_am_f_aa0ede5b = new ChoiceGroup(null, 4);
      this.f_am_i_a555694c = new TextField(null, null, 60, 0);
      this.f_am_j_a555694c = new TextField(null, null, 60, 0);
      this.f_am_k_a555694c = new TextField(null, null, 60, 0);
      this.f_am_l_a555694c = new TextField(null, null, 60, 0);
      this.f_am_a_aa0ede5b.append("---", null);
      this.f_am_a_aa0ede5b.append(C_bs.m_a_e96ea081("m2"), null);
      this.f_am_a_aa0ede5b.append(C_bs.m_a_e96ea081("y3"), null);
      this.append(this.f_am_a_a555694c);
      this.append(this.f_am_b_a555694c);
      this.append(this.f_am_c_a555694c);
      this.append(this.f_am_a_aa0ede5b);
      this.append(this.f_am_d_a555694c);
      this.append(this.f_am_e_a555694c);
      this.append(this.f_am_f_a555694c);
      this.append(this.f_am_g_a555694c);
      this.append(this.f_am_h_a555694c);
      this.addCommand(this.f_am_b_1570d10e);
      this.addCommand(this.f_am_a_1570d10e);
      this.setCommandListener(this);
   }

   public final void run() {
      for (int var1 = 0; var1 < 51; var1++) {
         f_am_c_aa0ede5b.append(C_bs.m_a_e96ea081(C_bi.m_a_47921032(var1)), null);
         f_am_d_aa0ede5b.append(C_bs.m_a_e96ea081(C_bi.m_a_47921032(var1)), null);
         f_am_e_aa0ede5b.append(C_bs.m_a_e96ea081(C_bi.m_a_47921032(var1)), null);
         f_am_f_aa0ede5b.append(C_bs.m_a_e96ea081(C_bi.m_a_47921032(var1)), null);

         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
         }
      }

      f_am_c_aa0ede5b.setSelectedIndex(C_bi.m_a_134621(C_bi.f_bi_a_b4e[0]), true);
      f_am_d_aa0ede5b.setSelectedIndex(C_bi.m_a_134621(C_bi.f_bi_a_b4e[1]), true);
      f_am_e_aa0ede5b.setSelectedIndex(C_bi.m_a_134621(C_bi.f_bi_a_b4e[2]), true);
      f_am_f_aa0ede5b.setSelectedIndex(C_bi.m_a_134621(C_bi.f_bi_a_b4e[3]), true);
      f_am_a_2405c3.f_am_i_a555694c.setString(f_am_a_6dccaaa5[44] != null ? f_am_a_6dccaaa5[44].substring(1) : null);
      f_am_a_2405c3.f_am_j_a555694c.setString(f_am_a_6dccaaa5[45] != null ? f_am_a_6dccaaa5[45].substring(1) : null);
      f_am_a_2405c3.f_am_k_a555694c.setString(f_am_a_6dccaaa5[46] != null ? f_am_a_6dccaaa5[46].substring(1) : null);
      f_am_a_2405c3.f_am_l_a555694c.setString(f_am_a_6dccaaa5[47] != null ? f_am_a_6dccaaa5[47].substring(1) : null);
      this.append(this.f_am_b_aa0ede5b);
      this.append(f_am_c_aa0ede5b);
      this.append(this.f_am_i_a555694c);
      this.append(f_am_d_aa0ede5b);
      this.append(this.f_am_j_a555694c);
      this.append(f_am_e_aa0ede5b);
      this.append(this.f_am_k_a555694c);
      this.append(f_am_f_aa0ede5b);
      this.append(this.f_am_l_a555694c);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_am_a_1570d10e) {
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_am_a_4d585e5d);
      }

      if (var1 == this.f_am_b_1570d10e) {
         f_am_a_6dccaaa5[1] = this.f_am_a_a555694c.getString();
         f_am_a_6dccaaa5[3] = this.f_am_d_a555694c.getString();
         f_am_a_6dccaaa5[13] = this.f_am_e_a555694c.getString();
         f_am_a_6dccaaa5[38] = this.f_am_b_a555694c.getString();
         f_am_a_6dccaaa5[39] = this.f_am_c_a555694c.getString();
         f_am_a_6dccaaa5[12] = this.f_am_f_a555694c.getString();
         f_am_a_6dccaaa5[22] = this.f_am_g_a555694c.getString();
         f_am_a_6dccaaa5[4] = this.f_am_h_a555694c.getString();
         f_am_a_6dccaaa5[11] = C_ce.m_c_47921032(this.f_am_a_aa0ede5b.getSelectedIndex());
         f_am_a_6dccaaa5[44] = this.f_am_i_a555694c.getString();
         f_am_a_6dccaaa5[45] = this.f_am_j_a555694c.getString();
         f_am_a_6dccaaa5[46] = this.f_am_k_a555694c.getString();
         f_am_a_6dccaaa5[47] = this.f_am_l_a555694c.getString();
         C_bi.f_bi_a_b4e[0] = C_bi.m_b_134621(f_am_c_aa0ede5b.getSelectedIndex());
         C_bi.f_bi_a_b4e[1] = C_bi.m_b_134621(f_am_d_aa0ede5b.getSelectedIndex());
         C_bi.f_bi_a_b4e[2] = C_bi.m_b_134621(f_am_e_aa0ede5b.getSelectedIndex());
         C_bi.f_bi_a_b4e[3] = C_bi.m_b_134621(f_am_f_aa0ede5b.getSelectedIndex());
         ByteArrayOutputStream var4;
         C_ce.m_a_559c4327(var4 = new ByteArrayOutputStream(), 3130, false);
         C_ce.m_a_2a17548d(320, var4, f_am_a_6dccaaa5[38], false);
         C_ch var5 = new C_ch(f_am_a_6dccaaa5);

         try {
            C_ac.m_a_cb38d14b(var5);
         } catch (C_aq var3) {
            C_aq.m_a_481c933f(var3);
            if (var3.f_aq_a_5a) {
               return;
            }
         }

         C_ac.f_ac_a_523beb0a = this.f_am_a_a555694c.getString().length() > 0 ? this.f_am_a_a555694c.getString() : C_bs.m_a_e96ea081("B3");
         C_cn.m_a_33097a9f("_5", var5, false);
      }
   }
}
