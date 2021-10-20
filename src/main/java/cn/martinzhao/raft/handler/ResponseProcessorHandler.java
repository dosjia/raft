package cn.martinzhao.raft.handler;

import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.global.NodeData;
import cn.martinzhao.raft.processor.RequestVoteResponseProcessor;
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
    private RequestVoteResponseProcessor requestVoteResponseProcessor = new RequestVoteResponseProcessor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("connect successfully.");
        Message message = new Message(Command.CONNECTION_SETUP);
        message.getHeader().setMessageLength(0);
        message.getHeader().setMachineName(NodeData.MACHINE_ID);
        message.setBody(new byte[0]);
        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("Get response from server.");
        if (msg instanceof Message) {
            Message message = (Message) msg;
            MessageHeader messageHeader = message.getHeader();
            switch (messageHeader.getCommandId()) {
                case CONNECTION_SETUP_ANSWER:
                    log.debug("Get connection setup response from machine <{}>", message.getHeader().getMachineName());
                    setupConnectionResponseProcessor.channelRead(ctx, message.getBody());
                    break;
                case REQUEST_VOTE_ANSWER:
                    log.debug("Get vote response from machine <{}>", message.getHeader().getMachineName());
                    requestVoteResponseProcessor.channelRead(ctx, message.getBody());
                    break;
                default:

            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
