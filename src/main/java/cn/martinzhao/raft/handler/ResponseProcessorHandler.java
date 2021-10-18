package cn.martinzhao.raft.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class ResponseProcessorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("connect successfully.");
        //TODO: Send connection setup to server
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("Get response from server.");
        //TODO: Get machine name from response and store.
    }
}
