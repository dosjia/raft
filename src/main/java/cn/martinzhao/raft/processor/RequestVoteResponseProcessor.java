package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.util.Constants;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/19
 */
public class RequestVoteResponseProcessor implements IProcessor {
    @Override
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {

    }
}
