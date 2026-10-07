import java.io.IOException;
import java.util.*;
import javax.microedition.io.*;
import javax.wireless.messaging.*;

/** Scripted external destinations; the real PhoneBook constructs forms and routes commands. */
public class PhoneBookIO {
    public static Vector events=new Vector();
    public static String failing="";
    public static int mode, ordinal=1, attempts, connectionKind, messageKind;
    public static boolean platformResult;
    public static Endpoint endpoint;
    public static Text text;
    public static class FaultError extends Error {}
    public static void reset(){events.removeAllElements();failing="";mode=0;ordinal=1;attempts=0;connectionKind=messageKind=0;platformResult=false;endpoint=null;text=null;}
    static boolean record(String name,Object[] args){events.addElement(new Object[]{name,args});return name.equals(failing)&&++attempts==ordinal;}
    static void runtime(boolean fail){if(!fail)return;if(mode==4)throw new FaultError();if(mode==3)throw new NullPointerException("scripted");throw new IllegalArgumentException("scripted");}
    static void io(boolean fail)throws IOException{if(fail&&mode==1)throw new IOException("scripted");runtime(fail);}
    public static Connection open(String address)throws IOException{
        io(record("open",new Object[]{address}));if(connectionKind==1)return null;if(connectionKind==2)return new WrongConnection();return endpoint=new Endpoint();
    }
    public static boolean call(Object receiver,String address)throws ConnectionNotFoundException{
        if(receiver==null)throw new NullPointerException();boolean fail=record("call",new Object[]{receiver,address});
        if(fail&&mode==1)throw new ConnectionNotFoundException("scripted");runtime(fail);return platformResult;
    }
    public static void display(Object receiver,Object screen){if(receiver==null)throw new NullPointerException();runtime(record("display",new Object[]{receiver,screen}));}
    public static void menu(){runtime(record("menu",new Object[0]));}
    public static class WrongConnection implements Connection {public void close(){throw new AssertionError("Wrong connection must fail at cast");}}
    public static class BareMessage implements Message {
        public String getAddress(){throw new AssertionError("Unexpected address getter");}
        public Date getTimestamp(){throw new AssertionError("Unexpected timestamp getter");}
        public void setAddress(String value){throw new AssertionError("Unexpected address setter");}
    }
    public static class Text extends BareMessage implements TextMessage {
        public String payload;
        public void setPayloadText(String value){runtime(record("payload",new Object[]{this,value}));payload=value;}
        public String getPayloadText(){throw new AssertionError("Unexpected payload getter");}
    }
    public static class Endpoint implements MessageConnection {
        public boolean closed, sent;
        public Message newMessage(String type){runtime(record("new",new Object[]{this,type}));if(messageKind==1)return null;if(messageKind==2)return new BareMessage();return text=new Text();}
        public void send(Message message)throws IOException{io(record("send",new Object[]{this,message}));if(message!=text)throw new AssertionError("Changed message identity");sent=true;}
        public void close()throws IOException{io(record("close",new Object[]{this}));closed=true;}
        public Message newMessage(String type,String address){throw new AssertionError("Unexpected addressed message overload");}
        public int numberOfSegments(Message message){throw new AssertionError("Unexpected segments call");}
        public Message receive(){throw new AssertionError("Unexpected receive");}
        public void setMessageListener(MessageListener listener){throw new AssertionError("Unexpected message listener");}
    }
}
