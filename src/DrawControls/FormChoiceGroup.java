package DrawControls;

import javax.microedition.lcdui.ChoiceGroup;

/** ChoiceGroup with its type retained for the canvas form renderer. */
public class FormChoiceGroup extends ChoiceGroup
{
    final int choiceType;

    public FormChoiceGroup(String label, int type)
    {
        super(label, type);
        choiceType = type;
    }

    public FormChoiceGroup(String label, String[] choices)
    {
        super(label, POPUP, choices, null);
        choiceType = POPUP;
    }

    public FormChoiceGroup(String label, int type, String[] choices,
                           javax.microedition.lcdui.Image[] images)
    {
        super(label, type, choices, images);
        choiceType = type;
    }
}
