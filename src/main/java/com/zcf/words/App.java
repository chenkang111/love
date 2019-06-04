package com.zcf.words;

import com.zcf.words.controller.api.ChatRoomServerEndpoint;
import com.zcf.words.qiqiim.netty.ServerBootStrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan("com.zcf.words.mapper")
@EnableScheduling
@SpringBootApplication
public class App {//implements CommandLineRunner

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    private ServerBootStrap ws;

    public static void main(String[] args) throws Exception {
        // SpringApplication 将引导我们的应用，启动 Spring，相应地启动被自动配置的 Tomcat web 服务器。
        // 我们需要将 App.class 作为参数传递给 run 方法，以此告诉 SpringApplication 谁是主要的 Spring 组件，并传递 args 数组以暴露所有的命令行参数。
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(App.class, args);
        //解决WebSocket不能注入的问题
        ChatRoomServerEndpoint.setApplicationContext(configurableApplicationContext);
//        getPageInfo.setApplicationContext(configurableApplicationContext);
//        getPageInfo page = new getPageInfo();
//        for (Integer i=7;i<54;i++){
//            page.getPage(i.toString());
//        }
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    // 注意这里的 run 方法是重载自 CommandLineRunner
//    @Override
//    public void run(String... args) throws Exception {
//        logger.info("Netty's ws server is listen: " + NettyConfig.WS_PORT);
//        InetSocketAddress address = new InetSocketAddress(NettyConfig.WS_HOST, NettyConfig.WS_PORT);
//        ChannelFuture future = ws.start(address);
//
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//                ws.destroy();
//            }
//        });
//
//        future.channel().closeFuture().syncUninterruptibly();
//    }

//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //你的项目地址
//        factory.setLocation("C:/words");
//        return factory.createMultipartConfig();
//    }

}
