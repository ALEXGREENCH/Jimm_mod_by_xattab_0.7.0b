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
 File: src/jimm/comm/XStatusForm.java
 Version: ###VERSION###  Date: ###DATE###
 Author: aspro
 *******************************************************************************/

package jimm;

import jimm.comm.*;
import jimm.util.*;
import DrawControls.*;
import java.util.*;
import javax.microedition.rms.*;
import javax.microedition.lcdui.*;

public class XStatusForm implements CommandListener
{
    private VirtualForm form;

    private TextField titleTextField;
    private TextField descTextField;
    private ChoiceGroup choiceGroup;
    private Vector xstatusform = new Vector();

    private int xstIndex = -1;
    private boolean happyFlag;

    /** Creates a new instance of XtrazForm */
    private XStatusForm() 
    {
    }

    private void showXtrazForm(int index) 
    {
        if (form == null)
        {
            form = new VirtualForm(ResourceBundle.getString("xtraz_msg"));
            form.setFontSize(Options.getInt(Options.OPTION_CL_FONT_SIZE) << 3);
            form.addCommandEx(JimmUI.cmdBack, VirtualList.MENU_RIGHT_BAR);
            form.addCommandEx(JimmUI.cmdSave, VirtualList.MENU_LEFT_BAR);
            form.setCommandListener(this);
            int constraints = TextField.ANY;
            if (Options.getBoolean(Options.OPTION_TEXT_ABC))
                constraints |= TextField.INITIAL_CAPS_SENTENCE;
            titleTextField = new TextField(ResourceBundle.getString("xtraz_title"), "", 20, constraints);
            descTextField = new TextField(ResourceBundle.getString("xtraz_desc"), "", 1000, constraints);
            choiceGroup = new FormChoiceGroup(null, Choice.MULTIPLE);
            choiceGroup.append(ResourceBundle.getString("xtraz_enable"), null);
            choiceGroup.append(ResourceBundle.getString("happy_balloon"), null);
        }
        xstIndex = index - 1;
        happyFlag = Options.getBoolean(Options.OPTION_FLAG_HAPPY);
        try
        {
            String titleAndDesc = getRecordDesc(xstIndex);
            titleTextField.setString(titleAndDesc.substring(0, titleAndDesc.indexOf("\t")));
            descTextField.setString(titleAndDesc.substring(titleAndDesc.indexOf("\t") + 1));
        }
        catch (Exception e) {}
        choiceGroup.setSelectedIndex(0, Options.getBoolean(Options.OPTION_XTRAZ_ENABLE));
        choiceGroup.setSelectedIndex(1, happyFlag);
        form.clear();
        form.append(titleTextField);
        form.append(descTextField);
        form.append(choiceGroup);
        form.activate(Jimm.display);
        //#sijapp cond.if target is "MIDP2" | target is "MOTOROLA"#
        LightControl.flash(true);
        //#sijapp cond.end#
    }

    public static void activate(int index) 
    {
        new XStatusForm().showXtrazForm(index);
    }
    
    public void commandAction(Command command, Displayable displayable) 
    {
        if (command == JimmUI.cmdSave)
        {
            Options.setString(Options.OPTION_XTRAZ_TITLE, titleTextField.getString());
            Options.setString(Options.OPTION_XTRAZ_MESSAGE, descTextField.getString());
            Options.setBoolean(Options.OPTION_XTRAZ_ENABLE, choiceGroup.isSelected(0));
            Options.setBoolean(Options.OPTION_FLAG_HAPPY, choiceGroup.isSelected(1));
            Options.setInt(Options.OPTION_XSTATUS, xstIndex);
            Options.safe_save();
            
            String xStatus = titleTextField.getString() + "\t" + descTextField.getString();
            if (!getRecordDesc(xstIndex).equals(xStatus))
            {
                xstatusform.setElementAt(xStatus, xstIndex);
                save();
            }
            
            MainMenu.build();

			if (Icq.isConnected())
			{
				ContactList.activate();
                try 
                {
                    OtherAction.setStandartUserInfo(false);

                    if (happyFlag != Options.getBoolean(Options.OPTION_FLAG_HAPPY))
						OtherAction.setStatus(Icq.setWebAware() | (int)Options.getLong(Options.OPTION_ONLINE_STATUS));
                } 
                catch (JimmException e) 
                {
                    JimmException.handleException(e);
                }
			}
			else
			{
				MainMenu.activate();
			}
            SplashCanvas.setXStatusToDraw(Icq.getCurrentXStatus());
        }
        else if (command == JimmUI.cmdBack)
        {
            MainMenu.showXStatusSelector();
        }
    }

    private void load()
    {
        RecordStore rms = null;
        xstatusform.removeAllElements();
        try
        {
            rms = RecordStore.openRecordStore("xtraz", true); 
            if (rms.getNumRecords() <= 0)
            {
                for (int i = 0; i <= XStatus.getXStatusCount(); i++)
                {
                    String str = XStatus.getStatusAsString(i) + "\t" + "";
                    xstatusform.addElement(str);
                }
            }
            else
            {
                byte[] data = rms.getRecord(1);
                LoadLineInTable(data);
            }
        }
        catch (Exception e) {}
        try 
        {
            rms.closeRecordStore();
        }
        catch (Exception e) {}
    }
    
    private void save()
    {
        try
        {
            RecordStore.deleteRecordStore("xtraz");
        } 
        catch (Exception e) {}  
        RecordStore rms = null;
        try
        {
            rms = RecordStore.openRecordStore("xtraz", true);
            byte[] buffer = Util.stringToByteArray(saveInLine(), true);
            rms.addRecord(buffer, 0, buffer.length);
        }
        catch (Exception e) {}
        try
        {
            rms.closeRecordStore();
        }
        catch (Exception e) {}
    }

    private String getRecordDesc(int num)
    {
        load();
        return (String)xstatusform.elementAt(num);
    }

    private String saveInLine()
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < XStatus.getXStatusCount(); i++)
        {
            result.append((String)xstatusform.elementAt(i)).append("\t\r");
        }
        return result.toString();
    }

    private void LoadLineInTable(byte[] data)
    {
        String str = Util.byteArrayToString(data, 0, data.length, true);
        int l = 0; //начало
        int l1 = 0; //конец
        do
        {
            l1 = str.indexOf("\t\r", l);
            String str1 = str.substring(l, l1);
            xstatusform.addElement(str1);
            l = l1 + 2;
        }
        while (str.indexOf("\t\r", l) != -1);
    }
}