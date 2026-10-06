package recovered;

/** 0.6 source correspondence (inferred): jimm.HistoryStorageList. Release class: r. */

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

final class C_r extends C_bf implements C_ay, Runnable, CommandListener, C_u {
   private static Command f_r_a_1570d10e = new Command(C_bt.m_a_e96ea081("c"), 8, 2);
   private static Command f_r_b_1570d10e = new Command(C_bt.m_a_e96ea081("w4"), 8, 3);
   private static Command f_r_c_1570d10e = new Command(C_bt.m_a_e96ea081("6"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 2);
   private static Command f_r_d_1570d10e = new Command(C_bt.a$7a1ba7c4("f1"), 8, 8);
   private static Command f_r_e_1570d10e = new Command(C_bt.m_a_e96ea081("t7"), 8, 5);
   private static Command f_r_f_1570d10e = new Command(C_bt.m_a_e96ea081("G7"), 8, 6);
   private static Command f_r_g_1570d10e = new Command(C_bt.m_a_e96ea081("H7"), 8, 7);
   private static Command f_r_h_1570d10e = new Command(C_bt.m_a_e96ea081("S2"), 8, 4);
   static C_t f_r_a_12b93;
   private static String f_r_a_523beb0a = new String();
   private static Form f_r_a_67f46df9;
   private static TextField f_r_a_a555694c;
   private static ChoiceGroup f_r_a_aa0ede5b;
   private boolean f_r_b_5a;
   private String f_r_b_523beb0a;
   private String f_r_c_523beb0a;
   private static C_t f_r_b_12b93;

   public C_r() {
      super(null);
      this.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
      this.m_a_48817c60(f_r_c_1570d10e, C_bf.f_bf_f_49);
      this.m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_g_49);
      this.m_a_48817c60(C_bi.f_bi_e_1570d10e, C_bf.f_bf_g_49);
      if (!C_bi.m_a_9b79()) {
         this.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
      }

      this.m_a_48817c60(C_bi.f_bi_d_1570d10e, C_bf.f_bf_g_49);
      this.m_a_48817c60(f_r_e_1570d10e, C_bf.f_bf_g_49);
      this.m_a_48817c60(f_r_f_1570d10e, C_bf.f_bf_g_49);
      this.m_a_48817c60(f_r_g_1570d10e, C_bf.f_bf_g_49);
      this.m_a_48817c60(f_r_d_1570d10e, C_bf.f_bf_g_49);
      this.m_a_6f63a2af(this);
      this.m_a_cb385cec(this);
      C_bi.m_a_9c7d0d74(this, false);
   }

   public final void m_a_cb3dd160(C_bf var1) {
      if (var1 == this) {
         C_bb var2;
         if ((var2 = C_aa.m_b_9e194e1(f_r_a_523beb0a, this.m_g_9b68())) == null) {
            return;
         }

         this.m_a_8eb9d703(f_r_h_1570d10e);
         if (var2.f_bb_a_5a) {
            this.m_a_48817c60(f_r_h_1570d10e, C_bf.f_bf_g_49);
         }

         this.m_c_aad3b1ff(C_bf.m_j_9b68() > 133 ? var2.f_bb_d_523beb0a + " " + var2.f_bb_c_523beb0a : var2.f_bb_c_523beb0a);
      }
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
      switch (var2) {
         case -8:
            C_bi.m_a_1cc7afeb("A7", C_bi.f_bi_a_6dccaaa5, this, 2, true);
            break;
         case 42:
            this.p_r_a_1385ff(true);
      }

      if (var1 == f_r_a_12b93 && var3 == 1) {
         switch (C_bf.m_b_134621(var2)) {
            case 2:
               this.p_r_a_13462e(-1);
               return;
            case 5:
               this.p_r_a_13462e(1);
         }
      }
   }

   private void p_r_a_1385ff(boolean var1) {
      int var2;
      if ((var2 = this.m_g_9b68()) != -1) {
         C_bb var3;
         if ((var3 = C_aa.m_b_9e194e1(f_r_a_523beb0a, var2)) != null) {
            if (var1) {
               C_bi.m_c_9b75();
               C_aa.f_aa_a_12b55.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
               if (f_r_a_12b93 != null) {
                  f_r_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
               }
            }

            C_bi.m_a_4c1e0f27(var3.f_bb_a_42 == 0, var3.f_bb_c_523beb0a, var3.f_bb_d_523beb0a, var3.f_bb_b_523beb0a + "\n", C_bi.m_a_a9514c81(true));
         }
      }
   }

   public final void m_b_cb3dd160(C_bf var1) {
      if (var1 == this) {
         this.p_r_a_9b75();
      }
   }

   private void p_r_a_13462e(int var1) {
      this.m_a_2563266(var1, false);
      this.p_r_a_9b75();
   }

   public final C_aw m_a_46a7a37a() {
      return null;
   }

   private void p_r_d_aad3b1ff(String var1) {
      this.f_r_b_523beb0a = var1;

      try {
         C_al.m_a_48a6e557(this);
         C_al.m_a_1385ff(true);
         C_al.m_a_9b75();
      } catch (C_ar var2) {
         C_ar.m_a_aef55300(var2);
      }
   }

   public final void m_a_aad3b1ff(String var1) {
   }

   public final void m_b_aad3b1ff(String var1) {
      this.f_r_c_523beb0a = var1;
      new Thread(this).start();
   }

   public final void run() {
      if (this.f_r_b_523beb0a == null) {
         this.p_r_a_904444f9(null);
      } else {
         this.p_r_a_904444f9(new C_aw[]{C_w.m_a_513388b0(this.f_r_b_523beb0a)});
      }
   }

   private void p_r_a_904444f9(C_aw[] var1) {
      this.f_r_b_5a = C_bq.m_a_134632(133);
      C_cp.m_a_aad3b1ff(C_bt.a$7a1ba7c4("L7"));
      C_cp.m_b_13462e(0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(Jimm.f_jimm_Jimm_a_3fbeb738.m_a_46a7aa23());
      if (var1 == null) {
         var1 = C_w.m_a_8f165573();
      }

      for (int var2 = 0; var2 < var1.length; var2++) {
         C_aw var10001 = var1[var2];
         String var5 = this.f_r_c_523beb0a + "jimm_hist_" + var1[var2].m_a_47921032(0) + ".txt";
         C_aw var4 = var10001;
         C_r var3 = this;

         try {
            if (C_aa.m_a_aad3b1f2(var4.m_a_47921032(0)) > 0) {
               C_au var19;
               OutputStream var6 = (var19 = p_r_a_51338872(var5)).m_a_75f818f3();
               if (!var3.f_r_b_5a) {
                  var6.write(new byte[]{-17, -69, -65});
               }

               C_r var15 = var3;
               String var7;
               int var8;
               if ((var8 = C_aa.m_a_aad3b1f2(var7 = var4.m_a_47921032(0))) > 0) {
                  String var9;
                  C_cp.m_a_aad3b1ff(var9 = var4.m_a_47921032(1).length() > 0 ? var4.m_a_47921032(1) : var7);
                  C_cp.m_b_13462e(0);
                  StringBuffer var16 = new StringBuffer()
                     .append("\r\n")
                     .append('\t')
                     .append(C_bt.m_a_e96ea081("J7"))
                     .append(var9)
                     .append(" (")
                     .append(var7)
                     .append(")\r\n")
                     .append('\t')
                     .append(C_bt.m_a_e96ea081("K7"))
                     .append(C_cf.m_a_2416688b(false, true))
                     .append("\r\n\r\n");
                  var6.write(C_cf.m_a_44c4d6c8(var16.toString(), !var3.f_r_b_5a));

                  for (int var10 = 0; var10 < var8; var10++) {
                     C_bb var17 = C_aa.m_a_9e194e1(var7, var10);
                     var6.write(
                        C_cf.m_a_44c4d6c8(
                           "\r\n"
                              + (
                                 var17.f_bb_a_42 == 0
                                    ? "------------------------------------<<<-\r\n " + var9
                                    : "------------------------------------>>>-\r\n " + C_ac.f_ac_a_523beb0a
                              )
                              + " ("
                              + var17.f_bb_c_523beb0a
                              + "):\r\n",
                           !var15.f_r_b_5a
                        )
                     );
                     String var18 = var17.f_bb_b_523beb0a.trim();
                     StringBuffer var11 = new StringBuffer(var18.length());
                     int var12 = var18.length();

                     for (int var13 = 0; var13 < var12; var13++) {
                        var11 = var11.append(var18.charAt(var13));
                     }

                     var11 = var11.append('\r').append('\n');
                     var6.write(C_cf.m_a_44c4d6c8(var11.toString(), !var15.f_r_b_5a));
                     var6.flush();
                     C_cp.m_b_13462e(var10 * 100 / var8);
                  }
               }

               var19.m_a_9b75();
            }
         } catch (Exception var14) {
            var14.printStackTrace();
            C_ar.m_a_aef55300(new C_ar(191, 0, false));
         }
      }

      C_w.m_a_9b75();
   }

   private static C_au p_r_a_51338872(String var0) {
      try {
         C_b var1;
         (var1 = new C_b()).m_a_aad3b1ff(var0);
         return var1;
      } catch (Exception var2) {
         var2.printStackTrace();
         C_ar.m_a_aef55300(new C_ar(191, 0, true));
         return null;
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_r_h_1570d10e) {
         C_r var3 = this;
         C_bb var4 = C_aa.m_b_9e194e1(f_r_a_523beb0a, this.m_g_9b68());
         Vector var11;
         if ((var11 = C_cf.m_a_dfd94fa3(var4.f_bb_b_523beb0a + "\n")) != null) {
            if (var11.size() == 1) {
               try {
                  Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest((String)var11.elementAt(0));
               } catch (Exception var7) {
               }
            } else {
               (f_r_b_12b93 = C_bi.m_a_53d5d032(C_bt.m_a_e96ea081("S2"), false)).m_a_48817c60(C_bi.f_bi_i_1570d10e, C_bf.f_bf_e_49);
               f_r_b_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
               f_r_b_12b93.m_a_6f63a2af(this);

               for (int var5 = 0; var5 < var11.size(); var5++) {
                  f_r_b_12b93.m_a_68a7a001((String)var11.elementAt(var5), var3.m_d_9b68(), C_bq.f_bq_g_49, var5).m_a_485a5b6b(var5);
               }

               C_bi.m_a_48a670f8(f_r_b_12b93);
            }
         }
      }

      if (var1 == f_r_f_1570d10e) {
         this.p_r_d_aad3b1ff(f_r_a_523beb0a);
      }

      if (var1 == f_r_g_1570d10e) {
         this.p_r_d_aad3b1ff(null);
      }

      if (var1 == C_bi.f_bi_c_1570d10e) {
         if (C_bi.m_a_cb3dd164(f_r_a_12b93) || var2 == f_r_a_67f46df9) {
            f_r_a_12b93 = null;
            C_bi.m_a_9b75();
            return;
         }

         if (C_bi.m_a_cb3dd164(f_r_b_12b93)) {
            f_r_b_12b93 = null;
            C_bi.m_a_9b75();
            return;
         }
      } else {
         if (var1 == f_r_c_1570d10e) {
            C_aa.m_a_9b75();
            f_r_a_12b93 = null;
            f_r_a_67f46df9 = null;
            System.gc();
            C_w.m_a_9b75();
            return;
         }

         if (var1 == C_bi.f_bi_i_1570d10e) {
            if (C_bi.m_a_cb3dd164(f_r_b_12b93)) {
               try {
                  Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest(f_r_b_12b93.m_a_4dee1afa(0, false));
                  return;
               } catch (Exception var6) {
                  return;
               }
            }

            this.p_r_a_9b75();
            return;
         }

         if (var1 == f_r_d_1570d10e) {
            C_bi.m_a_1cc7afeb("A7", C_bi.f_bi_a_6dccaaa5, this, 2, true);
            return;
         }

         if (C_bi.m_a_9b68() == 2) {
            if (var1 == C_bi.f_bi_a_1570d10e) {
               switch (C_bi.m_b_9b68()) {
                  case 0:
                     C_aa.m_a_aad3b1ff(f_r_a_523beb0a);
                     break;
                  case 1:
                     C_aa.m_b_aad3b1ff(f_r_a_523beb0a);
                     break;
                  case 2:
                     C_aa.m_b_aad3b1ff(null);
               }
            }

            this.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
            this.m_f_9b75();
            return;
         }

         if (var1 == C_bi.f_bi_e_1570d10e || var1 == C_bi.f_bi_f_1570d10e) {
            this.p_r_a_1385ff(var1 == C_bi.f_bi_e_1570d10e);
            return;
         }

         if (var1 == f_r_a_1570d10e) {
            this.p_r_a_13462e(1);
            return;
         }

         if (var1 == f_r_b_1570d10e) {
            this.p_r_a_13462e(-1);
            return;
         }

         if (var1 == C_bi.f_bi_d_1570d10e) {
            if (f_r_a_67f46df9 == null) {
               f_r_a_67f46df9 = new Form(C_bt.m_a_e96ea081("t2"));
               f_r_a_a555694c = new TextField(C_bt.m_a_e96ea081("q7"), "", 64, 0);
               (f_r_a_aa0ede5b = new ChoiceGroup(C_bt.m_a_e96ea081("c4"), 2)).append(C_bt.m_a_e96ea081("r7"), null);
               f_r_a_aa0ede5b.append(C_bt.m_a_e96ea081("s7"), null);
               f_r_a_aa0ede5b.setSelectedIndex(0, true);
               f_r_a_67f46df9.addCommand(C_bi.f_bi_a_1570d10e);
               f_r_a_67f46df9.addCommand(C_bi.f_bi_c_1570d10e);
               f_r_a_67f46df9.append(f_r_a_a555694c);
               f_r_a_67f46df9.append(f_r_a_aa0ede5b);
               f_r_a_67f46df9.setCommandListener(this);
            }

            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_r_a_67f46df9);
            return;
         }

         if (var1 == C_bi.f_bi_a_1570d10e) {
            C_aa.m_a_37a5f649(f_r_a_523beb0a, f_r_a_a555694c.getString(), f_r_a_aa0ede5b.isSelected(1), f_r_a_aa0ede5b.isSelected(0));
            return;
         }

         if (var1 == f_r_e_1570d10e) {
            RecordStore var10 = C_aa.m_a_fbacd853();

            try {
               Alert var9;
               (var9 = new Alert(
                     C_bt.m_a_e96ea081("t7"),
                     C_bt.m_a_e96ea081("u7")
                        + ": "
                        + C_aa.m_a_aad3b1f2(f_r_a_523beb0a)
                        + "\n"
                        + C_bt.m_a_e96ea081("w7")
                        + ": "
                        + var10.getSize() / 1024
                        + "\n"
                        + C_bt.m_a_e96ea081("x7")
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

   private void p_r_a_9b75() {
      if (this.m_g_9b68() < C_aa.m_a_aad3b1f2(f_r_a_523beb0a)) {
         if (f_r_a_12b93 == null) {
            (f_r_a_12b93 = new C_t(null)).c$13462e();
            f_r_a_12b93.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
            f_r_a_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
            f_r_a_12b93.m_a_48817c60(C_bi.f_bi_e_1570d10e, C_bf.f_bf_g_49);
            if (!C_bi.m_a_9b79()) {
               f_r_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
            }

            f_r_a_12b93.m_a_48817c60(f_r_a_1570d10e, C_bf.f_bf_g_49);
            f_r_a_12b93.m_a_48817c60(f_r_b_1570d10e, C_bf.f_bf_g_49);
            f_r_a_12b93.m_a_6f63a2af(this);
            f_r_a_12b93.m_a_cb385cec(this);
            C_bi.m_a_9c7d0d74(f_r_a_12b93, false);
         }

         C_bb var1 = C_aa.m_a_9e194e1(f_r_a_523beb0a, this.m_g_9b68());
         f_r_a_12b93.m_a_9b75();
         f_r_a_12b93.m_a_68a7a001(var1.f_bb_c_523beb0a + ":", f_r_a_12b93.m_d_9b68(), 1 + C_bq.f_bq_g_49, -1);
         f_r_a_12b93.m_a_485a5b6b(-1);
         C_ah.m_a_6e2482a1(f_r_a_12b93, var1.f_bb_b_523beb0a, C_bq.f_bq_g_49, f_r_a_12b93.m_d_9b68(), -1);
         f_r_a_12b93.m_a_8eb9d703(f_r_h_1570d10e);
         if (var1.f_bb_a_5a) {
            f_r_a_12b93.m_a_48817c60(f_r_h_1570d10e, C_bf.f_bf_g_49);
         }

         f_r_a_12b93.m_a_485a5b6b(-1);
         f_r_a_12b93.m_c_aad3b1ff(var1.f_bb_d_523beb0a);
         f_r_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
         f_r_a_12b93.m_d_9b75();
      }
   }

   static String m_a_73cf11cb() {
      return f_r_a_523beb0a;
   }

   static void a$16da05f7(String var0) {
      f_r_a_523beb0a = var0;
   }

   protected final int m_a_9b68() {
      return C_aa.m_a_aad3b1f2(f_r_a_523beb0a);
   }

   protected final void m_a_d826493f(int var1, C_ca var2) {
      C_bb var3;
      if ((var3 = C_aa.m_b_9e194e1(f_r_a_523beb0a, var1)) != null) {
         var2.f_ca_a_523beb0a = var3.f_bb_a_523beb0a;
         var2.f_ca_b_49 = var3.f_bb_a_42 == 0 ? C_bq.m_a_134621(108) : C_bq.m_a_134621(113);
      }
   }

   static {
      new String();
   }
}
