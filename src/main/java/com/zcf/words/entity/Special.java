package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "special")
public class Special {

    @Id
    private Integer special_id;//主键
    private String special_name;//专题名称
    private String special_pic;//专题图片
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
