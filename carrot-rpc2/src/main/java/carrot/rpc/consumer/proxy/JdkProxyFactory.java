package carrot.rpc.consumer.proxy;

import carrot.rpc.common.Invoker;
import carrot.rpc.consumer.cluster.AbstractCluster;
import carrot.rpc.consumer.cluster.Cluster;

import java.lang.reflect.Proxy;

public class JdkProxyFactory {

    public<T> T getProxy(Cluster cluster, Class<T>[] interfaces) {
        return (T) Proxy.
                newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        interfaces,
                        new InvokerInvocationHandler(cluster));
    }
}
