package recovered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotFoundException;
import jimm.Jimm;

public final class C_v implements C_ax, CommandListener, PlayerListener, C_k {
   private static boolean f_v_b_5a = false;
   private static C_v f_v_a_12bd1;
   private static Command f_v_a_1570d10e = new Command(C_bs.m_a_e96ea081("H3"), 2, 3);
   private static int f_v_a_49 = -1;
   private static int f_v_b_49 = 0;
   private static boolean f_v_c_5a;
   private static Vector f_v_a_48a69a2c;
   private static Vector f_v_b_48a69a2c;
   private static boolean f_v_d_5a = false;
   private static boolean f_v_e_5a;
   private static Hashtable f_v_a_d18d4967 = new Hashtable();
   public static C_c f_v_a_12984;
   public static final C_h f_v_a_12a1f = C_h.m_a_44af1588("/icons.png");
   public static final C_h f_v_b_12a1f = C_h.m_a_44af1588("/micons.png");
   public static final C_h f_v_c_12a1f = C_h.m_a_44af1588("/clicons.png");
   private static C_h f_v_f_12a1f = C_h.m_a_44af1588("/prlists.png");
   private static C_h f_v_g_12a1f = C_h.m_a_44af1588("/bday.png");
   public static final C_h f_v_d_12a1f = C_h.m_a_44af1588("/happy.png");
   private static C_h f_v_h_12a1f = C_h.m_a_44af1588("/auth.png");
   public static final C_h f_v_e_12a1f = C_h.m_a_44af1588("/pstatus.png");
   private static int f_v_c_49;
   private static int f_v_d_49;
   private static int f_v_e_49 = 268435456;
   private static Player f_v_a_2fd8b6bf;
   private static C_aw f_v_a_2406f9 = null;
   public static boolean f_v_a_5a = false;

   public C_v() {
      f_v_a_12bd1 = this;

      try {
         p_v_i_9b75();
      } catch (Exception var2) {
         f_v_c_5a = false;
         f_v_a_48a69a2c = new Vector();
         f_v_b_48a69a2c = new Vector();
      }

      (f_v_a_12984 = new C_c()).f_c_a_12a7c = this;
      f_v_a_12984.m_a_cb37e88d(this);
      f_v_a_12984.m_a_48a0fc84(f_v_a_12a1f);
      f_v_a_12984.m_a_13462e(0);
      m_a_13462e(C_as.m_a_9b68());
      f_v_a_12984.m_a_48817c60(f_v_a_1570d10e, C_bd.f_bd_f_49);
      f_v_a_12984.m_a_48817c60(C_bf.f_bf_i_1570d10e, C_bd.f_bd_e_49);
      f_v_a_12984.m_a_6f63a2af(this);
   }

   public final int m_a_496bbe28(C_e var1, C_e var2) {
      Object var4 = var1.f_e_a_5f790d9c;
      Object var6 = var2.f_e_a_5f790d9c;
      C_an var5 = (C_an)var4;
      C_an var7 = (C_an)var6;
      byte var8 = 0;
      int var10000;
      switch (f_v_d_49) {
         case 0:
            var10000 = var5.m_c_73cf11cb().compareTo(var7.m_c_73cf11cb());
            break;
         case 1:
         case 2:
            int var9 = var5.m_a_134621(f_v_d_49);
            int var3 = var7.m_a_134621(f_v_d_49);
            var10000 = var9 == var3 ? var5.m_c_73cf11cb().compareTo(var7.m_c_73cf11cb()) : (var9 < var3 ? -1 : 1);
            break;
         default:
            return var8;
      }

      return var10000;
   }

   public static C_bd m_a_46a7a4ee() {
      return f_v_a_12984;
   }

   public static C_h m_a_247797e() {
      return f_v_a_12a1f;
   }

   public static int m_a_9b68() {
      return f_v_a_49;
   }

   public static int m_b_9b68() {
      return f_v_b_49;
   }

   public static int m_c_9b68() {
      return f_v_a_48a69a2c.size();
   }

   public static C_aw m_a_c2f0d221(int var0) {
      return (C_aw)f_v_a_48a69a2c.elementAt(var0);
   }

   public static synchronized C_aw[] m_a_8f165573() {
      C_aw[] var0 = new C_aw[f_v_a_48a69a2c.size()];
      f_v_a_48a69a2c.copyInto(var0);
      return var0;
   }

   public static synchronized C_l[] m_a_46ae24e1() {
      C_l[] var0 = new C_l[f_v_b_48a69a2c.size()];
      f_v_b_48a69a2c.copyInto(var0);
      return var0;
   }

   public static void m_a_ab8148d2(Alert var0) {
      f_v_a_12984.m_a_75ca2789(Jimm.f_jimm_Jimm_a_4a58c677, var0);
      C_bt.m_a_1385ff(false);
   }

   public static void m_a_9b75() {
      if (C_bp.m_a_134632(155)) {
         System.gc();
      }

      f_v_a_12984.m_a_48a013c6(f_v_a_12a1f.m_a_485a59b9(C_bf.m_a_1349e2(C_ac.m_c_9b68())));
      f_v_a_12984.m_b_48a013c6(C_ac.m_a_2477940());
      f_v_a_12984.m_c_48a013c6(C_bn.m_b_2477940());
      f_v_a_12984.m_d_48a013c6(f_v_d_12a1f.m_a_485a59b9(C_bp.m_a_134632(171) ? 0 : -1));
      f_v_a_12984.m_e_48a013c6(C_bn.m_a_4949e94a(C_bp.m_a_134632(150)));
      f_v_a_12984.m_b_13462e(C_bp.m_a_134621(111) << 3);
      m_a_13462e(C_as.m_a_9b68());
      f_v_a_12984.m_i_9b75();
      p_v_k_9b75();
      p_v_j_9b75();
      f_v_a_12984.m_j_9b75();
      f_v_a_12984.d$1385ff();
      C_bf.m_a_5d527811(f_v_a_12984);
      f_v_a_12984.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
      C_bt.m_a_1385ff(false);
      if (f_v_b_5a) {
         f_v_b_5a = false;
         p_v_b_13462e(1);
      }
   }

   public static void m_a_1385ff(boolean var0) {
      if (var0) {
         f_v_d_5a = false;
      }
   }

   private static void p_v_i_9b75() {
      f_v_a_48a69a2c = new Vector();
      f_v_b_48a69a2c = new Vector();
      String[] var0 = RecordStore.listRecordStores();
      boolean var1 = false;

      for (int var2 = 0; var2 < var0.length; var2++) {
         if (var0[var2].equals("contactlist")) {
            var1 = true;
            break;
         }
      }

      if (!var1) {
         throw new Exception();
      } else {
         RecordStore var17 = RecordStore.openRecordStore("contactlist", false);

         try {
            byte[] var8 = var17.getRecord(1);
            ByteArrayInputStream var9 = new ByteArrayInputStream(var8);
            if (!new DataInputStream(var9).readUTF().equals(Jimm.f_jimm_Jimm_a_523beb0a)) {
               throw new IOException();
            }

            byte[] var10 = var17.getRecord(2);
            ByteArrayInputStream var11 = new ByteArrayInputStream(var10);
            DataInputStream var12;
            f_v_a_49 = (var12 = new DataInputStream(var11)).readInt();
            f_v_b_49 = var12.readUnsignedShort();
            int var16 = 3;

            while (var16 <= var17.getNumRecords()) {
               byte[] var13 = var17.getRecord(var16++);
               ByteArrayInputStream var14 = new ByteArrayInputStream(var13);
               DataInputStream var15 = new DataInputStream(var14);

               while (var15.available() > 0) {
                  byte var3;
                  if ((var3 = var15.readByte()) == 0) {
                     C_aw var18;
                     (var18 = new C_aw()).m_a_414f9488(var15);
                     f_v_a_48a69a2c.addElement(var18);
                  } else if (var3 == 1) {
                     C_l var4;
                     C_l var19;
                     (var4 = var19 = new C_l()).f_l_a_49 = var15.readInt();
                     var4.f_l_a_523beb0a = var15.readUTF();
                     f_v_b_48a69a2c.addElement(var19);
                  }
               }
            }
         } finally {
            var17.closeRecordStore();
         }
      }
   }

   protected static void m_b_9b75() {
      try {
         RecordStore.deleteRecordStore("contactlist");
      } catch (RecordStoreNotFoundException var8) {
      }

      RecordStore var0 = RecordStore.openRecordStore("contactlist", true);
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2;
      (var2 = new DataOutputStream(var1)).writeUTF(Jimm.f_jimm_Jimm_a_523beb0a);
      byte[] var3 = var1.toByteArray();
      var0.addRecord(var3, 0, var3.length);
      var1.reset();
      var2.writeInt(f_v_a_49);
      var2.writeShort((short)f_v_b_49);
      var3 = var1.toByteArray();
      var0.addRecord(var3, 0, var3.length);
      var1.reset();
      int var4;
      int var5 = (var4 = f_v_a_48a69a2c.size()) + f_v_b_48a69a2c.size();

      for (int var6 = 0; var6 < var5; var6++) {
         if (var6 < var4) {
            m_a_c2f0d221(var6).m_a_3faa729d(var2);
         } else {
            C_l var10 = (C_l)f_v_b_48a69a2c.elementAt(var6 - var4);
            var2.writeByte(1);
            var2.writeInt(var10.f_l_a_49);
            var2.writeUTF(var10.f_l_a_523beb0a);
         }

         if (var1.size() >= 4000 || var6 == var5 - 1) {
            var3 = var1.toByteArray();
            var0.addRecord(var3, 0, var3.length);
            var1.reset();
         }
      }

      var0.closeRecordStore();
   }

   public static void m_c_9b75() {
      f_v_d_5a = false;
      f_v_c_5a = true;
      f_v_e_5a = true;
      f_v_a_12984.m_a_9b75();
      m_d_9b75();
   }

   public static void m_d_9b75() {
      f_v_c_49 = 0;

      for (int var0 = f_v_a_48a69a2c.size() - 1; var0 >= 0; var0--) {
         m_a_c2f0d221(var0).m_d_9b75();
      }

      for (int var1 = f_v_b_48a69a2c.size() - 1; var1 >= 0; var1--) {
         ((C_l)f_v_b_48a69a2c.elementAt(var1)).m_a_255f295(0, 0);
      }

      f_v_d_5a = false;
   }

   public static void m_e_9b75() {
      long var0 = C_bp.a$134622();

      for (int var2 = f_v_a_48a69a2c.size() - 1; var2 >= 0; var2--) {
         C_aw var3 = m_a_c2f0d221(var2);
         if (var0 != 4L && var0 != 16L && var0 != 2L && var0 != 1L) {
            var3.f_aw_b_5a = true;
         } else {
            var3.f_aw_b_5a = false;
         }
      }
   }

   public static synchronized void m_a_67444a82(int var0, int var1, C_an[] var2) {
      if (f_v_c_5a) {
         f_v_a_48a69a2c.removeAllElements();
         f_v_b_48a69a2c.removeAllElements();
         f_v_c_5a = false;
         f_v_b_49 = 0;
      }

      for (int var3 = 0; var3 < var2.length; var3++) {
         if (var2[var3] instanceof C_aw) {
            f_v_a_48a69a2c.addElement(var2[var3]);
         } else if (var2[var3] instanceof C_l) {
            f_v_b_48a69a2c.addElement(var2[var3]);
         }
      }

      f_v_b_49 += var1;
      f_v_a_49 = var0;

      try {
         m_b_9b75();
      } catch (Exception var4) {
      }

      f_v_d_5a = false;

      for (int var6 = f_v_a_48a69a2c.size() - 1; var6 >= 0; var6--) {
         C_aw var5;
         (var5 = m_a_c2f0d221(var6)).m_a_2563266(16, C_ap.m_b_aad3b203(var5.m_a_47921032(0)));
      }
   }

   public static void m_f_9b75() {
      try {
         m_b_9b75();
      } catch (Exception var0) {
      }
   }

   private static synchronized void p_v_j_9b75() {
      f_v_d_49 = C_bp.m_a_134621(65);
      if (!C_bp.m_a_134632(136)) {
         f_v_a_12984.m_b_489f9f67(null);
      } else {
         f_v_a_12984.m_b_489f9f67(null);

         for (int var2 = 0; var2 < f_v_b_48a69a2c.size(); var2++) {
            C_l var0 = (C_l)f_v_b_48a69a2c.elementAt(var2);
            Object var1 = null;
            var1 = (C_e)f_v_a_d18d4967.get(new Integer(var0.f_l_a_49));
            f_v_a_12984.m_b_489f9f67((C_e)var1);
            p_v_a_496eecce((C_e)var1, var0);
         }
      }
   }

   private static C_e p_v_a_ab2734ec(C_l var0) {
      C_e var1 = f_v_a_12984.m_a_c92c35af(null, var0);
      f_v_a_d18d4967.put(new Integer(var0.f_l_a_49), var1);
      return var1;
   }

   private static synchronized void p_v_k_9b75() {
      boolean var3 = C_bp.m_a_134632(136);
      boolean var4 = C_bp.m_a_134632(130);
      int var2 = f_v_a_48a69a2c.size();
      int var1 = f_v_b_48a69a2c.size();
      if (!f_v_d_5a && (var2 != 0 || var1 != 0)) {
         f_v_a_12984.m_a_9b75();
         f_v_a_12984.m_a_1385ff(var3);
         f_v_a_d18d4967.clear();
         if (var3) {
            for (int var0 = 0; var0 < var1; var0++) {
               C_l var10000 = (C_l)f_v_b_48a69a2c.elementAt(var0);
               Object var5 = null;
               p_v_a_ab2734ec(var10000);
            }

            Object var12 = null;
         }

         for (int var6 = 0; var6 < var2; var6++) {
            C_aw var13 = m_a_c2f0d221(var6);
            if (!var4 || var13.m_a_9b79() || var13.m_b_134621(192) != -1) {
               if (var3) {
                  C_e var8 = (C_e)f_v_a_d18d4967.get(new Integer(var13.m_b_134621(65)));
                  f_v_a_12984.m_a_c92c35af(var8, var13);
               } else {
                  f_v_a_12984.m_a_c92c35af(null, var13);
               }
            }
         }

         if (C_bp.m_a_134632(129) && var3) {
            for (int var7 = f_v_b_48a69a2c.size() - 1; var7 >= 0; var7--) {
               C_l var9 = (C_l)f_v_b_48a69a2c.elementAt(var7);
               Integer var11 = new Integer(var9.f_l_a_49);
               C_e var10;
               if ((var10 = (C_e)f_v_a_d18d4967.get(var11)).m_a_9b68() == 0) {
                  f_v_a_12984.m_a_489f9f6b(var10);
                  f_v_a_d18d4967.remove(var11);
               }
            }
         }

         f_v_d_5a = true;
      }
   }

   public static C_l m_a_485a5a73(int var0) {
      for (int var2 = f_v_b_48a69a2c.size() - 1; var2 >= 0; var2--) {
         C_l var1;
         if ((var1 = (C_l)f_v_b_48a69a2c.elementAt(var2)).f_l_a_49 == var0) {
            return var1;
         }
      }

      return null;
   }

   public static C_aw m_a_513388b0(String var0) {
      int var3 = Integer.parseInt(var0);

      for (int var2 = f_v_a_48a69a2c.size() - 1; var2 >= 0; var2--) {
         C_aw var1;
         if ((var1 = m_a_c2f0d221(var2)).m_j_9b68() == var3) {
            return var1;
         }
      }

      return null;
   }

   public static C_aw[] m_a_9bf2fbac(int var0) {
      Vector var1 = new Vector();

      for (int var3 = 0; var3 < f_v_a_48a69a2c.size(); var3++) {
         C_aw var2;
         if ((var2 = m_a_c2f0d221(var3)).m_b_134621(65) == var0) {
            var1.addElement(var2);
         }
      }

      C_aw[] var4 = new C_aw[var1.size()];
      var1.copyInto(var4);
      return var4;
   }

   private static void p_v_a_496eecce(C_e var0, C_l var1) {
      if (var1 != null && var0 != null) {
         C_aw[] var5;
         if (var1 != null) {
            var5 = m_a_9bf2fbac(var1.f_l_a_49);
         } else {
            var5 = new C_aw[f_v_a_48a69a2c.size()];
            f_v_a_48a69a2c.copyInto(var5);
         }

         int var2 = 0;
         int var3 = var5.length;

         for (int var4 = 0; var4 < var3; var4++) {
            if (var5[var4].m_b_134621(192) != -1) {
               var2++;
            }
         }

         var1.m_a_255f295(var2, var3);
      }
   }

   public static void m_a_db417b2e(C_aw var0, boolean var1, boolean var2) {
      if (f_v_d_5a) {
         boolean var3 = false;
         boolean var5 = false;
         int var6 = 0;
         int var4 = 0;
         C_e var7 = null;
         int var9 = var0.m_b_134621(192);
         String var10 = var0.m_a_47921032(0);
         int var8 = var0.m_b_134621(65);
         boolean var11 = C_bp.m_a_134632(130);
         boolean var12 = C_bp.m_a_134632(136);
         boolean var13 = C_bp.m_a_134632(129);
         C_e var14;
         if ((var14 = (C_e)f_v_a_d18d4967.get(new Integer(var8))) == null) {
            C_c var19 = f_v_a_12984;
            var14 = f_v_a_12984.f_c_a_129c2;
         }

         var6 = var14.m_a_9b68();

         for (int var20 = 0; var20 < var6; var20++) {
            C_e var10000 = var7 = var14.m_a_485a599a(var20);
            Object var15 = null;
            if ((var15 = var10000.f_e_a_5f790d9c) instanceof C_aw && ((C_aw)var15).m_a_47921032(0).equals(var10)) {
               var3 = true;
               break;
            }
         }

         var4 = f_v_a_48a69a2c.indexOf(var0) != -1;
         f_v_a_12984.m_i_9b75();
         boolean var27 = var4 && !var3;
         if (var11 && !var3) {
            var27 |= var9 != -1 | var0.m_a_9b79();
         }

         boolean var22 = !var4 && var3;
         if (var11 && var3) {
            var22 |= var9 == -1 && !var0.m_a_9b79();
         }

         if (var27) {
            if (var13 && var12 && !f_v_a_d18d4967.containsKey(new Integer(var8))) {
               C_l var17;
               C_e var30;
               if ((var17 = m_a_485a5a73(var8)) != null) {
                  var30 = p_v_a_ab2734ec(var17);
               } else {
                  C_c var23 = f_v_a_12984;
                  var30 = f_v_a_12984.f_c_a_129c2;
               }

               var14 = var30;
            }

            var7 = f_v_a_12984.m_a_c92c35af(var14, var0);
         } else if (var22) {
            f_v_a_12984.m_a_489f9f6b(var7);
            if (var13 && var12 && var14.m_a_9b68() == 0) {
               f_v_a_12984.m_a_489f9f6b(var14);
               f_v_a_d18d4967.remove(new Integer(var8));
            }

            var5 = true;
         }

         if (var2 && !var5) {
            var3 = f_v_a_12984.m_a_2477921() == var7;
            boolean var16 = false;
            f_v_a_12984.m_a_cb54c07c(var14, C_c.m_a_496bbe28(var14, var7));
            var4 = var14.m_a_9b68();
            f_v_d_49 = C_bp.m_a_134621(65);

            for (int var28 = 0; var28 < var4; var28++) {
               C_e var25;
               if ((var25 = var14.m_a_485a599a(var28)).f_e_a_5f790d9c instanceof C_aw && f_v_a_12bd1.m_a_496bbe28(var7, var25) < 0) {
                  f_v_a_12984.m_a_e40c7b6e(var14, var7, var28);
                  var16 = true;
                  break;
               }
            }

            if (!var16) {
               f_v_a_12984.m_a_e40c7b6e(var14, var7, var4);
            }

            if (var3) {
               f_v_a_12984.m_a_489f9f67(var7);
            }
         }

         if (var1) {
            f_v_a_12984.m_a_489f9f67(var7);
         }

         f_v_a_12984.m_j_9b75();
         var0.m_f_9b75();
      }
   }

   public static synchronized void m_a_a274af6d(
      String var0, int var1, int var2, byte[] var3, byte[] var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12
   ) {
      C_aw var13;
      (var13 = m_a_513388b0(var0)).f_aw_b_4a = System.currentTimeMillis();
      int var19 = C_cf.m_a_134621(var1);
      if (var13 == null) {
         f_v_e_49 = var19;
      } else {
         long var15 = var13.m_b_134621(192);
         int var14 = var13.m_a_2477b4f().m_b_9b68();
         boolean var17 = var15 != var19;
         boolean var20 = var2 != var14;
         boolean var21 = var15 != -1L;
         boolean var16;
         if (!(var16 = var19 != -1)) {
            if (var21) {
               var13.m_a_4f708078(3, C_cf.m_a_2416688b(false, false));
               var13.m_a_255f295(75, 0);
               var13.m_a_25e06fc(new byte[0]);
               p_v_b_13462e(4);
               var13.m_c_9b75();
               var13.m_b_1385ff(false);
            } else if (!f_v_e_5a) {
               if (var13.f_aw_b_4a - var13.f_aw_a_4a < 60000L) {
                  C_bq.m_a_e925fa09(var0, "c0");
               } else {
                  var13.f_aw_a_4a = var13.f_aw_b_4a;
               }
            }

            var13.f_aw_b_49 = 0;
            var13.m_a_1385ff(false);
         } else if (!var21 && !f_v_e_5a) {
            p_v_b_13462e(2);
            var13.m_c_9b75();
            var13.m_b_1385ff(true);
         }

         p_v_a_8cee5d95(var13, var21, var16, 0);
         var13.m_a_255f295(192, var19);
         if (f_v_d_5a && var17) {
            C_bf.m_a_5d632d3a(C_bf.m_a_810c345d(), var13, var19);
         }

         if (var20 && var14 != -1) {
            var13.f_aw_a_5a = true;
         }

         if (var6 != -1 && var16) {
            var13.m_a_4870e775(225, var3);
            var13.m_a_4870e775(226, var4);
            var13.m_a_255f295(74, var5);
            var13.m_a_255f295(72, var6);
            var13.m_a_255f295(73, var7);
            var13.m_a_255f295(193, var8);
         }

         var13.m_a_255f295(194, var9);
         if (var16) {
            var13.m_a_255f295(191, var12);
         }

         var13.m_a_255f295(195, var10);
         var13.m_a_255f295(71, var11);
         if (var17 || var20) {
            m_a_db417b2e(var13, false, true);
         }

         if (f_v_a_12984.m_b_9b79()) {
            String var18 = null;
            if (var17) {
               var18 = C_bf.m_a_2f33e691(var19);
            }

            if (var18 != null) {
               C_bf.m_a_418a46c8(C_bf.m_a_810c345d(), var13.f_aw_a_523beb0a + ": " + var18, 4);
            }
         }
      }
   }

   public static synchronized void a$505cff1c(String var0) {
      m_a_a274af6d(var0, -1, -1, null, null, 0, 0, -1, 0, -1, -1, -1, -1);
   }

   private static void p_v_a_8cee5d95(C_aw var0, boolean var1, boolean var2, int var3) {
      boolean var4 = false;
      int var10000 = var0.m_b_134621(65);
      boolean var5 = false;
      C_l var6 = m_a_485a5a73(var10000);
      if (var1 && !var2) {
         f_v_c_49--;
         if (var6 != null) {
            var6.m_b_255f295(-1, 0);
         }

         var4 = true;
      }

      if (!var1 && var2) {
         f_v_c_49++;
         if (var6 != null) {
            var6.m_b_255f295(1, 0);
         }

         var4 = true;
      }

      if (var6 != null) {
         var6.m_b_255f295(0, var3);
         var4 |= var3 != 0;
      }

      if (var4) {
         C_u.m_a_d3ad8a43(7, null);
      }
   }

   public static void m_a_13462e(int var0) {
      String var1 = f_v_c_49 + "/" + f_v_a_48a69a2c.size();
      if (!C_bp.m_a_134632(173)) {
         var1 = var1 + "-" + C_cf.m_a_2416688b(true, false);
      }

      if (var0 != 0) {
         var1 = var1 + "-" + var0 / 1024 + "K";
      }

      if (C_bp.m_a_134632(179)) {
         var1 = var1 + "-" + Runtime.getRuntime().freeMemory() / 1024L + "K";
      }

      f_v_a_12984.m_c_aad3b1ff(var1);
   }

   public static synchronized void m_a_cb37742e(C_aw var0) {
      f_v_a_48a69a2c.removeElement(var0);
      m_a_db417b2e(var0, false, false);
      p_v_a_8cee5d95(var0, var0.m_b_134621(192) != -1, false, -1);

      try {
         m_b_9b75();
      } catch (Exception var1) {
      }
   }

   public static synchronized void m_b_cb37742e(C_aw var0) {
      if (!var0.m_a_134632(1)) {
         C_aw var1;
         if ((var1 = m_a_513388b0(var0.m_a_47921032(0))) != null) {
            m_a_cb37742e(var1);
            f_v_e_49 = var1.m_b_134621(192);
         }

         f_v_a_48a69a2c.addElement(var0);
         var0.m_a_2563266(1, true);
         var0.m_a_2563266(16, C_ap.m_b_aad3b203(var0.m_a_47921032(0)));
         if (f_v_e_49 != 268435456) {
            var0.m_a_255f295(192, f_v_e_49);
            f_v_e_49 = 268435456;
         }

         m_a_db417b2e(var0, true, true);
         p_v_a_8cee5d95(var0, false, var0.m_b_134621(192) != -1, 1);

         try {
            m_b_9b75();
            return;
         } catch (Exception var2) {
         }
      }
   }

   public static synchronized void m_a_48a2ce00(C_l var0) {
      f_v_b_48a69a2c.addElement(var0);
      if (C_bp.m_a_134632(136)) {
         C_e var1 = f_v_a_12984.m_a_c92c35af(null, var0);
         f_v_a_d18d4967.put(new Integer(var0.f_l_a_49), var1);

         try {
            m_b_9b75();
         } catch (Exception var2) {
         }
      }
   }

   public static synchronized void m_b_48a2ce00(C_l var0) {
      for (int var1 = f_v_a_48a69a2c.size() - 1; var1 >= 0; var1--) {
         C_aw var2;
         if ((var2 = m_a_c2f0d221(var1)).m_b_134621(65) == var0.f_l_a_49) {
            if (var2.m_b_134621(192) != -1) {
               f_v_c_49--;
            }

            f_v_a_48a69a2c.removeElementAt(var1);
         }
      }

      Integer var5 = new Integer(var0.f_l_a_49);
      if (C_bp.m_a_134632(136)) {
         C_e var6 = (C_e)f_v_a_d18d4967.get(var5);
         C_c var3;
         C_c var10000 = var3 = f_v_a_12984;
         var3 = f_v_a_12984;
         var10000.m_a_cb54c07c(f_v_a_12984.f_c_a_129c2, C_c.m_a_496bbe28(f_v_a_12984.f_c_a_129c2, var6));
         f_v_a_d18d4967.remove(var5);
      }

      f_v_b_48a69a2c.removeElement(var0);

      try {
         m_b_9b75();
      } catch (Exception var4) {
      }
   }

   public static synchronized C_aw m_b_513388b0(String var0) {
      C_aw var1;
      if ((var1 = m_a_513388b0(var0)) != null) {
         return var1;
      } else {
         try {
            var1 = new C_aw(0, 0, var0, var0, false, true);
         } catch (Exception var2) {
            return null;
         }

         f_v_a_48a69a2c.addElement(var1);
         var1.m_a_2563266(8, true);
         return var1;
      }
   }

   public static synchronized void m_a_9cd19a7a(C_bl var0, boolean var1) {
      Object var2 = null;
      var2 = var0.f_bl_c_523beb0a;
      C_aw var3;
      if ((var3 = m_a_513388b0(var0.f_bl_c_523beb0a)) == null) {
         var3 = m_b_513388b0((String)var2);
      }

      C_ap.m_a_f97a5fd3(var3, var0);
      C_cn.m_e_9b75();
      if (!f_v_d_5a) {
         f_v_b_5a |= true;
      } else if (var1) {
         p_v_b_13462e(1);
      }

      var3.m_a_2563266(16, true);
      m_a_db417b2e(var3, true, true);
   }

   public static boolean m_a_aad3b203(String var0) {
      p_v_a_b138468(var0);
      boolean var1 = f_v_a_2fd8b6bf != null;
      p_v_l_9b75();
      return var1;
   }

   public final void playerUpdate(Player var1, String var2, Object var3) {
      if (var2.equals("endOfMedia")) {
         var1.close();
      }
   }

   private static Player p_v_a_b138468(String var0) {
      p_v_l_9b75();

      try {
         String var1 = "wav";
         int var2;
         if ((var2 = var0.lastIndexOf(46)) != -1) {
            var1 = var0.substring(var2 + 1).toLowerCase();
         }

         if (var1.equals("mp3")) {
            var1 = "audio/mpeg";
         } else if (var1.equals("mid") || var1.equals("midi")) {
            var1 = "audio/midi";
         } else if (var1.equals("amr")) {
            var1 = "audio/amr";
         } else {
            var1 = "audio/X-wav";
         }

         InputStream var3;
         Class var6;
         if ((var3 = (var6 = new Object().getClass()).getResourceAsStream(var0)) == null) {
            var3 = var6.getResourceAsStream("/" + var0);
         }

         if (var3 != null) {
            (f_v_a_2fd8b6bf = Manager.createPlayer(var3, var1)).addPlayerListener(f_v_a_12bd1);
         }
      } catch (Exception var4) {
         f_v_a_2fd8b6bf = null;
      }

      return f_v_a_2fd8b6bf;
   }

   private static void p_v_l_9b75() {
      if (f_v_a_2fd8b6bf != null) {
         try {
            if (f_v_a_2fd8b6bf.getState() == 400) {
               f_v_a_2fd8b6bf.stop();
            }

            f_v_a_2fd8b6bf.close();
         } catch (Exception var0) {
         }

         f_v_a_2fd8b6bf = null;
      }
   }

   private static void p_v_c_13462e(int var0) {
      try {
         f_v_a_2fd8b6bf.realize();
         VolumeControl var1;
         if ((var1 = (VolumeControl)f_v_a_2fd8b6bf.getControl("VolumeControl")) != null) {
            var1.setLevel(var0);
         }

         f_v_a_2fd8b6bf.prefetch();
      } catch (Exception var2) {
      }
   }

   private static synchronized void p_v_b_afa340b5(String var0, boolean var1) {
      if (var1) {
         p_v_b_13462e(3);
      }

      if (C_ap.m_a_aad3b203(var0)) {
         C_ap.m_a_51338872(var0).m_d_9b75();
      } else {
         f_v_a_12984.m_c_9b75();
      }
   }

   public static synchronized void m_g_9b75() {
      f_v_a_12984.m_c_9b75();
   }

   public static void m_h_9b75() {
      C_cl var0 = new C_cl();
      Jimm.m_a_94e56161().schedule(var0, 7000L);
      C_bh.f_bh_b_5a = true;
      C_bh var2 = new C_bh(C_bp.m_a_47921032(254), "");

      try {
         C_ac.m_a_cb38d14b(var2);
      } catch (C_aq var1) {
      }
   }

   public static synchronized void m_a_afa340b5(String var0, boolean var1) {
      C_aw var2;
      if ((var2 = m_a_513388b0(var0)) == null && !C_bp.m_a_134632(162)) {
         var2 = m_b_513388b0(var0);
      }

      if (var2 != null) {
         if (!var2.m_b_134632(1024)) {
            var2.a$13462e();
         }

         var2.m_a_1385ff(var1);
         p_v_b_afa340b5(var0, var1);
      }
   }

   private static void p_v_b_13462e(int var0) {
      synchronized (f_v_a_12bd1) {
         if (f_v_d_5a) {
            int var2;
            if ((var2 = C_bp.m_a_134621(75)) == 2) {
               var2 = C_cn.m_a_9b79() ? 1 : 0;
            }

            if (var2 > 0 && var0 == 1) {
               Jimm.f_jimm_Jimm_a_4a58c677.vibrate(500);
            }

            if (!C_bp.m_a_134632(150)) {
               int var6 = 0;
               switch (var0) {
                  case 1:
                     var6 = C_bp.m_a_134621(66);
                     break;
                  case 2:
                     var6 = C_bp.m_a_134621(68);
                     break;
                  case 3:
                     var6 = C_bp.m_a_134621(88) - 1;
                     break;
                  case 4:
                     var6 = C_bp.m_a_134621(99);
               }

               switch (var6) {
                  case 1:
                     try {
                        switch (var0) {
                           case 1:
                              Manager.playTone(60, 500, C_bp.m_a_134621(67));
                              return;
                           case 2:
                              Manager.playTone(65, 500, C_bp.m_a_134621(67));
                              return;
                           case 3:
                              Manager.playTone(67, 500, C_bp.m_a_134621(67));
                              return;
                           case 4:
                              Manager.playTone(66, 500, C_bp.m_a_134621(67));
                        }
                     } catch (Exception var3) {
                     }
                     break;
                  case 2:
                     try {
                        if (var0 == 1) {
                           if ((f_v_a_2fd8b6bf = p_v_a_b138468(C_bp.m_a_47921032(4))) == null) {
                              return;
                           }

                           p_v_c_13462e(C_bp.m_a_134621(67));
                        } else if (var0 == 2) {
                           if ((f_v_a_2fd8b6bf = p_v_a_b138468(C_bp.m_a_47921032(5))) == null) {
                              return;
                           }

                           p_v_c_13462e(C_bp.m_a_134621(67));
                        } else if (var0 == 4) {
                           if ((f_v_a_2fd8b6bf = p_v_a_b138468(C_bp.m_a_47921032(41))) == null) {
                              return;
                           }

                           p_v_c_13462e(C_bp.m_a_134621(67));
                        } else {
                           if ((f_v_a_2fd8b6bf = p_v_a_b138468(C_bp.m_a_47921032(16))) == null) {
                              return;
                           }

                           p_v_c_13462e(C_bp.m_a_134621(67));
                        }

                        f_v_a_2fd8b6bf.start();
                     } catch (Exception var4) {
                     }
               }
            }
         }
      }
   }

   public static boolean m_a_138603(boolean var0) {
      boolean var1 = !C_bp.m_a_134632(150);
      C_bp.m_a_2563266(150, var1);
      if (C_bp.m_a_134632(168)) {
         C_bp.m_a_255f295(75, var1 ? 1 : 0);
      }

      C_bp.m_c_9b75();
      if (var0) {
         m_a_9b75();
      }

      return var1;
   }

   public final void m_a_e3fdbb00(C_e var1, C_bx var2) {
      C_an var3 = (C_an)var1.f_e_a_5f790d9c;
      var2.f_bx_a_129e1 = f_v_a_12a1f.m_a_485a59b9(var3.m_a_9b68());
      var2.f_bx_b_129e1 = var3.m_a_2477b4f().m_a_2477940();
      var2.f_bx_c_129e1 = f_v_d_12a1f.m_a_485a59b9(var3.m_e_9b68());
      var2.f_bx_d_129e1 = f_v_g_12a1f.m_a_485a59b9(var3.m_d_9b68());
      var2.f_bx_a_523beb0a = var3.m_a_73cf11cb();
      var2.f_bx_e_129e1 = f_v_h_12a1f.m_a_485a59b9(var3.m_f_9b68());
      var2.f_bx_f_129e1 = f_v_f_12a1f.m_a_485a59b9(var3.m_h_9b68());
      var2.f_bx_g_129e1 = f_v_f_12a1f.m_a_485a59b9(var3.m_g_9b68());
      var2.f_bx_h_129e1 = f_v_c_12a1f.m_a_485a59b9(var3.m_c_9b68());
      var2.f_bx_b_49 = var3.m_b_9b68();
      var2.f_bx_a_49 = var3.m_i_9b68();
   }

   public final void m_a_cb3ce8a2(C_bd var1) {
   }

   public final void m_b_cb3ce8a2(C_bd var1) {
   }

   public final void m_a_efb3a882(C_bd var1, int var2, int var3) {
      C_e var5;
      C_aw var10000 = (var5 = f_v_a_12984.m_a_2477921()) != null && var5.f_e_a_5f790d9c instanceof C_aw ? (C_aw)var5.f_e_a_5f790d9c : null;
      C_aw var6 = var10000;
      if (!C_bf.m_a_db398112(var10000, var2, var3)) {
         switch (var2) {
            case -8:
               if (var6 != null) {
                  C_bf.f_bf_a_12b74 = C_bf.a$680342a1(var6, C_bs.m_a_e96ea081("N4"), C_bs.m_a_e96ea081("N4") + " " + var6.f_aw_a_523beb0a + "?");
               }

               return;
            default:
               C_bf.m_a_db39810e(var6, var2, var3);

               try {
                  if (var3 == 3) {
                     switch (C_bd.m_b_134621(var2)) {
                        case 2:
                           if (var2 != 52) {
                              var1.m_e_1385ff(false);
                           }

                           return;
                        case 5:
                           if (var2 != 54) {
                              var1.m_e_1385ff(true);
                           }

                           return;
                     }
                  }
               } catch (Exception var4) {
               }
         }
      }
   }

   protected static synchronized String m_a_a9514c81(boolean var0) {
      int var1;
      if ((var1 = f_v_a_48a69a2c.indexOf(f_v_a_2406f9)) == -1) {
         return null;
      } else {
         int var5 = var0 ? 1 : -1;
         int var2 = f_v_a_48a69a2c.size();
         int var3 = var1 + var5;

         while (true) {
            if (var3 < 0) {
               var3 = var2 - 1;
            }

            if (var3 >= var2) {
               var3 = 0;
            }

            if (var3 == var1) {
               return null;
            }

            C_aw var4;
            if ((var4 = m_a_c2f0d221(var3)).m_a_134632(16)) {
               C_bf.f_bf_a_2406f9 = null;
               f_v_a_5a = false;
               f_v_a_2406f9 = var4;
               var4.m_e_9b75();
               return var4.m_a_47921032(0);
            }

            var3 += var5;
         }
      }
   }

   protected static int m_d_9b68() {
      int var0 = f_v_a_48a69a2c.size();
      int var1 = 0;

      for (int var2 = 0; var2 < var0; var2++) {
         var1 += m_a_c2f0d221(var2).m_k_9b68();
      }

      return var1;
   }

   public static C_aw[] m_a_7ef64a7e(C_l var0) {
      Vector var1 = new Vector();
      int var5 = var0.f_l_a_49;
      int var2 = f_v_a_48a69a2c.size();

      for (int var3 = 0; var3 < var2; var3++) {
         C_aw var4;
         if ((var4 = m_a_c2f0d221(var3)).m_b_134621(65) == var5) {
            var1.addElement(var4);
         }
      }

      C_aw[] var6 = new C_aw[var1.size()];
      var1.copyInto(var6);
      return var6;
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == f_v_a_1570d10e) {
         C_bn.m_b_9b75();
      } else {
         C_e var3;
         if (var1 == C_bf.f_bf_i_1570d10e && (var3 = f_v_a_12984.m_a_2477921()) != null) {
            C_an var4;
            if ((var4 = (C_an)var3.f_e_a_5f790d9c) instanceof C_aw) {
               (f_v_a_2406f9 = (C_aw)var4).m_e_9b75();
            }

            if (var4 instanceof C_l) {
               f_v_a_12984.m_a_cb55004d(var3, !var3.m_a_9b79());
            }
         }
      }
   }

   static boolean b$138603() {
      f_v_e_5a = false;
      return false;
   }
}
