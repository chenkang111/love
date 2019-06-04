package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "code")
public class Code {

    @Id
    private Integer cid;//主键
    private String code;//验证码
    private String phone;//手机号
    private Date create_time;//创建时间

}
