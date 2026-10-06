package recovered;

/** 0.6 source correspondence (inferred): jimm.ContactItem. Release class: aw. */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.TimerTask;
import jimm.Jimm;

public final class C_aw implements C_ao {
   private int f_aw_c_49;
   private int f_aw_d_49;
   private int f_aw_e_49;
   private int f_aw_f_49;
   public int f_aw_a_49;
   private int f_aw_g_49;
   private int f_aw_h_49;
   private int f_aw_i_49;
   private int f_aw_j_49;
   private int f_aw_k_49;
   private int f_aw_l_49;
   private int f_aw_m_49;
   private int f_aw_n_49;
   private int f_aw_o_49;
   private int f_aw_p_49;
   private int f_aw_q_49;
   private String f_aw_d_523beb0a;
   private String f_aw_e_523beb0a;
   private String f_aw_f_523beb0a;
   public String f_aw_a_523beb0a;
   public String f_aw_b_523beb0a;
   private byte[] f_aw_a_b47;
   public boolean f_aw_a_5a;
   public boolean f_aw_b_5a;
   public boolean f_aw_c_5a;
   public boolean f_aw_d_5a;
   public long f_aw_a_4a = 0L;
   public long f_aw_b_4a = 0L;
   private C_bg f_aw_a_2408ca;
   public static String f_aw_c_523beb0a = new String();
   private boolean f_aw_e_5a = false;
   private boolean f_aw_f_5a;
   private boolean f_aw_g_5a;
   private boolean f_aw_h_5a;
   private boolean f_aw_i_5a;
   private int f_aw_r_49;
   private int f_aw_s_49;
   private TimerTask f_aw_a_d50713bd;
   private C_x f_aw_a_12c0f = new C_x();
   public int f_aw_b_49;
   private volatile int f_aw_t_49;
   private volatile int f_aw_u_49;

   public final synchronized void m_a_4f708078(int var1, String var2) {
      switch (var1) {
         case 0:
            this.f_aw_l_49 = Integer.parseInt(var2);
            return;
         case 1:
            this.f_aw_a_523beb0a = var2;
            this.f_aw_f_523beb0a = null;
            return;
         case 2:
            this.f_aw_d_523beb0a = var2;
            return;
         case 3:
            this.f_aw_e_523beb0a = var2;
            return;
      }
   }

   public final synchronized String m_a_47921032(int var1) {
      switch (var1) {
         case 0:
            return Integer.toString(this.f_aw_l_49);
         case 1:
            return this.f_aw_a_523beb0a;
         case 2:
            return this.f_aw_d_523beb0a;
         case 3:
            return this.f_aw_e_523beb0a;
         default:
            return null;
      }
   }

   public final String m_c_73cf11cb() {
      if (this.f_aw_f_523beb0a == null) {
         this.f_aw_f_523beb0a = this.f_aw_a_523beb0a.toLowerCase();
         if (this.f_aw_f_523beb0a.equals(this.f_aw_a_523beb0a)) {
            this.f_aw_f_523beb0a = this.f_aw_a_523beb0a;
         }
      }

      return this.f_aw_f_523beb0a;
   }

   public final int m_a_134621(int var1) {
      int var2 = this.m_b_134621(192);
      if (this.m_c_134632(1)) {
         return 1;
      } else {
         switch (var1) {
            case 1:
               if (var2 == 32) {
                  return 2;
               }

               if (var2 == 0 || var2 == 20480 || var2 == 24576 || var2 == 256) {
                  return 3;
               }

               if (var2 == 12288) {
                  return 4;
               }

               if (var2 == 16384) {
                  return 5;
               }

               if (var2 == 16) {
                  return 6;
               }

               if (var2 == 2) {
                  return 7;
               }

               if (var2 == 8193) {
                  return 8;
               }

               if (var2 == 1) {
                  return 9;
               }

               if (var2 == 4) {
                  return 10;
               }
               break;
            case 2:
               if (var2 != -1) {
                  return 5;
               }
         }

         return (this.m_a_134632(8) || this.m_a_134632(32)) && var2 == -1 ? 20 : 15;
      }
   }

   public final synchronized void m_a_255f295(int var1, int var2) {
      switch (var1) {
         case 64:
            this.f_aw_c_49 = this.f_aw_c_49 & 65535 | var2 << 16;
            return;
         case 65:
            this.f_aw_c_49 = this.f_aw_c_49 & -65536 | var2;
            return;
         case 67:
            this.f_aw_f_49 = this.f_aw_f_49 & 16777215 | var2 << 24;
            return;
         case 68:
            this.f_aw_f_49 = this.f_aw_f_49 & -16711681 | var2 << 16;
            return;
         case 69:
            this.f_aw_f_49 = this.f_aw_f_49 & -65281 | var2 << 8;
            return;
         case 70:
            this.f_aw_f_49 = this.f_aw_f_49 & -256 | var2;
            return;
         case 71:
            this.f_aw_d_49 = var2;
            return;
         case 72:
            this.f_aw_g_49 = this.f_aw_g_49 & 0xFF | (var2 & 0xFF) << 8;
            return;
         case 73:
            this.f_aw_h_49 = this.f_aw_h_49 & -65536 | var2 & 65535;
            return;
         case 74:
            this.f_aw_h_49 = this.f_aw_h_49 & 65535 | (var2 & 65535) << 16;
            return;
         case 75:
            this.f_aw_a_49 = var2;
            return;
         case 76:
            this.f_aw_g_49 = this.f_aw_g_49 & 0xFF00 | var2 & 0xFF;
            return;
         case 191:
            this.f_aw_n_49 = var2;
            return;
         case 192:
            this.f_aw_p_49 = var2;
            return;
         case 193:
            this.f_aw_k_49 = var2;
            return;
         case 194:
            this.f_aw_o_49 = var2;
            return;
         case 195:
            this.f_aw_m_49 = var2;
            return;
         case 196:
            this.f_aw_q_49 = var2;
            return;
      }
   }

   public final synchronized int m_b_134621(int var1) {
      switch (var1) {
         case 64:
            return this.f_aw_c_49 >> 16 & 65535;
         case 65:
            return this.f_aw_c_49 & 65535;
         case 67:
            return this.f_aw_f_49 >>> 24;
         case 68:
            return this.f_aw_f_49 >> 16 & 0xFF & 0xFF;
         case 69:
            return this.f_aw_f_49 >> 8 & 0xFF & 0xFF;
         case 70:
            return this.f_aw_f_49 & 0xFF;
         case 71:
            return this.f_aw_d_49;
         case 72:
            return this.f_aw_g_49 >> 8 & 0xFF & 0xFF;
         case 73:
            return this.f_aw_h_49 & 65535;
         case 74:
            return this.f_aw_h_49 >> 16 & 65535;
         case 75:
            return this.f_aw_a_49;
         case 76:
            return this.f_aw_g_49 & 0xFF;
         case 191:
            return this.f_aw_n_49;
         case 192:
            return this.f_aw_p_49;
         case 193:
            return this.f_aw_k_49;
         case 194:
            return this.f_aw_o_49;
         case 195:
            return this.f_aw_m_49;
         case 196:
            return this.f_aw_q_49;
         default:
            return 0;
      }
   }

   public final synchronized void m_a_2563266(int var1, boolean var2) {
      this.f_aw_e_49 = this.f_aw_e_49 & ~var1 | (var2 ? var1 : 0);
   }

   public final synchronized boolean m_a_134632(int var1) {
      return (this.f_aw_e_49 & var1) != 0;
   }

   private static byte[] p_aw_b_255806f(int var0) {
      return var0 == 0 ? null : new byte[]{(byte)var0, (byte)(var0 >> 8), (byte)(var0 >> 16), (byte)(var0 >> 24)};
   }

   private static int p_aw_a_25e06ef(byte[] var0) {
      return var0 != null && var0.length >= 4 ? var0[0] & 0xFF | (var0[1] & 0xFF) << 8 | (var0[2] & 0xFF) << 16 | (var0[3] & 0xFF) << 24 : 0;
   }

   public final synchronized void m_a_4870e775(int var1, byte[] var2) {
      switch (var1) {
         case 225:
            this.f_aw_i_49 = p_aw_a_25e06ef(var2);
            return;
         case 226:
            this.f_aw_j_49 = p_aw_a_25e06ef(var2);
            return;
         case 227:
            this.f_aw_a_b47 = var2;
      }
   }

   public final synchronized byte[] m_a_255806f(int var1) {
      switch (var1) {
         case 225:
            return p_aw_b_255806f(this.f_aw_i_49);
         case 226:
            return p_aw_b_255806f(this.f_aw_j_49);
         case 227:
            return this.f_aw_a_b47;
         default:
            return null;
      }
   }

   public final void m_a_3faa729d(DataOutputStream var1) throws java.io.IOException {
      var1.writeByte(0);
      var1.writeInt(this.f_aw_c_49);
      var1.writeByte(this.f_aw_e_49 & 10);
      var1.writeInt(this.f_aw_l_49);
      var1.writeUTF(this.f_aw_a_523beb0a);
      var1.writeInt(this.m_m_9b68());
      var1.writeInt(this.f_aw_u_49 & 65535);
      var1.writeInt(this.f_aw_t_49);
      if (this.f_aw_a_b47 != null) {
         var1.writeShort(this.f_aw_a_b47.length);
         var1.write(this.f_aw_a_b47);
      } else {
         var1.writeShort(0);
      }
   }

   public final void m_a_414f9488(DataInputStream var1) throws java.io.IOException {
      this.f_aw_c_49 = var1.readInt();
      this.f_aw_e_49 = var1.readByte();
      this.f_aw_l_49 = var1.readInt();
      this.f_aw_a_523beb0a = var1.readUTF();
      this.m_e_13462e(var1.readInt());
      this.m_f_13462e(var1.readInt());
      int var3 = var1.readInt();
      Object var2 = null;
      this.f_aw_t_49 = var3;
      short var4;
      if ((var4 = var1.readShort()) != 0) {
         this.f_aw_a_b47 = new byte[var4];
         var1.read(this.f_aw_a_b47);
      } else {
         this.f_aw_a_b47 = null;
      }
   }

   public C_aw(int var1, int var2, String var3, String var4, boolean var5, boolean var6) {
      if (var1 == -1) {
         this.m_a_255f295(64, C_cf.m_b_9b68());
      } else {
         this.m_a_255f295(64, var1);
      }

      this.m_a_255f295(65, var2);
      this.m_a_4f708078(0, var3);
      this.m_a_4f708078(1, var4);
      this.m_a_2563266(2, var5);
      this.m_a_2563266(8, false);
      this.m_a_2563266(16, false);
      this.m_a_2563266(32, false);
      this.m_a_2563266(1, var6);
      this.m_a_255f295(192, -1);
      this.m_a_255f295(75, 0);
      this.m_a_255f295(67, 0);
      this.m_a_255f295(68, 0);
      this.m_a_255f295(69, 0);
      this.m_a_255f295(70, 0);
      this.m_a_4870e775(225, new byte[4]);
      this.m_a_4870e775(226, new byte[4]);
      this.m_a_255f295(74, 0);
      this.m_a_255f295(72, 0);
      this.m_a_255f295(73, 0);
      this.m_a_255f295(193, 0);
      this.m_a_255f295(194, -1);
      this.m_a_255f295(191, -1);
      this.f_aw_m_49 = -1;
      this.m_a_255f295(71, -1);
      this.m_a_255f295(76, 0);
      this.m_a_4f708078(2, "");
      this.m_a_4f708078(3, "");
      this.m_a_255f295(196, C_cl.m_a_aad3b1f2(var3));
   }

   public C_aw() {
   }

   public final synchronized boolean m_b_134632(int var1) {
      return (var1 & this.f_aw_a_49) != 0;
   }

   public final void a$13462e() {
      this.f_aw_a_49 |= 1024;
   }

   public final int m_b_9b68() {
      if ((!C_bq.m_a_134632(157) || !this.f_aw_f_5a) && !this.f_aw_g_5a) {
         if (this.m_a_134632(32)) {
            return 16711680;
         } else if (this.m_a_134632(8)) {
            return C_bq.m_a_134621(105);
         } else {
            return this.m_a_134632(16) ? C_bq.m_a_134621(104) : C_bq.m_a_134621(103);
         }
      } else {
         return C_bq.f_bq_b_49;
      }
   }

   public final int m_i_9b68() {
      return !this.m_a_134632(16) && !this.f_aw_g_5a && (!C_bq.m_a_134632(157) || !this.f_aw_f_5a) ? C_bq.m_a_134621(112) : 1 + C_bq.f_bq_g_49;
   }

   public final int m_j_9b68() {
      return this.f_aw_l_49;
   }

   public final String m_b_73cf11cb() {
      return Integer.toString(this.f_aw_l_49);
   }

   public final int m_a_9b68() {
      int var1 = 0;
      if (this.f_aw_e_5a) {
         return 18;
      } else if (this.f_aw_f_5a && C_bq.m_a_134632(156)) {
         return 19;
      } else {
         if (this.m_c_134632(1)) {
            var1 = 14;
         } else if (this.m_c_134632(2)) {
            var1 = 15;
         } else if (this.m_c_134632(4)) {
            var1 = 16;
         } else if (this.m_c_134632(3)) {
            var1 = 17;
         } else {
            var1 = C_bi.m_a_1349e2(this.m_b_134621(192));
         }

         return var1;
      }
   }

   public final String m_a_73cf11cb() {
      return this.f_aw_a_523beb0a;
   }

   public final void m_a_cb3e45bf(C_bg var1) {
      this.f_aw_a_2408ca = var1;
   }

   public final C_bg m_a_46a7a54b() {
      return this.f_aw_a_2408ca;
   }

   protected final boolean m_a_9b79() {
      return C_bq.m_a_134632(134)
         ? this.m_b_134621(67) > 0
            || this.m_b_134621(68) > 0
            || this.m_b_134621(69) > 0
            || this.m_b_134621(70) > 0
            || this.m_a_134632(8)
            || this.m_a_134632(32)
            || this.f_aw_h_5a
         : this.m_a_134632(16) || this.m_a_134632(8) || this.m_a_134632(32) || this.f_aw_h_5a;
   }

   protected final int m_k_9b68() {
      return this.m_b_134621(67) + this.m_b_134621(68) + this.m_b_134621(69) + this.m_b_134621(70);
   }

   protected final synchronized boolean m_c_134632(int var1) {
      switch (var1) {
         case 1:
            if (this.m_b_134621(67) > 0) {
               return true;
            }

            return false;
         case 2:
            if (this.m_b_134621(68) > 0) {
               return true;
            }

            return false;
         case 3:
            if (this.m_b_134621(69) > 0) {
               return true;
            }

            return false;
         case 4:
            if (this.m_b_134621(70) > 0) {
               return true;
            }

            return false;
         default:
            return this.m_b_134621(67) > 0;
      }
   }

   protected final synchronized void m_b_13462e(int var1) {
      switch (var1) {
         case 1:
            this.m_a_255f295(67, this.m_b_134621(67) + 1);
            return;
         case 2:
            this.m_a_255f295(68, this.m_b_134621(68) + 1);
            return;
         case 3:
            this.m_a_255f295(69, this.m_b_134621(69) + 1);
            return;
         case 4:
            this.m_a_255f295(70, this.m_b_134621(70) + 1);
      }
   }

   public final synchronized void m_a_9b75() {
      this.m_a_255f295(67, 0);
      this.m_a_255f295(68, 0);
      this.m_a_255f295(69, 0);
   }

   public final boolean equals(Object var1) {
      if (!(var1 instanceof C_aw)) {
         return false;
      } else {
         var1 = var1;
         return this.m_a_47921032(0).equals(((C_aw)var1).m_a_47921032(0)) && this.m_a_134632(8) == ((C_aw)var1).m_a_134632(8);
      }
   }

   public final void m_b_9b75() {
      C_aa.m_a_e925fa09(this.m_a_47921032(0), this.f_aw_a_523beb0a);
   }

   public final void m_a_1385ff(boolean var1) {
      this.f_aw_e_5a = var1;
      this.m_f_9b75();
   }

   public final void m_c_9b75() {
      this.f_aw_r_49 = 0;
      this.f_aw_f_5a = false;
      this.f_aw_g_5a = false;
      this.f_aw_h_5a = true;
   }

   public final void m_b_1385ff(boolean var1) {
      this.f_aw_i_5a = var1;
      if (this.f_aw_i_5a) {
         if (!C_bq.m_a_134632(157) && !C_bq.m_a_134632(156)) {
            return;
         }

         this.f_aw_g_5a = false;
         this.f_aw_s_49 = (C_bq.m_a_134621(93) << 1) + 1;
      } else {
         if (!C_bq.m_a_134632(184)) {
            this.f_aw_h_5a = false;
            return;
         }

         this.f_aw_f_5a = false;
         this.f_aw_s_49 = (C_bq.m_a_134621(89) << 1) + 1;
      }

      if (this.f_aw_a_d50713bd != null) {
         this.f_aw_a_d50713bd.cancel();
      }

      this.f_aw_a_d50713bd = new C_bn(this);
      Jimm.m_a_94e56161().schedule(this.f_aw_a_d50713bd, 500L, 500L);
   }

   public final void m_d_9b75() {
      this.f_aw_e_5a = false;
      this.m_a_255f295(192, -1);
   }

   public final synchronized int m_d_9b68() {
      return this.m_b_134621(196) != -1 ? 0 : -1;
   }

   public final synchronized int m_e_9b68() {
      return (this.f_aw_b_49 & 8) != 0 ? 0 : -1;
   }

   public final synchronized int m_f_9b68() {
      return this.m_a_134632(2) ? 0 : -1;
   }

   public final synchronized int m_c_9b68() {
      return this.m_b_134621(76) - 1;
   }

   public final synchronized int m_g_9b68() {
      if (this.m_m_9b68() != 0) {
         return 1;
      } else {
         return (this.f_aw_u_49 & 65535) != 0 ? 0 : -1;
      }
   }

   public final synchronized int m_h_9b68() {
      return this.f_aw_t_49 == 0 ? -1 : 2;
   }

   public final void m_a_aad3b1ff(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.f_aw_a_523beb0a = var1;
         this.f_aw_f_523beb0a = null;

         try {
            C_w.m_b_9b75();
            if (!this.m_a_134632(8)) {
               C_ac.m_a_cb3b8b85(new C_cn(this, 3));
            }
         } catch (C_ar var2) {
            if (var2.f_ar_a_5a) {
               return;
            }
         } catch (Exception var3) {
         }

         C_w.m_a_db417b2e(this, true, true);
         C_ap.m_b_e925fa09(this.m_a_47921032(0), this.f_aw_a_523beb0a);
      }
   }

   public final void m_e_9b75() {
      C_av var1 = null;
      f_aw_c_523beb0a = Integer.toString(this.f_aw_l_49);
      if (!C_w.f_w_a_5a) {
         C_ap.m_a_cb37742e(this);
      }

      if (this.m_a_134632(16) && !C_w.f_w_a_5a) {
         var1 = C_ap.m_a_51338891(f_aw_c_523beb0a);
         C_ap.m_b_aad3b1ff(f_aw_c_523beb0a);
         var1.m_a_cb37742e(this);
         var1.m_e_9b75() ;
         if (C_bq.m_a_134632(167) && this.f_aw_a_5a && this.m_b_134621(76) != 16) {
            try {
               C_l.m_a_afa300e4(f_aw_c_523beb0a, 0);
            } catch (Exception var2) {
            }

            this.f_aw_a_5a = false;
         }

         this.m_f_9b75();
      } else {
         C_bi.m_a_cb37742e(this);
      }
   }

   public final void m_f_9b75() {
      int var1 = this.f_aw_e_5a ? 18 : C_bi.m_a_1349e2(this.m_b_134621(192));
      if (C_cp.m_a_9b79()) {
         C_cp.m_a_13462e(var1);
         C_cp.m_a_48a013c6(this.m_a_2477b6e().m_a_2477940());
         C_cp.m_a_aad3b1ff(this.m_a_47921032(1)) ;
         C_cp.m_b_9b75();
         C_cp.m_f_9b75();
      } else {
         C_av var2;
         if ((var2 = C_ap.m_a_51338891(this.m_a_47921032(0))) != null) {
            var2.m_a_48a013c6(C_w.f_w_a_12a00.m_a_485a59b9(var1));
            var2.m_b_48a013c6(this.m_a_2477b6e().m_a_2477940());
            var2.m_c_48a013c6(C_w.f_w_d_12a00.m_a_485a59b9(this.m_e_9b68()));
         }
      }
   }

   public final synchronized void m_a_25e06fc(byte[] var1) {
      this.m_a_2477b6e().m_a_25e06fc(var1);
   }

   public final synchronized C_x m_a_2477b6e() {
      return this.f_aw_a_12c0f;
   }

   public final synchronized void m_c_13462e(int var1) {
      this.f_aw_b_49 = var1;
   }

   public final int m_l_9b68() {
      return this.f_aw_t_49;
   }

   public final void m_d_13462e(int var1) {
      this.f_aw_t_49 = var1;
   }

   public final int m_m_9b68() {
      return this.f_aw_u_49 >> 16 & 65535;
   }

   public final void m_e_13462e(int var1) {
      this.f_aw_u_49 = this.f_aw_u_49 & 65535 | var1 << 16;
   }

   public final int m_n_9b68() {
      return this.f_aw_u_49 & 65535;
   }

   public final void m_f_13462e(int var1) {
      this.f_aw_u_49 = this.f_aw_u_49 & -65536 | var1;
   }

   static int m_a_cb377421(C_aw var0) {
      return var0.f_aw_r_49++;
   }

   static boolean m_a_cb377432(C_aw var0) {
      return var0.f_aw_i_5a;
   }

   static boolean m_a_9bb7c46a(C_aw var0, boolean var1) {
      return var0.f_aw_f_5a = var1;
   }

   static boolean m_b_cb377432(C_aw var0) {
      return var0.f_aw_f_5a;
   }

   static boolean m_b_9bb7c46a(C_aw var0, boolean var1) {
      return var0.f_aw_g_5a = var1;
   }

   static boolean m_c_cb377432(C_aw var0) {
      return var0.f_aw_g_5a;
   }

   static int m_b_cb377421(C_aw var0) {
      return var0.f_aw_r_49;
   }

   static int m_c_cb377421(C_aw var0) {
      return var0.f_aw_s_49;
   }

   static TimerTask m_a_36162b95(C_aw var0) {
      return var0.f_aw_a_d50713bd;
   }

   static void m_a_cb37742e(C_aw var0) {
      var0.f_aw_h_5a = false;
      C_w.m_a_db417b2e(var0, false, true);
   }
}
