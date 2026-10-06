import java.lang.reflect.*;
import java.util.*;
import javax.microedition.lcdui.*;

/** Execute the real generic tree and TreeNode with equal model data and callback payloads. */
public class TreeProbe extends VirtualListProbe {
    static Class<?> itemData,treeCommands,listCommands;
    static Object t,root;
    static Object[] nodes,pictures;
    static Vector<String> callbacks=new Vector<String>();
    static int compareMode;
    static Object data(Object n)throws Exception{return n==null?null:f(node,"a","data",Object.class).get(n);}
    static String id(Object n)throws Exception{return n==null?"null":String.valueOf(data(n));}
    static Object at(int index)throws Exception{return m(tree,"a","getDrawItem",node,int.class).invoke(t,index);}
    static Object add(Object parent,String text)throws Exception{return m(tree,"a","addNode",node,node,Object.class).invoke(t,parent,text);}
    static void expand(Object n,boolean value)throws Exception{m(tree,"a","setExpandFlag",void.class,node,boolean.class).invoke(t,n,value);}
    static Object proxy(Class<?> type)throws Exception {
        return Proxy.newProxyInstance(loader,new Class<?>[]{type},new InvocationHandler(){
            public Object invoke(Object proxy,Method method,Object[] args)throws Exception {
                if(method.getReturnType()==int.class) {
                    String a=id(args[0]),b=id(args[1]);callbacks.add("compare:"+a+":"+b);
                    int value=a.substring(a.indexOf(':')+1).compareTo(b.substring(b.indexOf(':')+1));
                    return compareMode==2?0:compareMode==1?-value:value;
                }
                if(args!=null&&args.length==2&&itemData.isInstance(args[1])) {
                    Object dst=args[1];String value=id(args[0]);int index=Integer.parseInt(value.substring(0,value.indexOf(':')));
                    f(itemData,"a","text",String.class).set(dst,value+" row text");
                    f(itemData,"a","fontStyle",int.class).setInt(dst,index%4);f(itemData,"b","color",int.class).setInt(dst,0x223344+index*1000);f(itemData,"c","horizOffset",int.class).setInt(dst,123);
                    String[] old={"a","b","c","d","e","f","g","h"},names={"image","xStatusImg","happyImg","bDayImg","authImg","ignoreImg","visibilityImg","clientImg"};
                    for(int i=0;i<8;i++)f(itemData,old[i],names[i],icon).set(dst,(index&(1<<i))==0?null:pictures[i]);
                } else if(args!=null)callbacks.add(method.getParameterTypes().length==3?"key:"+args[1]+":"+args[2]:method.getName().equals(n("a","vlCursorMoved"))?"moved":"clicked");
                return null;
            }
        });
    }
    static void model(boolean auto,int mask,int step,int font,int width,int sort)throws Exception {
        compareMode=sort;callbacks.clear();alpha(171,171,128);background(true);bool(143,false);bool(185,false);integer(118,1);
        m(vl,"d","assignSoftKeys",void.class).invoke(null);
        t=instance(tree,new Class<?>[]{String.class,boolean.class},"Nested tree",auto);root=f(tree,"a","root",node).get(t);
        f(tree,"a","commands",treeCommands).set(t,proxy(treeCommands));m(vl,"a","setVLCommands",void.class,listCommands).invoke(t,proxy(listCommands));
        m(vl,"a","setCommandListener",void.class,CommandListener.class).invoke(t,new CommandListener(){public void commandAction(Command c,Displayable d){callbacks.add("command:"+c.getLabel());}});
        for(int place:new int[]{1,5})m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(t,new Command("Open",Command.OK,1),place);
        m(vl,"a","addCommandEx",void.class,Command.class,int.class).invoke(t,new Command("Back",Command.BACK,2),2);
        m(vl,"b","activate",void.class,Display.class).invoke(t,Display.getDisplay(org.microemu.MIDletBridge.getCurrentMIDlet()));f(vl,"H","forcedWidth",int.class).setInt(t,width);f(vl,"I","forcedHeight",int.class).setInt(t,width==90?80:220);
        m(tree,"a","setStepSize",void.class,int.class).invoke(t,step);m(vl,"b","setFontSize",void.class,int.class).invoke(t,font);
        String[] values={"Zulu","alpha","same","Beta","same","deep","deep6","leaf","epsilon","Alpha","same","tail"};int[] parent={-1,-1,-1,0,0,3,5,6,1,1,9,0};
        nodes=new Object[values.length];for(int i=0;i<nodes.length;i++){nodes[i]=add(parent[i]<0?null:nodes[parent[i]],i+":"+values[i]);expand(nodes[i],(mask&(1<<i))!=0);}
        callbacks.clear();
    }
    static String branch(Object n)throws Exception {
        StringBuilder b=new StringBuilder(id(n)+":"+f(node,"a","expanded",boolean.class).getBoolean(n)+":"+f(node,"a","level",int.class).getInt(n)+"[");
        Vector children=(Vector)f(node,"a","items",Vector.class).get(n);if(children!=null)for(Object child:children)b.append(branch(child)).append(';');return b.append(']').toString();
    }
    static void state(String label)throws Exception {
        boolean dirty=f(tree,"b","isChanged",boolean.class).getBoolean(t);int size=(Integer)m(vl,"a","getSize",int.class).invoke(t);
        StringBuilder b=new StringBuilder(label+":dirty="+dirty+":rows="+size+":selected="+id(m(tree,"a","getCurrentItem",node).invoke(t))+":index="+f(vl,"b","currItem",int.class).getInt(t)+":top="+f(vl,"d","topItem",int.class).getInt(t)+":locked="+f(vl,"b","dontRepaint",boolean.class).getBoolean(t)+":last="+id(f(tree,"b","lastNode",node).get(t))+":tree="+branch(root)+":flat=");
        for(int i=0;i<size;i++)b.append(id(at(i))).append(';');b.append(":callbacks=").append(callbacks);callbacks.clear();row(b.toString());
    }
    static void select(Object n)throws Exception{m(tree,"a","setCurrentItem",void.class,node).invoke(t,n);}
    static void operation(String label,String old,String name,Class<?> result,Class<?>[] types,Object...args)throws Exception {
        String value=invoke(m(tree,old,name,result,types),t,args);row(label+":result="+value);state(label);
    }
    static void models()throws Exception {
        for(boolean auto:new boolean[]{false,true})for(int mask:new int[]{0,1,0x63,0xfff})for(int step:new int[]{-8,0,6,20})for(int font:new int[]{0,8,16})for(int width:new int[]{90,176}) {
            model(auto,mask,step,font,width,0);String label="tree-"+auto+"-"+mask+"-"+step+"-"+font+"-"+width;state(label);render(label,t);
            select(nodes[7]);state(label+"-select-deep");render(label+"-deep",t);
            expand(nodes[0],false);state(label+"-collapse-parent");
            m(vl,"k","lock",void.class).invoke(t);select(nodes[10]);state(label+"-lock");m(vl,"l","unlock",void.class).invoke(t);state(label+"-unlock");
            operation(label+"-remove","a","removeNode",boolean.class,new Class<?>[]{node},nodes[9]);
            operation(label+"-sort","b","sortNode",void.class,new Class<?>[]{node},root);
            operation(label+"-clear","b","clear",void.class,new Class<?>[0]);
        }
    }
    static void input()throws Exception {
        for(boolean auto:new boolean[]{false,true})for(int selected:new int[]{0,3,7,10})for(int step:new int[]{0,6,20}) {
            model(auto,0xfff,step,0,176,0);select(nodes[selected]);String label="keys-"+auto+"-"+selected+"-"+step;
            for(int key:new int[]{-1,-2,-3,-4,-5,50,56,52,54,53,-6,-7}) {m(vl,"c","keyReaction",void.class,int.class,int.class).invoke(t,key,1);state(label+"-"+key);}
            for(int type:new int[]{0,2,3}){m(vl,"c","keyReaction",void.class,int.class,int.class).invoke(t,-5,type);state(label+"-repeat-"+type);}
        }
        for(boolean auto:new boolean[]{false,true})for(int step:new int[]{-8,0,6,20})for(int selected:new int[]{0,3,7})for(int x:new int[]{-1,0,1,15,16,17,20,24,40,90,175}) {
            model(auto,0xfff,step,0,176,0);select(nodes[selected]);String label="hook-"+auto+"-"+step+"-"+selected+"-"+x;
            operation(label,"a","pointerPressedOnUtem",boolean.class,new Class<?>[]{int.class,int.class,int.class,int.class},99,x,3,2);
        }
        for(boolean auto:new boolean[]{false,true})for(int mask:new int[]{0,0xfff})for(int x:new int[]{0,20,80,170}) {
            model(auto,mask,6,0,176,0);int cap=(Integer)m(vl,"m","getCapHeight",int.class).invoke(t);String label="tap-"+auto+"-"+mask+"-"+x;
            for(int y:new int[]{cap+2,cap+20,cap+40,cap+80,200})for(int repeat=0;repeat<2;repeat++){pointer(t,x,y);state(label+"-"+y+"-"+repeat);}
        }
    }
    static void mutations()throws Exception {
        for(int sort:new int[]{0,1,2})for(boolean lock:new boolean[]{false,true})for(int selected:new int[]{0,4,7,10}) {
            model(true,0xfff,6,0,176,sort);select(nodes[selected]);String label="mutate-"+sort+"-"+lock+"-"+selected;if(lock)m(vl,"k","lock",void.class).invoke(t);
            for(Object parent:new Object[]{root,nodes[0],nodes[1]})operation(label+"-sort-"+id(parent),"b","sortNode",void.class,new Class<?>[]{node},parent);
            operation(label+"-delete","a","deleteChild",void.class,new Class<?>[]{node,int.class},nodes[0],1);
            operation(label+"-insert","a","insertChild",void.class,new Class<?>[]{node,node,int.class},nodes[1],nodes[4],0);
            Object detached=instance(node,new Class<?>[]{Object.class},"99:detached");select(detached);state(label+"-detached");
            operation(label+"-remove-detached","a","removeNode",boolean.class,new Class<?>[]{node},detached);
            operation(label+"-bad-index","a","deleteChild",void.class,new Class<?>[]{node,int.class},nodes[0],99);
            if(lock)m(vl,"l","unlock",void.class).invoke(t);state(label+"-done");
        }
    }
    static void exerciseTree()throws Exception {
        setupFiles();icon=load("e","DrawControls.Icon");itemData=load("cn","DrawControls.ListItem");treeCommands=load("az","DrawControls.VirtualTreeCommands");listCommands=load("bx","DrawControls.VirtualListCommands");pictures=new Object[8];for(int i=0;i<8;i++)pictures[i]=symbol(5+i,7+i,0x225500+i*3333);
        models();input();mutations();row("rasters:"+rasters);
    }
    public static void main(String[] args){try{run(args,new Exercise(){public void run()throws Exception{exerciseTree();}});System.exit(0);}catch(Throwable e){e.printStackTrace();System.exit(1);}}
}
