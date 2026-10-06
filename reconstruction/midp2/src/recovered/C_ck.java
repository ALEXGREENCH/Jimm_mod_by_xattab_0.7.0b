package recovered;

/** 0.6 source correspondence (inferred): jimm.Search. Release class: ck. */

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

public final class C_ck implements C_ay, CommandListener {
   private Command f_ck_a_1570d10e;
   private Command f_ck_b_1570d10e;
   private Command f_ck_c_1570d10e;
   private Command f_ck_d_1570d10e;
   private Command f_ck_e_1570d10e;
   private Command f_ck_f_1570d10e;
   private Command f_ck_g_1570d10e;
   private Form f_ck_a_67f46df9;
   private C_t f_ck_a_12b93;
   private List f_ck_a_6846455f;
   private TextField f_ck_a_a555694c;
   private TextField f_ck_b_a555694c;
   private TextField f_ck_c_a555694c;
   private TextField f_ck_d_a555694c;
   private TextField f_ck_e_a555694c;
   private TextField f_ck_f_a555694c;
   private TextField f_ck_g_a555694c;
   private ChoiceGroup f_ck_a_aa0ede5b;
   private ChoiceGroup f_ck_b_aa0ede5b;
   private ChoiceGroup f_ck_c_aa0ede5b;
   private int f_ck_a_49;
   private final C_cg f_ck_a_240c8b;

   public C_ck(C_cg var1) {
      this.f_ck_a_240c8b = var1;
      this.f_ck_b_1570d10e = new Command(C_bt.m_a_e96ea081("j6"), 4, 1);
      this.f_ck_a_1570d10e = new Command(C_bt.m_a_e96ea081("6"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 2);
      this.f_ck_c_1570d10e = new Command(C_bt.m_a_e96ea081("p0"), 8, 1);
      this.f_ck_e_1570d10e = new Command(C_bt.m_a_e96ea081("c"), 8, 3);
      this.f_ck_d_1570d10e = new Command(C_bt.m_a_e96ea081("w4"), 8, 4);
      this.f_ck_f_1570d10e = new Command(C_bt.m_a_e96ea081("45"), 8, 6);
      this.f_ck_g_1570d10e = new Command(C_bt.m_a_e96ea081("83"), 8, 7);
      this.f_ck_a_67f46df9 = new Form(C_bt.m_a_e96ea081("a"));
      this.f_ck_a_a555694c = new TextField(C_bt.m_a_e96ea081("f6"), "", 32, 2);
      this.f_ck_b_a555694c = new TextField(C_bt.m_a_e96ea081("U3"), "", 32, 0);
      this.f_ck_c_a555694c = new TextField(C_bt.m_a_e96ea081("u2"), "", 32, 0);
      this.f_ck_d_a555694c = new TextField(C_bt.m_a_e96ea081("o3"), "", 32, 0);
      this.f_ck_e_a555694c = new TextField(C_bt.m_a_e96ea081("42"), "", 32, 1);
      this.f_ck_f_a555694c = new TextField(C_bt.m_a_e96ea081("d1"), "", 32, 0);
      this.f_ck_g_a555694c = new TextField(C_bt.m_a_e96ea081("m3"), "", 32, 0);
      this.f_ck_a_aa0ede5b = new ChoiceGroup(C_bt.m_a_e96ea081("v0"), 4, C_cf.m_a_639c22ad("---|13-17|18-22|23-29|30-39|40-49|50-59|> 60", '|'), null);
      this.f_ck_b_aa0ede5b = new ChoiceGroup(C_bt.m_a_e96ea081("R2"), 4);
      this.f_ck_b_aa0ede5b.append(C_bt.m_a_e96ea081("p2"), null);
      this.f_ck_b_aa0ede5b.append(C_bt.m_a_e96ea081("o2"), null);
      this.f_ck_b_aa0ede5b.append(C_bt.m_a_e96ea081("A3"), null);
      this.f_ck_c_aa0ede5b = new ChoiceGroup("", 2);
      this.f_ck_c_aa0ede5b.append(C_bt.m_a_e96ea081("b4"), null);
      this.f_ck_a_67f46df9.append(this.f_ck_c_aa0ede5b);
      this.f_ck_a_67f46df9.append(this.f_ck_a_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_b_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_c_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_d_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_f_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_b_aa0ede5b);
      this.f_ck_a_67f46df9.append(this.f_ck_e_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_g_a555694c);
      this.f_ck_a_67f46df9.append(this.f_ck_a_aa0ede5b);
      this.f_ck_a_67f46df9.setCommandListener(this);
      this.f_ck_a_12b93 = new C_t(null);
      this.f_ck_a_12b93.m_a_cb385cec(this);
      if (var1.f_cg_a_5a) {
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_c_1570d10e, C_bf.f_bf_e_49);
      } else {
         this.f_ck_a_12b93.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_d_1570d10e, C_bf.f_bf_g_49);
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_e_1570d10e, C_bf.f_bf_g_49);
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_c_1570d10e, C_bf.f_bf_g_49);
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_f_1570d10e, C_bf.f_bf_g_49);
         this.f_ck_a_12b93.m_a_48817c60(this.f_ck_g_1570d10e, C_bf.f_bf_g_49);
      }

      this.f_ck_a_12b93.c$13462e();
      C_bi.m_a_9c7d0d74(this.f_ck_a_12b93, false);
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            int var2 = this.f_ck_a_49;
            this.f_ck_a_12b93.m_a_9b75();
            if (this.f_ck_a_240c8b.f_cg_a_48a69a2c.size() > 0) {
               if (this.f_ck_a_240c8b.f_cg_a_48a69a2c.size() == 1) {
                  this.f_ck_a_12b93.m_a_8eb9d703(this.f_ck_e_1570d10e);
                  this.f_ck_a_12b93.m_a_8eb9d703(this.f_ck_d_1570d10e);
               }

               this.f_ck_a_12b93.m_j_9b75();
               C_bi.m_a_2f0dd4c3(this.f_ck_a_240c8b.m_a_233b2a7d(var2), this.f_ck_a_12b93);
               this.f_ck_a_12b93
                  .m_c_aad3b1ff(C_bt.m_a_e96ea081("05") + " " + Integer.toString(var2 + 1) + "/" + Integer.toString(this.f_ck_a_240c8b.f_cg_a_48a69a2c.size()));
               this.f_ck_a_12b93.m_k_9b75();
            } else {
               this.f_ck_a_12b93.m_j_9b75();
               this.f_ck_a_12b93.m_c_aad3b1ff(C_bt.m_a_e96ea081("05") + " 0/0");
               this.f_ck_a_12b93.m_a_68a7a001(C_bt.m_a_e96ea081("Z3") + ": ", 0, 1, -1);
               this.f_ck_a_12b93.m_k_9b75();
            }

            this.f_ck_a_12b93.m_a_48817c60(this.f_ck_a_1570d10e, C_bf.f_bf_f_49);
            this.f_ck_a_12b93.m_a_6f63a2af(this);
            this.f_ck_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            return;
         case 2:
            this.f_ck_a_67f46df9.addCommand(this.f_ck_b_1570d10e);
            this.f_ck_a_67f46df9.addCommand(this.f_ck_a_1570d10e);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_ck_a_67f46df9);
            return;
         case 3:
            this.f_ck_a_67f46df9.addCommand(this.f_ck_b_1570d10e);
            this.f_ck_a_67f46df9.addCommand(this.f_ck_a_1570d10e);
            Alert var3;
            (var3 = new Alert(null, C_bt.m_a_e96ea081("Z3"), null, null)).setTimeout(-2);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var3, this.f_ck_a_67f46df9);
      }
   }

   private void p_ck_a_1385ff(boolean var1) {
      if (var1) {
         this.f_ck_a_49 = (this.f_ck_a_49 + 1) % this.f_ck_a_240c8b.f_cg_a_48a69a2c.size();
      } else if (this.f_ck_a_49 == 0) {
         this.f_ck_a_49 = this.f_ck_a_240c8b.f_cg_a_48a69a2c.size() - 1;
      } else {
         this.f_ck_a_49 = (this.f_ck_a_49 - 1) % this.f_ck_a_240c8b.f_cg_a_48a69a2c.size();
      }

      this.m_a_13462e(1);
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
      if (var3 == 1) {
         switch (C_bf.m_b_134621(var2)) {
            case 2:
               this.p_ck_a_1385ff(false);
               return;
            case 5:
               this.p_ck_a_1385ff(true);
         }
      }
   }

   public final void m_a_cb3dd160(C_bf var1) {
   }

   public final void m_b_cb3dd160(C_bf var1) {
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_ck_a_1570d10e) {
         if (C_bi.m_a_cb3dd164(this.f_ck_a_12b93) && !this.f_ck_a_240c8b.f_cg_a_5a) {
            this.m_a_13462e(2);
            C_bu.m_a_1385ff(true);
         } else if (var2 == this.f_ck_a_67f46df9) {
            this.f_ck_a_67f46df9 = null;
            C_bp.m_b_9b75();
         } else {
            if (var2 != this.f_ck_a_6846455f) {
               this.f_ck_a_67f46df9 = null;
            }

            C_bi.m_a_9b75();
         }
      } else if (var1 == this.f_ck_b_1570d10e) {
         this.f_ck_a_49 = 0;
         String[] var9;
         (var9 = new String[10])[0] = this.f_ck_a_a555694c.getString();
         var9[1] = this.f_ck_b_a555694c.getString();
         var9[2] = this.f_ck_c_a555694c.getString();
         var9[3] = this.f_ck_d_a555694c.getString();
         var9[4] = this.f_ck_e_a555694c.getString();
         var9[5] = this.f_ck_f_a555694c.getString();
         var9[6] = this.f_ck_g_a555694c.getString();
         var9[7] = Integer.toString(this.f_ck_b_aa0ede5b.getSelectedIndex());
         var9[8] = this.f_ck_c_aa0ede5b.isSelected(0) ? "1" : "0";
         var9[9] = Integer.toString(this.f_ck_a_aa0ede5b.getSelectedIndex());
         C_z var12 = new C_z(this.f_ck_a_240c8b, var9);

         try {
            C_ac.m_a_cb3b8b85(var12);
         } catch (C_ar var3) {
            C_ar.m_a_aef55300(var3);
            if (var3.f_ar_a_5a) {
               return;
            }
         }

         this.f_ck_a_240c8b.f_cg_a_48a69a2c.removeAllElements();
         C_cp.m_a_335e07a5("17", var12, true);
      } else if (var1 == this.f_ck_e_1570d10e) {
         this.p_ck_a_1385ff(true);
      } else if (var1 == this.f_ck_d_1570d10e) {
         this.p_ck_a_1385ff(false);
      } else if (var1 == this.f_ck_c_1570d10e && C_bi.m_a_cb3dd164(this.f_ck_a_12b93)) {
         if (C_w.m_a_46ae2500().length == 0) {
            this.f_ck_a_67f46df9 = null;
            Alert var8;
            (var8 = new Alert(C_bt.m_a_e96ea081("37"), C_ar.m_a_ec2edeab(161, 0), null, AlertType.WARNING)).setTimeout(-2);
            C_w.m_a_ab8148d2(var8);
         } else {
            this.f_ck_a_6846455f = new List(C_bt.m_a_e96ea081("67"), 1);

            for (int var7 = 0; var7 < C_w.m_a_46ae2500().length; var7++) {
               this.f_ck_a_6846455f.append(C_w.m_a_46ae2500()[var7].m_b_73cf11cb(), null);
            }

            this.f_ck_a_6846455f.addCommand(this.f_ck_a_1570d10e);
            this.f_ck_a_6846455f.addCommand(this.f_ck_c_1570d10e);
            this.f_ck_a_6846455f.setCommandListener(this);
            C_bi.m_a_5d527811(this.f_ck_a_12b93);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_ck_a_6846455f);
         }
      } else if (var1 == this.f_ck_c_1570d10e && var2 == this.f_ck_a_6846455f) {
         this.f_ck_a_67f46df9 = null;
         String[] var6 = this.f_ck_a_240c8b.m_a_233b2a7d(this.f_ck_a_49);
         C_aw var11;
         (var11 = new C_aw(-1, C_w.m_a_46ae2500()[this.f_ck_a_6846455f.getSelectedIndex()].f_m_a_49, var6[37], var6[1], false, false))
            .m_a_2563266(2, var6[24].equals("1"));
         var11.m_a_2563266(8, true);
         var11.m_a_255f295(192, -1);
         C_ac.m_a_cb37742e(var11);
      } else if (var1 == this.f_ck_f_1570d10e) {
         String[] var5;
         C_aw var10;
         (var10 = C_w.m_b_513388b0((var5 = this.f_ck_a_240c8b.m_a_233b2a7d(this.f_ck_a_49))[37])).m_a_4f708078(1, var5[1]);
         C_bi.m_a_5d527811(this.f_ck_a_12b93);
         C_bi.m_a_b7252e78(var10, null);
      } else {
         if (var1 == this.f_ck_g_1570d10e) {
            String[] var4;
            C_bi.m_a_e925fa09((var4 = this.f_ck_a_240c8b.m_a_233b2a7d(this.f_ck_a_49))[37], var4[1]);
            C_bi.m_a_5d527811(this.f_ck_a_12b93);
         }
      }
   }
}
