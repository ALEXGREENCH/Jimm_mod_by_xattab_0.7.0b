package recovered;

import java.util.Vector;
import jimm.Jimm;

public final class C_bj {
   private static Vector f_bj_a_48a69a2c = new Vector();
   private static Vector f_bj_b_48a69a2c = new Vector();
   private static Vector f_bj_c_48a69a2c = new Vector();

   protected static void m_a_cb4a8bc4(C_cc var0) {
      if (var0 instanceof C_ai) {
         if (((C_ai)var0).m_b_9b68() == 1) {
            C_ac.f_ac_a_5a = false;
            throw new C_aq(110, 0);
         } else {
            throw new C_aq(100, 0);
         }
      } else {
         if (var0 instanceof C_bv) {
            C_bv var45;
            if ((var45 = (C_bv)var0).m_b_9b68() == 4 && var45.m_c_9b68() == 20 && C_bp.m_a_134621(88) > 0) {
               byte[] var1;
               int var3 = C_cf.m_a_49634b7a(var1 = var45.m_b_12d408(), 10);
               String var4 = C_cf.m_a_55a39fc4(var1, 11, var3);
               int var10000 = C_cf.m_b_49634b7a(var1, var3 + 11);
               boolean var5 = false;
               if (var10000 == 2) {
                  C_u.m_a_afa340b5(var4, true);
               } else {
                  C_u.m_a_afa340b5(var4, false);
               }
            }

            if (var45.m_b_9b68() == 4 && var45.m_c_9b68() == 11) {
               byte[] var51;
               int var73 = C_cf.m_a_49634b7a(var51 = var45.m_b_12d408(), 10);
               String var86 = C_cf.m_a_55a39fc4(var51, 11, var73);
               long var100 = (C_cf.m_a_49634b7b(var51, 0) << 32) + C_cf.m_a_49634b7b(var51, 4);
               C_ap.m_a_44be42cf(var86, var100, true);
               int var7 = var73 + 11;
               int var115 = var7 + 47;
               if (var115 < var51.length) {
                  int var2 = C_cf.m_a_e306d820(var51, var115, false);
                  C_aw var8 = C_v.m_a_513388b0(var86);
                  if (var2 >= 1000 && var2 <= 1004 && var8.f_aw_d_5a) {
                     int var9 = C_cf.m_a_e306d820(var51, var73 + 64, false);
                     String var10 = C_cf.m_a_20e7da8(var51, var73 + 66, var9, true);
                     if (!var8.m_a_134632(16)) {
                        C_ap.m_a_b7252e78(var8, var8.f_aw_a_523beb0a);
                     }

                     C_ap.m_a_dc197d0(var86, "", var10, "", 0L, true, false, C_v.f_v_a_12a1f.m_a_485a59b9(var8.m_a_9b68()), 0L);
                     C_v.f_v_a_5a = false;
                     if (!C_cn.m_a_9b79() && C_v.f_v_a_12984.m_b_9b79()) {
                        var8.m_e_9b75();
                     }

                     var8.f_aw_d_5a = false;
                  }

                  int var132 = C_cf.m_a_e306d820(var51, var115, false);
                  var7 = var115 + 6;
                  if (var132 == 26) {
                     var7 += 3;
                     int var137 = C_cf.m_a_e306d820(var51, var7, false);
                     int var118 = var7 + 2;
                     if (var137 != 79) {
                        throw new C_aq(509, 1, false);
                     }

                     var7 = var118 + 18;
                     long var12 = C_cf.m_a_e306d821(var51, var7, false);
                     var7 += 4;
                     String var11 = C_cf.m_a_55a39fc4(var51, var7, (int)var12);
                     var7 = (int)(var7 + var12);
                     int var122 = var7 + 15;
                     if (var11 != null && var11.compareTo("Script Plug-in: Remote Notification Arrive") == 0) {
                        var7 = var122 + 8;
                        int var14 = var51.length - var7;
                        String var15 = C_cf.m_a_20e7da8(var51, var7, var14, true);

                        try {
                           if ((var73 = var15.indexOf("<NR><RES>")) < 0) {
                              throw new Exception("cp #2");
                           }

                           int var33;
                           if ((var33 = var15.indexOf("</RES></NR>")) < 0) {
                              throw new Exception("cp #3");
                           }

                           String var34;
                           if ((var34 = C_cf.m_c_e96ea081(var15.substring(var73 + 9, var33))).indexOf("<val srv_id='") < 0) {
                              throw new Exception("cp #6");
                           }

                           int var36;
                           if ((var36 = var34.indexOf("<title>")) < 0) {
                              throw new Exception("cp #9");
                           }

                           int var87;
                           if ((var87 = var34.indexOf("/title>")) < 0) {
                              throw new Exception("cp #10");
                           }

                           String var101 = var34.substring(var36 + 7, var87 - 1);
                           int var6;
                           if ((var6 = var34.indexOf("<desc>")) < 0) {
                              throw new Exception("cp #11");
                           }

                           if ((var7 = var34.indexOf("</desc>")) < 0) {
                              throw new Exception("cp #12");
                           }

                           String var128 = var34.substring(var6 + 6, var7);
                           C_aw var133;
                           if ((var133 = C_v.m_a_513388b0(var86)) != null && (var101.length() >= 1 || var128.length() >= 1)) {
                              if (!var133.m_a_134632(16)) {
                                 C_ap.m_a_b7252e78(var133, var133.f_aw_a_523beb0a);
                              }

                              C_ap.m_a_dc197d0(var86, var101, var128, "", 0L, true, false, C_w.m_a_485a59b9(var133.m_a_2477b4f().m_b_9b68()), 0L);
                              C_v.f_v_a_5a = false;
                              if (var133.f_aw_c_5a && !C_cn.m_a_9b79() && C_v.f_v_a_12984.m_b_9b79()) {
                                 var133.m_e_9b75();
                              }

                              var133.f_aw_c_5a = false;
                           }
                        } catch (Exception var44) {
                        }
                     }
                  }
               }
            }

            if (var45.m_b_9b68() == 3 && var45.m_c_9b68() == 11 || var45.m_b_9b68() == 2 && var45.m_c_9b68() == 6) {
               byte[] var53 = new byte[4];
               byte[] var75 = new byte[4];
               int var88 = 0;
               int var102 = -1;
               int var108 = 0;
               int var125 = 0;
               boolean var68 = true;
               int var129 = 0;
               int var134 = 0;
               int var138 = 0;
               int var147 = 0;
               byte[] var13 = null;
               byte[] var142 = null;
               int var156 = -1;
               int var160 = -1;
               int var16 = -1;
               int var17 = -1;
               byte[] var18;
               int var19 = C_cf.m_a_49634b7a(var18 = var45.m_b_12d408(), 0);
               String var20 = C_cf.m_a_55a39fc4(var18, 1, var19);
               int var21 = 0;
               int var22 = -1;
               int var23 = -1;
               String var24 = "";
               var19 = var19 + 1 + 2;
               int var25 = C_cf.m_b_49634b7a(var18, var19);
               int var176 = var19 + 2;

               for (int var26 = 0; var26 < var25; var26++) {
                  int var27 = C_cf.m_b_49634b7a(var18, var176);
                  byte[] var30 = C_cf.m_a_e3062636(var18, var176);
                  if (var27 == 6) {
                     var21 = (int)C_cf.m_a_49634b7b(var30, 0);
                  } else if (var27 == 13) {
                     var13 = var30;
                  } else if (var27 == 25) {
                     var142 = var30;
                  } else if (var27 == 10) {
                     System.arraycopy(var30, 0, var75, 0, 4);
                  } else if (var27 == 12) {
                     boolean var241 = false;
                     System.arraycopy(var30, 0, var53, 0, 4);
                     var88 = (int)C_cf.m_a_49634b7b(var30, 4);
                     var102 = C_cf.m_a_49634b7a(var30, 8);
                     var108 = C_cf.m_b_49634b7a(var30, 9);
                     var125 = (int)C_cf.m_a_49634b7b(var30, 11);
                     var129 = (int)C_cf.m_a_49634b7b(var30, 23);
                     var134 = (int)C_cf.m_a_49634b7b(var30, 27);
                     var138 = (int)C_cf.m_a_49634b7b(var30, 31);
                     var68 = false;
                  } else if (var27 == 3) {
                     var16 = (int)C_cf.m_a_1349e3(C_cf.m_a_25e06f0(var30));
                  } else if (var27 == 4) {
                     var156 = (int)C_cf.m_a_25e06f0(var30) / 256;
                  } else if (var27 == 15) {
                     var160 = (int)C_cf.m_a_25e06f0(var30);
                  } else if (var27 == 5) {
                     var17 = (int)C_cf.m_a_25e06f0(var30);
                  } else if (var27 == 29) {
                     int var31 = 0;
                     int var28 = var30.length;

                     try {
                        while (var31 < var28 - 1) {
                           var27 = C_cf.m_b_49634b7a(var30, var31);
                           int var240 = var31 + 3;
                           int var29 = C_cf.m_a_49634b7a(var30, var240);
                           if (var29 > 0) {
                              switch (var27) {
                                 case 2:
                                    int var32 = var240 + 1;
                                    var147 = C_cf.m_b_49634b7a(var30, var32);
                                    var32 += 2;
                                    var24 = C_cf.m_a_20e7da8(var30, var32, var147, true);
                                    break;
                                 case 14:
                                    var23 = Integer.parseInt(C_cf.m_a_55a39fc4(var30, var240 + 8, var29 - 7));
                              }
                           }

                           var31 = var240 + var29 + 1;
                        }
                     } catch (Exception var43) {
                     }
                  }

                  var176 += 4 + var30.length;
               }

               C_aw var207;
               if ((var207 = C_v.m_a_513388b0(var20)) != null) {
                  var22 = var207.m_a_2477b4f().m_b_9b68();
                  byte[] var213 = C_cf.m_a_7ec6ec7a(var13, var142);
                  var207.m_a_25e06fc(var213);
                  var207.m_c_13462e(var21 >> 16 & 65535);
                  boolean var235;
                  if (var235 = var23 != -1 && var23 < 37) {
                     var207.m_a_2477b4f().m_a_13462e(var23);
                  }

                  if (var24 != null) {
                     if (var207.m_a_134632(16) && var207.m_b_134621(192) != -1 && var24.length() > 1 && !var24.equals(var207.f_aw_b_523beb0a)) {
                        C_f var242 = var235 ? C_w.m_a_485a59b9(var23) : C_v.f_v_a_12a1f.m_a_485a59b9(var207.m_a_9b68());
                        C_ap.m_a_dc197d0(var20, "", var24, "", 0L, true, false, var242, 0L);
                     }

                     var207.f_aw_b_523beb0a = var24;
                  }

                  var207.m_f_9b75();
               }

               C_cf.m_a_31f1f57c(var20, var129, var134, var138, C_cf.m_a_7ec6ec7a(var13, var142), var108, var68);
               Object[] var139;
               (var139 = new Object[13])[0] = var20;
               C_u.m_a_7ac2f2fc(var139, 1, var21);
               C_u.m_a_7ac2f2fc(var139, 2, var22);
               var139[3] = var53;
               var139[4] = var75;
               C_u.m_a_7ac2f2fc(var139, 5, var88);
               C_u.m_a_7ac2f2fc(var139, 6, var102);
               C_u.m_a_7ac2f2fc(var139, 7, var108);
               C_u.m_a_7ac2f2fc(var139, 8, var125);
               C_u.m_a_7ac2f2fc(var139, 9, var16);
               C_u.m_a_7ac2f2fc(var139, 10, var160);
               C_u.m_a_7ac2f2fc(var139, 11, var156);
               C_u.m_a_7ac2f2fc(var139, 12, var17);
               C_u.m_a_d3ad8a43(5, var139);
            }

            if (var45.m_b_9b68() == 3 && var45.m_c_9b68() == 12) {
               byte[] var67;
               int var85 = C_cf.m_a_49634b7a(var67 = var45.m_b_12d408(), 0);
               String var99 = C_cf.m_a_55a39fc4(var67, 1, var85);
               C_u.m_a_1ef468a(4, var99);
               return;
            }

            if (var45.m_b_9b68() == 4 && var45.m_c_9b68() == 12) {
               byte[] var66;
               if ((var66 = var45.m_b_12d408()).length > 11) {
                  long var84 = (C_cf.m_a_49634b7b(var66, 0) << 32) + C_cf.m_a_49634b7b(var66, 4);
                  int var107 = C_cf.m_a_49634b7a(var66, 10);
                  String var259 = C_cf.m_a_55a39fc4(var66, 11, var107);
                  Object var114 = null;
                  C_ap.m_a_44be42cf(var259, var84, false);
                  return;
               }
            } else if (var45.m_b_9b68() == 4 && var45.m_c_9b68() == 7) {
               byte[] var62 = var45.m_b_12d408();
               int var80 = 0;
               if (var62.length < 11) {
                  throw new C_aq(150, 0, false);
               }

               long var95 = C_cf.m_a_e306d821(var62, 0, false);
               long var112 = C_cf.m_a_e306d821(var62, 4, false);
               int var70 = C_cf.m_b_49634b7a(var62, 8);
               int var130 = C_cf.m_a_49634b7a(var62, 10);
               if (var62.length < var130 + 11 + 4) {
                  throw new C_aq(150, 1, false);
               }

               String var135 = C_cf.m_a_55a39fc4(var62, 11, var130);
               var80 = var130 + 11;
               var80 += 2;
               int var140 = C_cf.m_b_49634b7a(var62, var80);
               int var83 = var80 + 2;

               for (int var149 = 0; var149 < var140; var149++) {
                  byte[] var152;
                  if ((var152 = C_cf.m_a_e3062636(var62, var83)) == null) {
                     throw new C_aq(150, 2, false);
                  }

                  var83 += 4 + var152.length;
               }

               byte[] var150;
               int var153;
               do {
                  if ((var150 = C_cf.m_a_e3062636(var62, var83)) == null) {
                     throw new C_aq(150, 3, false);
                  }

                  var153 = C_cf.m_b_49634b7a(var62, var83);
                  var83 += 4 + var150.length;
               } while (var153 != 2 && var153 != 5);

               int var143 = 0;
               if (var70 == 1) {
                  byte[] var159 = null;

                  while (var143 < var150.length) {
                     byte[] var164;
                     if ((var164 = C_cf.m_a_e3062636(var150, var143)) == null) {
                        throw new C_aq(151, 0, false);
                     }

                     var153 = C_cf.m_b_49634b7a(var150, var143);
                     var143 += 4 + var164.length;
                     switch (var153) {
                        case 257:
                           var159 = var164;
                        case 1281:
                     }
                  }

                  if (var159 != null) {
                     if (var159.length < 4) {
                        throw new C_aq(151, 2, false);
                     }

                     String var165;
                     if (C_cf.m_b_49634b7a(var159, 0) == 2) {
                        var165 = C_cf.m_a_e96ea081(C_cf.m_b_55a39fc4(var159, 4, var159.length - 4));
                     } else {
                        var165 = C_cf.m_a_e96ea081(C_cf.m_a_55a39fc4(var159, 4, var159.length - 4));
                     }

                     C_d var258 = new C_d(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var165, false);
                     Object var168 = null;
                     C_u.m_a_cb408b9a(var258);
                     C_aw var172 = C_v.m_a_513388b0(var135);
                     if (C_bp.m_a_134632(166) && var172 != null) {
                        p_bj_a_cb37742e(var172);
                        return;
                     }
                  }
               } else if (var70 == 2) {
                  if (var150.length < 10) {
                     throw new C_aq(152, 0, false);
                  }

                  int var255 = C_cf.m_b_49634b7a(var150, 0);
                  boolean var158 = false;
                  if (var255 != 0) {
                     return;
                  }

                  var143 += 2;
                  int var145 = var143 + 8;
                  if (var150.length < 26) {
                     throw new C_aq(152, 1, false);
                  }

                  int var146 = var145 + 16;
                  int var167 = -1;
                  byte[] var171 = new byte[4];
                  byte[] var174 = new byte[4];
                  Object var178 = null;

                  byte[] var162;
                  do {
                     if ((var162 = C_cf.m_a_e3062636(var150, var146)) == null) {
                        throw new C_aq(152, 2, false);
                     }

                     switch (var153 = C_cf.m_b_49634b7a(var150, var146)) {
                        case 3:
                           System.arraycopy(var162, 0, var171, 0, 4);
                           break;
                        case 4:
                           System.arraycopy(var162, 0, var174, 0, 4);
                           break;
                        case 5:
                           C_cf.m_a_79834524(var162);
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        default:
                           break;
                        case 10:
                           var167 = C_cf.m_b_49634b7a(var162, 0);
                     }

                     var146 += 4 + var162.length;
                  } while (var153 != 10001);

                  int var183 = 0;
                  if (var162.length < 53) {
                     throw new C_aq(152, 3, false);
                  }

                  int var194;
                  if ((var194 = C_cf.m_a_e306d820(var162, 45, false)) != 1 && var194 != 4 && var194 != 26 && (var194 < 1000 || var194 > 1004)) {
                     return;
                  }

                  int var195 = C_cf.m_a_e306d820(var162, 51, false);
                  if ((var194 < 1000 || var194 > 1004) && var162.length < var195 + 53 + 4 + 4) {
                     throw new C_aq(152, 4, false);
                  }

                  byte[] var198 = new byte[var195];
                  System.arraycopy(var162, 53, var198, 0, var195);
                  int var184 = var195 + 53;
                  if ((var194 == 1 || var194 == 4) && var198.length > 1) {
                     if (var194 == 1 || var194 == 4) {
                        var184 += 8;
                     }

                     boolean var201 = false;
                     int var181;
                     if (var162.length >= var184 + 4
                        && (var181 = (int)C_cf.m_a_e306d821(var162, var184, false)) == 38
                        && C_cf.m_a_55a39fc4(var162, var184 + 4, var181).equals("{0946134E-4C7F-11D1-8222-444553540000}")) {
                        var201 = true;
                     }

                     if (var194 == 1) {
                        String var204;
                        if ((var204 = C_cf.m_a_e96ea081(C_cf.m_a_5a238448(var198, var201))).indexOf("rtf1") > 0) {
                           var204 = C_cf.m_e_e96ea081(var204);
                        }

                        var178 = new C_d(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var204, false);
                     } else {
                        int var205 = -1;

                        for (int var209 = 0; var209 < var198.length; var209++) {
                           if (var198[var209] == 254) {
                              var205 = var209;
                              break;
                           }
                        }

                        String var210;
                        String var220;
                        if (var205 != -1) {
                           var210 = C_cf.m_a_e96ea081(C_cf.m_a_20e7da8(var198, 0, var205, var201));
                           var220 = C_cf.m_a_e96ea081(C_cf.m_a_20e7da8(var198, var205 + 1, var198.length - var205 - 1, var201));
                        } else {
                           var210 = C_cf.m_a_e96ea081(C_cf.m_a_5a238448(var198, var201));
                           var220 = "";
                        }

                        var178 = new C_ae(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var220, var210);
                     }

                     C_u.m_a_cb408b9a((C_bl)var178);
                     C_aw var206 = C_v.m_a_513388b0(var135);
                     if (C_bp.m_a_134632(166) && var206 != null) {
                        p_bj_a_cb37742e(var206);
                     }

                     byte[] var211 = new byte[var130 + 11 + 2 + 51 + 3];
                     int var221 = 0;
                     System.arraycopy(var62, 0, var211, 0, 10);
                     C_cf.m_a_e306985c(var211, 10, var130);
                     byte[] var239;
                     System.arraycopy(var239 = C_cf.m_a_afa28ebe(var135), 0, var211, 11, var239.length);
                     var221 = 11 + var239.length;
                     C_cf.m_b_e306985c(var211, var221, 3);
                     var221 += 2;
                     System.arraycopy(var162, 0, var211, var221, 51);
                     var221 += 51;
                     C_cf.m_a_7dcd25f8(var211, var221, 1, false);
                     var221 += 2;
                     C_cf.m_a_e306985c(var211, var221, 0);
                     C_bv var249 = new C_bv(4, 11, 0L, new byte[0], var211);
                     C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var249);
                     return;
                  }

                  if (var194 == 26) {
                     if (var162.length < var184 + 2 + 18 + 4) {
                        throw new C_aq(152, 5, false);
                     }

                     var183 = var184 + 20;
                     int var179 = (int)C_cf.m_a_e306d821(var162, var183, false);
                     int var186 = var183 + 4;
                     if (var162.length < var186 + var179 + 15 + 4 + 4) {
                        throw new C_aq(152, 6, false);
                     }

                     String var202 = C_cf.m_a_55a39fc4(var162, var186, var179);
                     var183 = var186 + var179;
                     var183 += 19;
                     var195 = (int)C_cf.m_a_e306d821(var162, var183, false);
                     int var189 = var183 + 4;
                     if (var162.length < var189 + var195) {
                        throw new C_aq(152, 7, false);
                     }

                     String var208 = C_cf.m_a_e96ea081(C_cf.m_a_55a39fc4(var162, var189, var195));
                     var183 = var189 + var195;
                     if (var202.equals("File") && Jimm.f_jimm_Jimm_a_3fbeb738.m_a_46a7a9e5().isShown()) {
                        if (var167 == 2) {
                           var178 = Integer.toString(C_cf.m_b_49634b7a(var162, var183));
                           var183 += 2;
                           var183 += 2;
                           var195 = C_cf.m_a_e306d820(var162, var183, false);
                           int var193 = var183 + 2;
                           if (var162.length < var193 + var195) {
                              throw new C_aq(152, 8, false);
                           }

                           C_cf.m_a_e96ea081(C_cf.m_a_55a39fc4(var162, var193, var195));
                           Object var216 = null;
                           long var237 = 0L;
                           if (var150.length < 8) {
                              throw new C_aq(152, 9, false);
                           }

                           if ((var162 = C_cf.m_a_e3062636(var150, var146)) == null) {
                              throw new C_aq(152, 2, false);
                           }

                           if (C_cf.m_b_49634b7a(var150, var146) == 4) {
                              System.arraycopy(var162, 0, var174, 0, 4);
                           }

                           C_aw var227;
                           (var227 = C_v.m_a_513388b0(var135)).m_a_4870e775(225, var174);
                           var227.m_a_4870e775(226, var171);
                           var227.m_a_255f295(74, Integer.parseInt((String)var178));
                           var216 = new C_ay(var227.m_a_46a7a54b());

                           try {
                              C_ac.m_a_cb38d14b((C_az)var216);
                           } catch (C_aq var41) {
                              C_aq.m_a_481c933f(var41);
                              if (var41.f_aq_a_5a) {
                                 return;
                              }
                           }

                           C_cn.m_a_33097a9f("u2", (C_az)var216, true);
                           return;
                        }
                     } else {
                        if (var202.equals("Send Web Page Address (URL)")) {
                           int var214;
                           String var236;
                           String var243;
                           if ((var214 = var208.indexOf(254)) != -1) {
                              var236 = var208.substring(0, var214);
                              var243 = var208.substring(var214 + 1);
                           } else {
                              var236 = var208;
                              var243 = "";
                           }

                           C_ae var257 = new C_ae(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var243, var236);
                           Object var226 = null;
                           C_u.m_a_cb408b9a(var257);
                           byte[] var215 = new byte[var130 + 11 + 2 + 51 + 3 + 20 + 4 + var179 + 19 + 4 + var195];
                           int var229 = 0;
                           System.arraycopy(var62, 0, var215, 0, 10);
                           C_cf.m_a_e306985c(var215, 10, var130);
                           byte[] var251;
                           System.arraycopy(var251 = C_cf.m_a_afa28ebe(var135), 0, var215, 11, var251.length);
                           var229 = 11 + var251.length;
                           C_cf.m_b_e306985c(var215, var229, 3);
                           var229 += 2;
                           System.arraycopy(var150, 0, var215, var229, 51);
                           var229 += 51;
                           C_cf.m_a_7dcd25f8(var215, var229, 1, false);
                           var229 += 2;
                           C_cf.m_a_e306985c(var215, var229, 0);
                           System.arraycopy(var162, var184, var215, ++var229, var179 + 24 + 19 + 4 + var195);
                           C_bv var151 = new C_bv(4, 11, 0L, new byte[0], var215);
                           C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var151);
                           return;
                        }

                        if (var202.equals("Script Plug-in: Remote Notification Arrive")) {
                           try {
                              C_aw var96 = C_v.m_a_513388b0(var135);
                              C_bq.m_a_e925fa09(var135, "e0");
                              if (var96 != null
                                 && var96.m_n_9b68() == 0
                                 && C_bp.m_a_134632(159)
                                 && !var96.m_a_134632(8)
                                 && C_bp.m_a_134621(92) != 37
                                 && C_bp.m_a_134621(110) != 2
                                 && (C_bp.m_a_134621(110) != 3 || var96.m_m_9b68() != 0)) {
                                 int var113;
                                 if ((var113 = var208.indexOf("<QUERY>")) < 0) {
                                    throw new Exception("cp #2");
                                 }

                                 int var127;
                                 if ((var127 = var208.indexOf("</QUERY>")) < 0) {
                                    throw new Exception("cp #3");
                                 }

                                 if ((var130 = var208.indexOf("<NOTIFY>")) < 0) {
                                    throw new Exception("cp #4");
                                 }

                                 int var136;
                                 if ((var136 = var208.indexOf("</NOTIFY>")) < 0) {
                                    throw new Exception("cp #5");
                                 }

                                 int var46;
                                 String var141;
                                 if ((var46 = (var141 = C_cf.m_c_e96ea081(var208.substring(var113 + 7, var127))).indexOf("<PluginID>")) < 0) {
                                    throw new Exception("cp #6");
                                 }

                                 int var97;
                                 if ((var97 = var141.indexOf("</PluginID>")) < 0) {
                                    throw new Exception("cp #7");
                                 }

                                 if (var141.substring(var46 + 10, var97).toLowerCase().compareTo("srvmng") != 0) {
                                    throw new Exception("cp #8");
                                 }

                                 String var47;
                                 if ((var47 = C_cf.m_c_e96ea081(var208.substring(var130 + 8, var136))).indexOf("AwayStat") < 0) {
                                    throw new Exception("cp #9");
                                 }

                                 if ((var70 = var47.indexOf("<senderId>")) < 0) {
                                    throw new Exception("");
                                 }

                                 int var98;
                                 if ((var98 = var47.indexOf("</senderId>")) < 0) {
                                    throw new Exception("");
                                 }

                                 if (var47.substring(var70 + 10, var98).compareTo(var135) != 0) {
                                    throw new Exception("incorrect uin");
                                 }

                                 String var10001 = C_bp.m_a_47921032(32);
                                 String var72 = C_bp.m_a_47921032(33);
                                 String var64 = var10001;
                                 String var65 = "<NR><RES>"
                                    + C_cf.m_d_e96ea081(
                                       "<ret event='OnRemoteNotification'><srv><id>cAwaySrv</id><val srv_id='cAwaySrv'><Root><CASXtraSetAwayMessage></CASXtraSetAwayMessage><uin>"
                                          + C_bp.m_a_47921032(254)
                                          + "</uin><index>1</index><title>"
                                          + var64
                                          + "</title><desc>"
                                          + var72
                                          + "</desc></Root></val></srv></ret>"
                                    )
                                    + "</RES></NR>";
                                 byte[] var49 = C_m.m_a_40349d6f(var135, var194, var95, var112, var65);
                                 C_bv var50 = new C_bv(4, 11, 0L, new byte[0], var49);
                                 C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var50);
                                 return;
                              }

                              return;
                           } catch (Exception var42) {
                              return;
                           }
                        }
                     }
                  } else if (var194 >= 1000 && var194 <= 1004) {
                     C_bq.m_a_e925fa09(var135, "d0");
                     C_aw var200;
                     if ((var200 = C_v.m_a_513388b0(var135)) == null) {
                        return;
                     }

                     byte var203 = 0;
                     switch ((int)C_bp.a$134622()) {
                        case 1:
                           var203 = 7;
                           break;
                        case 2:
                           var203 = 19;
                           break;
                        case 4:
                           var203 = 20;
                           break;
                        case 16:
                           var203 = 21;
                           break;
                        case 8193:
                           var203 = 26;
                           break;
                        case 12288:
                           var203 = 22;
                           break;
                        case 16384:
                           var203 = 23;
                           break;
                        case 20480:
                           var203 = 24;
                           break;
                        case 24576:
                           var203 = 25;
                     }

                     if (var203 == 0) {
                        return;
                     }

                     byte[] var218;
                     if ((
                           var218 = C_cf.m_a_44c4d6c8(
                              C_cf.m_a_def8391f(
                                 C_cf.m_a_def8391f(C_bp.m_a_47921032(var203), "%time%", C_ac.m_a_73cf11cb(), true), "%nick%", var200.f_aw_a_523beb0a, true
                              ),
                              false
                           )
                        ).length
                        < 1) {
                        return;
                     }

                     byte[] var238 = new byte[var130 + 11 + 2 + 51 + 2 + var218.length + 1];
                     int var244 = 0;
                     System.arraycopy(var62, 0, var238, 0, 10);
                     C_cf.m_a_e306985c(var238, 10, var130);
                     byte[] var228;
                     System.arraycopy(var228 = C_cf.m_a_afa28ebe(var135), 0, var238, 11, var228.length);
                     var244 = 11 + var228.length;
                     C_cf.m_b_e306985c(var238, var244, 3);
                     var244 += 2;
                     System.arraycopy(var162, 0, var238, var244, 51);
                     C_cf.m_b_e306985c(var238, var244 + 2, 2048);
                     var244 += 51;
                     C_cf.m_a_7dcd25f8(var238, var244, var218.length + 1, false);
                     var244 += 2;
                     System.arraycopy(var218, 0, var238, var244, var218.length);
                     C_cf.m_a_e306985c(var238, var244 + var218.length, 0);
                     C_bv var219 = new C_bv(4, 11, 0L, new byte[0], var238);
                     C_ac.f_ac_a_240bd1.m_a_cb4a8bc4(var219);
                     return;
                  }
               } else if (var70 == 4) {
                  if (var150.length < 8) {
                     throw new C_aq(153, 0, false);
                  }

                  int var157;
                  if ((var157 = C_cf.m_a_e306d820(var150, 4, false)) != 1 && var157 != 4) {
                     return;
                  }

                  int var161 = C_cf.m_a_e306d820(var150, 6, false);
                  if (var150.length != var161 + 8) {
                     throw new C_aq(153, 1, false);
                  }

                  String var166 = C_cf.m_a_e96ea081(C_cf.m_a_55a39fc4(var150, 8, var161));
                  if (var157 == 1) {
                     C_d var254 = new C_d(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var166, false);
                     Object var170 = null;
                     C_u.m_a_cb408b9a(var254);
                     return;
                  }

                  if (var157 == 4) {
                     int var169;
                     String var173;
                     String var177;
                     if ((var169 = var166.indexOf(254)) != -1) {
                        var173 = var166.substring(0, var169);
                        var177 = var166.substring(var169 + 1);
                     } else {
                        var173 = var166;
                        var177 = "";
                     }

                     C_u.m_a_cb408b9a(new C_ae(var135, C_bp.m_a_47921032(254), C_cf.a$1385f3(), var177, var173));
                     return;
                  }
               }
            } else {
               if (var45.m_b_9b68() == 19 && var45.m_c_9b68() == 28) {
                  byte[] var61;
                  int var79 = C_cf.m_a_49634b7a(var61 = var45.m_b_12d408(), 0);
                  String var94 = C_cf.m_a_55a39fc4(var61, 1, var79);
                  C_ad var253 = new C_ad(1, var94, false, null);
                  Object var106 = null;
                  C_u.m_a_cb408b9a(var253);
                  return;
               }

               C_ad var126;
               if (var45.m_b_9b68() == 19 && var45.m_c_9b68() == 10) {
                  byte[] var60;
                  int var78 = C_cf.m_a_49634b7a(var60 = var45.m_b_12d408(), 1);
                  if (C_cf.m_a_49634b7a(var60, var78 + 2) == 0) {
                     return;
                  }

                  String var93 = C_cf.m_a_55a39fc4(var60, 2, var78);
                  int var105 = C_cf.m_a_49634b7a(var60, var78 + 13);
                  String var111 = C_cf.m_a_20e7da8(var60, var78 + 14, var105, true);
                  var126 = new C_ad(6, var93, false, var111);
               } else if (var45.m_b_9b68() == 19 && var45.m_c_9b68() == 25) {
                  int var57 = 0;
                  byte[] var77;
                  int var91 = C_cf.m_a_49634b7a(var77 = var45.m_b_12d408(), 0);
                  String var104 = C_cf.m_a_55a39fc4(var77, 1, var91);
                  var57 = var91 + 1;
                  var91 = C_cf.m_b_49634b7a(var77, var57);
                  var57 += 2;
                  String var110 = C_cf.m_a_20e7da8(var77, var57, var91, C_cf.m_a_e3069860(var77, var57, var91));
                  var126 = new C_ad(3, var104, false, var110);
               } else {
                  if (var45.m_b_9b68() != 19 || var45.m_c_9b68() != 27) {
                     return;
                  }

                  boolean var54 = false;
                  byte[] var76;
                  int var89 = C_cf.m_a_49634b7a(var76 = var45.m_b_12d408(), 0);
                  String var103 = C_cf.m_a_55a39fc4(var76, 1, var89);
                  int var55 = var89 + 1;
                  boolean var109 = false;
                  if (C_cf.m_a_49634b7a(var76, var55) == 1) {
                     var109 = true;
                  }

                  var55++;
                  if (!var109) {
                     var89 = C_cf.m_b_49634b7a(var76, var55);
                     String var69 = C_cf.m_a_20e7da8(var76, var55 + 2, var89, C_cf.m_a_e3069860(var76, var55 + 2, var89));
                     if (var89 == 0) {
                        var126 = new C_ad(2, var103, var109, null);
                     } else {
                        var126 = new C_ad(2, var103, var109, var69);
                     }
                  } else {
                     var126 = new C_ad(2, var103, var109, "");
                  }
               }

               C_u.m_a_cb408b9a(var126);
            }
         }
      }
   }

   private static void p_bj_a_cb37742e(C_aw var0) {
      if (var0.m_n_9b68() == 0 && !var0.m_a_134632(8) && C_bp.m_a_134621(110) != 2 && !var0.f_aw_b_5a && (C_bp.m_a_134621(110) != 3 || var0.m_m_9b68() != 0)) {
         long var1 = C_bp.a$134622();
         String var3 = new String();
         if (var1 == 1L) {
            var3 = C_bp.m_a_47921032(7);
         }

         if (var1 == 2L) {
            var3 = C_bp.m_a_47921032(19);
         }

         if (var1 == 4L) {
            var3 = C_bp.m_a_47921032(20);
         }

         if (var1 == 16L) {
            var3 = C_bp.m_a_47921032(21);
         }

         if (var3.length() > 1) {
            var3 = C_cf.m_a_def8391f(C_cf.m_a_def8391f(var3, "%time%", C_ac.m_a_73cf11cb(), true), "%nick%", var0.f_aw_a_523beb0a, true);
            C_d var5 = new C_d(var0.m_a_47921032(0), var0, 1, C_cf.a$1385f3(), C_bs.m_a_e96ea081("C0") + "\n" + var3);
            C_bu var6 = new C_bu(var5);

            try {
               C_ac.m_a_cb38d14b(var6);
            } catch (Exception var4) {
            }
         }

         var0.f_aw_b_5a = true;
      }
   }

   private static boolean p_bj_a_aad3b203(String var0) {
      for (int var1 = 0; var1 < f_bj_a_48a69a2c.size(); var1++) {
         if (f_bj_a_48a69a2c.elementAt(var1).equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public static boolean m_a_cb408b9e(C_bl var0) {
      if (!C_bp.m_a_134632(162)) {
         return false;
      } else {
         String var2 = null;
         String var1 = var0.f_bl_c_523beb0a;
         var2 = C_v.m_a_513388b0(var0.f_bl_c_523beb0a);
         if (null != var2 || p_bj_a_aad3b203(var1)) {
            return false;
         } else if (!(var0 instanceof C_d)) {
            return true;
         } else {
            boolean var10 = false;
            int var18 = 0;
            var2 = var1;
            int var4 = 0;

            boolean var10000;
            while (true) {
               if (var4 >= f_bj_b_48a69a2c.size()) {
                  var10000 = false;
                  break;
               }

               if (f_bj_b_48a69a2c.elementAt(var4).equals(var2)) {
                  var10000 = true;
                  break;
               }

               var4++;
            }

            if (!var10000) {
               f_bj_b_48a69a2c.addElement(var1);
               f_bj_c_48a69a2c.addElement(String.valueOf(0));
            } else {
               int var12 = f_bj_b_48a69a2c.indexOf(var1);
               String var22 = (String)f_bj_c_48a69a2c.elementAt(var12);
               Object var17 = null;
               var18 = Integer.parseInt(var22);
               f_bj_c_48a69a2c.setElementAt(String.valueOf(++var18), var12);
            }

            C_d var23 = (C_d)var0;
            var2 = null;
            var2 = var23.f_d_a_523beb0a;
            C_bq.m_a_eb1e1393(var1, "D0", var2);
            if (var0.m_a_9b79()) {
               return true;
            } else {
               String var19 = C_bp.m_a_47921032(37);
               var2 = var2;
               if (var2.length() != var19.length()) {
                  var10000 = false;
               } else {
                  label78: {
                     if (var2 != var19) {
                        int var7 = var2.length();

                        for (int var5 = 0; var5 < var7; var5++) {
                           if (C_aj.m_a_132f95(var2.charAt(var5)) != C_aj.m_a_132f95(var19.charAt(var5))) {
                              var10000 = false;
                              break label78;
                           }
                        }
                     }

                     var10000 = true;
                  }
               }

               if (var10000) {
                  f_bj_a_48a69a2c.addElement(var1);
               }

               if (var18 < 3 || p_bj_a_aad3b203(var1)) {
                  C_aw var26 = new C_aw(0, 0, var1, var1, true, false);
                  String var20 = C_bp.m_a_47921032(p_bj_a_aad3b203(var1) ? 36 : 35);
                  C_aw var16 = var26;
                  if (C_bp.a$134622() != 256L && C_bp.a$134622() != 512L && C_bp.m_a_47921032(37).length() >= 1) {
                     C_d var8 = new C_d(var16.m_a_47921032(0), var16, 1, C_cf.a$1385f3(), var20);
                     C_bu var21 = new C_bu(var8);

                     try {
                        C_ac.m_a_cb38d14b(var21);
                     } catch (Exception var6) {
                     }
                  }
               }

               return true;
            }
         }
      }
   }
}
