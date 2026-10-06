package recovered;

/** 0.6 source correspondence (inferred): jimm.EnterPassword. Release class: az. */

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDletStateChangeException;
import jimm.Jimm;

public final class C_az extends Form implements CommandListener {
   private TextBox f_az_a_fd805d5b = new TextBox("", "", 20, 65536);
   private Command f_az_a_1570d10e = new Command(C_bt.m_a_e96ea081("8"), 4, 1);
   private Command f_az_b_1570d10e = new Command(C_bt.m_a_e96ea081("5"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 2);
   private Displayable f_az_a_4d585e5d;
   private static C_az f_az_a_240756;

   private C_az(Displayable var1) {
      super(null);
      this.f_az_a_4d585e5d = var1;
      this.f_az_a_fd805d5b.setTitle(C_bt.m_a_e96ea081("72"));
      this.f_az_a_fd805d5b.addCommand(this.f_az_a_1570d10e);
      this.f_az_a_fd805d5b.addCommand(this.f_az_b_1570d10e);
      this.f_az_a_fd805d5b.setCommandListener(this);
   }

   public static void m_a_51c0f892(Displayable var0) {
      if (f_az_a_240756 == null) {
         f_az_a_240756 = new C_az(var0);
      }

      C_az var1 = f_az_a_240756;
      if (C_bq.m_a_47921032(38).length() > 0 && Jimm.f_jimm_Jimm_g_5a) {
         var1.f_az_a_fd805d5b.setString("");
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var1.f_az_a_fd805d5b);
         C_bu.m_a_1385ff(true);
      } else if (C_ac.m_a_9b79()) {
         p_az_a_9b75() ;
      } else {
         C_w.m_a_9b75();
      }
   }

   private static void p_az_a_9b75() {
      if (C_bq.m_a_134632(138)) {
         C_ac.f_ac_b_49 = C_bq.m_a_134621(91);
         C_w.m_c_9b75();
         C_ac.m_a_9b75();
      } else {
         C_bp.m_b_9b75();
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_az_a_1570d10e) {
         if (C_bq.m_a_47921032(38).equals(this.f_az_a_fd805d5b.getString())) {
            if (C_ac.m_a_9b79()) {
               p_az_a_9b75() ;
            } else {
               C_cp.m_d_9b75();
               C_cp.f_cp_a_4a = 0L;
            }

            Jimm.f_jimm_Jimm_g_5a = false;
            return;
         }
      } else {
         if (C_ac.m_a_9b79() && !C_cp.m_a_9b79()) {
            try {
               Jimm.f_jimm_Jimm_a_3fbeb738.destroyApp(true);
               return;
            } catch (MIDletStateChangeException var3) {
               return;
            }
         }

         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_az_a_4d585e5d);
      }
   }
}
