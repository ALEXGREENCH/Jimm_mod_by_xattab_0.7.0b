import javax.microedition.lcdui.*;

/** Delegate the actual getter; mutate the real model at an explicit API boundary. */
public class FormChoicesIO {
    public static ChoiceGroup expected;
    public static int mutation,trigger;
    public static boolean fired;
    public static String events="";
    static void failure(int kind) {
        if(kind==6||kind==8)throw new IllegalStateException("probe");
        if(kind==7||kind==9)throw new AssertionError("probe");
    }
    public static String getString(ChoiceGroup choices,int index) {
        if(choices!=expected)throw new AssertionError("Unexpected ChoiceGroup receiver");
        events+="get:"+index+";";
        boolean act=!fired&&index==trigger;
        if(act){fired=true;if(mutation==6||mutation==7)failure(mutation);}
        String text=choices.getString(index);
        if(act) {
            if(mutation==1)choices.append("added",null);
            if(mutation==2)choices.delete(choices.size()-1);
            if(mutation==3)choices.deleteAll();
            if(mutation==4)choices.set(index,"renamed",null);
            if(mutation==5)choices.insert(0,"inserted",null);
            if(mutation==8||mutation==9)failure(mutation);
        }
        return text;
    }
}
