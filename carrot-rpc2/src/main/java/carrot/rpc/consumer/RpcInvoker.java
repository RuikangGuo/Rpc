package carrot.rpc.consumer;

import carrot.rpc.common.Invoker;
import carrot.rpc.common.RpcFuture;
import carrot.rpc.common.codec.RpcJdkDecoder;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.common.dto.RpcResponse;
import carrot.rpc.common.codec.RpcJdkEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class RpcInvoker implements Invoker {

    private final static Logger logger = LoggerFactory.getLogger(RpcInvoker.class);


    private String host;

    private int port;

    private Bootstrap client = new Bootstrap();

    private Map<Long, RpcFuture> pendingFutures = new ConcurrentHashMap();

    private RpcHandler rpcHandler = new RpcHandler();

    public RpcInvoker(String host, int port) {
        this.host = host;
        this.port = port;
        this.start();
    }

    private void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        client.group(group)/*客户端主动发起连接*/
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new RpcJdkDecoder<RpcResponse>(RpcResponse.class))/*解码*/
                                .addLast(rpcHandler)
                                .addLast(new RpcJdkEncoder<RpcRequest>(RpcRequest.class));/*编码*/
                    }
                });
        try {
            client.connect(host, port).addListener(new GenericFutureListener() {

                public void operationComplete(Future future) throws Exception {
                    logger.info("RpcInvoker init successfully!");
                }
            }).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
    class RpcHandler extends SimpleChannelInboundHandler<RpcResponse> {
/*接受服务器端给的响应*/
        private volatile Channel channel;

        public Channel getChannel() {
            return channel;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            super.channelRegistered(ctx);
            this.channel = ctx.channel();
        }
/*先解码再接收provider端的数据*/
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
            System.out.println(response.requestId);
            RpcFuture rpcFuture = pendingFutures.get(response.requestId);
            rpcFuture.setResponse(response);
        }

        public RpcFuture sendRequest(RpcRequest request) {/*调用channelRead0*/
            RpcFuture rpcFuture = new RpcFuture();
            
            pendingFutures.put(request.requestId, rpcFuture);
            
            final CountDownLatch latch = new CountDownLatch(1);

            this.channel.writeAndFlush(request).addListener(new GenericFutureListener() {
                public void operationComplete(Future future) throws Exception {
                    latch.countDown();//
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {

            }
            return rpcFuture;
        }
    }

    public Object invoke(RpcRequest request) throws ExecutionException, InterruptedException, TimeoutException {
        RpcResponse response = rpcHandler.sendRequest(request).get(1000, TimeUnit.MILLISECONDS); // 同步实现，异步实现，返回future即可
        return response.getResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcInvoker invoker = (RpcInvoker) o;
        return port == invoker.port &&
                Objects.equals(host, invoker.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
