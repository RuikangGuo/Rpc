package carrot.rpc.common.dto;

import carrot.rpc.common.util.Util;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RpcRequest implements Serializable {

    public final long requestId;

    private String serviceName;

    String methodName;

    Class<?>[] parameterTypes;

    Object[] parameters;

    private String url;

    public RpcRequest(long requestId) {
        this.requestId = requestId;
    }

    public RpcRequest(String serviceName ,Method method, Object[] args) {
        this.requestId = Util.generateRequestId();
        this.serviceName = serviceName;
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        parameters = args;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
