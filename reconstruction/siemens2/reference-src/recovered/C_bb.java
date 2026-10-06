package recovered;

final class C_bb {
   long f_bb_a_4a;
   int f_bb_a_49;

   public C_bb(boolean var1, long var2, int var4, boolean var5) {
      this.f_bb_a_4a = var2;
      this.f_bb_a_49 = var4 & 16777215 | (var5 ? 134217728 : 0) | (var1 ? 67108864 : 0);
   }

   public final boolean m_a_9b79() {
      return (this.f_bb_a_49 & 67108864) != 0;
   }
}
