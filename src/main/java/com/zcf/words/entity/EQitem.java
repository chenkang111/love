package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "eqitem")
public class EQitem {
    @Id
    private Integer eqitem_id;//主键
    private Integer eqitem_test_id;//测试ID
    private Integer eqitem_content_id;//题目内容ID
    private String eqitem_content;//选项内容
    private String eqitem_point;//选项分数
    private String eqitem_select;//选项目标
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private EQtest eqtest;

    @Transient
    private EQcontent eqcontent;
}
