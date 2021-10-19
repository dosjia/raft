package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.NodeData;
import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.util.ByteUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class SetupConnectionRequestProcessor implements IProcessor {
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        Message message = new Message(Command.CONNECTION_SETUP_ANSWER);
        message.getHeader().setMachineName(NodeData.machineId);
        byte[] name = NodeData.machineId.getBytes(StandardCharsets.UTF_8);
        message.setBody(ByteUtil.concatBytes(ByteUtil.shortToBytes((short) name.length), name));
        ctx.channel().writeAndFlush(message);
    }
}
