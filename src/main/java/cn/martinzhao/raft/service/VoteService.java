package cn.martinzhao.raft.service;

import cn.martinzhao.raft.bean.Command;
import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.bean.Message;
import cn.martinzhao.raft.bean.NodeStatus;
import cn.martinzhao.raft.bean.so.RequestForVoteRequestBody;
import cn.martinzhao.raft.bean.so.RequestForVoteResponseBody;
import cn.martinzhao.raft.bean.so.SynchronizeLogRequestBody;
import cn.martinzhao.raft.bean.so.SynchronizeLogResponseBody;
import cn.martinzhao.raft.global.LocalCache;
import cn.martinzhao.raft.global.NodeData;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
@Slf4j
public class VoteService {
    public RequestForVoteResponseBody requestVoteByOtherNode(int term, String candidate, int lastLogTerm, int lastLogIndex) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        if (term < NodeData.currentTerm.get()) {
            lock.unlock();
            return RequestForVoteResponseBody.builder().term(NodeData.currentTerm.get()).success(false).build();
        }
        LogUnit<String> lastLog;
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
            return RequestForVoteResponseBody.builder().success(true).term(NodeData.currentTerm.get()).build();
        }
        lock.unlock();
        return RequestForVoteResponseBody.builder().success(false).term(NodeData.currentTerm.get()).build();
    }

    public void requestToOtherNodes() {
        Message message = new Message(Command.REQUEST_VOTE);
        message.getHeader().setMachineName(NodeData.MACHINE_ID);
        LogUnit<String> lastLog;
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

    /**
     * Send message to all Followers that request is the leader after vote.
     */
    public void sendToFollower() {
        Message message = new Message(Command.REQUEST_SYNC_LOG);
        message.getHeader().setMachineName(NodeData.MACHINE_ID);
        SynchronizeLogRequestBody requestBody = new SynchronizeLogRequestBody();
        requestBody.setLogs(new LogUnit[0]);
        requestBody.setTerm(NodeData.currentTerm.get());
        requestBody.setLeaderCommit(NodeData.commitIndex);
        requestBody.setPrevLogTerm(NodeData.lastApplied.getTerm());
        requestBody.setPrevLogIndex(NodeData.lastApplied.getIndex());
        message.setBody(requestBody.parseToBytes());
        LocalCache.CONTEXT_HOLDER.values().forEach(item -> item.channel().writeAndFlush(message));
    }

    public SynchronizeLogResponseBody synchronizeLogByOtherNodes(String requestMachineId, SynchronizeLogRequestBody
            requestBody) {
        if (requestBody.getTerm() < NodeData.currentTerm.get()) {
            return SynchronizeLogResponseBody.builder().success(false).term(NodeData.currentTerm.get()).build();
        }
        if (requestBody.getLogs().length == 0) {
            NodeData.status = NodeStatus.FOLLOWER;
            NodeData.votedFor = requestMachineId;
            log.info("Machine <{}> follow machine <{}>.", NodeData.MACHINE_ID, requestMachineId);
            return SynchronizeLogResponseBody.builder().success(true).term(NodeData.currentTerm.get()).build();
        }
        if (requestBody.getPrevLogTerm() != NodeData.lastApplied.getTerm()) {
            return SynchronizeLogResponseBody.builder().success(false).term(NodeData.currentTerm.get()).build();
        }
        //TODO: Synchronize the log.
        return SynchronizeLogResponseBody.builder().success(true).term(NodeData.currentTerm.get()).build();
    }


}
