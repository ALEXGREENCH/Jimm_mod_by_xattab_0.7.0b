package recovered;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class C_bo {
   public static C_bo f_bo_a_2409c2 = null;
   private int f_bo_a_49 = 16711680;
   private int f_bo_b_49 = 0;
   private int[] f_bo_a_b4e;
   private int[] f_bo_b_b4e;
   private int[] f_bo_c_b4e;
   private Image[] f_bo_a_5743b4d9 = new Image[4];
   private int f_bo_c_49;
   private int f_bo_d_49;

   public C_bo(String var1) {
      try {
         InputStream var4;
         (var4 = this.getClass().getResourceAsStream(var1)).read();
         this.f_bo_c_49 = var4.read();
         this.f_bo_d_49 = var4.read();
         this.f_bo_a_b4e = new int[this.f_bo_c_49];
         this.f_bo_b_b4e = new int[this.f_bo_c_49];
         this.f_bo_c_b4e = new int[this.f_bo_c_49];

         for (int var2 = 0; var2 < this.f_bo_c_49; var2++) {
            this.f_bo_a_b4e[var2] = var4.read();
            this.f_bo_b_b4e[var2] = var4.read();
            this.f_bo_c_b4e[var2] = var4.read() + 1;
         }
      } catch (IOException var3) {
      }
   }

   public final void m_a_aad3b1ff(String var1) {
      try {
         this.f_bo_a_5743b4d9[3] = Image.createImage(var1);
      } catch (IOException var5) {
      }

      C_bo var6;
      (var6 = this).f_bo_a_49 = C_bp.m_a_134621(113);
      var6.f_bo_b_49 = C_bp.m_a_134621(103);
      int var7 = var6.f_bo_a_5743b4d9[3].getHeight();
      int var2 = var6.f_bo_a_5743b4d9[3].getWidth();
      int[] var3 = new int[var7 * var2];
      var6.f_bo_a_5743b4d9[3].getRGB(var3, 0, var2, 0, 0, var2, var7);

      for (int var4 = 0; var4 < var2 * var7; var4++) {
         if (var3[var4] == -16777216) {
            var3[var4] = 0xFF000000 | var6.f_bo_b_49;
         }
      }

      var6.f_bo_a_5743b4d9[0] = Image.createRGBImage(var3, var2, var7, true);
      var6.f_bo_a_5743b4d9[3].getRGB(var3, 0, var2, 0, 0, var2, var7);

      for (int var8 = 0; var8 < var2 * var7; var8++) {
         if (var3[var8] == -16777216) {
            var3[var8] = -65536;
         }
      }

      var6.f_bo_a_5743b4d9[1] = Image.createRGBImage(var3, var2, var7, true);
      var6.f_bo_a_5743b4d9[3].getRGB(var3, 0, var2, 0, 0, var2, var7);

      for (int var9 = 0; var9 < var2 * var7; var9++) {
         if (var3[var9] == -16777216) {
            var3[var9] = 0xFF000000 | var6.f_bo_a_49;
         }
      }

      var6.f_bo_a_5743b4d9[2] = Image.createRGBImage(var3, var2, var7, true);
      var6.f_bo_a_5743b4d9[3] = null;
      System.gc();
   }

   public final int m_a_aad3b1f2(String var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         if ((var4 = var4) > 31 && var4 < 127) {
            var4 -= ' ';
         }

         if (var4 > 1039 && var4 < 1104) {
            var4 -= 'Ω';
         }

         if (var4 == 1025) {
            var4 -= 'Ρ';
         }

         var2 += var4 >= 0 && var4 < this.f_bo_c_49 && var4 != '\r' && var4 != '\n' ? this.f_bo_c_b4e[var4] : 0;
      }

      return var2;
   }

   public final int m_a_9b68() {
      return this.f_bo_d_49;
   }

   public final void m_a_81e714c2(Graphics var1, int var2, int var3, String var4, int var5) {
      int var6 = var1.getClipX();
      int var7 = var1.getClipY();
      int var8 = var1.getClipWidth();
      int var9 = var1.getClipHeight();
      byte var10 = 0;
      if (var5 == 16711680) {
         var10 = 1;
      } else if (var5 == this.f_bo_a_49) {
         var10 = 2;
      }

      for (int var14 = 0; var14 < var4.length(); var14++) {
         int var11;
         if ((var11 = var4.charAt(var14) - 1) + 1 != 13 && var11 + 1 != 10) {
            if ((var11 = (var11 = var11 != 1024 ? (var11 != 1104 ? var11 : 183) : 167) <= 1024 ? var11 : var11 - 848) > 30 && var11 < 127) {
               var11 -= 31;
            }

            if (var11 > 166 && var11 < 172) {
               var11 -= 70;
            }

            if (var11 == 183) {
               var11 -= 82;
            }

            if (var11 > 190) {
               var11 -= 88;
            }

            if (var11 >= 0 && var11 < this.f_bo_c_49) {
               int var12 = this.f_bo_c_b4e[var11];
               if (var11 > 0) {
                  int var13 = this.f_bo_a_b4e[var11];
                  var11 = this.f_bo_b_b4e[var11];
                  int var19 = var2 - var13;
                  int var18 = var3 - var11;
                  if (var2 < var6) {
                     if (var2 + var12 <= var6) {
                        var2 += var12;
                        continue;
                     }

                     var12 -= var6 - var2;
                     var2 = var6;
                  }

                  if (var2 + var12 > var6 + var8) {
                     if (var2 >= var6 + var8) {
                        break;
                     }

                     var12 = var6 + var8 - var2;
                  }

                  var1.setClip(var2, var3, var12, this.f_bo_d_49);
                  var1.drawImage(this.f_bo_a_5743b4d9[var10], var19, var18, 20);
               }

               var2 += var12;
            }
         }
      }

      var1.setClip(var6, var7, var8, var9);
   }
}
