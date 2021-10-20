package cn.martinzhao.raft.bean;

import cn.martinzhao.raft.util.Constants;

import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
public enum Command {
    CONNECTION_SETUP(Constants.CONNECTION_SETUP), CONNECTION_SETUP_ANSWER(Constants.CONNECTION_SETUP_ANSWER),
    REQUEST_VOTE(Constants.REQUEST_VOTE), REQUEST_VOTE_ANSWER(Constants.REQUEST_VOTE_ANSWER),
    REQUEST_SYNC_LOG(Constants.REQUEST_SYNC_LOG), REQUEST_SYNC_LOG_ANSWER(Constants.REQUEST_SYNC_LOG_ANSWER);
    private byte[] commandCode;

    public byte[] value() {
        return commandCode;
    }

    Command(byte[] value) {
        this.commandCode = value;
    }

    public static Command getCommand(byte[] value) {
        Command[] list = Command.values();
        for (Command command : list) if (Arrays.equals(command.value(), value)) return command;
        return null;
    }
}
