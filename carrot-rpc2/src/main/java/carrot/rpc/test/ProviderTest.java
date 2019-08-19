package carrot.rpc.test;

import carrot.rpc.provider.Provider;
import carrot.rpc.provider.handler.ServiceBean;

public class ProviderTest {
    public static void main(String[] args) throws Exception {
        Provider provider = new Provider();
        provider.setPort(8668);
        provider.afterPropertiesSet();
        TestService testService = new TestServiceImpl();
        
        ServiceBean serviceBean = new ServiceBean();
        
        serviceBean.setName("testService");
        
        serviceBean.setClazz(TestService.class);
        
        serviceBean.setService(testService);
        
        provider.export(serviceBean);
    }
}
