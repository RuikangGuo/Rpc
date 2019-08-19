package carrot.rpc.consumer.cluster;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.RpcException;
import carrot.rpc.common.dto.RpcRequest;

public class FailFastCluster extends AbstractCluster {

    Object doInvoke(Invoker invoker, RpcRequest request) {
        try{
            return invoker.invoke(request);
        } catch (Exception e) {
            throw new RpcException(e.getMessage());
        }
    }
}
