import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Replay real interface form construction, cached selections and save ordering with explicitly seeded models. */
public class OptionsInterfaceProbe extends OptionsProbe {
    static class ChangingChoices extends ChoiceGroup {
        boolean armed,selected;
        ChangingChoices(){super("model",Choice.MULTIPLE);for(int i=0;i<5;i++)append("item"+i,null);}
        public boolean isSelected(int index){boolean answer=super.isSelected(index);if(armed&&index==4){armed=false;setSelectedIndex(3,selected);}return answer;}
    }
    static String trace()throws Exception{String s="";for(Object item:actions()){Object[] e=(Object[])item;s+=e[0]+":"+enc(Arrays.toString((Object[])e[1]))+";";}return s;}
    static String state()throws Exception{
        StringBuilder table=new StringBuilder();Object[] current=values();for(int i=0;i<current.length;i++){Object value=current[i];table.append(i).append('=').append(value==null?"null":value.getClass().getName()+"/"+enc(value)).append(';');}
        String s=enc(table)+":"+f(options,"d","cursorAlpha",int.class).getInt(null)+":"+f(options,"e","captionAlpha",int.class).getInt(null)+":"+f(options,"f","softbarAlpha",int.class).getInt(null)+":"+f(options,"g","fontStyle",int.class).getInt(null);
        for(String[] names:new String[][]{{"j","fontViewChoiceGroup"},{"f","clSortByChoiceGroup"},{"g","captionChoiceGroup"},{"k","chrgChat"}})s+=":"+(value(names[0],names[1],ChoiceGroup.class)!=null);
        for(String[] names:new String[][]{{"a","cursorTransparency"},{"b","captionTransparency"},{"c","softbarTransparency"},{"d","lightLevel"}})s+=":"+(value(names[0],names[1],Gauge.class)!=null);
        s+=":"+(value("z","lightTimeout",TextField.class)!=null)+":"+(value("E","lightManual",ChoiceGroup.class)!=null);
        return s+":"+env.getField("saves").getInt(null)+":"+trace();
    }
    static void exercise()throws Exception{
        setupFiles();of=load("cg","jimm.OptionsForm");initial=values().clone();
        String[][] broken={{"",""},{"j","fontViewChoiceGroup"},{"a","cursorTransparency"},{"b","captionTransparency"},{"c","softbarTransparency"},{"d","lightLevel"},{"z","lightTimeout"}};
        for(int which=0;which<5;which++)for(int index=0;index<broken.length;index++){
            device(which);fresh(false,0);select(3);
            ChoiceGroup list=(ChoiceGroup)value("D","choiceContactList",ChoiceGroup.class);for(int i=0;i<list.size();i++)list.setSelectedIndex(i,!list.isSelected(i));
            ((ChoiceGroup)value("j","fontViewChoiceGroup",ChoiceGroup.class)).setSelectedIndex(1,true);
            ((Gauge)value("a","cursorTransparency",Gauge.class)).setValue(2);((Gauge)value("b","captionTransparency",Gauge.class)).setValue(3);((Gauge)value("c","softbarTransparency",Gauge.class)).setValue(4);
            if(index>0)f(of,broken[index][0],broken[index][1],index==1?ChoiceGroup.class:index==6?TextField.class:Gauge.class).set(c,null);
            actions().clear();row("save:"+which+":"+index+":"+invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null)+":"+state());
        }
        for(int which=0;which<5;which++)for(int option:new int[]{118,101,74})for(Object corrupt:new Object[]{Integer.valueOf(0),Integer.valueOf(-1),Integer.valueOf(3),Integer.valueOf(100),null,"wrong"}){
            device(which);fresh(false,0);values()[option]=corrupt;actions().clear();
            m(text,"a","selectTextByIndex",void.class,int.class).invoke(menu(),3);
            row("open:"+which+":"+option+":"+enc(corrupt)+":"+invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("j","cmdSelect"),null)+":"+state());
        }
        for(int which=0;which<5;which++)for(boolean selected:new boolean[]{false,true})for(boolean changing:new boolean[]{false,true}){
            device(which);fresh(false,0);select(3);ChangingChoices group=new ChangingChoices();group.setSelectedIndex(3,selected);group.selected=!selected;group.armed=changing;
            f(of,"D","choiceContactList",ChoiceGroup.class).set(c,group);f(of,"a","lastGroupsUsed",boolean.class).setBoolean(c,false);f(of,"b","lastHideOffline",boolean.class).setBoolean(c,false);f(of,"c","lastHideEmpty",boolean.class).setBoolean(c,selected);
            actions().clear();row("changing:"+which+":"+selected+":"+changing+":"+invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null)+":"+state()+":"+group.isSelected(3));
        }
        device(0);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exercise();}});if(observations!=145)throw new AssertionError(observations);System.out.println("PASS options interface: 145 real construction/save/model observations");System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
