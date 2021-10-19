package cn.martinzhao.raft.handler;

import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.processor.RequestVoteRequestProcessor;
import cn.martinzhao.raft.processor.SetupConnectionRequestProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class RequestProcessorHandler extends ChannelInboundHandlerAdapter {
    private SetupConnectionRequestProcessor setupConnectionProcessor = new SetupConnectionRequestProcessor();
    private RequestVoteRequestProcessor requestVoteRequestProcessor = new RequestVoteRequestProcessor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            MessageHeader messageHeader = message.getHeader();
            switch (messageHeader.getCommandId()) {
                case CONNECTION_SETUP:
                    log.debug("Get connection setup request from machine <{}>", message.getHeader().getMachineName());
                    setupConnectionProcessor.channelRead(ctx, message.getBody());
                    break;
                case REQUEST_VOTE:
                    log.debug("Get vote request from machine <{}>", message.getHeader().getMachineName());
                    requestVoteRequestProcessor.channelRead(ctx, message.getBody());
                    break;
                default:

            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        if (!ctx.channel().remoteAddress().toString().startsWith("/" + ignorePrefix)) {
        log.debug("=====================Exception thrown======================");
        cause.printStackTrace();
        log.error(format(ctx, "EXCEPTION", cause), cause);
//        }
        ctx.close();
    }

    private String format(ChannelHandlerContext ctx, String eventName, Object arg) {
        if (arg instanceof ByteBuf) {
            return formatByteBuf(ctx, eventName, (ByteBuf) arg);
        } else if (arg instanceof ByteBufHolder) {
            return formatByteBufHolder(ctx, eventName, (ByteBufHolder) arg);
        } else {
            return formatSimple(ctx, eventName, arg);
        }
    }

    private static String formatByteBuf(ChannelHandlerContext ctx, String eventName, ByteBuf msg) {
        try {
            String chStr = ctx.channel().toString();
            int length = msg.readableBytes();
            if (length == 0) {
                return chStr + ' ' + eventName + ": 0B";
            } else {
                int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
                StringBuilder buf = new StringBuilder(
                        chStr.length() + 1 + eventName.length() + 2 + 10 + 1 + 2 + rows * 80);

                buf.append(chStr).append(' ').append(eventName).append(": ").append(length).append('B').append(StringUtil.NEWLINE);
                ByteBufUtil.appendPrettyHexDump(buf, msg);

                return buf.toString();
            }
        } finally {
            msg.release();
        }
    }

    private static String formatByteBufHolder(ChannelHandlerContext ctx, String eventName, ByteBufHolder msg) {
        String chStr = ctx.channel().toString();
        String msgStr = msg.toString();
        ByteBuf content = msg.content();
        try {
            int length = content.readableBytes();
            if (length == 0) {
                return chStr + ' ' + eventName + ", " + msgStr + ", 0B";
            } else {
                int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
                StringBuilder buf = new StringBuilder(
                        chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 2 + 10 + 1 + 2 + rows * 80);

                buf.append(chStr).append(' ').append(eventName).append(": ").append(msgStr).append(", ").append(length)
                        .append('B').append(StringUtil.NEWLINE);
                ByteBufUtil.appendPrettyHexDump(buf, content);

                return buf.toString();
            }
        } finally {
            content.release();
        }
    }

    private static String formatSimple(ChannelHandlerContext ctx, String eventName, Object msg) {
        String chStr = ctx.channel().toString();
        String msgStr = String.valueOf(msg);
        return chStr + ' ' + eventName + ": " + msgStr;
    }
}
