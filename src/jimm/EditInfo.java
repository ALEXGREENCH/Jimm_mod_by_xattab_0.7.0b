/*******************************************************************************
Jimm - Mobile Messaging - J2ME ICQ clone
Copyright (C) 2003-06  Jimm Project

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
File: src/jimm/EditInfo.java
Version: ###VERSION###  Date: ###DATE###
Author(s): Igor Palkin
*******************************************************************************/
package jimm;

import DrawControls.*;
import java.io.ByteArrayOutputStream;
import javax.microedition.lcdui.*;
import jimm.comm.*;
import jimm.util.*;

public class EditInfo implements CommandListener, Runnable
{
	private VirtualForm form = new VirtualForm(ResourceBundle.getString("editform"));
	private TextField _NickNameItem;
	private TextField _FirstNameItem;
	private TextField _LastNameItem;
	private TextField _EmailItem;
	private TextField _BdayItem;
	private TextField _HomePageItem;
	private TextField _AboutItem;
	private TextField _CityItem;
	private FormChoiceGroup _SexItem;
	private FormChoiceGroup intterest1;
	private FormChoiceGroup intterest2;
	private FormChoiceGroup intterest3;
	private FormChoiceGroup intterest4;
	private TextField intterestText1;
	private TextField intterestText2;
	private TextField intterestText3;
	private TextField intterestText4;
	private Object _PreviousForm;
	private String[] userInfo;

	private EditInfo(String[] data, Object previousForm)
	{
		form.setFontSize(Options.getInt(Options.OPTION_CL_FONT_SIZE) << 3);
		_PreviousForm = previousForm;
		_NickNameItem = new TextField(ResourceBundle.getString("nick"), data[JimmUI.UI_NICK], 20, TextField.ANY);
		_FirstNameItem = new TextField(ResourceBundle.getString("firstname"), data[JimmUI.UI_FIRST_NAME], 20, TextField.ANY);
		_LastNameItem = new TextField(ResourceBundle.getString("lastname"), data[JimmUI.UI_LAST_NAME], 20, TextField.ANY);
		_EmailItem = new TextField(ResourceBundle.getString("email"), data[JimmUI.UI_EMAIL], 50, TextField.EMAILADDR);
		_BdayItem = new TextField(ResourceBundle.getString("birth_day"), data[JimmUI.UI_BDAY], 15, TextField.ANY);
		_HomePageItem = new TextField(ResourceBundle.getString("home_page"), data[JimmUI.UI_HOME_PAGE], 70, TextField.ANY);
		_AboutItem = new TextField(ResourceBundle.getString("notes"), data[JimmUI.UI_ABOUT], 600, TextField.ANY);
		_CityItem = new TextField(ResourceBundle.getString("city"), data[JimmUI.UI_CITY], 50, TextField.ANY);
		_SexItem = new FormChoiceGroup(ResourceBundle.getString("gender"), Choice.POPUP);
		_SexItem.append("---", null);
		_SexItem.append(ResourceBundle.getString("female"), null);
		_SexItem.append(ResourceBundle.getString("male"), null);
		_SexItem.setSelectedIndex(Util.stringToGender(data[JimmUI.UI_GENDER]), true);
		form.clear();
		form.append(_NickNameItem);
		form.append(_FirstNameItem);
		form.append(_LastNameItem);
		form.append(_SexItem);
		form.append(_EmailItem);
		form.append(_BdayItem);
		form.append(_HomePageItem);
		form.append(_AboutItem);
		form.append(_CityItem);
		form.addCommandEx(JimmUI.cmdSave, VirtualList.MENU_LEFT_BAR);
		form.addCommandEx(JimmUI.cmdBack, VirtualList.MENU_RIGHT_BAR);
		form.setCommandListener(this);
		form.activate(Jimm.display);
		(new Thread(this)).start();
	}

	public static void showEditForm(String[] userInfo, Object previousForm)
	{
		EditInfo editor = new EditInfo(userInfo, previousForm);
		editor.userInfo = userInfo;
		//#sijapp cond.if target is "MIDP2" | target is "MOTOROLA"#
		LightControl.flash(true);
		//#sijapp cond.end#
	}

	public void run()
	{
		intterest1 = new FormChoiceGroup(ResourceBundle.getString("interests"), Choice.POPUP);
		intterest2 = new FormChoiceGroup(ResourceBundle.getString("interests"), Choice.POPUP);
		intterest3 = new FormChoiceGroup(ResourceBundle.getString("interests"), Choice.POPUP);
		intterest4 = new FormChoiceGroup(ResourceBundle.getString("interests"), Choice.POPUP);
		intterestText1 = new TextField(null, null, 60, TextField.ANY);
		intterestText2 = new TextField(null, null, 60, TextField.ANY);
		intterestText3 = new TextField(null, null, 60, TextField.ANY);
		intterestText4 = new TextField(null, null, 60, TextField.ANY);
		for (int z = 0; z < 51; z++)
		{
			intterest1.append(ResourceBundle.getString(RequestInfoAction.getCategoriesName(z)), null);
			intterest2.append(ResourceBundle.getString(RequestInfoAction.getCategoriesName(z)), null);
			intterest3.append(ResourceBundle.getString(RequestInfoAction.getCategoriesName(z)), null);
			intterest4.append(ResourceBundle.getString(RequestInfoAction.getCategoriesName(z)), null);
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e) {}
		}
		intterest1.setSelectedIndex(RequestInfoAction.getSelectIndex(RequestInfoAction.indexCategories[0]), true);
		intterest2.setSelectedIndex(RequestInfoAction.getSelectIndex(RequestInfoAction.indexCategories[1]), true);
		intterest3.setSelectedIndex(RequestInfoAction.getSelectIndex(RequestInfoAction.indexCategories[2]), true);
		intterest4.setSelectedIndex(RequestInfoAction.getSelectIndex(RequestInfoAction.indexCategories[3]), true);
		intterestText1.setString(userInfo[JimmUI.UI_INETRESTS_1] != null ? userInfo[JimmUI.UI_INETRESTS_1].substring(1) : null);
		intterestText2.setString(userInfo[JimmUI.UI_INETRESTS_2] != null ? userInfo[JimmUI.UI_INETRESTS_2].substring(1) : null);
		intterestText3.setString(userInfo[JimmUI.UI_INETRESTS_3] != null ? userInfo[JimmUI.UI_INETRESTS_3].substring(1) : null);
		intterestText4.setString(userInfo[JimmUI.UI_INETRESTS_4] != null ? userInfo[JimmUI.UI_INETRESTS_4].substring(1) : null);
		form.append(intterest1);
		form.append(intterestText1);
		form.append(intterest2);
		form.append(intterestText2);
		form.append(intterest3);
		form.append(intterestText3);
		form.append(intterest4);
		form.append(intterestText4);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c == JimmUI.cmdBack)
		{
			JimmUI.selectScreen(_PreviousForm);
		}
		if (c == JimmUI.cmdSave)
		{
			userInfo[JimmUI.UI_NICK]       = _NickNameItem.getString();
			userInfo[JimmUI.UI_EMAIL]      = _EmailItem.getString();
			userInfo[JimmUI.UI_BDAY]       = _BdayItem.getString();
			userInfo[JimmUI.UI_FIRST_NAME] = _FirstNameItem.getString();
			userInfo[JimmUI.UI_LAST_NAME]  = _LastNameItem.getString();
			userInfo[JimmUI.UI_HOME_PAGE]  = _HomePageItem.getString();
			userInfo[JimmUI.UI_ABOUT]      = _AboutItem.getString();
			userInfo[JimmUI.UI_CITY]       = _CityItem.getString();
			userInfo[JimmUI.UI_GENDER]     = Util.genderToString(_SexItem.getSelectedIndex());
			userInfo[JimmUI.UI_INETRESTS_1] = intterestText1.getString();
			userInfo[JimmUI.UI_INETRESTS_2] = intterestText2.getString();
			userInfo[JimmUI.UI_INETRESTS_3] = intterestText3.getString();
			userInfo[JimmUI.UI_INETRESTS_4] = intterestText4.getString();
			RequestInfoAction.indexCategories[0] = RequestInfoAction.getCategoriesCode(intterest1.getSelectedIndex());
			RequestInfoAction.indexCategories[1] = RequestInfoAction.getCategoriesCode(intterest2.getSelectedIndex());
			RequestInfoAction.indexCategories[2] = RequestInfoAction.getCategoriesCode(intterest3.getSelectedIndex());
			RequestInfoAction.indexCategories[3] = RequestInfoAction.getCategoriesCode(intterest4.getSelectedIndex());

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Util.writeWord(stream, ToIcqSrvPacket.CLI_SET_FULLINFO, false);
			Util.writeAsciizTLV(SaveInfoAction.FIRSTNAME_TLV_ID, stream, userInfo[JimmUI.UI_FIRST_NAME], false);
			SaveInfoAction action = new SaveInfoAction(userInfo);
			try
			{
				Icq.requestAction(action);
			}
			catch (JimmException e)
			{
				JimmException.handleException(e);
				if (e.isCritical()) return;
			}
			Icq.myNick = _NickNameItem.getString().length() > 0 ? _NickNameItem.getString() : ResourceBundle.getString("me");
			SplashCanvas.addTimerTask("saveinfo", action, false);
		}
	}
}
