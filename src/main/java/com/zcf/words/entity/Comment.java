package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "comment")
public class Comment {

    @Id
    private Integer comment_id;//主键
    private Integer comment_discussion_id;//讨论问题Id
    private Integer comment_user_id;//评论用户
    private String comment_content;//评论内容
    private String comment_pic;//图片
    private String comment_anonymity;//讨论是否匿名 0不匿名 1匿名
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Discussion discussion;
    @Transient
    private User user;
}
