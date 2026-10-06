package recovered;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class C_g extends TimerTask {
   private HttpConnection f_g_a_473d1dc;
   private InputStream f_g_a_91ffb459;

   public final void run() {
      try {
         String var1 = C_cf.m_a_55a39fc4(C_cf.f_cf_o_b47, 0, C_cf.f_cf_o_b47.length);
         this.f_g_a_473d1dc = (HttpConnection)Connector.open(var1);
         this.f_g_a_473d1dc.setRequestProperty("X-Jimm-Version", "0.7b");
         if (this.f_g_a_473d1dc.getResponseCode() != 200) {
            throw new IOException();
         }

         this.f_g_a_91ffb459 = this.f_g_a_473d1dc.openInputStream();
         byte[] var7;
         int var2 = (var7 = new byte[(int)this.f_g_a_473d1dc.getLength()]).length;
         int var3 = 0;

         while (0 < var2) {
            int var4 = this.f_g_a_91ffb459.read(var7, var3, var2);
            if (-1 == var4) {
               return;
            }

            var3 += var4;
            var2 -= var4;
         }

         C_bf.m_a_e96ea081(new String(var7, 0, 10));
         C_bf.m_b_e96ea081(C_cf.m_a_20e7da8(var7, 11, var7.length - 11, true));
      } catch (Exception var6) {
         C_bf.m_a_e96ea081("Error: " + var6.getMessage());
      }

      synchronized (C_bf.m_a_46a7a52c()) {
         if (C_bf.m_a_2477ad3() != null && C_bf.m_a_2477ad3().m_b_9b79()) {
            C_bf.m_a_2477ad3().m_a_68a79fe2(C_bf.m_a_73cf11cb(), C_bf.m_a_2477ad3().m_d_9b68(), C_bp.f_bp_g_49, -1);
            C_bp.m_a_4f708078(39, C_bf.m_b_73cf11cb());
            C_bp.m_c_9b75();
         }
      }
   }
}
