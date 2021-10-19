package cn.martinzhao.raft.service;

import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.VoteResult;
import cn.martinzhao.raft.bean.so.RequestForVoteRequestBody;
import cn.martinzhao.raft.global.LocalCache;
import cn.martinzhao.raft.global.NodeData;
import io.netty.util.internal.StringUtil;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class VoteService {
    public VoteResult requestVoteByOtherNode(int term, String candidate, int lastLogTerm, int lastLogIndex) {
        if (term < NodeData.currentTerm.get()) {
            return VoteResult.builder().term(NodeData.currentTerm.get()).result(false).build();
        }
        LogUnit<Integer> lastLog = NodeData.logs.get(NodeData.logs.size() - 1);
        if ((StringUtil.isNullOrEmpty(NodeData.votedFor) || NodeData.votedFor.equals(candidate)) && lastLogTerm >= lastLog.getTerm() && lastLogIndex >=
                lastLog.getIndex()) {
            NodeData.votedFor = candidate;
            return VoteResult.builder().result(true).term(NodeData.currentTerm.get()).build();
        }
        return VoteResult.builder().result(false).term(NodeData.currentTerm.get()).build();
    }

    public void requestToOtherNodes() {
        Message message = new Message(Command.REQUEST_VOTE);
        message.getHeader().setMachineName(NodeData.machineId);
        LogUnit<Integer> lastLog = NodeData.logs.get(NodeData.logs.size() - 1);
        NodeData.votedFor = NodeData.machineId;
        RequestForVoteRequestBody body = RequestForVoteRequestBody.builder().term(NodeData.currentTerm.incrementAndGet())
                .indexOfLastLog(lastLog == null ? 0 : lastLog.getIndex()).termOfLastLog(lastLog == null ? 0 : lastLog
                        .getTerm()).build();
        message.setBody(body.parseToBytes());
        LocalCache.CONTEXT_HOLDER.values().forEach(item -> item.channel().writeAndFlush(message));
    }


}
