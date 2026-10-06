package recovered;

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
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import jimm.Jimm;

public final class C_ab implements Runnable, CommandListener, ItemStateListener, C_t {
   private int f_ab_a_49;
   private Form f_ab_a_67f46df9;
   private InputStream f_ab_a_91ffb459;
   private int f_ab_b_49;
   private String f_ab_a_523beb0a;
   private TextField f_ab_a_a555694c;
   private TextField f_ab_b_a555694c;
   private ChoiceGroup f_ab_a_aa0ede5b;
   private Alert f_ab_a_8de1009d;
   private C_aw f_ab_a_2406f9;
   private String f_ab_b_523beb0a;
   private String f_ab_c_523beb0a;
   private Command f_ab_a_1570d10e = new Command(C_bs.m_a_e96ea081("9"), 2, 2);
   private Command f_ab_b_1570d10e = new Command(C_bs.m_a_e96ea081("b"), 4, 1);

   public C_ab(C_aw var1) {
      this.f_ab_a_2406f9 = var1;
   }

   public final C_aw m_a_46a7a37a() {
      return this.f_ab_a_2406f9;
   }

   public final void m_a_9b75() {
      try {
         C_ak.m_a_48a670f8(this);
         C_ak.m_a_1385ff(false);
         C_ak.m_a_9b75();
      } catch (C_aq var1) {
         C_aq.m_a_481c933f(var1);
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
                        C_cn.m_b_13462e(100 * (var1.f_ab_b_49 - var6) / var1.f_ab_b_49);
                        C_cn.m_a_aad3b1ff(C_bs.m_a_e96ea081("N2"));
                     }
                  } while (var6 > 0);

                  C_as.m_b_13462e(var1.f_ab_b_49);
                  StringBuffer var19;
                  (var19 = new StringBuffer()).append("\r\n--").append(var2).append("--\r\n");
                  var3.write(C_cf.m_a_44c4d6c8(var19.toString(), true));
                  var3.flush();
                  int var20;
                  if ((var20 = var4.getResponseCode()) != 200) {
                     throw new C_aq(194, var20);
                  }

                  InputStream var12 = var4.openInputStream();
                  var5 = new StringBuffer();

                  while ((var6 = var12.read()) != -1) {
                     var5.append((char)(var6 & 0xFF));
                  }

                  String var17;
                  if ((var17 = var5.toString()).indexOf("http://") == -1) {
                     throw new C_aq(195, 0);
                  }

                  String var18 = C_cf.m_a_def8391f(C_cf.m_a_def8391f(var17, "\r", "", false), "\n", "", false);
                  var3.close();
                  var12.close();
                  var4.close();
                  StringBuffer var13;
                  (var13 = new StringBuffer()).append("Filesize: ").append(var1.f_ab_b_49 / 1024).append("KB").append("\n");
                  var13.append("Link: ").append(var18);
                  C_bf.m_a_122835b8(var13.toString(), var1.f_ab_a_2406f9);
               } catch (IOException var8) {
                  throw new C_aq(196, 0);
               }
            } catch (C_aq var9) {
               this.f_ab_a_523beb0a = var9.getMessage();
            }

            this.f_ab_a_49 = 10002;
            Jimm.f_jimm_Jimm_a_4a58c677.callSerially(this);
            return;
         case 10002:
            this.p_ab_b_9b75();
            if (this.f_ab_a_523beb0a != null) {
               this.f_ab_a_8de1009d = new Alert(C_bs.m_a_e96ea081("Error"), this.f_ab_a_523beb0a, null, AlertType.ERROR);
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
         C_at var7;
         (var7 = C_at.m_a_46a7a31d()).m_a_aad3b1ff(var1);
         var2 = var7.m_a_b52a89f8();
         Object var4 = null;
         if (this.f_ab_a_2406f9 == null) {
            C_bp.m_a_4f708078(34, var1);
            C_bp.f_bp_a_24088c.m_a_9b75();
            C_bd.m_a_b329a056((InputStream)var2, false);
         } else {
            var3 = (int)var7.m_a_9b69();
            this.f_ab_a_91ffb459 = (InputStream)var2;
            this.f_ab_b_49 = var3;
            var4 = "";
            this.f_ab_a_67f46df9 = new Form(C_bs.m_a_e96ea081("Z1"));
            this.f_ab_a_a555694c = new TextField(C_bs.m_a_e96ea081("t2"), var1, 255, 0);
            this.f_ab_b_a555694c = new TextField(C_bs.m_a_e96ea081("X1"), (String)var4, 255, 0);
            this.f_ab_a_67f46df9.append(this.f_ab_a_a555694c);
            this.f_ab_a_67f46df9.append(this.f_ab_b_a555694c);
            this.f_ab_a_67f46df9.append(new StringItem(C_bs.m_a_e96ea081("n5") + ": ", this.f_ab_b_49 / 1024 + " kb"));
            this.f_ab_a_67f46df9
               .append(
                  new StringItem(
                     C_bs.m_a_e96ea081("G1") + ": ",
                     C_as.m_b_47921032((this.f_ab_b_49 / C_bp.m_a_134621(72) + 1) * C_bp.m_a_134621(70)) + " " + C_bp.m_a_47921032(6)
                  )
               );
            this.f_ab_a_aa0ede5b = new ChoiceGroup(null, 2);
            this.f_ab_a_aa0ede5b.append(C_bs.m_a_e96ea081("P2"), null);
            this.f_ab_a_aa0ede5b.setSelectedIndex(0, C_bp.m_a_134621(97) == 0);
            this.f_ab_a_67f46df9.append(this.f_ab_a_aa0ede5b);
            this.f_ab_a_67f46df9.addCommand(this.f_ab_a_1570d10e);
            this.f_ab_a_67f46df9.addCommand(this.f_ab_b_1570d10e);
            this.f_ab_a_67f46df9.setCommandListener(this);
            this.f_ab_a_67f46df9.setItemStateListener(this);
            Jimm.f_jimm_Jimm_a_4a58c677.setCurrent(this.f_ab_a_67f46df9);
         }
      } catch (Exception var5) {
         C_aq.m_a_481c933f(new C_aq(191, 0, true));
      }
   }

   public final void m_b_aad3b1ff(String var1) {
   }

   public final void itemStateChanged(Item var1) {
      if (var1 == this.f_ab_a_aa0ede5b) {
         if (this.f_ab_a_aa0ede5b.isSelected(0)) {
            C_bp.m_a_255f295(97, 0);
         } else {
            C_bp.m_a_255f295(97, 1);
         }

         C_bp.m_c_9b75();
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (this.f_ab_a_8de1009d != null && var2 == this.f_ab_a_8de1009d) {
         this.f_ab_a_2406f9.m_e_9b75();
      } else {
         if (var1 == this.f_ab_b_1570d10e) {
            if (var2 == this.f_ab_a_67f46df9) {
               switch (C_bp.m_a_134621(97)) {
                  case 0:
                     C_cn.m_b_13462e(0);
                     C_cn.m_a_aad3b1ff(C_bs.m_a_e96ea081("c3"));
                     C_cn.m_b_8eb9d703(C_cn.f_cn_a_1570d10e);
                     C_cn.m_a_6f63a2af(this);
                     C_cn.m_a_9b75();
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
                     C_cn.m_b_13462e(0);
                     C_cn.m_a_aad3b1ff(C_bs.m_a_e96ea081("c3"));
                     C_cn.m_a_8eb9d703(C_cn.f_cn_a_1570d10e);
                     C_cn.m_a_6f63a2af(this);
                     C_cn.m_a_9b75();
                     C_bg var4 = new C_bg(C_bp.m_a_47921032(254), this.f_ab_a_2406f9, var6, var8, this.f_ab_a_91ffb459, this.f_ab_b_49);
                     C_bu var5 = new C_bu(var4);

                     try {
                        C_ac.m_a_cb38d14b(var5);
                        return;
                     } catch (C_aq var3) {
                        C_aq.m_a_481c933f(var3);
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

            if (var1 == C_cn.f_cn_a_1570d10e) {
               this.p_ab_b_9b75();
               C_v.m_a_9b75();
            }
         }
      }
   }

   private void p_ab_b_9b75() {
      this.f_ab_a_91ffb459 = null;
      this.f_ab_a_67f46df9 = null;
      this.f_ab_a_a555694c = null;
      this.f_ab_b_a555694c = null;
      this.f_ab_a_aa0ede5b = null;
      System.gc();
   }
}
