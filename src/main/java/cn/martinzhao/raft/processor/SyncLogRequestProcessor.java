package cn.martinzhao.raft.processor;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/20
 */
public class SyncLogRequestProcessor implements IProcessor {
    @Override
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        //TODO: Add logic to process synchronize log request.
    }
}
