package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "account")
public class Account {

    @Id
    private Integer account_id;//主键
    private String access_token;//token
    private String account_name;//管理员昵称
    private String account_passwrod;//管理员密码
    private Date create_time;//创建时间


}
