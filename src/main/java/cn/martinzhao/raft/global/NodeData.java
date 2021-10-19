package cn.martinzhao.raft.global;

import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.bean.NodeStatus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class NodeData {
    public static String machineId;
    public volatile static NodeStatus status = NodeStatus.CANDIDATE;
    public static List<LogUnit<Integer>> logs;
    int lastApplied;
    int commitIndex;
    public static AtomicInteger currentTerm;
    public volatile static String votedFor;
    int[] nextStartIndex;
    int[] nextEndIndex;

}
