import java.io.*;
import javax.microedition.lcdui.*;

/** Supply resource/hardware/editor inputs; preserve parser, image slicing and controllers. */
public class EmotionsIO extends ResourceIO {
    public static boolean scripted;
    public static int imageMode,starts,collections,caret=Integer.MIN_VALUE;
    public static InputStream resource(Class owner,String path){return scripted&&path.startsWith("/smiles")?open(owner,path):owner.getResourceAsStream(path);}
    public static Image image(String path)throws IOException{
        if(scripted&&path.equals("/smiles.png")){log.append("image:").append(imageMode).append(';');return Image.createImage(imageMode==1?"/emotions/atlas.png":imageMode==2?"/emotions/broken.png":imageMode==3?"/emotions/missing.png":path);}
        if(scripted&&path.startsWith("/smiles/"))return Image.createImage("/emotions/anim/"+path.substring(path.lastIndexOf('/')+1));
        return Image.createImage(path);
    }
    public static void start(Thread thread){starts++;}
    public static void collect(){collections++;}
    public static int caret(TextBox box){return caret==Integer.MIN_VALUE?box.getCaretPosition():caret;}
    public static void flash(boolean constant){log.append("flash:").append(constant).append(';');}
    public static void reset(){files.clear();log.setLength(0);failureAt=-1;closes=0;failClose=false;imageMode=0;starts=0;collections=0;caret=Integer.MIN_VALUE;}
}
