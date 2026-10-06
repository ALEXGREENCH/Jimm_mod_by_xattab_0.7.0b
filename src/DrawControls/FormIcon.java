package DrawControls;

import javax.microedition.lcdui.Graphics;
import jimm.Options;

/** Border of a text field, or the adjustable bar of a gauge. */
final class FormIcon extends Icon
{
    int value;
    final int stepWidth;
    private final int maximum;
    private final boolean gauge;

    FormIcon(int width, int height, int value, int maximum, boolean gauge)
    {
        super(null, 0, 0, width, height);
        this.value = value;
        this.maximum = maximum;
        stepWidth = width / maximum;
        this.gauge = gauge;
    }

    public void drawByLeft(Graphics g, int x, int y)
    {
        int top = y - height / 2 + 2;
        g.setColor(gauge ? 0xCC0000 : Options.getInt(Options.OPTION_COLOR_TEXT));
        g.drawRect(x, top, x + width, height - 5);
        if (gauge)
        {
            int end = value == 0 ? x + 1 : value == maximum ? x + width : x + value * stepWidth;
            g.setColor(0x33CCFF);
            g.fillRect(x + 1, top + 1, end - 1, height - 6);
        }
    }

    void setValue(int value)
    {
        this.value = Math.max(0, Math.min(value, maximum));
    }
}
