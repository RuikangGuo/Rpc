package carrot.rpc.consumer.proxy;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.consumer.cluster.AbstractCluster;
import carrot.rpc.consumer.cluster.Cluster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokerInvocationHandler implements InvocationHandler {
    private final Cluster cluster;

    public InvokerInvocationHandler(Cluster cluster) {
        this.cluster = cluster;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        
        Class<?>[] parameterTypes = method.getParameterTypes();
        
        Object rpcResponse = this.cluster.invoke(new RpcRequest(method.getDeclaringClass().getName(), method, args));
        return rpcResponse;
    }
}
