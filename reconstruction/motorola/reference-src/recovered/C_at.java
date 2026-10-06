package recovered;

import java.io.InputStream;
import java.io.OutputStream;
import jimm.Jimm;

public abstract class C_at {
   C_at() {
   }

   public static C_at m_a_46a7a31d() {
      return (C_at)(!Jimm.f_jimm_Jimm_a_5a ? new C_x() : new C_b());
   }

   public static String[] m_a_855a3144(String var0, boolean var1) {
      return !Jimm.f_jimm_Jimm_a_5a ? C_x.m_a_855a3144(var0, var1) : C_b.m_a_855a3144(var0, var1);
   }

   public static long m_a_aad3b1f3(String var0) {
      return !Jimm.f_jimm_Jimm_a_5a ? C_x.m_a_aad3b1f3(var0) : C_b.m_a_aad3b1f3(var0);
   }

   public abstract void m_a_aad3b1ff(String var1);

   public abstract OutputStream m_a_75f818f3();

   public abstract InputStream m_a_b52a89f8();

   public abstract void m_a_9b75();

   public abstract long m_a_9b69();
}
