package cn.martinzhao.raft;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/15
 */
public class LocalCache {
    /**
     * String key is used for machine name, and value is the object of channel context, which is used for communication
     * to other peer.
     */
    public static Map<String, ChannelHandlerContext> CONTEXT_HOLDER = new HashMap<>();
}
