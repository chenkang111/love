package com.zcf.words.qiqiim.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyConfig {

    // 存储所有连接的 channel
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // host name 和监听的端口号，需要配置到配置文件中
//    public static String WS_HOST = "172.16.1.233";
//    public static Integer WS_PORT = 9090;

    public static String WS_HOST = "47.97.163.4";
    public static Integer WS_PORT = 6044;
}
