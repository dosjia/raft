package cn.martinzhao.raft.processor;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class SetupConnectionRequestProcessor implements IProcessor{
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {

    }
}
