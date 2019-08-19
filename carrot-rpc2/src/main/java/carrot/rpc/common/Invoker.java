package carrot.rpc.common;

import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.common.dto.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Invoker {
    Object invoke(RpcRequest request) throws ExecutionException, InterruptedException, TimeoutException;
}
