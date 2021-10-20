package cn.martinzhao.raft.bean.so;

import cn.martinzhao.raft.bean.LogUnit;
import cn.martinzhao.raft.util.ByteUtil;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/20
 */
@Setter
@Getter
public class SynchronizeLogRequestBody implements ISocketInput, ISocketOutput {
    private int term;
    private int prevLogTerm;
    private int prevLogIndex;
    private LogUnit<String>[] logs;
    private int leaderCommit;

    @Override
    public void parseFromBytes(byte[] bytes) {
        term = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 0, 4));
        prevLogTerm = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 4, 8));
        prevLogIndex = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 8, 12));
        leaderCommit = ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 12, 16));
        logs = new LogUnit[ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, 16, 20))];
        int index = 20;
        for (int i = 0; i < this.logs.length; i++) {
            LogUnit<String> temp = new LogUnit();
            temp.setTerm(ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, index, index + 4)));
            temp.setIndex(ByteUtil.byteArrayToInt(Arrays.copyOfRange(bytes, index + 4, index + 8)));
            int length = ByteUtil.bytesToShort(Arrays.copyOfRange(bytes, index + 8, index + 10));
            temp.setContent(new String(Arrays.copyOfRange(bytes, index + 10, index + 10 + length)));
            logs[i] = temp;
            index = index + 10 + length;
        }
    }

    @Override
    public byte[] parseToBytes() {
        byte[] result;
        result = ByteUtil.concatBytes(ByteUtil.intToByteArray(term), ByteUtil.intToByteArray(prevLogTerm),
                ByteUtil.intToByteArray(prevLogIndex), ByteUtil.intToByteArray(leaderCommit), ByteUtil.intToByteArray(logs.length));
        for (int i = 0; i < logs.length; i++) {
            result = ByteUtil.concatBytes(result, ByteUtil.intToByteArray(logs[i].getTerm()), ByteUtil.intToByteArray(logs[i].getIndex()),
                    ByteUtil.intToByteArray(logs[i].getContent().getBytes(StandardCharsets.UTF_8).length),
                    logs[i].getContent().getBytes(StandardCharsets.UTF_8));
        }
        return result;
    }
}
