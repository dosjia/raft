package cn.martinzhao.raft.bean;

import cn.martinzhao.raft.bean.so.ISocketInput;
import cn.martinzhao.raft.util.ByteUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
@Builder
@Getter
public class VoteResult implements ISocketInput {
    int term;
    boolean success;

    @Override
    public void parseFromBytes(byte[] bytes) {
        this.term = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 0, 4));
        this.success = (bytes[4] == (short) 1);
    }
}
