package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
@Table(name = "bill")
public class Bill {

    @Id
    private Integer bill_id;//账单ID
    private Integer bill_user_id;//	账单用户ID
    private String bill_name;//账单名称
    private String bill_price;//账单金额
    private String bill_service;//账单服务
    private String bill_paytype;//支付类型
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

    private String bill_status;
    @Transient
    private User user;
}
