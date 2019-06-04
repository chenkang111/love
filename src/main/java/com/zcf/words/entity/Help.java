package com.zcf.words.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Table(name = "help")
public class Help {

    @Id
    private Integer help_id;//	主键
    private String help_content;//帮助内容
    private String help_img; //上传图片
}
