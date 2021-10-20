package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.bean.so.SynchronizeLogRequestBody;
import cn.martinzhao.raft.bean.so.SynchronizeLogResponseBody;
import cn.martinzhao.raft.global.NodeData;
import cn.martinzhao.raft.service.VoteService;
import cn.martinzhao.raft.util.Constants;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/20
 */
public class SyncLogRequestProcessor implements IProcessor {
    private VoteService service = new VoteService();
    @Override
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        SynchronizeLogRequestBody requestBody = new SynchronizeLogRequestBody();
        requestBody.parseFromBytes(msg);
        MessageHeader header = ctx.channel().attr(Constants.MESSAGE_HEADER_ATTRIBUTE).get();
        service.synchronizeLogByOtherNodes(header.getMachineName(),requestBody);
        //TODO: Add logic to process synchronize log request.
    }
}
