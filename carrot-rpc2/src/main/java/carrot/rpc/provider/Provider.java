package carrot.rpc.provider;

import carrot.rpc.common.codec.RpcJdkEncoder;
import carrot.rpc.common.dto.RpcRequest;
import carrot.rpc.common.codec.RpcJdkDecoder;
import carrot.rpc.common.dto.RpcResponse;
import carrot.rpc.provider.handler.InvocationHandler;
import carrot.rpc.provider.handler.ServiceInvoker;
import carrot.rpc.provider.handler.ServiceBean;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Provider implements InitializingBean, ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(Provider.class);

    private ApplicationContext applicationContext;

    private ServiceInvoker invoker;

    private int port;

    public void setPort(int port) {
        this.port = port;
    }

    public void afterPropertiesSet() throws Exception {
        startServer();
    }

    public void export(ServiceBean serviceBean){
        this.invoker.exportService(serviceBean);
    }

    private void startServer() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(10);
        ServerBootstrap server = new ServerBootstrap();
        this.invoker = new ServiceInvoker();
        server
                .group(bossGroup,workerGroup)/*等待连接的服务端*/
                .channel(NioServerSocketChannel.class)//指定NIO的模式
                .childOption(ChannelOption.SO_KEEPALIVE,true)//保持连接
                .childOption(ChannelOption.TCP_NODELAY,true)//同步
                .childHandler(new ChannelInitializer<SocketChannel>() {/* 绑定childHandler*/

                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new RpcJdkDecoder<RpcRequest>(RpcRequest.class))/*向管道里面加自己的业务逻辑*/
                                .addLast(new RpcJdkEncoder<RpcResponse>(RpcResponse.class))
                                .addLast(new InvocationHandler(invoker));
                    }
                });
        ChannelFuture future = server.bind(this.port).sync();
        logger.info("Provider start!");

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
