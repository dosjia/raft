package cn.martinzhao.raft.service;

import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.so.VoteResult;
import cn.martinzhao.raft.bean.so.RequestForVoteRequestBody;
import cn.martinzhao.raft.global.LocalCache;
import cn.martinzhao.raft.global.NodeData;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class VoteService {
    public VoteResult requestVoteByOtherNode(int term, String candidate, int lastLogTerm, int lastLogIndex) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        if (term < NodeData.currentTerm.get()) {
            lock.unlock();
            return VoteResult.builder().term(NodeData.currentTerm.get()).success(false).build();
        }
        LogUnit<Integer> lastLog;
        if (NodeData.logs == null || NodeData.logs.isEmpty()) {
            lastLog = null;
        } else {
            lastLog = NodeData.logs.get(NodeData.logs.size() - 1);
        }
        if ((StringUtil.isNullOrEmpty(NodeData.votedFor) || NodeData.votedFor.equals(candidate)) &&
                lastLogTerm >= (lastLog == null ? 0 : lastLog.getTerm()) &&
                lastLogIndex >= (lastLog == null ? 0 : lastLog.getIndex())) {
            NodeData.votedFor = candidate;
            lock.unlock();
            return VoteResult.builder().success(true).term(NodeData.currentTerm.get()).build();
        }
        lock.unlock();
        return VoteResult.builder().success(false).term(NodeData.currentTerm.get()).build();
    }

    public void requestToOtherNodes() {
        Message message = new Message(Command.REQUEST_VOTE);
        message.getHeader().setMachineName(NodeData.MACHINE_ID);
        LogUnit<Integer> lastLog;
        if (NodeData.logs == null || NodeData.logs.isEmpty()) {
            lastLog = null;
        } else {
            lastLog = NodeData.logs.get(NodeData.logs.size() - 1);
        }
        NodeData.votedFor = NodeData.MACHINE_ID;
        RequestForVoteRequestBody body = RequestForVoteRequestBody.builder().term(NodeData.currentTerm.incrementAndGet())
                .indexOfLastLog(lastLog == null ? 0 : lastLog.getIndex()).termOfLastLog(lastLog == null ? 0 : lastLog
                        .getTerm()).build();
        message.setBody(body.parseToBytes());
        LocalCache.CONTEXT_HOLDER.values().forEach(item -> item.channel().writeAndFlush(message));
    }


}
