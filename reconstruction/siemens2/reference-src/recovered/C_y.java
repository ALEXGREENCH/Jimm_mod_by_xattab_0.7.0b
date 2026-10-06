package recovered;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Vector;

public final class C_y extends C_az {
   private Hashtable f_y_a_d18d4967 = new Hashtable();
   private Hashtable f_y_b_d18d4967 = new Hashtable();
   private Hashtable f_y_c_d18d4967 = new Hashtable();
   private boolean f_y_b_5a = false;
   private static byte[] f_y_a_b47 = C_ce.m_a_529fb918("0,0,0,0,0,0B,1F,40,3,E7,3,E7,0,0,0,0", ',', 16);
   private static byte[] f_y_b_b47 = C_ce.m_a_529fb918(
      "00,22,00,01,01,10,16,4f,00,01,00,04,01,10,16,4f,00,13,00,04,01,10,16,4f,00,02,00,01,01,10,16,4f,00,03,00,01,01,10,16,4f,00,15,00,01,01,10,16,4f,00,04,00,01,01,10,16,4f,00,06,00,01,01,10,16,4f,00,09,00,01,01,10,16,4f,00,0a,00,01,01,10,16,4f,00,0b,00,01,01,10,16,4f",
      ',',
      16
   );
   private static short[] f_y_a_b58 = new short[]{34, 1, 1, 4, 19, 4, 2, 1, 3, 1, 21, 1, 4, 1, 6, 1, 9, 1, 10, 1, 11, 1};
   private int f_y_a_49 = 30000;
   private String f_y_a_523beb0a;
   private String f_y_b_523beb0a;
   private String f_y_c_523beb0a;
   private String f_y_d_523beb0a;
   private int f_y_b_49;
   private long f_y_a_4a = 0L;
   private boolean f_y_c_5a;
   private String f_y_e_523beb0a;
   private byte[] f_y_c_b47;
   private boolean f_y_d_5a;
   private C_bz f_y_a_240b17;

   public C_y(String var1, String var2, String var3, String var4) {
      super(true, false);
      this.f_y_a_523beb0a = var1;
      this.f_y_b_523beb0a = var2;
      this.f_y_c_523beb0a = var3;
      this.f_y_d_523beb0a = var4;
   }

   private void p_y_a_aad3b1ff(String var1) {
      int var2 = 0;

      try {
         var2 = (var2 = Integer.parseInt(C_bp.m_a_47921032(10))) > 0 ? var2 : 1;
      } catch (NumberFormatException var5) {
         var2 = 1;
      }

      int var3 = 0;

      while (var3 < var2) {
         this.f_y_a_4a = System.currentTimeMillis();
         if (!this.f_y_b_5a && this.f_y_a_240b17 == C_ac.f_ac_a_240b17) {
            try {
               this.f_y_a_240b17.m_a_aad3b1ff(var1);
               return;
            } catch (C_aq var6) {
               this.f_y_a_240b17.m_a_9b75();
               if (var3 < var2 - 1 && this.f_y_a_4a + this.f_y_a_49 >= System.currentTimeMillis()) {
                  var3++;
                  continue;
               }

               this.f_y_b_49 = -1;
               throw var6;
            }
         }

         return;
      }
   }

   protected final void m_a_9b75() {
      this.f_y_b_49 = 0;
      this.f_y_a_4a = System.currentTimeMillis();
      if (this.f_y_a_523beb0a.length() != 0 && this.f_y_b_523beb0a.length() != 0) {
         this.f_y_a_240b17 = C_ac.f_ac_a_240b17;
         this.p_y_a_aad3b1ff(this.f_y_c_523beb0a + ":" + this.f_y_d_523beb0a);
         this.f_y_b_49 = 1;
         this.f_y_a_4a = System.currentTimeMillis();
      } else {
         this.f_y_b_49 = -1;
         throw new C_aq(117, 0);
      }
   }

   protected final boolean m_a_cb4a1769(C_cb var1) {
      if (!this.f_y_b_5a && this.f_y_a_240b17 == C_ac.f_ac_a_240b17) {
         this.f_y_c_5a = true;

         try {
            int var2 = 0;
            if (this.f_y_b_49 == 1) {
               if (var1 instanceof C_j) {
                  C_j var108 = (C_j)var1;
                  Object var56 = null;
                  if (var108.m_a_9b68() == 1) {
                     if (C_bp.m_a_134632(144)) {
                        this.f_y_a_240b17.m_a_cb4a1765(new C_j());
                        byte[] var33;
                        C_ce.m_b_e306985c(var33 = new byte[4 + this.f_y_a_523beb0a.length()], 0, 1);
                        C_ce.m_b_e306985c(var33, 2, this.f_y_a_523beb0a.length());
                        byte[] var69;
                        System.arraycopy(var69 = C_ce.m_a_afa28ebe(this.f_y_a_523beb0a), 0, var33, 4, var69.length);
                        this.f_y_a_240b17.m_a_cb4a1765(new C_bu(23, 6, 0L, new byte[0], var33));
                     } else {
                        C_j var34 = new C_j(this.f_y_a_523beb0a, this.f_y_b_523beb0a);
                        this.f_y_a_240b17.m_a_cb4a1765(var34);
                     }

                     this.f_y_b_49 = !C_bp.m_a_134632(144) ? 3 : 2;
                     var2 = 1;
                  }
               }
            } else if (this.f_y_b_49 == 2) {
               if (var1 instanceof C_bu) {
                  C_bu var52;
                  if ((var52 = (C_bu)var1).m_b_9b68() != 23 || var52.m_c_9b68() != 7) {
                     throw new C_aq(100, 0);
                  }

                  byte[] var32;
                  int var68;
                  byte[] var78 = new byte[var68 = C_ce.m_b_49634b7a(var32 = var52.m_b_12d408(), 0)];
                  System.arraycopy(var32, 2, var78, 0, var68);
                  byte[] var63 = new byte[4 + this.f_y_a_523beb0a.length() + 2 + 2 + 16];
                  int var88 = 0;
                  C_ce.m_b_e306985c(var63, 0, 1);
                  C_ce.m_b_e306985c(var63, 2, this.f_y_a_523beb0a.length());
                  byte[] var96;
                  System.arraycopy(var96 = C_ce.m_a_afa28ebe(this.f_y_a_523beb0a), 0, var63, 4, var96.length);
                  var88 = 4 + var96.length;
                  C_ce.m_b_e306985c(var63, var88, 37);
                  var88 += 2;
                  C_ce.m_b_e306985c(var63, var88, 16);
                  var88 += 2;
                  byte[] var37 = new byte[var78.length + this.f_y_b_523beb0a.length() + C_ce.f_ce_y_b47.length];
                  boolean var53 = false;
                  System.arraycopy(var78, 0, var37, 0, var78.length);
                  int var54 = 0 + var78.length;
                  byte[] var80;
                  System.arraycopy(var80 = C_ce.m_a_afa28ebe(this.f_y_b_523beb0a), 0, var37, var54, var80.length);
                  int var55 = var54 + var80.length;
                  System.arraycopy(C_ce.f_ce_y_b47, 0, var37, var55, C_ce.f_ce_y_b47.length);
                  byte[] var107 = C_ce.m_b_4962d961(var37);
                  Object var99 = null;
                  System.arraycopy(var107, 0, var63, var88, 16);
                  this.f_y_a_240b17.m_a_cb4a1765(new C_bu(23, 2, 0L, new byte[0], var63));
                  this.f_y_b_49 = 3;
               }

               var2 = 1;
            } else if (this.f_y_b_49 == 3) {
               int var51;
               label314: {
                  var51 = -1;
                  if (C_bp.m_a_134632(144)) {
                     if (var1 instanceof C_bu) {
                        C_bu var29;
                        if ((var29 = (C_bu)var1).m_b_9b68() != 23 || var29.m_c_9b68() != 3) {
                           break label314;
                        }

                        byte[] var67 = var29.m_b_12d408();
                        int var77 = 0;

                        while (true) {
                           if (var77 >= var67.length) {
                              break label314;
                           }

                           byte[] var61 = C_ce.m_a_e3062636(var67, var77);
                           int var87 = C_ce.m_b_49634b7a(var67, var77);
                           var77 += 4 + var61.length;
                           switch (var87) {
                              case 5:
                                 this.f_y_e_523beb0a = C_ce.m_a_79834524(var61);
                                 break;
                              case 6:
                                 this.f_y_c_b47 = var61;
                              case 7:
                              default:
                                 break;
                              case 8:
                                 var51 = C_ce.m_b_49634b7a(var61, 0);
                           }
                        }
                     }

                     if (!(var1 instanceof C_ai)) {
                        break label314;
                     }
                  } else {
                     if (!(var1 instanceof C_ai)) {
                        break label314;
                     }

                     C_ai var30;
                     if ((var30 = (C_ai)var1).m_a_9b68() == 1) {
                        byte[] var10001;
                        if (var30.m_a_9b68() == 1) {
                           byte[] var62 = new byte[var30.f_ai_a_b47.length];
                           System.arraycopy(var30.f_ai_a_b47, 0, var62, 0, var30.f_ai_a_b47.length);
                           var10001 = var62;
                        } else {
                           var10001 = null;
                        }

                        this.f_y_c_b47 = var10001;
                        this.f_y_e_523beb0a = var30.m_a_9b68() == 1 ? var30.f_ai_b_523beb0a : null;
                     } else if (var30.m_a_9b68() == 2) {
                        var51 = var30.m_b_9b68();
                     }
                  }

                  var2 = 1;
               }

               if (var51 != -1) {
                  byte var31 = 100;
                  switch (var51) {
                     case 1:
                        var31 = 110;
                     case 2:
                     case 3:
                     case 6:
                     case 9:
                     case 10:
                     case 11:
                     case 12:
                     case 13:
                     case 14:
                     case 15:
                     case 16:
                     case 17:
                     case 18:
                     case 19:
                     case 20:
                     case 23:
                     case 25:
                     case 26:
                     case 27:
                     case 28:
                     default:
                        break;
                     case 4:
                     case 5:
                        var31 = 111;
                        break;
                     case 7:
                     case 8:
                        var31 = 112;
                        break;
                     case 21:
                     case 22:
                        var31 = 113;
                        break;
                     case 24:
                     case 29:
                        var31 = 114;
                  }

                  throw new C_aq(var31, var51);
               }

               if (var2 & this.f_y_e_523beb0a != null & this.f_y_c_b47 != null) {
                  if (!(this.f_y_a_240b17 instanceof C_i)) {
                     this.f_y_a_240b17.m_a_9b75();
                  }

                  this.p_y_a_aad3b1ff(this.f_y_e_523beb0a);
                  this.f_y_b_49 = 4;
               }
            } else if (this.f_y_b_49 == 4) {
               if (var1 instanceof C_j) {
                  C_j var106 = (C_j)var1;
                  Object var50 = null;
                  if (var106.m_a_9b68() == 1) {
                     C_j var28 = new C_j(this.f_y_c_b47);
                     this.f_y_a_240b17.m_a_cb4a1765(var28);
                     this.f_y_b_49 = 5;
                     var2 = 1;
                  }
               }
            } else if (this.f_y_b_49 == 5) {
               ByteArrayOutputStream var49 = new ByteArrayOutputStream();

               for (int var27 = 0; var27 < f_y_a_b58.length; var27++) {
                  C_ce.m_a_559c4327(var49, f_y_a_b58[var27], true);
               }

               this.f_y_a_240b17.m_a_cb4a1765(new C_bu(1, 23, 0L, new byte[0], var49.toByteArray()));
               this.f_y_b_49 = 6;
            } else if (this.f_y_b_49 == 6) {
               C_bu var43 = new C_bu(1, 14, 14L, new byte[0], new byte[0]);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               byte[] var25;
               C_ce.m_a_e3069c1d(var25 = new byte[6], 0, 720898L);
               C_ce.m_b_e306985c(var25, 4, 15);
               var43 = new C_bu(19, 2, 2L, new byte[0], var25);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               var43 = new C_bu(2, 2, 2L, new byte[0], new byte[0]);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               byte[] var26;
               C_ce.m_a_e3069c1d(var26 = new byte[6], 0, 327682L);
               C_ce.m_b_e306985c(var26, 4, 3);
               var43 = new C_bu(3, 2, 2L, new byte[0], var26);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               var43 = new C_bu(4, 4, 4L, new byte[0], new byte[0]);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               var43 = new C_bu(9, 2, 2L, new byte[0], new byte[0]);
               this.f_y_a_240b17.m_a_cb4a1765(var43);
               long var66 = C_w.m_a_9b68();
               int var60 = C_w.m_b_9b68();
               if ((var66 != -1L || var60 != -1) && C_w.m_c_9b68() != 0) {
                  byte[] var86;
                  C_ce.m_a_e3069c1d(var86 = new byte[6], 0, var66);
                  C_ce.m_b_e306985c(var86, 4, var60);
                  C_bu var95 = new C_bu(19, 5, 0L, new byte[0], var86);
                  this.f_y_a_240b17.m_a_cb4a1765(var95);
               } else {
                  C_bu var85 = new C_bu(19, 4, 0L, new byte[0], new byte[0]);
                  this.f_y_a_240b17.m_a_cb4a1765(var85);
               }

               this.f_y_b_49 = 7;
            } else if (this.f_y_b_49 == 7) {
               if (var1 instanceof C_bu) {
                  C_bu var41;
                  if ((var41 = (C_bu)var1).m_b_9b68() == 19 && var41.m_c_9b68() == 15) {
                     this.f_y_d_5a = true;
                     var2 = 1;
                  } else if (var41.m_b_9b68() == 19 && var41.m_c_9b68() == 6) {
                     if (var41.f_bu_d_49 != 1) {
                        this.f_y_d_5a = true;
                     }

                     Vector var22 = new Vector();
                     byte[] var5 = var41.m_b_12d408();
                     int var6 = 0;
                     if (var5.length < 3) {
                        throw new C_aq(115, 0);
                     }

                     var6++;
                     int var57 = C_ce.m_b_49634b7a(var5, 1);
                     int var71 = var6 + 2;

                     for (int var81 = 0; var81 < var57; var81++) {
                        if (var5.length < var71 + 2) {
                           throw new C_aq(115, 1);
                        }

                        int var9 = C_ce.m_b_49634b7a(var5, var71);
                        var6 = var71 + 2;
                        if (var5.length < var6 + var9 + 2 + 2 + 2 + 2) {
                           throw new C_aq(115, 2);
                        }

                        String var36 = C_ce.m_a_20e7da8(var5, var6, var9, C_ce.m_a_e3069860(var5, var6, var9));
                        var6 += var9;
                        int var42 = C_ce.m_b_49634b7a(var5, var6);
                        int var79 = C_ce.m_b_49634b7a(var5, var6 + 2);
                        int var98 = C_ce.m_b_49634b7a(var5, var6 + 4);
                        var6 += 6;
                        int var100 = C_ce.m_b_49634b7a(var5, var6);
                        var71 = var6 + 2;
                        if (var5.length < var71 + var100) {
                           throw new C_aq(115, 3);
                        }

                        if (var98 == 0 || (var98 == 25 || var98 == 27) && C_bp.m_a_134632(169)) {
                           ByteArrayOutputStream var104 = new ByteArrayOutputStream();
                           String var92 = new String(var36);
                           boolean var13 = false;

                           while (var100 > 0) {
                              byte[] var14;
                              if ((var14 = C_ce.m_a_e3062636(var5, var71)) == null) {
                                 throw new C_aq(115, 4);
                              }

                              int var15;
                              if ((var15 = C_ce.m_b_49634b7a(var5, var71)) == 305) {
                                 var92 = C_ce.m_a_5a238448(var14, true);
                              } else if (var15 == 102) {
                                 var13 = true;
                              } else if (var15 == 109 || var15 == 348 || var15 == 349) {
                                 C_ce.m_a_559c4327(var104, var15, true);
                                 C_ce.m_a_559c4327(var104, var14.length, true);
                                 C_ce.m_a_55a417bd(var104, var14);
                              }

                              var100 -= 4;
                              var100 -= var14.length;
                              var71 += 4 + var14.length;
                           }

                           if (var100 != 0) {
                              throw new C_aq(115, 5);
                           }

                           try {
                              C_aw var105;
                              (var105 = new C_aw(var79, var42, var36, var92, var13, true)).m_a_4870e775(227, var104.size() != 0 ? var104.toByteArray() : null);
                              var22.addElement(var105);
                              if (var98 == 25 || var98 == 27) {
                                 var105.m_a_2563266(32, true);
                              }
                           } catch (NumberFormatException var16) {
                           } catch (Exception var17) {
                           }
                        } else if (var98 == 1) {
                           var71 += var100;
                           if (var42 != 0) {
                              var22.addElement(new C_l(var42, var36));
                           }
                        } else if (var98 == 2) {
                           var71 += var100;
                           this.f_y_c_d18d4967.put(var36, new Integer(var79));
                        } else if (var98 == 3) {
                           var71 += var100;
                           this.f_y_b_d18d4967.put(var36, new Integer(var79));
                        } else if (var98 == 14) {
                           var71 += var100;
                           this.f_y_a_d18d4967.put(var36, new Integer(var79));
                        } else if (var98 != 4) {
                           var71 += var100;
                        } else {
                           while (var100 > 0) {
                              byte[] var103;
                              if ((var103 = C_ce.m_a_e3062636(var5, var71)) == null) {
                                 throw new C_aq(115, 110);
                              }

                              if (C_ce.m_b_49634b7a(var5, var71) == 202) {
                                 C_ac.m_a_46a7a10e().m_c_13462e(var79);
                              }

                              var100 -= 4;
                              var100 -= var103.length;
                              var71 += 4 + var103.length;
                           }

                           if (var100 != 0) {
                              throw new C_aq(115, 111);
                           }
                        }
                     }

                     if (var5.length != var71 + 4) {
                        throw new C_aq(115, 6);
                     }

                     int var82 = (int)C_ce.m_a_49634b7b(var5, var71);
                     C_an[] var93 = new C_an[var22.size()];
                     var22.copyInto(var93);
                     C_w.m_a_67444a82(var82, var57, var93);
                     var2 = 1;
                  }

                  if (this.f_y_d_5a) {
                     for (int var23 = 0; var23 < C_w.m_c_9b68(); var23++) {
                        C_aw var64;
                        String var75 = (var64 = C_w.m_a_c2f0d221(var23)).m_b_73cf11cb();
                        Integer var58 = (Integer)this.f_y_b_d18d4967.get(var75);
                        var64.m_f_13462e(var58 != null ? var58 : 0);
                        Integer var83 = (Integer)this.f_y_c_d18d4967.get(var75);
                        var64.m_e_13462e(var83 != null ? var83 : 0);
                        Integer var94 = (Integer)this.f_y_a_d18d4967.get(var75);
                        var64.m_d_13462e(var94 != null ? var94 : 0);
                     }

                     C_bu var24 = new C_bu(19, 7, 7L, new byte[0], new byte[0]);
                     this.f_y_a_240b17.m_a_cb4a1765(var24);
                     C_al.m_a_9b75();
                     C_bu var76;
                     if (C_bp.m_a_134621(88) > 0) {
                        var76 = new C_bu(4, 2, 0L, new byte[0], f_y_a_b47);
                     } else {
                        byte[] var65 = f_y_a_b47;
                        f_y_a_b47[5] = 3;
                        var76 = new C_bu(4, 2, 0L, new byte[0], var65);
                     }

                     this.f_y_a_240b17.m_a_cb4a1765(var76);
                     C_ac.m_c_9b75();
                     int var59 = (int)C_bp.a$134622();
                     if (C_bo.m_b_2477940() == null) {
                        byte var84 = 4;
                        if (var59 == 512) {
                           var84 = 2;
                        }

                        if (var59 == 256) {
                           var84 = 3;
                        }

                        C_ac.m_a_132be7(var84);
                     } else {
                        C_ac.m_a_132be7((byte)C_bp.m_a_134621(110));
                     }

                     C_al.m_a_13462e(C_ac.m_d_9b68() | var59);
                     this.f_y_b_49 = 8;
                  }
               }
            } else if (this.f_y_b_49 == 8) {
               C_bu var3 = new C_bu(1, 2, 0L, new byte[0], f_y_b_b47);
               this.f_y_a_240b17.m_a_cb4a1765(var3);
               C_ar var19 = new C_ar(this.f_y_a_523beb0a, 60, new byte[0], new byte[0]);
               this.f_y_a_240b17.m_a_cb4a1765(var19);
               this.f_y_b_49 = 9;
            } else if (this.f_y_b_49 == 9) {
               C_bu var38;
               if (var1 instanceof C_bu && (var38 = (C_bu)var1).m_b_9b68() == 21 && var38.m_c_9b68() == 1) {
                  this.f_y_b_49 = 10;
                  var2 = 1;
               }

               C_br var39;
               if (var1 instanceof C_br && !var2 && (var39 = (C_br)var1).m_b_9b68() == 21) {
                  if (var39.f_br_e_49 == 65) {
                     byte[] var20;
                     if ((var20 = var39.m_b_12d408()).length < 14) {
                        return false;
                     }

                     String var4 = String.valueOf(C_ce.m_a_e306d821(var20, 0, false));
                     long var8 = C_ce.m_a_6046c8c9(
                        C_ce.m_a_e306d820(var20, 4, false),
                        C_ce.m_a_49634b7a(var20, 6),
                        C_ce.m_a_49634b7a(var20, 7),
                        C_ce.m_a_49634b7a(var20, 8),
                        C_ce.m_a_49634b7a(var20, 9),
                        0
                     );
                     var2 = C_ce.m_a_e306d820(var20, 10, false);
                     int var40 = C_ce.m_a_e306d820(var20, 12, false);
                     String var7 = null;
                     if (var20.length >= var40 + 14) {
                        var7 = C_ce.m_a_e96ea081(C_ce.m_a_20e7da8(var20, 14, var40, C_ce.m_a_e3069860(var20, 14, var40)));
                     }

                     if (var7 != null) {
                        if (var2 == 1) {
                           C_d var10000 = new C_d(var4, var4, C_ce.m_a_1349e3(var8), var7, true);
                           Object var10 = null;
                           C_v.m_a_cb40fff9(var10000);
                        } else if (var2 == 4) {
                           String var11;
                           String var12;
                           int var97;
                           if ((var97 = var7.indexOf(254)) != -1) {
                              var11 = var7.substring(0, var97);
                              var12 = var7.substring(var97 + 1);
                           } else {
                              var11 = var7;
                              var12 = "";
                           }

                           C_v.m_a_cb40fff9(new C_ae(var4, var4, C_ce.m_a_1349e3(var8), var12, var11));
                        }
                     }

                     var2 = 1;
                  } else if (var39.f_br_e_49 == 66) {
                     C_ar var21 = new C_ar(this.f_y_a_523beb0a, 62, new byte[0], new byte[0]);
                     this.f_y_a_240b17.m_a_cb4a1765(var21);
                     this.f_y_b_49 = 10;
                     var2 = 1;
                  }
               }
            }

            this.f_y_a_4a = System.currentTimeMillis();
            this.f_y_c_5a = false;
            return (boolean)var2;
         } catch (C_aq var18) {
            this.f_y_a_4a = System.currentTimeMillis();
            this.f_y_c_5a = false;
            if (var18.f_aq_a_5a) {
               this.f_y_b_49 = -1;
            }

            throw var18;
         }
      } else {
         return false;
      }
   }

   public final boolean m_a_9b79() {
      return this.f_y_b_49 == 10;
   }

   public final boolean m_b_9b79() {
      if (!this.f_y_b_5a && this.f_y_a_240b17 == C_ac.f_ac_a_240b17) {
         if (this.f_y_b_49 != -1 && !this.f_y_c_5a && this.f_y_a_4a + this.f_y_a_49 < System.currentTimeMillis()) {
            C_aq var1;
            if (!C_ac.m_a_cb34b9f8(var1 = new C_aq(118, 0))) {
               C_aq.m_a_481c933f(var1);
            }

            this.f_y_b_49 = -1;
         }

         return this.f_y_b_49 == -1;
      } else {
         return true;
      }
   }

   public final int m_a_9b68() {
      switch (this.f_y_b_49) {
         case 0:
            return 3;
         case 1:
            return 12;
         case 2:
            return 20;
         case 3:
            return 32;
         case 4:
            return 45;
         case 5:
            return 57;
         case 6:
            return 64;
         case 7:
            return 71;
         case 8:
            return 79;
         case 9:
            return 87;
         case 10:
            return 100;
         default:
            return 0;
      }
   }

   public final String m_a_73cf11cb() {
      switch (this.f_y_b_49) {
         case 2:
            return "Sending auth key";
         case 3:
            return "Sending client ID";
         case 4:
            return "Changing server";
         case 5:
            return "Sending cookies";
         case 6:
         case 7:
            return "Checking roster";
         case 8:
            return "Sending statuses";
         case 9:
            return "Reading messages";
         case 10:
            return "Ready to chat!";
         default:
            return this.f_y_c_523beb0a;
      }
   }

   public final void m_a_13462e(int var1) {
      switch (var1) {
         case 1:
            C_w.m_a_9b75();
            C_w.m_h_9b75();
            if (C_bp.m_a_134632(153)) {
               C_ck.m_a_9b75();
               return;
            }
            break;
         case 2:
            C_ac.f_ac_a_5a = false;
            this.f_y_b_5a = true;
            C_ac.m_b_9b75();
            C_v.m_a_d3ad8a43(11, null);
      }
   }
}
