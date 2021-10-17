package cn.martinzhao.raft;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class NodeStateMachine implements Runnable {
    private NodeStatus status = NodeStatus.FOLLOWER;
    private VoteService service = new VoteService();

    @Override
    public void run() {
        LockSupport.parkNanos(1000);
        switch (status) {
            case FOLLOWER:
                //check timeout? if timeout then change status to candidate.
                break;
            case LEADER:
                //send heartbeat
                break;
            case CANDIDATE:
                //Get node list
                //raise request to other nodes with foreach
                service.requestToOtherNodes(NodeData.machineId);
                break;
            default:

        }

    }
}
