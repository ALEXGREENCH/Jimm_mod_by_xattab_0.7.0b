package recovered;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

public final class C_cj implements C_ax, CommandListener {
   private Command f_cj_a_1570d10e;
   private Command f_cj_b_1570d10e;
   private Command f_cj_c_1570d10e;
   private Command f_cj_d_1570d10e;
   private Command f_cj_e_1570d10e;
   private Command f_cj_f_1570d10e;
   private Command f_cj_g_1570d10e;
   private Form f_cj_a_67f46df9;
   private C_s f_cj_a_12b74;
   private List f_cj_a_6846455f;
   private TextField f_cj_a_a555694c;
   private TextField f_cj_b_a555694c;
   private TextField f_cj_c_a555694c;
   private TextField f_cj_d_a555694c;
   private TextField f_cj_e_a555694c;
   private TextField f_cj_f_a555694c;
   private TextField f_cj_g_a555694c;
   private ChoiceGroup f_cj_a_aa0ede5b;
   private ChoiceGroup f_cj_b_aa0ede5b;
   private ChoiceGroup f_cj_c_aa0ede5b;
   private int f_cj_a_49;
   private final C_ce f_cj_a_240c4d;

   public C_cj(C_ce var1) {
      this.f_cj_a_240c4d = var1;
      this.f_cj_b_1570d10e = new Command(C_bs.m_a_e96ea081("m6"), 4, 1);
      this.f_cj_a_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
      this.f_cj_c_1570d10e = new Command(C_bs.m_a_e96ea081("s0"), 8, 1);
      this.f_cj_e_1570d10e = new Command(C_bs.m_a_e96ea081("f"), 8, 3);
      this.f_cj_d_1570d10e = new Command(C_bs.m_a_e96ea081("z4"), 8, 4);
      this.f_cj_f_1570d10e = new Command(C_bs.m_a_e96ea081("75"), 8, 6);
      this.f_cj_g_1570d10e = new Command(C_bs.m_a_e96ea081("b3"), 8, 7);
      this.f_cj_a_67f46df9 = new Form(C_bs.m_a_e96ea081("d"));
      this.f_cj_a_a555694c = new TextField(C_bs.m_a_e96ea081("i6"), "", 32, 2);
      this.f_cj_b_a555694c = new TextField(C_bs.m_a_e96ea081("X3"), "", 32, 0);
      this.f_cj_c_a555694c = new TextField(C_bs.m_a_e96ea081("x2"), "", 32, 0);
      this.f_cj_d_a555694c = new TextField(C_bs.m_a_e96ea081("r3"), "", 32, 0);
      this.f_cj_e_a555694c = new TextField(C_bs.m_a_e96ea081("72"), "", 32, 1);
      this.f_cj_f_a555694c = new TextField(C_bs.m_a_e96ea081("g1"), "", 32, 0);
      this.f_cj_g_a555694c = new TextField(C_bs.m_a_e96ea081("p3"), "", 32, 0);
      this.f_cj_a_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("y0"), 4, C_cf.m_a_639c22ad("---|13-17|18-22|23-29|30-39|40-49|50-59|> 60", '|'), null);
      this.f_cj_b_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("U2"), 4);
      this.f_cj_b_aa0ede5b.append(C_bs.m_a_e96ea081("s2"), null);
      this.f_cj_b_aa0ede5b.append(C_bs.m_a_e96ea081("r2"), null);
      this.f_cj_b_aa0ede5b.append(C_bs.m_a_e96ea081("D3"), null);
      this.f_cj_c_aa0ede5b = new ChoiceGroup("", 2);
      this.f_cj_c_aa0ede5b.append(C_bs.m_a_e96ea081("e4"), null);
      this.f_cj_a_67f46df9.append(this.f_cj_c_aa0ede5b);
      this.f_cj_a_67f46df9.append(this.f_cj_a_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_b_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_c_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_d_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_f_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_b_aa0ede5b);
      this.f_cj_a_67f46df9.append(this.f_cj_e_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_g_a555694c);
      this.f_cj_a_67f46df9.append(this.f_cj_a_aa0ede5b);
      this.f_cj_a_67f46df9.setCommandListener(this);
      this.f_cj_a_12b74 = new C_s(null);
      this.f_cj_a_12b74.m_a_cb37e88d(this);
      if (var1.f_ce_a_5a) {
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_c_1570d10e, C_bd.f_bd_e_49);
      } else {
         this.f_cj_a_12b74.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_e_49);
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_d_1570d10e, C_bd.f_bd_g_49);
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_e_1570d10e, C_bd.f_bd_g_49);
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_c_1570d10e, C_bd.f_bd_g_49);
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_f_1570d10e, C_bd.f_bd_g_49);
         this.f_cj_a_12b74.m_a_48817c60(this.f_cj_g_1570d10e, C_bd.f_bd_g_49);
      }

      this.f_cj_a_12b74.c$13462e();
      C_bf.m_a_9c60de72(this.f_cj_a_12b74, false);
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            int var2 = this.f_cj_a_49;
            this.f_cj_a_12b74.m_a_9b75();
            if (this.f_cj_a_240c4d.f_ce_a_48a69a2c.size() > 0) {
               if (this.f_cj_a_240c4d.f_ce_a_48a69a2c.size() == 1) {
                  this.f_cj_a_12b74.m_a_8eb9d703(this.f_cj_e_1570d10e);
                  this.f_cj_a_12b74.m_a_8eb9d703(this.f_cj_d_1570d10e);
               }

               this.f_cj_a_12b74.m_i_9b75();
               C_bf.m_a_2f0d6064(this.f_cj_a_240c4d.m_a_233b2a7d(var2), this.f_cj_a_12b74);
               this.f_cj_a_12b74
                  .m_c_aad3b1ff(C_bs.m_a_e96ea081("35") + " " + Integer.toString(var2 + 1) + "/" + Integer.toString(this.f_cj_a_240c4d.f_ce_a_48a69a2c.size()));
               this.f_cj_a_12b74.m_j_9b75();
            } else {
               this.f_cj_a_12b74.m_i_9b75();
               this.f_cj_a_12b74.m_c_aad3b1ff(C_bs.m_a_e96ea081("35") + " 0/0");
               this.f_cj_a_12b74.m_a_68a79fe2(C_bs.m_a_e96ea081("14") + ": ", 0, 1, -1);
               this.f_cj_a_12b74.m_j_9b75();
            }

            this.f_cj_a_12b74.m_a_48817c60(this.f_cj_a_1570d10e, C_bd.f_bd_f_49);
            this.f_cj_a_12b74.m_a_6f63a2af(this);
            this.f_cj_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            return;
         case 2:
            this.f_cj_a_67f46df9.addCommand(this.f_cj_b_1570d10e);
            this.f_cj_a_67f46df9.addCommand(this.f_cj_a_1570d10e);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_cj_a_67f46df9);
            return;
         case 3:
            this.f_cj_a_67f46df9.addCommand(this.f_cj_b_1570d10e);
            this.f_cj_a_67f46df9.addCommand(this.f_cj_a_1570d10e);
            Alert var3;
            (var3 = new Alert(null, C_bs.m_a_e96ea081("14"), null, null)).setTimeout(-2);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var3, this.f_cj_a_67f46df9);
      }
   }

   private void p_cj_a_1385ff(boolean var1) {
      if (var1) {
         this.f_cj_a_49 = (this.f_cj_a_49 + 1) % this.f_cj_a_240c4d.f_ce_a_48a69a2c.size();
      } else if (this.f_cj_a_49 == 0) {
         this.f_cj_a_49 = this.f_cj_a_240c4d.f_ce_a_48a69a2c.size() - 1;
      } else {
         this.f_cj_a_49 = (this.f_cj_a_49 - 1) % this.f_cj_a_240c4d.f_ce_a_48a69a2c.size();
      }

      this.m_a_13462e(1);
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
      if (var3 == 3) {
         switch (C_bd.m_b_134621(var2)) {
            case 2:
               this.p_cj_a_1385ff(false);
               return;
            case 5:
               this.p_cj_a_1385ff(true);
         }
      }
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_cj_a_1570d10e) {
         if (C_bf.m_a_cb3ce8a6(this.f_cj_a_12b74) && !this.f_cj_a_240c4d.f_ce_a_5a) {
            this.m_a_13462e(2);
            C_bt.m_a_1385ff(true);
         } else if (var2 == this.f_cj_a_67f46df9) {
            this.f_cj_a_67f46df9 = null;
            C_bn.m_b_9b75();
         } else {
            if (var2 != this.f_cj_a_6846455f) {
               this.f_cj_a_67f46df9 = null;
            }

            C_bf.m_a_9b75();
         }
      } else if (var1 == this.f_cj_b_1570d10e) {
         this.f_cj_a_49 = 0;
         String[] var9;
         (var9 = new String[10])[0] = this.f_cj_a_a555694c.getString();
         var9[1] = this.f_cj_b_a555694c.getString();
         var9[2] = this.f_cj_c_a555694c.getString();
         var9[3] = this.f_cj_d_a555694c.getString();
         var9[4] = this.f_cj_e_a555694c.getString();
         var9[5] = this.f_cj_f_a555694c.getString();
         var9[6] = this.f_cj_g_a555694c.getString();
         var9[7] = Integer.toString(this.f_cj_b_aa0ede5b.getSelectedIndex());
         var9[8] = this.f_cj_c_aa0ede5b.isSelected(0) ? "1" : "0";
         var9[9] = Integer.toString(this.f_cj_a_aa0ede5b.getSelectedIndex());
         C_z var12 = new C_z(this.f_cj_a_240c4d, var9);

         try {
            C_ac.m_a_cb38d14b(var12);
         } catch (C_aq var3) {
            C_aq.m_a_481c933f(var3);
            if (var3.f_aq_a_5a) {
               return;
            }
         }

         this.f_cj_a_240c4d.f_ce_a_48a69a2c.removeAllElements();
         C_cn.m_a_33097a9f("47", var12, true);
      } else if (var1 == this.f_cj_e_1570d10e) {
         this.p_cj_a_1385ff(true);
      } else if (var1 == this.f_cj_d_1570d10e) {
         this.p_cj_a_1385ff(false);
      } else if (var1 == this.f_cj_c_1570d10e && C_bf.m_a_cb3ce8a6(this.f_cj_a_12b74)) {
         if (C_v.m_a_46ae24e1().length == 0) {
            this.f_cj_a_67f46df9 = null;
            Alert var8;
            (var8 = new Alert(C_bs.m_a_e96ea081("67"), C_aq.m_a_ec2edeab(161, 0), null, AlertType.WARNING)).setTimeout(-2);
            C_v.m_a_ab8148d2(var8);
         } else {
            this.f_cj_a_6846455f = new List(C_bs.m_a_e96ea081("97"), 1);

            for (int var7 = 0; var7 < C_v.m_a_46ae24e1().length; var7++) {
               this.f_cj_a_6846455f.append(C_v.m_a_46ae24e1()[var7].m_b_73cf11cb(), null);
            }

            this.f_cj_a_6846455f.addCommand(this.f_cj_a_1570d10e);
            this.f_cj_a_6846455f.addCommand(this.f_cj_c_1570d10e);
            this.f_cj_a_6846455f.setCommandListener(this);
            C_bf.m_a_5d527811(this.f_cj_a_12b74);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_cj_a_6846455f);
         }
      } else if (var1 == this.f_cj_c_1570d10e && var2 == this.f_cj_a_6846455f) {
         this.f_cj_a_67f46df9 = null;
         String[] var6 = this.f_cj_a_240c4d.m_a_233b2a7d(this.f_cj_a_49);
         C_aw var11;
         (var11 = new C_aw(-1, C_v.m_a_46ae24e1()[this.f_cj_a_6846455f.getSelectedIndex()].f_l_a_49, var6[37], var6[1], false, false))
            .m_a_2563266(2, var6[24].equals("1"));
         var11.m_a_2563266(8, true);
         var11.m_a_255f295(192, -1);
         C_ac.m_a_cb37742e(var11);
      } else if (var1 == this.f_cj_f_1570d10e) {
         String[] var5;
         C_aw var10;
         (var10 = C_v.m_b_513388b0((var5 = this.f_cj_a_240c4d.m_a_233b2a7d(this.f_cj_a_49))[37])).m_a_4f708078(1, var5[1]);
         C_bf.m_a_5d527811(this.f_cj_a_12b74);
         C_bf.m_a_b7252e78(var10, null);
      } else {
         if (var1 == this.f_cj_g_1570d10e) {
            String[] var4;
            C_bf.m_a_e925fa09((var4 = this.f_cj_a_240c4d.m_a_233b2a7d(this.f_cj_a_49))[37], var4[1]);
            C_bf.m_a_5d527811(this.f_cj_a_12b74);
         }
      }
   }
}
