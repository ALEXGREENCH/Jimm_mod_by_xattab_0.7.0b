import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Genuine OptionsForm tariff controls, saves, repeated edits and persisted option bytes. */
public final class TariffProbe extends OptionsProbe {
    static int openings, saves;

    static TextField[] tariffFields()throws Exception {
        Vector<TextField> fields=new Vector<TextField>();
        for(Item item:items(f(tree,"a","root",node).get(panel())))
            if(item instanceof TextField)fields.add((TextField)item);
        if(fields.size()!=4)throw new AssertionError("Expected the four native tariff fields");
        TextField[] result=fields.toArray(new TextField[4]);
        if(result[0].getMaxSize()!=6||result[1].getMaxSize()!=6||result[2].getMaxSize()!=4
                ||result[0].getConstraints()!=TextField.ANY||result[1].getConstraints()!=TextField.ANY
                ||result[2].getConstraints()!=TextField.NUMERIC||result[3].getMaxSize()!=4
                ||result[3].getConstraints()!=TextField.ANY)throw new AssertionError("Native tariff field constraints changed");
        openings++;
        return result;
    }

    static void snapshot(String label,TextField[] fields)throws Exception {
        for(int i=0;i<fields.length;i++)row(label+":field-"+i+"="+itemText(fields[i]));
        row(label+":raw="+values()[70]+":"+values()[71]+":"+values()[72]+":"+enc(values()[6]));
    }

    static void exerciseTariffs()throws Exception {
        setupFiles();of=load("cg","jimm.OptionsForm");line=load("bm","DrawControls.TextLine");
        part=load("bc","DrawControls.TextItem");popup=load("ci","DrawControls.VirtualAlert");
        initial=values().clone();
        for(int font:new int[]{0,2})for(int amount:new int[]{0,1,10,999,1000,9999,10000,12000,19999,99999}){
            fresh(false,font);integer(70,amount);integer(71,amount);integer(72,4096);select(7);
            snapshot("initial-"+font+"-"+amount,tariffFields());
        }
        String[] inputs={"","0","1","1.2","1.0001","0.0001","9.9999","00.001","..1","x","-1","-0.01",".","1.2345","1.0000"};
        for(int font:new int[]{0,2})for(int index=0;index<inputs.length;index++)for(int block:new int[]{1,32}){
            fresh(false,font);integer(70,12000);integer(71,1);integer(72,1024);select(7);
            for(int repeat=0;repeat<2;repeat++){
                TextField[] fields=tariffFields();fields[0].setString(inputs[index]);
                fields[1].setString(inputs[(index+repeat+1)%inputs.length]);fields[2].setString(Integer.toString(block));
                fields[3].setString(repeat==0?"USD":"\u0440\u0443\u0431");
                String label="save-"+font+"-"+index+"-"+block+"-"+repeat;
                actions().clear();String outcome=invoke(m(of,"commandAction","commandAction",void.class,Command.class,Displayable.class),c,cmd("s","cmdSave"),null);
                if(!"null".equals(outcome)||env.getField("saves").getInt(null)!=repeat+1||current()!=menu())
                    throw new AssertionError("Tariff save did not complete: "+outcome);
                saves++;row(label+":outcome="+outcome);snapshot(label,fields);
                row(label+":serialized="+b64(serialize())+":saves="+env.getField("saves").getInt(null));
                row(label+":menu="+(current()==menu()));select(7);snapshot(label+"-reopen",tariffFields());
            }
        }
        if(openings!=260||saves!=120)throw new AssertionError("Tariff coverage changed: "+openings+"/"+saves);
        row("openings:"+openings+"/saves:"+saves);
    }

    public static void main(String[] args){
        try{run(args,new Exercise(){public void run()throws Exception{exerciseTariffs();}});System.exit(0);}
        catch(Throwable error){error.printStackTrace();System.exit(1);}
    }
}
