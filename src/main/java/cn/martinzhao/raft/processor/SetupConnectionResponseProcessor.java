package cn.martinzhao.raft.processor;

import cn.martinzhao.raft.LocalCache;
import cn.martinzhao.raft.exception.ApplicationBaseException;
import cn.martinzhao.raft.exception.ExceptionEnum;
import cn.martinzhao.raft.util.ByteUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Slf4j
public class SetupConnectionResponseProcessor implements IProcessor {
    public void channelRead(ChannelHandlerContext ctx, byte[] msg) {
        short length = ByteUtil.bytesToShort(Arrays.copyOfRange(msg, 0, 2));
        String name = new String(Arrays.copyOfRange(msg, 2, 2 + length), StandardCharsets.UTF_8);
        log.info("Setup connection with machine:<{}> successfully.", name);
        LocalCache.CONTEXT_HOLDER.put(name, ctx);

    }
}
