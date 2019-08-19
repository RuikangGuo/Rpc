package carrot.rpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class RpcJdkDecoder<T> extends ByteToMessageDecoder {

    private Class<T> genericClass;

    public RpcJdkDecoder(Class<T> genericClass) {
        this.genericClass = genericClass;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int dataLength = in.readInt();
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        ObjectInputStream inputStream = new ObjectInputStream(is);
        Object object =  inputStream.readObject();
        out.add(object);
    }
}
