package recovered;

/** 0.6 source correspondence (inferred): jimm.GroupItem. Release class: m. */

public final class C_m implements C_ao {
   public int f_m_a_49;
   String f_m_a_523beb0a;
   private int f_m_b_49;
   private int f_m_c_49;
   private C_x f_m_a_12c0f = new C_x();

   public C_m(int var1, String var2) {
      this.f_m_a_49 = var1;
      this.f_m_a_523beb0a = new String(var2);
      this.f_m_b_49 = this.f_m_c_49 = 0;
   }

   public C_m() {
   }

   public C_m(String var1) {
      this.f_m_a_49 = C_cf.m_b_9b68();
      this.f_m_a_523beb0a = new String(var1);
      this.f_m_b_49 = this.f_m_c_49 = 0;
   }

   public final void m_a_255f295(int var1, int var2) {
      this.f_m_b_49 = var1;
      this.f_m_c_49 = var2;
   }

   public final void m_b_255f295(int var1, int var2) {
      this.f_m_b_49 += var1;
      this.f_m_c_49 += var2;
   }

   public final int m_a_9b68() {
      return 22;
   }

   public final synchronized C_x m_a_2477b6e() {
      return this.f_m_a_12c0f;
   }

   public final String m_a_73cf11cb() {
      return this.f_m_a_523beb0a + " (" + Integer.toString(this.f_m_b_49) + "/" + Integer.toString(this.f_m_c_49) + ")";
   }

   public final int m_b_9b68() {
      return C_bq.m_a_134621(103);
   }

   public final int m_c_9b68() {
      return -1;
   }

   public final int m_d_9b68() {
      return -1;
   }

   public final int m_e_9b68() {
      return -1;
   }

   public final int m_f_9b68() {
      return -1;
   }

   public final int m_g_9b68() {
      return -1;
   }

   public final int m_h_9b68() {
      return -1;
   }

   public final String m_b_73cf11cb() {
      return new String(this.f_m_a_523beb0a);
   }

   public final boolean equals(Object var1) {
      if (!(var1 instanceof C_m)) {
         return false;
      } else {
         var1 = var1;
         return this.f_m_a_49 == ((C_m)var1).f_m_a_49;
      }
   }

   public final int m_i_9b68() {
      return C_bq.m_a_134621(112);
   }

   public final String m_c_73cf11cb() {
      return this.f_m_a_523beb0a;
   }

   public final int m_a_134621(int var1) {
      return 0;
   }
}
