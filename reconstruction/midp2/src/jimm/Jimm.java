package jimm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import recovered.C_aa;
import recovered.C_ac;
import recovered.C_ah;
import recovered.C_ap;
import recovered.C_at;
import recovered.C_au;
import recovered.C_az;
import recovered.C_b;
import recovered.C_bf;
import recovered.C_bi;
import recovered.C_bp;
import recovered.C_bq;
import recovered.C_bt;
import recovered.C_bu;
import recovered.C_ce;
import recovered.C_cl;
import recovered.C_cp;
import recovered.C_v;
import recovered.C_w;

public class Jimm extends MIDlet {
   public static String f_jimm_Jimm_a_523beb0a;
   public static Jimm f_jimm_Jimm_a_3fbeb738;
   public static Display f_jimm_Jimm_a_4a58c677;
   public static boolean f_jimm_Jimm_a_5a;
   public static boolean f_jimm_Jimm_b_5a;
   public static boolean f_jimm_Jimm_c_5a;
   public static boolean f_jimm_Jimm_d_5a;
   public static boolean f_jimm_Jimm_e_5a;
   private static Timer f_jimm_Jimm_a_f6e8e442 = new Timer();
   private C_cp f_jimm_Jimm_a_240da2;
   private C_au f_jimm_Jimm_a_2406bb;
   private InputStream f_jimm_Jimm_a_91ffb459;
   public static final String f_jimm_Jimm_b_523beb0a = System.getProperty("microedition.platform");
   public static final String f_jimm_Jimm_c_523beb0a = System.getProperty("microedition.profiles");
   public static boolean f_jimm_Jimm_f_5a = false;
   public static boolean f_jimm_Jimm_g_5a = true;

   public void startApp() {
      C_v.f_v_a_13cbe5dc = this;
      if (f_jimm_Jimm_a_3fbeb738 != null) {
         if (f_jimm_Jimm_b_5a) {
            p_jimm_Jimm_b_9b75();
         }
      } else {
         f_jimm_Jimm_a_3fbeb738 = this;
         if ((f_jimm_Jimm_a_523beb0a = this.getAppProperty("Jimm-Version")) == null) {
            f_jimm_Jimm_a_523beb0a = "0.7.0b";
         }

         new C_bq();
         this.f_jimm_Jimm_a_240da2 = new C_cp(C_bt.m_a_e96ea081("w3"));
         Display.getDisplay(this).setCurrent(this.f_jimm_Jimm_a_240da2);
         C_bu.m_a_9b75();
         f_jimm_Jimm_a_4a58c677 = Display.getDisplay(this);
         C_cp.m_a_aad3b1ff("Get display object");
         C_cp.m_b_13462e(10);
         switch (C_bq.m_a_134621(69)) {
            case 1:
               C_bf.m_a_b329a056(null, false);
               break;
            case 2:
               try {
                  this.f_jimm_Jimm_a_2406bb = new C_b();
                  this.f_jimm_Jimm_a_2406bb.m_a_aad3b1ff(C_bq.m_a_47921032(34));
                  this.f_jimm_Jimm_a_91ffb459 = this.f_jimm_Jimm_a_2406bb.m_a_b52a89f8();
               } catch (IOException var1) {
               } catch (Exception var2) {
               }

               C_bf.m_a_b329a056(this.f_jimm_Jimm_a_91ffb459, false);
         }

         new C_ah();
         C_cp.m_a_aad3b1ff("Load emotion icons");
         C_cp.m_b_13462e(20);
         new C_ac();
         C_cp.m_a_13462e(C_bi.m_a_1349e2(C_ac.m_c_9b68()));
         C_cp.m_a_48a013c6(C_ac.m_a_2477940());
         C_cp.m_a_aad3b1ff("Create ICQ object");
         C_cp.m_b_13462e(30);
         new C_aa();
         C_cp.m_a_aad3b1ff("Create text storage");
         C_cp.m_b_13462e(40);
         new C_bp();
         C_cp.m_a_aad3b1ff("Create main menu");
         C_cp.m_b_13462e(50);
         new C_at();
         C_cp.m_a_aad3b1ff("Create Traffic object");
         C_cp.m_b_13462e(60);
         new C_w();
         C_w.m_c_9b75();
         C_cp.m_a_aad3b1ff("Create list object");
         C_cp.m_b_13462e(70);
         new C_ap();
         C_cp.m_a_aad3b1ff("Create chat object");
         C_cp.m_b_13462e(80);
         new C_ce();
         C_cp.m_a_aad3b1ff("Load templates");
         C_cp.m_b_13462e(90);
         new C_bi();
         C_cp.m_a_aad3b1ff("Load user interface");
         C_cp.m_b_13462e(100);
         C_cl.m_a_9b75();
         C_bi.m_a_1385ff(true);
         C_bf.m_a_297a162c(f_jimm_Jimm_a_4a58c677);
         C_az.m_a_51c0f892(f_jimm_Jimm_a_4a58c677.getCurrent());
         C_bq.m_a_9b75();
      }
   }

   public void pauseApp() {
   }

   public void destroyApp(boolean var1) throws javax.microedition.midlet.MIDletStateChangeException {
      C_ac.m_b_9b75();

      try {
         C_at.m_a_9b75();
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

   public final C_cp m_a_46a7aa23() {
      return this.f_jimm_Jimm_a_240da2;
   }

   private static void p_jimm_Jimm_b_9b75() {
      if (C_cp.m_a_9b79()) {
         C_cp.m_a_9b75();
      } else {
         C_az.m_a_51c0f892(f_jimm_Jimm_a_4a58c677.getCurrent());
      }
   }

   public static void m_a_1385ff(boolean var0) {
      if (var0) {
         f_jimm_Jimm_a_4a58c677.setCurrent(null);
      } else {
         Displayable var1;
         if (!(var1 = f_jimm_Jimm_a_4a58c677.getCurrent()).isShown()) {
            if (var1 != null) {
               f_jimm_Jimm_a_4a58c677.setCurrent(null);
            }

            p_jimm_Jimm_b_9b75();
         }
      }
   }

   static {
      if (f_jimm_Jimm_b_523beb0a != null) {
         String var0;
         f_jimm_Jimm_a_5a = (var0 = f_jimm_Jimm_b_523beb0a.toLowerCase()).toLowerCase().indexOf("fly") != -1;
         f_jimm_Jimm_b_5a = var0.toLowerCase().indexOf("ericsson") != -1;
         f_jimm_Jimm_c_5a = var0.indexOf("m600") != -1
            || var0.indexOf("p800") != -1
            || var0.indexOf("p900") != -1
            || var0.indexOf("p910") != -1
            || var0.indexOf("w950") != -1
            || var0.indexOf("p990") != -1
            || var0.indexOf("g900") != -1
            || var0.indexOf("p1i") != -1;
         f_jimm_Jimm_d_5a = var0.toLowerCase().indexOf("nokia") != -1;
      }

      f_jimm_Jimm_e_5a = f_jimm_Jimm_d_5a;
   }
}
