package recovered;

import javax.microedition.lcdui.Font;

public final class C_n {
   public C_f f_n_a_129e1;
   public String f_n_a_523beb0a;
   int f_n_a_49 = 0;
   private int f_n_b_49 = 0;

   public final int m_a_134621(int var1) {
      if (this.f_n_a_129e1 != null) {
         C_f var2;
         return (var2 = this.f_n_a_129e1).f_f_d_49;
      } else if (this.f_n_a_523beb0a == null) {
         return 0;
      } else {
         if ((this.f_n_b_49 & 65535) == 0) {
            Font var3 = Font.getFont(0, this.f_n_a_49 >>> 24, var1);
            this.f_n_b_49 = this.f_n_b_49 & -65536 | var3.getHeight();
         }

         return this.f_n_b_49 & 65535;
      }
   }

   public final int m_b_134621(int var1) {
      if (this.f_n_a_129e1 != null) {
         C_f var2;
         return (var2 = this.f_n_a_129e1).f_f_c_49 + 1;
      } else if (this.f_n_a_523beb0a == null) {
         return 0;
      } else {
         if ((this.f_n_b_49 & -65536) == 0) {
            Font var3 = Font.getFont(0, this.f_n_a_49 >>> 24, var1);
            this.f_n_b_49 = this.f_n_b_49 & 65535 | var3.stringWidth(this.f_n_a_523beb0a) << 16;
         }

         return this.f_n_b_49 >> 16;
      }
   }
}
