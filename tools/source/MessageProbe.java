import java.lang.reflect.*;
import java.util.*;

/** Compare incoming protocol parsing and automatic replies, keeping their real application logic. */
public class MessageProbe extends LoginProbe {
    static Class<?> listener,message,plain,url,notice,send,log,xstatus,icon,imageList,util;
    static Object receiver,c;
    static Method forward;
    static byte[] uin=new byte[]{53,52,51,50,49};
    static String encode(String s)throws Exception{return s==null?"null":b64(s.getBytes("UTF-8"));}
    static byte[] le(int n){return new byte[]{(byte)n,(byte)(n>>8)};}
    static void setup(boolean known,boolean chat,boolean online)throws Exception{
        reset(false);log.getMethod("reset").invoke(null);bool(166,false);bool(162,false);bool(183,true);bool(172,false);
        c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"54321","Nick",false,true);
        m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,16,chat);
        m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(c,192,online?0:-1);
        if(known)((Vector)f(list,"a","cItems",Vector.class).get(null)).add(c);
        f(icq,"b","lastStatusChangeTime",String.class).set(null,"12:34");
    }
    static String value(Object o)throws Exception{
        if(o==null)return "null";if(o instanceof String)return encode((String)o);
        if(o instanceof byte[])return "bytes="+b64((byte[])o);
        if(o instanceof Object[]){String s="[";for(Object v:(Object[])o)s+=value(v)+",";return s+"]";}
        if(send.isInstance(o))return "send="+value(f(send,"a","plainMsg",plain).get(o));
        if(message.isInstance(o)){
            String s=plain.isInstance(o)?"plain":url.isInstance(o)?"url":"notice";
            s+=":"+encode((String)f(message,"c","sndrUin",String.class).get(o))+":"+encode((String)f(message,"a","rcvrUin",String.class).get(o))
                +":"+f(message,"b","offline",boolean.class).getBoolean(o)+":"+f(message,"b","messageType",int.class).getInt(o);
            if(plain.isInstance(o))s+=":"+encode((String)f(plain,"a","text",String.class).get(o));
            if(url.isInstance(o))s+=":"+encode((String)f(url,"a","url",String.class).get(o))+":"+encode((String)f(url,"b","text",String.class).get(o));
            if(notice.isInstance(o))s+=":"+f(notice,"a","sysnotetype",int.class).getInt(o)+":"+f(notice,"a","AUTH_granted",boolean.class).getBoolean(o)+":"+encode((String)f(notice,"a","reason",String.class).get(o));
            return s;
        }
        if(icon.isInstance(o)){
            for(int i=0;i<37;i++)if(m(xstatus,"a","getStatusImage",icon,int.class).invoke(null,i)==o)return "xicon="+i;
            Object icons=f(list,"a","imageList",imageList).get(null);
            for(int i=0;i<30;i++){Object v=m(imageList,"a","elementAt",icon,int.class).invoke(icons,i);if(v==o)return "icon="+i;}
            return "other-icon";
        }
        return String.valueOf(o);
    }
    static void snapshot(String label,String result)throws Exception{
        String s=label+":"+result+":mood="+m(xstatus,"b","getStatusIndex",int.class).invoke(m(contact,"a","getXStatus",xstatus).invoke(c))
            +":title="+encode((String)f(contact,"b","clientCap",String.class).get(c))+":answered="+f(contact,"b","autoAnswered",boolean.class).getBoolean(c)
            +":read-status="+f(contact,"d","readStatusMess",boolean.class).getBoolean(c)+":open-chat="+f(contact,"c","openChat",boolean.class).getBoolean(c);
        Vector events=(Vector)log.getField("events").get(null);for(Object v:events)s+=":event="+value(v);events.clear();
        Vector messages=(Vector)io.getField("messages").get(null);for(Object v:messages)s+=":message="+value(v);messages.clear();
        Vector packets=(Vector)io.getField("packets").get(null);for(Object p:packets)s+=":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p));packets.clear();row(s);
    }
    static void step(String label,Object p)throws Exception{snapshot(label,invoke(forward,receiver,p));}
    static Object incoming(int format,byte[] data)throws Exception{return snac(4,7,0,join(new byte[]{1,2,3,4,5,6,7,8},word(format),new byte[]{5},uin,new byte[4],tlv(format==1?2:5,data)));}
    static byte[] format2(int type,byte[] text,boolean utf8)throws Exception{
        byte[] core=join(new byte[45],le(type),new byte[4],le(text.length),text,new byte[8]);
        if(utf8)core=join(core,new byte[]{38,0,0,0},"{0946134E-4C7F-11D1-8222-444553540000}".getBytes("US-ASCII"));
        return join(new byte[26],tlv(0x2711,core));
    }
    static Object presence(int family,int status,byte[] extension)throws Exception{
        byte[] statusData=new byte[]{0,0,(byte)(status>>8),(byte)status};
        return snac(family,family==3?11:6,0,join(new byte[]{5},uin,word(0),word(2),tlv(6,statusData),tlv(29,extension)));
    }
    static byte[] mood(String id,String title)throws Exception{
        byte[] text=title.getBytes("UTF-8"),mood=("icqmood"+id).getBytes("US-ASCII");
        return join(word(2),new byte[]{4,(byte)(text.length+4)},word(text.length),text,word(0),word(14),new byte[]{0,(byte)mood.length},mood);
    }
    static void exercise()throws Exception{
        owner=m(icq,"a","getIcq",icq).invoke(null);listener=load("ae","jimm.comm.ActionListener");message=load("ac","jimm.comm.Message");plain=load("av","jimm.comm.PlainMessage");
        url=load("bk","jimm.comm.UrlMessage");notice=load("s","jimm.comm.SystemNotice");send=load("cl","jimm.comm.SendMessageAction");log=load("MessageIO","MessageIO");
        xstatus=load("bj","jimm.comm.XStatus");icon=load("e","DrawControls.Icon");imageList=load("f","DrawControls.ImageList");util=load("co","jimm.comm.Util");
        forward=m(listener,"a","forward",void.class,packet);receiver=Modifier.isStatic(forward.getModifiers())?null:instance(listener,new Class<?>[0]);
        for(int family:new int[]{2,3})for(boolean chat:new boolean[]{false,true})for(boolean online:new boolean[]{false,true})for(String id:new String[]{"0","36","37","255","broken"}){
            setup(true,chat,online);String label="presence-"+family+"-"+chat+"-"+online+"-"+id;
            step(label,presence(family,0,mood(id,"Status text")));step(label+"-repeat",presence(family,0,mood(id,"Status text")));
            step(label+"-changed",presence(family,0,mood(id,"Other text")));
        }
        byte[] full=mood("5","Status");for(int len=0;len<full.length;len++){
            setup(true,true,true);step("mood-truncated-"+len,presence(3,0,Arrays.copyOf(full,len)));
        }
        for(int format:new int[]{1,2,4})for(int type:new int[]{1,4})for(String text:new String[]{"hello\r\nworld","\u041f\u0440\u0438\u0432\u0435\u0442", "", "x", "description\u00fehttps://example.invalid/"}){
            setup(true,false,true);byte[] bytes=text.getBytes("UTF-8");
            byte[] data=format==1?tlv(0x101,join(new byte[]{0,2,0,0},text.getBytes("UTF-16BE"))):format==2?format2(type,bytes,true):join(new byte[4],le(type),le(bytes.length),bytes);
            step("message-"+format+"-"+type+"-"+text.length(),incoming(format,data));
        }
        for(int format:new int[]{1,2,4})for(int length=0;length<60;length++){
            setup(false,false,false);step("message-truncated-"+format+"-"+length,incoming(format,new byte[length]));
        }
        for(int format:new int[]{1,2,4}){
            byte[] text="Prefix message\u0000".getBytes("UTF-8");
            byte[] valid=format==1?tlv(0x101,join(new byte[]{0,0,0,0},text)):format==2?format2(1,text,true):join(new byte[4],le(1),le(text.length),text);
            for(int length=0;length<valid.length;length++){
                setup(true,false,true);step("valid-prefix-"+format+"-"+length,incoming(format,Arrays.copyOf(valid,length)));
            }
        }
        for(boolean known:new boolean[]{false,true})for(int status:new int[]{0,1,2,4,16,8193,12288,16384,20480,24576})for(String text:new String[]{"","short","%time% %nick%","%TIME% %NICK%","%Time% %Nick%","%TIME% %time% %nick% %nick%"}){
            setup(known,false,true);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)status);
            for(int key:new int[]{7,19,20,21,22,23,24,25,26})string(key,text);
            step("status-request-"+known+"-"+status+"-"+text.length(),incoming(2,format2(1000,empty,false)));
            snapshot("auto-"+known+"-"+status+"-"+text.length(),invoke(m(listener,"a","sendAutoMessage",void.class,contact),null,c));
        }
        for(int guard=0;guard<6;guard++){
            setup(true,false,true);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,1L);string(7,"Away %time% %nick%");integer(110,4);
            if(guard==1)m(contact,"f","setInvisibleId",void.class,int.class).invoke(c,8);
            if(guard==2)m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,8,true);
            if(guard==3)integer(110,2);if(guard==4)integer(110,3);if(guard==5)f(contact,"b","autoAnswered",boolean.class).setBoolean(c,true);
            snapshot("auto-guard-"+guard,invoke(m(listener,"a","sendAutoMessage",void.class,contact),null,c));
            snapshot("auto-repeat-"+guard,invoke(m(listener,"a","sendAutoMessage",void.class,contact),null,c));
        }
        for(boolean enabled:new boolean[]{false,true})for(int command:new int[]{25,27,28}){
            setup(false,false,false);bool(183,enabled);byte[] data=join(new byte[]{5},uin,command==25?join(word(3),new byte[]{97,98,99}):command==27?new byte[]{1}:empty);
            step("notice-"+enabled+"-"+command,snac(19,command,0,data));
        }
        for(boolean read:new boolean[]{false,true})for(String text:new String[]{"ASCII", "\u041f\u0440\u0438\u0432\u0435\u0442", ""}){
            setup(true,true,true);f(contact,"d","readStatusMess",boolean.class).setBoolean(c,read);byte[] bytes=text.getBytes("UTF-8");
            step("away-reply-"+read+"-"+text.length(),snac(4,11,0,join(new byte[10],new byte[]{5},uin,new byte[47],le(1000),new byte[4],le(bytes.length),bytes)));
        }
        for(boolean open:new boolean[]{false,true})for(String title:new String[]{"", "Title", "\u041f\u0440\u0438\u0432\u0435\u0442"}){
            setup(true,true,true);f(contact,"c","openChat",boolean.class).setBoolean(c,open);
            byte[] plugin="Script Plug-in: Remote Notification Arrive".getBytes("US-ASCII");
            byte[] xml=("<NR><RES><val srv_id='1'><title>"+title+"</title><desc>Description</desc></val></RES></NR>").getBytes("UTF-8");
            step("xtraz-reply-"+open+"-"+title.length(),snac(4,11,0,join(new byte[10],new byte[]{5},uin,new byte[47],le(26),new byte[7],le(79),new byte[18],le(plugin.length),word(0),plugin,new byte[23],xml)));
        }
        for(int mode:new int[]{0,1,2})for(int flag:new int[]{0,1,2,3}){
            setup(true,false,true);integer(88,mode);step("typing-"+mode+"-"+flag,snac(4,20,0,join(new byte[10],new byte[]{5},uin,word(flag))));
        }
        setup(true,false,true);step("offline",snac(3,12,0,join(new byte[]{5},uin)));
        for(boolean known:new boolean[]{false,true})for(boolean offline:new boolean[]{false,true}){
            setup(known,false,true);bool(162,true);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,0L);
            string(35,"Challenge");string(36,"Welcome");string(37,"Answer");
            for(String[] names:new String[][]{{"a","uins"},{"b","uin1"},{"c","uin2"}})f(listener,names[0],names[1],Vector.class).set(null,new Vector());
            int idx=0;for(String text:new String[]{"first","second","third","fourth","ANSWER","normal"}){
                Object msg=instance(plain,new Class<?>[]{String.class,String.class,long.class,String.class,boolean.class},"54321","12345",1273665600L,text,offline);
                String result=invoke(m(listener,"a","isSpam",boolean.class,message),null,msg);
                snapshot("spam-"+known+"-"+offline+"-"+(idx++),result+":accepted="+f(listener,"a","uins",Vector.class).get(null)+":counts="+f(listener,"c","uin2",Vector.class).get(null));
            }
        }
        setup(true,false,true);integer(110,4);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,1L);
        string(7,"%nick%");f(contact,"a","name",String.class).set(c,"");
        snapshot("auto-empty-nick",invoke(m(listener,"a","sendAutoMessage",void.class,contact),null,c));
        setup(true,false,true);step("password-legacy",snac(21,3,0,new byte[]{-86,0,10,0,0,0,0,0,0,0,0,0,0,0}));
        for(String text:new String[]{"", "%time%", "%TIME%", "%Time%", "%TIME% %time%", "a%nick%b%nick%c"})for(boolean upper:new boolean[]{false,true})
            row("replace-"+encode(text)+"-"+upper+":"+invoke(m(util,"a","replaceStr",String.class,String.class,String.class,String.class,boolean.class),null,text,"%time%","12:34",upper));
    }
    public static void main(String[] args){try{LoginProbe.run(args,new Exercise(){public void run()throws Exception{exercise();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
