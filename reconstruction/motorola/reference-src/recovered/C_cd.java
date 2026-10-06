package recovered;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

public final class C_cd implements C_ax, CommandListener {
   private static Command f_cd_a_1570d10e = new Command(C_bs.m_a_e96ea081("6"), 4, 1);
   private static Command f_cd_b_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 6);
   private static Command f_cd_c_1570d10e = new Command(C_bs.m_a_e96ea081("p0"), 8, 3);
   private static Command f_cd_d_1570d10e = new Command(C_bs.m_a_e96ea081("52"), 8, 3);
   private static Command f_cd_e_1570d10e = new Command(C_bs.m_a_e96ea081("02"), 8, 4);
   private static Command f_cd_f_1570d10e = new Command(C_bs.m_a_e96ea081("i1"), 8, 5);
   private static Command f_cd_g_1570d10e = new Command(C_bs.m_a_e96ea081("a"), 4, 1);
   private static Command f_cd_h_1570d10e = new Command(C_bs.m_a_e96ea081("a"), 4, 1);
   private static Command f_cd_i_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
   private static C_s f_cd_a_12b74;
   private static C_cd f_cd_a_240c2e;
   private static TextBox f_cd_a_fd805d5b;
   private static Vector f_cd_a_48a69a2c = new Vector();
   private static Object f_cd_a_5f790d9c;
   private static TextBox f_cd_b_fd805d5b;
   private static int f_cd_a_49;

   public C_cd() {
      p_cd_c_9b75();
      f_cd_a_240c2e = this;
   }

   public static void m_a_23a88fec(TextBox var0, Object var1) {
      f_cd_a_5f790d9c = var1;
      f_cd_b_fd805d5b = var0;
      f_cd_a_49 = var0.getCaretPosition();
      C_bf.m_a_9c60de72(f_cd_a_12b74 = new C_s(null), false);
      f_cd_a_12b74.m_c_aad3b1ff(C_bs.m_a_e96ea081("56"));
      f_cd_a_12b74.m_a_48817c60(C_bf.f_bf_h_1570d10e, C_bd.f_bd_e_49);
      f_cd_a_12b74.m_a_48817c60(f_cd_b_1570d10e, C_bd.f_bd_f_49);
      f_cd_a_12b74.m_a_48817c60(f_cd_c_1570d10e, C_bd.f_bd_g_49);
      if (f_cd_a_48a69a2c.size() > 0) {
         p_cd_e_9b75();
      }

      f_cd_a_12b74.m_b_13462e(8);
      p_cd_b_9b75();
      f_cd_a_12b74.d$1385ff();
      f_cd_a_12b74.m_a_6f63a2af(f_cd_a_240c2e);
      f_cd_a_12b74.m_a_cb37e88d(f_cd_a_240c2e);
      f_cd_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
      this.p_cd_a_9b75();
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_cd_b_1570d10e) {
         C_bf.m_b_5d527811(f_cd_a_5f790d9c);
         f_cd_a_12b74 = null;
      }

      if (var1 == f_cd_a_1570d10e) {
         this.p_cd_a_9b75();
      }

      if (var1 == f_cd_c_1570d10e) {
         (f_cd_a_fd805d5b = new TextBox(C_bs.m_a_e96ea081("W3"), null, 1000, 0)).addCommand(f_cd_g_1570d10e);
         f_cd_a_fd805d5b.addCommand(f_cd_i_1570d10e);
         f_cd_a_fd805d5b.setCommandListener(f_cd_a_240c2e);
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cd_a_fd805d5b);
      }

      if (var1 == f_cd_d_1570d10e) {
         (f_cd_a_fd805d5b = new TextBox(C_bs.m_a_e96ea081("52"), p_cd_a_73cf11cb(), 1000, 0)).addCommand(f_cd_h_1570d10e);
         f_cd_a_fd805d5b.addCommand(f_cd_i_1570d10e);
         f_cd_a_fd805d5b.setCommandListener(f_cd_a_240c2e);
         Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(f_cd_a_fd805d5b);
      }

      if (var1 == f_cd_g_1570d10e) {
         String var3;
         if ((var3 = f_cd_a_fd805d5b.getString()).length() > 0) {
            f_cd_a_48a69a2c.addElement(var3);
         }

         p_cd_g_9b75();
         p_cd_e_9b75();
      }

      if (var1 == f_cd_e_1570d10e) {
         f_cd_a_48a69a2c.removeElementAt(f_cd_a_12b74.m_b_9b68());
         p_cd_g_9b75();
         if (f_cd_a_48a69a2c.size() <= 0) {
            p_cd_f_9b75();
         }
      }

      if (var1 == f_cd_h_1570d10e) {
         String var4;
         if ((var4 = f_cd_a_fd805d5b.getString()).length() > 0) {
            f_cd_a_48a69a2c.setElementAt(var4, f_cd_a_12b74.m_b_9b68());
         } else {
            f_cd_a_48a69a2c.removeElementAt(f_cd_a_12b74.m_b_9b68());
         }

         p_cd_g_9b75();
      }

      if (var1 == f_cd_i_1570d10e) {
         f_cd_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
         f_cd_a_fd805d5b = null;
      }

      if (var1 == f_cd_f_1570d10e) {
         C_bf.a$4d6ecd91(C_bs.m_a_e96ea081("J0"), C_bs.m_a_e96ea081("i1") + "?", f_cd_a_240c2e, 2);
      }

      if (C_bf.m_a_48817c53(var1, 2) == 3) {
         f_cd_a_48a69a2c.removeAllElements();
         p_cd_d_9b75();
         C_bf.m_b_5d527811(f_cd_a_5f790d9c);
      }

      if (C_bf.m_a_48817c53(var1, 2) == 4) {
         f_cd_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      }
   }

   private void p_cd_a_9b75() {
      String var4 = null;
      if (f_cd_a_12b74.m_a_9b68() != 0) {
         var4 = p_cd_a_73cf11cb();
      }

      String var1 = (String)f_cd_a_48a69a2c.elementAt(f_cd_a_12b74.m_b_9b68());

      for (int var2 = f_cd_a_12b74.m_b_9b68(); var2 >= 1; var2--) {
         String var3 = (String)f_cd_a_48a69a2c.elementAt(var2 - 1);
         f_cd_a_48a69a2c.setElementAt(var3, var2);
      }

      f_cd_a_48a69a2c.setElementAt(var1, 0);
      p_cd_d_9b75();
      f_cd_a_12b74 = null;
      C_bf.m_b_5d527811(f_cd_a_5f790d9c);
      if (var4 != null) {
         f_cd_b_fd805d5b.insert(var4, f_cd_a_49);
      }
   }

   private static void p_cd_b_9b75() {
      f_cd_a_12b74.m_i_9b75();
      f_cd_a_12b74.m_a_9b75();
      int var0 = f_cd_a_48a69a2c.size();

      for (int var1 = 0; var1 < var0; var1++) {
         f_cd_a_12b74.m_a_68a79fe2((String)f_cd_a_48a69a2c.elementAt(var1), f_cd_a_12b74.m_d_9b68(), 0, var1).m_a_485a5b4c(var1);
      }

      f_cd_a_12b74.m_j_9b75();
   }

   private static void p_cd_c_9b75() {
      RecordStore var0 = null;
      f_cd_a_48a69a2c.removeAllElements();

      try {
         int var1 = (var0 = RecordStore.openRecordStore("tmpl", false)).getNumRecords();

         for (int var2 = 1; var2 <= var1; var2++) {
            byte[] var3;
            String var6 = C_cf.m_a_20e7da8(var3 = var0.getRecord(var2), 0, var3.length, true);
            f_cd_a_48a69a2c.addElement(var6);
         }
      } catch (Exception var5) {
      }

      try {
         var0.closeRecordStore();
      } catch (Exception var4) {
      }
   }

   private static void p_cd_d_9b75() {
      try {
         RecordStore.deleteRecordStore("tmpl");
      } catch (Exception var5) {
      }

      if (f_cd_a_48a69a2c.size() != 0) {
         RecordStore var0 = null;

         try {
            var0 = RecordStore.openRecordStore("tmpl", true);
            int var1 = f_cd_a_48a69a2c.size();

            for (int var2 = 0; var2 < var1; var2++) {
               String var10000 = (String)f_cd_a_48a69a2c.elementAt(var2);
               Object var3 = null;
               var3 = C_cf.m_a_44c4d6c8(var10000, true);
               var0.addRecord((byte[])var3, 0, ((Object[])var3).length);
            }
         } catch (Exception var6) {
         }

         try {
            var0.closeRecordStore();
         } catch (Exception var4) {
         }
      }
   }

   private static String p_cd_a_73cf11cb() {
      return (String)f_cd_a_48a69a2c.elementAt(f_cd_a_12b74.m_b_9b68());
   }

   private static void p_cd_e_9b75() {
      p_cd_f_9b75();
      f_cd_a_12b74.m_a_48817c60(f_cd_a_1570d10e, C_bd.f_bd_g_49);
      f_cd_a_12b74.m_a_48817c60(f_cd_d_1570d10e, C_bd.f_bd_g_49);
      f_cd_a_12b74.m_a_48817c60(f_cd_e_1570d10e, C_bd.f_bd_g_49);
      f_cd_a_12b74.m_a_48817c60(f_cd_f_1570d10e, C_bd.f_bd_g_49);
   }

   private static void p_cd_f_9b75() {
      f_cd_a_12b74.m_a_8eb9d703(f_cd_a_1570d10e);
      f_cd_a_12b74.m_a_8eb9d703(f_cd_d_1570d10e);
      f_cd_a_12b74.m_a_8eb9d703(f_cd_e_1570d10e);
      f_cd_a_12b74.m_a_8eb9d703(f_cd_f_1570d10e);
   }

   private static void p_cd_g_9b75() {
      p_cd_d_9b75();
      p_cd_b_9b75();
      f_cd_a_12b74.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      f_cd_a_fd805d5b = null;
   }
}
