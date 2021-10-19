package cn.martinzhao.raft.bean;

import lombok.Builder;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
@Builder
public class VoteResult {
    int term;
    boolean result;
}
