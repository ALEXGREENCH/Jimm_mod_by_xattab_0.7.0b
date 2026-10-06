package recovered;

import java.util.Date;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public final class C_a implements CommandListener {
   private Command f_a_a_1570d10e;
   private Command f_a_b_1570d10e;
   C_s f_a_a_12b74;

   public C_a() {
      C_as.m_a_9b68();
      this.f_a_a_1570d10e = new Command(C_bs.m_a_e96ea081("7"), 2, 2);
      this.f_a_b_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 4, 1);
      this.f_a_a_12b74 = new C_s(C_bs.m_a_e96ea081("c6"));
      this.f_a_a_12b74.c$13462e();
      this.f_a_a_12b74.m_b_13462e(0);
      C_bf.m_a_9c60de72(this.f_a_a_12b74, false);
      this.f_a_a_12b74.m_a_48817c60(this.f_a_a_1570d10e, C_bd.f_bd_e_49);
      this.f_a_a_12b74.m_a_48817c60(this.f_a_b_1570d10e, C_bd.f_bd_f_49);
      this.f_a_a_12b74.m_a_6f63a2af(this);
   }

   public final void m_a_1385ff(boolean var1) {
      C_as.m_a_9b68();
      var1 = this.f_a_a_12b74.m_d_9b68();
      this.f_a_a_12b74.m_a_9b75();
      this.f_a_a_12b74
         .m_a_68a79fe2(C_bs.m_a_e96ea081("c5") + ":\n" + C_bs.m_a_e96ea081("q4") + "\n", var1, 1 + C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(11) + "\n", var1, C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(12) + "\n", var1, C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(13) + "\n\n", var1, C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_bs.m_a_e96ea081("m5") + " ", var1, 1 + C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(4) + "\n" + C_bs.m_a_e96ea081("q4") + "\n", var1, 1 + C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(21) + "\n", var1, C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(22) + "\n", var1, C_bp.f_bp_g_49, -1)
         .m_a_68a79fe2(C_as.m_a_47921032(23) + "\n", var1, C_bp.f_bp_g_49, -1);
      C_as.m_a_9b68();
      this.f_a_a_12b74.m_c_9b75();
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_a_a_1570d10e) {
         C_as.f_as_a_49 = 0;
         C_as.f_as_b_49 = 0;
         C_as.f_as_c_49 = 0;
         C_as.f_as_a_711fe21.setTime(new Date().getTime());

         try {
            C_as.m_a_9b75();
         } catch (Exception var3) {
         }

         this.m_a_1385ff(true);
      }

      if (var1 == this.f_a_b_1570d10e) {
         C_bf.m_a_9b75();
      }
   }
}
