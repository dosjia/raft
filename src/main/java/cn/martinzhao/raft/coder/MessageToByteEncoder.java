package cn.martinzhao.raft.coder;

import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public class MessageToByteEncoder extends MessageToMessageEncoder<Message> {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) {

        ByteBuf sendBuf = Unpooled.buffer();
        byte[] tempArray = ByteUtil.shortToBytes(msg.getHeader().getCommandId());
        sendBuf.writeBytes(tempArray);// Add command ID

        out.add(sendBuf);
        LOGGER.debug("Write return message. Message is " + msg.getHeader().toString());
    }
}
