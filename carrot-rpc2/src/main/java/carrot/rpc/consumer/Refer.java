package carrot.rpc.consumer;

import carrot.rpc.common.Invoker;
import carrot.rpc.consumer.cluster.Cluster;
import carrot.rpc.consumer.proxy.JdkProxyFactory;

public class Refer<T> {

    // 接口类型
    private String interfaceName;

    private Class<T>[] interfaceClass;

    private JdkProxyFactory jdkProxyFactory;

    private Cluster cluster;

    private transient volatile T ref;

    public void init(){
        ref = jdkProxyFactory.getProxy(cluster,interfaceClass);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<T>[] getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T>[] interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public JdkProxyFactory getJdkProxyFactory() {
        return jdkProxyFactory;
    }

    public void setJdkProxyFactory(JdkProxyFactory jdkProxyFactory) {
        this.jdkProxyFactory = jdkProxyFactory;
    }

    public T getRef() {
        return ref;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}
