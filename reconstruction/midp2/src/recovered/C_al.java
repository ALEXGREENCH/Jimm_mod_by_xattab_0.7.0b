package recovered;

/** 0.6 source correspondence (inferred): jimm.FileBrowser. Release class: al. */

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import jimm.Jimm;

public final class C_al implements C_ay, CommandListener, C_k {
   private static Command f_al_a_1570d10e = new Command(C_bt.m_a_e96ea081("6"), 1, 0);
   private static Command f_al_b_1570d10e = new Command(C_bt.m_a_e96ea081("3"), 4, 1);
   private static Command f_al_c_1570d10e = new Command(C_bt.m_a_e96ea081("9"), 4, 1);
   private static boolean f_al_a_5a;
   private static C_al f_al_a_2405a4;
   private static C_d f_al_a_129a3;
   private static C_u f_al_a_12bb2;
   private static C_g f_al_a_12a00 = C_g.m_a_44af1569("/fs.png");
   private static String[] f_al_a_6dccaaa5;
   private static String f_al_a_523beb0a;

   public C_al() {
      f_al_a_2405a4 = this;
      (f_al_a_129a3 = new C_d()).f_d_a_12a7c = this;
      f_al_a_129a3.m_a_cb385cec(this);
      f_al_a_129a3.m_a_48a08825(f_al_a_12a00);
      f_al_a_129a3.m_b_13462e(f_al_a_12a00.m_b_9b68() < 16 ? 8 : 0);
      f_al_a_129a3.m_a_13462e(-f_al_a_129a3.m_h_9b68() / 2);
      f_al_a_129a3.m_a_48a013c6(f_al_a_12a00.m_a_485a59b9(0));
      C_bi.m_a_9c7d0d74(f_al_a_129a3, false);
      f_al_a_129a3.d$1385ff();
      f_al_a_129a3.m_a_1385ff(false);
      f_al_a_129a3.m_a_48817c60(f_al_a_1570d10e, C_bf.f_bf_f_49);
      f_al_a_129a3.m_a_6f63a2af(this);
   }

   private static void p_al_b_9b75() {
      f_al_a_129a3.m_j_9b75();
      f_al_a_6dccaaa5 = new String[0];
      f_al_a_129a3.m_a_9b75() ;
      f_al_a_129a3.m_k_9b75();
   }

   private static int p_al_a_aad3b1f2(String var0) {
      if (var0.equals("../")) {
         return 0;
      } else {
         return var0.endsWith("/") ? 10 : 20;
      }
   }

   public final int m_a_496bbe28(C_e var1, C_e var2) {
      boolean var4 = false;
      String var5 = (String)var1.f_e_a_5f790d9c;
      String var6 = (String)var2.f_e_a_5f790d9c;
      int var7 = p_al_a_aad3b1f2(var5);
      int var3 = p_al_a_aad3b1f2(var6);
      if (var7 == var3) {
         return var5.toLowerCase().compareTo(var6.toLowerCase());
      } else {
         return var7 < var3 ? -1 : 1;
      }
   }

   private static void p_al_c_9b75() {
      f_al_a_129a3.m_j_9b75();
      f_al_a_129a3.m_a_9b75() ;

      for (int var0 = 0; var0 < f_al_a_6dccaaa5.length; var0++) {
         f_al_a_129a3.m_a_c92c35af(null, f_al_a_6dccaaa5[var0]);
      }

      f_al_a_129a3.m_b_489f9f67(null);
      f_al_a_129a3.m_k_9b75();
      p_al_a_aad3b1ff((String)f_al_a_129a3.m_a_2477921().f_e_a_5f790d9c);
   }

   public static void m_a_1385ff(boolean var0) {
      f_al_a_5a = var0;
   }

   public static void m_a_48a6e557(C_u var0) {
      f_al_a_12bb2 = var0;
   }

   private static void p_al_a_aad3b1ff(String var0) {
      f_al_a_129a3.m_a_8eb9d703(f_al_c_1570d10e);
      f_al_a_129a3.m_a_8eb9d703(f_al_b_1570d10e);
      f_al_a_129a3.m_a_8eb9d703(C_bi.f_bi_h_1570d10e);
      if (var0.equals("../")) {
         int var8 = f_al_a_523beb0a.lastIndexOf(47, f_al_a_523beb0a.length() - 2);
         f_al_a_129a3.m_a_48817c60(f_al_c_1570d10e, C_bf.f_bf_e_49);
         f_al_a_129a3.m_c_aad3b1ff(var8 != -1 ? f_al_a_523beb0a.substring(0, var8 + 1) : "/");
      } else if (var0.endsWith("/") & f_al_a_523beb0a.equals("/")) {
         try {
            C_d var10000 = f_al_a_129a3;
            StringBuffer var10001 = new StringBuffer().append(C_bt.m_a_e96ea081("86")).append(": ");
            long var4 = 0L;
            FileConnection var7;
            var4 = (var7 = (FileConnection)Connector.open("file://localhost/" + var0)).totalSize();
            var7.close();
            var10000.m_c_aad3b1ff(var10001.append(var4 >> 10).append("Kb").toString());
         } catch (Exception var6) {
         }

         f_al_a_129a3.m_a_48817c60(f_al_c_1570d10e, C_bf.f_bf_e_49);
      } else if (var0.endsWith("/") & !f_al_a_523beb0a.equals("/")) {
         if (f_al_a_5a) {
            f_al_a_129a3.m_a_48817c60(C_bi.f_bi_h_1570d10e, C_bf.f_bf_e_49);
            f_al_a_129a3.m_a_48817c60(f_al_b_1570d10e, C_bf.f_bf_g_49);
            f_al_a_129a3.m_a_48817c60(f_al_c_1570d10e, C_bf.f_bf_g_49);
         } else {
            f_al_a_129a3.m_a_48817c60(f_al_c_1570d10e, C_bf.f_bf_e_49);
         }

         f_al_a_129a3.m_c_aad3b1ff(f_al_a_523beb0a + var0);
      } else {
         f_al_a_129a3.m_a_48817c60(f_al_b_1570d10e, C_bf.f_bf_e_49);
         int var1 = var0.lastIndexOf(46);
         StringBuffer var2 = new StringBuffer();
         if (var1 != -1) {
            var2 = var2.append(var0.substring(var1 + 1).toUpperCase()).append(" file");
         }

         f_al_a_129a3.m_c_aad3b1ff(var2.toString());
      }
   }

   public final void m_a_e4015df8(C_e var1, C_ca var2) {
      String var3 = (String)var1.f_e_a_5f790d9c;
      var2.f_ca_a_523beb0a = var3;
      var2.f_ca_a_129e1 = f_al_a_12a00.m_a_485a59b9(var3.endsWith("/") ? 0 : 1);
      var2.f_ca_b_49 = f_al_a_129a3.m_d_9b68();
      var2.f_ca_a_49 = C_bq.m_a_134621(112);
   }

   public final void m_a_cb3dd160(C_bf var1) {
      if (var1 == f_al_a_129a3) {
         p_al_a_aad3b1ff((String)f_al_a_129a3.m_a_2477921().f_e_a_5f790d9c);
      }
   }

   public final void m_b_cb3dd160(C_bf var1) {
   }

   public final void m_a_f31d59c0(C_bf var1, int var2, int var3) {
   }

   public static void m_a_9b75() throws recovered.C_ar {
      if (f_al_a_2405a4 == null) {
         new C_al();
      }

      p_al_b_9b75();
      f_al_a_523beb0a = "/";
      f_al_a_6dccaaa5 = C_au.m_a_855a3144("/", f_al_a_5a);
      p_al_c_9b75();
      f_al_a_129a3.m_b_297a162c(Jimm.f_jimm_Jimm_a_4a58c677);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (C_bi.m_a_cb3dd164(f_al_a_129a3)) {
         if (var1 == f_al_c_1570d10e) {
            String var6;
            if ((var6 = (String)f_al_a_129a3.m_a_2477921().f_e_a_5f790d9c).equals("../")) {
               int var7;
               f_al_a_523beb0a = (var7 = f_al_a_523beb0a.lastIndexOf(47, f_al_a_523beb0a.length() - 2)) != -1 ? f_al_a_523beb0a.substring(0, var7 + 1) : "/";
               p_al_b_9b75();

               try {
                  f_al_a_6dccaaa5 = C_au.m_a_855a3144(f_al_a_523beb0a, f_al_a_5a);
               } catch (C_ar var3) {
                  C_ar.m_a_aef55300(var3);
               }

               p_al_c_9b75();
               return;
            }

            if (var6.endsWith("/")) {
               f_al_a_523beb0a = f_al_a_523beb0a + var6;
               p_al_b_9b75();

               try {
                  f_al_a_6dccaaa5 = C_au.m_a_855a3144(f_al_a_523beb0a, f_al_a_5a);
               } catch (C_ar var4) {
                  C_ar.m_a_aef55300(var4);
               }

               p_al_c_9b75();
               return;
            }

            f_al_a_12bb2.m_a_aad3b1ff(f_al_a_523beb0a + var6);
            return;
         }

         if (var1 == f_al_b_1570d10e) {
            String var5;
            if ((var5 = (String)f_al_a_129a3.m_a_2477921().f_e_a_5f790d9c).endsWith("/")) {
               f_al_a_12bb2.m_b_aad3b1ff(f_al_a_523beb0a + var5);
               return;
            }

            f_al_a_12bb2.m_a_aad3b1ff(f_al_a_523beb0a + var5);
            return;
         }

         if (var1 == f_al_a_1570d10e) {
            if (f_al_a_12bb2.m_a_46a7a37a() != null || f_al_a_5a) {
               C_w.m_a_9b75() ;
               return;
            }

            C_bq.f_bq_a_240927.m_a_9b75() ;
         }
      }
   }
}
