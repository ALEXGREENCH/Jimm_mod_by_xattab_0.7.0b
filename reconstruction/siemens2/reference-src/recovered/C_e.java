package recovered;

import java.util.Vector;

public final class C_e {
   public Object f_e_a_5f790d9c = null;
   protected Vector f_e_a_48a69a2c = null;
   protected boolean f_e_a_5a = true;
   protected int f_e_a_49 = 0;

   protected C_e(Object var1) {
      this.f_e_a_5f790d9c = var1;
   }

   protected final void m_a_9b75() {
      if (this.f_e_a_48a69a2c != null) {
         this.f_e_a_48a69a2c.removeAllElements();
      }
   }

   public final boolean m_a_9b79() {
      return this.f_e_a_5a;
   }

   public final int m_a_9b68() {
      return this.f_e_a_48a69a2c == null ? 0 : this.f_e_a_48a69a2c.size();
   }

   public final C_e m_a_485a599a(int var1) {
      return (C_e)this.f_e_a_48a69a2c.elementAt(var1);
   }

   protected final C_e m_a_9f354893(C_e var1) {
      if (this.f_e_a_48a69a2c == null) {
         this.f_e_a_48a69a2c = new Vector();
      }

      this.f_e_a_48a69a2c.addElement(var1);
      return var1;
   }

   protected final void m_a_cb54c07c(C_e var1, int var2) {
      if (this.f_e_a_48a69a2c == null) {
         this.f_e_a_48a69a2c = new Vector();
      }

      this.f_e_a_48a69a2c.insertElementAt(var1, var2);
   }

   protected final void m_a_13462e(int var1) {
      this.f_e_a_48a69a2c.removeElementAt(var1);
   }

   protected final int m_a_489f9f5a(C_e var1) {
      if (this.f_e_a_48a69a2c == null) {
         return -1;
      } else {
         int var2 = this.m_a_9b68();

         for (int var3 = 0; var3 < var2; var3++) {
            if (this.m_a_485a599a(var3) == var1) {
               return var3;
            }
         }

         return -1;
      }
   }

   private static int p_e_a_fc976c8e(Vector var0, C_e var1, C_k var2) {
      int var3;
      if ((var3 = var0.size()) == 0) {
         return 0;
      } else {
         int var4 = 0;
         int var5 = var3 - 1;
         int var6 = var2.m_a_496bbe28(var1, (C_e)var0.elementAt(0));
         int var8 = var2.m_a_496bbe28(var1, (C_e)var0.elementAt(var5));
         if (var6 < 0) {
            return 0;
         } else if (var8 > 0) {
            return var3;
         } else {
            do {
               var3 = (var4 + var5) / 2;
               int var7;
               if (((var7 = var2.m_a_496bbe28(var1, (C_e)var0.elementAt(var3))) > 0 || var8 < 0) && (var7 < 0 || var8 > 0)) {
                  var5 = var3;
                  var8 = var7;
               } else {
                  var4 = var3;
                  var6 = var7;
               }
            } while (var5 - var4 > 1);

            return var6 < 0 ? var4 : var5;
         }
      }
   }

   protected final void m_a_48a259a1(C_k var1) {
      Vector var5 = new Vector();
      if (this.f_e_a_48a69a2c != null) {
         int var3 = this.f_e_a_48a69a2c.size();

         for (int var2 = 0; var2 < var3; var2++) {
            C_e var4 = (C_e)this.f_e_a_48a69a2c.elementAt(var2);
            var5.insertElementAt(var4, p_e_a_fc976c8e(var5, var4, var1));
         }

         this.f_e_a_48a69a2c = var5;
      }
   }
}
