package cn.martinzhao.raft.bean.so;

import cn.martinzhao.raft.util.ByteUtil;
import lombok.Builder;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/19
 */
@Builder
public class RequestForVoteRequestBody implements ISocketOutput {
    private int term;
    private int termOfLastLog;
    private int indexOfLastLog;

    @Override
    public byte[] parseToBytes() {
        byte[] bytes = new byte[12];
        byte[] temp = ByteUtil.intToByteArray(term);
        bytes[0] = temp[0];
        bytes[1] = temp[1];
        bytes[2] = temp[2];
        bytes[3] = temp[3];
        temp = ByteUtil.intToByteArray(termOfLastLog);
        bytes[4] = temp[0];
        bytes[5] = temp[1];
        bytes[6] = temp[2];
        bytes[7] = temp[3];
        temp = ByteUtil.intToByteArray(indexOfLastLog);
        bytes[8] = temp[0];
        bytes[9] = temp[1];
        bytes[10] = temp[2];
        bytes[11] = temp[3];
        return bytes;
    }
}
