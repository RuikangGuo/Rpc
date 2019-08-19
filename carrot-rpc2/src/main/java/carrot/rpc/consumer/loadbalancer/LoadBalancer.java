package carrot.rpc.consumer.loadbalancer;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcRequest;

import java.util.List;

public interface LoadBalancer {

    //简单起见，后续支持更多算法，这里可能要修改
    Invoker select(List<Invoker> invokers, RpcRequest request);
}
