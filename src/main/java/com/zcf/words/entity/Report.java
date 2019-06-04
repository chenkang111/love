package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "report")
public class Report {

    @Id
    private Integer report_id;//主键
    private Integer report_discussion_id;//举报的ID
    private Integer report_user_id;//举报用户ID
    private String report_content;//举报内容
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private User user;
    private Integer type;

    private String title; //显示内容  文章显示标题，如果是问题显示答案

}
