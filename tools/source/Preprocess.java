import java.io.File;
import java.util.Hashtable;
import sijapp.Sijapp;
import sijapp.Preprocessor;
import langs.LangsTask;

/** Command-line bridge to the project's original build tasks. */
public final class Preprocess {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("source")) {
            Hashtable defines = new Hashtable();
            defines.put("target", args[3]);
            String enabled = "," + args[4] + ",";
            String[] modules = {"TRAFFIC", "HISTORY", "FILES", "PROXY", "SMILES",
                "ANISMILES", "GIFSMILES", "DEBUGLOG"};
            for (int i = 0; i < modules.length; i++) {
                defines.put("modules_" + modules[i],
                    enabled.indexOf("," + modules[i] + ",") >= 0 ? "true" : "false");
            }
            new Sijapp(new File(args[1]), new File(args[2])).run(new Preprocessor(defines));
        } else {
            LangsTask task = new LangsTask();
            task.setLanguages(args[3]);
            task.setInDir(args[1] + "/lng");
            task.setSrcDir(args[1]);
            task.setOutDir(args[2]);
            task.setIdealLang("EN");
            task.execute();
        }
    }
}
