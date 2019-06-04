package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "chat")
public class Chat {

    @Id
    private Integer chat_id;//主键
    private Integer chat_user_id;//聊天记录用户ID
    private String chat_content;//聊天记录内容
    private String chat_pic;//发送图片
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private User user;
}
