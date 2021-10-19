package cn.martinzhao.raft.bean;


/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public class Message {
    private MessageHeader header;
    private byte[] body;

    public byte[] getBody() {
        return body;
    }

    public Message() {
        super();
    }

    public Message(Command command) {
        this.header = new MessageHeader(command);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

}
