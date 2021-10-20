package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.bean.MessageHeader;
import cn.martinzhao.raft.bean.NodeStatus;
import cn.martinzhao.raft.bean.so.VoteResult;
import cn.martinzhao.raft.global.NodeData;
import cn.martinzhao.raft.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/19
 */
@Slf4j
public class RequestVoteResponseProcessor implements IProcessor {
    @Override
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        MessageHeader header = ctx.channel().attr(Constants.MESSAGE_HEADER_ATTRIBUTE).get();
        VoteResult result = VoteResult.builder().build();
        result.parseFromBytes(msg);
        log.debug("Get vote response from machine <{}> with result {}", header.getMachineName(), result.isSuccess());
        Map<String, Boolean> map = NodeData.voteResult;
        map.put(header.getMachineName(), result.isSuccess());
        if (map.values().stream().filter(item -> item == Boolean.TRUE).count() >= (map.size() + 1) / 2) {
            NodeData.status = NodeStatus.LEADER;
            log.info("Machine with name <{}> becomes a leader.", header.getMachineName());
            //TODO: inform other node of this success.
        }
    }
}
