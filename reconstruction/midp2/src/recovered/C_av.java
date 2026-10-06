package recovered;

/** 0.6 source correspondence (inferred): jimm.ChatTextList. Release class: av. */

import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import jimm.Jimm;

final class C_av implements C_ay, CommandListener {
   C_t f_av_a_12b93;
   private static final Command f_av_a_1570d10e = new Command(C_bt.m_a_e96ea081("X1"), 8, 1);
   private static final Command f_av_b_1570d10e = new Command(C_bt.m_a_e96ea081("1"), 4, 8);
   private static final Command f_av_c_1570d10e = new Command(C_bt.m_a_e96ea081("6"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 2);
   private static final Command f_av_d_1570d10e = new Command(C_bt.m_a_e96ea081("S1"), 3, 3);
   private static final Command f_av_e_1570d10e = new Command(C_bt.m_a_e96ea081("U4"), 8, 3);
   private static final Command f_av_f_1570d10e = new Command(C_bt.m_a_e96ea081("T2"), 8, 3);
   private static final Command f_av_g_1570d10e = new Command(C_bt.m_a_e96ea081("o0"), 8, 5);
   private static final Command f_av_h_1570d10e = new Command(C_bt.m_a_e96ea081("p7"), 8, 6);
   private static final Command f_av_i_1570d10e = new Command(C_bt.a$7a1ba7c4("P1"), 8, 9);
   public String f_av_a_523beb0a;
   private C_aw f_av_a_2406f9;
   private Vector f_av_a_48a69a2c = new Vector();
   private int f_av_a_49 = 0;

   C_av(String var1, C_aw var2) {
      this.f_av_a_12b93 = new C_t(null);
      this.f_av_a_12b93.c$13462e();
      this.f_av_a_12b93.m_b_13462e(C_bq.m_a_134632(135) ? 8 : 0);
      this.f_av_a_2406f9 = var2;
      this.f_av_a_523beb0a = var1;
      C_bi.m_a_9c7d0d74(this.f_av_a_12b93, C_bq.m_a_134632(145));
      this.f_av_a_12b93.m_a_cb385cec(this);
   }

   final void m_a_cb37742e(C_aw var1) {
      this.f_av_a_2406f9 = var1;
      this.f_av_a_12b93.m_m_9b75();
      this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
      this.f_av_a_12b93.m_a_48817c60(f_av_c_1570d10e, C_bf.f_bf_f_49);
      this.f_av_a_12b93.m_a_48817c60(f_av_a_1570d10e, C_bf.f_bf_g_49);
      this.f_av_a_12b93.m_a_48817c60(f_av_b_1570d10e, C_bf.f_bf_g_49);
      this.f_av_a_12b93.m_a_48817c60(f_av_i_1570d10e, C_bf.f_bf_g_49);
      this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_e_1570d10e, C_bf.f_bf_g_49);
      if (!C_bi.m_a_9b79()) {
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_j_1570d10e, C_bf.f_bf_g_49);
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_k_1570d10e, C_bf.f_bf_g_49);
      }

      if ((this.f_av_a_2406f9.m_a_134632(8) || this.f_av_a_2406f9.m_a_134632(32)) && !this.f_av_a_2406f9.m_a_134632(2)) {
         this.f_av_a_12b93.m_a_48817c60(f_av_g_1570d10e, C_bf.f_bf_g_49);
      }

      if (this.f_av_a_2406f9.m_a_134632(2)) {
         this.f_av_a_12b93.m_a_48817c60(f_av_e_1570d10e, C_bf.f_bf_g_49);
      }

      if (!C_bq.m_a_134632(137)) {
         this.f_av_a_12b93.m_a_48817c60(f_av_h_1570d10e, C_bf.f_bf_g_49);
      }

      this.m_b_9b75();
      this.m_c_9b75();
      this.f_av_a_12b93.m_a_6f63a2af(this);
   }

   public final boolean m_a_9b79() {
      return this.f_av_a_12b93 != null ? this.f_av_a_12b93.m_b_9b79() : false;
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (C_bi.m_a_9b68() == 1) {
         if (var1 == C_bi.f_bi_a_1570d10e) {
            byte var8 = -1;
            switch (C_bi.m_b_9b68()) {
               case 0:
                  var8 = 1;
                  break;
               case 1:
                  var8 = 2;
                  break;
               case 2:
                  var8 = 3;
            }

            C_ap.m_a_afa300e4(this.f_av_a_2406f9.m_b_73cf11cb(), var8);
            C_w.m_a_9b75();
         } else {
            this.m_e_9b75();
         }
      } else if (var1 == f_av_b_1570d10e) {
         C_bi.m_a_b7252e78(this.f_av_a_2406f9, null);
      } else if (var1 == f_av_c_1570d10e) {
         this.f_av_a_2406f9.m_a_9b75();
         this.f_av_a_12b93.f_bf_i_49 = 0;
         C_bi.f_bi_a_2406f9 = null;
         C_w.m_a_9b75();
      } else if (var1 == f_av_i_1570d10e) {
         C_bi.m_a_1cc7afeb("P1", C_bi.f_bi_a_6dccaaa5, this, 1, true);
      } else if (var1 == C_bi.f_bi_e_1570d10e) {
         C_bi.m_c_9b75();
         C_ap.m_a_e925fa09(this.f_av_a_2406f9.m_b_73cf11cb(), this.f_av_a_523beb0a);
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_j_1570d10e, C_bf.f_bf_g_49);
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_k_1570d10e, C_bf.f_bf_g_49);
      } else if (var1 == C_bi.f_bi_f_1570d10e) {
         C_ap.m_a_e925fa09(this.f_av_a_2406f9.m_b_73cf11cb(), this.f_av_a_523beb0a);
      } else if (var1 == C_bi.f_bi_j_1570d10e || var1 == C_bi.f_bi_k_1570d10e) {
         C_bi.m_a_b7252e78(this.f_av_a_2406f9, C_bi.m_a_a9514c81(var1 == C_bi.f_bi_j_1570d10e));
      } else if (var1 == C_bi.f_bi_l_1570d10e) {
         C_bi.m_a_9ba4c01b(this.f_av_a_12b93.m_a_4dee1afa(0, false), this.f_av_a_12b93);
      } else if (var1 == f_av_g_1570d10e) {
         C_bi.m_b_cb37742e(this.f_av_a_2406f9);
      } else if (var1 == f_av_h_1570d10e) {
         int var6 = this.f_av_a_12b93.m_b_9b68();
         C_bc var7 = (C_bc)this.f_av_a_48a69a2c.elementAt(var6);
         String var9;
         if ((var9 = this.f_av_a_12b93.m_a_4dee1afa(var7.f_bc_a_49 & 16777215, false)) != null) {
            C_aa.m_a_48303787(
               this.f_av_a_2406f9.m_b_73cf11cb(),
               var9,
               (byte)(var7.m_a_9b79() ? 0 : 1),
               var7.m_a_9b79() ? this.f_av_a_523beb0a : C_ac.f_ac_a_523beb0a,
               var7.f_bc_a_4a
            );
         }
      } else if (var1 == f_av_f_1570d10e) {
         this.f_av_a_2406f9.m_a_255f295(70, 0);
         this.m_a_9b75();
         C_ad var4 = new C_ad(4, this.f_av_a_2406f9.m_b_73cf11cb(), true, "");
         C_aq var5 = new C_aq(var4);

         try {
            C_ac.m_a_cb3b8b85(var5);
         } catch (C_ar var3) {
            C_ar.m_a_aef55300(var3);
            if (!var3.f_ar_a_5a) {
               ;
            }
         }
      } else if (var1 == f_av_d_1570d10e) {
         C_bi.m_a_a27867fb(10001, this.f_av_a_2406f9, "H4", null);
      } else if (var1 == f_av_e_1570d10e) {
         C_bi.m_a_a27867fb(10002, this.f_av_a_2406f9, "U4", "t4");
      } else {
         if (var1 == f_av_a_1570d10e) {
            C_bi.m_a_cb37742e(this.f_av_a_2406f9);
         }
      }
   }

   public final void m_a_9b75() {
      this.f_av_a_12b93.m_a_8eb9d703(f_av_f_1570d10e);
      this.f_av_a_12b93.m_a_8eb9d703(f_av_d_1570d10e);
   }

   static int m_a_1385f2(boolean var0) {
      return var0 ? C_bq.m_a_134621(108) : C_bq.m_a_134621(113);
   }

   final Vector m_a_6a39c0ed() {
      return this.f_av_a_48a69a2c;
   }

   public final void m_a_cb3dd160(C_bf var1) {
      this.m_b_9b75();
   }

   final void m_b_9b75() {
      this.f_av_a_12b93.m_a_8eb9d703(C_bi.f_bi_l_1570d10e);
      int var1;
      if ((var1 = this.f_av_a_12b93.m_b_9b68()) != -1 && (((C_bc)this.f_av_a_48a69a2c.elementAt(var1)).f_bc_a_49 & 134217728) != 0) {
         this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_l_1570d10e, C_bf.f_bf_g_49);
      }
   }

   final void m_c_9b75() {
      if (this.f_av_a_2406f9.m_c_134632(4)) {
         this.f_av_a_12b93.m_a_48817c60(f_av_f_1570d10e, C_bf.f_bf_g_49);
         this.f_av_a_12b93.m_a_48817c60(f_av_d_1570d10e, C_bf.f_bf_g_49);
      }
   }

   public final void m_a_48a013c6(C_f var1) {
      this.f_av_a_12b93.m_a_48a013c6(var1);
   }

   public final void m_b_48a013c6(C_f var1) {
      this.f_av_a_12b93.m_b_48a013c6(var1);
   }

   public final void m_c_48a013c6(C_f var1) {
      this.f_av_a_12b93.m_d_48a013c6(var1);
   }

   public final void m_b_cb3dd160(C_bf var1) {
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
      if (!C_bi.m_a_db398112(this.f_av_a_2406f9, var2, var3)) {
         switch (var2) {
            case -8:
               if (var3 == 1) {
                  C_bi.m_a_1cc7afeb("P1", C_bi.f_bi_a_6dccaaa5, this, 1, true);
               }

               return;
            case 35:
               if (!C_bi.m_a_9b79()) {
                  try {
                     C_bi.m_a_b7252e78(this.f_av_a_2406f9, C_bi.m_a_a9514c81(true));
                     return;
                  } catch (Exception var5) {
                     Alert var6 = new Alert(C_bt.m_a_e96ea081("46"));
                     Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var6, Jimm.f_jimm_Jimm_a_4a58c677.getCurrent());
                  }
               }

               return;
            case 42:
               C_bi.m_c_9b75();
               C_ap.m_a_e925fa09(this.f_av_a_2406f9.m_b_73cf11cb(), this.f_av_a_523beb0a);
               this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
               this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_j_1570d10e, C_bf.f_bf_g_49);
               this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_k_1570d10e, C_bf.f_bf_g_49);
               return;
            default:
               try {
                  if (var3 == 1) {
                     switch (C_bf.m_b_134621(var2)) {
                        case 2:
                           C_ap.m_c_aad3b1ff(C_w.m_a_a9514c81(false));
                           return;
                        case 5:
                           C_ap.m_c_aad3b1ff(C_w.m_a_a9514c81(true));
                           return;
                     }
                  }

                  C_bi.m_a_db39810e(this.f_av_a_2406f9, var2, var3);
               } catch (Exception var4) {
               }
         }
      }
   }

   public final void m_d_9b75() {
      this.f_av_a_12b93.m_d_9b75();
   }

   public final void m_a_d25f948(long var1, C_f var3, boolean var4) {
      C_t var7 = this.f_av_a_12b93;
      Long var9 = new Long(var1);
      if (var7.f_t_a_d18d4967.containsKey(var9)) {
         C_ag var11;
         C_n var12;
         int index = ((Integer)var7.f_t_a_d18d4967.get(var9)).intValue();
         if (index < var7.f_t_a_48a69a2c.size()
            && (var11 = (C_ag)var7.f_t_a_48a69a2c.elementAt(index)).f_ag_a_48a69a2c.size() > 0
            && (var12 = (C_n)var11.f_ag_a_48a69a2c.elementAt(0)).f_n_a_129e1 != null) {
            var12.f_n_a_129e1 = var3;
            var7.m_f_9b75();
         }

         if (var4) {
            var7.f_t_a_d18d4967.remove(var9);
         }
      }
   }

   final void m_a_3074c21a(String var1, String var2, String var3, long var4, boolean var6, boolean var7, C_f var8, long var9) {
      int var11 = 0;
      boolean var12 = var4 == 0L;
      this.f_av_a_12b93.m_j_9b75();
      int var13 = this.f_av_a_12b93.m_a_9b68();
      StringBuffer var22 = new StringBuffer();
      if (var8 != null) {
         int var10 = this.f_av_a_49;
         C_t var18 = this.f_av_a_12b93;
         if (this.f_av_a_12b93.f_t_a_48a69a2c.isEmpty()) {
            var18.f_t_a_48a69a2c.addElement(new C_ag());
         }

         C_ag var14;
         (var14 = (C_ag)var18.f_t_a_48a69a2c.lastElement()).f_ag_a_49 = var10;
         if (var14.f_ag_a_48a69a2c.size() > 0) {
            var18.m_a_485a5b6b(var10);
            var14 = (C_ag)var18.f_t_a_48a69a2c.lastElement();
         }

         C_n var21;
         (var21 = new C_n()).f_n_a_129e1 = var8;
         var21.f_n_a_523beb0a = "";
         var14.m_a_48a3b6be(var21);
         if (var9 != 0L) {
            int var20 = var18.f_t_a_48a69a2c.size() - 1;
            var18.f_t_a_d18d4967.put(new Long(var9), new Integer(var20));
         }
      }

      var22.append(!var12 ? var1 + " (" + C_cf.m_a_87d767d1(!var7, C_bq.m_a_134632(165), var4) + "): " : var1 + " ");
      if (var22.length() != 0) {
         this.f_av_a_12b93.m_a_68a7a001(var22.toString(), m_a_1385f2(var6), 1 + C_bq.f_bq_g_49, this.f_av_a_49);
         if (!var7 && !var12) {
            this.f_av_a_12b93.m_a_485a5b6b(this.f_av_a_49);
         }

         var11 = !var12 ? this.f_av_a_12b93.m_a_b1542ce3(0, false, this.f_av_a_49).length() : 0;
      } else {
         var11 = 0;
      }

      if (var3.length() > 0) {
         this.f_av_a_12b93.m_a_68a7a001(C_bt.m_a_e96ea081("url") + ": " + var3, 65280, C_bq.f_bq_g_49, this.f_av_a_49);
      }

      C_bi.m_a_7f6c84a2(this.f_av_a_12b93, var2, !var12 ? this.f_av_a_12b93.m_d_9b68() : m_a_1385f2(var6), this.f_av_a_49);
      boolean var17 = false;
      if (C_cf.m_a_dfd94fa3(var2) != null) {
         var17 = true;
         if (var11 == 1) {
            this.f_av_a_12b93.m_a_48817c60(C_bi.f_bi_l_1570d10e, C_bf.f_bf_g_49);
         }
      }

      this.f_av_a_48a69a2c.addElement(new C_bc(var6, var4, var11, var17));
      this.f_av_a_49++;
      if (!var6 || this.f_av_a_12b93.m_b_9b68() >= this.f_av_a_48a69a2c.size() - 2) {
         this.f_av_a_12b93.m_h_13462e(var13);
      }

      this.f_av_a_12b93.m_k_9b75();
   }

   public final void m_e_9b75() {
      this.f_av_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      C_bi.m_a_5d527811(this.f_av_a_12b93);
      this.f_av_a_2406f9.m_a_9b75();
   }
}
