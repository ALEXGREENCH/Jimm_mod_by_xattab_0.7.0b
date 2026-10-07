import java.io.*;
import java.util.*;
import javax.microedition.lcdui.Display;
import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;

/** Script external resources/MMAPI only; no application decisions or expected outputs. */
public class SoundIO {
    public static Hashtable resources=new Hashtable();
    public static Vector calls=new Vector(),players=new Vector(),streams=new Vector();
    public static int failAt,failure,step,controlMode,initialState=100;
    public static boolean nullPlayer,vibrationResult;
    public static Object expectedListener;
    public static void reset(){resources.clear();calls.clear();players.clear();streams.clear();failAt=failure=step=controlMode=0;initialState=100;nullPlayer=vibrationResult=false;}
    static void call(String value){calls.addElement(value+":monitor="+(expectedListener!=null&&Thread.holdsLock(expectedListener)));step++;if(step==failAt){if(failure==1)throw new IllegalStateException("device");if(failure==2)throw new AssertionError("device");}}
    public static InputStream resource(Class receiver,String name){
        if(receiver==null)throw new NullPointerException();call("resource:"+receiver.getName()+":"+name);
        byte[] data=(byte[])resources.get(name);if(data==null)return null;
        Stream s=new Stream(streams.size()+1,data);streams.addElement(s);return s;
    }
    public static Player create(InputStream stream,String type)throws MediaException {
        call("create:"+((Stream)stream).id+":"+type);
        if(step==failAt&&failure==3)throw new MediaException("device");
        if(nullPlayer)return null;DevicePlayer p=new DevicePlayer(players.size()+1);players.addElement(p);return p;
    }
    public static void tone(int note,int duration,int volume)throws MediaException {call("tone:"+note+":"+duration+":"+volume);if(step==failAt&&failure==3)throw new MediaException("device");}
    public static boolean vibrate(Display receiver,int duration){if(receiver==null)throw new NullPointerException();call("vibrate:"+duration);return vibrationResult;}
    public static Player seeded(int state){DevicePlayer p=new DevicePlayer(players.size()+1);p.state=state;players.addElement(p);return p;}
    public static String snapshot(Player current){
        StringBuffer b=new StringBuffer("current=");b.append(current==null?0:((DevicePlayer)current).id);
        for(int i=0;i<players.size();i++){DevicePlayer p=(DevicePlayer)players.elementAt(i);b.append("|p:").append(p.id).append(':').append(p.state).append(':').append(p.level).append(':').append(p.closed).append(':').append(p.stopped).append(':').append(p.started).append(':').append(p.listener==expectedListener).append(':').append(p.listener!=null);}
        for(int i=0;i<streams.size();i++){Stream s=(Stream)streams.elementAt(i);b.append("|s:").append(s.id).append(':').append(s.closed);}return b.toString();
    }
    public static class Stream extends ByteArrayInputStream {
        public int id;public boolean closed;Stream(int id,byte[] data){super(data);this.id=id;}
        public void close()throws IOException {call("stream-close:"+id);closed=true;super.close();}
    }
    public static class WrongControl implements Control {}
    public static class DeviceVolume implements VolumeControl {
        DevicePlayer player;DeviceVolume(DevicePlayer p){player=p;}
        public int setLevel(int value){call("volume:"+player.id+":"+value);player.level=value;return -777;}
        public int getLevel(){return player.level;}public void setMute(boolean v){}public boolean isMuted(){return false;}
    }
    public static class DevicePlayer implements Player {
        public int id,state,level,closed,stopped,started;PlayerListener listener;
        DevicePlayer(int id){this.id=id;state=initialState;}
        void operation(String name){call(name+":"+id);}
        void media(String name)throws MediaException {operation(name);if(step==failAt&&failure==3)throw new MediaException("device");}
        public void realize()throws MediaException {media("realize");state=200;}
        public void prefetch()throws MediaException {media("prefetch");state=300;}
        public void start()throws MediaException {media("start");state=400;started++;}
        public void stop()throws MediaException {media("stop");state=300;stopped++;}
        public void close(){operation("close");state=0;closed++;}
        public int getState(){operation("state");return state;}
        public void addPlayerListener(PlayerListener value){call("listener:"+id+":"+(value==expectedListener)+":"+(value!=null));listener=value;}
        public Control getControl(String name){call("control:"+id+":"+name);return controlMode==1?null:controlMode==2?new WrongControl():new DeviceVolume(this);}
        public Control[] getControls(){return new Control[]{getControl("VolumeControl")};}
        public void deallocate(){}public long setMediaTime(long time)throws MediaException{return time;}
        public long getMediaTime(){return 0;}public long getDuration(){return 0;}public String getContentType(){return "device";}
        public void setLoopCount(int count){}public void removePlayerListener(PlayerListener value){}
    }
}
