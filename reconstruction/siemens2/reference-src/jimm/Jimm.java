package jimm;

import com.siemens.mp.game.Light;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import recovered.C_aa;
import recovered.C_ac;
import recovered.C_ah;
import recovered.C_ao;
import recovered.C_as;
import recovered.C_at;
import recovered.C_av;
import recovered.C_b;
import recovered.C_be;
import recovered.C_bg;
import recovered.C_bo;
import recovered.C_bp;
import recovered.C_bs;
import recovered.C_cc;
import recovered.C_ci;
import recovered.C_cn;
import recovered.C_v;
import recovered.C_w;

public class Jimm extends MIDlet {
   public static String f_jimm_Jimm_a_523beb0a;
   public static Jimm f_jimm_Jimm_a_3fbeb738;
   public static Display f_jimm_Jimm_a_4a58c677;
   public static boolean f_jimm_Jimm_a_5a;
   private static Timer f_jimm_Jimm_a_f6e8e442 = new Timer();
   private C_cn f_jimm_Jimm_a_240d64;
   public static String f_jimm_Jimm_b_523beb0a;
   public static String f_jimm_Jimm_c_523beb0a;
   private C_at f_jimm_Jimm_a_24069c;
   private InputStream f_jimm_Jimm_a_91ffb459;
   public static final String f_jimm_Jimm_d_523beb0a = System.getProperty("microedition.platform");
   public static final String f_jimm_Jimm_e_523beb0a = System.getProperty("microedition.profiles");
   public static boolean f_jimm_Jimm_b_5a = false;
   public static boolean f_jimm_Jimm_c_5a = true;

   public void startApp() {
      C_v.f_v_a_13cbe5dc = this;
      if (f_jimm_Jimm_a_3fbeb738 == null) {
         f_jimm_Jimm_a_3fbeb738 = this;
         if ((f_jimm_Jimm_a_523beb0a = this.getAppProperty("Jimm-Version")) == null) {
            f_jimm_Jimm_a_523beb0a = "0.7.0b";
         }

         new C_bp();
         this.f_jimm_Jimm_a_240d64 = new C_cn(C_bs.m_a_e96ea081("u3"));
         Display.getDisplay(this).setCurrent(this.f_jimm_Jimm_a_240d64);
         f_jimm_Jimm_a_4a58c677 = Display.getDisplay(this);
         C_cn.m_a_aad3b1ff("Get display object");
         C_cn.m_b_13462e(10);
         switch (C_bp.m_a_134621(69)) {
            case 1:
               C_be.m_a_b329a056(null, false);
               break;
            case 2:
               try {
                  this.f_jimm_Jimm_a_24069c = new C_b();
                  this.f_jimm_Jimm_a_24069c.m_a_aad3b1ff(C_bp.m_a_47921032(34));
                  this.f_jimm_Jimm_a_91ffb459 = this.f_jimm_Jimm_a_24069c.m_a_b52a89f8();
               } catch (IOException var1) {
               } catch (Exception var2) {
               }

               C_be.m_a_b329a056(this.f_jimm_Jimm_a_91ffb459, false);
         }

         new C_ah();
         C_cn.m_a_aad3b1ff("Load emotion icons");
         C_cn.m_b_13462e(20);
         new C_ac();
         C_cn.m_a_13462e(C_bg.m_a_1349e2(C_ac.m_c_9b68()));
         C_cn.m_a_48a013c6(C_ac.m_a_2477940());
         C_cn.m_a_aad3b1ff("Create ICQ object");
         C_cn.m_b_13462e(30);
         new C_aa();
         C_cn.m_a_aad3b1ff("Create text storage");
         C_cn.m_b_13462e(40);
         new C_bo();
         C_cn.m_a_aad3b1ff("Create main menu");
         C_cn.m_b_13462e(50);
         new C_as();
         C_cn.m_a_aad3b1ff("Create Traffic object");
         C_cn.m_b_13462e(60);
         new C_w();
         C_w.m_c_9b75();
         C_cn.m_a_aad3b1ff("Create list object");
         C_cn.m_b_13462e(70);
         new C_ao();
         C_cn.m_a_aad3b1ff("Create chat object");
         C_cn.m_b_13462e(80);
         new C_cc();
         C_cn.m_a_aad3b1ff("Load templates");
         C_cn.m_b_13462e(90);
         new C_bg();
         C_cn.m_a_aad3b1ff("Load user interface");
         C_cn.m_b_13462e(100);
         C_ci.m_a_9b75();
         C_bg.m_a_1385ff(true);
         C_be.m_a_297a162c(f_jimm_Jimm_a_4a58c677);
         C_av.m_a_51c0f892(f_jimm_Jimm_a_4a58c677.getCurrent());
         if (f_jimm_Jimm_a_5a) {
            f_jimm_Jimm_b_523beb0a = new String("native://ELSE_STR_MYMENU");
            f_jimm_Jimm_c_523beb0a = new String("native://STUP_ILLUMINATI");
         } else {
            if (C_bp.m_a_134632(158)) {
               Light.setLightOn();
            }

            f_jimm_Jimm_b_523beb0a = new String("native://NAT_MAIN_MENU");
            f_jimm_Jimm_c_523beb0a = new String("native://NAT_ILLUMINATION");
         }

         C_bp.m_a_9b75();
      }
   }

   public void pauseApp() {
   }

   public void destroyApp(boolean var1) {
      C_ac.m_b_9b75();
      Light.setLightOff();

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

   static {
      if (f_jimm_Jimm_d_523beb0a != null) {
         String var0;
         f_jimm_Jimm_a_5a = (var0 = f_jimm_Jimm_d_523beb0a.toLowerCase()).indexOf("65") != -1
            || var0.indexOf("66") != -1
            || var0.indexOf("70") != -1
            || var0.indexOf("72") != -1
            || var0.indexOf("75") != -1 && var0.indexOf("s") < 0;
      }
   }
}
