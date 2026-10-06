package recovered;

import java.util.Date;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import jimm.Jimm;

public final class C_a implements CommandListener {
   private Command f_a_a_1570d10e;
   private Command f_a_b_1570d10e;
   C_t f_a_a_12b93;

   public C_a() {
      C_at.m_a_9b68();
      this.f_a_a_1570d10e = new Command(C_bt.m_a_e96ea081("4"), 1, 2);
      this.f_a_b_1570d10e = new Command(C_bt.m_a_e96ea081("6"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 1);
      this.f_a_a_12b93 = new C_t(C_bt.m_a_e96ea081("96"));
      this.f_a_a_12b93.c$13462e();
      this.f_a_a_12b93.m_b_13462e(8);
      C_bi.m_a_9c7d0d74(this.f_a_a_12b93, false);
      this.f_a_a_12b93.m_a_48817c60(this.f_a_a_1570d10e, C_bf.f_bf_e_49);
      this.f_a_a_12b93.m_a_48817c60(this.f_a_b_1570d10e, C_bf.f_bf_f_49);
      this.f_a_a_12b93.m_a_6f63a2af(this);
   }

   public final void m_a_1385ff(boolean var1) {
      C_at.m_a_9b68();
      int color = this.f_a_a_12b93.m_d_9b68();
      this.f_a_a_12b93.m_a_9b75();
      this.f_a_a_12b93
         .m_a_68a7a001(C_bt.m_a_e96ea081("95") + ":\n" + C_bt.m_a_e96ea081("n4") + "\n", color, 1 + C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(11) + "\n", color, C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(12) + "\n", color, C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(13) + "\n\n", color, C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_bt.m_a_e96ea081("j5") + " ", color, 1 + C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(4) + "\n" + C_bt.m_a_e96ea081("n4") + "\n", color, 1 + C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(21) + "\n", color, C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(22) + "\n", color, C_bq.f_bq_g_49, -1)
         .m_a_68a7a001(C_at.m_a_47921032(23) + "\n", color, C_bq.f_bq_g_49, -1);
      C_at.m_a_9b68();
      this.f_a_a_12b93.m_d_9b75();
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_a_a_1570d10e) {
         C_at.f_at_a_49 = 0;
         C_at.f_at_b_49 = 0;
         C_at.f_at_c_49 = 0;
         C_at.f_at_a_711fe21.setTime(new Date().getTime());

         try {
            C_at.m_a_9b75();
         } catch (Exception var3) {
         }

         this.m_a_1385ff(true);
      }

      if (var1 == this.f_a_b_1570d10e) {
         C_bi.m_a_9b75();
      }
   }
}
