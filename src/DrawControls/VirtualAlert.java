package DrawControls;

import javax.microedition.lcdui.*;
import jimm.JimmUI;
import jimm.Options;

/** Scrollable popup painted over the screen from which it was opened. */
public class VirtualAlert extends VirtualList implements CommandListener
{
    public String text;
    private TextList lines;
    private final Object previousScreen;
    private final Font font = Font.getFont(Font.FACE_SYSTEM,
        Options.getInt(Options.OPTION_CL_FONT_STYLE), Options.getInt(Options.OPTION_CL_FONT_SIZE) << 3);
    private int popupWidth, popupHeight, visibleLines;

    public VirtualAlert(Object previousScreen, String text, int tag)
    {
        super(null);
        this.text = text;
        this.previousScreen = previousScreen;
        JimmUI.curScreenTag = tag;
        addCommandEx(JimmUI.cmdBack, MENU_RIGHT_BAR);
        setCommandListener(this);
        updateText(false);
    }

    public void updateText(boolean repaint)
    {
        lines = new TextList(null);
        lines.setFontSize(font.getSize());
        JimmUI.setColorScheme(lines, false);
        lines.addBigTextInternal(text, lines.getTextColor(), font.getStyle(), -1, getWidth() / 10 * 9);
        int count = lines.getSize();
        popupHeight = Math.min(font.getHeight() * count + 8, getHeight() / 10 * 6);
        visibleLines = count;
        int height = 0;
        for (int i = 0; i < count; i++)
        {
            height += lines.getLine(i).getHeight(font.getSize());
            if (height > popupHeight - 3)
            {
                visibleLines = i;
                break;
            }
        }
        popupWidth = 0;
        for (int i = count - 1; i >= 0; i--)
            popupWidth = Math.max(popupWidth, lines.getLine(i).getWidth(font.getSize()));
        popupWidth += 11;
        if (repaint) invalidate();
    }

    protected void paint(Graphics g)
    {
        if (previousScreen == null) return;
        if (previousScreen instanceof VirtualList) ((VirtualList)previousScreen).paint(g);
        else
        {
            g.setColor(0);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        int x = getWidth() / 2 - popupWidth / 2 - 2;
        int y = (getHeight() << 1) / 5 - popupHeight / 2;
        int captionColor = Options.getInt(Options.OPTION_COLOR_CAP);
        g.setColor(captionColor);
        g.fillRoundRect(x, y - 2, popupWidth, popupHeight + 10, 4, 4);
        g.setColor(Options.getInt(Options.OPTION_COLOR_BACK));
        g.fillRect(x + 3, y + 3, popupWidth - 6, popupHeight - 6);
        int offset = Options.getBoolean(Options.OPTION_SWAP_SOFT_KEY) ? popupWidth - 15 : 0;
        int right = x + popupWidth - offset;
        int bottom = y + popupHeight;
        g.setColor(getInverseColor(captionColor));
        g.drawLine(right - 11, bottom, right - 6, bottom + 5);
        g.drawLine(right - 10, bottom, right - 6, bottom + 4);
        g.drawLine(right - 10, bottom - 1, right - 5, bottom + 4);
        g.drawLine(right - 11, bottom + 4, right - 6, bottom - 1);
        g.drawLine(right - 10, bottom + 4, right - 6, bottom);
        g.drawLine(right - 10, bottom + 5, right - 5, bottom);
        if (hasBothSoftKeys())
        {
            int left = x + offset;
            g.drawLine(left + 4, bottom + 1, left + 6, bottom + 3);
            g.drawLine(left + 4, bottom + 2, left + 6, bottom + 4);
            g.drawLine(left + 4, bottom + 3, left + 6, bottom + 5);
            g.drawLine(left + 6, bottom + 3, left + 10, bottom - 1);
            g.drawLine(left + 6, bottom + 4, left + 10, bottom);
            g.drawLine(left + 6, bottom + 5, left + 10, bottom + 1);
        }
        g.setClip(x, y + 3, popupWidth, popupHeight - 6);
        int count = lines.getSize();
        int end = y + popupHeight - 3;
        int top = y;
        for (int i = lines.topItem; i < count; i++)
        {
            TextLine line = lines.getLine(i);
            line.paint(x + 4, top + 4, g, font.getSize(), lines);
            top += line.getHeight(font.getSize());
            if (top >= end) break;
        }
        if (visibleLines < count)
            lines.drawScroller(g, y + 3, x + popupWidth - 1, visibleLines, getHeight() - end);
    }

    public void doKeyreaction(int key, int type)
    {
        int action = getGameAction(key);
        if (action == Canvas.DOWN)
        {
            if (visibleLines < lines.getSize())
            {
                lines.topItem = Math.min(lines.topItem + visibleLines, lines.getSize() - visibleLines);
                invalidate();
            }
        }
        else if (action == Canvas.UP)
        {
            lines.topItem = Math.max(0, lines.topItem - visibleLines);
            invalidate();
        }
        else super.doKeyreaction(key, type);
    }

    //#sijapp cond.if target is "MIDP2"#
    protected void pointerDragged(int x, int y)
    {
        if (lastPointerTopItem == -1 || lines.getSize() == visibleLines) return;
        lines.topItem = lastPointerTopItem + lines.getSize() * (y - lastPointerYCrd) / popupHeight;
        lines.topItem = Math.max(0, Math.min(lines.topItem, lines.getSize() - visibleLines));
        invalidate();
    }
    //#sijapp cond.end#

    protected int getSize() { return 0; }
    protected void get(int index, ListItem item) { }

    public void commandAction(Command command, Displayable displayable)
    {
        if (command == JimmUI.cmdBack) JimmUI.selectScreen(previousScreen);
        JimmUI.curScreenTag = -1;
    }
}
