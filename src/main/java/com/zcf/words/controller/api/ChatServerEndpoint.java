package com.zcf.words.controller.api;

import com.zcf.words.entity.Chat;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.ChatMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 聊天室
 *
 * @author Levin
 * @since 2018/6/26 0026
 */
@RestController
@ServerEndpoint(value = "/chatroom/{userid}")
public class ChatServerEndpoint {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, ChatServerEndpoint> clients = new ConcurrentHashMap<String, ChatServerEndpoint>();

    //此处是解决无法注入的关键
    private static ApplicationContext applicationContext;
    //你要注入的service或者dao
    private UserMapper userMapper;
    private ChatMapper chatMapper;
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String userid;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userid") String userid, Session session) {
        onlineNumber++;
        logger.info("现在来连接的客户id：" + session.getId() + "用户名：" + userid);
        this.userid = userid;
        this.session = session;
        logger.info("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            userMapper = applicationContext.getBean(UserMapper.class);
            User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
            String message = "欢迎用户[" + user.getUser_nickname() + "] 来到聊天室！";
            sendMessageAll(message, userid);
            //把自己的信息加入到map当中去
            clients.put(userid, this);
        } catch (IOException e) {
            logger.info(userid + "上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(userid);
        try {
            userMapper = applicationContext.getBean(UserMapper.class);
            User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
            //并且通知其他人当前用户已经离开聊天室了
            sendMessageAll("用户[" + user.getUser_nickname() + "] 已经离开聊天室了！", userid);
        } catch (IOException e) {
            logger.info(userid + "下线的时候通知所有人发生了错误");
        }
        logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            logger.info("来自客户端消息：" + message + "客户端的id是：" + session.getId());
            chatMapper = applicationContext.getBean(ChatMapper.class);
            userMapper = applicationContext.getBean(UserMapper.class);
            Chat chat = new Chat();
            chat.setChat_user_id(Integer.parseInt(userid));
            //JSONObject.
            StringBuffer sbf = new StringBuffer(message);
            String str = "chat_pic:";
            String pic = "";
            int count = sbf.indexOf(str);
            if (count >= 0) {
                pic = sbf.substring(str.length(), sbf.length());
                chat.setChat_pic(pic);
                User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
                chatMapper.insertSelective(chat);//插入聊天记录
                sendMessageAll("用户[" + user.getUser_nickname() + "] : " + pic,userid);
            } else {
                chat.setChat_content(message);
                User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
                chatMapper.insertSelective(chat);//插入聊天记录
                sendMessageAll("用户[" + user.getUser_nickname() + "] : " + message,userid);
            }
        } catch (Exception e) {
            logger.info("发生了错误了");
        }
    }


    public void sendMessageTo(String message, String ToUserName) throws IOException {
        for (ChatServerEndpoint item : clients.values()) {
            if (item.userid.equals(ToUserName)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    public void sendMessageAll(String message, String FromUserName) throws IOException {
        for (ChatServerEndpoint item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }


}
