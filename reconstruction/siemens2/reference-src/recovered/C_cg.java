package recovered;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

final class C_cg extends Canvas {
   private final int f_cg_a_49;
   private final int f_cg_b_49;
   private final int f_cg_c_49;
   private final int f_cg_d_49;
   private final Font f_cg_a_67f45fce;
   private int f_cg_e_49;
   private int f_cg_f_49;
   private int f_cg_g_49;
   private int f_cg_h_49;
   private String f_cg_a_523beb0a;
   private String f_cg_b_523beb0a;

   public C_cg(int var1, int var2) {
      this.setFullScreenMode(true);
      this.f_cg_g_49 = var1;
      this.f_cg_e_49 = C_bp.m_a_134621(this.f_cg_g_49);
      this.f_cg_f_49 = C_bn.f_bn_a_b4e[var2];
      this.f_cg_a_49 = this.getHeight();
      this.f_cg_b_49 = this.getWidth();
      this.f_cg_a_67f45fce = Font.getFont(32, 1, 0);
      this.f_cg_c_49 = this.f_cg_a_67f45fce.charWidth('0') + 2;
      this.f_cg_d_49 = this.f_cg_a_49 - this.f_cg_a_67f45fce.getHeight() - 7;
      this.f_cg_b_523beb0a = "00000" + Integer.toHexString(this.f_cg_e_49).toUpperCase();
      this.f_cg_b_523beb0a = this.f_cg_b_523beb0a.substring(this.f_cg_b_523beb0a.length() - 6);
   }

   protected final void keyPressed(int var1) {
      if ((var1 = this.getGameAction(var1)) == 2 || var1 == 5) {
         this.f_cg_h_49 = (this.f_cg_h_49 + (var1 != 2 ? 1 : -1) + 6) % 6;
      } else if (var1 == 1 || var1 == 6) {
         int var2 = 1048576 >> (this.f_cg_h_49 << 2);
         int var3 = 15728640 >> (this.f_cg_h_49 << 2);
         this.f_cg_f_49 = this.f_cg_f_49 & ~var3 | this.f_cg_f_49 + (var1 != 1 ? -var2 : var2) & var3;
      } else if (var1 != 8 && var1 != 8) {
         C_bg.m_a_9b75();
      } else {
         C_bp.m_a_255f295(this.f_cg_g_49, Integer.parseInt(this.f_cg_a_523beb0a, 16));
         C_bp.f_bp_a_2408ab.m_b_9b75();
         C_bp.f_bp_b_49 = C_bp.m_a_134621(107);
         C_bp.f_bp_c_49 = C_bp.m_a_134621(114);
      }

      this.repaint();
   }

   protected final void paint(Graphics var1) {
      var1.setColor(16777215);
      var1.fillRect(0, 0, this.f_cg_b_49, this.f_cg_a_49);
      var1.setColor(C_bp.m_a_134621(this.f_cg_g_49));
      var1.fillRect(2, 2, this.f_cg_b_49 - 4, (this.f_cg_d_49 - 4) / 2);
      var1.setColor(C_be.m_c_134621(this.f_cg_e_49));
      var1.setFont(this.f_cg_a_67f45fce);
      var1.drawString(this.f_cg_b_523beb0a, this.f_cg_b_49 / 2, (this.f_cg_d_49 - 4) / 4, 17);
      var1.setColor(this.f_cg_f_49);
      var1.fillRect(2, this.f_cg_d_49 / 2, this.f_cg_b_49 - 4, (this.f_cg_d_49 - 4) / 2);
      var1.setColor(0);
      var1.drawRect(0, 0, this.f_cg_b_49 - 1, this.f_cg_d_49 - 1);
      this.f_cg_a_523beb0a = "00000" + Integer.toHexString(this.f_cg_f_49).toUpperCase();
      this.f_cg_a_523beb0a = this.f_cg_a_523beb0a.substring(this.f_cg_a_523beb0a.length() - 6);
      int var2 = (this.f_cg_b_49 - 6 * this.f_cg_c_49) / 2;

      for (int var3 = 0; var3 < this.f_cg_a_523beb0a.length(); var3++) {
         var1.setColor(var3 >= 2 ? (var3 >= 4 ? 2105568 : 2154528) : 14688288);
         var1.drawSubstring(this.f_cg_a_523beb0a, var3, 1, var2 + var3 * this.f_cg_c_49, this.f_cg_d_49 + 4, 20);
         if (var3 == this.f_cg_h_49) {
            var1.setColor(0);
            var1.fillRect(var2 + var3 * this.f_cg_c_49, this.f_cg_d_49 + 3, this.f_cg_c_49 - 3, 2);
            var1.fillRect(var2 + var3 * this.f_cg_c_49, this.f_cg_d_49 + this.f_cg_a_67f45fce.getHeight() + 3, this.f_cg_c_49 - 3, 2);
         }
      }
   }
}
