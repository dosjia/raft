package cn.martinzhao.raft.global;

import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.bean.NodeStatus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class NodeData {
    public static String MACHINE_ID;
    public static NodeStatus status = NodeStatus.CANDIDATE;
    public static List<LogUnit<String>> logs;
    int lastApplied;
    int commitIndex;
    public volatile static ConcurrentHashMap<String, Boolean> voteResult = new ConcurrentHashMap<>();
    public static AtomicInteger currentTerm = new AtomicInteger(0);
    public volatile static String votedFor;
    int[] nextStartIndex;
    int[] nextEndIndex;

}
