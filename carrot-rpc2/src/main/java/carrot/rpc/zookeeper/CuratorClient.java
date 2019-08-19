package carrot.rpc.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

public class CuratorClient {
    private static CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryOneTime(100));
    static {
        client.start();
    }

    public static CuratorFramework getClient(){

        return client;
    }

}
