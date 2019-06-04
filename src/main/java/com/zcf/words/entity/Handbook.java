package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "handbook")
public class Handbook {

    @Id
    private Integer handbook_id;//主键
    private String  handbook_title;//恋爱指南标题
    private String handbook_pic;//恋爱指南图片
    private String handbook_content;//恋爱指南内容
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
