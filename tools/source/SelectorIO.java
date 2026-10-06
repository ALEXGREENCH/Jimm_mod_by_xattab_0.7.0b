/** External viewport width supplied to the real selector constructor. */
public class SelectorIO {
    public static int width(Object selector) {
        return Integer.getInteger("jimm.selector.width",176).intValue();
    }
}
