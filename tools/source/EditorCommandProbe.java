import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Real editor command handler, TextBox/clipboard/page state and actual insertion/transliteration. */
public class EditorCommandProbe extends EditorProbe {
    static int commandCalls, quoteCalls, pagingCalls;
    static void exerciseCommands()throws Exception {
        ui=load("cf","jimm.JimmUI");boundary=load("EditorIO","EditorIO");jimm=load("jimm.Jimm","jimm.Jimm");
        vl=load("cd","DrawControls.VirtualList");text=load("bi","DrawControls.TextList");controller=f(ui,"a","_this",ui).get(null);
        for(String[] names:new String[][]{{"g","serverLists"},{"f","tlContactMenu"},{"e","URLList"},{"c","infoTextList"},{"b","aboutTextList"},{"d","lstSelector"}})
            f(ui,names[0],names[1],text).set(null,null);
        for(String[] names:new String[][]{{"b","authTextbox"},{"c","renameTextbox"}})f(ui,names[0],names[1],TextBox.class).set(null,null);
        bool(146,true); // Isolate text commands; send/cancel typing is covered independently.
        Object[] commands={cmd("l","cmdQuote"),cmd("m","cmdPaste"),cmd("p","nextCmd"),cmd("q","prevCmd"),
                           cmd("r","cmdClearText"),cmd("w","transCmd"),cmd("x","detransCmd"),new Command("unknown",Command.SCREEN,7),null};
        Method handler=m(ui,"commandAction","commandAction",void.class,Command.class,Displayable.class);
        for(int cmd=0;cmd<commands.length;cmd++)for(boolean hidden:new boolean[]{false,true})
            for(String seed:new String[]{null,"","one","\u043f\u010d",SendTextProbe.sample(21,2)})
            for(String initial:new String[]{"","abc","\u043f\u010d\n","abcdefg"})for(int size:new int[]{0,1,3})for(int current:new int[]{-1,0,2,5}) {
                bool(154,hidden);clear();if(seed!=null)put(true,"date","name",seed,get(true));
                resetEditor(new TextBox("old",initial,8,0),size,current,"Caption");
                String label="command-"+commandCalls++;
                row(label+":id="+cmd+":empty="+m(ui,"a","clipBoardIsEmpty",boolean.class).invoke(null)+":result="+actual(handler,controller,commands[cmd],box()));
                state(label);snap(label+"-clipboard");log(label);
                if(cmd<2)quoteCalls++;if(cmd==2||cmd==3)pagingCalls++;
            }
        if(commandCalls!=4320||quoteCalls!=960||pagingCalls!=960)throw new AssertionError("Missing editor command cases");
        row("coverage:"+commandCalls+":"+quoteCalls+":"+pagingCalls);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseCommands();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
