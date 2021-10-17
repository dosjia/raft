package cn.martinzhao.raft;

import io.netty.util.internal.StringUtil;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class VoteService {
    public VoteResult requestVoteByOtherNode(int term, String candidate, int lastLogTerm, int lastLogIndex) {
        if (term < NodeData.currentTerm) {
            return VoteResult.builder().term(NodeData.currentTerm).result(false).build();
        }
        LogUnit<Integer> lastLog = NodeData.logs.get(NodeData.logs.size() - 1);
        if (StringUtil.isNullOrEmpty(NodeData.votedFor) && lastLogTerm >= lastLog.getTerm() && lastLogIndex >=
                lastLog.getIndex()) {
            NodeData.votedFor = candidate;
            return VoteResult.builder().result(true).term(NodeData.currentTerm).build();
        }
        return VoteResult.builder().result(false).term(NodeData.currentTerm).build();
    }

    public VoteResult requestToOtherNodes(String candidate) {
        return null;
    }


}
