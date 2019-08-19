package carrot.rpc.consumer.cluster;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.common.dto.RpcResponse;
import carrot.rpc.consumer.directory.Directory;
import carrot.rpc.consumer.loadbalancer.LoadBalancer;
import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCluster implements Cluster, Invoker {

    private Directory directory;

    private LoadBalancer loadBalancer;

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public Invoker select(RpcRequest request) {
        return loadBalancer.select(directory.list(), request);
    }

    public Object invoke(RpcRequest request) {
    	
        Invoker invoker = select(request);
        
        return doInvoke(invoker, request);
    }

    abstract Object doInvoke(Invoker invoker, RpcRequest request);

}
