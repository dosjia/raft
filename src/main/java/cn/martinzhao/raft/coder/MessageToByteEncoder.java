package cn.martinzhao.raft.coder;

import cn.martinzhao.raft.bean.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class MessageToByteEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
        ByteBuf sendBuf = Unpooled.buffer();
        byte[] tempArray = msg.getHeader().getCommandId().value();
        sendBuf.writeBytes(tempArray);// Add command ID
        byte[] machineNameByte = msg.getHeader().getMachineName().getBytes(StandardCharsets.UTF_8);
        sendBuf.writeShort(machineNameByte.length);
        sendBuf.writeBytes(machineNameByte);
        sendBuf.writeShort(msg.getBody().length);
        sendBuf.writeBytes(msg.getBody());
        tempArray = new byte[sendBuf.readableBytes()];
        sendBuf.readBytes(tempArray);
        sendBuf = Unpooled.buffer(tempArray.length);
        sendBuf.writeBytes(tempArray);
        out.add(sendBuf);
        log.debug("Write return message.");
    }
}
