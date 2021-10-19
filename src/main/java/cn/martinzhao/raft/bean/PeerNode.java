package cn.martinzhao.raft.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
@Setter
@Getter
public class PeerNode {
    private String address;
    private int port;
}
