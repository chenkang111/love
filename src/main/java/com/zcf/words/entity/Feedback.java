package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "feedback")
public class Feedback {

    @Id
    private Integer feedback_id;//主键
    private String feedback_phone;//意见反馈联系方式
    private String feedback_content;//意见反馈内容
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除
}
