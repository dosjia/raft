package cn.martinzhao.raft.util;

import cn.martinzhao.raft.bean.MessageHeader;
import io.netty.util.AttributeKey;

public class Constants {
    public static byte IDENTIFY_BYTE = 0x7e;
    public static byte IDENTIFY_BYTE_SECOND = 0x7d;
    public static byte[] CONNECTION_SETUP = {0x00, 0x01};
    public static byte[] CONNECTION_SETUP_ANSWER = {(byte) 0x80, 0x01};
    public static byte[] REQUEST_VOTE = {0x00, 0x02};
    public static byte[] REQUEST_VOTE_ANSWER = {(byte) 0x80, 0x02};

    public static final String MESSAGE_HEADER_KEY = "MessageHeader";
    public static final AttributeKey<MessageHeader> MESSAGE_HEADER_ATTRIBUTE = AttributeKey
            .valueOf(Constants.MESSAGE_HEADER_KEY);

}
