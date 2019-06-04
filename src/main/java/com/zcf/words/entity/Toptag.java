package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "toptag")
public class Toptag {

    @Id
    private Integer toptag_id;//主键
    private Integer toptag_toptitle_id;//专题标签所属专题标题
    private String toptag_name;//专题标签
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Toptitle toptitle;
}
