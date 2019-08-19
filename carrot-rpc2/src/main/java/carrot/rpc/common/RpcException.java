package carrot.rpc.common;

public class RpcException extends RuntimeException {
    private String message;

    public RpcException(String message) {
        this.message = message;
    }

}
