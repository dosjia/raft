package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.bean.VoteResult;
import cn.martinzhao.raft.service.VoteService;
import cn.martinzhao.raft.util.ByteUtil;
import cn.martinzhao.raft.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/19
 */
@Slf4j
public class RequestVoteRequestProcessor implements IProcessor {
    private VoteService voteService = new VoteService();

    @Override
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        MessageHeader header = ctx.channel().attr(Constants.MESSAGE_HEADER_ATTRIBUTE).get();
        int term = ByteUtil.byteArrayToInt(Arrays.copyOfRange(msg, 0, 4));
        int termOfLastLog = ByteUtil.byteArrayToInt(Arrays.copyOfRange(msg, 4, 8));
        int indexOfLastLog = ByteUtil.byteArrayToInt(Arrays.copyOfRange(msg, 8, 12));
        log.debug("Vote request from machine <{}>.", header.getMachineName());
        VoteResult result = voteService.requestVoteByOtherNode(term, header.getMachineName(), termOfLastLog, indexOfLastLog);
        log.debug("Vote request from machine <{}> with result {}", header.getMachineName(), result.isSuccess());
    }
}
