package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "atlas")
public class Atlas {

    @Id
    private Integer atlas_id;//主键

    private Integer atlas_panels_id;//所属展示板
    private String atlas_title;//图集标题
    private String atlas_pic;//图集图片
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private Panels panels;
}
