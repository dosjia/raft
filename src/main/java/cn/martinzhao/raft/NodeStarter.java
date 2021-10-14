package cn.martinzhao.raft;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/14
 */
@Slf4j
public class NodeStarter {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = NodeStarter.getBossGroup();
        ServerBootstrap bootStrap = NodeStarter.getBootStrap();
        EventLoopGroup workerGroup = NodeStarter.getWorkerGroup();
        Map<String, String> argsMap = getArguments(args);
        int port = Integer.parseInt(argsMap.get("port"));
        new Thread(() -> {
            try {
                bootStrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelInitializer());
                ChannelFuture f = bootStrap.bind(port).sync();
                log.info("Netty Server started at port " + port);
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    private static Map<String, String> getArguments(String[] args) {
        int length = args.length;
        if (length % 2 != 0) {
            log.error("Parameter is invalid, please double check.");
        }
        Map<String, String> argsMap = new HashMap<>(length / 2);
        for (int i = 0; i < length / 2; i++) {
            if (!args[i * 2].startsWith("-")) {
                log.error("Parameter should start with '-'.");
            }
            String key = args[i * 2].trim().substring(1);
            String value = args[i * 2 + 1].trim();
            argsMap.put(key.toLowerCase(), value);
        }
        return argsMap;
    }

    private static NioEventLoopGroup getWorkerGroup() {
        return new NioEventLoopGroup();
    }

    private static NioEventLoopGroup getBossGroup() {
        return new NioEventLoopGroup();
    }

    private static ServerBootstrap getBootStrap() {
        return new ServerBootstrap();
    }


}
