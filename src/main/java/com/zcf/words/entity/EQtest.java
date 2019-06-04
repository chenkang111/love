package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "eqtest")
public class EQtest {

    @Id
    private Integer eqtest_id;//主键
    private String eqtest_title;//情商测试标题
    private String eqtest_content;//情商测试内容
    private String eqtest_pic;//情商测试图片
    private String eqtest_vip;//情商测试VIP级别
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
