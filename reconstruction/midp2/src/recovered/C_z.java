package recovered;

/** 0.6 source correspondence (inferred): jimm.comm.SearchAction. Release class: z. */

import java.io.ByteArrayOutputStream;

public final class C_z extends C_ba {
   private int f_z_a_49;
   private String[] f_z_a_6dccaaa5;
   private C_cg f_z_a_240c8b;
   private long f_z_a_4a = System.currentTimeMillis();
   private final int[] f_z_a_b4e = new int[]{0, 99, 13, 17, 18, 22, 23, 29, 30, 39, 40, 49, 50, 59, 60, 99};

   public C_z(C_cg var1, String[] var2) {
      super(false, true);
      this.f_z_a_6dccaaa5 = var2;
      this.f_z_a_240c8b = var1;
   }

   protected final void m_a_9b75() throws recovered.C_ar {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      for (int var2 = this.f_z_a_6dccaaa5.length - 1; var2 >= 0; var2--) {
         if (this.f_z_a_6dccaaa5[var2] == null) {
            this.f_z_a_6dccaaa5[var2] = C_bq.f_bq_a_523beb0a;
         }
      }

      C_cf.m_a_559c4327(var1, 24325, true);
      if (this.f_z_a_6dccaaa5[0].length() != 0) {
         C_cf.m_a_559c4327(var1, 13825, true);
         C_cf.m_a_559c4327(var1, 4, false);
         C_cf.b$559c4327(var1, Integer.parseInt(this.f_z_a_6dccaaa5[0]));
      }

      if (this.f_z_a_6dccaaa5[1].length() != 0) {
         C_cf.m_a_1a21c327(21505, var1, this.f_z_a_6dccaaa5[1]);
      }

      if (this.f_z_a_6dccaaa5[2].length() != 0) {
         C_cf.m_a_1a21c327(16385, var1, this.f_z_a_6dccaaa5[2]);
      }

      if (this.f_z_a_6dccaaa5[3].length() != 0) {
         C_cf.m_a_1a21c327(18945, var1, this.f_z_a_6dccaaa5[3]);
      }

      if (this.f_z_a_6dccaaa5[4].length() != 0) {
         C_cf.m_a_1a21c327(24065, var1, this.f_z_a_6dccaaa5[4]);
      }

      if (this.f_z_a_6dccaaa5[5].length() != 0) {
         C_cf.m_a_1a21c327(36865, var1, this.f_z_a_6dccaaa5[5]);
      }

      if (this.f_z_a_6dccaaa5[6].length() != 0) {
         C_cf.m_a_1a21c327(9730, var1, this.f_z_a_6dccaaa5[6]);
      }

      int var4;
      if ((var4 = C_cf.m_a_afa300d7(this.f_z_a_6dccaaa5[9], 0)) != 0) {
         C_cf.m_a_559c4327(var1, 26625, true);
         C_cf.m_a_559c4327(var1, 4, false);
         C_cf.m_a_559c4327(var1, this.f_z_a_b4e[var4 << 1], false);
         C_cf.m_a_559c4327(var1, this.f_z_a_b4e[(var4 << 1) + 1], false);
      }

      if ((var4 = C_cf.m_a_afa300d7(this.f_z_a_6dccaaa5[7], 0)) != 0) {
         C_cf.m_a_559c4327(var1, 31745, true);
         C_cf.m_a_559c4327(var1, 1, false);
         C_cf.m_a_5557994d(var1, var4);
      }

      C_cf.m_a_559c4327(var1, 12290, true);
      C_cf.m_a_559c4327(var1, 1, false);
      C_cf.m_a_5557994d(var1, this.f_z_a_6dccaaa5[8].equals("1") ? 1 : 0);
      C_as var3 = new C_as(2L, 2, C_bq.m_a_47921032(254), new byte[0], var1.toByteArray());
      C_ac.f_ac_a_240bf0.m_a_cb4b0023(var3);
      this.f_z_a_49 = 1;
   }

   protected final synchronized boolean m_a_cb4b0027(C_cd var1) {
      if ((this.f_z_a_49 == 1 || this.f_z_a_49 == 4) && var1 instanceof C_bs) {
         C_bs var11 = (C_bs)var1;
         int var2 = 0;
         byte[] var12;
         if (C_cf.m_b_49634b7a(var12 = var11.m_b_12d408(), 0) == 41985) {
            this.f_z_a_49 = 4;
         }

         if (C_cf.m_b_49634b7a(var12, 0) == 44545) {
            this.f_z_a_49 = 5;
         }

         int var15 = var2 + 2;
         if (C_cf.m_a_49634b7a(var12, 2) == 10) {
            var2 = var15 + 3;
            long var5 = C_cf.m_a_e306d821(var12, 5, false);
            int var17 = var2 + 4;
            String[] var3 = new String[4];

            for (int var4 = 0; var4 < 4; var4++) {
               var3[var4] = C_cf.m_a_55a39fc4(var12, var17 + 2, C_cf.m_a_e306d820(var12, var17, false));
               var17 += 2 + C_cf.m_a_e306d820(var12, var17, false);
            }

            String var23 = C_cf.m_a_49634b7a(var12, var17) == 0 ? "1" : "0";
            int var7 = C_cf.m_a_e306d820(var12, ++var17, false);
            var2 = var17 + 2;
            String var8 = C_cf.m_c_47921032(C_cf.m_a_49634b7a(var12, var2));
            int var13 = C_cf.m_a_e306d820(var12, ++var2, false);
            C_cg var10000 = this.f_z_a_240c8b;
            String var10001 = String.valueOf(var5);
            String var10002 = var3[0];
            String var10003 = var3[1] + " " + var3[2];
            String var25 = var3[3];
            String var24 = var10003;
            String var22 = var10002;
            String var21 = var10001;
            C_cg var14 = var10000;
            String[] var10;
            (var10 = new String[48])[37] = var21;
            var10[1] = var22;
            var10[2] = var24;
            var10[3] = var25;
            var10[24] = var23;
            var10[25] = Integer.toString(var7);
            var10[11] = var8;
            var10[10] = Integer.toString(var13);
            var14.f_cg_a_48a69a2c.addElement(var10);
            if (this.f_z_a_49 == 5) {
               this.f_z_a_49 = 6;
            }
         } else {
            this.f_z_a_49 = -1;
         }
      }

      this.f_z_a_4a = System.currentTimeMillis();
      return false;
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            this.f_z_a_240c8b.m_a_46a7a988().m_a_13462e(1);
            return;
         case 2:
            this.f_z_a_240c8b.m_a_46a7a988().m_a_13462e(2);
            this.f_z_a_49 = 7;
         default:
            return;
         case 3:
            if (this.f_z_a_49 == 4) {
               C_ar.m_a_aef55300(new C_ar(159, 0, true));
            } else {
               this.f_z_a_240c8b.m_a_46a7a988().m_a_13462e(3);
            }
      }
   }

   public final boolean m_a_9b79() {
      return this.f_z_a_49 == 6 || this.f_z_a_49 == 7;
   }

   public final synchronized boolean m_b_9b79() {
      return this.f_z_a_49 == -1 || this.f_z_a_4a + 60000L < System.currentTimeMillis();
   }
}
