import javax.microedition.lcdui.*;

/** Capture the native status editor; choose a status-sender failure at the boundary. */
public class MenuIO {
    public static Displayable screen;
    public static int failure;
    public static void show(Display display, Displayable next) {
        screen = next;
        // Native display replaces the canvas. Model that transition synchronously,
        // so commands cannot be routed to the list underneath the native editor.
        try {
            ClassLoader loader = MenuIO.class.getClassLoader();
            Class list;
            try { list = Class.forName("cd", true, loader); }
            catch (ClassNotFoundException missing) { list = Class.forName("DrawControls.VirtualList", true, loader); }
            for (java.lang.reflect.Field field : list.getDeclaredFields()) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) || !Canvas.class.isAssignableFrom(field.getType())) continue;
                field.setAccessible(true); Object canvas = field.get(null);
                for (java.lang.reflect.Field control : canvas.getClass().getDeclaredFields()) {
                    if (control.getType() != list) continue;
                    control.setAccessible(true); control.set(canvas, null);
                }
            }
        } catch (Exception error) { throw new RuntimeException(error.toString()); }
    }
}
