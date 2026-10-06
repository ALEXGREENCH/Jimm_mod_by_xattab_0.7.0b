package recovered;

import java.util.Vector;

public abstract class C_cb implements Runnable {
   protected volatile boolean f_cb_a_5a;
   protected volatile Thread f_cb_a_72a5fc31;
   protected Vector f_cb_a_48a69a2c;

   public synchronized void m_a_aad3b1ff(String var1) throws recovered.C_ar {
   }

   public synchronized void m_a_9b75() {
   }

   public final synchronized int m_b_9b68() {
      return this.f_cb_a_48a69a2c == null ? 0 : this.f_cb_a_48a69a2c.size();
   }

   public final C_cd m_a_46a7a8af() throws recovered.C_ar {
      byte[] var1;
      synchronized (this.f_cb_a_48a69a2c) {
         if (this.f_cb_a_48a69a2c.size() == 0) {
            return null;
         }

         var1 = (byte[])this.f_cb_a_48a69a2c.elementAt(0);
         this.f_cb_a_48a69a2c.removeElementAt(0);
      }

      return C_cd.m_a_3c792e68(var1, 0, var1.length);
   }

   public void m_a_cb4b0023(C_cd var1) throws recovered.C_ar {
   }

   public int m_a_9b68() {
      return 0;
   }

   public byte[] m_a_12d408() {
      return new byte[4];
   }

   public void run() {
   }
}
