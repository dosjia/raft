package cn.martinzhao.raft.handler;

import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.processor.SetupConnectionResponseProcessor;
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
    private SetupConnectionResponseProcessor setupConnectionResponseProcessor = new SetupConnectionResponseProcessor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("connect successfully.");
        //TODO: Send connection setup to server
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("Get response from server.");
        if (msg instanceof Message) {
            Message message = (Message) msg;
            MessageHeader messageHeader = message.getHeader();
            switch (messageHeader.getCommandId()) {
                case CONNECTION_SETUP_ANSWER:
                    setupConnectionResponseProcessor.channelRead(ctx, message.getBody());
                    break;
                default:

            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
