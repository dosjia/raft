package cn.martinzhao.raft.util;

public class Constants {
    public static byte IDENTIFY_BYTE = 0x7e;
    public static byte IDENTIFY_BYTE_SECOND = 0x7d;
    public static byte[] CONNECTION_SETUP = {0x00, 0x01};
    public static byte[] CONNECTION_SETUP_ANSWER = {(byte) 0x80, 0x01};

}
