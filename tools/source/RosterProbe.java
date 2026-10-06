import java.lang.reflect.*;
import java.util.*;

/** Exercise SSI edit transactions with controlled server acknowledgements and UI delivery. */
public class RosterProbe extends LoginProbe {
    static Class<?> group,util,runner;
    static Object c,g1,g2;
    static Vector contacts()throws Exception{return (Vector)f(list,"a","cItems",Vector.class).get(null);}
    static Vector groups()throws Exception{return (Vector)f(list,"b","gItems",Vector.class).get(null);}
    static Vector queued()throws Exception{return (Vector)io.getField("contacts").get(null);}
    static Object setup(int type,boolean isGroup,int groupId)throws Exception{
        reset(false);f(util,"a","counter",int.class).setInt(null,0);
        m(icq,"c","setPrivateStatusId",void.class,int.class).invoke(owner,45);
        g1=instance(group,new Class<?>[]{int.class,String.class},groupId,"Friends");
        g2=instance(group,new Class<?>[]{int.class,String.class},9,"Work");groups().add(g1);groups().add(g2);
        c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17,groupId,"54321","Fixture",true,type!=1&&type!=5);
        m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,8,true);
        m(contact,"a","setIPValue",void.class,int.class,byte[].class).invoke(c,227,tlv(0x6d,new byte[]{2,4,6}));
        if(type!=1&&type!=5)contacts().add(c);
        contacts().add(instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},18,groupId,"54322","Other",false,true));
        return type==4?instance(update,new Class<?>[]{contact,group,group},c,g1,g2):instance(update,new Class<?>[]{item,int.class},isGroup?g1:c,type);
    }
    static void snapshot(String label,Object a,String result)throws Exception{
        String s=label+":"+result+":state="+f(update,"a","state",int.class).getInt(a)+":error="+f(update,"c","errorCode",int.class).getInt(a)
            +":time="+f(update,"a","lastActivity",long.class).getLong(a)+":queue="+queued().size()+":item="+b64(save(c));
        for(int flag:new int[]{1,2,8})s+=":flag"+flag+"="+m(contact,"a","getBooleanValue",boolean.class,int.class).invoke(c,flag);
        for(Object entry:contacts())s+=":contact="+b64(save(entry));
        for(Object entry:groups())s+=":group="+m(group,"b","getName",String.class).invoke(entry);
        Vector packets=(Vector)io.getField("packets").get(null);
        for(Object p:packets)s+=":packet="+b64((byte[])m(packet,"a","toByteArray",byte[].class).invoke(p));packets.clear();row(s);
    }
    static void drain()throws Exception{
        while(!queued().isEmpty()){
            Object value=queued().remove(0);
            Object task=instance(runner,new Class<?>[]{int.class,Object[].class},8,new Object[]{value});
            m(runner,"run","run",void.class).invoke(task);
        }
    }
    static void exercise()throws Exception{
        owner=m(icq,"a","getIcq",icq).invoke(null);group=load("bb","jimm.GroupItem");util=load("co","jimm.comm.Util");runner=load("l","jimm.RunnableImpl");
        for(int type=1;type<=5;type++)for(boolean isGroup:new boolean[]{false,true})for(boolean immediate:new boolean[]{false,true}){
            if(type==4&&isGroup)continue;
            Object a=setup(type,isGroup,4);String label="transaction-"+type+"-"+isGroup+"-"+immediate;
            snapshot(label+"-init",a,invoke(m(update,"a","init",void.class),a));
            if(immediate){drain();snapshot(label+"-ui",a,"drained");}
            for(int ack=0;ack<5;ack++){
                io.getField("now").setLong(null,1273665600100L+ack);
                snapshot(label+"-ack"+ack,a,invoke(m(update,"a","forward",boolean.class,packet),a,snac(19,14,0,word(0))));
            }
            if(!immediate){drain();snapshot(label+"-ui",a,"drained");}
        }
        for(int type=1;type<=5;type++)for(int code:new int[]{1,2,3,10,11,12,13,99}){
            Object a=setup(type,false,4);invoke(m(update,"a","init",void.class),a);drain();
            snapshot("ack-error-"+type+"-"+code,a,invoke(m(update,"a","forward",boolean.class,packet),a,snac(19,14,0,word(code))));
        }
        for(int state:new int[]{-1,0,1,3,4,5,6,7,8,9,10,11,12,14,17,18})for(long age:new long[]{9999,10000,10001}){
            Object a=setup(3,false,4);f(update,"a","state",int.class).setInt(a,state);io.getField("now").setLong(null,1273665600000L+age);
            snapshot("timeout-"+state+"-"+age,a,"complete="+m(update,"a","isCompleted",boolean.class).invoke(a)+",error="+m(update,"b","isError",boolean.class).invoke(a));
        }
        for(int state:new int[]{7,8}){
            Object a=setup(2,false,0);f(update,"a","state",int.class).setInt(a,state);
            snapshot("ungrouped-delete-"+state,a,invoke(m(update,"a","forward",boolean.class,packet),a,snac(19,14,0,word(0))));
        }
        for(int type=1;type<=5;type++){
            Object a=setup(type,false,4);io.getField("failSend").setBoolean(null,true);
            snapshot("send-failed-"+type,a,invoke(m(update,"a","init",void.class),a));
        }
        for(long seed:new long[]{0,1,-1,1273665600000L,Long.MAX_VALUE})for(boolean collision:new boolean[]{false,true}){
            Object a=setup(3,false,4);io.getField("now").setLong(null,seed);int candidate=new Random(seed).nextInt();if(candidate<0)candidate=-candidate;candidate=candidate%0x6fff+0x1000;
            if(collision){m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(c,64,candidate);m(icq,"c","setPrivateStatusId",void.class,int.class).invoke(owner,candidate);}
            row("random-"+seed+"-"+collision+":"+m(util,"b","createRandomId",int.class).invoke(null));
        }
    }
    public static void main(String[] args){try{LoginProbe.run(args,new Exercise(){public void run()throws Exception{exercise();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
