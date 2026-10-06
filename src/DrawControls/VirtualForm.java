package DrawControls;

import javax.microedition.lcdui.*;
import jimm.ContactList;
import jimm.Jimm;
import jimm.JimmUI;
import jimm.Options;
import jimm.util.ResourceBundle;
//#sijapp cond.if modules_SMILES is "true"#
import jimm.Emotions;
//#sijapp cond.end#

/** Canvas form used by the 0.7 interface; native items keep their data model. */
public class VirtualForm extends VirtualTree implements VirtualTreeCommands,
        VirtualListCommands, CommandListener
{
    private final ImageList formImages = ImageList.load("/forms.png");
    private ItemStateListener itemStateListener;
    private boolean firstItem = true;
    private final Command editOk = new Command(ResourceBundle.getString("ok"),
        swapEditorKeys() ? Command.BACK : Command.OK, 1);
    private final Command editCancel = new Command(ResourceBundle.getString("cancel"),
        swapEditorKeys() ? Command.STOP : Command.CANCEL, 14);

    private static boolean swapEditorKeys()
    {
        //#sijapp cond.if target is "MIDP2" | target is "MOTOROLA"#
        return Options.getBoolean(Options.OPTION_SWAP_SEND_AND_BACK);
        //#sijapp cond.else#
        return false;
        //#sijapp cond.end#
    }

    public VirtualForm(String caption)
    {
        super(caption, true);
        setStepSize(3);
        setImageList(formImages);
        JimmUI.setColorScheme(this, false);
        setVTCommands(this);
        setVLCommands(this);
        setCyclingCursor(true);
    }

    public void setItemStateListener(ItemStateListener listener)
    {
        itemStateListener = listener;
    }

    public void addCommand(Command command)
    {
        int type = command.getCommandType();
        addCommandEx(command, type == Command.BACK || type == Command.CANCEL
            ? MENU_RIGHT_BAR : command.getPriority() <= 1 ? MENU_LEFT_BAR : MENU_LEFT);
    }

    public void removeCommand(Command command) { removeCommandEx(command); }

    public void append(String text) { addNode(null, text); }

    public void append(Image image)
    {
        addNode(null, new FormItem(null, null, -1,
            new Icon(image, 0, 0, image.getWidth(), image.getHeight())));
    }

    public void activate(Display display)
    {
        refreshItems(getRoot());
        super.activate(display);
    }

    private void refreshItems(TreeNode parent)
    {
        for (int i = 0; i < parent.size(); i++)
        {
            TreeNode node = parent.elementAt(i);
            if (node.getData() instanceof FormItem)
            {
                FormItem row = (FormItem)node.getData();
                if (row.item instanceof FormChoiceGroup && row.choiceIndex >= 0)
                {
                    FormChoiceGroup choices = (FormChoiceGroup)row.item;
                    row.text = choices.getString(row.choiceIndex);
                    row.image = choiceImage(choices, row.choiceIndex);
                    setExpandFlag(node, choices.isSelected(row.choiceIndex));
                }
                else if (row.item instanceof TextField) row.text = getItemText(row.item);
                else if (row.item instanceof Gauge) ((FormIcon)row.image).setValue(((Gauge)row.item).getValue());
            }
            refreshItems(node);
        }
    }

    public void append(Item item)
    {
        String label = nonNull(item.getLabel());
        Icon border = null;
        if (item instanceof TextField || item instanceof Gauge)
        {
            if (firstItem) setCurrentItem(1);
            border = new FormIcon(getNonScrollerArea() + 9, getFontHeight() + 4, 1, 1, false);
            if (label.length() > 0) addNode(null, label);
            firstItem = false;
            if (item instanceof Gauge)
            {
                Gauge gauge = (Gauge)item;
                addNode(null, new FormItem(item, null, -1, new FormIcon(
                    getNonScrollerArea() + 9, getFontHeight() - 2,
                    gauge.getValue(), gauge.getMaxValue(), true)));
                return;
            }
        }
        else if (item instanceof FormChoiceGroup && label.length() == 0)
        {
            appendChoices(getRoot(), item);
            firstItem = false;
            return;
        }
        firstItem = false;
        appendChoices(addNode(null, new FormItem(item, getItemText(item), -1, border)), item);
    }

    private void appendChoices(TreeNode parent, Item item)
    {
        if (!(item instanceof FormChoiceGroup)) return;
        FormChoiceGroup choices = (FormChoiceGroup)item;
        for (int i = 0; i < choices.size(); i++)
        {
            TreeNode node = addNode(parent, new FormItem(item, choices.getString(i), i, choiceImage(choices, i)));
            setExpandFlag(node, choices.isSelected(i));
        }
    }

    public void vtGetItemDrawData(TreeNode node, ListItem row)
    {
        Object data = node.getData();
        if (data == null) return;
        if (data instanceof FormItem)
        {
            FormItem item = (FormItem)data;
            row.image = item.image;
            row.text = item.text;
            if (item.item instanceof FormChoiceGroup && item.choiceIndex < 0)
            {
                row.image = ContactList.groupIcons.elementAt(node.getExpanded() ? 1 : 0);
                row.fontStyle++;
            }
        }
        else
        {
            row.fontStyle++;
            row.text = (String)data;
        }
        row.color = getTextColor();
    }

    public int vtCompareNodes(TreeNode first, TreeNode second) { return 0; }
    public void vlCursorMoved(VirtualList list) { }

    //#sijapp cond.if target is "MIDP2"#
    protected boolean pointerPressedOnUtem(int index, int x, int y, int mode)
    {
        TreeNode node = getCurrentItem();
        if (node == null) return true;
        if (node.getData() instanceof FormItem)
        {
            FormItem item = (FormItem)node.getData();
            if (item.item instanceof Gauge)
            {
                FormIcon bar = (FormIcon)item.image;
                bar.setValue(x / bar.stepWidth);
                ((Gauge)item.item).setValue(bar.value);
                invalidate();
                return false;
            }
            vlItemClicked(this);
            return super.pointerPressedOnUtem(index, x, y, mode);
        }
        moveCursor(1, false);
        return true;
    }
    //#sijapp cond.end#

    public void vlKeyPress(VirtualList list, int key, int type)
    {
        if (type != KEY_PRESSED) return;
        TreeNode node = getCurrentItem();
        if (node == null) return;
        Object data = node.getData();
        if (key == Canvas.KEY_STAR && data instanceof FormItem)
        {
            FormItem item = (FormItem)data;
            String text = item.item instanceof TextField || item.item instanceof Gauge
                ? item.item.getLabel() : getItemText(data);
            if (text != null && text.length() > 0) new VirtualAlert(this, text, -1).activate(Jimm.display);
        }
        else if (data instanceof FormItem)
        {
            FormItem item = (FormItem)data;
            if (!(item.item instanceof Gauge)) return;
            Gauge gauge = (Gauge)item.item;
            try
            {
                switch (getGameAction(key))
                {
                case Canvas.LEFT: gauge.setValue(gauge.getValue() - 1); break;
                case Canvas.RIGHT: gauge.setValue(gauge.getValue() + 1); break;
                }
            }
            catch (IllegalArgumentException e) { }
            ((FormIcon)item.image).setValue(gauge.getValue());
            notifyItem(gauge);
            invalidate();
        }
        else if (key == Canvas.KEY_NUM1 || key == Canvas.KEY_NUM3)
        {
            moveCursor(1, false);
        }
        else
        {
            try
            {
                int action = getGameAction(key);
                if (action == Canvas.UP) moveCursor(-1, false);
                if (action == Canvas.DOWN) moveCursor(1, false);
            }
            catch (Exception e) { }
        }
    }

    public void vlItemClicked(VirtualList list)
    {
        TreeNode node = getCurrentItem();
        if (node == null || !(node.getData() instanceof FormItem)) return;
        FormItem item = (FormItem)node.getData();
        if (item.item instanceof FormChoiceGroup)
        {
            if (item.choiceIndex < 0) return;
            FormChoiceGroup choices = (FormChoiceGroup)item.item;
            setExpandFlag(node, !node.getExpanded());
            choices.setSelectedIndex(item.choiceIndex, node.getExpanded());
            item.image = choiceImage(choices, item.choiceIndex);
            if (choices.choiceType == Choice.POPUP || choices.choiceType == Choice.EXCLUSIVE)
            {
                TreeNode parent = findParent(getRoot(), node);
                for (int i = parent.size() - 1; i >= 0; i--)
                {
                    TreeNode sibling = parent.elementAt(i);
                    setExpandFlag(sibling, choices.isSelected(i));
                    ((FormItem)sibling.getData()).image = choiceImage(choices, i);
                }
            }
            notifyItem(item.item);
        }
        else if (item.item instanceof TextField)
        {
            TextField field = (TextField)item.item;
            TextBox editor = new TextBox(nonNull(field.getLabel()), field.getString(),
                field.getMaxSize(), field.getConstraints());
            editor.addCommand(editOk);
            editor.addCommand(editCancel);
            if (editor.getConstraints() != TextField.PASSWORD)
            {
                editor.addCommand(JimmUI.cmdPaste);
                //#sijapp cond.if modules_SMILES is "true"#
                editor.addCommand(JimmUI.cmdInsertEmo);
                //#sijapp cond.end#
            }
            //#sijapp cond.if target is "MIDP2"#
            if (Jimm.is_phone_SE()) System.gc();
            //#sijapp cond.end#
            editor.setCommandListener(this);
            Jimm.display.setCurrent(editor);
        }
    }

    public void commandAction(Command command, Displayable displayable)
    {
        if (!(displayable instanceof TextBox)) return;
        TextBox editor = (TextBox)displayable;
        if (command == editOk)
        {
            FormItem item = (FormItem)getCurrentItem().getData();
            TextField field = (TextField)item.item;
            field.setString(editor.getString());
            item.text = getItemText(field);
            notifyItem(field);
            refreshChoices(getDrawItem(0));
        }
        else if (command == JimmUI.cmdPaste)
        {
            try { editor.insert(JimmUI.getClipBoardText(false), editor.getCaretPosition()); }
            catch (Exception e) { }
            return;
        }
        //#sijapp cond.if modules_SMILES is "true"#
        else if (command == JimmUI.cmdInsertEmo)
        {
            try { Emotions.selectEmotion(editor, editor); }
            catch (Exception e) { }
            return;
        }
        //#sijapp cond.end#
        activate(Jimm.display);
    }

    private void refreshChoices(TreeNode parent)
    {
        for (int i = parent.size() - 1; i >= 0; i--)
        {
            FormItem item = (FormItem)parent.elementAt(i).getData();
            if (item.item instanceof FormChoiceGroup && item.choiceIndex != -1)
            {
                FormChoiceGroup choices = (FormChoiceGroup)item.item;
                item.image = choiceImage(choices, item.choiceIndex);
                item.text = choices.getString(item.choiceIndex);
            }
        }
    }

    private void notifyItem(Item item)
    {
        if (itemStateListener != null) itemStateListener.itemStateChanged(item);
    }

    private Icon choiceImage(FormChoiceGroup choices, int index)
    {
        int image = choices.isSelected(index) ? 1 : 0;
        if (choices.choiceType == Choice.EXCLUSIVE || choices.choiceType == Choice.POPUP) image += 2;
        return formImages.elementAt(image);
    }

    private static String fieldText(TextField field)
    {
        String text = field.getString();
        if (field.getConstraints() != TextField.PASSWORD) return text;
        StringBuffer masked = new StringBuffer(text.length());
        for (int i = text.length() - 1; i >= 0; i--) masked.append('*');
        return masked.toString();
    }

    private String fitField(TextField field, Font font)
    {
        String text = fieldText(field);
        int width = getNonScrollerArea() - font.stringWidth("...");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            result.append(ch == '\n' ? ' ' : ch);
            if (font.stringWidth(result.toString()) >= width) break;
        }
        if (result.length() != text.length()) result.append("...");
        return result.toString();
    }

    private static String nonNull(String text) { return text == null ? new String() : text; }

    private String getItemText(Object data)
    {
        if (data instanceof FormChoiceGroup) return ((Item)data).getLabel();
        if (data instanceof FormItem)
        {
            FormItem item = (FormItem)data;
            return item.item instanceof TextField ? fieldText((TextField)item.item) : item.text;
        }
        if (data instanceof TextField) return fitField((TextField)data, getQuickFont(Font.STYLE_PLAIN));
        if (data instanceof StringItem) return nonNull(((StringItem)data).getLabel()) + nonNull(((StringItem)data).getText());
        if (data instanceof Item) return nonNull(((Item)data).getLabel());
        return data instanceof String ? (String)data : null;
    }

    public void deleteAll()
    {
        clear();
        firstItem = true;
    }
}
