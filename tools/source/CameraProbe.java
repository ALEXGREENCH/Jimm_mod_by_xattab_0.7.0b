import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.zip.CRC32;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.microedition.lcdui.*;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

/** Exercise camera lifecycle, failures, snapshots, commands and complete preview rasters. */
public class CameraProbe extends FileTransferProbe {
    static Class<?> camera,media;
    static Object transfer;
    static byte[] png(int width,int height)throws Exception{
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for(int y=0;y<height;y++)for(int x=0;x<width;x++)image.setRGB(x,y,((x*13&255)<<16)|((y*17&255)<<8)|((x+y)&255));
        ByteArrayOutputStream out=new ByteArrayOutputStream();ImageIO.write(image,"png",out);return out.toByteArray();
    }
    static void configure(String field,Object value)throws Exception{media.getField(field).set(null,value);}
    static Object fresh()throws Exception{
        resetEnv();media.getMethod("reset").invoke(null);transfer=make(true);
        Object value=instance(camera,new Class<?>[]{ft},transfer);f(ft,"a","vf",camera).set(transfer,value);return value;
    }
    static String call(Object view,String old,String name)throws Exception{return invoke(m(camera,old,name,void.class),view);}
    static void render(String label,Object view)throws Exception{
        Canvas canvas=(Canvas)view;int w=canvas.getWidth(),h=canvas.getHeight();Image image=Image.createImage(w,h);
        m(camera,"paint","paint",void.class,Graphics.class).invoke(view,image.getGraphics());
        int[] rgb=new int[w*h];image.getRGB(rgb,0,w,0,0,w,h);CRC32 crc=new CRC32();for(int pixel:rgb){crc.update(pixel>>>16);crc.update(pixel>>>8);crc.update(pixel);}
        row(label+":raster="+w+"x"+h+":"+crc.getValue());
    }
    static void state(String label,Object view,String result)throws Exception{
        Image image=(Image)f(camera,"a","img",Image.class).get(view);
        row(label+":"+result+":active="+f(camera,"a","active",boolean.class).getBoolean(view)+":viewfinder="+f(camera,"b","viewfinder",boolean.class).getBoolean(view)
            +":player="+(f(camera,"a","p",Player.class).get(view)!=null)+":control="+(f(camera,"a","vc",VideoControl.class).get(view)!=null)
            +":data="+b64((byte[])f(camera,"a","data",byte[].class).get(view))+":image="+(image==null?"null":image.getWidth()+"x"+image.getHeight())
            +":outer="+(f(ft,"a","vf",camera).get(transfer)==view)+":collections="+env.getField("collections").getInt(null));
        row(label+":io="+media.getField("log").get(null));events(label);actions().clear();media.getField("log").set(null,new StringBuffer());
    }
    static String commandResult(Object view,Object command)throws Exception{return invoke(m(camera,"commandAction","commandAction",void.class,Command.class,Displayable.class),view,command,null);}
    static void exerciseCamera()throws Exception{
        setupFiles();camera=load("cc","jimm.FileTransfer$ViewFinder");media=load("CameraIO","CameraIO");
        integer(111,0);integer(72,1024);integer(70,1000);integer(97,0);string(6,"RUB");
        configure("image",png(8,6));
        Object view=fresh();Method commands=Displayable.class.getDeclaredMethod("getCommands");commands.setAccessible(true);
        String menu="";for(Object c:(Vector)commands.invoke(view))menu+=commandName((Command)c)+":"+(c==cmd("c","cmdBack"))+":"+(c==cmd("a","cmdOk"))+";";row("camera-menu:"+menu);
        state("new",view,"null");render("viewfinder",view);state("snapshot-no-player",view,call(view,"c","takeSnapshot"));
        for(String fail:new String[]{"","create:capture://image","create:","realize","getControl","initDisplayMode","setDisplayLocation","setDisplaySize","setDisplayFullScreen","setVisible","start"}){
            for(int type:new int[]{1,2}){
                view=fresh();configure("fail",fail);configure("failureType",type);configure("failCount",2);
                state("start-"+fail+"-"+type,view,call(view,"a","start"));
                configure("fail","");state("reset-"+fail+"-"+type,view,call(view,"b","reset"));
            }
        }
        for(int type:new int[]{0,1,2,3}){
            view=fresh();configure("fail","create:");configure("failureType",type);configure("failCount",2);state("create-error-"+type,view,call(view,"a","start"));
        }
        view=fresh();configure("noControl",true);state("no-control",view,call(view,"a","start"));
        view=fresh();state("first-start",view,call(view,"a","start"));state("second-start",view,call(view,"a","start"));
        for(int size:new int[]{8,320,640})for(int skips:new int[]{0,1,2,3}){
            view=fresh();configure("image",png(size,size*3/4));configure("snapshotSkips",skips);call(view,"a","start");
            String label="shot-"+size+"-"+skips;state(label,view,call(view,"c","takeSnapshot"));render(label,view);
            if(skips<3){
                state(label+"-send",view,call(view,"e","openSendScreen"));snapshot(label+"-form",transfer);
                InputStream input=(InputStream)f(ft,"a","fis",InputStream.class).get(transfer);row(label+":stream="+b64(input.readAllBytes()));
            }
        }
        for(String failure:new String[]{"getSnapshot","stop","setVisible"}){
            view=fresh();configure("image",png(8,6));call(view,"a","start");configure("fail",failure);configure("failureType",1);configure("failCount",1);
            state("snapshot-fault-"+failure,view,call(view,"c","takeSnapshot"));
        }
        for(String failure:new String[]{"getState","stop","deallocate","close","setVisible"}){
            view=fresh();call(view,"a","start");configure("fail",failure);configure("failureType",3);configure("failCount",1);state("reset-fault-"+failure,view,call(view,"b","reset"));
        }
        for(int action:new int[]{0,1,2,3}){
            view=fresh();configure("image",png(8,6));call(view,"a","start");
            state("command-shot-"+action,view,commandResult(view,cmd("a","cmdOk")));
            String result;
            if(action==0)result=commandResult(view,cmd("c","cmdBack"));
            else if(action==1)result=commandResult(view,cmd("a","cmdOk"));
            else if(action==2)result=invoke(m(camera,"keyPressed","keyPressed",void.class,int.class),view,((Canvas)view).getKeyCode(Canvas.FIRE));
            else result=commandResult(view,new Command("unused",Command.SCREEN,9));
            state("command-after-"+action,view,result);snapshot("command-form-"+action,transfer);
        }
        view=fresh();call(view,"a","start");state("back-live",view,commandResult(view,cmd("c","cmdBack")));
        view=fresh();call(view,"a","start");state("fire-live",view,invoke(m(camera,"keyPressed","keyPressed",void.class,int.class),view,((Canvas)view).getKeyCode(Canvas.FIRE)));
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseCamera();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
