package recovered;

/** 0.6 source correspondence (inferred): jimm.util.PhoneBook. Release class: p. */

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import jimm.Jimm;

public final class C_p implements CommandListener {
   private TextBox f_p_a_fd805d5b;
   private TextBox f_p_b_fd805d5b;
   private static Command f_p_a_1570d10e = new Command(C_bt.m_a_e96ea081("6"), 2, 1);
   private static Command f_p_b_1570d10e = new Command(C_bt.m_a_e96ea081("35"), 8, 3);
   private static Command f_p_c_1570d10e = new Command(C_bt.m_a_e96ea081("z3"), 8, 2);
   private static Command f_p_d_1570d10e = new Command(C_bt.m_a_e96ea081("b"), 8, 1);
   private static C_p f_p_a_12b17;

   public static void m_a_9b75() {
      if (f_p_a_12b17 == null) {
         f_p_a_12b17 = new C_p();
      }

      C_p var0;
      (var0 = f_p_a_12b17).f_p_b_fd805d5b = new TextBox(C_bt.m_a_e96ea081("r4"), "", 30, 3);
      var0.f_p_b_fd805d5b.addCommand(f_p_b_1570d10e);
      var0.f_p_b_fd805d5b.addCommand(f_p_c_1570d10e);
      var0.f_p_b_fd805d5b.addCommand(f_p_a_1570d10e);
      var0.f_p_b_fd805d5b.setCommandListener(var0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var0.f_p_b_fd805d5b);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 != f_p_c_1570d10e) {
         if (var1 == f_p_b_1570d10e) {
            if (this.f_p_b_fd805d5b.getString().length() > 0) {
               String var8 = this.f_p_b_fd805d5b.getString();
               this.f_p_a_fd805d5b = new TextBox("SMS " + var8, "", 500, 0);
               this.f_p_a_fd805d5b.addCommand(f_p_d_1570d10e);
               this.f_p_a_fd805d5b.addCommand(f_p_a_1570d10e);
               this.f_p_a_fd805d5b.setCommandListener(this);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_p_a_fd805d5b);
               return;
            }
         } else {
            if (var1 == f_p_d_1570d10e) {
               String var9 = this.f_p_a_fd805d5b.getString();
               String var5 = this.f_p_b_fd805d5b.getString();
               if (var9.length() > 0 && var5.length() > 0) {
                  try {
                     MessageConnection var6;
                     TextMessage var10;
                     (var10 = (TextMessage)(var6 = (MessageConnection)Connector.open("sms://" + var5)).newMessage("text")).setPayloadText(var9);
                     var6.send(var10);
                     var6.close();
                  } catch (Exception var3) {
                  }
               }

               C_bp.m_b_9b75();
               return;
            }

            if (var1 == f_p_a_1570d10e) {
               C_bp.m_b_9b75();
            }
         }
      } else {
         String var7;
         if ((var7 = this.f_p_b_fd805d5b.getString()).length() > 0) {
            try {
               Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest("tel:" + var7);
               return;
            } catch (Exception var4) {
            }
         }
      }
   }
}
