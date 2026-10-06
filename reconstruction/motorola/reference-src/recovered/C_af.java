package recovered;

import java.util.Vector;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

public final class C_af extends Form implements CommandListener {
   private TextField f_af_a_a555694c = new TextField(C_bs.m_a_e96ea081("37"), "", 20, 0);
   private TextField f_af_b_a555694c = new TextField(C_bs.m_a_e96ea081("17"), "", 1000, 0);
   private ChoiceGroup f_af_a_aa0ede5b = new ChoiceGroup(null, 2);
   private Command f_af_a_1570d10e = new Command(C_bs.m_a_e96ea081("a"), 1, 1);
   private Command f_af_b_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
   private static Vector f_af_a_48a69a2c = new Vector();
   private static C_af f_af_a_2404ea;
   private int f_af_a_49 = -1;
   private boolean f_af_a_5a;

   private C_af() {
      super(C_bs.m_a_e96ea081("27"));
      this.f_af_a_aa0ede5b.append(C_bs.m_a_e96ea081("07"), null);
      this.f_af_a_aa0ede5b.append(C_bs.m_a_e96ea081("_3"), null);
      this.append(this.f_af_a_a555694c);
      this.append(this.f_af_b_a555694c);
      this.append(this.f_af_a_aa0ede5b);
      this.addCommand(this.f_af_a_1570d10e);
      this.addCommand(this.f_af_b_1570d10e);
      this.setCommandListener(this);
   }

   public static void m_a_13462e(int var0) {
      if (f_af_a_2404ea == null) {
         f_af_a_2404ea = new C_af();
      }

      C_af var2 = f_af_a_2404ea;
      f_af_a_2404ea.f_af_a_49 = var0 - 1;
      var2.f_af_a_5a = C_bp.m_a_134632(171);
      String var3 = var2.p_af_a_47921032(var2.f_af_a_49);
      var2.f_af_a_a555694c.setString(var3.substring(0, var3.indexOf("\t")));
      var2.f_af_b_a555694c.setString(var3.substring(var3.indexOf("\t") + 1));
      var2.f_af_a_aa0ede5b.setSelectedIndex(0, C_bp.m_a_134632(159));
      var2.f_af_a_aa0ede5b.setSelectedIndex(1, var2.f_af_a_5a);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(var2);
      C_bt.m_a_1385ff(true);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == this.f_af_a_1570d10e) {
         C_bp.m_a_4f708078(32, this.f_af_a_a555694c.getString());
         C_bp.m_a_4f708078(33, this.f_af_b_a555694c.getString());
         C_bp.m_a_2563266(159, this.f_af_a_aa0ede5b.isSelected(0));
         C_bp.m_a_2563266(171, this.f_af_a_aa0ede5b.isSelected(1));
         C_bp.m_a_255f295(92, this.f_af_a_49);
         C_bp.m_c_9b75();
         String var7 = this.f_af_a_a555694c.getString() + "\t" + this.f_af_b_a555694c.getString();
         if (!this.p_af_a_47921032(this.f_af_a_49).equals(var7)) {
            f_af_a_48a69a2c.setElementAt(var7, this.f_af_a_49);

            try {
               RecordStore.deleteRecordStore("xtraz");
            } catch (Exception var6) {
            }

            RecordStore var8 = null;

            try {
               var8 = RecordStore.openRecordStore("xtraz", true);
               byte[] var9 = C_cf.m_a_44c4d6c8(p_af_a_73cf11cb(), true);
               var8.addRecord(var9, 0, var9.length);
            } catch (Exception var5) {
            }

            try {
               var8.closeRecordStore();
            } catch (Exception var4) {
            }
         }

         C_bn.m_a_9b75();
         if (C_ac.m_b_9b79()) {
            C_v.m_a_9b75();

            try {
               C_al.m_a_9b75();
               if (this.f_af_a_5a != C_bp.m_a_134632(171)) {
                  C_al.m_a_13462e(C_ac.m_d_9b68() | (int)C_bp.a$134622());
               }
            } catch (C_aq var3) {
               C_aq.m_a_481c933f(var3);
            }
         } else {
            C_bn.m_b_9b75();
         }

         C_cn.m_a_48a013c6(C_ac.m_a_2477940());
      } else {
         C_bn.m_b_9b75();
      }
   }

   private String p_af_a_47921032(int var1) {
      RecordStore var6 = null;
      f_af_a_48a69a2c.removeAllElements();

      try {
         if ((var6 = RecordStore.openRecordStore("xtraz", true)).getNumRecords() <= 0) {
            for (int var2 = 0; var2 <= C_w.m_a_9b68(); var2++) {
               String var3 = C_w.m_a_47921032(var2) + "\t" + "";
               f_af_a_48a69a2c.addElement(var3);
            }
         } else {
            p_af_a_25e06fc(var6.getRecord(1));
         }
      } catch (Exception var5) {
      }

      try {
         var6.closeRecordStore();
      } catch (Exception var4) {
      }

      return (String)f_af_a_48a69a2c.elementAt(var1);
   }

   private static String p_af_a_73cf11cb() {
      StringBuffer var0 = new StringBuffer();

      for (int var1 = 0; var1 < C_w.m_a_9b68(); var1++) {
         var0.append((String)f_af_a_48a69a2c.elementAt(var1)).append("\t\r");
      }

      return var0.toString();
   }

   private static void p_af_a_25e06fc(byte[] var0) {
      String var3 = C_cf.m_a_20e7da8(var0, 0, var0.length, true);
      int var1 = 0;
      int var2 = 0;

      do {
         var2 = var3.indexOf("\t\r", var1);
         String var4 = var3.substring(var1, var2);
         f_af_a_48a69a2c.addElement(var4);
         var1 = var2 + 2;
      } while (var3.indexOf("\t\r", var1) != -1);
   }
}
