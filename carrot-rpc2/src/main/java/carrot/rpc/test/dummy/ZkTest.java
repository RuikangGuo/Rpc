package carrot.rpc.test.dummy;

import carrot.rpc.consumer.directory.RegistryDirectory;
import carrot.rpc.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import java.util.List;

public class ZkTest {
    static class X extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        CuratorFramework zkClient = CuratorClient.getClient();
//        注册一次，监听一次
//        try {
//            zkClient.getData().usingWatcher(new Watcher() {
//                public void process(WatchedEvent event) {
//                    System.out.println("获取 two 节点 监听器 : " + event);
//                }
//            }).forPath("/b");
//
//        } catch (Exception e) {
//            logger.error("",e);
//        }
        final NodeCache nodeCache = new NodeCache(zkClient, "/b");
        PathChildrenCache pathChildrenCache =new PathChildrenCache(zkClient,"/b",true);
        pathChildrenCache.start();
        nodeCache.start(true);
        List<ChildData> invokerDatas = pathChildrenCache.getCurrentData();
        for (ChildData data : pathChildrenCache.getCurrentData()) {
            System.out.println(new String(data.getData()));
        }
        if (nodeCache.getCurrentData() != null) {
            System.out.println("节点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
        } else {
            System.out.println("节点初始化数据为空...");
        }
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener(){

            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    event.getData().getPath();
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    event.getData();
                }
            }
        });
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() == null) {
                    System.out.println("空");
                    return;
                }
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("节点路径：" + nodeCache.getCurrentData().getPath() + "数据：" + data);
            }
        });
        X x = new X();
        x.start();
        x.join();

        Thread.sleep(10000);


    }
}
