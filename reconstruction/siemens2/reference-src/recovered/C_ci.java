package recovered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordStore;

public final class C_ci implements Runnable {
   private static C_ci f_ci_a_240cc9 = new C_ci();
   private static Vector f_ci_a_48a69a2c = new Vector();
   private static Vector f_ci_b_48a69a2c = new Vector();
   private static Vector f_ci_c_48a69a2c = new Vector();
   private static long f_ci_a_4a;

   public static void m_a_9b75() {
      new Thread(f_ci_a_240cc9).start();
   }

   public final void run() {
      C_ck.f_ck_a_4a = C_ce.m_a_25deca9(false, true);
      int var10000 = C_w.m_c_9b68();
      boolean var3 = false;

      for (int var4 = var10000 - 1; var4 >= 0; var4--) {
         C_aw var1;
         (var1 = C_w.m_a_c2f0d221(var4)).m_a_255f295(196, m_a_aad3b1f2(var1.m_b_73cf11cb()));

         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
         }
      }
   }

   public static void m_a_44bd8e9f(String var0, int var1, int var2) {
      m_a_aad3b1ff(var0);
      C_aw var3 = C_w.m_a_513388b0(var0);
      if (f_ci_a_48a69a2c.size() <= 0) {
         p_ci_b_9b75();
      }

      if (var3 != null && f_ci_a_48a69a2c.indexOf(var0) == -1 && var1 != 0 && var2 != 0 && !var3.m_a_134632(8) && !var3.m_a_134632(32)) {
         f_ci_a_48a69a2c.addElement(var0);
         f_ci_b_48a69a2c.addElement(String.valueOf(var1));
         f_ci_c_48a69a2c.addElement(String.valueOf(var2));
         var3.m_a_255f295(196, m_a_aad3b1f2(var0));
         p_ci_c_9b75();
      }
   }

   public static void m_a_aad3b1ff(String var0) {
      int var1;
      if ((var1 = f_ci_a_48a69a2c.indexOf(var0)) != -1) {
         f_ci_a_48a69a2c.removeElementAt(var1);
         f_ci_b_48a69a2c.removeElementAt(var1);
         f_ci_c_48a69a2c.removeElementAt(var1);
         p_ci_c_9b75();
      }
   }

   public static int m_a_aad3b1f2(String var0) {
      if (f_ci_a_48a69a2c.size() <= 0) {
         p_ci_b_9b75();
      }

      int var5;
      if ((var5 = f_ci_a_48a69a2c.indexOf(var0)) == -1) {
         return var5;
      } else {
         int var1 = Integer.parseInt((String)f_ci_b_48a69a2c.elementAt(var5));
         int var6 = Integer.parseInt((String)f_ci_c_48a69a2c.elementAt(var5));
         int var2;
         if ((f_ci_a_4a = C_ce.m_a_6046c8c9(var2 = C_ce.m_a_255f4d5(C_ck.f_ck_a_4a)[5], var6, var1, 0, 0, 0)) < C_ck.f_ck_a_4a) {
            f_ci_a_4a = C_ce.m_a_6046c8c9(var2 + 1, var6, var1, 0, 0, 0);
         }

         long var3 = C_ck.f_ck_a_4a;

         for (int var7 = 0; var7 < 3; var7++) {
            if (var3 == f_ci_a_4a) {
               return var7;
            }

            var3 += 86400L;
         }

         return -1;
      }
   }

   private static void p_ci_b_9b75() {
      try {
         RecordStore var0;
         byte[] var1 = (var0 = RecordStore.openRecordStore("birthday", false)).getRecord(1);
         ByteArrayInputStream var3 = new ByteArrayInputStream(var1);
         DataInputStream var4 = new DataInputStream(var3);

         while (var4.available() > 0) {
            var4.readUnsignedByte();
            f_ci_a_48a69a2c.addElement(String.valueOf(var4.readInt()));
            var4.readUnsignedByte();
            f_ci_b_48a69a2c.addElement(String.valueOf(var4.readInt()));
            var4.readUnsignedByte();
            f_ci_c_48a69a2c.addElement(String.valueOf(var4.readInt()));
         }

         var0.closeRecordStore();
      } catch (Exception var2) {
      }
   }

   private static void p_ci_c_9b75() {
      try {
         RecordStore var0 = RecordStore.openRecordStore("birthday", true);

         while (var0.getNumRecords() < 2) {
            var0.addRecord(null, 0, 0);
         }

         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);
         int var3 = f_ci_b_48a69a2c.size() * 3;
         int var4 = 0;

         for (int var5 = 0; var3 > var4; var5++) {
            var2.writeByte(var4);
            var2.writeInt(Integer.parseInt((String)f_ci_a_48a69a2c.elementAt(var5)));
            var2.writeByte(++var4);
            var2.writeInt(Integer.parseInt((String)f_ci_b_48a69a2c.elementAt(var5)));
            var2.writeByte(++var4);
            var2.writeInt(Integer.parseInt((String)f_ci_c_48a69a2c.elementAt(var5)));
            var4++;
         }

         byte[] var7 = var1.toByteArray();
         var0.setRecord(1, var7, 0, var7.length);
         var0.closeRecordStore();
      } catch (Exception var6) {
      }
   }
}
