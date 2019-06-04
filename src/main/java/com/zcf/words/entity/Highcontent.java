package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Table(name = "highcontent")
public class Highcontent {

    @Id
    private Integer highcontent_id;//主键
    private Integer highcontent_highlight_id;//专题话术所属专题开场白
    private String highcontent_content;//专题话术
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Highlight highlight;
}
