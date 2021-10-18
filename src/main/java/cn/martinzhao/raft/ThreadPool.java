package cn.martinzhao.raft;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public class ThreadPool {
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
}
