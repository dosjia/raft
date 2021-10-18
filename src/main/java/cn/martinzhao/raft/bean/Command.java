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
    ;
    private byte[] command;

    public byte[] value() {
        return command;
    }

    Command(byte[] value) {
        this.command = value;
    }

    public static Command getCommand(byte[] value) {
        Command[] list = Command.values();
        for (Command command : list) if (Arrays.equals(command.value(), value)) return command;
        return null;
    }
}
