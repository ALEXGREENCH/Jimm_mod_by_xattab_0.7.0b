import javax.microedition.lcdui.*;

/** Scripted display call sites and work-screen destination; no predicate or instanceof substitution. */
public final class MinimizeIO {
    public static Displayable current,workScreen,other,editor,subclass;
    public static Object display;
    public static boolean shown,after;
    public static String failing="",trace="";
    public static int mode,ordinal;
    public static int[] calls=new int[4];
    public static final class Other extends Canvas { protected void paint(Graphics g){} }
    public static final class Editor extends TextBox { public Editor(){super("","",10,TextField.ANY);} }
    public static String identity(Object value) {
        if(value==null)return "null";if(value==display)return "display";if(value==other)return "other";
        if(value==editor)return "editor";if(value==subclass)return "subclass";if(value==workScreen)return "work-screen";
        throw new AssertionError("Unknown display identity");
    }
    public static void reset() {trace="";calls=new int[4];}
    public static void fail(String target,int call,boolean stage) {
        if(target.equals(failing)&&call==ordinal&&stage==after) {
            if(mode==1)throw new IllegalStateException("scripted display");
            if(mode==2)throw new AssertionError("scripted display");
        }
    }
    public static void setCurrent(Display receiver,Displayable value) {
        int call=++calls[0];trace+="/set:"+identity(receiver)+":"+identity(value);
        if(receiver==null)throw new NullPointerException();fail("set",call,false);current=value;fail("set",call,true);
    }
    public static Displayable getCurrent(Display receiver) {
        int call=++calls[1];trace+="/get:"+identity(receiver);
        if(receiver==null)throw new NullPointerException();fail("get",call,false);Displayable result=current;fail("get",call,true);return result;
    }
    public static boolean isShown(Displayable receiver) {
        int call=++calls[2];trace+="/shown:"+identity(receiver);
        if(receiver==null)throw new NullPointerException();fail("shown",call,false);boolean result=shown;fail("shown",call,true);return result;
    }
    public static void work() {
        int call=++calls[3];trace+="/work";fail("work",call,false);current=workScreen;fail("work",call,true);
    }
}
