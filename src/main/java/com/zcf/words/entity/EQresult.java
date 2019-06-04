package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "eqresult")
public class EQresult {

    @Id
    private Integer eqresult_id;//主键
    private Integer eqresult_test_id;//情商测试id
    private String eqresult_title;//情商答案标题
    private String eqresult_content;//情商答案内容
    private String eqresult_point_start;//最低分数
    private String eqresult_point_end;//最高分数
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    @Transient
    private EQtest eqtest;
}
