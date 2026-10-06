package recovered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;
import jimm.Jimm;

public final class C_bp {
   public static final String[] f_bp_a_6dccaaa5 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "#"};
   public static final int[] f_bp_a_b4e = new int[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 42, 35};
   public static final int f_bp_a_49 = f_bp_a_6dccaaa5.length;
   static int[] f_bp_b_b4e = new int[]{0, 228, 14, 229, 15, 230};
   public static final String f_bp_a_523beb0a = new String();
   private static Object[] f_bp_a_7b09cd37 = new Object[256];
   public static C_be f_bp_a_24088c;
   public static int f_bp_b_49;
   public static int f_bp_c_49;
   public static int f_bp_d_49;
   public static int f_bp_e_49;
   public static int f_bp_f_49;
   public static int f_bp_g_49;

   public C_bp() {
      try {
         p_bp_e_9b75();
         p_bp_f_9b75();
         p_bp_g_9b75();
         if (m_a_134632(148)) {
            m_a_2563266(148, false);
            p_bp_f_9b75();
         }
      } catch (Exception var1) {
         p_bp_e_9b75();
         p_bp_f_9b75();
      }

      C_bs.m_a_aad3b1ff(m_a_47921032(3));
      f_bp_b_49 = m_a_134621(107);
      f_bp_c_49 = m_a_134621(114);
      m_b_9b75();
   }

   public static void m_a_9b75() {
      if (Jimm.f_jimm_Jimm_a_4a58c677.numAlphaLevels() > 1) {
         f_bp_d_49 = 255 - m_a_134621(100);
         f_bp_e_49 = 255 - m_a_134621(116);
         f_bp_f_49 = 255 - m_a_134621(117);
         C_bd.f_bd_d_49 = 0;
      } else {
         f_bp_f_49 = 255;
         f_bp_e_49 = 255;
         f_bp_d_49 = 255;
      }
   }

   public static void m_b_9b75() {
      if (m_a_134621(112) == 2) {
         f_bp_g_49 = 2;
      } else {
         f_bp_g_49 = 0;
      }
   }

   private static void p_bp_e_9b75() {
      m_a_4f708078(0, f_bp_a_523beb0a);
      m_a_4f708078(228, f_bp_a_523beb0a);
      m_a_4f708078(1, "login.icq.com login.oscar.aol.com");
      m_a_4f708078(2, "5190");
      m_a_2563266(128, true);
      m_a_2563266(149, true);
      m_a_255f295(91, 10);
      m_a_4f708078(13, "120");
      m_a_255f295(64, 1);
      m_a_255f295(83, 0);
      m_a_2563266(144, true);
      m_a_2563266(138, false);
      m_a_4f708078(17, "unknown");
      m_a_4f708078(18, "unknown");
      m_a_4f708078(3, C_bs.f_bs_a_6dccaaa5[0]);
      m_a_2563266(129, false);
      m_a_255f295(65, 2);
      m_a_255f295(111, 0);
      m_a_255f295(112, 0);
      m_a_2563266(154, false);
      m_a_2563266(155, true);
      m_a_2563266(169, false);
      m_a_2563266(166, false);
      m_a_2563266(167, false);
      m_a_2563266(143, false);
      m_a_2563266(174, false);
      m_a_2563266(159, false);
      m_a_2563266(160, false);
      m_a_2563266(183, true);
      m_a_2563266(181, false);
      m_a_2563266(182, false);
      m_a_2563266(171, false);
      m_a_2563266(172, true);
      m_a_4f708078(31, "0.7.0b");
      m_a_255f295(98, 9);
      m_a_4f708078(38, f_bp_a_523beb0a);
      m_a_255f295(69, 0);
      m_a_4f708078(34, "/back.png");
      m_a_4f708078(35, f_bp_a_523beb0a);
      m_a_4f708078(36, f_bp_a_523beb0a);
      m_a_4f708078(37, f_bp_a_523beb0a);
      m_a_2563266(162, false);
      m_a_4f708078(39, f_bp_a_523beb0a);
      m_a_2563266(130, false);
      m_a_2563266(134, false);
      m_a_2563266(173, true);
      m_a_255f295(66, 0);
      m_a_4f708078(4, "message.mp3");
      m_a_255f295(68, 0);
      m_a_4f708078(5, "online.mp3");
      m_a_255f295(99, 0);
      m_a_4f708078(41, "offline.mp3");
      m_a_4f708078(16, "typing.mp3");
      m_a_255f295(88, 1);
      m_a_255f295(67, 100);
      m_a_255f295(74, 15);
      m_a_2563266(140, false);
      m_a_255f295(94, 7);
      m_a_2563266(146, false);
      m_a_2563266(156, true);
      m_a_2563266(157, true);
      m_a_255f295(93, 25);
      m_a_2563266(184, false);
      m_a_255f295(89, 25);
      m_a_2563266(133, C_bs.f_bs_a_6dccaaa5[0].equals("RU") || C_bs.f_bs_a_6dccaaa5[0].equals("CZ"));
      m_a_255f295(75, 1);
      m_a_255f295(97, 0);
      m_a_255f295(70, 0);
      m_a_255f295(71, 0);
      m_a_255f295(72, 1024);
      m_a_4f708078(6, "$");
      m_a_255f656(192, 0L);
      m_a_2563266(147, false);
      m_a_2563266(153, false);
      m_a_255f295(109, 15);
      m_a_255f295(92, 37);
      m_a_255f295(110, 4);
      m_a_2563266(135, false);
      m_a_2563266(136, false);
      m_a_2563266(137, false);
      m_a_2563266(141, true);
      m_a_2563266(132, false);
      m_a_2563266(131, true);
      m_a_2563266(142, false);
      m_a_255f295(76, 0);
      m_a_4f708078(8, f_bp_a_523beb0a);
      m_a_4f708078(9, "1080");
      m_a_4f708078(10, "1");
      m_a_4f708078(11, f_bp_a_523beb0a);
      m_a_4f708078(12, f_bp_a_523beb0a);
      m_a_255f295(77, 14);
      m_a_255f295(79, 10);
      m_a_255f295(80, 2);
      m_a_255f295(81, 3);
      m_a_255f295(82, 7);
      m_a_255f295(78, 0);
      m_a_255f295(102, 13421772);
      m_a_255f295(103, 0);
      m_a_255f295(104, 255);
      m_a_255f295(105, 9474192);
      m_a_255f295(106, 10027008);
      m_a_255f295(107, 13369344);
      m_a_255f295(108, 16711680);
      m_a_255f295(113, 255);
      m_a_255f295(114, 12114168);
      m_a_255f295(115, 0);
      m_a_2563266(150, false);
      m_a_255f295(100, 128);
      m_a_255f295(116, 0);
      m_a_255f295(117, 0);
      m_a_255f295(84, 0);
      m_a_2563266(164, true);
      m_a_2563266(170, true);
      m_a_2563266(165, false);
      m_a_4f708078(14, f_bp_a_523beb0a);
      m_a_4f708078(229, f_bp_a_523beb0a);
      m_a_4f708078(15, f_bp_a_523beb0a);
      m_a_4f708078(230, f_bp_a_523beb0a);
      m_a_255f295(86, 0);
      m_a_2563266(145, false);
      m_a_2563266(151, true);
      m_a_255f295(87, 0);
      m_a_255f295(90, 0);
      m_a_2563266(148, false);
      m_a_2563266(152, true);
      m_a_2563266(168, false);
      m_a_2563266(176, true);
      m_a_2563266(177, false);
      m_a_2563266(180, false);
      m_a_2563266(178, true);
      m_a_2563266(179, false);
      p_bp_a_afa300e4("online.", 5);
      p_bp_a_afa300e4("offline.", 41);
      p_bp_a_afa300e4("message.", 4);
      p_bp_a_afa300e4("typing.", 16);
      m_a_2563266(175, false);
      m_a_4f708078(40, f_bp_a_523beb0a);
   }

   private static void p_bp_f_9b75() {
      m_a_4f708078(7, C_bs.m_a_e96ea081("F5"));
      m_a_4f708078(19, C_bs.m_a_e96ea081("G5"));
      m_a_4f708078(20, C_bs.m_a_e96ea081("H5"));
      m_a_4f708078(21, C_bs.m_a_e96ea081("I5"));
      m_a_4f708078(22, C_bs.m_a_e96ea081("J5"));
      m_a_4f708078(23, C_bs.m_a_e96ea081("K5"));
      m_a_4f708078(24, C_bs.m_a_e96ea081("L5"));
      m_a_4f708078(25, C_bs.m_a_e96ea081("M5"));
      m_a_4f708078(26, C_bs.m_a_e96ea081("N5"));
   }

   private static void p_bp_g_9b75() {
      RecordStore var0;
      byte[] var1 = (var0 = RecordStore.openRecordStore("options", false)).getRecord(1);
      ByteArrayInputStream var4 = new ByteArrayInputStream(var1);
      new DataInputStream(var4);
      p_bp_e_9b75();
      var1 = var0.getRecord(2);
      ByteArrayInputStream var6 = new ByteArrayInputStream(var1);
      DataInputStream var7 = new DataInputStream(var6);

      while (var7.available() > 0) {
         int var2;
         if ((var2 = var7.readUnsignedByte()) < 64) {
            m_a_4f708078(var2, var7.readUTF());
         } else if (var2 < 128) {
            m_a_255f295(var2, var7.readInt());
         } else if (var2 < 192) {
            m_a_2563266(var2, var7.readBoolean());
         } else if (var2 < 224) {
            m_a_255f656(var2, var7.readLong());
         } else {
            byte[] var3 = new byte[var7.readUnsignedShort()];
            var7.readFully(var3);
            var3 = C_cf.m_a_4962d961(var3);
            m_a_4f708078(var2, C_cf.m_a_20e7da8(var3, 0, var3.length, true));
         }
      }

      var0.closeRecordStore();
   }

   public static void m_c_9b75() {
      try {
         RecordStore var0 = RecordStore.openRecordStore("options", true);

         while (var0.getNumRecords() < 3) {
            var0.addRecord(null, 0, 0);
         }

         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var10000 = new DataOutputStream(var1);
         DataOutputStream var2 = null;
         var10000.writeUTF(Jimm.f_jimm_Jimm_a_523beb0a);
         byte[] var6 = var1.toByteArray();
         var0.setRecord(1, var6, 0, var6.length);
         var1 = new ByteArrayOutputStream();
         var2 = new DataOutputStream(var1);

         for (int var3 = 0; var3 < f_bp_a_7b09cd37.length; var3++) {
            if (f_bp_a_7b09cd37[var3] != null) {
               var2.writeByte(var3);
               if (var3 < 64) {
                  var2.writeUTF((String)f_bp_a_7b09cd37[var3]);
               } else if (var3 < 128) {
                  var2.writeInt((Integer)f_bp_a_7b09cd37[var3]);
               } else if (var3 < 192) {
                  var2.writeBoolean((Boolean)f_bp_a_7b09cd37[var3]);
               } else if (var3 < 224) {
                  var2.writeLong((Long)f_bp_a_7b09cd37[var3]);
               } else if (var3 < 256) {
                  byte[] var11 = C_cf.m_a_44c4d6c8((String)f_bp_a_7b09cd37[var3], true);
                  Object var4 = null;
                  var4 = C_cf.m_a_4962d961(var11);
                  var2.writeShort(((Object[])var4).length);
                  var2.write((byte[])var4);
               }
            }
         }

         byte[] var8 = var1.toByteArray();
         var0.setRecord(2, var8, 0, var8.length);
         var0.closeRecordStore();
      } catch (Exception var5) {
         C_aq.m_a_481c933f(new C_aq(172, 0, true));
      }
   }

   public static synchronized String m_a_47921032(int var0) {
      switch (var0) {
         case 254:
         case 255:
            int var1 = m_a_134621(86) << 1;
            return m_a_47921032(f_bp_b_b4e[var0 == 254 ? var1 : var1 + 1]);
         default:
            return (String)f_bp_a_7b09cd37[var0];
      }
   }

   public static synchronized int m_a_134621(int var0) {
      return (Integer)f_bp_a_7b09cd37[var0];
   }

   public static synchronized boolean m_a_134632(int var0) {
      return (Boolean)f_bp_a_7b09cd37[var0];
   }

   public static synchronized long a$134622() {
      return (Long)f_bp_a_7b09cd37[192];
   }

   public static synchronized void m_a_4f708078(int var0, String var1) {
      f_bp_a_7b09cd37[var0] = new String(var1);
   }

   public static synchronized void m_a_255f295(int var0, int var1) {
      f_bp_a_7b09cd37[var0] = new Integer(var1);
   }

   public static synchronized void m_a_2563266(int var0, boolean var1) {
      f_bp_a_7b09cd37[var0] = new Boolean(var1);
   }

   public static synchronized void m_a_255f656(int var0, long var1) {
      f_bp_a_7b09cd37[var0] = new Long(var1);
   }

   public static void m_d_9b75() {
      (f_bp_a_24088c = new C_be()).m_c_9b75();
   }

   private static void p_bp_a_afa300e4(String var0, int var1) {
      if (!C_v.m_a_aad3b203(m_a_47921032(var1))) {
         String[] var2 = C_cf.m_a_639c22ad("wav|mp3", '|');

         for (int var3 = 0; var3 < var2.length; var3++) {
            String var4;
            if (C_v.m_a_aad3b203(var4 = var0 + var2[var3])) {
               m_a_4f708078(var1, var4);
               return;
            }
         }
      }
   }
}
