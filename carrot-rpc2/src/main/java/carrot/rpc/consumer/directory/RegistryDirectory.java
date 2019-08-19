package carrot.rpc.consumer.directory;

import carrot.rpc.common.Constants;
import carrot.rpc.common.Invoker;
import carrot.rpc.consumer.RpcInvoker;
import carrot.rpc.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegistryDirectory implements Directory {

    private final static Logger logger = LoggerFactory.getLogger(RegistryDirectory.class);

    private CuratorFramework zkClient;

    private List<Invoker> invokers = new ArrayList<Invoker>();

    final PathChildrenCache pathChildrenCache;

    public RegistryDirectory(Class service) {
        zkClient = CuratorClient.getClient();
        pathChildrenCache = new PathChildrenCache(zkClient, Constants.REGISTRY_PREFIX+"/"+service.getName(), true);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            logger.error("RegistryDirectory start fail, service = " + service.getName(), e);
        }
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener(){
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    String uri = getUri(event.getData().getPath());
                    Invoker invoker = generateInvoker(uri);
                    addInvoker(invoker);
                    
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    String uri = getUri(event.getData().getPath());
                    Invoker invoker = generateInvoker(uri);
                    removeInvoker(invoker);
                }
            }
        });
    }

    public String getUri(String zkPath){
        String[] strs = zkPath.split("/");
        String uri = strs[strs.length-1];
        return uri;
    }

    public void initInvokers() {
        List<ChildData> invokerDatas = pathChildrenCache.getCurrentData();
        for (ChildData data : invokerDatas) {
            String uri= new String(getUri(data.getPath()));
            this.invokers.add(generateInvoker(uri));
        }
    }

    private Invoker generateInvoker(String uri){
        String[] paras = uri.split(":");
        String ip = paras[0];
        int port = Integer.parseInt(paras[1]);
        RpcInvoker invoker = new RpcInvoker(ip,port);
        return invoker;
    }

    private void addInvoker(Invoker invoker){
        this.invokers.add(invoker);
    }

    private void removeInvoker(Invoker invoker){
        Iterator<Invoker> iterator = this.invokers.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(invoker)){
                iterator.remove();
                return;
            }
        }
    }

    public List<Invoker> list() {
        return invokers;
    }
}
