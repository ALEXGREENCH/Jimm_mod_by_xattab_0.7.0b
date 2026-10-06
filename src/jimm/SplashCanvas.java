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
 File: src/jimm/SplashCanvas.java
 Version: ###VERSION###  Date: ###DATE###
 Author(s): Manuel Linsmayer, Andreas Rossbacher
 *******************************************************************************/

package jimm;

import DrawControls.*;
import jimm.comm.*;
import jimm.util.*;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.*;

//#sijapp cond.if target is "MOTOROLA"#
import javax.microedition.lcdui.Screen;
//#sijapp cond.end#

//#sijapp cond.if target is "RIM"#
import net.rim.device.api.system.LED;
//#sijapp cond.end#

public class SplashCanvas extends VirtualList implements CommandListener
{
	static public SplashCanvas _this;

	//#sijapp cond.if target is "MIDP2"#
	public final static Command cancelCommnad = new Command(ResourceBundle.getString("cancel"), Jimm.is_smart_SE() ? Command.CANCEL : Command.BACK, 1);
	//#sijapp cond.else#
	public final static Command cancelCommnad = new Command(ResourceBundle.getString("cancel"), Command.BACK, 1);
	//#sijapp cond.end#

	//Timer for repaint
	static private Timer t1,t2;

	// Location of the splash image (inside the JAR file)
	private static final String SPLASH_IMG = "/logo.png";

	// Image object, holds the splash image
	private static Image splash;

	// Location of the notice image (inside the JAR file)
	private static final String NOTICE_IMG = "/notice.png";

	//#sijapp cond.if target is "SIEMENS2"#
	private static final String BATT_IMG = "/batt.png";
	private static Image battImg = null;
	
	private static Image getBattImg() 
	{
		if( battImg == null )
		{
			try
			{
				battImg = Image.createImage(SplashCanvas.BATT_IMG);
			}
			catch(IOException e){}
		}
		return battImg;
	}
	//#sijapp cond.end#
/*
	// Adding logo in About Screen
	private static Image AboutImg = null;
	
	static public Image getAboutImg() 
	{
		if (AboutImg == null)
		{
			try
			{
				AboutImg = Image.createImage("/about.png");
			}
			catch(IOException e){}
		}
		return AboutImg;
	}
*/
	// Image object, holds the notice image
	private static Image notice;

	// Font used to display the logo (if image is not available)
	private static Font logoFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD + Options.fontStyle, Font.SIZE_LARGE);
	
	// Font used to display the version nr
	private static Font versionFont = Font.getFont(Font.FACE_SYSTEM, Options.fontStyle, Font.SIZE_SMALL);
	/* For E2 */
	//private static Font versionFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);

	// Font (and font height in pixels) used to display informational messages
	private static Font font = Font.getFont(Font.FACE_SYSTEM, Options.fontStyle, Font.SIZE_SMALL);
	/* For E2 */
	//private static Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);

	private static int height = font.getHeight();

	// Initializer block
	static
	{
		// Construct splash image
		try
		{
			SplashCanvas.notice = Image.createImage(SplashCanvas.NOTICE_IMG);
		}
		catch (IOException e)
		{
			// Do nothing
		}
	}

	/*****************************************************************************/

	// Message to display beneath the splash image
	static private String message;

	// Progress in percent
	static private int progress; // = 0

	// True if keylock has been enabled
	static private boolean isLocked;

	// Number of available messages
	static private int availableMessages;
	
	// Time since last key # pressed 
	static public long poundPressTime;

	// Should the keylock message be drawn to the screen?
	static protected boolean showKeylock;

	static private int status_index = -1;
	static private Icon xstatus_img = null;
	static private TimerTasks actionTimer;
	static private Action currentAction;

	// Constructor
	public SplashCanvas(String message)
	{
		super(null);
		_this = this;
		setMessage(message);
		showKeylock = false;
	}

	// Constructor, blank message
	public SplashCanvas()
	{
		this(null);
	}

	// Returns the informational message
	static public synchronized String getMessage()
	{
		return (message);
	}

	// Sets the informational message
	static public synchronized void setMessage(String message)
	{
		SplashCanvas.message = new String(message);
		SplashCanvas._this.repaint();
	}

	public static synchronized void setStatusToDraw(int st_index)
	{
		status_index = st_index;
	}

	public static synchronized void setXStatusToDraw(Icon st_index)
	{
		xstatus_img = st_index;
	}

	// Returns the current progress in percent
	static public synchronized int getProgress()
	{
		return (progress);
	}
	
	static public Image getSplashImage()
	{
		if (SplashCanvas.splash == null)
		{
			try
			{
				SplashCanvas.splash = Image.createImage(SplashCanvas.SPLASH_IMG);
			}
			catch (Exception e)
			{
				SplashCanvas.splash = null;
			}
		}
		return SplashCanvas.splash;
	}

	static public void show()
	{
		if (t2 != null)
		{
			t2.cancel();
			t2 = null;
		}
		_this.activate(Jimm.display);
	}

	static public void addCmd(Command cmd)
	{
		_this.addCommandEx(cmd, MENU_RIGHT_BAR);
	}

	static public void removeCmd(Command cmd)
	{
		_this.removeCommandEx(cmd);
	}

	static public void setCmdListener(CommandListener l)
	{
		_this.setCommandListener(l);
	}

	static public void Repaint()
	{
		_this.repaint();
	}

	// Sets the current progress in percent (and request screen refresh)
	static public synchronized void setProgress(int progress)
	{
		if (SplashCanvas.progress == progress) return;
		SplashCanvas.progress = progress;
		_this.repaint();
	}

	// Enable keylock
	static public synchronized void lockScreen()
	{
		_this.removeAllCommands();
		if (isLocked) return;
		//#sijapp cond.if target is "MIDP2"#
		Displayable previous = Jimm.display.getCurrent();
		//#sijapp cond.end#
		isLocked = true;
		//#sijapp cond.if target is "MOTOROLA"#
		LightControl.Off();
		//#sijapp cond.end#
		setMessage(ResourceBundle.getString("keylock_enabled"));
		_this.activate(Jimm.display);
		//#sijapp cond.if target is "MIDP2"#
		if (!previous.isShown()) Jimm.setMinimized(true);
		//#sijapp cond.end#
		(t2 = new Timer()).schedule(new TimerTasks(TimerTasks.SC_AUTO_REPAINT), 20000, 20000);
		setProgress(0);
		Jimm.isPasswordProtected = true;
	}

    public static void activate()
    {
        if (t2 != null) { t2.cancel(); t2 = null; }
        _this.activate(Jimm.display);
    }

	// Disable keylock
	static public synchronized void unlock(boolean showContactList)
	{
		isLocked = false;
		availableMessages = 0;
		//#sijapp cond.if target is "RIM"#
		LED.setState(LED.STATE_OFF);
		//#sijapp cond.end#
		//#sijapp cond.if target is "MOTOROLA"#
		if (Options.getBoolean(Options.OPTION_LIGHT_MANUAL)) LightControl.On();
		//#sijapp cond.end#
		if (t2 != null) t2.cancel();
		if (showContactList) ContactList.activate();
		else MainMenu.activate();
	}

	// Is the screen locked?
	static public boolean locked()
	{
		return (isLocked);
	}


	// Called when message has been received
	static public synchronized void messageAvailable()
	{
		if (isLocked)
		{
			++availableMessages;
			// #sijapp cond.if target is "RIM"#
			LED.setConfiguration(500, 250, LED.BRIGHTNESS_50);
			LED.setState(LED.STATE_BLINKING);
			// #sijapp cond.end#
			// #sijapp cond.if target is "MOTOROLA"#
			if (Options.getInt(Options.OPTION_MESS_NOTIF_MODE) == 0)
			Jimm.display.flashBacklight(1000);
			// #sijapp cond.end#
			_this.repaint();
		}
	}

	// Called when a key is pressed
	protected void keyPressed(int keyCode)
	{
		//#sijapp cond.if target is "MIDP2" | target is "SIEMENS2"#
		if (hasSoftKeys())
		{
			super.doKeyreaction(keyCode, KEY_PRESSED);
			return;
		}
		//#sijapp cond.end#
		if (isLocked)
		{
			if ((keyCode == Canvas.KEY_POUND) || (keyCode == Canvas.KEY_STAR))
				poundPressTime = System.currentTimeMillis();
			else
			{
				if (t1 != null) t1.cancel();
				showKeylock = true;
				repaint();
			}
		}
		//#sijapp cond.if target is "MIDP2"#
		LightControl.reset();
		//#sijapp cond.end#
		//#sijapp cond.if target is "MOTOROLA"#
		Jimm.display.flashBacklight(3000);
		//#sijapp cond.end#
	}

	private static void tryToUnlock(int keyCode)
	{
		if (!isLocked) return;
		if ((keyCode != Canvas.KEY_POUND) && (keyCode != Canvas.KEY_STAR))
		{
			poundPressTime = 0;
			return;
		}
		if ((poundPressTime != 0) && ((System.currentTimeMillis() - poundPressTime) > 900))
			requestUnlock();
	}

	// Called when a key is released
	protected void keyReleased(int keyCode)
	{
		//#sijapp cond.if target is "MOTOROLA"#
		if (hasSoftKeys())
		{
			super.doKeyreaction(keyCode, KEY_RELEASED);
			return;
		}
		//#sijapp cond.end#
		tryToUnlock(keyCode);
	}
	
	protected void keyRepeated(int keyCode)
	{
		tryToUnlock(keyCode);
	}

	private static void requestUnlock()
	{
		if (Options.getString(Options.OPTION_ENTER_PASSWORD).length() > 0)
			EnterPassword.activate(Jimm.display.getCurrent());
		else
		{
			unlock(Icq.isConnected());
			poundPressTime = 0;
		}
	}

	//#sijapp cond.if target is "MIDP2"#
	protected void pointerPressed(int x, int y)
	{
		if (isLocked && y > getHeight() - height - 3) requestUnlock();
	}
	//#sijapp cond.end#

	// Render the splash image
	protected void paint(Graphics g)
	{
		int bgColor = Options.getInt(Options.OPTION_COLOR_SBACK);
		int txtColor = getInverseColor(bgColor);
		int bottom = getHeight() - height;
		if (g.getClipY() < bottom - 2)
		{
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			Image logo = getSplashImage();
			if (logo != null) g.drawImage(logo, getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.VCENTER);
			else
			{
				g.setFont(logoFont);
				drawString(g, "jimm", getWidth() / 2, getHeight() / 2 + 5, Graphics.HCENTER | Graphics.BASELINE, txtColor);
			}
			g.setFont(font);
			if (notice != null) g.drawImage(notice, getWidth() / 2, 2, Graphics.HCENTER | Graphics.TOP);
			if (isLocked && availableMessages > 0)
			{
				ContactList.imageList.elementAt(14).drawImage(g, 1, getHeight() - 2 * height - 9);
				drawString(g, "# " + availableMessages, ContactList.imageList.elementAt(14).getWidth() + 4,
					getHeight() - 2 * height - 5, Graphics.LEFT | Graphics.TOP, txtColor);
			}
			//#sijapp cond.if target is "SIEMENS2"#
			String accuLevel = System.getProperty("MPJC_CAP");
			if( accuLevel != null && isLocked )
			{
				accuLevel += "%";
				int fontX = getWidth() -  SplashCanvas.font.stringWidth(accuLevel) - 1;
				if (getBattImg() != null)
					g.drawImage(getBattImg(), fontX - getBattImg().getWidth() - 1, getHeight() - (2 * SplashCanvas.height) - 9, Graphics.LEFT | Graphics.TOP);
				g.setColor(txtColor);
				g.setFont(SplashCanvas.font);
				drawString(g, accuLevel, fontX, getHeight() - (2 * SplashCanvas.height) - 5, Graphics.LEFT | Graphics.TOP, txtColor);
			}
			//#sijapp cond.end#
			drawString(g, Util.getDateString(false, false), getWidth() / 2, 12, Graphics.TOP | Graphics.HCENTER, txtColor);
			drawString(g, Util.getCurrentDay(), getWidth() / 2, 13 + font.getHeight(), Graphics.TOP | Graphics.HCENTER, txtColor);
			if (showKeylock)
			{
				int size_x = (getWidth() / 10) << 3;
				int size_y = Font.getFont(Font.FACE_SYSTEM, Options.fontStyle, Font.SIZE_SMALL).getHeight()
					* TextList.getLineNumbers(ResourceBundle.getString("keylock_message"), size_x - 8, Font.SIZE_SMALL, Options.fontStyle, 0) + 8;
				int x = getWidth() / 2 - ((getWidth() / 10) << 2);
				int y = getHeight() / 2 - size_y / 2;
				g.setColor(txtColor);
				g.fillRect(x, y, size_x, size_y);
				g.setColor(bgColor);
				g.drawRect(x + 2, y + 2, size_x - 5, size_y - 5);
				TextList.showText(g, ResourceBundle.getString("keylock_message"), x + 4, y + 4,
					size_x - 8, Font.SIZE_SMALL, Options.fontStyle, getInverseColor(txtColor));
				(t1 = new Timer()).schedule(new TimerTasks(TimerTasks.SC_HIDE_KEYLOCK), 2000);
			}
		}
		g.setColor(txtColor);
		Icon draw_img = null;
		int im_width = 0;
		if (status_index != -1)
		{
			draw_img = ContactList.imageList.elementAt(status_index);
			im_width = draw_img.getWidth();
		}
		int ims_width = 0;
		int text_x = getWidth() / 2 + im_width / 2;
		int icon_x = getWidth() / 2 - font.stringWidth(message) / 2 + im_width / 2;
		if (xstatus_img != null && xstatus_img != XStatus.getStatusImage(XStatus.XSTATUS_NONE) && getWidth() > 129)
			ims_width = xstatus_img.getWidth();
		drawString(g, message, text_x, getHeight(), Graphics.BOTTOM | Graphics.HCENTER, txtColor);
		if (ims_width != 0) xstatus_img.drawByRight(g, icon_x, getHeight() - height / 2);
		if (draw_img != null) draw_img.drawByRight(g, icon_x - ims_width, getHeight() - height / 2);
		int progressPx = getWidth() * progress / 100;
		if (progressPx < 1) return;
		g.drawRect(1, bottom - height / 2 - 3, getWidth() - 3, height / 2);
		g.setColor(0x990000);
		g.fillRect(2, bottom - height / 2 - 2, progressPx - 4, height / 2 - 1);
	}

	public static int getAreaWidth()
	{
		return _this.getWidth();
	}

	public static void startTimer()
	{
		if (status_index != 8)
		{
			new Timer().schedule(new TimerTasks(TimerTasks.SC_RESET_TEXT_AND_IMG), 15000);
		}
	}

	public static void addTimerTask(String captionLngStr, Action action, boolean canCancel)
	{
		if (t2 != null)
		{
			t2.cancel();
			t2 = null;
		}
		cancelActionTimer();
		TimerTasks timerTask = new TimerTasks(action);
		_this.removeCommandEx(cancelCommnad);
		if (canCancel)
		{
			_this.addCommandEx(cancelCommnad, MENU_RIGHT_BAR);
			_this.setCommandListener(_this);
		}
		setMessage(ResourceBundle.getString(captionLngStr));
		setProgress(0);
		_this.activate(Jimm.display);
		Jimm.getTimerRef().schedule(timerTask, 1000, 1000);
		actionTimer = timerTask;
		currentAction = action;
	}

	private static void cancelActionTimer()
	{
		if (actionTimer != null)
		{
			try { actionTimer.cancel(); } catch (Exception e) {}
			actionTimer = null;
			currentAction = null;
		}
	}

	public void commandAction(Command command, Displayable displayable)
	{
		if (command == cancelCommnad && currentAction != null)
		{
			currentAction.onEvent(Action.ON_CANCEL);
			cancelActionTimer();
		}
	}

	protected void get(int index, ListItem item) {}
	protected int getSize() { return 0; }
}
