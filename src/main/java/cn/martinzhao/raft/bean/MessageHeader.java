package cn.martinzhao.raft.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Setter
@Getter
public class MessageHeader {
    private Command commandId;
    private String machineName;
    private int messageLength;
}
