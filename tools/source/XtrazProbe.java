import java.lang.reflect.*;
import java.util.*;

/** Real Xtraz serializers, XML helpers and incoming ActionListener query/response paths. */
public class XtrazProbe extends FileTransferProbe {
    static Class<?> xtraz,listener,chat,splash,util,icons;
    static Object receiver;static Method forward;static int sends,texts,newChats,logs;
    static String call(Method method,Object target,Object...args)throws Exception{
        try{Object value=method.invoke(target,args);return value instanceof byte[]?"bytes:"+b64((byte[])value):value instanceof String?"string:"+units((String)value):String.valueOf(value);}
        catch(InvocationTargetException e){Throwable t=e.getCause();if(t instanceof Error)throw new AssertionError("Broken Xtraz fixture",t);if(error.isInstance(t))return "error:"+f(error,"a","_ErrCode",int.class).getInt(t);return "exception:"+t.getClass().getName()+(t.getClass()==Exception.class?":"+units(t.getMessage()):"");}
    }
    static String units(String s){if(s==null)return "null";byte[] bytes=new byte[s.length()*2];for(int i=0;i<s.length();i++){bytes[i*2]=(byte)(s.charAt(i)>>8);bytes[i*2+1]=(byte)s.charAt(i);}return b64(bytes);}
    static String value(Object o)throws Exception{
        if(o==null)return "null";if(o instanceof String)return units((String)o);if(o instanceof Number||o instanceof Boolean)return String.valueOf(o);if(contact.isInstance(o))return "contact:"+m(contact,"a","getStringValue",String.class,int.class).invoke(o,0);if(icons.isInstance(o))return "icon";
        if(o instanceof Object[]){StringBuilder s=new StringBuilder("[");for(Object v:(Object[])o)s.append(value(v)).append(',');return s.append(']').toString();}return "other:"+o.getClass().getName();
    }
    static void prepare(boolean known,boolean hasChat,boolean open)throws Exception{
        reset(false);actions().clear();string(0,"54321");bool(133,true);bool(166,false);bool(162,false);bool(183,true);bool(172,false);bool(159,true);integer(92,1);integer(110,4);string(32,"Title");string(33,"Description");
        contactItem=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,"12345","Known",false,true);
        m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(contactItem,16,hasChat);m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(contactItem,76,0);
        f(contact,"c","openChat",boolean.class).setBoolean(contactItem,open);f(list,"b","enterContactMenu",boolean.class).setBoolean(null,true);if(known)((Vector)f(list,"a","cItems",Vector.class).get(null)).add(contactItem);
        f(util,"a","counter",int.class).setInt(null,0);m(splash,"a","show",void.class).invoke(null);
    }
    static void snapshot(String label,String result)throws Exception{
        StringBuilder s=new StringBuilder(label).append(':').append(result).append("/counter:").append(f(util,"a","counter",int.class).getInt(null)).append("/open:").append(f(contact,"c","openChat",boolean.class).getBoolean(contactItem)).append("/menu:").append(f(list,"b","enterContactMenu",boolean.class).getBoolean(null));
        Vector packets=(Vector)io.getField("packets").get(null);for(Object p:packets){s.append("/packet:").append(b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));sends++;}packets.clear();
        for(Object item:actions()){Object[] e=(Object[])item;Object[] args=(Object[])e[1];if(e[0].equals("xtraz-log"))args[1]=m(resource,"a","getString",String.class,String.class).invoke(null,args[1]);s.append('/').append(e[0]).append(':').append(value(args));if(e[0].equals("text"))texts++;if(e[0].equals("xtraz-chat"))newChats++;if(e[0].equals("xtraz-log"))logs++;}actions().clear();row(s.toString());
    }
    static void xml()throws Exception{
        Method escape=m(util,"d","MangleXml",String.class,String.class),unescape=m(util,"c","DeMangleXml",String.class,String.class);
        ArrayList<String> seeds=new ArrayList<>(Arrays.asList(null,"","<>&\"'","&lt;&gt;&amp;&quot;&apos;","&amp;lt;","&","&&","&&;","&x;","&&lt;","&lt","x&broken tail","&amp;&amp;","\u0000\ud800\udfff\uffff"));
        char[] all=new char[65536];for(int i=0;i<all.length;i++)all[i]=(char)i;seeds.add(new String(all));
        Random random=new Random(0x787472617aL);String alphabet="<>&;ltampxyz\"'\r\n";for(int i=0;i<1500;i++){StringBuilder s=new StringBuilder();for(int j=0,n=random.nextInt(100);j<n;j++)s.append(alphabet.charAt(random.nextInt(alphabet.length())));seeds.add(s.toString());}
        for(int i=0;i<seeds.size();i++){row("escape-"+i+":"+call(escape,null,seeds.get(i)));row("unescape-"+i+":"+call(unescape,null,seeds.get(i)));}
    }
    static void packets()throws Exception{
        Method query=m(xtraz,"a","a",void.class,String.class,int.class),reply=m(xtraz,"a","a",byte[].class,String.class,int.class,long.class,long.class,String.class);
        for(boolean cp:new boolean[]{false,true})for(String target:new String[]{null,"","1","12345",String.join("",Collections.nCopies(256,"1")),"\u0410\u010d\ud83d\ude00"})for(long now:new long[]{0,-1,2147483648L,1273665600000L,Long.MAX_VALUE})for(int count:new int[]{0,-1,Integer.MAX_VALUE,Integer.MIN_VALUE}){
            prepare(false,true,false);bool(133,cp);io.getField("now").setLong(null,now);f(util,"a","counter",int.class).setInt(null,count);snapshot("query-"+cp+"-"+units(target)+"-"+now+"-"+count,call(query,null,target,0));
        }
        for(int failure=0;failure<3;failure++){prepare(false,true,false);io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);snapshot("query-failure-"+failure,call(query,null,"12345",0));}
        for(boolean cp:new boolean[]{false,true})for(String target:new String[]{null,"","12345","\u0410\u010d\ud83d\ude00"})for(String content:new String[]{null,"","ASCII","<tag>&amp;\u0000\u041f\ud83d\ude00",String.join("",Collections.nCopies(65536,"x"))})for(int seq:new int[]{0,-1,26,65535,65536,Integer.MAX_VALUE,Integer.MIN_VALUE}){
            prepare(false,true,false);bool(133,cp);row("reply-"+cp+"-"+units(target)+"-"+(content==null?-1:content.length())+"-"+seq+":"+call(reply,null,target,seq,0x1234567887654321L,Long.MIN_VALUE,content));
        }
        for(String name:new String[]{"a","b"})for(int length:new int[]{0,1,2,3,4,78,79,80,81,82,100})for(int offset:new int[]{-1,0,1,2,79,99,Integer.MAX_VALUE}){
            byte[] bytes=new byte[length];Arrays.fill(bytes,(byte)0x5a);String result=call(m(xtraz,name,name,int.class,byte[].class,int.class),null,bytes,offset);row("header-"+name+"-"+length+"-"+offset+":"+result+":"+b64(bytes));
        }
    }
    static String escaped(String s){return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
    static byte[] replyData(String uin,String xml)throws Exception{
        byte[] user=uin.getBytes("US-ASCII"),plugin="Script Plug-in: Remote Notification Arrive".getBytes("US-ASCII"),body=xml.getBytes("UTF-8");
        return join(new byte[8],word(2),new byte[]{(byte)user.length},user,new byte[47],le(26),new byte[4],le(1),new byte[1],le(79),new byte[18],dword(plugin.length),plugin,new byte[15],dword(body.length+4),dword(body.length),body);
    }
    static byte[] dword(int n){return new byte[]{(byte)n,(byte)(n>>8),(byte)(n>>16),(byte)(n>>24)};}
    static byte[] queryData(String xml)throws Exception{
        byte[] plugin="Script Plug-in: Remote Notification Arrive".getBytes("US-ASCII"),body=xml.getBytes("UTF-8");
        byte[] core=join(new byte[45],le(26),new byte[4],le(1),new byte[1],new byte[20],dword(plugin.length),plugin,new byte[19],dword(body.length),body);
        return join(new byte[]{1,2,3,4,5,6,7,8},word(2),new byte[]{5},"12345".getBytes("US-ASCII"),new byte[4],tlv(5,join(new byte[26],tlv(10,word(0)),tlv(0x2711,core))));
    }
    static void incoming()throws Exception{
        String goodQuery="<N><QUERY>"+escaped("<Q><PluginID>srvMng</PluginID></Q>")+"</QUERY><NOTIFY>"+escaped("<srv><id>AwayStat</id><senderId>12345</senderId></srv>")+"</NOTIFY></N>";
        String goodReply="<NR><RES>"+escaped("<val srv_id='cAwaySrv'><title>Title</title><desc>Description</desc></val>")+"</RES></NR>";
        ArrayList<String> queries=new ArrayList<>(Arrays.asList("",goodQuery,goodQuery.replace("srvMng","SRVMNG"),goodQuery.replace("srvMng","other"),goodQuery.replace("AwayStat","other"),goodQuery.replace("12345","54321"),goodQuery.replace("<QUERY>",""),goodQuery.replace("</QUERY>",""),goodQuery.replace("<NOTIFY>",""),goodQuery.replace("</NOTIFY>","")));
        for(String tag:new String[]{"&lt;PluginID&gt;","&lt;/PluginID&gt;","&lt;senderId&gt;","&lt;/senderId&gt;"})queries.add(goodQuery.replace(tag,""));
        for(int i=0;i<=goodQuery.length();i++)queries.add(goodQuery.substring(0,i));
        for(String xml:queries){prepare(true,true,false);snapshot("incoming-query-"+units(xml),call(forward,receiver,snac(4,7,0,queryData(xml))));}
        for(boolean known:new boolean[]{false,true})for(boolean enabled:new boolean[]{false,true})for(boolean temp:new boolean[]{false,true})for(int privacy:new int[]{0,1,2,3,4,5})for(int status:new int[]{0,37}){
            prepare(known,true,false);bool(159,enabled);integer(92,status);integer(110,privacy);m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(contactItem,8,temp);snapshot("gate-"+known+"-"+enabled+"-"+temp+"-"+privacy+"-"+status,call(forward,receiver,snac(4,7,0,queryData(goodQuery))));
        }
        ArrayList<String> replies=new ArrayList<>(Arrays.asList("",goodReply,goodReply.replace("Title",""),goodReply.replace("Description",""),goodReply.replace("Title","&amp;lt;"),goodReply.replace("Title","\u041f\ud83d\ude00")));
        for(String tag:new String[]{"<NR><RES>","</RES></NR>","&lt;val srv_id='","&lt;title&gt;","/title&gt;","&lt;desc&gt;","&lt;/desc&gt;"})replies.add(goodReply.replace(tag,""));
        for(int i=0;i<=goodReply.length();i++)replies.add(goodReply.substring(0,i));
        for(String xml:replies)for(boolean known:new boolean[]{false,true})for(boolean hasChat:new boolean[]{false,true}){
            prepare(known,hasChat,true);snapshot("incoming-reply-"+units(xml)+"-"+known+"-"+hasChat,call(forward,receiver,snac(4,11,0,replyData("12345",xml))));
        }
        for(int invisible:new int[]{-1,0,1,65536})for(int visible:new int[]{-1,0,1,65536})for(int privacy:new int[]{2,3,4})for(int status:new int[]{-1,0,1,37,38}){
            prepare(true,true,false);m(contact,"f","setInvisibleId",void.class,int.class).invoke(contactItem,invisible);m(contact,"e","setVisibleId",void.class,int.class).invoke(contactItem,visible);integer(110,privacy);integer(92,status);
            snapshot("list-gate-"+invisible+"-"+visible+"-"+privacy+"-"+status,call(forward,receiver,snac(4,7,0,queryData(goodQuery))));
        }
        for(int failure=0;failure<3;failure++)for(String title:new String[]{null,"","<>&\u0000\u041f"})for(String desc:new String[]{null,"","<>&\ud83d\ude00"}){
            prepare(true,true,false);Object[] optionValues=(Object[])f(options,"a","options",Object[].class).get(null);optionValues[32]=title;optionValues[33]=desc;io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);snapshot("query-response-"+failure+"-"+units(title)+"-"+units(desc),call(forward,receiver,snac(4,7,0,queryData(goodQuery))));
        }
        for(boolean hasChat:new boolean[]{false,true})for(boolean open:new boolean[]{false,true})for(boolean active:new boolean[]{false,true})for(boolean locked:new boolean[]{false,true}){
            prepare(true,hasChat,open);if(active)m(vl,"b","activate",void.class,javax.microedition.lcdui.Display.class).invoke(f(list,"a","tree",tree).get(null),javax.microedition.lcdui.Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(splash,"c","isLocked",boolean.class).setBoolean(null,locked);
            snapshot("reply-open-"+hasChat+"-"+open+"-"+active+"-"+locked,call(forward,receiver,snac(4,11,0,replyData("12345",goodReply))));
        }
    }
    static void exerciseXtraz()throws Exception{
        setupFiles();xtraz=load("ba","jimm.comm.XtrazSM");util=load("co","jimm.comm.Util");listener=load("ae","jimm.comm.ActionListener");splash=load("cv","jimm.SplashCanvas");icons=load("e","DrawControls.Icon");forward=m(listener,"a","forward",void.class,packet);receiver=Modifier.isStatic(forward.getModifiers())?null:instance(listener,new Class<?>[0]);
        xml();packets();incoming();if(sends<20||texts<2||newChats<1||logs<10)throw new AssertionError("Xtraz successful paths: "+sends+"/"+texts+"/"+newChats+"/"+logs);row("sends:"+sends+"/texts:"+texts+"/new-chats:"+newChats+"/logs:"+logs);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseXtraz();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
