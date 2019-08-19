package carrot.rpc.provider.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcDecoder<T> extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(RpcDecoder.class);

    private Class<T> genericClass;

    public RpcDecoder(Class<T> genericClass) {
        this.genericClass = genericClass;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] reg = new byte[buf.readableBytes()];
        buf.readBytes(reg);
        String body = new String(reg, "UTF-8");
        logger.info("The server receive  order : " + body);
    }

}
