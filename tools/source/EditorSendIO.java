/** Terminal captures for the editor send route; editor state is always real. */
public class EditorSendIO {
    public static boolean exists;
    public static String events="", fail="";
    public static int kind;
    static void event(String name) {
        events+=name+";";
        if(fail.equals(name.split("=")[0])) {
            if(kind==1)throw new IllegalStateException("probe");
            if(kind==2)throw new AssertionError("probe");
        }
    }
    public static void send(String text,Object contact) {
        String s="";for(int i=0;i<text.length();i++)s+=Integer.toHexString(text.charAt(i))+",";
        event("send="+s+":"+(contact==null));
    }
    public static boolean chat(Object contact) {event("chat="+(contact==null));return exists;}
    public static boolean chatWithReceiver(Object receiver,Object contact) {return chat(contact);}
    public static void back() {event("back");}
}
