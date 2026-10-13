import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Replay real import/export/cache/stream state using the explicitly bounded MIDP2 Options host. */
public class OptionsImportProbe extends OptionsProbe {
    static void exercise()throws Exception {
        setupFiles();of=load("cg","jimm.OptionsForm");initial=values().clone();
        Field cursor=f(options,"d","cursorAlpha",int.class),caption=f(options,"e","captionAlpha",int.class),softbar=f(options,"f","softbarAlpha",int.class);
        for(int profile=0;profile<2;profile++)for(boolean importing:new boolean[]{false,true})for(int failure:new int[]{0,1,4,6,7,8,10})for(int payload=0;payload<3;payload++) {
            fresh(false,0);integer(100,221);integer(116,123);integer(117,79);byte[] saved=payload!=0?new byte[0]:serialize();
            integer(100,profile==0?238:7);integer(116,profile==0?222:8);integer(117,profile==0?213:9);cursor.setInt(null,profile==0?17:323);caption.setInt(null,profile==0?33:-252);softbar.setInt(null,42);
            if(payload==2)values()[145]="wrong boolean";
            env.getMethod("file",String.class,byte[].class).invoke(null,"/card/jimm_options.sav",saved);env.getField("fileFailure").setInt(null,failure);
            f(of,"d","importingOptions",boolean.class).setBoolean(c,importing);f(of,"b","optionsPath",String.class).set(c,importing?"/card/jimm_options.sav":"/card/");
            String answer=invoke(m(of,"run","run",void.class),c);
            row("import:"+profile+":"+importing+":"+failure+":"+payload+":"+answer+":"+cursor.getInt(null)+":"+caption.getInt(null)+":"+softbar.getInt(null)+":"+values()[100]+":"+values()[116]+":"+values()[117]+":"+env.getField("inputCloses").getInt(null)+":"+env.getField("outputCloses").getInt(null)+":"+env.getField("flushes").getInt(null)+":"+env.getField("fileCloses").getInt(null)+":"+env.getField("saves").getInt(null));
        }
        for(boolean picked:new boolean[]{false,true})for(int mode=0;mode<3;mode++)for(String typed:new String[]{"","/typed.png","/card/one.png"}) {
            fresh(false,0);string(34,"/old.png");select(9);
            String pick="none";
            if(picked){env.getMethod("file",String.class,byte[].class).invoke(null,"/picked.png",new byte[]{1,2,3});pick=invoke(m(ft,"a","onFileSelect",void.class,String.class),make(false),"/picked.png");}
            ChoiceGroup group=(ChoiceGroup)value("B","bgImgChoiceGroup",ChoiceGroup.class);TextField path=(TextField)value("r","bgImgPathTextField",TextField.class);
            group.setSelectedIndex(mode,true);path.setString(typed);actions().clear();
            String answer=invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null);
            String trace="";for(Object item:actions()){Object[] event=(Object[])item;trace+=event[0]+":"+enc(Arrays.toString((Object[])event[1]))+";";}
            row("background:"+picked+":"+mode+":"+enc(typed)+":"+pick+":"+answer+":"+enc(values()[34])+":"+values()[69]+":"+enc(path.getString())+":"+env.getField("saves").getInt(null)+":"+trace);
        }
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exercise();}});if(observations!=102)throw new AssertionError(observations);System.out.println("PASS options import: 102 real import/save controller observations");System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
