package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "vip")
public class Vip {

    @Id
    private Integer vip_id;//主键
    private String vip_name;//vip名称
    private String vip_content;//vip内容
    private String vip_price;//vip价格
    private String vip_day;//vip时长
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
