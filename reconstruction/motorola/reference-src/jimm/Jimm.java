package jimm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import recovered.C_aa;
import recovered.C_ac;
import recovered.C_ah;
import recovered.C_ap;
import recovered.C_as;
import recovered.C_at;
import recovered.C_av;
import recovered.C_bd;
import recovered.C_bf;
import recovered.C_bn;
import recovered.C_bp;
import recovered.C_bs;
import recovered.C_cd;
import recovered.C_cn;
import recovered.C_u;
import recovered.C_v;

public class Jimm extends MIDlet {
   public static String f_jimm_Jimm_a_523beb0a;
   public static Jimm f_jimm_Jimm_a_3fbeb738;
   public static Display f_jimm_Jimm_a_4a58c677;
   public static final boolean f_jimm_Jimm_a_5a;
   private static Timer f_jimm_Jimm_a_f6e8e442 = new Timer();
   private C_cn f_jimm_Jimm_a_240d64;
   private C_at f_jimm_Jimm_a_24069c;
   private InputStream f_jimm_Jimm_a_91ffb459;
   public static final String f_jimm_Jimm_b_523beb0a = System.getProperty("microedition.platform");
   public static final String f_jimm_Jimm_c_523beb0a = System.getProperty("microedition.profiles");
   public static boolean f_jimm_Jimm_b_5a = true;

   public void startApp() {
      C_u.f_u_a_13cbe5dc = this;
      if (f_jimm_Jimm_a_3fbeb738 == null) {
         f_jimm_Jimm_a_3fbeb738 = this;
         if ((f_jimm_Jimm_a_523beb0a = this.getAppProperty("Jimm-Version")) == null) {
            f_jimm_Jimm_a_523beb0a = "0.7.0b";
         }

         new C_bp();
         this.f_jimm_Jimm_a_240d64 = new C_cn(C_bs.m_a_e96ea081("z3"));
         Display.getDisplay(this).setCurrent(this.f_jimm_Jimm_a_240d64);
         f_jimm_Jimm_a_4a58c677 = Display.getDisplay(this);
         C_cn.m_a_aad3b1ff("Get display object");
         C_cn.m_b_13462e(10);
         switch (C_bp.m_a_134621(69)) {
            case 1:
               C_bd.m_a_b329a056(null, false);
               break;
            case 2:
               try {
                  this.f_jimm_Jimm_a_24069c = C_at.m_a_46a7a31d();
                  this.f_jimm_Jimm_a_24069c.m_a_aad3b1ff(C_bp.m_a_47921032(34));
                  this.f_jimm_Jimm_a_91ffb459 = this.f_jimm_Jimm_a_24069c.m_a_b52a89f8();
               } catch (IOException var1) {
               } catch (Exception var2) {
               }

               C_bd.m_a_b329a056(this.f_jimm_Jimm_a_91ffb459, false);
         }

         new C_ah();
         C_cn.m_a_aad3b1ff("Load emotion icons");
         C_cn.m_b_13462e(20);
         new C_ac();
         C_cn.m_a_13462e(C_bf.m_a_1349e2(C_ac.m_c_9b68()));
         C_cn.m_a_48a013c6(C_ac.m_a_2477940());
         C_cn.m_a_aad3b1ff("Create ICQ object");
         C_cn.m_b_13462e(30);
         new C_aa();
         C_cn.m_a_aad3b1ff("Create text storage");
         C_cn.m_b_13462e(40);
         new C_bn();
         C_cn.m_a_aad3b1ff("Create main menu");
         C_cn.m_b_13462e(50);
         new C_as();
         C_cn.m_a_aad3b1ff("Create Traffic object");
         C_cn.m_b_13462e(60);
         new C_v();
         C_v.m_c_9b75();
         C_cn.m_a_aad3b1ff("Create list object");
         C_cn.m_b_13462e(70);
         new C_ap();
         C_cn.m_a_aad3b1ff("Create chat object");
         C_cn.m_b_13462e(80);
         new C_cd();
         C_cn.m_a_aad3b1ff("Load templates");
         C_cn.m_b_13462e(90);
         new C_bf();
         C_cn.m_a_aad3b1ff("Load user interface");
         C_cn.m_b_13462e(100);
         C_bf.m_a_1385ff(true);
         C_bd.m_a_297a162c(f_jimm_Jimm_a_4a58c677);
         C_av.m_a_51c0f892(f_jimm_Jimm_a_4a58c677.getCurrent());
         C_bp.m_a_9b75();
      }
   }

   public void pauseApp() {
   }

   public void destroyApp(boolean var1) {
      C_ac.m_b_9b75();

      try {
         C_as.m_a_9b75();
      } catch (Exception var2) {
      }

      f_jimm_Jimm_a_4a58c677.setCurrent(null);
      this.notifyDestroyed();
   }

   public static Timer m_a_94e56161() {
      return f_jimm_Jimm_a_f6e8e442;
   }

   public static void m_a_9b75() {
      try {
         f_jimm_Jimm_a_f6e8e442.cancel();
      } catch (IllegalStateException var0) {
      }

      f_jimm_Jimm_a_f6e8e442 = new Timer();
   }

   public final C_cn m_a_46a7a9e5() {
      return this.f_jimm_Jimm_a_240d64;
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   static {
      boolean var0 = false;
      boolean var3 = false /* VF: Semaphore variable */;

      label35: {
         try {
            var3 = true;
            var0 = Class.forName("javax.microedition.io.file.FileConnection") != null;
            var3 = false;
            break label35;
         } catch (ClassNotFoundException var4) {
            var3 = false;
         } finally {
            if (var3) {
               f_jimm_Jimm_a_5a = false;
            }
         }

         f_jimm_Jimm_a_5a = false;
         return;
      }

      f_jimm_Jimm_a_5a = var0;
   }
}
