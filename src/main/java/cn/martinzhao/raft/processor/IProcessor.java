package cn.martinzhao.raft.processor;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public interface IProcessor {
    void channelRead(ChannelHandlerContext ctx, byte[] msg);
}
