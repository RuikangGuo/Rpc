package carrot.rpc.consumer.loadbalancer;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcRequest;
import io.netty.util.internal.ThreadLocalRandom;

import java.util.List;

public class RandomLoadBalancer implements LoadBalancer {

    public Invoker select(List<Invoker> invokers, RpcRequest request) {
        int index = ThreadLocalRandom.current().nextInt(invokers.size());
        Invoker invoker = invokers.get(index);
        return invoker;
    }

}
