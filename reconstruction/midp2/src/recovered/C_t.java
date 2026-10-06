package recovered;

/** 0.6 source correspondence (inferred): DrawControls.TextList. Release class: t. */

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class C_t extends C_bf {
   public Vector f_t_a_48a69a2c = new Vector();
   public Hashtable f_t_a_d18d4967 = new Hashtable();

   public C_t(String var1) {
      super(var1);
   }

   public final int m_a_9b68() {
      if (this.f_t_a_48a69a2c.isEmpty()) {
         return 0;
      } else {
         int var1 = this.f_t_a_48a69a2c.size();
         return ((C_ag)this.f_t_a_48a69a2c.lastElement()).f_ag_a_48a69a2c.size() == 0 ? var1 - 1 : var1;
      }
   }

   private C_ag p_t_a_c2f0d031(int var1) {
      return (C_ag)this.f_t_a_48a69a2c.elementAt(var1);
   }

   protected final boolean m_b_134632(int var1) {
      int var2;
      int var3;
      return (var3 = (var2 = this.m_g_9b68()) >= this.f_t_a_48a69a2c.size() ? -1 : this.p_t_a_c2f0d031(var2).f_ag_a_49) == -1
         ? false
         : this.p_t_a_c2f0d031(var1).f_ag_a_49 == var3;
   }

   protected final void m_a_d826493f(int var1, C_ca var2) {
      C_ag var3 = this.p_t_a_c2f0d031(var1);
      var2.m_a_9b75();
      if (var3.f_ag_a_48a69a2c.size() != 0) {
         C_n var4 = var3.m_a_485a5ab1(0);
         var2.f_ca_a_523beb0a = var4.f_n_a_523beb0a;
         var2.f_ca_b_49 = var4.f_n_a_49 & 16777215;
         var2.f_ca_a_49 = var4.f_n_a_49 >> 24;
      }
   }

   public final void m_a_9b75() {
      this.f_t_a_48a69a2c.removeAllElements();
      this.f_t_a_d18d4967.clear();
      this.m_i_13462e(0);
      this.m_f_9b75();
   }

   public final void m_a_68bdbb6d(String var1, int var2, int var3, int var4, boolean var5, char var6) {
      C_n var7;
      (var7 = new C_n()).f_n_a_523beb0a = var1;
      var7.f_n_a_49 = var7.f_n_a_49 & 0xFF000000 | var2 & 16777215;
      var7.f_n_a_49 = var7.f_n_a_49 & 16777215 | (var3 & 0xFF) << 24;
      if (this.f_t_a_48a69a2c.isEmpty()) {
         this.f_t_a_48a69a2c.addElement(new C_ag());
      }

      C_ag var8;
      (var8 = (C_ag)this.f_t_a_48a69a2c.lastElement()).m_a_48a3b6be(var7);
      var8.f_ag_a_49 = var4;
      if (var5) {
         var8.f_ag_a_43 = var6;
         C_ag var9;
         (var9 = new C_ag()).f_ag_a_49 = var4;
         this.f_t_a_48a69a2c.addElement(var9);
      }
   }

   public final int m_a_134621(int var1) {
      if (this.m_f_9b68() != 3) {
         return super.m_a_134621(var1);
      } else {
         return var1 >= this.f_t_a_48a69a2c.size() ? 1 : this.p_t_a_c2f0d031(var1).m_a_134621(this.m_c_9b68());
      }
   }

   protected final void m_a_c1306d97(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (this.m_f_9b68() != 3) {
         super.m_a_c1306d97(var1, var2, var3, var4, var5, var6, var7);
      } else {
         this.p_t_a_c2f0d031(var2).m_a_dcdfe9b7(super.f_bf_c_49, var4, var1, this.m_c_9b68(), this);
      }
   }

   public final void m_a_2563266(int var1, boolean var2) {
      int var3 = 0;
      int var6 = this.m_e_9b68() / 2;
      switch (var1) {
         case -1:
         case 1:
            int var4 = this.m_b_9b68();
            int count = this.m_a_9b68();
            this.m_h_9b75();
            int var5 = 0;

            while (var5 < var6) {
               super.f_bf_b_49 += var1;
               if (super.f_bf_b_49 < 0 || super.f_bf_b_49 >= count) {
                  if (var3 != 0) {
                     super.f_bf_b_49 -= var1;
                  }
                  break;
               }

               C_ag var7 = this.p_t_a_c2f0d031(super.f_bf_b_49);
               if (var4 != var7.f_ag_a_49) {
                  var4 = var7.f_ag_a_49;
                  var3++;
                  if (var3 == 2 || !this.m_c_134632(super.f_bf_b_49) && var5 > 0) {
                     super.f_bf_b_49 -= var1;
                     break;
                  }
               }

               if (!this.m_c_134632(super.f_bf_b_49) || var3 != 0) {
                  var5++;
               }
            }

            this.m_d_13462e(var1);
            this.m_g_9b75();
            this.m_i_9b75();
            return;
         default:
            super.m_a_2563266(var1, (boolean)var2);
      }
   }

   public final String m_a_b1542ce3(int var1, boolean var2, int var3) {
      StringBuffer var4 = new StringBuffer();
      int var5 = this.f_t_a_48a69a2c.size();

      for (int var7 = 0; var7 < var5; var7++) {
         C_ag var6 = this.p_t_a_c2f0d031(var7);
         if (var2 || var3 == -1 || var6.f_ag_a_49 == var3) {
            var6.m_a_c7ce505f(var4);
            if (var6.f_ag_a_43 != 0) {
               if (var6.f_ag_a_43 == '\n') {
                  var4.append("\n");
               } else {
                  var4.append(var6.f_ag_a_43);
               }
            }
         }
      }

      if (var4.length() == 0) {
         return null;
      } else {
         String var9;
         int var8 = (var9 = var4.toString()).length();
         return var1 > var8 ? null : var9.substring(var1, var8);
      }
   }

   public final void m_a_13462e(int var1) {
      if (var1 != -1) {
         int var2 = this.f_t_a_48a69a2c.size();

         for (int var3 = 0; var3 < var2; var3++) {
            if (this.p_t_a_c2f0d031(var3).f_ag_a_49 == var1) {
               this.m_i_13462e(var3);
               return;
            }
         }
      }
   }

   public final String m_a_4dee1afa(int var1, boolean var2) {
      return this.m_a_b1542ce3(var1, var2, this.m_b_9b68());
   }

   public final int m_b_9b68() {
      int var1;
      return (var1 = this.m_g_9b68()) >= 0 && var1 < this.f_t_a_48a69a2c.size() ? this.p_t_a_c2f0d031(var1).f_ag_a_49 : -1;
   }

   public final void m_a_c4b201b5(int var1, int var2, int var3, int var4) {
      super.m_a_c4b201b5(var1, var2, var3, var4);
   }

   public final C_t m_a_485a5b6b(int var1) {
      if (this.f_t_a_48a69a2c.size() != 0) {
         ((C_ag)this.f_t_a_48a69a2c.lastElement()).f_ag_a_43 = '\n';
      }

      C_ag var2;
      (var2 = new C_ag()).f_ag_a_49 = var1;
      this.f_t_a_48a69a2c.addElement(var2);
      return this;
   }

   public final C_t m_a_3371d4f0(C_f var1, String var2, int var3) {
      if (this.f_t_a_48a69a2c.isEmpty()) {
         this.f_t_a_48a69a2c.addElement(new C_ag());
      }

      C_ag var4;
      (var4 = (C_ag)this.f_t_a_48a69a2c.lastElement()).f_ag_a_49 = var3;
      if (var4.m_b_134621(this.m_c_9b68()) + var1.f_f_c_49 > this.p_t_k_9b68()) {
         this.m_a_485a5b6b(var3);
         var4 = (C_ag)this.f_t_a_48a69a2c.lastElement();
      }

      C_n var6;
      (var6 = new C_n()).f_n_a_129e1 = var1;
      var6.f_n_a_523beb0a = var2;
      var4.m_a_48a3b6be(var6);
      return this;
   }

   private int p_t_k_9b68() {
      return this.m_i_9b68() - C_bf.f_bf_a_49 - (super.f_bf_c_49 << 1);
   }

   private static String p_t_a_f169196d(String var0, String var1, String var2) {
      int var3 = var1.length();

      int var4;
      while ((var4 = var0.indexOf(var1)) != -1) {
         var0 = var0.substring(0, var4) + var2 + var0.substring(var4 + var3, var0.length());
      }

      return var0;
   }

   public final void m_a_ba2c33f(String var1, int var2, int var3, int var4, int var5) {
      int var12 = 0;
      String var17 = null;
      if (var1 != null) {
         String var18 = p_t_a_f169196d(p_t_a_f169196d(var1, "\r\n", "\n"), "\r", "\n");
         Font var6 = this.m_a_a729dba6(var3);
         int var11 = this.f_t_a_48a69a2c.isEmpty() ? var5 : var5 - ((C_ag)this.f_t_a_48a69a2c.lastElement()).m_b_134621(this.m_c_9b68());
         int var10 = 0;
         int var9 = -1;
         int var7 = var18.length();
         int var8 = 0;

         label145:
         while (var8 < var7) {
            char var13;
            boolean var14 = (var13 = var18.charAt(var8)) == 32;
            boolean var22 = var13 == 10;
            boolean var15 = var8 == var7 - 1;
            boolean var16 = false;
            if (var15 && !var22) {
               var8++;
            }

            if (var22 || var15 || var14) {
               var17 = var18.substring(var10, var8);
               var12 = var6.stringWidth(var17);
            }

            if ((var22 || var15) && var12 <= var11) {
               this.m_a_68bdbb6d(var17, var2, var3, var4, var22, (char)(var22 ? '\n' : ' '));
               var11 = var5;
               var10 = ++var8;
               var9 = -1;
            } else {
               if ((var22 || var15 || var14) && var12 > var11) {
                  if (var12 < var5 && var9 != -1) {
                     var16 = true;
                  } else if (var5 != var11 && var9 == -1) {
                     this.m_a_485a5b6b(var4);
                     var8 = var10;
                     var11 = var5;
                     var9 = -1;
                     continue;
                  }
               }

               if ((var22 || var15 || var14) && var12 > var5 && !var16) {
                  if (var9 == -1) {
                     while (true) {
                        if (var8 >= 1) {
                           var17 = var18.substring(var10, var8);
                           if (var6.stringWidth(var17) > var11) {
                              var8--;
                              continue;
                           }
                        }

                        this.m_a_68bdbb6d(var17, var2, var3, var4, true, '\u0000');
                        var11 = var5;
                        var10 = var8;
                        var9 = -1;
                        continue label145;
                     }
                  }

                  var16 = true;
               }

               if (!var16) {
                  if (var14) {
                     var9 = var8;
                  }

                  var8++;
               } else {
                  if (C_bq.m_a_134632(132)) {
                     int splitIndex;
                     for (splitIndex = var8; splitIndex >= 1; splitIndex--) {
                        var17 = var18.substring(var10, splitIndex);
                        if ((var12 = var6.stringWidth(var17)) <= var11) {
                           break;
                        }
                     }

                     if (splitIndex - (var9 + 1) > 1) {
                        String var19 = var18.substring(var10, splitIndex);
                        this.m_a_68bdbb6d(var19, var2, var3, var4, true, '\u0000');
                        var8 = splitIndex;
                     } else {
                        String var20 = var18.substring(var10, var9);
                        this.m_a_68bdbb6d(var20, var2, var3, var4, true, ' ');
                        var8 = var9 + 1;
                     }
                  } else {
                     String var21 = var18.substring(var10, var9);
                     this.m_a_68bdbb6d(var21, var2, var3, var4, true, ' ');
                     var8 = var9 + 1;
                  }

                  var10 = var8;
                  var11 = var5;
                  var9 = -1;
               }
            }
         }
      }
   }

   public final C_t m_a_68a7a001(String var1, int var2, int var3, int var4) {
      this.m_a_ba2c33f(var1, var2, var3, var4, this.p_t_k_9b68());
      this.m_f_9b75();
      return this;
   }

   public static void m_a_60d6f02(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      C_t var13;
      (var13 = new C_t(null)).m_b_13462e(0);
      var13.m_a_ba2c33f(var1, var8, var7, -1, var4);
      int var12 = 0;
      var7 = var13.m_a_9b68();

      for (int var9 = 0; var9 < var7; var9++) {
         var12 += var13.p_t_a_c2f0d031(var9).m_a_134621(0);
      }

      int var11 = var3 + (var5 - var12) / 2;

      for (int var10 = 0; var10 < var7; var10++) {
         var13.p_t_a_c2f0d031(var10).m_a_dcdfe9b7(var2, var11, var0, 0, var13);
         var11 += var13.p_t_a_c2f0d031(var10).m_a_134621(0);
      }
   }
}
