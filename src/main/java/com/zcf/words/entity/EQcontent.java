package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "eqcontent")
public class EQcontent {

    @Id
    private Integer eqcontent_id;//主键
    private Integer eqcontent_eqtest_id;//所属测试
    private String eqcontent_tag;//测试内容标签
    private String eqcontent_content;//测试内容
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private EQtest eqtest;
}
