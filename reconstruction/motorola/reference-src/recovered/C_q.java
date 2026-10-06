package recovered;

import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

final class C_q extends C_bd implements C_ax, Runnable, CommandListener, C_t {
   private static Command f_q_a_1570d10e = new Command(C_bs.m_a_e96ea081("f"), 8, 2);
   private static Command f_q_b_1570d10e = new Command(C_bs.m_a_e96ea081("z4"), 8, 3);
   private static Command f_q_c_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
   private static Command f_q_d_1570d10e = new Command(C_bs.a$7a1ba7c4("i1"), 8, 8);
   private static Command f_q_e_1570d10e = new Command(C_bs.m_a_e96ea081("w7"), 8, 5);
   private static Command f_q_f_1570d10e = new Command(C_bs.m_a_e96ea081("J7"), 8, 6);
   private static Command f_q_g_1570d10e = new Command(C_bs.m_a_e96ea081("K7"), 8, 7);
   private static Command f_q_h_1570d10e = new Command(C_bs.m_a_e96ea081("V2"), 8, 4);
   static C_s f_q_a_12b74;
   private static String f_q_a_523beb0a = new String();
   private static Form f_q_a_67f46df9;
   private static TextField f_q_a_a555694c;
   private static ChoiceGroup f_q_a_aa0ede5b;
   private boolean f_q_b_5a;
   private String f_q_b_523beb0a;
   private String f_q_c_523beb0a;
   private static C_s f_q_b_12b74;

   public C_q() {
      super(null);
      this.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_e_49);
      this.m_a_48817c60(f_q_c_1570d10e, C_bd.f_bd_f_49);
      this.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_g_49);
      this.m_a_48817c60(C_bf.f_bf_e_1570d10e, C_bd.f_bd_g_49);
      if (!C_bf.m_a_9b79()) {
         this.m_a_48817c60(C_bf.f_bf_f_1570d10e, C_bd.f_bd_g_49);
      }

      this.m_a_48817c60(C_bf.f_bf_d_1570d10e, C_bd.f_bd_g_49);
      this.m_a_48817c60(f_q_e_1570d10e, C_bd.f_bd_g_49);
      this.m_a_48817c60(f_q_f_1570d10e, C_bd.f_bd_g_49);
      this.m_a_48817c60(f_q_g_1570d10e, C_bd.f_bd_g_49);
      this.m_a_48817c60(f_q_d_1570d10e, C_bd.f_bd_g_49);
      this.m_a_6f63a2af(this);
      this.m_a_cb37e88d(this);
      C_bf.m_a_9c60de72(this, false);
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
      if (var1 == this) {
         C_ba var2;
         if ((var2 = C_aa.m_b_9e194c2(f_q_a_523beb0a, this.m_g_9b68())) == null) {
            return;
         }

         this.m_a_8eb9d703(f_q_h_1570d10e);
         if (var2.f_ba_a_5a) {
            this.m_a_48817c60(f_q_h_1570d10e, C_bd.f_bd_g_49);
         }

         this.m_c_aad3b1ff(C_bd.m_j_9b68() > 133 ? var2.f_ba_d_523beb0a + " " + var2.f_ba_c_523beb0a : var2.f_ba_c_523beb0a);
      }
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
      switch (var2) {
         case -8:
            C_bf.m_a_1cc7afeb("D7", C_bf.f_bf_a_6dccaaa5, this, 2, true);
            break;
         case 42:
            this.p_q_a_1385ff(true);
      }

      if (var1 == f_q_a_12b74 && var3 == 3) {
         switch (C_bd.m_b_134621(var2)) {
            case 2:
               this.p_q_a_13462e(-1);
               return;
            case 5:
               this.p_q_a_13462e(1);
         }
      }
   }

   private void p_q_a_1385ff(boolean var1) {
      int var2;
      if ((var2 = this.m_g_9b68()) != -1) {
         C_ba var3;
         if ((var3 = C_aa.m_b_9e194c2(f_q_a_523beb0a, var2)) != null) {
            if (var1) {
               C_bf.m_c_9b75();
               C_aa.f_aa_a_12b36.m_a_48817c60(C_bf.f_bf_f_1570d10e, C_bd.f_bd_g_49);
               if (f_q_a_12b74 != null) {
                  f_q_a_12b74.m_a_48817c60(C_bf.f_bf_f_1570d10e, C_bd.f_bd_g_49);
               }
            }

            C_bf.m_a_4c1e0f27(var3.f_ba_a_42 == 0, var3.f_ba_c_523beb0a, var3.f_ba_d_523beb0a, var3.f_ba_b_523beb0a + "\n", C_bf.m_a_a9514c81(true));
         }
      }
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
      if (var1 == this) {
         this.p_q_a_9b75();
      }
   }

   private void p_q_a_13462e(int var1) {
      this.m_a_2563266(var1, false);
      this.p_q_a_9b75();
   }

   public final C_aw m_a_46a7a37a() {
      return null;
   }

   private void p_q_d_aad3b1ff(String var1) {
      this.f_q_b_523beb0a = var1;

      try {
         C_ak.m_a_48a670f8(this);
         C_ak.m_a_1385ff(true);
         C_ak.m_a_9b75();
      } catch (C_aq var2) {
         C_aq.m_a_481c933f(var2);
      }
   }

   public final void m_a_aad3b1ff(String var1) {
   }

   public final void m_b_aad3b1ff(String var1) {
      this.f_q_c_523beb0a = var1;
      new Thread(this).start();
   }

   public final void run() {
      if (this.f_q_b_523beb0a == null) {
         this.p_q_a_904444f9(null);
      } else {
         this.p_q_a_904444f9(new C_aw[]{C_v.m_a_513388b0(this.f_q_b_523beb0a)});
      }
   }

   private void p_q_a_904444f9(C_aw[] var1) {
      this.f_q_b_5a = C_bp.m_a_134632(133);
      C_cn.m_a_aad3b1ff(C_bs.a$7a1ba7c4("O7"));
      C_cn.m_b_13462e(0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(Jimm.f_jimm_Jimm_a_3fbeb738.m_a_46a7a9e5());
      if (var1 == null) {
         var1 = C_v.m_a_8f165573();
      }

      for (int var2 = 0; var2 < var1.length; var2++) {
         C_aw var10001 = var1[var2];
         String var5 = this.f_q_c_523beb0a + "jimm_hist_" + var1[var2].m_a_47921032(0) + ".txt";
         C_aw var4 = var10001;
         C_q var3 = this;

         try {
            if (C_aa.m_a_aad3b1f2(var4.m_a_47921032(0)) > 0) {
               C_at var19;
               OutputStream var6 = (var19 = p_q_a_51338853(var5)).m_a_75f818f3();
               if (!var3.f_q_b_5a) {
                  var6.write(new byte[]{-17, -69, -65});
               }

               C_q var15 = var3;
               String var7;
               int var8;
               if ((var8 = C_aa.m_a_aad3b1f2(var7 = var4.m_a_47921032(0))) > 0) {
                  String var9;
                  C_cn.m_a_aad3b1ff(var9 = var4.m_a_47921032(1).length() > 0 ? var4.m_a_47921032(1) : var7);
                  C_cn.m_b_13462e(0);
                  StringBuffer var16 = new StringBuffer()
                     .append("\r\n")
                     .append('\t')
                     .append(C_bs.m_a_e96ea081("M7"))
                     .append(var9)
                     .append(" (")
                     .append(var7)
                     .append(")\r\n")
                     .append('\t')
                     .append(C_bs.m_a_e96ea081("N7"))
                     .append(C_cf.m_a_2416688b(false, true))
                     .append("\r\n\r\n");
                  var6.write(C_cf.m_a_44c4d6c8(var16.toString(), !var3.f_q_b_5a));

                  for (int var10 = 0; var10 < var8; var10++) {
                     C_ba var17 = C_aa.m_a_9e194c2(var7, var10);
                     var6.write(
                        C_cf.m_a_44c4d6c8(
                           "\r\n"
                              + (
                                 var17.f_ba_a_42 == 0
                                    ? "------------------------------------<<<-\r\n " + var9
                                    : "------------------------------------>>>-\r\n " + C_ac.f_ac_a_523beb0a
                              )
                              + " ("
                              + var17.f_ba_c_523beb0a
                              + "):\r\n",
                           !var15.f_q_b_5a
                        )
                     );
                     String var18 = var17.f_ba_b_523beb0a.trim();
                     StringBuffer var11 = new StringBuffer(var18.length());
                     int var12 = var18.length();

                     for (int var13 = 0; var13 < var12; var13++) {
                        var11 = var11.append(var18.charAt(var13));
                     }

                     var11 = var11.append('\r').append('\n');
                     var6.write(C_cf.m_a_44c4d6c8(var11.toString(), !var15.f_q_b_5a));
                     var6.flush();
                     C_cn.m_b_13462e(var10 * 100 / var8);
                  }
               }

               var19.m_a_9b75();
            }
         } catch (Exception var14) {
            var14.printStackTrace();
            C_aq.m_a_481c933f(new C_aq(191, 0, false));
         }
      }

      C_v.m_a_9b75();
   }

   private static C_at p_q_a_51338853(String var0) {
      try {
         C_at var1;
         (var1 = C_at.m_a_46a7a31d()).m_a_aad3b1ff(var0);
         return var1;
      } catch (Exception var2) {
         var2.printStackTrace();
         C_aq.m_a_481c933f(new C_aq(191, 0, true));
         return null;
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_q_h_1570d10e) {
         C_q var3 = this;
         C_ba var4 = C_aa.m_b_9e194c2(f_q_a_523beb0a, this.m_g_9b68());
         Vector var11;
         if ((var11 = C_cf.m_a_dfd94fa3(var4.f_ba_b_523beb0a + "\n")) != null) {
            if (var11.size() == 1) {
               try {
                  Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest((String)var11.elementAt(0));
               } catch (Exception var7) {
               }
            } else {
               (f_q_b_12b74 = C_bf.m_a_53d5d013(C_bs.m_a_e96ea081("V2"), false)).m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
               f_q_b_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
               f_q_b_12b74.m_a_6f63a2af(this);

               for (int var5 = 0; var5 < var11.size(); var5++) {
                  f_q_b_12b74.m_a_68a79fe2((String)var11.elementAt(var5), var3.m_d_9b68(), C_bp.f_bp_g_49, var5).m_a_485a5b4c(var5);
               }

               C_bf.m_a_48a5fc99(f_q_b_12b74);
            }
         }
      }

      if (var1 == f_q_f_1570d10e) {
         this.p_q_d_aad3b1ff(f_q_a_523beb0a);
      }

      if (var1 == f_q_g_1570d10e) {
         this.p_q_d_aad3b1ff(null);
      }

      if (var1 == C_bf.f_bf_c_1570d10e) {
         if (C_bf.m_a_cb3ce8a6(f_q_a_12b74) || var2 == f_q_a_67f46df9) {
            f_q_a_12b74 = null;
            C_bf.m_a_9b75();
            return;
         }

         if (C_bf.m_a_cb3ce8a6(f_q_b_12b74)) {
            f_q_b_12b74 = null;
            C_bf.m_a_9b75();
            return;
         }
      } else {
         if (var1 == f_q_c_1570d10e) {
            C_aa.m_a_9b75();
            f_q_a_12b74 = null;
            f_q_a_67f46df9 = null;
            System.gc();
            C_v.m_a_9b75();
            return;
         }

         if (var1 == C_bf.f_bf_i_1570d10e) {
            if (C_bf.m_a_cb3ce8a6(f_q_b_12b74)) {
               try {
                  Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest(f_q_b_12b74.m_a_4dee1afa(0, false));
                  return;
               } catch (Exception var6) {
                  return;
               }
            }

            this.p_q_a_9b75();
            return;
         }

         if (var1 == f_q_d_1570d10e) {
            C_bf.m_a_1cc7afeb("D7", C_bf.f_bf_a_6dccaaa5, this, 2, true);
            return;
         }

         if (C_bf.m_a_9b68() == 2) {
            if (var1 == C_bf.f_bf_a_1570d10e) {
               switch (C_bf.m_b_9b68()) {
                  case 0:
                     C_aa.m_a_aad3b1ff(f_q_a_523beb0a);
                     break;
                  case 1:
                     C_aa.m_b_aad3b1ff(f_q_a_523beb0a);
                     break;
                  case 2:
                     C_aa.m_b_aad3b1ff(null);
               }
            }

            this.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            this.m_e_9b75();
            return;
         }

         if (var1 == C_bf.f_bf_e_1570d10e || var1 == C_bf.f_bf_f_1570d10e) {
            this.p_q_a_1385ff(var1 == C_bf.f_bf_e_1570d10e);
            return;
         }

         if (var1 == f_q_a_1570d10e) {
            this.p_q_a_13462e(1);
            return;
         }

         if (var1 == f_q_b_1570d10e) {
            this.p_q_a_13462e(-1);
            return;
         }

         if (var1 == C_bf.f_bf_d_1570d10e) {
            if (f_q_a_67f46df9 == null) {
               f_q_a_67f46df9 = new Form(C_bs.m_a_e96ea081("w2"));
               f_q_a_a555694c = new TextField(C_bs.m_a_e96ea081("t7"), "", 64, 0);
               (f_q_a_aa0ede5b = new ChoiceGroup(C_bs.m_a_e96ea081("f4"), 2)).append(C_bs.m_a_e96ea081("u7"), null);
               f_q_a_aa0ede5b.append(C_bs.m_a_e96ea081("v7"), null);
               f_q_a_aa0ede5b.setSelectedIndex(0, true);
               f_q_a_67f46df9.addCommand(C_bf.f_bf_a_1570d10e);
               f_q_a_67f46df9.addCommand(C_bf.f_bf_c_1570d10e);
               f_q_a_67f46df9.append(f_q_a_a555694c);
               f_q_a_67f46df9.append(f_q_a_aa0ede5b);
               f_q_a_67f46df9.setCommandListener(this);
            }

            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_q_a_67f46df9);
            return;
         }

         if (var1 == C_bf.f_bf_a_1570d10e) {
            C_aa.m_a_37a5f649(f_q_a_523beb0a, f_q_a_a555694c.getString(), f_q_a_aa0ede5b.isSelected(1), f_q_a_aa0ede5b.isSelected(0));
            return;
         }

         if (var1 == f_q_e_1570d10e) {
            RecordStore var10 = C_aa.m_a_fbacd853();

            try {
               Alert var9;
               (var9 = new Alert(
                     C_bs.m_a_e96ea081("w7"),
                     C_bs.m_a_e96ea081("x7")
                        + ": "
                        + C_aa.m_a_aad3b1f2(f_q_a_523beb0a)
                        + "\n"
                        + C_bs.m_a_e96ea081("z7")
                        + ": "
                        + var10.getSize() / 1024
                        + "\n"
                        + C_bs.m_a_e96ea081("A7")
                        + ": "
                        + var10.getSizeAvailable() / 1024
                        + "\n",
                     null,
                     AlertType.INFO
                  ))
                  .setTimeout(-2);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var9);
               return;
            } catch (Exception var8) {
            }
         }
      }
   }

   private void p_q_a_9b75() {
      if (this.m_g_9b68() < C_aa.m_a_aad3b1f2(f_q_a_523beb0a)) {
         if (f_q_a_12b74 == null) {
            (f_q_a_12b74 = new C_s(null)).c$13462e();
            f_q_a_12b74.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_e_49);
            f_q_a_12b74.m_a_48817c60(C_bf.f_bf_c_1570d10e, C_bd.f_bd_f_49);
            f_q_a_12b74.m_a_48817c60(C_bf.f_bf_e_1570d10e, C_bd.f_bd_g_49);
            if (!C_bf.m_a_9b79()) {
               f_q_a_12b74.m_a_48817c60(C_bf.f_bf_f_1570d10e, C_bd.f_bd_g_49);
            }

            f_q_a_12b74.m_a_48817c60(f_q_a_1570d10e, C_bd.f_bd_g_49);
            f_q_a_12b74.m_a_48817c60(f_q_b_1570d10e, C_bd.f_bd_g_49);
            f_q_a_12b74.m_a_6f63a2af(this);
            f_q_a_12b74.m_a_cb37e88d(this);
            C_bf.m_a_9c60de72(f_q_a_12b74, false);
         }

         C_ba var1 = C_aa.m_a_9e194c2(f_q_a_523beb0a, this.m_g_9b68());
         f_q_a_12b74.m_a_9b75();
         f_q_a_12b74.m_a_68a79fe2(var1.f_ba_c_523beb0a + ":", f_q_a_12b74.m_d_9b68(), 1 + C_bp.f_bp_g_49, -1);
         f_q_a_12b74.m_a_485a5b4c(-1);
         C_ah.m_a_e9f435a0(f_q_a_12b74, var1.f_ba_b_523beb0a, C_bp.f_bp_g_49, f_q_a_12b74.m_d_9b68(), -1);
         f_q_a_12b74.m_a_8eb9d703(f_q_h_1570d10e);
         if (var1.f_ba_a_5a) {
            f_q_a_12b74.m_a_48817c60(f_q_h_1570d10e, C_bd.f_bd_g_49);
         }

         f_q_a_12b74.m_a_485a5b4c(-1);
         f_q_a_12b74.m_c_aad3b1ff(var1.f_ba_d_523beb0a);
         f_q_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
         f_q_a_12b74.m_c_9b75();
      }
   }

   static String m_a_73cf11cb() {
      return f_q_a_523beb0a;
   }

   static void a$16da05f7(String var0) {
      f_q_a_523beb0a = var0;
   }

   protected final int m_a_9b68() {
      return C_aa.m_a_aad3b1f2(f_q_a_523beb0a);
   }

   protected final void m_a_d822a647(int var1, C_bx var2) {
      C_ba var3;
      if ((var3 = C_aa.m_b_9e194c2(f_q_a_523beb0a, var1)) != null) {
         var2.f_bx_a_523beb0a = var3.f_ba_a_523beb0a;
         var2.f_bx_b_49 = var3.f_ba_a_42 == 0 ? C_bp.m_a_134621(108) : C_bp.m_a_134621(113);
      }
   }

   static {
      new String();
   }
}
