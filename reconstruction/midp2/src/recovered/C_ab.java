package recovered;

/** 0.6 source correspondence (inferred): jimm.FileTransfer. Release class: ab. */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

public final class C_ab implements Runnable, CommandListener, ItemStateListener, C_u {
   private int f_ab_a_49;
   C_be f_ab_a_24088c;
   private Form f_ab_a_67f46df9;
   private InputStream f_ab_a_91ffb459;
   private int f_ab_b_49;
   private String f_ab_a_523beb0a;
   private TextField f_ab_a_a555694c;
   private TextField f_ab_b_a555694c;
   private ChoiceGroup f_ab_a_aa0ede5b;
   private Alert f_ab_a_8de1009d;
   private int f_ab_c_49;
   private C_aw f_ab_a_2406f9;
   private String f_ab_b_523beb0a;
   private String f_ab_c_523beb0a;
   private Command f_ab_a_1570d10e = new Command(C_bt.m_a_e96ea081("6"), Jimm.f_jimm_Jimm_c_5a ? 3 : 2, 2);
   private Command f_ab_b_1570d10e = new Command(C_bt.m_a_e96ea081("8"), 4, 1);

   public C_ab(int var1, C_aw var2) {
      this.f_ab_c_49 = var1;
      this.f_ab_a_2406f9 = var2;
   }

   public final C_aw m_a_46a7a37a() {
      return this.f_ab_a_2406f9;
   }

   public final void m_a_b3296085(InputStream var1, int var2) {
      this.f_ab_a_91ffb459 = var1;
      this.f_ab_b_49 = var2;
   }

   public final void m_a_9b75() {
      if (this.f_ab_c_49 == 2) {
         this.f_ab_a_24088c = new C_be(this);
         Display.getDisplay(Jimm.f_jimm_Jimm_a_3fbeb738).setCurrent(this.f_ab_a_24088c);
         this.f_ab_a_24088c.m_a_9b75() ;
      } else {
         if (this.f_ab_c_49 == 1) {
            try {
               C_al.m_a_48a6e557(this);
               C_al.m_a_1385ff(false);
               C_al.m_a_9b75() ;
               return;
            } catch (C_ar var1) {
               C_ar.m_a_aef55300(var1);
            }
         }
      }
   }

   public final void run() {
      switch (this.f_ab_a_49) {
         case 10001:
            try {
               C_ab var1 = this;
               this.f_ab_a_523beb0a = null;
               String var2 = "filetransfer.jimm.org";
               String var10 = "http://" + var2 + "/__receive_file.php";

               try {
                  HttpConnection var4;
                  (var4 = (HttpConnection)Connector.open(var10, 3)).setRequestMethod("POST");
                  var2 = "a9f843c9b8a736e53c40f598d434d283e4d9ff72";
                  var4.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + var2);
                  OutputStream var3 = var4.openOutputStream();
                  StringBuffer var5;
                  (var5 = new StringBuffer()).append("--").append(var2).append("\r\n");
                  var5.append("Content-Disposition: form-data; name=\"jimmfile\"; filename=\"").append(var1.f_ab_c_523beb0a).append("\"\r\n");
                  var5.append("Content-Type: application/octet-stream\r\n");
                  var5.append("Content-Transfer-Encoding: binary\r\n");
                  var5.append("\r\n");
                  var3.write(C_cf.m_a_44c4d6c8(var5.toString(), true));
                  byte[] var14 = new byte[1024];
                  int var6 = var1.f_ab_b_49;

                  do {
                     int var7 = var1.f_ab_a_91ffb459.read(var14);
                     var3.write(var14, 0, var7);
                     var6 -= var7;
                     if (var1.f_ab_b_49 != 0) {
                        C_cp.m_b_13462e(100 * (var1.f_ab_b_49 - var6) / var1.f_ab_b_49);
                        C_cp.m_a_aad3b1ff(C_bt.m_a_e96ea081("K2")) ;
                     }
                  } while (var6 > 0);

                  C_at.m_b_13462e(var1.f_ab_b_49);
                  StringBuffer var19;
                  (var19 = new StringBuffer()).append("\r\n--").append(var2).append("--\r\n");
                  var3.write(C_cf.m_a_44c4d6c8(var19.toString(), true));
                  var3.flush();
                  int var20;
                  if ((var20 = var4.getResponseCode()) != 200) {
                     throw new C_ar(194, var20);
                  }

                  InputStream var12 = var4.openInputStream();
                  var5 = new StringBuffer();

                  while ((var6 = var12.read()) != -1) {
                     var5.append((char)(var6 & 0xFF));
                  }

                  String var17;
                  if ((var17 = var5.toString()).indexOf("http://") == -1) {
                     throw new C_ar(195, 0);
                  }

                  String var18 = C_cf.m_a_def8391f(C_cf.m_a_def8391f(var17, "\r", "", false), "\n", "", false);
                  var3.close();
                  var12.close();
                  var4.close();
                  StringBuffer var13;
                  (var13 = new StringBuffer()).append("Filesize: ").append(var1.f_ab_b_49 / 1024).append("KB").append("\n");
                  var13.append("Link: ").append(var18);
                  C_bi.m_a_122835b8(var13.toString(), var1.f_ab_a_2406f9);
               } catch (IOException var8) {
                  throw new C_ar(196, 0);
               }
            } catch (C_ar var9) {
               this.f_ab_a_523beb0a = var9.getMessage();
            }

            this.f_ab_a_49 = 10002;
            Jimm.f_jimm_Jimm_a_4a58c677.callSerially(this);
            return;
         case 10002:
            this.p_ab_b_9b75();
            if (this.f_ab_a_523beb0a != null) {
               this.f_ab_a_8de1009d = new Alert(C_bt.m_a_e96ea081("Error"), this.f_ab_a_523beb0a, null, AlertType.ERROR);
               this.f_ab_a_8de1009d.setCommandListener(this);
               this.f_ab_a_8de1009d.setTimeout(-2);
               Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_ab_a_8de1009d);
               return;
            } else {
               this.f_ab_a_2406f9.m_e_9b75();
            }
      }
   }

   public final void m_a_aad3b1ff(String var1) {
      try {
         Object var2 = null;
         int var3 = 0;
         C_b var4;
         (var4 = new C_b()).m_a_aad3b1ff(var1);
         var2 = var4.m_a_b52a89f8();
         Object var7 = null;
         if (this.f_ab_a_2406f9 == null) {
            C_bq.m_a_4f708078(34, var1);
            C_bq.f_bq_a_240927.m_a_9b75() ;
            C_bf.m_a_b329a056((InputStream)var2, false);
         } else {
            var3 = (int)var4.m_a_9b69();
            this.m_a_b3296085((InputStream)var2, var3);
            this.m_a_e925fa09(var4.m_a_73cf11cb(), "");
         }
      } catch (Exception var5) {
         C_ar.m_a_aef55300(new C_ar(191, 0, true));
      }
   }

   public final void m_b_aad3b1ff(String var1) {
   }

   public final void itemStateChanged(Item var1) {
      if (var1 == this.f_ab_a_aa0ede5b) {
         if (this.f_ab_a_aa0ede5b.isSelected(0)) {
            C_bq.m_a_255f295(97, 0);
         } else {
            C_bq.m_a_255f295(97, 1);
         }

         C_bq.m_c_9b75();
      }
   }

   public final void m_a_e925fa09(String var1, String var2) {
      this.f_ab_a_67f46df9 = new Form(C_bt.m_a_e96ea081("W1"));
      this.f_ab_a_a555694c = new TextField(C_bt.m_a_e96ea081("q2"), var1, 255, 0);
      this.f_ab_b_a555694c = new TextField(C_bt.m_a_e96ea081("U1"), var2, 255, 0);
      this.f_ab_a_67f46df9.append(this.f_ab_a_a555694c);
      this.f_ab_a_67f46df9.append(this.f_ab_b_a555694c);
      this.f_ab_a_67f46df9.append(new StringItem(C_bt.m_a_e96ea081("k5") + ": ", this.f_ab_b_49 / 1024 + " kb"));
      this.f_ab_a_67f46df9
         .append(
            new StringItem(
               C_bt.m_a_e96ea081("D1") + ": ", C_at.m_b_47921032((this.f_ab_b_49 / C_bq.m_a_134621(72) + 1) * C_bq.m_a_134621(70)) + " " + C_bq.m_a_47921032(6)
            )
         );
      this.f_ab_a_aa0ede5b = new ChoiceGroup(null, 2);
      this.f_ab_a_aa0ede5b.append(C_bt.m_a_e96ea081("M2"), null);
      this.f_ab_a_aa0ede5b.setSelectedIndex(0, C_bq.m_a_134621(97) == 0);
      this.f_ab_a_67f46df9.append(this.f_ab_a_aa0ede5b);
      this.f_ab_a_67f46df9.addCommand(this.f_ab_a_1570d10e);
      this.f_ab_a_67f46df9.addCommand(this.f_ab_b_1570d10e);
      this.f_ab_a_67f46df9.setCommandListener(this);
      this.f_ab_a_67f46df9.setItemStateListener(this);
      Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_ab_a_67f46df9);
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (this.f_ab_a_8de1009d != null && var2 == this.f_ab_a_8de1009d) {
         this.f_ab_a_2406f9.m_e_9b75();
      } else {
         if (var1 == this.f_ab_b_1570d10e) {
            if (var2 == this.f_ab_a_67f46df9) {
               switch (C_bq.m_a_134621(97)) {
                  case 0:
                     C_cp.m_b_13462e(0);
                     C_cp.m_a_aad3b1ff(C_bt.m_a_e96ea081("93")) ;
                     C_cp.m_b_8eb9d703(C_cp.f_cp_a_1570d10e);
                     C_cp.m_a_6f63a2af(this);
                     C_cp.m_a_9b75() ;
                     this.f_ab_b_523beb0a = this.f_ab_a_a555694c.getString();
                     String[] var7 = C_cf.m_a_639c22ad(this.f_ab_b_523beb0a, '/');
                     this.f_ab_c_523beb0a = var7.length == 0 ? this.f_ab_b_523beb0a : var7[var7.length - 1];
                     this.f_ab_a_49 = 10001;
                     new Thread(this).start();
                  default:
                     return;
                  case 1:
                     String var10001 = this.f_ab_a_a555694c.getString();
                     String var8 = this.f_ab_b_a555694c.getString();
                     String var6 = var10001;
                     this.f_ab_a_24088c = null;
                     C_cp.m_b_13462e(0);
                     C_cp.m_a_aad3b1ff(C_bt.m_a_e96ea081("93")) ;
                     C_cp.m_a_8eb9d703(C_cp.f_cp_a_1570d10e);
                     C_cp.m_a_6f63a2af(this);
                     C_cp.m_a_9b75() ;
                     C_bg var4 = new C_bg(C_bq.m_a_47921032(254), this.f_ab_a_2406f9, var6, var8, this.f_ab_a_91ffb459, this.f_ab_b_49);
                     C_bv var5 = new C_bv(var4);

                     try {
                        C_ac.m_a_cb3b8b85(var5);
                        return;
                     } catch (C_ar var3) {
                        C_ar.m_a_aef55300(var3);
                        return;
                     }
               }
            }
         } else {
            if (var1 == this.f_ab_a_1570d10e) {
               this.p_ab_b_9b75();
               this.f_ab_a_2406f9.m_e_9b75();
               return;
            }

            if (var1 == C_cp.f_cp_a_1570d10e) {
               this.p_ab_b_9b75();
               C_w.m_a_9b75() ;
            }
         }
      }
   }

   private void p_ab_b_9b75() {
      this.f_ab_a_24088c = null;
      this.f_ab_a_91ffb459 = null;
      this.f_ab_a_67f46df9 = null;
      this.f_ab_a_a555694c = null;
      this.f_ab_b_a555694c = null;
      this.f_ab_a_aa0ede5b = null;
      System.gc();
   }
}
