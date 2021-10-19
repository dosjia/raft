package cn.martinzhao.raft.bean.so;


public interface ISocketInput {
    void parseFromBytes(byte[] bytes);
}
