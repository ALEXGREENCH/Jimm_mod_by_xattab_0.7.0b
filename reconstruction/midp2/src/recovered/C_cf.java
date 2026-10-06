package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.Util. Release class: cf. */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class C_cf {
   public static final byte[] f_cf_a_b47 = m_a_529fb918("09,46,13,49,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_b_b47 = m_a_529fb918("09,46,13,4E,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_c_b47 = m_a_529fb918(
      "7b,30,39,34,36,31,33,34,45,2D,34,43,37,46,2D,31,31,44,31,2D,38,32,32,32,2D,34,34,34,35,35,33,35,34,30,30,30,30,7D", ',', 16
   );
   private static final byte[] f_cf_z_b47 = m_a_529fb918("09,49,13,49,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_d_b47 = m_a_529fb918("4D,69,72,61,6E,64,61,4D,00,06,03,00,00,03,08,07", ',', 16);
   private static final byte[] f_cf_A_b47 = m_a_529fb918("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,09", ',', 16);
   private static final byte[] f_cf_B_b47 = m_a_529fb918("*SIM client  ,00,00,00,00", ',', 16);
   private static final byte[] f_cf_C_b47 = m_a_529fb918("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,00", ',', 16);
   private static final byte[] f_cf_D_b47 = m_a_529fb918("*Licq client ,00,00,00,00", ',', 16);
   public static final byte[] f_cf_e_b47 = m_a_529fb918("*Kopete ICQ  ,00,0c,00,02", ',', 16);
   public static final byte[] f_cf_f_b47 = m_a_529fb918("*&RQinside,08,08,09,00,00,00,00", ',', 16);
   public static final byte[] f_cf_g_b47 = m_a_529fb918("56,3F,C8,09,0B,6F,41,*QIP 2005a", ',', 16);
   public static final byte[] f_cf_h_b47 = m_a_529fb918("56,3F,C8,09,0B,6F,41,*QIP     !", ',', 16);
   public static final byte[] f_cf_i_b47 = m_a_529fb918("51,AD,D1,90,72,04,47,3D,A1,A1,49,F4,A3,97,A4,1F", ',', 16);
   public static final byte[] f_cf_j_b47 = m_a_529fb918("7C,73,75,02,C3,BE,4F,3E,A6,9F,01,53,13,43,1E,1A", ',', 16);
   public static final byte[] f_cf_k_b47 = m_a_529fb918("*VmICQ ,76,30,2E,31,2E,39,62,00,00,00", ',', 16);
   private static final byte[] f_cf_E_b47 = m_a_529fb918("74,ED,C3,36,44,DF,48,5B,8B,1C,67,1A,1F,86,09,9F", ',', 16);
   public static final byte[] f_cf_l_b47 = m_a_529fb918("dd,16,f2,02,84,e6,11,d4,90,db,00,10,4b,9b,4b,7d", ',', 16);
   public static final byte[] f_cf_m_b47 = m_a_529fb918("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,92", ',', 16);
   public static final byte[] f_cf_n_b47 = m_a_529fb918("01,38,ca,7b,76,9a,49,15,88,f2,13,fc,00,97,9e,a8", ',', 16);
   public static final byte[] f_cf_o_b47 = m_a_529fb918("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,76,65,72", ',', 16);
   public static final byte[] f_cf_p_b47 = m_a_529fb918("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,77,61,70", ',', 16);
   public static final byte[] f_cf_q_b47 = m_a_529fb918("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,61,64,76", ',', 16);
   private static final byte[] f_cf_F_b47 = m_a_529fb918("a0,e9,3f,37,4f,e9,d3,11,bc,d2,00,04,ac,96,dd,96", ',', 16);
   private static final byte[] f_cf_G_b47 = m_a_529fb918("09,46,13,46,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_H_b47 = m_a_529fb918("09,46,13,45,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_I_b47 = m_a_529fb918("74,8F,24,20,62,87,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_J_b47 = m_a_529fb918("4D,49,50,20", ',', 16);
   private static final byte[] f_cf_K_b47 = m_a_529fb918("*Yapp", ',', 16);
   private static final byte[] f_cf_L_b47 = m_a_529fb918("*Smaper", ',', 16);
   public static final byte[] f_cf_r_b47 = m_a_529fb918("1A,09,3C,6C,D7,FD,4E,C5,9D,51,A6,47,4E,34,F5,A0", ',', 16);
   public static final byte[] f_cf_s_b47 = m_a_529fb918("09,46,13,43,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_t_b47 = m_a_529fb918("09,46,13,44,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_u_b47 = m_a_529fb918("*Jimm ,00,00,00,00,00,00,00,00,00,00,00", ',', 16);
   public static final byte[] f_cf_v_b47 = m_a_529fb918("09,46,13,4C,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   public static final byte[] f_cf_w_b47 = m_a_529fb918("56,3f,c8,09,0b,6f,41,bd,9f,79,42,26,09,df,a2,f3", ',', 16);
   public static final byte[] f_cf_x_b47 = m_a_529fb918("*mChat icq,20,32,2E,33,2E,30,6D", ',', 16);
   private static final byte[] f_cf_M_b47 = m_a_529fb918("09,46,13,48,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_N_b47 = m_a_529fb918("*NatICQ", ',', 16);
   private static final byte[] f_cf_O_b47 = m_a_529fb918("62,61,79,61,6E,49,43,51", ',', 16);
   private static final byte[] f_cf_P_b47 = m_a_529fb918("44,5B,69,5D,43,68,61,74", ',', 16);
   private static final byte[] f_cf_Q_b47 = m_a_529fb918("*qutim", ',', 16);
   private static final byte[] f_cf_R_b47 = m_a_529fb918("50,49,47,45,4F,4E,21", ',', 16);
   private static final byte[] f_cf_S_b47 = m_a_529fb918("76,65,72,3A", ',', 16);
   private static final byte[] f_cf_T_b47 = m_a_529fb918("09,46,E0,01,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_U_b47 = m_a_529fb918("09,46,E0,02,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   private static final byte[] f_cf_V_b47 = m_a_529fb918("77,4A,69,6D,6D", ',', 16);
   private static final byte[] f_cf_W_b47 = m_a_529fb918("4C,6F,63,49,44", ',', 16);
   private static final byte[] f_cf_X_b47 = m_a_529fb918("C8,95,3A,9F,21,F1,4F,AA,B0,B2,6D,E6,63,AB,F5,B7", ',', 16);
   private static final byte[] f_cf_Y_b47 = m_a_529fb918("69,63,71", ',', 16);
   private static final byte[] f_cf_Z_b47 = m_a_529fb918("09,46", ',', 16);
   private static final byte[] f_cf_aa_b47 = m_a_529fb918("4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
   private static final String[] f_cf_a_6dccaaa5 = m_a_639c22ad(
      "Not detected|QIP|Miranda|&RQ|R&Q|Trillian|SIM|Kopete|Jimm|StICQ|Agile Messenger|Libicq2000|VmICQ|QIP PDA (Symbian)|QIP PDA (Windows)|QIP Infium|ICQ v6|ICQ Lite|ICQ Lite v4|ICQ Lite v5|ICQ 2003b|ICQ2GO!|mChat|Mac ICQ|Pidgin (Gaim)|GnomeICU|LICQ|MIP|Yapp|Sm@peR|Mail.ru Agent|BayanICQ|D[i]Chat|wJimm|LocID|qutIM|PIGEON!|ICQ v7|Slick|IM2|NatICQ|SmartICQ|ICQ for PPC|mICQ|WebICQ|StrICQ|YSM|vICQ|Alicq|CenterICQ|Libicq2000|SPAM|AOL AIM|",
      '|'
   );
   private static byte[] f_cf_ab_b47 = m_a_529fb918("F3,26,81,C4,39,86,DB,92,71,A3,B9,E6,53,7A,95,7C", ',', 16);
   private static int f_cf_a_49 = 0;
   private static final byte[] f_cf_ac_b47 = m_a_529fb918("31,28,31,30,31,30,31,31,30,31,30,31", ',', 10);
   private static final int[] f_cf_a_b4e = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
   static final byte[] f_cf_y_b47 = m_a_529fb918("*AOL Instant Messenger (SM)", ',', 16);
   private static byte[] f_cf_ad_b47 = m_a_529fb918(
      "-128,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", ',', 10
   );
   private static long[] f_cf_a_b4f = new long[4];
   private static long[] f_cf_b_b4f = new long[2];
   private static byte[] f_cf_ae_b47 = new byte[64];
   private static byte[] f_cf_af_b47 = new byte[16];

   public static void m_a_31f1f57c(String var0, int var1, int var2, int var3, byte[] var4, int var5, boolean var6) {
      byte var7 = 0;
      String var8 = "";
      long var9 = 0L;
      C_aw var13;
      if ((var13 = C_w.m_a_513388b0(var0)) != null) {
         if (var4 != null) {
            for (int var11 = 0; var11 < var4.length / 16; var11++) {
               int var12 = var11 << 4;
               if (p_cf_a_5c8eef72(var4, var12, f_cf_a_b47, 0, 16)) {
                  var9 |= 1L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_b_b47, 0, 16)) {
                  var9 |= 2L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_d_b47, 0, 8)) {
                  var9 |= 4294967296L;
                  var8 = p_cf_c_55a39fc4(var4, 2, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_Y_b47, 0, 3)) {
                  var9 |= 17179869184L;
                  var8 = p_cf_c_55a39fc4(var4, 2, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_A_b47, 0, 16)) {
                  var9 |= 8589934592L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_B_b47, 0, 12)) {
                  var9 |= 34359738368L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_C_b47, 0, 16)) {
                  var9 |= 68719476736L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_D_b47, 0, 12)) {
                  var9 |= 137438953472L;
                  var8 = p_cf_c_55a39fc4(var4, 26, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_z_b47, 0, 16)) {
                  var9 |= 137438953472L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_e_b47, 0, 12)) {
                  var9 |= 274877906944L;
                  var8 = p_cf_c_55a39fc4(var4, 7, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_f_b47, 0, 9)) {
                  var9 |= 1099511627776L;
                  var8 = p_cf_c_55a39fc4(var4, 3, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_g_b47, 0, 16)) {
                  var9 |= 2199023255552L;
                  var8 = p_cf_c_55a39fc4(var4, 1, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_j_b47, 0, 16)) {
                  var9 |= 549755813888L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_E_b47, 0, 16)) {
                  var9 |= 4398046511104L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_l_b47, 0, 16)) {
                  var9 |= 8796093022208L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_m_b47, 0, 16)) {
                  var9 |= 4L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_k_b47, 0, 6)) {
                  var9 |= 17592186044416L;
                  var8 = p_cf_c_55a39fc4(var4, 12, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_i_b47, 0, 16)) {
                  var9 |= 35184372088832L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_F_b47, 0, 16)) {
                  var9 |= 70368744177664L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_G_b47, 0, 16)) {
                  var9 |= 8L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_H_b47, 0, 16)) {
                  var9 |= 128L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_n_b47, 0, 16)) {
                  var9 |= 2048L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_T_b47, 0, 16)) {
                  var9 |= 8192L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_U_b47, 0, 16)) {
                  var9 |= 16384L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_X_b47, 0, 16)) {
                  var9 |= 32768L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_h_b47, 0, 16)) {
                  var9 |= 562949953421312L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_r_b47, 0, 16)) {
                  var9 |= 32L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_s_b47, 0, 16)) {
                  var9 |= 64L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_u_b47, 0, 5)) {
                  var9 |= 4503599627370496L;
                  var8 = p_cf_c_55a39fc4(var4, 8, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_V_b47, 0, 5)) {
                  var9 |= 576460752303423488L;
                  var8 = p_cf_c_55a39fc4(var4, 33, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_W_b47, 0, 5)) {
                  var9 |= 1152921504606846976L;
                  var8 = p_cf_c_55a39fc4(var4, 34, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_Q_b47, 0, 5)) {
                  var9 |= 72057594037927936L;
                  var8 = p_cf_c_55a39fc4(var4, 35, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_R_b47, 0, 7)) {
                  var9 |= 144115188075855872L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_S_b47, 0, 4)) {
                  var9 |= 288230376151711744L;
                  var8 = p_cf_c_55a39fc4(var4, 36, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_O_b47, 0, 8)) {
                  var9 |= 140737488355328L;
                  var8 = p_cf_c_55a39fc4(var4, 31, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_P_b47, 0, 8)) {
                  var9 |= 9007199254740992L;
                  var8 = p_cf_c_55a39fc4(var4, 32, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_v_b47, 0, 16)) {
                  var9 |= 256L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_t_b47, 0, 16)) {
                  var9 |= 512L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_w_b47, 0, 16)) {
                  var9 |= 1024L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_x_b47, 0, 9)) {
                  var9 |= 18014398509481984L;
                  var8 = p_cf_c_55a39fc4(var4, 22, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_J_b47, 0, 4)) {
                  var9 |= 1125899906842624L;
                  var8 = p_cf_c_55a39fc4(var4, 27, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_K_b47, 0, 4)) {
                  var9 |= 2251799813685248L;
                  var8 = p_cf_c_55a39fc4(var4, 28, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_L_b47, 0, 5)) {
                  var9 |= 281474976710656L;
                  var8 = p_cf_c_55a39fc4(var4, 29, var11);
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_M_b47, 0, 16)) {
                  var9 |= 4096L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_N_b47, 0, 6)) {
                  var9 |= 36028797018963968L;
               } else if (p_cf_a_5c8eef72(var4, var12, f_cf_I_b47, 0, 16)) {
                  var9 |= 16L;
               }
            }

            var13.m_a_255f295(75, (int)var9);
         }

         if (!var6) {
            if ((var9 & 72057594037927936L) != 0L) {
               var7 = 35;
            } else if ((var9 & 144115188075855872L) != 0L) {
               var7 = 36;
            } else if ((var9 & 17592186044416L) != 0L) {
               var7 = 12;
            } else if ((var9 & 35184372088832L) != 0L) {
               var7 = 13;
            } else if ((var9 & 562949953421312L) != 0L) {
               var7 = 14;
            } else if ((var9 & 18014398509481984L) != 0L) {
               var7 = 22;
            } else if ((var9 & 2199023255552L) != 0L) {
               var7 = 1;
               if (var1 >>> 24 != 0) {
                  var8 = var8 + " (" + (var1 >>> 24) + (var1 >> 16 & 0xFF) + (var1 >> 8 & 0xFF) + (var1 & 0xFF) + ")";
               }
            } else if ((var9 & 549755813888L) != 0L) {
               var7 = 15;
               if ((var1 & 65535) != 0) {
                  var8 = var8 + "(" + (var1 & 65535) + ")";
               }
            } else if (var5 == 31337) {
               if ((var9 & 2L) != 0L) {
                  var7 = 15;
               } else {
                  var7 = 1;
                  var8 = "2005a";
               }
            } else if ((var9 & 4503599627370496L) != 0L) {
               var7 = 8;
            } else if ((var9 & 576460752303423488L) != 0L) {
               var7 = 33;
            } else if ((var9 & 1152921504606846976L) != 0L) {
               var7 = 34;
            } else if ((var9 & 140737488355328L) != 0L) {
               var7 = 31;
            } else if ((var9 & 9007199254740992L) != 0L) {
               var7 = 32;
            } else if ((var9 & 1125899906842624L) != 0L) {
               var7 = 27;
            } else if ((var9 & 2251799813685248L) != 0L) {
               var7 = 28;
            } else if ((var9 & 281474976710656L) != 0L) {
               var7 = 29;
            } else if ((var9 & 2048L) != 0L) {
               if ((var9 & 256L) != 0L) {
                  var7 = 30;
               } else {
                  var7 = 16;
               }
            } else if ((var9 & 32768L) != 0L) {
               var7 = 17;
               if ((var9 & 8192L) != 0L && (var9 & 16384L) != 0L) {
                  var7 = 37;
               }
            } else if ((var9 & 8589934592L) != 0L) {
               var7 = 5;
            } else if ((var9 & 4398046511104L) != 0L) {
               var7 = 39;
            } else if ((var9 & 103079215104L) != 0L) {
               var7 = 6;
            } else if ((var9 & 274877906944L) != 0L) {
               var7 = 7;
            } else if ((var9 & 137438953472L) != 0L) {
               var7 = 26;
            } else if ((var9 & 16L) != 0L) {
               if ((var9 & 2L) != 0L) {
                  var7 = 24;
               } else {
                  var7 = 52;
               }
            } else {
               if ((var9 & 2L) != 0L) {
                  switch (var5) {
                     case 10:
                        if ((var9 & 1024L) != 0L && (var9 & 4L) != 0L) {
                           var7 = 20;
                        }
                     case 7:
                        if ((var9 & 1L) == 0L && (var9 & 512L) == 0L && var1 == 0 && var2 == 0 && var3 == 0) {
                           var7 = 21;
                        }
                        break;
                     default:
                        if (var1 == 0 && var2 == 0 && var3 == 0) {
                           if ((var9 & 4L) != 0L) {
                              var7 = 17;
                              if ((var9 & 256L) != 0L && (var9 & 32L) != 0L) {
                                 if ((var9 & 64L) != 0L) {
                                    var7 = 19;
                                 } else {
                                    var7 = 18;
                                 }
                              }
                           } else if ((var9 & 4096L) != 0L) {
                              var7 = 38;
                           } else if ((var9 & 36028797018963968L) != 0L) {
                              var7 = 40;
                           } else {
                              var7 = 10;
                           }
                        }
                  }
               }

               if ((var9 & 8796093022208L) != 0L) {
                  var7 = 23;
               } else if ((var1 & -8454144) == 2097152000) {
                  var7 = 26;
                  int var14;
                  if ((var14 = var1 & 65535) % 10 != 0) {
                     var8 = var14 / 1000 + "." + var14 / 10 % 100 + "." + var14 % 10;
                  } else {
                     var8 = var14 / 1000 + "." + var14 / 10 % 100;
                  }
               } else {
                  switch (var1) {
                     case -2458:
                        var7 = 4;
                        var8 = (var2 & 65535) + "";
                        break;
                     case -190:
                        var7 = 43;
                        break;
                     case -129:
                        var7 = 3;
                        var8 = (var2 >>> 24) + "." + (var2 >> 16 & 0xFF) + "." + (var2 >> 8 & 0xFF) + "." + (var2 & 0xFF);
                        break;
                     case -113:
                        var7 = 45;
                        break;
                     case -85:
                        var7 = 46;
                        break;
                     case -66:
                        var7 = 48;
                        break;
                     case -2:
                        if (var3 == var1) {
                           var7 = 8;
                        }
                        break;
                     case -1:
                        if (var2 == 0 && var3 != -1) {
                           if (var5 == 7) {
                              var7 = 44;
                              break;
                           }

                           if (var3 == 997345517 && (var9 & 2L) == 0L && (var9 & 4L) == 0L) {
                              var7 = 51;
                              break;
                           }
                        }

                        var7 = 2;
                        var8 = (var2 >> 24 & 127) + "." + (var2 >> 16 & 0xFF) + "." + (var2 >> 8 & 0xFF) + "." + (var2 & 0xFF);
                        break;
                     case 67312000:
                        var7 = 47;
                        break;
                     case 984052718:
                        if (var2 == 983982976 && var3 == 981957186) {
                           if (var5 == 7 && (var9 & 1L) != 0L && (var9 & 512L) != 0L && (var9 & 4L) != 0L) {
                              var7 = 49;
                           } else {
                              var7 = 11;
                           }
                        }
                        break;
                     case 997567497:
                        var7 = 5;
                        break;
                     case 1000922031:
                        if (var5 == 2) {
                           var7 = 9;
                        }
                        break;
                     case 1072798699:
                        if (var5 == 8 && var1 == var3) {
                           var7 = 39;
                        }
                        break;
                     case 1107424276:
                        if ((var2 & var3) == var1 && var5 == 8) {
                           var7 = 51;
                        }
                        break;
                     case Integer.MAX_VALUE:
                        if ((var9 & 4294967296L) != 0L || (var9 & 17179869184L) != 0L) {
                           var7 = 2;
                           var8 = var8 + " (ICQ " + (var2 >> 24 & 127) + "." + (var2 >> 16 & 0xFF) + "." + (var2 >> 8 & 0xFF) + "." + (var2 & 0xFF) + ")";
                        }
                  }

                  if (var7 == 0) {
                     if (var1 != 0 && var1 == var3 && var3 == var2 && var9 == 0L) {
                        var7 = 47;
                     } else if ((var9 & 70368744177665L) != 0L && var1 == 0 && var2 == 0 && var3 == 0 && var5 == 0) {
                        var7 = 42;
                     } else {
                        label496: {
                           if (var5 == 7) {
                              if ((var9 & 1L) != 0L && (var9 & 512L) != 0L) {
                                 if (var1 == 0 && var2 == 0 && var3 == 0) {
                                    var7 = 3;
                                    break label496;
                                 }
                              } else if ((var9 & 4L) != 0L) {
                                 var7 = 25;
                                 break label496;
                              }
                           }

                           if (var1 > 889192448 && var1 < 1073741824) {
                              switch (var5) {
                                 case 9:
                                    var7 = 17;
                                    break;
                                 case 10:
                                    var7 = 20;
                              }
                           }
                        }
                     }
                  }
               }
            }

            var13.m_a_255f295(76, var7);
            var13.m_a_4f708078(2, var8);
         }
      }
   }

   public static String m_a_f2253399(byte var0) {
      return f_cf_a_6dccaaa5[var0];
   }

   private static String p_cf_c_55a39fc4(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[16];
      System.arraycopy(var0, var2 << 4, var3, 0, 16);
      String var4 = "";
      if (var1 == 2) {
         if (var3[3] == 112 || var3[3] == 106) {
            var4 = "0." + var3[5] + "." + var3[6] + "." + var3[7];
         } else if (var3[12] == 0 && var3[13] == 0 && var3[14] == 0 && var3[15] == 1) {
            var4 = "0.1.2.0";
         } else if (var3[12] == 0 && var3[13] <= 3 && var3[14] <= 3 && var3[15] <= 1) {
            var4 = "0." + var3[13] + "." + var3[14] + "." + var3[15];
         } else {
            var4 = (var3[8] & 127) + "." + var3[9] + "." + var3[10] + "." + var3[11];
         }
      } else if (var1 == 26) {
         var4 = var3[12] + "." + var3[13] % 100 + "." + var3[14];
      } else if (var1 == 7) {
         var4 = var3[12] + "." + var3[13] + "." + var3[14] + "." + var3[15];
      } else if (var1 == 3) {
         var4 = var3[12] + "." + var3[11] + "." + var3[10] + "." + var3[9];
      } else if (var1 == 8) {
         var4 = m_a_20e7da8(var3, 5, 11, false);
      } else if (var1 == 33) {
         var4 = m_a_20e7da8(var3, 6, 7, false);
      } else if (var1 == 34) {
         var4 = Integer.toHexString(var3[11]) + "." + Integer.toHexString(var3[12]) + " p" + var3[15];
      } else if (var1 == 31) {
         var4 = m_a_20e7da8(var3, 8, 4, false);
      } else if (var1 == 32) {
         var4 = m_a_20e7da8(var3, 9, 6, false);
      } else if (var1 == 1) {
         var4 = m_a_20e7da8(var3, 11, 5, false);
      } else if (var1 == 27) {
         var4 = m_a_20e7da8(var3, 4, 12, false);
      } else if (var1 == 28) {
         var4 = m_a_20e7da8(var3, 8, 5, false);
      } else if (var1 == 29) {
         var4 = m_a_20e7da8(var3, 7, 6, false);
      } else if (var1 == 35) {
         if (var3[6] == 46) {
            int var5 = var3[5] - 48;
            var1 = var3[7] - 48;
            var4 = var5 + "." + var1;
         } else {
            byte var6 = var3[6];
            byte var9 = var3[7];
            String var7 = var6 + "." + var9;
            switch (var3[5]) {
               case 108:
                  var4 = var7 + " (Linux)";
                  break;
               case 109:
                  var4 = var7 + " (MacOS)";
                  break;
               case 117:
               default:
                  var4 = var7 + " (Unknown OS)";
                  break;
               case 119:
                  var4 = var7 + " (Windows)";
            }
         }
      } else if (var1 == 36) {
         var4 = m_a_20e7da8(var3, 4, 7, false);
      } else if (var1 == 22) {
         var4 = m_a_20e7da8(var3, 10, 6, false);
      } else if (var1 == 12) {
         var4 = m_a_20e7da8(var3, 6, 7, false);
      }

      return var4;
   }

   public static synchronized int m_a_9b68() {
      return ++f_cf_a_49;
   }

   public static int m_a_49634b7a(byte[] var0, int var1) {
      return var0[var1] & 0xFF;
   }

   public static void m_a_e306985c(byte[] var0, int var1, int var2) {
      var0[var1] = (byte)var2;
   }

   public static int m_a_e306d820(byte[] var0, int var1, boolean var2) {
      int result;
      if (var2) {
         result = var0[var1] << 8 & 0xFF00 | var0[++var1] & 255;
      } else {
         result = var0[var1] & 255 | var0[++var1] << 8 & 0xFF00;
      }

      return result;
   }

   public static DataInputStream a$6f0c2d54(byte[] var0) {
      return new DataInputStream(new ByteArrayInputStream(var0, 0, var0.length));
   }

   public static int a$175c50c1(DataInputStream var0) throws java.io.IOException {
      return var0.readByte() & 0xFF | var0.readByte() << 8 & 0xFF00;
   }

   public static String m_a_b4050118(DataInputStream var0) throws java.io.IOException {
      int var1;
      if ((var1 = a$175c50c1(var0)) == 0) {
         return new String();
      } else {
         byte[] var2 = new byte[var1];
         var0.readFully(var2);
         return m_a_79834524(var2);
      }
   }

   public static void m_a_559c4327(ByteArrayOutputStream var0, int var1, boolean var2) {
      if (var2) {
         var0.write(var1 >> 8 & 0xFF & 0xFF);
         var0.write(var1 & 0xFF);
      } else {
         var0.write(var1 & 0xFF);
         var0.write(var1 >> 8 & 0xFF & 0xFF);
      }
   }

   public static void m_a_55a417bd(ByteArrayOutputStream var0, byte[] var1) {
      try {
         var0.write(var1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public static void b$559c4327(ByteArrayOutputStream var0, int var1) {
      var0.write(var1 & 0xFF);
      var0.write(var1 >> 8 & 0xFF & 0xFF);
      var0.write(var1 >> 16 & 0xFF & 0xFF);
      var0.write(var1 >>> 24);
   }

   public static void m_a_5557994d(ByteArrayOutputStream var0, int var1) {
      var0.write(var1);
   }

   public static void m_a_e13aaa14(ByteArrayOutputStream var0, String var1, boolean var2) {
      byte[] var3 = m_a_44c4d6c8(var1, var2);
      m_a_559c4327(var0, var3.length, true);
      var0.write(var3, 0, var3.length);
   }

   public static void m_a_2a17548d(int var0, ByteArrayOutputStream var1, String var2, boolean var3) {
      m_a_559c4327(var1, var0, var3);
      Object var4 = null;
      byte[] var5 = m_a_44c4d6c8(var2, false);
      m_a_559c4327(var1, var5.length + 3, false);
      m_a_559c4327(var1, var5.length + 1, false);
      var1.write(var5, 0, var5.length);
      var1.write(0);
   }

   public static void m_a_1a21c327(int var0, ByteArrayOutputStream var1, String var2) {
      m_a_2a17548d(var0, var1, var2, true);
   }

   public static void m_a_c9e49450(int var0, ByteArrayOutputStream var1, int var2, String var3) {
      m_a_559c4327(var1, 490, false);
      Object var4 = null;
      byte[] var5 = m_a_44c4d6c8(var3, false);
      m_a_559c4327(var1, var5.length + 5, false);
      m_a_559c4327(var1, var2, false);
      m_a_559c4327(var1, var5.length + 1, false);
      var1.write(var5, 0, var5.length);
      var1.write(0);
   }

   public static int m_b_49634b7a(byte[] var0, int var1) {
      return m_a_e306d820(var0, var1, true);
   }

   public static void m_a_7dcd25f8(byte[] var0, int var1, int var2, boolean var3) {
      if (var3) {
         var0[var1] = (byte)(var2 >> 8);
         var0[++var1] = (byte)var2;
      } else {
         var0[var1] = (byte)var2;
         var0[++var1] = (byte)(var2 >> 8);
      }
   }

   public static void m_b_e306985c(byte[] var0, int var1, int var2) {
      m_a_7dcd25f8(var0, var1, var2, true);
   }

   public static long m_a_e306d821(byte[] var0, int var1, boolean var2) {
      long var11;
      if (var2) {
         long var10000 = (long)var0[var1] << 24 & -16777216L;
         var11 = 0L;
         var11 = var10000 | (long)var0[++var1] << 16 & 16711680L | (long)var0[++var1] << 8 & 65280L | var0[++var1] & 255L;
      } else {
         long var15 = var0[var1] & 255L;
         var11 = 0L;
         var11 = var15 | (long)var0[++var1] << 8 & 65280L | (long)var0[++var1] << 16 & 16711680L | (long)var0[++var1] << 24 & -16777216L;
      }

      return var11;
   }

   public static long m_a_49634b7b(byte[] var0, int var1) {
      return m_a_e306d821(var0, var1, true);
   }

   public static void m_a_7dcd9a57(byte[] var0, int var1, long var2, boolean var4) {
      if (var4) {
         var0[var1] = (byte)(var2 >> 24 & 255L);
         var0[++var1] = (byte)(var2 >> 16 & 255L);
         var0[++var1] = (byte)(var2 >> 8 & 255L);
         var0[++var1] = (byte)(var2 & 255L);
      } else {
         var0[var1] = (byte)(var2 & 255L);
         var0[++var1] = (byte)(var2 >> 8 & 255L);
         var0[++var1] = (byte)(var2 >> 16 & 255L);
         var0[++var1] = (byte)(var2 >> 24 & 255L);
      }
   }

   public static void m_a_e3069c1d(byte[] var0, int var1, long var2) {
      m_a_7dcd9a57(var0, var1, var2, true);
   }

   public static byte[] m_a_e3062636(byte[] var0, int var1) {
      if (var1 + 4 > var0.length) {
         return null;
      } else {
         int var3 = var1 + 2;
         Object var2 = null;
         int var4 = m_a_e306d820(var0, var3, true);
         if (var1 + 4 + var4 > var0.length) {
            return null;
         } else {
            byte[] var5 = new byte[var4];
            System.arraycopy(var0, var1 + 4, var5, 0, var4);
            return var5;
         }
      }
   }

   public static String m_a_20e7da8(byte[] var0, int var1, int var2, boolean var3) {
      if (var0.length < var1 + var2) {
         return null;
      } else {
         while (var2 > 0 && var0[var1 + var2 - 1] == 0) {
            var2--;
         }

         byte[] var4 = var0;
         boolean var10000;
         if ((var2 & 1) != 0) {
            var10000 = false;
         } else {
            int var6 = var1 + var2;
            boolean var7 = true;
            int var8 = var1;

            while (true) {
               if (var8 >= var6) {
                  var10000 = var7;
                  break;
               }

               byte var5;
               if ((var5 = var4[var8]) > 0 && var5 < 9) {
                  var10000 = true;
                  break;
               }

               if (var5 == 0 && var4[var8 + 1] != 0) {
                  var10000 = true;
                  break;
               }

               if (var5 > 32 || var5 < 0) {
                  var7 = false;
               }

               var8 += 2;
            }
         }

         if (var10000) {
            return m_b_55a39fc4(var0, var1, var2);
         } else {
            if (var3) {
               try {
                  byte[] var10;
                  m_a_7dcd25f8(var10 = new byte[var2 + 2], 0, var2, true);
                  System.arraycopy(var0, var1, var10, 2, var2);
                  ByteArrayInputStream var11 = new ByteArrayInputStream(var10);
                  return new DataInputStream(var11).readUTF();
               } catch (Exception var9) {
               }
            }

            return C_bq.m_a_134632(133) ? p_cf_d_55a39fc4(var0, var1, var2) : new String(var0, var1, var2);
         }
      }
   }

   public static String m_a_55a39fc4(byte[] var0, int var1, int var2) {
      return m_a_20e7da8(var0, var1, var2, false);
   }

   public static String m_a_5a238448(byte[] var0, boolean var1) {
      return m_a_20e7da8(var0, 0, var0.length, var1);
   }

   public static String m_a_79834524(byte[] var0) {
      return m_a_20e7da8(var0, 0, var0.length, false);
   }

   public static long m_a_25e06f0(byte[] var0) {
      long var1 = 0L;
      long var3 = ((0L | var0[0] & 255) << 8 | var0[1] & 255) << 8;
      if (var0.length > 3) {
         var3 = (var3 | var0[2] & 255) << 8 | var0[3] & 255;
      }

      return var3;
   }

   public static String m_b_79834524(byte[] var0) {
      StringBuffer var1 = new StringBuffer(var0.length);

      for (int var3 = 0; var3 < var0.length; var3++) {
         String var2 = Integer.toHexString(256 + (var0[var3] & 255)).substring(1);
         var1.append((var2.length() < 2 ? "0" : "") + var2);
      }

      return var1.toString();
   }

   public static byte[] m_a_44c4d6c8(String var0, boolean var1) {
      if (var1) {
         try {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            DataOutputStream var10000 = new DataOutputStream(var4);
            byte[] var2 = null;
            var10000.writeUTF(var0);
            byte[] var5;
            var2 = new byte[(var5 = var4.toByteArray()).length - 2];
            System.arraycopy(var5, 2, var2, 0, var5.length - 2);
            return (byte[])var2;
         } catch (Exception var3) {
         }
      }

      return C_bq.m_a_134632(133) ? p_cf_d_afa28ebe(var0) : var0.getBytes();
   }

   public static byte[] m_a_afa28ebe(String var0) {
      return m_a_44c4d6c8(var0, false);
   }

   public static byte[] m_b_afa28ebe(String var0) {
      byte[] var1 = new byte[var0.length() << 1];

      for (int var2 = 0; var2 < var0.length(); var2++) {
         int var10001 = var2 << 1;
         char var4 = var0.charAt(var2);
         m_a_7dcd25f8(var1, var10001, var4, true);
      }

      return var1;
   }

   public static String m_b_55a39fc4(byte[] var0, int var1, int var2) {
      if (var1 + var2 <= var0.length && var2 % 2 == 0) {
         StringBuffer var3 = new StringBuffer();

         for (int var4 = var1; var4 < var1 + var2; var4 += 2) {
            var3.append((char)m_a_e306d820(var0, var4, true));
         }

         return var3.toString();
      } else {
         return null;
      }
   }

   public static String m_a_e96ea081(String var0) {
      StringBuffer var1 = new StringBuffer();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3;
         if ((var3 = var0.charAt(var2)) != 0 && var3 != '\r') {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   public static String m_b_e96ea081(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = var0.length();

      for (int var3 = 0; var3 < var2; var3++) {
         char var4;
         if ((var4 = var0.charAt(var3)) != '\r') {
            if (var4 == '\n') {
               var1.append("\r\n");
            } else {
               var1.append(var4);
            }
         }
      }

      return var1.toString();
   }

   private static boolean p_cf_a_5c8eef72(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      if (var1 + var4 <= var0.length && var4 <= var2.length) {
         for (int var5 = 0; var5 < var4; var5++) {
            if (var0[var1 + var5] != var2[var5]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static byte[] m_a_4962d961(byte[] var0) {
      byte[] var1 = new byte[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = (byte)(var0[var2] ^ f_cf_ab_b47[var2 % 16]);
      }

      return var1;
   }

   public static int m_a_134621(int var0) {
      if ((var0 & 256) != 0 && (var0 & 65535) != 256 && var0 != -1) {
         var0 &= -257;
      }

      if (var0 == -1) {
         return -1;
      } else if ((var0 & 2) != 0) {
         return 2;
      } else if ((var0 & 256) != 0) {
         return 256;
      } else if ((var0 & 512) != 0) {
         return 256;
      } else if ((var0 & 16) != 0) {
         return 16;
      } else if ((var0 & 4) != 0) {
         return 4;
      } else if ((var0 & 32) != 0) {
         return 32;
      } else if ((var0 & 8193) == 8193) {
         return 8193;
      } else if ((var0 & 12288) == 12288) {
         return 12288;
      } else if ((var0 & 20480) == 20480) {
         return 20480;
      } else if ((var0 & 24576) == 24576) {
         return 24576;
      } else if ((var0 & 1) == 1) {
         return 1;
      } else {
         return (var0 & 16384) == 16384 ? 16384 : 0;
      }
   }

   public static int m_b_134621(int var0) {
      if (var0 == 1) {
         return 1;
      } else if (var0 == 32) {
         return 32;
      } else if (var0 == 2) {
         return 19;
      } else if (var0 == 256) {
         return 256;
      } else if (var0 == 512) {
         return 256;
      } else if (var0 == 4) {
         return 5;
      } else if (var0 == 16) {
         return 17;
      } else if (var0 == 8193) {
         return 8193;
      } else if (var0 == 12288) {
         return 12288;
      } else if (var0 == 16384) {
         return 16384;
      } else if (var0 == 20480) {
         return 20480;
      } else {
         return var0 == 24576 ? 24576 : 0;
      }
   }

   public static String m_a_47921032(int var0) {
      return var0 < 10 ? "0" + String.valueOf(var0) : String.valueOf(var0);
   }

   public static String m_c_79834524(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();

         for (int var2 = 0; var2 < 4; var2++) {
            int var3 = var0[var2] & 255;
            if (var1.length() != 0) {
               var1.append('.');
            }

            var1.append(var3);
         }

         return var1.toString();
      }
   }

   public static byte[] m_c_afa28ebe(String var0) {
      byte[] var1;
      return (var1 = m_a_529fb918(var0, '.', 10)) != null && var1.length == 4 ? var1 : null;
   }

   public static boolean m_a_aad3b203(String var0) {
      boolean var1 = false;

      try {
         return m_c_afa28ebe(var0) != null;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static int m_b_9b68() {
      C_w.m_a_46ae2500();
      C_w.m_a_8f165573();
      Random var10000 = new Random(System.currentTimeMillis());
      Object var0 = null;
      int var1;
      if ((var1 = var10000.nextInt()) < 0) {
         var1 = -var1;
      }

      return var1 % 28671 + 4096;
   }

   public static boolean m_a_e3069860(byte[] var0, int var1, int var2) {
      if (var2 == 0) {
         return false;
      } else if (var0.length < var1 + var2) {
         return false;
      } else {
         while (var2 > 0) {
            byte var3 = 0;
            byte var4 = var0[var1++];
            var2--;
            if ((var4 & 224) == 192) {
               var3 = 1;
            } else if ((var4 & 240) == 224) {
               var3 = 2;
            } else if ((var4 & 248) == 240) {
               var3 = 3;
            } else if ((var4 & 252) == 248) {
               var3 = 4;
            } else if ((var4 & 254) == 252) {
               var3 = 5;
            }

            if (var3 == 0) {
               if ((var4 & 128) == 128) {
                  return false;
               }
            } else {
               for (int var5 = 0; var5 < var3; var5++) {
                  if (var2 == 0) {
                     return false;
                  }

                  if ((var0[var1++] & 192) != 128) {
                     return false;
                  }

                  var2--;
               }

               if (var2 == 0) {
                  break;
               }
            }
         }

         return true;
      }
   }

   public static String m_b_47921032(int var0) {
      Object var1 = null;
      Object var2 = null;

      try {
         if (var0 == 0) {
            return new String("0.0");
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
         return new String("0.0");
      }
   }

   public static int m_a_aad3b1f2(String var0) {
      int var1 = 0;
      boolean var2 = false;
      var1 = new String(".").charAt(0);

      try {
         byte var7 = 0;

         while (var7 < var0.length() && var1 == var0.charAt(var7)) {
            var7++;
         }

         if (var7 == var0.length() - 1) {
            return Integer.parseInt(var0) * 10000;
         } else {
            while (var1 != var0.charAt(var7)) {
               var7++;
            }

            var1 = Integer.parseInt(var0.substring(0, var7)) * 1000;
            String var4 = var0.substring(var7 + 1, var0.length());

            while (var4.length() > 4) {
               var4 = var4.substring(0, var4.length() - 1);
            }

            while (var4.length() < 4) {
               var4 = var4 + "0";
            }

            return var1 + Integer.parseInt(var4);
         }
      } catch (Exception var3) {
         return 0;
      }
   }

   public static String m_c_47921032(int var0) {
      switch (var0) {
         case 1:
            return C_bt.m_a_e96ea081("o2");
         case 2:
            return C_bt.m_a_e96ea081("A3");
         default:
            return new String();
      }
   }

   public static int m_b_aad3b1f2(String var0) {
      if (var0 == C_bt.m_a_e96ea081("o2")) {
         return 1;
      } else {
         return var0 == C_bt.m_a_e96ea081("A3") ? 2 : 0;
      }
   }

   private static byte[] p_cf_d_afa28ebe(String var0) {
      byte[] var1 = new byte[var0.length()];
      int var2 = var0.length();

      for (int var3 = 0; var3 < var2; var3++) {
         char var4;
         switch (var4 = var0.charAt(var3)) {
            case 'Ё':
               var1[var3] = -88;
               break;
            case 'Є':
               var1[var3] = -86;
               break;
            case 'І':
               var1[var3] = -78;
               break;
            case 'Ї':
               var1[var3] = -81;
               break;
            case 'ё':
               var1[var3] = -72;
               break;
            case 'є':
               var1[var3] = -70;
               break;
            case 'і':
               var1[var3] = -77;
               break;
            case 'ї':
               var1[var3] = -65;
               break;
            case 'Ґ':
               var1[var3] = -91;
               break;
            case 'ґ':
               var1[var3] = -76;
               break;
            default:
               if (var4 >= 1040 && var4 <= 1103) {
                  var1[var3] = (byte)(var4 - 1040 + 192);
               } else {
                  var1[var3] = (byte)var4;
               }
         }
      }

      return var1;
   }

   private static String p_cf_d_55a39fc4(byte[] var0, int var1, int var2) {
      int var3 = var1 + var2;

      StringBuffer var6;
      for (var6 = new StringBuffer(var2); var1 < var3; var1++) {
         int var4;
         switch (var4 = var0[var1] & 0xFF) {
            case 165:
               var6.append('Ґ');
               break;
            case 166:
            case 167:
            case 169:
            case 171:
            case 172:
            case 173:
            case 174:
            case 176:
            case 177:
            case 181:
            case 182:
            case 183:
            case 185:
            case 187:
            case 188:
            case 189:
            case 190:
            default:
               try {
                  if (var4 >= 192 && var4 <= 255) {
                     var6.append((char)(var4 + 1040 - 192));
                     continue;
                  }

                  var6.append((char)var4);
               } catch (Exception var5) {
               }
               break;
            case 168:
               var6.append('Ё');
               break;
            case 170:
               var6.append('Є');
               break;
            case 175:
               var6.append('Ї');
               break;
            case 178:
               var6.append('І');
               break;
            case 179:
               var6.append('і');
               break;
            case 180:
               var6.append('ґ');
               break;
            case 184:
               var6.append('ё');
               break;
            case 186:
               var6.append('є');
               break;
            case 191:
               var6.append('ї');
         }
      }

      return var6.toString();
   }

   private static int p_cf_c_134621(int var0) {
      for (int var1 = 0; var1 < f_cf_a_b4e.length; var1++) {
         if (f_cf_a_b4e[var1] == var0) {
            return var1 + 1;
         }
      }

      return -1;
   }

   public static long a$1385f3() {
      return m_a_25deca9(false, false);
   }

   public static long m_a_25deca9(boolean var0, boolean var1) {
      Calendar var2;
      (var2 = Calendar.getInstance()).setTime(new Date());
      long var6;
      if (var1) {
         var6 = m_a_6046c8c9(var2.get(1), p_cf_c_134621(var2.get(2)), var2.get(5), 0, 0, 0);
      } else {
         var6 = m_a_6046c8c9(var2.get(1), p_cf_c_134621(var2.get(2)), var2.get(5), var2.get(11), var2.get(12), var2.get(13));
      }

      long var4 = C_bq.m_a_134621(90);
      long var7;
      return m_a_1349e3(var7 = var6 + var4 * 3600L);
   }

   public static String m_a_87d767d1(boolean var0, boolean var1, long var2) {
      if (var2 == 0L) {
         return "***error***";
      } else {
         int[] var4 = m_a_255f4d5(var2);
         StringBuffer var3 = new StringBuffer();
         if (!var0) {
            var3.append(m_a_47921032(var4[3])).append('.').append(m_a_47921032(var4[4])).append('.').append(var4[5]).append(' ');
         }

         var3.append(m_a_47921032(var4[2])).append(':').append(m_a_47921032(var4[1]));
         if (var1) {
            var3.append(':').append(m_a_47921032(var4[0]));
         }

         return var3.toString();
      }
   }

   public static long m_a_6046c8c9(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var8 = (var0 - 1970) * 365 + var2 + (var0 - 1968) / 4;
      if (var0 >= 2000) {
         var8--;
      }

      byte var6;
      if (var0 % 4 == 0 && var0 != 2000) {
         var8--;
         var6 = 29;
      } else {
         var6 = 28;
      }

      for (int var7 = 0; var7 < var1 - 1; var7++) {
         var8 += var7 == 1 ? var6 : f_cf_ac_b47[var7];
      }

      return var8 * 24L * 3600L + var3 * 3600L + var4 * 60L + var5;
   }

   public static int[] m_a_255f4d5(long var0) {
      int var3 = (int)(var0 % 60L);
      int var4 = (int)(var0 / 60L % 60L);
      long var9;
      int var5 = (int)((var9 = var0 - var4 * 60) / 3600L % 24L);
      int var10 = (int)((var9 - var5 * 3600) / 86400L);

      int var1;
      int var7;
      for (var7 = 1970; (var1 = var10 - (var7 % 4 == 0 && var7 != 2000 ? 366 : 365)) > 0; var7++) {
         var10 = var1;
      }

      int var8 = var7 % 4 == 0 && var7 != 2000 ? 29 : 28;
      int var6 = 1;

      for (int var2 = 0; var2 < 12 && (var1 = var10 - (var2 == 1 ? var8 : f_cf_ac_b47[var2])) > 0; var2++) {
         var6++;
         var10 = var1;
      }

      return new int[]{var3, var4, var5, var10, var6, var7};
   }

   public static String m_a_2416688b(boolean var0, boolean var1) {
      return m_a_87d767d1(var0, var1, m_a_25deca9(false, false));
   }

   public static long m_a_1349e3(long var0) {
      long var2 = C_bq.m_a_134621(87);
      return var0 + var2 * 3600L;
   }

   public static String m_a_2f33e691(long var0) {
      StringBuffer var2 = new StringBuffer();
      int var3 = (int)(var0 / 86400L);
      long var5;
      int var4 = (int)((var5 = var0 % 86400L) / 3600L);
      int var6 = (int)(var5 % 3600L / 60L);
      if (var3 != 0) {
         var2.append(var3).append(' ').append(C_bt.m_a_e96ea081("M1")).append(' ');
      }

      if (var4 != 0) {
         var2.append(var4).append(' ').append(C_bt.m_a_e96ea081("33")).append(' ');
      }

      if (var6 != 0) {
         var2.append(var6).append(' ').append(C_bt.m_a_e96ea081("L3"));
      }

      return var2.toString();
   }

   public static byte[] m_b_4962d961(byte[] var0) {
      f_cf_b_b4f[0] = 0L;
      f_cf_b_b4f[1] = 0L;
      f_cf_a_b4f[0] = 1732584193L;
      f_cf_a_b4f[1] = 4023233417L;
      f_cf_a_b4f[2] = 2562383102L;
      f_cf_a_b4f[3] = 271733878L;
      p_cf_a_49634b87(var0, var0.length);
      p_cf_a_7ecb0198(var0 = new byte[8], f_cf_b_b4f, 8);
      int var1;
      var1 = (var1 = (int)(f_cf_b_b4f[0] >>> 3) & 63) < 56 ? 56 - var1 : 120 - var1;
      p_cf_a_49634b87(f_cf_ad_b47, var1);
      p_cf_a_49634b87(var0, 8);
      p_cf_a_7ecb0198(f_cf_af_b47, f_cf_a_b4f, 16);
      return f_cf_af_b47;
   }

   private static long p_cf_a_db620423(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var20;
      return ((int)(var20 = var0 + (var2 & var4 | ~var2 & var6) + var8 + var12) << (int)var10 | (int)var20 >>> (int)(32L - var10)) + var2;
   }

   private static long p_cf_b_db620423(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var19;
      return ((int)(var19 = var0 + (var2 & var6 | var4 & ~var6) + var8 + var12) << (int)var10 | (int)var19 >>> (int)(32L - var10)) + var2;
   }

   private static long p_cf_c_db620423(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var19;
      return ((int)(var19 = var0 + (var2 ^ var4 ^ var6) + var8 + var12) << (int)var10 | (int)var19 >>> (int)(32L - var10)) + var2;
   }

   private static long p_cf_d_db620423(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var20;
      return ((int)(var20 = var0 + (var4 ^ (var2 | ~var6)) + var8 + var12) << (int)var10 | (int)var20 >>> (int)(32L - var10)) + var2;
   }

   private static void p_cf_a_49634b87(byte[] var0, int var1) {
      byte[] var4 = new byte[64];
      int var3 = (int)(f_cf_b_b4f[0] >>> 3) & 63;
      if ((f_cf_b_b4f[0] = f_cf_b_b4f[0] + (var1 << 3)) < var1 << 3) {
         f_cf_b_b4f[1]++;
      }

      f_cf_b_b4f[1] = f_cf_b_b4f[1] + (var1 >>> 29);
      int var2 = 64 - var3;
      if (var1 >= var2) {
         p_cf_a_ea789700(f_cf_ae_b47, var0, var3, 0, var2);
         p_cf_a_25e06fc(f_cf_ae_b47);

         while (var2 + 63 < var1) {
            p_cf_a_ea789700(var4, var0, 0, var2, 64);
            p_cf_a_25e06fc(var4);
            var2 += 64;
         }

         var3 = 0;
      } else {
         var2 = 0;
      }

      p_cf_a_ea789700(f_cf_ae_b47, var0, var3, var2, var1 - var2);
   }

   private static void p_cf_a_ea789700(byte[] var0, byte[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var4; var5++) {
         var0[var2 + var5] = var1[var3 + var5];
      }
   }

   private static void p_cf_a_25e06fc(byte[] var0) {
      long var1 = f_cf_a_b4f[0];
      long var3 = f_cf_a_b4f[1];
      long var5 = f_cf_a_b4f[2];
      long var7 = f_cf_a_b4f[3];
      long[] var9;
      long[] var10000 = var9 = new long[16];
      boolean var13 = false;
      byte[] var10 = var0;
      long[] var14 = var10000;
      int var11 = 0;

      for (byte var12 = 0; var12 < 64; var12 += 4) {
         var14[var11] = p_cf_a_132bdb(var10[var12])
            | p_cf_a_132bdb(var10[var12 + 1]) << 8
            | p_cf_a_132bdb(var10[var12 + 2]) << 16
            | p_cf_a_132bdb(var10[var12 + 3]) << 24;
         var11++;
      }

      var1 = p_cf_a_db620423(var1, var3, var5, var7, var9[0], 7L, 3614090360L);
      var7 = p_cf_a_db620423(var7, var1, var3, var5, var9[1], 12L, 3905402710L);
      var5 = p_cf_a_db620423(var5, var7, var1, var3, var9[2], 17L, 606105819L);
      var3 = p_cf_a_db620423(var3, var5, var7, var1, var9[3], 22L, 3250441966L);
      var1 = p_cf_a_db620423(var1, var3, var5, var7, var9[4], 7L, 4118548399L);
      var7 = p_cf_a_db620423(var7, var1, var3, var5, var9[5], 12L, 1200080426L);
      var5 = p_cf_a_db620423(var5, var7, var1, var3, var9[6], 17L, 2821735955L);
      var3 = p_cf_a_db620423(var3, var5, var7, var1, var9[7], 22L, 4249261313L);
      var1 = p_cf_a_db620423(var1, var3, var5, var7, var9[8], 7L, 1770035416L);
      var7 = p_cf_a_db620423(var7, var1, var3, var5, var9[9], 12L, 2336552879L);
      var5 = p_cf_a_db620423(var5, var7, var1, var3, var9[10], 17L, 4294925233L);
      var3 = p_cf_a_db620423(var3, var5, var7, var1, var9[11], 22L, 2304563134L);
      var1 = p_cf_a_db620423(var1, var3, var5, var7, var9[12], 7L, 1804603682L);
      var7 = p_cf_a_db620423(var7, var1, var3, var5, var9[13], 12L, 4254626195L);
      var5 = p_cf_a_db620423(var5, var7, var1, var3, var9[14], 17L, 2792965006L);
      var3 = p_cf_a_db620423(var3, var5, var7, var1, var9[15], 22L, 1236535329L);
      var1 = p_cf_b_db620423(var1, var3, var5, var7, var9[1], 5L, 4129170786L);
      var7 = p_cf_b_db620423(var7, var1, var3, var5, var9[6], 9L, 3225465664L);
      var5 = p_cf_b_db620423(var5, var7, var1, var3, var9[11], 14L, 643717713L);
      var3 = p_cf_b_db620423(var3, var5, var7, var1, var9[0], 20L, 3921069994L);
      var1 = p_cf_b_db620423(var1, var3, var5, var7, var9[5], 5L, 3593408605L);
      var7 = p_cf_b_db620423(var7, var1, var3, var5, var9[10], 9L, 38016083L);
      var5 = p_cf_b_db620423(var5, var7, var1, var3, var9[15], 14L, 3634488961L);
      var3 = p_cf_b_db620423(var3, var5, var7, var1, var9[4], 20L, 3889429448L);
      var1 = p_cf_b_db620423(var1, var3, var5, var7, var9[9], 5L, 568446438L);
      var7 = p_cf_b_db620423(var7, var1, var3, var5, var9[14], 9L, 3275163606L);
      var5 = p_cf_b_db620423(var5, var7, var1, var3, var9[3], 14L, 4107603335L);
      var3 = p_cf_b_db620423(var3, var5, var7, var1, var9[8], 20L, 1163531501L);
      var1 = p_cf_b_db620423(var1, var3, var5, var7, var9[13], 5L, 2850285829L);
      var7 = p_cf_b_db620423(var7, var1, var3, var5, var9[2], 9L, 4243563512L);
      var5 = p_cf_b_db620423(var5, var7, var1, var3, var9[7], 14L, 1735328473L);
      var3 = p_cf_b_db620423(var3, var5, var7, var1, var9[12], 20L, 2368359562L);
      var1 = p_cf_c_db620423(var1, var3, var5, var7, var9[5], 4L, 4294588738L);
      var7 = p_cf_c_db620423(var7, var1, var3, var5, var9[8], 11L, 2272392833L);
      var5 = p_cf_c_db620423(var5, var7, var1, var3, var9[11], 16L, 1839030562L);
      var3 = p_cf_c_db620423(var3, var5, var7, var1, var9[14], 23L, 4259657740L);
      var1 = p_cf_c_db620423(var1, var3, var5, var7, var9[1], 4L, 2763975236L);
      var7 = p_cf_c_db620423(var7, var1, var3, var5, var9[4], 11L, 1272893353L);
      var5 = p_cf_c_db620423(var5, var7, var1, var3, var9[7], 16L, 4139469664L);
      var3 = p_cf_c_db620423(var3, var5, var7, var1, var9[10], 23L, 3200236656L);
      var1 = p_cf_c_db620423(var1, var3, var5, var7, var9[13], 4L, 681279174L);
      var7 = p_cf_c_db620423(var7, var1, var3, var5, var9[0], 11L, 3936430074L);
      var5 = p_cf_c_db620423(var5, var7, var1, var3, var9[3], 16L, 3572445317L);
      var3 = p_cf_c_db620423(var3, var5, var7, var1, var9[6], 23L, 76029189L);
      var1 = p_cf_c_db620423(var1, var3, var5, var7, var9[9], 4L, 3654602809L);
      var7 = p_cf_c_db620423(var7, var1, var3, var5, var9[12], 11L, 3873151461L);
      var5 = p_cf_c_db620423(var5, var7, var1, var3, var9[15], 16L, 530742520L);
      var3 = p_cf_c_db620423(var3, var5, var7, var1, var9[2], 23L, 3299628645L);
      var1 = p_cf_d_db620423(var1, var3, var5, var7, var9[0], 6L, 4096336452L);
      var7 = p_cf_d_db620423(var7, var1, var3, var5, var9[7], 10L, 1126891415L);
      var5 = p_cf_d_db620423(var5, var7, var1, var3, var9[14], 15L, 2878612391L);
      var3 = p_cf_d_db620423(var3, var5, var7, var1, var9[5], 21L, 4237533241L);
      var1 = p_cf_d_db620423(var1, var3, var5, var7, var9[12], 6L, 1700485571L);
      var7 = p_cf_d_db620423(var7, var1, var3, var5, var9[3], 10L, 2399980690L);
      var5 = p_cf_d_db620423(var5, var7, var1, var3, var9[10], 15L, 4293915773L);
      var3 = p_cf_d_db620423(var3, var5, var7, var1, var9[1], 21L, 2240044497L);
      var1 = p_cf_d_db620423(var1, var3, var5, var7, var9[8], 6L, 1873313359L);
      var7 = p_cf_d_db620423(var7, var1, var3, var5, var9[15], 10L, 4264355552L);
      var5 = p_cf_d_db620423(var5, var7, var1, var3, var9[6], 15L, 2734768916L);
      var3 = p_cf_d_db620423(var3, var5, var7, var1, var9[13], 21L, 1309151649L);
      var1 = p_cf_d_db620423(var1, var3, var5, var7, var9[4], 6L, 4149444226L);
      var7 = p_cf_d_db620423(var7, var1, var3, var5, var9[11], 10L, 3174756917L);
      var5 = p_cf_d_db620423(var5, var7, var1, var3, var9[2], 15L, 718787259L);
      var3 = p_cf_d_db620423(var3, var5, var7, var1, var9[9], 21L, 3951481745L);
      f_cf_a_b4f[0] = f_cf_a_b4f[0] + var1;
      f_cf_a_b4f[1] = f_cf_a_b4f[1] + var3;
      f_cf_a_b4f[2] = f_cf_a_b4f[2] + var5;
      f_cf_a_b4f[3] = f_cf_a_b4f[3] + var7;
   }

   private static void p_cf_a_7ecb0198(byte[] var0, long[] var1, int var2) {
      int var3 = 0;

      for (byte var4 = 0; var4 < var2; var4 += 4) {
         var0[var4] = (byte)(var1[var3] & 255L);
         var0[var4 + 1] = (byte)(var1[var3] >>> 8 & 255L);
         var0[var4 + 2] = (byte)(var1[var3] >>> 16 & 255L);
         var0[var4 + 3] = (byte)(var1[var3] >>> 24 & 255L);
         var3++;
      }
   }

   private static long p_cf_a_132bdb(byte var0) {
      return var0 < 0 ? var0 & 255 : var0;
   }

   public static String m_a_73cf11cb() {
      Calendar var0;
      (var0 = Calendar.getInstance()).setTime(new Date());
      String var1 = "";
      switch (var0.get(7)) {
         case 1:
            var1 = "j";
            break;
         case 2:
            var1 = "d";
            break;
         case 3:
            var1 = "e";
            break;
         case 4:
            var1 = "f";
            break;
         case 5:
            var1 = "g";
            break;
         case 6:
            var1 = "h";
            break;
         case 7:
            var1 = "i";
      }

      return C_bt.m_a_e96ea081(var1);
   }

   private static boolean p_cf_a_2537830(char var0, boolean var1) {
      if (!var1) {
         return var0 <= ' ' || var0 == '"' ? false : (var0 & '\uff00') == 0;
      } else {
         return var0 >= 'A' && var0 <= 'Z' || var0 >= 'a' && var0 <= 'z' || var0 >= '0' && var0 <= '9' || var0 == '-';
      }
   }

   public static Vector m_a_dfd94fa3(String var0) {
      if (var0.indexOf(46) == -1) {
         return null;
      } else {
         Vector var1 = new Vector();
         int var2 = var0.length();
         int var3 = 0;

         int var6;
         while (var3 < var2 && (var6 = var0.indexOf(46, var3)) != -1) {
            int var4 = var6 - 1;

            while (var4 >= 0 && p_cf_a_2537830(var0.charAt(var4), true)) {
               var4--;
            }

            int var5 = var6 + 1;

            while (var5 < var2 && p_cf_a_2537830(var0.charAt(var5), false)) {
               var5++;
            }

            if (var4 == -1 || !p_cf_a_2537830(var0.charAt(var4), true)) {
               var4++;
            }

            var3 = var5;
            if (var6 != var4 && var5 - var6 >= 2) {
               var1.addElement("http://" + var0.substring(var4, var5));
            }
         }

         return var1.size() == 0 ? null : var1;
      }
   }

   public static int m_a_afa300d7(String var0, int var1) {
      if (var0 == null) {
         return 0;
      } else {
         int var3 = 0;

         try {
            var3 = Integer.parseInt(var0);
         } catch (Exception var2) {
         }

         return var3;
      }
   }

   public static String m_a_def8391f(String var0, String var1, String var2, boolean var3) {
      int var4 = var0.indexOf(var1);
      if (var3 && var4 == -1) {
         var4 = var0.indexOf(var1.toUpperCase());
      }

      return var4 == -1 ? var0 : var0.substring(0, var4) + var2 + var0.substring(var4 + var1.length(), var0.length());
   }

   public static byte[] m_a_529fb918(String var0, char var1, int var2) {
      String[] var6 = m_a_639c22ad(var0, var1);
      ByteArrayOutputStream var7 = new ByteArrayOutputStream();

      for (int var3 = 0; var3 < var6.length; var3++) {
         String var4;
         if ((var4 = var6[var3]).charAt(0) == '*') {
            for (int var5 = 1; var5 < var4.length(); var5++) {
               var7.write((byte)var4.charAt(var5));
            }
         } else {
            var7.write(Integer.parseInt(var4, var2));
         }
      }

      return var7.toByteArray();
   }

   public static String[] m_a_639c22ad(String var0, char var1) {
      Vector var2 = new Vector();
      StringBuffer var3 = new StringBuffer();
      int var4 = var0.length();

      for (int var5 = 0; var5 < var4; var5++) {
         char var6;
         if ((var6 = var0.charAt(var5)) == var1) {
            var2.addElement(var3.toString());
            var3.delete(0, var3.length());
         } else {
            var3.append(var6);
         }
      }

      var2.addElement(var3.toString());
      String[] var7 = new String[var2.size()];
      var2.copyInto(var7);
      return var7;
   }

   public static byte[] m_a_7ec6ec7a(byte[] var0, byte[] var1) {
      if (var1 == null) {
         return var0;
      } else if (var0 == null) {
         return var1;
      } else {
         byte[] var2 = new byte[var1.length << 3];

         for (byte var3 = 0; var3 < var1.length; var3 += 2) {
            System.arraycopy(f_cf_Z_b47, 0, var2, var3 << 3, f_cf_Z_b47.length);
            System.arraycopy(var1, var3, var2, (var3 << 3) + f_cf_Z_b47.length, 2);
            System.arraycopy(f_cf_aa_b47, 0, var2, (var3 << 3) + f_cf_Z_b47.length + 2, f_cf_aa_b47.length);
         }

         boolean var8 = false;

         for (byte var7 = 0; var7 < var0.length; var7 += 16) {
            byte[] var4 = new byte[16];
            System.arraycopy(var0, var7, var4, 0, 16);

            for (byte var5 = 0; var5 < var2.length; var5 += 16) {
               byte[] var6 = new byte[16];
               System.arraycopy(var2, var5, var6, 0, 16);
               if (var4 == var6) {
                  var8 = true;
                  break;
               }
            }

            if (!var8) {
               byte[] var9 = new byte[var2.length + 16];
               System.arraycopy(var2, 0, var9, 0, var2.length);
               System.arraycopy(var4, 0, var9, var2.length, var4.length);
               var2 = var9;
               var8 = false;
            }
         }

         return var2;
      }
   }

   public static String m_c_e96ea081(String var0) {
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;
      StringBuffer var4 = new StringBuffer(var0.length());

      while (var1 >= 0 && (var1 = var0.indexOf("&", var3)) >= 0) {
         var4.append(var0.substring(var2, var1));
         var2 = var1;
         if ((var3 = var0.indexOf(";", var1)) < 0) {
            break;
         }

         var2 = var3 + 1;
         String var5;
         if ((var5 = var0.substring(var1, var3 + 1)).equals("&lt;")) {
            var4.append("<");
         } else if (var5.equals("&gt;")) {
            var4.append(">");
         } else if (var5.equals("&amp;")) {
            var4.append("&");
         } else {
            var4.append(var5);
         }
      }

      if (var2 < var0.length()) {
         var4.append(var0.substring(var2));
      }

      return var4.toString();
   }

   public static String m_d_e96ea081(String var0) {
      StringBuffer var1 = new StringBuffer(var0.length());

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3;
         switch (var3 = var0.charAt(var2)) {
            case '&':
               var1.append("&amp;");
               break;
            case '<':
               var1.append("&lt;");
               break;
            case '>':
               var1.append("&gt;");
               break;
            default:
               var1.append(var3);
         }
      }

      return var1.toString();
   }

   public static Image m_a_622be521(Image var0, int var1, int var2) {
      int var3;
      int var4;
      label44: {
         var3 = var0.getWidth();
         var4 = var0.getHeight();
         if (var2 != 0 || var1 == 0) {
            if (var1 == 0 && var2 != 0) {
               var1 = var2 * var3 / var4;
               break label44;
            }

            if (var4 >= var3) {
               var1 = var2 * var3 / var4;
               break label44;
            }
         }

         var2 = var1 * var4 / var3;
      }

      Image var5;
      Graphics var6 = (var5 = Image.createImage(var1, var2)).getGraphics();

      for (int var7 = 0; var7 < var2; var7++) {
         for (int var8 = 0; var8 < var1; var8++) {
            var6.setClip(var8, var7, 1, 1);
            int var9 = var8 * var3 / var1;
            int var10 = var7 * var4 / var2;
            var6.drawImage(var0, var8 - var9, var7 - var10, 20);
         }
      }

      return Image.createImage(var5);
   }

   public static String m_e_e96ea081(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;
      int var3 = 0;

      for (int var4 = var0.length(); var3 < var4; var3++) {
         char var5;
         if ((var5 = var0.charAt(var3)) == '{') {
            var2++;
         } else if (var5 == '}') {
            var2--;
         }

         if (var2 == 1) {
            var3++;
            boolean var6 = false;
            StringBuffer var7 = new StringBuffer();

            while (var2 == 1) {
               if ((var5 = var0.charAt(var3++)) == '{') {
                  var2++;
                  break;
               }

               if (var5 == '}') {
                  var2--;
                  break;
               }

               if (var5 == '\\') {
                  if (var7.toString().equals("tab")) {
                     var1.append(' ');
                  }

                  var7.setLength(0);
                  if ((var5 = var0.charAt(var3)) == '\\') {
                     var1.append('\\');
                     var3++;
                     var6 = false;
                  } else if (var5 == '\'') {
                     var5 = (char)Integer.parseInt(var0.substring(var3 + 1, var3 + 3), 16);
                     var1.append((char)(var5 > 127 ? (var5 == 168 ? 1025 : (var5 == 184 ? 1105 : var5 + 848)) : var5));
                     var3 += 3;
                     var6 = false;
                  } else {
                     var6 = true;
                  }
               } else if ((var5 == ' ' || var5 == '\n') && var6) {
                  var6 = false;
                  if (var7.toString().equals("par")) {
                     var1.append('\n');
                  }

                  var7.setLength(0);
               } else if (!var6 && var5 >= ' ') {
                  var1.append(var5);
               } else {
                  var7.append(var5);
               }
            }
         }
      }

      return var1.toString();
   }

   static {
      char[] var10000 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   }
}
