package recovered;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDletStateChangeException;
import jimm.Jimm;

public final class C_av extends Form implements CommandListener {
   private TextBox f_av_a_fd805d5b = new TextBox("", "", 20, 65536);
   private Command f_av_a_1570d10e = new Command(C_bs.m_a_e96ea081("6"), 4, 1);
   private Command f_av_b_1570d10e = new Command(C_bs.m_a_e96ea081("3"), 2, 2);
   private Displayable f_av_a_4d585e5d;
   private static C_av f_av_a_2406da;

   private C_av(Displayable var1) {
      super(null);
      this.f_av_a_4d585e5d = var1;
      this.f_av_a_fd805d5b.setTitle(C_bs.m_a_e96ea081("52"));
      this.f_av_a_fd805d5b.addCommand(this.f_av_a_1570d10e);
      this.f_av_a_fd805d5b.addCommand(this.f_av_b_1570d10e);
      this.f_av_a_fd805d5b.setCommandListener(this);
   }

   public static void m_a_51c0f892(Displayable var0) {
      if (f_av_a_2406da == null) {
         f_av_a_2406da = new C_av(var0);
      }

      C_av var1 = f_av_a_2406da;
      if (C_bp.m_a_47921032(38).length() > 0 && Jimm.f_jimm_Jimm_c_5a) {
         var1.f_av_a_fd805d5b.setString("");
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var1.f_av_a_fd805d5b);
      } else if (C_ac.m_a_9b79()) {
         p_av_a_9b75();
      } else {
         C_w.m_a_9b75();
      }
   }

   private static void p_av_a_9b75() {
      if (C_bp.m_a_134632(138)) {
         C_ac.f_ac_b_49 = C_bp.m_a_134621(91);
         C_w.m_c_9b75();
         C_ac.m_a_9b75();
      } else {
         C_bo.m_b_9b75();
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_av_a_1570d10e) {
         if (C_bp.m_a_47921032(38).equals(this.f_av_a_fd805d5b.getString())) {
            if (C_ac.m_a_9b79()) {
               p_av_a_9b75();
            } else {
               C_cn.m_d_9b75();
               C_cn.f_cn_a_4a = 0L;
            }

            Jimm.f_jimm_Jimm_c_5a = false;
            return;
         }
      } else {
         if (C_ac.m_a_9b79() && !C_cn.m_a_9b79()) {
            try {
               Jimm.f_jimm_Jimm_a_3fbeb738.destroyApp(true);
               return;
            } catch (MIDletStateChangeException var3) {
               return;
            }
         }

         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_av_a_4d585e5d);
      }
   }
}
