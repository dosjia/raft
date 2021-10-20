package cn.martinzhao.raft.bean.so;

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
public class VoteResult implements ISocketInput, ISocketOutput {
    int term;
    boolean success;

    @Override
    public void parseFromBytes(byte[] bytes) {
        this.term = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 0, 4));
        this.success = (bytes[4] == (short) 1);
    }

    @Override
    public byte[] parseToBytes() {
        byte[] bytes = new byte[5];
        byte[] temp = ByteUtil.intToByteArray(term);
        bytes[0] = temp[0];
        bytes[1] = temp[1];
        bytes[2] = temp[2];
        bytes[3] = temp[3];
        bytes[4] = success ? 0x0 : (byte) 0x01;
        return bytes;
    }
}
