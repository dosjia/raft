package cn.martinzhao.raft;

import cn.martinzhao.raft.channel.ClientChildChannelInitializer;
import cn.martinzhao.raft.channel.ServerChildChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/14
 */
@Slf4j
public class NodeStarter {

    private static final String NODE_LIST = "node.list";

    public static void main(String[] args) {
        Map<String, String> argsMap = getArguments(args);
        initNodeListener(argsMap);
        initConnectorToPeerNode(argsMap);
    }

    private static void initConnectorToPeerNode(Map<String, String> argsMap) {
        PeerNodesInfo.peerNodes = initListenerForOtherNodes(argsMap.get(NODE_LIST));
        for (PeerNode node : PeerNodesInfo.peerNodes) {
            ThreadPool.scheduledThreadPool.schedule(() -> {
                EventLoopGroup group = new NioEventLoopGroup();
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ClientChildChannelInitializer());
                    ChannelFuture f = b.connect(node.getAddress(), node.getPort()).sync();
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            }, 300, TimeUnit.MILLISECONDS);

        }
    }

    private static void initNodeListener(Map<String, String> argsMap) {
        EventLoopGroup bossGroup = NodeStarter.getBossGroup();
        ServerBootstrap bootStrap = NodeStarter.getBootStrap();
        EventLoopGroup workerGroup = NodeStarter.getWorkerGroup();
        int port = Integer.parseInt(argsMap.get("port"));
        new Thread(() -> {
            try {
                bootStrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ServerChildChannelInitializer());
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

    private static List<PeerNode> initListenerForOtherNodes(String nodeList) {
        List<PeerNode> nodes = new ArrayList<>();
        for (String temp : nodeList.split(";")) {
            String[] node = temp.split(":");
            PeerNode peerNode = new PeerNode();
            peerNode.setAddress(node[0]);
            peerNode.setPort(Integer.parseInt(node[1]));
            nodes.add(peerNode);
        }
        return nodes;

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
        String path = argsMap.getOrDefault("config", "");
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(path + "\\conf.properties")) {
            properties.load(is);
            argsMap.put(NODE_LIST, properties.getProperty(NODE_LIST));
            argsMap.put("machineId", properties.getProperty("machineId"));

        } catch (IOException e) {
            log.error("Read properties file error. Some properties may not set as you wished. Please config the file path correctly.");
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
