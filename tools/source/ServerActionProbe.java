import java.lang.reflect.*;
import java.util.*;

/** Real SSI list actions, removal requests and authorization messages. */
public class ServerActionProbe extends FileTransferProbe {
    static Class<?> lists,remove,noticeAction,notice,actionBase,message,spy;
    static int successfulSends,acceptedAcks;
    static String invoke(Method method,Object value,Object...args)throws Exception{
        try{return String.valueOf(method.invoke(value,args));}catch(InvocationTargetException e){Throwable cause=e.getCause();if(cause instanceof Error)throw new AssertionError("Broken server-action fixture",cause);
            if(error.isInstance(cause))return "error:"+f(error,"a","_ErrCode",int.class).getInt(cause)+":"+b64(cause.getMessage().getBytes("UTF-8"));return "exception:"+cause.getClass().getName();}
    }
    static Vector changes()throws Exception{return (Vector)spy.getField("changes").get(null);}
    static Object freshContact(String uin)throws Exception{return instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,4,uin,"Known",false,true);}
    static void ids(int visible,int invisible,int ignore)throws Exception{m(contact,"e","setVisibleId",void.class,int.class).invoke(contactItem,visible);m(contact,"f","setInvisibleId",void.class,int.class).invoke(contactItem,invisible);m(contact,"d","setIgnoreId",void.class,int.class).invoke(contactItem,ignore);}
    static String ids()throws Exception{return m(contact,"m","getVisibleId",int.class).invoke(contactItem)+","+m(contact,"n","getInvisibleId",int.class).invoke(contactItem)+","+m(contact,"l","getIgnoreId",int.class).invoke(contactItem);}
    static void prepare(String uin)throws Exception{reset(false);changes().clear();contactItem=freshContact(uin);ids(0,0,0);string(0,"54321");integer(86,0);bool(133,true);}
    static int packets(StringBuilder b)throws Exception{Vector sent=(Vector)io.getField("packets").get(null);int count=sent.size();for(Object p:sent)b.append("/packet:").append(b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p)));sent.clear();return count;}
    static void snapshot(String label,Object a,String result,boolean listAction)throws Exception{
        StringBuilder b=new StringBuilder(label).append(':').append(result).append("/done:").append(invoke(m(a.getClass(),"a","isCompleted",boolean.class),a)).append("/error:").append(invoke(m(a.getClass(),"b","isError",boolean.class),a));
        if(listAction){Date init=(Date)f(lists,"a","init",Date.class).get(a);b.append("/sub:").append(f(lists,"a","subaction",int.class).get(a)).append("/id:").append(f(lists,"c","id",int.class).get(a)).append("/counter:").append(f(lists,"d","packetCounter",int.class).get(a)).append("/time:").append(init==null?"null":init.getTime()).append("/progress:").append(invoke(m(lists,"a","getProgress",int.class),a)).append("/ids:").append(ids());
            for(Object event:changes()){Object[] fields=(Object[])event;b.append("/changed:").append(fields[0]==contactItem).append(':').append(fields[1]).append(':').append(fields[2]);}changes().clear();}
        int sent=packets(b);if(result.equals("null"))successfulSends+=sent;row(b.toString());
    }
    static void lists()throws Exception{
        for(int target:new int[]{-1,0,1,2,3,14,15,65538})for(int oldId:new int[]{0,1,-1,65535,65536,Integer.MIN_VALUE,Integer.MAX_VALUE})for(int failure=0;failure<3;failure++){
            prepare("12345");ids(oldId,oldId,oldId);io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);
            Object a=instance(lists,new Class<?>[]{int.class,contact},target,contactItem);String label="list-"+target+"-"+oldId+"-"+failure;snapshot(label+"-before",a,"new",true);
            String result=invoke(m(lists,"a","init",void.class),a);snapshot(label+"-init",a,result,true);
            if(failure==0&& !result.equals("null"))throw new AssertionError("Valid list init failed: "+result);
            for(int code:new int[]{1,0,0x100,0xffff,0}){Object p=snac(19,14,0,new byte[]{(byte)code,(byte)(code>>8)});result=invoke(m(lists,"a","forward",boolean.class,packet),a,p);snapshot(label+"-ack-"+code,a,result,true);if(result.equals("true"))acceptedAcks++;}
            for(long delay:new long[]{-1,0,2999,3000,3001,Long.MAX_VALUE}){io.getField("now").setLong(null,1273665600000L+delay);snapshot(label+"-timeout-"+delay,a,"tick",true);}
            reset(false);snapshot(label+"-reinit",a,invoke(m(lists,"a","init",void.class),a),true);
        }
        for(String uin:new String[]{"","0","-1","2147483647","2147483648","4294967295","bad"})for(int target:new int[]{2,3,14}){try{prepare(uin);}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken contact fixture",e.getCause());row("list-uin-"+uin+"-"+target+"/contact-exception:"+e.getCause().getClass().getName());continue;}Object a=instance(lists,new Class<?>[]{int.class,contact},target,contactItem);snapshot("list-uin-"+uin+"-"+target,a,invoke(m(lists,"a","init",void.class),a),true);}
        for(int family:new int[]{0,1,19,20})for(int command:new int[]{0,13,14,15})for(int size:new int[]{0,1,2,3,8}){
            prepare("12345");Object a=instance(lists,new Class<?>[]{int.class,contact},2,contactItem);m(lists,"a","init",void.class).invoke(a);((Vector)io.getField("packets").get(null)).clear();snapshot("ack-format-"+family+"-"+command+"-"+size,a,invoke(m(lists,"a","forward",boolean.class,packet),a,snac(family,command,0,new byte[size])),true);
        }
        prepare("12345");Object a=instance(lists,new Class<?>[]{int.class,contact},2,contactItem);m(lists,"a","init",void.class).invoke(a);snapshot("null-packet",a,invoke(m(lists,"a","forward",boolean.class,packet),a,new Object[]{null}),true);snapshot("hello-packet",a,invoke(m(lists,"a","forward",boolean.class,packet),a,hello()),true);
        Object missing=instance(lists,new Class<?>[]{int.class,contact},2,null);row("null-contact-init:"+invoke(m(lists,"a","init",void.class),missing));
        for(long now:new long[]{0,1,-1,Long.MAX_VALUE,Long.MIN_VALUE,Long.MAX_VALUE-3000,Long.MAX_VALUE-2999}){
            prepare("12345");io.getField("now").setLong(null,now);a=instance(lists,new Class<?>[]{int.class,contact},2,contactItem);snapshot("clock-init-"+now,a,invoke(m(lists,"a","init",void.class),a),true);
            for(long delta:new long[]{2999,3000,3001}){io.getField("now").setLong(null,now+delta);snapshot("clock-tick-"+now+"-"+delta,a,"tick",true);}snapshot("clock-late-ack-"+now,a,invoke(m(lists,"a","forward",boolean.class,packet),a,snac(19,14,0,new byte[]{0,0})),true);
        }
        prepare("12345");for(int count:new int[]{-1,0,1,2,21474836,21474837,Integer.MAX_VALUE,Integer.MIN_VALUE}){a=instance(lists,new Class<?>[]{int.class,contact},2,contactItem);f(lists,"d","packetCounter",int.class).setInt(a,count);snapshot("counter-"+count,a,"seed",true);snapshot("counter-ack-"+count,a,invoke(m(lists,"a","forward",boolean.class,packet),a,snac(19,14,0,new byte[]{0,0})),true);}
    }
    static void notices()throws Exception{
        for(boolean cp:new boolean[]{true,false})for(String uin:new String[]{"","12345","-1","\u0410\u010d\ud83d\ude00",String.join("",Collections.nCopies(256,"1"))})for(String reason:new String[]{null,"","ASCII","\u041f\u0440\u0438\u0432\u0435\u0442","a\u0000b\r\nc","\ud83d\ude00\u4e2d"})for(int type=0;type<=7;type++)for(boolean granted:new boolean[]{false,true}){
            prepare("12345");bool(133,cp);Object note=instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},type,uin,granted,reason);Object a=instance(noticeAction,new Class<?>[]{notice},note);
            String label="notice-"+cp+"-"+enc(uin)+"-"+enc(reason)+"-"+type+"-"+granted;
            row(label+"/model:"+f(notice,"a","sysnotetype",int.class).get(note)+":"+f(notice,"a","AUTH_granted",boolean.class).get(note)+":"+enc(f(notice,"a","reason",String.class).get(note))+":"+enc(f(message,"c","sndrUin",String.class).get(note))+":"+enc(f(message,"a","rcvrUin",String.class).get(note))+":"+f(message,"a","newDate",long.class).get(note));
            snapshot(label,a,invoke(m(noticeAction,"a","init",void.class),a),false);
        }
        for(String uin:new String[]{null,"","123","\u0410\u010d\ud83d\ude00",String.join("",Collections.nCopies(256,"1"))})for(int failure=0;failure<3;failure++){
            prepare("12345");io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);Object a=instance(remove,new Class<?>[]{String.class},uin);
            snapshot("remove-"+enc(uin)+"-"+failure,a,invoke(m(remove,"a","init",void.class),a),false);snapshot("remove-forward",a,invoke(m(remove,"a","forward",boolean.class,packet),a,new Object[]{null}),false);
        }
        for(int type:new int[]{4,5})for(boolean granted:new boolean[]{false,true})for(int failure=0;failure<3;failure++){
            prepare("12345");io.getField("failSend").setBoolean(null,failure==1);if(failure==2)f(icq,"a","c",connection).set(null,null);Object note=instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},type,"12345",granted,"reason");Object a=instance(noticeAction,new Class<?>[]{notice},note);
            snapshot("notice-fault-"+type+"-"+granted+"-"+failure,a,invoke(m(noticeAction,"a","init",void.class),a),false);snapshot("notice-forward",a,invoke(m(noticeAction,"a","forward",boolean.class,packet),a,hello()),false);
        }
        prepare("12345");Object a=instance(noticeAction,new Class<?>[]{notice},new Object[]{null});snapshot("notice-null-model",a,invoke(m(noticeAction,"a","init",void.class),a),false);
        for(int type:new int[]{4,5}){Object note=instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},type,null,true,"request");a=instance(noticeAction,new Class<?>[]{notice},note);snapshot("notice-null-recipient-"+type,a,invoke(m(noticeAction,"a","init",void.class),a),false);}
        for(int account:new int[]{-1,0,1,2,3,Integer.MAX_VALUE}){
            prepare("12345");string(0,"111");string(14,"222");string(15,"333");integer(86,account);
            Object note;try{note=instance(notice,new Class<?>[]{int.class,String.class,boolean.class,String.class},5,"12345",true,"request");}catch(InvocationTargetException e){if(e.getCause() instanceof Error)throw new AssertionError("Broken account fixture",e.getCause());row("notice-account-"+account+"/exception:"+e.getCause().getClass().getName());continue;}
            row("notice-account-"+account+"/receiver:"+enc(f(message,"a","rcvrUin",String.class).get(note)));a=instance(noticeAction,new Class<?>[]{notice},note);snapshot("notice-account-send-"+account,a,invoke(m(noticeAction,"a","init",void.class),a),false);
        }
    }
    static void exerciseActions()throws Exception{
        setupFiles();lists=load("cq","jimm.comm.ServerListsAction");remove=load("al","jimm.comm.RemoveMeAction");noticeAction=load("v","jimm.comm.SysNoticeAction");notice=load("s","jimm.comm.SystemNotice");message=load("ac","jimm.comm.Message");actionBase=load("aa","jimm.comm.Action");spy=load("ServerActionIO","ServerActionIO");lists();notices();
        if(successfulSends<300||acceptedAcks<500)throw new AssertionError("Too few successful action paths");row("successful-sends:"+successfulSends+"/accepted-acks:"+acceptedAcks);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseActions();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
