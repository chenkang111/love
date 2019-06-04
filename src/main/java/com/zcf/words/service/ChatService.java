package com.zcf.words.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcf.words.common.json.Body;
import com.zcf.words.common.json.LayUiResult;
import com.zcf.words.entity.Chat;
import com.zcf.words.entity.User;
import com.zcf.words.mapper.ChatMapper;
import com.zcf.words.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatMapper chatmapper;
    @Autowired
    private UserMapper usermapper;

    public Body getChat() {
        List<Chat> list = chatmapper.selectAll();
        for (Chat  chat:list) {
            User user  = usermapper.selectByPrimaryKey(chat.getChat_user_id());
            chat.setUser(user);
        }
        return Body.newInstance(list);
    }

    public Body insertChat(String userId,String content) {
        Chat chat = new Chat();
        chat.setChat_user_id(Integer.parseInt(userId));
        chat.setChat_content(content);
        chatmapper.insertSelective(chat);//插入聊天记录
        return Body.newInstance(chat);
    }


    /***************************************后台接口****************************************/



    public boolean add(Chat chat) {
        return this.chatmapper.insertSelective(chat) == 1;
    }

    public boolean delete(Integer id) {
        return this.chatmapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean update(Chat chat) {
        return this.chatmapper.updateByPrimaryKeySelective(chat) == 1;
    }

    public LayUiResult query(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Example e= new Example(Chat.class);
        e.orderBy("create_time").desc();
        List<Chat> list = this.chatmapper.selectByExample(e);
        for (Chat  chat:list) {
            if(chat.getChat_content()!=null && chat.getChat_content().indexOf("chatpic")>=0){
                //存在
                chat.setChat_pic(chat.getChat_content().substring(7));
                chat.setChat_content("");
            }
            if(chat.getChat_pic()==null){
                chat.setChat_pic("img/moren.jpg");
            }
            User user  = usermapper.selectByPrimaryKey(chat.getChat_user_id());
            chat.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }



    public LayUiResult search(Integer page, Integer limit,String keywords) {
        Example example = new Example(Chat.class);
        keywords = "%" + keywords + "%";
        example.createCriteria().andLike("chat_content", keywords);//name为你想要搜索的字段
        PageHelper.startPage(page, limit);
        List<Chat> list = this.chatmapper.selectByExample(example);
        for (Chat  chat:list) {
            User user  = usermapper.selectByPrimaryKey(chat.getChat_user_id());
            chat.setUser(user);
        }
        return new LayUiResult("0", "查询成功", new PageInfo<>(list).getTotal(), list);
    }
}
