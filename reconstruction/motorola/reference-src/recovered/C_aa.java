package recovered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

public final class C_aa {
   private static RecordStore f_aa_a_a0e37fd2;
   public static C_q f_aa_a_12b36;
   private static String f_aa_a_523beb0a = new String();
   private static Hashtable f_aa_a_d18d4967;

   public C_aa() {
      try {
         RecordStore.deleteRecordStore("B7");
      } catch (Exception var1) {
      }
   }

   public static synchronized void m_a_48303787(String var0, String var1, byte var2, String var3, long var4) {
      boolean var6 = false;
      RecordStore var7 = null;
      if (f_aa_a_12b36 != null) {
         if (f_aa_a_12b36.m_a_9b68() == 0) {
            var6 = true;
         } else if (f_aa_a_12b36.m_g_9b68() == f_aa_a_12b36.m_a_9b68() - 1) {
            var6 = true;
         }
      }

      boolean var8 = f_aa_a_523beb0a.equals(var0);

      try {
         var7 = var8 ? f_aa_a_a0e37fd2 : RecordStore.openRecordStore(p_aa_a_e96ea081(var0), true);
         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         DataOutputStream var10;
         (var10 = new DataOutputStream(var9)).writeUTF(var3);
         var10.writeUTF(var1);
         var10.writeUTF(C_cf.m_a_87d767d1(false, true, var4));
         byte[] var12;
         byte[] var13;
         int var14;
         C_cf.m_a_e3069c1d(var12 = new byte[(var14 = (var13 = var9.toByteArray()).length) + 1], 0, var7.getNextRecordID());
         var12[0] = var2;
         System.arraycopy(var13, 0, var12, 1, var14);
         var7.addRecord(var12, 0, var12.length);
         if (!var8 && var7 != null) {
            var7.closeRecordStore();
         }
      } catch (Exception var11) {
      }

      if (f_aa_a_12b36 != null && C_q.m_a_73cf11cb().equals(var0)) {
         f_aa_a_12b36.m_c_9b75();
         if (var6) {
            f_aa_a_12b36.m_h_13462e(f_aa_a_12b36.m_a_9b68() - 1);
         }
      }
   }

   static RecordStore m_a_fbacd853() {
      return f_aa_a_a0e37fd2;
   }

   private static String p_aa_a_e96ea081(String var0) {
      return "hist" + var0;
   }

   private static void p_aa_c_aad3b1ff(String var0) {
      if (!f_aa_a_523beb0a.equals(var0)) {
         try {
            if (f_aa_a_a0e37fd2 != null) {
               f_aa_a_a0e37fd2.closeRecordStore();
               f_aa_a_a0e37fd2 = null;
               System.gc();
            }

            f_aa_a_a0e37fd2 = RecordStore.openRecordStore(p_aa_a_e96ea081(var0), true);
         } catch (Exception var1) {
            f_aa_a_a0e37fd2 = null;
            return;
         }

         f_aa_a_523beb0a = var0;
         if (f_aa_a_d18d4967 == null) {
            f_aa_a_d18d4967 = new Hashtable();
         }
      }
   }

   public static int m_a_aad3b1f2(String var0) {
      p_aa_c_aad3b1ff(var0);

      try {
         var2 = f_aa_a_a0e37fd2.getNumRecords();
      } catch (Exception var1) {
         var2 = 0;
      }

      return var2;
   }

   public static synchronized C_ba m_a_9e194c2(String var0, int var1) {
      p_aa_c_aad3b1ff(var0);
      C_ba var2 = new C_ba();

      try {
         byte[] var4 = f_aa_a_a0e37fd2.getRecord(var1 + 1);
         var2.f_ba_a_42 = var4[0];
         ByteArrayInputStream var5 = new ByteArrayInputStream(var4, 1, var4.length - 1);
         DataInputStream var6 = new DataInputStream(var5);
         var2.f_ba_d_523beb0a = var6.readUTF();
         var2.f_ba_b_523beb0a = var6.readUTF();
         var2.f_ba_c_523beb0a = var6.readUTF();
         if (C_cf.m_a_dfd94fa3(var2.f_ba_b_523beb0a) != null) {
            var2.f_ba_a_5a = true;
         } else {
            var2.f_ba_a_5a = false;
         }

         return var2;
      } catch (Exception var3) {
         var2.f_ba_b_523beb0a = var2.f_ba_c_523beb0a = var2.f_ba_d_523beb0a = "b2";
         return null;
      }
   }

   public static C_ba m_b_9e194c2(String var0, int var1) {
      C_ba var2;
      if ((var2 = (C_ba)f_aa_a_d18d4967.get(new Integer(var1))) != null) {
         return var2;
      } else if ((var2 = m_a_9e194c2(var0, var1)) == null) {
         return null;
      } else {
         if (var2.f_ba_b_523beb0a.length() > 20) {
            var2.f_ba_a_523beb0a = var2.f_ba_b_523beb0a.substring(0, 20) + "...";
         } else {
            var2.f_ba_a_523beb0a = var2.f_ba_b_523beb0a;
         }

         var2.f_ba_a_523beb0a = var2.f_ba_a_523beb0a.replace('\n', ' ');
         var2.f_ba_a_523beb0a = var2.f_ba_a_523beb0a.replace('\r', ' ');
         f_aa_a_d18d4967.put(new Integer(var1), var2);
         return var2;
      }
   }

   public static void m_a_e925fa09(String var0, String var1) {
      if (f_aa_a_12b36 == null) {
         var1 = C_bs.m_a_e96ea081("B7");
         (f_aa_a_12b36 = new C_q()).m_c_aad3b1ff(var1);
      }

      C_q.a$16da05f7(var0);
      f_aa_a_12b36.m_i_9b75();
      if (f_aa_a_12b36.m_a_9b68() != 0) {
         f_aa_a_12b36.m_h_13462e(f_aa_a_12b36.m_a_9b68() - 1);
      }

      f_aa_a_12b36.m_j_9b75();
      C_bf.m_a_5d527811(f_aa_a_12b36);
      f_aa_a_12b36.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public static synchronized void m_a_aad3b1ff(String var0) {
      try {
         p_aa_c_aad3b1ff(var0);
         f_aa_a_a0e37fd2.closeRecordStore();
         f_aa_a_a0e37fd2 = null;
         System.gc();
         RecordStore.deleteRecordStore(p_aa_a_e96ea081(var0));
         if (f_aa_a_d18d4967 != null) {
            f_aa_a_d18d4967.clear();
         }

         f_aa_a_523beb0a = new String();
      } catch (Exception var1) {
      }
   }

   public static void m_a_9b75() {
      if (f_aa_a_d18d4967 != null) {
         f_aa_a_d18d4967.clear();
         f_aa_a_d18d4967 = null;
      }

      f_aa_a_12b36 = null;
      f_aa_a_523beb0a = new String();
   }

   public static void m_b_9b75() {
      if (f_aa_a_12b36 != null) {
         C_bf.m_a_9c60de72(f_aa_a_12b36, false);
         if (C_q.f_q_a_12b74 != null) {
            C_bf.m_a_9c60de72(C_q.f_q_a_12b74, false);
         }
      }
   }

   private static synchronized boolean p_aa_a_37a5f64d(String var0, String var1, boolean var2, boolean var3) {
      int var4;
      if ((var4 = f_aa_a_12b36.m_g_9b68()) >= 0 && var4 < f_aa_a_12b36.m_a_9b68()) {
         if (!var2) {
            var1 = var1.toLowerCase();
         }

         int var5 = m_a_aad3b1f2(var0);

         while (var4 >= 0 && var4 < var5) {
            C_ba var6 = m_a_9e194c2(var0, var4);
            if ((var2 ? var6.f_ba_b_523beb0a : var6.f_ba_b_523beb0a.toLowerCase()).indexOf(var1) != -1) {
               f_aa_a_12b36.m_h_13462e(var4);
               f_aa_a_12b36.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
               return true;
            }

            if (var3) {
               var4--;
            } else {
               var4++;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   static void m_a_37a5f649(String var0, String var1, boolean var2, boolean var3) {
      if (f_aa_a_12b36 != null) {
         boolean var10000 = p_aa_a_37a5f64d(var0, var1, var2, var3);
         boolean var4 = false;
         if (!var10000) {
            Alert var5;
            (var5 = new Alert(C_bs.m_a_e96ea081("w2"), var1 + "\n" + C_bs.m_a_e96ea081("C7"), null, AlertType.INFO)).setTimeout(-2);
            f_aa_a_12b36.m_a_75ca2789(Jimm.f_jimm_Jimm_a_4a58c677, var5);
         }
      }
   }

   static synchronized void m_b_aad3b1ff(String var0) {
      var0 = var0 == null ? null : p_aa_a_e96ea081(var0);

      try {
         if (f_aa_a_a0e37fd2 != null) {
            f_aa_a_a0e37fd2.closeRecordStore();
            f_aa_a_a0e37fd2 = null;
            System.gc();
            f_aa_a_523beb0a = new String();
         }

         String[] var1 = RecordStore.listRecordStores();

         for (int var2 = 0; var2 < var1.length; var2++) {
            String var3;
            if ((var3 = var1[var2]).indexOf("hist") != -1 && (var0 == null || !var0.equals(var3))) {
               RecordStore.deleteRecordStore(var3);
            }
         }
      } catch (Exception var4) {
      }
   }
}
