package com.zcf.words.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Table(name = "user")
@Data
public class User {

    @Id
    private Integer user_id;//主键ID
    private String user_nickname;//昵称
    private String user_phone;//手机号
    private String user_password;//加密密码
    private String user_realpassword;//真实密码
    private String user_headpic;//头像图片
    private String user_vip;//用户会员 0免费会员 1低级会员 2中级会员 3高级会员
    private String user_vip_time;//用户会员所剩时间(天)
    private String user_wx_id;//第三方登录微信ID
    private String user_qq_id;//第三方登录QQID
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
