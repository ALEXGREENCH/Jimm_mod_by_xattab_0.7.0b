/*******************************************************************************
 Jimm - Mobile Messaging - J2ME ICQ clone
 Copyright (C) 2003-05  Jimm Project

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ********************************************************************************
 File: src/jimm/comm/Util.java
 Version: ###VERSION###  Date: ###DATE###
 Author(s): Manuel Linsmayer, Andreas Rossbacher, Sergey Chernov, Andrey B. Ivlev
 *******************************************************************************/

package jimm.comm;

import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;

import jimm.*;
import jimm.util.*;

public class Util
{
	// Client CAPS
	public static final byte[] CAP_AIM_SERVERRELAY = explodeToBytes("09,46,13,49,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_UTF8 = explodeToBytes("09,46,13,4E,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_UTF8_GUID = explodeToBytes(
      "7b,30,39,34,36,31,33,34,45,2D,34,43,37,46,2D,31,31,44,31,2D,38,32,32,32,2D,34,34,34,35,35,33,35,34,30,30,30,30,7D", ',', 16
   );
	public static final byte[] CAP_MSGTYPE2 = explodeToBytes("09,49,13,49,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_MIRANDAIM = explodeToBytes("4D,69,72,61,6E,64,61,4D,00,06,03,00,00,03,08,07", ',', 16);
	public static final byte[] CAP_TRILLIAN = explodeToBytes("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,09", ',', 16);
	private static final byte[] CAP_TRILCRYPT		= explodeToBytes("f2,e7,c7,f4,fe,ad,4d,fb,b2,35,36,79,8b,df,00,00", ',', 16);
	public static final byte[] CAP_SIM = explodeToBytes("*SIM client  ,00,00,00,00", ',', 16);
	private static final byte[] CAP_SIMOLD			= explodeToBytes("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,00", ',', 16);
	public static final byte[] CAP_LICQ = explodeToBytes("*Licq client ,00,00,00,00", ',', 16);
	public static final byte[] CAP_KOPETE = explodeToBytes("*Kopete ICQ  ,00,0c,00,02", ',', 16);
	public static final byte[] CAP_ANDRQ = explodeToBytes("*&RQinside,08,08,09,00,00,00,00", ',', 16);
	public static final byte[] CAP_QIP = explodeToBytes("56,3F,C8,09,0B,6F,41,*QIP 2005a", ',', 16);
	public static final byte[] CAP_QIPPDAWIN = explodeToBytes("56,3F,C8,09,0B,6F,41,*QIP     !", ',', 16);
	public static final byte[] CAP_QIPPDASYM = explodeToBytes("51,AD,D1,90,72,04,47,3D,A1,A1,49,F4,A3,97,A4,1F", ',', 16);
	public static final byte[] CAP_QIPINFIUM = explodeToBytes("7C,73,75,02,C3,BE,4F,3E,A6,9F,01,53,13,43,1E,1A", ',', 16);
	public static final byte[] CAP_VMICQ = explodeToBytes("*VmICQ ,76,30,2E,31,2E,39,62,00,00,00", ',', 16);
	public static final byte[] CAP_IM2 = explodeToBytes("74,ED,C3,36,44,DF,48,5B,8B,1C,67,1A,1F,86,09,9F", ',', 16);
	public static final byte[] CAP_MACICQ = explodeToBytes("dd,16,f2,02,84,e6,11,d4,90,db,00,10,4b,9b,4b,7d", ',', 16);
	public static final byte[] CAP_RICHTEXT = explodeToBytes("97,b1,27,51,24,3c,43,34,ad,22,d6,ab,f7,3f,14,92", ',', 16);
	public static final byte[] CAP_ICQ6 = explodeToBytes("01,38,ca,7b,76,9a,49,15,88,f2,13,fc,00,97,9e,a8", ',', 16);
	public static final byte[] CAP_STR20012 = explodeToBytes("a0,e9,3f,37,4f,e9,d3,11,bc,d2,00,04,ac,96,dd,96", ',', 16);
	private static final byte[] CAP_AIMICON			= explodeToBytes("09,46,13,46,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
	private static final byte[] CAP_AIMIMIMAGE		= explodeToBytes("09,46,13,45,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_AIMCHAT = explodeToBytes("74,8F,24,20,62,87,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_MIP = explodeToBytes("4D,49,50,20", ',', 16);
	public static final byte[] CAP_YAPP = explodeToBytes("*Yapp", ',', 16);
	public static final byte[] CAP_SMAPER = explodeToBytes("*Smaper", ',', 16);
	public static final byte[] CAP_XTRAZ = explodeToBytes("1A,09,3C,6C,D7,FD,4E,C5,9D,51,A6,47,4E,34,F5,A0", ',', 16);
	public static final byte[] CAP_AIMFILE = explodeToBytes("09,46,13,43,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_DIRECT = explodeToBytes("09,46,13,44,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_JIMM = explodeToBytes("*Jimm ,00,00,00,00,00,00,00,00,00,00,00", ',', 16);
	public static final byte[] CAP_AVATAR = explodeToBytes("09,46,13,4C,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_TYPING = explodeToBytes("56,3f,c8,09,0b,6f,41,bd,9f,79,42,26,09,df,a2,f3", ',', 16);
	public static final byte[] CAP_MCHAT = explodeToBytes("*mChat icq,20,32,2E,33,2E,30,6D", ',', 16);
	public static final byte[] CAP_FILE_SHARING = explodeToBytes("09,46,13,48,4c,7f,11,d1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_NAT_ICQ = explodeToBytes("*NatICQ", ',', 16);
//	private static final byte[] CAP_MICQ				= explodeToBytes("*mICQ ,A9,* R.K. ',00,00,00,00", ',', 16);
//	private static final byte[] CAP_RAMBLER				= explodeToBytes("7E,11,B7,78,A3,53,49,26,A8,02,44,73,52,08,C4,2A", ',', 16);

	// Arrays for new capability blowup
	public static final byte[] CAP_OLD_HEAD = explodeToBytes("09,46", ',', 16);
	public static final byte[] CAP_OLD_TAIL = explodeToBytes("4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	
	public static final byte[] CAP_JIMM_VERSION = explodeToBytes("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,76,65,72", ',', 16);
	public static final byte[] CAP_JIMM_WAP = explodeToBytes("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,77,61,70", ',', 16);
	public static final byte[] CAP_JIMM_ADV = explodeToBytes("68,74,74,70,3A,2F,2F,6A,69,6D,6D,2E,69,6D,2F,61,64,76", ',', 16);
	public static final byte[] CAP_BAYAN = explodeToBytes("62,61,79,61,6E,49,43,51", ',', 16);
	public static final byte[] CAP_DICHAT = explodeToBytes("44,5B,69,5D,43,68,61,74", ',', 16);
	public static final byte[] CAP_QUTIM = explodeToBytes("*qutim", ',', 16);
	public static final byte[] CAP_PIGEON = explodeToBytes("50,49,47,45,4F,4E,21", ',', 16);
	public static final byte[] CAP_PIGEON_VERSION = explodeToBytes("76,65,72,3A", ',', 16);
	public static final byte[] CAP_HTML = explodeToBytes("09,46,E0,01,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_XTRAZ_VIDEO = explodeToBytes("09,46,E0,02,4C,7F,11,D1,82,22,44,45,53,54,00,00", ',', 16);
	public static final byte[] CAP_WJIMM = explodeToBytes("77,4A,69,6D,6D", ',', 16);
	public static final byte[] CAP_LOCID = explodeToBytes("4C,6F,63,49,44", ',', 16);
	public static final byte[] CAP_ICQ7 = explodeToBytes("C8,95,3A,9F,21,F1,4F,AA,B0,B2,6D,E6,63,AB,F5,B7", ',', 16);
	public static final byte[] CAP_MIRANDA_ICQ = explodeToBytes("69,63,71", ',', 16);
	public static final byte[] CAP_DIM = explodeToBytes("44,5B,69,6D,5D", ',', 16);
	public static final byte[] CAP_QIP_STATUS = explodeToBytes("B7,07,43,78,F5,0C,77,77,97,77,57,78,50,2D,05,00", ',', 16);
	// No capability
	public static final int CAPF_NO_INTERNAL = 0x00000000;
	// Client unterstands type-2 messages
	public static final int CAPF_AIM_SERVERRELAY_INTERNAL = 0x00000001;
	// Client unterstands UTF-8 messages
	public static final int CAPF_UTF8_INTERNAL = 0x00000002;
	// Client capabilities for detection
	public static final int CAPF_MIRANDAIM	= 0x00000004;
 	public static final int CAPF_TRILLIAN		= 0x00000008;
	public static final int CAPF_TRILCRYPT	= 0x00000010;
	public static final int CAPF_SIM			= 0x00000020;
	public static final int CAPF_SIMOLD		= 0x00000040;
	public static final int CAPF_LICQ			= 0x00000080;
	public static final int CAPF_KOPETE		= 0x00000100;
	public static final int CAPF_QIPINFIUM	= 0x00000200;
	public static final int CAPF_ANDRQ		= 0x00000400;
	public static final int CAPF_QIP			= 0x00000800;
	public static final int CAPF_IM2			= 0x00001000;
	public static final int CAPF_MACICQ		= 0x00002000;
	public static final int CAPF_RICHTEXT = 0x4;
	public static final int CAPF_VMICQ		= 0x00008000;
	public static final int CAPF_QIPPDASYM	= 0x00010000;
	public static final int CAPF_STR20012		= 0x00020000;
	public static final int CAPF_AIMICON		= 0x00040000; // можно заменить на что нить...
	public static final int CAPF_SMAPER		= 0x00080000;
	public static final int CAPF_ICQ6 = 0x200;
	public static final int CAPF_QIPPDAWIN	= 0x00200000;
	public static final int CAPF_MIP			= 0x00400000;
	public static final int CAPF_YAPP			= 0x00800000;
	public static final int CAPF_XTRAZ = 0x10;
	public static final int CAPF_AIMFILE = 0x20;
	public static final int CAPF_JIMM			= 0x04000000;
	public static final int CAPF_AIMIMIMAGE	= 0x08000000; // можно заменить на что нить...
	public static final int CAPF_AVATAR = 0x40;
	public static final int CAPF_DIRECT = 0x80;
	public static final int CAPF_TYPING = 0x100;
	public static final int CAPF_MCHAT		= 0x80000000;

	private static boolean HAS_CAP_FILE_SHARING;
	private static boolean HAS_CAP_NAT_ICQ;
	private static boolean HAS_CAP_AIMCHAT;

	public static final int CAPF_AIMCHAT = 0x8;
	public static final int CAPF_FILE_SHARING = 0x400;
	public static final int CAPF_HTML = 0x800;
	public static final int CAPF_XTRAZ_VIDEO = 0x1000;
	public static final int CAPF_ICQ7 = 0x2000;
	public static final int CAPF_STATUS_CHAT = 0x4000;
	public static final int CAPF_STATUS_EVIL = 0x8000;
	public static final int CAPF_STATUS_DEPRESSION = 0x10000;
	public static final int CAPF_STATUS_HOME = 0x20000;
	public static final int CAPF_STATUS_WORK = 0x40000;
	public static final int CAPF_STATUS_LUNCH = 0x80000;
	public static final byte CLI_MRA = 30;
	public static final byte CLI_BAYAN = 31;
	public static final byte CLI_DICHAT = 32;
	public static final byte CLI_WJIMM = 33;
	public static final byte CLI_LOCID = 34;
	public static final byte CLI_QUTIM = 35;
	public static final byte CLI_PIGEON = 36;
	public static final byte CLI_ICQ7 = 37;
	public static final byte CLI_DIM = 38;
	// Client IDs
	public static final byte CLI_NONE			=  0;
	public static final byte CLI_QIP			=  1;
	public static final byte CLI_MIRANDA		=  2;
	public static final byte CLI_ANDRQ		=  3;
	public static final byte CLI_RANDQ		=  4;
	public static final byte CLI_TRILLIAN		=  5;
	public static final byte CLI_SIM			=  6;
	public static final byte CLI_KOPETE		=  7;
	public static final byte CLI_JIMM			=  8;
	public static final byte CLI_STICQ		=  9;
	public static final byte CLI_AGILE		= 10;
	public static final byte CLI_LIBICQ2000	= 11;
	public static final byte CLI_VMICQ		= 12;
	public static final byte CLI_QIPPDASYM	= 13;
	public static final byte CLI_QIPPDAWIN	= 14;
	public static final byte CLI_QIPINFIUM	= 15;
	public static final byte CLI_ICQ6			= 16;
	public static final byte CLI_ICQLITE		= 17;
	public static final byte CLI_ICQLITE4		= 18;
	public static final byte CLI_ICQLITE5		= 19;
	public static final byte CLI_ICQ2003B		= 20;
	public static final byte CLI_ICQ2GO		= 21;
	public static final byte CLI_MCHAT		= 22;
	public static final byte CLI_MACICQ		= 23;
	public static final byte CLI_GAIM			= 24;
	public static final byte CLI_GNOMEICQ		= 25;
	public static final byte CLI_LICQ			= 26;
	public static final byte CLI_MIP			= 27;
	public static final byte CLI_YAPP			= 28;
	public static final byte CLI_SMAPER		= 29;
	public static final byte CLI_SLICK = 39;
	public static final byte CLI_IM2 = 40;
	public static final byte CLI_NATICQ = 41;
	public static final byte CLI_SMARTICQ = 42;
	public static final byte CLI_ICQPPC = 43;
	public static final byte CLI_MICQ = 44;
	public static final byte CLI_WEBICQ = 45;
	public static final byte CLI_STRICQ = 46;
	public static final byte CLI_YSM = 47;
	public static final byte CLI_VICQ = 48;
	public static final byte CLI_ALICQ = 49;
	public static final byte CLI_CENTERICQ = 50;
	public static final byte CLI_LIBICQJABBER = 51;
	public static final byte CLI_SPAM = 52;
	public static final byte CLI_AIM = 53;
	
	private static final String[] clientNames = explode(
      "Not detected|QIP|Miranda|&RQ|R&Q|Trillian|SIM|Kopete|Jimm|StICQ|Agile Messenger|Libicq2000|VmICQ|QIP PDA (Symbian)|QIP PDA (Windows)|QIP Infium|ICQ v6|ICQ Lite|ICQ Lite v4|ICQ Lite v5|ICQ 2003b|ICQ2GO!|mChat|Mac ICQ|Pidgin (Gaim)|GnomeICU|LICQ|MIP|Yapp|Sm@peR|Mail.ru Agent|BayanICQ|D[i]Chat|wJimm|LocID|qutIM|PIGEON!|ICQ v7|D[im]|Slick|IM2|NatICQ|SmartICQ|ICQ for PPC|mICQ|WebICQ|StrICQ|YSM|vICQ|Alicq|CenterICQ|Libicq2000|SPAM|AOL AIM|",
      '|'
   );

	public static void PrintCapabilities(String caption, byte[] caps)
	{
		for( int n = 0; n < caps.length; n += 16 )
		{
			if (caps.length - n < 15) continue;
			byte[] b = new byte[16];
			System.arraycopy(caps, n, b, 0, 16);

			String bytes = new String();
			for (int i = 0; i < b.length; i++)
			{
				bytes += Integer.toHexString(b[i] & 0xFF);
			}
		}
	}
	
	public static void detectUserClient(String uin, int dwFP1, int dwFP2, int dwFP3, byte[] capabilities, int protocolVersion, boolean statusChange) {
      byte client = CLI_NONE;
      String clientVersion = "";
      long caps = 0L;
      ContactItem item;
      if ((item = ContactList.getItembyUIN(uin)) != null) {
         if (capabilities != null) {
            for (int index = 0; index < capabilities.length / 16; index++) {
               int offset = index << 4;
               if (byteArrayEquals(capabilities, offset, CAP_AIM_SERVERRELAY, 0, 16)) {
                  caps |= 1L;
               } else if (byteArrayEquals(capabilities, offset, CAP_UTF8, 0, 16)) {
                  caps |= 2L;
               } else if (byteArrayEquals(capabilities, offset, CAP_MIRANDAIM, 0, 8)) {
                  caps |= 4294967296L;
                  clientVersion = detectClientVersion(capabilities, 2, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_MIRANDA_ICQ, 0, 3)) {
                  caps |= 17179869184L;
                  clientVersion = detectClientVersion(capabilities, 2, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_TRILLIAN, 0, 16)) {
                  caps |= 8589934592L;
               } else if (byteArrayEquals(capabilities, offset, CAP_SIM, 0, 12)) {
                  caps |= 34359738368L;
               } else if (byteArrayEquals(capabilities, offset, CAP_LICQ, 0, 12)) {
                  caps |= 137438953472L;
                  clientVersion = detectClientVersion(capabilities, 26, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_MSGTYPE2, 0, 16)) {
                  caps |= 137438953472L;
               } else if (byteArrayEquals(capabilities, offset, CAP_KOPETE, 0, 12)) {
                  caps |= 274877906944L;
                  clientVersion = detectClientVersion(capabilities, 7, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_ANDRQ, 0, 9)) {
                  caps |= 1099511627776L;
                  clientVersion = detectClientVersion(capabilities, 3, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_QIP, 0, 16)) {
                  caps |= 2199023255552L;
                  clientVersion = detectClientVersion(capabilities, 1, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_QIPINFIUM, 0, 16)) {
                  caps |= 549755813888L;
               } else if (byteArrayEquals(capabilities, offset, CAP_IM2, 0, 16)) {
                  caps |= 4398046511104L;
               } else if (byteArrayEquals(capabilities, offset, CAP_MACICQ, 0, 16)) {
                  caps |= 8796093022208L;
               } else if (byteArrayEquals(capabilities, offset, CAP_RICHTEXT, 0, 16)) {
                  caps |= CAPF_RICHTEXT;
               } else if (byteArrayEquals(capabilities, offset, CAP_VMICQ, 0, 6)) {
                  caps |= 17592186044416L;
                  clientVersion = detectClientVersion(capabilities, 12, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_QIPPDASYM, 0, 16)) {
                  caps |= 35184372088832L;
               } else if (byteArrayEquals(capabilities, offset, CAP_STR20012, 0, 16)) {
                  caps |= 70368744177664L;
               } else if (byteArrayEquals(capabilities, offset, CAP_ICQ6, 0, 16)) {
                  caps |= CAPF_ICQ6;
               } else if (byteArrayEquals(capabilities, offset, CAP_HTML, 0, 16)) {
                  caps |= CAPF_HTML;
               } else if (byteArrayEquals(capabilities, offset, CAP_XTRAZ_VIDEO, 0, 16)) {
                  caps |= CAPF_XTRAZ_VIDEO;
               } else if (byteArrayEquals(capabilities, offset, CAP_ICQ7, 0, 16)) {
                  caps |= CAPF_ICQ7;
               } else if (byteArrayEquals(capabilities, offset, CAP_QIPPDAWIN, 0, 16)) {
                  caps |= 562949953421312L;
               } else if (byteArrayEquals(capabilities, offset, CAP_XTRAZ, 0, 16)) {
                  caps |= CAPF_XTRAZ;
               } else if (byteArrayEquals(capabilities, offset, CAP_AIMFILE, 0, 16)) {
                  caps |= CAPF_AIMFILE;
               } else if (byteArrayEquals(capabilities, offset, CAP_JIMM, 0, 5)) {
                  caps |= 4503599627370496L;
                  clientVersion = detectClientVersion(capabilities, 8, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_WJIMM, 0, 5)) {
                  caps |= 576460752303423488L;
                  clientVersion = detectClientVersion(capabilities, 33, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_LOCID, 0, 5)) {
                  caps |= 1152921504606846976L;
                  clientVersion = detectClientVersion(capabilities, 34, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_QUTIM, 0, 5)) {
                  caps |= 72057594037927936L;
                  clientVersion = detectClientVersion(capabilities, 35, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_PIGEON, 0, 7)) {
                  caps |= 144115188075855872L;
               } else if (byteArrayEquals(capabilities, offset, CAP_PIGEON_VERSION, 0, 4)) {
                  caps |= 288230376151711744L;
                  clientVersion = detectClientVersion(capabilities, 36, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_BAYAN, 0, 8)) {
                  caps |= 140737488355328L;
                  clientVersion = detectClientVersion(capabilities, 31, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_DICHAT, 0, 8)) {
                  caps |= 9007199254740992L;
                  clientVersion = detectClientVersion(capabilities, 32, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_DIM, 0, 5)) {
                  caps |= 68719476736L;
                  clientVersion = detectClientVersion(capabilities, 38, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_AVATAR, 0, 16)) {
                  caps |= CAPF_AVATAR;
               } else if (byteArrayEquals(capabilities, offset, CAP_DIRECT, 0, 16)) {
                  caps |= CAPF_DIRECT;
               } else if (byteArrayEquals(capabilities, offset, CAP_TYPING, 0, 16)) {
                  caps |= CAPF_TYPING;
               } else if (byteArrayEquals(capabilities, offset, CAP_MCHAT, 0, 9)) {
                  caps |= 18014398509481984L;
                  clientVersion = detectClientVersion(capabilities, 22, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_MIP, 0, 4)) {
                  caps |= 1125899906842624L;
                  clientVersion = detectClientVersion(capabilities, 27, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_YAPP, 0, 4)) {
                  caps |= 2251799813685248L;
                  clientVersion = detectClientVersion(capabilities, 28, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_SMAPER, 0, 5)) {
                  caps |= 281474976710656L;
                  clientVersion = detectClientVersion(capabilities, 29, index);
               } else if (byteArrayEquals(capabilities, offset, CAP_FILE_SHARING, 0, 16)) {
                  caps |= CAPF_FILE_SHARING;
               } else if (byteArrayEquals(capabilities, offset, CAP_NAT_ICQ, 0, 6)) {
                  caps |= 36028797018963968L;
               } else if (byteArrayEquals(capabilities, offset, CAP_AIMCHAT, 0, 16)) {
                  caps |= CAPF_AIMCHAT;
               } else if (byteArrayEquals(capabilities, offset, CAP_QIP_STATUS, 0, 15)) {
                  byte[] capability = new byte[16];
                  System.arraycopy(capabilities, index << 4, capability, 0, 16);
                  int statusCode = capability[15] & 15;
                        switch (statusCode) {
                     case 0:
                        caps |= CAPF_STATUS_DEPRESSION;
                     case 5:
                        caps |= CAPF_STATUS_CHAT;
                     case 6:
                        caps |= CAPF_STATUS_HOME;
                     case 7:
                        caps |= CAPF_STATUS_WORK;
                     case 8:
                        caps |= CAPF_STATUS_LUNCH;
                     case 9:
                        caps |= CAPF_STATUS_EVIL;
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                  }
               }
            }

            item.setIntValue(ContactItem.CONTACTITEM_CAPABILITIES, (int)caps);
         }

         if (!statusChange) {
            if ((caps & 72057594037927936L) != 0L) {
               client = CLI_QUTIM;
            } else if ((caps & 144115188075855872L) != 0L) {
               client = CLI_PIGEON;
            } else if ((caps & 17592186044416L) != 0L) {
               client = CLI_VMICQ;
            } else if ((caps & 35184372088832L) != 0L) {
               client = CLI_QIPPDASYM;
            } else if ((caps & 562949953421312L) != 0L) {
               client = CLI_QIPPDAWIN;
            } else if ((caps & 18014398509481984L) != 0L) {
               client = CLI_MCHAT;
            } else if ((caps & 2199023255552L) != 0L) {
               client = CLI_QIP;
               if (dwFP1 >>> 24 != 0) {
                  clientVersion = clientVersion + " (" + (dwFP1 >>> 24) + (dwFP1 >> 16 & 0xFF) + (dwFP1 >> 8 & 0xFF) + (dwFP1 & 0xFF) + ")";
               }
            } else if ((caps & 549755813888L) != 0L) {
               client = CLI_QIPINFIUM;
               if ((dwFP1 & 65535) != 0) {
                  clientVersion = clientVersion + "(" + (dwFP1 & 65535) + ")";
               }
            } else if (protocolVersion == 31337) {
               if ((caps & 2L) != 0L) {
                  client = CLI_QIPINFIUM;
               } else {
                  client = CLI_QIP;
                  clientVersion = "2005a";
               }
            } else if ((caps & 4503599627370496L) != 0L) {
               client = CLI_JIMM;
            } else if ((caps & 576460752303423488L) != 0L) {
               client = CLI_WJIMM;
            } else if ((caps & 1152921504606846976L) != 0L) {
               client = CLI_LOCID;
            } else if ((caps & 140737488355328L) != 0L) {
               client = CLI_BAYAN;
            } else if ((caps & 9007199254740992L) != 0L) {
               client = CLI_DICHAT;
            } else if ((caps & 68719476736L) != 0L) {
               client = CLI_DIM;
            } else if ((caps & 1125899906842624L) != 0L) {
               client = CLI_MIP;
            } else if ((caps & 2251799813685248L) != 0L) {
               client = CLI_YAPP;
            } else if ((caps & 281474976710656L) != 0L) {
               client = CLI_SMAPER;
            } else if ((caps & CAPF_ICQ6) != 0L) {
               if ((caps & CAPF_AVATAR) != 0L) {
                  client = CLI_MRA;
               } else {
                  client = CLI_ICQ6;
               }
            } else if ((caps & CAPF_ICQ7) != 0L) {
               client = CLI_ICQLITE;
               if ((caps & CAPF_HTML) != 0L && (caps & CAPF_XTRAZ_VIDEO) != 0L) {
                  client = CLI_ICQ7;
               }
            } else if ((caps & 8589934592L) != 0L) {
               client = CLI_TRILLIAN;
            } else if ((caps & 4398046511104L) != 0L) {
               client = CLI_IM2;
            } else if ((caps & 34359738368L) != 0L) {
               client = CLI_SIM;
            } else if ((caps & 274877906944L) != 0L) {
               client = CLI_KOPETE;
            } else if ((caps & 137438953472L) != 0L) {
               client = CLI_LICQ;
            } else if ((caps & CAPF_AIMCHAT) != 0L) {
               if ((caps & 2L) != 0L) {
                  client = CLI_GAIM;
               } else {
                  client = CLI_AIM;
               }
            } else {
               if ((caps & 2L) != 0L) {
                  switch (protocolVersion) {
                     case 10:
                        if ((caps & CAPF_TYPING) != 0L && (caps & CAPF_RICHTEXT) != 0L) {
                           client = CLI_ICQ2003B;
                        }
                     case 7:
                        if ((caps & 1L) == 0L && (caps & CAPF_DIRECT) == 0L && dwFP1 == 0 && dwFP2 == 0 && dwFP3 == 0) {
                           client = CLI_ICQ2GO;
                        }
                        break;
                     default:
                        if (dwFP1 == 0 && dwFP2 == 0 && dwFP3 == 0) {
                           if ((caps & CAPF_RICHTEXT) != 0L) {
                              client = CLI_ICQLITE;
                              if ((caps & CAPF_AVATAR) != 0L && (caps & CAPF_XTRAZ) != 0L) {
                                 if ((caps & CAPF_AIMFILE) != 0L) {
                                    client = CLI_ICQLITE5;
                                 } else {
                                    client = CLI_ICQLITE4;
                                 }
                              }
                           } else if ((caps & CAPF_FILE_SHARING) != 0L) {
                              client = CLI_SLICK;
                           } else if ((caps & 36028797018963968L) != 0L) {
                              client = CLI_NATICQ;
                           } else {
                              client = CLI_AGILE;
                           }
                        }
                  }
               }

               if ((caps & 8796093022208L) != 0L) {
                  client = CLI_MACICQ;
               } else if ((dwFP1 & -8454144) == 2097152000) {
                  client = CLI_LICQ;
                  int packedVersion;
                  if ((packedVersion = dwFP1 & 65535) % 10 != 0) {
                     clientVersion = packedVersion / 1000 + "." + packedVersion / 10 % 100 + "." + packedVersion % 10;
                  } else {
                     clientVersion = packedVersion / 1000 + "." + packedVersion / 10 % 100;
                  }
               } else {
                  switch (dwFP1) {
                     case -2458:
                        client = CLI_RANDQ;
                        clientVersion = (dwFP2 & 65535) + "";
                        break;
                     case -190:
                        client = CLI_MICQ;
                        break;
                     case -129:
                        client = CLI_ANDRQ;
                        clientVersion = (dwFP2 >>> 24) + "." + (dwFP2 >> 16 & 0xFF) + "." + (dwFP2 >> 8 & 0xFF) + "." + (dwFP2 & 0xFF);
                        break;
                     case -113:
                        client = CLI_STRICQ;
                        break;
                     case -85:
                        client = CLI_YSM;
                        break;
                     case -66:
                        client = CLI_ALICQ;
                        break;
                     case -2:
                        if (dwFP3 == dwFP1) {
                           client = CLI_JIMM;
                        }
                        break;
                     case -1:
                        if (dwFP2 == 0 && dwFP3 != -1) {
                           if (protocolVersion == 7) {
                              client = CLI_WEBICQ;
                              break;
                           }

                           if (dwFP3 == 997345517 && (caps & 2L) == 0L && (caps & CAPF_RICHTEXT) == 0L) {
                              client = CLI_SPAM;
                              break;
                           }
                        }

                        client = CLI_MIRANDA;
                        clientVersion = (dwFP2 >> 24 & 127) + "." + (dwFP2 >> 16 & 0xFF) + "." + (dwFP2 >> 8 & 0xFF) + "." + (dwFP2 & 0xFF);
                        break;
                     case 67312000:
                        client = CLI_VICQ;
                        break;
                     case 984052718:
                        if (dwFP2 == 983982976 && dwFP3 == 981957186) {
                           if (protocolVersion == 7 && (caps & 1L) != 0L && (caps & CAPF_DIRECT) != 0L && (caps & CAPF_RICHTEXT) != 0L) {
                              client = CLI_CENTERICQ;
                           } else {
                              client = CLI_LIBICQ2000;
                           }
                        }
                        break;
                     case 997567497:
                        client = CLI_TRILLIAN;
                        break;
                     case 1000922031:
                        if (protocolVersion == 2) {
                           client = CLI_STICQ;
                        }
                        break;
                     case 1072798699:
                        if (protocolVersion == 8 && dwFP1 == dwFP3) {
                           client = CLI_IM2;
                        }
                        break;
                     case 1107424276:
                        if ((dwFP2 & dwFP3) == dwFP1 && protocolVersion == 8) {
                           client = CLI_SPAM;
                        }
                        break;
                     case Integer.MAX_VALUE:
                        if ((caps & 4294967296L) != 0L || (caps & 17179869184L) != 0L) {
                           client = CLI_MIRANDA;
                           clientVersion = clientVersion + " (ICQ " + (dwFP2 >> 24 & 127) + "." + (dwFP2 >> 16 & 0xFF) + "." + (dwFP2 >> 8 & 0xFF) + "." + (dwFP2 & 0xFF) + ")";
                        }
                  }

                  if (client == 0) {
                     if (dwFP1 != 0 && dwFP1 == dwFP3 && dwFP3 == dwFP2 && caps == 0L) {
                        client = CLI_VICQ;
                     } else if ((caps & 70368744177665L) != 0L && dwFP1 == 0 && dwFP2 == 0 && dwFP3 == 0 && protocolVersion == 0) {
                        client = CLI_ICQPPC;
                     } else {
                        legacyDetection: {
                           if (protocolVersion == 7) {
                              if ((caps & 1L) != 0L && (caps & CAPF_DIRECT) != 0L) {
                                 if (dwFP1 == 0 && dwFP2 == 0 && dwFP3 == 0) {
                                    client = CLI_ANDRQ;
                                    break legacyDetection;
                                 }
                              } else if ((caps & CAPF_RICHTEXT) != 0L) {
                                 client = CLI_GNOMEICQ;
                                 break legacyDetection;
                              }
                           }

                           if (dwFP1 > 889192448 && dwFP1 < 1073741824) {
                              switch (protocolVersion) {
                                 case 9:
                                    client = CLI_ICQLITE;
                                    break;
                                 case 10:
                                    client = CLI_ICQ2003B;
                              }
                           }
                        }
                     }
                  }
               }
            }

            item.setIntValue(ContactItem.CONTACTITEM_CLIENT, client);
            item.setStringValue(ContactItem.CONTACTITEM_CLIVERSION, clientVersion);
         }
      }
   }

	public static String getClientString(byte cli)
	{
		return (clientNames[cli]);
	}

	private static String detectClientVersion(byte[] capabilities, int client, int index) {
      byte[] capability = new byte[16];
      System.arraycopy(capabilities, index << 4, capability, 0, 16);
      String version = "";
      if (client == CLI_MIRANDA) {
         if (capability[3] == 112 || capability[3] == 106) {
            version = "0." + capability[5] + "." + capability[6] + "." + capability[7];
         } else if (capability[12] == 0 && capability[13] == 0 && capability[14] == 0 && capability[15] == 1) {
            version = "0.1.2.0";
         } else if (capability[12] == 0 && capability[13] <= 3 && capability[14] <= 3 && capability[15] <= 1) {
            version = "0." + capability[13] + "." + capability[14] + "." + capability[15];
         } else {
            version = (capability[8] & 127) + "." + capability[9] + "." + capability[10] + "." + capability[11];
         }
      } else if (client == CLI_LICQ) {
         version = capability[12] + "." + capability[13] % 100 + "." + capability[14];
      } else if (client == CLI_KOPETE) {
         version = capability[12] + "." + capability[13] + "." + capability[14] + "." + capability[15];
      } else if (client == CLI_ANDRQ) {
         version = capability[12] + "." + capability[11] + "." + capability[10] + "." + capability[9];
      } else if (client == CLI_JIMM) {
         version = byteArrayToString(capability, 5, 11, false);
      } else if (client == CLI_WJIMM) {
         version = byteArrayToString(capability, 6, 7, false);
      } else if (client == CLI_LOCID) {
         version = (capability[11] >> 4 & 15) + "." + (capability[11] & 15) + "." + Integer.toHexString(capability[12] & 255) + " p" + capability[15];
      } else if (client == CLI_BAYAN) {
         version = byteArrayToString(capability, 8, 4, false);
      } else if (client == CLI_DICHAT) {
         version = byteArrayToString(capability, 9, 6, false);
      } else if (client == CLI_DIM) {
         version = byteArrayToString(capability, 6, 6, false);
      } else if (client == CLI_QIP) {
         version = byteArrayToString(capability, 11, 5, false);
      } else if (client == CLI_MIP) {
         version = byteArrayToString(capability, 4, 12, false);
      } else if (client == CLI_YAPP) {
         version = byteArrayToString(capability, 8, 5, false);
      } else if (client == CLI_SMAPER) {
         version = byteArrayToString(capability, 7, 6, false);
      } else if (client == CLI_QUTIM) {
         if (capability[6] == 46) {
            int major = capability[5] - 48;
            client = capability[7] - 48;
            version = major + "." + client;
         } else {
            byte majorVersion = capability[6];
            byte minorVersion = capability[7];
            String versionText = majorVersion + "." + minorVersion;
            switch (capability[5]) {
               case 108:
                  version = versionText + " (Linux)";
                  break;
               case 109:
                  version = versionText + " (MacOS)";
                  break;
               case 117:
               default:
                  version = versionText + " (Unknown OS)";
                  break;
               case 119:
                  version = versionText + " (Windows)";
            }
         }
      } else if (client == CLI_PIGEON) {
         version = byteArrayToString(capability, 4, 7, false);
      } else if (client == CLI_MCHAT) {
         version = byteArrayToString(capability, 10, 6, false);
      } else if (client == CLI_VMICQ) {
         version = byteArrayToString(capability, 6, 7, false);
      }

      return version;
   }
	
	// Password encryption key
	public static final byte[] PASSENC_KEY = explodeToBytes("F3,26,81,C4,39,86,DB,92,71,A3,B9,E6,53,7A,95,7C", ',', 16);

	// Online status (set values)
	public static final int SET_STATUS_AWAY		= 0x0001;
	public static final int SET_STATUS_DND		= 0x0013;
	public static final int SET_STATUS_NA			= 0x0005;
	public static final int SET_STATUS_OCCUPIED	= 0x0011;
	public static final int SET_STATUS_CHAT		= 0x0020;
	public static final int SET_STATUS_INVISIBLE	= 0x0100;
	public static final int SET_STATUS_EVIL		= 0x3000;
	public static final int SET_STATUS_DEPRESSION	= 0x4000;
	public static final int SET_STATUS_HOME		= 0x5000;
	public static final int SET_STATUS_WORK		= 0x6000;
	public static final int SET_STATUS_LUNCH		= 0x2001;
	public static final int SET_STATUS_ONLINE		= 0x0000;
	
	// Counter variable
	private static int counter = 0;

	public synchronized static int getCounter()
	{
		counter++;
		return (counter);
	}
	
	public static String toHexString(byte[] b)
	{
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			//	look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

			//	look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
			sb.append(" ");
			if ((i != 0) && ((i % 15) == 0))
			{
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	//	table to convert a nibble to a hex char.
	private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	// Extracts the byte from the buffer (buf) at position off
	public static int getByte(byte[] buf, int off)
	{
		int val;
		val = ((int) buf[off]) & 0x000000FF;
		return (val);
	}

	// Puts the specified byte (val) into the buffer (buf) at position off
	public static void putByte(byte[] buf, int off, int val)
	{
		buf[off] = (byte) (val & 0x000000FF);
	}

	// Extracts the word from the buffer (buf) at position off using the specified byte ordering (bigEndian)
	public static int getWord(byte[] buf, int off, boolean bigEndian)
	{
		int val;
		if (bigEndian)
		{
			val = (((int) buf[off]) << 8) & 0x0000FF00;
			val |= (((int) buf[++off])) & 0x000000FF;
		}
		else // Little endian
		{
			val = (((int) buf[off])) & 0x000000FF;
			val |= (((int) buf[++off]) << 8) & 0x0000FF00;
		}
		return (val);
	}
	
	static public DataInputStream getDataInputStream(byte[] array, int offset)
	{
		return new DataInputStream(new ByteArrayInputStream(array, offset, array.length-offset));
	}
	
	static public int getWord(DataInputStream stream, boolean bigEndian) throws IOException
	{
		return bigEndian ?	stream.readUnsignedShort() : ((int)stream.readByte() & 0x00FF) | (((int)stream.readByte() << 8) & 0xFF00);
	}
	
	static public String readAsciiz(DataInputStream stream) throws IOException
	{
		int len = Util.getWord(stream, false);
		if (len == 0) return new String();
		byte[] buffer = new byte[len];
		stream.readFully(buffer);
		return Util.byteArrayToString(buffer); 
	}
	
	static public void writeWord(ByteArrayOutputStream stream, int value, boolean bigEndian)
	{
		if (bigEndian)
		{
			stream.write(((value & 0xFF00)>>8) & 0xFF);
			stream.write(value & 0xFF);
		}
		else
		{
			stream.write(value & 0xFF);
			stream.write(((value & 0xFF00) >> 8) & 0xFF);
		}
	}
	
	static public void writeByteArray(ByteArrayOutputStream stream, byte[] array)
	{
		try
		{
			stream.write(array);
		}
		catch (Exception e)
		{
			//System.out.println("Util.writeByteArray: " + e.toString());
		}
	}
	
	static public void writeDWord(ByteArrayOutputStream stream, int value, boolean bigEndian)
	{
		if (bigEndian)
		{
			stream.write(((value & 0xFF000000) >> 24) & 0xFF);
			stream.write(((value & 0xFF0000) >> 16) & 0xFF);
			stream.write(((value & 0xFF00) >> 8) & 0xFF);
			stream.write(value & 0xFF);
		}
		else
		{
			stream.write(value & 0xFF);
			stream.write(((value & 0xFF00) >> 8) & 0xFF);
			stream.write(((value & 0xFF0000) >> 16) & 0xFF);
			stream.write(((value & 0xFF000000) >> 24) & 0xFF);
		}
	}
	
	static public void writeByte(ByteArrayOutputStream stream, int value)
	{
		stream.write(value);
	}
	
	static public void writeLenAndString(ByteArrayOutputStream stream, String value, boolean utf8)
	{
		byte[] raw = Util.stringToByteArray(value, utf8);
		writeWord(stream, raw.length, true);
		stream.write(raw, 0, raw.length);
	}

	static public void writeLenLEAndStringAsciiz(ByteArrayOutputStream stream, String value)
	{
		byte[] raw = Util.stringToByteArray(value, false);
		writeWord(stream, raw.length + 1, false);
		writeByteArray(stream, raw);
		writeByte(stream, 0);
	}

	static public void writeAsciizTLV(int type, ByteArrayOutputStream stream, String value, boolean bigEndian)
	{
		writeWord(stream, type, bigEndian);
		byte[] raw = Util.stringToByteArray(value);
		writeWord(stream, raw.length + 3, false);
		writeWord(stream, raw.length + 1, false);
		stream.write(raw, 0, raw.length);
		stream.write(0);
	}

	static public void writeAsciizTLV(int type, ByteArrayOutputStream stream, String value)
	{
		writeAsciizTLV(type, stream, value, true);
	}

	static public void writeAsciizTLVInterest(int type, ByteArrayOutputStream stream, int code, String value)
	{
		writeWord(stream, type, false);
		byte[] raw = Util.stringToByteArray(value);
		writeWord(stream, raw.length + 5, false);
		writeWord(stream, code, false);
		writeWord(stream, raw.length + 1, false);
		stream.write(raw, 0, raw.length);
		stream.write(0);
	}

	static public void writeTLV(int type, ByteArrayOutputStream stream, ByteArrayOutputStream data)
	{
		writeTLV(type, stream, data, true);
	}
	static public void writeTLV(int type, ByteArrayOutputStream stream, ByteArrayOutputStream data, boolean bigEndian)
	{
		byte[] raw = data.toByteArray();
		writeWord(stream, type, bigEndian);
		writeWord(stream, raw.length, false);
		stream.write(raw, 0, raw.length);
	}

	// Extracts the word from the buffer (buf) at position off using big endian byte ordering
	public static int getWord(byte[] buf, int off)
	{
		return (Util.getWord(buf, off, true));
	}

	// Puts the specified word (val) into the buffer (buf) at position off using the specified byte ordering (bigEndian)
	public static void putWord(byte[] buf, int off, int val, boolean bigEndian)
	{
		if (bigEndian)
		{
			buf[off] = (byte) ((val >> 8) & 0x000000FF);
			buf[++off] = (byte) ((val) & 0x000000FF);
		}
		else   // Little endian
		{
			buf[off] = (byte) ((val) & 0x000000FF);
			buf[++off] = (byte) ((val >> 8) & 0x000000FF);
		}
	}

	// Puts the specified word (val) into the buffer (buf) at position off using big endian byte ordering
	public static void putWord(byte[] buf, int off, int val)
	{
		Util.putWord(buf, off, val, true);
	}

	// Extracts the double from the buffer (buf) at position off using the specified byte ordering (bigEndian)
	public static long getDWord(byte[] buf, int off, boolean bigEndian)
	{
		long val;
		if (bigEndian)
		{
			val = (((long) buf[off]) << 24) & 0xFF000000;
			val |= (((long) buf[++off]) << 16) & 0x00FF0000;
			val |= (((long) buf[++off]) << 8) & 0x0000FF00;
			val |= (((long) buf[++off])) & 0x000000FF;
		}
		else   // Little endian
		{
			val = (((long) buf[off])) & 0x000000FF;
			val |= (((long) buf[++off]) << 8) & 0x0000FF00;
			val |= (((long) buf[++off]) << 16) & 0x00FF0000;
			val |= (((long) buf[++off]) << 24) & 0xFF000000;
		}
		return (val);
	}

	// Extracts the double from the buffer (buf) at position off using big endian byte ordering
	public static long getDWord(byte[] buf, int off)
	{
		return (Util.getDWord(buf, off, true));
	}

	// Puts the specified double (val) into the buffer (buf) at position off using the specified byte ordering (bigEndian)
	public static void putDWord(byte[] buf, int off, long val, boolean bigEndian)
	{
		if (bigEndian)
		{
			buf[off] = (byte) ((val >> 24) & 0x00000000000000FF);
			buf[++off] = (byte) ((val >> 16) & 0x00000000000000FF);
			buf[++off] = (byte) ((val >> 8) & 0x00000000000000FF);
			buf[++off] = (byte) ((val) & 0x00000000000000FF);
		}
		else   // Little endian
		{
			buf[off] = (byte) ((val) & 0x00000000000000FF);
			buf[++off] = (byte) ((val >> 8) & 0x00000000000000FF);
			buf[++off] = (byte) ((val >> 16) & 0x00000000000000FF);
			buf[++off] = (byte) ((val >> 24) & 0x00000000000000FF);
		}
	}

	// Puts the specified double (val) into the buffer (buf) at position off using big endian byte ordering
	public static void putDWord(byte[] buf, int off, long val)
	{
		Util.putDWord(buf, off, val, true);
	}

	// getTlv(byte[] buf, int off) => byte[]
	public static byte[] getTlv(byte[] buf, int off)
	{
		if (off + 4 > buf.length) return (null);   // Length check (#1)
		int length = Util.getWord(buf, off + 2);
		if (off + 4 + length > buf.length) return (null);   // Length check (#2)
		byte[] value = new byte[length];
		System.arraycopy(buf, off + 4, value, 0, length);
		return (value);
	}
	
	// Extracts a string from the buffer (buf) starting at position off, ending at position off+len
	public static String byteArrayToString(byte[] buf, int off, int len, boolean utf8)
	{
		// Length check
		if (buf.length < off + len)
		{
			return (null);
		}

		// Remove \0's at the end
		while ((len > 0) && (buf[off + len - 1] == 0x00))
		{
			len--;
		}

		// Read string in UCS-2BE format
		if (isDataUCS2(buf, off, len))
		{
			return (ucs2beByteArrayToString(buf, off, len));
		}

		// Read string in UTF-8 format
		if (utf8)
		{
			try
			{
				byte[] buf2 = new byte[len + 2];
				Util.putWord(buf2, 0, len);
				System.arraycopy(buf, off, buf2, 2, len);
				ByteArrayInputStream bais = new ByteArrayInputStream(buf2);
				DataInputStream dis = new DataInputStream(bais);
				return (dis.readUTF());
			}
			catch (Exception e)
			{
				// do nothing
			}
		}

		// CP1251 or default character encoding?
		if (Options.getBoolean(Options.OPTION_CP1251_HACK))
		{
			return (byteArray1251ToString(buf, off, len));
		}
		else
		{
			return (new String(buf, off, len));
		}
	}

	// Extracts a string from the buffer (buf) starting at position off, ending at position off+len
	public static String byteArrayToString(byte[] buf, int off, int len)
	{
		return (Util.byteArrayToString(buf, off, len, false));
	}

	// Converts the specified buffer (buf) to a string
	public static String byteArrayToString(byte[] buf, boolean utf8)
	{
		return (Util.byteArrayToString(buf, 0, buf.length, utf8));
	}

	// Converts the specified buffer (buf) to a string
	public static String byteArrayToString(byte[] buf)
	{
		return (Util.byteArrayToString(buf, 0, buf.length, false));
	}

	// Converts the specific 4 byte max buffer to an unsigned long
	public static long byteArrayToLong(byte[] b) 
	{
		long l = 0;
		l |= b[0] & 0xFF;
		l <<= 8;
		l |= b[1] & 0xFF;
		l <<= 8;
		if (b.length > 3)
		{
			l |= b[2] & 0xFF;
			l <<= 8;
			l |= b[3] & 0xFF;
		}
		return l;
	}
	
	// Converts a byte array to a hex string
	public static String byteArrayToHexString(byte[] buf) {
		StringBuffer hexString = new StringBuffer(buf.length);
		String hex;
		for (int i = 0; i < buf.length; i++) {
			hex = Integer.toHexString(0x0100 + (buf[i] & 0x00FF)).substring(1);
			hexString.append((hex.length() < 2 ? "0" : "") + hex);
		}
		return hexString.toString();
	}
	
	// Converts the specified string (val) to a byte array
	public static byte[] stringToByteArray(String val, boolean utf8)
	{
		// Write string in UTF-8 format
		if (utf8)
		{
			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeUTF(val);
				byte[] raw = baos.toByteArray(); 
				byte[] result = new byte[raw.length - 2];
				System.arraycopy(raw, 2, result, 0, raw.length - 2);
				return result;
			}
			catch (Exception e)
			{
				// Do nothing
			}
		}

		// CP1251 or default character encoding?
		if (Options.getBoolean(Options.OPTION_CP1251_HACK))
		{
			return (stringToByteArray1251(val));
		}
		else
		{
			return (val.getBytes());
		}
		
	}

	// Converts the specified string (val) to a byte array
	public static byte[] stringToByteArray(String val)
	{
		return (Util.stringToByteArray(val, false));
	}

	// Converts the specified string to UCS-2BE
	public static byte[] stringToUcs2beByteArray(String val)
	{
		byte[] ucs2be = new byte[val.length() * 2];
		for (int i = 0; i < val.length(); i++)
		{
			Util.putWord(ucs2be, i * 2, (int) val.charAt(i));
		}
		return (ucs2be);
	}

	// Extract a UCS-2BE string from the specified buffer (buf) starting at position off, ending at position off+len
	public static String ucs2beByteArrayToString(byte[] buf, int off, int len)
	{
		// Length check
		// if ((off + len > buf.length) || (buf.length % 2 != 0)) /* old variant */
		if ((off + len > buf.length) || (len % 2 != 0)) /* aspro variant */
		{
			return (null);
		}

		// Convert
		StringBuffer sb = new StringBuffer();
		for (int i = off; i < off + len; i += 2)
		{
			sb.append((char) Util.getWord(buf, i));
		}
		return (sb.toString());
	}

	public static boolean isDataUCS2(byte[] array, int start, int lenght)
	{
		if ((lenght & 1) != 0) return false;
		int end = start + lenght;
		byte b;
		boolean result = true;
		for (int i = start; i < end; i += 2)
		{
			b = array[i];
			if (b > 0 && b < 0x09) return true;
			if (b == 0 && array[i + 1] != 0) return true;
			if (b > 0x20 || b < 0x00) result = false;
		}
		return result;
	}

	// Extracts a UCS-2BE string from the specified buffer (buf)
	public static String ucs2beByteArrayToString(byte[] buf)
	{
		return (Util.ucs2beByteArrayToString(buf, 0, buf.length));
	}
	
	public static void showBytes(byte[] data)
	{
		StringBuffer buffer1 = new StringBuffer(), buffer2 = new StringBuffer(); 
		
		for (int i = 0; i < data.length; i++)
		{
			int charaster = ((int)data[i]) & 0xFF; 
			buffer1.append(charaster < ' ' || charaster >= 128 ? '.' : (char)charaster);
			String hex = Integer.toHexString(((int)data[i]) & 0xFF);
			buffer2.append(hex.length() == 1 ? "0" + hex : hex);
			buffer2.append(" ");
			
			if (((i % 16) == 15) || (i == (data.length - 1))) 
			{
				while (buffer2.length() < 16 * 3) buffer2.append(' ');
				
				buffer1.setLength(0);
				buffer2.setLength(0);
			}
		}
	}
	
	// Removes all CR occurences
	public static String removeCr(String val)
	{
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < val.length(); i++)
		{
			char chr = val.charAt(i);
			if ((chr == 0) || (chr == '\r')) continue;
			result.append(chr);
		}
		return result.toString();
	}
	
	// Restores CRLF sequense from LF
	public static String restoreCrLf(String val)
	{
		StringBuffer result = new StringBuffer();
		int size = val.length();
		for (int i = 0; i < size; i++)
		{
			char chr = val.charAt(i);
			if (chr == '\r') continue;
			if (chr == '\n') result.append("\r\n");
			else result.append(chr);
		}
		return result.toString();
	}
	
	public static String removeClRfAndTabs(String val)
	{
		int len = val.length();
		char[] dst = new char[len];
		for (int i = 0; i < len; i++)
		{
			char chr = val.charAt(i);
			if ((chr == '\n') || (chr == '\r') || (chr == '\t')) chr = ' ';
			dst[i] = chr; 
		}
		return new String(dst, 0, len);  
	}

	// Compare to byte arrays (return true if equals, false otherwise)
	public static boolean byteArrayEquals(byte[] buf1, int off1, byte[] buf2, int off2, int len)
	{
		// Length check
		if ((off1 + len > buf1.length) || (off2 + len > buf2.length))
		{
			return (false);
		}

		// Compare bytes, stop at first mismatch
		for (int i = 0; i < len; i++)
		{
			if (buf1[off1 + i] != buf2[off2 + i])
			{
				return (false);
			}
		}

		// Return true if this point is reached
		return (true);
	}

	// DeScramble password
	public static byte[] decipherPassword(byte[] buf)
	{
		byte[] ret = new byte[buf.length];
		for (int i = 0; i < buf.length; i++)
		{
			ret[i] = (byte) (buf[i] ^ Util.PASSENC_KEY[i % 16]);
		}
		return (ret);
	}

	// translateStatus(long status) => void
	 public static int translateStatusReceived(int status, ContactItem contact)
	 {
        if (status == ContactList.STATUS_OFFLINE) return ContactList.STATUS_OFFLINE;
        if (contact != null)
        {
            if (contact.hasCapability(CAPF_STATUS_DEPRESSION)) return ContactList.STATUS_DEPRESSION;
            if (contact.hasCapability(CAPF_STATUS_CHAT)) return ContactList.STATUS_CHAT;
            if (contact.hasCapability(CAPF_STATUS_HOME)) return ContactList.STATUS_HOME;
            if (contact.hasCapability(CAPF_STATUS_WORK)) return ContactList.STATUS_WORK;
            if (contact.hasCapability(CAPF_STATUS_LUNCH)) return ContactList.STATUS_LUNCH;
            if (contact.hasCapability(CAPF_STATUS_EVIL)) return ContactList.STATUS_EVIL;
        }
        if ((status & 0x0100) != 0 && (status & 0xFFFF) != 0x0100) status &= ~0x0100;
        if ((status & ContactList.STATUS_DND) != 0) return ContactList.STATUS_DND;
        if ((status & ContactList.STATUS_INVISIBLE) != 0) return ContactList.STATUS_INVISIBLE;
        if ((status & ContactList.STATUS_INVIS_ALL) != 0) return ContactList.STATUS_INVISIBLE;
        if ((status & ContactList.STATUS_OCCUPIED) != 0) return ContactList.STATUS_OCCUPIED;
        if ((status & ContactList.STATUS_NA) != 0) return ContactList.STATUS_NA;
        if ((status & ContactList.STATUS_AWAY) != 0) return ContactList.STATUS_AWAY;
        return ContactList.STATUS_ONLINE;
    }

	// Get online status set value
	public static int translateStatusSend(int status)
	{
		if (status == ContactList.STATUS_AWAY) return (Util.SET_STATUS_AWAY);
		if (status == ContactList.STATUS_DND) return (Util.SET_STATUS_DND);
		if (status == ContactList.STATUS_INVISIBLE) return (Util.SET_STATUS_INVISIBLE);
		if (status == ContactList.STATUS_INVIS_ALL) return (Util.SET_STATUS_INVISIBLE);
		if (status == ContactList.STATUS_NA) return (Util.SET_STATUS_NA);
		if (status == ContactList.STATUS_OCCUPIED) return (Util.SET_STATUS_OCCUPIED);
		return (Util.SET_STATUS_ONLINE);
	}

	//  If the numer has only one digit add a 0
	public static String makeTwo(int number)
	{
		if (number < 10)
		{
			return ("0" + String.valueOf(number));
		}
		else
		{
			return (String.valueOf(number));
		}
	}
	
	// Byte array IP to String
	public static String ipToString(byte[] ip)
	{
		if (ip == null) return null;
		StringBuffer strIP = new StringBuffer();

		for (int i = 0; i < 4; i++)
		{
			int tmp = (int) ip[i] & 0xFF;
			if (strIP.length() != 0) strIP.append('.');
			strIP.append(tmp);
		}

		return strIP.toString();
	}
	
	// String IP to byte array
	public static byte[] ipToByteArray(String ip)
	{
		byte[] arrIP = explodeToBytes(ip, '.', 10);
		return ((arrIP == null) || (arrIP.length != 4)) ? null : arrIP;
	}
	
	// #sijapp cond.if modules_PROXY is "true"#
	// Try to parse string IP
	public static boolean isIP(String ip)
	{
		boolean isTrueIp = false;
		try
		{
			isTrueIp = (ipToByteArray(ip) != null);
		} 
		catch (NumberFormatException e)
		{
			return false;
		}
		return isTrueIp;
	}

	// #sijapp cond.end #
	
	// Create a random roster id
	public static int createRandomId()
	{
		ContactList.getGroupItems();
		ContactList.getContactItems();
		Random rand = new Random(System.currentTimeMillis());
		int randint = rand.nextInt();
		if (randint < 0) randint = -randint;
		return randint % 0x6FFF + 0x1000;
	}
	
	// Check is data array utf-8 string
	public static boolean isDataUTF8(byte[] array, int start, int lenght)
	{
		if (lenght == 0) return false;
		if (array.length < (start + lenght)) return false;
		
		for (int i = start, len = lenght; len > 0;)
		{
			int seqLen = 0;
			byte bt = array[i++];
			len--;
			
			if	  ((bt&0xE0) == 0xC0) seqLen = 1;
			else if ((bt&0xF0) == 0xE0) seqLen = 2;
			else if ((bt&0xF8) == 0xF0) seqLen = 3;
			else if ((bt&0xFC) == 0xF8) seqLen = 4;
			else if ((bt&0xFE) == 0xFC) seqLen = 5;
			
			if (seqLen == 0)
			{
				if ((bt&0x80) == 0x80) return false;
				else continue;
			}
			
			for (int j = 0; j < seqLen; j++)
			{
				if (len == 0) return false;
				bt = array[i++];
				if ((bt&0xC0) != 0x80) return false;
				len--;
			}
			if (len == 0) break;
		}
		return true;
	}
  
	// #sijapp cond.if modules_TRAFFIC is "true" #
	// Returns String value of cost value
	public static String intToDecimal(int value)
	{
		String costString = "";
		String afterDot = "";
		try
		{
			if (value != 0) {
				costString = Integer.toString(value / 1000) + ".";
				afterDot = Integer.toString(value % 1000);
				while (afterDot.length() != 3)
				{
					afterDot = "0" + afterDot;
				}
				while ((afterDot.endsWith("0")) && (afterDot.length() > 2))
				{
					afterDot = afterDot.substring(0, afterDot.length() - 1);
				}
				costString = costString + afterDot;
				return costString;
			}
			else
			{
				return new String("0.0");
			}
		}
		catch (Exception e)
		{
			return new String("0.0");
		}
	}

	// Extracts the number value form String
	public static int decimalToInt(String string)
	{
		int value = 0;
		byte i = 0;
		char c = new String(".").charAt(0);
		try
		{
			for (i = 0; i < string.length(); i++)
			{
				if (c != string.charAt(i))
				{
					break;
				}
			}
			if (i == string.length()-1)
			{
				value = Integer.parseInt(string) * 1000;
				return (value);
			}
			else
			{
				while (c != string.charAt(i))
				{
					i++;
				}
				value = Integer.parseInt(string.substring(0, i)) * 1000;
				string = string.substring(i + 1, string.length());
				while (string.length() > 3)
				{
					string = string.substring(0, string.length() - 1);
				}
				while (string.length() < 3)
				{
					string = string + "0";
				}
				value = value + Integer.parseInt(string);
				return value;
			}
		}
		catch (Exception e)
		{
			return (0);
		}
	}
	// #sijapp cond.end#

	// Convert gender code to string
	static public String genderToString(int gender)
	{
		switch (gender)
		{
		case 1:
			return ResourceBundle.getString("female");
		case 2:
			return ResourceBundle.getString("male");
		}
		return new String();
	}

	static public int stringToGender(String gender)
	{
		if (gender == ResourceBundle.getString("female"))
		{
			return 1;
		}
		else if (gender == ResourceBundle.getString("male"))
		{
			return 2;
		}
		return 0;
	}
	
    // Converts an Unicode string into CP1251 byte array
    public static byte[] stringToByteArray1251(String s)
    {
        byte buf[] = new byte[s.length()];
        int size = s.length();
        for(int i = 0; i < size; i++)
        {
            char ch = s.charAt(i);
            switch (ch) {
            case 1025:
                buf[i] = -88;
                break;
            case 1105:
                buf[i] = -72;
                break;
            /* Ukrainian CP1251 chars section */
            case 1168:
                buf[i] = -91;
                break;
            case 1028:
                buf[i] = -86;
                break;
            case 1031:
                buf[i] = -81;
                break;
            case 1030:
                buf[i] = -78;
                break;
            case 1110:
                buf[i] = -77;
                break;
            case 1169:
                buf[i] = -76;
                break;
            case 1108:
                buf[i] = -70;
                break;
            case 1111:
                buf[i] = -65;
                break;
            /* end of section */

            default:
                if (ch >= '\u0410' && ch <= '\u044F')
                {
                    buf[i] = (byte) ((ch - 1040) + 192);
                }
                else
                {
                    buf[i] = (byte)((int)ch & 0xFF);
                }
                break;
            }
        }
        return buf;
    }

    // Converts an CP1251 byte array into an Unicode string
    public static String byteArray1251ToString(byte buf[], int pos, int len)
    {
        int end = pos + len;
        StringBuffer stringbuffer = new StringBuffer(len);
        for(int i = pos; i < end; i++)
        {
            int ch = buf[i] & 0xff;
            switch (ch)
            {
            case 168:
                stringbuffer.append('\u0401');
                break;
            case 184:
                stringbuffer.append('\u0451');
                break;
            /* Ukrainian CP1251 chars section */
            case 165:
                stringbuffer.append('\u0490');
                break;
            case 170:
                stringbuffer.append('\u0404');
                break;
            case 175:
                stringbuffer.append('\u0407');
                break;
            case 178:
                stringbuffer.append('\u0406');
                break;
            case 179:
                stringbuffer.append('\u0456');
                break;
            case 180:
                stringbuffer.append('\u0491');
                break;
            case 186:
                stringbuffer.append('\u0454');
                break;
            case 191:
                stringbuffer.append('\u0457');
                break;
            /* end of section */

            default:
                try
                {
                    if (ch >= 192 && ch <= 255)
                    {
                        stringbuffer.append((char) ((1040 + ch) - 192));
                    }
                    else
                    {
                        stringbuffer.append((char)ch);
                    }
                }
                catch (Exception e) {}
                break;
            }
        }
        return stringbuffer.toString();
    }
	
	/*/////////////////////////////////////////////////////////////////////////
	//																	   //
	//				 METHODS FOR DATE AND TIME PROCESSING				  //
	//																	   //	
	/////////////////////////////////////////////////////////////////////////*/

	private final static String error_str = "***error***";
	final public static int TIME_SECOND = 0;
	final public static int TIME_MINUTE = 1;
	final public static int TIME_HOUR   = 2;
	final public static int TIME_DAY	= 3;
	final public static int TIME_MON	= 4;
	final public static int TIME_YEAR   = 5;
	
	final private static byte[] dayCounts = explodeToBytes("31,28,31,30,31,30,31,31,30,31,30,31", ',', 10);
	
	final private static int[] monthIndexes = 
	{ 
		Calendar.JANUARY,
		Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
		Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
		Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER 
	};
	
	static private int convertDateMonToSimpleMon(int dateMon)
	{
		for (int i = 0; i < monthIndexes.length; i++) if (monthIndexes[i] == dateMon) return i + 1;
		return -1;
	}

	/* Creates current date (GMT or local) */
	public static long createCurrentDate(boolean gmt)
	{
		return createCurrentDate(gmt, false);
	}

	public static long createCurrentDate(boolean gmt, boolean onlyDate)
	{
		long result;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		if (onlyDate)
		{
			result = createLongTime
					  (
					   	  calendar.get(Calendar.YEAR),
					   	  convertDateMonToSimpleMon(calendar.get(Calendar.MONTH)),
					   	  calendar.get(Calendar.DAY_OF_MONTH),
					   	  0,
					   	  0,
					   	  0
					  );
		}
		else
		{
			result = createLongTime
					  (
					   	  calendar.get(Calendar.YEAR),
					   	  convertDateMonToSimpleMon(calendar.get(Calendar.MONTH)),
					   	  calendar.get(Calendar.DAY_OF_MONTH),
					   	  calendar.get(Calendar.HOUR_OF_DAY),
					   	  calendar.get(Calendar.MINUTE),
					   	  calendar.get(Calendar.SECOND)
					  );
		}

		/* convert result to GMT time */
		long diff = Options.getInt(Options.OPTIONS_LOCAL_OFFSET);
		result += (diff * 3600);
		
		/* returns GMT or local time */
		return gmt ? result : gmtTimeToLocalTime(result);
	}
	
	/* Show date string */
	public static String getDateString(boolean onlyTime, boolean fullTime, long date)
	{
		if (date == 0) return error_str;
		
		int[] loclaDate = createDate(date);
		
		StringBuffer sb = new StringBuffer();
		
		if (!onlyTime)
		{
			sb.append(Util.makeTwo(loclaDate[TIME_DAY]))
			  .append('.')
			  .append(Util.makeTwo(loclaDate[TIME_MON]))
			  .append('.')
			  .append(loclaDate[TIME_YEAR])
			  .append(' ');
		}
		
		sb.append(Util.makeTwo(loclaDate[TIME_HOUR]))
		  .append(':')
		  .append(Util.makeTwo(loclaDate[TIME_MINUTE]));
		
		if (fullTime)
		{
			sb.append(':')
			  .append(Util.makeTwo(loclaDate[TIME_SECOND]));
		}
		
		return sb.toString();
	}
	
	/* Generates seconds count from 1st Jan 1970 till mentioned date */ 
	public static long createLongTime(int year, int mon, int day, int hour, int min, int sec)
	{
		int day_count, i, febCount;

		day_count = (year - 1970) * 365+day;
		day_count += (year - 1968) / 4;
		if (year >= 2000) day_count--;

		if ((year % 4 == 0) && (year != 2000))
		{
			day_count--;
			febCount = 29;
		}
		else febCount = 28;

		for (i = 0; i < mon - 1; i++) day_count += (i == 1) ? febCount : dayCounts[i];

		return day_count * 24L * 3600L + hour * 3600L + min * 60L + sec;
	}
	
	// Creates array of calendar values form value of seconds since 1st jan 1970 (GMT)
	public static int[] createDate(long value)
	{
		int total_days, last_days, i;
		int sec, min, hour, day, mon, year;

		sec = (int) (value % 60);

		min = (int) ((value / 60) % 60); // min
		value -= 60 * min;

		hour = (int) ((value / 3600) % 24); // hour
		value -= 3600 * hour;

		total_days = (int) (value / (3600 * 24));

		year = 1970;
		for (;;)
		{
			last_days = total_days - ((year % 4 == 0) && (year != 2000) ? 366 : 365);
			if (last_days <= 0) break;
			total_days = last_days;
			year++;
		} // year

		int febrDays = ((year % 4 == 0) && (year != 2000)) ? 29 : 28;

		mon = 1;
		for (i = 0; i < 12; i++)
		{
			last_days = total_days - ((i == 1) ? febrDays : dayCounts[i]);
			if (last_days <= 0) break;
			mon++;
			total_days = last_days;
		} // mon

		day = total_days; // day

		return new int[] { sec, min, hour, day, mon, year };
	}
	
	public static String getDateString(boolean onlyTime, boolean fullTime)
	{
		return getDateString(onlyTime, fullTime, createCurrentDate(false));
	}
	
	public static long gmtTimeToLocalTime(long gmtTime)
	{
		long diff = Options.getInt(Options.OPTIONS_GMT_OFFSET);
		return gmtTime + diff * 3600L;
	}	
	
	public static String longitudeToString(long seconds)
	{
		StringBuffer buf = new StringBuffer();
		int days = (int)(seconds / 86400);
		seconds %= 86400;
		int hours = (int)(seconds / 3600);
		seconds %= 3600;
		int minutes = (int)(seconds / 60);
		
		if (days != 0) buf.append(days).append(' ').append(ResourceBundle.getString("days")).append(' ');
		if (hours != 0) buf.append(hours).append(' ').append(ResourceBundle.getString("hours")).append(' ');
		if (minutes != 0) buf.append(minutes).append(' ').append(ResourceBundle.getString("minutes"));
		
		return buf.toString();
	}

	/*====================================================*/
	/*													*/
	/*					 MD5 stuff					  */
	/*													*/
	/*====================================================*/

	static final byte[] AIM_MD5_STRING = explodeToBytes("*AOL Instant Messenger (SM)", ',', 16);
	static final int S11 = 7;
	static final int S12 = 12;
	static final int S13 = 17;
	static final int S14 = 22;
	static final int S21 = 5;
	static final int S22 = 9;
	static final int S23 = 14;
	static final int S24 = 20;
	static final int S31 = 4;
	static final int S32 = 11;
	static final int S33 = 16;
	static final int S34 = 23;
	static final int S41 = 6;
	static final int S42 = 10;
	static final int S43 = 15;
	static final int S44 = 21;
	static final byte[] PADDING = explodeToBytes("-128,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", ',', 10);
	static private long[] state = new long[4];
	static private long[] count = new long[2];
	static private byte[] buffer = new byte[64];
	static private byte[] digest = new byte[16];
	static public byte[] calculateMD5(byte[] inbuf)
	{
		md5Init();
		md5Update(inbuf, inbuf.length);
		md5Final();
		return digest;
	}
	static private void md5Init()
	{
		count[0] = 0L;
		count[1] = 0L;
		state[0] = 0x67452301L;
		state[1] = 0xefcdab89L;
		state[2] = 0x98badcfeL;
		state[3] = 0x10325476L;
		return;
	}
	static private long F(long x, long y, long z)
	{
		return (x & y) | ((~x) & z);
	}
	static private long G(long x, long y, long z)
	{
		return (x & z) | (y & (~z));
	}
	static private long H(long x, long y, long z)
	{
		return x ^ y ^ z;
	}
	static private long I(long x, long y, long z)
	{
		return y ^ (x | (~z));
	}
	static private long FF(long a, long b, long c,
		long d, long x, long s,long ac)
	{
		a += F (b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}
	static private long GG(long a, long b, long c,
		long d, long x, long s,long ac)
	{
		a += G (b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}
	static private long HH(long a, long b, long c,
		long d, long x, long s,long ac)
	{
		a += H (b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}
	static private long II(long a, long b, long c,
		long d, long x, long s,long ac)
	{
		a += I (b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}
	static private void md5Update(byte[] inbuf, int inputLen)
	{
		int i, index, partLen;
		byte[] block = new byte[64];
		index = (int)(count[0] >>> 3) & 0x3F;
		if ((count[0] += (inputLen << 3)) < (inputLen << 3))
		count[1]++;
		count[1] += (inputLen >>> 29);
		partLen = 64 - index;
		if (inputLen >= partLen)
		{
			md5Memcpy(buffer, inbuf, index, 0, partLen);
			md5Transform(buffer);
			for (i = partLen; i + 63 < inputLen; i += 64)
			{
				md5Memcpy(block, inbuf, 0, i, 64);
				md5Transform (block);
			}
			index = 0;
		} else i = 0;
		md5Memcpy(buffer, inbuf, index, i, inputLen - i);
	}
	static private void md5Final ()
	{
		byte[] bits = new byte[8];
		int index, padLen;
		Encode (bits, count, 8);
		index = (int)(count[0] >>> 3) & 0x3f;
		padLen = (index < 56) ? (56 - index) : (120 - index);
		md5Update (PADDING, padLen);
		md5Update(bits, 8);
		Encode (digest, state, 16);
	}
	static private void md5Memcpy (byte[] output, byte[] input,
		int outpos, int inpos, int len)
	{
		int i;
		for (i = 0; i < len; i++)
		output[outpos + i] = input[inpos + i];
	}
	static private void md5Transform (byte block[])
	{
		long a = state[0], b = state[1], c = state[2], d = state[3];
		long[] x = new long[16];
		Decode (x, block, 64);
		a = FF (a, b, c, d, x[0],  S11, 0xd76aa478L); /* 1 */
		d = FF (d, a, b, c, x[1],  S12, 0xe8c7b756L); /* 2 */
		c = FF (c, d, a, b, x[2],  S13, 0x242070dbL); /* 3 */
		b = FF (b, c, d, a, x[3],  S14, 0xc1bdceeeL); /* 4 */
		a = FF (a, b, c, d, x[4],  S11, 0xf57c0fafL); /* 5 */
		d = FF (d, a, b, c, x[5],  S12, 0x4787c62aL); /* 6 */
		c = FF (c, d, a, b, x[6],  S13, 0xa8304613L); /* 7 */
		b = FF (b, c, d, a, x[7],  S14, 0xfd469501L); /* 8 */
		a = FF (a, b, c, d, x[8],  S11, 0x698098d8L); /* 9 */
		d = FF (d, a, b, c, x[9],  S12, 0x8b44f7afL); /* 10 */
		c = FF (c, d, a, b, x[10], S13, 0xffff5bb1L); /* 11 */
		b = FF (b, c, d, a, x[11], S14, 0x895cd7beL); /* 12 */
		a = FF (a, b, c, d, x[12], S11, 0x6b901122L); /* 13 */
		d = FF (d, a, b, c, x[13], S12, 0xfd987193L); /* 14 */
		c = FF (c, d, a, b, x[14], S13, 0xa679438eL); /* 15 */
		b = FF (b, c, d, a, x[15], S14, 0x49b40821L); /* 16 */
		a = GG (a, b, c, d, x[1],  S21, 0xf61e2562L); /* 17 */
		d = GG (d, a, b, c, x[6],  S22, 0xc040b340L); /* 18 */
		c = GG (c, d, a, b, x[11], S23, 0x265e5a51L); /* 19 */
		b = GG (b, c, d, a, x[0],  S24, 0xe9b6c7aaL); /* 20 */
		a = GG (a, b, c, d, x[5],  S21, 0xd62f105dL); /* 21 */
		d = GG (d, a, b, c, x[10], S22, 0x2441453L); /* 22 */
		c = GG (c, d, a, b, x[15], S23, 0xd8a1e681L); /* 23 */
		b = GG (b, c, d, a, x[4],  S24, 0xe7d3fbc8L); /* 24 */
		a = GG (a, b, c, d, x[9],  S21, 0x21e1cde6L); /* 25 */
		d = GG (d, a, b, c, x[14], S22, 0xc33707d6L); /* 26 */
		c = GG (c, d, a, b, x[3],  S23, 0xf4d50d87L); /* 27 */
		b = GG (b, c, d, a, x[8],  S24, 0x455a14edL); /* 28 */
		a = GG (a, b, c, d, x[13], S21, 0xa9e3e905L); /* 29 */
		d = GG (d, a, b, c, x[2],  S22, 0xfcefa3f8L); /* 30 */
		c = GG (c, d, a, b, x[7],  S23, 0x676f02d9L); /* 31 */
		b = GG (b, c, d, a, x[12], S24, 0x8d2a4c8aL); /* 32 */
		a = HH (a, b, c, d, x[5],  S31, 0xfffa3942L); /* 33 */
		d = HH (d, a, b, c, x[8],  S32, 0x8771f681L); /* 34 */
		c = HH (c, d, a, b, x[11], S33, 0x6d9d6122L); /* 35 */
		b = HH (b, c, d, a, x[14], S34, 0xfde5380cL); /* 36 */
		a = HH (a, b, c, d, x[1],  S31, 0xa4beea44L); /* 37 */
		d = HH (d, a, b, c, x[4],  S32, 0x4bdecfa9L); /* 38 */
		c = HH (c, d, a, b, x[7],  S33, 0xf6bb4b60L); /* 39 */
		b = HH (b, c, d, a, x[10], S34, 0xbebfbc70L); /* 40 */
		a = HH (a, b, c, d, x[13], S31, 0x289b7ec6L); /* 41 */
		d = HH (d, a, b, c, x[0],  S32, 0xeaa127faL); /* 42 */
		c = HH (c, d, a, b, x[3],  S33, 0xd4ef3085L); /* 43 */
		b = HH (b, c, d, a, x[6],  S34, 0x4881d05L); /* 44 */
		a = HH (a, b, c, d, x[9],  S31, 0xd9d4d039L); /* 45 */
		d = HH (d, a, b, c, x[12], S32, 0xe6db99e5L); /* 46 */
		c = HH (c, d, a, b, x[15], S33, 0x1fa27cf8L); /* 47 */
		b = HH (b, c, d, a, x[2],  S34, 0xc4ac5665L); /* 48 */
		a = II (a, b, c, d, x[0],  S41, 0xf4292244L); /* 49 */
		d = II (d, a, b, c, x[7],  S42, 0x432aff97L); /* 50 */
		c = II (c, d, a, b, x[14], S43, 0xab9423a7L); /* 51 */
		b = II (b, c, d, a, x[5],  S44, 0xfc93a039L); /* 52 */
		a = II (a, b, c, d, x[12], S41, 0x655b59c3L); /* 53 */
		d = II (d, a, b, c, x[3],  S42, 0x8f0ccc92L); /* 54 */
		c = II (c, d, a, b, x[10], S43, 0xffeff47dL); /* 55 */
		b = II (b, c, d, a, x[1],  S44, 0x85845dd1L); /* 56 */
		a = II (a, b, c, d, x[8],  S41, 0x6fa87e4fL); /* 57 */
		d = II (d, a, b, c, x[15], S42, 0xfe2ce6e0L); /* 58 */
		c = II (c, d, a, b, x[6],  S43, 0xa3014314L); /* 59 */
		b = II (b, c, d, a, x[13], S44, 0x4e0811a1L); /* 60 */
		a = II (a, b, c, d, x[4],  S41, 0xf7537e82L); /* 61 */
		d = II (d, a, b, c, x[11], S42, 0xbd3af235L); /* 62 */
		c = II (c, d, a, b, x[2],  S43, 0x2ad7d2bbL); /* 63 */
		b = II (b, c, d, a, x[9],  S44, 0xeb86d391L); /* 64 */
		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}
	static private void Encode (byte[] output, long[] input, int len)
	{
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4)
		{
			output[j] = (byte)(input[i] & 0xffL);
			output[j + 1] = (byte)((input[i] >>> 8) & 0xffL);
			output[j + 2] = (byte)((input[i] >>> 16) & 0xffL);
			output[j + 3] = (byte)((input[i] >>> 24) & 0xffL);
		}
	}
	static private void Decode (long[] output, byte[] input, int len)
	{
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4)
			output[i] = b2iu(input[j]) |
				(b2iu(input[j + 1]) << 8) |
				(b2iu(input[j + 2]) << 16) |
				(b2iu(input[j + 3]) << 24);
		return;
	}
	public static long b2iu(byte b)
	{
		return b < 0 ? b & 0x7F + 128 : b;
	}

	public static String getCurrentDay()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String day = "";
		
		switch (cal.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.MONDAY:
				day = "monday";
				break;

			case Calendar.TUESDAY:
				day = "tuesday";
				break;
				
			case Calendar.WEDNESDAY:
				day = "wednesday";
				break;
				
			case Calendar.THURSDAY:
				day = "thursday";
				break;
				
			case Calendar.FRIDAY:
				day = "friday";
				break;
				
			case Calendar.SATURDAY:
				day = "saturday";
				break;
				
			case Calendar.SUNDAY:
				day = "sunday";
				break;
		}
		return ResourceBundle.getString(day);
	}
	
	private static boolean isURLChar(char chr, boolean before)
	{
		if (before) return    ((chr >= 'A') && (chr <= 'Z'))
							|| ((chr >= 'a') && (chr <= 'z'))
							|| ((chr >= '0') && (chr <= '9'))
							|| (chr == '-');
		if ((chr <= ' ') || (chr == '\"')) return false;
		return ((chr & 0xFF00) == 0);
	}

	public static Vector parseMessageForURL(String msg)
	{
		if (msg.indexOf('.') == -1) return null;
		
		Vector result = new Vector();
		int size = msg.length();
		int findIndex = 0, beginIdx, endIdx;
		for (;;)
		{
			if (findIndex >= size) break;
			int ptIndex = msg.indexOf('.', findIndex);
			if (ptIndex == -1) break;
			
			for (beginIdx = ptIndex - 1; beginIdx >= 0; beginIdx--) if (!isURLChar(msg.charAt(beginIdx), true)) break;
			for (endIdx = ptIndex + 1; endIdx < size; endIdx++) if (!isURLChar(msg.charAt(endIdx), false)) break;
			if ((beginIdx == -1) || !isURLChar(msg.charAt(beginIdx), true)) beginIdx++;
		
			findIndex = endIdx;
			if ((ptIndex == beginIdx) || (endIdx-ptIndex < 2)) continue;
			
			result.addElement("http:\57\57" + msg.substring(beginIdx, endIdx));
		}
		
		return (result.size() == 0) ? null : result;
	}
	
	static public int strToIntDef(String str, int defValue)
	{
		if (str == null) return defValue;
		int result = defValue;
		try
		{
			result = Integer.parseInt(str);
		}
		catch (Exception e) {}
		return result;
	}
	
	static public String replaceStr(String original, String from, String to)
	{
		int index = original.indexOf(from);
		if (index == -1) return original;
		return original.substring(0, index)+to+original.substring(index+from.length(), original.length());
	}
	
	static public byte[] explodeToBytes(String text, char serparator, int radix)
	{
		String[] strings = explode(text, serparator);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
		
		for (int i = 0; i < strings.length; i++)
		{
			String item = strings[i];
			if (item.charAt(0) == '*') 
				for (int j = 1; j < item.length(); j++) bytes.write((byte)item.charAt(j));
			else 
				bytes.write(Integer.parseInt(item, radix));
			
		}
		return bytes.toByteArray();
	}
	
	/* Divide text to array of parts using serparator charaster */
	static public String[] explode(String text, char serparator)
	{
		Vector tmp = new Vector();
		StringBuffer strBuf = new StringBuffer();
		int len = text.length();
		for (int i = 0; i < len; i++)
		{
			char chr = text.charAt(i);
			if (chr == serparator)
			{
				tmp.addElement(strBuf.toString());
				strBuf.delete(0, strBuf.length());
			}
			else strBuf.append(chr);
		}
		tmp.addElement(strBuf.toString());
		String[] result = new String[tmp.size()];
		tmp.copyInto(result);
		return result; 
	}
	
	// Merge two received capabilities into one byte array
	public static byte[] mergeCapabilities(byte[] capabilities_old, byte[] capabilities_new)
	{
		if (capabilities_new == null)
			return capabilities_old;
		if (capabilities_old == null)
			return capabilities_new;
		
		// Extend new capabilities to match with old ones
		byte[] extended_new = new byte[capabilities_new.length * 8];
		for (int i = 0; i < capabilities_new.length; i += 2)
		{
			System.arraycopy(CAP_OLD_HEAD, 0, extended_new, (i * 8), CAP_OLD_HEAD.length);
			System.arraycopy(capabilities_new, i, extended_new, ((i * 8) + CAP_OLD_HEAD.length), 2);
			System.arraycopy(CAP_OLD_TAIL, 0, extended_new, ((i * 8) + CAP_OLD_HEAD.length + 2), CAP_OLD_TAIL.length);
		}
		// Check for coexisting capabilities and merge
		boolean found = false;
		for (int i = 0; i < capabilities_old.length; i += 16)
		{
			byte[] tmp_old = new byte[16];
			System.arraycopy(capabilities_old, i, tmp_old, 0, 16);
			for (int j = 0; j < extended_new.length; j += 16)
			{
				byte[] tmp_new = new byte[16];
				System.arraycopy(extended_new, j, tmp_new, 0, 16);
				if (tmp_old == tmp_new)
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				//System.out.println("Merge capability");
				byte[] merged = new byte[extended_new.length + 16];
				System.arraycopy(extended_new, 0, merged, 0, extended_new.length);
				System.arraycopy(tmp_old, 0, merged, extended_new.length, tmp_old.length);
				extended_new = merged;
				found = false;
			}
		}
	return extended_new;
	}

   /****************************ICQ XTraz Support********************************/

	public static String DeMangleXml(String s1)
	{
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		StringBuffer stringbuffer = new StringBuffer(s1.length());
		do
		{
			if(i1 < 0 || (i1 = s1.indexOf("&", k1)) < 0)
				break;
			stringbuffer.append(s1.substring(j1, i1));
			j1 = i1;
			if((k1 = s1.indexOf(";", j1)) < 0)
				break;
			j1 = k1 + 1;
			String s2;
			if((s2 = s1.substring(i1, k1 + 1)).equals("&lt;"))
				stringbuffer.append("<");
			else
			if(s2.equals("&gt;"))
				stringbuffer.append(">");
			else
			if(s2.equals("&amp;"))
				stringbuffer.append("&");
			else
				stringbuffer.append(s2);
		} while(true);
		if(j1 < s1.length())
			stringbuffer.append(s1.substring(j1));
		return stringbuffer.toString();
	}

	public static String MangleXml(String s1)
	{
		StringBuffer stringbuffer = new StringBuffer(s1.length());
		for(int i1 = 0; i1 < s1.length(); i1++)
		{
			char c1;
			switch(c1 = s1.charAt(i1))
			{
			case 60: // '<'
				stringbuffer.append("&lt;");
				break;

			case 62: // '>'
				stringbuffer.append("&gt;");
				break;

			case 38: // '&'
				stringbuffer.append("&amp;");
				break;

			default:
				stringbuffer.append(c1);
				break;
			}
		}
		return stringbuffer.toString();
	}
	/*****************************************************************************/

	public static Image createThumbnail(Image image, int width, int height)
	{
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		if ((height == 0) && (width != 0))
		{
			height = width * sourceHeight / sourceWidth;
		}
		else if ((width == 0) && (height != 0))
		{
			width = height * sourceWidth / sourceHeight;
		}
		else if (sourceHeight >= sourceWidth)
		{
			width = height * sourceWidth / sourceHeight;
		}
		else
		{
			height = width * sourceHeight / sourceWidth;
		}
		
		Image thumb = Image.createImage(width, height);
		Graphics g = thumb.getGraphics();

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				g.setClip(x, y, 1, 1);
				int dx = x * sourceWidth / width;
				int dy = y * sourceHeight / height;
				g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
			}
		}
		Image immutableThumb = Image.createImage(thumb);

		return immutableThumb;
	}

	/************************* расшифровка RTF сообщений *************************/
	public static String DecodeRTF(String msg) {
	   StringBuffer sb = new StringBuffer();
	   int nl = 0; //уровень вложенности
	   int ps = 0; //текущая позиция
	   int msgSz = msg.length();
	   while(ps<msgSz){
		   char ch = msg.charAt(ps);
		   if(ch=='{'){
			   nl++;
		   }else if(ch=='}')
			   nl--;
			   if(nl==1){ //можно парсить текст
				   ps++;
				   boolean ctrl = false;
				   StringBuffer ctrl2 = new StringBuffer();
				   while(nl==1){
					   ch =  msg.charAt(ps++);
					   if(ch=='{'){
						   nl++;
						   break;
					   }else if(ch=='}'){
						   nl--;
						   break;
					   }else if(ch=='\\'){//слэш
						   if(ctrl2.toString().equals("tab"))
							   sb.append(' ');
						   ctrl2.setLength(0);
						   char ch2 = msg.charAt(ps);
						   if(ch2=='\\'){
							   sb.append('\\');
							   ps++;
							   ctrl = false;
						   }else if(ch2=='\''){//нелатиница
							   char ch3 = (char)Integer.parseInt(msg.substring(ps+1, ps+3), 16);
							   sb.append((char)(ch3 > 127 ? ch3 == 0xA8 ? 0x401 : ch3 == 0xB8 ? 0x451 : ch3 + 0x0350 : ch3));
							   ps+=3;
							   ctrl = false;
						   }else
							   ctrl = true;
					   }else if((ch==' '|| ch=='\n') && ctrl) {
						   ctrl = false;
						   if(ctrl2.toString().equals("par"))
							   sb.append('\n');
						   ctrl2.setLength(0);
					   }else if(!ctrl && ch>=32)
						   sb.append(ch);
					   else ctrl2.append(ch);
				   }
			   }
		   ps++;
	   }
	   return sb.toString();
   } 
   /**************************************************************************************/  
}
