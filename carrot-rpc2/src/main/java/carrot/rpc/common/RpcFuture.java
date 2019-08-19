package carrot.rpc.common;

import carrot.rpc.common.dto.RpcResponse;

import java.util.concurrent.*;

public class RpcFuture implements Future<RpcResponse> {

    RpcResponse response;

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public void setResponse(RpcResponse response) {
        this.response = response;
        countDownLatch.countDown();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public RpcResponse get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return this.response;
    }

    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    	
        countDownLatch.await(timeout,unit);
        return this.response;
    }
}
