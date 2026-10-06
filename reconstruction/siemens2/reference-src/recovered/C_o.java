package recovered;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import jimm.Jimm;

public final class C_o implements CommandListener {
   private TextBox f_o_a_fd805d5b;
   private TextBox f_o_b_fd805d5b;
   private static Command f_o_a_1570d10e = new Command(C_bs.m_a_e96ea081("4"), 2, 1);
   private static Command f_o_b_1570d10e = new Command(C_bs.m_a_e96ea081("15"), 8, 3);
   private static Command f_o_c_1570d10e = new Command(C_bs.m_a_e96ea081("x3"), 8, 2);
   private static Command f_o_d_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 8, 1);
   private static C_o f_o_a_12af8;

   public static void m_a_9b75() {
      if (f_o_a_12af8 == null) {
         f_o_a_12af8 = new C_o();
      }

      C_o var0;
      (var0 = f_o_a_12af8).f_o_b_fd805d5b = new TextBox(C_bs.m_a_e96ea081("p4"), "", 30, 3);
      var0.f_o_b_fd805d5b.addCommand(f_o_b_1570d10e);
      var0.f_o_b_fd805d5b.addCommand(f_o_c_1570d10e);
      var0.f_o_b_fd805d5b.addCommand(f_o_a_1570d10e);
      var0.f_o_b_fd805d5b.setCommandListener(var0);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var0.f_o_b_fd805d5b);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 != f_o_c_1570d10e) {
         if (var1 == f_o_b_1570d10e) {
            if (this.f_o_b_fd805d5b.getString().length() > 0) {
               String var8 = this.f_o_b_fd805d5b.getString();
               this.f_o_a_fd805d5b = new TextBox("SMS " + var8, "", 500, 0);
               this.f_o_a_fd805d5b.addCommand(f_o_d_1570d10e);
               this.f_o_a_fd805d5b.addCommand(f_o_a_1570d10e);
               this.f_o_a_fd805d5b.setCommandListener(this);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_o_a_fd805d5b);
               return;
            }
         } else {
            if (var1 == f_o_d_1570d10e) {
               String var9 = this.f_o_a_fd805d5b.getString();
               String var5 = this.f_o_b_fd805d5b.getString();
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

               C_bo.m_b_9b75();
               return;
            }

            if (var1 == f_o_a_1570d10e) {
               C_bo.m_b_9b75();
            }
         }
      } else {
         String var7;
         if ((var7 = this.f_o_b_fd805d5b.getString()).length() > 0) {
            try {
               Jimm.f_jimm_Jimm_a_3fbeb738.platformRequest("tel:" + var7);
               return;
            } catch (Exception var4) {
            }
         }
      }
   }
}
