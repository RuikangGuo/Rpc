package carrot.rpc.provider.handler;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.dto.RpcResponse;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static carrot.rpc.common.Constants.REGISTRY_PREFIX;

public class ServiceInvoker implements Invoker {

    private final static Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);

    private CuratorFramework zkClient = CuratorClient.getClient();

    Map<String, ServiceBean> serviceBeanMap=new ConcurrentHashMap<String, ServiceBean>();

    public void exportService(ServiceBean service){
        serviceBeanMap.put(service.getClazz().getName(),service);



        try {
            Stat stat = zkClient.checkExists().forPath(REGISTRY_PREFIX + "/"
                    + service.getClazz().getName());
            if(stat==null){
                zkClient.create()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(REGISTRY_PREFIX+"/"
                                +service.getClazz().getName());
            }
            InetAddress addr = InetAddress.getLocalHost();

            zkClient.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(REGISTRY_PREFIX+"/"
                            +service.getClazz().getName()+"/"
                            +addr.getHostAddress()+":"+ 8668,  //后面需要做成可配置的
                            "empty".getBytes());

        } catch (Exception e) {
            logger.error("export service fail ",e);
        }
    }

    public RpcResponse invoke(RpcRequest request){
    	
        RpcResponse rpcResponse = new RpcResponse(request.requestId);
        ServiceBean serviceBean = serviceBeanMap.get(request.getServiceName());
        Method m = null;
        try {
        	
        	m = serviceBean.getService().getClass().getMethod(request.getMethodName(),request.getParameterTypes());
           
        	Object result = m.invoke(serviceBean.getService(), request.getParameters());
           
        	rpcResponse.setResult(result);
        } catch (NoSuchMethodException e) {
            logger.info("invoke error",e);
        } catch (IllegalAccessException e) {
            logger.info("invoke error",e);
        } catch (InvocationTargetException e) {
            logger.info("invoke error",e);
        }
        return rpcResponse;
    }
}
