package recovered;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public final class C_cf extends C_az {
   private int f_cf_a_49;
   private int f_cf_b_49;
   private C_aw f_cf_a_2406f9;
   private Date f_cf_a_711fe21;
   private int f_cf_c_49;
   private int f_cf_d_49 = 0;

   public C_cf(int var1, C_aw var2) {
      super(false, true);
      this.f_cf_b_49 = var1;
      this.f_cf_a_2406f9 = var2;
   }

   public final synchronized void m_a_9b75() {
      this.f_cf_c_49 = 0;
      switch (this.f_cf_b_49) {
         case 2:
            this.f_cf_c_49 = this.f_cf_a_2406f9.m_m_9b68();
            break;
         case 3:
            this.f_cf_c_49 = this.f_cf_a_2406f9.m_n_9b68();
            break;
         case 14:
            this.f_cf_c_49 = this.f_cf_a_2406f9.m_l_9b68();
      }

      if (this.f_cf_c_49 == 0) {
         this.f_cf_c_49 = C_ce.m_b_9b68();
         this.f_cf_a_49 = 0;
      } else {
         this.f_cf_a_49 = 1;
      }

      ByteArrayOutputStream var1;
      C_ce.m_a_e13aaa14(var1 = new ByteArrayOutputStream(), Integer.toString(this.f_cf_a_2406f9.m_j_9b68()), true);
      C_ce.m_a_559c4327(var1, 0, true);
      C_ce.m_a_559c4327(var1, this.f_cf_c_49, true);
      C_ce.m_a_559c4327(var1, this.f_cf_b_49, true);
      C_ce.m_a_559c4327(var1, 0, false);
      C_bu var2 = null;
      switch (this.f_cf_a_49) {
         case 0:
            var2 = new C_bu(19, 8, 0L, new byte[0], var1.toByteArray());
            break;
         case 1:
            var2 = new C_bu(19, 10, 0L, new byte[0], var1.toByteArray());
            this.f_cf_c_49 = 0;
      }

      C_ac.f_ac_a_240b17.m_a_cb4a1765(var2);
      this.f_cf_a_711fe21 = new Date();
   }

   public final synchronized boolean m_a_cb4a1769(C_cb var1) {
      if (var1 instanceof C_bu) {
         C_bu var2;
         if ((var2 = (C_bu)var1).m_b_9b68() == 19 && var2.m_c_9b68() == 14) {
            if (C_ce.m_a_e306d820(var2.m_b_12d408(), 0, false) == 0) {
               switch (this.f_cf_b_49) {
                  case 2:
                     this.f_cf_a_2406f9.m_e_13462e(this.f_cf_c_49);
                     break;
                  case 3:
                     this.f_cf_a_2406f9.m_f_13462e(this.f_cf_c_49);
                     break;
                  case 14:
                     this.f_cf_a_2406f9.m_d_13462e(this.f_cf_c_49);
               }

               C_w.m_a_db417b2e(this.f_cf_a_2406f9, false, false);
            }

            this.f_cf_d_49++;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final synchronized boolean m_a_9b79() {
      return this.f_cf_d_49 >= 1;
   }

   public final synchronized boolean m_b_9b79() {
      return this.f_cf_a_711fe21.getTime() + 3000L < System.currentTimeMillis();
   }

   public final int m_a_9b68() {
      return this.f_cf_d_49 * 100;
   }
}
