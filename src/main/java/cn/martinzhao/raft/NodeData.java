package cn.martinzhao.raft;

import java.util.List;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class NodeData {
    public static String machineId;
    public static List<LogUnit<Integer>> logs;
    int lastApplied;
    int commitIndex;
    public static int currentTerm;
    public static String votedFor;
    int[] nextStartIndex;
    int[] nextEndIndex;

}
