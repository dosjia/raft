package cn.martinzhao.raft.channel;

import cn.martinzhao.raft.coder.ByteToMessageDecoder;
import cn.martinzhao.raft.coder.FormatEncoder;
import cn.martinzhao.raft.coder.MessageToByteEncoder;
import cn.martinzhao.raft.handler.CustomerReadIdleHandler;
import cn.martinzhao.raft.handler.RequestProcessorHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public class ClientChildChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final static int MAX_LENGTH = 2042;
    private final static ByteBuf DELIMITER = Unpooled.copiedBuffer(new byte[]{0x7e});
    private int timeout = 10000;
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(MAX_LENGTH, DELIMITER));
        ch.pipeline().addLast(new ByteToMessageDecoder());
        ch.pipeline().addLast(new IdleStateHandler(timeout, 0, 0));
        ch.pipeline().addLast(new CustomerReadIdleHandler());
        ch.pipeline().addLast(new RequestProcessorHandler());
        ch.pipeline().addLast(new FormatEncoder());
        ch.pipeline().addLast(new MessageToByteEncoder());
    }
}
