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
 File: src/DrawControls/LightControl.java
 Version: ###VERSION###  Date: ###DATE###
 Author(s): Dmitry Tunin
 *******************************************************************************/

//#sijapp cond.if target is "MOTOROLA" | target is "MIDP2"#
package DrawControls;

import jimm.Jimm;
import jimm.Options;
//#sijapp cond.if target is "MIDP2"#
import java.util.TimerTask;
import com.nokia.mid.ui.DeviceControl;
//#sijapp cond.end#

public class LightControl
{
    private static boolean lightOn = true;
    private static int TIMEOUT = Options.getInt(Options.OPTION_LIGHT_TIMEOUT) * 1000;
    //#sijapp cond.if target is "MIDP2"#
    private static TimerTask lightTask;
    //#sijapp cond.end#

    public static void flash(boolean constant)
    {
        //#sijapp cond.if target is "MIDP2"#
        if (Jimm.supportsNokiaLight && Options.getBoolean(Options.OPTION_LIGHT_MANUAL))
        {
            if (constant) cancelTimeout();
            else reset();
        }
        //#sijapp cond.else#
        if (!Options.getBoolean(Options.OPTION_LIGHT_MANUAL))
            Jimm.display.flashBacklight(constant ? Integer.MAX_VALUE : TIMEOUT);
        //#sijapp cond.end#
    }

    //#sijapp cond.if target is "MIDP2"#
    private static void cancelTimeout()
    {
        On();
        if (lightTask != null)
        {
            lightTask.cancel();
            lightTask = null;
        }
    }

    public static void reset()
    {
        if (Jimm.supportsNokiaLight && Options.getBoolean(Options.OPTION_LIGHT_MANUAL))
        {
            cancelTimeout();
            lightTask = new TimerTask()
            {
                public void run() { Off(); }
            };
            Jimm.getTimerRef().schedule(lightTask, TIMEOUT);
        }
    }
    //#sijapp cond.end#

    public static void changeState()
    {
        if (lightOn) Off();
        else On();
    }

    public static void Off()
    {
        //#sijapp cond.if target is "MIDP2"#
        DeviceControl.setLights(0, 0);
        //#sijapp cond.else#
        Jimm.display.flashBacklight(1);
        //#sijapp cond.end#
        lightOn = false;
    }

    public static void On()
    {
        //#sijapp cond.if target is "MIDP2"#
        DeviceControl.setLights(0, Options.getInt(Options.OPTION_LIGHT_LEVEL));
        //#sijapp cond.else#
        Jimm.display.flashBacklight(Integer.MAX_VALUE);
        //#sijapp cond.end#
        lightOn = true;
    }
}
//#sijapp cond.end#
