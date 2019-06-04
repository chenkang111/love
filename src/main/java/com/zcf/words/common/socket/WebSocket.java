package com.zcf.words.common.socket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocket {
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);// 加入Set中
        addOnlineCount();// 在线人数加1
        System.out.println("有新链接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 链接关闭时调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);// 从set中删除
        subOnlineCount();// 在线人数减一
        System.out.println("有新链接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        for (WebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 错误时调用该方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("系统内部出错");
        error.printStackTrace();

    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);

    }

    private static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }
}
