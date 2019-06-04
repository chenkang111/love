package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "toptitle")
public class Toptitle {

    @Id
    private Integer toptitle_id;//主键
    private Integer toptitle_special_id;//专题标题所属精彩专题
    private String toptitle_title;//专题开场标题
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Special special;
}
