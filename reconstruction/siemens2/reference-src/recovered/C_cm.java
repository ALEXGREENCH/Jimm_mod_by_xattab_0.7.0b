package recovered;

import java.io.ByteArrayOutputStream;

public final class C_cm extends C_az {
   private C_aw f_cm_a_2406f9;
   private C_l f_cm_a_12a9b;
   private C_l f_cm_b_12a9b;
   private int f_cm_a_49;
   private int f_cm_b_49;
   private int f_cm_c_49;
   private long f_cm_a_4a = System.currentTimeMillis();

   public C_cm(C_an var1, int var2) {
      super(false, true);
      this.f_cm_b_49 = var2;
      if (var1 instanceof C_aw) {
         this.f_cm_a_2406f9 = (C_aw)var1;
         this.f_cm_a_12a9b = null;
      } else {
         this.f_cm_a_2406f9 = null;
         this.f_cm_a_12a9b = (C_l)var1;
      }
   }

   public C_cm(C_aw var1, C_l var2, C_l var3) {
      super(false, true);
      this.f_cm_a_2406f9 = var1;
      this.f_cm_a_12a9b = var2;
      this.f_cm_b_12a9b = var3;
      this.f_cm_b_49 = 4;
   }

   protected final void m_a_9b75() {
      Object var1 = null;
      if (this.f_cm_b_49 != 3) {
         C_ac.f_ac_a_240b17.m_a_cb4a1765(new C_bu(19, 17, C_ce.m_a_9b68(), new byte[0], new byte[0]));
      }

      switch (this.f_cm_b_49) {
         case 1:
         case 5:
            if (this.f_cm_a_2406f9 != null) {
               int var7 = this.f_cm_a_2406f9.m_b_134621(65);
               this.f_cm_a_12a9b = C_w.m_a_485a5a73(var7);
               this.f_cm_a_2406f9.m_a_255f295(64, C_ce.m_b_9b68());
               C_aw var2 = this.f_cm_a_2406f9;
               C_v.m_a_1ef468a(8, var2);
               var1 = this.p_cm_a_db390ee8(this.f_cm_a_2406f9, var7);
               if (this.f_cm_b_49 == 5) {
                  this.f_cm_a_49 = 17;
               } else {
                  this.f_cm_a_49 = 18;
               }
            } else {
               var1 = p_cm_a_cbb6f2dd(this.f_cm_a_12a9b);
               this.f_cm_a_49 = 11;
            }

            var1 = new C_bu(19, 8, C_ce.m_a_9b68(), new byte[0], (byte[])var1);
            C_ac.f_ac_a_240b17.m_a_cb4a1765((C_cb)var1);
            return;
         case 2:
            if (this.f_cm_a_2406f9 != null) {
               var1 = this.p_cm_a_db390ee8(this.f_cm_a_2406f9, 0);
               this.f_cm_a_49 = 7;
            } else {
               var1 = p_cm_a_cbb6f2dd(this.f_cm_a_12a9b);
               this.f_cm_a_49 = 9;
            }

            var1 = new C_bu(19, 10, C_ce.m_a_9b68(), new byte[0], (byte[])var1);
            C_ac.f_ac_a_240b17.m_a_cb4a1765((C_cb)var1);
            return;
         case 3:
            if (this.f_cm_a_2406f9 != null) {
               var1 = this.p_cm_a_db390ee8(this.f_cm_a_2406f9, 0);
            } else {
               var1 = p_cm_a_cbb6f2dd(this.f_cm_a_12a9b);
            }

            var1 = new C_bu(19, 9, C_ce.m_a_9b68(), new byte[0], (byte[])var1);
            C_ac.f_ac_a_240b17.m_a_cb4a1765((C_cb)var1);
            this.f_cm_a_49 = 1;
            return;
         case 4:
            C_ac.f_ac_a_240b17
               .m_a_cb4a1765(new C_bu(19, 9, C_ce.m_a_9b68(), new byte[0], this.p_cm_a_db390ee8(this.f_cm_a_2406f9, this.f_cm_a_12a9b.f_l_a_49)));
            this.f_cm_a_49 = 4;
      }
   }

   private static void p_cm_b_9b75() {
      C_ac.f_ac_a_240b17.m_a_cb4a1765(new C_bu(19, 18, C_ce.m_a_9b68(), new byte[0], new byte[0]));
   }

   private static void p_cm_c_9b75() {
      C_ac.f_ac_a_240b17.m_a_cb4a1765(new C_bu(19, 9, C_ce.m_a_9b68(), new byte[0], p_cm_a_12d408()));
   }

   private static void p_cm_a_48a2ce00(C_l var0) {
      try {
         C_ac.f_ac_a_240b17.m_a_cb4a1765(new C_bu(19, 9, C_ce.m_a_9b68(), new byte[0], p_cm_a_cbb6f2dd(var0)));
      } catch (Exception var1) {
      }
   }

   protected final synchronized boolean m_a_cb4a1769(C_cb var1) {
      boolean var10;
      label73: {
         boolean var3 = false;
         C_bu var2;
         if (var1 instanceof C_bu && (var2 = (C_bu)var1).m_b_9b68() == 19 && var2.m_c_9b68() == 14) {
            int var5;
            switch (var5 = C_ce.m_b_49634b7a(var2.m_b_12d408(), 0)) {
               case 2:
                  this.f_cm_c_49 = 154;
                  break;
               case 3:
                  this.f_cm_c_49 = 155;
               case 4:
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 11:
               default:
                  break;
               case 10:
                  this.f_cm_c_49 = 156;
                  break;
               case 12:
                  this.f_cm_c_49 = 157;
                  break;
               case 13:
                  this.f_cm_c_49 = 158;
            }

            if (this.f_cm_c_49 != 0) {
               this.f_cm_a_49 = -1;
               var10 = true;
               break label73;
            }

            label67: {
               switch (this.f_cm_a_49) {
                  case 1:
                     if (this.f_cm_b_49 != 3) {
                        p_cm_b_9b75();
                     }

                     this.f_cm_a_49 = 3;
                  case 2:
                  case 3:
                  case 13:
                  case 15:
                  case 16:
                  default:
                     break label67;
                  case 4:
                     C_bz var9 = C_ac.f_ac_a_240b17;
                     long var11 = C_ce.m_a_9b68();
                     byte[] var12 = new byte[0];
                     Object var8 = null;
                     var9.m_a_cb4a1765(new C_bu(19, 10, var11, var12, this.p_cm_a_db390ee8(this.f_cm_a_2406f9, this.f_cm_a_12a9b.f_l_a_49)));
                     this.f_cm_a_2406f9.m_a_255f295(65, this.f_cm_b_12a9b.f_l_a_49);
                     this.f_cm_a_2406f9.m_a_255f295(64, C_ce.m_b_9b68());
                     this.f_cm_a_49 = 5;
                     break label67;
                  case 5:
                     C_bz var10000 = C_ac.f_ac_a_240b17;
                     long var10005 = C_ce.m_a_9b68();
                     byte[] var10006 = new byte[0];
                     Object var7 = null;
                     var10000.m_a_cb4a1765(new C_bu(19, 8, var10005, var10006, this.p_cm_a_db390ee8(this.f_cm_a_2406f9, this.f_cm_b_12a9b.f_l_a_49)));
                     this.f_cm_a_49 = 6;
                     break label67;
                  case 6:
                     p_cm_a_48a2ce00(this.f_cm_b_12a9b);
                     this.f_cm_a_49 = 14;
                     break label67;
                  case 7:
                     C_l var6 = C_w.m_a_485a5a73(this.f_cm_a_2406f9.m_b_134621(65));
                     C_w.m_a_cb37742e(this.f_cm_a_2406f9);
                     p_cm_a_48a2ce00(var6);
                     this.f_cm_a_49 = 8;
                     break label67;
                  case 8:
                     break;
                  case 9:
                     p_cm_c_9b75();
                     this.f_cm_a_49 = 10;
                     break label67;
                  case 10:
                     C_w.m_b_48a2ce00(this.f_cm_a_12a9b);
                     this.f_cm_a_49 = 3;
                     p_cm_b_9b75();
                     break label67;
                  case 11:
                     p_cm_c_9b75();
                     this.f_cm_a_49 = 12;
                     break label67;
                  case 12:
                     C_w.m_a_48a2ce00(this.f_cm_a_12a9b);
                     break;
                  case 14:
                     p_cm_b_9b75();
                     this.f_cm_a_49 = 3;
                     break label67;
                  case 17:
                     p_cm_a_48a2ce00(this.f_cm_a_12a9b);
                     break;
                  case 18:
                     if (var5 == 0) {
                        p_cm_a_48a2ce00(this.f_cm_a_12a9b);
                        this.f_cm_a_2406f9.m_a_2563266(8, false);
                        this.f_cm_a_2406f9.m_a_2563266(2, false);
                     }

                     p_cm_b_9b75();
                     this.f_cm_a_49 = 3;
                     break label67;
               }

               p_cm_b_9b75();
               this.f_cm_a_49 = 3;
            }

            var3 = true;
            this.f_cm_a_4a = System.currentTimeMillis();
            if (this.f_cm_a_2406f9 != null && this.f_cm_b_49 == 2 && this.f_cm_a_2406f9.m_b_134621(65) == 0) {
               this.f_cm_a_49 = 3;
            }
         }

         var10 = var3;
      }

      boolean var4 = var10;
      if (var10 && this.f_cm_c_49 != 0) {
         if (this.f_cm_b_49 != 4 && this.f_cm_b_49 != 3) {
            p_cm_b_9b75();
         }

         this.f_cm_a_4a = System.currentTimeMillis();
      }

      return var4;
   }

   public final boolean m_a_9b79() {
      return this.f_cm_a_49 == 3;
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            switch (this.f_cm_b_49) {
               case 1:
               case 2:
               case 4:
                  C_w.m_a_1385ff(true);
               case 3:
               default:
                  C_w.m_a_9b75();
                  if (this.f_cm_b_49 != 2 && this.f_cm_a_2406f9 != null) {
                     C_w.m_a_db417b2e(this.f_cm_a_2406f9, true, false);
                     return;
                  }

                  return;
            }
         case 3:
            if (this.f_cm_c_49 != 0) {
               C_aq.m_a_481c933f(new C_aq(this.f_cm_c_49, 0, true));
               return;
            }

            C_aq.m_a_481c933f(new C_aq(154, 3, true));
      }
   }

   public final boolean m_b_9b79() {
      if (this.f_cm_a_49 == -1) {
         return true;
      } else {
         if (this.f_cm_a_4a + 10000L < System.currentTimeMillis() || this.f_cm_c_49 != 0) {
            this.f_cm_a_49 = -1;
         }

         return this.f_cm_a_49 == -1;
      }
   }

   private byte[] p_cm_a_db390ee8(C_aw var1, int var2) {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      if (var2 == 0) {
         var2 = var1.m_b_134621(65);
      }

      C_ce.m_a_e13aaa14(var3, var1.m_a_47921032(0), true);
      C_ce.m_a_559c4327(var3, var2, true);
      C_ce.m_a_559c4327(var3, var1.m_b_134621(64), true);
      C_ce.m_a_559c4327(var3, 0, true);
      ByteArrayOutputStream var5 = new ByteArrayOutputStream();
      if (this.f_cm_b_49 != 2) {
         C_ce.m_a_559c4327(var5, 305, true);
         C_ce.m_a_e13aaa14(var5, var1.m_a_47921032(1), true);
      }

      byte[] var4;
      if ((var4 = var1.m_a_255806f(227)) != null) {
         C_ce.m_a_55a417bd(var5, var4);
      }

      if (this.f_cm_b_49 == 5) {
         C_ce.m_a_559c4327(var5, 102, true);
         C_ce.m_a_559c4327(var5, 0, true);
      }

      C_ce.m_a_559c4327(var3, var5.size(), true);
      var3.write(var5.toByteArray(), 0, var5.size());
      return var3.toByteArray();
   }

   private static byte[] p_cm_a_cbb6f2dd(C_l var0) {
      ByteArrayOutputStream var1;
      C_ce.m_a_e13aaa14(var1 = new ByteArrayOutputStream(), var0.m_b_73cf11cb(), true);
      Object var2 = null;
      C_ce.m_a_559c4327(var1, var0.f_l_a_49, true);
      C_ce.m_a_559c4327(var1, 0, true);
      C_ce.m_a_559c4327(var1, 1, true);
      C_aw[] var3;
      if ((var3 = C_w.m_a_7ef64a7e(var0)).length != 0) {
         C_ce.m_a_559c4327(var1, (var3.length << 1) + 4, true);
         C_ce.m_a_559c4327(var1, 200, true);
         C_ce.m_a_559c4327(var1, var3.length << 1, true);

         for (int var4 = 0; var4 < var3.length; var4++) {
            C_ce.m_a_559c4327(var1, var3[var4].m_b_134621(64), true);
         }
      } else {
         C_ce.m_a_559c4327(var1, 0, true);
      }

      return var1.toByteArray();
   }

   private static byte[] p_cm_a_12d408() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      C_l[] var1 = C_w.m_a_46ae24e1();
      C_ce.m_a_e13aaa14(var0, "", true);
      C_ce.m_a_559c4327(var0, 0, true);
      C_ce.m_a_559c4327(var0, 0, true);
      C_ce.m_a_559c4327(var0, 1, true);
      C_ce.m_a_559c4327(var0, (var1.length << 1) + 4, true);
      C_ce.m_a_559c4327(var0, 200, true);
      C_ce.m_a_559c4327(var0, var1.length << 1, true);

      for (int var2 = 0; var2 < var1.length; var2++) {
         C_ce.m_a_559c4327(var0, var1[var2].f_l_a_49, true);
      }

      return var0.toByteArray();
   }
}
