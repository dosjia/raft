package cn.martinzhao.raft;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/14
 */
@Slf4j
public class ChildChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("init here");
    }
}
