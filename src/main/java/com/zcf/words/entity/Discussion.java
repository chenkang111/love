package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "discussion")
public class Discussion {

    @Id
    private Integer discussion_id;//主键
    private Integer discussion_user_id;//发布讨论的用户ID
    private String discussion_title;//讨论标题
    private String discussion_content;//讨论内容
    private String discussion_pic;//图片
    private String discussion_anonymity;//讨论是否匿名 0不匿名 1匿名
    private String discussion_splendid;//讨论精彩 0一般 1精彩
    private String discussion_count;//讨论精彩数量
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private User user;
}
