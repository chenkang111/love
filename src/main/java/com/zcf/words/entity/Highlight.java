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
@Table(name = "highlight")
public class Highlight {

    @Id
    private Integer highlight_id;//主键
    private Integer highlight_toptag_id;//专题开场白所属专题标签
    private String highlight_content;//专题开场白
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Toptag toptag;
}
