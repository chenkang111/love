package com.zcf.words.controller.api;

import com.zcf.words.entity.Chat;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.ChatMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.zcf.words.common.socket.WebSocketUtils.*;

/**
 * 聊天室
 *
 * @author Levin
 * @since 2018/6/26 0026
 */
@RestController
@ServerEndpoint(value = "/chat-room/{userid}")
public class ChatRoomServerEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomServerEndpoint.class);


    //此处是解决无法注入的关键
    private static ApplicationContext applicationContext;
    //你要注入的service或者dao
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChatMapper chatMapper;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ChatRoomServerEndpoint.applicationContext = applicationContext;
    }


    @OnOpen
    public void openSession(@PathParam("userid") String userid, Session session) {
        try {
            LIVING_SESSIONS_CACHE.put(userid, session);
        } catch (Exception e) {
            log.error("IO异常-->openSession:{}", e);
        }
    }


    @OnMessage
    public void onMessage(@PathParam("userid") String userid, String message) {
        log.info(message);
        chatMapper = applicationContext.getBean(ChatMapper.class);
        userMapper = applicationContext.getBean(UserMapper.class);
        Chat chat = new Chat();
        chat.setChat_user_id(Integer.parseInt(userid));
        //JSONObject.
        StringBuffer sbf = new StringBuffer(message);
        String str = "chat_pic:";
        String str2 = "close:true";
        String pic = "";
        int count = sbf.indexOf(str);
        int ct = sbf.indexOf(str2);
        if(ct>=0){
            onClose(userid,LIVING_SESSIONS_CACHE.get(userid));
        }else{
            if (count >= 0) {
                pic = sbf.substring(str.length(), sbf.length());
                chat.setChat_pic(pic);
                User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
                chatMapper.insertSelective(chat);//插入聊天记录
                sendMessageAll("用户[" + user.getUser_nickname() + "] : " + pic);
            } else {
                chat.setChat_content(message);
                User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
                chatMapper.insertSelective(chat);//插入聊天记录
                sendMessageAll("用户[" + user.getUser_nickname() + "] : " + message);
            }
        }
    }


    @OnClose
    public void onClose(@PathParam("userid") String userid, Session session) {
        //当前的Session 移除
        userMapper = applicationContext.getBean(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
        //并且通知其他人当前用户已经离开聊天室了
       // sendMessageAll("用户[" + user.getUser_nickname() + "] 已经离开聊天室了！");
        try {
            LIVING_SESSIONS_CACHE.remove(userid);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IO异常-->onClose2:{}", e);
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IO异常-->onError:{}", e);
        }
        throwable.printStackTrace();
    }


    @GetMapping("/chat-room/{sender}/to/{receive}")
    public void onMessage(@PathVariable("sender") String sender, @PathVariable("receive") String receive, String message) {
        chatMapper = applicationContext.getBean(ChatMapper.class);
        userMapper = applicationContext.getBean(UserMapper.class);
        User senderuser = userMapper.selectByPrimaryKey(Integer.parseInt(sender));//发送人
        User receiveuser = userMapper.selectByPrimaryKey(Integer.parseInt(receive));//接收人
        sendMessage(LIVING_SESSIONS_CACHE.get(receive), "[" + senderuser.getUser_nickname() + "]"
                + "-> [" + receiveuser.getUser_nickname() + "] : " + message);
    }

//    public static void main(String[] args){
//        String message = "chat_pic:/upload/userfile";
//        StringBuffer sbf = new StringBuffer(message);
//        String str = "chat_pic:";
//        int count = sbf.indexOf(str);
//        if (count>=0){
//            String pic = sbf.substring(str.length(),sbf.length());
//            System.out.print(pic);
//        }else
//        {
//            System.out.print("没有");
//        }
//    }
}
