package carrot.rpc.provider.handler;

import carrot.rpc.common.dto.RpcResponse;
import carrot.rpc.common.dto.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InvocationHandler extends SimpleChannelInboundHandler {
  /*服务器端接受客户端的请求*/
    private ServiceInvoker invoker;

    public InvocationHandler(ServiceInvoker invoker) {
        this.invoker = invoker;
    }

    /*数据请求的时候才会调用执行此方法*/
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        RpcRequest request = (RpcRequest) o ;
        System.out.println(request);
        RpcResponse future = invoker.invoke(request);
        channelHandlerContext.writeAndFlush(future);
    }
}
