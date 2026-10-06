package recovered;

/** 0.6 source correspondence (inferred): jimm.util.MagicEye. Release class: br. */

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import jimm.Jimm;

public final class C_br implements C_ay, CommandListener {
   private static C_br f_br_a_240a1f = new C_br();
   private static C_t f_br_a_12b93;
   private static C_br f_br_b_240a1f;
   private Vector f_br_a_48a69a2c = new Vector();
   private static final Command f_br_a_1570d10e = new Command(C_bt.m_a_e96ea081("X1"), 4, 1);
   private int f_br_a_49 = 1;

   public C_br() {
      f_br_b_240a1f = this;
   }

   public static void m_a_9b75() {
      p_br_b_9b75();
      if (f_br_a_12b93.m_b_9b68() != -1) {
         f_br_a_12b93.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
         f_br_a_12b93.m_a_48817c60(C_bi.f_bi_e_1570d10e, C_bf.f_bf_g_49);
         if (!C_bi.m_a_9b79()) {
            f_br_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
         }

         f_br_a_12b93.m_a_48817c60(C_bi.f_bi_g_1570d10e, C_bf.f_bf_g_49);
         f_br_a_12b93.m_a_48817c60(C_bi.f_bi_m_1570d10e, C_bf.f_bf_g_49);
         f_br_a_12b93.m_a_48817c60(f_br_a_1570d10e, C_bf.f_bf_g_49);
      }

      f_br_a_12b93.m_a_6f63a2af(f_br_b_240a1f);
      f_br_a_12b93.m_a_cb385cec(f_br_b_240a1f);
      C_bi.m_a_9c7d0d74(f_br_a_12b93, false);
      f_br_a_12b93.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   private static void p_br_b_9b75() {
      f_br_a_12b93.m_a_8eb9d703(C_bi.f_bi_h_1570d10e);
      f_br_a_12b93.m_a_8eb9d703(C_bi.f_bi_e_1570d10e);
      f_br_a_12b93.m_a_8eb9d703(C_bi.f_bi_f_1570d10e);
      f_br_a_12b93.m_a_8eb9d703(C_bi.f_bi_g_1570d10e);
      f_br_a_12b93.m_a_8eb9d703(C_bi.f_bi_m_1570d10e);
      f_br_a_12b93.m_a_8eb9d703(f_br_a_1570d10e);
   }

   private synchronized void p_br_b_eb1e1393(String var1, String var2, String var3) {
      if (C_bq.m_a_134632(172)) {
         this.f_br_a_48a69a2c.addElement(var1);
         C_aw var4 = C_w.m_a_513388b0(var1);
         String var8 = C_bt.m_a_e96ea081(var2);
         String var5 = C_cf.m_a_2416688b(true, true);
         int var6 = C_bq.m_a_134621(103);
         int var7 = C_bq.m_a_134621(113);
         f_br_a_12b93.m_j_9b75();
         f_br_a_12b93.m_a_68a7a001("[" + this.f_br_a_49 + "]: ", var6, C_bq.f_bq_g_49, this.f_br_a_49);
         if (var4 == null) {
            f_br_a_12b93.m_a_68a7a001(var1 + " (" + var5 + ") ", 16711680, 1 + C_bq.f_bq_g_49, this.f_br_a_49);
         } else {
            f_br_a_12b93.m_a_68a7a001(var4.f_aw_a_523beb0a + " (" + var5 + ") ", var7, 1 + C_bq.f_bq_g_49, this.f_br_a_49);
         }

         f_br_a_12b93.m_a_68a7a001(var8, var6, C_bq.f_bq_g_49, this.f_br_a_49);
         if (null != var3) {
            f_br_a_12b93.m_a_485a5b6b(this.f_br_a_49);
            f_br_a_12b93.m_a_68a7a001(var3, var6, C_bq.f_bq_g_49, this.f_br_a_49);
         }

         f_br_a_12b93.m_a_485a5b6b(this.f_br_a_49);
         this.f_br_a_49++;
         f_br_a_12b93.m_h_13462e(f_br_a_12b93.m_a_9b68());
         f_br_a_12b93.m_k_9b75();
      }
   }

   public static void m_a_eb1e1393(String var0, String var1, String var2) {
      f_br_a_240a1f.p_br_b_eb1e1393(var0, var1, var2);
   }

   public static void m_a_e925fa09(String var0, String var1) {
      f_br_a_240a1f.p_br_b_eb1e1393(var0, var1, null);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == C_bi.f_bi_c_1570d10e) {
         f_br_a_12b93.f_bf_i_49 = 0;
         if (C_bi.f_bi_a_5f790d9c == f_br_a_12b93) {
            C_w.m_a_9b75();
         } else {
            C_bi.m_a_9b75();
         }
      } else if (var1 == f_br_a_1570d10e) {
         C_bi.m_a_5d527811(f_br_a_12b93);

         try {
            C_bi.m_a_cb37742e(C_w.m_b_513388b0((String)this.f_br_a_48a69a2c.elementAt(f_br_a_12b93.m_b_9b68() - 1)));
         } catch (Exception var3) {
         }
      } else if (var1 == C_bi.f_bi_e_1570d10e || var1 == C_bi.f_bi_g_1570d10e) {
         C_bi.m_c_9b75();
         p_br_a_1385ff(var1 == C_bi.f_bi_g_1570d10e);
         f_br_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
      } else if (var1 == C_bi.f_bi_f_1570d10e) {
         p_br_a_1385ff(false);
      } else if (var1 == C_bi.f_bi_m_1570d10e) {
         synchronized (f_br_a_240a1f) {
            this.f_br_a_48a69a2c.removeAllElements();
            this.f_br_a_49 = 1;
            f_br_a_12b93.m_a_9b75();
            p_br_b_9b75();
         }
      }
   }

   private static void p_br_a_1385ff(boolean var0) {
      C_bi.m_a_4c1e0f27(true, "***error***", C_bi.m_a_69d680af(f_br_a_12b93), f_br_a_12b93.m_a_4dee1afa(0, var0), C_bi.m_a_a9514c81(true));
   }

   public final void m_b_cb3dd160(C_bf var1) {
   }

   public final void m_a_cb3dd160(C_bf var1) {
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
      if (var1 == f_br_a_12b93) {
         switch (var2) {
            case 42:
               C_bi.m_c_9b75();
               p_br_a_1385ff(false);
               f_br_a_12b93.m_a_48817c60(C_bi.f_bi_f_1570d10e, C_bf.f_bf_g_49);
         }
      }
   }

   static {
      (f_br_a_12b93 = new C_t(C_bt.m_a_e96ea081("70"))).m_b_13462e(8);
      f_br_a_12b93.c$13462e();
      f_br_a_12b93.m_a_48817c60(C_bi.f_bi_c_1570d10e, C_bf.f_bf_f_49);
   }
}
