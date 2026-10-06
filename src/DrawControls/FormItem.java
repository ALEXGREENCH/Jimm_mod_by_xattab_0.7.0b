package DrawControls;

import javax.microedition.lcdui.Item;

/** Data associated with one visible row of a VirtualForm. */
final class FormItem
{
    final Item item;
    Icon image;
    final int choiceIndex;
    String text;

    FormItem(Item item, String text, int choiceIndex, Icon image)
    {
        this.item = item;
        this.text = text;
        this.choiceIndex = choiceIndex;
        this.image = image;
    }
}
