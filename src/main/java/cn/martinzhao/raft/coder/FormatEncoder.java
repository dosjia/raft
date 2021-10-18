package cn.martinzhao.raft.coder;

import cn.martinzhao.raft.util.ByteUtil;
import cn.martinzhao.raft.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class FormatEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        byte[] array = msg.array();
        byte validate = ByteUtil.getXor(array);
        array = ByteUtil.replaceKeyWords(ByteUtil.concatBytes(array, new byte[]{validate}));
        array = ByteUtil.concatBytes(new byte[]{Constants.IDENTIFY_BYTE}, array,
                new byte[]{Constants.IDENTIFY_BYTE});
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeBytes(array);
        out.add(sendBuf);
    }

}
