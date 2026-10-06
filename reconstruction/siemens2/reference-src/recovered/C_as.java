package recovered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

public final class C_as {
   private static int f_as_d_49;
   static int f_as_a_49;
   static int f_as_b_49;
   private static volatile int f_as_e_49;
   private static volatile int f_as_f_49;
   private static volatile int f_as_g_49;
   static Date f_as_a_711fe21;
   private static Date f_as_b_711fe21;
   static int f_as_c_49;
   private static int f_as_h_49;
   public static C_a f_as_a_12946;

   public C_as() {
      f_as_f_49 = 0;
      f_as_g_49 = 0;
      f_as_c_49 = 0;
      f_as_b_711fe21 = new Date(1L);
      f_as_h_49 = 0;
      f_as_a_711fe21 = new Date();

      try {
         RecordStore var3;
         byte[] var1 = (var3 = RecordStore.openRecordStore("traffic", false)).getRecord(2);
         ByteArrayInputStream var4 = new ByteArrayInputStream(var1);
         DataInputStream var5;
         f_as_a_49 = (var5 = new DataInputStream(var4)).readInt();
         f_as_b_49 = var5.readInt();
         f_as_a_711fe21.setTime(var5.readLong());
         f_as_b_711fe21.setTime(var5.readLong());
         f_as_c_49 = var5.readInt();
         var3.closeRecordStore();
      } catch (Exception var2) {
         f_as_a_711fe21.setTime(new Date().getTime());
         f_as_a_49 = 0;
         f_as_b_49 = 0;
      }

      f_as_a_12946 = new C_a();
   }

   public static void m_a_9b75() {
      RecordStore var0 = RecordStore.openRecordStore("traffic", true);

      while (var0.getNumRecords() < 4) {
         var0.addRecord(null, 0, 0);
      }

      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var10000 = new DataOutputStream(var1);
      DataOutputStream var2 = null;
      var10000.writeUTF(Jimm.f_jimm_Jimm_a_523beb0a);
      byte[] var3 = var1.toByteArray();
      var0.setRecord(1, var3, 0, var3.length);
      var1 = new ByteArrayOutputStream();
      (var2 = new DataOutputStream(var1)).writeInt(f_as_a_49 + f_as_f_49);
      var2.writeInt(f_as_b_49 + f_as_g_49);
      var2.writeLong(f_as_a_711fe21.getTime());
      var2.writeLong(f_as_b_711fe21.getTime());
      var2.writeInt(p_as_a_1385f2(false));
      byte[] var5 = var1.toByteArray();
      var0.setRecord(2, var5, 0, var5.length);
      var0.closeRecordStore();
   }

   protected static String m_a_47921032(int var0) {
      Calendar var1;
      (var1 = Calendar.getInstance()).setTime(f_as_a_711fe21);
      int var2 = f_as_b_49 + f_as_g_49;
      int var3 = f_as_a_49 + f_as_f_49;
      f_as_d_49 = var2 + var3;
      switch (var0) {
         case 4:
            return C_ce.m_a_47921032(var1.get(5)) + "." + C_ce.m_a_47921032(var1.get(2) + 1) + "." + var1.get(1);
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         default:
            return "";
         case 11:
            return f_as_g_49 + " /" + f_as_f_49 + " /" + f_as_e_49 + " B";
         case 12:
            return f_as_g_49 / 1024 + " /" + f_as_f_49 / 1024 + " /" + f_as_e_49 / 1024 + " Kb";
         case 13:
            return m_b_47921032(p_as_a_1385f2(true)) + " " + C_bp.m_a_47921032(6);
         case 21:
            return var2 + " /" + var3 + " /" + f_as_d_49 + " B";
         case 22:
            return var2 / 1024 + " /" + var3 / 1024 + " /" + f_as_d_49 / 1024 + " Kb";
         case 23:
            return m_b_47921032(p_as_a_1385f2(false)) + " " + C_bp.m_a_47921032(6);
      }
   }

   public static String m_b_47921032(int var0) {
      Object var1 = null;
      Object var2 = null;

      try {
         if (var0 == 0) {
            return new String("0.00");
         } else {
            var1 = Integer.toString(var0 / 10000) + ".";
            String var5 = Integer.toString(var0 % 10000);

            while (var5.length() != 4) {
               var5 = "0" + var5;
            }

            while (var5.endsWith("0") && var5.length() > 2) {
               var5 = var5.substring(0, var5.length() - 1);
            }

            return var1 + var5;
         }
      } catch (Exception var3) {
         return new String("0.00");
      }
   }

   private static int p_as_a_1385f2(boolean var0) {
      int var1 = C_bp.m_a_134621(70);
      int var2 = C_bp.m_a_134621(72);
      if (var0) {
         if (f_as_e_49 != 0) {
            var0 = (f_as_e_49 / var2 + 1) * var1;
         } else {
            var0 = 0;
         }
      } else if (f_as_e_49 != 0) {
         var0 = (f_as_e_49 / var2 + 1) * var1 + f_as_c_49;
      } else {
         var0 = f_as_c_49;
      }

      Calendar var4 = Calendar.getInstance();
      Calendar var5 = Calendar.getInstance();
      var4.setTime(new Date());
      var5.setTime(f_as_b_711fe21);
      if ((var4.get(5) != var5.get(5) || var4.get(2) != var5.get(2) || var4.get(1) != var5.get(1)) && f_as_e_49 != 0 && f_as_h_49 == 0) {
         f_as_h_49 = f_as_h_49 + C_bp.m_a_134621(71);
         f_as_b_711fe21.setTime(new Date().getTime());
      }

      return var0 + f_as_h_49;
   }

   public static int m_a_9b68() {
      return f_as_e_49 = f_as_g_49 + f_as_f_49;
   }

   public static void m_a_13462e(int var0) {
      f_as_f_49 += var0;
      C_w.m_a_13462e(m_a_9b68());
   }

   public static void m_b_13462e(int var0) {
      f_as_g_49 += var0;
      C_w.m_a_13462e(m_a_9b68());
   }
}
