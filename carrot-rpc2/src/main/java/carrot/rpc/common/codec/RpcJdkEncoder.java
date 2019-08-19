package carrot.rpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class RpcJdkEncoder<T> extends MessageToByteEncoder {

    private final static Logger logger = LoggerFactory.getLogger(RpcJdkEncoder.class);

    private Class<T> genericClass;

    public RpcJdkEncoder(Class<T> genericClass) {
        this.genericClass = genericClass;
    }

    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(os);
        outputStream.writeObject(msg);
        outputStream.flush();
        byte[] data = os.toByteArray();
        out.writeInt(data.length);
        out.writeBytes(data);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcJdkEncoder error:",cause);
    }
}
