package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "correlation")
public class Correlation {

    @Id
    private Integer correlation_id;//主键
    private String correlation_pic;//关于APP图片
    private String correlation_appname;//关于APP名称
    private String correlation_version;//关于APP版本号
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
