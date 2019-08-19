package carrot.rpc.consumer.cluster;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.common.dto.RpcResponse;

public interface Cluster extends Invoker{

    Invoker select(RpcRequest request);
}
