package cn.martinzhao.raft.coder;

import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.exception.ApplicationBaseException;
import cn.martinzhao.raft.exception.ExceptionEnum;
import cn.martinzhao.raft.util.ByteUtil;
import cn.martinzhao.raft.util.Constants;
import cn.martinzhao.raft.util.ReplaceByte;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class ByteToMessageDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = ((ByteBuf) msg);
            try {
                if (buf.capacity() != 0) {
                    log.debug("Read " + buf.capacity() + " capacity message.");
                    log.debug("Read " + buf.readableBytes() + " readableBytes message.");
                    byte[] array = new byte[buf.readableBytes()];
                    buf.readBytes(array);
                    array = replaceKeyWords(array);
                    array = validateCheckByte(array);
                    Message message = parseSocketMessage(array);
                    ctx.channel().attr(Constants.MESSAGE_HEADER_ATTRIBUTE).set(message.getHeader());
                    log.info(message.getHeader().toString());
                    ctx.fireChannelRead(message);
                } else {
                    log.debug("Read 0 capacity message.");
                }
            } finally {
                buf.release();
            }

        }

    }

    private byte[] replaceKeyWords(byte[] array) {
        ReplaceByte replace1 = new ReplaceByte();
        Byte[] replacement = {0x7e};
        replace1.setReplacement(replacement);
        Byte[] target = {0x7d, 0x02};
        replace1.setTarget(target);
        ReplaceByte replace2 = new ReplaceByte();
        Byte[] replacement2 = {0x7d};
        replace2.setReplacement(replacement2);
        Byte[] target2 = {0x7d, 0x01};
        replace2.setTarget(target2);
        ReplaceByte[] replaces = new ReplaceByte[]{replace1, replace2};
        Byte[] result = ByteUtil.RepalceByteArray(ByteUtil.toObjects(array), replaces);
        return ByteUtil.toPrimitives(result);
    }

    private byte[] validateCheckByte(byte[] array) throws ApplicationBaseException {
        byte check = array[array.length - 1];
        array = Arrays.copyOfRange(array, 0, array.length - 1);
        byte result = ByteUtil.getXor(array);
        if (result == check) {
            return array;
        } else {
            log.error("Validate should be: " + result + ". But from message is" + check + ".");
            throw new ApplicationBaseException(ExceptionEnum.INVALID_DATA, "Check byte is not passed.");
        }
    }

    private Message parseSocketMessage(byte[] array) throws ApplicationBaseException {
        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setCommandId(Command.getCommand(Arrays.copyOfRange(array, 0, 2)));
        int machineNameLength = ByteUtil.bytesToShort(Arrays.copyOfRange(array, 2, 4));
        header.setMachineName(new String(Arrays.copyOfRange(array, 4, 4 + machineNameLength), StandardCharsets.UTF_8));
        int messageBodyLength = ByteUtil.bytesToShort(Arrays.copyOfRange(array, 4 + machineNameLength, 6 + machineNameLength));
        if (messageBodyLength == 0) {
            message.setBody(new byte[0]);
        } else {
            message.setBody(Arrays.copyOfRange(array, 6 + machineNameLength, 6 + machineNameLength + messageBodyLength));
        }

        message.setHeader(header);
        return message;
    }


    private MessageHeader processProperties(MessageHeader header, byte[] original) throws ApplicationBaseException {
        return header;
    }

}
