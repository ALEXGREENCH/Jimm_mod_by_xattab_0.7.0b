import java.lang.reflect.*;
import java.util.*;

/** Compare real tree order, selection, counters, visibility and roster mutations with May. */
public class ContactTreeProbe extends ListMenuProbe {
    static Object t,cl;
    static Object[] contacts,groups;
    static Vector roster()throws Exception{return (Vector)f(list,"a","cItems",Vector.class).get(null);}
    static Vector groupRoster()throws Exception{return (Vector)f(list,"b","gItems",Vector.class).get(null);}
    static void flag(Object c,int key,boolean value)throws Exception{m(contact,"a","setBooleanValue",void.class,int.class,boolean.class).invoke(c,key,value);}
    static void value(Object c,int key,int value)throws Exception{m(contact,"a","setIntValue",void.class,int.class,int.class).invoke(c,key,value);}
    static int value(Object c,int key)throws Exception{return (Integer)m(contact,"b","getIntValue",int.class,int.class).invoke(c,key);}
    static String stringValue(Object c,int key)throws Exception{return (String)m(contact,"a","getStringValue",String.class,int.class).invoke(c,key);}
    static Object newContact(int i,int groupId,int status,String name,boolean added)throws Exception {
        Object c=instance(contact,new Class<?>[]{int.class,int.class,String.class,String.class,boolean.class,boolean.class},17+i,groupId,""+(10000+i),name,false,added);
        value(c,192,status);return c;
    }
    static void model(boolean useGroups,boolean hide,boolean hideEmpty,boolean expanded,int sort,int pattern)throws Exception {
        reset(false);resetEnv();bool(136,useGroups);bool(130,hide);bool(129,hideEmpty);bool(155,false);bool(179,false);bool(173,true);bool(134,false);bool(150,true);bool(171,false);bool(156,false);bool(157,false);bool(184,false);
        integer(65,sort);integer(111,0);integer(112,0);integer(118,0);assign();
        cl=instance(list,new Class<?>[0]);t=f(list,"a","tree",tree).get(null);
        f(list,"a","cItems",Vector.class).set(null,new Vector());f(list,"b","gItems",Vector.class).set(null,new Vector());f(list,"a","gNodes",Hashtable.class).set(null,new Hashtable());
        f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);f(list,"a","justConnected",boolean.class).setBoolean(null,true);f(list,"d","haveToBeCleared",boolean.class).setBoolean(null,false);f(list,"e","lastUnknownStatus",int.class).setInt(null,268435456);
        groups=new Object[3];for(int i=0;i<3;i++){groups[i]=instance(group,new Class<?>[]{int.class,String.class},4+i,new String[]{"Zulu","alpha","Empty"}[i]);f(group,"a","expanded",boolean.class).setBoolean(groups[i],expanded);groupRoster().add(groups[i]);}
        String[] names={"Beta","alpha","Gamma","delta","Alpha","orphan"};int[] ids={4,4,5,5,4,9},statuses={-1,0,32,-1,1,16};
        contacts=new Object[6];int online=0;
        for(int i=0;i<6;i++) {
            int status=pattern==1?-1:pattern==2?0:statuses[i];contacts[i]=newContact(i,ids[i],status,names[i],true);
            flag(contacts[i],16,pattern==3&&(i==0||i==2));flag(contacts[i],8,pattern==3&&i==3);
            if(status!=-1)online++;roster().add(contacts[i]);
        }
        f(list,"c","onlineCounter",int.class).setInt(null,online);m(list,"j","buildTree",void.class).invoke(null);m(list,"i","sortAll",void.class).invoke(null);
        f(vl,"H","forcedWidth",int.class).setInt(t,176);f(vl,"I","forcedHeight",int.class).setInt(t,220);actions().clear();
    }
    static String data(Object d)throws Exception {
        if(d==null)return "root";
        if(group.isInstance(d))return "g"+f(group,"a","id",int.class).getInt(d);
        if(contact.isInstance(d))return "c"+stringValue(d,0);
        return "other";
    }
    static String branch(Object n)throws Exception {
        if(n==null)return "null";StringBuilder b=new StringBuilder(data(f(node,"a","data",Object.class).get(n))+":"+f(node,"a","expanded",boolean.class).getBoolean(n)+"[");
        Vector children=(Vector)f(node,"a","items",Vector.class).get(n);if(children!=null)for(Object c:children)b.append(branch(c)).append(';');return b.append(']').toString();
    }
    static void state(String label)throws Exception {
        StringBuilder b=new StringBuilder(label+":tree="+branch(f(tree,"a","root",node).get(t)));
        Object selected=m(tree,"a","getCurrentItem",node).invoke(t);b.append(":selected=").append(selected==null?"null":data(f(node,"a","data",Object.class).get(selected)));
        b.append(":rows=").append(m(vl,"a","getSize",int.class).invoke(t)).append(":locked=").append(f(vl,"b","dontRepaint",boolean.class).getBoolean(t)).append(":built=").append(f(list,"e","treeBuilt",boolean.class).getBoolean(null)).append(":online=").append(f(list,"c","onlineCounter",int.class).getInt(null));
        b.append(":group-map=").append(new TreeSet(((Hashtable)f(list,"a","gNodes",Hashtable.class).get(null)).keySet()));
        for(Object g:groupRoster())b.append(":g=").append(data(g)).append('/').append(enc(m(group,"c","getText",String.class).invoke(g))).append('/').append(f(group,"a","expanded",boolean.class).getBoolean(g));
        for(Object c:roster())b.append(":c=").append(data(c)).append('/').append(value(c,192)).append('/').append(f(contact,"e","booleanValues",int.class).getInt(c)).append('/').append(f(contact,"b","autoAnswered",boolean.class).getBoolean(c));
        for(Object e:actions()){Object[] event=(Object[])e;b.append(":event=").append(event[0]);if(event[0].equals("ui")||event[0].equals("list-sound"))b.append('/').append(((Object[])event[1])[0]);}
        actions().clear();row(b.toString());
    }
    static String call(String old,String name,Object c,Class<?> type)throws Exception{return invoke(m(list,old,name,void.class,type),null,c);}
    static void changed(Object c,boolean selected,boolean sort)throws Exception{row("change:"+invoke(m(list,"a","contactChanged",void.class,contact,boolean.class,boolean.class),null,c,selected,sort));}
    static void trees()throws Exception {
        for(boolean useGroups:new boolean[]{false,true})for(boolean hide:new boolean[]{false,true})for(boolean hideEmpty:new boolean[]{false,true})for(boolean expanded:new boolean[]{false,true})for(int sort:new int[]{0,1,2,3,99})for(int pattern=0;pattern<4;pattern++) {
            model(useGroups,hide,hideEmpty,expanded,sort,pattern);String label="model-"+useGroups+"-"+hide+"-"+hideEmpty+"-"+expanded+"-"+sort+"-"+pattern;state(label);
            value(contacts[0],192,0);changed(contacts[0],true,true);state(label+"-online");
            value(contacts[0],192,-1);changed(contacts[0],false,true);state(label+"-offline");
            flag(contacts[0],16,true);changed(contacts[0],true,true);state(label+"-chat");
            row(label+"-remove:"+call("a","removeContactItem",contacts[2],contact));state(label+"-removed");
            Object replacement=newContact(2,5,32,"Replacement",false);row(label+"-add:"+call("b","addContactItem",replacement,contact));state(label+"-added");
            Object duplicate=newContact(1,4,-1,"Duplicate",false);row(label+"-duplicate:"+call("b","addContactItem",duplicate,contact));state(label+"-replaced");
            f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);m(list,"j","buildTree",void.class).invoke(null);m(list,"i","sortAll",void.class).invoke(null);state(label+"-rebuilt");
        }
    }
    static void lifecycle()throws Exception {
        for(boolean groups:new boolean[]{false,true})for(boolean hide:new boolean[]{false,true})for(int status:new int[]{-1,0,1,2,4,16,32,256}) {
            model(groups,hide,true,true,3,3);m(options,"a","setLong",void.class,int.class,long.class).invoke(null,192,(long)status);
            m(list,"e","resetAutoAnsweredFlag",void.class).invoke(null);state("answers-"+groups+"-"+hide+"-"+status);
            m(list,"c","beforeConnect",void.class).invoke(null);state("before-"+groups+"-"+hide+"-"+status);
            row("before-flags:"+f(list,"a","justConnected",boolean.class).getBoolean(null)+":"+f(list,"d","haveToBeCleared",boolean.class).getBoolean(null));
        }
        for(boolean groups:new boolean[]{false,true})for(boolean hide:new boolean[]{false,true}) {
            model(groups,hide,true,true,2,0);Object g=instance(group,new Class<?>[]{int.class,String.class},11,"New group");
            row("group-add:"+call("a","addGroup",g,group));state("group-added-"+groups+"-"+hide);
            row("group-remove:"+call("b","removeGroup",ContactTreeProbe.groups[0],group));state("group-removed-"+groups+"-"+hide);
        }
    }
    static Method statusMethod()throws Exception {
        return m(list,"a","update",void.class,String.class,int.class,int.class,byte[].class,byte[].class,int.class,int.class,int.class,int.class,int.class,int.class,int.class,int.class);
    }
    static void network()throws Exception {
        for(boolean groups:new boolean[]{false,true})for(boolean hide:new boolean[]{false,true})for(boolean justConnected:new boolean[]{false,true})for(int dc:new int[]{-1,0,1}) {
            model(groups,hide,true,true,3,0);f(list,"a","justConnected",boolean.class).setBoolean(null,justConnected);
            Object c=contacts[0];f(contact,"a","lastOfflineActivity",long.class).setLong(c,1273665540001L);
            String label="network-"+groups+"-"+hide+"-"+justConnected+"-"+dc;int step=0;
            for(int status:new int[]{-1,-1,0,32,1,8192,-1,-1}) {
                io.getField("now").setLong(null,1273665600000L+step*31000L);
                row(label+"-"+step+":result="+invoke(statusMethod(),null,"10000",status,step%3-1,new byte[]{10,0,0,1},new byte[]{(byte)192,(byte)168,1,2},65537,dc,9,0xabcdef01,100+step,200+step,300+step,400+step));
                state(label+"-"+step);
                StringBuilder b=new StringBuilder(label+"-"+step+":time="+f(contact,"b","statusUpdateTime",long.class).getLong(c)+":last-offline="+f(contact,"a","lastOfflineActivity",long.class).getLong(c)+":offline="+enc(stringValue(c,3))+":read-xtraz="+f(contact,"a","readXtraz",boolean.class).getBoolean(c));
                for(int key:new int[]{74,72,73,193,194,195,71,191})b.append(':').append(key).append('=').append(value(c,key));
                for(int key:new int[]{225,226})b.append(':').append(key).append('=').append(b64((byte[])m(contact,"a","getIPValue",byte[].class,int.class).invoke(c,key)));
                row(b.toString());step++;
            }
            row(label+":unknown="+invoke(statusMethod(),null,"99999",0,-1,null,null,0,-1,0,0,-1,-1,-1,-1)+":last="+f(list,"e","lastUnknownStatus",int.class).getInt(null));
        }
    }
    static void rosterUpdates()throws Exception {
        Class<?> array=Array.newInstance(item,0).getClass();
        for(boolean groups:new boolean[]{false,true})for(boolean hide:new boolean[]{false,true})for(boolean clear:new boolean[]{false,true}) {
            model(groups,hide,true,true,3,3);f(list,"d","haveToBeCleared",boolean.class).setBoolean(null,clear);f(list,"b","ssiNumberOfItems",int.class).setInt(null,17);
            for(int chunk=0;chunk<3;chunk++) {
                Object items=Array.newInstance(item,3);Array.set(items,0,newContact(7+chunk,11,chunk==0?-1:0,"Chunk "+chunk,true));Array.set(items,1,instance(group,new Class<?>[]{int.class,String.class},11+chunk,"Received "+chunk));
                String result=ref?invoke(m(list,"a","update",void.class,int.class,int.class,array),null,100+chunk,3,items):invoke(m(list,"a","update",void.class,int.class,int.class,int.class,array),null,chunk,100+chunk,3,items);
                String label="roster-"+groups+"-"+hide+"-"+clear+"-"+chunk;row(label+":"+result+":version="+f(list,"a","ssiListLastChangeTime",int.class).getInt(null)+":count="+f(list,"b","ssiNumberOfItems",int.class).getInt(null)+":clear="+f(list,"d","haveToBeCleared",boolean.class).getBoolean(null));state(label+"-received");
                m(list,"j","buildTree",void.class).invoke(null);m(list,"i","sortAll",void.class).invoke(null);state(label+"-rebuilt");
            }
            roster().clear();groupRoster().clear();f(list,"e","treeBuilt",boolean.class).setBoolean(null,false);m(list,"j","buildTree",void.class).invoke(null);state("empty-roster-"+groups+"-"+hide+"-"+clear);
        }
    }
    static void exerciseTree()throws Exception {
        setupFiles();group=load("bb","jimm.GroupItem");trees();lifecycle();network();rosterUpdates();
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTree();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
